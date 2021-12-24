package com.kmw.soom2.Home.Activitys.SymptomActivitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Activitys.NewStaticSymptomActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_LIST_ALL_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_SYMPTOM_LIST_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_SYMPTOM_LIST_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_SYMPTOM_LIST_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.PUSH_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST_CHECK;
import static com.kmw.soom2.Common.Utils.Collapse;
import static com.kmw.soom2.Common.Utils.Expand;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMM2;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;
import static com.kmw.soom2.Common.Utils.userItem;

public class NewSymptomRecordActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "NewSymptomRecordActivity";
    private TextView mTextViewBack;
    private ImageView mImageViewRemove;
    private TextView mTextViewDate,mTextViewTime;
    private LinearLayout mLinearSymptomNone,mLinearSymptomCough,mLinearSymptomPhlegm;
    private LinearLayout mLinearSymptomFrustrated,mLinearSymptomDifficulty,mLinearSymptomWheezing,mLinearSymptomEtc;
    private LinearLayout mLinearCauseVisible;
    private TextView mTextViewCause,mTextViewMemoLength;
    private EditText mEditViewMemo;
    private Button mButtonFinish;
    private ScrollView mScrollView;
    private RelativeLayout mRelaCause;

    //popup
    private BottomSheetDialog mCauseBottomSheetDialog;
    private View mBottomSheetDialogCause;
    private Button mButtonPopupSave;
    private LinearLayout mLinearCause01,mLinearCause02,mLinearCause03,mLinearCause04,mLinearCause05,mLinearCause06,mLinearCause07,mLinearCause08;
    private LinearLayout mLinearCause09,mLinearCause10,mLinearCause11,mLinearCause12,mLinearCause13,mLinearCause14,mLinearCause15,mLinearCause16;

    private int[] categoryIdx = new int[]{11, 16, 14, 12, 13, 15, 40};
    private String[] categorys = new String[]{"기침", "가래", "가슴답답", "숨쉬기 불편", "천명음", "기타증상", "증상없음"};
    private String[] causes = new String[]{"감기", "미세먼지", "찬바람", "운동", "집먼지진드기", "애완동물", "음식알러지", "꽃가루", "스트레스", "담배연기", "요리연기", "강한냄새", "건조함", "곰팡이", "높은습도", "기타"};
    private int[] causesIdx = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14, 15, 11};

    private Intent beforeIntent;

    private ArrayList<LinearLayout> mLinearSymptomArray = new ArrayList<>();
    private ArrayList<String> mCategoryArray = new ArrayList<>();
    private ArrayList<LinearLayout> mLinearCauseArray = new ArrayList<>();
    private ArrayList<String> mCauseIdxArray = new ArrayList<>();
    private ArrayList<LinearLayout> mLinearCauseArray1 = new ArrayList<>();
    private ArrayList<String> mCauseIdxArray1 = new ArrayList<>();
    private ArrayList<String> mSaveBeforeCategoryArray = new ArrayList<>();
    private ArrayList<String> mSaveBeforeCauseIdxArray = new ArrayList<>();
    private String mCause = "";
    private String mCauseText = "";
    private String mRegisterDt = "";

    private JSONArray mFeedArray = new JSONArray();
    private JSONObject mFeedObject = new JSONObject();

    private boolean mLastSession = false;
    private TwoButtonDialog twoButtonDialog;

    private String mYear = "";
    private String mMonth = "";
    private boolean dateCheck = true;
    private SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
    private SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");
    private String mSaveBeforeRegisterDt = "";
    private String mSaveAfterRegisterDt = "";
    private String mSaveBeforeMemo = "";
    private String mSaveAfterMemo = "";
    private boolean mCheck = false;
    private Typeface typefaceBold,typefaceMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_symptom_record);

        beforeIntent = getIntent();

        FindViewById();

        typefaceMedium = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);
        typefaceBold = ResourcesCompat.getFont(this, R.font.notosanscjkkr_bold);

        if (beforeIntent.hasExtra("pushNo")){
            NetworkCall(ALARM_LIST_ALL_READ);
        }

        NullCheck(this);

    }

    void FindViewById(){
        mTextViewBack               = (TextView)findViewById(R.id.txt_new_symptom_record_back);
        mImageViewRemove            = (ImageView)findViewById(R.id.img_new_symptom_record_remove);
        mTextViewDate               = (TextView)findViewById(R.id.txt_new_symptom_record_date);
        mTextViewTime               = (TextView)findViewById(R.id.txt_new_symptom_record_time);
        mLinearSymptomNone          = (LinearLayout)findViewById(R.id.lin_new_symptom_record_none);
        mLinearSymptomCough         = (LinearLayout)findViewById(R.id.lin_new_symptom_record_cough);
        mLinearSymptomPhlegm        = (LinearLayout)findViewById(R.id.lin_new_symptom_record_phlegm);
        mLinearSymptomFrustrated    = (LinearLayout)findViewById(R.id.lin_new_symptom_record_frustrated);
        mLinearSymptomDifficulty    = (LinearLayout)findViewById(R.id.lin_new_symptom_record_difficulty);
        mLinearSymptomWheezing      = (LinearLayout)findViewById(R.id.lin_new_symptom_record_wheezing);
        mLinearSymptomEtc           = (LinearLayout)findViewById(R.id.lin_new_symptom_record_etc);
        mLinearCauseVisible         = (LinearLayout)findViewById(R.id.lin_new_symptom_record_cause_visible);
        mTextViewCause              = (TextView)findViewById(R.id.txt_new_symptom_record_cause);
        mTextViewMemoLength         = (TextView)findViewById(R.id.txt_new_symptom_memo_length);
        mEditViewMemo               = (EditText)findViewById(R.id.edt_new_symptom_record_memo);
        mButtonFinish               = (Button)findViewById(R.id.btn_new_symptom_record_finish);
        mScrollView                 = (ScrollView)findViewById(R.id.scr_new_symptom_record);
        mRelaCause                  = (RelativeLayout)findViewById(R.id.rela_new_symptom_record_cause);

        mImageViewRemove.setVisibility(View.GONE);

        mTextViewBack.setOnClickListener(this);
        mImageViewRemove.setOnClickListener(this);
        mTextViewDate.setOnClickListener(this);
        mTextViewTime.setOnClickListener(this);
        mLinearSymptomNone.setOnClickListener(this);
        mLinearSymptomCough.setOnClickListener(this);
        mLinearSymptomPhlegm.setOnClickListener(this);
        mLinearSymptomFrustrated.setOnClickListener(this);
        mLinearSymptomDifficulty.setOnClickListener(this);
        mLinearSymptomWheezing.setOnClickListener(this);
        mLinearSymptomEtc.setOnClickListener(this);
        mTextViewCause.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);

        //popup
        mCauseBottomSheetDialog     = new BottomSheetDialog(NewSymptomRecordActivity.this,R.style.Theme_Design_BottomSheetDialog);
        mBottomSheetDialogCause     = getLayoutInflater().inflate(R.layout.bottom_sheet_cause, null);
        mLinearCause01              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_01);
        mLinearCause02              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_02);
        mLinearCause03              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_03);
        mLinearCause04              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_04);
        mLinearCause05              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_05);
        mLinearCause06              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_06);
        mLinearCause07              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_07);
        mLinearCause08              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_08);
        mLinearCause09              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_09);
        mLinearCause10              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_10);
        mLinearCause11              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_11);
        mLinearCause12              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_12);
        mLinearCause13              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_13);
        mLinearCause14              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_14);
        mLinearCause15              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_15);
        mLinearCause16              = (LinearLayout)mBottomSheetDialogCause.findViewById(R.id.lin_cause_popup_16);
        mButtonPopupSave            = (Button)mBottomSheetDialogCause.findViewById(R.id.btn_causes_popup_save);
        mCauseBottomSheetDialog.setContentView(mBottomSheetDialogCause);
//        mCauseBottomSheetDialog.setCancelable(false);

        mLinearCause01.setOnClickListener(this);
        mLinearCause02.setOnClickListener(this);
        mLinearCause03.setOnClickListener(this);
        mLinearCause04.setOnClickListener(this);
        mLinearCause05.setOnClickListener(this);
        mLinearCause06.setOnClickListener(this);
        mLinearCause07.setOnClickListener(this);
        mLinearCause08.setOnClickListener(this);
        mLinearCause09.setOnClickListener(this);
        mLinearCause10.setOnClickListener(this);
        mLinearCause11.setOnClickListener(this);
        mLinearCause12.setOnClickListener(this);
        mLinearCause13.setOnClickListener(this);
        mLinearCause14.setOnClickListener(this);
        mLinearCause15.setOnClickListener(this);
        mLinearCause16.setOnClickListener(this);
        mButtonPopupSave.setOnClickListener(this);

        setHideKeyboard(this,mScrollView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                View tabViewChild1 = mLinearSymptomNone.getChildAt(2);
                ((TextView)tabViewChild1).setTypeface(typefaceMedium);

                View tabViewChild2 = mLinearSymptomCough.getChildAt(2);
                ((TextView)tabViewChild2).setTypeface(typefaceMedium);

                View tabViewChild3 = mLinearSymptomPhlegm.getChildAt(2);
                ((TextView)tabViewChild3).setTypeface(typefaceMedium);

                View tabViewChild4 = mLinearSymptomFrustrated.getChildAt(2);
                ((TextView)tabViewChild4).setTypeface(typefaceMedium);

                View tabViewChild5 = mLinearSymptomDifficulty.getChildAt(2);
                ((TextView)tabViewChild5).setTypeface(typefaceMedium);

                View tabViewChild6 = mLinearSymptomWheezing.getChildAt(2);
                ((TextView)tabViewChild6).setTypeface(typefaceMedium);

                View tabViewChild7 = mLinearSymptomEtc.getChildAt(2);
                ((TextView)tabViewChild7).setTypeface(typefaceMedium);
            }
        },100);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")){
                try {
                    mTextViewDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0,10))));
                    mTextViewTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11,18))));
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (beforeIntent.hasExtra("category")){
                    String[] categoryList = beforeIntent.getStringExtra("category").split(",");
                    for (int i = 0; i < categoryList.length; i++){
                        mSaveBeforeCategoryArray.add(categoryList[i]);
                        if (categoryList[i].equals("11")){
                            SymptomLinearLayoutAdd(mLinearSymptomCough,categoryList[i]);
                        }else if (categoryList[i].equals("16")){
                            SymptomLinearLayoutAdd(mLinearSymptomPhlegm,categoryList[i]);
                        }else if (categoryList[i].equals("14")){
                            SymptomLinearLayoutAdd(mLinearSymptomFrustrated,categoryList[i]);
                        }else if (categoryList[i].equals("12")){
                            SymptomLinearLayoutAdd(mLinearSymptomDifficulty,categoryList[i]);
                        }else if (categoryList[i].equals("13")){
                            SymptomLinearLayoutAdd(mLinearSymptomWheezing,categoryList[i]);
                        }else if (categoryList[i].equals("15")){
                            SymptomLinearLayoutAdd(mLinearSymptomEtc,categoryList[i]);
                        }else if (categoryList[i].equals("40")){
                            SymptomLinearLayoutAdd(mLinearSymptomNone,categoryList[i]);
                        }
                    }
                }

                mEditViewMemo.setText(beforeIntent.getStringExtra("memo"));
                mSaveBeforeMemo = beforeIntent.getStringExtra("memo");
                mSaveAfterMemo = beforeIntent.getStringExtra("memo");

                if (beforeIntent.getStringExtra("cause").length() > 0){

                    String[] causeList = beforeIntent.getStringExtra("cause").split(",");

                    List<Integer> intList = new ArrayList<Integer>(causesIdx.length);
                    for (int i : causesIdx)
                    {
                        intList.add(i);
                    }

                    for (int i = 0; i < causeList.length; i++){
                        mSaveBeforeCauseIdxArray.add(causeList[i]);
                        if (intList.contains(Integer.parseInt(causeList[i]))){
                            int finalI = intList.indexOf(Integer.parseInt(causeList[i]));
                            if (mCause.length() == 0){
                                mCause += "" + causesIdx[finalI];
                                mCauseText += causes[finalI];
                            }else{
                                mCause += "," + causesIdx[finalI];
                                mCauseText += "," + causes[finalI];
                            }
                            if (finalI == 0){
                                CauseLinearLayoutAdd(mLinearCause01,causesIdx[finalI]);
                            }else if (finalI == 1){
                                CauseLinearLayoutAdd(mLinearCause02,causesIdx[finalI]);
                            }else if (finalI == 2){
                                CauseLinearLayoutAdd(mLinearCause03,causesIdx[finalI]);
                            }else if (finalI == 3){
                                CauseLinearLayoutAdd(mLinearCause04,causesIdx[finalI]);
                            }else if (finalI == 4){
                                CauseLinearLayoutAdd(mLinearCause05,causesIdx[finalI]);
                            }else if (finalI == 5){
                                CauseLinearLayoutAdd(mLinearCause06,causesIdx[finalI]);
                            }else if (finalI == 6){
                                CauseLinearLayoutAdd(mLinearCause07,causesIdx[finalI]);
                            }else if (finalI == 7){
                                CauseLinearLayoutAdd(mLinearCause08,causesIdx[finalI]);
                            }else if (finalI == 8){
                                CauseLinearLayoutAdd(mLinearCause09,causesIdx[finalI]);
                            }else if (finalI == 9){
                                CauseLinearLayoutAdd(mLinearCause10,causesIdx[finalI]);
                            }else if (finalI == 10){
                                CauseLinearLayoutAdd(mLinearCause11,causesIdx[finalI]);
                            }else if (finalI == 11){
                                CauseLinearLayoutAdd(mLinearCause12,causesIdx[finalI]);
                            }else if (finalI == 12){
                                CauseLinearLayoutAdd(mLinearCause13,causesIdx[finalI]);
                            }else if (finalI == 13){
                                CauseLinearLayoutAdd(mLinearCause14,causesIdx[finalI]);
                            }else if (finalI == 14){
                                CauseLinearLayoutAdd(mLinearCause15,causesIdx[finalI]);
                            }else if (finalI == 15){
                                CauseLinearLayoutAdd(mLinearCause16,causesIdx[finalI]);
                            }
                        }
                    }
                    mCauseIdxArray1.addAll(mCauseIdxArray);
                    mLinearCauseArray1.addAll(mLinearCauseArray);
                    mTextViewCause.setText(mCauseText);
                }else{

                }
                mImageViewRemove.setVisibility(View.VISIBLE);
            }else if (beforeIntent.hasExtra("date")){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        View tabViewChild = mLinearSymptomNone.getChildAt(2);
                        ((TextView)tabViewChild).setTypeface(typefaceBold);
                    }
                },100);

                SymptomLinearLayoutAdd(mLinearSymptomNone,"40");
                mSaveBeforeCategoryArray.add("40");
                mTextViewDate.setText(beforeIntent.getStringExtra("date"));
                mTextViewTime.setText(beforeIntent.getStringExtra("time"));
                try {
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        View tabViewChild = mLinearSymptomNone.getChildAt(2);
                        ((TextView)tabViewChild).setTypeface(typefaceBold);
                    }
                },100);
                SymptomLinearLayoutAdd(mLinearSymptomNone,"40");
                mSaveBeforeCategoryArray.add("40");
                mTextViewDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
                mTextViewTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
                try {
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    View tabViewChild = mLinearSymptomNone.getChildAt(2);
                    ((TextView)tabViewChild).setTypeface(typefaceBold);
                }
            },100);
            SymptomLinearLayoutAdd(mLinearSymptomNone,"40");
            mSaveBeforeCategoryArray.add("40");
            mTextViewDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            mTextViewTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
            try {
                mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try {
            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
            mSaveBeforeRegisterDt = mRegisterDt;
            mSaveAfterRegisterDt = mRegisterDt;
        } catch (ParseException e) {
            e.printStackTrace();
            mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
            mSaveBeforeRegisterDt = mRegisterDt;
            mSaveAfterRegisterDt = mRegisterDt;
        }

        mEditViewMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextViewMemoLength.setText("("+s.length()+"/2000)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditViewMemo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.smoothScrollTo(0, mScrollView.getBottom() + mScrollView.getScrollY());
                    }
                });
                }
            }
        });

        mEditViewMemo.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (mEditViewMemo.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });
    }

    public void setHideKeyboard(final Context context, View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText || view instanceof ScrollView)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return false;
                    }

                });
            }

            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {

                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setHideKeyboard(context, innerView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void DateTimePicker(final int flag, String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker, null);
        final Dialog dateDialog = new Dialog(NewSymptomRecordActivity.this);
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
                dateCheck = true;
                if (flag == 1){

                    mTextViewDate.setText(formatYYYYMMDD2.format(date));
                    try {
                        mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                        mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                        mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                        mSaveAfterRegisterDt = mRegisterDt;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{

                    mTextViewTime.setText(Utils.formatHHMM.format(date));

                    try {
                        SimpleDateFormat ttDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm");
                        if (new Date(System.currentTimeMillis()).after(ttDateFormat.parse(mTextViewDate.getText().toString() + " " + Utils.formatHHMM.format(date)))){
                            Log.i(TAG,"이후");
                        }else{
                            Log.i(TAG,"이전");
                        }
                        mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                        mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString()));
                        mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                        mSaveAfterRegisterDt = mRegisterDt;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    NetworkCall(SELECT_HOME_FEED_LIST);
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

    void SymptomLinearLayoutAdd(LinearLayout mLinearLayout, String category){
        Log.i(TAG,"change");
        if (mLinearSymptomArray != null){
            if (mLinearSymptomArray.contains(mLinearLayout)){
                Log.i(TAG,"2222");
                mLinearSymptomArray.remove(mLinearLayout);
                mCategoryArray.remove(category);
                mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_off);

                View tabViewChild = mLinearLayout.getChildAt(2);
                ((TextView)tabViewChild).setTypeface(typefaceMedium);
                ((TextView)tabViewChild).setTextColor(Color.parseColor("#1a1a1a"));

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        View tabViewChild = mLinearLayout.getChildAt(2);
//                        ((TextView)tabViewChild).setTypeface(typefaceMedium);
//                        ((TextView)tabViewChild).setTextColor(Color.parseColor("#1a1a1a"));
//                    }
//                },100);
                if (mLinearLayout == mLinearSymptomNone){
                    if (mLinearCauseVisible.getVisibility() == View.GONE){
                        Expand(mLinearCauseVisible);
                        mCategoryArray = new ArrayList<>();
                    }
                }
            }else{
                if (mLinearLayout == mLinearSymptomNone){
                    Log.i(TAG,"3333");
                    for (int i = 0; i < mLinearSymptomArray.size(); i++){
                        mLinearSymptomArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_off);
                        View tabViewChild = mLinearSymptomArray.get(i).getChildAt(2);
                        ((TextView)tabViewChild).setTypeface(typefaceMedium);
                        ((TextView)tabViewChild).setTextColor(Color.parseColor("#1a1a1a"));
                    }
                    mCategoryArray = new ArrayList<>();
                    mCategoryArray.add("40");
                    mLinearSymptomArray = new ArrayList<>();
                    mLinearSymptomArray.add(mLinearSymptomNone);
                    mLinearSymptomNone.setBackgroundResource(R.drawable.symptom_cause_bg_on);

                    View tabViewChild = mLinearSymptomNone.getChildAt(2);
                    ((TextView)tabViewChild).setTypeface(typefaceBold);
                    ((TextView)tabViewChild).setTextColor(Color.parseColor("#44ad60"));

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            View tabViewChild = mLinearSymptomNone.getChildAt(2);
//                            ((TextView)tabViewChild).setTypeface(typefaceBold);
//                            ((TextView)tabViewChild).setTextColor(Color.parseColor("#44ad60"));
//                        }
//                    },100);

                    if (mLinearCauseVisible.getVisibility() == View.VISIBLE){
                        Collapse(mLinearCauseVisible);
//                        mCause = "";
//                        mCauseText = "";
                    }
//                    mLinearCauseVisible.setVisibility(View.GONE);
                }else{
                    Log.i(TAG,"4444");
                    if (mLinearSymptomArray.contains(mLinearSymptomNone)){
                        mCategoryArray.remove("40");
                        mLinearSymptomArray.remove(mLinearSymptomNone);

                        View tabViewChild = mLinearSymptomNone.getChildAt(2);
                        ((TextView)tabViewChild).setTypeface(typefaceMedium);
                        ((TextView)tabViewChild).setTextColor(Color.parseColor("#1a1a1a"));

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                View tabViewChild = mLinearSymptomNone.getChildAt(2);
//                                ((TextView)tabViewChild).setTypeface(typefaceMedium);
//                                ((TextView)tabViewChild).setTextColor(Color.parseColor("#1a1a1a"));
//                            }
//                        },100);

                        mLinearSymptomNone.setBackgroundResource(R.drawable.symptom_cause_bg_off);
                    }
                    mCategoryArray.add(category);
                    mLinearSymptomArray.add(mLinearLayout);
                    mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_on);

                    View tabViewChild = mLinearLayout.getChildAt(2);
                    ((TextView)tabViewChild).setTypeface(typefaceBold);
                    ((TextView)tabViewChild).setTextColor(Color.parseColor("#44ad60"));

//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            View tabViewChild = mLinearLayout.getChildAt(2);
//                            ((TextView)tabViewChild).setTypeface(typefaceBold);
//                            ((TextView)tabViewChild).setTextColor(Color.parseColor("#44ad60"));
//                        }
//                    },100);
                    if (mLinearCauseVisible.getVisibility() == View.GONE){
                        Expand(mLinearCauseVisible);
                    }
                }
            }
        }else{
            mLinearSymptomArray = new ArrayList<>();
            mLinearSymptomArray.add(mLinearLayout);
            mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_on);

            View tabViewChild = mLinearLayout.getChildAt(2);
            ((TextView)tabViewChild).setTypeface(typefaceBold);
            ((TextView)tabViewChild).setTextColor(Color.parseColor("#44ad60"));

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    View tabViewChild = mLinearLayout.getChildAt(2);
//                    ((TextView)tabViewChild).setTypeface(typefaceBold);
//                    ((TextView)tabViewChild).setTextColor(Color.parseColor("#44ad60"));
//                }
//            },100);
            if (mLinearLayout == mLinearSymptomNone){
                if (mLinearCauseVisible.getVisibility() == View.VISIBLE){
                    Collapse(mLinearCauseVisible);
                }
            }
        }
    }

    void CauseLinearLayoutAdd(LinearLayout mLinearLayout, int causeIdx){
        if (mLinearCauseArray != null) {
            if (mLinearCauseArray.contains(mLinearLayout)){
                mLinearCauseArray.remove(mLinearLayout);
                mCauseIdxArray.remove(""+causeIdx);
                mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_off);
            }else{
                mLinearCauseArray.add(mLinearLayout);
                mCauseIdxArray.add(""+causeIdx);
                mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_on);
            }
            if (mLinearCauseArray.size() == 0){
                mButtonPopupSave.setBackgroundTintList(getColorStateList(R.color.f5f6fa));
                mButtonPopupSave.setTextColor(getColor(R.color.black));
            }else{
                mButtonPopupSave.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                mButtonPopupSave.setTextColor(getColor(R.color.white));
            }
        }else{
            mLinearCauseArray = new ArrayList<>();
            mLinearCauseArray.add(mLinearLayout);
            mCauseIdxArray.add(""+causeIdx);
            mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_on);
        }
    }

    void SymptomJsonMake(String cause, String registerDt, String category){
        try {
            mFeedArray = new JSONArray();
            mFeedObject = new JSONObject();
            mFeedObject.accumulate("USER_NO",""+userItem.getUserNo());
            mFeedObject.accumulate("NICKNAME",userItem.getNickname());
            mFeedObject.accumulate("GENDER",""+userItem.getGender());
            mFeedObject.accumulate("BIRTH",""+userItem.getBirth());
            if (category.equals("40")){
                mFeedObject.accumulate("CAUSE","");
            }else{
                mFeedObject.accumulate("CAUSE",""+cause);
            }
            mFeedObject.accumulate("MEMO",mEditViewMemo.getText().toString());
            mFeedObject.accumulate("REGISTER_DT",registerDt);
            mFeedObject.accumulate("CATEGORY",Integer.valueOf(category));
            mFeedArray.put(mFeedObject);
            Log.i(TAG,"aa : " + mFeedArray.toString());
            NetworkCall(NEW_SYMPTOM_LIST_INSERT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(NEW_SYMPTOM_LIST_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mFeedArray.toString());
        }else if (mCode.equals(NEW_SYMPTOM_LIST_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"));
        }else if (mCode.equals(NEW_SYMPTOM_LIST_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"));
        }else if (mCode.equals(SELECT_HOME_FEED_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),mYear,mMonth);
        }else if (mCode.equals(SELECT_HOME_FEED_LIST_CHECK)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),mYear,mMonth);
        }else if (mCode.equals(ALARM_LIST_ALL_READ)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+String.valueOf(userItem.getUserNo()));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(NEW_SYMPTOM_LIST_INSERT)){
                if (mLastSession){
                    setResult(RESULT_OK);
                    if (beforeIntent.hasExtra("push")){
                        Intent i = new Intent(NewSymptomRecordActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        finish();
                    }
                }
            }else if (mCode.equals(NEW_SYMPTOM_LIST_UPDATE)){
                try {
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
                }
                for (int i = 0; i < mCategoryArray.size(); i++){
                    if (i == mCategoryArray.size()-1){
                        mLastSession = true;
                    }
                    SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
                }
            }else if (mCode.equals(NEW_SYMPTOM_LIST_DELETE)){
                mImageViewRemove.setClickable(true);
                setResult(RESULT_OK);
                finish();
            }else if (mCode.equals(SELECT_HOME_FEED_LIST)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                            if (JsonIsNullCheck(object,"CATEGORY").equals("11") || JsonIsNullCheck(object,"CATEGORY").equals("16") ||
                                    JsonIsNullCheck(object,"CATEGORY").equals("14") || JsonIsNullCheck(object,"CATEGORY").equals("12") ||
                                    JsonIsNullCheck(object,"CATEGORY").equals("13") || JsonIsNullCheck(object,"CATEGORY").equals("15") ||
                                    JsonIsNullCheck(object,"CATEGORY").equals("40")){
                                if (JsonIsNullCheck(object,"REGISTER_DT").equals(mRegisterDt)){
                                    dateCheck = false;
                                    OneButtonDialog oneButtonDialog = new OneButtonDialog(NewSymptomRecordActivity.this,"증상기록 중복","선택한 시간에 이미\n증상 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
                                    oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                        @Override
                                        public void OnButtonClick(View v) {
                                            oneButtonDialog.dismiss();
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(SELECT_HOME_FEED_LIST_CHECK)){
//                try {
//                    JSONObject jsonObject = new JSONObject(mResult);
//                    if (jsonObject.has("list")){
//                        JSONArray jsonArray = jsonObject.getJSONArray("list");
//                        for (int i = 0; i < jsonArray.length(); i++){
//                            JSONObject object = jsonArray.getJSONObject(i);
//                            Log.i(TAG,"object : " + object.toString());
//                            if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
//                                if (JsonIsNullCheck(object,"CATEGORY").equals("11") || JsonIsNullCheck(object,"CATEGORY").equals("16") ||
//                                        JsonIsNullCheck(object,"CATEGORY").equals("14") || JsonIsNullCheck(object,"CATEGORY").equals("12") ||
//                                        JsonIsNullCheck(object,"CATEGORY").equals("13") || JsonIsNullCheck(object,"CATEGORY").equals("15") ||
//                                        JsonIsNullCheck(object,"CATEGORY").equals("40")){
//                                    if (JsonIsNullCheck(object,"REGISTER_DT").equals(mRegisterDt)){
//                                        if (beforeIntent != null){
//                                            if (beforeIntent.hasExtra("registerDt")){
//                                                mButtonFinish.setEnabled(true);
//                                            }else{
//                                                dateCheck = false;
//                                                OneButtonDialog oneButtonDialog = new OneButtonDialog(NewSymptomRecordActivity.this,"증상기록 중복","선택한 시간에 이미\n증상 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
//                                                oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
//                                                    @Override
//                                                    public void OnButtonClick(View v) {
//                                                        mButtonFinish.setEnabled(true);
//                                                        oneButtonDialog.dismiss();
//                                                    }
//                                                });
//                                                break;
//                                            }
//                                        }else{
//                                            dateCheck = false;
//                                            OneButtonDialog oneButtonDialog = new OneButtonDialog(NewSymptomRecordActivity.this,"증상기록 중복","선택한 시간에 이미\n증상 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
//                                            oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
//                                                @Override
//                                                public void OnButtonClick(View v) {
//                                                    mButtonFinish.setEnabled(true);
//                                                    oneButtonDialog.dismiss();
//                                                }
//                                            });
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    if (dateCheck){
//                        if (mCategoryArray.size() > 0){
//                            if (mCategoryArray.contains("40")){
//                                if (beforeIntent != null){
//                                    if (beforeIntent.hasExtra("registerDt")){
//                                        NetworkCall(NEW_SYMPTOM_LIST_UPDATE);
//                                    }else{
//                                        try {
//                                            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                        for (int i = 0; i < mCategoryArray.size(); i++){
//                                            if (i == mCategoryArray.size()-1){
//                                                mLastSession = true;
//                                            }
//                                            SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
//                                        }
//                                    }
//                                }else{
//                                    try {
//                                        mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//                                    for (int i = 0; i < mCategoryArray.size(); i++){
//                                        if (i == mCategoryArray.size()-1){
//                                            mLastSession = true;
//                                        }
//                                        SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
//                                    }
//                                }
//                            }else{
//                                if (mCause.length() == 0){
//                                    //팝업
//                                    Toast.makeText(this, "증상 발생 원인을 선택해주세요.", Toast.LENGTH_SHORT).show();
//                                    //scrollTo
//                                    mScrollView.post(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mButtonFinish.setEnabled(true);
//                                            mScrollView.scrollTo(0, mLinearCauseVisible.getTop());
//                                        }
//                                    });
//                                }else{
//                                    if (beforeIntent != null){
//                                        if (beforeIntent.hasExtra("registerDt")){
//                                            NetworkCall(NEW_SYMPTOM_LIST_UPDATE);
//                                        }else{
//                                            try {
//                                                mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
//                                            } catch (ParseException e) {
//                                                e.printStackTrace();
//                                            }
//                                            for (int i = 0; i < mCategoryArray.size(); i++){
//                                                if (i == mCategoryArray.size()-1){
//                                                    mLastSession = true;
//                                                }
//                                                SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
//                                            }
//                                        }
//                                    }else{
//                                        try {
//                                            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                        for (int i = 0; i < mCategoryArray.size(); i++){
//                                            if (i == mCategoryArray.size()-1){
//                                                mLastSession = true;
//                                            }
//                                            SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
//                                        }
//                                    }
//                                }
//                            }
//                        }else{
//                            Toast.makeText(this, "증상 종류를 선택해주세요.", Toast.LENGTH_SHORT).show();
//                            mScrollView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mScrollView.scrollTo(0, mLinearCause01.getTop());
//                                }
//                            });
//                        }
//                    }
//                }catch (JSONException e){
//
//                }
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (mSaveBeforeCategoryArray.size() == mCategoryArray.size() && mSaveBeforeCauseIdxArray.size() == mCauseIdxArray.size() && mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt) && mSaveBeforeMemo.equals(mEditViewMemo.getText().toString())){
            for (int i = 0; i < mSaveBeforeCategoryArray.size(); i++){
                if (mCategoryArray.contains(mSaveBeforeCategoryArray.get(i))){
                    mCheck = true;
                }else{
                    mCheck = false;
                    break;
                }
            }
            if (mCheck){
                for (int i = 0; i < mSaveBeforeCauseIdxArray.size(); i++){
                    if (mCauseIdxArray.contains(mSaveBeforeCauseIdxArray.get(i))){
                        mCheck = true;
                    }else{
                        mCheck = false;
                        break;
                    }
                }
            }
        }else{
            mCheck = false;
        }

        if (!mCheck){
            twoButtonDialog = new TwoButtonDialog(NewSymptomRecordActivity.this,"증상기록", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
            twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                @Override
                public void OkButtonClick(View v) {
                    twoButtonDialog.dismiss();
                    if (beforeIntent.hasExtra("push")){
                        Intent i = new Intent(NewSymptomRecordActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        finish();
                    }
                }
            });
            twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                @Override
                public void CancelButtonClick(View v) {
                    twoButtonDialog.dismiss();
                }
            });
        }else{
            if (beforeIntent.hasExtra("push")){
                Intent i = new Intent(NewSymptomRecordActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                finish();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_new_symptom_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_new_symptom_record_remove : {
                mImageViewRemove.setClickable(false);
                twoButtonDialog = new TwoButtonDialog(NewSymptomRecordActivity.this,"증상기록", "기록을 삭제하시겠습니까?","취소", "확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        twoButtonDialog.dismiss();
                        NetworkCall(NEW_SYMPTOM_LIST_DELETE);
                    }
                });

                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        mImageViewRemove.setClickable(true);
                        twoButtonDialog.dismiss();
                    }
                });
                break;
            }
            case R.id.txt_new_symptom_record_date : {
                DateTimePicker(1,"날짜를 선택해주세요.");
                break;
            }
            case R.id.txt_new_symptom_record_time : {
                DateTimePicker(2,"시간을 선택해주세요.");
                break;
            }
            case R.id.lin_new_symptom_record_none : {
                SymptomLinearLayoutAdd(mLinearSymptomNone,"40");
                break;
            }
            case R.id.lin_new_symptom_record_cough : {
                SymptomLinearLayoutAdd(mLinearSymptomCough,String.valueOf(categoryIdx[0]));
                break;
            }
            case R.id.lin_new_symptom_record_phlegm : {
                SymptomLinearLayoutAdd(mLinearSymptomPhlegm,String.valueOf(categoryIdx[1]));
                break;
            }
            case R.id.lin_new_symptom_record_frustrated : {
                SymptomLinearLayoutAdd(mLinearSymptomFrustrated,String.valueOf(categoryIdx[2]));
                break;
            }
            case R.id.lin_new_symptom_record_difficulty : {
                SymptomLinearLayoutAdd(mLinearSymptomDifficulty,String.valueOf(categoryIdx[3]));
                break;
            }
            case R.id.lin_new_symptom_record_wheezing : {
                SymptomLinearLayoutAdd(mLinearSymptomWheezing,String.valueOf(categoryIdx[4]));
                break;
            }
            case R.id.lin_new_symptom_record_etc : {
                SymptomLinearLayoutAdd(mLinearSymptomEtc,String.valueOf(categoryIdx[5]));
                break;
            }
            case R.id.txt_new_symptom_record_cause : {
                if (mCause.length() == 0){
                    if (mLinearCauseArray != null){
                        for (int i = 0; i < mLinearCauseArray.size(); i++){
                            mLinearCauseArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_off);
                        }
                    }
                    mLinearCauseArray = new ArrayList<>();
                    mCauseIdxArray = new ArrayList<>();
                }else{
                    for (int i = 0; i < mLinearCauseArray.size(); i++){
                        mLinearCauseArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_off);
                    }

                    mLinearCauseArray = new ArrayList<>();
                    mCauseIdxArray = new ArrayList<>();

                    mCauseIdxArray.addAll(mCauseIdxArray1);
                    mLinearCauseArray.addAll(mLinearCauseArray1);

                    Log.i(TAG,"mCauseIdxArray.size() : " + mCauseIdxArray.size());

                    for (int i = 0; i < mCauseIdxArray.size(); i++){
                        mLinearCauseArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_on);
                    }
                }
                mCauseBottomSheetDialog.show();
                break;
            }
            case R.id.btn_new_symptom_record_finish : {
                mButtonFinish.setEnabled(false);
//                NetworkCall(SELECT_HOME_FEED_LIST_CHECK);

                if (mCategoryArray.size() > 0){
                    if (mCategoryArray.contains("40")){
                        if (beforeIntent != null){
                            if (beforeIntent.hasExtra("registerDt")){
                                NetworkCall(NEW_SYMPTOM_LIST_UPDATE);
                            }else{
                                try {
                                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                for (int i = 0; i < mCategoryArray.size(); i++){
                                    if (i == mCategoryArray.size()-1){
                                        mLastSession = true;
                                    }
                                    SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
                                }
                            }
                        }else{
                            try {
                                mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < mCategoryArray.size(); i++){
                                if (i == mCategoryArray.size()-1){
                                    mLastSession = true;
                                }
                                SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
                            }
                        }
                    }else{
                        if (mCause.length() == 0){
                            //팝업
                            Toast.makeText(this, "증상 발생 원인을 선택해주세요.", Toast.LENGTH_SHORT).show();
                            //scrollTo
                            mScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mButtonFinish.setEnabled(true);
                                    mScrollView.scrollTo(0, mLinearCauseVisible.getTop());
                                }
                            });
                        }else{
                            if (beforeIntent != null){
                                if (beforeIntent.hasExtra("registerDt")){
                                    NetworkCall(NEW_SYMPTOM_LIST_UPDATE);
                                }else{
                                    try {
                                        mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    for (int i = 0; i < mCategoryArray.size(); i++){
                                        if (i == mCategoryArray.size()-1){
                                            mLastSession = true;
                                        }
                                        SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
                                    }
                                }
                            }else{
                                try {
                                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(mTextViewDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(mTextViewTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                for (int i = 0; i < mCategoryArray.size(); i++){
                                    if (i == mCategoryArray.size()-1){
                                        mLastSession = true;
                                    }
                                    SymptomJsonMake(mCause,mRegisterDt,mCategoryArray.get(i));
                                }
                            }
                        }
                    }
                }else{
                    mButtonFinish.setEnabled(true);
                    Toast.makeText(this, "증상 종류를 선택해주세요.", Toast.LENGTH_SHORT).show();
                    mScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.scrollTo(0, mLinearCause01.getTop());
                        }
                    });
                }
                break;
            }
            case R.id.lin_cause_popup_01 : {
                CauseLinearLayoutAdd(mLinearCause01,causesIdx[0]);
                break;
            }
            case R.id.lin_cause_popup_02 : {
                CauseLinearLayoutAdd(mLinearCause02,causesIdx[1]);
                break;
            }
            case R.id.lin_cause_popup_03 : {
                CauseLinearLayoutAdd(mLinearCause03,causesIdx[2]);
                break;
            }
            case R.id.lin_cause_popup_04 : {
                CauseLinearLayoutAdd(mLinearCause04,causesIdx[3]);
                break;
            }
            case R.id.lin_cause_popup_05 : {
                CauseLinearLayoutAdd(mLinearCause05,causesIdx[4]);
                break;
            }
            case R.id.lin_cause_popup_06 : {
                CauseLinearLayoutAdd(mLinearCause06,causesIdx[5]);
                break;
            }
            case R.id.lin_cause_popup_07 : {
                CauseLinearLayoutAdd(mLinearCause07,causesIdx[6]);
                break;
            }
            case R.id.lin_cause_popup_08 : {
                CauseLinearLayoutAdd(mLinearCause08,causesIdx[7]);
                break;
            }
            case R.id.lin_cause_popup_09 : {
                CauseLinearLayoutAdd(mLinearCause09,causesIdx[8]);
                break;
            }
            case R.id.lin_cause_popup_10 : {
                CauseLinearLayoutAdd(mLinearCause10,causesIdx[9]);
                break;
            }
            case R.id.lin_cause_popup_11 : {
                CauseLinearLayoutAdd(mLinearCause11,causesIdx[10]);
                break;
            }
            case R.id.lin_cause_popup_12 : {
                CauseLinearLayoutAdd(mLinearCause12,causesIdx[11]);
                break;
            }
            case R.id.lin_cause_popup_13 : {
                CauseLinearLayoutAdd(mLinearCause13,causesIdx[12]);
                break;
            }
            case R.id.lin_cause_popup_14 : {
                CauseLinearLayoutAdd(mLinearCause14,causesIdx[13]);
                break;
            }
            case R.id.lin_cause_popup_15 : {
                CauseLinearLayoutAdd(mLinearCause15,causesIdx[14]);
                break;
            }
            case R.id.lin_cause_popup_16 : {
                CauseLinearLayoutAdd(mLinearCause16,causesIdx[15]);
                break;
            }
            case R.id.btn_causes_popup_save : {
                if (mCauseIdxArray.size() != 0){
                    mCause = "";
                    mCauseText = "";
                    
                    mRelaCause.setBackgroundResource(R.drawable.symptom_cause_bg_on);

                    mCauseIdxArray1 = new ArrayList<>();
                    mLinearCauseArray1 = new ArrayList<>();

                    mCauseIdxArray1.addAll(mCauseIdxArray);
                    mLinearCauseArray1.addAll(mLinearCauseArray);

                    Log.i(TAG,"cause size : " + mCauseIdxArray1.size());

                    for (int i = 0; i < causesIdx.length; i++){
                        if (mCauseIdxArray.contains(""+causesIdx[i])){
                            if (mCause.length() == 0){
                                mCause += "" + causesIdx[i];
                                mCauseText += causes[i];
                            }else{
                                mCause += "," + causesIdx[i];
                                mCauseText += "," + causes[i];
                            }
                        }
                    }
                    if (mCause.length() == 0){
                        mRelaCause.setBackgroundResource(R.drawable.symptom_cause_bg_off);
                    }
                    mTextViewCause.setText(mCauseText);
                    mCauseBottomSheetDialog.dismiss();
                }

                break;
            }
        }
    }
}