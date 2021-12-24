package com.kmw.soom2.Home.Activitys.MedicineInsertActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MemoActivity;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.REVIEW_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.REVIEW_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.REVIEW_UPDATE;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class MedicineReviewWriteActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AsyncResponse {

    String TAG = "MedicineReviewActivity";
    ImageView imgBack;
    TextView txtTerm;
    RadioButton btnFirst,btnSecond,btnThird;
    EditText edtContents;
    Button btnSave;
    TextView txtTitle;
    ImageView imgRemove;
    TextView txtContentsLength;
    ProgressDialog progressDialog;
    ScrollView scrollView;

    String[] termStrings = new String[]{"2주 이내","~1달","~3달","6달 이상"};

    Intent beforeIntent;
    Typeface fontBold;
    Typeface fontMedium;
    int sideEffectFlag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_review);

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

        fontBold = ResourcesCompat.getFont(this, R.font.notosanscjkkr_bold);
        fontMedium = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("reviewNo")){
                txtTitle.setText("약 후기 수정");
                imgRemove.setVisibility(View.VISIBLE);

                txtTerm.setText(beforeIntent.getStringExtra("dosePeriod"));
                txtTerm.setTextColor(getResources().getColor(R.color.color09D182));

                if (beforeIntent.getIntExtra("sideEffectFlag",0) == 1){
                    btnFirst.setChecked(true);
                    sideEffectFlag = 1;
                }else if (beforeIntent.getIntExtra("sideEffectFlag",0) == 2){
                    btnSecond.setChecked(true);
                    sideEffectFlag = 2;
                }else if (beforeIntent.getIntExtra("sideEffectFlag",0) == 3){
                    btnThird.setChecked(true);
                    sideEffectFlag = 3;
                }

                edtContents.setHint("");
                edtContents.setText(beforeIntent.getStringExtra("contents"));
            }else{
//                txtTerm.setText("2주 이내");
//                btnFirst.setChecked(true);
//                sideEffectFlag = 1;
            }
        }else{
//            txtTerm.setText("2주 이내");
//            btnFirst.setChecked(true);
//            sideEffectFlag = 1;
        }
    }

    void FindViewById(){
        imgBack = (ImageView)findViewById(R.id.img_medicine_review_insert_back);
        txtTitle = (TextView)findViewById(R.id.txt_review_title);
        imgRemove = (ImageView)findViewById(R.id.img_review_remove);
        txtTerm = (TextView)findViewById(R.id.txt_medicine_review_insert_term);
        btnFirst = (RadioButton)findViewById(R.id.rdo_medicine_review_insert_first);
        btnSecond = (RadioButton)findViewById(R.id.rdo_medicine_review_insert_second);
        btnThird = (RadioButton)findViewById(R.id.rdo_medicine_review_insert_third);
        edtContents = (EditText)findViewById(R.id.edt_medicine_review_insert_contents);
        btnSave = (Button)findViewById(R.id.btn_medicine_review_insert_save);
        txtContentsLength = (TextView)findViewById(R.id.txt_medicine_review_insert_contents_length);
        scrollView = (ScrollView)findViewById(R.id.scr_medicine_review);

        edtContents.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if (edtContents.getText().length() == 0){
                        edtContents.setHint("");
                    }
                }
            }
        });

        edtContents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContentsLength.setText("("+s.length()+"/2000)");
                if (s.toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                    btnSave.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                }else{
                    btnSave.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgBack.setOnClickListener(this);
        txtTerm.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        btnFirst.setOnCheckedChangeListener(this);
        btnSecond.setOnCheckedChangeListener(this);
        btnThird.setOnCheckedChangeListener(this);

        setHideKeyboard(this,scrollView);
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

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }
    
    void DayCntPicker(String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.dialog_number_picker,null);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_dialog_number_picker_title);
        Button btnCancel = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_cancel);
        Button btnDone = (Button)dateTimeView.findViewById(R.id.btn_dialog_number_picker_done);
        final NumberPicker numberPicker = (NumberPicker)dateTimeView.findViewById(R.id.number_picker);
        final Dialog dateTimeDialog = new Dialog(MedicineReviewWriteActivity.this);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        numberPicker.setWrapSelectorWheel(false);

        txtTitle.setText(title);

        numberPicker.setMinValue(0);

        numberPicker.setMaxValue(termStrings.length-1);
        numberPicker.setDisplayedValues(termStrings);

        for (int i = 0; i < termStrings.length; i++){
            if (termStrings[i].equals(txtTerm.getText().toString())){
                numberPicker.setValue(i);
            }
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int idx = numberPicker.getValue();
                txtTerm.setText(termStrings[idx]);
                txtTerm.setTextColor(getResources().getColor(R.color.color09D182));
                dateTimeDialog.dismiss();
                if (edtContents.getText().toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                    btnSave.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });
    }
    TwoButtonDialog twoButtonDialog;

    @Override
    public void onBackPressed() {

        twoButtonDialog = new TwoButtonDialog(MedicineReviewWriteActivity.this,"약 후기", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_medicine_review_insert_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_medicine_review_insert_term : {
                DayCntPicker("복용기간 선택");
                break;
            }
            case R.id.btn_medicine_review_insert_save : {
                btnSave.setClickable(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSave.setClickable(true);
                    }
                },3000);
                if (beforeIntent != null) {
                    if (beforeIntent.hasExtra("reviewNo")) {
                        if (edtContents.getText().toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                            NetworkCall(REVIEW_UPDATE);
                        }
                    }else{
                        if (sideEffectFlag != 0 && txtTerm.getText().length() != 0){
                            if (edtContents.getText().toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                                NetworkCall(REVIEW_INSERT);
                            }
                        }
                    }
                }else{
                    if (sideEffectFlag != 0 && txtTerm.getText().length() != 0){
                        if (edtContents.getText().toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                            NetworkCall(REVIEW_INSERT);
                        }
                    }
                }
                break;
            }
            case R.id.img_review_remove : {
                imgRemove.setClickable(false);
                twoButtonDialog = new TwoButtonDialog(this, "약 후기 삭제","약 후기를 삭제하시겠습니까?","취소","삭제");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        NetworkCall(REVIEW_DELETE);
                    }
                });
                break;
            }
        }
    }

    public void TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

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
                NetworkCall(REVIEW_DELETE);
            }
        });
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.rdo_medicine_review_insert_first : {
                if (isChecked){
                    btnFirst.setBackgroundResource(R.drawable.toggle_btn_checked_2dp);
                    btnFirst.setTextColor(Color.parseColor("#09D182"));
                    btnFirst.setTypeface(fontBold);
                    sideEffectFlag = 1;
                    if (edtContents.getText().toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                        btnSave.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }else{
                    btnFirst.setBackgroundResource(R.drawable.toggle_btn);
                    btnFirst.setTextColor(Color.parseColor("#5c5c5c"));
                    btnFirst.setTypeface(fontMedium);
                }
                break;
            }
            case R.id.rdo_medicine_review_insert_second : {
                if (isChecked){
                    btnSecond.setBackgroundResource(R.drawable.toggle_btn_checked_2dp);
                    btnSecond.setTextColor(Color.parseColor("#09D182"));
                    btnSecond.setTypeface(fontBold);
                    sideEffectFlag = 2;
                    if (edtContents.getText().toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                        btnSave.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }else{
                    btnSecond.setBackgroundResource(R.drawable.toggle_btn);
                    btnSecond.setTextColor(Color.parseColor("#5c5c5c"));
                    btnSecond.setTypeface(fontMedium);
                }
                break;
            }
            case R.id.rdo_medicine_review_insert_third : {
                if (isChecked){
                    btnThird.setBackgroundResource(R.drawable.toggle_btn_checked_2dp);
                    btnThird.setTextColor(Color.parseColor("#09D182"));
                    btnThird.setTypeface(fontBold);
                    sideEffectFlag = 3;
                    if (edtContents.getText().toString().length() > 0 && txtTerm.getText().toString().length() > 0 && sideEffectFlag != 0){
                        btnSave.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }else{
                    btnThird.setBackgroundResource(R.drawable.toggle_btn);
                    btnThird.setTextColor(Color.parseColor("#5c5c5c"));
                    btnThird.setTypeface(fontMedium);
                }
                break;
            }
        }
    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(REVIEW_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),""+beforeIntent.getIntExtra("medicineNo",0),
                    edtContents.getText().toString(),txtTerm.getText().toString(),""+sideEffectFlag);
        }else if (mCode.equals(REVIEW_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+beforeIntent.getIntExtra("reviewNo",0),""+beforeIntent.getIntExtra("medicineNo",0),
                    edtContents.getText().toString(),txtTerm.getText().toString(),""+sideEffectFlag);
        }else if (mCode.equals(REVIEW_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+beforeIntent.getIntExtra("reviewNo",0),""+beforeIntent.getIntExtra("medicineNo",0));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(REVIEW_INSERT)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSave.setClickable(true);
                        setResult(RESULT_OK);
                        finish();
                    }
                },500);
            }else if (mCode.equals(REVIEW_UPDATE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnSave.setClickable(true);
                        setResult(RESULT_OK);
                        finish();
                    }
                },500);
            }else if (mCode.equals(REVIEW_DELETE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgRemove.setClickable(true);
                        setResult(RESULT_OK);
                        finish();
                    }
                },500);
            }
        }
    }
}
