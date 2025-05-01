package com.example.straytostay.Classes;

import java.util.List;

public class Question {
    private String text;
    private boolean isOpen;
    private List<String> options;
    private List<Integer> next;

    public Question(){}

    public Question(String text, boolean isOpen, List<String> options, List<Integer> next) {
        this.text = text;
        this.options = options;
        this.isOpen = isOpen;
        this.next = next;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        isOpen = isOpen;
    }

    public List<Integer> getNext() {
        return next;
    }

    public void setNext(List<Integer> next) {
        this.next = next;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }
}