package com.kmw.soom2.MyPage.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Login.Activitys.DatePickerDialogActivity;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class PatientInfoActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "PatientInfo";
    RadioGroup genderRadioGroup;
    RadioButton maleRadioButton, femaleRadioButton;
    RadioGroup confirmRadioGroup;
    RadioButton confirmRadioButton, suspicionRadioButton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    TextView backTextView, btnSave;
    TextView txtFlagText;
    TextView dateInfo, calendarTextView;
    String date;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialogActivity dp;

    RelativeLayout linCalendar;

    EditText editName, editBirth;

    String response;
    UserItem userItem;

    TwoButtonDialog twoButtonDialog;

    private String mSaveBeforeName = "";
    private String mSaveBeforeBirth = "";
    private int mSaveBeforeGender = 0;
    private int mSaveBeforeFlag = 0;
    private int mSaveAfterGender = 0;
    private int mSaveAfterFlag = 0;
    private String mSaveBeforeDate = "";
    private String mSaveAfterDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        btnSave = findViewById(R.id.txt_patient_info_save);
        userItem = Utils.userItem;
        editName = findViewById(R.id.name_edit_text_activity_patient_info);
        editBirth = findViewById(R.id.birth_edit_text_activity_patient_info);
        linCalendar = findViewById(R.id.lin_patient_info_calendar);

        backTextView = (TextView) findViewById(R.id.back_text_view_activity_patient_info);
        txtFlagText = (TextView)findViewById(R.id.txt_patient_flag_text);

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_toggle_btn_activity_patient_info);
        maleRadioButton = (RadioButton) findViewById(R.id.male_btn_activity_patient_info);
        femaleRadioButton = (RadioButton) findViewById(R.id.feamale_btn_activity_patient_info);

        confirmRadioGroup = (RadioGroup) findViewById(R.id.confirm_toggle_btn_activity_patient_info);
        confirmRadioButton = (RadioButton) findViewById(R.id.confirm_btn_activity_patient_info);
        suspicionRadioButton = (RadioButton) findViewById(R.id.suspicion_btn_activity_patient_info);

        dateInfo = (TextView) findViewById(R.id.date_info_activity_patient_info);
        calendarTextView = (TextView) findViewById(R.id.calendar_text_view_activity_patient_info);

        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        Calendar newDate = Calendar.getInstance();
        date = format1.format(newDate.getTime());

        NullCheck(this);

        btnSave.setOnClickListener(this);
        editBirth.setOnClickListener(this);
        backTextView.setOnClickListener(this);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male_btn_activity_patient_info) {
                    maleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    maleRadioButton.setTextColor(Color.parseColor("#09D182"));
                    femaleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    femaleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                    mSaveAfterGender = 1;
                } else if (checkedId == R.id.feamale_btn_activity_patient_info) {
                    femaleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    femaleRadioButton.setTextColor(Color.parseColor("#09D182"));
                    maleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    maleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                    mSaveAfterGender = 2;
                }
            }
        });

        confirmRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.confirm_btn_activity_patient_info) {
                    if (Utils.userItem.getLv() == 33){
                        confirmRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        confirmRadioButton.setTextColor(Color.parseColor("#09D182"));
                        suspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        suspicionRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                        dateInfo.setVisibility(View.VISIBLE);
                        dateInfo.setText("의심시기");
                        linCalendar.setVisibility(View.VISIBLE);
                        calendarTextView.setEnabled(true);
                        mSaveAfterFlag = 1;
                    }else{
                        confirmRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        confirmRadioButton.setTextColor(Color.parseColor("#09D182"));
                        suspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        suspicionRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                        dateInfo.setVisibility(View.VISIBLE);
                        dateInfo.setText("확진일");
                        linCalendar.setVisibility(View.VISIBLE);
                        calendarTextView.setEnabled(true);
                        mSaveAfterFlag = 1;
                    }

                } else if (checkedId == R.id.suspicion_btn_activity_patient_info) {
                    if (Utils.userItem.getLv() == 33){
                        suspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        suspicionRadioButton.setTextColor(Color.parseColor("#09D182"));
                        confirmRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        confirmRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                        dateInfo.setVisibility(View.INVISIBLE);
                        dateInfo.setText("의심시기");
                        linCalendar.setVisibility(View.GONE);
                        calendarTextView.setEnabled(false);
                        mSaveAfterFlag = 2;
                    }else{
                        suspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        suspicionRadioButton.setTextColor(Color.parseColor("#09D182"));
                        confirmRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        confirmRadioButton.setTextColor(Color.parseColor("#5c5c5c"));
                        dateInfo.setVisibility(View.VISIBLE);
                        dateInfo.setText("의심시기");
                        linCalendar.setVisibility(View.VISIBLE);
                        calendarTextView.setEnabled(true);
                        mSaveAfterFlag = 2;
                    }

                }
            }
        });

        calendarTextView.setOnClickListener(this);

        if (userItem.getBirth() == 0){
            editBirth.setText("");
        }else{
            editBirth.setText(""+userItem.getBirth());
        }


        editName.setText(userItem.getName());
        if (userItem.getGender() == 1) {
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        } else {
            maleRadioButton.setChecked(false);
            femaleRadioButton.setChecked(true);
        }

        Log.i(TAG,"outbreak : " + userItem.getOutbreakDt());

        if (Utils.userItem.getLv() == 33){
            if (userItem.getDiagnosisFlag() == 1) {
                confirmRadioButton.setChecked(true);
                suspicionRadioButton.setChecked(false);
                linCalendar.setVisibility(View.VISIBLE);
                calendarTextView.setText(userItem.getOutbreakDt().replace("-",""));
            } else {
                confirmRadioButton.setChecked(false);
                suspicionRadioButton.setChecked(true);
                linCalendar.setVisibility(View.GONE);
                calendarTextView.setText(Utils.formatYYYYMMDD.format(System.currentTimeMillis()).replace("-",""));
            }
        }else{
            if (userItem.getDiagnosisFlag() == 1) {
                confirmRadioButton.setChecked(true);
                suspicionRadioButton.setChecked(false);
                linCalendar.setVisibility(View.VISIBLE);
                calendarTextView.setText(userItem.getOutbreakDt().replace("-",""));
            } else {
                confirmRadioButton.setChecked(false);
                suspicionRadioButton.setChecked(true);
                linCalendar.setVisibility(View.VISIBLE);
                calendarTextView.setText(userItem.getOutbreakDt().replace("-",""));
            }
        }

        if (Utils.userItem.getLv() == 33){
            confirmRadioButton.setText("의심");
            suspicionRadioButton.setText("건강해요");
            dateInfo.setText("의심시기");
        }else if (Utils.userItem.getLv() == 60 || Utils.userItem.getLv() == 80 || Utils.userItem.getLv() == 99){
            confirmRadioGroup.setVisibility(View.GONE);
            txtFlagText.setVisibility(View.GONE);
            dateInfo.setVisibility(View.GONE);
            linCalendar.setVisibility(View.GONE);
        }else{

        }


        mSaveBeforeName = userItem.getName();
        mSaveBeforeBirth = ""+userItem.getBirth();
        mSaveBeforeGender = userItem.getGender();
        mSaveBeforeFlag = userItem.getDiagnosisFlag();
        mSaveAfterGender = userItem.getGender();
        mSaveAfterFlag = userItem.getDiagnosisFlag();
        mSaveBeforeDate = calendarTextView.getText().toString();
        mSaveAfterDate = calendarTextView.getText().toString();

    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_patient_info_save:
                btnSave.setClickable(false);
                if (getNetworkState() != null && getNetworkState().isConnected()) {
                    new UpdateUserInfoNetwork().execute();
                }
                break;
            case R.id.back_text_view_activity_patient_info:
                onBackPressed();
                break;
            case R.id.calendar_text_view_activity_patient_info:
                FragmentManager fragmentManager = getSupportFragmentManager();

                dp = new DatePickerDialogActivity(calendarTextView.getText().toString(), new DatePickerDialogActivity.ConfirmButtonListener() {

                    @Override
                    public void confirmButton(String data) {
                        calendarTextView.setText(data.replace("-",""));
                        mSaveAfterDate = calendarTextView.getText().toString();
                    }
                });
                dp.show(fragmentManager, "test");
                break;
            case R.id.birth_edit_text_activity_patient_info :
                dp = new DatePickerDialogActivity(""+editBirth.getText().toString(), new DatePickerDialogActivity.ConfirmButtonListener() {
                    @Override
                    public void confirmButton(String data) {
                        editBirth.setText(data.replace("-",""));
                    }
                });
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                dp.show(fragmentManager1, "test");
                break;
        }
    }


    public class UpdateUserInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add("GENDER", maleRadioButton.isChecked() ? "1" : "2");
            body.add("NAME", editName.getText().toString());
            body.add("BIRTH", editBirth.getText().toString());
            body.add("PROFILE_IMG",userItem.getProfileImg());
            body.add("DIAGNOSIS_FLAG", confirmRadioButton.isChecked() ? "1" : "2");
            if (linCalendar.getVisibility() == View.VISIBLE) {
                body.add("OUTBREAK_DT", calendarTextView.getText().toString().replace("-",""));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateUserInfo(), body.build());
                Log.i(TAG,"response : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                btnSave.setClickable(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                    btnSave.setClickable(true);
                } else {
                    new GetReloadUserDataNetwork().execute();
                }
            } catch (JSONException e) {
                btnSave.setClickable(true);
            }
        }
    }

    public class GetReloadUserDataNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("ID", userItem.getId());

            if (userItem.getLoginType() == 1) {
                body.add("PASSWORD", userItem.getPassword());
            }
            body.add("DEVICE_CODE", Utils.TOKEN);
            body.add("OS_TYPE", "1");

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body.build());
                Log.i(TAG,"login : " + response);
                return response;
            } catch (IOException e) {
                btnSave.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {

                    JSONObject object = jsonArray.getJSONObject(0);
                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                        UserItem userItem = new UserItem();

                        userItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                        userItem.setLv(JsonIntIsNullCheck(object, "LV"));
                        userItem.setId(JsonIsNullCheck(object, "ID"));
                        userItem.setEmail(JsonIsNullCheck(object, "EMAIL"));
                        userItem.setPassword(JsonIsNullCheck(object, "PASSWORD"));
                        userItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                        userItem.setName(JsonIsNullCheck(object, "NAME"));
                        userItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                        userItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                        userItem.setDiagnosisFlag(JsonIntIsNullCheck(object, "DIAGNOSIS_FLAG"));  // 확진 여부
                        userItem.setOutbreakDt(JsonIsNullCheck(object, "OUTBREAK_DT")); // 발병일
                        userItem.setProfileImg(JsonIsNullCheck(object, "PROFILE_IMG"));  // 프로필 사진
                        userItem.setDeviceCode(JsonIsNullCheck(object, "DEVICE_CODE"));  // fcm토큰
                        userItem.setLoginType(JsonIntIsNullCheck(object, "LOGIN_TYPE"));  //  1: 일반 이메일, 2 : 네이버, 3 : 카카오, 4 : 애플
                        userItem.setOsType(JsonIntIsNullCheck(object, "OS_TYPE")); // 1 : android, 2 : ios
                        userItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        userItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                        userItem.setPhone(JsonIsNullCheck(object, "PHONE"));
                        Utils.userItem = userItem;

                        preferences = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("DEVICE_CODE", userItem.getDeviceCode());
                        editor.putString("USER_ID", userItem.getId());
                        editor.putString("USER_PASSWORD", userItem.getPassword());
                        editor.putInt("LOGIN_TYPE", userItem.getLoginType());
                        editor.putInt("OS_TYPE", userItem.getOsType());

                        new OneButtonDialog(PatientInfoActivity.this, "회원정보 수정", "저장되었습니다.", "확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                finish();
                            }
                        });
//                        customOneButtonView.show();

                    } else {
                    }
                } else {
                }
            } catch (JSONException e) {
            }
            btnSave.setClickable(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSaveBeforeName.equals(editName.getText().toString()) && mSaveBeforeBirth.equals(editBirth.getText().toString()) && mSaveBeforeGender == mSaveAfterGender
                && mSaveBeforeFlag == mSaveAfterFlag && mSaveBeforeDate.equals(mSaveAfterDate)){
            finish();
        }else{
            twoButtonDialog = new TwoButtonDialog(PatientInfoActivity.this,"기본정보", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
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
        }
    }
}

