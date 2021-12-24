package com.kmw.soom2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.Common.DividerItemDecoration;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.Communitys.NewCommunityWebFragment;
import com.kmw.soom2.DrugControl.Adapter.DrugAlarmRecyclerViewAdapter;
import com.kmw.soom2.DrugControl.DrugFragment;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewSymptomRecordActivity;
import com.kmw.soom2.Home.HomeFragment;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.MyPage.Activity.NoticeActivity;
import com.kmw.soom2.MyPage.MyPageFragment;
import com.kmw.soom2.Reports.StaticsFragment;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";
    Intent beforeIntent;
    MainActivity mainActivity;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    Fragment homeFragment,drugFragment,communityFragmnet,staticFragment,myPageFragment;
    LinearLayout linTabHome,linTabDrug,linTabCommuninty,linTabStatics,linTabMyPage;
    Uri uri = null;
    private long backKeyPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_main);
        this.mainActivity = new MainActivity();
        beforeIntent = getIntent();

        pref    = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor  = pref.edit();

        frameLayout = (FrameLayout)findViewById(R.id.frame_layout);
        linTabHome = (LinearLayout)findViewById(R.id.lin_home_tab_home);
        linTabDrug = (LinearLayout)findViewById(R.id.lin_home_tab_drug);
        linTabCommuninty = (LinearLayout)findViewById(R.id.lin_home_tab_community);
        linTabStatics = (LinearLayout)findViewById(R.id.lin_home_tab_statics);
        linTabMyPage = (LinearLayout)findViewById(R.id.lin_home_tab_my_page);

        linTabHome.setOnClickListener(this);
        linTabDrug.setOnClickListener(this);
        linTabCommuninty.setOnClickListener(this);
        linTabStatics.setOnClickListener(this);
        linTabMyPage.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();

//        startService(new Intent(this, ForecdTerminationService.class));

        if (beforeIntent.getData() != null){
            uri = getIntent().getData();
            Uri uri1 = Uri.parse(uri.getQueryParameter("url"));
            Log.i(TAG,"scheme data : " + getIntent().getData());
            Log.i(TAG,"scheme host : " + uri.getHost());
            Log.i(TAG,"scheme path : " + uri.getPath());
            Log.i(TAG,"scheme path : " + uri1.getPath());
            Log.i(TAG,"scheme param : " + uri1.getQueryParameter("no"));
            if (uri.getHost().equals("home")){
                tintSelect(linTabHome,linTabDrug,linTabCommuninty,linTabStatics,linTabMyPage);
                if(homeFragment == null) {
                    homeFragment = new HomeFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, homeFragment).commit();
                }
                if (homeFragment != null) fragmentManager.beginTransaction().show(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            } else if (uri.getHost().equals("manage")){
                tintSelect(linTabDrug,linTabHome,linTabCommuninty,linTabStatics,linTabMyPage);

                if(drugFragment == null) {
                    drugFragment = new DrugFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, drugFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().show(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            }else if (uri.getHost().equals("community")){               //memo 커뮤니티 탭 이동
                tintSelect(linTabCommuninty,linTabHome,linTabDrug,linTabStatics,linTabMyPage);

                if (uri1.getQueryParameter("no") != null){
                     new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(MainActivity.this, NewCommunityDetailActivity.class);
                            i.putExtra("communityNo", uri1.getQueryParameter("no"));
                            startActivityForResult(i,5555);
                        }
                    },0);
                }else{
                    if(communityFragmnet == null) {
                        if (uri.getPath().equals("/all/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("0");
                        }else if (uri.getPath().equals("/contents/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("1");
                        }else if (uri.getPath().equals("/notice/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("2");
                        }else if (uri.getPath().equals("/write/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("write");
                        }else if (uri.getPath().equals("/search/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("search");
                        }
                        fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragmnet).commit();
                    }

                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                    if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                    if(communityFragmnet != null) fragmentManager.beginTransaction().show(communityFragmnet).commit();
                    if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                    if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();
                }

            }else if (uri.getHost().equals("community_detail")){        //memo 게시글 이동
                tintSelect(linTabCommuninty,linTabHome,linTabDrug,linTabStatics,linTabMyPage);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(MainActivity.this, NewCommunityDetailActivity.class);
                        i.putExtra("communityNo", uri1.getQueryParameter("no"));
                        startActivity(i);
                    }
                },0);

                if(communityFragmnet == null) {
                    communityFragmnet = new NewCommunityWebFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragmnet).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().show(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();



            }else if (uri.getHost().equals("report")){
                tintSelect(linTabStatics,linTabHome,linTabDrug,linTabCommuninty,linTabMyPage);

                if(staticFragment == null) {
                    staticFragment = new StaticsFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, staticFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().show(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            }else if (uri.getHost().equals("mypage")){
                tintSelect(linTabMyPage,linTabHome,linTabDrug,linTabCommuninty,linTabStatics);

                if(myPageFragment == null) {
                    myPageFragment = new MyPageFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, myPageFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().show(myPageFragment).commit();
            }
            getIntent().setData(null);
        }else{
            if (beforeIntent.hasExtra("newbieBack")) {
                if (beforeIntent.getBooleanExtra("newbieBack", false)) {
                    linTabHome.performClick();
                }
            }else if (beforeIntent.hasExtra("notification")){
                if (beforeIntent.hasExtra("android_channel_id")){
                    if (beforeIntent.getStringExtra("android_channel_id").equals("dosing")){
                        linTabDrug.performClick();
                    }else if (beforeIntent.getStringExtra("android_channel_id").equals("community")){
                        linTabCommuninty.performClick();
                    }else if (beforeIntent.getStringExtra("android_channel_id").equals("notice")){
                        linTabMyPage.performClick();
                    }else{
                        linTabHome.performClick();
                    }
                }
            }else if (beforeIntent.hasExtra("community")){
                linTabCommuninty.performClick();
            }else if (beforeIntent.hasExtra("notice")) {
                linTabMyPage.performClick();
            }else if (beforeIntent.hasExtra("medicineInsert")) {
                linTabDrug.performClick();
            }else if (beforeIntent.hasExtra("scoreRetry")) {
                if (beforeIntent.getBooleanExtra("scoreRetry", false)) {
                    Intent i = new Intent(MainActivity.this, AsthmaControlActivity.class);
                    startActivity(i);
                }
            }else{
                linTabHome.performClick();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    SimpleDateFormat formatHH = new SimpleDateFormat("HH");
    SimpleDateFormat formatMM = new SimpleDateFormat("mm");

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DbOpenHelper mDbOpenHelper;
        Cursor iCursor;
        RecyclerView linAlarmListParent = null;
        DrugAlarmRecyclerViewAdapter drugAlarmRecyclerViewAdapter;
        if (requestCode == 3333) {
            if (resultCode == RESULT_OK) {

                linAlarmListParent = (RecyclerView) findViewById(R.id.lin_drug_alarm_parent);
                mDbOpenHelper = new DbOpenHelper(this);
                try {
                    mDbOpenHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mDbOpenHelper.create();
                iCursor = mDbOpenHelper.selectColumns();
                drugAlarmRecyclerViewAdapter = new DrugAlarmRecyclerViewAdapter(this, mDbOpenHelper);
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
                linAlarmListParent.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                linAlarmListParent.addItemDecoration(new DividerItemDecoration(this, R.drawable.line_divider));
                drugAlarmRecyclerViewAdapter.notifyDataSetChanged();
                linAlarmListParent.setAdapter(drugAlarmRecyclerViewAdapter);
            } else {

            }
        }else if (requestCode == 5555){
            if (resultCode == RESULT_OK){
                Log.i(TAG,"result 5555");
                if(communityFragmnet == null) {
                    if (uri != null){
                        if (uri.getPath().equals("/all/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("0");
                        }else if (uri.getPath().equals("/contents/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("1");
                        }else if (uri.getPath().equals("/notice/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("2");
                        }else if (uri.getPath().equals("/write/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("write");
                        }else if (uri.getPath().equals("/search/")){
                            communityFragmnet = new NewCommunityWebFragment().newInstance("search");
                        }
                    }else{
                        communityFragmnet = new NewCommunityWebFragment().newInstance("");
                    }
                    fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragmnet).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().show(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();
            }
        }else if (requestCode == 4444){
            if (resultCode == RESULT_OK){
                if(myPageFragment == null) {
                    myPageFragment = new MyPageFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, myPageFragment).commit();
                }else{
                    myPageFragment.onActivityResult(1122,RESULT_OK,null);
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().show(myPageFragment).commit();
            }
        }else {
            Log.i(TAG,"request code : " + requestCode);
            BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
        }
    }

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void diaryNotification(int idx, long times) {
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("cnt", idx);
        alarmIntent = PendingIntent.getBroadcast(this, idx, intent, 0);
        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(formatHH.format(new Date(times))));
        calendar.set(Calendar.MINUTE, Integer.parseInt(formatMM.format(new Date(times))));
        calendar.set(Calendar.SECOND, 0);
        if (new Date(times).before(new Date(System.currentTimeMillis()))) {
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

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        TEST = (LinearLayout) findViewById(R.id.TEST);
//
//    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"destroy");
    }

//    public void logSentFriendRequestEvent () {
//        logger.logEvent("sentFriendRequest");
//    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            if (pref.getInt("homeCoachMark1",0) != 2){
                editor.putInt("homeCoachMark1",0);
            }
            if (pref.getInt("homeCoachMark2",0) != 2){
                editor.putInt("homeCoachMark2",0);
            }
            if (pref.getInt("medicineCoachMark1",0) != 2){
                editor.putInt("medicineCoachMark1",0);
            }
            if (pref.getInt("medicineCoachMark2",0) != 2){
                editor.putInt("medicineCoachMark2",0);
            }
            if (pref.getInt("communityCoachMark",0) != 2){
                editor.putInt("communityCoachMark",0);
            }
            if (pref.getBoolean("communityCheck11",false)){
                editor.putBoolean("communityCheck11",false);
            }
            editor.apply();
        }
        super.onBackPressed();
    }

    private void tintSelect(LinearLayout lin1,LinearLayout lin2,LinearLayout lin3,LinearLayout lin4,LinearLayout lin5){
        lin1.setClickable(false);
        lin2.setClickable(true);
        lin3.setClickable(true);
        lin4.setClickable(true);
        lin5.setClickable(true);
        for (int i = 0; i < lin1.getChildCount(); i++){
            if (lin1.getChildAt(i) instanceof TextView){
                TextView textView1 = (TextView)lin1.getChildAt(i);
                textView1.setTextColor(getResources().getColor(R.color.black));

                TextView textView2 = (TextView)lin2.getChildAt(i);
                textView2.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                TextView textView3 = (TextView)lin3.getChildAt(i);
                textView3.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                TextView textView4 = (TextView)lin4.getChildAt(i);
                textView4.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                TextView textView5 = (TextView)lin5.getChildAt(i);
                textView5.setTextColor(getResources().getColor(R.color.colorb1b1b1));

            }else if (lin1.getChildAt(i) instanceof ImageView){
                ImageView imageView1 = (ImageView) lin1.getChildAt(i);
                imageView1.setColorFilter(getResources().getColor(R.color.black));

                ImageView imageView2 = (ImageView) lin2.getChildAt(i);
                imageView2.setColorFilter(getResources().getColor(R.color.colorb1b1b1));

                ImageView imageView3 = (ImageView) lin3.getChildAt(i);
                imageView3.setColorFilter(getResources().getColor(R.color.colorb1b1b1));

                ImageView imageView4 = (ImageView) lin4.getChildAt(i);
                imageView4.setColorFilter(getResources().getColor(R.color.colorb1b1b1));

                ImageView imageView5 = (ImageView) lin5.getChildAt(i);
                imageView5.setColorFilter(getResources().getColor(R.color.colorb1b1b1));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_home_tab_home :

                tintSelect(linTabHome,linTabDrug,linTabCommuninty,linTabStatics,linTabMyPage);

                if(homeFragment == null) {
                    homeFragment = new HomeFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, homeFragment).commit();
                }else{
                    homeFragment.onActivityResult(1234,RESULT_OK,null);
                }

                if (homeFragment != null) fragmentManager.beginTransaction().show(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

                if (beforeIntent.hasExtra("notification")){
                    if (beforeIntent.hasExtra("android_channel_id")){
                        if (beforeIntent.getStringExtra("android_channel_id").equals("home")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(MainActivity.this, PushAlarmListActivity.class);
                                    i.putExtra("push", true);
                                    startActivity(i);
                                    getIntent().removeExtra("notification");
                                }
                            },500);
                        }else if (beforeIntent.getStringExtra("android_channel_id").equals("symptom")){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(MainActivity.this, NewSymptomRecordActivity.class);
                                    i.putExtra("body", beforeIntent.getStringExtra("body"));
                                    i.putExtra("push", true);
                                    startActivity(i);
                                    getIntent().removeExtra("notification");
                                }
                            },500);
                        }
                    }
                }
                break;
            case R.id.lin_home_tab_drug :

                tintSelect(linTabDrug,linTabHome,linTabCommuninty,linTabStatics,linTabMyPage);

                if(drugFragment == null) {
                    drugFragment = new DrugFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, drugFragment).commit();
                }else{
                    drugFragment.onActivityResult(1122,RESULT_OK,null);
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().show(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

                if (beforeIntent.hasExtra("notification")){
                    if (beforeIntent.hasExtra("android_channel_id")){
                        if (beforeIntent.getStringExtra("android_channel_id").equals("dosing")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(MainActivity.this, MedicineSelectActivity.class);
                                    i.putExtra("body", beforeIntent.getStringExtra("body"));
                                    i.putExtra("push", true);
                                    i.putExtra("keyNo", beforeIntent.getStringExtra("keyNo"));
                                    i.putExtra("medicineInsert", true);
                                    startActivity(i);
                                    getIntent().removeExtra("notification");
                                }
                            },500);
                        }
                    }
                }
                break;
            case R.id.lin_home_tab_community :

                tintSelect(linTabCommuninty,linTabHome,linTabDrug,linTabStatics,linTabMyPage);

                if (beforeIntent.hasExtra("notification")){
                    if (beforeIntent.hasExtra("android_channel_id")){
                        if (beforeIntent.getStringExtra("android_channel_id").equals("community")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(MainActivity.this, NewCommunityDetailActivity.class);
                                    i.putExtra("communityNo", beforeIntent.getStringExtra("communityNo"));
                                    i.putExtra("push", true);
                                    startActivityForResult(i,5555);
                                    overridePendingTransition(0,0);
                                    getIntent().removeExtra("notification");
                                }
                            },0);
                        }
                    }
                }else{
                    if(communityFragmnet == null) {
//                    communityFragmnet = new NewCommunityFragment().newInstance("");
                        communityFragmnet = new NewCommunityWebFragment().newInstance("");
                        fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragmnet).commit();
                    }else{
                        communityFragmnet.onActivityResult(1122,RESULT_OK,null);
                    }

                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                    if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                    if(communityFragmnet != null) fragmentManager.beginTransaction().show(communityFragmnet).commit();
                    if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                    if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();
                }

                break;
            case R.id.lin_home_tab_statics :

                tintSelect(linTabStatics,linTabHome,linTabDrug,linTabCommuninty,linTabMyPage);

                if(staticFragment == null) {
                    staticFragment = new StaticsFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, staticFragment).commit();
                }else{
                    staticFragment.onActivityResult(1122,RESULT_OK,null);
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().show(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

                break;
            case R.id.lin_home_tab_my_page :

                tintSelect(linTabMyPage,linTabHome,linTabDrug,linTabCommuninty,linTabStatics);

                if (beforeIntent.hasExtra("notification")){
                    if (beforeIntent.hasExtra("android_channel_id")){
                       if (beforeIntent.getStringExtra("android_channel_id").equals("notice")) {
                           new Handler().postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   Intent i = new Intent(MainActivity.this, NoticeActivity.class);
                                   Log.i(TAG,"noticeNo : " + beforeIntent.getStringExtra("noticeNo"));
                                   i.putExtra("noticeNo", beforeIntent.getStringExtra("noticeNo"));
                                   i.putExtra("body", beforeIntent.getStringExtra("body"));
                                   i.putExtra("push", true);
                                   startActivityForResult(i,4444);
                                   overridePendingTransition(0,0);
                                   getIntent().removeExtra("notification");
                               }
                           },0);
                        }
                    }
                }else{
                    if(myPageFragment == null) {
                        myPageFragment = new MyPageFragment().newInstance("");
                        fragmentManager.beginTransaction().add(R.id.frame_layout, myPageFragment).commit();
                    }else{
                        myPageFragment.onActivityResult(1122,RESULT_OK,null);
                    }

                    if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                    if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                    if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                    if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                    if(myPageFragment != null) fragmentManager.beginTransaction().show(myPageFragment).commit();
                }
                break;
        }
    }


}
