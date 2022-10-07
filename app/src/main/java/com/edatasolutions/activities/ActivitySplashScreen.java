package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.edatasolutions.R;
import com.edatasolutions.utils.SessionManager;

public class ActivitySplashScreen extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sessionManager = new SessionManager(ActivitySplashScreen.this);


        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });
        crashButton.setVisibility(View.GONE);
        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        GotoNextScreen();
    }

    private void GotoNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    if (sessionManager.isLoggedIn()) {

                        String role_id = sessionManager.getAdminLogin().get(SessionManager.ROLE_ID);
                        assert role_id!=null;
                        if (role_id.equals("1")){
                            // Goto Home View
                            final Intent mainIntent = new Intent(ActivitySplashScreen.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }else {
                            // Goto Home View
                            final Intent mainIntent = new Intent(ActivitySplashScreen.this, ActivityHome.class);
                            startActivity(mainIntent);
                            finish();
                        }

                    } else {
                        //Goto Login View
                        final Intent mainIntent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);
                        startActivity(mainIntent);
                        finish();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 1000);
    }
}