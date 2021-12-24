package com.kmw.soom2.ex.Community;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Adapters.CommunityBannerAdapter;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.R;
import com.kmw.soom2.SplashActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.BLOG_BANNER;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.RUTIN_BANNER;
import static com.kmw.soom2.Common.Utils.COMMUNITY_BLOG_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LV_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_PARENT_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_REFRESH;
import static com.kmw.soom2.Common.Utils.COMMUNITY_RUTIN_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_WRITE_REFRESH;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class exMiddleFragment extends Fragment implements RadioGroup.OnCheckedChangeListener , AsyncResponse {

    private String TAG = "NewMiddleFragment";
    String mTag;
    String mTitle;

    private HorizontalScrollView linTabParentVisible;
    private LinearLayout linTabParent;
    private RadioGroup radioTabParent;
//    private FrameLayout frameLayout;

    public static boolean firstCheck = false;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int tabCnt = 0;

    ArrayList<RadioButton> radioButtonArrayList = new ArrayList<>();
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTagArrayList = new ArrayList<>();
    ArrayList<String> fragmentMenuNoList = new ArrayList<>();
    FragmentManager fragmentManager;
    Typeface typefaceBold,typefaceMedium ;
    int dpi;
    float density;

    public static CommunityBannerAdapter viewPagerBlogAdapter;
    public static CommunityBannerAdapter viewPagerRutinAdapter;
    public static ViewPager viewPagerBlog;
    public static ViewPager viewPagerRutin;

    public exMiddleFragment() {

    }

    public static exMiddleFragment newInstance(String mTag, String mTitle){

        exMiddleFragment newMiddleFragment = new exMiddleFragment();

        Bundle args = new Bundle();
        args.putString("mTag",mTag);
        args.putString("mTitle",mTitle);
        newMiddleFragment.setArguments(args);
        return newMiddleFragment;
    }

    public String getInstance(String mKey){
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

        View v                      = inflater.inflate(R.layout.fragment_middle_community, container, false);

        linTabParentVisible         = v.findViewById(R.id.scr_new_community_child_tab_vislble);
        linTabParent                = v.findViewById(R.id.lin_new_community_child_tab);
        radioTabParent              = v.findViewById(R.id.radio_group_new_community_child_tab);

        NullCheck(getActivity());

        radioTabParent.setOnCheckedChangeListener(this);

        fragmentManager = getChildFragmentManager();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        dpi = displayMetrics.densityDpi;
        density = displayMetrics.density;

        mTag = getInstance("mTag");
        mTitle = getInstance("mTitle");

        Log.i(TAG,"TAG : " + mTag + " TITLE : " + mTitle);

        typefaceBold = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);
        typefaceMedium = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_medium);

        NetWorkCall();

        getChildFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("bundleKey");
                Log.i(TAG,"result : " + result);
                if (fragmentTagArrayList.contains(result)){
                    radioButtonArrayList.get(fragmentTagArrayList.indexOf(result)).setChecked(true);
                }
            }
        });

        return v;
    }

    //나머지 탭
    void TabCagegoryMake(){
        if (COMMUNITY_MUNU_NO_LIST != null){
            for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                if (COMMUNITY_LV_LIST.get(i).equals("2")){
                    if (mTag.equals(COMMUNITY_PARENT_LIST.get(i))){
                        RadioButton radioButton = new RadioButton(getActivity());
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.rightMargin = dpToPx(6);
                        radioButton.setPadding(dpToPx(13),dpToPx(4),dpToPx(13),dpToPx(4));
                        radioButton.setLayoutParams(params);
                        radioButton.setButtonDrawable(0);

                        radioButton.setText(COMMUNITY_NAME_LIST.get(i));
                        radioButton.setTextColor(getResources().getColor(R.color.color9f9f9f));
                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14f);
                        radioButton.setTypeface(typefaceMedium);
                        radioButton.setGravity(Gravity.CENTER);
                        radioButton.setTag(COMMUNITY_MUNU_NO_LIST.get(i));
                        radioButton.setChecked(false);

                        if (mTag.equals("1")){
                            radioButton.setBackgroundResource(R.drawable.bg_community_middle_category_01);
                        }else if (mTag.equals("2")){
                            radioButton.setBackgroundResource(R.drawable.bg_community_middle_category_02);
                        }else if (mTag.equals("3")){
                            radioButton.setBackgroundResource(R.drawable.bg_community_middle_category_03);
                        }

                        radioButtonArrayList.add(radioButton);

                        radioTabParent.addView(radioButton);

                        if (mTag.equals("3")){
                            fragmentArrayList.add(new exChildBlogFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i), mTag,COMMUNITY_NAME_LIST.get(i)));
                        }else{
                            fragmentArrayList.add(new exChildFragment().newInstance(COMMUNITY_MUNU_NO_LIST.get(i), mTag,COMMUNITY_NAME_LIST.get(i)));
                        }

                        fragmentTagArrayList.add(COMMUNITY_NAME_LIST.get(i));
                        fragmentMenuNoList.add(COMMUNITY_MUNU_NO_LIST.get(i));
                    }
                }
            }
        }else{
            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (fragmentArrayList.size() > 0){
            //전체 탭 삽입
            Log.i(TAG,"TAG : " + mTag + "TITLE : " + mTitle);
            RadioButton radioButton = new RadioButton(getActivity());
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radioButton.setPadding(dpToPx(13),dpToPx(4),dpToPx(13),dpToPx(4));

            params.rightMargin = dpToPx(6);
            radioButton.setLayoutParams(params);
            radioButton.setButtonDrawable(0);
            radioButton.setText("전체");
            radioButton.setTextColor(Color.parseColor("#2e2e2e"));
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14f);
            radioButton.setTypeface(typefaceBold);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setTag(mTag);

            if (mTag.equals("1")){
                radioButton.setBackgroundResource(R.drawable.bg_community_middle_category_01);
            }else if (mTag.equals("2")){
                radioButton.setBackgroundResource(R.drawable.bg_community_middle_category_02);
            }else if (mTag.equals("3")){
                radioButton.setBackgroundResource(R.drawable.bg_community_middle_category_03);
            }

            radioButtonArrayList.add(0,radioButton);
            radioTabParent.addView(radioButton,0);

            fragmentMenuNoList.add(mTag);
            if (mTag.equals("3")){
                fragmentArrayList.add(0,new exChildBlogFragment().newInstance(mTag, mTag,"전체"));
            }else{
                fragmentArrayList.add(0,new exChildFragment().newInstance(mTag, mTag,"전체"));
            }

            fragmentTagArrayList.add(0,"전체");

            radioButtonArrayList.get(0).setChecked(true);
        }else{
            linTabParentVisible.setVisibility(View.GONE);
            if (mTag.equals("3")){
                fragmentManager.beginTransaction().add(R.id.fragment_new_middle_community_layout, new exChildBlogFragment().newInstance(mTag, mTitle,"")).commitAllowingStateLoss();
                fragmentManager.beginTransaction().show(new exChildBlogFragment().newInstance(mTag, mTitle,"")).commitAllowingStateLoss();
            }else{
                fragmentManager.beginTransaction().add(R.id.fragment_new_middle_community_layout, new exChildFragment().newInstance(mTag, mTitle,"")).commitAllowingStateLoss();
                fragmentManager.beginTransaction().show(new exChildFragment().newInstance(mTag, mTitle,"")).commitAllowingStateLoss();
            }

        }

    }

    public int dpToPx(float dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getActivity().getResources().getDisplayMetrics());
        return px;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);
        int idx = 0;
        if (radioButton instanceof RadioButton){
            idx = group.indexOfChild(radioButton);
        }

        if (fragmentArrayList.size() > 0){
            if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(idx)) == null){
                fragmentManager.beginTransaction().add(R.id.fragment_new_middle_community_layout, fragmentArrayList.get(idx),fragmentTagArrayList.get(idx)).commitAllowingStateLoss();
                fragmentManager.beginTransaction().show(fragmentArrayList.get(idx)).commitAllowingStateLoss();
            }else{
                fragmentManager.beginTransaction().show(fragmentArrayList.get(idx)).commitAllowingStateLoss();
                fragmentArrayList.get(idx).onActivityResult(COMMUNITY_REFRESH,RESULT_OK,null);
            }

            for (int i = 0; i < radioButtonArrayList.size(); i++){
                if (mTag.equals("1")){
                    if (i == idx){
                        radioButtonArrayList.get(i).setTextColor(Color.parseColor("#09D182"));
                        radioButtonArrayList.get(i).setTypeface(typefaceBold);
                    }else{
                        radioButtonArrayList.get(i).setTextColor(Color.parseColor("#838383"));
                        radioButtonArrayList.get(i).setTypeface(typefaceMedium);
                    }
                }else if (mTag.equals("2")){
                    if (i == idx){
                        radioButtonArrayList.get(i).setTextColor(Color.parseColor("#6B8CBF"));
                        radioButtonArrayList.get(i).setTypeface(typefaceBold);
                    }else{
                        radioButtonArrayList.get(i).setTextColor(Color.parseColor("#838383"));
                        radioButtonArrayList.get(i).setTypeface(typefaceMedium);
                    }
                }else if (mTag.equals("3")){
                    if (i == idx){
                        radioButtonArrayList.get(i).setTextColor(Color.parseColor("#CEB218"));
                        radioButtonArrayList.get(i).setTypeface(typefaceBold);
                    }else{
                        radioButtonArrayList.get(i).setTextColor(Color.parseColor("#838383"));
                        radioButtonArrayList.get(i).setTypeface(typefaceMedium);
                    }
                }
            }

            for (int i = 0; i < fragmentArrayList.size(); i++){
                if (i != idx){
                    if (fragmentManager.findFragmentByTag(fragmentTagArrayList.get(i)) != null){
                        fragmentManager.beginTransaction().hide(fragmentArrayList.get(i)).commitAllowingStateLoss();
                    }
                }
            }
        }
    }

    public void NetWorkCall(){
        if (mTag.equals("1")){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,BLOG_BANNER).execute();
        }else if (mTag.equals("2")){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,RUTIN_BANNER).execute();
        }else{
            TabCagegoryMake();
        }
    }
    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(BLOG_BANNER)){
                Log.i(TAG,"blog : " + mResult);
                try{
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.has("list")){
                        COMMUNITY_BLOG_BANNER_LIST = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                                BannerItem bannerItem = new BannerItem();
                                bannerItem.setBannerNo(JsonIsNullCheck(object, "BANNER_NO"));
                                bannerItem.setBannerType(JsonIsNullCheck(object, "BANNER_TYPE"));
                                bannerItem.setBannerLink(JsonIsNullCheck(object, "BANNER_LINK"));
                                bannerItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                bannerItem.setPriority(JsonIntIsNullCheck(object, "PRIORITY"));
                                bannerItem.setBannerTitle(JsonIsNullCheck(object,"BANNER_TITLE"));

                                COMMUNITY_BLOG_BANNER_LIST.add(bannerItem);
                            }
                        }
                        if (COMMUNITY_BLOG_BANNER_LIST.size() > 0){
                            Collections.sort(COMMUNITY_BLOG_BANNER_LIST, UpComparator);
                            viewPagerBlogAdapter = new CommunityBannerAdapter(getActivity(), COMMUNITY_BLOG_BANNER_LIST,true);
                        }
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(RUTIN_BANNER)){
                Log.i(TAG,"rutin : " + mResult);
                try{
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (jsonObject.has("list")){
                        COMMUNITY_RUTIN_BANNER_LIST = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);

                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                                BannerItem bannerItem = new BannerItem();
                                bannerItem.setBannerNo(JsonIsNullCheck(object, "BANNER_NO"));
                                bannerItem.setBannerType(JsonIsNullCheck(object, "BANNER_TYPE"));
                                bannerItem.setBannerLink(JsonIsNullCheck(object, "BANNER_LINK"));
                                bannerItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                bannerItem.setPriority(JsonIntIsNullCheck(object, "PRIORITY"));
                                bannerItem.setBannerTitle(JsonIsNullCheck(object,"BANNER_TITLE"));

                                COMMUNITY_RUTIN_BANNER_LIST.add(bannerItem);
                            }
                        }
                        if (COMMUNITY_RUTIN_BANNER_LIST.size() > 0){
                            Collections.sort(COMMUNITY_RUTIN_BANNER_LIST, UpComparator);
                            viewPagerRutinAdapter = new CommunityBannerAdapter(getActivity(), COMMUNITY_RUTIN_BANNER_LIST,true);
                        }
                    }
                }catch (JSONException e){

                }
            }
        }
        TabCagegoryMake();
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
                //tag함 fragment 도 같이 refresh 시켜야함
                radioButtonArrayList.get(0).setChecked(true);
                linTabParentVisible.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linTabParentVisible.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                    }
                },200);

                fragmentArrayList.get(0).onActivityResult(COMMUNITY_WRITE_REFRESH,RESULT_OK,null);

//                for (int i = 0; i < fragmentArrayList.size(); i++){
//                    if (fragmentMenuNoList.get(i).equals(COMMUNITY_SELECT_TAG)){
//                        fragmentArrayList.get(i).onActivityResult(COMMUNITY_WRITE_REFRESH,RESULT_OK,null);
//                    }
//                }
            }
        }
    }

    private Comparator<BannerItem> UpComparator = new Comparator<BannerItem>() {
        @Override
        public int compare(BannerItem bannerItem, BannerItem t1) {
            int reset;
            if (bannerItem.getPriority() > t1.getPriority()) {
                reset = -1;
            } else if (bannerItem.getPriority() == t1.getPriority()) {
                reset = 0;
            } else
                reset = 1;
            return reset;
        }
    };
}
