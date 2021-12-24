package com.kmw.soom2.Home.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kmw.soom2.R;

public class AsthmaPercentDetailActivity extends AppCompatActivity {

    private String TAG = "AsthmaPercentDetailActivity";
    private TextView mTextViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asthma_percent_detail);

        mTextViewBack = (TextView) findViewById(R.id.txt_asthma_percent_detail_back);

        mTextViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}