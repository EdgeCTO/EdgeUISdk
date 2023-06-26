package com.edgesdk;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;

import com.edgesdk.Utils.Constants;
import com.edgesdk.Utils.LogConstants;
import com.edgesdk.Utils.Messages;
import com.edgesdk.dialogues.WagerPointsDialogue;
import com.edgesdk.models.Poll_Answer;
import com.edgesdk.models.Poll_Question;
import com.edgesdk.models.Poll_to_be_resolved;
import com.edgesdk.models.TickerNotifications;
import com.github.jinatonic.confetti.ConfettiManager;

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

public class Ticker extends LinearLayout {
    private TextView txt_total_eats,txt_eat_market_price,txt_eat_market_inc_dec,txt_balance,txt_staked,txt_est_apy,txt_earned,txt_per_day,txt_total_points;
    private TextView txt_today,txt_watch_to_earn_heading,txt_title_total_eats,txt_title_eat_market_price,txt_title_eat_market_inc_dec,txt_title_balance,txt_title_staked,txt_title_est_apy,txt_title_earned,txt_title_per_day,txt_title_total_points;
    EdgeSdk edgeSdk;
    private LinearLayout ticker_layout;
    Typeface custom_font;
    TickerNotifications[] tickerNotifications;
    int currentNotificationMsgNumber;
    boolean isPrintingThreadsRunning,playing;
    private boolean isBackpressed,isVideoPlayedForFirstTime;
    boolean isTickerVisibilityThreadRunning;
    private Activity callingActivity;
    private LinearLayout gamificationStatusLayout;
    private ImageView gamificationQRCode;
    private Handler qrCodeViewhandler;
    private LinearLayout polls_holder;
    private LinearLayout polls_to_resolve_holder;
    Timer staked_values_timer,est_apy_values_timer,earning_per_day_timer,eat_market_price_timer,ticker_values_timer,is_video_playing_or_paused_detector_timer,total_eats_timer,watch_to_earn_title_updater_timer,second_secreen_command_listener,ticker_visibility_controler_timer,poll_list_moderator_timer,poll_list_to_be_resolved_moderator_timer;
    private ImageView sdk_logo;
    private KonfettiView konfettiView = null;
    private Shape.DrawableShape drawableShape = null;
    private MediaPlayer mediaPlayer;
    private LinearLayout win_message_layout;
    private ScrollView polls_holder_scroll_container,polls_waiting_to_resolve_container;
    private TextView number_of_won_coins;
    private boolean isFocusOnLeftSide;
    private LinearLayout gamification_ticker_layout,gamification_poll_layout;
    Party party;
    long last_poll_displayed_id;
    private Map<String, Poll_to_be_resolved> listOfWageredPolls;

    public Ticker(Context context,EdgeSdk edgeSdk) {
        super(context);
        this.edgeSdk=edgeSdk;
        callingActivity = (Activity) context;
        init();
    }

    @SuppressLint("MissingInflatedId")
    private void init() {
        System.out.println("init-method-called");

        isPrintingThreadsRunning=false;
        isBackpressed=false;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.ticker_layout, this);
        txt_total_eats = view.findViewById(R.id.txt_total_eats);
        txt_eat_market_price = view.findViewById(R.id.txt_eat_market_price);
        txt_eat_market_inc_dec = view.findViewById(R.id.txt_eat_market_inc_dec);
        txt_balance = view.findViewById(R.id.txt_balance);
        txt_staked = view.findViewById(R.id.txt_staked);
        txt_est_apy = view.findViewById(R.id.txt_est_apy);
        txt_earned = view.findViewById(R.id.txt_earned);
        //txt_per_day = view.findViewById(R.id.txt_per_day);
        txt_total_points = view.findViewById(R.id.txt_total_points);
        txt_title_total_points = view.findViewById(R.id.txt_title_total_points);
        polls_holder = view.findViewById(R.id.polls_holder);
        polls_to_resolve_holder = view.findViewById(R.id.polls_waiting_to_resolve_holder);

        txt_today = view.findViewById(R.id.txt_today);
        txt_watch_to_earn_heading= view.findViewById(R.id.txt_watch_to_earn_heading);
        txt_title_total_eats = view.findViewById(R.id.txt_title_total_eats);
        txt_title_eat_market_price = view.findViewById(R.id.txt_title_eat_market_price);
        txt_title_eat_market_inc_dec = view.findViewById(R.id.txt_eat_market_inc_dec);
        txt_title_balance = view.findViewById(R.id.txt_title_balance);
        txt_title_staked = view.findViewById(R.id.txt_title_staked);
        txt_title_est_apy = view.findViewById(R.id.txt_title_est_apy);
        txt_title_earned = view.findViewById(R.id.txt_title_earned);
        sdk_logo = view.findViewById(R.id.sdk_logo);
        win_message_layout = view.findViewById(R.id.win_message_layout);
        number_of_won_coins = view.findViewById(R.id.number_of_won_coins);
        //txt_title_per_day = view.findViewById(R.id.txt_title_per_day);
        polls_holder_scroll_container = findViewById(R.id.polls_holder_scroll_container);
        polls_waiting_to_resolve_container = findViewById(R.id.polls_waiting_to_resolve_container);
        gamificationStatusLayout = findViewById(R.id.gamificationStatusLayout);

        gamificationStatusLayout.setVisibility(View.INVISIBLE);

        ticker_layout = findViewById(R.id.ticker_layout);

        gamification_ticker_layout = findViewById(R.id.gamification_ticker_layout);
        gamification_poll_layout = findViewById(R.id.gamification_poll_layout);

        switchUIForGamification();

        gamificationQRCode = findViewById(R.id.gamificationQRCode);
        // Set the image source for the ImageView
        // Optionally, you can customize other properties of the ImageView
        gamificationQRCode.setScaleType(ImageView.ScaleType.FIT_CENTER);
        gamificationQRCode.setVisibility(GONE);
        //qrCodeView.setVisibility(View.GONE);
        // Create a Handler to post a delayed runnable
        qrCodeViewhandler = new Handler();

        DisplayMetrics metrics = new DisplayMetrics();
        callingActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;
        int screenWidthPx = metrics.widthPixels;
        Log.i("Density", "screenWidthPx"+screenWidthPx);
        ticker_layout.getLayoutParams().width = screenWidthPx;

        custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/proxima_nova_regular.ttf");
        isPrintingThreadsRunning=false;
        isFocusOnLeftSide=true;
        //setting-up fonts
        txt_total_eats.setTypeface(custom_font);
        txt_title_total_eats.setTypeface(custom_font);
        txt_eat_market_price.setTypeface(custom_font);
        txt_title_eat_market_price.setTypeface(custom_font);
        txt_eat_market_inc_dec.setTypeface(custom_font);
        txt_balance.setTypeface(custom_font);
        txt_title_balance.setTypeface(custom_font);
        txt_staked.setTypeface(custom_font);
        txt_title_staked.setTypeface(custom_font);
        txt_est_apy.setTypeface(custom_font);
        txt_title_est_apy.setTypeface(custom_font);
        txt_today.setTypeface(custom_font);
        txt_earned.setTypeface(custom_font);
        txt_title_earned.setTypeface(custom_font);
        //txt_per_day.setTypeface(custom_font);
        //txt_title_per_day.setTypeface(custom_font);
        txt_watch_to_earn_heading.setTypeface(custom_font);
        txt_title_eat_market_inc_dec.setTypeface(custom_font);
        txt_total_points.setTypeface(custom_font);
        txt_title_total_points.setTypeface(custom_font);
        //setting up notification.
        isVideoPlayedForFirstTime=false;
        isTickerVisibilityThreadRunning=true;
        currentNotificationMsgNumber=0;
        tickerNotifications = new TickerNotifications[6];

        listOfWageredPolls = new HashMap<>();

        String not_staking_no_balance = edgeSdk.getLocalStorageManager().getStringValue(Messages.NOT_STAKING_NO_BALANCE);
        not_staking_no_balance=not_staking_no_balance!=null?not_staking_no_balance:Messages.DEFAULT_NOT_STAKING_NO_BALANCE;

        String not_staking_low_balance = edgeSdk.getLocalStorageManager().getStringValue(Messages.NOT_STAKING_LOW_BALANCE);
        not_staking_low_balance=not_staking_low_balance!=null?not_staking_low_balance:Messages.DEFAULT_NOT_STAKING_LOW_BALANCE;

        String watch_2_earn_active = edgeSdk.getLocalStorageManager().getStringValue(Messages.WATCH_2_EARN_ACTIVE);
        watch_2_earn_active=watch_2_earn_active!=null?watch_2_earn_active:Messages.DEFAULT_WATCH_2_EARN_ACTIVE;

        String staking_active = edgeSdk.getLocalStorageManager().getStringValue(Messages.STAKING_ACTIVE);
        staking_active=staking_active!=null?staking_active:Messages.DEFAULT_STAKING_ACTIVE;

        String wallet_not_forwarded = edgeSdk.getLocalStorageManager().getStringValue(Messages.WALLET_NO_FORWARDED);
        wallet_not_forwarded=wallet_not_forwarded!=null?wallet_not_forwarded:Messages.DEFAULT_WALLET_NO_FORWARDED;

        String landing_page_message_1 = edgeSdk.getLocalStorageManager().getStringValue(Messages.LANDING_PAGE_MESSAGE_1);
        landing_page_message_1=landing_page_message_1!=null?landing_page_message_1:Messages.DEFAULT_LANDING_PAGE_MESSAGE_1;

        String catagory_page_message_1 = edgeSdk.getLocalStorageManager().getStringValue(Messages.CATAGORY_PAGE_MESSAGE_1);
        catagory_page_message_1=catagory_page_message_1!=null?catagory_page_message_1:Messages.DEFAULT_CATAGORY_PAGE_MESSAGE_1;


        tickerNotifications[0] = new TickerNotifications(wallet_not_forwarded,60000,0,true,false,false,false);
        tickerNotifications[1] = new TickerNotifications(watch_2_earn_active,5000,0,true,true,false,false);
        tickerNotifications[2] = new TickerNotifications(staking_active,5000,0,true,true,false,false);

        //if wallet is  forwarded and staking is 0 + w2e is connected and staking is working

        tickerNotifications[3] = new TickerNotifications(watch_2_earn_active,5000,0,true,false,false,true);
        tickerNotifications[4] = new TickerNotifications(staking_active,5000,0,true,false,false,true);
        tickerNotifications[5] = new TickerNotifications(not_staking_low_balance,5000,0,true,false,false,true);


        boolean isOptOutEnabled = this.edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_OPT_OUT_W2E_ENABLED);
        if(!isOptOutEnabled){

            staked_values_timer = new Timer();

            est_apy_values_timer = new Timer();

            earning_per_day_timer = new Timer();

            eat_market_price_timer = new Timer();

            ticker_values_timer = new Timer();

            total_eats_timer = new Timer();

            watch_to_earn_title_updater_timer = new Timer();

            poll_list_moderator_timer = new Timer();

            poll_list_to_be_resolved_moderator_timer = new Timer();
        }

        //getting logo .
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                String logoPath = edgeSdk.getLocalStorageManager().getStringValue(Constants.LOGO_IMAGE_PATH);
                if(logoPath != null){
                    Bitmap bitmap = BitmapFactory.decodeFile(logoPath);
                    sdk_logo.setImageBitmap(bitmap);
                    Log.i(LogConstants.Authentication,"updated logo");
                }else{
                    Log.i(LogConstants.Authentication,"logo image path is null");
                    handler.postDelayed(this, 1000); // run this code again after 1 second
                }
            }
        };
        handler.post(runnableCode);


        final Drawable drawable = ContextCompat.getDrawable(callingActivity.getApplicationContext(), R.drawable.ic_heart);
        drawableShape = new Shape.DrawableShape(drawable, true);

        konfettiView = findViewById(R.id.konfettiView);
        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(2000L)
                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                .sizes(new Size(12, 5f, 0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();
    }

    public void switchUIForGamification(){
         ticker_layout.setVisibility(GONE);
         gamification_poll_layout.setVisibility(VISIBLE);
         gamification_ticker_layout.setVisibility(VISIBLE);
    }
    public void switchUIForW2E(){
         ticker_layout.setVisibility(VISIBLE);
         gamification_poll_layout.setVisibility(GONE);
         gamification_ticker_layout.setVisibility(GONE);
    }


    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isBackpressed() {
        return isBackpressed;
    }

    public void setBackpressed(boolean backpressed) {
        isBackpressed = backpressed;
    }

    public boolean isTickerVisibilityThreadRunning() {
        return isTickerVisibilityThreadRunning;
    }

    public void setTickerVisibilityThreadRunning(boolean tickerVisibilityThreadRunning) {
        isTickerVisibilityThreadRunning = tickerVisibilityThreadRunning;
    }

    public boolean isVideoPlayedForFirstTime() {
        return isVideoPlayedForFirstTime;
    }

    public void setVideoPlayedForFirstTime(boolean videoPlayedForFirstTime) {
        isVideoPlayedForFirstTime = videoPlayedForFirstTime;
    }

    public boolean isPrintingThreadsRunning() {
        return isPrintingThreadsRunning;
    }

    public void setPrintingThreadsRunning(boolean printingThreadsRunning) {
        isPrintingThreadsRunning = printingThreadsRunning;
    }

    public void displayQRCodeForGamification(int time){
        Bitmap qrCode = this.edgeSdk.getQRCodeManager().getGamifiedTvQRCode();
        qrCodeViewhandler.post(new Runnable() {
            @Override
            public void run() {
                gamificationQRCode.setVisibility(VISIBLE);
                gamificationQRCode.setImageBitmap(qrCode);
                // Create an ObjectAnimator that gradually increases the ImageView's alpha from 0 to 1 over 2 seconds
                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(gamificationQRCode, "alpha", 0f, 1f);
                fadeIn.setDuration(3000); // 2 seconds

                // Start the fade-in animation
                fadeIn.start();


            }
        });

        qrCodeViewhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Set the visibility of the ImageView to GONE after the specified time
                gamificationQRCode.setVisibility(View.GONE);
            }
        }, time);
    }
    @SuppressLint("MissingInflatedId")
    public void addPollInList(String poll_question, String poll_answer_a, String poll_answer_b, String poll_answer_c, String poll_answer_d,Poll_Question poll) {
        View poll_view = callingActivity.getLayoutInflater().inflate(R.layout.poll, null);
        Log.i(LogConstants.Live_Gamification,"currentView"+poll_view);
        TextView question = poll_view.findViewById(R.id.poll_question);
        TextView answer_a = poll_view.findViewById(R.id.poll_answer_a);
        TextView answer_b = poll_view.findViewById(R.id.poll_answer_b);
        TextView answer_c = poll_view.findViewById(R.id.poll_answer_c);
        TextView answer_d = poll_view.findViewById(R.id.poll_answer_d);
        Animation slideInFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        slideInFromLeft.setDuration(1000);

        answer_a.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(callingActivity, "Answer a is selected", Toast.LENGTH_SHORT).show();
                callingActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        if(poll.getMode()==1) {
                            WagerPointsDialogue wagerPointsDialogue = new WagerPointsDialogue(callingActivity, poll_question, poll_answer_a, Ticker.this, poll,edgeSdk);
                            wagerPointsDialogue.show();
                        }else{
                            addPollToResolveInList(poll_question,poll_answer_a,"5",poll);
                        }
                    }
                });
            }
        });

        answer_b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(callingActivity, "Answer b is selected", Toast.LENGTH_SHORT).show();
                callingActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        if(poll.getMode()==1) {
                            WagerPointsDialogue wagerPointsDialogue = new WagerPointsDialogue(callingActivity, poll_question, poll_answer_b, Ticker.this, poll,edgeSdk);
                            wagerPointsDialogue.show();
                        }else{
                            addPollToResolveInList(poll_question,poll_answer_b,"5",poll);
                        }
                    }
                });
            }
        });

        answer_c.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(callingActivity, "Answer c is selected", Toast.LENGTH_SHORT).show();
                callingActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        if(poll.getMode()==1) {
                            WagerPointsDialogue wagerPointsDialogue = new WagerPointsDialogue(callingActivity, poll_question, poll_answer_c, Ticker.this, poll,edgeSdk);
                            wagerPointsDialogue.show();
                        }else{
                            addPollToResolveInList(poll_question,poll_answer_c,"5",poll);
                        }
                    }
                });
            }
        });

        answer_d.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(callingActivity, "Answer d is selected", Toast.LENGTH_SHORT).show();
                callingActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        if(poll.getMode()==1) {
                            WagerPointsDialogue wagerPointsDialogue = new WagerPointsDialogue(callingActivity, poll_question, poll_answer_d, Ticker.this, poll,edgeSdk);
                            wagerPointsDialogue.show();
                        }else{
                            addPollToResolveInList(poll_question,poll_answer_d,"5",poll);
                        }
                    }
                });
            }
        });



        polls_holder.post(new Runnable() {
            @Override
            public void run() {
                question.setText(poll_question);
                answer_a.setText(poll_answer_a);
                answer_b.setText(poll_answer_b);
                answer_c.setText(poll_answer_c);
                answer_d.setText(poll_answer_d);

                // Get the width of the TextView container
                int containerWidth = question.getWidth();

                // Get the total text length of the question and answers
                int totalTextLength = poll_question.length() + poll_answer_a.length() + poll_answer_b.length() + poll_answer_c.length() + poll_answer_d.length();

                // Calculate the average text length per character
                float averageTextLengthPerChar = (float) totalTextLength / (float) (poll_question.length() + poll_answer_a.length() + poll_answer_b.length() + poll_answer_c.length() + poll_answer_d.length());

                // Calculate the font size based on the container width and average text length per character
                float fontSize = containerWidth / (averageTextLengthPerChar * 1.5f);

                // Set the maximum and minimum font sizes
                float maxFontSize = 18f;
                float minFontSize = 12f;

                // If the calculated font size is greater than the maximum font size, set it to the maximum font size
                if (fontSize > maxFontSize) {
                    fontSize = maxFontSize;
                }

                // If the calculated font size is less than the minimum font size, set it to the minimum font size
                if (fontSize < minFontSize) {
                    fontSize = minFontSize;
                }

                // Set the font size of the TextViews
                question.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                answer_a.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                answer_b.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                answer_c.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                answer_d.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                Log.i(LogConstants.Live_Gamification,"(int) poll.getId()"+(int) poll.getId());
                poll_view.setId((int) poll.getId());

                polls_holder.addView(poll_view, 0);

                if(isFocusOnLeftSide) {
                    //do not set focus to new question if user is on right
                    answer_a.setFocusable(true);
                    answer_a.setFocusableInTouchMode(true);///add this line
                    answer_a.requestFocus();
                }

                poll_view.startAnimation(slideInFromLeft);
                View space = new View(callingActivity);
                LinearLayout.LayoutParams spaceLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // width
                        10// height,
                );

                space.setLayoutParams(spaceLayoutParams);
                polls_holder.addView(space, 0);
                mediaPlayer = MediaPlayer.create(callingActivity.getApplicationContext(), R.raw.new_game_started_sound);
                mediaPlayer.start();

                answer_a.setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_UP) {
                            int currentIndex = polls_holder.indexOfChild(poll_view);
                            if (currentIndex > 0) {
                                View previousView = polls_holder.getChildAt(currentIndex-2);
                                if (previousView != null) {
                                    TextView previousAnswer = (TextView) previousView.findViewById(R.id.poll_answer_d);
                                    if (previousAnswer != null) {
                                        // Do something with the previous view
                                        polls_holder_scroll_container.smoothScrollBy(0, -previousView.getHeight());
                                        previousAnswer.setFocusable(true);
                                        previousAnswer.requestFocus();
                                    }
                                }
                            }
                            return true;
                        }
                        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.getAction() == KeyEvent.ACTION_UP) {
                            View childView = polls_to_resolve_holder.getChildAt(1);
                            if(childView!=null) {
                                TextView poll_to_resolve_selected_answer = childView.findViewById(R.id.poll_to_resolve_selected_answer);
                                Log.i(LogConstants.Live_Gamification, "poll_to_resolve_selected_answer" + poll_to_resolve_selected_answer.getText());
                                poll_to_resolve_selected_answer.setFocusable(true);
                                poll_to_resolve_selected_answer.requestFocus();
                                isFocusOnLeftSide=false;
                            }
                            }

                        return false;
                    }
                });

                //shifting focus to previous question
                answer_d.setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN) {
                            int currentIndex = polls_holder.indexOfChild(poll_view);
                            if (currentIndex > 0) {
                                View nextView = polls_holder.getChildAt(currentIndex+2);
                                if (nextView != null) {
                                    TextView previousAnswer = (TextView) nextView.findViewById(R.id.poll_answer_a);
                                    if (previousAnswer != null) {
                                        // Do something with the previous view
                                        polls_holder_scroll_container.smoothScrollBy(0, nextView.getHeight());
                                        previousAnswer.setFocusable(true);
                                        previousAnswer.requestFocus();
                                    }
                                }
                            }
                            return true;
                        }
                        return false;
                    }
                });

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //polls_holder.removeView(poll_view);
                        removePollFromPollList(poll_view.getId());
                    }
                }, 20000); // 20 seconds delay (in milliseconds)
            }
        });
    }

    public void removePollFromPollList(int id){
        polls_holder.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < polls_holder.getChildCount(); i++) {
                    View childView = polls_holder.getChildAt(i);
                    View spaceView = polls_holder.getChildAt(i+1);

                    if(childView.getId()==id){
                        polls_holder.removeView(childView);
                        polls_holder.removeView(spaceView);
                        edgeSdk.getLiveGamificationManager().removePollFromPollQuestionList(id);
                    }
                }
            }
        });
    }

    public int getViewIndex(int id){
        for (int i = 0; i < polls_holder.getChildCount(); i++) {
            View childView = polls_holder.getChildAt(i);
            if(childView.getId()==id){
                return i;
            }
        }
        return -1;
    }
    public void showAnimation(){
        rain();
    }

    public void explode() {
        EmitterConfig emitterConfig = new Emitter(100L, TimeUnit.MILLISECONDS).max(100);
        konfettiView.post(new Runnable() {
            @Override
            public void run() {
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
        }) ;
    }

    public void parade() {
        EmitterConfig emitterConfig = new Emitter(5, TimeUnit.SECONDS).perSecond(30);
        konfettiView.post(new Runnable() {
            @Override
            public void run() {

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
        }) ;
    }

    public void rain() {
        EmitterConfig emitterConfig = new Emitter(2, TimeUnit.SECONDS).perSecond(300);
       konfettiView.post(new Runnable() {
           @Override
           public void run() {
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
       });
    }

    public void addWINOrLooseMsgToResolveInList(String poll_question, String selectedAnswer,String coins,Poll_Question poll,String correct_answer,String type) {

        View poll_view = callingActivity.getLayoutInflater().inflate( type=="win" ? R.layout.poll_win_msg_card : R.layout.poll_loose_msg_card, null);
        TextView question = poll_view.findViewById(R.id.poll_to_resolve_question);
        TextView answer = poll_view.findViewById(R.id.poll_to_resolve_selected_answer);
        TextView wagered_coins = poll_view.findViewById(R.id.poll_to_resolve_wagered_coins);

        Animation slideInFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        slideInFromRight.setDuration(1000);

        polls_to_resolve_holder.post(new Runnable() {
            @Override
            public void run() {
                question.setText(poll_question);
                answer.setText((selectedAnswer));
                wagered_coins.setText(coins + " coins wagered");
                if(type=="loose"){
                    ( (TextView) poll_view.findViewById(R.id.correct_msg)).setText(correct_answer);
                }
                // Get the width of the TextView container
                int containerWidth = question.getWidth();

                // Get the total text length of the question and answers
                int totalTextLength = poll_question.length();

                // Calculate the average text length per character
                float averageTextLengthPerChar = (float) totalTextLength / (float) (poll_question.length());

                // Calculate the font size based on the container width and average text length per character
                float fontSize = containerWidth / (averageTextLengthPerChar * 1.5f);

                // Set the maximum and minimum font sizes
                float maxFontSize = 18f;
                float minFontSize = 12f;

                // If the calculated font size is greater than the maximum font size, set it to the maximum font size
                if (fontSize > maxFontSize) {
                    fontSize = maxFontSize;
                }

                // If the calculated font size is less than the minimum font size, set it to the minimum font size
                if (fontSize < minFontSize) {
                    fontSize = minFontSize;
                }

                // Set the font size of the TextViews
                question.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                poll_view.setId((int) poll.getId());
                polls_to_resolve_holder.addView(poll_view, 0);
                poll_view.startAnimation(slideInFromRight);

                View space = new View(callingActivity);
                LinearLayout.LayoutParams spaceLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // width
                        10// height,
                );

                space.setLayoutParams(spaceLayoutParams);
                polls_to_resolve_holder.addView(space, 0);
                Poll_to_be_resolved poll_to_be_resolved = new Poll_to_be_resolved();
                poll_to_be_resolved.setSelectedAnswer(selectedAnswer);
                poll_to_be_resolved.setId(poll.getId());
                poll_to_be_resolved.setWagered_coins(Integer.parseInt(coins));
                poll_to_be_resolved.setPoll_question(poll);
                listOfWageredPolls.put(poll.getId() + "", poll_to_be_resolved);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        polls_to_resolve_holder.removeView(poll_view);
                        polls_to_resolve_holder.removeView(space);

                    }
                }, 5000);
            }
        });
    }

    public void addCorrectOrWrongMsgToResolveInList(String poll_question, String selectedAnswer,String coins,Poll_Question poll,String type) {

        View poll_view = callingActivity.getLayoutInflater().inflate( type=="correct" ? R.layout.poll_correct_msg_card : R.layout.poll_wrong_msg_card, null);
        TextView question = poll_view.findViewById(R.id.poll_to_resolve_question);
        TextView answer = poll_view.findViewById(R.id.poll_to_resolve_selected_answer);
        TextView wagered_coins = poll_view.findViewById(R.id.poll_to_resolve_wagered_coins);
        TextView correct_wrong_message = poll_view.findViewById(type=="correct" ? R.id.correct_msg : R.id.wrong_msg );
        Animation slideInFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        slideInFromRight.setDuration(1000);

        polls_to_resolve_holder.post(new Runnable() {
            @Override
            public void run() {
                question.setText(poll_question);
                answer.setText((selectedAnswer));
                wagered_coins.setText(coins + " coins wagered");
                correct_wrong_message.setText(type=="correct" ? "Correct" : poll.getChoices()[poll.getCorrect()[0]-2]);
                // Get the width of the TextView container
                int containerWidth = question.getWidth();

                // Get the total text length of the question and answers
                int totalTextLength = poll_question.length();

                // Calculate the average text length per character
                float averageTextLengthPerChar = (float) totalTextLength / (float) (poll_question.length());

                // Calculate the font size based on the container width and average text length per character
                float fontSize = containerWidth / (averageTextLengthPerChar * 1.5f);

                // Set the maximum and minimum font sizes
                float maxFontSize = 18f;
                float minFontSize = 12f;

                // If the calculated font size is greater than the maximum font size, set it to the maximum font size
                if (fontSize > maxFontSize) {
                    fontSize = maxFontSize;
                }

                // If the calculated font size is less than the minimum font size, set it to the minimum font size
                if (fontSize < minFontSize) {
                    fontSize = minFontSize;
                }

                // Set the font size of the TextViews
                question.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                poll_view.setId((int) poll.getId());
                polls_to_resolve_holder.addView(poll_view, 0);
                poll_view.startAnimation(slideInFromRight);

                View space = new View(callingActivity);
                LinearLayout.LayoutParams spaceLayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // width
                        10// height,
                );

                space.setLayoutParams(spaceLayoutParams);
                polls_to_resolve_holder.addView(space, 0);
                Poll_to_be_resolved poll_to_be_resolved = new Poll_to_be_resolved();
                poll_to_be_resolved.setSelectedAnswer(selectedAnswer);
                poll_to_be_resolved.setId(poll.getId());
                poll_to_be_resolved.setWagered_coins(Integer.parseInt(coins));
                poll_to_be_resolved.setPoll_question(poll);
                listOfWageredPolls.put(poll.getId() + "", poll_to_be_resolved);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        polls_to_resolve_holder.removeView(poll_view);
                        polls_to_resolve_holder.removeView(space);
                    }
                }, 5000);
            }
        });
    }

    public void addPollToResolveInList(String poll_question, String selectedAnswer,String coins,Poll_Question poll) {

        int poll_mode = poll.getMode();
        Log.i(LogConstants.Live_Gamification,"poll_mode: "+poll_mode);
        if(poll_mode==2 || poll_mode==3){
            Log.i(LogConstants.Live_Gamification,"Its MCQs question");

            //it means don't need to wait for poll resolutions
            String[] all_answers = poll.getChoices();
            int correctAnswerIndex = poll.getCorrect()[0];
            Log.i(LogConstants.Live_Gamification,correctAnswerIndex+"");
            for(int i=0;i<poll.getCorrect().length;i++){
                Log.i(LogConstants.Live_Gamification,poll.getCorrect()[i]+" array value");
            }

            String correctAnswer = all_answers[correctAnswerIndex-2];

            Log.i(LogConstants.Live_Gamification,"correctAnswer:"+correctAnswer);
            Log.i(LogConstants.Live_Gamification,"selectedAnswer:"+selectedAnswer);
            int selectedAnswerIndex=getAnswerIndex(selectedAnswer,poll.getChoices());
            Log.i(LogConstants.Live_Gamification,"selectedAnswerIndex : "+selectedAnswerIndex);
            edgeSdk.getLiveGamificationManager().sendAnswerToSocketServer( poll.getId(),selectedAnswerIndex,5);
            if(selectedAnswer.equals(correctAnswer)){
                //correct answer
                mediaPlayer = MediaPlayer.create(callingActivity.getApplicationContext(), R.raw.success_sound);
                mediaPlayer.start();
                addCorrectOrWrongMsgToResolveInList(poll_question,selectedAnswer,coins,poll,"correct");
                updatePointsNumber(Float.parseFloat(coins+""));
                rain();
            }else{
                addCorrectOrWrongMsgToResolveInList(poll_question,selectedAnswer,coins,poll,"wrong");
                mediaPlayer = MediaPlayer.create(callingActivity.getApplicationContext(), R.raw.loose_sound);
                mediaPlayer.start();
            }

            removePollFromPollList((int) poll.getId());

        }else {
            Log.i(LogConstants.Live_Gamification,"Its waiting question");
            //add in waiting list.
            View poll_view = callingActivity.getLayoutInflater().inflate(R.layout.poll_to_resolve, null);
            TextView question = poll_view.findViewById(R.id.poll_to_resolve_question);
            TextView answer = poll_view.findViewById(R.id.poll_to_resolve_selected_answer);
            TextView wagered_coins = poll_view.findViewById(R.id.poll_to_resolve_wagered_coins);

            Animation slideInFromRight = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                    Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
            slideInFromRight.setDuration(1000);

            polls_to_resolve_holder.post(new Runnable() {
                @Override
                public void run() {
                    question.setText(poll_question);
                    answer.setText((selectedAnswer));
                    wagered_coins.setText(coins + " coins wagered");
                    // Get the width of the TextView container
                    int containerWidth = question.getWidth();

                    // Get the total text length of the question and answers
                    int totalTextLength = poll_question.length();

                    // Calculate the average text length per character
                    float averageTextLengthPerChar = (float) totalTextLength / (float) (poll_question.length());

                    // Calculate the font size based on the container width and average text length per character
                    float fontSize = containerWidth / (averageTextLengthPerChar * 1.5f);

                    // Set the maximum and minimum font sizes
                    float maxFontSize = 18f;
                    float minFontSize = 12f;

                    // If the calculated font size is greater than the maximum font size, set it to the maximum font size
                    if (fontSize > maxFontSize) {
                        fontSize = maxFontSize;
                    }

                    // If the calculated font size is less than the minimum font size, set it to the minimum font size
                    if (fontSize < minFontSize) {
                        fontSize = minFontSize;
                    }

                    // Set the font size of the TextViews
                    question.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
                    poll_view.setId((int) poll.getId());
                    polls_to_resolve_holder.addView(poll_view, 0);
                    poll_view.startAnimation(slideInFromRight);

                    View space = new View(callingActivity);
                    LinearLayout.LayoutParams spaceLayoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, // width
                            10// height,
                    );

                    space.setLayoutParams(spaceLayoutParams);
                    polls_to_resolve_holder.addView(space, 0);
                    Poll_to_be_resolved poll_to_be_resolved = new Poll_to_be_resolved();
                    poll_to_be_resolved.setSelectedAnswer(selectedAnswer);
                    poll_to_be_resolved.setId(poll.getId());
                    poll_to_be_resolved.setWagered_coins(Integer.parseInt(coins));
                    poll_to_be_resolved.setPoll_question(poll);
                    listOfWageredPolls.put(poll.getId() + "", poll_to_be_resolved);
                    removePollFromPollList((int) poll.getId());
                }
            });
        }
    }

    public void  removePollFromListOfPendingPolls(int id){
        polls_to_resolve_holder.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < polls_to_resolve_holder.getChildCount(); i++) {
                    View childView = polls_to_resolve_holder.getChildAt(i);

                    Log.i("childView",id+"-"+childView.getId());
                    if(childView.getId()==id){
                        polls_to_resolve_holder.removeView(childView);
                        edgeSdk.getLiveGamificationManager().removePollFromPollAnswerList(id);
                    }
                }
            }
        });
    }

    public void displayPollMessage(String message) {

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1000);

        Animation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        fadeOut.setDuration(1000);
        fadeOut.setStartOffset(3000);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Start your animation here
                number_of_won_coins.setText(message);
                win_message_layout.setVisibility(View.VISIBLE);
                win_message_layout.startAnimation(fadeIn);
            }
        });

        Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                win_message_layout.startAnimation(fadeOut);
                win_message_layout.setVisibility(View.GONE);
            }
        }, 3000);
    }

    public void hideQRCodeForGamification(){
        qrCodeViewhandler.post(new Runnable() {
            @Override
            public void run() {
                gamificationQRCode.setVisibility(GONE);
            }
        });
    }

    public void displayQRCodeForGamification(){
        Bitmap qrCode = this.edgeSdk.getQRCodeManager().getGamifiedTvQRCode();
        qrCodeViewhandler.post(new Runnable() {
            @Override
            public void run() {
                gamificationQRCode.setVisibility(VISIBLE);
            }
        });
    }

    public void makeGamificationLayoutVisible(int duration){
        // Calculate the height of the LinearLayout
        int targetHeight = gamificationStatusLayout.getHeight();
        // Create a TranslateAnimation that translates the LinearLayout from top to bottom
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -targetHeight, 0);
        translateAnimation.setDuration(duration); // Set the duration of the animation in milliseconds
        // Set an AnimationListener to listen for animation events
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation start event
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation end event
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Animation repeat event
            }
        });

        // Start the animation on the LinearLayout
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // UI-related code to be executed on the main UI thread
                gamificationStatusLayout.setVisibility(View.VISIBLE);
                gamificationStatusLayout.startAnimation(translateAnimation);
            }
        });
    }

    public void hideGamificationLayoutVisible(){
        // Start the animation on the LinearLayout
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // UI-related code to be executed on the main UI thread
                gamificationStatusLayout.setVisibility(View.GONE);
            }
        });
    }

    class WatchToEarnTitleStatusPrinter extends  TimerTask{

        @Override
        public void run() {

            //setting-up default text size.
            txt_watch_to_earn_heading.post(new Runnable() {
                @Override
                public void run() {
                    txt_watch_to_earn_heading.setTextSize(getResources().getDimension(R.dimen.w2e_notification_area_font_size_style_1)); //default size.
                }
            });

            //setting up default value.
            if(!isBackpressed()) {
                if(isPlaying()){
                    try {
                        //case 1 Wallet not forwarded.
                        if(!edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_VIEWER_WALLET_ADDRESS_FORWARDED))
                        {
                            //wallet not forwarded and connected to w2e server and staking is working and staking is 0 and hrs remaking is null:
                            if(
                                    !tickerNotifications[currentNotificationMsgNumber].isTempWalletForwarded()
                                            && edgeSdk.isW2ESocketOpen()
                                            && edgeSdk.isStakingServerRunning()
                                            && edgeSdk.getStakingValueFetchingManager().getStkResults().getResumingStakingIn()==null
                                            && roundTwoDecimals(edgeSdk.getStakingValueFetchingManager().getStkResults().getStakingPercentage()).equals("0.00")
                                            && (
                                            currentNotificationMsgNumber==0
                                                    || currentNotificationMsgNumber==1
                                                    || currentNotificationMsgNumber==2
                                    )
                            )
                            {
                                //w2e : active
                                txt_watch_to_earn_heading.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(currentNotificationMsgNumber==1 && edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_BOOST_ENABLED)){
                                            txt_watch_to_earn_heading.setText(tickerNotifications[currentNotificationMsgNumber].getMessage()+" & Boosted");
                                        }else{
                                            txt_watch_to_earn_heading.setText(tickerNotifications[currentNotificationMsgNumber].getMessage());
                                        }
                                    }
                                });

                                isTickerVisibilityThreadRunning=false; //setting it up to false will allow to control visibility with remote
                                try {Thread.sleep(tickerNotifications[currentNotificationMsgNumber].getTimeForMessageToStay());} catch (Exception e) {}
                                currentNotificationMsgNumber++;

                            }
                            else if(
                                    !tickerNotifications[currentNotificationMsgNumber].isTempWalletForwarded()
                                            && edgeSdk.isW2ESocketOpen()
                                            && !edgeSdk.isStakingServerRunning()
                                            && edgeSdk.getStakingValueFetchingManager().getStkResults().getResumingStakingIn()==null
                                            && roundTwoDecimals(edgeSdk.getStakingValueFetchingManager().getStkResults().getStakingPercentage()).equals("0.00")
                                            && (
                                            currentNotificationMsgNumber==0
                                                    || currentNotificationMsgNumber==1
                                                    || currentNotificationMsgNumber==3
                                    )
                            ){

                                txt_watch_to_earn_heading.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        txt_watch_to_earn_heading.setText(tickerNotifications[currentNotificationMsgNumber].getMessage());
                                    }
                                });

                                isTickerVisibilityThreadRunning=false; //setting it up to false will allow to control visibility with remote
                                try {Thread.sleep(tickerNotifications[currentNotificationMsgNumber].getTimeForMessageToStay());} catch (Exception e) {}
                                currentNotificationMsgNumber++;

                            }else{
                                if(currentNotificationMsgNumber>0)currentNotificationMsgNumber++;
                            }

                        }else{
                            //wallet forwarded
                            if(edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_VIEWER_WALLET_ADDRESS_FORWARDED))
                            {
                                if(
                                        tickerNotifications[currentNotificationMsgNumber].isTempWalletForwarded()
                                                && edgeSdk.isW2ESocketOpen()
                                                && edgeSdk.isStakingServerRunning()
                                                && edgeSdk.getStakingValueFetchingManager().getStkResults().getResumingStakingIn()==null
                                                && (
                                                currentNotificationMsgNumber==3
                                                        || currentNotificationMsgNumber==4
                                        )
                                )
                                {
                                    txt_watch_to_earn_heading.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(currentNotificationMsgNumber==3 && edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_BOOST_ENABLED)){
                                                txt_watch_to_earn_heading.setText(tickerNotifications[currentNotificationMsgNumber].getMessage()+" & Boosted");
                                            }else{
                                                txt_watch_to_earn_heading.setText(tickerNotifications[currentNotificationMsgNumber].getMessage());
                                            }
                                        }
                                    });

                                    isTickerVisibilityThreadRunning=false; //setting it up to false will allow to control visibility with remote
                                    try {Thread.sleep(tickerNotifications[currentNotificationMsgNumber].getTimeForMessageToStay());} catch (Exception e) {}
                                    currentNotificationMsgNumber++;
                                }
                                else if(
                                        tickerNotifications[currentNotificationMsgNumber].isTempWalletForwarded()
                                                && edgeSdk.isW2ESocketOpen()
                                                && edgeSdk.isStakingServerRunning()
                                                && edgeSdk.getStakingValueFetchingManager().getStkResults().getResumingStakingIn()!=null
                                                && (
                                                currentNotificationMsgNumber==3
                                                        || currentNotificationMsgNumber==5
                                        )
                                )
                                {
                                    Log.i("debug_ticker_visibility","case 3 detected");
                                    if(currentNotificationMsgNumber!=5) {
                                        txt_watch_to_earn_heading.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                txt_watch_to_earn_heading.setText(tickerNotifications[currentNotificationMsgNumber].getMessage());
                                            }
                                        });

                                    }else if(currentNotificationMsgNumber==5){
                                        // xx days.
                                        txt_watch_to_earn_heading.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                String remTime = edgeSdk.getStakingValueFetchingManager().getStkResults().getResumingStakingIn();
                                                txt_watch_to_earn_heading.setText(tickerNotifications[currentNotificationMsgNumber].getMessage().replace("xx",remTime));
                                            }
                                        });
                                    }

                                    Log.i("debug_ticker_visibility","wait for : "+tickerNotifications[currentNotificationMsgNumber].getTimeForTickerToAppear());
                                    isTickerVisibilityThreadRunning=false; //setting it up to false will allow to control visibility with remote
                                    try {Thread.sleep(tickerNotifications[currentNotificationMsgNumber].getTimeForMessageToStay());} catch (Exception e) {}
                                    Log.i("debug_ticker_visibility","waited for: "+tickerNotifications[currentNotificationMsgNumber].getTimeForTickerToAppear());
                                    currentNotificationMsgNumber++;

                                }
                                else{
                                    //TODO:Cause problem in notification
                                    currentNotificationMsgNumber++;
                                }
                            }
                        }

                        watch_to_earn_title_updater_timer.schedule(new WatchToEarnTitleStatusPrinter(), 10);

                    }   catch (IllegalStateException e) {
                        watch_to_earn_title_updater_timer = new Timer();
                        watch_to_earn_title_updater_timer.schedule(new WatchToEarnTitleStatusPrinter(), 10);
                        currentNotificationMsgNumber=0;
                    } catch (ArrayIndexOutOfBoundsException e){
                        Log.i("debug_ticker_visibility","printed all messages");
                        isTickerVisibilityThreadRunning=false;
                        currentNotificationMsgNumber=0;
                        watch_to_earn_title_updater_timer = new Timer();
                        watch_to_earn_title_updater_timer.schedule(new WatchToEarnTitleStatusPrinter(), 10);
                    }
                }
            }else{
                Log.i("debug_ticker_visibility","waiting for video to play.");
                watch_to_earn_title_updater_timer.schedule(new WatchToEarnTitleStatusPrinter(), 100);
            }

        }
    }

    class StakedValuesPrinter extends TimerTask {
        @SuppressLint("LongLogTag")
        public void run() {
            txt_staked.post(new Runnable() {
                @Override
                public void run() {
                    if(edgeSdk.getStakingValueFetchingManager().getStkResults()!=null){
                        String value = roundTwoDecimals(edgeSdk.getStakingValueFetchingManager().getStkResults().getStakingPercentage());
                        if(value.equals("0.00")){
                            txt_staked.setText("-.-- %");
                        }else {
                            txt_staked.setText(value + "%");
                        }
                    }else{
                        txt_staked.setText("-.-- %");
                    }
                }
            });

            try{
                staked_values_timer.schedule(new StakedValuesPrinter(), 10);
            }catch (IllegalStateException e){
                staked_values_timer = new Timer();
                staked_values_timer.schedule(new StakedValuesPrinter(), 10);
            }
        }
    }

    class Poll_List_Moderator extends TimerTask {
        @SuppressLint("LongLogTag")
        public void run() {
            //Now we need to first check if game is going on or not ?
            //then get the list of poll questions and print lastly added element from map.
            if(edgeSdk.getLiveGamificationManager().isIsGameGoingOn()){
                Map.Entry<String, Poll_Question> lastEntry = null;
                Map<String,Poll_Question> poll_questions_list = edgeSdk.getLiveGamificationManager().getPollQuestionList();
                for (Map.Entry<String, Poll_Question> entry : poll_questions_list.entrySet()) {
                    Log.i(LogConstants.Live_Gamification,"Poll list items : "+entry.getValue().getId());
                    lastEntry = entry;
                }

                //assert lastEntry != null;
                //Log.i(LogConstants.Live_Gamification,"Last item : "+lastEntry.getValue().getId());
                // Print the key and value of the last entry
                if (lastEntry != null) {
                    long current_poll_id = poll_questions_list.get(lastEntry.getKey()).getId();
                    Log.i(LogConstants.Live_Gamification,"Current Poll id :"+current_poll_id);
                    Log.i(LogConstants.Live_Gamification,"Last Poll id :"+last_poll_displayed_id);
                    if(current_poll_id!=last_poll_displayed_id){
                        last_poll_displayed_id=current_poll_id;
                        String question = poll_questions_list.get(poll_questions_list.get(lastEntry.getKey()).getId()+"").getPoll();
                        String answer_a = poll_questions_list.get(poll_questions_list.get(lastEntry.getKey()).getId()+"").getChoices()[0];
                        String answer_b = poll_questions_list.get(poll_questions_list.get(lastEntry.getKey()).getId()+"").getChoices()[1];
                        String answer_c = poll_questions_list.get(poll_questions_list.get(lastEntry.getKey()).getId()+"").getChoices()[2];
                        String answer_d = poll_questions_list.get(poll_questions_list.get(lastEntry.getKey()).getId()+"").getChoices()[3];
                        addPollInList(question,answer_a,answer_b,answer_c,answer_d,poll_questions_list.get(poll_questions_list.get(lastEntry.getKey()).getId()+""));
                    }

                    Log.i(LogConstants.Live_Gamification,"Current Poll Question :"+poll_questions_list.get(lastEntry.getKey()).getPoll());
                    //Log.i(LogConstants.Live_Gamification,"Last entry: " + lastEntry.getKey() + "=" + poll_questions_list.get(lastEntry.getKey()).getPoll());
                }

            }else{
                Log.i(LogConstants.Live_Gamification,"Game is not started yet");
            }
            try{
                poll_list_moderator_timer.schedule(new Poll_List_Moderator(), 5000);
            }catch (IllegalStateException e){
                poll_list_moderator_timer = new Timer();
                poll_list_moderator_timer.schedule(new Poll_List_Moderator(), 5000);
            }
        }
    }

    class Poll_To_Be_Resolve_List_Moderator extends TimerTask {
        @SuppressLint("LongLogTag")
        public void run() {
            //Now we need to first check if game is going on or not ?
            //then get the list of poll questions and print lastly added element from map.
            try {
                if (edgeSdk.getLiveGamificationManager().isIsGameGoingOn()) {
                    Log.i(LogConstants.Live_Gamification, "game is going on ,");
                    Map<String, Poll_Answer> pollAnswerList = edgeSdk.getLiveGamificationManager().getPollAnswerList();
                    for (Map.Entry<String, Poll_Answer> answer : pollAnswerList.entrySet()) {
                        Log.i(LogConstants.Live_Gamification, "resolved answer :" + answer.getValue().getId());
                        for (Map.Entry<String, Poll_to_be_resolved> poll_to_be_resolved : listOfWageredPolls.entrySet()) {
                            Log.i(LogConstants.Live_Gamification, "pending answer :" + poll_to_be_resolved.getValue().getId());
                            if (answer.getValue().getId() == poll_to_be_resolved.getValue().getId()) {
                                Log.i(LogConstants.Live_Gamification, "Question resolved" + answer.getValue().getCorrect()[0]);
                                String correctAnswer = poll_to_be_resolved.getValue().getPoll_question().getChoices()[answer.getValue().getCorrect()[0]];
                                String selectedAnswer = poll_to_be_resolved.getValue().getSelectedAnswer();
                                Log.i(LogConstants.Live_Gamification, "selectedAnswer" + selectedAnswer);
                                Log.i(LogConstants.Live_Gamification, "correctAnswer" + correctAnswer);
                                int selectedAnswerIndex=getAnswerIndex(selectedAnswer,poll_to_be_resolved.getValue().getPoll_question().getChoices());
                                Log.i(LogConstants.Live_Gamification,"selectedAnswerIndex : "+selectedAnswerIndex);
                                edgeSdk.getLiveGamificationManager().sendAnswerToSocketServer((int) poll_to_be_resolved.getValue().getId(),selectedAnswerIndex,poll_to_be_resolved.getValue().getWagered_coins());
                                //won
                                if (correctAnswer.equals(selectedAnswer)) {
                                    //send message to
                                    mediaPlayer = MediaPlayer.create(callingActivity.getApplicationContext(), R.raw.success_sound);
                                    mediaPlayer.start();
                                    rain();
                                    removePollFromPollList((int) poll_to_be_resolved.getValue().getId());
                                    removePollFromListOfPendingPolls((int) poll_to_be_resolved.getValue().getId());
                                    addWINOrLooseMsgToResolveInList(poll_to_be_resolved.getValue().getPoll_question().getPoll(),poll_to_be_resolved.getValue().getSelectedAnswer(), String.valueOf(poll_to_be_resolved.getValue().getWagered_coins()),poll_to_be_resolved.getValue().getPoll_question(),null,"win");
                                    updatePointsNumber(Float.parseFloat(poll_to_be_resolved.getValue().getWagered_coins()+""));
                                }
                                //loosed
                                else {
                                    mediaPlayer = MediaPlayer.create(callingActivity.getApplicationContext(), R.raw.loose_sound);
                                    mediaPlayer.start();
                                    removePollFromPollList((int) poll_to_be_resolved.getValue().getId());
                                    removePollFromListOfPendingPolls((int) poll_to_be_resolved.getValue().getId());
                                    addWINOrLooseMsgToResolveInList(poll_to_be_resolved.getValue().getPoll_question().getPoll(),poll_to_be_resolved.getValue().getSelectedAnswer(), String.valueOf(poll_to_be_resolved.getValue().getWagered_coins()),poll_to_be_resolved.getValue().getPoll_question(),correctAnswer,"loose");
                                    updatePointsNumber(-(Float.parseFloat(poll_to_be_resolved.getValue().getWagered_coins()+"")));
                                }

                                listOfWageredPolls.remove(poll_to_be_resolved.getKey());
                            } else {
                                Log.i(LogConstants.Live_Gamification, "Not found");
                            }
                        }
                    }
                } else {
                    Log.i(LogConstants.Live_Gamification, "Game is not started yet");
                }

            }catch (Exception e){
                Log.i(LogConstants.Live_Gamification,"Error while moderating list of pending polls :"+ e);
                Log.i(LogConstants.Live_Gamification,"Error while moderating list of pending polls :"+e.getMessage());
            }

            try{
                poll_list_to_be_resolved_moderator_timer.schedule(new Poll_To_Be_Resolve_List_Moderator(), 5000);
            }catch (IllegalStateException e){
                poll_list_to_be_resolved_moderator_timer = new Timer();
                poll_list_to_be_resolved_moderator_timer.schedule(new Poll_To_Be_Resolve_List_Moderator(), 5000);
            }
        }
    }

    private int getAnswerIndex(String selectedAnswer,String choices[]){
        for(int i=0;i<4;i++){
            if(choices[i].equals(selectedAnswer)){
                return i;
            }
        }

        return -1;
    }

    class ESTApyValuesPrinter extends TimerTask {
        @SuppressLint("LongLogTag")
        public void run() {
            txt_est_apy.post(new Runnable() {
                @Override
                public void run() {
                    if(edgeSdk.getStakingValueFetchingManager().getStkResults()!=null) {
                        String value = roundTwoDecimals(edgeSdk.getStakingValueFetchingManager().getStkResults().getEstimatedApyPercentage());
                        if (value.equals("0.00")) {
                            txt_est_apy.setText("-.-- %");
                        } else {
                            txt_est_apy.setText(value + " %");
                        }
                    }else{
                        txt_est_apy.setText("-.-- %");
                    }
                }
            });
            try{
                est_apy_values_timer.schedule(new ESTApyValuesPrinter(), 10);
            }catch (IllegalStateException e){
                est_apy_values_timer = new Timer();
                est_apy_values_timer.schedule(new ESTApyValuesPrinter(), 10);
            }

        }
    }

    class EarningPerDayValuesPrinter extends TimerTask {
        @SuppressLint("LongLogTag")
        public void run() {
            try {

                float earning_per_hr = edgeSdk.getW2EarnManager().getResults().getEstimateEatsPerHour()*edgeSdk.getMarketPriceManager().getPrice();
                //Log.i(LogConstants.Watch_2_Earn,"earning_per_hr :"+earning_per_hr);
                float finalEarning_per_hr = earning_per_hr;

            }
            catch (Exception e){
                Log.e("error","error while printing total earning per day "+e.getMessage());
            }

            try {
                earning_per_day_timer.schedule(new EarningPerDayValuesPrinter(), 1000);
            }catch (IllegalStateException e){
                earning_per_day_timer = new Timer();
                earning_per_day_timer.schedule(new EarningPerDayValuesPrinter(), 1000);
            }
        }
    }

    class EatMarketPriceValuePrinter extends TimerTask {
        public void run() {

            float eat_market_inc_dec =edgeSdk.getMarketPriceManager().getChange();
            txt_title_eat_market_inc_dec.post(new Runnable() {
                @Override
                public void run() {

                    txt_title_eat_market_inc_dec.setText("+"+roundTwoDecimals(eat_market_inc_dec)+"%");
                    txt_title_eat_market_inc_dec.setTextColor(Color.parseColor("#11D70D"));
                    if(eat_market_inc_dec>0){
                        txt_title_eat_market_inc_dec.setText("+"+roundTwoDecimals(eat_market_inc_dec)+"%");
                        txt_title_eat_market_inc_dec.setTextColor(Color.parseColor("#11D70D"));
                    }else if(eat_market_inc_dec==0){
                        txt_title_eat_market_inc_dec.setText(roundTwoDecimals(eat_market_inc_dec)+"%");
                        txt_title_eat_market_inc_dec.setTextColor(Color.parseColor("#11D70D"));
                    }else {
                        txt_title_eat_market_inc_dec.setText(roundTwoDecimals(eat_market_inc_dec)+"%");
                        txt_title_eat_market_inc_dec.setTextColor(Color.RED);
                    }
                }
            });

            txt_eat_market_price.post(new Runnable() {
                @Override
                public void run() {
                    txt_eat_market_price.setText("$"+ roundFourDecimals(edgeSdk.getMarketPriceManager().getPrice()));
                }
            });

            try {
                eat_market_price_timer.schedule(new EatMarketPriceValuePrinter(), 1000);
            }catch (IllegalStateException e){
                eat_market_price_timer = new Timer();
                eat_market_price_timer.schedule(new EatMarketPriceValuePrinter(), 1000);
            }
        }
    }

    class TotalEatsValuePrinter extends TimerTask {
        public void run() {

            txt_total_eats.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        txt_total_eats.setText(roundTwoDecimals(edgeSdk.getW2EarnManager().getResults().getBalance()) + "");

                    }catch (Exception e){
                        Log.e("error","error while printing total eats"+e.getMessage());
                        txt_total_eats.post(new Runnable() {
                            @Override
                            public void run() {
                                txt_total_eats.setText("-.--");
                            }
                        });
                    }
                }
            });

            try {
                total_eats_timer.schedule(new TotalEatsValuePrinter(), 10);
            }catch (IllegalStateException e){
                total_eats_timer = new Timer();
                total_eats_timer.schedule(new TotalEatsValuePrinter(), 10);
            }
        }
    }

    class TickerValuePrinter extends TimerTask {
        public void run() {
            final String[] Balance =new String[1];
            try {
                Balance[0] = roundTwoDecimals(edgeSdk.getW2EarnManager().getResults().getBalance());

                if (Balance[0] == null) Balance[0] = "0.00";

                txt_balance.post(new Runnable() {
                    @Override
                    public void run() {
                        txt_balance.setText("$" + roundTwoDecimals(edgeSdk.getW2EarnManager().getResults().getEstimatedEarnedEatsInUSD()));
                    }
                });
            }catch (Exception e){
                Log.e(com.edgesdk.Utils.LogConstants.Watch_2_Earn,e.getMessage());
                txt_balance.post(new Runnable() {
                    @Override
                    public void run() {
                        txt_balance.setText("$-.--");
                    }
                });
                Log.e("error","Error while printing balance in usd "+e.getMessage());
            }

            txt_earned.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("txt_earned",edgeSdk.getW2EarnManager().getResults().getEstimateEatsPerHour()+"");
                    if(edgeSdk.getW2EarnManager().getResults().getEstimateEatsPerHour()!=0.0)
                        txt_earned.setText(roundThreeDecimals( edgeSdk.getW2EarnManager().getResults().getEstimateEatsPerHour()));
                    else txt_earned.setText(Constants.DEFAULT_VALUE_EAT_HR);
                }
            });

            txt_total_points.post(new Runnable() {
                @Override
                public void run() {
                    Log.i("txt_total_points",edgeSdk.getW2EarnManager().getResults().getPoints()+"");
                    if(edgeSdk.getW2EarnManager().getResults().getPoints()!=0.0)
                        txt_total_points.setText(roundThreeDecimals( edgeSdk.getW2EarnManager().getResults().getPoints()));
                    else txt_total_points.setText("0.00");
                }
            });

            try {
                ticker_values_timer.schedule(new TickerValuePrinter(), 1000);
            }catch (IllegalStateException e){
                ticker_values_timer = new Timer();
                ticker_values_timer.schedule(new TickerValuePrinter(), 1000);
            }
        }
    }

    public void updatePointsNumber(float points){

        callingActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final float startValue = edgeSdk.getW2EarnManager().getResults().getOffChainBalance();
                final float endValue = startValue + points;
                edgeSdk.getW2EarnManager().getResults().setPoints(points);

                // Define the colors for normal and changed states
                int normalColor = getResources().getColor(R.color.white);
                int changedColor = getResources().getColor(R.color.yellow);
                if(points<0){
                    changedColor = getResources().getColor(R.color.red);
                }
                // Create a ValueAnimator for the scale animation
                ValueAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(
                        txt_total_points,
                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.0f),
                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.2f)
                );
                scaleAnimator.setDuration(2000);
                scaleAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                scaleAnimator.setRepeatMode(ValueAnimator.REVERSE);
                scaleAnimator.setRepeatCount(1);

                // Create an ObjectAnimator for the color change animation
                ObjectAnimator colorAnimator = ObjectAnimator.ofArgb(txt_total_points, "textColor", normalColor, changedColor);
                colorAnimator.setDuration(2000);
                colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
                colorAnimator.setRepeatCount(1);

                // Create an AnimatorSet to play the animations together
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(scaleAnimator, colorAnimator);
                animatorSet.start();


                if (points>0) {
                    // Win animation
                    Animation winAnimation = AnimationUtils.loadAnimation(callingActivity, R.anim.win_animation);
                    txt_total_points.startAnimation(winAnimation);
                } else {
                    // Lose animation
                    Animation loseAnimation = AnimationUtils.loadAnimation(callingActivity, R.anim.win_animation);
                    txt_total_points.startAnimation(loseAnimation);
                }
                // Create a ValueAnimator for the counter effect
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(startValue, endValue);
                valueAnimator.setDuration(5000);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    DecimalFormat decimalFormat = new DecimalFormat("#0.000");
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float counterValue = (float) animation.getAnimatedValue();
                        String formattedValue = decimalFormat.format(counterValue);
                        txt_total_points.setText(formattedValue);

                        // Apply animation to the TextView

                    }
                });

                valueAnimator.start();
            }
        });


    }

    public String roundTwoDecimals(double d)
    {
        double prev = d;
        try{
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            double formatedNumber = Double.parseDouble(twoDForm.format(d));

            if(formatedNumber==0){
                return "0.00";
            }else if(String.valueOf(formatedNumber).contains("E-")){
                return "0.00";
            }
            //Log.i("debug_ticker_value_actual",formatedNumber+"" );
            String strNumber = String.valueOf(formatedNumber);
            int integerPart = Integer.parseInt(strNumber.substring(0,strNumber.indexOf(".")));
            String formateIntegerPart = NumberFormat.getNumberInstance(Locale.US).format(integerPart);
            if(strNumber.substring(strNumber.indexOf(".")+1,strNumber.length()).length()==1){
                //6.4
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+2)+"0";
            }
            if((strNumber.substring(strNumber.indexOf(".")+1,strNumber.indexOf(".")+2)).equals("0")  && (strNumber.substring(strNumber.indexOf(".")+1,strNumber.length())).length()==1){
                // Log.d("debug_ticker_value_formate","actual value is with one 0 after decimal");
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+2)+"0";
            }

            if(strNumber.substring(strNumber.indexOf(".")+1,strNumber.indexOf(".")+3).equals("00")){
                // Log.d("debug_ticker_value_formate","actual value is with two 0 after decimal");
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+2)+"0";
            }

            strNumber = String.valueOf(formatedNumber);
            integerPart = Integer.parseInt(strNumber.substring(0,strNumber.indexOf(".")));
            formateIntegerPart = NumberFormat.getNumberInstance(Locale.US).format(integerPart);
            //Log.d("debug_ticker_value_formate","actual value is with more than 2 digits after decimal but that is rounded to two");
            return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.length());

        }catch (Exception e){
            Log.e("error",e.toString());
            return null;
        }
    }
    @SuppressLint("LongLogTag")
    public String roundThreeDecimals(double d)
    {
        double prev = d;
        try{
            DecimalFormat twoDForm = new DecimalFormat("#.###");
            double formatedNumber = Double.parseDouble(twoDForm.format(d));

            if(formatedNumber==0){
                return "0.000";
            }else if(String.valueOf(formatedNumber).contains("E-")){
                return "0.000";
            }

            Log.i("debug_ticker_value_actual",d+" Actual" );
            Log.i("debug_ticker_value_actual",formatedNumber+" formated" );
            String strNumber = String.valueOf(formatedNumber);
            int integerPart = Integer.parseInt(strNumber.substring(0,strNumber.indexOf(".")));
            String formateIntegerPart = NumberFormat.getNumberInstance(Locale.US).format(integerPart);
            if(strNumber.substring(strNumber.indexOf(".")+1,strNumber.length()).length()==1){
                //0.1
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+2)+"00";
            }
            if(strNumber.substring(strNumber.indexOf(".")+1,strNumber.length()).length()==2){
                //0.12
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+3)+"0";
            }

            strNumber = String.valueOf(formatedNumber);
            integerPart = Integer.parseInt(strNumber.substring(0,strNumber.indexOf(".")));
            formateIntegerPart = NumberFormat.getNumberInstance(Locale.US).format(integerPart);
            String numberToPrint = formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.length());
            return numberToPrint;

        }catch (Exception e){
            Log.e("error",e.toString());
            return null;
        }
    }
    @SuppressLint("LongLogTag")
    public String roundFourDecimals(double d)
    {
        double prev = d;
        try{
            DecimalFormat twoDForm = new DecimalFormat("#.####");
            double formatedNumber = Double.parseDouble(twoDForm.format(d));

            if(formatedNumber==0){
                return "0.0000";
            }else if(String.valueOf(formatedNumber).contains("E-")){
                return "0.0000";
            }

            Log.i("debug_ticker_value_actual",d+" Actual" );
            Log.i("debug_ticker_value_actual",formatedNumber+" formated" );
            String strNumber = String.valueOf(formatedNumber);

            int integerPart = Integer.parseInt(strNumber.substring(0,strNumber.indexOf(".")));

            String formateIntegerPart = NumberFormat.getNumberInstance(Locale.US).format(integerPart);
            if(strNumber.substring(strNumber.indexOf(".")+1,strNumber.length()).length()==1){
                //0.1
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+2)+"000";
            }
            if(strNumber.substring(strNumber.indexOf(".")+1,strNumber.length()).length()==2){
                //0.12
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+3)+"00";
            }
            if(strNumber.substring(strNumber.indexOf(".")+1,strNumber.length()).length()==3){
                //0.121
                return formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.indexOf(".")+4)+"0";
            }

            strNumber = String.valueOf(formatedNumber);
            integerPart = Integer.parseInt(strNumber.substring(0,strNumber.indexOf(".")));
            formateIntegerPart = NumberFormat.getNumberInstance(Locale.US).format(integerPart);
            String numberToPrint = formateIntegerPart+strNumber.substring(strNumber.indexOf("."),strNumber.length());
            Log.i("debug_ticker_value_actual",numberToPrint+" numberToPrint" );

            return numberToPrint;

        }catch (Exception e){
            Log.e("error",e.toString());
            return null;
        }
    }

    public void StopValuesPrintingThreads(){
        isPrintingThreadsRunning=false;
        if(staked_values_timer!=null)
            staked_values_timer.cancel();
        if(est_apy_values_timer!=null)
            est_apy_values_timer.cancel();
        if(earning_per_day_timer!=null)
            earning_per_day_timer.cancel();
        if(eat_market_price_timer!=null)
            eat_market_price_timer.cancel();
        if(ticker_values_timer!=null)
            ticker_values_timer.cancel();
        if(total_eats_timer!=null)
            total_eats_timer.cancel();
        if(watch_to_earn_title_updater_timer!=null)
            watch_to_earn_title_updater_timer.cancel();
        if(poll_list_moderator_timer!=null)
            poll_list_moderator_timer.cancel();
        if(poll_list_to_be_resolved_moderator_timer!=null)
            poll_list_to_be_resolved_moderator_timer.cancel();
    }

    public void PauseValuesPrintingThreads(){
        isPrintingThreadsRunning=false;
        if(staked_values_timer!=null)
            staked_values_timer.cancel();
        if(est_apy_values_timer!=null)
            est_apy_values_timer.cancel();
        if(earning_per_day_timer!=null)
            earning_per_day_timer.cancel();
        if(ticker_values_timer!=null)
            ticker_values_timer.cancel();
        if(total_eats_timer!=null)
            total_eats_timer.cancel();
        if(watch_to_earn_title_updater_timer!=null)
            watch_to_earn_title_updater_timer.cancel();
        if(poll_list_moderator_timer!=null)
            poll_list_moderator_timer.cancel();
        if(poll_list_to_be_resolved_moderator_timer!=null){
            poll_list_to_be_resolved_moderator_timer.cancel();
        }
    }

    public void ResumeValuesPrintingThreads(){
        isPrintingThreadsRunning=true;
        staked_values_timer = new Timer();
        staked_values_timer.schedule(new StakedValuesPrinter(),1000);

        est_apy_values_timer = new Timer();
        est_apy_values_timer.schedule(new ESTApyValuesPrinter(),1000);

        earning_per_day_timer = new Timer();
        //earning_per_day_timer.schedule(new EarningPerDayValuesPrinter(),1000);

        eat_market_price_timer = new Timer();
        eat_market_price_timer.schedule(new EatMarketPriceValuePrinter(),1000);

        total_eats_timer = new Timer();
        total_eats_timer.schedule(new TotalEatsValuePrinter(),0);

        ticker_values_timer = new Timer();
        ticker_values_timer.schedule(new TickerValuePrinter(),1000);

        watch_to_earn_title_updater_timer = new Timer();
        watch_to_earn_title_updater_timer.schedule(new WatchToEarnTitleStatusPrinter(),1000);

        poll_list_moderator_timer = new Timer();
        poll_list_moderator_timer.schedule(new Poll_List_Moderator(),4000);

        poll_list_to_be_resolved_moderator_timer = new Timer();
        poll_list_to_be_resolved_moderator_timer.schedule(new Poll_To_Be_Resolve_List_Moderator(),4000);

    }

    public void onPause(){
        boolean isOptOutEnabled = this.edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_OPT_OUT_W2E_ENABLED);
        if(!isOptOutEnabled) {
            StopValuesPrintingThreads();
            //StopControllerAndVideoStatusDetectorThread();
            if(edgeSdk.isW2ESocketOpen())
                edgeSdk.pauseW2E();
            edgeSdk.stopStaking();
            //StopSecondScreenCommandListenerThread();
        }
    }
    public void onResume(){
        boolean isOptOutEnabled = this.edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_OPT_OUT_W2E_ENABLED);
        if(!isOptOutEnabled) {
            //HomeActivity.edgeSdkExecutor.startStaking();
            edgeSdk.startStaking();
            ResumeValuesPrintingThreads();
            //ResumeControllerAndVideoStatusDetectorThread();
            //StartSecondScreenCommandListenerThread();
        }
    }

    public void onStop(){
        boolean isOptOutEnabled = this.edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_OPT_OUT_W2E_ENABLED);
        if(!isOptOutEnabled) {
            StopValuesPrintingThreads();
            //StopControllerAndVideoStatusDetectorThread();
            //HomeActivity.edgeSdkExecutor.pauseWatchToEarn();
            //HomeActivity.edgeSdkExecutor.stopStaking();
            if(edgeSdk.isW2ESocketOpen())
                edgeSdk.pauseW2E();
            edgeSdk.stopStaking();
            //StopSecondScreenCommandListenerThread();
        }
    }

    public void onBackPressed(){
        boolean isOptOutEnabled = this.edgeSdk.getLocalStorageManager().getBooleanValue(Constants.IS_OPT_OUT_W2E_ENABLED);
        if(!isOptOutEnabled) {
            StopValuesPrintingThreads();
            //StopControllerAndVideoStatusDetectorThread();
            //HomeActivity.edgeSdkExecutor.pauseWatchToEarn();
            //HomeActivity.edgeSdkExecutor.stopStaking();
            if(edgeSdk.isW2ESocketOpen())
                edgeSdk.pauseW2E();
            edgeSdk.stopStaking();
            //StopSecondScreenCommandListenerThread();
        }
    }
}