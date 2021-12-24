package com.kmw.soom2.DrugControl.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.kmw.soom2.AlarmReceiver;
import com.kmw.soom2.Common.DateTimePicker.DrugAlarmDatePicker;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.DrugControl.Adapter.DrugAlarmRecyclerViewAdapter;
import com.kmw.soom2.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;
import static com.kmw.soom2.Common.Utils.formatHHMM;

public class DrugAlarmInsertActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "DrugAlarmInsertActivity";
    TextView txtBack,txtTitle;
    DrugAlarmDatePicker singleDateAndTimePicker;
    TextView txtSun,txtMon,txtTue,txtWed,txtThur,txtFri,txtSat;
    Button btnFinish;
    ImageView imgRemove;
    EditText edtAlarmName;

    boolean sun = false;
    boolean mon = false;
    boolean tue = false;
    boolean wed = false;
    boolean thur = false;
    boolean fri = false;
    boolean sat = false;
    Date currentDate = null;
    Calendar calendar = Calendar.getInstance();

    long selectTime = 0;

    Intent beforeIntent;

    ArrayList<String> selectDayList = new ArrayList<>();

    DbOpenHelper mDbOpenHelper;
    Cursor cursor;
    SimpleDateFormat formatHH = new SimpleDateFormat("HH");

    SimpleDateFormat formatMM = new SimpleDateFormat("mm");
    Typeface typefaceBold,typefaceMedium;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_alarm_insert);
        beforeIntent = getIntent();
        FindViewById();

        NullCheck(this);

        typefaceBold = ResourcesCompat.getFont(this, R.font.notosanscjkkr_bold);
        typefaceMedium = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);

        mDbOpenHelper = new DbOpenHelper(this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mDbOpenHelper.create();
        if (beforeIntent.hasExtra("alarmId")){

            imgRemove.setVisibility(View.VISIBLE);

            if (beforeIntent.hasExtra("drugName")){
                if (beforeIntent.getStringExtra("drugName").length() > 0){
                    edtAlarmName.setText(beforeIntent.getStringExtra("drugName"));
                }
            }
            currentDate = new Date(beforeIntent.getLongExtra("alarmTime",0));
            String[] selectDays = beforeIntent.getStringExtra("alarmDay").split(",");

            for (int i = 0; i < selectDays.length; i++){
                if (selectDays[i].equals("1")){
                    txtSun.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtSun.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtSun.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtSun.setTypeface(typefaceBold);
                    selectDayList.add("1");
                    sun = true;
                }else if (selectDays[i].equals("2")){
                    txtMon.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtMon.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtMon.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtMon.setTypeface(typefaceBold);
                    selectDayList.add("2");
                    mon = true;
                }else if (selectDays[i].equals("3")){
                    txtTue.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtTue.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtTue.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtTue.setTypeface(typefaceBold);
                    selectDayList.add("3");
                    tue = true;
                }else if (selectDays[i].equals("4")){
                    txtWed.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtWed.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtWed.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtWed.setTypeface(typefaceBold);
                    selectDayList.add("4");
                    wed = true;
                }else if (selectDays[i].equals("5")){
                    txtThur.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtThur.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtThur.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtThur.setTypeface(typefaceBold);
                    selectDayList.add("5");
                    thur = true;
                }else if (selectDays[i].equals("6")){
                    txtFri.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtFri.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtFri.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtFri.setTypeface(typefaceBold);
                    selectDayList.add("6");
                    fri = true;
                }else if (selectDays[i].equals("7")){
                    txtSat.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtSat.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtSat.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtSat.setTypeface(typefaceBold);
                    selectDayList.add("7");
                    sat = true;
                }
            }

        }else{
            currentDate = new Date(System.currentTimeMillis());
        }

        selectTime = currentDate.getTime();
        singleDateAndTimePicker.setDefaultDate(currentDate);
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_drug_alarm_insert_back);
        txtTitle = (TextView)findViewById(R.id.txt_drug_alarm_insert_title);
        singleDateAndTimePicker = (DrugAlarmDatePicker)findViewById(R.id.date_picker_drug_alarm_insert);
        txtSun = (TextView)findViewById(R.id.txt_drug_alarm_insert_sun);
        txtMon = (TextView)findViewById(R.id.txt_drug_alarm_insert_mon);
        txtTue = (TextView)findViewById(R.id.txt_drug_alarm_insert_tue);
        txtWed = (TextView)findViewById(R.id.txt_drug_alarm_insert_wed);
        txtThur = (TextView)findViewById(R.id.txt_drug_alarm_insert_thur);
        txtFri = (TextView)findViewById(R.id.txt_drug_alarm_insert_fri);
        txtSat = (TextView)findViewById(R.id.txt_drug_alarm_insert_sat);
        btnFinish = (Button)findViewById(R.id.btn_drug_alarm_insert_finish);
        imgRemove = (ImageView)findViewById(R.id.img_drug_alarm_remove);
        edtAlarmName = (EditText)findViewById(R.id.edt_drug_alarm_insert_name);

        singleDateAndTimePicker.setBtnVisible(false);

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayMonths(false);
        singleDateAndTimePicker.setDisplayDays(false);

        singleDateAndTimePicker.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                selectTime = date.getTime();
                currentDate = date;
            }
        });

        txtBack.setOnClickListener(this);
        txtSun.setOnClickListener(this);
        txtMon.setOnClickListener(this);
        txtTue.setOnClickListener(this);
        txtWed.setOnClickListener(this);
        txtThur.setOnClickListener(this);
        txtFri.setOnClickListener(this);
        txtSat.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
    }

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_drug_alarm_insert_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_drug_alarm_insert_sun : {
                if (sun){
                    txtSun.setBackgroundResource(0);
                    txtSun.setTextColor(getResources().getColor(R.color.ff6767));
                    txtSun.setTypeface(typefaceMedium);
                    sun = false;
                    selectDayList.remove("1");
                }else{
                    txtSun.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtSun.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtSun.setTypeface(typefaceBold);
                    sun = true;
                    selectDayList.add("1");
                }
                break;
            }
            case R.id.txt_drug_alarm_insert_mon : {
                if (mon){
                    txtMon.setBackgroundResource(0);
                    txtMon.setTextColor(getResources().getColor(R.color.black));
                    txtMon.setTypeface(typefaceMedium);
                    mon = false;
                    selectDayList.remove("2");
                }else{
                    txtMon.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtMon.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtMon.setTypeface(typefaceBold);
                    mon = true;
                    selectDayList.add("2");
                }
                break;
            }
            case R.id.txt_drug_alarm_insert_tue : {
                if (tue){
                    txtTue.setBackgroundResource(0);
                    txtTue.setTextColor(getResources().getColor(R.color.black));
                    txtTue.setTypeface(typefaceMedium);
                    tue = false;
                    selectDayList.remove("3");
                }else{
                    txtTue.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtTue.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtTue.setTypeface(typefaceBold);
                    tue = true;
                    selectDayList.add("3");
                }
                break;
            }
            case R.id.txt_drug_alarm_insert_wed : {
                if (wed){
                    txtWed.setBackgroundResource(0);
                    txtWed.setTextColor(getResources().getColor(R.color.black));
                    txtWed.setTypeface(typefaceMedium);
                    wed = false;
                    selectDayList.remove("4");
                }else{
                    txtWed.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtWed.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtWed.setTypeface(typefaceBold);
                    wed = true;
                    selectDayList.add("4");
                }
                break;
            }
            case R.id.txt_drug_alarm_insert_thur : {
                if (thur){
                    txtThur.setBackgroundResource(0);
                    txtThur.setTextColor(getResources().getColor(R.color.black));
                    txtThur.setTypeface(typefaceMedium);
                    thur = false;
                    selectDayList.remove("5");
                }else{
                    txtThur.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtThur.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtThur.setTypeface(typefaceBold);
                    thur = true;
                    selectDayList.add("5");
                }
                break;
            }
            case R.id.txt_drug_alarm_insert_fri : {
                if (fri){
                    txtFri.setBackgroundResource(0);
                    txtFri.setTextColor(getResources().getColor(R.color.black));
                    txtFri.setTypeface(typefaceMedium);
                    fri = false;
                    selectDayList.remove("6");
                }else{
                    txtFri.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtFri.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtFri.setTypeface(typefaceBold);
                    fri = true;
                    selectDayList.add("6");
                }
                break;
            }
            case R.id.txt_drug_alarm_insert_sat : {
                if (sat){
                    txtSat.setBackgroundResource(0);
                    txtSat.setTextColor(getResources().getColor(R.color.ff6767));
                    txtSat.setTypeface(typefaceMedium);
                    sat = false;
                    selectDayList.remove("7");
                }else{
                    txtSat.setBackgroundResource(R.drawable.stroke_radius_50dp);
                    txtSat.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txtSat.setTypeface(typefaceBold);
                    sat = true;
                    selectDayList.add("7");
                }
                break;
            }
            case R.id.btn_drug_alarm_insert_finish : {
                if (edtAlarmName.getText().toString().length() == 0 || edtAlarmName.getText().toString().trim().length() != 0){
                    DrugAlarmRecyclerViewAdapter adapter = new DrugAlarmRecyclerViewAdapter(this,mDbOpenHelper);
                    if (selectDayList.size() > 0){
                        ProgressDialog progressDialog = new ProgressDialog(this,R.style.MyTheme);
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        Intent intent = getIntent();
                        Calendar pickCalendar = Calendar.getInstance();
                        pickCalendar.setTimeInMillis(singleDateAndTimePicker.getDate().getTime());
                        pickCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(formatHH.format(singleDateAndTimePicker.getDate().getTime())));
                        pickCalendar.set(Calendar.MINUTE, Integer.parseInt(formatMM.format(singleDateAndTimePicker.getDate().getTime())));
                        pickCalendar.set(Calendar.SECOND, 0);
                        if (beforeIntent.hasExtra("alarmId")){
                            mDbOpenHelper.updateColumn(edtAlarmName.getText().toString(),"","","",beforeIntent.getIntExtra("alarmId",0),"drugAlarm",selectDayList.toString().replace("[","").replace("]","").replace(" ",""),pickCalendar.getTimeInMillis(),1);
                        }else{
                            mDbOpenHelper.insertColumn(edtAlarmName.getText().toString(),"","","","drugAlarm",selectDayList.toString().replace("[","").replace("]","").replace(" ",""),pickCalendar.getTimeInMillis(),1);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                setResult(RESULT_OK);
                                onBackPressed();
                            }
                        },500);
                    }else{
                        if (beforeIntent.hasExtra("alarmId")){
                            OneBtnPopup(this,"복약 알람 수정","요일을 선택해주세요.","확인");
                        }else{
                            OneBtnPopup(this,"복약 알람 등록","요일을 선택해주세요.","확인");
                        }
                    }
                }else{
                    Toast.makeText(this, "알림 이름을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.img_drug_alarm_remove : {
                mDbOpenHelper.deleteColumn(beforeIntent.getIntExtra("alarmId",0));

                ProgressDialog progressDialog = new ProgressDialog(this,R.style.MyTheme);
                progressDialog.show();
                progressDialog.setCancelable(false);

                alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);
                alarmIntent = PendingIntent.getBroadcast(this, beforeIntent.getIntExtra("alarmId",0), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmMgr.cancel(alarmIntent);
                intent.putExtra("removeFlag", true);
                mDbOpenHelper.deleteColumn(beforeIntent.getIntExtra("alarmId",0));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
//                        setResult(RESULT_OK,intent);
                        onBackPressed();
                    }
                },500);
                break;
            }
        }
    }

}
