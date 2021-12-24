package com.kmw.soom2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.kmw.soom2.Common.DbOpenHelper;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Notification.CATEGORY_MESSAGE;

public class AlarmService extends Service {

    DbOpenHelper mDbOpenHelper;
    Cursor iCursor;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    SimpleDateFormat formatHH = new SimpleDateFormat("HH");
    SimpleDateFormat formatMM = new SimpleDateFormat("mm");

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mDbOpenHelper = new DbOpenHelper(AlarmService.this);
        try {
            mDbOpenHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mDbOpenHelper.create();
        iCursor = mDbOpenHelper.selectColumns();

        while (iCursor.moveToNext()) {
            if (intent.getIntExtra("cnt", 0) == iCursor.getInt(iCursor.getColumnIndex("_id"))) {
                Log.i("Alarm","day : " + iCursor.getString(iCursor.getColumnIndex("selectDay")) + " now : " + doDayOfWeek());
                if (iCursor.getString(iCursor.getColumnIndex("selectDay")).contains(doDayOfWeek()) && iCursor.getInt(iCursor.getColumnIndex("pushCheck")) == 1) {

                    String drugName = "";
                    for (int i = 0; i < iCursor.getColumnNames().length; i++) {
                        if (iCursor.getColumnNames()[i].equals("drugName")){
                            drugName = iCursor.getString(iCursor.getColumnIndex("drugName"));
                        }
                    }

                    if (drugName.length() == 0){
                        drugName = "약 먹을 시간입니다.";
                    }
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());//추가

                    Intent notificationIntent = new Intent(AlarmService.this, SplashActivity.class);
                    notificationIntent.putExtra("medicineAlarm", true);
                    notificationIntent.putExtra("android_channel_id", "drugAlarm");
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingI = PendingIntent.getActivity(getApplicationContext(), 0,
                            notificationIntent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "drugAlarm");

                    //OREO API 26 이상에서는 채널 필요
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        builder.setSmallIcon(R.drawable.soom_logo_512); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
                    } else
                        builder.setSmallIcon(R.mipmap.soom_logo_512); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

                    builder.setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setTicker("{Time to watch some cool stuff!}")
                            .setContentTitle(drugName)
                            .setContentText("클릭, 간편하게 복용여부 기록하기.")
                            .setContentInfo("INFO")
                            .setChannelId("drugAlarm")
                            .setPriority(NotificationCompat.PRIORITY_HIGH) //알람 중요도
                            .setContentIntent(pendingI);
                    notificationManager.notify((int) (System.currentTimeMillis() / 1000), builder.build());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        builder.setCategory(CATEGORY_MESSAGE)
                                .setPriority(Notification.PRIORITY_MAX)
                                .setVisibility(Notification.VISIBILITY_PUBLIC);
                    }
                }
            }
            String tempID = iCursor.getString(iCursor.getColumnIndex("drugAlarm"));
            String tempSelectDay = iCursor.getString(iCursor.getColumnIndex("selectDay"));
            long tempSelectTime = iCursor.getLong(iCursor.getColumnIndex("selectTime"));
            int tempPushCheck = iCursor.getInt(iCursor.getColumnIndex("pushCheck"));
//            drugAlarmRecyclerViewAdapter.addItem(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectDay, tempSelectTime, tempPushCheck);
            diaryNotification(iCursor.getInt(iCursor.getColumnIndex("_id")), tempSelectTime,getApplicationContext());
        }
        return START_NOT_STICKY;
    }

    public void diaryNotification(int idx, long times, Context context) {
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("cnt", idx);
        alarmIntent = PendingIntent.getBroadcast(context, idx, intent, 0);
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        compareCalendar.add(Calendar.SECOND, 10);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(formatHH.format(new Date(times))));
        calendar.set(Calendar.MINUTE, Integer.parseInt(formatMM.format(new Date(times))));
        calendar.set(Calendar.SECOND, 0);

        if (new Date(times).before(compareCalendar.getTime())) {

            calendar.add(Calendar.DAY_OF_MONTH, +1);
        }
        setExactAndAllowWhileIdle(alarmMgr, AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

    public static void setExactAndAllowWhileIdle(AlarmManager alarmManager, int type, long triggerAtMillis, PendingIntent operation) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(type, triggerAtMillis, operation);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(type, triggerAtMillis, operation);
        } else {
            alarmManager.set(type, triggerAtMillis, operation);
        }
    }

    private String doDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        String strWeek = null;

        int week = calendar.get(Calendar.DAY_OF_WEEK);

        if (week == 1) {
            strWeek = "1";
        } else if (week == 2) {
            strWeek = "2";
        } else if (week == 3) {
            strWeek = "3";
        } else if (week == 4) {
            strWeek = "4";
        } else if (week == 5) {
            strWeek = "5";
        } else if (week == 6) {
            strWeek = "6";
        } else if (week == 7) {
            strWeek = "7";
        }
        return strWeek;
    }
}
