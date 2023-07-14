package com.edgesdk;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.edgesdk.Utils.Constants;
import com.edgesdk.Utils.LogConstants;
import com.edgesdk.Utils.Messages;
import com.edgesdk.Utils.Utils;
import com.edgesdk.dialogues.WagerPointsDialogue;
import com.edgesdk.models.Poll_Answer;
import com.edgesdk.models.Poll_Question;
import com.edgesdk.models.Poll_to_be_resolved;
import com.edgesdk.models.TickerNotifications;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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

public class W2ESettings extends LinearLayout {

    EdgeSdk edgeSdk;
    private LinearLayout ticker_layout;
    private Activity callingActivity;
    EditText txtWalletAddress;
    Typeface custom_font;
    TypeWriterView txt_description;
    CheckBox ckbx_should_ticker_hide,ckbx_should_gaimified_ticker_hide,optOutW2E;
    Button btnModify;
    public W2ESettings(Context context, EdgeSdk edgeSdk) {
        super(context);
        this.edgeSdk=edgeSdk;
        callingActivity = (Activity) context;
        init();
    }

    @SuppressLint("MissingInflatedId")
    private void init() {
        System.out.println("init-method-called");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.w2e_settings, this);
        custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/proxima_nova_regular.ttf");

        // Calculate screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        callingActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        // Set layout parameters for W2ESettings view
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
        setLayoutParams(layoutParams);

        txt_description = view.findViewById(R.id.txt_description);
        txtWalletAddress = view.findViewById(R.id.txtWalletAddress);
        ckbx_should_ticker_hide = view.findViewById(R.id.ckbx_should_ticker_hide);
        ckbx_should_gaimified_ticker_hide = view.findViewById(R.id.ckbx_should_gaimified_ticker_hide);
        optOutW2E = view.findViewById(R.id.optOutW2E);
        btnModify = view.findViewById(R.id.btnModify);

        txt_description.setTypeface(custom_font);


        txt_description.setText("");
        txt_description.setCharacterDelay(20);
        txt_description.animateText("Gaimified TV is a revolutionary television experience that brings interactive elements and real-time rewards to enhance viewer engagement. As you watch your favorite shows, you have the opportunity to earn \u0024EAT tokens, which will soon be replaced by \u0024GAIM. For every 1 \u0024EAT earned, you will receive 2 \u0024GAIM. The \u0024GAIM token is set to be listed on various crypto exchanges in September 2023. To learn more about \u0024GAIM and its exciting possibilities, visit gaimtoken.edgevideo.com");

        setUpFocusListeners();
        setUpClickListeners();
        txtWalletAddress.setFocusable(true);
        txtWalletAddress.requestFocus();

    }

    private void setUpClickListeners(){
        ckbx_should_ticker_hide.setChecked(edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_TICKER_ALLOWED_TO_HIDE));
        ckbx_should_ticker_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // CheckBox is checked
                    // Apply desired changes when the CheckBox is checked
                    edgeSdk.getLocalStorageManager().storeBooleanValue(true,Constants.IS_TICKER_ALLOWED_TO_HIDE);
                } else {
                    // CheckBox is not checked
                    // Apply desired changes when the CheckBox is not checked
                    edgeSdk.getLocalStorageManager().storeBooleanValue(true,Constants.IS_TICKER_ALLOWED_TO_HIDE);
                }
            }
        });

        ckbx_should_gaimified_ticker_hide.setChecked(edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_GAMIFICATION_TICKER_ALLOWED_TO_HIDE));
        ckbx_should_gaimified_ticker_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // CheckBox is checked
                    // Apply desired changes when the CheckBox is checked
                    edgeSdk.getLocalStorageManager().storeBooleanValue(true,Constants.IS_GAMIFICATION_TICKER_ALLOWED_TO_HIDE);
                } else {
                    // CheckBox is not checked
                    // Apply desired changes when the CheckBox is not checked
                    edgeSdk.getLocalStorageManager().storeBooleanValue(true,Constants.IS_GAMIFICATION_TICKER_ALLOWED_TO_HIDE);
                }
            }
        });

    }
    private void setUpFocusListeners(){
        final float ckbx_should_ticker_hide_defaultTextSize = ckbx_should_ticker_hide.getTextSize();
        final float ckbx_should_ticker_hide_focusedTextSize = ckbx_should_ticker_hide_defaultTextSize + 8; // Increase text size by 4dp when focused

        ckbx_should_ticker_hide.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Apply bigger text size when focused
                    ckbx_should_ticker_hide.setTextSize(TypedValue.COMPLEX_UNIT_PX, ckbx_should_ticker_hide_focusedTextSize);
                    ckbx_should_ticker_hide.setTextColor(Color.YELLOW);
                } else {
                    // Apply default text size when not focused
                    ckbx_should_ticker_hide.setTextSize(TypedValue.COMPLEX_UNIT_PX, ckbx_should_ticker_hide_defaultTextSize);
                    ckbx_should_ticker_hide.setTextColor(Color.WHITE);
                }
            }
        });

        final float ckbx_should_gaimified_ticker_hide_defaultTextSize = ckbx_should_gaimified_ticker_hide.getTextSize();
        final float ckbx_should_gaimified_ticker_hide_focusedTextSize = ckbx_should_gaimified_ticker_hide_defaultTextSize + 8; // Increase text size by 4dp when focused

        ckbx_should_gaimified_ticker_hide.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Apply bigger text size when focused
                    ckbx_should_gaimified_ticker_hide.setTextSize(TypedValue.COMPLEX_UNIT_PX, ckbx_should_gaimified_ticker_hide_focusedTextSize);
                    ckbx_should_gaimified_ticker_hide.setTextColor(Color.YELLOW);
                } else {
                    // Apply default text size when not focused
                    ckbx_should_gaimified_ticker_hide.setTextSize(TypedValue.COMPLEX_UNIT_PX, ckbx_should_gaimified_ticker_hide_defaultTextSize);
                    ckbx_should_gaimified_ticker_hide.setTextColor(Color.WHITE);
                }
            }
        });

        final float optOutW2E_defaultTextSize = optOutW2E.getTextSize();
        final float optOutW2E_focusedTextSize = optOutW2E_defaultTextSize + 8; // Increase text size by 4dp when focused

        optOutW2E.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Apply bigger text size when focused
                    optOutW2E.setTextSize(TypedValue.COMPLEX_UNIT_PX, optOutW2E_focusedTextSize);
                    optOutW2E.setTextColor(Color.YELLOW);
                } else {
                    // Apply default text size when not focused
                    optOutW2E.setTextSize(TypedValue.COMPLEX_UNIT_PX, optOutW2E_defaultTextSize);
                    optOutW2E.setTextColor(Color.WHITE);
                }
            }
        });

        int defaultOutlineColor = ContextCompat.getColor(callingActivity, R.color.gray); // Replace with your default outline color
        int focusedOutlineColor = ContextCompat.getColor(callingActivity, R.color.white); // Replace with your focused outline color
        txtWalletAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // EditText has gained focus
                    txtWalletAddress.setBackgroundTintList(ColorStateList.valueOf(focusedOutlineColor));
                } else {
                    // EditText has lost focus
                    txtWalletAddress.setBackgroundTintList(ColorStateList.valueOf(defaultOutlineColor));
                }
            }
        });

        ColorStateList defaultTextColor = btnModify.getTextColors(); // Save the default text color for later use
        float defaultTextSize = btnModify.getTextSize(); // Save the default text size for later use

        btnModify.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Button has gained focus
                    btnModify.setTextColor(getResources().getColor(R.color.yellow));
                    btnModify.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize * 0.7f); // Increase the text size by 20%
                } else {
                    // Button has lost focus
                    btnModify.setTextColor(defaultTextColor);
                    btnModify.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize); // Restore the default text size
                }
            }
        });
    }
}