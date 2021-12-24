package com.kmw.soom2.ex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Activitys.AgreeActivity;
import com.kmw.soom2.Login.Activitys.EmailLoginActivity;
import com.kmw.soom2.Login.Activitys.EmailSignupActivity;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.Login.Activitys.SnsSignupActivity;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.R;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ID_OVERLAP;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_SNS_PROCESS;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.StartActivity;
import static com.kmw.soom2.Common.Utils.USER_INFO_SHARED;
import static com.kmw.soom2.MyPage.Activity.AccountActivity.mOAuthLoginInstance;

public class exNewAnotherActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String                      TAG = "NewAnotherLoginActivity";
    private ImageView mImageBack;
    private ImageView                   mImageKakao;
    private ImageView                   mImageNaver;
    private Button mButtonNaver;
    private TextView mTextSignUp;
    private TextView                    mTextLogin;
    private BottomSheetDialog mAgreeBottomSheetDialog;
    private View                        bottomSheetDialog_Agree_View;
    private LinearLayout mLinAgreeAll;
    private CheckBox mChbAgreeAll;
    private CheckBox                    mChbAgreeService;
    private CheckBox                    mChbAgreePersonal;
    private CheckBox                    mChbAgreeSensitivity;
    private CheckBox                    mChbAgreeMarketing;
    private Button                      mBtnAgreeDone;
    private ImageView                   mImageDetailService;
    private ImageView                   mImageDetailPersonal;
    private ImageView                   mImageDetailSensitivity;
    OAuthLoginButton mNaverOAuthLoginButton;

    ProgressDialog progressDialog;
    int flag = 0; //memo 1 - 이메일, 2 - SNS
    String mSnsIdText = "";
    String mSnsEmailText = "";
    int loginType;
    String email = "";
    String id;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private static String OAUTH_CLIENT_ID = "7WKQmVJi1ykWlETPeUzB";
    private static String OAUTH_CLIENT_SECRET = "qeMzoQNmf3";
    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_new_another);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        preferences = getSharedPreferences("preferences", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        mImageBack                      = findViewById(R.id.img_new_another_login_back);
        mImageKakao                     = findViewById(R.id.img_new_another_login_kakao_login);
        mImageNaver                     = findViewById(R.id.img_new_another_login_naver_login);
        mNaverOAuthLoginButton          = findViewById(R.id.btn_new_another_login_naver_login);
        mTextSignUp                     = findViewById(R.id.txt_new_another_login_sign_up);
        mTextLogin                      = findViewById(R.id.txt_new_another_login_login);

        mImageBack.setOnClickListener(this);
        mImageKakao.setOnClickListener(this);
        mImageNaver.setOnClickListener(this);
        mTextSignUp.setOnClickListener(this);
        mTextLogin.setOnClickListener(this);

        bottomSheetDialog_Agree_View    = getLayoutInflater().inflate(R.layout.agree_dialog,null);
        mAgreeBottomSheetDialog         = new BottomSheetDialog(exNewAnotherActivity.this);
        mLinAgreeAll                    = (LinearLayout)bottomSheetDialog_Agree_View.findViewById(R.id.lin_agree_check_all);
        mChbAgreeAll                    = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_all);
        mChbAgreeService                = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_service);
        mChbAgreePersonal               = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_personal);
        mChbAgreeSensitivity            = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_sensitivity);
        mChbAgreeMarketing              = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_marketing);
        mBtnAgreeDone                   = (Button)bottomSheetDialog_Agree_View.findViewById(R.id.btn_agree_done);
        mImageDetailService             = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_service);
        mImageDetailPersonal            = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_personal);
        mImageDetailSensitivity         = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_sensitivity);

        mAgreeBottomSheetDialog.setContentView(bottomSheetDialog_Agree_View);

        mChbAgreeAll.setOnClickListener(this);
        mChbAgreeService.setOnClickListener(this);
        mChbAgreePersonal.setOnClickListener(this);
        mChbAgreeSensitivity.setOnClickListener(this);
        mChbAgreeMarketing.setOnClickListener(this);
        mBtnAgreeDone.setOnClickListener(this);
        mImageDetailService.setOnClickListener(this);
        mImageDetailPersonal.setOnClickListener(this);
        mImageDetailSensitivity.setOnClickListener(this);
    }

    public void MarketingOneBtnPopup(Context context) {

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.marketing_one_btn_popup, null);

        TextView txtAgreeDate = (TextView) layout.findViewById(R.id.txt_marketing_one_btn_popup_contents);
        Button btnOk = (Button) layout.findViewById(R.id.btn_marketing_one_btn_popup_ok);

        txtAgreeDate.setText("동의일자 " + new SimpleDateFormat("yyyy.MM.dd").format(new Date(System.currentTimeMillis())));

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
                if (flag == 1){
                    Intent intent = new Intent(exNewAnotherActivity.this, EmailSignupActivity.class);
                    intent.putExtra("LOGIN_TYPE", 1);
                    startActivity(intent);
                }else if (flag == 2){
                    Intent i = new Intent(exNewAnotherActivity.this, SnsSignupActivity.class);
                    i.putExtra("ID", mSnsIdText);
                    i.putExtra("EMAIL", mSnsEmailText);
                    i.putExtra("LOGIN_TYPE", loginType);
                    startActivity(i);
                }
                dateTimeDialog.dismiss();
            }
        });
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {

        @Override
        public void run(boolean success) {

            if (success) {
                new Thread() {
                    public void run() {
                        String accessToken = mOAuthLoginInstance.getAccessToken(getApplicationContext());
                        String refreshToken = mOAuthLoginInstance.getRefreshToken(getApplicationContext());
                        String data = mOAuthLoginInstance.requestApi(getApplicationContext(), accessToken, "https://openapi.naver.com/v1/nid/me");
                        try {
                            JSONObject result = new JSONObject(data);
                            JSONObject jsonObject2 = (JSONObject) result.get("response");

                            email = jsonObject2.has("email") ? jsonObject2.getString("email") : "";
                            id = jsonObject2.getString("id");
                            loginType = 2;
                            NetworkCall(ID_OVERLAP);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }else{

            }
        }

    };

    Function2<OAuthToken, Throwable, Unit> callBack = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
            if (oAuthToken != null){
                //memo 로그인 되었을 때 처리
                Log.i(TAG,"로그인 되었음");
                updateKakaoLogin();
            }
            if (throwable != null){
                Toast.makeText(exNewAnotherActivity.this, "현재 카카오 로그인에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }

            return null;
        }
    };

    private void updateKakaoLogin(){
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if (user != null){ //로그인 상태
                    Log.i(TAG,"id : " + user.getId());
                    Log.i(TAG,"email : " + user.getKakaoAccount().getEmail());
                    id = String.valueOf(user.getId());
                    if (user.getKakaoAccount().getEmail() != null) {
                        email = user.getKakaoAccount().getEmail();
                    } else {
                        email = "";
                    }

                    editor.putString("email", email);
                    editor.apply();

                    loginType = 3;

                    NetworkCall(ID_OVERLAP);
                }else{
                    Toast.makeText(exNewAnotherActivity.this, "kakaoLogin : " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return null;
            }
        });
    }

    public void onClickLogout() {
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                return null;
            }
        });
    }

    void NetworkCall(String mCode){
        if (mCode.equals(ID_OVERLAP)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(id);
        }else if (mCode.equals(LOGIN_SNS_PROCESS)){
            if (progressDialog == null){
                progressDialog = new ProgressDialog(this,R.style.MyTheme);
                progressDialog.show();
            }
            if (Utils.TOKEN == null){
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                Utils.TOKEN = task.getResult();
                                new NetworkUtils.NetworkCall(exNewAnotherActivity.this,exNewAnotherActivity.this,TAG,mCode).execute(id,Utils.TOKEN);
                                Log.d(TAG, Utils.TOKEN);

                            }
                        });
            }else{
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(id,Utils.TOKEN);
            }
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(ID_OVERLAP)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                        // 사용 중
                        NetworkCall(LOGIN_SNS_PROCESS);
                    } else {
                        //memo 20201207 이용약관 팝업 추가
                        mAgreeBottomSheetDialog.show();
                        flag = 2;
                        mSnsIdText = id;
                        mSnsEmailText = email;
                    }
                } catch (JSONException e) {

                }
            }else if (mCode.equals(LOGIN_SNS_PROCESS)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
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
                            userItem.setLabelColor(JsonIsNullCheck(object,"LABEL_COLOR"));
                            userItem.setLabelName(JsonIsNullCheck(object,"LABEL_NAME"));
                            USER_INFO_SHARED(exNewAnotherActivity.this,userItem);

                            editor.putString("DEVICE_CODE", userItem.getDeviceCode());
                            editor.putString("USER_ID", userItem.getId());
                            editor.putString("USER_PASSWORD", userItem.getPassword());
                            editor.putInt("LOGIN_TYPE", userItem.getLoginType());
                            editor.putInt("OS_TYPE", userItem.getOsType());
                            editor.putInt("IS_LOGIN_FLAG", 1);
                            editor.putInt("LOGIN_FLAG",0);
                            editor.apply();

                            Intent intent = new Intent(exNewAnotherActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "SoomCare2");
                            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, userItem.getId());
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                            finish();
                            startActivity(intent);
                        } else if (JsonIntIsNullCheck(object, "ALIVE_FLAG") ==  2 && JsonIntIsNullCheck(object, "LV") == 60){
                            OneButtonDialog oneButtonDialog= new OneButtonDialog(this, "", "", "확인",true);
                            oneButtonDialog.setCancelable(false);
                            oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    oneButtonDialog.dismiss();
                                }
                            });
                        }else {
                            if (JsonIntIsNullCheck(object, "LOGIN_TYPE") == 3){
                                onClickLogout();
                            }
                            new OneButtonDialog(getApplicationContext(), "탈퇴한 계정", "탈퇴한 계정입니다.\n새로운 이메일의 소셜계정이나 이메일 가입하기를 이용해주세요.", "확인");
                        }
                    } else {

                    }
                } catch (JSONException e) {
                }
            }
        }
    }

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(getApplicationContext());
            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
            }
            return null;
        }
        protected void onPostExecute(Void v) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_new_another_login_back : {
                onBackPressed();
                break;
            }
            case R.id.img_new_another_login_kakao_login : {
                if(LoginClient.getInstance().isKakaoTalkLoginAvailable(this)){
                    LoginClient.getInstance().loginWithKakaoTalk(this, callBack);
                }else{
                    LoginClient.getInstance().loginWithKakaoAccount(this, callBack);
                }
                break;
            }
            case R.id.img_new_another_login_naver_login : {
                OAuthLogin.getInstance().enableWebViewLoginOnly();
                mOAuthLoginInstance = OAuthLogin.getInstance();
                mOAuthLoginInstance.showDevelopersLog(true);
                mOAuthLoginInstance.init(this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

                new DeleteTokenTask().execute();
                mOAuthLoginInstance.startOauthLoginActivity(this, mOAuthLoginHandler);
                mNaverOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
                break;
            }
            case R.id.txt_new_another_login_sign_up : {
                flag = 1;
                mAgreeBottomSheetDialog.show();
                break;
            }
            case R.id.txt_new_another_login_login : {
                StartActivity(this, EmailLoginActivity.class);
                break;
            }
            case R.id.chb_agree_all : {
                if (!mChbAgreeAll.isChecked()){
                    mChbAgreeService.setChecked(false);
                    mChbAgreePersonal.setChecked(false);
                    mChbAgreeSensitivity.setChecked(false);
                    mChbAgreeMarketing.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    mChbAgreeService.setChecked(true);
                    mChbAgreePersonal.setChecked(true);
                    mChbAgreeSensitivity.setChecked(true);
                    mChbAgreeMarketing.setChecked(true);
                    mBtnAgreeDone.setEnabled(true);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                }
                break;
            }
            case R.id.chb_agree_service: {
                if (!mChbAgreeService.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
//                    if (mChbAgreePersonal.isChecked()){
                    if (mChbAgreePersonal.isChecked() && mChbAgreeSensitivity.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_personal : {
                if (!mChbAgreePersonal.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
//                    if (mChbAgreeService.isChecked()){
                    if (mChbAgreeService.isChecked() && mChbAgreeSensitivity.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_sensitivity : {
                if (!mChbAgreeSensitivity.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreePersonal.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_marketing : {
                if (!mChbAgreeMarketing.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
//                    if (mChbAgreeService.isChecked() && mChbAgreePersonal.isChecked()){
                    if (mChbAgreeService.isChecked() && mChbAgreeSensitivity.isChecked() && mChbAgreePersonal.isChecked()){
                        mChbAgreeAll.setChecked(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                        mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                    }
                }
                break;
            }
            case R.id.img_agree_move_service : {
                Intent i = new Intent(this, AgreeActivity.class);
                i.putExtra("flag",1);
                startActivity(i);
                break;
            }
            case R.id.img_agree_move_personal : {
                Intent i = new Intent(this,AgreeActivity.class);
                i.putExtra("flag",2);
                startActivity(i);
                break;
            }
            case R.id.img_agree_move_sensitivity : {
                Intent i = new Intent(this,AgreeActivity.class);
                i.putExtra("flag",3);
                startActivity(i);
                break;
            }
            case R.id.btn_agree_done : {
                if (mChbAgreeMarketing.isChecked()){
                    mAgreeBottomSheetDialog.dismiss();
                    editor.putInt("marketing",1);
                    editor.apply();
                    MarketingOneBtnPopup(this);
                }else{
                    mAgreeBottomSheetDialog.dismiss();
                    if (flag == 1){
                        Intent intent = new Intent(exNewAnotherActivity.this, EmailSignupActivity.class);
                        intent.putExtra("LOGIN_TYPE", 1);
                        startActivity(intent);
                    }else if (flag == 2){
                        Intent i = new Intent(exNewAnotherActivity.this, SnsSignupActivity.class);
                        i.putExtra("ID", mSnsIdText);
                        i.putExtra("EMAIL", mSnsEmailText);
                        i.putExtra("LOGIN_TYPE", loginType);
                        startActivity(i);
                    }
                }
                break;
            }
        }
    }
}
