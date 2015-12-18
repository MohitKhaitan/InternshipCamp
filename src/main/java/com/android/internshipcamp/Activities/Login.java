package com.android.internshipcamp.Activities;


import android.app.Dialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internshipcamp.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import static android.support.v7.app.AlertDialog.*;


public class Login extends AppCompatActivity {
    private Button login_button;
    private TextView help, hiText;
    EditText userEmail, userPass;
    String email, pass;
    CheckBox showPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setContentView(R.layout.activity_login);
        help = (TextView) findViewById(R.id.help_login);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Stop bluffing. You didn't.", Toast.LENGTH_SHORT).show();
            }
        });

        hiText = (TextView) findViewById(R.id.textHi);
        hiText.setTypeface(Typeface.createFromAsset(getAssets(), "ABeeZee-Regular.ttf"));

        userEmail = (EditText) findViewById(R.id.emailid_etext);
        userPass = (EditText) findViewById(R.id.password_etext);

        showPass = (CheckBox) findViewById(R.id.showPass);
        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    userPass.setTransformationMethod(new PasswordTransformationMethod());
                    userPass.setSelection(userPass.length());
                } else {
                    userPass.setTransformationMethod(null);
                    userPass.setSelection(userPass.length());
                }
            }
        });

        login_button = (Button) findViewById(R.id.button_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = userEmail.getText().toString();
                pass = userPass.getText().toString();

                if (email.isEmpty()) {
                    userEmail.setError("Email can't be empty!");
                } else if (pass.isEmpty()) {
                    userPass.setError("Password is missing!");
                } else if (email.isEmpty() && pass.isEmpty()) {
                    userPass.setError("Missing!");
                    userEmail.setError("Missing!");
                } else if (!email.contains("@")) {
                    userEmail.setError("That's not an email.");
                } else {
                    final Dialog dialog = new Dialog(Login.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_login);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    ParseUser.logInInBackground(email, pass, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            dialog.dismiss();
                            if (user != null) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
