package com.edgesdk.models;

public class Poll_Answer {
    String type,explanation;
    int [] correct = new int[1];
    int id;

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

    public int[] getCorrect() {
        return correct;
    }

    public void setCorrect(int[] correct) {
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
