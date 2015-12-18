package com.android.internshipcamp.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.renderscript.Type;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.internshipcamp.Others.ConnectionDetector;
import com.android.internshipcamp.R;
import com.parse.Parse;
import com.parse.ParseUser;

public class Splash extends AppCompatActivity {
    private TextView logoText, logTagLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkConnection(false);

        logoText = (TextView) findViewById(R.id.logoText);
        logoText.setTypeface(Typeface.createFromAsset(getAssets(), "ABeeZee-Regular.ttf"));

        logTagLine = (TextView) findViewById(R.id.logoTagLine);
        logTagLine.setTypeface(Typeface.createFromAsset(getAssets(), "CODE_Bold.otf"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnection(true);
    }

    private void checkConnection(boolean isResume) {
        if (ConnectionDetector.isConnected(this)) {

            if (isResume) {
                ConnectionDetector.dismiss();
            }

            if (!ConnectionDetector.isConnected) {
                ConnectionDetector.retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ConnectionDetector.isConnected(Splash.this)) {
                            ConnectionDetector.dismiss();
                            ConnectionDetector.isConnected = true;
                        }
                    }
                });
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ParseUser.getCurrentUser() != null) {
                        startActivity(new Intent(Splash.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(Splash.this, Login.class));
                    }
                    finish();
                }
            }, 2000);
        } else {
            ConnectionDetector.showErrorDialog(this);
        }
    }
}
