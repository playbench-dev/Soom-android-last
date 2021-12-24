package com.kmw.soom2.Login.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Item.ItemClass;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.FIND_USER_ID;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.FIND_USER_PW;

public class SearchPwActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "SearchPwActivity";
    TextView txtBack;
    EditText edtEmail, edtName;
    Button btnFinish;
    InputMethodManager ime;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pw);

        FindViewById();
    }

    void FindViewById(){
        ime = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        txtBack = (TextView) findViewById(R.id.back_text_view_activity_search_pw);
        edtEmail = (EditText) findViewById(R.id.name_edit_activity_search_pw);
        edtName = (EditText) findViewById(R.id.birth_edit_activity_search_pw);
        btnFinish = (Button) findViewById(R.id.login_success_btn_activity_search_pw);

        txtBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(FIND_USER_PW)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(edtName.getText().toString().trim(),edtEmail.getText().toString().trim());
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(FIND_USER_PW)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.get("result").equals("Y")){
//                        new OneButtonDialog(SearchPwActivity.this,"비밀번호 찾기","해당 이메일로 임시비밀번호 발송 완료!","확인");
                        Intent intent = new Intent(SearchPwActivity.this,SuccessPwSearchActivity.class);
                        startActivity(intent);
                        onBackPressed();
                        btnFinish.setEnabled(true);
                    }else{
                        new OneButtonDialog(SearchPwActivity.this,"비밀번호 찾기",Utils.JsonIsNullCheck(jsonObject,"message"),"확인");
                        btnFinish.setEnabled(true);
                    }
                }catch (JSONException e){

                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_text_view_activity_search_pw : {
                onBackPressed();
                break;
            }
            case R.id.login_success_btn_activity_search_pw : {
                if (edtEmail.getText().length() == 0 || !ItemClass.checkEmail(edtEmail.getText().toString().trim())){
                    new OneButtonDialog(this,"비밀번호 찾기","이메일 또는 닉네임을 확인해주세요.","확인");
                }else{
                    btnFinish.setEnabled(false);
                    NetworkCall(FIND_USER_PW);
                }
                break;
            }
        }
    }
}
