package com.kmw.soom2.ex.Drug;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.AlarmReceiver;
import com.kmw.soom2.Common.DbOpenHelper;
import com.kmw.soom2.Common.DividerItemDecoration;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Activity.DrugAlarmInsertActivity;
import com.kmw.soom2.DrugControl.Adapter.DrugAlarmRecyclerViewAdapter;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class exDrugAlarmFragment extends Fragment implements View.OnClickListener {

    private String TAG = "DrugAlarmFragment";
    Toolbar linAlarmPlus;
    LinearLayout linNoItem;
    RecyclerView linAlarmListParent;

    public exDrugAlarmFragment() {
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

        linAlarmPlus = (Toolbar) view.findViewById(R.id.lin_drug_alarm_plus);
        linAlarmListParent = (RecyclerView) view.findViewById(R.id.lin_drug_alarm_parent);
        linNoItem = (LinearLayout)view.findViewById(R.id.lin_drug_alarm_no_item);

        linAlarmPlus.setOnClickListener(this);

        linAlarmListParent.setVisibility(View.GONE);
        linNoItem.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_drug_alarm_plus: {
                Intent i = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivityForResult(i, 3333);
                break;
            }
        }
    }
}
