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
        private EditText firstNameEditText, lastNameEditText, emailEditText;
        private Button signInButton;

        public Ticker(Context context) {
            super(context);
        }

        @SuppressLint("MissingInflatedId")
        private void init() {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.ticker_layout, this);

            firstNameEditText = view.findViewById(R.id.first_name);
            lastNameEditText = view.findViewById(R.id.last_name);
            emailEditText = view.findViewById(R.id.email);
            signInButton = view.findViewById(R.id.sign_in_button);
            signInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(),"FName :"+getFirstName()+" LName :"+getLastName()+" and Email :"+getEmail(),Toast.LENGTH_SHORT).show();
                }
            });
        }

        public String getFirstName() {
            return firstNameEditText.getText().toString();
        }

        public String getLastName() {
            return lastNameEditText.getText().toString();
        }

        public String getEmail() {
            return emailEditText.getText().toString();
        }
    }


