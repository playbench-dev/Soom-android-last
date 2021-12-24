package com.kmw.soom2.Reports.Activitys;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Adapter.StaticBreathListAdapter;
import com.kmw.soom2.Reports.Item.HistoryItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class StaticBreathActivity extends AppCompatActivity implements View.OnClickListener  {

    final String TAG = "StaticBreathActivity";
    StaticBreathListAdapter adapter;
    ListView listView;
    TextView btnBack;

    String response;
    ArrayList<HistoryItems> historyItems;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_breath);

        findViewByIds();

        NullCheck(this);

        progressDialog = new ProgressDialog(StaticBreathActivity.this,R.style.MyTheme);
        progressDialog.setCancelable(false);
//        new SelectHistoryNetWork().execute();
        new SelectPefHistoryListNetWork().execute();
    }

    void findViewByIds() {
        listView = findViewById(R.id.breath_static_listView);
        btnBack = findViewById(R.id.txt_statics_detail_back);

        adapter = new StaticBreathListAdapter(this);

        btnBack.setOnClickListener(this);
    }
    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }
    public class SelectPefHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("CATEGORY", "22").add("USER_NO", ""+Utils.userItem.getUserNo()).build();

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
                    new OneButtonDialog(StaticBreathActivity.this, "보고서", "기록이 없어\n보고서를 만들지 못했습니다.", "확인");
                }else{
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1) {
                            HistoryItems historyItem = new HistoryItems();

                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));

                            historyItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                            historyItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));   // 1 사용, 2 미사용

                            historyItems.add(historyItem);
                        }
                    }
                    HashMap<String, ArrayList<HistoryItems>> map = setupHistoryData(historyItems);

                    TreeMap<String, ArrayList<HistoryItems>> treeMap = new TreeMap<>(map);

                    for (Map.Entry<String, ArrayList<HistoryItems>> entry : treeMap.descendingMap().entrySet()) {
                        String key = entry.getKey();
                        ArrayList<HistoryItems> value = entry.getValue();
                        adapter.addItem(value, key);
                    }

                    listView.setAdapter(adapter);
                }
            }catch (JSONException e){
            }
            progressDialog.dismiss();
        }
    }

    public class SelectHistoryNetWork extends AsyncTask<String, String, String> {

        String mUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", ""+Utils.userItem.getUserNo()).build();

            mUrl = Utils.Server.selectHomeFeedList();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, mUrl, body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (JsonIsNullCheck(jsonObject,"result").equals("N")){
                    new OneButtonDialog(StaticBreathActivity.this, "보고서", "폐기능 기록이 없어\n" +
                            "보고서를 만들지 못했습니다.\n" +
                            "HOME에서 폐기능을 기록해주세요.", "확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {
                            onBackPressed();
                        }
                    });
                }else{
                    ArrayList<HistoryItems> items = new ArrayList<>();

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (JsonIntIsNullCheck(object,"ALIVE_FLAG") == 1) {
                            HistoryItems historyItem = new HistoryItems();

                            historyItem.setUserHistoryNo(JsonIntIsNullCheck(object,"USER_HISTORY_NO"));
                            historyItem.setUserNo(JsonIntIsNullCheck(object,"USER_NO"));
                            historyItem.setCategory(JsonIntIsNullCheck(object,"CATEGORY"));
                            historyItem.setRegisterDt(JsonIsNullCheck(object,"REGISTER_DT"));

                            historyItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                            historyItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                            historyItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                            historyItem.setKo(JsonIsNullCheck(object,"KO"));
                            historyItem.setEmergencyFlag(JsonIntIsNullCheck(object,"EMERGENCY_FLAG"));
                            historyItem.setUnit(JsonIsNullCheck(object,"UNIT"));

                            items.add(historyItem);
                        }
                    }

                    ArrayList<HistoryItems> medicineHistory = new ArrayList<>();
                    ArrayList<HistoryItems> symptomHistory = new ArrayList<>();
                    ArrayList<HistoryItems> pefHistory = new ArrayList<>();
                    ArrayList<HistoryItems> actHistory = new ArrayList<>();

                    for (int i = 0; i < items.size(); i++) {
                        if (items.get(i).getCategory() == 1) {
                            medicineHistory.add(items.get(i));
                        }else if (items.get(i).getCategory() > 10 && items.get(i).getCategory() < 20) {
                            symptomHistory.add(items.get(i));
                        }else if (items.get(i).getCategory() == 21) {
                            actHistory.add(items.get(i));
                        }else if (items.get(i).getCategory() == 22) {
                            pefHistory.add(items.get(i));
                        }
                    }

                    if (pefHistory.size() > 0){
                        new SelectPefHistoryListNetWork().execute();
                    }else{
                        new OneButtonDialog(StaticBreathActivity.this, "보고서", "폐기능 기록이 없어\n" +
                                "보고서를 만들지 못했습니다.\n" +
                                "HOME에서 폐기능을 기록해주세요.", "확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                onBackPressed();
                            }
                        });
                    }
                }

            }catch (JSONException e){
//                isNullMedicineListData();
            }
        }
    }

    public HashMap<String, ArrayList<HistoryItems>> setupHistoryData(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt().substring(0, 10))) {
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }else {
                map.put(datas.get(i).getRegisterDt().substring(0, 10), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }
        }

        return map;
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
