package com.edgesdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.edgesdk.Utils.Constants;
import com.edgesdk.Utils.LogConstants;
import com.edgesdk.Utils.Messages;
import com.edgesdk.models.TickerNotifications;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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
    Timer staked_values_timer,est_apy_values_timer,earning_per_day_timer,eat_market_price_timer,ticker_values_timer,is_video_playing_or_paused_detector_timer,total_eats_timer,watch_to_earn_title_updater_timer,second_secreen_command_listener,ticker_visibility_controler_timer;
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

        txt_today = view.findViewById(R.id.txt_today);
        txt_watch_to_earn_heading= view.findViewById(R.id.txt_watch_to_earn_heading);
        txt_title_total_eats = view.findViewById(R.id.txt_title_total_eats);
        txt_title_eat_market_price = view.findViewById(R.id.txt_title_eat_market_price);
        txt_title_eat_market_inc_dec = view.findViewById(R.id.txt_eat_market_inc_dec);
        txt_title_balance = view.findViewById(R.id.txt_title_balance);
        txt_title_staked = view.findViewById(R.id.txt_title_staked);
        txt_title_est_apy = view.findViewById(R.id.txt_title_est_apy);
        txt_title_earned = view.findViewById(R.id.txt_title_earned);
        //txt_title_per_day = view.findViewById(R.id.txt_title_per_day);

        ticker_layout = findViewById(R.id.ticker_layout);

        DisplayMetrics metrics = new DisplayMetrics();
        callingActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int density = metrics.densityDpi;
        int screenWidthPx = metrics.widthPixels;
        Log.i("Density", "screenWidthPx"+screenWidthPx);
        ticker_layout.getLayoutParams().width = screenWidthPx;

        custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/proxima_nova_regular.ttf");
        isPrintingThreadsRunning=false;
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

        }
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
//                txt_per_day.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(finalEarning_per_hr !=0.0)
//                            txt_per_day.setText("$"+roundThreeDecimals(finalEarning_per_hr));
//                        else txt_per_day.setText("$"+Constants.DEFAULT_VALUE_EAT_HR);
//                    }
//                });

            }
            catch (Exception e){
                Log.e("error","error while printing total earning per day "+e.getMessage());
//                txt_per_day.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        //txt_per_day.setText("-.--");
//                    }
//                });
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