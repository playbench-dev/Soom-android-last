package com.kmw.soom2.Login.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSearchActivity;
import com.kmw.soom2.R;

public class WelcomeActivity extends AppCompatActivity implements  View.OnClickListener {

    Button mButtonConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_through);

        mButtonConfirm = (Button)findViewById(R.id.btn_welcome_confirm);

        mButtonConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_welcome_confirm:
                Intent i = new Intent(WelcomeActivity.this,WelcomeMedicinActivity.class);
                startActivity(i);
                break;
        }
    }
}
