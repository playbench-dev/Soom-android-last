package com.kmw.soom2.Home.Activitys.AdultActivitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kmw.soom2.R;

public class NewActLicenseActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "NewActLicenseActivity";
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_act_license);

        FindViewById();

    }

    void FindViewById(){
        imgBack = (ImageView)findViewById(R.id.img_new_act_license_back);

        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_new_act_license_back :
                onBackPressed();
                break;
        }
    }
}