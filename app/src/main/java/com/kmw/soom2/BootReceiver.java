package com.kmw.soom2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.PowerManager;

import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.DrugControl.Adapter.DrugAlarmRecyclerViewAdapter;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BootReceiver extends BroadcastReceiver {

    DbOpenHelper mDbOpenHelper;
    Cursor iCursor;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    SimpleDateFormat formatHH = new SimpleDateFormat("HH");
    SimpleDateFormat formatMM = new SimpleDateFormat("mm");
    DrugAlarmRecyclerViewAdapter drugAlarmRecyclerViewAdapter;
    Context context;
    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("hh:mm");

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            mDbOpenHelper = new DbOpenHelper(context);
            try {
                mDbOpenHelper.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mDbOpenHelper.create();
            iCursor = mDbOpenHelper.selectColumns();
            drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(context, mDbOpenHelper);
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wakeLock.acquire(3000);

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
                diaryNotification(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectTime);
            }
        }
    }

    void diaryNotification(int idx, long times) {

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("cnt", idx);
        alarmIntent = PendingIntent.getBroadcast(context, idx, intent,0);
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(formatHH.format(new Date(times))));
        calendar.set(Calendar.MINUTE, Integer.parseInt(formatMM.format(new Date(times))));
        calendar.set(Calendar.SECOND, 0);

        setExactAndAllowWhileIdle(alarmMgr,AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),alarmIntent);
    }

    public static void setExactAndAllowWhileIdle(AlarmManager alarmManager, int type, long triggerAtMillis, PendingIntent operation) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(type, triggerAtMillis, operation);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(type, triggerAtMillis, operation);
        } else {
            alarmManager.set(type, triggerAtMillis, operation);
        }
    }
}
