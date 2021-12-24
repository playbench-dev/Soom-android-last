package com.kmw.soom2.ex.Drug;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Activity.DrugCompleteListActivity;
import com.kmw.soom2.DrugControl.DrugFragment;
import com.kmw.soom2.DrugControl.Fragment.DrugAlarmFragment;
import com.kmw.soom2.DrugControl.Fragment.NewDrugListFragment;
import com.kmw.soom2.Home.Activitys.CoachMark.MedicineCoachMarkActivity;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTypeItem;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_TYPE_CALL_LIST;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_TYPE_LIST;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class exDrugFragment extends Fragment {

    private String TAG = "exDrugFragment";
    TabLayout tabLayout;
    ImageView imgAlarm;

    LinearLayout linPrevMedicine;

    Fragment mEexDrugListFragment,mEexDrugAlarmFragment;
    FragmentManager fragmentManager;
    private String mTag                         = "";

    public exDrugFragment() {
    }

    public static exDrugFragment newInstance(String mTag){

        exDrugFragment drugFragment = new exDrugFragment();

        Bundle args = new Bundle();
        args.putString("mTag",mTag);
        drugFragment.setArguments(args);
        return drugFragment;
    }

    public String getInstance(String mKey){
        if (getArguments().getString(mKey) == null){
            return null;
        }
        return getArguments().getString(mKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,"onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG,"onCreateView");
        View v = inflater.inflate(R.layout.fragment_new_drug_control_main, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.drug_control_tab_layout_container);
        imgAlarm = (ImageView)v.findViewById(R.id.img_drug_control_alarm);

        // memo: 2021-01-13 김지훈 수정 시작
        linPrevMedicine = (LinearLayout) v.findViewById(R.id.lin_drug_control_prev_medicine);
        // memo: 2021-01-13 김지훈 수정 종료
        tabLayout.addTab(tabLayout.newTab().setText("약 리스트"));
        tabLayout.addTab(tabLayout.newTab().setText("약 알람"));

        fragmentManager = getChildFragmentManager();

        mTag = getInstance("mTag");

        imgAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
                startActivity(intent);
            }
        });
        // memo: 2021-01-13 김지훈 수정 시작
        linPrevMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        if(mEexDrugListFragment == null) {
            mEexDrugListFragment = new exDrugListFragment();
            fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mEexDrugListFragment).commit();
        }
        if (mEexDrugListFragment != null) fragmentManager.beginTransaction().show(mEexDrugListFragment).commit();
        if(mEexDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mEexDrugAlarmFragment).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    if(mEexDrugListFragment == null) {
                        mEexDrugListFragment = new exDrugListFragment();
                        fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mEexDrugListFragment).commit();
                    }
                    if (mEexDrugListFragment != null) fragmentManager.beginTransaction().show(mEexDrugListFragment).commit();
                    if(mEexDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mEexDrugAlarmFragment).commit();
                }else{
                    if(mEexDrugAlarmFragment == null) {
                        mEexDrugAlarmFragment = new exDrugAlarmFragment();
                        fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mEexDrugAlarmFragment).commit();
                    }
                    if (mEexDrugAlarmFragment != null) fragmentManager.beginTransaction().show(mEexDrugAlarmFragment).commit();
                    if(mEexDrugListFragment != null) fragmentManager.beginTransaction().hide(mEexDrugListFragment).commit();
                }
                ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                View tabViewChild = tabView.getChildAt(1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
                ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.color343434));
                ((TextView) tabViewChild).setTypeface(typeface);
            }
            @Override

            public void onTabUnselected(TabLayout.Tab tab) {
                ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                View tabViewChild = tabView.getChildAt(1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_medium);
                ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.color5c5c5c));
                ((TextView) tabViewChild).setTypeface(typeface);
            }
            @Override

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(0);
        View tabViewChild = tabView.getChildAt(1);
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
        ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.color343434));
        ((TextView) tabViewChild).setTypeface(typeface);

        Log.i(TAG,"mTag : " + mTag);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
    }

}

