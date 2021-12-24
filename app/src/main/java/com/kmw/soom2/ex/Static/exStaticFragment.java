package com.kmw.soom2.ex.Static;

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
import com.kmw.soom2.Reports.Activitys.StaticAsthmaActivity;
import com.kmw.soom2.Reports.Activitys.StaticBreathActivity;
import com.kmw.soom2.Reports.Activitys.StaticMedicineActivity;
import com.kmw.soom2.Reports.Activitys.TotalStaticActivity;
import com.kmw.soom2.Reports.Item.HistoryItems;
import com.kmw.soom2.Reports.StaticsFragment;
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

public class exStaticFragment extends Fragment implements View.OnClickListener {

    private String TAG = "StaticsFragment";
    ImageView imgAlarm;
    LinearLayout linMedicine,linSymptom,linBreath,linAsthma,linStatic;
    TextView imgMedicine,imgSymptom,imgBreath,imgAsthma;

    String response;

    ProgressDialog progressDialog;
    private String mTag                         = "";
    boolean medicineFlag = false, symptomFlag = false, breathFlag = false, asthmaFlag = false;

    public exStaticFragment() {
        // Required empty public constructor
    }

    public static exStaticFragment newInstance(String mTag){

        exStaticFragment staticsFragment = new exStaticFragment();

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
        View v = inflater.inflate(R.layout.fragment_ex_static, container, false);

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

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_statics_alarm : {

                break;
            }
            case R.id.lin_statics_medicine : {
                Intent i = new Intent(getActivity(), exStaticMedicineActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_symptom : {
                Intent i = new Intent(getActivity(), exStaticSymptomActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_breath : {
                Intent i = new Intent(getActivity(), exStaticBreathActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_asthma : {
                Intent i = new Intent(getActivity(), exStaticActActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_statics_static : {
                Intent i = new Intent(getActivity(), exStaticTotalActivity.class);
                startActivity(i);
                break;
            }
        }
    }

}
