package com.example.straytostay.Main.Adoptante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.straytostay.R;

import java.util.HashMap;
import java.util.Map;

public class Form extends AppCompatActivity {

    private TextView textQuestion;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3, option4;
    private EditText openAnswer;

    private Button btnNext;

    private int currentQuestion = 1;
    private final Map<Integer, Question> questionFlow = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.adop_activity_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });

        textQuestion = findViewById(R.id.textQuestion);
        radioGroup = findViewById(R.id.radioGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        openAnswer = findViewById(R.id.openAnswer);
        btnNext = findViewById(R.id.btnNext);

        setupQuestions();
        showQuestion(currentQuestion);

        btnNext.setOnClickListener(v -> nextStep());
    }

    private void nextStep() {
        Question question = questionFlow.get(currentQuestion);
        int next = -1;

        if (question.isOpenAnswer) {
            if (question.next.size() > 0) {
                next = question.next.get(0);
            }
        } else {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) return;
            if(question.next.size() == 0) return;
            //determinate the number of options
            if (question.next.size() >= 1 && selectedId == R.id.option1) {
                next = question.next.get(0);
            }
            if (question.next.size() >= 2 && selectedId == R.id.option2) {
                next = question.next.get(1);
            }
            if (question.next.size() >= 3 && selectedId == R.id.option3) {
                next = question.next.get(2);
            }
            if (question.next.size() >= 4 && selectedId == R.id.option4) {
                next = question.next.get(3);
            }

        }

        if (next == -1) {
            finishForm();
        } else {
            currentQuestion = next;
            showQuestion(currentQuestion);
        }
    }


    private void setupQuestions() {
        // Formato: (texto, si es abierta, lista de siguientes preguntas)

        questionFlow.put(1, new Question(getString(R.string.p1), true, 2));
        questionFlow.put(2, new Question(getString(R.string.p2), false, 3, 4, 17, 24));
        questionFlow.put(3, new Question(getString(R.string.p3), false, 12, -1));
        questionFlow.put(4, new Question(getString(R.string.p4), false, 10, 5, 14));
        questionFlow.put(5, new Question(getString(R.string.p5), false, 6, -1));
        questionFlow.put(6, new Question(getString(R.string.p6), false, 8, 7, 9));
        questionFlow.put(7, new Question(getString(R.string.p7), false, 8, 8, 8));
        questionFlow.put(8, new Question(getString(R.string.p8), false, 27, 27, 27));
        questionFlow.put(9, new Question(getString(R.string.p9), true, 27));
        questionFlow.put(10, new Question(getString(R.string.p10), false, 11, -1));
        questionFlow.put(11, new Question(getString(R.string.p11), false, 12, 27, 8));
        questionFlow.put(12, new Question(getString(R.string.p12), false, 13, 13, 13));
        questionFlow.put(13, new Question(getString(R.string.p13), false, 27, 27));
        questionFlow.put(14, new Question(getString(R.string.p14), false, 15, 15));
        questionFlow.put(15, new Question(getString(R.string.p15), false, 16, 16));
        questionFlow.put(16, new Question(getString(R.string.p16), false, 8, 27));
        questionFlow.put(17, new Question(getString(R.string.p17), false, 20, 18, 19));
        questionFlow.put(18, new Question(getString(R.string.p18), false, 20, 20));
        questionFlow.put(19, new Question(getString(R.string.p19), false, 20, 20));
        questionFlow.put(20, new Question(getString(R.string.p20), false, 21, 21, 21));
        questionFlow.put(21, new Question(getString(R.string.p21), false, 22, 22));
        questionFlow.put(22, new Question(getString(R.string.p22), false, 23, 23));
        questionFlow.put(23, new Question(getString(R.string.p23), true, 8));
        questionFlow.put(24, new Question(getString(R.string.p24), false, 25, 26));
        questionFlow.put(25, new Question(getString(R.string.p25), true, 26));
        questionFlow.put(26, new Question(getString(R.string.p26), false, 8, 8));
        questionFlow.put(27, new Question(getString(R.string.p27), false, 28, 28));
        questionFlow.put(28, new Question(getString(R.string.p28), false, 29, 30));
        questionFlow.put(29, new Question(getString(R.string.p29), true, 30));
        questionFlow.put(30, new Question(getString(R.string.p30), true, 31));
        questionFlow.put(31, new Question(getString(R.string.p31), false, 33, 33, 32));
        questionFlow.put(32, new Question(getString(R.string.p32), true, 33));
        questionFlow.put(33, new Question(getString(R.string.p33), false, 34, 34));
        questionFlow.put(34, new Question(getString(R.string.p34), false, 35, 35));
        questionFlow.put(35, new Question(getString(R.string.p35), false, 36, 36, 36));
        questionFlow.put(36, new Question(getString(R.string.p36), false, 37, 37));
        questionFlow.put(37, new Question(getString(R.string.p37), false, 38, 38));
        questionFlow.put(38, new Question(getString(R.string.p38), false, -1, -1));
    }
    private void showQuestion(int qNum) {
        Question q = questionFlow.get(qNum);

        textQuestion.setText(q.text);

        if (q.isOpenAnswer) {
            openAnswer.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            openAnswer.setText("");
        } else {
            openAnswer.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.clearCheck();

            // Reset all options to invisible initially
            option1.setVisibility(View.GONE);
            option2.setVisibility(View.GONE);
            option3.setVisibility(View.GONE);
            option4.setVisibility(View.GONE);

            showQuestionOptions(qNum,option1,option2,option3,option4);
        }
    }

    public void showQuestionOptions(int qNum, RadioButton option1, RadioButton option2, RadioButton option3, RadioButton option4) {
        // Primero, limpiamos los TextViews por si acaso tienen texto previo
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");

        if (qNum == 2) {
            option1.setText(getString(R.string.r2_1));
            option2.setText(getString(R.string.r2_2));
            option3.setText(getString(R.string.r2_3));
            option4.setText(getString(R.string.r2_4)); // Agregamos la cuarta opción
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
            option4.setVisibility(View.VISIBLE);
        } else if (qNum == 3) {
            option1.setText(getString(R.string.r3_1));
            option2.setText(getString(R.string.r3_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 4) {
            option1.setText(getString(R.string.r4_1));
            option2.setText(getString(R.string.r4_2));
            option3.setText(getString(R.string.r4_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 5) {
            option1.setText(getString(R.string.r5_1));
            option2.setText(getString(R.string.r5_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 6) {
            option1.setText(getString(R.string.r6_1));
            option2.setText(getString(R.string.r6_2));
            option3.setText(getString(R.string.r6_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 7) {
            option1.setText(getString(R.string.r7_1));
            option2.setText(getString(R.string.r7_2));
            option3.setText(getString(R.string.r7_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 8) {
            option1.setText(getString(R.string.r8_1));
            option2.setText(getString(R.string.r8_2));
            option3.setText(getString(R.string.r8_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 10) {
            option1.setText(getString(R.string.r10_1));
            option2.setText(getString(R.string.r10_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 11) {
            option1.setText(getString(R.string.r11_1));
            option2.setText(getString(R.string.r11_2));
            option3.setText(getString(R.string.r11_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 12) {
            option1.setText(getString(R.string.r12_1));
            option2.setText(getString(R.string.r12_2));
            option3.setText(getString(R.string.r12_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 13) {
            option1.setText(getString(R.string.r13_1));
            option2.setText(getString(R.string.r13_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 14) {
            option1.setText(getString(R.string.r14_1));
            option2.setText(getString(R.string.r14_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 15) {
            option1.setText(getString(R.string.r15_1));
            option2.setText(getString(R.string.r15_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 16) {
            option1.setText(getString(R.string.r16_1));
            option2.setText(getString(R.string.r16_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 17) {
            option1.setText(getString(R.string.r17_1));
            option2.setText(getString(R.string.r17_2));
            option3.setText(getString(R.string.r17_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 18) {
            option1.setText(getString(R.string.r18_1));
            option2.setText(getString(R.string.r18_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 19) {
            option1.setText(getString(R.string.r19_1));
            option2.setText(getString(R.string.r19_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 20) {
            option1.setText(getString(R.string.r20_1));
            option2.setText(getString(R.string.r20_2));
            option3.setText(getString(R.string.r20_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 21) {
            option1.setText(getString(R.string.r21_1));
            option2.setText(getString(R.string.r21_2));
        } else if (qNum == 22) {
            option1.setText(getString(R.string.r22_1));
            option2.setText(getString(R.string.r22_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 24) {
            option1.setText(getString(R.string.r24_1));
            option2.setText(getString(R.string.r24_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 26) {
            option1.setText(getString(R.string.r26_1));
            option2.setText(getString(R.string.r26_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 27) {
            option1.setText(getString(R.string.r27_1));
            option2.setText(getString(R.string.r27_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 28) {
            option1.setText(getString(R.string.r28_1));
            option2.setText(getString(R.string.r28_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 31) {
            option1.setText(getString(R.string.r31_1));
            option2.setText(getString(R.string.r31_2));
            option3.setText(getString(R.string.r31_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 33) {
            option1.setText(getString(R.string.r33_1));
            option2.setText(getString(R.string.r33_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 34) {
            option1.setText(getString(R.string.r34_1));
            option2.setText(getString(R.string.r34_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 35) {
            option1.setText(getString(R.string.r35_1));
            option2.setText(getString(R.string.r35_2));
            option3.setText(getString(R.string.r35_3));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
            option3.setVisibility(View.VISIBLE);
        } else if (qNum == 36) {
            option1.setText(getString(R.string.r36_1));
            option2.setText(getString(R.string.r36_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 37) {
            option1.setText(getString(R.string.r37_1));
            option2.setText(getString(R.string.r37_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        } else if (qNum == 38) {
            option1.setText(getString(R.string.r38_1));
            option2.setText(getString(R.string.r38_2));
            option1.setVisibility(View.VISIBLE);
            option2.setVisibility(View.VISIBLE);
        }
    }

    private void finishForm() {
        Toast.makeText(this, "Formulario finalizado.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private static class Question {
        String text;
        boolean isOpenAnswer;
        java.util.List<Integer> next;

        Question(String text, boolean isOpenAnswer, Integer... next) {
            this.text = text;
            this.isOpenAnswer = isOpenAnswer;
            this.next = java.util.Arrays.asList(next);
        }
    }
}
