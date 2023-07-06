package com.example.edgeuisdk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.edgesdk.Utils.Constants;
import com.edgesdk.Utils.LogConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

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
    private KonfettiView konfettiView = null;
    private Shape.DrawableShape drawableShape = null;
    EdgeSdk edgeSdk;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        final Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_heart);
//        drawableShape = new Shape.DrawableShape(drawable, true);
//
//        konfettiView = findViewById(R.id.konfettiView);
//        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
//        Party party = new PartyFactory(emitterConfig)
//                .angle(270)
//                .spread(90)
//                .setSpeedBetween(1f, 5f)
//                .timeToLive(2000L)
//                .shapes(new Shape.Rectangle(0.2f), drawableShape)
//                .sizes(new Size(12, 5f, 0.2f))
//                .position(0.0, 0.0, 1.0, 0.0)
//                .build();
//        konfettiView.setOnClickListener(view ->
//                konfettiView.start(party)
//        );
//
//        findViewById(R.id.btnExplode).setOnClickListener(v -> {
//            explode();
//        });
//        findViewById(R.id.btnParade).setOnClickListener(v -> {
//            parade();
//        });
//        findViewById(R.id.btnRain).setOnClickListener(v -> {
//            rain();
//        });

        edgeSdk = new EdgeSdk(this,"3bf76d424eeb0a1dcbdef11da9d148d8");

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
        ticker.switchUIForGamification();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    Log.i(LogConstants.Watch_2_Earn,"is base rate updated" +edgeSdk.getW2EarnManager().updateBaseRateOnServer(600));
                    ticker.makeGamificationLayoutVisible(3000);
                    edgeSdk.getLiveGamificationManager().sendChannelUUIDToSocketServer("190f377c-7afa-407f-9684-a7a4d09512ce");
                    Log.i(LogConstants.Watch_2_Earn,"is base rate updated" +edgeSdk.getW2EarnManager().updateBaseRateOnServer(600));
                    Thread.sleep(60000);
                    Log.i(LogConstants.Watch_2_Earn,"is base rate updated" +edgeSdk.getW2EarnManager().updateBaseRateOnServer(600));
                    edgeSdk.getLiveGamificationManager().sendChannelUUIDToSocketServer("190f377c-7afa-407f-9684-a7a4d09512ce");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                currentQuestionIndex = (currentQuestionIndex + 1) % QUESTIONS.length;
//                String question = QUESTIONS[currentQuestionIndex];
//                String[] answers = ANSWERS[currentQuestionIndex];
//                shuffle(answers);
//                ticker.addPollInList(question,answers[0],answers[1],answers[2],answers[3]);
//                ticker.addPollToResolveInList(question,answers[0],"2");
//
//            }
//        }, 0, 10000);

    }

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .spread(360)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 30f)
                        .position(new Position.Relative(0.5, 0.3))
                        .build()
        );
    }

    public void parade() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(30);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.RIGHT - 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(0.0, 0.5))
                        .build(),
                new PartyFactory(emitterConfig)
                        .angle(Angle.LEFT + 45)
                        .spread(Spread.SMALL)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(10f, 30f)
                        .position(new Position.Relative(1.0, 0.5))
                        .build()
        );
    }

    public void rain() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(100);
        konfettiView.start(
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .spread(Spread.ROUND)
                        .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 15f)
                        .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                        .build()
        );
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

    @Override
    protected void onStop() {
        super.onStop();
        edgeSdk.close();
        ticker.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        edgeSdk.close();

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        edgeSdk.close();
        ticker.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}