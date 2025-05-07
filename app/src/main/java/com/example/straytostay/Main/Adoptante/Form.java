package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.straytostay.Classes.Answer;
import com.example.straytostay.Classes.Question;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Form extends Fragment {

    private final List<Question> questionList = new ArrayList<>();
    private final Map<String, Question> questionMap = new HashMap<>();
    private final Map<String, String> openResponses = new HashMap<>();
    private final Map<String, Integer> categoryScores = new HashMap<>();
    Map<String, Integer> maxPossibleScores = new HashMap<>();
    private FirebaseFirestore db;
    private TextView questionText;
    private RadioGroup radioGroup;
    private RadioButton radio1, radio2, radio3, radio4;
    private Button nextButton;
    private EditText openAnswer;
    private int selectedIndex;
    private String currentQuestionId;
    private List<Answer> currentAnswers;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.adop_activity_form, container, false);

        db = FirebaseFirestore.getInstance();

        questionText = view.findViewById(R.id.textQuestion);
        openAnswer = view.findViewById(R.id.openAnswer);
        radioGroup = view.findViewById(R.id.radioGroup);
        radio1 = view.findViewById(R.id.option1);
        radio2 = view.findViewById(R.id.option2);
        radio3 = view.findViewById(R.id.option3);
        radio4 = view.findViewById(R.id.option4);
        nextButton = view.findViewById(R.id.btnNext);

        fetchQuestions();

        nextButton.setOnClickListener(v -> handleNext());

        return view;
    }

    private void fetchQuestions() {
        db.collection("questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Question q = doc.toObject(Question.class);
                            questionList.add(q);
                            questionMap.put(doc.getId(), q);
                        }

                        currentQuestionId = "q02";
                        displayQuestion(currentQuestionId);
                    } else {
                        Toast.makeText(requireContext(), "Error loading questions", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayQuestion(String questionId) {
        Question question = questionMap.get(questionId);
        if (question == null) {
            Toast.makeText(requireContext(), "Question not found: " + questionId, Toast.LENGTH_SHORT).show();
            return;
        }

        questionText.setText(question.getText());
        radioGroup.clearCheck();
        openAnswer.setText("");

        currentAnswers = question.getAnswers();

        radio1.setVisibility(View.GONE);
        radio2.setVisibility(View.GONE);
        radio3.setVisibility(View.GONE);
        radio4.setVisibility(View.GONE);
        openAnswer.setVisibility(View.GONE);

        Log.d("isOpen", String.valueOf(question.isOpen()));
        Log.d("answers", String.valueOf(currentAnswers));

        if (currentAnswers != null && currentAnswers.size() == 1) {
            Answer onlyAnswer = currentAnswers.get(0);
            if ("none".equals(onlyAnswer.getCategory()) && "open".equalsIgnoreCase(onlyAnswer.getText())) {
                Log.d("OpenAnswerDebug", "Question is open-ended. Detected via answer content.");
                radio1.setText(currentAnswers.get(0).getText());
                radio1.setVisibility(View.VISIBLE);
                return;
            }
        }


        if (currentAnswers.size() > 0) {
            radio1.setText(currentAnswers.get(0).getText());
            radio1.setVisibility(View.VISIBLE);
        }
        if (currentAnswers.size() > 1) {
            radio2.setText(currentAnswers.get(1).getText());
            radio2.setVisibility(View.VISIBLE);
        }
        if (currentAnswers.size() > 2) {
            radio3.setText(currentAnswers.get(2).getText());
            radio3.setVisibility(View.VISIBLE);
        }
        if (currentAnswers.size() > 3) {
            radio4.setText(currentAnswers.get(3).getText());
            radio4.setVisibility(View.VISIBLE);
        }
    }

    private void handleNext() {
        Question currentQuestion = questionMap.get(currentQuestionId);
        if (currentQuestion == null) return;

        List<Integer> nextList = currentQuestion.getNext();

        // Only called for multiple-choice questions
        selectedIndex = getSelectedRadioIndex();
        if (selectedIndex == -1) {
            Toast.makeText(requireContext(), "Please select an option", Toast.LENGTH_SHORT).show();
            return;
        }

        Answer selectedAnswer = currentAnswers.get(selectedIndex);
        String category = selectedAnswer.getCategory();
        int score = selectedAnswer.getScore();

        // Scores
        int currentScore = categoryScores.getOrDefault(category, 0);
        categoryScores.put(category, currentScore + score);

        // Scores for Normalization
        Map<String, Integer> maxPossibleScores = new HashMap<>();
        maxPossibleScores.put("Available Time", 20);
        maxPossibleScores.put("Environment", 50);
        maxPossibleScores.put("Resources", 40);
        maxPossibleScores.put("LifeStyle", 60);


        int nextNum = nextList.get(selectedIndex);
        if (nextNum == -1) {
            for (Map.Entry<String, Integer> entry : categoryScores.entrySet()) {
                Log.d("Scores", entry.getKey() + ": " + entry.getValue());
            }

            HashMap<String, Float> normalizedScores = new HashMap<>();
            int NormScoresSum = 0;

            for (Map.Entry<String, Integer> entry : categoryScores.entrySet()) {
                String cat = entry.getKey();
                int rawScore = entry.getValue();
                int maxScore = maxPossibleScores.getOrDefault(cat, 1); // prevent division by zero

                float normalized = rawScore / (float) maxScore * 100f;
                NormScoresSum += normalized;

                Log.d("AAAAAAAAAAAAA", "Each: " + normalized + " and Sum is now " + NormScoresSum);


                normalizedScores.put(cat, normalized); // ✅ fixed
                Log.d("Normalized Scores", cat + ": " + normalized);
            }

            float totalNormScore = NormScoresSum / 4;
            normalizedScores.put("Total", totalNormScore);
            Log.d("Total Normalized Score", "Total" + ": " + totalNormScore);

            completeForm(normalizedScores);


        } else {
            currentQuestionId = String.format("q%02d", nextNum);
            displayQuestion(currentQuestionId);
        }
    }


    private int getSelectedRadioIndex() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == radio1.getId()) return 0;
        if (selectedId == radio2.getId()) return 1;
        if (selectedId == radio3.getId()) return 2;
        if (selectedId == radio4.getId()) return 3;
        return -1;
    }

    private void completeForm(HashMap<String, Float> normalizedScores) {
        Log.d("Form", "Completed!");
        Log.d("Scores", categoryScores.toString());

        Toast.makeText(requireContext(), "Form complete!", Toast.LENGTH_LONG).show();

        FormThankYou fragment = FormThankYou.newInstance(normalizedScores);


        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment) // make sure this ID exists in the layout of Form.java
                .addToBackStack(null)
                .commit();
    }
}
