package com.kmw.soom2.MyPage.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.MyPage.Item.HospitalItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.hospitalItem;
import static com.kmw.soom2.Common.Utils.userItem;

public class MyHospitalInfoActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "AttendingHospital";
    EditText hospitalNameEditText, departmentNameEditText, doctorNameEditText;
    Button saveButton;
    ImageView imgShared;
    String address = "";
    ImageView mImageViewSearch;

    HospitalItem hosItem = new HospitalItem();

    Intent beforeIntent;
    String response;
    ImageView btnBack;

    TwoButtonDialog twoButtonDialog;

    // memo: 2021-01-14 ????????? ?????? ??????
    OneButtonDialog oneButtonDialog;
    // memo: 2021-01-14 ????????? ?????? ??????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_hospital);

        btnBack = findViewById(R.id.back_img_view_activity_attending_hospital);
        hospitalNameEditText = (EditText) findViewById(R.id.hospital_name_edit_text_activity_attending_hospital);
        departmentNameEditText = (EditText) findViewById(R.id.department_edit_text_activity_attending_hospital);
        doctorNameEditText = (EditText) findViewById(R.id.doctor_name_edit_text_activity_attending_hospital);

        saveButton = (Button) findViewById(R.id.save_button_activity_attending_hospital);
        imgShared = (ImageView) findViewById(R.id.share_button_activity_attending_hospital);

        mImageViewSearch = (ImageView)findViewById(R.id.img_attending_hospital_search);

        NullCheck(this);

        btnBack.setOnClickListener(this);
        hospitalNameEditText.setFocusable(false);
        hospitalNameEditText.setClickable(false);
        hospitalNameEditText.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        imgShared.setOnClickListener(this);
        mImageViewSearch.setOnClickListener(this);

        beforeIntent = getIntent();

        new SelectHospitalInfoNetWork().execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111){
            if (resultCode == RESULT_OK){
                hospitalNameEditText.setText(data.getStringExtra("hospitalName"));
                address = data.getStringExtra("hospitalAddress");

                mImageViewSearch.setImageResource(R.drawable.ic_close_black_24dp);
            }
        }else if (requestCode == 2222){
            if (resultCode == RESULT_OK){
                Toast.makeText(this, "???????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hospital_name_edit_text_activity_attending_hospital:
                hospitalNameEditText.setClickable(false);
                Intent intent = new Intent(MyHospitalInfoActivity.this, HospitalSearchActivity.class);
                startActivityForResult(intent,1111);
                hospitalNameEditText.setClickable(true);
                break;

            case R.id.save_button_activity_attending_hospital:
                saveButton.setClickable(false);
                Log.i(TAG,"hospitalItem : " + hospitalItem);

                if (hospitalItem != null) {
                    new UpdateHospitalInfoNetwork().execute();
                }else {
//                    new InsertHospitalInfoNetwork().execute();
                    // memo: 2021-01-14 ????????? ?????? ??????
                    // ???????????? ????????? ?????????????????? ?????? ?????? ????????? ?????? ?????? ??????
                    if(hospitalNameEditText.getText().length() != 0 || departmentNameEditText.getText().length() != 0 || doctorNameEditText.getText().length() != 0) {
                        new InsertHospitalInfoNetwork().execute();
                    }
                    // memo: 2021-01-14 ????????? ?????? ??????
                }
                break;

            case R.id.share_button_activity_attending_hospital:
                Intent i = new Intent(MyHospitalInfoActivity.this, CommunityWriteActivity.class);
                if (hosItem != null){
                    if(hosItem.getName().length() != 0 || hosItem.getDepartment().length() != 0 || hosItem.getDoctor().length() != 0){
                        if (hosItem != null){
                            i.putExtra("hospitalName", hosItem.getName());
                            i.putExtra("hospitalAddress", address);
                            i.putExtra("hospitalDoctorName", hosItem.getDoctor());
                            i.putExtra("hospitalDepartment", hosItem.getDepartment());
                            i.putExtra("hashTagList",Utils.tagList);
                        }else{
                            i.putExtra("hospitalAddress", address);
                            i.putExtra("hashTagList",Utils.tagList);
                        }
                        i.putExtra("menuNo","2");
                        i.putExtra("cMenuNo","21");
                        i.putExtra("menuTitle","????????????");
                        i.putExtra("cMenuTitle","?????? ??????");
                        startActivityForResult(i,2222);
                    }else {
                        new OneButtonDialog(MyHospitalInfoActivity.this,"????????????","??????????????? ?????? ??? ??????????????????!","??????");
                    }
                }else{
                    new OneButtonDialog(MyHospitalInfoActivity.this,"????????????","??????????????? ?????? ??? ??????????????????!","??????");
                }

                break;
            case R.id.back_img_view_activity_attending_hospital:
                onBackPressed();
                break;
            case R.id.img_attending_hospital_search:
                if (hospitalNameEditText.getText().length() > 0){
                    hospitalNameEditText.setText("");
                    mImageViewSearch.setImageResource(R.drawable.ic_search_black_24dp);
                    address = "";
                }else{
                    hospitalNameEditText.setClickable(false);
                    Intent intent1 = new Intent(MyHospitalInfoActivity.this, HospitalSearchActivity.class);
                    startActivityForResult(intent1,1111);
                    hospitalNameEditText.setClickable(true);
                }
                break;
        }
    }
    public class InsertHospitalInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder()).add("USER_NO", "" + userItem.getUserNo());
            body.add("NAME", hospitalNameEditText.getText().toString());
            body.add("ADDR", address);

            if (departmentNameEditText.getText().toString() != "") {
                body.add("DEPARTMENT", departmentNameEditText.getText().toString());
            }
            if (doctorNameEditText.getText().toString() != "") {
                body.add("DOCTOR", doctorNameEditText.getText().toString());
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertHospital(), body.build());
                return response;
            } catch (IOException e) {
                saveButton.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (JsonIsNullCheck(jsonObject, "result") == "N") {

                }else {
//                    new OneButtonDialog(MyHospitalInfoActivity.this,"???????????? ??????","??????????????? ?????????????????????.","??????");
//                    new SelectHospitalInfoNetWork().execute();

                    // memo: 2021-01-14 ????????? ?????? ??????
                    oneButtonDialog = new OneButtonDialog(MyHospitalInfoActivity.this, "???????????? ??????", "??????????????? ?????????????????????.", "??????");
                    oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {
                            twoButtonDialog = new TwoButtonDialog(MyHospitalInfoActivity.this,"????????????", "????????? ????????????????\n?????? ????????? ?????? ????????????\n??????????????? ???????????????!","????????????!", "???!");
                            twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                                @Override
                                public void OkButtonClick(View v) {
                                    twoButtonDialog.dismiss();
                                    imgShared.performClick();
                                }
                            });

                            twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                                @Override
                                public void CancelButtonClick(View v) {
                                    twoButtonDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    });
                    new SelectHospitalInfoNetWork().execute();
                    // memo: 2021-01-14 ????????? ?????? ??????
                }
                saveButton.setClickable(true);
            }catch (JSONException e){
                saveButton.setClickable(true);
                //StartActivity(SplashActivity.this, HomeActivity.class);
            }
        }
    }

    public class UpdateHospitalInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("HOSPITAL_NO", String.valueOf(hosItem.getHospitalNo()));

            if (hospitalNameEditText.getText().length() == 0){
                body.add("NAME", "");
                hosItem.setName("");
            }else{
                body.add("NAME", hospitalNameEditText.getText().toString());
                hosItem.setName(hospitalNameEditText.getText().toString());
            }
            if (address.length() == 0){
                body.add("ADDR", "");
                hosItem.setName("");
            }else{
                body.add("ADDR", address.toString());
                hosItem.setName(address);
            }
            if (departmentNameEditText.getText().length() == 0){
                body.add("DEPARTMENT", "");
                hosItem.setDepartment("");
            }else{
                body.add("DEPARTMENT", departmentNameEditText.getText().toString());
                hosItem.setDepartment(departmentNameEditText.getText().toString());
            }
            if (doctorNameEditText.getText().length() == 0){
                body.add("DOCTOR", "");
                hosItem.setDoctor("");
            }else{
                body.add("DOCTOR", doctorNameEditText.getText().toString());
                hosItem.setDoctor(doctorNameEditText.getText().toString());
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateHospital(), body.build());
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                saveButton.setClickable(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                // memo: 2021-01-14 ????????? ?????? ??????
                // ??????, ?????? ??? ?????? ????????? ?????? ??????
                oneButtonDialog = new OneButtonDialog(MyHospitalInfoActivity.this, "???????????? ??????", "??????????????? ?????????????????????.", "??????");
                oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                    @Override
                    public void OnButtonClick(View v) {
                        oneButtonDialog.dismiss();
                        twoButtonDialog = new TwoButtonDialog(MyHospitalInfoActivity.this,"????????????", "????????? ????????????????\n?????? ????????? ?????? ????????????\n??????????????? ???????????????!","????????????!", "???!");
                        twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                            @Override
                            public void OkButtonClick(View v) {
                                twoButtonDialog.dismiss();
                                imgShared.performClick();
                            }
                        });

                        twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                            @Override
                            public void CancelButtonClick(View v) {
                                twoButtonDialog.dismiss();
                                finish();
                            }
                        });
                    }
                });
                // memo: 2021-01-14 ????????? ?????? ??????
                if (JsonIsNullCheck(jsonObject, "result") == "N") {
                }else {
                    new SelectHospitalInfoNetWork().execute();
                }
                saveButton.setClickable(true);
            }catch (JSONException e){
                saveButton.setClickable(true);
                //                StartActivity(SplashActivity.this, HomeActivity.class);
            }
        }
    }

    public class SelectHospitalInfoNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread")
            RequestBody body = new FormBody.Builder()
                    .add("USER_NO",""+ Utils.userItem.getUserNo()).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHospitalInfo(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                Log.i(TAG,"list : " + s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
                    HospitalItem hospitalItem = new HospitalItem();

                    hospitalItem.setHospitalNo(JsonIntIsNullCheck(object, "HOSPITAL_NO"));
                    hospitalItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                    hospitalItem.setName(JsonIsNullCheck(object, "NAME"));
                    hospitalItem.setAddr(JsonIsNullCheck(object, "ADDR"));
                    hospitalItem.setDepartment(JsonIsNullCheck(object, "DEPARTMENT"));
                    hospitalItem.setDoctor(JsonIsNullCheck(object, "DOCTOR"));
                    hospitalItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                    hospitalItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));

                    Utils.hospitalItem = hospitalItem;
                    hosItem = hospitalItem;

                    if (hosItem != null) {
                        hospitalNameEditText.setText(hosItem.getName());
                        departmentNameEditText.setText(hosItem.getDepartment());
                        doctorNameEditText.setText(hosItem.getDoctor());
                        if (hosItem.getName().length() != 0){
                            address = hosItem.getAddr();
                        }
                        if (hosItem.getName().length() > 0){
                            mImageViewSearch.setImageResource(R.drawable.ic_close_black_24dp);
                        }else{
                            mImageViewSearch.setImageResource(R.drawable.ic_search_black_24dp);
                        }
                    }else {
                        mImageViewSearch.setImageResource(R.drawable.ic_search_black_24dp);
                    }
                }

            }catch (JSONException e){

            }
        }
    }

    public void OneBtnPopup(Context context, String title, String contents, String btnText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.one_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_one_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_one_btn_popup_contents);
        Button btnOk = (Button)layout.findViewById(R.id.btn_one_btn_popup_ok);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnOk.setText(btnText);

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
                dateTimeDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (hospitalItem != null){
            if (!hospitalItem.getName().equals(hospitalNameEditText.getText().toString()) ||
                    !hospitalItem.getDepartment().equals(departmentNameEditText.getText().toString()) ||
                    !hospitalItem.getDoctor().equals(doctorNameEditText.getText().toString())){
                twoButtonDialog = new TwoButtonDialog(MyHospitalInfoActivity.this,"????????????", "??????????????? ???????????? ???????????????. ?????? ??????????????????????","??????", "??????");
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
            }else{
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }
}
