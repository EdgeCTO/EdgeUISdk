package com.edgesdk.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.edgesdk.R;
import com.edgesdk.Ticker;
import com.edgesdk.models.Poll_Question;

public class WagerPointsDialogue extends Dialog {
    Ticker ticker;
    Poll_Question poll;
    public WagerPointsDialogue(@NonNull Context context, String question, String answer, Ticker ticker, Poll_Question poll) {
        super(context);
        this.ticker=ticker;
        setContentView(R.layout.wager_dialogue);
        TextView question_view = findViewById(R.id.current_question);
        TextView answer_view = findViewById(R.id.current_answer);
        TextView btn_wager = findViewById(R.id.btn_wager);
        EditText wagered_coins = findViewById(R.id.wagered_coins);
        question_view.setText("Question : "+question);
        answer_view.setText("Selected answer : "+answer);
        this.poll=poll;
        btn_wager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticker.addPollToResolveInList(question,answer,wagered_coins.getText().toString(),poll);
                dismiss();
            }
        });
    }
}
