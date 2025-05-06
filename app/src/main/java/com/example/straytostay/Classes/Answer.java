package com.example.straytostay.Classes;

import java.util.Map;

public class Answer {
    private String text, category;
    private int score;

    public Answer(){}
    public Answer(String text, String category, int score) {
        this.text = text;
        this.category = category;
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
