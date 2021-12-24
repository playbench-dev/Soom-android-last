package com.kmw.soom2.Home.Activitys.MedicineInsertActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.DrugControl.Activity.DrugCompleteListActivity;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_CALL_DETAIL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_HISTORY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_HISTORY_LIST_CHECK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_REVIEW_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_REVIEW_LIST_ONRESUME;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.REVIEW_DELETE;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.MedicineTakingItems;
import static com.kmw.soom2.Common.Utils.userItem;

public class MedicineInsertActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "MedicineInsertActivity";
    TextView imgBack;
    TextView txtMedicineName;
    TextView txtSetting, txtInfo, txtReview;
    LinearLayout linSettingLine, linInfoLine, linReviewLine;
    LinearLayout linSettingVisible, linInfoVisible, linReviewVisible;
    ImageView imgMedicine;
    ImageView imgRemove;
    TwoButtonDialog twoButtonDialog;
    OneButtonDialog oneButtonDialog;
    ProgressDialog progressDialog;
    //setting
    EditText edtAmount;
    TextView txtUnit, txtCount, txtStartDt, txtEndDt;
    //info
    TextView txtEfficacy, txtInstructions, txtInformation, txtStability, txtPrecautions;
    //review
    LinearLayout linReviewInsert;
    LinearLayout linReviewList, linReviewNoList;
    LinearLayout linReviewMore;
    Button btnFinish;

    Calendar calendar = Calendar.getInstance();
    Date date = new Date(System.currentTimeMillis());

    String[] unitStrings = new String[]{"개", "회", "ml", "g", "mg", "방울"};
    String[] cntStrings = new String[]{"필요시", "하루 1번", "하루 2번", "하루 3번", "하루 4번", "하루 5번", "하루 6번", "하루 7번", "하루 8번"};

    int frequency = 1;

    Intent beforeIntent;

    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy");

    ArrayList<Integer> userNoList = new ArrayList<>();
    ArrayList<Integer> reviewNoList = new ArrayList<>();
    ArrayList<Integer> effectList = new ArrayList<>();
    ArrayList<String> nicknameList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> lvList = new ArrayList<>();
    ArrayList<String> periodList = new ArrayList<>();
    ArrayList<String> contentsList = new ArrayList<>();
    ArrayList<String> birthList = new ArrayList<>();
    ArrayList<Integer> genderList = new ArrayList<>();
    ArrayList<String> profilePathList = new ArrayList<>();
    ArrayList<String> labelNameList = new ArrayList<>();
    ArrayList<String> labelColorList = new ArrayList<>();

    ArrayList<TextView> txtContentsList = new ArrayList<>();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean moreTextCheck = false;

    private String mSaveBeforeVolume = "";
    private String mSaveBeforeUnit = "";
    private String mSaveBeforeCount = "";
    private String mSaveBeforeStartDt = "";
    private String mSaveBeforeEndDt = "";

    Typeface typefaceBold,typefaceMedium ;

    LinearLayout linEndVisible,linBallonVisible;
    FrameLayout frameEndVisivle;
    ImageView imgQuestionMark;
    Switch shwEndVisible;

    int complete = 1; //1 - 기본 2 - 끝내기

    RequestOptions requestOptions = new RequestOptions();

    //복약종료 시
    TextView txtDisItem01,txtDisItem02,txtDisItem03,txtDisItem04,txtDisItem05;
    ImageView imgDisItem01,imgDisItem02,imgDisItem03,imgDisItem04;

    int mReviewNo;
    int mMedicineNo;
    View mReviewRemoveView;

    NestedScrollView nestedScrollView;
    Toolbar toolbar;
    View viewLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_insert);

        beforeIntent = getIntent();

        pref    = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor  = pref.edit();

        FindViewById();

        typefaceBold = ResourcesCompat.getFont(this, R.font.notosanscjkkr_bold);
        typefaceMedium = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);

        NullCheck(this);

        imgRemove.setVisibility(View.INVISIBLE);

        if (beforeIntent != null) {
            if (beforeIntent.hasExtra("historyNo")) {

                imgRemove.setVisibility(View.VISIBLE);

                txtMedicineName.setText(beforeIntent.getStringExtra("name"));
                edtAmount.setText("" + beforeIntent.getStringExtra("volume"));
                txtUnit.setText(beforeIntent.getStringExtra("unit"));

                frequency = beforeIntent.getIntExtra("frequency", 0);

                if (beforeIntent.getIntExtra("frequency", 0) == -1) {
                    txtCount.setText(cntStrings[0]);
                } else {
                    txtCount.setText(cntStrings[beforeIntent.getIntExtra("frequency", 0)]);
                }

                try {
                    txtStartDt.setText(dateFormat2.format(dateFormat1.parse(beforeIntent.getStringExtra("startDt"))));
                    txtEndDt.setText(dateFormat2.format(dateFormat1.parse(beforeIntent.getStringExtra("endDt"))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                btnFinish.setText("수정하기");

                btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));

                linEndVisible.setVisibility(View.VISIBLE);

                if (beforeIntent.hasExtra("complete")){
                    txtDisItem01.setTextColor(Color.parseColor("#33000000"));
                    txtDisItem02.setTextColor(Color.parseColor("#33000000"));
                    txtDisItem03.setTextColor(Color.parseColor("#33000000"));
                    txtDisItem04.setTextColor(Color.parseColor("#33000000"));
                    txtDisItem05.setTextColor(Color.parseColor("#33000000"));
                    imgDisItem01.setVisibility(View.INVISIBLE);
                    imgDisItem02.setVisibility(View.INVISIBLE);
                    imgDisItem03.setVisibility(View.INVISIBLE);
                    imgDisItem04.setVisibility(View.INVISIBLE);
                    imgQuestionMark.setVisibility(View.INVISIBLE);
                    txtDisItem05.setText("종료일");
                    edtAmount.setEnabled(false);
                    edtAmount.setClickable(false);
                    edtAmount.setTextColor(Color.parseColor("#33000000"));
                    txtCount.setEnabled(false);
                    txtCount.setClickable(false);
                    txtCount.setTextColor(Color.parseColor("#33000000"));
                    txtUnit.setEnabled(false);
                    txtUnit.setClickable(false);
                    txtUnit.setTextColor(Color.parseColor("#33000000"));
                    txtStartDt.setEnabled(false);
                    txtStartDt.setClickable(false);
                    txtStartDt.setTextColor(Color.parseColor("#33000000"));
                    txtEndDt.setEnabled(false);
                    txtEndDt.setClickable(false);
                    txtEndDt.setTextColor(Color.parseColor("#33000000"));
                    btnFinish.setText("복용이 종료된 약이에요");
                    btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    btnFinish.setEnabled(false);
                    btnFinish.setClickable(false);

                    linEndVisible.setVisibility(View.VISIBLE);
                    frameEndVisivle.setVisibility(View.VISIBLE);
                    shwEndVisible.setEnabled(false);
                    shwEndVisible.setClickable(false);

                    try {
                        txtStartDt.setText(dateFormat2.format(dateFormat1.parse(beforeIntent.getStringExtra("startDt"))));
                        txtEndDt.setText(dateFormat2.format(dateFormat1.parse(beforeIntent.getStringExtra("endDt"))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    txtEndDt.setText(dateFormat2.format(new Date(System.currentTimeMillis())));
                }

                NetworkCall(MEDICINE_CALL_DETAIL);

            } else if (beforeIntent.hasExtra("medicineNo")) {
                btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                txtEndDt.setText(dateFormat2.format(new Date(System.currentTimeMillis())));
                NetworkCall(MEDICINE_CALL_DETAIL);
                linEndVisible.setVisibility(View.GONE);
                linBallonVisible.setVisibility(View.GONE);
                frameEndVisivle.setVisibility(View.GONE);
            }else{
                txtEndDt.setText(dateFormat2.format(new Date(System.currentTimeMillis())));
            }
        }else{
            txtEndDt.setText(dateFormat2.format(new Date(System.currentTimeMillis())));
        }

        mSaveBeforeVolume = edtAmount.getText().toString();
        mSaveBeforeUnit = txtUnit.getText().toString();
        mSaveBeforeCount = txtCount.getText().toString();
        mSaveBeforeStartDt = txtStartDt.getText().toString();
        mSaveBeforeEndDt = txtEndDt.getText().toString();

        NetworkCall(MEDICINE_REVIEW_LIST);

    }

    void FindViewById() {
        imgBack = (TextView) findViewById(R.id.txt_medicine_insert_back);
        txtMedicineName = (TextView) findViewById(R.id.txt_medicine_insert_title);
        txtSetting = (TextView) findViewById(R.id.txt_medicine_insert_first);
        txtInfo = (TextView) findViewById(R.id.txt_medicine_insert_second);
        txtReview = (TextView) findViewById(R.id.txt_medicine_insert_third);
        linSettingLine = (LinearLayout) findViewById(R.id.lin_medicine_insert_line_first);
        linInfoLine = (LinearLayout) findViewById(R.id.lin_medicine_insert_line_second);
        linReviewLine = (LinearLayout) findViewById(R.id.lin_medicine_insert_line_third);
        linSettingVisible = (LinearLayout) findViewById(R.id.lin_medicine_insert_first);
        linInfoVisible = (LinearLayout) findViewById(R.id.lin_medicine_insert_second);
        linReviewVisible = (LinearLayout) findViewById(R.id.lin_medicine_insert_third);
        imgMedicine = (ImageView) findViewById(R.id.img_medicine_insert_img);
        imgRemove = (ImageView) findViewById(R.id.img_medicine_insert_remove);
        linEndVisible = (LinearLayout)findViewById(R.id.lin_medicine_insert_end_visible);
        linBallonVisible = (LinearLayout)findViewById(R.id.lin_medicine_insert_ballon_visible);
        frameEndVisivle = (FrameLayout)findViewById(R.id.frame_medicine_insert_end_visible);
        imgQuestionMark = (ImageView)findViewById(R.id.img_medicine_insert_question);
        shwEndVisible = (Switch)findViewById(R.id.swh_medicine_insert_end);
        nestedScrollView = (NestedScrollView)findViewById(R.id.scr_medicine_insert);
        toolbar = (Toolbar)findViewById(R.id.toolbar_medicine_insert);
        viewLine = (View)findViewById(R.id.view_medicine_insert);

        txtDisItem01 = (TextView)findViewById(R.id.txt_medicine_insert_dis_item01);
        txtDisItem02 = (TextView)findViewById(R.id.txt_medicine_insert_dis_item02);
        txtDisItem03 = (TextView)findViewById(R.id.txt_medicine_insert_dis_item03);
        txtDisItem04 = (TextView)findViewById(R.id.txt_medicine_insert_dis_item04);
        txtDisItem05 = (TextView)findViewById(R.id.txt_medicine_insert_dis_item05);
        imgDisItem01 = (ImageView) findViewById(R.id.img_medicine_insert_dis_item01);
        imgDisItem02 = (ImageView)findViewById(R.id.img_medicine_insert_dis_item02);
        imgDisItem03 = (ImageView)findViewById(R.id.img_medicine_insert_dis_item03);
        imgDisItem04 = (ImageView)findViewById(R.id.img_medicine_insert_dis_item04);

        //setting
        edtAmount = (EditText) findViewById(R.id.edt_medicine_insert_first_amount);
        txtUnit = (TextView) findViewById(R.id.txt_medicine_insert_first_unit);
        txtCount = (TextView) findViewById(R.id.txt_medicine_insert_first_cnt);
        txtStartDt = (TextView) findViewById(R.id.txt_medicine_insert_first_start_dt);
        txtEndDt = (TextView) findViewById(R.id.txt_medicine_insert_first_end_dt);
        //info
        txtEfficacy = (TextView) findViewById(R.id.txt_medicine_insert_second_efficacy);
        txtInstructions = (TextView) findViewById(R.id.txt_medicine_insert_second_instructions);
        txtInformation = (TextView) findViewById(R.id.txt_medicine_insert_second_information);
        txtStability = (TextView) findViewById(R.id.txt_medicine_insert_second_stability);
        txtPrecautions = (TextView) findViewById(R.id.txt_medicine_insert_second_precautions);
        //review
        linReviewInsert = (LinearLayout) findViewById(R.id.lin_medicine_insert_third_review_insert);
        linReviewList = (LinearLayout) findViewById(R.id.lin_medicine_insert_third_review_list_parent);
        linReviewNoList = (LinearLayout) findViewById(R.id.lin_medicine_insert_third_review_no_list);
        linReviewMore = (LinearLayout) findViewById(R.id.lin_medicine_insert_review_more);

        btnFinish = (Button) findViewById(R.id.btn_medicine_insert_finish);

        imgBack.setOnClickListener(this);
        txtSetting.setOnClickListener(this);
        txtInfo.setOnClickListener(this);
        txtReview.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        imgQuestionMark.setOnClickListener(this);

        //setting
        txtUnit.setOnClickListener(this);
        txtCount.setOnClickListener(this);
        txtStartDt.setOnClickListener(this);
        txtEndDt.setOnClickListener(this);
        //review
        linReviewInsert.setOnClickListener(this);
        linReviewMore.setOnClickListener(this);


        txtCount.setHint("횟수를 선택해주세요.");
        txtCount.setHintTextColor(getColor(R.color.acacac));

        btnFinish.setOnClickListener(this);

        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtAmount.getText().length() > 0 && txtCount.getText().length() > 0 && txtUnit.getText().length() > 0
                && txtStartDt.getText().length() > 0 && txtEndDt.getText().length() > 0){
                    if (mSaveBeforeVolume.equals(edtAmount.getText().toString()) && mSaveBeforeUnit.equals(txtUnit.getText().toString()) &&
                            mSaveBeforeCount.equals(txtCount.getText().toString()) && mSaveBeforeStartDt.equals(txtStartDt.getText().toString()) &&
                            mSaveBeforeEndDt.equals(txtEndDt.getText().toString())){
                        btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    }else{
                        btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }else{
                    btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        shwEndVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    frameEndVisivle.setVisibility(View.VISIBLE);
                    complete = 2;
                    btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));

                }else{
                    frameEndVisivle.setVisibility(View.GONE);
                    complete = 1;
                    btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                }
            }
        });


    }

    void DateTimePicker(final int flag, String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker, null);
        final Dialog dateTimeDialog = new Dialog(MedicineInsertActivity.this);
        TextView txtTitle = (TextView) dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker) dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false,"");

        if (flag != 1){
            singleDateAndTimePicker.setMustBeOnFuture(false);
        }

        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                if (flag == 1) {
                    txtStartDt.setText(dateFormat2.format(date));
                    if (edtAmount.getText().length() > 0 && txtCount.getText().length() > 0 && txtUnit.getText().length() > 0
                            && txtStartDt.getText().length() > 0){
                        if (mSaveBeforeVolume.equals(edtAmount.getText().toString()) && mSaveBeforeUnit.equals(txtUnit.getText().toString()) &&
                                mSaveBeforeCount.equals(txtCount.getText().toString()) && mSaveBeforeStartDt.equals(txtStartDt.getText().toString()) &&
                                mSaveBeforeEndDt.equals(txtEndDt.getText().toString())){
                            btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                        }else{
                            btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                        }
                    }else{
                        btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    }
                    dateTimeDialog.dismiss();
                } else {
                    if (Integer.parseInt(dateFormat2.format(new Date(System.currentTimeMillis())).replace(".","")) >= Integer.parseInt(dateFormat2.format(date).replace(".",""))){
                        txtEndDt.setText(dateFormat2.format(date));
                        dateTimeDialog.dismiss();
                    }
                }
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateTimeDialog.dismiss();
            }
        });
    }

    void DayCntPicker(final int flag, String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.dialog_number_picker, null);
        TextView txtTitle = (TextView) dateTimeView.findViewById(R.id.txt_dialog_number_picker_title);
        Button btnCancel = (Button) dateTimeView.findViewById(R.id.btn_dialog_number_picker_cancel);
        Button btnDone = (Button) dateTimeView.findViewById(R.id.btn_dialog_number_picker_done);
        final NumberPicker numberPicker = (NumberPicker) dateTimeView.findViewById(R.id.number_picker);
        final Dialog dateTimeDialog = new Dialog(MedicineInsertActivity.this);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        numberPicker.setWrapSelectorWheel(false);

        txtTitle.setText(title);

        numberPicker.setMinValue(0);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        if (flag == 1) {

            numberPicker.setMaxValue(unitStrings.length - 1);
            numberPicker.setDisplayedValues(unitStrings);

            for (int i = 0; i < unitStrings.length; i++) {
                if (unitStrings[i].equals(txtUnit.getText().toString())) {
                    numberPicker.setValue(i);
                }
            }
        } else {

            numberPicker.setMaxValue(cntStrings.length - 1);
            numberPicker.setDisplayedValues(cntStrings);

            for (int i = 0; i < cntStrings.length; i++) {
                if (cntStrings[i].equals(txtCount.getText().toString())) {
                    numberPicker.setValue(i);
                }
            }
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = numberPicker.getValue();
                if (flag == 1) {
                    txtUnit.setText(unitStrings[idx]);
                } else {
                    txtCount.setText(cntStrings[idx]);
                    if (idx == 0) {
                        frequency = -1;
                    } else {
                        frequency = idx;
                    }
                }
                if (edtAmount.getText().length() > 0 && txtCount.getText().length() > 0 && txtUnit.getText().length() > 0
                        && txtStartDt.getText().length() > 0 && txtEndDt.getText().length() > 0){
                    if (mSaveBeforeVolume.equals(edtAmount.getText().toString()) && mSaveBeforeUnit.equals(txtUnit.getText().toString()) &&
                            mSaveBeforeCount.equals(txtCount.getText().toString()) && mSaveBeforeStartDt.equals(txtStartDt.getText().toString()) &&
                            mSaveBeforeEndDt.equals(txtEndDt.getText().toString())){
                        btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    }else{
                        btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }else{
                    btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                }
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

    String[] staticsList = new String[]{"전체", "기침", "천명음", "숨쉬기 불편", "가슴답답함"};

    ArrayList<String> line = new ArrayList<>();

    void ReviewList(boolean more, final int userNo, final int reviewNo, String name, String date, String lv, final String contents, final int sideEffectFlag, final String dosePeriod, String birth, int gender, String profilePath, boolean last, String labelName, String labelColor) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.new_review_list_item, null);

        ImageView imgProfile = (ImageView)listView.findViewById(R.id.img_review_list_item_profile);
        ImageView imgShared = (ImageView)listView.findViewById(R.id.img_review_list_item_shared);
        ImageView imgMine = (ImageView)listView.findViewById(R.id.img_review_list_item_mine);
        TextView txtName = (TextView) listView.findViewById(R.id.txt_review_list_item_name);
        TextView txtDate = (TextView) listView.findViewById(R.id.txt_review_list_item_date);
        TextView txtInfo = (TextView) listView.findViewById(R.id.txt_review_list_item_birth_gender);
        TextView txtContents = (TextView) listView.findViewById(R.id.txt_review_list_item_contents);
        TextView txtPeriod = (TextView) listView.findViewById(R.id.txt_review_list_item_period);
        TextView txtEffect = (TextView) listView.findViewById(R.id.txt_review_list_item_effect);
        TextView txtMore = (TextView) listView.findViewById(R.id.txt_review_list_more);
        LinearLayout linDivider = (LinearLayout)listView.findViewById(R.id.lin_review_list_item_divider);
        TextView txtLabel = (TextView)listView.findViewById(R.id.txt_review_list_item_label);
        TextView txtEdit = (TextView)listView.findViewById(R.id.txt_review_list_edit);
        LinearLayout linBg = (LinearLayout)listView.findViewById(R.id.lin_review_list_item_bg);
        ImageView imgMore = (ImageView)listView.findViewById(R.id.img_review_list_item_more);

        if (last){
            linDivider.setVisibility(View.GONE);
        }
        if (profilePath.length() > 0) {
            String replaceText = profilePath;
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            if (profilePath.contains("https:")) {
                Glide.with(MedicineInsertActivity.this).load(replaceText).apply(requestOptions.circleCrop()).into(imgProfile);
            } else {
                Glide.with(MedicineInsertActivity.this).load("https:" + replaceText).apply(requestOptions.circleCrop()).into(imgProfile);
            }
        } else {
            int resource = 0;
            if (gender == 1){
                resource = R.drawable.ic_no_profile_m;
            }else if (gender == 2){
                resource = R.drawable.ic_no_profile_w;
            }else{
                resource = R.drawable.ic_no_profile;
            }
            Glide.with(MedicineInsertActivity.this).load(resource).apply(requestOptions.circleCrop()).into(imgProfile);
        }

        if (labelName.length() != 0){
            txtLabel.setText(labelName);
        }else{
            txtLabel.setVisibility(View.GONE);
        }

        if (labelColor.length() != 0){
            txtLabel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(labelColor)));
        }


        if (userItem.getUserNo() == userNo){
            imgMine.setVisibility(View.VISIBLE);
            imgShared.setVisibility(View.VISIBLE);
            txtEdit.setVisibility(View.VISIBLE);
            linBg.setBackgroundTintList(getColorStateList(R.color.F2F7F5));
            imgMore.setVisibility(View.GONE);
        }else{
            imgMine.setVisibility(View.GONE);
            imgShared.setVisibility(View.GONE);
            txtEdit.setVisibility(View.GONE);
            imgMore.setVisibility(View.VISIBLE);
        }

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new BottomSheetDialog(MedicineInsertActivity.this, R.style.SheetDialog);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentView = inflater.inflate(R.layout.dialog_community_comment_guest_url, null);

                TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_community_guest_cancel);
                TextView txtInquiry = (TextView)contentView.findViewById(R.id.txt_dialog_community_guest_inquiry);

                dialog.setContentView(contentView);

                txtInquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Toast.makeText(MedicineInsertActivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        txtContents.setText(contents);

        if (txtContents.getLineCount() == 0) {
            txtContents.post(() -> {
                int lineCount = txtContents.getLineCount();

                if (lineCount > 3 ){
                    txtMore.setVisibility(View.VISIBLE);
                } else {
                    Log.i(TAG,"line1 : " + txtContents.getLineCount());
                    txtMore.setVisibility(View.GONE);
                    txtContents.setMaxLines(Integer.MAX_VALUE);
                }
            });
        }else {
            if (txtContents.getLineCount() > 3){
                txtMore.setVisibility(View.VISIBLE);
            }else{
                Log.i(TAG,"line2 : " + txtContents.getLineCount());
                txtMore.setVisibility(View.GONE);
                txtContents.setMaxLines(Integer.MAX_VALUE);
            }
        }

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                txtContents.setMaxLines(Integer.MAX_VALUE);
//                v.setVisibility(View.GONE);
                Intent intent = new Intent(MedicineInsertActivity.this,MedicineReviewListActivity.class);
                intent.putExtra("medicineNo",beforeIntent.getIntExtra("medicineNo", 0));
                intent.putExtra("reviewNo",reviewNo);
                startActivityForResult(intent,1111);
            }
        });

        txtContentsList.add(txtContents);

        txtDate.setText(date.substring(2, 10).replace("-","."));
        txtName.setText(name);

//        if ((birth.length() > 0 && gender > 0) && !birth.equals("0") ){
//            if (gender == 1){
//                txtInfo.setText(" ( " + (Integer.parseInt(dateFormat3.format(new Date(System.currentTimeMillis()))) - Integer.parseInt(birth.substring(0, 4)) + 1) + "세 / " + "남" + " )");
//            }else if (gender == 2){
//                txtInfo.setText(" ( " + (Integer.parseInt(dateFormat3.format(new Date(System.currentTimeMillis()))) - Integer.parseInt(birth.substring(0, 4)) + 1) + "세 / " + "여" + " )");
//            }
//        }else if (birth.length() > 0 && !birth.equals("0") ){
//            txtInfo.setText(" ( " + (Integer.parseInt(dateFormat3.format(new Date(System.currentTimeMillis()))) - Integer.parseInt(birth.substring(0, 4)) + 1) + "세 / " + "-" + " )");
//        }else if (gender > 0){
//            if (gender == 1){
//                txtInfo.setText(" ( " + "-" + " / " + "남" + " )");
//            }else if (gender == 2){
//                txtInfo.setText(" ( " + "-" + " / " + "여" + " )");
//            }
//        }

        txtPeriod.setText(dosePeriod);

        String effect = "";
        if (sideEffectFlag == 1) {
            effect = "있었어요";
            txtEffect.setText("있었어요");
        } else if (sideEffectFlag == 2) {
            effect = "없었어요";
            txtEffect.setText("없었어요");
        } else if (sideEffectFlag == 3) {
            effect = "모르겠어요";
            txtEffect.setText("모르겠어요");
        }

//        if (more){
////            setReadMore(txtContents,contents,4);
//        }else{
//            txtContents.setText(contents);
//        }

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MedicineInsertActivity.this, MedicineReviewWriteActivity.class);
                i.putExtra("reviewNo", reviewNo);
                i.putExtra("medicineNo", beforeIntent.getIntExtra("medicineNo", 0));
                i.putExtra("contents", contents);
                i.putExtra("sideEffectFlag", sideEffectFlag);
                i.putExtra("dosePeriod", dosePeriod);
                startActivityForResult(i,1111);
            }
        });

        String finalEffect = effect;
        imgShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MedicineInsertActivity.this, CommunityWriteActivity.class);
                i.putExtra("medicine",true);
                i.putExtra("menuNo","2");
                i.putExtra("cMenuNo","22");
                i.putExtra("menuTitle","후기있숨");
                i.putExtra("cMenuTitle","약 후기");
                i.putExtra("contents","약 : " + txtMedicineName.getText().toString() +"\n복용기간 : " + dosePeriod + "\n" + "부작용여부 : " + finalEffect + "\n\n" + contents);
                startActivityForResult(i,2222);
            }
        });

        linReviewList.addView(listView);
    }

    public static void Twopopup(final Context context) {
        final Dialog dialog = new BottomSheetDialog(context, R.style.SheetDialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_comment_two_btn, null);

        TextView txtDeclaration = (TextView) contentView.findViewById(R.id.txt_dialog_comment_two_declaration);
        TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_comment_two_cancel);

        dialog.setContentView(contentView);

        txtDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(context, InquiryActivity.class);
                i.putExtra("inquiry", "inquiry");
                context.startActivity(i);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(MEDICINE_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),"" + beforeIntent.getIntExtra("medicineNo", 0),
                    "" + frequency,edtAmount.getText().toString(),txtUnit.getText().toString(),txtStartDt.getText().toString().replace(".", ""),"21201230");
        }else if (mCode.equals(MEDICINE_UPDATE)){
            if (shwEndVisible.isChecked()){
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+beforeIntent.getIntExtra("historyNo", 0),"" + beforeIntent.getIntExtra("medicineNo", 0),
                        "" + frequency,edtAmount.getText().toString(),txtUnit.getText().toString(),txtStartDt.getText().toString().replace(".", ""),txtEndDt.getText().toString().replace(".", ""));
            }else{
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+beforeIntent.getIntExtra("historyNo", 0),"" + beforeIntent.getIntExtra("medicineNo", 0),
                        "" + frequency,edtAmount.getText().toString(),txtUnit.getText().toString(),txtStartDt.getText().toString().replace(".", ""),"21201230");
            }

        }else if (mCode.equals(MEDICINE_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+beforeIntent.getIntExtra("historyNo", 0),"" + Utils.userItem.getUserNo());
        }else if (mCode.equals(MEDICINE_REVIEW_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute("" + beforeIntent.getIntExtra("medicineNo", 0));
        }else if (mCode.equals(MEDICINE_REVIEW_LIST_ONRESUME)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute("" + beforeIntent.getIntExtra("medicineNo", 0));
        }else if (mCode.equals(MEDICINE_HISTORY_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo());
        }else if (mCode.equals(MEDICINE_HISTORY_LIST_CHECK)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo());
        }else if (mCode.equals(MEDICINE_CALL_DETAIL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+beforeIntent.getIntExtra("medicineNo",0));
        }else if (mCode.equals(REVIEW_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+mReviewNo,""+beforeIntent.getIntExtra("medicineNo", 0));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(MEDICINE_INSERT)){
                editor.putInt("medicineRefresh",1);
                editor.apply();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        twoButtonDialog = new TwoButtonDialog(MedicineInsertActivity.this, "추가 완료!", beforeIntent.getStringExtra("medicineKo") + "가\n현재 복용중인 약에 추가되었습니다.\n약을 더 추가 하시겠습니까?", "아니요", "더 추가하기");
                        twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                            @Override
                            public void CancelButtonClick(View v) {
                                Intent i = new Intent(MedicineInsertActivity.this, MainActivity.class);
                                if (beforeIntent.hasExtra("newbieCheck")){
                                    if (beforeIntent.getBooleanExtra("newbieCheck",false)){
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    }else{
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    }
                                }else{
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                i.putExtra("medicineInsert", true);
                                setResult(RESULT_OK, i);
                                startActivity(i);
                                btnFinish.setClickable(true);
                                finish();
                            }
                        });
                        twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                            @Override
                            public void OkButtonClick(View v) {
                                NetworkCall(MEDICINE_HISTORY_LIST);
                            }
                        });
                    }
                }, 500);
            }else if (mCode.equals(MEDICINE_UPDATE)){
                editor.putInt("medicineRefresh",1);
                editor.apply();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!shwEndVisible.isChecked()){
                            oneButtonDialog = new OneButtonDialog(MedicineInsertActivity.this, "복약 정보", "수정완료", "확인");
                        }else{
                            oneButtonDialog = new OneButtonDialog(MedicineInsertActivity.this, "복용 끝", "끝! 수고하셨어요.\n"+ txtMedicineName.getText().toString() +"\n복용 종료로 옮겼습니다.", "확인");
                        }
                        oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                Intent i = null;
                                if (beforeIntent.hasExtra("complete")){
                                    i = new Intent(MedicineInsertActivity.this, DrugCompleteListActivity.class);
                                }else{
                                    if (!shwEndVisible.isChecked()){
                                        i = new Intent(MedicineInsertActivity.this, MainActivity.class);
                                    }else{
                                        i = new Intent(MedicineInsertActivity.this, DrugCompleteListActivity.class);
                                        i.putExtra("medicineRefresh",true);
                                    }
                                }

                                if (beforeIntent.hasExtra("newbieCheck")){
                                    if (beforeIntent.getBooleanExtra("newbieCheck",false)){
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    }else{
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    }
                                }else{
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }
                                i.putExtra("medicineInsert", true);
                                setResult(RESULT_OK, i);
                                startActivity(i);
                                btnFinish.setClickable(true);
                                finish();
                            }
                        });
                    }
                }, 500);
            }else if (mCode.equals(MEDICINE_DELETE)){
                editor.putInt("medicineRefresh",1);
                editor.apply();
                Intent i = null;
                if (beforeIntent.hasExtra("complete")){
                    i = new Intent(MedicineInsertActivity.this, DrugCompleteListActivity.class);
                }else{
                    i = new Intent(MedicineInsertActivity.this, MainActivity.class);
                }
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("medicineInsert", true);
                setResult(RESULT_OK, i);
                startActivity(i);
                btnFinish.setClickable(true);
                finish();
            }else if (mCode.equals(MEDICINE_REVIEW_LIST)){
                userNoList = new ArrayList<>();
                reviewNoList = new ArrayList<>();
                effectList = new ArrayList<>();
                nicknameList = new ArrayList<>();
                dateList = new ArrayList<>();
                lvList = new ArrayList<>();
                periodList = new ArrayList<>();
                contentsList = new ArrayList<>();
                birthList = new ArrayList<>();
                profilePathList = new ArrayList<>();
                labelNameList = new ArrayList<>();
                labelColorList = new ArrayList<>();

                linReviewList.removeAllViews();

                if (moreTextCheck){
                    moreTextCheck = false;
                }

                try {
                    JSONObject jsonObject = new JSONObject(mResult);

                    if (jsonObject.has("list")) {
                        linReviewNoList.setVisibility(View.GONE);
                        linReviewList.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Log.i(TAG,"review : " + object.toString());
                            JSONObject userObject = object.getJSONObject("clsUserBean");
                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                                userNoList.add(JsonIntIsNullCheck(object, "USER_NO"));
                                reviewNoList.add(JsonIntIsNullCheck(object, "REVIEW_NO"));
                                effectList.add(JsonIntIsNullCheck(object, "SIDE_EFFECT_FLAG"));
                                nicknameList.add(JsonIsNullCheck(object, "NICKNAME"));
                                dateList.add(JsonIsNullCheck(object, "UPDATE_DT").length() != 0 ? JsonIsNullCheck(object, "UPDATE_DT") : JsonIsNullCheck(object, "CREATE_DT"));
                                lvList.add(JsonIsNullCheck(object, "LV"));
                                periodList.add(JsonIsNullCheck(object, "DOSE_PERIOD"));
                                contentsList.add(JsonIsNullCheck(object, "CONTENTS"));
                                birthList.add(JsonIsNullCheck(object, "BIRTH"));
                                genderList.add(JsonIntIsNullCheck(object,"GENDER"));
                                profilePathList.add(JsonIsNullCheck(object,"PROFILE_IMG"));
                                labelNameList.add(JsonIsNullCheck(object,"LABEL_NAME"));
                                labelColorList.add(JsonIsNullCheck(object,"LABEL_COLOR"));
                            }
                        }
                        if (jsonArray.length() > 4) {
                            linReviewMore.setVisibility(View.VISIBLE);
                            if (beforeIntent.hasExtra("review")){
                                btnFinish.setVisibility(View.GONE);
                                for (int i = 0; i < 4; i++) {
                                    ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                            nicknameList.get(i), dateList.get(i),
                                            lvList.get(i), contentsList.get(i),
                                            effectList.get(i), periodList.get(i),
                                            birthList.get(i),genderList.get(i),profilePathList.get(i), i == 3 ? true : false, labelNameList.get(i), labelColorList.get(i));
                                }
                            }
                        } else {
                            linReviewMore.setVisibility(View.GONE);
                            if (beforeIntent.hasExtra("review")){
                                btnFinish.setVisibility(View.GONE);
                                for (int i = 0; i < userNoList.size(); i++) {
                                    ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                            nicknameList.get(i), dateList.get(i),
                                            lvList.get(i), contentsList.get(i),
                                            effectList.get(i), periodList.get(i),
                                            birthList.get(i),genderList.get(i),profilePathList.get(i), i == userNoList.size()-1 ? true : false, labelNameList.get(i), labelColorList.get(i));
                                }
                            }
                        }

                        if (userNoList.size() == 0){
                            linReviewNoList.setVisibility(View.VISIBLE);
                            linReviewList.setVisibility(View.GONE);
                        }
                    } else {
                        linReviewNoList.setVisibility(View.VISIBLE);
                        linReviewList.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {

                }
            }else if (mCode.equals(MEDICINE_REVIEW_LIST_ONRESUME)){
                userNoList = new ArrayList<>();
                reviewNoList = new ArrayList<>();
                effectList = new ArrayList<>();
                nicknameList = new ArrayList<>();
                dateList = new ArrayList<>();
                lvList = new ArrayList<>();
                periodList = new ArrayList<>();
                contentsList = new ArrayList<>();
                birthList = new ArrayList<>();
                profilePathList = new ArrayList<>();
                labelNameList = new ArrayList<>();
                labelColorList = new ArrayList<>();

                linReviewList.removeAllViews();

                if (moreTextCheck){
                    moreTextCheck = false;
                }

                try {
                    JSONObject jsonObject = new JSONObject(mResult);

                    if (jsonObject.has("list")) {
                        linReviewNoList.setVisibility(View.GONE);
                        linReviewList.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                                userNoList.add(JsonIntIsNullCheck(object, "USER_NO"));
                                reviewNoList.add(JsonIntIsNullCheck(object, "REVIEW_NO"));
                                effectList.add(JsonIntIsNullCheck(object, "SIDE_EFFECT_FLAG"));
                                nicknameList.add(JsonIsNullCheck(object, "NICKNAME"));
                                dateList.add(JsonIsNullCheck(object, "UPDATE_DT").length() != 0 ? JsonIsNullCheck(object, "UPDATE_DT") : JsonIsNullCheck(object, "CREATE_DT"));
                                lvList.add(JsonIsNullCheck(object, "LV"));
                                periodList.add(JsonIsNullCheck(object, "DOSE_PERIOD"));
                                contentsList.add(JsonIsNullCheck(object, "CONTENTS"));
                                birthList.add(JsonIsNullCheck(object, "BIRTH"));
                                genderList.add(JsonIntIsNullCheck(object,"GENDER"));
                                profilePathList.add(JsonIsNullCheck(object,"PROFILE_IMG"));
                                labelNameList.add(JsonIsNullCheck(object,"LABEL_NAME"));
                                labelColorList.add(JsonIsNullCheck(object,"LABEL_COLOR"));
                            }
                        }
                        if (jsonArray.length() > 4) {
                            linReviewMore.setVisibility(View.VISIBLE);
                            for (int i = 0; i < 4; i++) {
                                ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                        nicknameList.get(i), dateList.get(i),
                                        lvList.get(i), contentsList.get(i),
                                        effectList.get(i), periodList.get(i),
                                        birthList.get(i),genderList.get(i),profilePathList.get(i),i == 3 ? true : false, labelNameList.get(i), labelColorList.get(i));
                            }
                        } else {
                            linReviewMore.setVisibility(View.GONE);
                            for (int i = 0; i < userNoList.size(); i++) {
                                ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                        nicknameList.get(i), dateList.get(i),
                                        lvList.get(i), contentsList.get(i),
                                        effectList.get(i), periodList.get(i),
                                        birthList.get(i),genderList.get(i),profilePathList.get(i),i == userNoList.size()-1 ? true : false, labelNameList.get(i), labelColorList.get(i));
                            }
                        }

                        if (userNoList.size() == 0){
                            linReviewNoList.setVisibility(View.VISIBLE);
                            linReviewList.setVisibility(View.GONE);
                        }
                    } else {
                        linReviewNoList.setVisibility(View.VISIBLE);
                        linReviewList.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {

                }
            }else if (mCode.equals(MEDICINE_HISTORY_LIST)){
                MedicineTakingItems = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    Log.i(TAG,"MEDICINE_HISTORY_LIST : " + jsonObject);
                    if (jsonObject.has("list")){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            MedicineTakingItem medicineTakingItem = new MedicineTakingItem();
                            medicineTakingItem.setMedicineHistoryNo(JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"));
                            medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                            medicineTakingItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                            medicineTakingItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                            medicineTakingItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                            medicineTakingItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                            medicineTakingItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                            medicineTakingItem.setAliveFlag(JsonIntIsNullCheck(object,"ALIVE_FLAG"));
                            medicineTakingItem.setCreateDt(JsonIsNullCheck(object,"CREATE_DT"));
                            medicineTakingItem.setUpdateDt(JsonIsNullCheck(object,"UPDATE_DT"));

                            //clsMedicineBean
                            medicineTakingItem.setMedicineKo(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"KO"));
                            medicineTakingItem.setMedicineEn(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"EN"));
                            medicineTakingItem.setManufacturer(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"MANUFACTURER"));
                            medicineTakingItem.setIngredient(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"INGREDIENT"));
                            medicineTakingItem.setForm(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"FORM"));
                            medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"),"MEDICINE_TYPE_NO"));
                            medicineTakingItem.setStorageMethod(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"STORAGE_METHOD"));
                            medicineTakingItem.setEfficacy(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"EFFICACY"));
                            medicineTakingItem.setInformation(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"INFORMATION"));
                            medicineTakingItem.setStabiltyRationg(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"STABILITY_RATING"));
                            medicineTakingItem.setPrecaution(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"PRECAUTIONS"));
                            medicineTakingItem.setBookMark(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"),"BOOKMARK"));
                            medicineTakingItem.setMedicineImg(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"MEDICINE_IMG"));
                            if (Integer.parseInt(dateFormat1.format(new Date(System.currentTimeMillis()))) < JsonIntIsNullCheck(object,"END_DT")){
                                medicineTakingItem.setInsertCheck(1);
                            }
                            Log.i(TAG,"JsonIsNullCheck(object,\"END_DT\") : " + JsonIsNullCheck(object,"END_DT"));
                            MedicineTakingItems.add(medicineTakingItem);
                        }
                    }

                    btnFinish.setClickable(true);
                    Intent i = new Intent(MedicineInsertActivity.this, MedicineSelectActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("medicineInsert", true);
                    startActivity(i);
                    finish();

                }catch (JSONException e){

                }
            }else if (mCode.equals(MEDICINE_HISTORY_LIST_CHECK)){
                MedicineTakingItems = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    Log.i(TAG,"MEDICINE_HISTORY_LIST_CHECK : " + jsonObject);
                    if (jsonObject.has("list")){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            MedicineTakingItem medicineTakingItem = new MedicineTakingItem();
                            medicineTakingItem.setMedicineHistoryNo(JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"));
                            medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                            medicineTakingItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                            medicineTakingItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                            medicineTakingItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                            medicineTakingItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                            medicineTakingItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                            medicineTakingItem.setAliveFlag(JsonIntIsNullCheck(object,"ALIVE_FLAG"));
                            medicineTakingItem.setCreateDt(JsonIsNullCheck(object,"CREATE_DT"));
                            medicineTakingItem.setUpdateDt(JsonIsNullCheck(object,"UPDATE_DT"));

                            //clsMedicineBean
                            medicineTakingItem.setMedicineKo(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"KO"));
                            medicineTakingItem.setMedicineEn(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"EN"));
                            medicineTakingItem.setManufacturer(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"MANUFACTURER"));
                            medicineTakingItem.setIngredient(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"INGREDIENT"));
                            medicineTakingItem.setForm(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"FORM"));
                            medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"),"MEDICINE_TYPE_NO"));
                            medicineTakingItem.setStorageMethod(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"STORAGE_METHOD"));
                            medicineTakingItem.setEfficacy(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"EFFICACY"));
                            medicineTakingItem.setInformation(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"INFORMATION"));
                            medicineTakingItem.setStabiltyRationg(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"STABILITY_RATING"));
                            medicineTakingItem.setPrecaution(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"PRECAUTIONS"));
                            medicineTakingItem.setBookMark(JsonIntIsNullCheck(object.getJSONObject("clsMedicineBean"),"BOOKMARK"));
                            medicineTakingItem.setMedicineImg(JsonIsNullCheck(object.getJSONObject("clsMedicineBean"),"MEDICINE_IMG"));

                            Log.i(TAG,"JsonIsNullCheck(object,\"END_DT\") : " + JsonIsNullCheck(object,"END_DT"));
                            if (Integer.parseInt(dateFormat1.format(new Date(System.currentTimeMillis()))) < JsonIntIsNullCheck(object,"END_DT")){
                                medicineTakingItem.setInsertCheck(1);
                            }
                            MedicineTakingItems.add(medicineTakingItem);
                        }
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(MEDICINE_CALL_DETAIL)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.has("list")){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            txtMedicineName.setText(JsonIsNullCheck(object,"KO"));
                            if (JsonIsNullCheck(object,"MEDICINE_IMG").length() > 0) {
                                String replaceText = JsonIsNullCheck(object,"MEDICINE_IMG");
                                if (replaceText.contains("soom2.testserver-1.com:8080")){
                                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                                }else if (replaceText.contains("103.55.190.193")){
                                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                                }
                                Glide.with(MedicineInsertActivity.this).load("https:" + replaceText).into(imgMedicine);
                            }
                            txtEfficacy.setText(JsonIsNullCheck(object,"EFFICACY"));
                            txtInstructions.setText(JsonIsNullCheck(object,"INSTRUCTIONS"));
                            txtInformation.setText(JsonIsNullCheck(object,"INFORMATION"));
                            txtStability.setText(JsonIsNullCheck(object,"STABILITY_RATING"));
                            txtPrecautions.setText(JsonIsNullCheck(object,"PRECAUTIONS"));
                        }
                    }

                    if (beforeIntent.hasExtra("review")) {
                        linSettingVisible.setVisibility(View.GONE);
                        linInfoVisible.setVisibility(View.GONE);
                        linReviewVisible.setVisibility(View.VISIBLE);

                        txtSetting.setTypeface(typefaceMedium);
                        txtInfo.setTypeface(typefaceMedium);
                        txtReview.setTypeface(typefaceBold);

                        txtSetting.setTextColor(getResources().getColor(R.color.color5c5c5c));
                        txtInfo.setTextColor(getResources().getColor(R.color.color5c5c5c));
                        txtReview.setTextColor(getResources().getColor(R.color.color343434));

                        linSettingLine.setVisibility(View.INVISIBLE);
                        linInfoLine.setVisibility(View.INVISIBLE);
                        linReviewLine.setVisibility(View.VISIBLE);
                    }

                    NetworkCall(MEDICINE_HISTORY_LIST_CHECK);
                }catch (JSONException e){

                }
            }else if (mCode.equals(REVIEW_DELETE)){
                linReviewList.removeView(mReviewRemoveView);
                if (linReviewList.getChildCount() == 0){
                    linReviewNoList.setVisibility(View.VISIBLE);
                    linReviewList.setVisibility(View.GONE);
                }
            }
        }
    }

    public static void setReadMore(final TextView view, final String text, final int maxLine) {
        final Context context = view.getContext();
        final String expanedText = "···더보기";

        if (view.getTag() != null && view.getTag().equals(text)) { //Tag로 전값 의 text를 비교하여똑같으면 실행하지 않음.
            return;
        }
        view.setTag(text); //Tag에 text 저장
        view.setText(text); // setText를 미리 하셔야  getLineCount()를 호출가능

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getLineCount() == maxLine){

                }else if (view.getLineCount() > maxLine) { //Line Count가 설정한 MaxLine의 값보다 크다면 처리시작

                    int lineEndIndex = view.getLayout().getLineVisibleEnd(maxLine - 1); //Max Line 까지의 text length

                    String[] split = text.split("\n"); //text를 자름
                    int splitLength = 0;

                    String lessText = "";
                    for (String item : split) {
                        splitLength += item.length() + 1;
                        if (splitLength >= lineEndIndex) { //마지막 줄일때!
                            if (item.length() >= expanedText.length()) {
                                lessText += item.substring(0, item.length() - (expanedText.length())) + expanedText;
                            } else {
                                lessText += item + expanedText;
                            }
                            break; //종료
                        }
                        lessText += item + "\n";
                    }
                    SpannableString spannableString = new SpannableString(lessText);
                    spannableString.setSpan(new ClickableSpan() {//클릭이벤트
                        @Override
                        public void onClick(View v) {
                            view.setText(text);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) { //컬러 처리
                            ds.setColor(ContextCompat.getColor(context, R.color.black));
                            ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        }
                    }, spannableString.length() - expanedText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.setText(spannableString);
                    view.setMovementMethod(LinkMovementMethod.getInstance());
                }
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (mSaveBeforeVolume.equals(edtAmount.getText().toString()) && mSaveBeforeUnit.equals(txtUnit.getText().toString()) &&
                mSaveBeforeCount.equals(txtCount.getText().toString()) && mSaveBeforeStartDt.equals(txtStartDt.getText().toString()) &&
                mSaveBeforeEndDt.equals(txtEndDt.getText().toString())){
            finish();
        }else{
            twoButtonDialog = new TwoButtonDialog(MedicineInsertActivity.this,"약 관리", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
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
        switch (v.getId()) {
            case R.id.txt_medicine_insert_back: {
                onBackPressed();
                break;
            }
            case R.id.img_medicine_insert_remove: {
                String contents = "";
                if (beforeIntent.hasExtra("complete")){
                    contents = "복용 종료된 약을 삭제하시겠습니까?";
                }else{
                    contents = "복용중인 약을 삭제하시겠습니까?";
                }
                twoButtonDialog = new TwoButtonDialog(this, "약 삭제", contents, "취소", "삭제");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        NetworkCall(MEDICINE_DELETE);
                    }
                });
                break;
            }
            case R.id.txt_medicine_insert_first: {

                btnFinish.setVisibility(View.VISIBLE);

                linSettingVisible.setVisibility(View.VISIBLE);
                linInfoVisible.setVisibility(View.GONE);
                linReviewVisible.setVisibility(View.GONE);

                txtSetting.setTypeface(typefaceBold);
                txtInfo.setTypeface(typefaceMedium);
                txtReview.setTypeface(typefaceMedium);

                txtSetting.setTextColor(getResources().getColor(R.color.color343434));
                txtInfo.setTextColor(getResources().getColor(R.color.color5c5c5c));
                txtReview.setTextColor(getResources().getColor(R.color.color5c5c5c));

                linSettingLine.setVisibility(View.VISIBLE);
                linInfoLine.setVisibility(View.INVISIBLE);
                linReviewLine.setVisibility(View.INVISIBLE);

                break;
            }
            case R.id.txt_medicine_insert_second: {

                if (mSaveBeforeVolume.equals(edtAmount.getText().toString()) && mSaveBeforeUnit.equals(txtUnit.getText().toString()) &&
                        mSaveBeforeCount.equals(txtCount.getText().toString()) && mSaveBeforeStartDt.equals(txtStartDt.getText().toString()) && mSaveBeforeEndDt.equals(txtEndDt.getText().toString()) && complete == 1){
                    linSettingVisible.setVisibility(View.GONE);
                    linInfoVisible.setVisibility(View.VISIBLE);
                    linReviewVisible.setVisibility(View.GONE);

                    txtSetting.setTypeface(typefaceMedium);
                    txtInfo.setTypeface(typefaceBold);
                    txtReview.setTypeface(typefaceMedium);

                    txtSetting.setTextColor(getResources().getColor(R.color.color5c5c5c));
                    txtInfo.setTextColor(getResources().getColor(R.color.color343434));
                    txtReview.setTextColor(getResources().getColor(R.color.color5c5c5c));

                    linSettingLine.setVisibility(View.INVISIBLE);
                    linInfoLine.setVisibility(View.VISIBLE);
                    linReviewLine.setVisibility(View.INVISIBLE);

                    btnFinish.setVisibility(View.GONE);
                }else{
                    twoButtonDialog = new TwoButtonDialog(MedicineInsertActivity.this,"약 관리", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
                    twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                        @Override
                        public void OkButtonClick(View v) {
                            twoButtonDialog.dismiss();
                            linSettingVisible.setVisibility(View.GONE);
                            linInfoVisible.setVisibility(View.VISIBLE);
                            linReviewVisible.setVisibility(View.GONE);
                            shwEndVisible.setChecked(false);

                            btnFinish.setVisibility(View.GONE);

                            txtSetting.setTypeface(typefaceMedium);
                            txtInfo.setTypeface(typefaceBold);
                            txtReview.setTypeface(typefaceMedium);

                            txtSetting.setTextColor(getResources().getColor(R.color.color5c5c5c));
                            txtInfo.setTextColor(getResources().getColor(R.color.color343434));
                            txtReview.setTextColor(getResources().getColor(R.color.color5c5c5c));

                            linSettingLine.setVisibility(View.INVISIBLE);
                            linInfoLine.setVisibility(View.VISIBLE);
                            linReviewLine.setVisibility(View.INVISIBLE);

                            edtAmount.setText(mSaveBeforeVolume);
                            txtUnit.setText(mSaveBeforeUnit);
                            txtCount.setText(mSaveBeforeCount);
                            txtStartDt.setText(mSaveBeforeStartDt);
                            txtEndDt.setText(mSaveBeforeEndDt);
                            btnFinish.setVisibility(View.GONE);
                        }
                    });

                    twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                        @Override
                        public void CancelButtonClick(View v) {
                            twoButtonDialog.dismiss();
                            btnFinish.setVisibility(View.VISIBLE);
                        }
                    });
                }

                break;
            }
            case R.id.txt_medicine_insert_third: {

                if (mSaveBeforeVolume.equals(edtAmount.getText().toString()) && mSaveBeforeUnit.equals(txtUnit.getText().toString()) &&
                        mSaveBeforeCount.equals(txtCount.getText().toString()) && mSaveBeforeStartDt.equals(txtStartDt.getText().toString()) && mSaveBeforeEndDt.equals(txtEndDt.getText().toString()) && complete == 1){
                    linSettingVisible.setVisibility(View.GONE);
                    linInfoVisible.setVisibility(View.GONE);
                    linReviewVisible.setVisibility(View.VISIBLE);

                    txtSetting.setTypeface(typefaceMedium);
                    txtInfo.setTypeface(typefaceMedium);
                    txtReview.setTypeface(typefaceBold);

                    txtSetting.setTextColor(getResources().getColor(R.color.color5c5c5c));
                    txtInfo.setTextColor(getResources().getColor(R.color.color5c5c5c));
                    txtReview.setTextColor(getResources().getColor(R.color.color343434));

                    linSettingLine.setVisibility(View.INVISIBLE);
                    linInfoLine.setVisibility(View.INVISIBLE);
                    linReviewLine.setVisibility(View.VISIBLE);

                    btnFinish.setVisibility(View.GONE);

                    if (!beforeIntent.hasExtra("review")){
                        if (userNoList.size() > 4){
                            for (int i = 0; i < 4; i++) {
                                ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                        nicknameList.get(i), dateList.get(i),
                                        lvList.get(i), contentsList.get(i),
                                        effectList.get(i), periodList.get(i),
                                        birthList.get(i),genderList.get(i),profilePathList.get(i),i == 3 ? true : false, labelNameList.get(i), labelColorList.get(i));
                            }
                        }else{
                            if (linReviewList.getChildCount() == 0){
                                for (int i = 0; i < userNoList.size(); i++) {
                                    ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                            nicknameList.get(i), dateList.get(i),
                                            lvList.get(i), contentsList.get(i),
                                            effectList.get(i), periodList.get(i),
                                            birthList.get(i),genderList.get(i),profilePathList.get(i),i == userNoList.size()-1 ? true : false, labelNameList.get(i), labelColorList.get(i));
                                }
                            }
                        }
                    }
                }else{
                    twoButtonDialog = new TwoButtonDialog(MedicineInsertActivity.this,"약 관리", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
                    twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                        @Override
                        public void OkButtonClick(View v) {
                            twoButtonDialog.dismiss();
                            linSettingVisible.setVisibility(View.GONE);
                            linInfoVisible.setVisibility(View.GONE);
                            linReviewVisible.setVisibility(View.VISIBLE);
                            shwEndVisible.setChecked(false);
                            txtSetting.setTypeface(typefaceMedium);
                            txtInfo.setTypeface(typefaceMedium);
                            txtReview.setTypeface(typefaceBold);

                            txtSetting.setTextColor(getResources().getColor(R.color.color5c5c5c));
                            txtInfo.setTextColor(getResources().getColor(R.color.color5c5c5c));
                            txtReview.setTextColor(getResources().getColor(R.color.color343434));

                            linSettingLine.setVisibility(View.INVISIBLE);
                            linInfoLine.setVisibility(View.INVISIBLE);
                            linReviewLine.setVisibility(View.VISIBLE);

                            btnFinish.setVisibility(View.GONE);

                            if (!beforeIntent.hasExtra("review")){
                                if (userNoList.size() > 4){
                                    for (int i = 0; i < 4; i++) {
                                        ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                                nicknameList.get(i), dateList.get(i),
                                                lvList.get(i), contentsList.get(i),
                                                effectList.get(i), periodList.get(i),
                                                birthList.get(i),genderList.get(i),profilePathList.get(i),i == 3 ? true : false, labelNameList.get(i), labelColorList.get(i));
                                    }
                                }else{
                                    if (linReviewList.getChildCount() == 0){
                                        for (int i = 0; i < userNoList.size(); i++) {
                                            ReviewList(false,userNoList.get(i), reviewNoList.get(i),
                                                    nicknameList.get(i), dateList.get(i),
                                                    lvList.get(i), contentsList.get(i),
                                                    effectList.get(i), periodList.get(i),
                                                    birthList.get(i),genderList.get(i),profilePathList.get(i),i == userNoList.size()-1 ? true : false, labelNameList.get(i), labelColorList.get(i));
                                        }
                                    }
                                }
                            }

                            edtAmount.setText(mSaveBeforeVolume);
                            txtUnit.setText(mSaveBeforeUnit);
                            txtCount.setText(mSaveBeforeCount);
                            txtStartDt.setText(mSaveBeforeStartDt);
                            txtEndDt.setText(mSaveBeforeEndDt);
                        }
                    });

                    twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                        @Override
                        public void CancelButtonClick(View v) {
                            twoButtonDialog.dismiss();
                            btnFinish.setVisibility(View.VISIBLE);
                        }
                    });
                }

                break;
            }
            case R.id.txt_medicine_insert_first_unit: {
                DayCntPicker(1, "투약 단위 설정");
                break;
            }
            case R.id.txt_medicine_insert_first_cnt: {
                DayCntPicker(2, "일일 투약 횟수 설정");
                break;
            }
            case R.id.txt_medicine_insert_first_start_dt: {
                DateTimePicker(1, "투약 시작일");
                break;
            }
            case R.id.txt_medicine_insert_first_end_dt: {
                if (txtStartDt.getText().length() == 0) {
                    new OneButtonDialog(this, "투약 종료일", "시작일을 먼저 선택해주세요.", "확인");
                } else {
                    DateTimePicker(2, "투약 종료일");
                }
                break;
            }
            case R.id.img_medicine_insert_question : {
                if (linBallonVisible.getVisibility() == View.GONE){
                    linBallonVisible.setVisibility(View.VISIBLE);
                }else{
                    linBallonVisible.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.lin_medicine_insert_third_review_insert: {
                Intent i = new Intent(this, MedicineReviewWriteActivity.class);
                i.putExtra("medicineNo", beforeIntent.getIntExtra("medicineNo", 0));
                startActivityForResult(i,1111);
                break;
            }
            case R.id.btn_medicine_insert_finish: {

                if (beforeIntent.hasExtra("historyNo")){
                    if (mSaveBeforeVolume.equals(edtAmount.getText().toString()) && mSaveBeforeUnit.equals(txtUnit.getText().toString()) &&
                            mSaveBeforeCount.equals(txtCount.getText().toString()) && mSaveBeforeStartDt.equals(txtStartDt.getText().toString()) &&
                            mSaveBeforeEndDt.equals(txtEndDt.getText().toString()) && complete == 1){

                    }else{
                        if (!edtAmount.getText().toString().equals("0") && edtAmount.getText().length() != 0 && txtUnit.getText().length() != 0 && txtStartDt.getText().length() != 0 && txtCount.getText().length() != 0) {
                            btnFinish.setClickable(true);
                            NetworkCall(MEDICINE_UPDATE);
                        } else {
                            if (edtAmount.getText().toString().equals("0")){
                                new OneButtonDialog(this, "약 수정", "1회 복용량을 확인해주세요.", "확인");
                            }else if (edtAmount.getText().length() == 0){
                                new OneButtonDialog(this, "약 수정", "1회 복용량을 확인해주세요.", "확인");
                            }else if (txtUnit.getText().length() == 0){
                                new OneButtonDialog(this, "약 수정", "투약 단위를 확인해주세요.", "확인");
                            }else if (txtCount.getText().length() == 0){
                                new OneButtonDialog(this, "약 수정", "일일 복용횟수를 확인해주세요.", "확인");
                            }else if (txtStartDt.getText().length() == 0){
                                new OneButtonDialog(this, "약 수정", "시작일을 확인해주세요.", "확인");
                            }
                            btnFinish.setClickable(true);
                        }
                    }
                }else{
                    if (!edtAmount.getText().toString().equals("0") && edtAmount.getText().length() != 0 && txtUnit.getText().length() != 0 && txtStartDt.getText().length() != 0 && txtCount.getText().length() != 0) {
                        btnFinish.setClickable(true);
                        boolean check = false;
                        if (MedicineTakingItems != null) {
                            for (int x = 0; x < MedicineTakingItems.size(); x++) {
                                if (MedicineTakingItems.get(x).getMedicineNo() == beforeIntent.getIntExtra("medicineNo", 0) && MedicineTakingItems.get(x).getInsertCheck() == 1) {
                                    check = true;
                                    break;
                                } else {
                                    check = false;
                                }
                            }
                        }
                        if (check) {
                            new OneButtonDialog(MedicineInsertActivity.this, "약 등록", "이미 추가된 약입니다.", "확인");
                        } else {
                            NetworkCall(MEDICINE_INSERT);
                        }
                    } else {
                        if (edtAmount.getText().toString().equals("0")){
                            new OneButtonDialog(this, "약 등록", "1회 복용량을 확인해주세요.", "확인");
                        }else if (edtAmount.getText().length() == 0){
                            new OneButtonDialog(this, "약 등록", "1회 복용량을 확인해주세요.", "확인");
                        }else if (txtUnit.getText().length() == 0){
                            new OneButtonDialog(this, "약 등록", "투약 단위를 확인해주세요.", "확인");
                        }else if (txtCount.getText().length() == 0){
                            new OneButtonDialog(this, "약 등록", "일일 복용횟수를 확인해주세요.", "확인");
                        }else if (txtStartDt.getText().length() == 0){
                            new OneButtonDialog(this, "약 등록", "시작일을 확인해주세요.", "확인");
                        }
                        btnFinish.setClickable(true);
                    }
                }

                break;
            }
            case R.id.lin_medicine_insert_review_more: {
                Intent intent = new Intent(this,MedicineReviewListActivity.class);
                intent.putExtra("medicineNo",beforeIntent.getIntExtra("medicineNo", 0));
                intent.putExtra("medicineName",txtMedicineName.getText().toString());
                startActivityForResult(intent,1111);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111){
            if (resultCode == RESULT_OK){
                Log.i(TAG,"aaaaaaaaaaa");
                NetworkCall(MEDICINE_REVIEW_LIST_ONRESUME);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
