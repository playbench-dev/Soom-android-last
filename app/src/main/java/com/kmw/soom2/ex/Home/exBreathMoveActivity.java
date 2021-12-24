package com.kmw.soom2.ex.Home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.Item.ForeignKeys;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HOME_FEED_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.PEF_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.PEF_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST_CHECK;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;
import static com.kmw.soom2.Common.Utils.userItem;

public class exBreathMoveActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "BreathRecordActivity";
    TextView txtBack;
    TextView txtDate,txtTime;
    EditText edtValue;
    ImageView imgEditClose;
    RadioButton rdoUnUsed,rdoUsed;
    LinearLayout linQuestion;
    Button btnFinish;
    ImageView imgRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_breath_record);

        FindViewById();

        imgRemove.setVisibility(View.GONE);

        txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
        txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_breath_record_back);
        txtDate = (TextView)findViewById(R.id.txt_breath_record_date);
        txtTime = (TextView)findViewById(R.id.txt_breath_record_time);
        edtValue = (EditText)findViewById(R.id.edt_breath_record_value);
        rdoUsed = (RadioButton)findViewById(R.id.rdo_breath_record_used);
        rdoUnUsed = (RadioButton)findViewById(R.id.rdo_breath_record_unused);
        imgRemove = (ImageView)findViewById(R.id.img_breath_record_remove);
        imgEditClose = (ImageView)findViewById(R.id.img_breath_record_edit_close);
        linQuestion = (LinearLayout)findViewById(R.id.lin_breath_record_question);
        btnFinish = (Button)findViewById(R.id.btn_breath_record_finish);

        txtBack.setOnClickListener(this);
        imgEditClose.setOnClickListener(this);
        linQuestion.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        edtValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    imgEditClose.setVisibility(View.VISIBLE);
                }else{
                    imgEditClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rdoUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(exBreathMoveActivity.this, NewAnotherLoginActivity.class);
                startActivity(i);
            }
        });

        rdoUnUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(exBreathMoveActivity.this, NewAnotherLoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_breath_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_breath_record_edit_close : {
                edtValue.setText("");
                break;
            }
            case R.id.img_breath_record_remove : {

                break;
            }
            case R.id.lin_breath_record_question : {
                Intent i = new Intent(exBreathMoveActivity.this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btn_breath_record_finish : {
                Intent i = new Intent(exBreathMoveActivity.this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_breath_record_date: {
                Intent i = new Intent(exBreathMoveActivity.this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_breath_record_time: {
                Intent i = new Intent(exBreathMoveActivity.this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
        }
    }
}
