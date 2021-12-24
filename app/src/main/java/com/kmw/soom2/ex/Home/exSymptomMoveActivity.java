package com.kmw.soom2.ex.Home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewSymptomRecordActivity;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_LIST_ALL_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_SYMPTOM_LIST_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_SYMPTOM_LIST_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_SYMPTOM_LIST_UPDATE;
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

public class exSymptomMoveActivity extends AppCompatActivity implements View.OnClickListener {

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
    Date date = new Date(System.currentTimeMillis());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_new_symptom_record);

        FindViewById();

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

        mTextViewDate.setText(Utils.formatYYYYMMDD2.format(date));
        mTextViewTime.setText(formatHHMM.format(date));

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
        mEditViewMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.smoothScrollTo(0, mScrollView.getBottom() + mScrollView.getScrollY());
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_new_symptom_record_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_new_symptom_record_date : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.txt_new_symptom_record_time : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_new_symptom_record_none : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_new_symptom_record_cough : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_new_symptom_record_phlegm : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_new_symptom_record_frustrated : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_new_symptom_record_difficulty : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_new_symptom_record_wheezing : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_new_symptom_record_etc : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.txt_new_symptom_record_cause : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
            }
            case R.id.btn_new_symptom_record_finish : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
