package com.edgesdk.models;

public class Poll_Answer {
    String type,explanation;
    String [] correct = new String[1];
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

    public String[] getCorrect() {
        return correct;
    }

    public void setCorrect(String[] correct) {
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
