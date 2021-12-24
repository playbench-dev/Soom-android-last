package com.kmw.soom2.ex.Home;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.HomeAdapter.MedicineRecordListAdapter;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
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

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.FEED_INSERT_MEDICINE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_HISTORY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SELECT_HOME_FEED_LIST_CHECK;
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

public class exMedicineMoveActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MedicinRecordActivity";
    TextView txtBack;
    TextView txtDate, txtTime;
    LinearLayout linNoList,linInfoVisible;
    ScrollView scrList;
    ListView listView;
    Button btnFinish;
    ImageView addImage;
    String registerDt = "";
    Date currentDate = null;
    ImageView imgPlus;
    LinearLayout linPlus;

    Calendar calendar = Calendar.getInstance();
    Date date = new Date(System.currentTimeMillis());

    ProgressDialog progressDialog;

    Intent beforeIntent;

    Calendar currentCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_medicin_record);

        beforeIntent = getIntent();

        currentCalendar = Calendar.getInstance();

        FindViewById();

    }

    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_medicine_record_back);
        txtDate = (TextView) findViewById(R.id.txt_medicine_record_date);
        txtTime = (TextView) findViewById(R.id.txt_medicine_record_time);
        linNoList = (LinearLayout) findViewById(R.id.lin_medicine_record_no_list);
        linInfoVisible = (LinearLayout)findViewById(R.id.lin_medicine_record_info_visible);
        scrList = (ScrollView) findViewById(R.id.scr_medicine_record_list);
        listView = (ListView) findViewById(R.id.list_medicine_record);
        btnFinish = (Button) findViewById(R.id.btn_medicine_record_finish);
        addImage = (ImageView) findViewById(R.id.add_img_medicine_record_list);
        imgPlus = (ImageView)findViewById(R.id.img_medicine_record_plus);
        linPlus = (LinearLayout)findViewById(R.id.lin_medicine_record_plus);

        txtDate.setText(Utils.formatYYYYMMDD2.format(date));
        txtTime.setText(formatHHMM.format(date));

        calendar.setTime(date);
        txtBack.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        imgPlus.setOnClickListener(this);
        linPlus.setOnClickListener(this);

        btnFinish.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_medicine_record_date: {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_medicine_record_time : {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_medicine_record_back: {
                onBackPressed();
                break;
            }
            case R.id.btn_medicine_record_finish: {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.img_medicine_record_plus : {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_medicine_record_plus : {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
        }
    }
}
