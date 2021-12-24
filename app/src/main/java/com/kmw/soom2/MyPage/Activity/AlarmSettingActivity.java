package com.kmw.soom2.MyPage.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.sdk.user.UserApiClient;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Activitys.LoginSignUpSelectActivity;
import com.kmw.soom2.MyPage.Item.AlarmSettingItem;
import com.kmw.soom2.R;
import com.nhn.android.naverlogin.OAuthLogin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.AGREE_UPDATE;
import static com.kmw.soom2.Common.Utils.CustomToast;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class AlarmSettingActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {
    Switch asthmaAlarmSwitch, noticeSwitch, likeSwitch, commentSwitch,
            commentReplySwitch,swhMarketing,allCommunityAlarmSwitch;
    ImageView imgQuestion,imgSettingMove;
    LinearLayout linQuestionVisible;
    TextView secessionTextView;

    final String TAG = "AlarmSettingActivity";
    String response;

    AlarmSettingItem alarmSettingItem;

    TextView btnBack;
    TwoButtonDialog twoButtonDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public OAuthLogin mOAuthLoginInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        preferences = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = preferences.edit();

        btnBack = findViewById(R.id.back_text_view_activity_attending_hospital);
        asthmaAlarmSwitch = (Switch) findViewById(R.id.asthma_control_alarm_switch_activity_alarm_setting);
        noticeSwitch = (Switch) findViewById(R.id.notice_switch_activity_alarm_setting);
        likeSwitch = (Switch) findViewById(R.id.like_switch_activity_alarm_setting);
        commentSwitch = (Switch) findViewById(R.id.comment_switch_activity_alarm_setting);
        commentReplySwitch = (Switch) findViewById(R.id.comment_reply_switch_activity_alarm_setting);
        swhMarketing = (Switch)findViewById(R.id.swh_alarm_setting_marketing);
        allCommunityAlarmSwitch = (Switch)findViewById(R.id.community_all_switch_activity_alarm_setting);
        imgQuestion = (ImageView)findViewById(R.id.img_alarm_setting_question);
        linQuestionVisible = (LinearLayout)findViewById(R.id.lin_alarm_setting_question_visible);
        imgSettingMove = (ImageView)findViewById(R.id.img_alarm_setting_setting_move);
        secessionTextView = (TextView) findViewById(R.id.here_text_view_activity_account);

        NullCheck(this);

        if (userItem.getMarketingPermissionFlag() < 1){
            swhMarketing.setChecked(false);
        }else{
            swhMarketing.setChecked(true);
        }

        if (getNetworkState() != null && getNetworkState().isConnected()) {
            new SelectAlarmSettingNetwork().execute();
        }
        btnBack.setOnClickListener(this);
//        allSelectSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                allSelectSwitch.setChecked(isChecked);
//            }
//        });

        secessionTextView.setOnClickListener(this);
        asthmaAlarmSwitch.setOnClickListener(this);
        allCommunityAlarmSwitch.setOnClickListener(this);
        noticeSwitch.setOnClickListener(this);
        likeSwitch.setOnClickListener(this);
        commentSwitch.setOnClickListener(this);
        commentReplySwitch.setOnClickListener(this);
        swhMarketing.setOnClickListener(this);
        imgQuestion.setOnClickListener(this);
        imgSettingMove.setOnClickListener(this);
    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_text_view_activity_attending_hospital: {
                onBackPressed();
                break;
            }
            case R.id.asthma_control_alarm_switch_activity_alarm_setting:
                alarmSettingItem.setAsthmaFlag(asthmaAlarmSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(asthmaAlarmSwitch.isChecked() ? "1" : "-1", "SYMPTOM_FLAG");
                break;
            case R.id.notice_switch_activity_alarm_setting:
                alarmSettingItem.setNoticeFlag(noticeSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(noticeSwitch.isChecked() ? "1" : "-1", "NOTICE_FLAG");
                break;
            case R.id.like_switch_activity_alarm_setting:
                if (likeSwitch.isChecked()){
                    allCommunityAlarmSwitch.setChecked(true);
                }
                alarmSettingItem.setCommunityLikeFlag(likeSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(likeSwitch.isChecked() ? "1" : "-1", "COMMUNITY_LIKE_FLAG");
                break;
            case R.id.comment_switch_activity_alarm_setting:
                if (commentSwitch.isChecked()){
                    allCommunityAlarmSwitch.setChecked(true);
                }
                alarmSettingItem.setCommunityCommentFlag(commentSwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(commentSwitch.isChecked() ? "1" : "-1", "COMMUNITY_COMMENT_FLAG");
                break;
            case R.id.comment_reply_switch_activity_alarm_setting:
                if (commentReplySwitch.isChecked()){
                    allCommunityAlarmSwitch.setChecked(true);
                }
                alarmSettingItem.setCommunityCommentReplyFlag(commentReplySwitch.isChecked() ? 1 : -1);
                new UpdateAlarmInfoNetwork().execute(commentReplySwitch.isChecked() ? "1" : "-1", "COMMUNITY_COMMENT_REPLY_FLAG");
                break;
            case R.id.swh_alarm_setting_marketing :
                NetWorkCall(AGREE_UPDATE);
                break;
            case R.id.community_all_switch_activity_alarm_setting:
                setAllSwitchCheck(allCommunityAlarmSwitch.isChecked());
                break;
            case R.id.img_alarm_setting_question : {
                if (linQuestionVisible.getVisibility() == View.GONE){
                    linQuestionVisible.setVisibility(View.VISIBLE);
                }else{
                    linQuestionVisible.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.img_alarm_setting_setting_move : {
                String packageName = "com.kmw.soom2";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
                    startActivity(intent);
                }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", packageName);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                }
                break;
            }
            case R.id.here_text_view_activity_account:
                secessionTextView.setClickable(false);
                twoButtonDialog = new TwoButtonDialog(AlarmSettingActivity.this,"회원탈퇴", "정말 탈퇴하실 건가요? \n(탈퇴하시면 현재 이메일 주소로 재가입은 불가능해요!)","취소", "확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        if(getNetworkState() != null && getNetworkState().isConnected()) {
                            if(editor != null) {
                                editor.clear();
                                editor.apply();
                            }
                            new DeleteUserInfoNetwork().execute();
                        }
                        Intent i = new Intent(AlarmSettingActivity.this, LoginSignUpSelectActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        secessionTextView.setClickable(true);
                        finish();
                    }
                });

                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        secessionTextView.setClickable(true);
                    }
                });

                break;
        }
    }

    void setAllSwitchCheck(boolean flag) {
        likeSwitch.setChecked(flag);
        commentSwitch.setChecked(flag);
        commentReplySwitch.setChecked(flag);

        if (flag) {
            new UpdateAlarmAllInfoNetwork().execute("1");
        } else {
            new UpdateAlarmAllInfoNetwork().execute("-1");
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(AGREE_UPDATE)){
                if (swhMarketing.isChecked()){
                    CustomToast(this,getLayoutInflater(),new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))+"\n"+"마케팅 앱 푸쉬 동의가 완료되었습니다.");
                }else{
                    CustomToast(this,getLayoutInflater(),new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))+"\n"+"마케팅 앱 푸쉬 거절이 완료되었습니다.");
                }
            }
        }
    }

    public void NetWorkCall(String mCode){
        if (mCode.equals(AGREE_UPDATE)){
            userItem.setMarketingPermissionFlag(swhMarketing.isChecked() == true ? 1 : -1);
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),"1",swhMarketing.isChecked() == true ? "1" : "-1");
        }
    }

    public class UpdateAlarmAllInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add("COMMUNITY_LIKE_FLAG", strings[0]);
            body.add("COMMUNITY_COMMENT_FLAG", strings[0]);
            body.add("COMMUNITY_COMMENT_REPLY_FLAG", strings[0]);
            alarmSettingItem.setCommunityLikeFlag(Integer.parseInt(strings[0]));
            alarmSettingItem.setCommunityCommentFlag(Integer.parseInt(strings[0]));
            alarmSettingItem.setCommunityCommentReplyFlag(Integer.parseInt(strings[0]));
            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateAlarmInfo(), body.build());
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                } else {

                }
            } catch (JSONException e) {
            }
        }
    }

    public class UpdateAlarmInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add(strings[1], strings[0]);


            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateAlarmInfo(), body.build());

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                } else {
                    allCommunityAlarmSwitch.setChecked((alarmSettingItem.checkSettingFlag() == 1));
                }
            } catch (JSONException e) {
            }
        }
    }

    public class SelectAlarmSettingNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectAlarmInfo(), body.build());
                Log.i(TAG,"SelectAlarmSettingNetwork : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.length() > 0) {
                        JSONObject object = jsonArray.getJSONObject(0);

                        alarmSettingItem = new AlarmSettingItem();

                        alarmSettingItem.setAsthmaFlag(JsonIntIsNullCheck(object, "SYMPTOM_FLAG"));
                        alarmSettingItem.setSymptomFlag(JsonIntIsNullCheck(object,"SYMPTOM_HISTORY_FLAG"));
                        alarmSettingItem.setDosingFlag(JsonIntIsNullCheck(object, "DOSING_FLAG"));
                        alarmSettingItem.setNoticeFlag(JsonIntIsNullCheck(object, "NOTICE_FLAG"));
                        alarmSettingItem.setCommunityLikeFlag(JsonIntIsNullCheck(object, "COMMUNITY_LIKE_FLAG"));
                        alarmSettingItem.setCommunityCommentFlag(JsonIntIsNullCheck(object, "COMMUNITY_COMMENT_FLAG"));
                        alarmSettingItem.setCommunityCommentReplyFlag(JsonIntIsNullCheck(object, "COMMUNITY_COMMENT_REPLY_FLAG"));

                        alarmSettingItem.checkSettingFlag();

                        asthmaAlarmSwitch.setChecked((alarmSettingItem.getAsthmaFlag() == 1));
                        noticeSwitch.setChecked((alarmSettingItem.getNoticeFlag() == 1));
                        likeSwitch.setChecked((alarmSettingItem.getCommunityLikeFlag() == 1));
                        commentSwitch.setChecked((alarmSettingItem.getCommunityCommentFlag() == 1));
                        commentReplySwitch.setChecked((alarmSettingItem.getCommunityCommentReplyFlag() == 1));
                        allCommunityAlarmSwitch.setChecked((alarmSettingItem.checkSettingFlag() == 1));
                    }


                }
            } catch (JSONException e) {
            }
        }
    }

    public class DeleteUserInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteUserInfo(), body.build());
                return response;
            } catch (IOException e) {
                secessionTextView.setClickable(false);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                editor = preferences.edit();

                if(userItem.getLoginType() == 2) {
                    mOAuthLoginInstance = OAuthLogin.getInstance();
                    mOAuthLoginInstance.logout(AlarmSettingActivity.this);
                }else if(userItem.getLoginType() == 3) {
                    onClickLogout();
                }

                Utils.itemsArrayList = null;
                Utils.itemsNoticeArrayList = null;
                Utils.itemsWriteArrayList = null;
                Utils.itemsScrabArrayList = null;
                Utils.itemsCommentArrayList = null;
                Utils.likeItemArrayList = null;
                Utils.scrapItemArrayList = null;
                Utils.COMMUNITY_LEFT_PAGEING = 0;
                Utils.COMMUNITY_LEFT_SEARCH_TOTAL_PAGE = 0;
                Utils.COMMUNITY_RIGHT_PAGEING = 0;
                Utils.COMMUNITY_RIGHT_SEARCH_TOTAL_PAGE = 0;
                Utils.COMMUNITY_SELECT_TAB_POSITION = 0;

                Utils.registerDtList = null;
                Utils.koList = null;
                Utils.etcItemArrayList = null;
                Utils.mList = null;
                Utils.hisItems = null;
                Utils.AllRegisterDtList = null;
                Utils.AllKoList = null;
                Utils.AllEtcItemArrayList = null;
                Utils.AllList = null;
                Utils.FEED_LIST_LAST_POSITION = 0;
                Utils.FEED_ALL_LIST_LAST_POSITION = 0;;

                Utils.userItem = null;

                Utils.MedicineTakingItems = null;
                Utils.MEDICINE_SELECT_TAB_POSITION = 0;

                editor.putInt("IS_LOGIN_FLAG",0);
                editor.putInt("LOGIN_FLAG",1);
                editor.apply();

            }catch (JSONException e){
                secessionTextView.setClickable(true);
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
}
