package com.kmw.soom2.Reports.Activitys;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Adapter.StaticAsthmaListAdapter;
import com.kmw.soom2.Reports.Item.HistoryItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class StaticAsthmaActivity extends AppCompatActivity implements View.OnClickListener   {
    final String TAG = "StaticAsthmaActivity";
    ListView listView;
    TextView btnBack;
    StaticAsthmaListAdapter adapter;
    String response;
    ArrayList<HistoryItems> historyItems;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_asthma);

        findViewByIds();

        NullCheck(this);

        progressDialog = new ProgressDialog(StaticAsthmaActivity.this,R.style.MyTheme);
        progressDialog.setCancelable(false);
        new SelectActHistoryListNetWork().execute();
    }
    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    void findViewByIds() {
        listView = findViewById(R.id.asthma_static_listview);
        btnBack = findViewById(R.id.txt_statics_detail_back);
        btnBack.setOnClickListener(this);

        adapter = new StaticAsthmaListAdapter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_statics_detail_back: { // 뒤로 가기
                onBackPressed();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class SelectActHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("CATEGORY", "21").add("USER_NO", ""+Utils.userItem.getUserNo()).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedList(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            historyItems = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject,"result").equals("N")){
                    new OneButtonDialog(StaticAsthmaActivity.this, "보고서", "기록이 없어\n보고서를 만들지 못했습니다.", "확인");
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.i(TAG,"result : " + object.toString());
                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1) {
                            HistoryItems historyItem = new HistoryItems();

                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                            historyItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                            historyItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                            historyItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                            historyItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));

                            historyItems.add(historyItem);
                        }
                    }
                    for (int i = 0; i < historyItems.size(); i++) {
                        adapter.addItem(historyItems.get(i));
                    }
                    listView.setAdapter(adapter);
                }
            }catch (JSONException e){
            }
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
