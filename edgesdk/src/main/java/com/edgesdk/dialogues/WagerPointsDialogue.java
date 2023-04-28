package com.edgesdk.dialogues;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.edgesdk.R;

public class WagerPointsDialogue extends Dialog {
    public WagerPointsDialogue(@NonNull Context context,String question,String answer) {
        super(context);
        setContentView(R.layout.wager_dialogue);
        TextView question_view = findViewById(R.id.current_question);
        TextView answer_view = findViewById(R.id.current_answer);
        question_view.setText("Question : "+question);
        answer_view.setText("Selected answer : "+answer);

    }
}
