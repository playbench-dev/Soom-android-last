package com.kmw.soom2.ex.Static;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Activitys.ActResult.StaticActResultFirstActivity;
import com.kmw.soom2.ex.exNewAnotherActivity;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class exStaticActActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "StaticAsthmaResultActivity";
    TextView txtTitle;
    TextView txtTitle1;
    LinearLayout linScore;
    TextView txtScore,txtContents, btnBack;
    Button btnFinish;
    TextView txtName;
    ImageView imgIcon;
    Intent beforeIntent;
    Button btnMove;

    ArrayList<Activity> activityArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_static_act);

        beforeIntent = getIntent();

        FindViewById();

        txtTitle1.setText(getResources().getString(R.string.result_title3));
        txtContents.setText(getResources().getString(R.string.result_vice_contents3));
        txtScore.setText("18점");
        imgIcon.setImageResource(R.drawable.ic_act_bad);
        linScore.setBackgroundResource(R.drawable.bg_gradient_result03);
    }

    void FindViewById(){
        txtTitle = (TextView)findViewById(R.id.txt_statics_asthma_result_date);
        txtTitle1 = (TextView)findViewById(R.id.txt_check_result_title);
        linScore = (LinearLayout)findViewById(R.id.lin_check_result_score);
        txtScore = (TextView)findViewById(R.id.txt_check_result_score);
        txtContents = (TextView)findViewById(R.id.txt_check_result_contents);
        txtName = (TextView)findViewById(R.id.txt_check_result_name);
        imgIcon = (ImageView)findViewById(R.id.img_check_result_icon);
        btnMove = findViewById(R.id.btn_ex_act_static_move);

        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(exStaticActActivity.this, NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        btnBack = findViewById(R.id.txt_statics_asthma_detail_back);

        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_statics_asthma_detail_back: { // 뒤로 가기
                onBackPressed();
                break;
            }

        }
    }
}