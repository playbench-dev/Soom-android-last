package com.kmw.soom2.Home.Activitys.AdultActivitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.CalendarActivity;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.R;

import java.util.ArrayList;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HOME_FEED_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ACT_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ACT_UPDATE;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class AdultCheckResultActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "AdultCheckResultActivity";
    TextView txtTitle1;
    LinearLayout linScore;
    TextView txtScore,txtContents;
    TextView txtName;
    ImageView imgIcon;
    Button btnRetry,btnFinish;
    TextView txtTitile;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    TextView btnBack;
    LinearLayout linBack;

    Intent beforeIntent;
    int type;
    int status;
    String actSelected = "";
    ArrayList<Activity> activityArrayList = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_check_result);

        beforeIntent = getIntent();

        pref    = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor  = pref.edit();

        type = beforeIntent.getIntExtra("resultType",0);
        actSelected = beforeIntent.getStringExtra("kidsScore");

        FindViewById();

        NullCheck(this);

        if (beforeIntent.hasExtra("intoHomeFeed")) {
            linBack.setVisibility(View.VISIBLE);
            txtTitile.setText(beforeIntent.getStringExtra("registerDt").substring(0,10));
        }else {
            linBack.setVisibility(View.GONE);
        }

        if (beforeIntent.hasExtra("score")){
            txtName.setText(userItem.getNickname()+"님!");
            if (beforeIntent.getIntExtra("score",0) == 25){
                txtTitle1.setText(getResources().getString(R.string.result_title1));
                txtContents.setText(getResources().getString(R.string.result_vice_contents1));
                imgIcon.setImageResource(R.drawable.ic_act_good);
                linScore.setBackgroundResource(R.drawable.bg_gradient_result01);
                txtScore.setText("25점");
                status = 1;
            }else if (beforeIntent.getIntExtra("score",0) >= 20){
                txtTitle1.setText(getResources().getString(R.string.result_title2));
                txtContents.setText(getResources().getString(R.string.result_vice_contents2));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                imgIcon.setImageResource(R.drawable.ic_act_not_bad);
                linScore.setBackgroundResource(R.drawable.bg_gradient_result02);
                status = 2;
            }else{
                txtTitle1.setText(getResources().getString(R.string.result_title3));
                txtContents.setText(getResources().getString(R.string.result_vice_contents3));
                txtScore.setText(""+beforeIntent.getIntExtra("score",0)+"점");
                imgIcon.setImageResource(R.drawable.ic_act_bad);
                linScore.setBackgroundResource(R.drawable.bg_gradient_result03);
                status = 3;
            }
        }
    }

    void FindViewById(){
        txtTitle1 = (TextView)findViewById(R.id.txt_adult_check_result_title);
        linScore = (LinearLayout)findViewById(R.id.lin_adult_check_result_score);
        txtScore = (TextView)findViewById(R.id.txt_adult_check_result_score);
        txtContents = (TextView)findViewById(R.id.txt_adult_check_result_contents);
        btnRetry = (Button)findViewById(R.id.btn_adult_check_result_retry);
        btnFinish = (Button)findViewById(R.id.btn_adult_check_result_finish);
        txtTitile = (TextView)findViewById(R.id.txt_act_preview_result_date);
        txtName = (TextView)findViewById(R.id.txt_adult_check_result_name);
        imgIcon = (ImageView)findViewById(R.id.img_adult_check_result_icon);

        btnBack = findViewById(R.id.txt_act_preview_result_back);
        linBack = findViewById(R.id.lin_act_preview_result_back);

        btnRetry.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    public void TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

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

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getNetworkState() != null && getNetworkState().isConnected()) {
                    NetworkCall(HOME_FEED_DELETE);
                }
                dateTimeDialog.dismiss();
            }
        });
    }
    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(ACT_INSERT)){
            editor.putBoolean("actCheck",true);
            editor.apply();
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis())),
                    ""+beforeIntent.getIntExtra("score",0),""+type,""+status,actSelected,Utils.userItem.getNickname(),
                    ""+Utils.userItem.getGender(),""+Utils.userItem.getBirth());
        }else if (mCode.equals(ACT_UPDATE)){
            editor.putBoolean("actCheck",true);
            editor.apply();
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"),beforeIntent.getStringExtra("registerDt"),
                    ""+beforeIntent.getIntExtra("score",0),""+type,""+status,actSelected);
        }else if (mCode.equals(HOME_FEED_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"));

        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(ACT_INSERT)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i;
                        if (beforeIntent.hasExtra("homeFragment")){

                            i = new Intent(AdultCheckResultActivity.this,MainActivity.class);

                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }else{
                            i = new Intent(AdultCheckResultActivity.this,CalendarActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                        btnFinish.setClickable(false);
                    }
                },500);
            }else if (mCode.equals(ACT_UPDATE)){

            }else if (mCode.equals(HOME_FEED_DELETE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(AdultCheckResultActivity.this, AsthmaControlActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (beforeIntent != null){
                            if (beforeIntent.hasExtra("homeFragment")){
                                i.putExtra("homeFragment",true);
                            }
                        }
                        startActivity(i);
                        setResult(RESULT_OK);

                        onBackPressed();
                    }
                },500);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (beforeIntent.hasExtra("medicineHistoryNo")){
            super.onBackPressed();
        }
    }
    TwoButtonDialog twoButtonDialog;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_adult_check_result_retry : {
                if (beforeIntent.hasExtra("medicineHistoryNo")){
                    twoButtonDialog = new TwoButtonDialog(this,"재검사","이전 검사결과가 삭제됩니다.\n다시 하시겠습니까?","취소","다시하기");
                    twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                        @Override
                        public void OkButtonClick(View v) {
                            if(getNetworkState() != null && getNetworkState().isConnected()) {
                                NetworkCall(HOME_FEED_DELETE);
                            }
                        }
                    });
                }else{
                    Intent i = new Intent(this, AsthmaControlActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("scoreRetry",true);
                    if (beforeIntent != null){
                        if (beforeIntent.hasExtra("homeFragment")){
                            i.putExtra("homeFragment",true);
                        }
                    }
                    startActivity(i);
                }
                break;
            }
            case R.id.btn_adult_check_result_finish : {
                btnFinish.setClickable(false);
                if (beforeIntent.hasExtra("medicineHistoryNo")) {
//                    NetworkCall(ACT_UPDATE);
                    onBackPressed();
                    btnFinish.setClickable(true);
                } else {
                    NetworkCall(ACT_INSERT);
                }
                break;
            }
            case R.id.txt_act_preview_result_back : {
                v.setClickable(false);
                onBackPressed();
                break;
            }
        }
    }
}
