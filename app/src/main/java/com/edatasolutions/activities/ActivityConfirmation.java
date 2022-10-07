package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.edatasolutions.R;
import com.edatasolutions.utils.SessionManager;

public class ActivityConfirmation extends AppCompatActivity {

    private ImageView done,clear_page;
    private SessionManager sessionManager;
    private LinearLayout print_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        sessionManager = new SessionManager(ActivityConfirmation.this);


        done = findViewById(R.id.done);
        clear_page = findViewById(R.id.clear_page);
        print_data = findViewById(R.id.print_data);

        clear_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.ClearDataEntery();
                sessionManager.clearSession();
                Intent i = new Intent(ActivityConfirmation.this, ActivityHome.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

        print_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityConfirmation.this, ZebraPrinterActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sessionManager.ClearDataEntery();
        sessionManager.clearSession();
        Intent i = new Intent(ActivityConfirmation.this, ActivityHome.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}