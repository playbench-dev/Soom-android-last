package com.kmw.soom2.Home.Activitys.MedicineInsertActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.Home.HomeAdapter.MedicineSearchAdapter;
import com.kmw.soom2.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_INSERT_APPLICATION;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class MedicineSearchActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "MedicineSearchActivity";
    TextView txtBack;
    EditText edtSearch;
    ListView listView;
    LinearLayout linNoList;
    Button btnRequest;
    MedicineSearchAdapter adapter;
    ProgressDialog progressDialog;

    boolean newbieCheck = false;
    Intent beforIntent;
    Activity thisActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_search);

        beforIntent = getIntent();

        if (beforIntent != null){
            if (beforIntent.hasExtra("newbie")){
                newbieCheck = true;
            }
        }
        thisActivity = MedicineSearchActivity.this;

        FindViewById();

        NullCheck(this);

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_medicine_search_back);
        edtSearch = (EditText)findViewById(R.id.edt_medicine_search);
        listView = (ListView)findViewById(R.id.list_medicine_search);
        linNoList = (LinearLayout)findViewById(R.id.lin_medicine_search_no_list);
        btnRequest = (Button)findViewById(R.id.btn_medicine_search_request);

        adapter = new MedicineSearchAdapter(this,newbieCheck,thisActivity);

        if (MEDICINE_LIST != null){
            for (int i = 0; i < MEDICINE_LIST.size(); i++){
                adapter.addItem(MEDICINE_LIST.get(i));
            }

            listView.setAdapter(adapter);
        }

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.searchName(String.valueOf(s).trim());

                if (adapter.getCount() == 0){
                    listView.setVisibility(View.GONE);
                    linNoList.setVisibility(View.VISIBLE);
                }else{
                    listView.setVisibility(View.VISIBLE);
                    linNoList.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtBack.setOnClickListener(this);
        btnRequest.setOnClickListener(this);
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

            }
        });
    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(MEDICINE_INSERT_APPLICATION)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),edtSearch.getText().toString());
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(MEDICINE_INSERT_APPLICATION)){
                try{
                    JSONObject jsonObject = new JSONObject(mResult);

                    if (jsonObject.has("result")){
                        if (jsonObject.get("result").equals("Y")){
                            new OneButtonDialog(MedicineSearchActivity.this,"약 등록 요청완료!","약을 정확히 등록하기 위해\n최대 이틀이 소요될 수 있습니다.\n등록 완료 후 알림을 보내드립니다.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    edtSearch.setText("");
                                }
                            });
                        }
                    }
                }catch (JSONException e){

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_medicine_search_back : {
                if (newbieCheck) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("newbieBack", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    onBackPressed();
                }
                break;
            }
            case R.id.btn_medicine_search_request : {
                NetworkCall(MEDICINE_INSERT_APPLICATION);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (newbieCheck) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("newbieBack", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if(beforIntent.getBooleanExtra("medicineInsert",false)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("medicineInsert", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        super.onBackPressed();
    }
}
