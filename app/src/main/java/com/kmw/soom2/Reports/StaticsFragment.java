package com.kmw.soom2.Reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Activitys.NewStaticSymptomActivity;
import com.kmw.soom2.Reports.Activitys.TotalStaticActivity;
import com.kmw.soom2.Reports.Item.HistoryItems;
import com.kmw.soom2.Reports.Activitys.StaticAsthmaActivity;
import com.kmw.soom2.Reports.Activitys.StaticBreathActivity;
import com.kmw.soom2.Reports.Activitys.StaticMedicineActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;


public class StaticsFragment extends Fragment implements View.OnClickListener {

    private String TAG = "StaticsFragment";
    ImageView imgAlarm;
    LinearLayout linMedicine,linSymptom,linBreath,linAsthma,linStatic;
    TextView imgMedicine,imgSymptom,imgBreath,imgAsthma;

    String response;

    ProgressDialog progressDialog;
    private String mTag                         = "";
    boolean medicineFlag = false, symptomFlag = false, breathFlag = false, asthmaFlag = false;

    public StaticsFragment() {
        // Required empty public constructor
    }

    public static StaticsFragment newInstance(String mTag){

        StaticsFragment staticsFragment = new StaticsFragment();

        Bundle args = new Bundle();
        args.putString("mTag",mTag);
        staticsFragment.setArguments(args);
        return staticsFragment;
    }

    public String getInstance(String mKey){
        if (getArguments().getString(mKey) == null){
            return null;
        }
        return getArguments().getString(mKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statics, container, false);

        imgAlarm = (ImageView)v.findViewById(R.id.img_statics_alarm);
        linMedicine = (LinearLayout)v.findViewById(R.id.lin_statics_medicine);
        linSymptom = (LinearLayout)v.findViewById(R.id.lin_statics_symptom);
        linBreath = (LinearLayout)v.findViewById(R.id.lin_statics_breath);
        linAsthma = (LinearLayout)v.findViewById(R.id.lin_statics_asthma);
        linStatic = (LinearLayout)v.findViewById(R.id.lin_statics_static);

        NullCheck(getActivity());

        linMedicine.setOnClickListener(this);
        linSymptom.setOnClickListener(this);
        linBreath.setOnClickListener(this);
        linAsthma.setOnClickListener(this);
        linStatic.setOnClickListener(this);

        imgAlarm.setOnClickListener(this);

        progressDialog = new ProgressDialog(getActivity(),R.style.MyTheme);

        mTag = getInstance("mTag");

        if (mTag == null || mTag.length() == 0){

        }else{
            if (mTag.equals("medicine")){
                linMedicine.performClick();
            }else if (mTag.equals("symptom")){
                linSymptom.performClick();
            }else if (mTag.equals("act")){
                linBreath.performClick();
            }else if (mTag.equals("pef")){
                linAsthma.performClick();
            }else if (mTag.equals("total")){
                linStatic.performClick();
            }
        }

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_statics_alarm : {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.lin_statics_medicine : {
                Intent i = new Intent(getActivity(), StaticMedicineActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_symptom : {
//                Intent i = new Intent(getActivity(), StaticSymptomActivity.class);
                Intent i = new Intent(getActivity(), NewStaticSymptomActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_breath : {
                Intent i = new Intent(getActivity(), StaticBreathActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_asthma : {
                Intent i = new Intent(getActivity(), StaticAsthmaActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_static : {
                Intent i = new Intent(getActivity(), TotalStaticActivity.class);
                startActivity(i);
                break;
            }
        }
    }

    private String returnDateAfterToday(String stDay) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
//        Date date = new Date(calendar.getTimeInMillis());
//        String toDay = format.format(date);
        long today = calendar.getTimeInMillis();

        try {
            long lastDay = format.parse(stDay).getTime();
            long differ = today - lastDay;

            return "" + (differ/(24*60*60*1000));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public class SelectHistoryNetWork extends AsyncTask<String, String, String> {

        int status;
        String mUrl;

        public SelectHistoryNetWork(int status) {
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (status == 1){
                progressDialog.show();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (status == 1){
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
                        Collections.sort(medicineHistory, new Comparator<HistoryItems>() {
                            @Override
                            public int compare(HistoryItems t0, HistoryItems t1) {
                                return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                            }
                        });
                        Collections.sort(symptomHistory, new Comparator<HistoryItems>() {
                            @Override
                            public int compare(HistoryItems t0, HistoryItems t1) {
                                return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                            }
                        });
                        Collections.sort(actHistory, new Comparator<HistoryItems>() {
                            @Override
                            public int compare(HistoryItems t0, HistoryItems t1) {
                                return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                            }
                        });
                        Collections.sort(pefHistory, new Comparator<HistoryItems>() {
                            @Override
                            public int compare(HistoryItems t0, HistoryItems t1) {
                                return t0.getRegisterDt().compareTo(t1.getRegisterDt());
                            }
                        });


                        if (medicineHistory.size() > 0) {
                            if (returnDateAfterToday(medicineHistory.get(medicineHistory.size() - 1).getRegisterDt()) != "") {
                                medicineFlag = true;
                            }else {
                                medicineFlag = false;
                            }
                        }else {
                            medicineFlag = false;
                        }
                        if (symptomHistory.size() > 0) {
                            if (returnDateAfterToday(symptomHistory.get(symptomHistory.size() - 1).getRegisterDt()) != "") {
                                symptomFlag = true;
                            }else {
                                symptomFlag = false;
                            }
                        }else {
                            symptomFlag = false;
                        }
                        if (pefHistory.size() > 0) {
                            if (returnDateAfterToday(pefHistory.get(pefHistory.size() - 1).getRegisterDt()) != "") {
                                breathFlag = true;
                            }else {
                                breathFlag = false;
                            }
                        }else {
                            breathFlag = false;
                        }
                        if (actHistory.size() > 0) {
                            if (returnDateAfterToday(actHistory.get(actHistory.size() - 1).getRegisterDt()) != "") {
                                asthmaFlag = true;
                            }else {
                                asthmaFlag = false;
                            }
                        }else {
                            asthmaFlag = false;
                        }
                    }else{
                        if (JsonIsNullCheck(jsonObject,"FLAG").equals("2")) {
                            imgAlarm.setImageResource(R.drawable.ic_alarm_on);
                        }else {
                            imgAlarm.setImageResource(R.drawable.ic_alarm_off);
                        }
                    }
                }catch (JSONException e){
//                isNullMedicineListData();

                }
            }

            progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", ""+Utils.userItem.getUserNo()).build();

            if (status == 1){
                mUrl = Utils.Server.selectHomeFeedList();
            }else{
                mUrl = Utils.Server.selectUnreadMessage();
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, mUrl, body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        new SelectHistoryNetWork(2).execute();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getVisibleFragment() instanceof StaticsFragment){
            if (requestCode == 1122){
                if (resultCode == RESULT_OK){
//                    new SelectHistoryNetWork(2).execute();
                }
            }
        }
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}