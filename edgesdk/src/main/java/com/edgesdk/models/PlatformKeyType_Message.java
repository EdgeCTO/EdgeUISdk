package com.edgesdk.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PlatformKeyType_Message {
    String type,key,platform;
    public PlatformKeyType_Message(String key){
        this.type="app";
        this.key=key;
        this.platform="android";
    }
    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getPlatform() {
        return platform;
    }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            PlatformKeyType_Message gameStatusCheckMessage = new PlatformKeyType_Message(this.key);
            return mapper.writeValueAsString(gameStatusCheckMessage);
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
