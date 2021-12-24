package com.kmw.soom2.Login.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.R;


public class UserTypeChoiceActivity extends AppCompatActivity {
    LinearLayout linPatient,linGuardian,linBasic;
    TextView txtPatientTitle,txtGuardianTitle,txtBasicTitle;
    TextView txtPatientContents,txtGuardianContents,txtBasicContents;
    ImageView imgPatient,imgGuardian,imgBasic;

    ImageView imgBack;
    String lv = "";
    Intent beforeIntent;
    Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_choice);

        beforeIntent = getIntent();

        FindViewById();

        if (beforeIntent.hasExtra("STAFF")){
            Intent intent = new Intent(UserTypeChoiceActivity.this, LoginUserInfoInsertActivity.class);

            intent.putExtra("LV", "60");
            intent.putExtra("EMAIL", beforeIntent.getStringExtra("EMAIL"));
            if (beforeIntent.hasExtra("PASSWORD")) {
                intent.putExtra("PASSWORD", beforeIntent.getStringExtra("PASSWORD"));
            }
            intent.putExtra("NICKNAME", beforeIntent.getStringExtra("NICKNAME"));
            intent.putExtra("LOGIN_TYPE", beforeIntent.getIntExtra("LOGIN_TYPE", 0));
            intent.putExtra("ID", beforeIntent.getStringExtra("ID"));
            intent.putExtra("STAFF",true);
            startActivity(intent);
            onBackPressed();
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        View.OnClickListener choiceUser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.lin_user_type_choice_01: {
                        lv = "11";
                        linPatient. setBackgroundResource(R.drawable.user_choice_bg);
                        linGuardian.setBackgroundResource(R.drawable.user_type_choice_bg);
                        linBasic.   setBackgroundResource(R.drawable.user_type_choice_bg);

                        txtPatientTitle.setTextColor(getResources().getColor(R.color.white));
                        txtPatientContents.setTextColor(getResources().getColor(R.color.white));

                        txtGuardianTitle.setTextColor(getResources().getColor(R.color.color363636));
                        txtGuardianContents.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                        txtBasicTitle.setTextColor(getResources().getColor(R.color.color363636));
                        txtBasicContents.setTextColor(getResources().getColor(R.color.colorb1b1b1));
                        break;
                    }
                    case R.id.lin_user_type_choice_02: {
                        lv = "22";
                        linPatient. setBackgroundResource(R.drawable.user_type_choice_bg);
                        linGuardian.setBackgroundResource(R.drawable.user_choice_bg);
                        linBasic.   setBackgroundResource(R.drawable.user_type_choice_bg);

                        txtPatientTitle.setTextColor(getResources().getColor(R.color.color363636));
                        txtPatientContents.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                        txtGuardianTitle.setTextColor(getResources().getColor(R.color.white));
                        txtGuardianContents.setTextColor(getResources().getColor(R.color.white));

                        txtBasicTitle.setTextColor(getResources().getColor(R.color.color363636));
                        txtBasicContents.setTextColor(getResources().getColor(R.color.colorb1b1b1));
                        break;
                    }
                    case R.id.lin_user_type_choice_03: {
                        lv = "33";
                        linPatient. setBackgroundResource(R.drawable.user_type_choice_bg);
                        linGuardian.setBackgroundResource(R.drawable.user_type_choice_bg);
                        linBasic.   setBackgroundResource(R.drawable.user_choice_bg);

                        txtPatientTitle.setTextColor(getResources().getColor(R.color.color363636));
                        txtPatientContents.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                        txtGuardianTitle.setTextColor(getResources().getColor(R.color.color363636));
                        txtGuardianContents.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                        txtBasicTitle.setTextColor(getResources().getColor(R.color.white));
                        txtBasicContents.setTextColor(getResources().getColor(R.color.white));

                        break;
                    }
                }

                btnDone.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
            }
        };

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv.length() != 0){
                    Intent intent = new Intent(UserTypeChoiceActivity.this, LoginUserInfoInsertActivity.class);

                    intent.putExtra("LV", lv);
                    if (lv.equals("33")){
                        intent.putExtra("BASIC",true);
                    }
                    intent.putExtra("EMAIL", beforeIntent.getStringExtra("EMAIL"));
                    if (beforeIntent.hasExtra("PASSWORD")) {
                        intent.putExtra("PASSWORD", beforeIntent.getStringExtra("PASSWORD"));
                    }
                    intent.putExtra("NICKNAME", beforeIntent.getStringExtra("NICKNAME"));
                    intent.putExtra("LOGIN_TYPE", beforeIntent.getIntExtra("LOGIN_TYPE", 0));
                    intent.putExtra("ID", beforeIntent.getStringExtra("ID"));
                    startActivity(intent);
                }
            }
        });

        linPatient.setOnClickListener(choiceUser);
        linGuardian.setOnClickListener(choiceUser);
        linBasic.setOnClickListener(choiceUser);

    }

    void FindViewById(){
        imgBack = (ImageView)findViewById(R.id.img_type_choice_back);
        linPatient = findViewById(R.id.lin_user_type_choice_01);
        linGuardian = findViewById(R.id.lin_user_type_choice_02);
        linBasic = findViewById(R.id.lin_user_type_choice_03);
        txtPatientTitle = findViewById(R.id.txt_user_type_choice_01_title);
        txtGuardianTitle = findViewById(R.id.txt_user_type_choice_02_title);
        txtBasicTitle = findViewById(R.id.txt_user_type_choice_03_title);
        txtPatientContents = findViewById(R.id.txt_user_type_choice_01_contents);
        txtGuardianContents = findViewById(R.id.txt_user_type_choice_02_contents);
        txtBasicContents = findViewById(R.id.txt_user_type_choice_03_contents);
        imgPatient = findViewById(R.id.img_user_type_choice_01);
        imgGuardian = findViewById(R.id.img_user_type_choice_02);
        imgBasic = findViewById(R.id.img_user_type_choice_03);
        btnDone = (Button)findViewById(R.id.btn_type_choice_done);
    }
}
