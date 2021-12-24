package com.kmw.soom2.Login.Activitys;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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

import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Item.ItemClass;
import com.kmw.soom2.Common.BackPressEditText;

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
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NICK_NAME_OVERLAP;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;

public class EmailSignupActivity extends AppCompatActivity implements AsyncResponse {
    final String TAG = "EmailSignupActivity";
    TextView mEmailWarningTextView, mPasswordWarningTextView, mNickNameWarningTextView;
    ImageView mBackButtonTextView;
    EditText mEmailEditText, mPasswordEditText;
    BackPressEditText mNicknameEditText;
    Button mSuccessButton;
    InputMethodManager mInputMethdManager;
    TextView mEditTextCountTextView;
    ItemClass itemClass = new ItemClass();
    Intent beforeIntent;
    String response;
    LinearLayout rootLayout;
    boolean enableEmail = false;
    boolean enableNickname = false;
    ProgressDialog progressDialog;
    ImageView mImagePwShow;
    LinearLayout linIdBg,linPwBg;
    RelativeLayout relaNickBg;

    TextView mTxtStaff;
    CheckBox mChbStaff;
    boolean mShowPwFlag = false;

//    Pattern pattern = Pattern.compile("^[ㄱ-ㅎ가-힣a-zA-Z0-9]*$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_signup);
        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

        beforeIntent = getIntent();
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);

        mInputMethdManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mEditTextCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_email_signup);
        mEmailWarningTextView = (TextView) findViewById(R.id.email_warning_activity_email_signup);
        mPasswordWarningTextView = (TextView) findViewById(R.id.password_warning_activity_email_signup);
        mNickNameWarningTextView = (TextView) findViewById(R.id.nickname_warning_activity_email_signup);
        mBackButtonTextView = (ImageView) findViewById(R.id.img_email_signup_back);
        mImagePwShow = findViewById(R.id.img_pw_show);

        linIdBg = findViewById(R.id.lin_email_sign_up_id);
        linPwBg = findViewById(R.id.lin_email_sign_up_pw);
        relaNickBg = findViewById(R.id.rela_email_sign_up_nick);

        mEmailEditText = (EditText) findViewById(R.id.email_edit_activity_email_signup);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_activity_email_signup);
        mNicknameEditText = (BackPressEditText) findViewById(R.id.nickname_edit_activity_email_signup);

        mTxtStaff = (TextView)findViewById(R.id.txt_email_sign_up_staff);
        mChbStaff = (CheckBox)findViewById(R.id.chb_email_sign_up_staff);

        mSuccessButton = (Button) findViewById(R.id.success_btn_activity_email_signup);
        mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
        mEmailEditText.clearFocus();
        mNicknameEditText.setOnBackPressListener(onBackPressListener);

        mBackButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());

        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mNicknameEditText.getWindowToken(),0);
            }
        });

        mChbStaff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mTxtStaff.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mSuccessButton.setText("다음 1/2");
                }else{
                    mTxtStaff.setTextColor(Color.parseColor("#909090"));
                    mSuccessButton.setText("다음 1/3");
                }
            }
        });

        mImagePwShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowPwFlag){
                    mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImagePwShow.setImageResource(R.drawable.ic_pw_off);
                    mShowPwFlag = false;
                    mPasswordEditText.setSelection(mPasswordEditText.getText().length());
                }else{
                    mPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    mImagePwShow.setImageResource(R.drawable.ic_pw_on);
                    mShowPwFlag = true;
                    mPasswordEditText.setSelection(mPasswordEditText.getText().length());
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
                    linIdBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorDB7676));
                } else if (mEmailEditText.getText().toString().trim().length() == 0) {
                    mEmailWarningTextView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        if (mPasswordEditText.length() > 0) {
//                            if (keyCode == KeyEvent.KEYCODE_DEL) {
//                                mPasswordEditText.setText("");
//                            }
//                        }
//                        return false;
//                    }
//                });
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPasswordEditText.getText().toString().trim().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString().trim())) {
                    mPasswordWarningTextView.setText("사용가능한 비밀번호입니다.");
                    mPasswordWarningTextView.setTextColor(Color.parseColor("#08d181"));
                    mPasswordWarningTextView.setVisibility(View.VISIBLE);
                    linPwBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                } else if (mPasswordEditText.getText().toString().trim().length() > 0 && !itemClass.checkPass(mPasswordEditText.getText().toString().trim())) {
                    mPasswordWarningTextView.setText("비밀번호 형식이 올바르지 않습니다.");
                    mPasswordWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                    mPasswordWarningTextView.setVisibility(View.VISIBLE);
                    linPwBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorDB7676));
                } else if (mPasswordEditText.getText().toString().trim().length() == 0) {
                    mPasswordWarningTextView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (mNicknameEditText.getText().toString().trim().length() > 0
                        && mPasswordEditText.getText().toString().trim().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString().trim())
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
        mNicknameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mNicknameEditText.setText(mNicknameEditText.getText().toString().trim());
                enableNickname = false;
                mSuccessButton.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                if (hasFocus) {
                    mNickNameWarningTextView.setVisibility(View.INVISIBLE);
                } else if (!hasFocus && mNicknameEditText.getText().toString().trim().length() > 0 && itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                    NetworkCall(NICK_NAME_OVERLAP);
                } else if (!hasFocus && mNicknameEditText.getText().toString().trim().length() > 0 && !itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                    mNickNameWarningTextView.setText("닉네임형식이 올바르지 않습니다.");
                    mNickNameWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                    mNickNameWarningTextView.setVisibility(View.VISIBLE);
                } else {
                    mNickNameWarningTextView.setVisibility(View.INVISIBLE);
                }
            }
        });

        mNicknameEditText.addTextChangedListener(new TextWatcher() {
            String result = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Matcher m = pattern.matcher(s.toString().trim());
                enableNickname = false;
//                if (m.matches()){
                    if (!TextUtils.isEmpty(s.toString().trim()) && !s.toString().trim().equals(result))
                        if (mNicknameEditText.getText().toString().trim().length() > 8) {
                            mNickNameWarningTextView.setText("닉네임은 여덟자까지 가능합니다.");
                            mNickNameWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                            mNickNameWarningTextView.setVisibility(View.VISIBLE);
                            mSuccessButton.setEnabled(false);
                        } else if (mNicknameEditText.getText().toString().trim().length() < 2 || !itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                            mNickNameWarningTextView.setText("닉네임은 두글자 이상이어야 합니다.");
                            mNickNameWarningTextView.setTextColor(Color.parseColor("#DB7676"));
                            mNickNameWarningTextView.setVisibility(View.VISIBLE);
                            mSuccessButton.setEnabled(false);
                        } else if (mNicknameEditText.getText().toString().trim().length() < 9 || mNicknameEditText.getText().toString().trim().length() > 1 || !itemClass.checkNick(mNicknameEditText.getText().toString().trim())) {
                            mNickNameWarningTextView.setText("키보드의 '완료' 눌러 중복체크 해주세요.");
                            mNickNameWarningTextView.setTextColor(Color.parseColor("#477bf4"));
                            mNickNameWarningTextView.setVisibility(View.VISIBLE);
                            mSuccessButton.setEnabled(false);
                        } else {
                            mNickNameWarningTextView.setVisibility(View.INVISIBLE);
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
                        && mPasswordEditText.getText().toString().trim().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString().trim())
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
                        && itemClass.checkNick(mNicknameEditText.getText().toString().trim())
                        && mPasswordEditText.getText().toString().trim().length() > 0 && itemClass.checkPass(mPasswordEditText.getText().toString().trim())
                        && mEmailEditText.getText().toString().trim().length() > 0 && itemClass.checkEmail(mEmailEditText.getText().toString().trim())
                        && mNicknameEditText.getText().toString().trim().length() < 8 && mNicknameEditText.getText().toString().trim().length() > 2) {
                    mNicknameEditText.clearFocus();
                    mSuccessButton.performClick();
                    hideKeyboard();

                } else {
                    mNicknameEditText.clearFocus();
                    hideKeyboard();
                }
                return false;
            }
        });

        mSuccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChbStaff.isChecked()){
                    TwoButtonDialog twoButtonDialog;
                    twoButtonDialog = new TwoButtonDialog(EmailSignupActivity.this,"의료진 가입안내","사용하는 이메일 주소를 입력해주세요.\n이메일을 통해 의료진 인증을 진행합니다.","재입력","확인");
                    twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                        @Override
                        public void OkButtonClick(View v) {
                            if (enableEmail && enableNickname) {
                                Intent i = new Intent(EmailSignupActivity.this, LoginUserInfoInsertActivity.class);
                                i.putExtra("LOGIN_TYPE", beforeIntent.getIntExtra("LOGIN_TYPE", 1));    // 2 : sns 가입, 1 :  일반 이메일
                                i.putExtra("EMAIL", mEmailEditText.getText().toString());
                                i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                                i.putExtra("ID", mEmailEditText.getText().toString());
                                i.putExtra("PASSWORD", mPasswordEditText.getText().toString());
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
                }else{
                    if (enableEmail && enableNickname) {
                        Intent i = new Intent(EmailSignupActivity.this, UserTypeChoiceActivity.class);
                        i.putExtra("LOGIN_TYPE", beforeIntent.getIntExtra("LOGIN_TYPE", 1));    // 2 : sns 가입, 1 :  일반 이메일
                        i.putExtra("EMAIL", mEmailEditText.getText().toString());
                        i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                        i.putExtra("ID", mEmailEditText.getText().toString());
                        i.putExtra("PASSWORD", mPasswordEditText.getText().toString());
                        startActivity(i);
                    }
                }
            }
        });
    }

    InputFilter inputFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
            if (!pattern.matcher(mNicknameEditText.getText().toString().trim()).matches()){
                return "";
            }
            return null;
        }
    };

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
                    Intent i = new Intent(EmailSignupActivity.this, LoginUserInfoInsertActivity.class);
                    i.putExtra("LOGIN_TYPE", beforeIntent.getIntExtra("LOGIN_TYPE", 1));    // 2 : sns 가입, 1 :  일반 이메일
                    i.putExtra("EMAIL", mEmailEditText.getText().toString());
                    i.putExtra("NICKNAME", mNicknameEditText.getText().toString());
                    i.putExtra("ID", mEmailEditText.getText().toString());
                    i.putExtra("PASSWORD", mPasswordEditText.getText().toString());
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
        mNickNameWarningTextView.setText("사용가능한 닉네임입니다.");
        mNickNameWarningTextView.setTextColor(Color.parseColor("#08d181"));
        mNickNameWarningTextView.setVisibility(View.VISIBLE);
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
        mNickNameWarningTextView.setText("이미 사용중인 닉네임입니다.");
        mNickNameWarningTextView.setTextColor(Color.parseColor("#FF7676"));
        mNickNameWarningTextView.setVisibility(View.VISIBLE);
    }

    private void isEmailCanNotUsed() {
        linIdBg.setBackgroundTintList(getResources().getColorStateList(R.color.colorDB7676));
        mEmailWarningTextView.setText("이미 사용중인 이메일입니다.");
        mEmailWarningTextView.setTextColor(Color.parseColor("#FF7676"));
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
                    } else {
                        isNicknameCanUsed();
                        enableNickname = true;
                    }
                } catch (JSONException e) {

                }
            }else if (mCode.equals(EMAIL_OVERLAP)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);

                    if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                        // 사용 중
                        isEmailCanNotUsed();
                        enableEmail = false;
                    } else {
                        isEmailCanUsed();
                        enableEmail = true;
                    }
                } catch (JSONException e) {
                }
            }
        }
    }
}