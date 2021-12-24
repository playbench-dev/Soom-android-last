package com.kmw.soom2.Login.Activitys;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.sdk.user.UserApiClient;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.R;
import com.kmw.soom2.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_PROCESS;
import static com.kmw.soom2.Common.Utils.AllEtcItemArrayList;
import static com.kmw.soom2.Common.Utils.AllKoList;
import static com.kmw.soom2.Common.Utils.AllList;
import static com.kmw.soom2.Common.Utils.AllRegisterDtList;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LEFT_PAGEING;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LEFT_SEARCH_TOTAL_PAGE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_RIGHT_PAGEING;
import static com.kmw.soom2.Common.Utils.COMMUNITY_RIGHT_SEARCH_TOTAL_PAGE;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.StartActivity;
import static com.kmw.soom2.Common.Utils.USER_INFO_SHARED;
import static com.kmw.soom2.Common.Utils.etcItemArrayList;
import static com.kmw.soom2.Common.Utils.hisItems;
import static com.kmw.soom2.Common.Utils.itemsCommentArrayList;
import static com.kmw.soom2.Common.Utils.itemsNoticeArrayList;
import static com.kmw.soom2.Common.Utils.itemsScrabArrayList;
import static com.kmw.soom2.Common.Utils.itemsWriteArrayList;
import static com.kmw.soom2.Common.Utils.koList;
import static com.kmw.soom2.Common.Utils.registerDtList;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "EmailLoginActivity";
    ImageView imgBack,imgPwHide;
    EditText edtId,edtPw;
    TextView txtFindId,txtFindPw,txtSignUp;
    Button btnFinish;
    LinearLayout linParent;
    private FirebaseAnalytics mFirebaseAnalytics;
    ProgressDialog progressDialog;

    boolean mShowPwFlag = false;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private BottomSheetDialog           mAgreeBottomSheetDialog;
    private View                        bottomSheetDialog_Agree_View;
    private LinearLayout                mLinAgreeAll;
    private CheckBox                    mChbAgreeAll;
    private CheckBox                    mChbAgreeService;
    private CheckBox                    mChbAgreePersonal;
    private CheckBox                    mChbAgreeSensitivity;
    private CheckBox                    mChbAgreeMarketing;
    private Button                      mBtnAgreeDone;
    private ImageView                   mImageDetailService;
    private ImageView                   mImageDetailPersonal;
    private ImageView                   mImageDetailSensitivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        FindViewById();

    }

    void FindViewById(){
        imgBack = (ImageView)findViewById(R.id.img_email_login_back);
        imgPwHide = (ImageView)findViewById(R.id.img_email_login_pw_hide);
        edtId = (EditText)findViewById(R.id.edt_email_login_id);
        edtPw = (EditText)findViewById(R.id.edt_email_login_pw);
        txtFindId = (TextView)findViewById(R.id.txt_email_login_id_search);
        txtFindPw = (TextView)findViewById(R.id.txt_email_login_pw_search);
        txtSignUp = findViewById(R.id.txt_email_login_sign_up);
        btnFinish = (Button)findViewById(R.id.btn_email_login_finish);
        linParent = (LinearLayout)findViewById(R.id.lin_email_login_parent);

        imgBack.setOnClickListener(this);
        imgPwHide.setOnClickListener(this);
        txtFindId.setOnClickListener(this);
        txtFindPw.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        linParent.setOnClickListener(this);

        bottomSheetDialog_Agree_View    = getLayoutInflater().inflate(R.layout.agree_dialog,null);
        mAgreeBottomSheetDialog         = new BottomSheetDialog(EmailLoginActivity.this);
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
                Intent intent = new Intent(EmailLoginActivity.this, EmailSignupActivity.class);
                intent.putExtra("LOGIN_TYPE", 1);
                startActivity(intent);
                dateTimeDialog.dismiss();
            }
        });
    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(LOGIN_PROCESS)){
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
                                new NetworkUtils.NetworkCall(EmailLoginActivity.this,EmailLoginActivity.this,TAG,mCode).execute(edtId.getText().toString(),edtPw.getText().toString(),Utils.TOKEN);
                                Log.d(TAG, Utils.TOKEN);

                            }
                        });
            }else{
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(edtId.getText().toString(),edtPw.getText().toString(),Utils.TOKEN);
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
            if (mCode.equals(LOGIN_PROCESS)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);

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

                    if (jsonObject.has("list")){
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
                                userItem.setPhone(JsonIsNullCheck(object,"PHONE"));
                                userItem.setLabelColor(JsonIsNullCheck(object,"LABEL_COLOR"));
                                userItem.setLabelName(JsonIsNullCheck(object,"LABEL_NAME"));
                                userItem.setEssentialPermissionFlag(JsonIntIsNullCheck(object,"ESSENTIAL_PERMISSION_FLAG"));
                                userItem.setMarketingPermissionFlag(JsonIntIsNullCheck(object,"MARKETING_PERMISSION_FLAG"));

                                USER_INFO_SHARED(EmailLoginActivity.this,userItem);

                                editor.putString("DEVICE_CODE",JsonIsNullCheck(object, "DEVICE_CODE"));
                                editor.putString("USER_ID", JsonIsNullCheck(object, "ID"));
                                editor.putString("USER_PASSWORD", JsonIsNullCheck(object, "PASSWORD"));
                                editor.putInt("LOGIN_TYPE", JsonIntIsNullCheck(object, "LOGIN_TYPE"));
                                editor.putInt("OS_TYPE", JsonIntIsNullCheck(object, "OS_TYPE"));
                                editor.putInt("IS_LOGIN_FLAG", 1);  // 1 : 로그인 됨

                                editor.apply();

                                Intent i = new Intent(EmailLoginActivity.this,MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(FirebaseAnalytics.Param.METHOD, "SoomCare2");
                                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Utils.userItem.getId());
                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                onBackPressed();

                            }else if (JsonIntIsNullCheck(object, "ALIVE_FLAG") ==  2 && JsonIntIsNullCheck(object, "LV") == 60){
                                OneButtonDialog oneButtonDialog= new OneButtonDialog(this, "", "", "확인",true);
                                oneButtonDialog.setCancelable(false);
                                oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                    @Override
                                    public void OnButtonClick(View v) {
                                        oneButtonDialog.dismiss();
                                    }
                                });
                            }else{
                                if (JsonIntIsNullCheck(object, "LOGIN_TYPE") == 3){
                                    onClickLogout();
                                }
                                new OneButtonDialog(EmailLoginActivity.this, "탈퇴한 계정", "탈퇴한 계정입니다.\n새로운 이메일의 소셜계정이나 이메일 가입하기를 이용해주세요.", "확인");
                            }
                        }
                    }else{
                        new OneButtonDialog(EmailLoginActivity.this,"로그인 오류","아이디와 비밀번호를 확인해주세요.","확인");
                    }

                }catch (JSONException e){

                }
            }
        }
    }

    public void onClickLogout() {
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                return null;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_email_login_back : {
                onBackPressed();
                break;
            }
            case R.id.img_email_login_pw_hide : {
                if (mShowPwFlag){
                    edtPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imgPwHide.setImageResource(R.drawable.ic_pw_off);
                    mShowPwFlag = false;
                    edtPw.setSelection(edtPw.getText().length());
                }else{
                    edtPw.setInputType(InputType.TYPE_CLASS_TEXT);
                    imgPwHide.setImageResource(R.drawable.ic_pw_on);
                    mShowPwFlag = true;
                    edtPw.setSelection(edtPw.getText().length());
                }
                break;
            }
            case R.id.txt_email_login_id_search : {
                StartActivity(this, SearchIdActivity.class);
                break;
            }
            case R.id.txt_email_login_pw_search : {
                StartActivity(this, SearchPwActivity.class);
                break;
            }
            case R.id.txt_email_login_sign_up : {
                mAgreeBottomSheetDialog.show();
                break;
            }
            case R.id.btn_email_login_finish : {
                NetworkCall(LOGIN_PROCESS);
//                if (edtId.getText().toString().length() > 0 && edtPw.getText().toString().length() > 0) {
//                    NetworkCall(LOGIN_PROCESS);
//                }else {
//                    new OneButtonDialog(this,"로그인 오류","아이디와 비밀번호를 확인해주세요.","확인");
//                }
                break;
            }
            case R.id.lin_email_login_parent : {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(edtPw.getWindowToken(),0);
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
                Intent i = new Intent(this,AgreeActivity.class);
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
                    Intent intent = new Intent(EmailLoginActivity.this, EmailSignupActivity.class);
                    intent.putExtra("LOGIN_TYPE", 1);
                    startActivity(intent);
                }
                break;
            }
        }
    }
}
