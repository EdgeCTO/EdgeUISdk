package com.edgesdk.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.edgesdk.R;

public class WagerPointsDialogue extends Dialog {
    public WagerPointsDialogue(@NonNull Context context,String question,String answer) {
        super(context);
        setContentView(R.layout.wager_dialogue);
        TextView question_view = findViewById(R.id.current_question);
        TextView answer_view = findViewById(R.id.current_answer);
        TextView btn_wager = findViewById(R.id.btn_wager);
        question_view.setText("Question : "+question);
        answer_view.setText("Selected answer : "+answer);
        btn_wager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
