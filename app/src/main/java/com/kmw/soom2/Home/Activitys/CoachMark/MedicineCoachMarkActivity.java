package com.kmw.soom2.Home.Activitys.CoachMark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.NullCheck;

public class MedicineCoachMarkActivity extends AppCompatActivity implements View.OnClickListener {

    Button                          mButtonNever;
    Button                          mButtonFinish;
    ImageView                       mImageBg;

    SharedPreferences               pref;
    SharedPreferences.Editor        editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_coach_mark);

        pref                        = getSharedPreferences("preferences",MODE_PRIVATE);
        editor                      = pref.edit();

        FindViewById();

        NullCheck(this);

        if (pref.getInt("medicineCoachMark1",0) == 2){
            mImageBg.setBackgroundResource(R.drawable.img_coach_mark_medicine_2);
            mButtonFinish.setText("닫기");
        }else if (pref.getInt("medicineCoachMark2",0) == 2){
            mImageBg.setBackgroundResource(R.drawable.img_coach_mark_medicine_1);
            mButtonFinish.setText("닫기");
        }

    }

    void FindViewById(){

        mButtonNever                = (Button)findViewById(R.id.btn_medicine_coach_mark_never);
        mButtonFinish               = (Button)findViewById(R.id.btn_medicine_coach_mark_finish);
        mImageBg                    = (ImageView)findViewById(R.id.img_medicine_coach_mark_bg);

        mButtonNever.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {

    }

    //memo 2 - never , 1 - 앱 종료시 다시 시작, 0 - 무조건 띄우기
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_medicine_coach_mark_never : {
                if (mButtonFinish.getText().toString().equals("다음")){
                    mImageBg.setBackgroundResource(R.drawable.img_coach_mark_medicine_2);
                    mButtonFinish.setText("닫기");
                    editor.putInt("medicineCoachMark1",2);
                    editor.apply();
                }else{
                    if (pref.getInt("medicineCoachMark2",0) == 2){
                        editor.putInt("medicineCoachMark1",2);
                        editor.apply();
                        finish();
                    }else{
                        editor.putInt("medicineCoachMark2",2);
                        editor.apply();
                        finish();
                    }
                }
                break;
            }
            case R.id.btn_medicine_coach_mark_finish : {
                if (mButtonFinish.getText().toString().equals("다음")){
                    mImageBg.setBackgroundResource(R.drawable.img_coach_mark_medicine_2);
                    mButtonFinish.setText("닫기");
                    editor.putInt("medicineCoachMark1",1);
                    editor.apply();
                }else{
                    if (pref.getInt("medicineCoachMark2",0) == 2){
                        editor.putInt("medicineCoachMark1",1);
                        editor.apply();
                        finish();
                    }else{
                        editor.putInt("medicineCoachMark2",1);
                        editor.apply();
                        finish();
                    }
                }
                break;
            }
        }
    }
}
