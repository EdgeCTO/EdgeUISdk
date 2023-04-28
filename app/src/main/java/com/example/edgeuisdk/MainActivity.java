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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Ticker ticker;
    int poll_number=0;
    private static final String[] QUESTIONS = {
            "What is the capital city of France and how did it get its name?",
            "Who is credited with inventing the telephone and what was the first words spoken over it?",
            "What is the name of the largest planet in our solar system and what makes it so unique?",
            "What is the name and location of the tallest mountain in the world, and what challenges do climbers face when attempting to summit it?",
            "Who is the author of the novel 'To Kill a Mockingbird', and what inspired them to write it?"
    };

    private static final String[][] ANSWERS = {
            {"Paris", "London", "Berlin", "Madrid"},
            {"Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Samuel Morse - Although Bell is widely credited with inventing the telephone, there were several other inventors working on similar technologies at the same time, including Edison, Tesla, and Morse."},
            {"Jupiter", "Saturn", "Neptune", "Uranus - Although Jupiter is the largest planet by diameter, Saturn is the most massive planet in the solar system."},
            {"Mount Everest (Nepal/Tibet)", "K2 (Pakistan/China)", "Kangchenjunga (Nepal/India)", "Lhotse (Nepal/China) - Mount Everest is the tallest mountain in the world, but K2 is the second-tallest and is considered by many to be a more difficult climb."},
            {"Harper Lee", "F. Scott Fitzgerald", "Ernest Hemingway", "Mark Twain - Harper Lee is the author of 'To Kill a Mockingbird', a novel about racial injustice in the American South."}
    };

    private static final Random RANDOM = new Random();
    private static int currentQuestionIndex = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EdgeSdk edgeSdk = new EdgeSdk(this,"3bf76d424eeb0a1dcbdef11da9d148d8");

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

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentQuestionIndex = (currentQuestionIndex + 1) % QUESTIONS.length;
                String question = QUESTIONS[currentQuestionIndex];
                String[] answers = ANSWERS[currentQuestionIndex];
                shuffle(answers);
                ticker.addPollInList(question,answers[0],answers[1],answers[2],answers[3]);
            }
        }, 0, 10000);

    }

    private static void shuffle(String[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
}