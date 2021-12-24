package com.kmw.soom2.Reports.Activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Activitys.ActResult.StaticActResultFirstActivity;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class StaticAsthmaResultActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "StaticAsthmaResultActivity";
    TextView txtTitle;
    TextView txtTitle1;
    LinearLayout linScore;
    TextView txtScore,txtContents, btnBack;
    Button btnFinish;
    TextView txtName;
    ImageView imgIcon;
    Intent beforeIntent;

    ArrayList<Activity> activityArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_asthma_result);

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

        txtTitle.setText(beforeIntent.getStringExtra("date"));

        if (beforeIntent.hasExtra("score")){
            txtName.setText(userItem.getNickname()+"님!");
            if (beforeIntent.getIntExtra("score",0) == 25){
                txtTitle1.setText(getResources().getString(R.string.result_title1));
                txtContents.setText(getResources().getString(R.string.result_vice_contents1));
                linScore.setBackgroundResource(R.drawable.bg_gradient_result01);
                imgIcon.setImageResource(R.drawable.ic_act_good);
                txtScore.setText("25점");
            }else if (beforeIntent.getIntExtra("score",0) >= 20){
                txtTitle1.setText(getResources().getString(R.string.result_title2));
                txtContents.setText(getResources().getString(R.string.result_vice_contents2));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                imgIcon.setImageResource(R.drawable.ic_act_not_bad);
                linScore.setBackgroundResource(R.drawable.bg_gradient_result02);
            }else{
                txtTitle1.setText(getResources().getString(R.string.result_title3));
                txtContents.setText(getResources().getString(R.string.result_vice_contents3));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                imgIcon.setImageResource(R.drawable.ic_act_bad);
                linScore.setBackgroundResource(R.drawable.bg_gradient_result03);
            }
        }
    }

    void FindViewById(){
        txtTitle = (TextView)findViewById(R.id.txt_statics_asthma_result_date);
        txtTitle1 = (TextView)findViewById(R.id.txt_check_result_title);
        linScore = (LinearLayout)findViewById(R.id.lin_check_result_score);
        txtScore = (TextView)findViewById(R.id.txt_check_result_score);
        txtContents = (TextView)findViewById(R.id.txt_check_result_contents);
        txtName = (TextView)findViewById(R.id.txt_check_result_name);
        imgIcon = (ImageView)findViewById(R.id.img_check_result_icon);
        btnFinish = (Button)findViewById(R.id.btn_check_result_finish);

        btnBack = findViewById(R.id.txt_statics_asthma_detail_back);

        btnBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_check_result_finish : {
                Intent intent = new Intent(this, StaticActResultFirstActivity.class);
                intent.putExtra("score", beforeIntent.getIntExtra("score", 0));
                intent.putExtra("type", beforeIntent.getIntExtra("type",1));
                intent.putExtra("selected", beforeIntent.getStringExtra("selected"));
                intent.putExtra("date",txtTitle.getText().toString());
                this.startActivity(intent);
                break;
            }
            case R.id.txt_statics_asthma_detail_back: { // 뒤로 가기
                onBackPressed();
                break;
            }
        }
    }
}
