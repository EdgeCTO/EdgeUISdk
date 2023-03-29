package com.example.edgeuisdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.edgesdk.EdgeSdk;
import com.edgesdk.Ticker;

public class MainActivity extends AppCompatActivity {
    Ticker ticker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeSdk edgeSdk = new EdgeSdk(this);

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
                    Thread.sleep(10000);
                    edgeSdk.getW2EarnManager().updateBaseRateOnServer(600);
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}