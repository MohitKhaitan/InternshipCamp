package com.android.internshipcamp.Adapters;

import android.os.Build;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.SaveCallback;

/**
 * Created by Nilesh Singh on 12-12-2015.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this);

        ParseInstallation pi = ParseInstallation.getCurrentInstallation();
        pi.put("deviceID", Build.SERIAL);
        pi.put("deviceBrand", Build.BRAND);
        pi.put("deviceModel", Build.MODEL);
        pi.put("androidVer", Build.VERSION.SDK_INT);
        pi.put("deviceManuf", Build.MANUFACTURER);
        pi.saveEventually();

    }
}
