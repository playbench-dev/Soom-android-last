package com.kmw.soom2.Communitys.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.TempUtil;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Adapters.NewCommunityWebAdapter;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.R;
import com.kmw.soom2.SplashActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.BLOG_BANNER;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DETAIL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.RUTIN_BANNER;
import static com.kmw.soom2.Common.Utils.COMMUNITY_BLOG_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_DETAIL_MOVE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_REFRESH;
import static com.kmw.soom2.Common.Utils.COMMUNITY_RUTIN_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECT_TAG;
import static com.kmw.soom2.Common.Utils.COMMUNITY_WRITE_REFRESH;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.preferencesName;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.ITEM_TYPE;

public class NewCommunityChildWebFragment extends Fragment implements AsyncResponse {

    private String TAG = "NewCommunityChildWebFragment";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String mMenuNo = "";
    private String mParentTitle;
    private String mMiddleTitle;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NewCommunityWebAdapter newCommunityAdapter;
    private ArrayList<CommunityItems> communityItemsArrayList;
    public ArrayList<CommunityRecyclerViewItem>       mList = new ArrayList<>();

    private int communitySearchTotalPage;
    private int pagingNo = 1;
    private String communityNo = "";
    private int position = -1;
    private boolean mLockScrollView = false;
    public static Activity thisActivity = null;
    private ProgressDialog progressDialog;

    private ViewPager viewPager;
    private TextView txtPagerCnt;
    private TextView txtPagerTotalCnt;
    private LinearLayout linPagerCntParent;

    private Handler handler = new Handler();
    private String mPopularity = "";

    public NewCommunityChildWebFragment() {

    }

    public static NewCommunityChildWebFragment newInstance(String mMenuNo, String mParentTitle, String mMiddleTitle){

        NewCommunityChildWebFragment newFragment = new NewCommunityChildWebFragment();

        Bundle args = new Bundle();
        args.putString("mMenuNo",mMenuNo);
        args.putString("mParentTitle",mParentTitle);
        args.putString("mMiddleTitle",mMiddleTitle);
        newFragment.setArguments(args);
        return newFragment;
    }

    public String getInstance(String mKey){
        return getArguments().getString(mKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        thisActivity = getActivity();

//        if (mParentTitle != null && mMiddleTitle != null){
//            if (mParentTitle.equals("1")) {
//                if (pref.getBoolean("communityCheck11",false) || pref.getBoolean("communityWrite",false)){
//                    editor.putBoolean("communityCheck11",false);
//                    editor.putBoolean("communityWrite",false);
//                    editor.apply();
//                    pagingNo = 1;
//                    progressDialog.show();
//                    Log.i(TAG,"communityWrite onResume");
//                    NetworkCall(NEW_COMMUNITY_LIST);
//                }
//            }else if (newCommunityAdapter == null){
//
//            }
//        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_child_new, container, false);

        NullCheck(getActivity());

        pref = getActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        editor = pref.edit();

        progressDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
//        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
//        drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent),
//                PorterDuff.Mode.SRC_IN);
//        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setCancelable(false);
        progressDialog.show();

        mMenuNo = getInstance("mMenuNo");
        mParentTitle = getInstance("mParentTitle");
        mMiddleTitle = getInstance("mMiddleTitle");

        if (COMMUNITY_POPULARUTY != null){
            mPopularity = COMMUNITY_POPULARUTY.get(COMMUNITY_MUNU_NO_LIST.indexOf(mMenuNo));
        }

        Log.i(TAG,"mTag : " + mMenuNo + " middle : " + mMiddleTitle + " parent : " + mParentTitle + " pop : " + mPopularity);

        FindViewById(v);

        COMMUNITY_SELECT_TAG = mMenuNo;

        //mMiddleTitle == 전체 ? MENU_NO : C_MENU_NO
        NetworkCall(NEW_COMMUNITY_LIST);

        Log.i(TAG,"time : " + new SimpleDateFormat("mm:ss").format(new Date(System.currentTimeMillis())));

        return v;
    }

    private void FindViewById(View view){
        swipeRefreshLayout          = view.findViewById(R.id.community_child_new_refresh_layout);
        recyclerView                = view.findViewById(R.id.community_child_new_recyclerview);
        viewPager                   = view.findViewById(R.id.view_pager_community_banner);
        txtPagerCnt                 = view.findViewById(R.id.txt_community_banner_pager_cnt);
        txtPagerTotalCnt            = view.findViewById(R.id.txt_community_banner_pager_total_cnt);
        linPagerCntParent           = view.findViewById(R.id.lin_pager_community_banner_cnt_parent);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCnt = recyclerView.getAdapter().getItemCount();

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && pagingNo < communitySearchTotalPage && mLockScrollView) {
                    mLockScrollView = false;
                    ++pagingNo;
                    progressDialog.show();
                    NetworkCall(NEW_COMMUNITY_LIST);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pagingNo = 1;
                progressDialog.show();
                NetworkCall(NEW_COMMUNITY_LIST);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (mParentTitle.equals("1")){
            if (COMMUNITY_BLOG_BANNER_LIST == null){
                NetworkCall(BLOG_BANNER);
            }else if (COMMUNITY_BLOG_BANNER_LIST.size() > 0){

                DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
                int width1 = dm1.widthPixels;

                viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width1 * 0.402));
                        viewPager.setLayoutParams(params);
                        viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });

                viewPager.setVisibility(View.VISIBLE);
                linPagerCntParent.setVisibility(View.VISIBLE);
                viewPager.setAdapter(NewCommunityMiddleWebFragment.viewPagerBlogAdapter);

                txtPagerCnt.setText("1");
                txtPagerTotalCnt.setText("/"+COMMUNITY_BLOG_BANNER_LIST.size());
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        int pos = position % COMMUNITY_BLOG_BANNER_LIST.size();
                        txtPagerCnt.setText(""+(pos+1));
                        txtPagerTotalCnt.setText("/"+COMMUNITY_BLOG_BANNER_LIST.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        handler.postDelayed(this, 8000);
                    }
                }, 8000);
            }else{
                viewPager.setVisibility(View.GONE);
                linPagerCntParent.setVisibility(View.GONE);
            }

        }else if (mParentTitle.equals("2")){
            if (COMMUNITY_RUTIN_BANNER_LIST == null){
                NetworkCall(RUTIN_BANNER);
            }else if (COMMUNITY_RUTIN_BANNER_LIST.size() > 0){

                DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
                int width1 = dm1.widthPixels;

                viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width1 * 0.402));
                        viewPager.setLayoutParams(params);
                        viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });

                viewPager.setVisibility(View.VISIBLE);
                linPagerCntParent.setVisibility(View.VISIBLE);
                viewPager.setAdapter(NewCommunityMiddleWebFragment.viewPagerRutinAdapter);
                txtPagerCnt.setText("1");
                txtPagerTotalCnt.setText("/"+COMMUNITY_RUTIN_BANNER_LIST.size());
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        int pos = position % COMMUNITY_RUTIN_BANNER_LIST.size();
                        txtPagerCnt.setText(""+(pos+1));
                        txtPagerTotalCnt.setText("/"+COMMUNITY_RUTIN_BANNER_LIST.size());
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        handler.postDelayed(this, 8000);
                    }
                }, 8000);
            }else{
                viewPager.setVisibility(View.GONE);
                linPagerCntParent.setVisibility(View.GONE);
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
        Log.i(TAG,"aaaa : " + requestCode);
        if (requestCode == COMMUNITY_REFRESH){    //  탭 클릭시 refresh
            if (resultCode == RESULT_OK){
                pagingNo = 1;
                if (mParentTitle != null && mMiddleTitle != null){
                    NetworkCall(NEW_COMMUNITY_LIST);
                }else{
                    Intent intent = new Intent(thisActivity, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        }else if (requestCode == Integer.parseInt(mMenuNo)){   // 해쉬태그 별 수정 시 refresh
            if (resultCode == RESULT_OK){
                position = data.getIntExtra("position",-1);
                communityNo = data.getStringExtra("communityNo");
                progressDialog.show();
                NetworkCall(NEW_COMMUNITY_DETAIL);
            }
        }else if (requestCode == COMMUNITY_WRITE_REFRESH){  //글 작성시 refresh
            if (resultCode == RESULT_OK){
                Log.i(TAG,"communityWrite refresh");
//                editor.putBoolean("communityWrite",false);
//                editor.apply();
                pagingNo = 1;
                progressDialog.show();
                if (mParentTitle != null && mMiddleTitle != null){
                    NetworkCall(NEW_COMMUNITY_LIST);
                }else{
                    Intent intent = new Intent(thisActivity, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
//                if (mParentTitle.equals("모두보기")){
//                    NetworkCall(COMMUNITY_SELECT_BY_MENU_TAG_LIST);
//                }else if (mMiddleTitle.equals("전체")){
//                    NetworkCall(COMMUNITY_SELECT_BY_MENU_CONTENTS_ALL_LIST);
//                }else{
//                    NetworkCall(COMMUNITY_SELECT_BY_MENU_LIST);
//                }
            }
        }else if (requestCode == COMMUNITY_DETAIL_MOVE){
            if (resultCode == RESULT_OK){
                Log.i(TAG,"커뮤니티 리스트 RESULT_OK");
                //memo 상세화면에서 삭제시 돌아온 후에 게시물 삭제
                if (data != null){
                    Log.i(TAG,"null아님");
                    if (data.hasExtra("communityRemove")){
                        Log.i(TAG,"커뮤니티 리스트 if");
                        position = data.getIntExtra("position",-1);
                        mList.remove(position);
                        newCommunityAdapter.itemArrayList.remove(position);
                        newCommunityAdapter.notifyDataSetChanged();
                    }else{
                        if (data.hasExtra("position")){
                            Log.i(TAG,"커뮤니티 리스트 else");
                            position = data.getIntExtra("position",-1);
                            communityNo = data.getStringExtra("communityNo");
                            NetworkCall(NEW_COMMUNITY_DETAIL);
                        }
                    }
                }
            }
        }else if (requestCode == 4343){
            if (resultCode == RESULT_OK){
                Bundle result = new Bundle();
                result.putString("bundleKey", data.getStringExtra("text"));
                getParentFragmentManager().setFragmentResult("requestKey",result);
            }
        }
    }

    private void NetworkCall(String mCode){
        Log.i(TAG,"time 1 : " + new SimpleDateFormat("mm:ss").format(new Date(System.currentTimeMillis())));
        if (mCode.equals(NEW_COMMUNITY_LIST)){
            String orderBy = "";
            if (mMiddleTitle.equals("전체")){ //상단고정
                orderBy = "2";
            }else {                         //날짜순
                orderBy = "1";
            }
            if (mPopularity.equals("1")){
                new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+Utils.userItem.getUserNo(),""+pagingNo,"20","3",mParentTitle,"all");  //memo 인기
            }else{
                new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+Utils.userItem.getUserNo(),""+pagingNo,"20",orderBy,mMenuNo,mMiddleTitle.equals("전체") ? "all" : "nothing");
            }
        }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),communityNo);
        }else if (mCode.equals(RUTIN_BANNER)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,RUTIN_BANNER).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else if (mCode.equals(BLOG_BANNER)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,BLOG_BANNER).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){

            if (mCode.equals(NEW_COMMUNITY_LIST)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    communitySearchTotalPage = jsonObject.getInt("Search_TotalPage");

                    if (pagingNo == 1){
                        mList = new ArrayList<>();
//                        mList.add(new CommunityRecyclerViewItem(null,HEADER_TYPE));
                        communityItemsArrayList = new ArrayList<>();
                        newCommunityAdapter = new NewCommunityWebAdapter(getActivity(),this, mMenuNo,mParentTitle);
                    }

                    int size = jsonArray.length();

                    for (int i = 0; i < size; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            CommunityItems communityItems = new CommunityItems();
                            communityItems.setNo(JsonIsNullCheck(object, "COMMUNITY_NO"));
                            communityItems.setProfile(JsonIsNullCheck(object, "PROFILE_IMG"));
                            communityItems.setName(JsonIsNullCheck(object, "NICKNAME"));
                            communityItems.setGender(JsonIntIsNullCheck(object,"GENDER"));
                            communityItems.setLabel(JsonIsNullCheck(object,"LABEL_NAME"));
                            communityItems.setLabelColor(JsonIsNullCheck(object,"LABEL_COLOR"));
                            if (JsonIsNullCheck(object, "UPDATE_DT").length() == 0) {
                                communityItems.setDate(JsonIsNullCheck(object, "CREATE_DT"));
                            } else {
                                communityItems.setDate(JsonIsNullCheck(object, "UPDATE_DT"));
                            }
                            communityItems.setcMenuTitle(JsonIsNullCheck(object, "C_MENU_TITLE"));
                            communityItems.setImgListPath(JsonIsNullCheck(object, "IMAGE_FILE"));
                            communityItems.setcMenuNo(JsonIsNullCheck(object,"C_MENU_NO"));
                            communityItems.setmMenuNo(JsonIsNullCheck(object,"MENU_NO"));
                            communityItems.setContents(JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n"));
                            communityItems.setHashTag(JsonIsNullCheck(object, "HASHTAG"));
                            communityItems.setLikeCnt(JsonIntIsNullCheck(object, "LIKE_CNT"));
                            communityItems.setCommentCnt(JsonIntIsNullCheck(object, "COMMENT_CNT"));
                            communityItems.setPriority(JsonIntIsNullCheck(object,"PRIORITY"));
                            communityItems.setLv(JsonIsNullCheck(object, "LV"));
                            communityItems.setUserNo(JsonIsNullCheck(object, "USER_NO"));
                            communityItems.setLikeFlag(JsonIntIsNullCheck(object,"LIKE_FLAG"));
                            communityItems.setScrapFlag(JsonIntIsNullCheck(object,"SCRAP_FLAG"));
                            communityItems.setExpandableFlag(1);

                            // Todo 여기서 빼줘야 함
                            ArrayList<String> blackList = TempUtil.getStringArrayPref(requireContext(), "BLACK_LIST");
                            if(!blackList.contains(JsonIsNullCheck(object, "USER_NO"))) {
                                communityItemsArrayList.add(communityItems);
                                mList.add(new CommunityRecyclerViewItem(communityItems,ITEM_TYPE));
                            }
                        }
                    }

                    newCommunityAdapter.addItem(mList);

                    if (pagingNo == 1){
                        recyclerView.setAdapter(newCommunityAdapter);
                    }
                    newCommunityAdapter.notifyDataSetChanged();

                    mLockScrollView = true;

                } catch (JSONException e) {

                }
            }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
                Log.i(TAG,"aaaa : " + mResult);
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONObject object = jsonObject.getJSONArray("list").getJSONObject(0);

                    CommunityItems communityItems = new CommunityItems();
                    communityItems.setNo(JsonIsNullCheck(object, "COMMUNITY_NO"));
                    communityItems.setProfile(JsonIsNullCheck(object, "PROFILE_IMG"));
                    communityItems.setName(JsonIsNullCheck(object, "NICKNAME"));
                    communityItems.setGender(JsonIntIsNullCheck(object,"GENDER"));
                    communityItems.setLabel(JsonIsNullCheck(object,"LABEL_NAME"));
                    communityItems.setLabelColor(JsonIsNullCheck(object,"LABEL_COLOR"));
                    if (JsonIsNullCheck(object, "UPDATE_DT").length() == 0) {
                        communityItems.setDate(JsonIsNullCheck(object, "CREATE_DT"));
                    } else {
                        communityItems.setDate(JsonIsNullCheck(object, "UPDATE_DT"));
                    }
                    communityItems.setcMenuTitle(JsonIsNullCheck(object, "C_MENU_TITLE"));
                    communityItems.setcMenuNo(JsonIsNullCheck(object,"C_MENU_NO"));
                    communityItems.setmMenuNo(JsonIsNullCheck(object,"MENU_NO"));
                    communityItems.setImgListPath(JsonIsNullCheck(object, "IMAGE_FILE"));
                    communityItems.setContents(JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n"));
                    communityItems.setHashTag(JsonIsNullCheck(object, "HASHTAG"));
                    communityItems.setLikeCnt(JsonIntIsNullCheck(object, "LIKE_CNT"));
                    communityItems.setCommentCnt(JsonIntIsNullCheck(object, "COMMENT_CNT"));
                    communityItems.setPriority(JsonIntIsNullCheck(object,"PRIORITY"));
                    communityItems.setLv(JsonIsNullCheck(object, "LV"));
                    communityItems.setUserNo(JsonIsNullCheck(object, "USER_NO"));
                    communityItems.setLikeFlag(JsonIntIsNullCheck(object,"LIKE_FLAG"));
                    communityItems.setScrapFlag(JsonIntIsNullCheck(object,"SCRAP_FLAG"));
                    communityItems.setExpandableFlag(1);

                    mList.set(position,new CommunityRecyclerViewItem(communityItems,ITEM_TYPE));
                    newCommunityAdapter.itemArrayList.set(position, new CommunityRecyclerViewItem(communityItems,ITEM_TYPE));
                    newCommunityAdapter.notifyDataSetChanged();

                }catch (JSONException e){

                }
            }else if (mCode.equals(BLOG_BANNER)){
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

                            DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
                            int width1 = dm1.widthPixels;

                            viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width1 * 0.402));
                                    viewPager.setLayoutParams(params);
                                    viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                                    return true;
                                }
                            });

                            viewPager.setVisibility(View.VISIBLE);
                            linPagerCntParent.setVisibility(View.VISIBLE);
                            viewPager.setAdapter(NewCommunityMiddleWebFragment.viewPagerBlogAdapter);

                            txtPagerCnt.setText("1");
                            txtPagerTotalCnt.setText("/"+COMMUNITY_BLOG_BANNER_LIST.size());
                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    int pos = position % COMMUNITY_BLOG_BANNER_LIST.size();
                                    txtPagerCnt.setText(""+(pos+1));
                                    txtPagerTotalCnt.setText("/"+COMMUNITY_BLOG_BANNER_LIST.size());
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                    handler.postDelayed(this, 8000);
                                }
                            }, 8000);
                        }else{
                            viewPager.setVisibility(View.GONE);
                            linPagerCntParent.setVisibility(View.GONE);
                        }
                    }
//                    NetworkCall(SYMPTOM_MANAGE_LINK);
                }catch (JSONException e){
//                    NetworkCall(SYMPTOM_MANAGE_LINK);
                }
            }else if (mCode.equals(RUTIN_BANNER)){
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

                            DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
                            int width1 = dm1.widthPixels;

                            viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width1 * 0.402));
                                    viewPager.setLayoutParams(params);
                                    viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                                    return true;
                                }
                            });

                            viewPager.setVisibility(View.VISIBLE);
                            linPagerCntParent.setVisibility(View.VISIBLE);
                            viewPager.setAdapter(NewCommunityMiddleWebFragment.viewPagerRutinAdapter);
                            txtPagerCnt.setText("1");
                            txtPagerTotalCnt.setText("/"+COMMUNITY_RUTIN_BANNER_LIST.size());
                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    int pos = position % COMMUNITY_RUTIN_BANNER_LIST.size();
                                    txtPagerCnt.setText(""+(pos+1));
                                    txtPagerTotalCnt.setText("/"+COMMUNITY_RUTIN_BANNER_LIST.size());
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                    handler.postDelayed(this, 8000);
                                }
                            }, 8000);
                        }else{
                            viewPager.setVisibility(View.GONE);
                            linPagerCntParent.setVisibility(View.GONE);
                        }
                    }
//                    NetworkCall(BLOG_BANNER);
                }catch (JSONException e){
//                    NetworkCall(BLOG_BANNER);
                }
            }
            progressDialog.dismiss();
        }else{

        }
    }

    public static String response = "";

    public static class DeleteCommunityNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO", strings[0])
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteCommunity(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    //좋아요 스크랩 추가
    public static class InsertCommunityLikeNetWork extends AsyncTask<String, String, String> {

        private String TAG = "InsertCommunityLikeNetWork";
        String flag = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO", strings[0])
                    .add("USER_NO", "" + Utils.userItem.getUserNo())
                    .add("FLAG", strings[1])
                    .build();

            flag = strings[1];

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertCommunityLike(), body);
                Log.i(TAG,"response : " + response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (flag.equals("2")){
                Toast.makeText(thisActivity, "게시글을 저장했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //좋아요 스크랩 삭제
    public static class DeleteCommunityLikeNetWork extends AsyncTask<String, String, String> {

        private String TAG = "InsertCommunityLikeNetWork";
        String communityNo = "";
        String status = "";
        String flag = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            communityNo = strings[0];
            status = strings[1];
            RequestBody body = new FormBody.Builder().add("COMMUNITY_NO", strings[0])
                    .add("USER_NO", "" + Utils.userItem.getUserNo())
                    .add("FLAG", strings[1])
                    .build();

            flag = strings[1];

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteCommunityLike(), body);
                Log.i(TAG,"response : " + response);
//                logLargeString(response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (flag.equals("2")){
                Toast.makeText(thisActivity, "게시글 저장을 취소했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(mMiddleTitle,"hidden : " + hidden);
        if (!hidden){
            if (handler != null){
                handler.removeMessages(0);
                handler = null;
            }

            handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    handler.postDelayed(this, 8000);
                }
            }, 8000);
        }else{
            if (handler != null){
                handler.removeMessages(0);
                handler = null;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
