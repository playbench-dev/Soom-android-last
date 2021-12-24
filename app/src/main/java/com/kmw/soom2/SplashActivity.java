package com.kmw.soom2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.messaging.FirebaseMessaging;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kmw.soom2.Common.Item.ForeignKeys;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.UpdateHelper;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.MedicineTypeItem;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.Login.Activitys.LoginSignUpSelectActivity;
import com.kmw.soom2.Login.Activitys.NewWorkThroughActivity;
import com.kmw.soom2.Login.Item.UserItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.BLOG_BANNER;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_MENU_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HASH_TAG_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_SNS_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_CALL_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_TYPE_CALL_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.RUTIN_BANNER;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SYMPTOM_MANAGE_LINK;
import static com.kmw.soom2.Common.Utils.COMMUNITY_BLOG_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LV_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_PARENT_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_RUTIN_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECTABLE;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.LOCATION_PERMISSION;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.MEDICINE_TYPE_LIST;
import static com.kmw.soom2.Common.Utils.StartActivity;
import static com.kmw.soom2.Common.Utils.USER_INFO_SHARED;

public class SplashActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckListener, AsyncResponse {

    private static final Logger LOG = LoggerFactory.getLogger(SplashActivity.class);
    private String TAG = "SplashActivity";
    private ImageView mImageAnimation;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String response;
    Intent beforeIntent;
    OneButtonDialog oneButtonDialog;
    Handler handler = new Handler();
    double i;
    Dialog dialog;
    boolean updateOne = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.i(TAG,"test");
        beforeIntent = getIntent();

        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        mImageAnimation = (ImageView)findViewById(R.id.img_splash_animation);

        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) 1; //0.85 small size, 1 normal size, 1,15 big etc
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        configuration.densityDpi = (int) getResources().getDisplayMetrics().xdpi;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setFillAfter(true);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new LinearInterpolator());
        mImageAnimation.startAnimation(rotate);

//        Intent intent = new Intent(this, ImageCropTestActivity.class);
//        startActivity(intent);
        UpdateHelper.with(this).onUpdateCheck(this).check();
    }

    void LoginProcess(){
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
                        Log.d(TAG, Utils.TOKEN);

                    }
                });

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
            TedPermission.with(this)
                    .setPermissionListener(permissionListener)
                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                    .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    .check();
        }else{
            check();
        }

    }

    public void OneBtnPopup(Context context, String title, String contents, String btnText) {

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
                dateTimeDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            LOCATION_PERMISSION = true;

            if (pref.getInt("IS_LOGIN_FLAG", 0) == 1) {
                if (pref.getInt("LOGIN_TYPE", 0) == 1 || pref.getInt("LOGIN_TYPE", 0) == 0){
                    NetworkCall(LOGIN_PROCESS);
                }else{
                    NetworkCall(LOGIN_SNS_PROCESS);
                }
            } else {
//                if (MEDICINE_LIST == null){
                    NetworkCall(MEDICINE_CALL_LIST);
//                }else{
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            NetworkCall(MEDICINE_TYPE_CALL_LIST);
//                        }
//                    },1500);
//                }
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            if (deniedPermissions.contains("android.permission.ACCESS_FINE_LOCATION") || deniedPermissions.contains("android.permission.ACCESS_COARSE_LOCATION")) {
                LOCATION_PERMISSION = false;
            }

            if (pref.getInt("IS_LOGIN_FLAG", 0) == 1) {
                if (pref.getInt("LOGIN_TYPE", 0) == 1 || pref.getInt("LOGIN_TYPE", 0) == 0){
                    NetworkCall(LOGIN_PROCESS);
                }else{
                    NetworkCall(LOGIN_SNS_PROCESS);
                }
            } else {
//                if (MEDICINE_LIST == null){
                    NetworkCall(MEDICINE_CALL_LIST);
//                }else{
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            NetworkCall(MEDICINE_TYPE_CALL_LIST);
//                        }
//                    },1500);
//                }
            }
        }
    };

    @Override
    public void onUpdateCheckListener(String updateStatus, boolean necessaryUpdate) {
        Log.i(TAG,"status update : " + updateStatus);

        if (updateStatus.equals("true")){
            final String appPackageName = getPackageName();
            final Dialog dialog = new BottomSheetDialog(SplashActivity.this, R.style.SheetDialog);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentView = inflater.inflate(R.layout.update_dialog, null);

            Button btnDone = (Button)contentView.findViewById(R.id.btn_update_popup_done);
            Button btnCancel = (Button)contentView.findViewById(R.id.btn_update_popup_cancel);
            LinearLayout linLine = (LinearLayout)contentView.findViewById(R.id.lin_update_popup_line);

            dialog.setContentView(contentView);
            dialog.setCancelable(false);
            dialog.show();

            if (necessaryUpdate){
                btnCancel.setVisibility(View.GONE);
                linLine.setVisibility(View.GONE);
            }else{
                btnCancel.setVisibility(View.VISIBLE);
                linLine.setVisibility(View.VISIBLE);
            }

            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    LoginProcess();
                }
            });
        }else if (updateOne == false){
            LoginProcess();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234:
                if (pref.getInt("IS_LOGIN_FLAG", 0) == 1) {
                    if (pref.getInt("LOGIN_TYPE", 0) == 1 || pref.getInt("LOGIN_TYPE", 0) == 0){
                        NetworkCall(LOGIN_PROCESS);
                    }else{
                        NetworkCall(LOGIN_SNS_PROCESS);
                    }
                } else {
//                    if (MEDICINE_LIST == null){
                        NetworkCall(MEDICINE_CALL_LIST);
//                    }else{
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                NetworkCall(MEDICINE_TYPE_CALL_LIST);
//                            }
//                        },1500);
//                    }
                }
            default:
                break;
        }
    }

    //오드로이드 용
    private void check() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 1234);
        }else{
            if (pref.getInt("IS_LOGIN_FLAG", 0) == 1) {
                if (pref.getInt("LOGIN_TYPE", 0) == 1 || pref.getInt("LOGIN_TYPE", 0) == 0){
                    NetworkCall(LOGIN_PROCESS);
                }else{
                    NetworkCall(LOGIN_SNS_PROCESS);
                }
            } else {
//                if (MEDICINE_LIST == null){
                    NetworkCall(MEDICINE_CALL_LIST);
//                }else{
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            NetworkCall(MEDICINE_TYPE_CALL_LIST);
//                        }
//                    },1500);
//                }
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(LOGIN_PROCESS)){
            if (Utils.TOKEN == null){
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    new NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode).execute(pref.getString("USER_ID", ""),pref.getString("USER_PASSWORD", ""),"");
//                                    NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode,pref.getString("USER_ID", ""),pref.getString("USER_PASSWORD", ""),"");
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                Utils.TOKEN = task.getResult();
                                new NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode).execute(pref.getString("USER_ID", ""),pref.getString("USER_PASSWORD", ""),Utils.TOKEN);
//                                NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode,pref.getString("USER_ID", ""),pref.getString("USER_PASSWORD", ""),Utils.TOKEN);
                                Log.d(TAG, Utils.TOKEN);

                            }
                        });

            }else{
                Log.i(TAG,"token : " + Utils.TOKEN);
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(pref.getString("USER_ID", ""),pref.getString("USER_PASSWORD", ""),Utils.TOKEN);
//                NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode,pref.getString("USER_ID", ""),pref.getString("USER_PASSWORD", ""),Utils.TOKEN);
            }
        }else if (mCode.equals(LOGIN_SNS_PROCESS)){
            if (Utils.TOKEN == null){
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    new NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode).execute(pref.getString("USER_ID", ""),"");
//                                    NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode,pref.getString("USER_ID", ""),"");
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                Utils.TOKEN = task.getResult();
                                new NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode).execute(pref.getString("USER_ID", ""),Utils.TOKEN);
//                                NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode,pref.getString("USER_ID", ""),Utils.TOKEN);
                                Log.d(TAG, Utils.TOKEN);

                            }
                        });
            }else{
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(pref.getString("USER_ID", ""),Utils.TOKEN);
//                NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode,pref.getString("USER_ID", ""),Utils.TOKEN);
            }
        }else if (mCode.equals(MEDICINE_CALL_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
            Log.i(TAG,"로그인 후 호출11");
//            NetworkUtils.NetworkCall(SplashActivity.this,SplashActivity.this,TAG,mCode);
        }else if (mCode.equals(MEDICINE_TYPE_CALL_LIST) || mCode.equals(SYMPTOM_MANAGE_LINK) || mCode.equals(HASH_TAG_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else if (mCode.equals(COMMUNITY_MENU_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else if (mCode.equals(RUTIN_BANNER)){
            new NetworkUtils.NetworkCall(this,this,TAG,RUTIN_BANNER).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else if (mCode.equals(BLOG_BANNER)){
            new NetworkUtils.NetworkCall(this,this,TAG,BLOG_BANNER).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(LOGIN_PROCESS) || mCode.equals(LOGIN_SNS_PROCESS)){
                Log.i(TAG,"login : " + mResult);
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
                            userItem.setLabelColor(JsonIsNullCheck(object,"LABEL_COLOR"));
                            userItem.setLabelName(JsonIsNullCheck(object,"LABEL_NAME"));
                            userItem.setPhone(JsonIsNullCheck(object, "PHONE"));
                            userItem.setEssentialPermissionFlag(JsonIntIsNullCheck(object,"ESSENTIAL_PERMISSION_FLAG"));
                            userItem.setMarketingPermissionFlag(JsonIntIsNullCheck(object,"MARKETING_PERMISSION_FLAG"));

                            USER_INFO_SHARED(SplashActivity.this,userItem);

                            editor.putString("DEVICE_CODE", JsonIsNullCheck(object, "DEVICE_CODE"));
                            editor.putString("USER_ID", JsonIsNullCheck(object, "ID"));
                            editor.putString("USER_PASSWORD", JsonIsNullCheck(object, "PASSWORD"));
                            editor.putInt("LOGIN_TYPE", JsonIntIsNullCheck(object, "LOGIN_TYPE"));
                            editor.putInt("OS_TYPE", JsonIntIsNullCheck(object, "OS_TYPE"));

                            editor.putInt("IS_LOGIN_FLAG", 1);  // 1 : 로그인 됨
                            editor.apply();

//                            if (MEDICINE_LIST == null){
                            Log.i(TAG,"로그인 후 호출");
                                NetworkCall(MEDICINE_CALL_LIST);
//                            }else{
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        NetworkCall(MEDICINE_TYPE_CALL_LIST);
//                                    }
//                                },1500);
//                            }

                        } else {
                            if (pref.getBoolean("workThrough",false)){
                                StartActivity(SplashActivity.this, LoginSignUpSelectActivity.class);
                                finish();
                            }else{
                                StartActivity(SplashActivity.this, NewWorkThroughActivity.class);
                                finish();
                            }
                        }
                    } else {
                        if (pref.getBoolean("workThrough",false)){
                            StartActivity(SplashActivity.this, LoginSignUpSelectActivity.class);
                            finish();
                        }else{
                            StartActivity(SplashActivity.this, NewWorkThroughActivity.class);
                            finish();
                        }
                    }
                } catch (JSONException e) {
                    if (pref.getBoolean("workThrough",false)){
                        StartActivity(SplashActivity.this, LoginSignUpSelectActivity.class);
                        finish();
                    }else{
                        StartActivity(SplashActivity.this, NewWorkThroughActivity.class);
                        finish();
                    }
                }
            }else if (mCode.equals(MEDICINE_CALL_LIST)){

                MEDICINE_LIST = new ArrayList<>();
                Log.i(TAG,"medicine type list : " + mResult);
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        //notice_list 테이블의 PUSH_FLAG 1인 경우 push_alarm_list 테이블에 저장 (PUSH_MESSAGE 값을 push_alarm_list의 CONTENTS로 저장)

                        MedicineTakingItem medicineTakingItem = new MedicineTakingItem();

                        medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                        medicineTakingItem.setMedicineKo(JsonIsNullCheck(object, "KO"));
                        medicineTakingItem.setMedicineEn(JsonIsNullCheck(object, "EN"));
                        medicineTakingItem.setManufacturer(JsonIsNullCheck(object, "MANUFACTURER"));
                        medicineTakingItem.setIngredient(JsonIsNullCheck(object, "INGREDIENT"));
                        medicineTakingItem.setForm(JsonIsNullCheck(object, "FORM"));
                        medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object, "MEDICINE_TYPE_NO"));
                        medicineTakingItem.setStorageMethod(JsonIsNullCheck(object, "STORAGE_METHOD"));
                        medicineTakingItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                        medicineTakingItem.setEfficacy(JsonIsNullCheck(object, "EFFICACY"));
                        medicineTakingItem.setInformation(JsonIsNullCheck(object, "INFORMATION"));
                        medicineTakingItem.setInstructions(JsonIsNullCheck(object, "INSTRUCTIONS"));
                        medicineTakingItem.setStabiltyRationg(JsonIsNullCheck(object, "STABILITY_RATING"));
                        medicineTakingItem.setPrecaution(JsonIsNullCheck(object, "PRECAUTIONS"));
                        medicineTakingItem.setBookMark(JsonIntIsNullCheck(object, "BOOKMARK"));
                        medicineTakingItem.setMedicineImg(JsonIsNullCheck(object, "MEDICINE_IMG"));
                        medicineTakingItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        medicineTakingItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));

                        MEDICINE_LIST.add(medicineTakingItem);
                    }

                    NetworkCall(MEDICINE_TYPE_CALL_LIST);
                    NetworkCall(COMMUNITY_MENU_LIST);
                    NetworkCall(RUTIN_BANNER);
                    NetworkCall(BLOG_BANNER);
                    NetworkCall(SYMPTOM_MANAGE_LINK);
                    NetworkCall(HASH_TAG_LIST);

                } catch (JSONException e) {

                }
            }else if (mCode.equals(MEDICINE_TYPE_CALL_LIST)){
                MEDICINE_TYPE_LIST = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.i(TAG,"object : " + object.toString());

                        MedicineTypeItem medicineTypeItem = new MedicineTypeItem();

                        medicineTypeItem.setMedicineTypeNo(JsonIntIsNullCheck(object, "MEDICINE_TYPE_NO"));
                        medicineTypeItem.setTypeImg(JsonIsNullCheck(object, "TYPE_IMG"));

                        MEDICINE_TYPE_LIST.add(medicineTypeItem);
                    }

//                    NetworkCall(COMMUNITY_MENU_LIST);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (mCode.equals(COMMUNITY_MENU_LIST)){
                try {
                    COMMUNITY_MUNU_NO_LIST = new ArrayList<>();
                    COMMUNITY_LV_LIST = new ArrayList<>();
                    COMMUNITY_NAME_LIST = new ArrayList<>();
                    COMMUNITY_PARENT_LIST = new ArrayList<>();
                    COMMUNITY_SELECTABLE = new ArrayList<>();
                    COMMUNITY_POPULARUTY = new ArrayList<>();
                    Log.i(TAG,"mResult : " + mResult);

                    JSONObject jsonObject = new JSONObject(mResult);

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    int cnt = 0;

                    for (int i = 0; i < jsonArray.length(); i++){
                        COMMUNITY_MUNU_NO_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"MENU_NO"));
                        COMMUNITY_LV_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"LV"));
                        COMMUNITY_NAME_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"NAME"));
                        COMMUNITY_PARENT_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"PARENT"));
                        COMMUNITY_SELECTABLE.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"SELECTABLE"));
                        COMMUNITY_POPULARUTY.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"POPULARITY"));
                    }
//                    NetworkCall(RUTIN_BANNER);
                }catch (JSONException e){
//                    NetworkCall(RUTIN_BANNER);
                }

            }else if (mCode.equals(RUTIN_BANNER)){
                try{
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.has("list")){
                        COMMUNITY_RUTIN_BANNER_LIST = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                                BannerItem bannerItem = new BannerItem();
                                bannerItem.setBannerNo(JsonIsNullCheck(object, "BANNER_NO"));
                                bannerItem.setBannerType(JsonIsNullCheck(object, "BANNER_TYPE"));
                                bannerItem.setBannerLink(JsonIsNullCheck(object, "BANNER_LINK"));
                                bannerItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                bannerItem.setPriority(JsonIntIsNullCheck(object, "PRIORITY"));
                                bannerItem.setBannerTitle(JsonIsNullCheck(object,"BANNER_TITLE"));

                                COMMUNITY_RUTIN_BANNER_LIST.add(bannerItem);
                            }
                        }

                    }
//                    NetworkCall(BLOG_BANNER);
                }catch (JSONException e){
//                    NetworkCall(BLOG_BANNER);
                }
            }else if (mCode.equals(BLOG_BANNER)){
                try{
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.has("list")){
                        COMMUNITY_BLOG_BANNER_LIST = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                                BannerItem bannerItem = new BannerItem();
                                bannerItem.setBannerNo(JsonIsNullCheck(object, "BANNER_NO"));
                                bannerItem.setBannerType(JsonIsNullCheck(object, "BANNER_TYPE"));
                                bannerItem.setBannerLink(JsonIsNullCheck(object, "BANNER_LINK"));
                                bannerItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                bannerItem.setPriority(JsonIntIsNullCheck(object, "PRIORITY"));
                                bannerItem.setBannerTitle(JsonIsNullCheck(object,"BANNER_TITLE"));

                                COMMUNITY_BLOG_BANNER_LIST.add(bannerItem);
                            }
                        }
                    }
//                    NetworkCall(SYMPTOM_MANAGE_LINK);
                }catch (JSONException e){
//                    NetworkCall(SYMPTOM_MANAGE_LINK);
                }
            }else if (mCode.equals(SYMPTOM_MANAGE_LINK)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.length() > 0) {
                        ArrayList<ForeignKeys> items = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            ForeignKeys item = new ForeignKeys();

                            item.setAliveFlag(JsonIntIsNullCheck(object, "ALIVE_FLAG"));
                            item.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                            item.setLinkNo(JsonIntIsNullCheck(object, "LINK_NO"));
                            item.setLinkUrl(JsonIsNullCheck(object, "LINK_URL"));
                            item.setTitle(JsonIsNullCheck(object, "TITLE"));

                            items.add(item);
                        }
                        Utils.linkKeys = items;
//                        NetworkCall(HASH_TAG_LIST);
                    } else {

                    }
                } catch (JSONException e) {

                }
            }else if (mCode.equals(HASH_TAG_LIST)){
                try {
                    Utils.tagList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (!JsonIsNullCheck(object, "TITLE").equals("")) {
                            Utils.tagList.add("#" + JsonIsNullCheck(object, "TITLE"));
                        }
                    }

                    if (pref.getInt("IS_LOGIN_FLAG", 0) == 1){
                        Intent i = null;
                        if (beforeIntent.hasExtra("medicineAlarm")) {
                            i = new Intent(SplashActivity.this, MedicinRecordActivity.class);
                            i.putExtra("medicineAlarm", true);
                        } else if (beforeIntent.hasExtra("android_channel_id")) {
                            Log.i(TAG,"android_channel_id : " + beforeIntent.getStringExtra("android_channel_id"));
                            i = new Intent(SplashActivity.this, MainActivity.class);
                            i.putExtra("notification",true);
                            i.putExtra("android_channel_id",beforeIntent.getStringExtra("android_channel_id"));
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            if (beforeIntent.getStringExtra("android_channel_id").equals("community")) {
//                                if (beforeIntent.hasExtra("communityNo")) {
//                                    Log.i(TAG,"communityNo : " + beforeIntent.getStringExtra("no"));
//                                    i = new Intent(SplashActivity.this, NewCommunityDetailActivity.class);
//                                    i.putExtra("communityNo", beforeIntent.getStringExtra("no"));
//                                    i.putExtra("push", true);
//                                } else {
//                                    i = new Intent(SplashActivity.this, MainActivity.class);
//                                    i.putExtra("community", true);
//                                    i.putExtra("push", true);
//                                }
//                                i = new Intent(SplashActivity.this, NewCommunityDetailActivity.class);
                                i.putExtra("communityNo", beforeIntent.getStringExtra("no"));
                                i.putExtra("push", true);
                            } else if (beforeIntent.getStringExtra("android_channel_id").equals("home")){
//                                i = new Intent(SplashActivity.this, PushAlarmListActivity.class);
                                i.putExtra("push", true);
                            }else if (beforeIntent.getStringExtra("android_channel_id").equals("notice")) {
//                                i = new Intent(SplashActivity.this, NoticeActivity.class);
                                i.putExtra("noticeNo", beforeIntent.getStringExtra("no"));
                                i.putExtra("body", beforeIntent.getStringExtra("body"));
                                i.putExtra("push", true);
                            } else if (beforeIntent.getStringExtra("android_channel_id").equals("dosing")) {
                                // memo: 2021-01-14 김지훈 수정 시작
//                                i = new Intent(SplashActivity.this, MedicineSelectActivity.class);
                                // memo: 2021-01-14 김지훈 수정 종료
                                i.putExtra("body", beforeIntent.getStringExtra("body"));
                                i.putExtra("push", true);
                                i.putExtra("keyNo", beforeIntent.getStringExtra("no"));
                                i.putExtra("medicineInsert", true);
                            } else if (beforeIntent.getStringExtra("android_channel_id").equals("symptom")){
//                                i = new Intent(SplashActivity.this, NewSymptomRecordActivity.class);
                                i.putExtra("body", beforeIntent.getStringExtra("body"));
                                i.putExtra("push", true);
                            }
                        } else {
                            i = new Intent(SplashActivity.this, MainActivity.class);
                        }
                        startActivity(i);
                        finish();
                    }else{
                        if (pref.getBoolean("workThrough",false)){
                            StartActivity(SplashActivity.this, LoginSignUpSelectActivity.class);
                            finish();
                        }else{
                            StartActivity(SplashActivity.this, NewWorkThroughActivity.class);
                            finish();
                        }
                    }
                } catch (JSONException e) {

                }
            }else if (mCode.equals("code")){
                Log.i(TAG,"mResult : " + mResult);
            }
        }else{
            Toast.makeText(this, "서버와의 연결이 불안정합니다. 잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
