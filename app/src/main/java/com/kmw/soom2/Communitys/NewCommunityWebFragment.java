package com.kmw.soom2.Communitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Communitys.Activitys.NewCommunitySearchActivity;
import com.kmw.soom2.Communitys.Fragments.NewCommunityMiddleWebFragment;
import com.kmw.soom2.R;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_MENU_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LV_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_PARENT_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECTABLE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_WRITE_REFRESH;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class NewCommunityWebFragment extends Fragment implements AsyncResponse, View.OnClickListener {

    private String TAG = "NewCommunityWebFragment";
    private Context context;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private TabLayout tabLayout;
    private ImageView imgWrite;
    private ImageView imgSearch;
    private String mTag = "";

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTagArrayList = new ArrayList<>();
    FragmentManager fragmentManager;

    String menuNo = "";
    String menuTitle = "";

    ArrayList<String> menuNoList = new ArrayList<>();
    ArrayList<String> menuTitleList = new ArrayList<>();

    int position = 0;

    public NewCommunityWebFragment() {
        // Required empty public constructor
    }

    public static NewCommunityWebFragment newInstance(String mTag){

        NewCommunityWebFragment newCommunityFragment = new NewCommunityWebFragment();

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
        View v = inflater.inflate(R.layout.fragment_new_community_web, container, false);

        NullCheck(getActivity());

        mTag = getInstance("mTag");

        tabLayout = v.findViewById(R.id.tab_community_main_category);
        imgWrite = v.findViewById(R.id.img_community_write);
        imgSearch = v.findViewById(R.id.img_community_search);

        fragmentManager = getActivity().getSupportFragmentManager();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (fragmentArrayList.size() > 0){
                    position = tab.getPosition();
                    if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(tab.getPosition())) == null){
                        fragmentManager.beginTransaction().add(R.id.fragment_new_community_layout, fragmentArrayList.get(tab.getPosition()),fragmentTagArrayList.get(tab.getPosition())).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tab.getPosition())).commitAllowingStateLoss();
                    }else{
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tab.getPosition())).commitAllowingStateLoss();
                    }

                    menuNo = menuNoList.get(tab.getPosition());
                    menuTitle = menuTitleList.get(tab.getPosition());

                    for (int i = 0; i < fragmentArrayList.size(); i++){
                        if (i != tab.getPosition()){
                            if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(i)) != null){
                                fragmentManager.beginTransaction().hide(fragmentArrayList.get(i)).commitAllowingStateLoss();
                            }
                        }else{
                            ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
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
                ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                View tabViewChild = tabView.getChildAt(1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_medium_regular);
                ((TextView) tabViewChild).setTypeface(typeface);
            }

            @Override

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        imgSearch.setOnClickListener(this);
        imgWrite.setOnClickListener(this);

        if (COMMUNITY_MUNU_NO_LIST != null){
            for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                if (COMMUNITY_LV_LIST.get(i).equals("1")){
                    tabLayout.addTab(tabLayout.newTab().setText(COMMUNITY_NAME_LIST.get(i)));
                    fragmentArrayList.add(new NewCommunityMiddleWebFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i),COMMUNITY_NAME_LIST.get(i)));
                    fragmentTagArrayList.add(COMMUNITY_NAME_LIST.get(i));
                    menuNoList.add(COMMUNITY_MUNU_NO_LIST.get(i));
                    menuTitleList.add(COMMUNITY_NAME_LIST.get(i));
                }
            }

            if (mTag == null || mTag.length() == 0 || mTag.equals("write") || mTag.equals("search")){
                if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(0)) == null){
                    fragmentManager.beginTransaction().add(R.id.fragment_new_community_layout, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                }else{
                    fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                }
                menuNo = menuNoList.get(0);
                menuTitle = menuTitleList.get(0);
            }else{
                fragmentManager.beginTransaction().add(R.id.fragment_new_community_layout, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                menuNo = menuNoList.get(0);
                menuTitle = menuTitleList.get(0);
            }

            ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
            ViewGroup tabView = null;
            tabView = (ViewGroup) mainView.getChildAt(tabLayout.getSelectedTabPosition());

            if (mTag == null || mTag.length() == 0 || mTag.equals("write") || mTag.equals("search")){
                tabLayout.getTabAt(0).select();
                tabView = (ViewGroup) mainView.getChildAt(0);
                menuNo = menuNoList.get(0);
                menuTitle = menuTitleList.get(0);
            }else{
                tabLayout.getTabAt(Integer.parseInt(mTag)).select();
                tabView = (ViewGroup) mainView.getChildAt(Integer.parseInt(mTag));
                menuNo = menuNoList.get(Integer.parseInt(mTag));
                menuTitle = menuTitleList.get(Integer.parseInt(mTag));
            }
            View tabViewChild = tabView.getChildAt(1);
            Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
            ((TextView) tabViewChild).setTypeface(typeface);

            if (mTag.equals("write")){
                imgWrite.performClick();
            }else if (mTag.equals("search")){
                imgSearch.performClick();
            }
        }else{
            NetWorkCall(COMMUNITY_MENU_LIST);
        }

        Log.i(TAG,"time : " + new SimpleDateFormat("mm:ss").format(new Date(System.currentTimeMillis())));
        return v;
    }

    public void NetWorkCall(String mCode){
        if (mCode.equals(COMMUNITY_MENU_LIST)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                    Log.i(TAG,"mResult : " + mResult);

                    JSONObject jsonObject = new JSONObject(mResult);

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    int cnt = 0;

                    for (int i = 0; i < jsonArray.length(); i++){
                        COMMUNITY_MUNU_NO_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"MENU_NO"));
                        COMMUNITY_LV_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"LV"));
                        COMMUNITY_NAME_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"NAME"));
                        COMMUNITY_PARENT_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"PARENT"));
                        COMMUNITY_SELECTABLE.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"SELECTABLE"));
                        COMMUNITY_POPULARUTY.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"POPULARITY"));
                        if (JsonIsNullCheck(jsonArray.getJSONObject(i),"LV").equals("1")){
                            cnt++;
                            tabLayout.addTab(tabLayout.newTab().setText(JsonIsNullCheck(jsonArray.getJSONObject(i),"NAME")));
                            fragmentArrayList.add(new NewCommunityMiddleWebFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i),COMMUNITY_NAME_LIST.get(i)));
                            fragmentTagArrayList.add(COMMUNITY_NAME_LIST.get(i));
                            menuNoList.add(COMMUNITY_MUNU_NO_LIST.get(i));
                            menuTitleList.add(COMMUNITY_NAME_LIST.get(i));
                        }
                    }

                    if (mTag == null || mTag.length() == 0 || mTag.equals("write") || mTag.equals("search")){
                        if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(0)) == null){
                            fragmentManager.beginTransaction().add(R.id.fragment_new_community_layout, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                            fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                        }else{
                            fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                        }
                        menuNo = menuNoList.get(0);
                        menuTitle = menuTitleList.get(0);
                    }else{
                        fragmentManager.beginTransaction().add(R.id.fragment_new_community_layout, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                        menuNo = menuNoList.get(0);
                        menuTitle = menuTitleList.get(0);
                    }

                    ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                    ViewGroup tabView = null;
                    tabView = (ViewGroup) mainView.getChildAt(tabLayout.getSelectedTabPosition());

                    if (mTag == null || mTag.length() == 0 || mTag.equals("write") || mTag.equals("search")){
                        tabLayout.getTabAt(0).select();
                        tabView = (ViewGroup) mainView.getChildAt(0);
                        menuNo = menuNoList.get(0);
                        menuTitle = menuTitleList.get(0);
                    }else{
                        tabLayout.getTabAt(Integer.parseInt(mTag)).select();
                        tabView = (ViewGroup) mainView.getChildAt(Integer.parseInt(mTag));
                        menuNo = menuNoList.get(Integer.parseInt(mTag));
                        menuTitle = menuTitleList.get(Integer.parseInt(mTag));
                    }
                    View tabViewChild = tabView.getChildAt(1);
                    Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
                    ((TextView) tabViewChild).setTypeface(typeface);

                    if (mTag.equals("write")){
                        imgWrite.performClick();
                    }else if (mTag.equals("search")){
                        imgSearch.performClick();
                    }
                }catch (JSONException e){

                }
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == COMMUNITY_WRITE_REFRESH) {
                if (resultCode == RESULT_OK) {
                    //write 작성시 lv로 나누기
                    editor.putBoolean("communityWrite",true);
                    editor.apply();
//                    tabLayout.getTabAt(0).select();
                    fragmentArrayList.get(position).onActivityResult(COMMUNITY_WRITE_REFRESH,RESULT_OK,null);
                }
            }else if (requestCode == 2222){
                if (resultCode == RESULT_OK){
                    NetWorkCall(COMMUNITY_MENU_LIST);
                }
            }else if (requestCode == 1122){
                if (resultCode == RESULT_OK){
    //                    NetWorkCall(ALARM_UNREAD_CALL);
                }
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_community_search : {
                Intent intent = new Intent(getActivity(), NewCommunitySearchActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.img_community_write : {
                Intent intent = new Intent(getActivity(), CommunityWriteActivity.class);
                intent.putExtra("menuWriteNo",menuNo);
                intent.putExtra("menuWriteTitle",menuTitle);
                startActivityForResult(intent,COMMUNITY_WRITE_REFRESH);
            }
        }
    }
}
