package com.edgesdk.managers;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.edgesdk.EdgeSdk;
import com.edgesdk.Utils.Constants;
import com.edgesdk.Utils.LogConstants;
import com.edgesdk.Utils.Urls;
import com.edgesdk.Utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
            Log.i(LogConstants.Authentication,serverResponse+"");
            boolean isVerified = Boolean.parseBoolean(serverResponse.get("success")+"");
            String message = serverResponse.get("message").toString();
            Log.i(LogConstants.Authentication,"message"+message);
            if(isVerified){
                postData = new JSONObject();
                postData.put("sdkAPIKey", this.sdkAuthKey);
                serverResponse = Utils.makePostRequest(Urls.LOAD_SDK_LOGO,postData);
                JsonNode dataNode = serverResponse.get("data");
                if (dataNode != null && dataNode.isArray() && dataNode.size() > 0) {
                    JsonNode firstSdkNode = dataNode.get(0);
                    if (firstSdkNode != null) {
                        String logoUrl = firstSdkNode.get("logo_url").asText();
                        // Use the logoUrl as needed
                        Log.i(LogConstants.Authentication,logoUrl);
                        String  imageUrl = edgeSdk.getLocalStorageManager().getStringValue(Constants.LOGO_URL);
                        if (imageUrl == null || !imageUrl.equals(logoUrl)) {
                            Glide.with(edgeSdk.getContext()).load(logoUrl).diskCacheStrategy(DiskCacheStrategy.ALL).downloadOnly(new SimpleTarget<File>() {
                                @Override
                                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                    String fileName = resource.getName();
                                    String filePath = resource.getAbsolutePath();
                                    Log.i(LogConstants.Authentication,"filePath:"+filePath);
                                    edgeSdk.getLocalStorageManager().storeStringValue(filePath,Constants.LOGO_IMAGE_PATH);
                                    Log.i(LogConstants.Authentication,edgeSdk.getLocalStorageManager().getStringValue(Constants.LOGO_IMAGE_PATH));
                                }
                            });
                        }
                    }
                }

            }else{
                this.edgeSdk.getLocalStorageManager().storeStringValue("false",this.sdkAuthKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            this.edgeSdk.getLocalStorageManager().storeStringValue(null,this.sdkAuthKey);
        }

    }
}
