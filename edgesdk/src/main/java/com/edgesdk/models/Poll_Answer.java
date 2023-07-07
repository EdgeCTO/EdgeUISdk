package com.edgesdk.models;

public class Poll_Answer {
    String type,explanation;
    int [] correct = new int[1];
    int id,amount;
    boolean isCorrect=false;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int[] getCorrectArray() {
        return correct;
    }

    public void setCorrectArray(int[] correct) {
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
