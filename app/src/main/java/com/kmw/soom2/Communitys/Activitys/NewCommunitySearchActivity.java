package com.kmw.soom2.Communitys.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Fragments.NewCommunitySearchChildFragment;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_MENU_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LV_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_PARENT_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECTABLE;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NEW_COMMUNITY_SEARCH;

public class NewCommunitySearchActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "NewCommunitySearchActivity";
    public static EditText edtSearch;
    private ImageView imgSearch;
    private TextView txtCancel;
    private TextView txtNoTagList;

    private LinearLayout linSearchVisible;
    private TextView txtTagAllRemove;
    private LinearLayout linTagListParent;
    private TabLayout tabLayout;
    private LinearLayout linFragmentVisible;
    private FrameLayout frameLayoutVisible;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTagArrayList = new ArrayList<>();
    FragmentManager fragmentManager;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private ArrayList<String> mTagList = new ArrayList<>();
    Typeface typefaceBold,typefaceMedium ;
    public static boolean mGuest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_community_search);

        pref = getSharedPreferences(Utils.preferencesName,MODE_PRIVATE);
        editor = pref.edit();

        if (getIntent() != null){
            if (getIntent().hasExtra("guest")){
                mGuest = true;
            }else{
                mGuest = false;
            }
        }else{
            mGuest = false;
        }

        typefaceBold = ResourcesCompat.getFont(this, R.font.notosanscjkkr_bold);
        typefaceMedium = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium);

        FindViewById();
    }

    void FindViewById(){
        edtSearch = findViewById(R.id.edt_new_community_search);
        imgSearch = findViewById(R.id.img_new_community_search);
        txtCancel = findViewById(R.id.txt_new_community_search_cancel);
        txtNoTagList = findViewById(R.id.txt_new_community_search_no_tag_list);
        linSearchVisible = findViewById(R.id.lin_new_community_tag_visible);
        txtTagAllRemove = findViewById(R.id.txt_new_community_search_tag_all_remove);
        linTagListParent = findViewById(R.id.lin_new_community_search_tag_parent);
        tabLayout = findViewById(R.id.tab_new_community_search_category);
        linFragmentVisible = findViewById(R.id.lin_new_community_fragment_visible);


        imgSearch.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        txtTagAllRemove.setOnClickListener(this);

        mTagList = mGetTagArrayPref("tagList");

        for (int i = 0; i < mTagList.size(); i++){
            mTagListItem(mTagList.get(i));
        }

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linSearchVisible.setVisibility(View.VISIBLE);
                linFragmentVisible.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (edtSearch.getText().toString().trim().length() > 0){
                        if (!mTagList.contains(edtSearch.getText().toString())){
                            mTagList.add(0,edtSearch.getText().toString());
                            mSetTagArrayPref("tagList",mTagList);
                            mTagList = new ArrayList<>();
                            mTagList = mGetTagArrayPref("tagList");
                            linTagListParent.removeAllViews();

                            for (int i = 0; i < mTagList.size(); i++){
                                mTagListItem(mTagList.get(i));
                            }
                        }else{
                            mTagList.remove(edtSearch.getText().toString());
                            mTagList.add(0,edtSearch.getText().toString());
                            mSetTagArrayPref("tagList",mTagList);

                            mTagList = new ArrayList<>();
                            mTagList = mGetTagArrayPref("tagList");
                            linTagListParent.removeAllViews();

                            for (int i = 0; i < mTagList.size(); i++){
                                mTagListItem(mTagList.get(i));
                            }
                        }

                        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(),0);
                        linSearchVisible.setVisibility(View.GONE);
                        linFragmentVisible.setVisibility(View.VISIBLE);
                        //memo 검색 커뮤니티 호출 추가
                        if (fragmentArrayList.size() == 0){
                            for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                                if (COMMUNITY_LV_LIST.get(i).equals("1")){
                                    if (COMMUNITY_MUNU_NO_LIST.get(i).equals("3")){
                                        fragmentArrayList.add(new NewCommunitySearchChildFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i), COMMUNITY_MUNU_NO_LIST.get(i),"전체"));
                                    }else{
                                        fragmentArrayList.add(new NewCommunitySearchChildFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i), COMMUNITY_MUNU_NO_LIST.get(i),"전체"));
                                    }
                                    fragmentTagArrayList.add(COMMUNITY_NAME_LIST.get(i));
                                }
                            }
                            fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                            fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                        }else{
                            if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(tabLayout.getSelectedTabPosition())) == null){
                                fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(tabLayout.getSelectedTabPosition()),fragmentTagArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                                fragmentManager.beginTransaction().show(fragmentArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                            }else{
                                fragmentManager.beginTransaction().show(fragmentArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                                fragmentArrayList.get(tabLayout.getSelectedTabPosition()).onActivityResult(NEW_COMMUNITY_SEARCH,RESULT_OK,null);
                            }
                        }
                        ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                        ViewGroup tabView = null;
                        tabView = (ViewGroup) mainView.getChildAt(tabLayout.getSelectedTabPosition());
                        View tabViewChild = tabView.getChildAt(1);
                        Typeface typeface = ResourcesCompat.getFont(NewCommunitySearchActivity.this, R.font.notosanscjkkr_bold);
                        ((TextView) tabViewChild).setTypeface(typeface);
                        return true;
                    }
                }
                return false;
            }
        });

        fragmentManager = getSupportFragmentManager();

        if (COMMUNITY_MUNU_NO_LIST != null){
            for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                if (COMMUNITY_LV_LIST.get(i).equals("1")){
                    tabLayout.addTab(tabLayout.newTab().setText(COMMUNITY_NAME_LIST.get(i)));
                }
            }
        }else{
            NetWorkCall(COMMUNITY_MENU_LIST);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (fragmentArrayList.size() > 0){
                    if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(tab.getPosition())) == null){
                        fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(tab.getPosition()),fragmentTagArrayList.get(tab.getPosition())).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tab.getPosition())).commitAllowingStateLoss();
                    }else{
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tab.getPosition())).commitAllowingStateLoss();
                        fragmentArrayList.get(tab.getPosition()).onActivityResult(NEW_COMMUNITY_SEARCH,RESULT_OK,null);
                    }

                    for (int i = 0; i < fragmentArrayList.size(); i++){
                        if (i != tab.getPosition()){
                            if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(i)) != null){
                                fragmentManager.beginTransaction().hide(fragmentArrayList.get(i)).commitAllowingStateLoss();
                            }
                        }else{
                            ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                            ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
                            View tabViewChild = tabView.getChildAt(1);
                            Typeface typeface = ResourcesCompat.getFont(NewCommunitySearchActivity.this, R.font.notosanscjkkr_bold);
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
                Typeface typeface = ResourcesCompat.getFont(NewCommunitySearchActivity.this, R.font.notosanscjkkr_medium);
                ((TextView) tabViewChild).setTypeface(typeface);
            }

            @Override

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void mTagListItem (String title){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_community_search_tag_list_item,null);
        TextView txtTitle = listView.findViewById(R.id.txt_new_community_search_tag_list_item_title);
        ImageView imgRemove = listView.findViewById(R.id.img_new_community_search_tag_list_item_remove);

        txtTitle.setText(title);

        View finalListView = listView;
        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagList.remove(title);
                linTagListParent.removeView(finalListView);
                mSetTagArrayPref("tagList",mTagList);
            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText(title);
                linSearchVisible.setVisibility(View.GONE);
                linFragmentVisible.setVisibility(View.VISIBLE);
                //memo 커뮤니티 호출 추가
                if (fragmentArrayList.size() == 0){
                    for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                        if (COMMUNITY_LV_LIST.get(i).equals("1")){
                            fragmentArrayList.add(new NewCommunitySearchChildFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i), COMMUNITY_MUNU_NO_LIST.get(i),"전체"));
                            fragmentTagArrayList.add(COMMUNITY_NAME_LIST.get(i));
                        }
                    }
                    fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                }else{
                    if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(tabLayout.getSelectedTabPosition())) == null){
                        fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(tabLayout.getSelectedTabPosition()),fragmentTagArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                    }else{
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                        fragmentArrayList.get(tabLayout.getSelectedTabPosition()).onActivityResult(NEW_COMMUNITY_SEARCH,RESULT_OK,null);
                    }
                }
                ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup tabView = null;
                tabView = (ViewGroup) mainView.getChildAt(tabLayout.getSelectedTabPosition());
                View tabViewChild = tabView.getChildAt(1);
                Typeface typeface = ResourcesCompat.getFont(NewCommunitySearchActivity.this, R.font.notosanscjkkr_bold);
                ((TextView) tabViewChild).setTypeface(typeface);
            }
        });

        linTagListParent.addView(listView);
    }

    private ArrayList<String> mGetTagArrayPref(String key){
        String json = pref.getString(key,null);
        ArrayList<String> mList = new ArrayList<>();

        if (json != null){
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++){
                    String tag = array.optString(i);
                    mList.add(tag);
                }
            }catch (JSONException e){

            }
        }
        if (mList.size() > 0){
            txtTagAllRemove.setVisibility(View.VISIBLE);
            txtNoTagList.setVisibility(View.GONE);
        }else{
            txtTagAllRemove.setVisibility(View.GONE);
            txtNoTagList.setVisibility(View.VISIBLE);
        }
        return mList;
    }

    private void mSetTagArrayPref(String key, ArrayList<String> values){
        JSONArray array = new JSONArray();
        if (values.size() > 10){
            for (int i = 0; i < 10; i++){
                array.put(values.get(i));
            }
        }else{
            for (int i = 0; i < values.size(); i++){
                array.put(values.get(i));
            }
        }

        if (!values.isEmpty()){
            editor.putString(key, array.toString());
            txtTagAllRemove.setVisibility(View.VISIBLE);
            txtNoTagList.setVisibility(View.GONE);
        }else{
            editor.putString(key, null);
            txtTagAllRemove.setVisibility(View.GONE);
            txtNoTagList.setVisibility(View.VISIBLE);
        }
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_new_community_search : {
                if (edtSearch.getText().toString().trim().length() > 0){
                    mTagList.add(edtSearch.getText().toString());
                    mSetTagArrayPref("tagList",mTagList);
                    mTagList = new ArrayList<>();
                    mTagList = mGetTagArrayPref("tagList");
                    linTagListParent.removeAllViews();

                    for (int i = 0; i < mTagList.size(); i++){
                        mTagListItem(mTagList.get(i));
                    }

                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(),0);

                    linSearchVisible.setVisibility(View.GONE);
                    linFragmentVisible.setVisibility(View.VISIBLE);

                    if (fragmentArrayList.size() == 0){
                        for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                            if (COMMUNITY_LV_LIST.get(i).equals("1")){
                                fragmentArrayList.add(new NewCommunitySearchChildFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i), COMMUNITY_MUNU_NO_LIST.get(i),"전체"));
                                fragmentTagArrayList.add(COMMUNITY_NAME_LIST.get(i));
                            }
                        }
                        fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                        fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();
                    }else{
                        if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(tabLayout.getSelectedTabPosition())) == null){
                            fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(tabLayout.getSelectedTabPosition()),fragmentTagArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                            fragmentManager.beginTransaction().show(fragmentArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                        }else{
                            fragmentManager.beginTransaction().show(fragmentArrayList.get(tabLayout.getSelectedTabPosition())).commitAllowingStateLoss();
                            fragmentArrayList.get(tabLayout.getSelectedTabPosition()).onActivityResult(NEW_COMMUNITY_SEARCH,RESULT_OK,null);
                        }
                    }
                    ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                    ViewGroup tabView = null;
                    tabView = (ViewGroup) mainView.getChildAt(tabLayout.getSelectedTabPosition());
                    View tabViewChild = tabView.getChildAt(1);
                    Typeface typeface = ResourcesCompat.getFont(NewCommunitySearchActivity.this, R.font.notosanscjkkr_bold);
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
                break;
            }
            case R.id.txt_new_community_search_tag_all_remove : {
                mTagList = new ArrayList<>();
                mSetTagArrayPref("tagList",mTagList);
                linTagListParent.removeAllViews();
                break;
            }
            case R.id.txt_new_community_search_cancel : {
                onBackPressed();
                break;
            }
        }
    }

    public void NetWorkCall(String mCode){
        if (mCode.equals(COMMUNITY_MENU_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
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
                            fragmentArrayList.add(new NewCommunitySearchChildFragment().newInstance(JsonIsNullCheck(jsonArray.getJSONObject(i),"MENU_NO"), JsonIsNullCheck(jsonArray.getJSONObject(i),"MENU_NO"),"전체"));
                            fragmentTagArrayList.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"NAME"));
                        }
                    }
                    fragmentManager.beginTransaction().add(R.id.fragment_new_community_search, fragmentArrayList.get(0),fragmentTagArrayList.get(0)).commitAllowingStateLoss();
                    fragmentManager.beginTransaction().show(fragmentArrayList.get(0)).commitAllowingStateLoss();

                }catch (JSONException e){

                }
            }
        }
    }
}