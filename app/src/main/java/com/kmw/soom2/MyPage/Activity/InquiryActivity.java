package com.kmw.soom2.MyPage.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class InquiryActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "InquiryActivity";
    String response;

    RadioGroup inquiryRadioGroup;
    RadioButton inquiryRadioButton, declarationRadioButton;
    EditText inquiryEditText;
    TextView editTextCountTextView,txtContentsTitle;
    EditText editEmail;
    TextView btnBack;
    Button btnSend;
    TextView txtTitle;

    Intent beforeIntent;
    TwoButtonDialog twoButtonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        beforeIntent = getIntent();

        editEmail = findViewById(R.id.email_edit_text_activity_inquiry);
        btnBack = findViewById(R.id.back_text_view_activity_inquiry);
        btnSend = findViewById(R.id.send_button_activity_inquiry);
        inquiryRadioGroup = (RadioGroup) findViewById(R.id.radio_group_activity_inquiry);
        inquiryRadioButton = (RadioButton) findViewById(R.id.inquiry_radio_btn_activity_inquiry);
        declarationRadioButton = (RadioButton) findViewById(R.id.declaration_radio_btn_activity_inquiry);
        inquiryEditText = (EditText) findViewById(R.id.inquiry_edit_text_activity_inquiry);
        editTextCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_inquiry);
        txtTitle = (TextView) findViewById(R.id.txt_inquiry_title);
        txtContentsTitle = (TextView)findViewById(R.id.inquiry_text_view_activity_inquiry);

        NullCheck(this);

        if (beforeIntent != null) {
            if (beforeIntent.hasExtra("inquiry")) {
                txtTitle.setVisibility(View.VISIBLE);
                inquiryRadioGroup.setVisibility(View.GONE);
                txtContentsTitle.setText("신고내용");
            }else {
                inquiryEditText.setHint("개선 사항 혹은 오류 내용을 입력해주세요.");
            }
        }else{
            inquiryEditText.setHint("개선 사항 혹은 오류 내용을 입력해주세요.");
        }

        editEmail.setText(Utils.userItem.getEmail());

        if (beforeIntent.hasExtra("guest")){
            editEmail.setText("");
        }

        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        inquiryRadioButton.setChecked(true);

        inquiryRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (inquiryRadioButton.isChecked()) {
                    inquiryRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_checked_a);
                    inquiryRadioButton.setTextColor(Color.parseColor("#ffffff"));
                    declarationRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_b);
                    declarationRadioButton.setTextColor(Color.parseColor("#09D182"));
                    txtContentsTitle.setText("문의내용");
                    inquiryEditText.setHint("개선 사항 혹은 오류 내용을 입력해주세요.");
                } else if (declarationRadioButton.isChecked()) {
                    declarationRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_checked_b);
                    declarationRadioButton.setTextColor(Color.parseColor("#ffffff"));
                    inquiryRadioButton.setBackgroundResource(R.drawable.inquiry_radio_button_a);
                    inquiryRadioButton.setTextColor(Color.parseColor("#09D182"));
                    txtContentsTitle.setText("신고내용");
                    inquiryEditText.setHint("내용을 입력해주세요.");
                }
            }
        });

        inquiryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editTextCountTextView.setText("" + inquiryEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        String title = "";
        if (inquiryEditText.getText().length() > 0){
            if (inquiryRadioButton.isChecked()){
                title = "문의";
            }else{
                title = "신고";
            }
            twoButtonDialog = new TwoButtonDialog(InquiryActivity.this,title, "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
            twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                @Override
                public void OkButtonClick(View v) {
                    twoButtonDialog.dismiss();
                    finish();
                }
            });

            twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                @Override
                public void CancelButtonClick(View v) {
                    twoButtonDialog.dismiss();
                }
            });
        }else{
            finish();
        }
    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_text_view_activity_inquiry:
                onBackPressed();
                break;
            case R.id.send_button_activity_inquiry:
                if (editEmail.getText().length() == 0){
                    new OneButtonDialog(this,"문의 및 신고","이메일을 입력해주세요.","확인");
                }else if (inquiryEditText.getText().length() == 0){
                    new OneButtonDialog(this,"문의 및 신고","내용을 입력해주세요.","확인");
                }else{
                    new SendInquiryNetwork().execute();
                }
                break;
        }
    }

    public class SendInquiryNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("CONTENTS", inquiryEditText.getText().toString());
            body.add("EMAIL", editEmail.getText().toString());

            if (!beforeIntent.hasExtra("guest")){
                body.add("USER_NO", String.valueOf(userItem.getUserNo()));
                body.add("WRITER", userItem.getNickname());
            }

            if (beforeIntent != null) {
                if (beforeIntent.hasExtra("inquiry")) {
                    body.add("TITLE", "신고하기");
                    body.add("INQUIRY_FLAG", "2");
                } else {
                    body.add("TITLE", inquiryRadioButton.isChecked() ? "문의하기" : "신고하기");
                    body.add("INQUIRY_FLAG", inquiryRadioButton.isChecked() ? "1" : "2");
                }
            } else {
                body.add("TITLE", inquiryRadioButton.isChecked() ? "문의하기" : "신고하기");
                body.add("INQUIRY_FLAG", inquiryRadioButton.isChecked() ? "1" : "2");
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertInquery(), body.build());

                return response;
            } catch (IOException e) {
                btnSend.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {

                } else {
                    if (inquiryRadioButton.isChecked()) {
                        new OneButtonDialog(InquiryActivity.this,"문의","소중한 문의 감사합니다.\n빠른 시일 안에 답변 드릴께요.","확인")
                                .setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                    @Override
                                    public void OnButtonClick(View v) {
                                        finish();
                                    }
                                });

                    } else {
                        new OneButtonDialog(InquiryActivity.this,"신고","불편하셨죠? ㅠㅠ 빨리 조치할게요.","확인")
                                .setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                    @Override
                                    public void OnButtonClick(View v) {
                                        finish();
                                    }
                                });
                    }
                }
                btnSend.setClickable(true);
            } catch (JSONException e) {
                btnSend.setClickable(true);
            }
        }
    }
}
