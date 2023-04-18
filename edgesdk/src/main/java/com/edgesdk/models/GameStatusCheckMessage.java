package com.edgesdk.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameStatusCheckMessage {
    String type,name;
    public GameStatusCheckMessage(String name){
        this.type="channel";
        this.name=name;
    }
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            GameStatusCheckMessage gameStatusCheckMessage = new GameStatusCheckMessage(this.name);
            return mapper.writeValueAsString(gameStatusCheckMessage);
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
