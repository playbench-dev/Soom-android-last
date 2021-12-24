package com.kmw.soom2.Home.Activitys.SymptomActivitys;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.Item.ForeignKeys;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeAdapter.MedicineSelectAdapter;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HOME_FEED_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.PEF_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.PEF_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST_CHECK;
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

public class BreathRecordActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String TAG = "BreathRecordActivity";
    TextView txtBack;
    TextView txtDate,txtTime;
    EditText edtValue;
    ImageView imgEditClose;
    RadioButton rdoUnUsed,rdoUsed;
    LinearLayout linQuestion;
    Button btnFinish;
    ImageView imgRemove;

    Intent beforeIntent;
    private String mRegisterDt = "";
    ProgressDialog progressDialog;
    String mYear = "";
    String mMonth = "";
    boolean dateCheck = true;
    SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
    SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");
    private String mSaveBeforeRegisterDt = "";
    private String mSaveAfterRegisterDt = "";
    private String mSaveBeforeScore = "";
    private String mSaveAfterScore = "";
    private int mSaveBeforeFlag = 0;
    private int mSaveAfterFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breath_record);

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

        imgRemove.setVisibility(View.GONE);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")){
                try {
                    txtDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0,10))));
                    txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11,18))));
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edtValue.setText(""+beforeIntent.getIntExtra("pefScore",0));

                if (beforeIntent.getIntExtra("inspiratorFlag",0) == 1){
                    rdoUsed.setChecked(true);
                }else{
                    rdoUnUsed.setChecked(true);
                }

                mSaveBeforeScore = ""+beforeIntent.getIntExtra("pefScore",0);
                mSaveAfterScore = ""+beforeIntent.getIntExtra("pefScore",0);
                mSaveBeforeFlag = beforeIntent.getIntExtra("inspiratorFlag",0);
                mSaveAfterFlag = beforeIntent.getIntExtra("inspiratorFlag",0);

                imgRemove.setVisibility(View.VISIBLE);
            }else if (beforeIntent.hasExtra("date")){
                txtDate.setText(beforeIntent.getStringExtra("date"));
                txtTime.setText(beforeIntent.getStringExtra("time"));
                try {
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else{
                txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
                txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
                try {
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
            try {
                mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mSaveBeforeRegisterDt = mRegisterDt;
        mSaveAfterRegisterDt = mRegisterDt;
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_breath_record_back);
        txtDate = (TextView)findViewById(R.id.txt_breath_record_date);
        txtTime = (TextView)findViewById(R.id.txt_breath_record_time);
        edtValue = (EditText)findViewById(R.id.edt_breath_record_value);
        rdoUsed = (RadioButton)findViewById(R.id.rdo_breath_record_used);
        rdoUnUsed = (RadioButton)findViewById(R.id.rdo_breath_record_unused);
        imgRemove = (ImageView)findViewById(R.id.img_breath_record_remove);
        imgEditClose = (ImageView)findViewById(R.id.img_breath_record_edit_close);
        linQuestion = (LinearLayout)findViewById(R.id.lin_breath_record_question);
        btnFinish = (Button)findViewById(R.id.btn_breath_record_finish);

        txtBack.setOnClickListener(this);
        imgEditClose.setOnClickListener(this);
        linQuestion.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        edtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    imgEditClose.setVisibility(View.VISIBLE);
                }else{
                    imgEditClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rdoUsed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rdoUsed.setBackgroundTintList(null);
                    rdoUnUsed.setBackgroundTintList(getColorStateList(R.color.f5f5f5));
                    rdoUsed.setTextColor(getResources().getColor(R.color.color09D182));
                    rdoUnUsed.setTextColor(getResources().getColor(R.color.color5c5c5c));
                    mSaveAfterFlag = 1;
                }else{
                    rdoUnUsed.setBackgroundTintList(null);
                    rdoUsed.setBackgroundTintList(getColorStateList(R.color.f5f5f5));
                    rdoUsed.setTextColor(getResources().getColor(R.color.color5c5c5c));
                    rdoUnUsed.setTextColor(getResources().getColor(R.color.color09D182));
                    mSaveAfterFlag = 0;
                }
            }
        });
    }

    void breathDetailPopup(){
        final Dialog dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_breath_detail,null);

        ImageView imgClose = (ImageView)layout.findViewById(R.id.img_breath_detail_popup_close);
        Button btnMore = (Button)layout.findViewById(R.id.btn_breath_detail_popup_more);

        dialog.setContentView(layout);
        dialog.show();
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.linkKeys != null) {
                    ForeignKeys key = Utils.linkKeys.stream().filter(new Predicate<ForeignKeys>() {
                        @Override
                        public boolean test(ForeignKeys foreignKeys) {
                            if (foreignKeys.getTitle().equals("wrtingPEFmore")) {
                                return true;
                            }
                            return false;
                        }
                    }).collect(Collectors.toList()).get(0);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(key.getLinkUrl()));
                    startActivity(browserIntent);
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void TwoBtnPopup(int no, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(this);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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

        DisplayMetrics dm = getResources().getDisplayMetrics();
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
                imgRemove.setClickable(false);
                NetworkCall(HOME_FEED_DELETE);
            }
        });
    }


    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(PEF_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),mRegisterDt,edtValue.getText().toString(),
                    rdoUsed.isChecked() == true ? "1" : "2",Utils.userItem.getNickname(),""+Utils.userItem.getGender(),""+Utils.userItem.getBirth());
        }else if (mCode.equals(PEF_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+beforeIntent.getStringExtra("medicineHistoryNo"),mRegisterDt,edtValue.getText().toString(),
                    rdoUsed.isChecked() == true ? "1" : "2");
        }else if (mCode.equals(HOME_FEED_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"));
        }else if (mCode.equals(SELECT_HOME_FEED_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),mYear,mMonth);
        }else if (mCode.equals(SELECT_HOME_FEED_LIST_CHECK)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),mYear,mMonth);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(PEF_INSERT)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        btnFinish.setClickable(true);
                        finish();
                    }
                },500);
            }else if (mCode.equals(PEF_UPDATE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        btnFinish.setClickable(true);
                        finish();
                    }
                },500);
            }else if (mCode.equals(HOME_FEED_DELETE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        imgRemove.setClickable(true);
                        finish();
                    }
                },500);
            }else if (mCode.equals(SELECT_HOME_FEED_LIST)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                            if (JsonIsNullCheck(object,"CATEGORY").equals("1")){
                                if (JsonIsNullCheck(object,"REGISTER_DT").equals(mRegisterDt)){
                                    dateCheck = false;
                                    OneButtonDialog oneButtonDialog = new OneButtonDialog(BreathRecordActivity.this,"폐기능기록 중복","선택한 시간에 이미\n폐기능 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
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
                if (beforeIntent.hasExtra("registerDt")){

                }else{

                }
            }
        }
    }

    TwoButtonDialog twoButtonDialog;

    @Override
    public void onBackPressed() {

        if (mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt) && mSaveBeforeScore.equals(edtValue.getText().toString()) && mSaveBeforeFlag == mSaveAfterFlag){
            finish();
        }else{
            twoButtonDialog = new TwoButtonDialog(BreathRecordActivity.this,"페기능", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_breath_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_breath_record_edit_close : {
                edtValue.setText("");
                break;
            }
            case R.id.img_breath_record_remove : {
                twoButtonDialog = new TwoButtonDialog(this,"폐기능 기록","기록을 삭제하시겠습니까?","취소","확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        imgRemove.setClickable(false);
                        NetworkCall(HOME_FEED_DELETE);
                    }
                });
                break;
            }
            case R.id.lin_breath_record_question : {
                breathDetailPopup();
                break;
            }
            case R.id.btn_breath_record_finish : {
                Log.i(TAG,"check : " + rdoUsed.isChecked());
                if (edtValue.getText().length() > 0){
                    if (!edtValue.getText().toString().equals("0")){
                        btnFinish.setClickable(false);
                        if (beforeIntent != null) {
                            if (beforeIntent.hasExtra("registerDt")) {
                                try {
                                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                                            + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                                } catch (ParseException e) {
                                    mRegisterDt = beforeIntent.getStringExtra("registerDt");
                                    e.printStackTrace();
                                }
                                NetworkCall(PEF_UPDATE);
                            } else {
                                try {
                                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))
                                            + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                NetworkCall(PEF_INSERT);
                            }
                        }else{
                            NetworkCall(PEF_INSERT);
                        }
                    }else{
                        Toast.makeText(this, "측정값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "측정값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.txt_breath_record_date: {
                DateTimePicker(1, "날짜를 선택하세요.");
                break;
            }
            case R.id.txt_breath_record_time: {
                DateTimePicker("시간을 선택하세요.");
                break;
            }
        }
    }
    
    void DateTimePicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateDialog = new Dialog(BreathRecordActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateDialog.setContentView(dateTimeView);
        dateDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false,"");
        singleDateAndTimePicker.setMustBeOnFuture(false);
        Date date = null;

        try {
            date = formatYYYYMMDD2.parse(txtDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                txtDate.setText(formatYYYYMMDD2.format(date));
                dateCheck = true;
                try {
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                    mSaveAfterRegisterDt = mRegisterDt;
                } catch (ParseException e) {
                    e.printStackTrace();
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
    void DateTimePicker(String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(BreathRecordActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayMonths(false);
        singleDateAndTimePicker.setDisplayDaysOfMonth(false);
        singleDateAndTimePicker.setDisplayHours(true);
        singleDateAndTimePicker.setDisplayMinutes(true,txtDate.getText().toString());

        Date date = null;

        try {
            date = Utils.formatHHMM.parse(txtTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                txtTime.setText(Utils.formatHHMM.format(date));
                dateCheck = true;
                try {
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                    mSaveAfterRegisterDt = mRegisterDt;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                NetworkCall(SELECT_HOME_FEED_LIST);
                dateTimeDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateTimeDialog.dismiss();
            }
        });
    }
}
