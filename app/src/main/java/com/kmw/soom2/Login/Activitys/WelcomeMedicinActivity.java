package com.kmw.soom2.Login.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSearchActivity;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.R;

public class WelcomeMedicinActivity extends AppCompatActivity implements View.OnClickListener {

    TextView mTextSkip;
    Button mButtomInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_medicin);

        FindViewById();

    }

    void FindViewById(){
        mTextSkip = (TextView)findViewById(R.id.txt_welcome_medicine_skip);
        mButtomInsert = (Button)findViewById(R.id.btn_welcome_medicine_confirm);

        mTextSkip.setOnClickListener(this);
        mButtomInsert.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_welcome_medicine_skip:{
                Intent i = new Intent(WelcomeMedicinActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            }
            case R.id.btn_welcome_medicine_confirm:{
                // memo: 2021-01-14 김지훈 수정 시작
                // 약 검색 화면 하나로 통일함으로써 화면 수정
//                Intent i = new Intent(WelcomeMedicinActivity.this, MedicineSearchActivity.class);
                Intent i = new Intent(WelcomeMedicinActivity.this, MedicineSelectActivity.class);
                // memo: 2021-01-14 김지훈 수정 종료
                i.putExtra("newbie",true);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;
            }
        }
    }
}
