package com.kmw.soom2.Home.Activitys.CoachMark;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.NullCheck;

public class CommunityCoachMarkActivity extends AppCompatActivity implements View.OnClickListener {

    Button mButtonNever;
    Button mButtonFinish;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_coach_mark);

        pref = getSharedPreferences("preferences",MODE_PRIVATE);
        editor = pref.edit();

        FindViewById();

        NullCheck(this);

    }

    void FindViewById(){
        mButtonNever = (Button)findViewById(R.id.btn_community_coach_mark_never);
        mButtonFinish = (Button)findViewById(R.id.btn_community_coach_mark_finish);

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
            case R.id.btn_community_coach_mark_never : {
                editor.putInt("communityCoachMark",2);
                editor.apply();
                setResult(RESULT_OK);
                finish();
                break;
            }
            case R.id.btn_community_coach_mark_finish : {
                editor.putInt("communityCoachMark",1);
                editor.apply();
                setResult(RESULT_OK);
                finish();
                break;
            }
        }
    }
}
