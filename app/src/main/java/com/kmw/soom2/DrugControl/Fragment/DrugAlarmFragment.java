package com.kmw.soom2.DrugControl.Fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.AlarmReceiver;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.Common.DividerItemDecoration;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Activity.DrugAlarmInsertActivity;
import com.kmw.soom2.DrugControl.Adapter.DrugAlarmRecyclerViewAdapter;
import com.kmw.soom2.R;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.Utils.NullCheck;


public class  DrugAlarmFragment extends Fragment implements View.OnClickListener {

    private String TAG = "DrugAlarmFragment";
    Toolbar linAlarmPlus;
    SimpleDateFormat dateTimeStatusFormat = new SimpleDateFormat("a");
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("hh:mm");
    SimpleDateFormat dateTimeFormat1 = new SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분 ss초");
    Date date = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    LinearLayout linNoItem;

    DbOpenHelper mDbOpenHelper;
    Cursor iCursor;

    SimpleDateFormat formatHH = new SimpleDateFormat("HH");
    SimpleDateFormat formatMM = new SimpleDateFormat("mm");

    RecyclerView linAlarmListParent;
    DrugAlarmRecyclerViewAdapter drugAlarmRecyclerViewAdapter;

    public DrugAlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_alarm, container, false);

        pref = getActivity().getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        NullCheck(getActivity());

        mDbOpenHelper = new DbOpenHelper(getActivity());
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mDbOpenHelper.create();

        iCursor = mDbOpenHelper.selectColumns();
        linAlarmPlus = (Toolbar) view.findViewById(R.id.lin_drug_alarm_plus);
        linAlarmListParent = (RecyclerView) view.findViewById(R.id.lin_drug_alarm_parent);
        linNoItem = (LinearLayout)view.findViewById(R.id.lin_drug_alarm_no_item);

        linAlarmPlus.setOnClickListener(this);

        drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(getActivity(), mDbOpenHelper);

        if (iCursor.getCount() > 0) {
            while (iCursor.moveToNext()) {
                String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
                String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
                long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
                int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));
                String drugName = "";
                for (int i = 0; i < iCursor.getColumnNames().length; i++) {
                    if (iCursor.getColumnNames()[i].equals("drugName")) {
                        drugName = iCursor.getString(iCursor.getColumnIndex("drugName"));
                    }
                }
                drugAlarmRecyclerViewAdapter.addItem(drugName, iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
            }

            linAlarmListParent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            linAlarmListParent.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.line_divider));
            linAlarmListParent.setAdapter(drugAlarmRecyclerViewAdapter);

            linAlarmListParent.setVisibility(View.VISIBLE);
            linNoItem.setVisibility(View.GONE);
        }else {
            linAlarmListParent.setVisibility(View.GONE);
            linNoItem.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDbOpenHelper = new DbOpenHelper(getActivity());
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mDbOpenHelper.create();

        iCursor = mDbOpenHelper.selectColumns();
        drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(getActivity(), mDbOpenHelper);

        if (iCursor.getCount() > 0) {
            while (iCursor.moveToNext()) {
                String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
                String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
                long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
                int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));
                String drugName = "";
                for (int i = 0; i < iCursor.getColumnNames().length; i++) {
                    if (iCursor.getColumnNames()[i].equals("drugName")) {
                        drugName = iCursor.getString(iCursor.getColumnIndex("drugName"));
                    }
                }
                drugAlarmRecyclerViewAdapter.addItem(drugName, iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
            }

            linAlarmListParent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            linAlarmListParent.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.line_divider));
            drugAlarmRecyclerViewAdapter.notifyDataSetChanged();
            linAlarmListParent.setAdapter(drugAlarmRecyclerViewAdapter);

            linAlarmListParent.setVisibility(View.VISIBLE);
            linNoItem.setVisibility(View.GONE);
        }else {
            linAlarmListParent.setVisibility(View.GONE);
            linNoItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3333) {
            if(resultCode == RESULT_OK) {
                mDbOpenHelper = new DbOpenHelper(getContext());
                try {
                    mDbOpenHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mDbOpenHelper.create();
                iCursor = mDbOpenHelper.selectColumns();
                drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(getContext(), mDbOpenHelper);
                while (iCursor.moveToNext()) {
                    String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
                    String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
                    long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
                    int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));
                    String drugName = "";
                    for (int i = 0; i < iCursor.getColumnNames().length; i++) {
                        if (iCursor.getColumnNames()[i].equals("drugName")){
                            drugName = iCursor.getString(iCursor.getColumnIndex("drugName"));
                        }
                    }
                    drugAlarmRecyclerViewAdapter.addItem(drugName,iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
                    diaryNotification(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectTime,getContext());
                }
                linAlarmListParent.setAdapter(drugAlarmRecyclerViewAdapter);
            }else {
            }
        }
    }

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    public void diaryNotification(int idx, long times,Context context) {
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("cnt", idx);
        alarmIntent = PendingIntent.getBroadcast(context, idx, intent, 0);
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.add(Calendar.SECOND,10);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(formatHH.format(new Date(times))));
        calendar.set(Calendar.MINUTE, Integer.parseInt(formatMM.format(new Date(times))));
        calendar.set(Calendar.SECOND, 0);

        if (new Date(times).before(compareCalendar.getTime())){
            calendar.add(Calendar.DAY_OF_MONTH, +1);
        }

        setExactAndAllowWhileIdle(alarmMgr, AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public static void setExactAndAllowWhileIdle(AlarmManager alarmManager, int type, long triggerAtMillis, PendingIntent operation) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(type, triggerAtMillis,operation);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(type, triggerAtMillis, operation);
        } else {
            alarmManager.set(type, triggerAtMillis, operation);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_drug_alarm_plus: {
                Intent i = new Intent(getActivity(), DrugAlarmInsertActivity.class);
                startActivityForResult(i, 3333);
                break;
            }
        }
    }
}
