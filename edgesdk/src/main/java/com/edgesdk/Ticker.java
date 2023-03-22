package com.edgesdk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Ticker extends LinearLayout {

        public Ticker(Context context) {
            super(context);
            init();
        }

        @SuppressLint("MissingInflatedId")
        private void init() {
            System.out.println("init-method-called");
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.ticker_layout, this);
            //firstNameEditText = view.findViewById(R.id.first_name);
        }


    }


