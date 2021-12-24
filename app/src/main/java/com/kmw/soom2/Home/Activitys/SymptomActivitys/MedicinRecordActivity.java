package com.kmw.soom2.Home.Activitys.SymptomActivitys;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeItem.MedicineTypeItem;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeAdapter.MedicineRecordListAdapter;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.FEED_INSERT_MEDICINE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HOME_FEED_MEDICINE_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_HISTORY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_TYPE_CALL_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST_CHECK;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.MEDICINE_TYPE_LIST;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.breathItem;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMM2;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDDHHMMSS;
import static com.kmw.soom2.Common.Utils.setListViewHeightBasedOnChildren;
import static com.kmw.soom2.Common.Utils.userItem;

public class MedicinRecordActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String TAG = "MedicinRecordActivity";
    TextView txtBack;
    TextView txtDate, txtTime;
    LinearLayout linNoList,linInfoVisible;
    ScrollView scrList;
    ListView listView;
    LinearLayout linListViewParentVisible;
    Button btnFinish;
    ImageView addImage;
    String registerDt = "";
    Date currentDate = null;
    ImageView imgPlus;
    LinearLayout linPlus;
    private TextView mTextMemoLength;
    private EditText mEditMemo;
    private ScrollView mScrollView;

    Calendar calendar = Calendar.getInstance();
    Date date = new Date(System.currentTimeMillis());

    ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();
    public static ArrayList<MedicineTakingItem> medicineTakingItems1 = new ArrayList<>();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
    SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");

    MedicineRecordListAdapter adapter;

//    public static SimpleDateFormat formatYYYYMMDDHHMMSS = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");

    ProgressDialog progressDialog;

    Intent beforeIntent;

    Calendar currentCalendar;

    JSONArray medicineArray = new JSONArray();
    JSONObject medicineObject = new JSONObject();
    String mYear = "";
    String mMonth = "";
    boolean dateCheck = true;
    int j = 0;
    int jCheck = 0;

    String mSaveBeforeRegisterDt = "";
    String mSaveAfterRegisterDt = "";

    TwoButtonDialog twoButtonDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicin_record);

        beforeIntent = getIntent();

        currentCalendar = Calendar.getInstance();

        FindViewById();

        if (beforeIntent.hasExtra("date")) {
            txtDate.setText(beforeIntent.getStringExtra("date"));
            txtTime.setText(beforeIntent.getStringExtra("time"));
            try {
                mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString()));
                mSaveBeforeRegisterDt = registerDt;
                mSaveAfterRegisterDt = registerDt;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString()));
                mSaveBeforeRegisterDt = registerDt;
                mSaveAfterRegisterDt = registerDt;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        linNoList.setVisibility(View.VISIBLE);
        linListViewParentVisible.setVisibility(View.GONE);
        linInfoVisible.setVisibility(View.GONE);
        btnFinish.setEnabled(false);
        btnFinish.setBackgroundTintList(getColorStateList(R.color.e2e2e2));

        if (MEDICINE_TYPE_LIST == null){
            NetworkCall(MEDICINE_TYPE_CALL_LIST);
        }else{
            NetworkCall(MEDICINE_HISTORY_LIST);
        }

    }

    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_medicine_record_back);
        txtDate = (TextView) findViewById(R.id.txt_medicine_record_date);
        txtTime = (TextView) findViewById(R.id.txt_medicine_record_time);
        linNoList = (LinearLayout) findViewById(R.id.lin_medicine_record_no_list);
        linInfoVisible = (LinearLayout)findViewById(R.id.lin_medicine_record_info_visible);
        scrList = (ScrollView) findViewById(R.id.scr_medicine_record_list);
        listView = (ListView) findViewById(R.id.list_medicine_record);
        btnFinish = (Button) findViewById(R.id.btn_medicine_record_finish);
        addImage = (ImageView) findViewById(R.id.add_img_medicine_record_list);
        imgPlus = (ImageView)findViewById(R.id.img_medicine_record_plus);
        linPlus = (LinearLayout)findViewById(R.id.lin_medicine_record_plus);
        linListViewParentVisible = (LinearLayout)findViewById(R.id.lin_list_view_parent_visible);
        mTextMemoLength         = (TextView)findViewById(R.id.txt_new_symptom_memo_length);
        mEditMemo               = (EditText)findViewById(R.id.edt_new_symptom_record_memo);
        mScrollView = (ScrollView)findViewById(R.id.scr_medicine_record_list);

        txtDate.setText(Utils.formatYYYYMMDD2.format(date));
        txtTime.setText(formatHHMM.format(date));

        calendar.setTime(date);
        addImage.setOnClickListener(this);
        txtBack.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        imgPlus.setOnClickListener(this);
        linPlus.setOnClickListener(this);

        mEditMemo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextMemoLength.setText("("+s.length()+"/2000)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mEditMemo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        mEditMemo.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (mEditMemo.hasFocus()) {
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

        setHideKeyboard(this,mScrollView);
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

    void DateTimePicker() {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker, null);
        final Dialog dateTimeDialog = new Dialog(MedicinRecordActivity.this);
        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker) dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayMonths(false);
        singleDateAndTimePicker.setDisplayDaysOfMonth(false);
        singleDateAndTimePicker.setDisplayHours(true);
        singleDateAndTimePicker.setDisplayMinutes(true,txtDate.getText().toString());
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                dateCheck = true;
                txtTime.setText(formatHHMM.format(date));
                try {
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString()));
                    mSaveAfterRegisterDt = registerDt;
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

    //건너뜀0, 일반1, 응급2

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(MEDICINE_HISTORY_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute("" + Utils.userItem.getUserNo());
        }else if (mCode.equals(FEED_INSERT_MEDICINE)){
            if (medicineTakingItems1.size() > 0) {
                progressDialog.dismiss();
                try {
                    registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < medicineTakingItems1.size(); i++) {
                    if (medicineTakingItems1.get(i).getEmergencyFlag().equals("1") || medicineTakingItems1.get(i).getEmergencyFlag().equals("2")) {
                        j++;
                        MedicineJsonMake(userItem.getUserNo(),medicineTakingItems1.get(i).getMedicineHistoryNo(),medicineTakingItems1.get(i).getMedicineNo(),  medicineTakingItems1.get(i).getFrequency(),
                                medicineTakingItems1.get(i).getVolume(), medicineTakingItems1.get(i).getUnit(), medicineTakingItems1.get(i).getEmergencyFlag(),
                                medicineTakingItems1.get(i).getStartDt(), medicineTakingItems1.get(i).getEndDt(), medicineTakingItems1.get(i).getMedicineKo(),Utils.userItem.getNickname(),
                                Utils.userItem.getGender(),Utils.userItem.getBirth(),registerDt);
                    }
                }
                if (j == 0){
                    progressDialog.dismiss();
                    new OneButtonDialog(MedicinRecordActivity.this,"복용기록","복용한 약을 체크해주세요.","확인");
                }else{
                    new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(medicineArray.toString());
                }
            }
        }else if (mCode.equals(SELECT_HOME_FEED_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),mYear,mMonth);
        }else if (mCode.equals(SELECT_HOME_FEED_LIST_CHECK)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),mYear,mMonth);
        }else if (mCode.equals(MEDICINE_TYPE_CALL_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(MEDICINE_HISTORY_LIST)){
                adapter = new MedicineRecordListAdapter(getLayoutInflater(), MedicinRecordActivity.this);
                medicineTakingItems = new ArrayList<>();
                medicineTakingItems1 = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    calendar.add(Calendar.DATE,1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.has("list")) {
                        linNoList.setVisibility(View.GONE);
                        linListViewParentVisible.setVisibility(View.VISIBLE);
                        linInfoVisible.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1 &&
                                    Integer.parseInt(simpleDateFormat.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))) >= Integer.parseInt(JsonIsNullCheck(object, "START_DT")) &&
                                    Integer.parseInt(simpleDateFormat.format(calendar.getTime())) < Integer.parseInt(JsonIsNullCheck(object, "END_DT"))) {
                                MedicineTakingItem medicineTakingItem = new MedicineTakingItem();
                                medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                medicineTakingItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                medicineTakingItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                medicineTakingItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                medicineTakingItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                                medicineTakingItem.setEndDt(JsonIsNullCheck(object, "END_DT"));
                                medicineTakingItem.setAliveFlag(JsonIntIsNullCheck(object, "ALIVE_FLAG"));
                                medicineTakingItem.setMedicineHistoryNo(JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"));
                                medicineTakingItem.setEmergencyFlag("0");

                                //clsMedicineBean
                                medicineTakingItem.setMedicineKo(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"), "KO"));
                                medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"), "MEDICINE_TYPE_NO"));

                                medicineTakingItems.add(medicineTakingItem);
                                medicineTakingItems1.add(medicineTakingItem);

                                adapter.addItem(medicineTakingItem);

                            }
                        }
                        if (adapter.getCount() == 0) {
                            linNoList.setVisibility(View.VISIBLE);
                            linListViewParentVisible.setVisibility(View.GONE);
                            linInfoVisible.setVisibility(View.GONE);
                            btnFinish.setEnabled(false);
                            btnFinish.setBackgroundTintList(getColorStateList(R.color.e2e2e2));
                        } else {
                            listView.setAdapter(adapter);
                            linNoList.setVisibility(View.GONE);
                            linListViewParentVisible.setVisibility(View.VISIBLE);
                            linInfoVisible.setVisibility(View.VISIBLE);
                            btnFinish.setEnabled(true);
                            btnFinish.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                            setListViewHeightBasedOnChildren(listView);
                        }
                    } else {
                        linNoList.setVisibility(View.VISIBLE);
                        linListViewParentVisible.setVisibility(View.GONE);
                        linInfoVisible.setVisibility(View.GONE);
                        btnFinish.setEnabled(false);
                        btnFinish.setBackgroundTintList(getColorStateList(R.color.e2e2e2));
                    }

                } catch (JSONException e) {

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if (mCode.equals(FEED_INSERT_MEDICINE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnFinish.setEnabled(true);
                        setResult(RESULT_OK);
                        if (beforeIntent.hasExtra("medicineAlarm")) {
                            Intent i = new Intent(MedicinRecordActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
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
                                if (JsonIsNullCheck(object,"REGISTER_DT").equals(registerDt)){
                                    dateCheck = false;
                                    OneButtonDialog oneButtonDialog = new OneButtonDialog(MedicinRecordActivity.this,"복용기록 중복","선택한 시간에 이미\n약 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
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
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.has("list")){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                                if (JsonIsNullCheck(object,"CATEGORY").equals("1")){
                                    if (JsonIsNullCheck(object,"REGISTER_DT").equals(registerDt)){
                                        dateCheck = false;
                                        OneButtonDialog oneButtonDialog = new OneButtonDialog(MedicinRecordActivity.this,"복용기록 중복","선택한 시간에 이미\n약 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
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
                    }
                    if (dateCheck){
                        NetworkCall(FEED_INSERT_MEDICINE);
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(MEDICINE_TYPE_CALL_LIST)){
                MEDICINE_TYPE_LIST = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        MedicineTypeItem medicineTypeItem = new MedicineTypeItem();

                        medicineTypeItem.setMedicineTypeNo(JsonIntIsNullCheck(object, "MEDICINE_TYPE_NO"));
                        medicineTypeItem.setTypeImg(JsonIsNullCheck(object, "TYPE_IMG"));

                        MEDICINE_TYPE_LIST.add(medicineTypeItem);
                    }

                    NetworkCall(MEDICINE_HISTORY_LIST);
                }catch (JSONException e){

                }
            }
        }
    }

    Date now = Calendar.getInstance().getTime();
    SimpleDateFormat format = new SimpleDateFormat("yyyy.mm.dd");

    void DateTimePicker(String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker, null);
        final Dialog dateDialog = new Dialog(MedicinRecordActivity.this);
        TextView txtTitle = (TextView) dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateDialog.setContentView(dateTimeView);
        dateDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = dateTimeView.findViewById(R.id.single_day_picker);

//        WheelYearPicker wheelYearPicker = new WheelYearPicker(this);
//        Calendar instance = Calendar.getInstance();
//        int currentYear = instance.get(Calendar.YEAR);
//        wheelYearPicker.setMaxYear(currentYear);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false,"");
        singleDateAndTimePicker.setMustBeOnFuture(false);

        try {
            currentDate = formatYYYYMMDD2.parse(txtDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        singleDateAndTimePicker.setDefaultDate(currentDate);

        calendar.setTime(currentDate);

        singleDateAndTimePicker.selectDate(calendar);
        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                dateCheck = true;
                txtDate.setText(Utils.formatYYYYMMDD2.format(date));
                try {
                    registerDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMMSS.format(formatHHMM.parse(txtTime.getText().toString()));
                    mSaveAfterRegisterDt = registerDt;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                NetworkCall(MEDICINE_HISTORY_LIST);
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

    boolean backCheck = false;

    @Override
    public void onBackPressed() {
        if (medicineTakingItems1.size() > 0){
            for (int i = 0; i < medicineTakingItems1.size(); i++){
                if (mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt) && medicineTakingItems1.get(i).getEmergencyFlag().equals("0")){
                    backCheck = false;
                }else{
                    backCheck = true;
                    break;
                }
            }
        }else{
            if (mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt)){
                backCheck = false;
            }else{
                backCheck = true;
            }
        }

        if (backCheck){
            twoButtonDialog = new TwoButtonDialog(MedicinRecordActivity.this,"증상기록", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
            twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                @Override
                public void OkButtonClick(View v) {
                    twoButtonDialog.dismiss();
                    if (beforeIntent.hasExtra("medicineAlarm")) {
                        Intent i = new Intent(MedicinRecordActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                    finish();
                }
            });
            twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                @Override
                public void CancelButtonClick(View v) {
                    twoButtonDialog.dismiss();
                }
            });
        }else{
            if (beforeIntent.hasExtra("medicineAlarm")) {
                Intent i = new Intent(MedicinRecordActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_medicine_record_date: {
                DateTimePicker("날짜선택");
                break;
            }
            case R.id.txt_medicine_record_time : {
                DateTimePicker();
                break;
            }
            case R.id.add_img_medicine_record_list: {
                Intent i = new Intent(MedicinRecordActivity.this, MedicineSelectActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_medicine_record_back: {
                onBackPressed();
                break;
            }
            case R.id.btn_medicine_record_finish: {
                NetworkCall(SELECT_HOME_FEED_LIST_CHECK);
                break;
            }
            case R.id.img_medicine_record_plus : {
                Intent intent = new Intent(this,MedicineSelectActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_medicine_record_plus : {
                Intent intent = new Intent(this,MedicineSelectActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NullCheck(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }


    void MedicineJsonMake(int userNo, int medicineHistoryNo,int medicineNo, int frequency, String volume, String unit, String emergencyFlag, String startDt, String endDt,
                          String medicineKo , String nickName, int gender, int birth, String registerDt){
        try {
            medicineObject = new JSONObject();
            medicineObject.accumulate("USER_NO",""+userNo            );
            medicineObject.accumulate("MEDICINE_HISTORY_NO",""+medicineHistoryNo       );
            medicineObject.accumulate("MEDICINE_NO",""+medicineNo    );
            medicineObject.accumulate("FREQUENCY",""+frequency       );
            medicineObject.accumulate("VOLUME",""+volume             );
            medicineObject.accumulate("UNIT",unit                           );
            medicineObject.accumulate("EMERGENCY_FLAG",emergencyFlag        );
            medicineObject.accumulate("START_DT",startDt                    );
            medicineObject.accumulate("END_DT",endDt                        );
            medicineObject.accumulate("KO",medicineKo                       );
            medicineObject.accumulate("NICKNAME",nickName                   );
            medicineObject.accumulate("GENDER",""+gender             );
            medicineObject.accumulate("BIRTH",""+birth               );
            medicineObject.accumulate("REGISTER_DT",registerDt              );
            medicineObject.accumulate("CATEGORY","1"                 );
            medicineObject.accumulate("MEMO",mEditMemo.getText().toString());
            medicineArray.put(medicineObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
