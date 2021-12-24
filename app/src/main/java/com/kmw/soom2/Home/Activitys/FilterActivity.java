package com.kmw.soom2.Home.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Home.HomeFragment;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.NullCheck;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener , CompoundButton.OnCheckedChangeListener {

    private String TAG = "FilterActivity";

    ImageView imgBack;
    CheckBox drugCheckedChecBox, asthmaCheckedChecBox, symptomCheckedChecBox, dustCheckedChecBox, lungsCheckedChecBox, memoCheckedChecBox,asthmaPercentChb;
    LinearLayout drugCheckedChecBoxParent, asthmaCheckedChecBoxParent, symptomCheckedChecBoxParent, dustCheckedChecBoxParent, lungsCheckedChecBoxParent, memoCheckedChecBoxParent, asthmaPercentParent;
    private TextView mTextViewDrug;
    private TextView mTextViewAshtma;
    private TextView mTextViewSymptom;
    private TextView mTextViewDust;
    private TextView mTextViewLungs;
    private TextView mTextViewMemo;
    private TextView mTextViewAsthmaPercent;

    Button successButton,btnReset;

    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

    }

    void FindViewById() {
        imgBack = (ImageView) findViewById(R.id.img_filter_back);
        drugCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_drug_activity_filter);
        asthmaCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_asthma_activity_filter);
        symptomCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_symptom_activity_filter);
        dustCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_dust_activity_filter);
        lungsCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_lungs_activity_filter);
        memoCheckedChecBox = (CheckBox) findViewById(R.id.checkbox_memo_activity_filter);
        asthmaPercentChb = (CheckBox)findViewById(R.id.checkbox_asthma_percent_activity_filter);
        mTextViewDrug = (TextView)findViewById(R.id.txt_filter_drug);
        mTextViewAshtma = (TextView)findViewById(R.id.txt_filter_asthma);
        mTextViewSymptom = (TextView)findViewById(R.id.txt_filter_symptom);
        mTextViewDust = (TextView)findViewById(R.id.txt_filter_dust);
        mTextViewLungs = (TextView)findViewById(R.id.txt_filter_lungs);
        mTextViewMemo = (TextView)findViewById(R.id.txt_filter_memo);
        mTextViewAsthmaPercent = (TextView)findViewById(R.id.txt_filter_asthma_percent);
        drugCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_drug_activity_filter_parent);
        asthmaCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_asthma_activity_filter_parent);
        symptomCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_symptom_activity_filter_parent);
        dustCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_dust_activity_filter_parent);
        lungsCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_lungs_activity_filter_parent);
        memoCheckedChecBoxParent = (LinearLayout) findViewById(R.id.checkbox_memo_activity_filter_parent);
        asthmaPercentParent = (LinearLayout)findViewById(R.id.checkbox_asthma_percent_activity_filter_parent);
        successButton = (Button) findViewById(R.id.success_btn_activity_filter);
        btnReset = (Button)findViewById(R.id.reset_btn_activity_filter);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drugCheckedChecBox.setChecked(true);
                mTextViewDrug.setTextColor(getResources().getColor(R.color.color343434));
                symptomCheckedChecBox.setChecked(true);
                mTextViewSymptom.setTextColor(getResources().getColor(R.color.color343434));
                asthmaCheckedChecBox.setChecked(true);
                mTextViewAshtma.setTextColor(getResources().getColor(R.color.color343434));
                lungsCheckedChecBox.setChecked(true);
                mTextViewLungs.setTextColor(getResources().getColor(R.color.color343434));
                dustCheckedChecBox.setChecked(true);
                mTextViewDust.setTextColor(getResources().getColor(R.color.color343434));
                asthmaPercentChb.setChecked(true);
                mTextViewAsthmaPercent.setTextColor(getResources().getColor(R.color.color343434));
                memoCheckedChecBox.setChecked(true);
                mTextViewMemo.setTextColor(getResources().getColor(R.color.color343434));

            }
        });

        if (HomeFragment.filterTextList.contains("1")) {
            drugCheckedChecBox.setChecked(true);
            mTextViewDrug.setTextColor(getResources().getColor(R.color.color343434));
        }
        if (HomeFragment.filterTextList.contains("11") || HomeFragment.filterTextList.contains("12") ||
                HomeFragment.filterTextList.contains("13") || HomeFragment.filterTextList.contains("14") || HomeFragment.filterTextList.contains("15") ||
                HomeFragment.filterTextList.contains("16") || HomeFragment.filterTextList.contains("40")) {
            symptomCheckedChecBox.setChecked(true);
            mTextViewSymptom.setTextColor(getResources().getColor(R.color.color343434));
        }
        if (HomeFragment.filterTextList.contains("21")) {
            asthmaCheckedChecBox.setChecked(true);
            mTextViewAshtma.setTextColor(getResources().getColor(R.color.color343434));
        }
        if (HomeFragment.filterTextList.contains("22")) {
            lungsCheckedChecBox.setChecked(true);
            mTextViewLungs.setTextColor(getResources().getColor(R.color.color343434));
        }
        if (HomeFragment.filterTextList.contains("23")) {
            dustCheckedChecBox.setChecked(true);
            mTextViewDust.setTextColor(getResources().getColor(R.color.color343434));
        }
        if (HomeFragment.filterTextList.contains("24")) {
            asthmaPercentChb.setChecked(true);
            mTextViewAsthmaPercent.setTextColor(getResources().getColor(R.color.color343434));
        }
        if (HomeFragment.filterTextList.contains("30")) {
            memoCheckedChecBox.setChecked(true);
            mTextViewMemo.setTextColor(getResources().getColor(R.color.color343434));
        }

        drugCheckedChecBoxParent.setOnClickListener(this);
        asthmaCheckedChecBoxParent.setOnClickListener(this);
        symptomCheckedChecBoxParent.setOnClickListener(this);
        dustCheckedChecBoxParent.setOnClickListener(this);
        lungsCheckedChecBoxParent.setOnClickListener(this);
        memoCheckedChecBoxParent.setOnClickListener(this);
        asthmaPercentParent.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        successButton.setOnClickListener(this);

        drugCheckedChecBox.setOnCheckedChangeListener(this);
        symptomCheckedChecBox.setOnCheckedChangeListener(this);
        asthmaCheckedChecBox.setOnCheckedChangeListener(this);
        dustCheckedChecBox.setOnCheckedChangeListener(this);
        lungsCheckedChecBox.setOnCheckedChangeListener(this);
        memoCheckedChecBox.setOnCheckedChangeListener(this);
        asthmaPercentChb.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.checkbox_drug_activity_filter_parent: {
                drugCheckedChecBox.performClick();
//                mTextViewDrug.setTextColor(getResources().getColor(R.color.color343434));
                break;
            }
            case R.id.checkbox_asthma_activity_filter_parent: {
                asthmaCheckedChecBox.performClick();
//                mTextViewAshtma.setTextColor(getResources().getColor(R.color.color343434));
                break;
            }
            case R.id.checkbox_symptom_activity_filter_parent: {
                symptomCheckedChecBox.performClick();
//                mTextViewSymptom.setTextColor(getResources().getColor(R.color.color343434));
                break;
            }
            case R.id.checkbox_dust_activity_filter_parent: {
                dustCheckedChecBox.performClick();
//                mTextViewDust.setTextColor(getResources().getColor(R.color.color343434));
                break;
            }
            case R.id.checkbox_lungs_activity_filter_parent: {
                lungsCheckedChecBox.performClick();
//                mTextViewLungs.setTextColor(getResources().getColor(R.color.color343434));
                break;
            }
            case R.id.checkbox_memo_activity_filter_parent: {
                memoCheckedChecBox.performClick();
//                mTextViewMemo.setTextColor(getResources().getColor(R.color.color343434));
                break;
            }
            case R.id.checkbox_asthma_percent_activity_filter_parent : {
                asthmaPercentChb.performClick();
                break;
            }
            case R.id.img_filter_back: {
                onBackPressed();
                break;
            }
            case R.id.success_btn_activity_filter: {
                HomeFragment.filterTextList = new ArrayList();
                if (drugCheckedChecBox.isChecked() || asthmaCheckedChecBox.isChecked() || symptomCheckedChecBox.isChecked() || dustCheckedChecBox.isChecked()
                        || lungsCheckedChecBox.isChecked() || memoCheckedChecBox.isChecked() || asthmaPercentChb.isChecked()) {
                    if (drugCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("1")) {
                            HomeFragment.filterTextList.add("1");
                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("1")) {
                            HomeFragment.filterTextList.remove("1");
                        }
                    }
                    if (asthmaCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("21")) {
                            HomeFragment.filterTextList.add("21");
                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("21")) {
                            HomeFragment.filterTextList.remove("21");
                        }
                    }
                    if (symptomCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("11")) {
                            HomeFragment.filterTextList.add("11");
                        }
                        if (!HomeFragment.filterTextList.contains("12")) {
                            HomeFragment.filterTextList.add("12");
                        }
                        if (!HomeFragment.filterTextList.contains("13")) {
                            HomeFragment.filterTextList.add("13");
                        }
                        if (!HomeFragment.filterTextList.contains("14")) {
                            HomeFragment.filterTextList.add("14");
                        }
                        if (!HomeFragment.filterTextList.contains("15")){
                            HomeFragment.filterTextList.add("15");
                        }
                        if (!HomeFragment.filterTextList.contains("16")){
                            HomeFragment.filterTextList.add("16");
                        }
                        if (!HomeFragment.filterTextList.contains("40")){
                            HomeFragment.filterTextList.add("40");
                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("11")) {
                            HomeFragment.filterTextList.remove("11");
                        }
                        if (HomeFragment.filterTextList.contains("12")) {
                            HomeFragment.filterTextList.remove("12");
                        }
                        if (HomeFragment.filterTextList.contains("13")) {
                            HomeFragment.filterTextList.remove("13");
                        }
                        if (HomeFragment.filterTextList.contains("14")) {
                            HomeFragment.filterTextList.remove("14");
                        }
                        if (HomeFragment.filterTextList.contains("15")){
                            HomeFragment.filterTextList.remove("15");
                        }
                        if (HomeFragment.filterTextList.contains("16")){
                            HomeFragment.filterTextList.remove("16");
                        }
                        if (HomeFragment.filterTextList.contains("40")){
                            HomeFragment.filterTextList.remove("40");
                        }
                    }
                    if (dustCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("23")) {
                            HomeFragment.filterTextList.add("23");
                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("23")) {
                            HomeFragment.filterTextList.remove("23");
                        }
                    }
                    if (lungsCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("22")) {
                            HomeFragment.filterTextList.add("22");
                        }
                    } else {
                        if (HomeFragment.filterTextList.contains("22")) {
                            HomeFragment.filterTextList.remove("22");
                        }
                    }
                    if (memoCheckedChecBox.isChecked()) {
                        if (!HomeFragment.filterTextList.contains("30")) {
                            HomeFragment.filterTextList.add("30");
                        }
                    } else {

                        if (HomeFragment.filterTextList.contains("30")) {
                            HomeFragment.filterTextList.remove("30");
                        }
                    }
                    if (asthmaPercentChb.isChecked()){
                        if (!HomeFragment.filterTextList.contains("24")){
                            HomeFragment.filterTextList.add("24");
                        }
                    }else{
                        if (HomeFragment.filterTextList.contains("24")) {
                            HomeFragment.filterTextList.remove("24");
                        }
                    }
                            setResult(RESULT_OK);
                            onBackPressed();
                } else {
                    new OneButtonDialog(this, "필터", "최소 1개 이상 선택되어 있어야 합니다.", "확인");
                }
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkbox_drug_activity_filter : {
                if (isChecked){
                    mTextViewDrug.setTextColor(getResources().getColor(R.color.color343434));
                }else{
                    mTextViewDrug.setTextColor(getResources().getColor(R.color.acacac));
                }
                break;
            }
            case R.id.checkbox_asthma_activity_filter : {
                if (isChecked){
                    mTextViewAshtma.setTextColor(getResources().getColor(R.color.color343434));
                }else{
                    mTextViewAshtma.setTextColor(getResources().getColor(R.color.acacac));
                }
                break;
            }
            case R.id.checkbox_symptom_activity_filter : {
                if (isChecked){
                    mTextViewSymptom.setTextColor(getResources().getColor(R.color.color343434));
                }else{
                    mTextViewSymptom.setTextColor(getResources().getColor(R.color.acacac));
                }
                break;
            }
            case R.id.checkbox_dust_activity_filter : {
                if (isChecked){
                    mTextViewDust.setTextColor(getResources().getColor(R.color.color343434));
                }else{
                    mTextViewDust.setTextColor(getResources().getColor(R.color.acacac));
                }
                break;
            }
            case R.id.checkbox_lungs_activity_filter : {
                if (isChecked){
                    mTextViewLungs.setTextColor(getResources().getColor(R.color.color343434));
                }else{
                    mTextViewLungs.setTextColor(getResources().getColor(R.color.acacac));
                }
                break;
            }
            case R.id.checkbox_memo_activity_filter : {
                if (isChecked){
                    mTextViewMemo.setTextColor(getResources().getColor(R.color.color343434));
                }else{
                    mTextViewMemo.setTextColor(getResources().getColor(R.color.acacac));
                }
                break;
            }
            case R.id.checkbox_asthma_percent_activity_filter : {
                if (isChecked){
                    mTextViewAsthmaPercent.setTextColor(getResources().getColor(R.color.color343434));
                }else{
                    mTextViewAsthmaPercent.setTextColor(getResources().getColor(R.color.acacac));
                }
                break;
            }
        }
    }
}
