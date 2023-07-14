package com.edgesdk;


import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class TypeWriterView extends androidx.appcompat.widget.AppCompatTextView {

    private CharSequence myText;
    private int myIndex;
    private long myDelay=150;

    public TypeWriterView(Context context) {
        super(context);
    }

    public TypeWriterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler myHandler = new Handler();

    private Runnable characterAdapter = new Runnable() {
        @Override
        public void run() {
            setText(myText.subSequence(0,myIndex++));
            if(myIndex<=myText.length()){
                myHandler.postDelayed(characterAdapter,myDelay);
            }
        }
    };

    public void  animateText(CharSequence myText){
        this.myText=myText;
        myIndex=0;
        setText("");
        myHandler.removeCallbacks(characterAdapter);
        myHandler.postDelayed(characterAdapter,myDelay);
    }
    public void setCharacterDelay(long m){
        myDelay=m;
    }
}
