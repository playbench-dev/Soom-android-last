package com.kmw.soom2.Common.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Adapter.PushAlarmListAdapter;
import com.kmw.soom2.Common.Item.PushItems;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_LIST_ALL_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_PROCESS;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.LOGIN_SNS_PROCESS;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.USER_INFO_SHARED;
import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;
import static com.kmw.soom2.Common.Utils.userItem;

public class PushAlarmListActivity extends AppCompatActivity implements AsyncResponse{

    final String TAG = "PushAlarmListActivity";
    ListView listView;
    String response;
    PushAlarmListAdapter adapter;
    TextView notContents;
    TextView imgBack;
    Intent beforeIntent;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int paging = 1;
    private boolean lastItemVisibleFlag = false;
    private boolean mLockListView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_alarm_list);

        pref = getSharedPreferences(Utils.preferencesName,MODE_PRIVATE);
        editor = pref.edit();

        beforeIntent = getIntent();

        NullCheck(this);

        findViewById();

        adapter = new PushAlarmListAdapter(this);

        new PushAlarmListNetwork().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+paging);
        NetWorkCall(ALARM_LIST_ALL_READ);
    }

    void findViewById() {
        listView = findViewById(R.id.push_list_view);
        imgBack = findViewById(R.id.txt_check_first_back);
        notContents = findViewById(R.id.txt_not_contents);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
                    mLockListView = true;
                    paging++;
                    new PushAlarmListNetwork().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+paging);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });
    }

    void NetWorkCall(String mCode){
        if (mCode.equals(ALARM_LIST_ALL_READ)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+String.valueOf(userItem.getUserNo()));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {

    }

    public class PushAlarmListNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());

            body.add("USER_NO", String.valueOf(userItem.getUserNo()));
            body.add("Search_Page",strings[0]);
            body.add("Search_ShowCNT","15");

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectPushAlarmList(), body.build());
                Log.i(TAG,"PushAlarmListNetwork : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                    JSONObject jsonObject = new JSONObject(s);
                    if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                        notContents.setVisibility(View.VISIBLE);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        PushItems item;
                        if (jsonArray.length() > 0) {
//                            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                            // memo: 2021-01-15 김지훈 수정 시작
                            // 현재 역순으로 표현되고 있어서 수정
                            for (int i = 0; i < jsonArray.length(); i ++) {
                                // memo: 2021-01-15 김지훈 수정 종료
                                item = new PushItems();
                                JSONObject object = jsonArray.getJSONObject(i);

                                item.setTitle(JsonIsNullCheck(object, "TITLE"));
                                item.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                item.setPushNo(JsonIntIsNullCheck(object, "PUSH_NO"));
                                item.setContents(JsonIsNullCheck(object, "CONTENTS"));
                                item.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                                item.setKeyNo(JsonIsNullCheck(object,"KEY_NO"));
                                item.setChannelId(JsonIsNullCheck(object,"CHANNEL_ID"));
                                item.setReadFlag(JsonIntIsNullCheck(object,"READ_FLAG"));

                                adapter.addItem(item);

                            }
                            if (paging == 1){
                                listView.setAdapter(adapter);
                            }
                            listView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            mLockListView = false;
                        }

                    }

            }catch (JSONException e){

            }
        }
    }

    public static class ReadAlarmUpdateNetwork extends AsyncTask<String, String, String> {

        String response = "";
        String TAG = "PushAlarmListActivity";
        int status;
        String mUrl = "";

        public ReadAlarmUpdateNetwork(int status) {
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());

            if (status == 1){
                body.add("USER_NO", String.valueOf(userItem.getUserNo()));
                body.add("KEY_NO", strings[0]);
                body.add("CHANNEL_ID", strings[1]);
                mUrl = Utils.Server.updateAlarmReadFlag();
            }else {
                body.add("PUSH_NO", strings[0]);
                mUrl = Utils.Server.updateAlarmReadLocalFlag();
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, mUrl, body.build());
                Log.i(TAG,"ReadAlarmUpdateNetwork : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);

            }catch (JSONException e){

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (beforeIntent.hasExtra("push")) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        super.onBackPressed();
    }


}