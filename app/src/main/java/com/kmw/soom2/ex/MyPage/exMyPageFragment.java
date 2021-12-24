package com.kmw.soom2.ex.MyPage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Activitys.AgreeActivity;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.MyPage.Activity.AccountActivity;
import com.kmw.soom2.MyPage.Activity.AirPollutionInfoActivity;
import com.kmw.soom2.MyPage.Activity.AlarmSettingActivity;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.MyPage.Activity.MyHospitalInfoActivity;
import com.kmw.soom2.MyPage.Activity.MypageCreateCsvActivity;
import com.kmw.soom2.MyPage.Activity.NoticeActivity;
import com.kmw.soom2.MyPage.Activity.PatientInfoActivity;
import com.kmw.soom2.MyPage.Activity.PostsActivity;
import com.kmw.soom2.MyPage.MyPageFragment;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;
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
import static com.kmw.soom2.Common.Utils.userItem;

public class exMyPageFragment extends Fragment implements View.OnClickListener {
    LinearLayout inquiryLayout;
    LinearLayout termsLayout, appVerLayout;
    LinearLayout linMoveLogin;
    ImageView profileImageView, alarmImageView;

    TextView txtUserNickname, txtUserType;

    String response;
    final String TAG = "MyPageFragment";
    String versionName = BuildConfig.VERSION_NAME;

    TextView txtAppVer;
    int writingCnt = 0, commentCnt = 0, scrapCnt = 0;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String mTag                         = "";

    public exMyPageFragment() {
        // Required empty public constructor
    }

    public static exMyPageFragment newInstance(String mTag){

        exMyPageFragment myPageFragment = new exMyPageFragment();

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
        View view = inflater.inflate(R.layout.fragment_ex_my_page, container, false);
        inquiryLayout = (LinearLayout) view.findViewById(R.id.inquiry_linear_layout_fragment_my_page);
        termsLayout = (LinearLayout) view.findViewById(R.id.terms_linear_layout_fragment_my_page);
        appVerLayout = (LinearLayout) view.findViewById(R.id.app_ver_linear_layout_fragment_my_page);
        linMoveLogin = (LinearLayout)view.findViewById(R.id.lin_ex_my_page_move_login);
        txtAppVer = (TextView) view.findViewById(R.id.txt_app_ver);
        txtAppVer.setText(versionName);
        //=========================
        profileImageView = view.findViewById(R.id.user_icon_image_view_fragment_my_page);
        txtUserNickname = view.findViewById(R.id.user_name_fragment_my_page);
        txtUserType = view.findViewById(R.id.user_category_fragment_my_page);
        //=========================

        inquiryLayout.setOnClickListener(this);
        termsLayout.setOnClickListener(this);
        appVerLayout.setOnClickListener(this);
        linMoveLogin.setOnClickListener(this);
        alarmImageView = view.findViewById(R.id.alarm_fragment_my_page);

        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inquiry_linear_layout_fragment_my_page:
                Intent intentInquiry = new Intent(getActivity(), InquiryActivity.class);
                intentInquiry.putExtra("guest",true);
                startActivity(intentInquiry);
                break;
            case R.id.terms_linear_layout_fragment_my_page:
                Intent intentTerms = new Intent(getActivity(), AgreeActivity.class);
                startActivity(intentTerms);
                break;
            case R.id.lin_ex_my_page_move_login : {
                Intent intentSetting = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(intentSetting);
                break;
            }
        }
    }
}
