package com.kmw.soom2.Reports.Activitys.ActResult;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.kmw.soom2.R;

public class StaticActResultFourthActivity extends AppCompatActivity implements View.OnClickListener  {
    TextView txtBack;
    TextView txtDate;
    RadioGroup rdoGroup;
    Button btnNext;
    RadioButton rdo1,rdo2,rdo3,rdo4,rdo5;
    TextView txtActResultQuestion;

    Intent beforeIntent;

    int idxScore = 0;
    int actType = 0;    // 1 일반, 2 : 아동
    String date = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_result_fourth);



        beforeIntent = getIntent();

        actType = beforeIntent.getIntExtra("type",0);
        idxScore = Integer.parseInt(beforeIntent.getStringExtra("selected").split(",")[3]);
        date = beforeIntent.getStringExtra("date");

        FindViewById();

        txtDate.setText(date);
        btnNext.setEnabled(false);
        btnNext.setBackgroundTintList(getColorStateList(R.color.f5f5f5));
        btnNext.setTextColor(getResources().getColor(R.color.black));


        if (actType == 1) {
            txtActResultQuestion.setText(R.string.adult_check_fourth_text);
        }else {
            txtActResultQuestion.setText(R.string.kids_check_fourth_text);
        }

        setRdoId(idxScore);

    }
    void setRdoId(int idxScore) {
        int idValue = 0;

        if (actType == 1){
            if (idxScore == 1) {
                rdo1.setChecked(true);
                rdo1.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if (idxScore == 2) {
                rdo2.setChecked(true);
                rdo2.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if (idxScore == 3) {
                rdo3.setChecked(true);
                rdo3.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if (idxScore == 4) {
                rdo4.setChecked(true);
                rdo4.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else {
                rdo5.setChecked(true);
                rdo5.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        }else{
            if (idxScore == 1) {
                rdo5.setChecked(true);
                rdo5.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if (idxScore == 2) {
                rdo4.setChecked(true);
                rdo4.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if (idxScore == 3) {
                rdo3.setChecked(true);
                rdo3.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else if (idxScore == 4) {
                rdo2.setChecked(true);
                rdo2.setTextColor(getResources().getColor(R.color.colorPrimary));

            }else {
                rdo1.setChecked(true);
                rdo1.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        }

        btnNext.setEnabled(true);
        btnNext.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        btnNext.setTextColor(getResources().getColor(R.color.white));
    }
    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_check_fourth_back);
        txtDate = (TextView)findViewById(R.id.txt_check_four_date);
        rdoGroup = (RadioGroup)findViewById(R.id.rdo_check_fourth_group);
        btnNext = (Button)findViewById(R.id.btn_check_fourth_next);

        txtActResultQuestion = findViewById(R.id.txt_act_result_fourth_question);

        rdo1 = findViewById(R.id.rdo_check_fourth1);
        rdo2 = findViewById(R.id.rdo_check_fourth2);
        rdo3 = findViewById(R.id.rdo_check_fourth3);
        rdo4 = findViewById(R.id.rdo_check_fourth4);
        rdo5 = findViewById(R.id.rdo_check_fourth5);

        txtActResultQuestion.setMovementMethod(new ScrollingMovementMethod());

        rdo1.setEnabled(false);
        rdo2.setEnabled(false);
        rdo3.setEnabled(false);
        rdo4.setEnabled(false);
        rdo5.setEnabled(false);

        rdo1.setText((actType == 1) ? R.string.adult_check_page4_rdo_1 : R.string.kids_check_page4_rdo_1);
        rdo2.setText((actType == 1) ? R.string.adult_check_page4_rdo_2 : R.string.kids_check_page4_rdo_2);
        rdo3.setText((actType == 1) ? R.string.adult_check_page4_rdo_3 : R.string.kids_check_page4_rdo_3);
        rdo4.setText((actType == 1) ? R.string.adult_check_page4_rdo_4 : R.string.kids_check_page4_rdo_4);
        rdo5.setText((actType == 1) ? R.string.adult_check_page4_rdo_5 : R.string.kids_check_page4_rdo_5);

        txtBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_check_fourth_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_check_fourth_next : {
                Intent i = new Intent(this, StaticActResultFifthActivity.class);
                i.putExtra("score", beforeIntent.getIntExtra("score", 0));
                i.putExtra("type", beforeIntent.getIntExtra("type",0));
                i.putExtra("selected", beforeIntent.getStringExtra("selected"));
                i.putExtra("date", date);
                startActivity(i);
                break;
            }
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}