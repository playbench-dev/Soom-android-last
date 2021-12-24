package com.kmw.soom2.ex.Community;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.NewCommunitySearchActivity;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_UNREAD_CALL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_MENU_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LV_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_PARENT_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECTABLE;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;

public class exCommunityFragment extends Fragment implements View.OnClickListener , AsyncResponse {

    private String TAG = "exCommunityFragment";
    ImageView imgAlarm;
    TabLayout tabLayoutMain;
    ImageView imgCommunityWrite,imgCommunitySearch;

    Context context;
    public static boolean firstCheck = false;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTagArrayList = new ArrayList<>();
    FragmentManager fragmentManager;

    private String mTag = "";

    public exCommunityFragment() {
        // Required empty public constructor
    }

    public static exCommunityFragment newInstance(String mTag){

        exCommunityFragment newCommunityFragment = new exCommunityFragment();

        Bundle args = new Bundle();
        args.putString("mTag",mTag);
        newCommunityFragment.setArguments(args);
        return newCommunityFragment;
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
        pref    = getActivity().getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor  = pref.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ex_community, container, false);
        v.setTag("communityF");
        tabLayoutMain = (TabLayout) v.findViewById(R.id.tab_community_main_category);
        imgCommunityWrite = (ImageView) v.findViewById(R.id.img_community_write);
        imgCommunitySearch = (ImageView) v.findViewById(R.id.img_community_search);

        mTag = getInstance("mTag");

        fragmentManager = getActivity().getSupportFragmentManager();

        imgCommunityWrite.setOnClickListener(this);
        imgCommunitySearch.setOnClickListener(this);

        tabLayoutMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (fragmentArrayList.size() > 0){
                    if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(tab.getPosition())) == null){
                        fragmentManager.beginTransaction().add(R.id.fragment_new_community_layout, fragmentArrayList.get(tab.getPosition()),fragmentTagArrayList.get(tab.getPosition())).commit();
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tab.getPosition())).commit();
                    }else{
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tab.getPosition())).commit();
                    }

                    for (int i = 0; i < fragmentArrayList.size(); i++){
                        if (i != tab.getPosition()){
                            if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(i)) != null){
                                fragmentManager.beginTransaction().hide(fragmentArrayList.get(i)).commit();
                            }
                        }else{
                            ViewGroup mainView = (ViewGroup) tabLayoutMain.getChildAt(0);
                            ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                            View tabViewChild = tabView.getChildAt(1);
                            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
                            ((TextView) tabViewChild).setTypeface(typeface);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i(TAG,"tab position : " + tab.getPosition());
                ViewGroup mainView = (ViewGroup) tabLayoutMain.getChildAt(0);
                ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                View tabViewChild = tabView.getChildAt(1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_medium_regular);
                ((TextView) tabViewChild).setTypeface(typeface);
            }

            @Override

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        NetWorkCall(COMMUNITY_MENU_LIST);

        return v;
    }

    public void NetWorkCall(String mCode){
        if (mCode.equals(COMMUNITY_MENU_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(COMMUNITY_MENU_LIST)){
                try {
                    COMMUNITY_MUNU_NO_LIST = new ArrayList<>();
                    COMMUNITY_LV_LIST = new ArrayList<>();
                    COMMUNITY_NAME_LIST = new ArrayList<>();
                    COMMUNITY_PARENT_LIST = new ArrayList<>();
                    COMMUNITY_SELECTABLE = new ArrayList<>();
                    COMMUNITY_POPULARUTY = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(mResult);

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    int cnt = 0;

                    fragmentArrayList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++){
                        COMMUNITY_MUNU_NO_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"MENU_NO"));
                        COMMUNITY_LV_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"LV"));
                        COMMUNITY_NAME_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"NAME"));
                        COMMUNITY_PARENT_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"PARENT"));
                        COMMUNITY_SELECTABLE.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"SELECTABLE"));
                        COMMUNITY_POPULARUTY.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"POPULARITY"));
                        if (JsonIsNullCheck(jsonArray.getJSONObject(i),"LV").equals("1")){
                            cnt++;
                            tabLayoutMain.addTab(tabLayoutMain.newTab().setText(JsonIsNullCheck(jsonArray.getJSONObject(i),"NAME")));
                            fragmentArrayList.add(new exMiddleFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i),COMMUNITY_NAME_LIST.get(i)));
                            fragmentTagArrayList.add(COMMUNITY_NAME_LIST.get(i));
                        }
                    }

                    if (mTag == null || mTag.length() == 0 || mTag.equals("write") || mTag.equals("search")){
                        if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(0)) == null){
                            fragmentManager.beginTransaction().add(R.id.fragment_new_community_layout, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commit();
                            fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commit();
                        }else{
                            fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commit();
                        }
                    }

                    ViewGroup mainView = (ViewGroup) tabLayoutMain.getChildAt(0);
                    ViewGroup tabView = null;

                    if (mTag == null || mTag.length() == 0 || mTag.equals("write") || mTag.equals("search")){
                        tabLayoutMain.getTabAt(0).select();
                        tabView = (ViewGroup) mainView.getChildAt(0);
                    }else{
                        tabLayoutMain.getTabAt(Integer.parseInt(mTag)).select();
                        tabView = (ViewGroup) mainView.getChildAt(Integer.parseInt(mTag));
                    }
                    View tabViewChild = tabView.getChildAt(1);
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
                    ((TextView) tabViewChild).setTypeface(typeface);

                    if (mTag.equals("write")){
                        imgCommunityWrite.performClick();
                    }else if (mTag.equals("search")){
                        imgCommunitySearch.performClick();
                    }

                }catch (JSONException e){

                }
            }else if (mCode.equals(ALARM_UNREAD_CALL)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);

                    if (JsonIsNullCheck(jsonObject,"FLAG").equals("2")) {
                        imgAlarm.setImageResource(R.drawable.ic_alarm_on);
                    }else {
                        imgAlarm.setImageResource(R.drawable.ic_alarm_off);
                    }
                }catch (JSONException e){

                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_community_write: {
                Intent intent = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.img_community_search: {
                Intent intent = new Intent(getActivity(), NewCommunitySearchActivity.class);
                intent.putExtra("guest",true);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}

