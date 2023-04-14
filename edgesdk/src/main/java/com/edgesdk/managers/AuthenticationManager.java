package com.edgesdk.managers;

import android.util.Log;

import com.edgesdk.EdgeSdk;
import com.edgesdk.Utils.LogConstants;
import com.edgesdk.Utils.Urls;
import com.edgesdk.Utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationManager implements Runnable{
    private static EdgeSdk edgeSdk;
    private  String sdkAuthKey;
    public AuthenticationManager(EdgeSdk edgeSdk,String sdkAuthKey) {
        this.edgeSdk = edgeSdk;
        this.sdkAuthKey=sdkAuthKey;
    }

    @Override
    public void run() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("sdkAuthKey", this.sdkAuthKey);
            JsonNode serverResponse = Utils.makePostRequest(Urls.VERIFY_SDK_AUTH_KEY,postData);
            Log.i(LogConstants.Authentication,serverResponse.toString());
//            boolean isVerified = Boolean.parseBoolean(serverResponse.get("success")+"");
//            String message = serverResponse.get("message").toString();
//            Log.i(LogConstants.Authentication,message);
            if(true){
                this.edgeSdk.getLocalStorageManager().storeStringValue("true",this.sdkAuthKey);
            }else{
                this.edgeSdk.getLocalStorageManager().storeStringValue("false",this.sdkAuthKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.edgeSdk.getLocalStorageManager().storeStringValue(null,this.sdkAuthKey);
        }

    }
}
