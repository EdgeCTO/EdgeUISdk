package com.edgesdk.dialogues;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.edgesdk.R;

public class WagerPointsDialogue extends Dialog {
    public WagerPointsDialogue(@NonNull Context context) {
        super(context);
        setContentView(R.layout.wager_dialogue);
    }
}
