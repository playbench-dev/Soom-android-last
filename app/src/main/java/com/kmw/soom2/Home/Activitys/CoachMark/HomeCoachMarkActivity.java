package com.kmw.soom2.Home.Activitys.CoachMark;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Activitys.AgreeActivity;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.AGREE_SELECT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.AGREE_UPDATE;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class HomeCoachMarkActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String                  TAG = "HomeCoachMarkActivity";
    Button                          mButtonNever;
    Button                          mButtonFinish;
    ImageView                       mImageBg;

    SharedPreferences               pref;
    SharedPreferences.Editor        editor;

    //memo 약관동의 팝업
    View                bottomSheetDialog_Agree_View;
    BottomSheetDialog   mAgreeBottomSheetDialog;
    LinearLayout        mLinAgreeAll;
    CheckBox            mChbAgreeAll;
    CheckBox            mChbAgreeService;
    CheckBox            mChbAgreePersonal;
    CheckBox            mChbAgreeSensitivity;
    CheckBox            mChbAgreeMarketing;
    Button              mBtnAgreeDone;
    ImageView           mImageDetailService;
    ImageView           mImageDetailPersonal;
    ImageView           mImageDetailSensitivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_coach_mark);

        pref                        = getSharedPreferences("preferences",MODE_PRIVATE);
        editor                      = pref.edit();

        FindViewById();

        NullCheck(this);

        if (pref.getInt("homeCoachMark1",0) == 2){
            mImageBg.setBackgroundResource(R.drawable.img_coach_mark_home_2);
            mButtonFinish.setText("닫기");
        }else if (pref.getInt("homeCoachMark2",0) == 2){
            mImageBg.setBackgroundResource(R.drawable.img_coach_mark_home_1);
            mButtonFinish.setText("닫기");
        }

        NetworkCall(AGREE_SELECT);
    }

    void FindViewById(){
        mButtonNever                = (Button)findViewById(R.id.btn_home_coach_mark_never);
        mButtonFinish               = (Button)findViewById(R.id.btn_home_coach_mark_finish);
        mImageBg                    = (ImageView)findViewById(R.id.img_home_coach_mark_bg);

        mButtonNever.setOnClickListener(this);
        mButtonFinish.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

    }

    //memo 2 - never , 1 - 앱 종료시 다시 시작, 0 - 무조건 띄우기
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_home_coach_mark_never : {
                if (mButtonFinish.getText().toString().equals("다음")){
                    mImageBg.setBackgroundResource(R.drawable.img_coach_mark_home_2);
                    mButtonFinish.setText("닫기");
                    editor.putInt("homeCoachMark1",2);
                    editor.apply();
                }else{
                    if (pref.getInt("homeCoachMark2",0) == 2){
                        editor.putInt("homeCoachMark1",2);
                        editor.apply();
                        finish();
                    }else{
                        editor.putInt("homeCoachMark2",2);
                        editor.apply();
                        finish();
                    }
                }
                break;
            }
            case R.id.btn_home_coach_mark_finish : {
                if (mButtonFinish.getText().toString().equals("다음")){
                    mImageBg.setBackgroundResource(R.drawable.img_coach_mark_home_2);
                    mButtonFinish.setText("닫기");
                    editor.putInt("homeCoachMark1",1);
                    editor.apply();
                }else{
                    if (pref.getInt("homeCoachMark2",0) == 2){
                        editor.putInt("homeCoachMark1",1);
                        editor.apply();
                        finish();
                    }else{
                        editor.putInt("homeCoachMark2",1);
                        editor.apply();
                        finish();
                    }
                }
                break;
            }
            case R.id.chb_agree_all : {
                if (!mChbAgreeAll.isChecked()){
                    mChbAgreeService.setChecked(false);
                    mChbAgreePersonal.setChecked(false);
                    mChbAgreeSensitivity.setChecked(false);
                    mChbAgreeMarketing.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    mChbAgreeService.setChecked(true);
                    mChbAgreePersonal.setChecked(true);
                    mChbAgreeSensitivity.setChecked(true);
                    mChbAgreeMarketing.setChecked(true);
                    mBtnAgreeDone.setEnabled(true);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                }
                break;
            }
            case R.id.chb_agree_service: {
                if (!mChbAgreeService.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreePersonal.isChecked() && mChbAgreeSensitivity.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_personal : {
                if (!mChbAgreePersonal.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreeSensitivity.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_sensitivity : {
                if (!mChbAgreeSensitivity.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreePersonal.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_marketing : {
                if (!mChbAgreeMarketing.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreeSensitivity.isChecked() && mChbAgreePersonal.isChecked()){
                        mChbAgreeAll.setChecked(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                        mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                    }
                }
                break;
            }
            case R.id.img_agree_move_service : {
                Intent i = new Intent(this, AgreeActivity.class);
                i.putExtra("flag",1);
                startActivity(i);
                break;
            }
            case R.id.img_agree_move_personal : {
                Intent i = new Intent(this,AgreeActivity.class);
                i.putExtra("flag",2);
                startActivity(i);
                break;
            }
            case R.id.img_agree_move_sensitivity : {
                Intent i = new Intent(this,AgreeActivity.class);
                i.putExtra("flag",3);
                startActivity(i);
                break;
            }
            case R.id.btn_agree_done : {
                if (mChbAgreeMarketing.isChecked()){
                    mAgreeBottomSheetDialog.dismiss();
                    MarketingOneBtnPopup(this);
                }else{
                    //memo 약관동의 url
                    mAgreeBottomSheetDialog.dismiss();
                }
                break;
            }
        }
    }

    void AgreePopUp(){
        bottomSheetDialog_Agree_View    = getLayoutInflater().inflate(R.layout.agree_dialog,null);
        mAgreeBottomSheetDialog         = new BottomSheetDialog(this);

        mLinAgreeAll                    = (LinearLayout)bottomSheetDialog_Agree_View.findViewById(R.id.lin_agree_check_all);
        mChbAgreeAll                    = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_all);
        mChbAgreeService                = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_service);
        mChbAgreePersonal               = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_personal);
        mChbAgreeSensitivity            = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_sensitivity);
        mChbAgreeMarketing              = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_marketing);
        mBtnAgreeDone                   = (Button)bottomSheetDialog_Agree_View.findViewById(R.id.btn_agree_done);
        mImageDetailService             = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_service);
        mImageDetailPersonal            = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_personal);
        mImageDetailSensitivity         = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_sensitivity);

        mAgreeBottomSheetDialog.setContentView(bottomSheetDialog_Agree_View);
        mAgreeBottomSheetDialog.setCancelable(false);

        mChbAgreeAll.setOnClickListener(this);
        mChbAgreeService.setOnClickListener(this);
        mChbAgreePersonal.setOnClickListener(this);
        mChbAgreeSensitivity.setOnClickListener(this);
        mChbAgreeMarketing.setOnClickListener(this);
        mBtnAgreeDone.setOnClickListener(this);
        mImageDetailService.setOnClickListener(this);
        mImageDetailPersonal.setOnClickListener(this);
        mImageDetailSensitivity.setOnClickListener(this);

        mAgreeBottomSheetDialog.show();
    }

    public void MarketingOneBtnPopup(Context context) {

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.marketing_one_btn_popup, null);

        TextView txtAgreeDate = (TextView) layout.findViewById(R.id.txt_marketing_one_btn_popup_contents);
        Button btnOk = (Button) layout.findViewById(R.id.btn_marketing_one_btn_popup_ok);

        txtAgreeDate.setText("동의일자 " + new SimpleDateFormat("yyyy.MM.dd").format(new Date(System.currentTimeMillis())));

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
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //memo 약관동의 url
                dateTimeDialog.dismiss();
            }
        });
    }

    void NetworkCall(String mCode){
        if (mCode.equals(AGREE_SELECT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo());
        }else if (mCode.equals(AGREE_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(AGREE_SELECT)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    JSONObject object = jsonArray.getJSONObject(0);

                    if (JsonIntIsNullCheck(object,"ESSENTIAL_PERMISSION_FLAG") < 1){
                        AgreePopUp();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (mCode.equals(AGREE_UPDATE)){

            }
        }
    }
}
