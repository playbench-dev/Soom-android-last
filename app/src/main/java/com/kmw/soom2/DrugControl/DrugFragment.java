package com.kmw.soom2.DrugControl;

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

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.HttpClient;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Activity.DrugCompleteListActivity;
import com.kmw.soom2.DrugControl.Fragment.DrugAlarmFragment;
import com.kmw.soom2.DrugControl.Fragment.NewDrugListFragment;
import com.kmw.soom2.Home.Activitys.CoachMark.MedicineCoachMarkActivity;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTypeItem;
import com.kmw.soom2.R;
import com.google.android.material.tabs.TabLayout;
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


public class DrugFragment extends Fragment implements AsyncResponse {

    private String TAG = "DrugControlMainFragment";
    TabLayout tabLayout;
    ImageView imgAlarm;
    ImageView imgDrugComplete;

    // memo: 2021-01-13 김지훈 수정 시작
    LinearLayout linPrevMedicine;
    // memo: 2021-01-13 김지훈 수정 종

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");

    Fragment mDrugAlarmFragment,mDrugListFragment;
    FragmentManager fragmentManager;
    private String mTag                         = "";

    public DrugFragment() {
    }

    public static DrugFragment newInstance(String mTag){

        DrugFragment drugFragment = new DrugFragment();

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
        Log.i(TAG,"onViewCreated");
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,"onDestroyView");
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
        pref    = getActivity().getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor  = pref.edit();
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
                Intent intent = new Intent(getActivity(), DrugCompleteListActivity.class);
                startActivity(intent);
            }
        });
        // memo: 2021-01-13 김지훈 수정 종료
        if (pref.getInt("medicineRefresh",0) == 1){
            NetworkCall(MEDICINE_TYPE_CALL_LIST);
        }else{
            if (MEDICINE_TYPE_LIST == null){
                NetworkCall(MEDICINE_TYPE_CALL_LIST);
            }else{
                if(mDrugListFragment == null) {
                    mDrugListFragment = new NewDrugListFragment();
                    fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugListFragment).commitAllowingStateLoss();
                }
                if (mDrugListFragment != null) fragmentManager.beginTransaction().show(mDrugListFragment).commitAllowingStateLoss();
                if(mDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mDrugAlarmFragment).commitAllowingStateLoss();

                if (mTag.equals("search")){
                    Intent i = new Intent(getActivity(), MedicineSelectActivity.class);
                    startActivityForResult(i,1234);
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0){
                    if(mDrugListFragment == null) {
                        mDrugListFragment = new NewDrugListFragment();
                        fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugListFragment).commitAllowingStateLoss();
                    }
                    if (mDrugListFragment != null) fragmentManager.beginTransaction().show(mDrugListFragment).commitAllowingStateLoss();
                    if(mDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mDrugAlarmFragment).commitAllowingStateLoss();
                }else{
                    if(mDrugAlarmFragment == null) {
                        mDrugAlarmFragment = new DrugAlarmFragment();
                        fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugAlarmFragment).commitAllowingStateLoss();
                    }
                    if (mDrugAlarmFragment != null) fragmentManager.beginTransaction().show(mDrugAlarmFragment).commitAllowingStateLoss();
                    if(mDrugListFragment != null) fragmentManager.beginTransaction().hide(mDrugListFragment).commitAllowingStateLoss();
                }
                ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                View tabViewChild = tabView.getChildAt(1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
                ((TextView) tabViewChild).setTypeface(typeface);
                ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.color343434));
            }
            @Override

            public void onTabUnselected(TabLayout.Tab tab) {
                ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                View tabViewChild = tabView.getChildAt(1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_medium);
                ((TextView) tabViewChild).setTypeface(typeface);
                ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.color5c5c5c));
            }
            @Override

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(0);
        View tabViewChild = tabView.getChildAt(1);
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
        ((TextView) tabViewChild).setTypeface(typeface);
        ((TextView) tabViewChild).setTextColor(getResources().getColor(R.color.color343434));

        Log.i(TAG,"mTag : " + mTag);

//        if (mTag == null || mTag.length() == 0){
//
//        }else{
//            if (mTag.equals("alarm")){
//                tabLayout.getTabAt(1).select();
//                if(mDrugAlarmFragment == null) {
//                    mDrugAlarmFragment = new DrugAlarmFragment();
//                    fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugAlarmFragment).commitAllowingStateLoss();
//                }
//                if (mDrugAlarmFragment != null) fragmentManager.beginTransaction().show(mDrugAlarmFragment).commitAllowingStateLoss();
//                if(mDrugListFragment != null) fragmentManager.beginTransaction().hide(mDrugListFragment).commitAllowingStateLoss();
//            }else if (mTag.equals("search")){
//                Intent i = new Intent(getActivity(), MedicineSelectActivity.class);
//                startActivityForResult(i,1234);
//            }
//        }

//        if (pref.getInt("medicineCoachMark1",0) == 0 || pref.getInt("medicineCoachMark2",0) == 0){
//            Intent i = new Intent(getActivity(), MedicineCoachMarkActivity.class);
//            getActivity().overridePendingTransition(0,0);
//            startActivity(i);
//        }

        return v;
    }
    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    void NetworkCall(String mCode){
        if (mCode.equals(MEDICINE_TYPE_CALL_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mCode.equals(MEDICINE_TYPE_CALL_LIST)){
            MEDICINE_TYPE_LIST = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(mResult);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    MedicineTypeItem medicineTypeItem = new MedicineTypeItem();

                    medicineTypeItem.setMedicineTypeNo(JsonIntIsNullCheck(object, "MEDICINE_TYPE_NO"));
                    medicineTypeItem.setTypeImg(JsonIsNullCheck(object, "TYPE_IMG"));

                    MEDICINE_TYPE_LIST.add(medicineTypeItem);
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mTag == null || mTag.length() == 0){
                            if(mDrugListFragment == null) {
                                mDrugListFragment = new NewDrugListFragment();
                                fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugListFragment).commitAllowingStateLoss();
                            }
                            if (mDrugListFragment != null) fragmentManager.beginTransaction().show(mDrugListFragment).commitAllowingStateLoss();
                            if (mDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mDrugAlarmFragment).commitAllowingStateLoss();
                        }else {
                            if(mTag.equals("medicine")){
                                if(mDrugListFragment == null) {
                                    mDrugListFragment = new NewDrugListFragment();
                                    fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugListFragment).commitAllowingStateLoss();
                                }
                                if (mDrugListFragment != null) fragmentManager.beginTransaction().show(mDrugListFragment).commitAllowingStateLoss();
                                if (mDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mDrugAlarmFragment).commitAllowingStateLoss();
                            } else if (mTag.equals("alarm")) {
                                tabLayout.getTabAt(1).select();
                                if (mDrugAlarmFragment == null) {
                                    mDrugAlarmFragment = new DrugAlarmFragment();
                                    fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugAlarmFragment).commitAllowingStateLoss();
                                }
                                if (mDrugAlarmFragment != null)
                                    fragmentManager.beginTransaction().show(mDrugAlarmFragment).commitAllowingStateLoss();
                                if (mDrugListFragment != null)
                                    fragmentManager.beginTransaction().hide(mDrugListFragment).commitAllowingStateLoss();
                            }else{
                                if(mDrugListFragment == null) {
                                    mDrugListFragment = new NewDrugListFragment();
                                    fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugListFragment).commitAllowingStateLoss();
                                }
                                if (mDrugListFragment != null) fragmentManager.beginTransaction().show(mDrugListFragment).commitAllowingStateLoss();
                                if (mDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mDrugAlarmFragment).commitAllowingStateLoss();

                                if (mTag.equals("search")){
                                    Intent i = new Intent(getActivity(), MedicineSelectActivity.class);
                                    startActivityForResult(i,1234);
                                }
                            }


                        }
                    }
                },0);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        Log.i(TAG,"event code : " + event.getRequestCode());
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getVisibleFragment() instanceof DrugFragment){
            if (requestCode == 1234){
                Log.i(TAG,"code 들어옴");
                if (resultCode == RESULT_OK){
                    Log.i(TAG,"okkkkkkkkk");
                    if(mDrugListFragment == null) {
                        mDrugListFragment = new NewDrugListFragment();
                        fragmentManager.beginTransaction().add(R.id.view_pager_container_frag_drug_control_main, mDrugListFragment).commitAllowingStateLoss();
                    }else{
                        mDrugListFragment.onActivityResult(1234,RESULT_OK,null);
                    }
                    if (mDrugListFragment != null) fragmentManager.beginTransaction().show(mDrugListFragment).commitAllowingStateLoss();
                    if (mDrugAlarmFragment != null) fragmentManager.beginTransaction().hide(mDrugAlarmFragment).commitAllowingStateLoss();
                }
            }else if (requestCode == 2222){
                if (resultCode == RESULT_OK){
                    Log.i(TAG,"삭제");
                }
            }else if (requestCode == 1122){
                if (resultCode == RESULT_OK){
//                    new SelectMedicineHistoryListNetWork(2).execute();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        NullCheck(getActivity());
//        new SelectMedicineHistoryListNetWork(2).execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
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
