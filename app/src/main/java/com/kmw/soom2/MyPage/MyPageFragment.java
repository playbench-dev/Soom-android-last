package com.kmw.soom2.MyPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.BuildConfig;
import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.UpdateHelper;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Adapters.NewCommunitySearchAdapter;
import com.kmw.soom2.Login.Activitys.AgreeActivity;
import com.kmw.soom2.MyPage.Activity.AccountActivity;
import com.kmw.soom2.MyPage.Activity.AirPollutionInfoActivity;
import com.kmw.soom2.MyPage.Activity.AlarmSettingActivity;
import com.kmw.soom2.MyPage.Activity.MyHospitalInfoActivity;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.MyPage.Activity.MypageCreateCsvActivity;
import com.kmw.soom2.MyPage.Activity.NoticeActivity;
import com.kmw.soom2.MyPage.Activity.PatientInfoActivity;
import com.kmw.soom2.MyPage.Activity.PostsActivity;
import com.kmw.soom2.R;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.UPDATE_VERSION;
import static com.kmw.soom2.Common.Utils.userItem;


public class MyPageFragment extends Fragment implements View.OnClickListener {
    LinearLayout writeLinearLayout, commentLinearLayout, scrabLinearLayout, userInfoLayout;
    LinearLayout hosInfoLayout, alarmSettinLayout, inquiryLayout, reportSendLayout;
    LinearLayout noticeLayout, termsLayout, appVerLayout, airPollutionInfoLayout;
    ImageView settingImageView, profileImageView, alarmImageView, imgUpdate;

    TextView txtUserNickname, txtUserType, txtLabel;

    String response;
    final String TAG = "MyPageFragment";
    String versionName = BuildConfig.VERSION_NAME;

    TextView txtAppVer;
    int writingCnt = 0, commentCnt = 0, scrapCnt = 0;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String mTag                         = "";

    public MyPageFragment() {
        // Required empty public constructor
    }

    public static MyPageFragment newInstance(String mTag){

        MyPageFragment myPageFragment = new MyPageFragment();

        Bundle args = new Bundle();
        args.putString("mTag",mTag);
        myPageFragment.setArguments(args);
        return myPageFragment;
    }

    public String getInstance(String mKey){
        if (getArguments().getString(mKey) == null){
            return null;
        }
        return getArguments().getString(mKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        writeLinearLayout = (LinearLayout) view.findViewById(R.id.write_linear_layout_fragment_my_page);
        commentLinearLayout = (LinearLayout) view.findViewById(R.id.comment_linear_layout_fragment_my_page);
        scrabLinearLayout = (LinearLayout) view.findViewById(R.id.scrap_linear_layout_fragment_my_page);
        userInfoLayout = (LinearLayout) view.findViewById(R.id.user_info_linear_layout_fragment_my_page);
        hosInfoLayout = (LinearLayout) view.findViewById(R.id.hos_info_linear_layout_fragment_my_page);
        alarmSettinLayout = (LinearLayout) view.findViewById(R.id.alarm_setting_linear_layout_fragment_my_page);
        inquiryLayout = (LinearLayout) view.findViewById(R.id.inquiry_linear_layout_fragment_my_page);
        noticeLayout = (LinearLayout) view.findViewById(R.id.notice_linear_layout_fragment_my_page);
        termsLayout = (LinearLayout) view.findViewById(R.id.terms_linear_layout_fragment_my_page);
        appVerLayout = (LinearLayout) view.findViewById(R.id.app_ver_linear_layout_fragment_my_page);
        airPollutionInfoLayout = (LinearLayout) view.findViewById(R.id.air_pollution_linear_layout_info_fragment_my_page);
        reportSendLayout = (LinearLayout) view.findViewById(R.id.report_send_linear_layout_fragment_my_page);
        settingImageView = (ImageView) view.findViewById(R.id.setting_image_view_fragment_my_page);
        txtLabel = (TextView)view.findViewById(R.id.txt_fragment_my_page_label);
        txtAppVer = (TextView) view.findViewById(R.id.txt_app_ver);
        imgUpdate = (ImageView)view.findViewById(R.id.img_my_page_update);
        txtAppVer.setText(versionName);
        if (UPDATE_VERSION != null){
            if (Integer.parseInt(UPDATE_VERSION.replace(".","")) > Integer.parseInt(versionName.replace(".",""))){
                imgUpdate.setImageResource(R.drawable.ic_update_off);
            }else{
                imgUpdate.setImageResource(R.drawable.ic_update_on);
            }
        }

        //=========================
        profileImageView = view.findViewById(R.id.user_icon_image_view_fragment_my_page);
        txtUserNickname = view.findViewById(R.id.user_name_fragment_my_page);
        txtUserType = view.findViewById(R.id.user_category_fragment_my_page);
        //=========================

        writeLinearLayout.setOnClickListener(this);
        commentLinearLayout.setOnClickListener(this);
        scrabLinearLayout.setOnClickListener(this);
        userInfoLayout.setOnClickListener(this);
        hosInfoLayout.setOnClickListener(this);
        alarmSettinLayout.setOnClickListener(this);
        inquiryLayout.setOnClickListener(this);
        noticeLayout.setOnClickListener(this);
        termsLayout.setOnClickListener(this);
        appVerLayout.setOnClickListener(this);
        airPollutionInfoLayout.setOnClickListener(this);
        reportSendLayout.setOnClickListener(this);
        settingImageView.setOnClickListener(this);
        alarmImageView = view.findViewById(R.id.alarm_fragment_my_page);

        pref = getActivity().getSharedPreferences(Utils.preferencesName, Context.MODE_PRIVATE);
        editor = pref.edit();

        mTag = getInstance("mTag");

        if (mTag == null || mTag.length() == 0){

        }else{
            if (mTag.equals("hospital")){
                hosInfoLayout.performClick();
            }else if (mTag.equals("inquery")){
                inquiryLayout.performClick();
            }else if (mTag.equals("notice")){
                noticeLayout.performClick();
            }
        }

        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
//                getActivity().startActivityForResult(intent, 1111);
                startActivity(intent);
            }
        });

//        RetrofitClient retrofitClient = new RetrofitClient();
//        Call<JsonObject> call = retrofitClient.apiService.getretrofitdata();
//        call.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    Log.i(TAG,"response : " + response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//            }
//        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (getVisibleFragment() instanceof MyPageFragment){
            NullCheck(getActivity());
            new SelectUnReadPushListCheck().execute();

            txtUserNickname.setText(Utils.userItem.getNickname());

            String userLv = "";

            if (Utils.userItem.getLv() == 11) {
                userLv = "본인";
            }else if(Utils.userItem.getLv() == 22){
                userLv = "보호자";
            }else if(Utils.userItem.getLv() == 33){
                userLv = "일반";
            }else if (Utils.userItem.getLv() == 60){
                userLv = "의료진";
            }else if (Utils.userItem.getLv() == 80){
                userLv = "서브관리자";
            }else if (Utils.userItem.getLv() == 99){
                userLv = "관리자";
            }else {

            }

        Log.i(TAG,"label : " + Utils.userItem.getLabelName() + " color : " + Utils.userItem.getLabelColor());

            if (userItem.getLabelColor() != null && userItem.getLabelColor().length() != 0){
                txtLabel.setText(userItem.getLabelName());
                txtLabel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(userItem.getLabelColor())));
            }else{
                txtLabel.setVisibility(View.GONE);
            }

            txtUserType.setText("가입유형 : " + userLv);

            if (Utils.userItem.getProfileImg().length() != 0) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
                String replaceText = Utils.userItem.getProfileImg();
                if (replaceText.contains("soom2.testserver-1.com:8080")){
                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                }else if (replaceText.contains("103.55.190.193")){
                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                }
                if (!Utils.userItem.getProfileImg().contains("https:")) {
                    Glide.with(this).asBitmap().load("https:" + replaceText).apply(requestOptions).into(profileImageView);
                } else {
                    Glide.with(this).asBitmap().load(replaceText).apply(requestOptions).into(profileImageView);
                }
            } else {
                int resource = R.drawable.ic_no_profile;
                if (userItem.getGender() == 1){
                    resource = R.drawable.ic_no_profile_m;
                }else if (userItem.getGender() == 2){
                    resource = R.drawable.ic_no_profile_w;
                }
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(40));
                Glide.with(this)
                        .load(resource)
                        .apply(requestOptions)
                        .into(profileImageView);
            }

            if (Utils.userItem.getEmail().length() == 0 && pref.getInt("emailCheck",0) != 1){
                TwoButtonDialog twoButtonDialog = new TwoButtonDialog(getActivity(),"마이페이지","중요 이벤트 공지 위해\n이메일을 추가해주세요.","다시보지 않기","확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        twoButtonDialog.dismiss();
                        Intent intent = new Intent(getActivity(),AccountActivity.class);
                        startActivity(intent);
                    }
                });

                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        twoButtonDialog.dismiss();
                        editor.putInt("emailCheck",1);
                        editor.apply();
                    }
                });
            }
//        }
    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_linear_layout_fragment_my_page:
                Intent intentWrite = new Intent(getActivity(), PostsActivity.class);
                intentWrite.putExtra("paging", 0);
                startActivity(intentWrite);
                break;
            case R.id.comment_linear_layout_fragment_my_page:
                Intent intentComment = new Intent(getActivity(), PostsActivity.class);
                intentComment.putExtra("paging", 1);
                startActivity(intentComment);
                break;
            case R.id.scrap_linear_layout_fragment_my_page:
                Intent intentScrap = new Intent(getActivity(), PostsActivity.class);
                intentScrap.putExtra("paging", 2);
                startActivity(intentScrap);
                break;
            case R.id.user_info_linear_layout_fragment_my_page:
                Intent intentUserInfo = new Intent(getActivity(), PatientInfoActivity.class);
                startActivity(intentUserInfo);
                break;
            case R.id.hos_info_linear_layout_fragment_my_page:
                Intent intentHosInfo = new Intent(getActivity(), MyHospitalInfoActivity.class);
                startActivity(intentHosInfo);
                break;
            case R.id.alarm_setting_linear_layout_fragment_my_page:
                Intent intentAlarmSetting = new Intent(getActivity(), AlarmSettingActivity.class);
                startActivity(intentAlarmSetting);
                break;
            case R.id.inquiry_linear_layout_fragment_my_page:
                Intent intentInquiry = new Intent(getActivity(), InquiryActivity.class);
                startActivity(intentInquiry);
                break;
            case R.id.notice_linear_layout_fragment_my_page:
                Intent intentNotice = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intentNotice);
                break;
            case R.id.terms_linear_layout_fragment_my_page:
                Intent intentTerms = new Intent(getActivity(), AgreeActivity.class);
                startActivity(intentTerms);
                break;
              /*  case R.id.app_ver_linear_layout_fragment_my_page:
                Intent intentAppVer = new Intent(getActivity(), PostsActivity.class);
                startActivity(intentAppVer);
                break;*/
            case R.id.air_pollution_linear_layout_info_fragment_my_page:
                Intent intentAirPollution = new Intent(getActivity(), AirPollutionInfoActivity.class);
                startActivity(intentAirPollution);
                break;
            case R.id.setting_image_view_fragment_my_page:
                Intent intentSetting = new Intent(getActivity(), AccountActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.report_send_linear_layout_fragment_my_page:
                Intent intentReportSend = new Intent(getActivity(), MypageCreateCsvActivity.class);
                startActivity(intentReportSend);
                break;
        }
    }

    public class SelectUnReadPushListCheck extends AsyncTask<String, String, String> {
        String mUrl;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (JsonIsNullCheck(jsonObject,"FLAG").equals("2")) {
                        alarmImageView.setImageResource(R.drawable.ic_alarm_on);
                    }else {
                        alarmImageView.setImageResource(R.drawable.ic_alarm_off);
                    }
                } catch (JSONException e) {

                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", String.valueOf(Utils.userItem.getUserNo())).build();

            mUrl = Utils.Server.selectUnreadMessage();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, mUrl, body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getVisibleFragment() instanceof MyPageFragment){
            if (requestCode == 1122){
                if (resultCode == RESULT_OK){
                    new SelectUnReadPushListCheck().execute();
                }
            }
        }
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}
