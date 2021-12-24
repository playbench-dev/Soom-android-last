package com.kmw.soom2.Reports.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.kmw.soom2.R;
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

import static com.kmw.soom2.Common.Utils.Collapse;
import static com.kmw.soom2.Common.Utils.Expand;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.formatYYYYMM;

public class NewStaticSymptomActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_static_symptom);

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

        progressDialog = new ProgressDialog(NewStaticSymptomActivity.this,R.style.MyTheme);
//        new SelectHistoryNetWork().execute();
        new SelectSymptomHistoryNetWork().execute();
    }

    private void findViewByIds() {

        txtCalendarTitle = findViewById(R.id.txt_statics_symptom_title);
        btnBack = findViewById(R.id.txt_statics_detail_back);

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

//    private String[] causes = new String[]{"감기", "미세먼지", "찬바람", "운동", "집먼지진드기", "애완동물", "음식알러지", "꽃가루", "스트레스", "담배연기", "요리연기", "강한냄새", "건조함", "곰팡이", "높은습도", "모르겠어요"};
//    private int[] causesIdx = new int[]{    0,      1,        2,      3,        4,         5,        6,        7,       8,        9,        10,      12,      13,     14,      15,        11};
//                                            0       1         2       3         4          5         6         7        8         9         10       11       12      13       14         15
    void setCausesTexts() {
        causes = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        if (stCause.length() == 0) {
            txtCause1.setText("" + causes[0] + "회");
            txtCause2.setText("" + causes[1] + "회");
            txtCause3.setText("" + causes[2] + "회");
            txtCause4.setText("" + causes[3] + "회");
            txtCause5.setText("" + causes[13] + "회");
            txtCause6.setText("" + causes[4] + "회");
            txtCause7.setText("" + causes[5] + "회");
            txtCause8.setText("" + causes[6] + "회");
            txtCause9.setText("" + causes[7] + "회");
            txtCause10.setText("" + causes[14] + "회");
            txtCause11.setText("" + causes[8] + "회");
            txtCause12.setText("" + causes[9] + "회");
            txtCause13.setText("" + causes[10] + "회");
            txtCause14.setText("" + causes[12] + "회");
            txtCause15.setText("" + causes[15] + "회");
            txtCause16.setText("" + causes[11] + "회");

            return;
        }

        for (int i = 0; i < stCause.split(",").length; i++) {
            if (stCause.startsWith(",")) {
                stCause = stCause.substring(1, stCause.length());
            } else if (stCause.endsWith(",")) {
                stCause = stCause.substring(0, stCause.length() - 1);
            }
            if (stCause.contains(",")) {
                causes[Integer.parseInt(stCause.split(",")[i])] += 1;
            } else {
                if (!stCause.equals("")) {
                    causes[Integer.parseInt(stCause)] += 1;
                }
            }
        }
        txtCause1.setText("" + causes[0] + "회");
        txtCause2.setText("" + causes[1] + "회");
        txtCause3.setText("" + causes[2] + "회");
        txtCause4.setText("" + causes[3] + "회");
        txtCause5.setText("" + causes[13] + "회");
        txtCause6.setText("" + causes[4] + "회");
        txtCause7.setText("" + causes[5] + "회");
        txtCause8.setText("" + causes[6] + "회");
        txtCause9.setText("" + causes[7] + "회");
        txtCause10.setText("" + causes[14] + "회");
        txtCause11.setText("" + causes[8] + "회");
        txtCause12.setText("" + causes[9] + "회");
        txtCause13.setText("" + causes[10] + "회");
        txtCause14.setText("" + causes[12] + "회");
        txtCause15.setText("" + causes[15] + "회");
        txtCause16.setText("" + causes[11] + "회");

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
                    }else{
                        Toast.makeText(this, "3개까지 선택 가능해요.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }else{
            mLinearSymptomArray = new ArrayList<>();
            mLinearSymptomArray.add(mLinearLayout);
            mLinearLayout.setBackgroundResource(R.drawable.symptom_cause_bg_on);

        }
    }

    public class SelectSymptomHistoryNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewStaticSymptomActivity.this,R.style.MyTheme);
            if (!NewStaticSymptomActivity.this.isFinishing()){
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            String month = "";
            String year = "";

            if (currentCalendar.get(Calendar.MONTH) < 9) {
                month = "0" + (currentCalendar.get(Calendar.MONTH) + 1);
            } else {
                month = "" + (currentCalendar.get(Calendar.MONTH) + 1);
            }

            year = "" + currentCalendar.get(Calendar.YEAR);
            RequestBody body = new FormBody.Builder().add("MONTH", month).add("YEAR", year).add("USER_NO", "" + Utils.userItem.getUserNo()).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedList(), body);
                Log.i(TAG,"response : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
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

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject,"result").equals("N")){
                    isNullHistoryData(0, 0.0);
                    new OneButtonDialog(NewStaticSymptomActivity.this, "보고서", "기록이 없어\n보고서를 만들지 못했습니다.", "확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {

                        }
                    });
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            HistoryItems historyItem = new HistoryItems();

                            if (JsonIntIsNullCheck(object, "CATEGORY") == 11) {
                                historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                                historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                historyItem.setKo(JsonIsNullCheck(object, "KO"));
                                historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                                coughItems.add(historyItem);

                                int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                                if (hour >= 0 && hour < 6) {
                                    dawns[0] = dawns[0] + 1;
                                } else if (hour >= 6 && hour < 12) {
                                    mornings[0] = mornings[0] + 1;
                                } else if (hour >= 12 && hour < 18) {
                                    afternoons[0] = afternoons[0] + 1;
                                } else {
                                    nights[0] = nights[0] + 1;
                                }
                                if (registerDtArray.contains(JsonIsNullCheck(object, "REGISTER_DT"))){

                                }else {
                                    registerDtArray.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    if (historyItem.getCause().length() > 0) {
                                        if (!stCause.startsWith(",")) {
                                            if (stCause.length() > 0) {
                                                stCause += "," + historyItem.getCause();
                                            } else {
                                                stCause += historyItem.getCause();
                                            }
                                        } else {
                                            stCause += "," + historyItem.getCause();
                                        }
                                    }
                                }

                            } else if (JsonIntIsNullCheck(object, "CATEGORY") == 12) {
                                historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                                historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                historyItem.setKo(JsonIsNullCheck(object, "KO"));
                                historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                                stuffyItems.add(historyItem);

                                int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                                if (hour >= 0 && hour < 6) {
                                    dawns[3] = dawns[3] + 1;
                                } else if (hour >= 6 && hour < 12) {
                                    mornings[3] = mornings[3] + 1;
                                } else if (hour >= 12 && hour < 18) {
                                    afternoons[3] = afternoons[3] + 1;
                                } else {
                                    nights[3] = nights[3] + 1;
                                }
                                if (registerDtArray.contains(JsonIsNullCheck(object, "REGISTER_DT"))){

                                }else {
                                    registerDtArray.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    if (historyItem.getCause().length() > 0) {
                                        if (!stCause.startsWith(",")) {
                                            if (stCause.length() > 0) {
                                                stCause += "," + historyItem.getCause();
                                            } else {
                                                stCause += historyItem.getCause();
                                            }
                                        } else {
                                            stCause += "," + historyItem.getCause();
                                        }
                                    }
                                }
                            } else if (JsonIntIsNullCheck(object, "CATEGORY") == 13) {
                                historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                                historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                historyItem.setKo(JsonIsNullCheck(object, "KO"));
                                historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                                wheezeItems.add(historyItem);

                                int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                                if (hour >= 0 && hour < 6) {
                                    dawns[4] = dawns[4] + 1;
                                } else if (hour >= 6 && hour < 12) {
                                    mornings[4] = mornings[4] + 1;
                                } else if (hour >= 12 && hour < 18) {
                                    afternoons[4] = afternoons[4] + 1;
                                } else {
                                    nights[4] = nights[4] + 1;
                                }
                                if (registerDtArray.contains(JsonIsNullCheck(object, "REGISTER_DT"))){

                                }else {
                                    registerDtArray.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    if (historyItem.getCause().length() > 0) {
                                        if (!stCause.startsWith(",")) {
                                            if (stCause.length() > 0) {
                                                stCause += "," + historyItem.getCause();
                                            } else {
                                                stCause += historyItem.getCause();
                                            }
                                        } else {
                                            stCause += "," + historyItem.getCause();
                                        }
                                    }
                                }
                            } else if (JsonIntIsNullCheck(object, "CATEGORY") == 14) {
                                historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                                historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                historyItem.setKo(JsonIsNullCheck(object, "KO"));
                                historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                                breathItems.add(historyItem);

                                int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                                if (hour >= 0 && hour < 6) {
                                    dawns[2] = dawns[2] + 1;
                                } else if (hour >= 6 && hour < 12) {
                                    mornings[2] = mornings[2] + 1;
                                } else if (hour >= 12 && hour < 18) {
                                    afternoons[2] = afternoons[2] + 1;
                                } else {
                                    nights[2] = nights[2] + 1;
                                }
                                if (registerDtArray.contains(JsonIsNullCheck(object, "REGISTER_DT"))){

                                }else {
                                    registerDtArray.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    if (historyItem.getCause().length() > 0) {
                                        if (!stCause.startsWith(",")) {
                                            if (stCause.length() > 0) {
                                                stCause += "," + historyItem.getCause();
                                            } else {
                                                stCause += historyItem.getCause();
                                            }
                                        } else {
                                            stCause += "," + historyItem.getCause();
                                        }
                                    }
                                }
                            }else if (JsonIntIsNullCheck(object, "CATEGORY") == 15) {
                                historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                                historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                historyItem.setKo(JsonIsNullCheck(object, "KO"));
                                historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                                etcItems.add(historyItem);

                                int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                                if (hour >= 0 && hour < 6) {
                                    dawns[5] = dawns[5] + 1;
                                } else if (hour >= 6 && hour < 12) {
                                    mornings[5] = mornings[5] + 1;
                                } else if (hour >= 12 && hour < 18) {
                                    afternoons[5] = afternoons[5] + 1;
                                } else {
                                    nights[5] = nights[5] + 1;
                                }
                                if (registerDtArray.contains(JsonIsNullCheck(object, "REGISTER_DT"))){

                                }else {
                                    registerDtArray.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    if (historyItem.getCause().length() > 0) {
                                        if (!stCause.startsWith(",")) {
                                            if (stCause.length() > 0) {
                                                stCause += "," + historyItem.getCause();
                                            } else {
                                                stCause += historyItem.getCause();
                                            }
                                        } else {
                                            stCause += "," + historyItem.getCause();
                                        }
                                    }
                                }
                            }else if (JsonIntIsNullCheck(object, "CATEGORY") == 16) {
                                historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                                historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                historyItem.setKo(JsonIsNullCheck(object, "KO"));
                                historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                                phlegmItems.add(historyItem);

                                int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                                if (hour >= 0 && hour < 6) {
                                    dawns[1] = dawns[1] + 1;
                                } else if (hour >= 6 && hour < 12) {
                                    mornings[1] = mornings[1] + 1;
                                } else if (hour >= 12 && hour < 18) {
                                    afternoons[1] = afternoons[1] + 1;
                                } else {
                                    nights[1] = nights[1] + 1;
                                }
                                if (registerDtArray.contains(JsonIsNullCheck(object, "REGISTER_DT"))){

                                }else {
                                    registerDtArray.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    if (historyItem.getCause().length() > 0) {
                                        if (!stCause.startsWith(",")) {
                                            if (stCause.length() > 0) {
                                                stCause += "," + historyItem.getCause();
                                            } else {
                                                stCause += historyItem.getCause();
                                            }
                                        } else {
                                            stCause += "," + historyItem.getCause();
                                        }
                                    }
                                }
                            }else if (JsonIntIsNullCheck(object, "CATEGORY") == 40) {
                                historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                                historyItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                historyItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                historyItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                historyItem.setKo(JsonIsNullCheck(object, "KO"));
                                historyItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                historyItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                historyItem.setCause(JsonIsNullCheck(object, "CAUSE"));

                                noneItems.add(historyItem);

                                int hour = Integer.parseInt(historyItem.getRegisterDt().substring(11, 13));

                                if (hour >= 0 && hour < 6) {
                                    dawns[6] = dawns[6] + 1;
                                } else if (hour >= 6 && hour < 12) {
                                    mornings[6] = mornings[6] + 1;
                                } else if (hour >= 12 && hour < 18) {
                                    afternoons[6] = afternoons[6] + 1;
                                } else {
                                    nights[6] = nights[6] + 1;
                                }
                                if (registerDtArray.contains(JsonIsNullCheck(object, "REGISTER_DT"))){

                                }else {
                                    registerDtArray.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    if (historyItem.getCause().length() > 0) {
                                        if (!stCause.startsWith(",")) {
                                            if (stCause.length() > 0) {
                                                stCause += "," + historyItem.getCause();
                                            } else {
                                                stCause += historyItem.getCause();
                                            }
                                        } else {
                                            stCause += "," + historyItem.getCause();
                                        }
                                    }
                                }
                            }
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
            } catch (JSONException e) {
//                isNullMedicineListData();

                //memo
                isNullHistoryData(0, 0.0);
            }
            progressDialog.dismiss();
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
                for (int i = 0; i < mLinearSymptomArray.size(); i++){
                    mLinearSymptomArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_off);
                }
                mLinearSymptomAll.setBackgroundResource(R.drawable.symptom_cause_bg_on);
                mCategoryArray = new ArrayList<>();
                mLinearSymptomArray = new ArrayList<>();
                mLinearSymptomArray.add(mLinearSymptomAll);
                mCategoryArray.add("100");
                mCategoryArray1 = new ArrayList<>();
                mLinearSymptomArray1 = new ArrayList<>();
                mLinearSymptomArray1.add(mLinearSymptomAll);
                mCategoryArray1.add("100");
                currentCalendar.add(Calendar.MONTH, -1);
                txtCalendarTitle.setText(dataFormatYYYYMM.format(currentCalendar.getTime()));

                new SelectSymptomHistoryNetWork().execute();
                linRightBtn.setEnabled(true);
                txtNextBtn.setTextColor(Color.parseColor("#000000"));

                break;
            }
            case R.id.lin_statics_symptom_right_btn: {
                if (currentCalendar.get(Calendar.MONTH) + 1 == currentDate.getMonth()&&currentCalendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                    txtNextBtn.setTextColor(Color.parseColor("#acacac"));
                }

                if (currentCalendar.get(Calendar.MONTH) == currentDate.getMonth()&&currentCalendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                    linRightBtn.setEnabled(false);
                } else {
                    for (int i = 0; i < mLinearSymptomArray.size(); i++){
                        mLinearSymptomArray.get(i).setBackgroundResource(R.drawable.symptom_cause_bg_off);
                    }
                    mLinearSymptomAll.setBackgroundResource(R.drawable.symptom_cause_bg_on);

                    mCategoryArray = new ArrayList<>();
                    mLinearSymptomArray = new ArrayList<>();
                    mLinearSymptomArray.add(mLinearSymptomAll);
                    mCategoryArray.add("100");
                    mCategoryArray1 = new ArrayList<>();
                    mLinearSymptomArray1 = new ArrayList<>();
                    mLinearSymptomArray1.add(mLinearSymptomAll);
                    mCategoryArray1.add("100");

                    currentCalendar.add(Calendar.MONTH, 1);
                    txtCalendarTitle.setText(dataFormatYYYYMM.format(currentCalendar.getTime()));
                    new SelectSymptomHistoryNetWork().execute();
                }
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

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}