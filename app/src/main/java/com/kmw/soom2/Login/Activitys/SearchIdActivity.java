package com.kmw.soom2.Login.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_FIRST_SETTING;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.FIND_USER_ID;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_SNS_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.USER_SIGN_UP;

public class SearchIdActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {
    String TAG = "SearchIdActivity";
    TextView mBackTextView;
    EditText mEmailEditText;
    Button mSuccessButton;
    InputMethodManager mInputMethdManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_id);

        mInputMethdManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mBackTextView = (TextView) findViewById(R.id.back_text_view_activity_search_id);

        mEmailEditText = (EditText) findViewById(R.id.name_edit_activity_search_id);

        mSuccessButton = (Button) findViewById(R.id.login_success_btn_activity_search_id);

        mBackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEmailEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    mEmailEditText.clearFocus();
                    mSuccessButton.performClick();
                    hideKeyboard();
                }
                return false;
            }
        });

        mSuccessButton.setOnClickListener(this);

    }

    private void hideKeyboard() {
        mInputMethdManager.hideSoftInputFromWindow(mEmailEditText.getWindowToken(), 0);

    }

    String response = "";

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(FIND_USER_ID)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mEmailEditText.getText().toString());
        }
    }
    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(FIND_USER_ID)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.get("result").equals("N")){
                        new OneButtonDialog(SearchIdActivity.this,"아이디 찾기","닉네임을 확인해주세요.","확인");
                    }else{
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        Intent intent = new Intent(SearchIdActivity.this, SearchIdResultActivity.class);
                        intent.putExtra("email",jsonArray.getJSONObject(0).getString("EMAIL"));
                        startActivityForResult(intent,1111);
                    }

                }catch (JSONException e){

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == 1111){
                onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_success_btn_activity_search_id : {
                if (mEmailEditText.getText().length() != 0){
                    NetworkCall(FIND_USER_ID);
                }else{
                    new OneButtonDialog(this,"아이디 찾기","정보를 정확히 입력해주세요.","확인");
                }
                break;
            }
        }
    }
}
