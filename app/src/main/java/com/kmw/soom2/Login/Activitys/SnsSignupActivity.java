package com.kmw.soom2.Login.Activitys;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.BackPressEditText;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Item.ItemClass;
import com.kmw.soom2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.EMAIL_OVERLAP;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NICK_NAME_OVERLAP;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class SnsSignupActivity extends AppCompatActivity implements AsyncResponse {
    TextView mEmailWarningTextView, mNnicknameWarningTextView;
    ImageView mBackButtonTextView;
    EditText mEmailEditText;
    BackPressEditText mNicknameEditText;
    Button mSuccessButton;
    InputMethodManager mInputMethdManager;
    TextView mEditTextCountTextView;
    SharedPreferences preferences;
    ItemClass itemClass = new ItemClass();
    ProgressDialog progressDialog;
    LinearLayout linIdBg;
    RelativeLayout relaNickBg;

    final  String TAG = "SnsSignupActivity";
    String response;

    boolean enableEmail = false;
    boolean enableNickname = false;

    Intent beforIntent;

    TextView mTxtStaff;
    CheckBox mChbStaff;

    Pattern pattern = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$");

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_signup);

        beforIntent = getIntent();

        mInputMethdManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        linIdBg = findViewById(R.id.lin_sns_sign_up_id_bg);
        relaNickBg = findViewById(R.id.rela_sns_sign_up_id_bg);

        mEditTextCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_sns_signup);
        mEmailWarningTextView = (TextView) findViewById(R.id.email_warning_activity_sns_signup);
        mNnicknameWarningTextView = (TextView) findViewById(R.id.nickname_warning_activity_sns_signup);
        mBackButtonTextView = (ImageView) findViewById(R.id.img_sns_signup_back);

        mEmailEditText = (EditText) findViewById(R.id.email_edit_activity_sns_signup);
        mNicknameEditText = (BackPressEditText) findViewById(R.id.nickname_edit_activity_sns_signup);

        mTxtStaff = (TextView)findViewById(R.id.txt_sns_sign_up_staff);
        mChbStaff = (CheckBox)findViewById(R.id.chb_sns_sign_up_staff);

        mSuccessButton = (Button) findViewById(R.id.success_btn_activity_sns_signup);
        mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
        mEmailEditText.clearFocus();
        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        Intent getIntent = getIntent();

//        String email = preferences.getString("email", "");

        String email = beforIntent.getStringExtra("EMAIL");
        mNicknameEditText.setOnBackPressListener(onBackPressListener);

        mEmailEditText.setText(email);

        if (!email.equals("")) {
            NetworkCall(EMAIL_OVERLAP);
        }

        mBackButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mChbStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mTxtStaff.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mSuccessButton.setText("다음 1/2");
                }else{
                    mTxtStaff.setTextColor(getResources().getColor(R.color.acacac));
                    mSuccessButton.setText("다음 1/3");
                }
            }
        });

        mEmailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mEmailEditText.setText(mEmailEditText.getText().toString().trim());
                enableEmail = false;
                mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                if (hasFocus) {
                    mEmailWarningTextView.setVisibility(View.INVISIBLE);
                } else if (!hasFocus && mEmailEditText.getText().toString().trim().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString().trim())) {
                    NetworkCall(EMAIL_OVERLAP);
                } else if (!hasFocus && mEmailEditText.getText().toString().trim().length() > 0 && !itemClass.checkEmail(mEmailEditText.getText().toString().trim())) {
                    mEmailWarningTextView.setText("이메일형식이 올바르지 않습니다.");
                    mEmailWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                    mEmailWarningTextView.setVisibility(View.VISIBLE);
                } else if (mEmailEditText.getText().toString().length() == 0) {
                    mEmailWarningTextView.setVisibility(View.INVISIBLE);
                }
            }
        });


        mNicknameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mNicknameEditText.setText(mNicknameEditText.getText().toString().trim());
                enableNickname = false;
                mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                if (hasFocus) {
                    mNnicknameWarningTextView.setVisibility(View.INVISIBLE);
                    v.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK){
                                NetworkCall(NICK_NAME_OVERLAP);
                                return true;
                            }
                            return false;
                        }
                    });
                } else if (!hasFocus && mNicknameEditText.getText().toString().trim().length() > 0 && itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                    NetworkCall(NICK_NAME_OVERLAP);
                } else if (!hasFocus && mNicknameEditText.getText().toString().trim().length() > 0 && !itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                    mNnicknameWarningTextView.setText("닉네임형식이 올바르지 않습니다.");
                    mNnicknameWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                    mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                } else if (mNicknameEditText.getText().toString().length() == 0) {
                    mNnicknameWarningTextView.setVisibility(View.INVISIBLE);
                }
            }

        });


        mNicknameEditText.addTextChangedListener(new TextWatcher() {
            String result;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableNickname = false;
//                Matcher m = pattern.matcher(s.toString().trim());
//                if (m.matches()){
                    if (!TextUtils.isEmpty(s.toString().trim()) && !s.toString().trim().equals(result))
                        if (mNicknameEditText.getText().toString().trim().length() < 2 || !itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                            mNnicknameWarningTextView.setText("닉네임은 두글자 이상이어야 합니다.");
                            mNnicknameWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                            mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                            mSuccessButton.setEnabled(false);
                        }else if (mNicknameEditText.getText().toString().trim().length() < 8 || mNicknameEditText.getText().toString().trim().length() > 1 || !itemClass.checkNick(mNicknameEditText.getText().toString().trim())){
                            mNnicknameWarningTextView.setText("키보드의 '완료'를 눌러 중복체크 해주세요.");
                            mNnicknameWarningTextView.setTextColor(Color.parseColor("#477bf4"));
                            mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                            mSuccessButton.setEnabled(false);
                        }else if (mNicknameEditText.getText().toString().trim().length() > 8){
                            mNnicknameWarningTextView.setText("닉네임은 여덟자까지 가능합니다.");
                            mNnicknameWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                            mNnicknameWarningTextView.setVisibility(View.VISIBLE);
                            mSuccessButton.setEnabled(false);
                        }else {
                            mNnicknameWarningTextView.setVisibility(View.INVISIBLE);
                        }
                    result = mNicknameEditText.getText().toString().trim();
                    mEditTextCountTextView.setText("" + mNicknameEditText.getText().toString().trim().length());
//                }else{
//                    mNicknameEditText.setText(result);
//                    mNicknameEditText.setSelection(result.length());
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mNicknameEditText.getText().toString().trim().length() > 0
                        && mEmailEditText.getText().toString().trim().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString().trim())
                        && itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                    mSuccessButton.setEnabled(true);
                    mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                } else {
                    mSuccessButton.setEnabled(false);
                    mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                }
            }
        });

        mNicknameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mNicknameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        && mNicknameEditText.getText().toString().trim().length() > 0
                        && mEmailEditText.getText().toString().trim().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString().trim())
                        && itemClass.checkNick(mNicknameEditText.getText().toString().trim())
                        && mNicknameEditText.getText().toString().trim().length() <= 8 && mNicknameEditText.getText().toString().trim().length() >= 2) {
                    mNicknameEditText.clearFocus();
                    hideKeyboard();
//                    mSuccessButton.performClick();


                } else {
                    mNicknameEditText.clearFocus();
                    hideKeyboard();
                }
                return true;
            }
        });
        mSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChbStaff.isChecked()){

                    TwoButtonDialog twoButtonDialog;
                    twoButtonDialog = new TwoButtonDialog(SnsSignupActivity.this,"의료진 가입안내","사용하는 이메일 주소를 입력해주세요.\n이메일을 통해 의료진 인증을 진행합니다.","재입력","확인");
                    twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                        @Override
                        public void OkButtonClick(View v) {
                            if (enableEmail && enableNickname) {
                                Intent i = new Intent(SnsSignupActivity.this, LoginUserInfoInsertActivity.class);
                                i.putExtra("LOGIN_TYPE", beforIntent.getIntExtra("LOGIN_TYPE", 2));    // 2 : sns 가입, 1 :  일반 이메일
                                i.putExtra("EMAIL", mEmailEditText.getText().toString());
                                i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                                i.putExtra("ID", beforIntent.getStringExtra("ID"));
                                if (mChbStaff.isChecked()){
                                    i.putExtra("STAFF",true);
                                }
                                startActivity(i);
                            }
                            twoButtonDialog.dismiss();
                        }
                    });

                    twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                        @Override
                        public void CancelButtonClick(View v) {
                            twoButtonDialog.dismiss();
                        }
                    });
                }else if (enableEmail && enableNickname) {
                    Intent i = new Intent(SnsSignupActivity.this, UserTypeChoiceActivity.class);
                    i.putExtra("LOGIN_TYPE", beforIntent.getIntExtra("LOGIN_TYPE", 2));    // 2 : sns 가입, 1 :  일반 이메일
                    i.putExtra("EMAIL", mEmailEditText.getText().toString());
                    i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                    i.putExtra("ID", beforIntent.getStringExtra("ID"));
                    startActivity(i);
                }
            }
        });
    }

    public void OneBtnPopup(Activity context, String title, String contents, String btnText) {

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.one_btn_popup, null);

        TextView txtTitle = (TextView) layout.findViewById(R.id.txt_one_btn_popup_title);
        TextView txtContents = (TextView) layout.findViewById(R.id.txt_one_btn_popup_contents);
        Button btnOk = (Button) layout.findViewById(R.id.btn_one_btn_popup_ok);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnOk.setText(btnText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateTimeDialog.setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enableEmail && enableNickname) {
                    Intent i = new Intent(SnsSignupActivity.this, LoginUserInfoInsertActivity.class);
                    i.putExtra("LOGIN_TYPE", beforIntent.getIntExtra("LOGIN_TYPE", 2));    // 2 : sns 가입, 1 :  일반 이메일
                    i.putExtra("EMAIL", mEmailEditText.getText().toString());
                    i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                    i.putExtra("ID", beforIntent.getStringExtra("ID"));
                    if (mChbStaff.isChecked()){
                        i.putExtra("STAFF",true);
                    }
                    startActivity(i);
                }
                dateTimeDialog.dismiss();
            }
        });

    }

    private void isNicknameCanUsed() {
        relaNickBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        mNnicknameWarningTextView.setText("사용가능한 닉네임입니다.");
        mNnicknameWarningTextView.setTextColor(Color.parseColor("#08d181"));
        mNnicknameWarningTextView.setVisibility(View.VISIBLE);
        if (mNicknameEditText.getText().toString().trim().length() > 0
                && mEmailEditText.getText().toString().trim().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString().trim())
                && itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
            mSuccessButton.setEnabled(true);
            mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {
            mSuccessButton.setEnabled(false);
            mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
        }
    }
    private void isNicknameCanNotUsed() {
        relaNickBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorDB7676));
        mNnicknameWarningTextView.setText("이미 사용중인 닉네임입니다.");
        mNnicknameWarningTextView.setTextColor(Color.parseColor("#DB7676"));
        mNnicknameWarningTextView.setVisibility(View.VISIBLE);
    }
    private void isEmailCanNotUsed() {
        linIdBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorDB7676));
        mEmailWarningTextView.setText("이미 사용중인 이메일입니다.");
        mEmailWarningTextView.setTextColor(Color.parseColor("#DB7676"));
        mEmailWarningTextView.setVisibility(View.VISIBLE);
    }
    private void isEmailCanUsed() {
        linIdBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        mEmailWarningTextView.setText("사용가능한 이메일입니다.");
        mEmailWarningTextView.setTextColor(Color.parseColor("#08d181"));
        mEmailWarningTextView.setVisibility(View.VISIBLE);
        if (mNicknameEditText.getText().toString().trim().length() > 0
                && mEmailEditText.getText().toString().trim().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString().trim())
                && itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
            mSuccessButton.setEnabled(true);
            mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {
            mSuccessButton.setEnabled(false);
            mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
        }
    }

    private void didBackPressOnEditText()
    {
        if(!mEmailEditText.getText().toString().trim().equals(""))
            NetworkCall(EMAIL_OVERLAP);
        if(mNicknameEditText.getText().toString().trim().length() >= 2)
            NetworkCall(NICK_NAME_OVERLAP);
        mNicknameEditText.clearFocus();
    }

    private BackPressEditText.OnBackPressListener onBackPressListener = new BackPressEditText.OnBackPressListener()
    {
        @Override
        public void onBackPress()
        {
            didBackPressOnEditText();
        }
    };
    private void hideKeyboard() {
        mInputMethdManager.hideSoftInputFromWindow(mNicknameEditText.getWindowToken(), 0);

    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(EMAIL_OVERLAP)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mEmailEditText.getText().toString().trim());
        }else if (mCode.equals(NICK_NAME_OVERLAP)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mNicknameEditText.getText().toString().trim());
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(NICK_NAME_OVERLAP)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                        // 사용 중
                        isNicknameCanNotUsed();
                        enableNickname = false;
                    }else {
                        isNicknameCanUsed();
                        enableNickname = true;
                    }
                }catch (JSONException e){
                }
            }else if (mCode.equals(EMAIL_OVERLAP)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                        // 사용 중
                        isEmailCanNotUsed();
                        enableEmail = false;
                    }else {
                        isEmailCanUsed();
                        enableEmail = true;
                    }
                }catch (JSONException e){

                }
            }
        }
    }
}