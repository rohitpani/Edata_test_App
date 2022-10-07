package com.edatasolutions.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edatasolutions.R;
import com.edatasolutions.utils.SessionManager;

public class ActivityFailed extends AppCompatActivity {

    private TextView citation_no_txt;
    private ImageView clear_page;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);
        sessionManager =  new SessionManager(ActivityFailed.this);

        String citation_no = getIntent().getStringExtra("citation_number");
        citation_no_txt = findViewById(R.id.citation_no_txt);
        clear_page = findViewById(R.id.clear_page);
        citation_no_txt.setText(citation_no);

        clear_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.ClearDataEntery();
                sessionManager.clearSession();
                Intent i = new Intent(ActivityFailed.this, ActivityHome.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
    }

    @Override
    public void onBackPressed() {

        sessionManager.ClearDataEntery();
        sessionManager.clearSession();
        Intent i = new Intent(ActivityFailed.this, ActivityHome.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.enter,R.anim.exit);

        super.onBackPressed();
    }
}