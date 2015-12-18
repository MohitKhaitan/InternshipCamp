package com.android.internshipcamp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.android.internshipcamp.Fragments.Home;
import com.android.internshipcamp.Fragments.ImpDates;
import com.android.internshipcamp.Fragments.MyBookmarks;
import com.android.internshipcamp.Fragments.MyInternship;
import com.android.internshipcamp.Fragments.VenueDetails;
import com.android.internshipcamp.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView nView;
    TextView toolTitle;
    FragmentManager manager;
    Fragment fragment;
    TextView plusName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.ic_menu_white_48dp);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        nView = (NavigationView) findViewById(R.id.navBar);
        nView.setNavigationItemSelectedListener(this);

        plusName = (TextView) nView.getHeaderView(0).findViewById(R.id.nameUser);
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            String plusStr = null;
            try {
                plusStr = user.fetch().get("fName").toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            plusName.setText("+" + plusStr);
        }

        toolTitle = (TextView) findViewById(R.id.toolbarTitle);

        fragment = new Home();
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame, fragment)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commit();

        CircleImageView circleImageView = new CircleImageView(this);
        circleImageView.setBorderColor(Color.parseColor("#39add1"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        drawerLayout.closeDrawer(GravityCompat.START);

        if (id == R.id.home) {
            fragment = new Home();
            toolTitle.setText("Internship Camp");
            colorSetter("#3F51B5");
        } else if (id == R.id.myBookmarks) {
            fragment = new MyBookmarks();
            toolTitle.setText("My Bookmarks");
            colorSetter("#FF5722");
        } else if (id == R.id.event) {
            fragment = new VenueDetails();
            toolTitle.setText("Event Details");
            colorSetter("#9C27B0");
        } else if (id == R.id.logOut) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    startActivity(new Intent(MainActivity.this, Splash.class));
                    finish();
                }
            });
        } else if (id == R.id.myInternships) {
            fragment = new MyInternship();
            toolTitle.setText("My Internships");
            colorSetter("#2196F3");
        } else if (id == R.id.eventDates) {
            fragment = new ImpDates();
            toolTitle.setText("Important Dates");
            colorSetter("#009688");
        }

        manager.beginTransaction().replace(R.id.frame, fragment)
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();

        return true;
    }

    private void colorSetter(String color) {
        toolbar.setBackgroundColor(Color.parseColor(color));
        toolTitle.setBackgroundColor(Color.parseColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor(color));
        }
    }
}
