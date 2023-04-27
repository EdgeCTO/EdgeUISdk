package com.edgesdk.models;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GetSDKLogMessage {
    String sdkAPIKey;
    public GetSDKLogMessage(String sdkAPIKey){
        this.sdkAPIKey=sdkAPIKey;
    }
    public String getSdkAPIKey() {
        return sdkAPIKey;
    }
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            GetSDKLogMessage qrCodeSocket_createMessageModel = new GetSDKLogMessage(this.sdkAPIKey);
            return mapper.writeValueAsString(qrCodeSocket_createMessageModel);
        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
