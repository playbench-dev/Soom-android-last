package com.kmw.soom2.ex.Static;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Activitys.StaticMedicineActivity;
import com.kmw.soom2.Reports.Item.HistoryItems;
import com.kmw.soom2.Views.CalendarView;
import com.kmw.soom2.Views.StaticMedicineCalendarView;
import com.kmw.soom2.ex.exNewAnotherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.formatYYYYMM;

public class exStaticMedicineActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "StaticMedicineActivity";
    TextView txtBack;
    LinearLayout linLeftBtn, linRightBtn;
    public static TextView txtCalendarTitle;
    StaticMedicineCalendarView staticMedicineCalendarView;
    Calendar currentCalendar;
    ImageView imgNextBtn;
    LinearLayout linListParent;
    ArrayList<CheckBox> checkBoxArrayList = new ArrayList<>();
    ArrayList<TextView> nameTextList = new ArrayList<>();
    ArrayList<TextView> basicTextList = new ArrayList<>();
    ArrayList<TextView> emergencyTextList = new ArrayList<>();
    Date currentDate = Calendar.getInstance().getTime();

    ArrayList<HistoryItems> historyItems = new ArrayList<>();
    String response;

    ProgressDialog progressDialog;

    Button btnMove;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

    String[] strings1 = new String[]{"-01 ","-02 ","-07 ","-14 ","-15 ","-22 "};
    String[] strings2 = new String[]{"-01 ","-02 ","-05 ","-06 ","-07 ","-13 ","-15 ","-16 ","-18 ","-22 ","-25 ","-26 ","-27 "};
    String[] strings3 = new String[]{"-01 ","-02 ","-05 ","-06 ","-07 ","-13 ","-15 ","-16 ","-18 ","-22 ","-25 ","-26 ","-27 "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_static_medicine);

        FindViewById();

    }

    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_statics_medicine_back);
        linLeftBtn = (LinearLayout) findViewById(R.id.lin_statics_medicine_left_btn);
        linRightBtn = (LinearLayout) findViewById(R.id.lin_statics_medicine_right_btn);
        txtCalendarTitle = (TextView) findViewById(R.id.txt_statics_medicine_title);
        staticMedicineCalendarView = (StaticMedicineCalendarView) findViewById(R.id.calendar_statics_medicine);
        linListParent = (LinearLayout) findViewById(R.id.lin_statics_medicine_list_parent);
        imgNextBtn = (ImageView) findViewById(R.id.img_statics_medicine_right_btn);
        btnMove = findViewById(R.id.btn_ex_medicine_static_move);

        btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(exStaticMedicineActivity.this, NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        linRightBtn.setEnabled(false);
        imgNextBtn.setColorFilter(Color.parseColor("#acacac"));


        currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        txtCalendarTitle.setText(formatYYYYMM.format(calendar.getTime()));

        staticMedicineCalendarView.setRobotoCalendarListener(new CalendarView.RobotoCalendarListener() {
            @Override
            public void onDayClick(Calendar daySelectedCalendar) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(System.currentTimeMillis()));
                if (daySelectedCalendar.before(calendar)) {

                }
            }

            @Override
            public void onDayLongClick(Calendar daySelectedCalendar) {

            }

            @Override
            public void onRightButtonClick(Calendar daySelectedCalendar) {
            }

            @Override
            public void onLeftButtonClick(Calendar daySelectedCalendar) {

            }
        });

        txtBack.setOnClickListener(this);
        linLeftBtn.setOnClickListener(this);
        linRightBtn.setOnClickListener(this);

//        for (int i = 0; i < 5; i++){
//            medicineListMake("name"+i,i,i);
//        }

        for (int i = 0; i < 6; i++) {
            HistoryItems historyItem = new HistoryItems();

            historyItem.setUserHistoryNo(1);
            historyItem.setCategory(1);
            historyItem.setRegisterDt(simpleDateFormat.format(new Date(System.currentTimeMillis())) + strings1[i]);
            historyItem.setMedicineNo(1);

            if (i == 5){
                historyItem.setEmergencyFlag(2);
            }else{
                historyItem.setEmergencyFlag(1);
            }

            historyItem.setKo("진해거담제");

            historyItems.add(historyItem);
            Collections.reverse(historyItems);
        }

        for (int i = 0; i < 12; i++) {
            HistoryItems historyItem = new HistoryItems();

            historyItem.setUserHistoryNo(2);
            historyItem.setCategory(2);
            historyItem.setRegisterDt(simpleDateFormat.format(new Date(System.currentTimeMillis())) + strings2[i]);
            historyItem.setMedicineNo(2);

            if (i == 5 || i == 7 || i == 9){
                historyItem.setEmergencyFlag(2);
            }else{
                historyItem.setEmergencyFlag(1);
            }

            historyItem.setKo("스테로이드제");

            historyItems.add(historyItem);
            Collections.reverse(historyItems);
        }

        for (int i = 0; i < 12; i++) {
            HistoryItems historyItem = new HistoryItems();

            historyItem.setUserHistoryNo(3);
            historyItem.setCategory(3);
            historyItem.setRegisterDt(simpleDateFormat.format(new Date(System.currentTimeMillis())) + strings3[i]);
            historyItem.setMedicineNo(3);

            if (i == 5 || i == 7 || i == 8){
                historyItem.setEmergencyFlag(2);
            }else{
                historyItem.setEmergencyFlag(1);
            }

            historyItem.setKo("기침약");

            historyItems.add(historyItem);
            Collections.reverse(historyItems);
        }

        HashMap<String, ArrayList<HistoryItems>> map = setupHistoryData(historyItems);

        linListParent.removeAllViews();
        int i = 0;
        for (Map.Entry<String, ArrayList<HistoryItems>> entry : map.entrySet()) {
            String key = entry.getKey();
            ArrayList<HistoryItems> value = entry.getValue();
            //TODO: other cool stuff
            medicineListMake(i, key, value);
            i++;
        }
    }

    void isNullMedicineListData() {
        linListParent.removeAllViews();
    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    void medicineListMake(int index, String name, final ArrayList<HistoryItems> items) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_statics_medicine_list_item, null);

        final CheckBox chbCheck = (CheckBox) listView.findViewById(R.id.chb_statics_medicine_list_item);
        final TextView txtName = (TextView) listView.findViewById(R.id.txt_statics_medicine_list_item_name);
        final TextView txtBasic = (TextView) listView.findViewById(R.id.txt_statics_medicine_list_item_basic);
        final TextView txtEmergency = (TextView) listView.findViewById(R.id.txt_statics_medicine_list_item_emergency);

        linListParent.addView(listView);

        int basicCnt = items.stream().filter(new Predicate<HistoryItems>() {
            @Override
            public boolean test(HistoryItems f) {
                return (f.getEmergencyFlag() == 1);
            }
        }).collect(Collectors.toList()).size();
        int emergencyCnt = items.stream().filter(new Predicate<HistoryItems>() {
            @Override
            public boolean test(HistoryItems f) {
                return (f.getEmergencyFlag() == 2);
            }
        }).collect(Collectors.toList()).size();

        txtName.setText(name);
        txtBasic.setText("일반복용 " + basicCnt + " 회");
        txtEmergency.setText("응급복용 " + emergencyCnt + " 회");

        checkBoxArrayList.add(chbCheck);
        nameTextList.add(txtName);
        basicTextList.add(txtBasic);
        emergencyTextList.add(txtEmergency);

        if (index == 0) {
            chbCheck.setChecked(true);
            txtName.setTextColor(getResources().getColor(R.color.black));
            txtBasic.setTextColor(getResources().getColor(R.color.colorPrimary));
            txtEmergency.setTextColor(getResources().getColor(R.color.ff6767));

            staticMedicineCalendarView.setCalendarItem(items);
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < checkBoxArrayList.size(); i++) {
                    checkBoxArrayList.get(i).setChecked(false);
                    nameTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    basicTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    emergencyTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                }
                chbCheck.setChecked(true);
                txtName.setTextColor(getResources().getColor(R.color.black));
                txtBasic.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtEmergency.setTextColor(getResources().getColor(R.color.ff6767));

                staticMedicineCalendarView.setCalendarItem(items);

            }
        });
    }

    void medicineListMake(String name, int basicCnt, int emergencyCnt) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_statics_medicine_list_item, null);

        final CheckBox chbCheck = (CheckBox) listView.findViewById(R.id.chb_statics_medicine_list_item);
        final TextView txtName = (TextView) listView.findViewById(R.id.txt_statics_medicine_list_item_name);
        final TextView txtBasic = (TextView) listView.findViewById(R.id.txt_statics_medicine_list_item_basic);
        final TextView txtEmergency = (TextView) listView.findViewById(R.id.txt_statics_medicine_list_item_emergency);

        linListParent.addView(listView);

        txtName.setText(name);
        txtBasic.setText("일반복용 " + basicCnt + " 회");
        txtEmergency.setText("응급복용 " + emergencyCnt + " 회");

        checkBoxArrayList.add(chbCheck);
        nameTextList.add(txtName);
        basicTextList.add(txtBasic);
        emergencyTextList.add(txtEmergency);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < checkBoxArrayList.size(); i++) {
                    checkBoxArrayList.get(i).setChecked(false);
                    nameTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    basicTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                    emergencyTextList.get(i).setTextColor(getResources().getColor(R.color.acacac));
                }
                chbCheck.setChecked(true);
                txtName.setTextColor(getResources().getColor(R.color.black));
                txtBasic.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtEmergency.setTextColor(getResources().getColor(R.color.ff6767));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_statics_medicine_back: {
                onBackPressed();
                break;
            }
            case R.id.lin_statics_medicine_left_btn: {

                break;
            }
            case R.id.lin_statics_medicine_right_btn: {

                break;
            }
        }
    }

    public HashMap<String, ArrayList<HistoryItems>> setupHistoryData(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getKo())) {
                map.get(datas.get(i).getKo()).add(datas.get(i));
            } else {
                map.put(datas.get(i).getKo(), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getKo()).add(datas.get(i));
            }
        }

        return map;
    }
}