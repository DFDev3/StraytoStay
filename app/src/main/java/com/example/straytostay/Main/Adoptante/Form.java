package com.example.straytostay.Main.Adoptante;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.straytostay.Classes.Answer;
import com.example.straytostay.Classes.Question;
import com.example.straytostay.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Form extends AppCompatActivity {

    private final List<Question> questionList = new ArrayList<>();
    private final Map<String, Question> questionMap = new HashMap<>();
    private final Map<String, String> openResponses = new HashMap<>();
    private final Map<String, Integer> categoryScores = new HashMap<>();
    private FirebaseFirestore db;
    private TextView questionText;
    private RadioGroup radioGroup;
    private RadioButton radio1, radio2, radio3, radio4;
    private Button nextButton;
    private EditText openAnswer;
    private int selectedIndex;
    private String currentQuestionId;
    private List<Answer> currentAnswers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.adop_activity_form);

        db = FirebaseFirestore.getInstance();

        questionText = findViewById(R.id.textQuestion);
        openAnswer = findViewById(R.id.openAnswer);
        radioGroup = findViewById(R.id.radioGroup);
        radio1 = findViewById(R.id.option1);
        radio2 = findViewById(R.id.option2);
        radio3 = findViewById(R.id.option3);
        radio4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.btnNext);

        fetchQuestions();

        nextButton.setOnClickListener(v -> handleNext());
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
                        Toast.makeText(this, "Error loading questions", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayQuestion(String questionId) {
        Question question = questionMap.get(questionId);
        if (question == null) {
            Toast.makeText(this, "Question not found: " + questionId, Toast.LENGTH_SHORT).show();
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
                openAnswer.setVisibility(View.VISIBLE);
                question.setOpen(true);
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

        boolean isOpenEnded = currentQuestion.isOpen();
        Log.d("BOOOOOOOOOOO", String.valueOf(isOpenEnded));

        if (isOpenEnded) {
            Log.d("ISSSSSSSSSS", "is open ended");
            selectedIndex = -2;
            String response = openAnswer.getText().toString().trim();
            if (response.isEmpty()) {
                Toast.makeText(this, "Please enter your answer", Toast.LENGTH_SHORT).show();
                return;
            }

            openResponses.put(currentQuestionId, response);

            if (nextList == null || nextList.isEmpty() || nextList.get(0) == -1) {
                completeForm();
            } else {
                currentQuestionId = String.format("q%02d", nextList.get(0));
                displayQuestion(currentQuestionId);
            }
            return;
        } else {

            // Only called for multiple-choice questions
            selectedIndex = getSelectedRadioIndex();
            if (selectedIndex == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
                return;
            }

            Answer selectedAnswer = currentAnswers.get(selectedIndex);
            String category = selectedAnswer.getCategory();
            int score = selectedAnswer.getScore();

            int currentScore = categoryScores.getOrDefault(category, 0);
            categoryScores.put(category, currentScore + score);
        }

        int nextNum = nextList.get(selectedIndex);
        if (nextNum == -1) {
            completeForm();
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

    private void completeForm() {
        Log.d("Form", "Completed!");
        Log.d("Scores", categoryScores.toString());
        Log.d("OpenAnswers", openResponses.toString());

        Toast.makeText(this, "Form complete!", Toast.LENGTH_LONG).show();
        finish();
    }
}
