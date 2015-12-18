package com.android.internshipcamp.Others;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.internshipcamp.R;

/**
 * Created by Nilesh Singh on 16-12-2015.
 */
public class ConnectionDetector {
    public static Dialog dialog;
    public static boolean isConnected = false;
    public static Button retry;
    public static boolean isDialogShown = false;

    public static NetworkInfo getNetwork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        return networkInfo;
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetwork(context);
        isConnected = true;
        return (info != null && info.isConnected());
    }

    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetwork(context);
        return (info != null && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetwork(context);
        return (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static void showErrorDialog(final Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_no_connection);
        dialog.setCanceledOnTouchOutside(false);

        isDialogShown = true;

        Button settings = (Button) dialog.findViewById(R.id.settingsWifi);
        retry = (Button) dialog.findViewById(R.id.retry);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent);
            }
        });

        dialog.show();
    }

    public static void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
            isDialogShown = false;
        }
    }

}
