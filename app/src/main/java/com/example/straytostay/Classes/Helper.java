//package com.example.straytostay.Classes;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//
//import com.example.straytostay.R;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.List;
//
//public class Helper extends Fragment {
//
//    FirebaseFirestore db;
//    private Button post;
//    private List<Question> questionList;
//    private int currentIndex = 0;
//
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.admin_admin_list_fragment, container, false);
//
//
//        db = FirebaseFirestore.getInstance();
//
//        questionList = List.of(
//                new Question(getString(R.string.p2),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r2_1), "LifeStyle", 5),
//                                new Answer(getString(R.string.r2_2), "LifeStyle", 7),
//                                new Answer(getString(R.string.r2_3), "LifeStyle", 10),
//                                new Answer(getString(R.string.r2_4), "LifeStyle", 8)),
//                        List.of(3, 4, 17, 24)),
//
//                new Question(
//                        getString(R.string.p3),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r3_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r3_2), "LifeStyle", 0)),
//                        List.of(12, -1)),
//
//                new Question(
//                        getString(R.string.p4),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r4_1), "Environment", 7),
//                                new Answer(getString(R.string.r4_2), "Environment", 8),
//                                new Answer(getString(R.string.r4_3), "Environment", 6)),
//                        List.of(10, 5, 14)),
//
//                new Question(
//                        getString(R.string.p5),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r5_1), "Environment", 10),
//                                new Answer(getString(R.string.r5_2), "Environment", 0)),
//                        List.of(6, -1)),
//
//                new Question(
//                        getString(R.string.p6),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r6_1), "Resources", 10),
//                                new Answer(getString(R.string.r6_2), "Resources", 7),
//                                new Answer(getString(R.string.r6_3), "Resources", 5)),
//                        List.of(8, 7, 9)),
//
//                new Question(
//                        getString(R.string.p7),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r7_1), "Resources", 4),
//                                new Answer(getString(R.string.r7_2), "Resources", 6),
//                                new Answer(getString(R.string.r7_3), "Resources", 8)),
//                        List.of(8, 8, 8)),
//
//                new Question(
//                        getString(R.string.p8),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r8_1), "Resources", 4),
//                                new Answer(getString(R.string.r8_2), "Resources", 6),
//                                new Answer(getString(R.string.r8_3), "Resources", 8)),
//                        List.of(20,20,20)),
//
//                new Question(
//                        getString(R.string.p9),
//                        true,
//                        List.of(
//                                new Answer(getString(R.string.r9_1), "Resources", 2),
//                                new Answer(getString(R.string.r9_2), "Resources", 5),
//                                new Answer(getString(R.string.r9_3), "Resources", 0)),
//                        List.of(20,20,20)),
//
//                new Question(
//                        getString(R.string.p10),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r10_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r10_2), "LifeStyle", 0)),
//                        List.of(11, -1)),
//
//                new Question(
//                        getString(R.string.p11),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r11_1), "Resources", 10),
//                                new Answer(getString(R.string.r11_2), "Resources", 7),
//                                new Answer(getString(R.string.r11_3), "Resources", 8)),
//                        List.of(12, 20, 8)),
//
//                new Question(
//                        getString(R.string.p12),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r12_1), "Resources", 4),
//                                new Answer(getString(R.string.r12_2), "Resources", 6),
//                                new Answer(getString(R.string.r12_3), "Resources", 8)),
//                        List.of(13, 13, 13)),
//
//                new Question(
//                        getString(R.string.p13),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r13_1), "Resources", 10),
//                                new Answer(getString(R.string.r13_2), "Resources", 0)),
//                        List.of(20, 20)),
//
//                new Question(
//                        getString(R.string.p14),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r14_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r14_2), "LifeStyle", 0)),
//                        List.of(15, 15)),
//
//                new Question(
//                        getString(R.string.p15),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r15_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r15_2), "LifeStyle", 0)),
//                        List.of(16, 16)),
//
//                new Question(
//                        getString(R.string.p16),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r16_1), "Environment", 10),
//                                new Answer(getString(R.string.r16_2), "Environment", 0)),
//                        List.of(8, 20)),
//
//                new Question(
//                        getString(R.string.p17),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r17_1), "LifeStyle", 8),
//                                new Answer(getString(R.string.r17_2), "LifeStyle", 9),
//                                new Answer(getString(R.string.r17_3), "LifeStyle", 10)),
//                        List.of(20, 18, 19)),
//
//                new Question(
//                        getString(R.string.p18),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r18_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r18_2), "LifeStyle", 0)),
//                        List.of(20, 20)),
//
//                new Question(
//                        getString(R.string.p19),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r19_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r19_2), "LifeStyle", 0)),
//                        List.of(20, 20)),
//
//                new Question(
//                        getString(R.string.p20),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r20_1), "Available Time", 4),
//                                new Answer(getString(R.string.r20_2), "Available Time", 7),
//                                new Answer(getString(R.string.r20_3), "Available Time", 10)),
//                        List.of(21, 21, 21)),
//
//                new Question(
//                        getString(R.string.p21),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r21_1), "Resources", 5),
//                                new Answer(getString(R.string.r21_2), "Resources", 10)),
//                        List.of(22, 22)),
//
//                new Question(
//                        getString(R.string.p22),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r22_1), "Environment", 5),
//                                new Answer(getString(R.string.r21_2), "Environment", 10)),
//                        List.of(23, 23)),
//
//                new Question(
//                        getString(R.string.p23),
//                        true,
//                        List.of(
//                                new Answer(getString(R.string.r23_1), "Environment", 10),
//                                new Answer(getString(R.string.r23_2), "Environment", 6),
//                                new Answer(getString(R.string.r23_3), "Environment", 4),
//                                new Answer(getString(R.string.r23_4), "Environment", 0)),
//                        List.of(8,8,8,8)),
//
//                new Question(
//                        getString(R.string.p24),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r24_1), "LifeStyle", 5),
//                                new Answer(getString(R.string.r24_2), "LifeStyle", 10)),
//                        List.of(25, 26)),
//
//                new Question(
//                        getString(R.string.p25),
//                        true,
//                        List.of(
//                                new Answer(getString(R.string.r25_1), "LifeStyle", 6),
//                                new Answer(getString(R.string.r25_2), "LifeStyle", 3),
//                                new Answer(getString(R.string.r25_3), "LifeStyle", 0),
//                                new Answer(getString(R.string.r25_4), "LifeStyle", 8)),
//                        List.of(26,26,26,26)),
//
//                new Question(
//                        getString(R.string.p26),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r26_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r26_2), "LifeStyle", 0)),
//                        List.of(8, 8)),
//
//                new Question(
//                        getString(R.string.p27),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r27_1), "Environment", 10),
//                                new Answer(getString(R.string.r27_2), "Environment", 0)),
//                        List.of(28, 28)),
//
//                new Question(
//                        getString(R.string.p28),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r28_1), "Environment", 8),
//                                new Answer(getString(R.string.r28_2), "Environment", 10)),
//                        List.of(29, 30)),
//
//                new Question(
//                        getString(R.string.p29),
//                        true,
//                        List.of(
//                                new Answer(getString(R.string.r29_1), "LifeStyle", 7),
//                                new Answer(getString(R.string.r29_2), "LifeStyle", 0)),
//                        List.of(30,30)),
//
//                new Question(
//                        getString(R.string.p30),
//                        true,
//                        List.of(
//                                new Answer(getString(R.string.r30_1), "Environment", 10),
//                                new Answer(getString(R.string.r30_2), "Environment", 8)),
//                        List.of(31,31)),
//                new Question(
//                        getString(R.string.p31),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r31_1), "Resources", 10),
//                                new Answer(getString(R.string.r31_2), "Resources", 7)),
//                        List.of(32,32)),
//
//                new Question(
//                        getString(R.string.p32),
//                        true,
//                        List.of(
//                                new Answer(getString(R.string.r32_1), "Resources", 10),
//                                new Answer(getString(R.string.r32_2), "Resources", 0)),
//                        List.of(33,33)),
//
//                new Question(
//                        getString(R.string.p33),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r33_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r33_2), "LifeStyle", 0)),
//                        List.of(34,34)),
//
//                new Question(
//                        getString(R.string.p34),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r34_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r34_2), "LifeStyle", 0)),
//                        List.of(35, 35)),
//
//                new Question(
//                        getString(R.string.p35),
//                        false,
//                        List.of(
//                                new Answer(getString(R.string.r35_1), "LifeStyle", 10),
//                                new Answer(getString(R.string.r35_2), "LifeStyle", 7)),
//                        List.of(36, 36)));
//
//
//        post.setOnClickListener(v -> {
//            if (currentIndex < questionList.size()) {
//                String docId = String.format("q%02d", currentIndex + 2); // q02, q03, etc.
//                Question question = questionList.get(currentIndex);
//
//                db.collection("questions")
//                        .document(docId)
//                        .set(question)
//                        .addOnSuccessListener(aVoid -> Log.d("Upload", docId + " uploaded"))
//                        .addOnFailureListener(e -> Log.e("Upload", "Error uploading " + docId, e));
//
//                Log.d("Helper", "Uploading question index: " + currentIndex);
//                currentIndex++;
//            } else {
//                Log.d("Helper", "All questions have been uploaded.");
//            }
//        });
//
//        return view;
//    }
//
//}