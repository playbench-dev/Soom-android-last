package com.kmw.soom2.ex.Static;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.UrlWebViewActivity;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Activitys.NewStaticSymptomActivity;
import com.kmw.soom2.Reports.Item.HistoryItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.formatYYYYMM;

public class exStaticSymptomActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "NewStaticSymptomActivity";
    private ProgressDialog progressDialog;
    SimpleDateFormat dataFormatYYYYMM = new SimpleDateFormat("yyyy년 MM월");

    LinearLayout linEnvBtn, linBadBtn, linAllergyBtn;
    LinearLayout linLeftBtn, linRightBtn;
    public TextView txtCalendarTitle;
    TextView mTextViewNoData;
    Calendar currentCalendar;
    Date currentDate = Calendar.getInstance().getTime();
    TextView btnBack;
    TextView txtNextBtn;
    LinearLayout mLinearSymptomBar;
    TextView txtSymptomCough, txtSymptomPhlegm,txtSymptombreath,txtSymptomStuffy,txtSymptomWheeze, txtSymptomEtc, txtSymptomNone;
    LinearLayout linSelectTimezone;
    TextView btnSelectTimeZone;

    TextView txtCause1, txtCause2, txtCause3, txtCause4, txtCause5, txtCause6, txtCause7, txtCause8, txtCause9, txtCause10, txtCause11, txtCause12, txtCause13, txtCause14, txtCause15, txtCause16;

    BarChart barChart;
//    CustomPopupBasic symptomPickerView;

    ArrayList<HistoryItems> coughItems, breathItems, wheezeItems, stuffyItems, etcItems,phlegmItems,noneItems;
    ArrayList<String> registerDtArray;

    Double[] pieValues = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    String[] barChartXAxisText = {"새벽", "오전", "오후", "저녁"};
    String[] barChartvaluesTitle = {"전체", "기침", "천명음", "숨쉬기 불편", "가슴답답함","기타증상"};

    float[] dawns ;
    float[] mornings ;
    float[] afternoons ;
    float[] nights;
    int[] causes = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String stCause = "";

    String response;

    int selectedIndex = 0;  // 0

    ArrayList<String> causeArr;

    RelativeLayout rllEnv, rllAllergy, rllBad;

    //popup
    private BottomSheetDialog mSymptomBottomSheetDialog;
    private View mBottomSheetDialogSymptom;
    private Button mButtonPopupSave;
    private LinearLayout mLinearSymptomAll, mLinearSymptomCough, mLinearSymptomPhlegm, mLinearSymptomBreath, mLinearSymptomStuffy, mLinearSymptomWheeze, mLinearSymptomEtc, mLinearSymptomNone;
    private ArrayList<LinearLayout> mLinearSymptomArray = new ArrayList<>();
    private ArrayList<LinearLayout> mLinearSymptomArray1 = new ArrayList<>();
    private ArrayList<String> mCategoryArray = new ArrayList<>();
    private ArrayList<String> mCategoryArray1 = new ArrayList<>();
    private ArrayList<String> mList = new ArrayList<>();

    String[] registerList = new String[]{"2021-06-01","2021-06-01"};
    private String[] causes11 = new String[]{"감기", "미세먼지", "찬바람", "운동", "집먼지진드기", "애완동물", "음식알러지", "꽃가루", "스트레스", "담배연기", "요리연기", "기타", "강한냄새", "건조함", "곰팡이", "높은습도"};

    Button btnMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_static_symptom);

        findViewByIds();

        NullCheck(this);
        mList.add("11");
        mList.add("16");
        mList.add("14");
        mList.add("12");
        mList.add("13");
        mList.add("15");
        mList.add("40");
        mCategoryArray.add("100");
        mCategoryArray1.add("100");
        mLinearSymptomArray.add(mLinearSymptomAll);
        mLinearSymptomArray1.add(mLinearSymptomAll);

        coughItems = new ArrayList<>();
        wheezeItems = new ArrayList<>();
        breathItems = new ArrayList<>();
        stuffyItems = new ArrayList<>();
        etcItems    = new ArrayList<>();
        phlegmItems = new ArrayList<>();
        noneItems   = new ArrayList<>();
        registerDtArray = new ArrayList<>();

        causeArr = new ArrayList<>();
        stCause = "";
        dawns = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f};
        mornings = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f};
        afternoons = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f};
        nights = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f,0.0f, 0.0f};

        mLinearSymptomBar.setVisibility(View.VISIBLE);
        mTextViewNoData.setVisibility(View.GONE);

        HistoryItems historyItem = new HistoryItems();

        for (int i = 0; i < 3; i++){
            historyItem.setCategory(11);
            historyItem.setRegisterDt(registerList[0]);
            historyItem.setCause("11");

            coughItems.add(historyItem);

            if (i % 2 == 0) {
                dawns[0] = dawns[0] + 1;
            } else {
                nights[0] = nights[0] + 1;
            }
        }

        for (int i = 0; i < 3; i++){
            historyItem.setCategory(12);
            historyItem.setRegisterDt(registerList[0]);
            historyItem.setCause("12");

            stuffyItems.add(historyItem);

            if (i % 3 == 0) {
                dawns[3] = dawns[3] + 1;
            } else {
                mornings[3] = mornings[3] + 1;
            }
        }

        for (int i = 0; i < 3; i++){
            historyItem.setCategory(13);
            historyItem.setRegisterDt(registerList[0]);
            historyItem.setCause("13");

            wheezeItems.add(historyItem);

            if (i % 2 == 0) {
                mornings[4] = mornings[4] + 1;
            } else {
                afternoons[4] = afternoons[4] + 1;
            }

        }

        for (int i = 0; i < 6; i++){
            historyItem.setCategory(14);
            historyItem.setRegisterDt(registerList[0]);
            historyItem.setCause("14");

            breathItems.add(historyItem);

            if (i % 3 == 0) {
                dawns[2] = dawns[2] + 1;
            } else {
                mornings[2] = mornings[2] + 1;
            }
        }

        for (int i = 0; i < 10; i++){
            historyItem.setCategory(15);
            historyItem.setRegisterDt(registerList[0]);
            historyItem.setCause("3");

            etcItems.add(historyItem);

            if (i % 2 == 0) {
                mornings[5] = mornings[5] + 1;
            } else {
                afternoons[5] = afternoons[5] + 1;
            }
        }

        for (int i = 0; i < 2; i++){
            historyItem.setCategory(16);
            historyItem.setRegisterDt(registerList[0]);
            historyItem.setCause("2");

            phlegmItems.add(historyItem);

            if (i % 3 == 0) {
                dawns[1] = dawns[1] + 1;
            } else {
                mornings[1] = mornings[1] + 1;
            }
        }

        for (int i = 0; i < 4; i++){
            historyItem.setCategory(40);
            historyItem.setRegisterDt(registerList[0]);
            historyItem.setCause("1");

            noneItems.add(historyItem);

            if (i % 2 == 0) {
                dawns[6] = dawns[6] + 1;
            } else {
                nights[6] = nights[6] + 1;
            }
        }

        pieValues[0] = Double.valueOf(coughItems.size());
        txtSymptomCough.setText(coughItems.size() + "회");

        pieValues[1] = Double.valueOf(phlegmItems.size());
        txtSymptomPhlegm.setText(phlegmItems.size() + "회");

        pieValues[2] = Double.valueOf(breathItems.size());
        txtSymptombreath.setText(breathItems.size() + "회");

        pieValues[3] = Double.valueOf(stuffyItems.size());
        txtSymptomStuffy.setText(stuffyItems.size() + "회");

        pieValues[4] = Double.valueOf(wheezeItems.size());
        txtSymptomWheeze.setText(wheezeItems.size() + "회");

        pieValues[5] = Double.valueOf(etcItems.size());
        txtSymptomEtc.setText(etcItems.size() + "회");

        pieValues[6] = Double.valueOf(noneItems.size());
        txtSymptomNone.setText(noneItems.size() + "회");

        //memo
        setUpBarMake();
        setupBarChart();
        setCausesTexts();
    }

    private void findViewByIds() {

        txtCalendarTitle = findViewById(R.id.txt_statics_symptom_title);
        btnBack = findViewById(R.id.txt_statics_detail_back);
        btnMove = findViewById(R.id.btn_ex_symptom_static_move);

        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(exStaticSymptomActivity.this, NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        mLinearSymptomBar = (LinearLayout)findViewById(R.id.lin_new_static_symptom_radius_bar);
        mTextViewNoData = (TextView)findViewById(R.id.txt_new_static_symptom_no_data);
//        /// 증상 빈도

        txtSymptomCough = findViewById(R.id.txt_new_static_symptom_frequency_cough_cnt);
        txtSymptomPhlegm = findViewById(R.id.txt_new_static_symptom_frequency_phlegm_cnt);
        txtSymptombreath = findViewById(R.id.txt_new_static_symptom_frequency_breath_cnt);
        txtSymptomStuffy = findViewById(R.id.txt_new_static_symptom_frequency_frustrated_cnt);
        txtSymptomWheeze = findViewById(R.id.txt_new_static_symptom_frequency_wheeze_cnt);
        txtSymptomEtc    = findViewById(R.id.txt_new_static_symptom_frequency_etc_cnt);
        txtSymptomNone    = findViewById(R.id.txt_new_static_symptom_frequency_none_cnt);

//
//        /// 시간대별 증상
        linSelectTimezone = findViewById(R.id.lin_symptom_static_result_select_cause);
        barChart = findViewById(R.id.bar_chart_new_static_symptom);
        btnSelectTimeZone = findViewById(R.id.symptom_static_result_select_cause);
//
        currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));
//
        txtCalendarTitle.setText(formatYYYYMM.format(currentCalendar.getTime()));
//
        linLeftBtn = findViewById(R.id.lin_statics_symptom_left_btn);
        linRightBtn = findViewById(R.id.lin_statics_symptom_right_btn);
//
        txtCause1 = findViewById(R.id.symptom_static_cause_1);
        txtCause2 = findViewById(R.id.symptom_static_cause_2);
        txtCause3 = findViewById(R.id.symptom_static_cause_3);
        txtCause4 = findViewById(R.id.symptom_static_cause_4);
        txtCause5 = findViewById(R.id.symptom_static_cause_5);
        txtCause6 = findViewById(R.id.symptom_static_cause_6);
        txtCause7 = findViewById(R.id.symptom_static_cause_7);
        txtCause8 = findViewById(R.id.symptom_static_cause_8);
        txtCause9 = findViewById(R.id.symptom_static_cause_9);
        txtCause10 = findViewById(R.id.symptom_static_cause_10);
        txtCause11 = findViewById(R.id.symptom_static_cause_11);
        txtCause12 = findViewById(R.id.symptom_static_cause_12);
        txtCause13 = findViewById(R.id.symptom_static_cause_13);
        txtCause14 = findViewById(R.id.symptom_static_cause_14);
        txtCause15 = findViewById(R.id.symptom_static_cause_15);
        txtCause16 = findViewById(R.id.symptom_static_cause_16);
//
        txtNextBtn = findViewById(R.id.txt_statics_symptom_right_btn);
        txtNextBtn.setTextColor(Color.parseColor("#acacac"));

        linAllergyBtn = findViewById(R.id.lin_allergy_cause);
        linEnvBtn = findViewById(R.id.lin_env_cause);
        linBadBtn = findViewById(R.id.lin_bad_cause);

        //증상종류 선택팝업
        mSymptomBottomSheetDialog       = new BottomSheetDialog(this,R.style.Theme_Design_BottomSheetDialog);
        mBottomSheetDialogSymptom       = getLayoutInflater().inflate(R.layout.symptom_select_dialog, null);
        mLinearSymptomAll               = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_all);
        mLinearSymptomCough             = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_cough);
        mLinearSymptomPhlegm            = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_phlegm);
        mLinearSymptomBreath            = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_breath);
        mLinearSymptomStuffy            = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_difficulty);
        mLinearSymptomWheeze            = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_wheezing);
        mLinearSymptomEtc               = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_etc);
        mLinearSymptomNone              = (LinearLayout)mBottomSheetDialogSymptom.findViewById(R.id.lin_symptom_select_dialog_none);
        mButtonPopupSave                = (Button)mBottomSheetDialogSymptom.findViewById(R.id.btn_symptom_select_dialog_done);
        mSymptomBottomSheetDialog.setContentView(mBottomSheetDialogSymptom);

        linAllergyBtn.setOnClickListener(this);
        linEnvBtn.setOnClickListener(this);
        linBadBtn.setOnClickListener(this);

        btnBack.setOnClickListener(this);
        linLeftBtn.setOnClickListener(this);
        linRightBtn.setOnClickListener(this);
        linSelectTimezone.setOnClickListener(this);

        mButtonPopupSave.setOnClickListener(this);
        mLinearSymptomAll.setOnClickListener(this);
        mLinearSymptomCough.setOnClickListener(this);
        mLinearSymptomPhlegm.setOnClickListener(this);
        mLinearSymptomBreath.setOnClickListener(this);
        mLinearSymptomStuffy.setOnClickListener(this);
        mLinearSymptomWheeze.setOnClickListener(this);
        mLinearSymptomEtc.setOnClickListener(this);
        mLinearSymptomNone.setOnClickListener(this);


    }

    public void setUpBarMake(){
        mLinearSymptomBar.removeAllViews();
        LinearLayout lin1 = new LinearLayout(this);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params1.weight = pieValues[0].floatValue();
        lin1.setBackgroundColor(Color.parseColor("#364064"));
        lin1.setLayoutParams(params1);

        LinearLayout lin2 = new LinearLayout(this);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params2.weight = pieValues[1].floatValue();
        lin2.setBackgroundColor(Color.parseColor("#8da3c4"));
        lin2.setLayoutParams(params2);

        LinearLayout lin3 = new LinearLayout(this);
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3.weight = pieValues[2].floatValue();
        lin3.setBackgroundColor(Color.parseColor("#bec7d0"));
        lin3.setLayoutParams(params3);

        LinearLayout lin4 = new LinearLayout(this);
        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params4.weight = pieValues[3].floatValue();
        lin4.setBackgroundColor(Color.parseColor("#f07a55"));
        lin4.setLayoutParams(params4);

        LinearLayout lin5 = new LinearLayout(this);
        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params5.weight = pieValues[4].floatValue();
        lin5.setBackgroundColor(Color.parseColor("#f7c24f"));
        lin5.setLayoutParams(params5);

        LinearLayout lin6 = new LinearLayout(this);
        LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params6.weight = pieValues[5].floatValue();
        lin6.setBackgroundColor(Color.parseColor("#1388ac"));
        lin6.setLayoutParams(params6);

        LinearLayout lin7 = new LinearLayout(this);
        LinearLayout.LayoutParams params7 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params7.weight = pieValues[6].floatValue();
        lin7.setBackgroundColor(Color.parseColor("#8d8d8d"));
        lin7.setLayoutParams(params7);

        mLinearSymptomBar.addView(lin1);
        mLinearSymptomBar.addView(lin2);
        mLinearSymptomBar.addView(lin3);
        mLinearSymptomBar.addView(lin4);
        mLinearSymptomBar.addView(lin5);
        mLinearSymptomBar.addView(lin6);
        mLinearSymptomBar.addView(lin7);
    }

    void setupBarChart() {

        ArrayList barEndtry = new ArrayList();
        int[] colors = new int[]{getColor(R.color.static_symptom_cough), getColor(R.color.static_symptom_phlegm), getColor(R.color.static_symptom_breath), getColor(R.color.static_symptom_frustrated), getColor(R.color.static_symptom_wheeze), getColor(R.color.static_symptom_etc), getColor(R.color.static_symptom_none)};

        float[] dawns2 = new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
        float[] mornings2 = new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
        float[] afternoons2 = new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};
        float[] nights2 = new float[]{0.0f,0.0f,0.0f,0.0f,0.0f,0.0f,0.0f};

        if (mCategoryArray.get(0).equals("100")) {
            barEndtry.add(new BarEntry(0, dawns));
            barEndtry.add(new BarEntry(1, mornings));
            barEndtry.add(new BarEntry(2, afternoons));
            barEndtry.add(new BarEntry(3, nights));
        } else {
            for (int i = 0; i < mCategoryArray.size(); i++){
                dawns2[mList.indexOf(mCategoryArray.get(i))] = dawns[mList.indexOf(mCategoryArray.get(i))];
                mornings2[mList.indexOf(mCategoryArray.get(i))] = mornings[mList.indexOf(mCategoryArray.get(i))];
                afternoons2[mList.indexOf(mCategoryArray.get(i))] = afternoons[mList.indexOf(mCategoryArray.get(i))];
                nights2[mList.indexOf(mCategoryArray.get(i))] = nights[mList.indexOf(mCategoryArray.get(i))];
            }
            barEndtry.add(new BarEntry(0, dawns2));
            barEndtry.add(new BarEntry(1, mornings2));
            barEndtry.add(new BarEntry(2, afternoons2));
            barEndtry.add(new BarEntry(3, nights2));
        }

        BarDataSet dataSet = new BarDataSet(barEndtry, "");
        if (mCategoryArray.get(0).equals("100")) {
            dataSet.setColors(colors);
        } else {
            dataSet.setColors(colors);
        }
        BarData barData = new BarData(dataSet);

        Legend l = barChart.getLegend();
        l.setEnabled(false);
        barData.setBarWidth(0.5f);


        barChart.getXAxis().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        YAxis rightYAxis = barChart.getAxisRight();

        rightYAxis.setEnabled(false);
        barData.setDrawValues(false);
        barChart.setData(barData);


        String[] labels = {"새벽", "오전", "오후", "저녁"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
//        xAxis.setCenterAxisLabels(true);
        xAxis.setEnabled(true);
        Typeface tf = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);
        xAxis.setTypeface(tf);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.parseColor("#7e7e7e"));
        xAxis.setYOffset(5);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.setExtraBottomOffset(20);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    void setCausesTexts() {
        txtCause1.setText("3회");
        txtCause2.setText("3회");
        txtCause3.setText("3회");
        txtCause4.setText("1회");
        txtCause5.setText("2회");
        txtCause6.setText("5회");
        txtCause7.setText("0회");
        txtCause8.setText("0회");
        txtCause9.setText("0회");
        txtCause10.setText("0회");
        txtCause11.setText("0회");
        txtCause12.setText("0회");
        txtCause13.setText("10회");
        txtCause14.setText("3회");
        txtCause15.setText("2회");
        txtCause16.setText("11회");

        return;
    }

    void isNullHistoryData(int index, Double value) {

        stCause = "";

        coughItems = new ArrayList<>();
        wheezeItems = new ArrayList<>();
        breathItems = new ArrayList<>();
        stuffyItems = new ArrayList<>();
        etcItems    = new ArrayList<>();

        pieValues = new Double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        dawns = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        mornings = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        afternoons = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        nights = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        causes = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        txtSymptomCough.setText("0회");
        txtSymptomWheeze.setText("0회");
        txtSymptombreath.setText("0회");
        txtSymptomStuffy.setText("0회");
        txtSymptomEtc.setText("0회");

        setupBarChart();
        mLinearSymptomBar.setVisibility(View.GONE);
        mTextViewNoData.setVisibility(View.VISIBLE);
        setCausesTexts();
    }

    void SymptomLinearLayoutAdd(LinearLayout mLinearLayout, String category){
        if (mLinearSymptomArray != null){
            if (mLinearSymptomArray.contains(mLinearLayout)){
                mLinearSymptomArray.remove(mLinearLayout);
                mCategoryArray.remove(category);
                mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_off);
            }else{
                if (mLinearLayout == mLinearSymptomAll){
                    for (int i = 0; i < mLinearSymptomArray.size(); i++){
                        mLinearSymptomArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_off);
                    }
                    mCategoryArray = new ArrayList<>();
                    mCategoryArray.add("100");
                    mLinearSymptomArray = new ArrayList<>();
                    mLinearSymptomArray.add(mLinearSymptomAll);
                    mLinearSymptomAll.setBackgroundResource(R.drawable.symptom_cause_bg_on);
                }else{
                    if (mLinearSymptomArray.size() < 3){
                        if (mLinearSymptomArray.contains(mLinearSymptomAll)){
                            mCategoryArray.remove("100");
                            mLinearSymptomArray.remove(mLinearSymptomAll);
                            mLinearSymptomAll.setBackgroundResource(R.drawable.symptom_cause_bg_off);
                        }
                        mCategoryArray.add(category);
                        mLinearSymptomArray.add(mLinearLayout);
                        mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_on);
                    }
                }
            }
        }else{
            mLinearSymptomArray = new ArrayList<>();
            mLinearSymptomArray.add(mLinearLayout);
            mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_on);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_allergy_cause: {
                if (Utils.linkKeys != null) {
                    for (int i = 0; i < Utils.linkKeys.size(); i++) {
                        if (Utils.linkKeys.get(i).getTitle().equals("symptomreport2")) {
                            if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                                Intent intent = new Intent(this, UrlWebViewActivity.class);
                                intent.putExtra("url",Utils.linkKeys.get(i).getLinkUrl());
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case R.id.lin_env_cause: {
                if (Utils.linkKeys != null) {
                    for (int i = 0; i < Utils.linkKeys.size(); i++) {
                        if (Utils.linkKeys.get(i).getTitle().equals("symptomreport1")) {
                            if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                                Intent intent = new Intent(this, UrlWebViewActivity.class);
                                intent.putExtra("url",Utils.linkKeys.get(i).getLinkUrl());
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case R.id.lin_bad_cause: {
                if (Utils.linkKeys != null) {
                    for (int i = 0; i < Utils.linkKeys.size(); i++) {
                        if (Utils.linkKeys.get(i).getTitle().equals("symptomreport3")) {
                            if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                                Intent intent = new Intent(this, UrlWebViewActivity.class);
                                intent.putExtra("url",Utils.linkKeys.get(i).getLinkUrl());
                                startActivity(intent);
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case R.id.txt_statics_detail_back: {
                onBackPressed();
                break;
            }
            case R.id.lin_statics_symptom_left_btn: {
                break;
            }
            case R.id.lin_statics_symptom_right_btn: {

                break;
            }
            case R.id.lin_symptom_static_result_select_cause: {
                for (int i = 0; i < mLinearSymptomArray.size(); i++){
                    mLinearSymptomArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_off);
                }
                mCategoryArray = new ArrayList<>();
                mCategoryArray.addAll(mCategoryArray1);
                mLinearSymptomArray = new ArrayList<>();
                mLinearSymptomArray.addAll(mLinearSymptomArray1);

                for (int i = 0; i < mLinearSymptomArray.size(); i++){
                    mLinearSymptomArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_on);
                }
                mSymptomBottomSheetDialog.show();
                break;
            }
            case R.id.btn_symptom_select_dialog_done : {
                if (mCategoryArray.size() > 0){
                    mCategoryArray1 = new ArrayList<>();
                    mCategoryArray1.addAll(mCategoryArray);
                    mLinearSymptomArray1 = new ArrayList<>();
                    mLinearSymptomArray1.addAll(mLinearSymptomArray);
                    mSymptomBottomSheetDialog.dismiss();
                    setupBarChart();

                    String cause = "";
                    for (int i = 0; i < mCategoryArray1.size(); i++){
                        if (mCategoryArray1.get(i).equals("11")){
                            if (i == 0){
                                cause += "기침";
                            }else{
                                cause += ",기침";
                            }
                        }else if (mCategoryArray1.get(i).equals("12")){
                            if (i == 0){
                                cause += "숨쉬기 불편";
                            }else{
                                cause += ",숨쉬기 불편";
                            }
                        }else if (mCategoryArray1.get(i).equals("13")){
                            if (i == 0){
                                cause += "천명음";
                            }else{
                                cause += ",천명음";
                            }
                        }else if (mCategoryArray1.get(i).equals("14")){
                            if (i == 0){
                                cause += "가슴답답";
                            }else{
                                cause += ",가슴답답";
                            }
                        }else if (mCategoryArray1.get(i).equals("15")){
                            if (i == 0){
                                cause += "기타증상";
                            }else{
                                cause += ",기타증상";
                            }
                        }else if (mCategoryArray1.get(i).equals("16")){
                            if (i == 0){
                                cause += "가래";
                            }else{
                                cause += ",가래";
                            }
                        }else if (mCategoryArray1.get(i).equals("40")){
                            if (i == 0){
                                cause += "증상없음";
                            }else{
                                cause += ",증상없음";
                            }
                        }else if (mCategoryArray1.get(i).equals("100")){
                            cause = "전체";
                        }
                    }
                    btnSelectTimeZone.setText(cause);
                }
                break;
            }
            case R.id.lin_symptom_select_dialog_all : {
                SymptomLinearLayoutAdd(mLinearSymptomAll,"100");
                break;
            }
            case R.id.lin_symptom_select_dialog_cough : {
                SymptomLinearLayoutAdd(mLinearSymptomCough,"11");
                break;
            }
            case R.id.lin_symptom_select_dialog_phlegm : {
                SymptomLinearLayoutAdd(mLinearSymptomPhlegm,"16");
                break;
            }
            case R.id.lin_symptom_select_dialog_breath : {
                SymptomLinearLayoutAdd(mLinearSymptomBreath,"14");
                break;
            }
            case R.id.lin_symptom_select_dialog_difficulty : {
                SymptomLinearLayoutAdd(mLinearSymptomStuffy,"12");
                break;
            }
            case R.id.lin_symptom_select_dialog_wheezing : {
                SymptomLinearLayoutAdd(mLinearSymptomWheeze,"13");
                break;
            }
            case R.id.lin_symptom_select_dialog_etc : {
                SymptomLinearLayoutAdd(mLinearSymptomEtc,"15");
                break;
            }
            case R.id.lin_symptom_select_dialog_none : {
                SymptomLinearLayoutAdd(mLinearSymptomNone,"40");
                break;
            }
        }
    }
}