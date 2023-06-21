package com.edgesdk.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PollWagerAnswer_Message {
    String type;
    int id,answer,wager;
    public PollWagerAnswer_Message(int id,int answer,int wager){
        this.type="answer";
        this.id=id;
        this.answer=answer;
        this.wager=wager;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getAnswer() {
        return answer;
    }

    public int getWager() {
        return wager;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PollWagerAnswer_Message gameStatusCheckMessage = new PollWagerAnswer_Message(this.id,this.answer,this.wager);
            return mapper.writeValueAsString(gameStatusCheckMessage);
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
