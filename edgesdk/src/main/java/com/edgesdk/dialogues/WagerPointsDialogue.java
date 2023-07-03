package com.edgesdk.dialogues;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.edgesdk.EdgeSdk;
import com.edgesdk.R;
import com.edgesdk.Ticker;
import com.edgesdk.Utils.LogConstants;
import com.edgesdk.models.Poll_Question;

public class WagerPointsDialogue extends Dialog {
    Ticker ticker;
    Poll_Question poll;
    EdgeSdk edgeSdk;
    public WagerPointsDialogue(@NonNull Context context, String question, String answer, Ticker ticker, Poll_Question poll, EdgeSdk edgeSdk) {
        super(context);
        this.ticker=ticker;
        this.edgeSdk=edgeSdk;
        setContentView(R.layout.wager_dialogue);
        TextView question_view = findViewById(R.id.current_question);
        TextView answer_view = findViewById(R.id.current_answer);
        TextView btn_wager = findViewById(R.id.btn_wager);
        @SuppressLint("MissingInflatedId") TextView txt_error = findViewById(R.id.txt_error);
        EditText wagered_coins = findViewById(R.id.wagered_coins);
        question_view.setText("Question : "+question);
        answer_view.setText("Selected answer : "+answer);
        this.poll=poll;
        wagered_coins.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_error.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_wager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float total_points =  edgeSdk.getW2EarnManager().getResults().getPoints();
                float wagered_points = Float.parseFloat(wagered_coins.getText().toString());
                Log.i(LogConstants.Live_Gamification,"total_points"+total_points);
                Log.i(LogConstants.Live_Gamification,"wagered_points"+wagered_points);
                if(total_points>0.0) {
//                    if (total_points > wagered_points) {
                    //TODO:revert it
                    if (true) {

                        txt_error.setVisibility(View.GONE);
                        ticker.addPollToResolveInList(question, answer, wagered_coins.getText().toString(), poll);
                        dismiss();
                    } else {
                        //display error message on dialogue to show the wagered coins are more then your total amount
                        txt_error.setVisibility(View.VISIBLE);
                        txt_error.setText("Its more then your total points..!");
                    }
                }else{
                    txt_error.setVisibility(View.VISIBLE);
                    txt_error.setText("You can't wager due to 0 points in wallet");
                    btn_wager.setEnabled(false);
                }
            }
        });
    }
}
