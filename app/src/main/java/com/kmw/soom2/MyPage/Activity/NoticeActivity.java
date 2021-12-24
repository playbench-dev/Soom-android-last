package com.kmw.soom2.MyPage.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.Adapter.PushAlarmListAdapter;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.MyPage.Adapter.NoticeAdapter;
import com.kmw.soom2.MyPage.Item.NoticeItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "NoticeActivity";
    TextView txtBack;
    ListView listView;
    NoticeAdapter noticeAdapter;
    String response;
    ProgressDialog progressDialog;
    Intent beforeIntent;
    String noticeNo = "", contents = "";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int paging = 1;
    private boolean lastItemVisibleFlag = false;
    private boolean mLockListView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        pref = getSharedPreferences(Utils.preferencesName,MODE_PRIVATE);
        editor = pref.edit();

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

        if (beforeIntent.hasExtra("push")){
            Intent intent = new Intent(NoticeActivity.this, NoticeDetailActivity.class);
            intent.putExtra("push", true);
            intent.putExtra("noticeNo", beforeIntent.getStringExtra("noticeNo"));
            startActivityForResult(intent,5555);
            overridePendingTransition(0,0);
        }else{
            new SelectNoticeListNetWork().execute("1");
        }
    }

    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_notice_back);
        listView = (ListView) findViewById(R.id.list_view_notice);

        txtBack.setOnClickListener(this);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
                    mLockListView = true;
                    paging++;
                    new SelectNoticeListNetWork().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+paging);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });
    }

    public class SelectNoticeListNetWork extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("Search_Page",strings[0]).add("Search_ShowCNT","15").build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectNoticeList(), body);
//                Log.i(TAG,"SelectNoticeListNetWork : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                try {

                    if (paging == 1){
                        noticeAdapter = new NoticeAdapter(NoticeActivity.this);
                    }

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

//                    Log.i(TAG,"noticeNo : " + beforeIntent.getStringExtra("noticeNo"));

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        Log.i(TAG,"object : " + object.toString());

                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            NoticeItem noticeItem = new NoticeItem();
                            noticeItem.setNo(JsonIsNullCheck(object, "NOTICE_NO"));
                            noticeItem.setNoticeFlag(JsonIntIsNullCheck(object, "NOTICE_FLAG"));
                            noticeItem.setTitle(JsonIsNullCheck(object, "TITLE"));
                            noticeItem.setContents(JsonIsNullCheck(object, "CONTENTS"));
                            noticeItem.setDate(JsonIsNullCheck(object, "CREATE_DT"));

                            noticeAdapter.addItem(noticeItem);
                        }
                    }

                    if (paging == 1){
                        listView.setAdapter(noticeAdapter);
                    }
                    listView.setVisibility(View.VISIBLE);
                    noticeAdapter.notifyDataSetChanged();
                    mLockListView = false;

                } catch (JSONException e) {

                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5555){
            if (resultCode == RESULT_OK){
//                new SelectNoticeListNetWork().execute("1");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (beforeIntent.hasExtra("push")){
            Intent communityIntent = new Intent(NoticeActivity.this,MainActivity.class);
            communityIntent.putExtra("notice", true);
            communityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(communityIntent);
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_notice_back: {
                onBackPressed();
                break;
            }
        }
    }
}
