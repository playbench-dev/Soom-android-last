package com.kmw.soom2.Login.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.AGREE_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_FIRST_SETTING;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_SNS_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.STAFF_SIGN_UP;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.USER_SIGN_UP;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.USER_INFO_SHARED;
import static com.kmw.soom2.Common.Utils.userItem;

public class LoginUserInfoInsertActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String TAG = "UserInfoActivity";
    RadioGroup mGenderRadioGroup;
    RadioButton mMaleRadioButton, mFemaleRadioButton;
    RadioGroup mConfirmRadioGroup;
    RadioButton mConfirmRadioButton, mSuspicionRadioButton;
    EditText edtName;
    TextView edtBirth;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageView mBackTextView;
    TextView mDateInfo, mCalendarTextView;
    LinearLayout linCalendarTitleVisible,linCalendarVisible;
    TextView mTextTitle,mTextSubject;
    String date;
    Button successButton;
    TextView mTextViewConfirmTitle;
    TextView txtNameTitle;
    LinearLayout mLinearConfirmVisible;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    DatePickerDialogActivity dp;
    private FirebaseAnalytics mFirebaseAnalytics;
    String response;
    int gender = 0; // 1 : 남, 2 : 여
    int diagnosisFlag = 0; // 2 : 의심, 1 : 확진
    String mStBirth = "";
    String mStOutBreakDt = "";
    int flag = 0;

    Intent beforeIntent;

    Typeface typefaceBold, typefaceMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        beforeIntent = getIntent();

        typefaceBold = ResourcesCompat.getFont(this, R.font.notosanscjkkr_bold);
        typefaceMedium = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);

        FindViewById();
    }

    void FindViewById() {
        mBackTextView = (ImageView) findViewById(R.id.img_user_info_back);

        edtName = (EditText) findViewById(R.id.edt_user_info_name);
        edtBirth = (TextView) findViewById(R.id.edt_user_info_birth);

        mGenderRadioGroup = (RadioGroup) findViewById(R.id.gender_toggle_btn_activity_user_info);
        mMaleRadioButton = (RadioButton) findViewById(R.id.male_btn_activity_user_info);
        mFemaleRadioButton = (RadioButton) findViewById(R.id.feamale_btn_activity_user_info);

        mConfirmRadioGroup = (RadioGroup) findViewById(R.id.confirm_toggle_btn_activity_user_info);
        mConfirmRadioButton = (RadioButton) findViewById(R.id.confirm_btn_activity_user_info);
        mSuspicionRadioButton = (RadioButton) findViewById(R.id.suspicion_btn_activity_user_info);
        txtNameTitle = (TextView)findViewById(R.id.txt_user_info_name_title);

        mDateInfo = (TextView) findViewById(R.id.date_info_activity_user_info);
        mCalendarTextView = (TextView) findViewById(R.id.calendar_text_view_activity_user_info);
        linCalendarTitleVisible = (LinearLayout)findViewById(R.id.lin_calendar_title_visible);
        linCalendarVisible = (LinearLayout)findViewById(R.id.lin_calendar_visible);

        mTextViewConfirmTitle = (TextView)findViewById(R.id.txt_user_info_confirm_title_visible);
        mLinearConfirmVisible = (LinearLayout)findViewById(R.id.lin_user_info_confirm_visible);

        mTextTitle = findViewById(R.id.txt_user_info_title);
//        mTextSubject = findViewById(R.id.txt_user_info_subject);

        successButton = (Button) findViewById(R.id.success_btn_activity_user_info);

        if (beforeIntent.hasExtra("STAFF")){
            mTextViewConfirmTitle.setVisibility(View.INVISIBLE);
            mLinearConfirmVisible.setVisibility(View.INVISIBLE);
            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
//            mTextTitle.setText("의료진 기본 정보 입력");
//            mTextSubject.setText("의료진 인증을 위해 정확한 정보를 입력해주세요");
            txtNameTitle.setText("병원이름/성함");
            edtName.setHint("예) [숨케어내과]김의사");
            successButton.setText("완료 2/2");
        }else if (beforeIntent.hasExtra("BASIC")){
            mConfirmRadioButton.setText("의심");
            mSuspicionRadioButton.setText("건강해요");
            mDateInfo.setText("의심시기");
            successButton.setText("완료 3/3");
            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
        }else{
            successButton.setText("완료 3/3");
            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
        }

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        successButton.setOnClickListener(this);
//        successButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserInfoActivity.this, WorkThroughActivity.class);
//                startActivity(intent);
//            }
//        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!beforeIntent.hasExtra("STAFF")){
                    if (!beforeIntent.hasExtra("BASIC")){
                        if (s.toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        } else {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }else{
                        if (s.toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 ) {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        } else {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }
                }else{
                    if (s.toString().trim().length() > 0){
                        successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
//        Calendar newDate = Calendar.getInstance();
//        date = format1.format(newDate.getTime());
//
//        final String confirmDate = preferences.getString("confirm_date", null);
//
//        if (preferences.getString("confirm_date", null) != null) {
//            mCalendarTextView.setText(confirmDate);
//        } else {
//            mCalendarTextView.setText(date);
//        }

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mGenderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.male_btn_activity_user_info) {
                    mMaleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mMaleRadioButton.setTextColor(Color.parseColor("#09D182"));
                    mFemaleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mFemaleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                    mMaleRadioButton.setTypeface(typefaceBold);
                    mFemaleRadioButton.setTypeface(typefaceMedium);

                    gender = 1;

                    if (!beforeIntent.hasExtra("STAFF")){
                        if (!beforeIntent.hasExtra("BASIC")){
                            if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }else{
                            if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 ) {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }

                    }else{
                        if (edtName.getText().toString().trim().length() > 0){
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        }else{
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }

                } else if (checkedId == R.id.feamale_btn_activity_user_info) {
                    mFemaleRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                    mFemaleRadioButton.setTextColor(Color.parseColor("#09D182"));
                    mMaleRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                    mMaleRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                    mMaleRadioButton.setTypeface(typefaceMedium);
                    mFemaleRadioButton.setTypeface(typefaceBold);

                    gender = 2;

                    if (!beforeIntent.hasExtra("STAFF")){
                        if (!beforeIntent.hasExtra("BASIC")){
                            if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }else{
                            if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0) {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }
                    }else{
                        if (edtName.getText().toString().trim().length() > 0){
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        }else{
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }

                }
            }
        });

        mConfirmRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                diagnosisFlag = 0;
                if (checkedId == R.id.confirm_btn_activity_user_info) {
                    if (beforeIntent.hasExtra("STAFF")){

                    }else if (beforeIntent.hasExtra("BASIC")){
                        mConfirmRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        mConfirmRadioButton.setTextColor(Color.parseColor("#09D182"));
                        mSuspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        mSuspicionRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                        mConfirmRadioButton.setTypeface(typefaceBold);
                        mSuspicionRadioButton.setTypeface(typefaceMedium);

                        linCalendarVisible.setVisibility(View.VISIBLE);
                        linCalendarTitleVisible.setVisibility(View.VISIBLE);
                        mCalendarTextView.setEnabled(true);
                        diagnosisFlag = 1;

                        if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        } else {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }else{
                        mConfirmRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        mConfirmRadioButton.setTextColor(Color.parseColor("#09D182"));
                        mSuspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        mSuspicionRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                        mConfirmRadioButton.setTypeface(typefaceBold);
                        mSuspicionRadioButton.setTypeface(typefaceMedium);

                        linCalendarVisible.setVisibility(View.VISIBLE);
                        linCalendarTitleVisible.setVisibility(View.VISIBLE);
                        mCalendarTextView.setEnabled(true);
                        diagnosisFlag = 1;
                        mDateInfo.setText("확진일");
                        if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                        } else {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }
                } else if (checkedId == R.id.suspicion_btn_activity_user_info) {
                    if (beforeIntent.hasExtra("STAFF")){

                    }else if (beforeIntent.hasExtra("BASIC")){
                        mSuspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        mSuspicionRadioButton.setTextColor(Color.parseColor("#09D182"));
                        mConfirmRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        mConfirmRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                        mSuspicionRadioButton.setTypeface(typefaceBold);
                        mConfirmRadioButton.setTypeface(typefaceMedium);

                        linCalendarVisible.setVisibility(View.GONE);
                        linCalendarTitleVisible.setVisibility(View.GONE);
                        mCalendarTextView.setEnabled(false);
                        diagnosisFlag = 2;

                        if (!beforeIntent.hasExtra("STAFF")){
                            if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0) {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }
                    }else{
                        mSuspicionRadioButton.setBackgroundResource(R.drawable.toggle_btn_checked);
                        mSuspicionRadioButton.setTextColor(Color.parseColor("#09D182"));
                        mConfirmRadioButton.setBackgroundResource(R.drawable.toggle_btn);
                        mConfirmRadioButton.setTextColor(Color.parseColor("#5c5c5c"));

                        mSuspicionRadioButton.setTypeface(typefaceBold);
                        mConfirmRadioButton.setTypeface(typefaceMedium);

                        diagnosisFlag = 2;
                        mDateInfo.setText("의심시기");
                        linCalendarVisible.setVisibility(View.VISIBLE);
                        linCalendarTitleVisible.setVisibility(View.VISIBLE);

                        if (!beforeIntent.hasExtra("STAFF")){
                            if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }
                    }
                }
            }
        });

        mCalendarTextView.setOnClickListener(this);
        edtBirth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_user_info_birth: {
                flag = 0;
                FragmentManager fragmentManager = getSupportFragmentManager();
                dp = new DatePickerDialogActivity(flag, this, new DatePickerDialogActivity.ConfirmButtonListener() {

                    @Override
                    public void confirmButton(String data) {
                        edtBirth.setText(data);
                    }
                });

                dp.show(fragmentManager, "test");
                break;
            }
            case R.id.calendar_text_view_activity_user_info: {
                flag = 1;

                dp = new DatePickerDialogActivity(flag, this, new DatePickerDialogActivity.ConfirmButtonListener() {

                    @Override
                    public void confirmButton(String data) {
                        mCalendarTextView.setText(data);
                        if (!beforeIntent.hasExtra("STAFF")){

                            if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }else{
                            if (edtName.getText().toString().trim().length() > 0){
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            }else{
                                successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                            }
                        }
                    }
                });
                FragmentManager fragmentManager = getSupportFragmentManager();
                dp.show(fragmentManager, "test");
                break;
            }
            case R.id.success_btn_activity_user_info: {
                if (beforeIntent.hasExtra("STAFF")){
                    if (edtName.getText().toString().trim().length() > 0){
                        NetworkCall(STAFF_SIGN_UP);
                    }
                }else {
                    if (beforeIntent.hasExtra("BASIC") && diagnosisFlag == 2){
                        if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0) {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            successButton.setClickable(false);
                            for (int i = 0; i < edtBirth.getText().toString().split("-").length; i++) {
                                mStBirth += edtBirth.getText().toString().split("-")[i];
                            }
                            for (int i = 0; i < mCalendarTextView.getText().toString().split("-").length; i++) {
                                mStOutBreakDt += mCalendarTextView.getText().toString().split("-")[i];
                            }
                            NetworkCall(USER_SIGN_UP);
                        } else {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }else{
                        if (edtName.getText().toString().trim().length() > 0 && edtBirth.getText().toString().trim().length() > 0 && gender != 0 && diagnosisFlag != 0 && mCalendarTextView.getText().toString().trim().length() > 0) {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                            successButton.setClickable(false);
                            for (int i = 0; i < edtBirth.getText().toString().split("-").length; i++) {
                                mStBirth += edtBirth.getText().toString().split("-")[i];
                            }
                            for (int i = 0; i < mCalendarTextView.getText().toString().split("-").length; i++) {
                                mStOutBreakDt += mCalendarTextView.getText().toString().split("-")[i];
                            }
                            NetworkCall(USER_SIGN_UP);
                        } else {
                            successButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }
                    }
                }
            }
        }
    }

    void NetworkCall(String mCode){

        if (mCode.equals(USER_SIGN_UP)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("EMAIL"),beforeIntent.getStringExtra("ID"),
                    beforeIntent.getStringExtra("NICKNAME"),"" + gender,beforeIntent.getStringExtra("LV"), mStBirth,"" + diagnosisFlag,
                    "" + beforeIntent.getIntExtra("LOGIN_TYPE", 0),Utils.TOKEN,edtName.getText().toString(),mStOutBreakDt,beforeIntent.getStringExtra("PASSWORD"));
        }else if (mCode.equals(LOGIN_PROCESS)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("ID"),beforeIntent.getStringExtra("PASSWORD"),Utils.TOKEN);
        }else if (mCode.equals(LOGIN_SNS_PROCESS)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("ID"),Utils.TOKEN);
        }else if (mCode.equals(ALARM_FIRST_SETTING)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo());
        }else if (mCode.equals(AGREE_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),"1",""+preferences.getInt("marketing",-1));
        }else if (mCode.equals(STAFF_SIGN_UP)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("EMAIL"),beforeIntent.getStringExtra("ID"),
                    beforeIntent.getStringExtra("NICKNAME"),"" + gender, mStBirth,
                    "" + beforeIntent.getIntExtra("LOGIN_TYPE", 0),Utils.TOKEN,edtName.getText().toString(),beforeIntent.getStringExtra("PASSWORD"));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {

        if (mResult != null){
            if (mCode.equals(USER_SIGN_UP)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                        // 가입 실패
                    } else {
                        if (beforeIntent.getIntExtra("LOGIN_TYPE", 0) == 1) {
                            if (beforeIntent.hasExtra("PASSWORD")) {
                                NetworkCall(LOGIN_PROCESS);
                            }else{
                                NetworkCall(LOGIN_SNS_PROCESS);
                            }
                        }else {
                            NetworkCall(LOGIN_SNS_PROCESS);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,beforeIntent.getStringExtra("ID"));
                        bundle.putString(FirebaseAnalytics.Param.METHOD, "SoomCare2");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
                    }
                } catch (JSONException e) {
                    successButton.setClickable(true);
                }
            }else if (mCode.equals(STAFF_SIGN_UP)){
                if (beforeIntent.getIntExtra("LOGIN_TYPE", 0) == 1) {
                    if (beforeIntent.hasExtra("PASSWORD")) {
                        NetworkCall(LOGIN_PROCESS);
                    }else{
                        NetworkCall(LOGIN_SNS_PROCESS);
                    }
                }else {
                    NetworkCall(LOGIN_SNS_PROCESS);
                }
            }else if (mCode.equals(LOGIN_PROCESS) || mCode.equals(LOGIN_SNS_PROCESS)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    Utils.itemsArrayList = null;
                    Utils.itemsNoticeArrayList = null;
                    Utils.itemsWriteArrayList = null;
                    Utils.itemsScrabArrayList = null;
                    Utils.itemsCommentArrayList = null;
                    Utils.likeItemArrayList = null;
                    Utils.scrapItemArrayList = null;
                    Utils.COMMUNITY_LEFT_PAGEING = 1;
                    Utils.COMMUNITY_LEFT_SEARCH_TOTAL_PAGE = 1;
                    Utils.COMMUNITY_RIGHT_PAGEING = 1;
                    Utils.COMMUNITY_RIGHT_SEARCH_TOTAL_PAGE = 1;

                    Utils.userItem = null;
                    Utils.registerDtList = null;
                    Utils.koList = null;
                    Utils.etcItemArrayList = null;
                    Utils.mList = null;
                    Utils.hisItems = null;
                    Utils.AllRegisterDtList = null;
                    Utils.AllKoList = null;
                    Utils.AllEtcItemArrayList = null;
                    Utils.AllList = null;

                    if (jsonArray.length() > 0) {

                        JSONObject object = jsonArray.getJSONObject(0);
                        UserItem userItem = new UserItem();
                        Log.i(TAG,"TEST1");
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
                        userItem.setLabelColor(JsonIsNullCheck(object,"LABEL_COLOR"));
                        userItem.setLabelName(JsonIsNullCheck(object,"LABEL_NAME"));

                        Utils.userItem = userItem;

                        USER_INFO_SHARED(LoginUserInfoInsertActivity.this,userItem);

                        preferences = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("DEVICE_CODE", userItem.getDeviceCode());
                        editor.putString("USER_ID", userItem.getId());
                        editor.putString("USER_PASSWORD", userItem.getPassword());
                        editor.putInt("LOGIN_TYPE", userItem.getLoginType());
                        editor.putInt("OS_TYPE", userItem.getOsType());
                        editor.putInt("IS_LOGIN_FLAG", 1);  // 1 : 로그인 됨

                        editor.apply();

                        Log.i(TAG,"TEST");
                        NetworkCall(AGREE_UPDATE);
                    } else {
                        successButton.setClickable(true);
                    }
                } catch (JSONException e) {
                    successButton.setClickable(true);
                }
            }else if (mCode.equals(ALARM_FIRST_SETTING)){
                NetworkCall(AGREE_UPDATE);
            }else if (mCode.equals(AGREE_UPDATE)){
                Log.i(TAG,"TEST1");
                userItem.setEssentialPermissionFlag(1);
                userItem.setMarketingPermissionFlag(preferences.getInt("marketing",-1));
                editor.remove("marketing");
                editor.apply();

                if (beforeIntent.hasExtra("STAFF")){
                    Intent intent = new Intent(LoginUserInfoInsertActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    successButton.setClickable(true);
                    finish();
                }else{
                    Intent intent = new Intent(LoginUserInfoInsertActivity.this, WelcomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    successButton.setClickable(true);
                    finish();
                }
            }
        }
    }
}
