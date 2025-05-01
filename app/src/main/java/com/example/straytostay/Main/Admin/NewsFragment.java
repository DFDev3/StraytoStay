package com.example.straytostay.Main.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.straytostay.Classes.Question;
import com.example.straytostay.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NewsFragment extends Fragment {

    FirebaseFirestore db;
    private Button post;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_news_fragment, container, false);

        post = view.findViewById(R.id.btnPost);

        List<Question> questionList = List.of(
                new Question(
                        getString(R.string.p2),
                        false,
                        List.of(
                                getString(R.string.r2_1),
                                getString(R.string.r2_2),
                                getString(R.string.r2_3),
                                getString(R.string.r2_4)),
                        List.of(3, 4, 17, 24)),

                new Question(
                        getString(R.string.p3),
                        false,
                        List.of(
                                getString(R.string.r3_1),
                                getString(R.string.r3_2)),
                        List.of(12, -1)),

                new Question(
                        getString(R.string.p4),
                        false,
                        List.of(
                                getString(R.string.r4_1),
                                getString(R.string.r4_2),
                                getString(R.string.r4_3)),
                        List.of(10, 5, 14)),

                new Question(
                        getString(R.string.p5),
                        false,
                        List.of(
                                getString(R.string.r5_1),
                                getString(R.string.r5_2)),
                        List.of(6, -1)),

                new Question(
                        getString(R.string.p6),
                        false,
                        List.of(
                                getString(R.string.r6_1),
                                getString(R.string.r6_2),
                                getString(R.string.r6_3)),
                        List.of(8, 7, 9)),

                new Question(
                        getString(R.string.p7),
                        false,
                        List.of(
                                getString(R.string.r7_1),
                                getString(R.string.r7_2),
                                getString(R.string.r7_3)),
                        List.of(8, 8, 8)),

                new Question(
                        getString(R.string.p8),
                        false,
                        List.of(
                                getString(R.string.r8_1),
                                getString(R.string.r8_2),
                                getString(R.string.r8_3)),
                        List.of(27, 27, 27)),

                new Question(
                        getString(R.string.p9),
                        true,
                        List.of(""),
                        List.of(27)),

                new Question(
                        getString(R.string.p10),
                        false,
                        List.of(
                                getString(R.string.r10_1),
                                getString(R.string.r10_2)),
                        List.of(11, -1)),

                new Question(
                        getString(R.string.p11),
                        false,
                        List.of(
                                getString(R.string.r11_1),
                                getString(R.string.r11_2),
                                getString(R.string.r11_3)),
                        List.of(12, 27, 8)),

                new Question(
                        getString(R.string.p12),
                        false,
                        List.of(
                                getString(R.string.r12_1),
                                getString(R.string.r12_2),
                                getString(R.string.r12_3)),
                        List.of(13, 13, 13)),

                new Question(
                        getString(R.string.p13),
                        false,
                        List.of(
                                getString(R.string.r13_1),
                                getString(R.string.r13_2)),
                        List.of(27, 27)),

                new Question(
                        getString(R.string.p14),
                        false,
                        List.of(
                                getString(R.string.r14_1),
                                getString(R.string.r14_2)),
                        List.of(15, 15)),

                new Question(
                        getString(R.string.p15),
                        false,
                        List.of(
                                getString(R.string.r15_1),
                                getString(R.string.r15_2)),
                        List.of(16, 16)),

                new Question(
                        getString(R.string.p16),
                        false,
                        List.of(
                                getString(R.string.r16_1),
                                getString(R.string.r16_2)),
                        List.of(8, 27)),

                new Question(
                        getString(R.string.p17),
                        false,
                        List.of(
                                getString(R.string.r17_1),
                                getString(R.string.r17_2),
                                getString(R.string.r17_3)),
                        List.of(20, 18, 19)),

                new Question(
                        getString(R.string.p18),
                        false,
                        List.of(
                                getString(R.string.r18_1),
                                getString(R.string.r18_2)),
                        List.of(20, 20)),

                new Question(
                        getString(R.string.p19),
                        false,
                        List.of(
                                getString(R.string.r19_1),
                                getString(R.string.r19_2)),
                        List.of(20, 20)),

                new Question(
                        getString(R.string.p20),
                        false,
                        List.of(
                                getString(R.string.r20_1),
                                getString(R.string.r20_2),
                                getString(R.string.r20_3)),
                        List.of(21, 21, 21)),

                new Question(
                        getString(R.string.p21),
                        false,
                        List.of(
                                getString(R.string.r21_1),
                                getString(R.string.r21_2)),
                        List.of(22, 22)),

                new Question(
                        getString(R.string.p22),
                        false,
                        List.of(
                                getString(R.string.r22_1),
                                getString(R.string.r22_2)),
                        List.of(23, 23)),

                new Question(
                        getString(R.string.p23),
                        true,
                        List.of(""),
                        List.of(8)),

                new Question(
                        getString(R.string.p24),
                        false,
                        List.of(
                                getString(R.string.r24_1),
                                getString(R.string.r24_2)),
                        List.of(25, 26)),

                new Question(
                        getString(R.string.p25),
                        true,
                        List.of(""),
                        List.of(26)),

                new Question(
                        getString(R.string.p26),
                        false,
                        List.of(
                                getString(R.string.r26_1),
                                getString(R.string.r26_2)),
                        List.of(8, 8)),

                new Question(
                        getString(R.string.p27),
                        false,
                        List.of(
                                getString(R.string.r27_1),
                                getString(R.string.r27_2)),
                        List.of(28, 28)),

                new Question(
                        getString(R.string.p28),
                        false,
                        List.of(
                                getString(R.string.r28_1),
                                getString(R.string.r28_2)),
                        List.of(29, 30)),

                new Question(
                        getString(R.string.p29),
                        true,
                        List.of(""),
                        List.of(30)),

                new Question(
                        getString(R.string.p30),
                        true,
                        List.of(""),
                        List.of(31)),
                new Question(
                        getString(R.string.p31),
                        false,
                        List.of(
                                getString(R.string.r31_1),
                                getString(R.string.r31_2),
                                getString(R.string.r31_3)),
                        List.of(33, 33, 32)),

                new Question(
                        getString(R.string.p32),
                        true,
                        List.of(""),
                        List.of(33)),

                new Question(
                        getString(R.string.p33),
                        false,
                        List.of(
                                getString(R.string.r33_1),
                                getString(R.string.r33_2)),
                        List.of(34, 34)),

                new Question(
                        getString(R.string.p34),
                        false,
                        List.of(
                                getString(R.string.r34_1),
                                getString(R.string.r34_2)),
                        List.of(35, 35)),

                new Question(
                        getString(R.string.p35),
                        false,
                        List.of(
                                getString(R.string.r35_1),
                                getString(R.string.r35_2),
                                getString(R.string.r35_3)),
                        List.of(36, 36, 36)),

                new Question(
                        getString(R.string.p36),
                        false,
                        List.of(
                                getString(R.string.r36_1),
                                getString(R.string.r36_2)),
                        List.of(37, 37)),

                new Question(
                        getString(R.string.p37),
                        false,
                        List.of(
                                getString(R.string.r37_1),
                                getString(R.string.r37_2)),
                        List.of(38, 38)),

                new Question(
                        getString(R.string.p38),
                        false,
                        List.of(
                                getString(R.string.r38_1),
                                getString(R.string.r38_2)),
                        List.of(-1, -1)));


        post.setOnClickListener(v -> uploadQuestions(questionList));
        return view;
    }

    public void uploadQuestions(List<Question> questionList) {
        db = FirebaseFirestore.getInstance();

        for (int i = 0; i < questionList.size(); i++) {
            String docId = String.format("q%02d", i + 2); // Ensures q02, q03, ..., q10, etc.
            db.collection("questions")
                    .document(docId)
                    .set(questionList.get(i))
                    .addOnSuccessListener(aVoid -> Log.d("Upload", docId + " uploaded"))
                    .addOnFailureListener(e -> Log.e("Upload", "Error uploading " + docId, e));
        }
    }

}

