package com.kmw.soom2.Home.Activitys.SymptomActivitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.Item.BlurBuilder;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.AsthmaPercentDetailActivity;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ASTHMA_CALL_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ASTHMA_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ASTHMA_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HOME_FEED_DELETE;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMM2;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDDHHMMSS;
import static com.kmw.soom2.Common.Utils.userItem;

public class AstmaPercentActivity extends AppCompatActivity implements AsyncResponse, View.OnClickListener {

    private String TAG = "AstmaPercentActivity";

    private TextView mTextViewBack;
    private ImageView mImageViewRemove;
    private TextView mTextViewDate,mTextViewTime;
    private TextView mTextViewLocation;
    private LinearLayout mLinearBlurBg;
    private TextView mTextViewLow,mTextViewBasic,mTextViewHigh,mTextViewGoHigh;
    private TextView mTextViewInfo;
    private LinearLayout mLinearPercentBar;
    private LinearLayout mLinearPercentRingBetween;
    private ImageView mImageViewRingLow,mImageViewRingBasic,mImageViewRingHigh, mImageViewRingGoHigh;
    private LinearLayout mLinearInfoDetail;
    private Button mButtonDone;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
    private String mNowDate = "";
    private String[] mCodeList = new String[]{"1100000000", "2600000000", "2700000000", "2800000000", "2900000000", "3000000000", "3100000000", "3600000000", "4100000000", "4200000000", "4300000000", "4400000000", "4500000000", "4600000000", "4700000000", "4800000000", "5000000000"};
    private String[] mNameList = new String[]{"서울특별시", "부산광역시", "대구광역시", "인천광역시", "광주광역시", "대전광역시", "울산광역시", "세종특별자치시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주특별자치도"};
    private String mRegisterDt = "";
    private String mLocation = "";
    private String mLocationCode = "";
    private int mAsthmaScore = -1;
    private Intent beforeIntent;
    private TwoButtonDialog twoButtonDialog;
    private OneButtonDialog oneButtonDialog;

    private String mSaveBeforeRegisterDt = "";
    private String mSaveAfterRegisterDt = "";
    private String mSaveBeforeLocation = "";
    private int mSaveBeforeScore = 0;
    private boolean mRetryStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_astma_percent);

        beforeIntent = getIntent();

        FindViewById();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_asthma_bg_test1);
        mLinearBlurBg.setBackground(new BitmapDrawable(getResources(),BlurBuilder.blur(this,bitmap,0)));

        if (beforeIntent != null) {
            if (beforeIntent.hasExtra("registerDt")) {
                try {
                    mTextViewDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0, 10))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    mTextViewTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11, 18))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mImageViewRemove.setVisibility(View.VISIBLE);
                mLinearBlurBg.setVisibility(View.INVISIBLE);

                mAsthmaScore = beforeIntent.getIntExtra("asthmaScore",0);
                mLocation = beforeIntent.getStringExtra("location");
                mSaveBeforeLocation = mLocation;
                mSaveBeforeScore = mAsthmaScore;

                mTextViewLocation.setText(mLocation);
                for (int i = 0; i < mNameList.length; i++){
                    if (mNameList[i].equals(mLocation)){
                        mLocationCode = mCodeList[i];
                    }
                }
                if (mAsthmaScore == 1){
                    mTextViewBasic.setTextColor(Color.parseColor("#ead21b"));
                    mTextViewInfo.setText("규칙적 생활습관 유지하기\n만성 천식, 폐질환 환자들은 주의");
                    TextImageViewVisible(mTextViewLow,mTextViewHigh,mTextViewGoHigh,mImageViewRingBasic,mImageViewRingLow,mImageViewRingHigh,mImageViewRingGoHigh);
                }else if (mAsthmaScore == 2){
                    mTextViewHigh.setTextColor(Color.parseColor("#f79d23"));
                    mTextViewInfo.setText("실내를 청결하게 하고 자주 환기하기 대기오염이\n증가하는 시기에는 창문과 문을 닫아 외부\n노출을 줄이고 공기청정기 사용하기");
                    TextImageViewVisible(mTextViewLow,mTextViewBasic,mTextViewGoHigh,mImageViewRingHigh,mImageViewRingLow,mImageViewRingBasic,mImageViewRingGoHigh);
                }else if (mAsthmaScore == 3){
                    mTextViewGoHigh.setTextColor(Color.parseColor("#e02d2d"));
                    mTextViewInfo.setText("청결한 환경을 유지하는데 각별히 신경쓰기\n천식환자들은 각별한 주의 요망");
                    TextImageViewVisible(mTextViewLow,mTextViewBasic,mTextViewHigh,mImageViewRingGoHigh,mImageViewRingLow,mImageViewRingHigh,mImageViewRingBasic);
                }else{
                    mTextViewLow.setTextColor(Color.parseColor("#09D182"));
                    mTextViewInfo.setText("평소 건강관리에 유의하기");
                    TextImageViewVisible(mTextViewBasic,mTextViewHigh,mTextViewGoHigh,mImageViewRingLow,mImageViewRingBasic,mImageViewRingHigh,mImageViewRingGoHigh);
                }
                mButtonDone.setText("완료");
            } else if (beforeIntent.hasExtra("date")) {
                mTextViewDate.setText(beforeIntent.getStringExtra("date"));
                mTextViewTime.setText(beforeIntent.getStringExtra("time"));
            } else {
                mLocation = "서울특별시";
                mLocationCode = "1100000000";
                mTextViewLocation.setText(mLocation);
                mTextViewDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
                mTextViewTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));

                mNowDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                NetworkCall(ASTHMA_CALL_LIST);
            }
        } else {
            mLocation = "서울특별시";
            mLocationCode = "1100000000";
            mTextViewLocation.setText(mLocation);
            mTextViewDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            mTextViewTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));

            mNowDate = simpleDateFormat.format(new Date(System.currentTimeMillis()));
            NetworkCall(ASTHMA_CALL_LIST);
        }
        try {
            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()))
                    + " " + formatHHMMSS.format(formatHHMM.parse(mTextViewTime.getText().toString()));
            mSaveBeforeRegisterDt = mRegisterDt;
            mSaveAfterRegisterDt = mRegisterDt;
            mNowDate = simpleDateFormat.format(formatYYYYMMDDHHMMSS.parse(mRegisterDt));
        } catch (ParseException e) {
            e.printStackTrace();
            mRegisterDt = mSaveBeforeRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
            mSaveBeforeRegisterDt = mRegisterDt;
            mSaveAfterRegisterDt = mRegisterDt;
        }
    }

    void FindViewById(){
        mTextViewBack = (TextView)findViewById(R.id.txt_asthma_percent_record_back);
        mImageViewRemove = (ImageView)findViewById(R.id.img_asthma_percent_record_remove);
        mTextViewDate = (TextView)findViewById(R.id.txt_asthma_percent_record_date);
        mTextViewTime = (TextView)findViewById(R.id.txt_asthma_percent_record_time);
        mTextViewLocation = (TextView) findViewById(R.id.txt_asthma_percent_record_location);
        mLinearBlurBg = (LinearLayout)findViewById(R.id.lin_asthma_percent_bg);
        mTextViewLow = (TextView)findViewById(R.id.txt_asthma_percent_low);
        mTextViewBasic = (TextView)findViewById(R.id.txt_asthma_percent_basic);
        mTextViewHigh = (TextView)findViewById(R.id.txt_asthma_percent_high);
        mTextViewGoHigh = (TextView)findViewById(R.id.txt_asthma_percent_go_high);
        mTextViewInfo = (TextView)findViewById(R.id.txt_asthma_percent_info);
        mLinearPercentBar = (LinearLayout)findViewById(R.id.lin_asthma_percent_bar_bg);
        mImageViewRingLow = (ImageView)findViewById(R.id.img_asthma_percent_ring_low);
        mImageViewRingBasic = (ImageView)findViewById(R.id.img_asthma_percent_ring_basic);
        mImageViewRingHigh = (ImageView)findViewById(R.id.img_asthma_percent_ring_high);
        mImageViewRingGoHigh = (ImageView)findViewById(R.id.img_asthma_percent_ring_go_high);
        mLinearInfoDetail = (LinearLayout)findViewById(R.id.lin_asthma_percent_record_detail);
        mButtonDone = (Button)findViewById(R.id.btn_asthma_percent_record_finish);

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {0xFF09D182,0xFFf4e859,0xFFf7a02b,0xFFb91e1e});
        gd.setCornerRadius(50f);

        mLinearPercentBar.setBackgroundDrawable(gd);

        mTextViewBack.setOnClickListener(this);
        mLinearInfoDetail.setOnClickListener(this);
        mButtonDone.setOnClickListener(this);
        mTextViewDate.setOnClickListener(this);
        mTextViewTime.setOnClickListener(this);
        mTextViewLocation.setOnClickListener(this);
        mImageViewRemove.setOnClickListener(this);
    }

    void CityPicker() {
        View dateTimeView = getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
        TextView txtTitle = (TextView) dateTimeView.findViewById(R.id.txt_dialog_number_picker_title);
        Button btnCancel = (Button) dateTimeView.findViewById(R.id.btn_dialog_number_picker_cancel);
        Button btnDone = (Button) dateTimeView.findViewById(R.id.btn_dialog_number_picker_done);
        final NumberPicker numberPicker = (NumberPicker) dateTimeView.findViewById(R.id.number_picker);
        final Dialog dateTimeDialog = new Dialog(AstmaPercentActivity.this);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker.setWrapSelectorWheel(false);

        txtTitle.setText("측정장소");

        numberPicker.setMinValue(0);

        numberPicker.setMaxValue(mNameList.length - 1);
        numberPicker.setDisplayedValues(mNameList);

        for (int i = 0; i < mNameList.length; i++) {
            if (mNameList[i].equals(mTextViewLocation.getText().toString())) {
                numberPicker.setValue(i);
            }
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = numberPicker.getValue();
                mLocation = mNameList[idx];
                mTextViewLocation.setText(mLocation);
                mLocationCode = mCodeList[idx];
                NetworkCall(ASTHMA_CALL_LIST);
                dateTimeDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });
    }

    public void TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText) {

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup, null);

        TextView txtTitle = (TextView) layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView) layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button) layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button) layout.findViewById(R.id.btn_two_btn_popup_right);

        dateTimeDialog.setCancelable(false);

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
                dateTimeDialog.dismiss();
                try {
                    mNowDate = simpleDateFormat.format(formatYYYYMMDDHHMMSS.parse(mRegisterDt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                NetworkCall(ASTHMA_CALL_LIST);
            }
        });
    }

    void DateTimePicker(final int flag, String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker, null);
        final Dialog dateDialog = new Dialog(AstmaPercentActivity.this);
        TextView txtTitle = (TextView) dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateDialog.setContentView(dateTimeView);
        dateDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = dateTimeView.findViewById(R.id.single_day_picker);


        Date date = null;

        if (flag == 1){ //날짜
            singleDateAndTimePicker.setDisplayYears(true);
            singleDateAndTimePicker.setDisplayMonths(true);
            singleDateAndTimePicker.setDisplayDaysOfMonth(true);
            singleDateAndTimePicker.setDisplayHours(false);
            singleDateAndTimePicker.setDisplayMinutes(false,"");
            singleDateAndTimePicker.setMustBeOnFuture(false);

            try {
                date = formatYYYYMMDD2.parse(mTextViewDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            singleDateAndTimePicker.setDisplayYears(false);
            singleDateAndTimePicker.setDisplayMonths(false);
            singleDateAndTimePicker.setDisplayDaysOfMonth(false);
            singleDateAndTimePicker.setDisplayHours(true);
            singleDateAndTimePicker.setDisplayMinutes(true,mTextViewDate.getText().toString());

            try {
                date = Utils.formatHHMM.parse(mTextViewTime.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                if (flag == 1){
                    mTextViewDate.setText(formatYYYYMMDD2.format(date));
                }else{
                    mTextViewTime.setText(Utils.formatHHMM.format(date));
                }
                try {
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()))
                            + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                    mSaveAfterRegisterDt = mRegisterDt;
                    mNowDate = simpleDateFormat.format(formatYYYYMMDDHHMMSS.parse(mRegisterDt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (Integer.valueOf(mNowDate) <= 2021030723){
                    mAsthmaScore = -1;
                    OneBtnPopup(AstmaPercentActivity.this,"천식폐질환 정보","3월8일 이전의 데이터는 존재하지않습니다.","확인");
                }else{
                    NetworkCall(ASTHMA_CALL_LIST);
                }
                dateDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateDialog.dismiss();
            }
        });
    }

    void TextImageViewVisible(TextView textView1, TextView textView2, TextView textView3,
                              ImageView imageView1, ImageView imageView2, ImageView imageView3, ImageView imageView4){
        textView1.setTextColor(getColor(R.color.acacac));
        textView2.setTextColor(getColor(R.color.acacac));
        textView3.setTextColor(getColor(R.color.acacac));
        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.INVISIBLE);
        imageView3.setVisibility(View.INVISIBLE);
        imageView4.setVisibility(View.INVISIBLE);
    }

    void NetworkCall(String mCode){
        try {
            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()))
                    + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
            mRegisterDt = beforeIntent.getStringExtra("registerDt");
        }
        if (mCode.equals(ASTHMA_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+ Utils.userItem.getUserNo(),mRegisterDt,mLocation,""+mAsthmaScore,userItem.getNickname(),""+userItem.getGender(),""+userItem.getBirth());
        }else if (mCode.equals(ASTHMA_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"),mRegisterDt,mLocation,""+mAsthmaScore,userItem.getNickname(),""+userItem.getGender(),""+userItem.getBirth());
        }else if (mCode.equals(HOME_FEED_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"));
        }else if (mCode.equals(ASTHMA_CALL_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mLocationCode,mNowDate);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(ASTHMA_INSERT) || mCode.equals(ASTHMA_UPDATE) || mCode.equals(HOME_FEED_DELETE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                }, 500);
            }else if (mCode.equals(ASTHMA_CALL_LIST)){
                if (mResult == null){
                    mAsthmaScore = -1;
//                    TwoBtnPopup(AstmaPercentActivity.this,"천식폐질환 정보","천식폐질환 정보를 불러오는데 실패했습니다.\n다시 호출하시겠습니까?","취소","확인");
                    new OneButtonDialog(AstmaPercentActivity.this,"천식폐질환 정보","기상청으로부터 해당 데이터를\n 받아오지 못 하고 있어요.\n내일 다시 시도해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {
                            onBackPressed();
                        }
                    });
                }else if (mResult.length() == 0){
                    mAsthmaScore = -1;
//                    TwoBtnPopup(AstmaPercentActivity.this,"천식폐질환 정보","천식폐질환 정보를 불러오는데 실패했습니다.\n다시 호출하시겠습니까?","취소","확인");
                    new OneButtonDialog(AstmaPercentActivity.this,"천식폐질환 정보","기상청으로부터 해당 데이터를\n 받아오지 못 하고 있어요.\n내일 다시 시도해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {
                            onBackPressed();
                        }
                    });
                }else {
                    Log.i(TAG,"s : " + mResult);
                    try {
                        JSONObject jsonObject = new JSONObject(mResult);
                        if (jsonObject.getString("result").equals("N")){
                            mAsthmaScore = -1;
                            mLinearBlurBg.setVisibility(View.VISIBLE);
                            if (!mRetryStatus){
                                new OneButtonDialog(AstmaPercentActivity.this,"천식폐질환 정보","천식폐질환 가능지수를\n불러오지 못 했어요.\n다시 시도 할까요?","재시도").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                    @Override
                                    public void OnButtonClick(View v) {
                                        mRetryStatus = true;
                                        new NetworkUtils.NetworkCall(AstmaPercentActivity.this,AstmaPercentActivity.this,TAG,mCode).execute(mLocationCode,mNowDate);
                                    }
                                });
                            }else{
                                new OneButtonDialog(AstmaPercentActivity.this,"천식폐질환 정보","기상청으로부터 해당 데이터를\n 받아오지 못 하고 있어요.\n내일 다시 시도해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                    @Override
                                    public void OnButtonClick(View v) {
                                        finish();
                                    }
                                });
                            }
                        }else{
                            mLinearBlurBg.setVisibility(View.INVISIBLE);
                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");

                            if (jsonArrayList.getJSONObject(0).has("today")){
                                mAsthmaScore = JsonIntIsNullCheck(jsonArrayList.getJSONObject(0),"today");
                                if (mAsthmaScore == 1){
                                    mTextViewBasic.setTextColor(Color.parseColor("#ead21b"));
                                    mTextViewInfo.setText("규칙적 생활습관 유지하기\n만성 천식, 폐질환 환자들은 주의");
                                    TextImageViewVisible(mTextViewLow,mTextViewHigh,mTextViewGoHigh,mImageViewRingBasic,mImageViewRingLow,mImageViewRingHigh,mImageViewRingGoHigh);
                                }else if (mAsthmaScore == 2){
                                    mTextViewHigh.setTextColor(Color.parseColor("#f79d23"));
                                    mTextViewInfo.setText("실내를 청결하게 하고 자주 환기하기 대기오염이\n증가하는 시기에는 창문과 문을 닫아 외부\n노출을 줄이고 공기청정기 사용하기");
                                    TextImageViewVisible(mTextViewLow,mTextViewBasic,mTextViewGoHigh,mImageViewRingHigh,mImageViewRingLow,mImageViewRingBasic,mImageViewRingGoHigh);
                                }else if (mAsthmaScore == 3){
                                    mTextViewGoHigh.setTextColor(Color.parseColor("#e02d2d"));
                                    mTextViewInfo.setText("청결한 환경을 유지하는데 각별히 신경쓰기\n천식환자들은 각별한 주의 요망");
                                    TextImageViewVisible(mTextViewLow,mTextViewBasic,mTextViewHigh,mImageViewRingGoHigh,mImageViewRingLow,mImageViewRingHigh,mImageViewRingBasic);
                                }else if (mAsthmaScore == 0){
                                    mTextViewLow.setTextColor(Color.parseColor("#09D182"));
                                    mTextViewInfo.setText("평소 건강관리에 유의하기");
                                    TextImageViewVisible(mTextViewBasic,mTextViewHigh,mTextViewGoHigh,mImageViewRingLow,mImageViewRingBasic,mImageViewRingHigh,mImageViewRingGoHigh);
                                }else{
                                    OneBtnPopup(AstmaPercentActivity.this,"천식폐질환 정보","해당일에 천식/폐질환지수가 없습니다.","확인");
                                }
                            }
                        }
                    } catch (JSONException e) {

                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt) && mSaveBeforeLocation.equals(mTextViewLocation.getText().toString()) && mSaveBeforeScore == mAsthmaScore){
            finish();
        }else{
            twoButtonDialog = new TwoButtonDialog(AstmaPercentActivity.this,"천식폐질환 가능지수", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
            twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                @Override
                public void OkButtonClick(View v) {
                    twoButtonDialog.dismiss();
                    finish();
                }
            });

            twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                @Override
                public void CancelButtonClick(View v) {
                    twoButtonDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_asthma_percent_record_back : {
                onBackPressed();
                break;
            }
            case R.id.lin_asthma_percent_record_detail : {
                Intent intent = new Intent(this, AsthmaPercentDetailActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.img_asthma_percent_record_remove : {
                twoButtonDialog = new TwoButtonDialog(this, "천식폐질환 가능지수", "천식폐질환 가능지수 기록을 삭제하시겠습니까?", "취소", "확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        twoButtonDialog.dismiss();
                        NetworkCall(HOME_FEED_DELETE);
                    }
                });
                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        twoButtonDialog.dismiss();
                    }
                });
                break;
            }
            case R.id.btn_asthma_percent_record_finish : {
                if (mAsthmaScore != -1){
                    if (beforeIntent != null) {
                        if (beforeIntent.hasExtra("registerDt")) {
                            NetworkCall(ASTHMA_UPDATE);
                        }else{
                            NetworkCall(ASTHMA_INSERT);
                        }
                    }else{
                        NetworkCall(ASTHMA_INSERT);
                    }
                }
                break;
            }
            case R.id.txt_asthma_percent_record_date : {
                DateTimePicker(1,"날짜를 선택해주세요.");
                break;
            }
            case R.id.txt_asthma_percent_record_time : {
                DateTimePicker(2,"시간을 선택해주세요.");
                break;
            }
            case R.id.txt_asthma_percent_record_location : {
                CityPicker();
                break;
            }
        }
    }

    public int JsonIntIsNullCheck(JSONObject object, String value) {
        try {
            if (object.isNull(value)) {
                return -1;
            } else {
                return object.getInt(value);
            }
        } catch (JSONException e) {

        }
        return -1;
    }
}