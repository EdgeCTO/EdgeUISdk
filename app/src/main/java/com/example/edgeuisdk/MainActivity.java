package com.example.edgeuisdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edgesdk.EdgeSdk;
import com.edgesdk.Ticker;
import com.edgesdk.Utils.LogConstants;

public class MainActivity extends AppCompatActivity {
    Ticker ticker;
    int poll_number=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EdgeSdk edgeSdk = new EdgeSdk(this,"574b4537766ea033a492f69dbb928c0b");

        edgeSdk.getLocalStorageManager().storeBooleanValue(true, com.edgesdk.Utils.Constants.IS_TICKER_ALLOWED_TO_HIDE);
        edgeSdk.getLocalStorageManager().storeBooleanValue(false,com.edgesdk.Utils.Constants.IS_OPT_OUT_W2E_ENABLED);
        edgeSdk.getLocalStorageManager().storeBooleanValue(true,com.edgesdk.Utils.Constants.IS_VIEWER_WALLET_ADDRESS_FORWARDED);
        edgeSdk.getLocalStorageManager().storeStringValue("0x1CE5db82533E4Fec3e1D983D89070F8185fC163F",com.edgesdk.Utils.Constants.WALLET_ADDRESS);

        edgeSdk.start();
        edgeSdk.startStaking();
        ticker = new Ticker(this,edgeSdk);

        ticker.setBackpressed(false);
        ticker.setPlaying(true);

        LinearLayout layout = findViewById(R.id.main_layout);

        layout.addView(ticker);
        ticker.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    edgeSdk.getW2EarnManager().updateBaseRateOnServer(600);
                    ticker.makeGamificationLayoutVisible(3000);
                    ticker.displayQRCodeForGamification(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }       
            }
        }).start();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ticker.addPollInList(poll_number);
        poll_number++;
        return super.onKeyDown(keyCode, event);
    }
}