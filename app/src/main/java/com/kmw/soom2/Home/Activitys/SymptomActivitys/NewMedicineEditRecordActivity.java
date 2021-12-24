package com.kmw.soom2.Home.Activitys.SymptomActivitys;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeItem.EtcItem;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.Views.CustomScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.FEED_INSERT_MEDICINE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.FEED_UPDATE_MEDICINE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HOME_FEED_CALL_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_HISTORY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_SYMPTOM_LIST_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;
import static com.kmw.soom2.Common.Utils.userItem;

public class NewMedicineEditRecordActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "NewMedicineEditRecordActivity";
    private TextView txtBack;
    private ImageView imgAllRemove;
    private TextView txtDate,txtTime;
    private LinearLayout listView;
    private Button mButtonFinish;
    private TextView mTextMemoLength;
    private EditText mEditMemo;
    private CustomScrollView mScrollView;

    private Intent beforeIntent;
    private ProgressDialog progressDialog;
    private TwoButtonDialog twoButtonDialog;
    public static boolean allRemove = false;
    public ArrayList<EtcItem> etcItemArrayList = new ArrayList<>();
    public ArrayList<String> etcItemMedicineNoArrayList = new ArrayList<>();

    Calendar calendar = Calendar.getInstance();
    Date date = new Date(System.currentTimeMillis());

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy");
    SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM");

    Calendar currentCalendar;
    Date currentDate = null;

    JSONArray medicineArray = new JSONArray();
    JSONObject medicineObject = new JSONObject();
    String mYear = "";
    String mMonth = "";
    String registerDt = "";
    boolean dateCheck = true;
    ArrayList<String> historyNoList = new ArrayList<>();
    ArrayList<String> medicineNoList = new ArrayList<>();
    ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();
    private String allRemoveNo = "";

    String mSaveBeforeRegisterDt = "";
    String mSaveAfterRegisterDt = "";
    int[] mSaveBeforeEmergencyFlag = new int[]{};
    int[] mSaveAfterEmergencyFlag = new int[]{};
    boolean mSaveCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine_edit_record);

        beforeIntent = getIntent();

        NullCheck(this);

        FindViewById();

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_medicine_record_edit_back);
        imgAllRemove = (ImageView) findViewById(R.id.img_medicine_record_edit_all_remove);
        txtDate = (TextView)findViewById(R.id.txt_medicine_record_edit_date);
        txtTime = (TextView)findViewById(R.id.txt_medicine_record_edit_time);
        mButtonFinish = (Button)findViewById(R.id.btn_medicine_record_edit_finish);
        listView = (LinearLayout)findViewById(R.id.list_medicine_record_edit);
        mTextMemoLength         = (TextView)findViewById(R.id.txt_new_symptom_memo_length);
        mEditMemo               = (EditText)findViewById(R.id.edt_new_symptom_record_memo);
        mScrollView = (CustomScrollView) findViewById(R.id.scr_medicine_record_list);

        txtBack.setOnClickListener(this);
        imgAllRemove.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")) {
                registerDt = beforeIntent.getStringExtra("registerDt");
                mSaveBeforeRegisterDt = registerDt;
                mSaveAfterRegisterDt = registerDt;
                if (beforeIntent.hasExtra("memo")){
                    mEditMemo.setText(beforeIntent.getStringExtra("memo"));
                }
                try {
                    txtDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0,10))));
                    txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11,18))));
                    mYear = simpleDateFormatYear.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                    mMonth = simpleDateFormatMonth.format(formatYYYYMMDD2.parse(txtDate.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            NetworkCall(MEDICINE_HISTORY_LIST);
        }else{
            txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
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

//        mEditMemo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mScrollView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mScrollView.smoothScrollTo(0, mScrollView.getBottom() + mScrollView.getScrollY());
//                    }
//                });
//            }
//        });

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

    void MedicineListMake(int position, String medicineNo, String historyNo, int emergencyFlag, String ko, String volume, int medicineTypeNo){

        View view = getLayoutInflater().inflate(R.layout.view_medicine_record_list_item,null);

        ImageView imgIcon = (ImageView)view.findViewById(R.id.img_medicine_record_list_item_icon);
        TextView txtName = (TextView)view.findViewById(R.id.txt_medicine_record_list_item_name);
        LinearLayout linStatus = (LinearLayout)view.findViewById(R.id.lin_medicine_record_list_item_status);
        ImageView txtStatus = (ImageView)view.findViewById(R.id.txt_medicine_record_list_item_status);
        LinearLayout linClick = (LinearLayout)view.findViewById(R.id.lin_medicine_record_list_item_click);

        historyNoList.add(historyNo);

        LinearLayout linSpace = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,40);
        linSpace.setLayoutParams(params);

        listView.addView(view);
        listView.addView(linSpace);

        txtName.setText(ko);

        if (Utils.MEDICINE_TYPE_LIST != null){
            for (int j = 0; j < Utils.MEDICINE_TYPE_LIST.size(); j++){
                if (Utils.MEDICINE_TYPE_LIST.get(j).getMedicineTypeNo() == medicineTypeNo){
                    if (Utils.MEDICINE_TYPE_LIST.get(j).getTypeImg().length() != 0){
                        String replaceText = Utils.MEDICINE_TYPE_LIST.get(j).getTypeImg();
                        if (replaceText.contains("soom2.testserver-1.com:8080")){
                            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                        }else if (replaceText.contains("103.55.190.193")){
                            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                        }
                        Glide.with(this).load("https:"+replaceText).into(imgIcon);
                    }
                }
            }
        }

        linStatus.setBackgroundTintList(getColorStateList(R.color.dbdbdb));
        imgIcon.setBackgroundTintList(getColorStateList(R.color.white));
        txtName.setTextColor(getColor(R.color.black));

        final int[] mEmergencyFlag = {emergencyFlag};

        if (mEmergencyFlag[0] == 1){
            linStatus.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
            imgIcon.setBackgroundTintList(getColorStateList(R.color.color33d16b30));
            txtName.setTextColor(getColor(R.color.color09D192));
            medicineNoList.add(historyNo);
        }else if (mEmergencyFlag[0] == 2){
            linStatus.setBackgroundTintList(getColorStateList(R.color.ff6767));
            imgIcon.setBackgroundTintList(getColorStateList(R.color.ff676760));
            txtName.setTextColor(getColor(R.color.ff6767));
            medicineNoList.add(historyNo);
        }else{
            linStatus.setBackgroundTintList(getColorStateList(R.color.dbdbdb));
            imgIcon.setBackgroundTintList(getColorStateList(R.color.white));
            txtName.setTextColor(getColor(R.color.black));
        }

        mSaveBeforeEmergencyFlag[position] = emergencyFlag;
        mSaveAfterEmergencyFlag[position] = emergencyFlag;

        linClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmergencyFlag[0] == 1){
                    linStatus.setBackgroundTintList(getColorStateList(R.color.ff6767));
                    imgIcon.setBackgroundTintList(getColorStateList(R.color.ff676760));
                    txtName.setTextColor(getColor(R.color.ff6767));
                    mEmergencyFlag[0] = 2;
                    mSaveAfterEmergencyFlag[position] = 2;
                    etcItemArrayList.get(position).setEmergencyFlag(2);
                }else if (mEmergencyFlag[0] == 2){
                    linStatus.setBackgroundTintList(getColorStateList(R.color.dbdbdb));
                    imgIcon.setBackgroundTintList(getColorStateList(R.color.white));
                    txtName.setTextColor(getColor(R.color.black));
                    mEmergencyFlag[0] = 0;
                    mSaveAfterEmergencyFlag[position] = 0;
                    etcItemArrayList.get(position).setEmergencyFlag(0);
                }else{
                    linStatus.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                    imgIcon.setBackgroundTintList(getColorStateList(R.color.color33d16b30));
                    txtName.setTextColor(getColor(R.color.color09D192));
                    mEmergencyFlag[0] = 1;
                    mSaveAfterEmergencyFlag[position] = 1;
                    etcItemArrayList.get(position).setEmergencyFlag(1);
                }
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

    void DateTimePicker() {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker, null);
        final Dialog dateTimeDialog = new Dialog(NewMedicineEditRecordActivity.this);
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

    void NetworkCall(String mCode){
        Log.i(TAG,"mCode : " + mCode);
        if (mCode.equals(MEDICINE_HISTORY_LIST)){   //recordActivity 에서 사용
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute("" + Utils.userItem.getUserNo());
        }else if (mCode.equals(HOME_FEED_CALL_LIST)){   //recordEditActivity 사용
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),"1",txtDate.getText().toString().substring(0,4),txtDate.getText().toString().substring(6,8));
        }else if (mCode.equals(NEW_SYMPTOM_LIST_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(allRemoveNo);
        }else if (mCode.equals(FEED_INSERT_MEDICINE)){
            int j = 0;
            if (etcItemArrayList.size() > 0) {
                for (int i = 0; i < etcItemArrayList.size(); i++) {
                    if (etcItemArrayList.get(i).getEmergencyFlag()==1 || etcItemArrayList.get(i).getEmergencyFlag()==2) {
                        j++;
                        MedicineJsonMake(userItem.getUserNo(), etcItemArrayList.get(i).getMedicineHistoryNo(),etcItemArrayList.get(i).getMedicineNo(), etcItemArrayList.get(i).getFrequency(),
                                etcItemArrayList.get(i).getVolume(), etcItemArrayList.get(i).getUnit(), etcItemArrayList.get(i).getEmergencyFlag(),
                                etcItemArrayList.get(i).getStartDt(), etcItemArrayList.get(i).getEndDt(), etcItemArrayList.get(i).getKo(), registerDt);
                    }
                }
                if (j == 0) {
                    new OneButtonDialog(NewMedicineEditRecordActivity.this, "복용기록", "복용한 약을 체크해주세요.", "확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {
                            mButtonFinish.setEnabled(true);
                        }
                    });
                } else {
                    new NetworkUtils.NetworkCall(this, this, TAG, mCode).execute(medicineArray.toString());
                }
            }
        }else if (mCode.equals(FEED_UPDATE_MEDICINE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(allRemoveNo);
        }else if (mCode.equals(SELECT_HOME_FEED_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),mYear,mMonth);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(HOME_FEED_CALL_LIST)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    int size = jsonArray.length();

                    for (int i = 0; i < size; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.i(TAG,"object : " + object.toString());
                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1){
                            if (JsonIntIsNullCheck(object,"CATEGORY") == 1){
                                if (beforeIntent.getStringExtra("registerDt").equals(JsonIsNullCheck(object,"REGISTER_DT"))) {
                                    if (etcItemMedicineNoArrayList.contains(JsonIsNullCheck(object,"MEDICINE_NO"))){
                                        EtcItem etcItem = new EtcItem();
                                        etcItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                                        etcItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                                        etcItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                                        etcItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                                        etcItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                                        etcItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                                        etcItem.setRegisterDt(JsonIsNullCheck(object,"REGISTER_DT"));
                                        etcItem.setMedicineNo(JsonIsNullCheck(object,"MEDICINE_NO"));
                                        etcItem.setMedicineHistoryNo(JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"));
                                        etcItem.setUserHistoryNo(JsonIsNullCheck(object,"USER_HISTORY_NO"));
//                                        etcItem.setMedicineTypeNo(etcItemArrayList.get(etcItemMedicineNoArrayList.indexOf(JsonIsNullCheck(object,"MEDICINE_NO"))).getMedicineTypeNo());
                                        etcItem.setKo(JsonIsNullCheck(object,"KO"));
                                        etcItem.setMedicineTypeNo(JsonIntIsNullCheck(object,"MEDICINE_TYPE_NO"));
                                        if (allRemoveNo.length() == 0){
                                            allRemoveNo += "" + JsonIsNullCheck(object,"USER_HISTORY_NO");
                                        }else{
                                            allRemoveNo += "," + JsonIsNullCheck(object,"USER_HISTORY_NO");
                                        }
                                        etcItemArrayList.set(etcItemMedicineNoArrayList.indexOf(JsonIsNullCheck(object,"MEDICINE_NO")),etcItem);
                                    }
                                }
                            }
                        }
                    }

                    mSaveBeforeEmergencyFlag = new int[etcItemArrayList.size()];
                    mSaveAfterEmergencyFlag = new int[etcItemArrayList.size()];

                    for (int i = 0; i < etcItemArrayList.size(); i++){
                        MedicineListMake(i,etcItemArrayList.get(i).getMedicineNo(),etcItemArrayList.get(i).getUserHistoryNo(),etcItemArrayList.get(i).getEmergencyFlag(),
                                etcItemArrayList.get(i).getKo(),etcItemArrayList.get(i).getVolume()+etcItemArrayList.get(i).getUnit(),etcItemArrayList.get(i).getMedicineTypeNo());
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(MEDICINE_HISTORY_LIST)){
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
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1 &&
                                    Integer.parseInt(simpleDateFormat.format(formatYYYYMMDD2.parse(txtDate.getText().toString()))) >= Integer.parseInt(JsonIsNullCheck(object, "START_DT")) &&
                                    Integer.parseInt(simpleDateFormat.format(calendar.getTime())) < Integer.parseInt(JsonIsNullCheck(object, "END_DT"))) {
                                EtcItem etcItem = new EtcItem();
                                etcItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                                etcItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                                etcItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                                etcItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                                etcItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                                etcItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                                etcItem.setRegisterDt(JsonIsNullCheck(object,"REGISTER_DT"));
                                etcItem.setMedicineNo(JsonIsNullCheck(object,"MEDICINE_NO"));
                                etcItem.setUserHistoryNo(JsonIsNullCheck(object,"USER_HISTORY_NO"));
                                etcItem.setMedicineHistoryNo(JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"));
                                etcItem.setKo(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"), "KO"));
                                etcItem.setMedicineTypeNo(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"), "MEDICINE_TYPE_NO"));
                                etcItemMedicineNoArrayList.add(JsonIsNullCheck(object,"MEDICINE_NO"));
                                etcItemArrayList.add(etcItem);
                            }
                        }
                        NetworkCall(HOME_FEED_CALL_LIST);
                    }
                } catch (JSONException e) {

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if (mCode.equals(FEED_INSERT_MEDICINE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                },500);
            }else if (mCode.equals(FEED_UPDATE_MEDICINE)){
                NetworkCall(FEED_INSERT_MEDICINE);
            }else if (mCode.equals(NEW_SYMPTOM_LIST_DELETE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
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
                                    OneButtonDialog oneButtonDialog = new OneButtonDialog(NewMedicineEditRecordActivity.this,"복용기록 중복","선택한 시간에 이미\n약 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
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
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mSaveBeforeEmergencyFlag.length > 0){
            for (int i = 0; i < mSaveBeforeEmergencyFlag.length; i++){
                if (mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt) && mSaveBeforeEmergencyFlag[i] == mSaveAfterEmergencyFlag[i]){
                    mSaveCheck = false;
                }else{
                    mSaveCheck = true;
                    break;
                }
            }
        }else{
            if (mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt)){
                mSaveCheck = false;
            }else{
                mSaveCheck = true;
            }
        }

        if (mSaveCheck){
            twoButtonDialog = new TwoButtonDialog(NewMedicineEditRecordActivity.this,"증상기록", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
            twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                @Override
                public void OkButtonClick(View v) {
                    twoButtonDialog.dismiss();
                    if (beforeIntent.hasExtra("medicineAlarm")) {
                        Intent i = new Intent(NewMedicineEditRecordActivity.this, MainActivity.class);
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
                Intent i = new Intent(NewMedicineEditRecordActivity.this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_medicine_record_edit_back: {
                onBackPressed();
                break;
            }
            case R.id.txt_medicine_record_edit_date: {
//                DateTimePicker("날짜선택");
                break;
            }
            case R.id.txt_medicine_record_edit_time : {
                DateTimePicker();
                break;
            }
            case R.id.img_medicine_record_edit_all_remove : {
                twoButtonDialog = new TwoButtonDialog(this,"복용기록 삭제","복용기록을\n전체 삭제하시겠습니까?","취소","확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        NetworkCall(NEW_SYMPTOM_LIST_DELETE);
                    }
                });
                break;
            }
            case R.id.btn_medicine_record_edit_finish: {
                if (dateCheck){
                    mButtonFinish.setEnabled(false);
                    NetworkCall(FEED_UPDATE_MEDICINE);
                }else{
                    OneButtonDialog oneButtonDialog = new OneButtonDialog(NewMedicineEditRecordActivity.this,"복용기록 중복","선택한 시간에 이미\n약 기록이 존재합니다.\n다른 시간을 선택해주세요.","확인");
                    oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {
                            oneButtonDialog.dismiss();
                        }
                    });
                }
                break;
            }
        }
    }

    void MedicineJsonMake(int userNo, int medicineHistoryNo ,String medicineNo, int frequency, String volume, String unit, int emergencyFlag, String startDt, String endDt,
                          String medicineKo, String registerDt){
        try {
            medicineObject = new JSONObject();
            medicineObject.accumulate("USER_NO",""+userNo             );
            medicineObject.accumulate("MEDICINE_HISTORY_NO",""+medicineHistoryNo       );
            medicineObject.accumulate("MEDICINE_NO",medicineNo     );
            medicineObject.accumulate("FREQUENCY",""+frequency        );
            medicineObject.accumulate("VOLUME",volume              );
            medicineObject.accumulate("UNIT",unit                            );
            medicineObject.accumulate("EMERGENCY_FLAG",""+emergencyFlag        );
            medicineObject.accumulate("START_DT",startDt                    );
            medicineObject.accumulate("END_DT",endDt                        );
            medicineObject.accumulate("KO",medicineKo                       );
            medicineObject.accumulate("NICKNAME",userItem.getNickname()                   );
            medicineObject.accumulate("GENDER",""+userItem.getGender()              );
            medicineObject.accumulate("BIRTH",""+userItem.getBirth()              );
            medicineObject.accumulate("REGISTER_DT",registerDt               );
            medicineObject.accumulate("CATEGORY","1"                );
            medicineObject.accumulate("MEMO",mEditMemo.getText().toString());
            medicineArray.put(medicineObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}