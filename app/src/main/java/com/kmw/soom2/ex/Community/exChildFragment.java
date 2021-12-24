package com.kmw.soom2.ex.Community;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem;
import com.kmw.soom2.R;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DETAIL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_BLOG_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_RUTIN_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECT_TAG;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.preferencesName;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.ITEM_TYPE;

public class exChildFragment extends Fragment implements AsyncResponse {

    private String TAG = "NewFragment";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String mMenuNo = "";
    private String mParentTitle;
    private String mMiddleTitle;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private exCommunityAdapter newCommunityAdapter;
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

    public exChildFragment() {

    }

    public static exChildFragment newInstance(String mMenuNo, String mParentTitle, String mMiddleTitle){

        exChildFragment newFragment = new exChildFragment();

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

        if (mParentTitle != null && mMiddleTitle != null){
            if (mParentTitle.equals("1")) {
                if (pref.getBoolean("communityCheck11",false) || pref.getBoolean("communityWrite",false)){
                    editor.putBoolean("communityCheck11",false);
                    editor.putBoolean("communityWrite",false);
                    editor.apply();
                    pagingNo = 1;
                    progressDialog.show();
                    Log.i(TAG,"communityWrite onResume");
                    NetworkCall(NEW_COMMUNITY_LIST);
                }
            }else if (newCommunityAdapter == null){

            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ex_community_child, container, false);

        NullCheck(getActivity());

        pref = getActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        editor = pref.edit();

        progressDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        progressDialog.setCancelable(false);
        progressDialog.show();

        mMenuNo = getInstance("mMenuNo");
        mParentTitle = getInstance("mParentTitle");
        mMiddleTitle = getInstance("mMiddleTitle");

        FindViewById(v);

        COMMUNITY_SELECT_TAG = mMenuNo;

        if (COMMUNITY_POPULARUTY != null){
            mPopularity = COMMUNITY_POPULARUTY.get(COMMUNITY_MUNU_NO_LIST.indexOf(mMenuNo));
        }

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

        NetworkCall(NEW_COMMUNITY_LIST);

        return v;
    }

    private void FindViewById(View view){
        swipeRefreshLayout          = view.findViewById(R.id.swipe_refresh_ex_community);
        recyclerView                = view.findViewById(R.id.community_child_new_recyclerview);
        viewPager                   = view.findViewById(R.id.view_pager_community_banner);
        txtPagerCnt                 = view.findViewById(R.id.txt_community_banner_pager_cnt);
        txtPagerTotalCnt            = view.findViewById(R.id.txt_community_banner_pager_total_cnt);
        linPagerCntParent           = view.findViewById(R.id.lin_pager_community_banner_cnt_parent);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                NetworkCall(NEW_COMMUNITY_LIST);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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

        Log.i(TAG,"parent : " + mParentTitle + " banner : " + COMMUNITY_BLOG_BANNER_LIST.size());

        if (mParentTitle.equals("1")){
            if (COMMUNITY_BLOG_BANNER_LIST.size() > 0){
                viewPager.setVisibility(View.VISIBLE);
                linPagerCntParent.setVisibility(View.VISIBLE);
                viewPager.setAdapter(exMiddleFragment.viewPagerBlogAdapter);

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
            if (COMMUNITY_RUTIN_BANNER_LIST.size() > 0){
                viewPager.setVisibility(View.VISIBLE);
                linPagerCntParent.setVisibility(View.VISIBLE);
                viewPager.setAdapter(exMiddleFragment.viewPagerRutinAdapter);
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

    private void NetworkCall(String mCode){
        if (mCode.equals(NEW_COMMUNITY_LIST)){
            String orderBy = "";
            if (mMiddleTitle.equals("전체")){ //상단고정
                orderBy = "2";
            }else {                         //날짜순
                orderBy = "1";
            }
            if (mPopularity.equals("1")){
                new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),""+pagingNo,"20","3",mParentTitle,"all");  //memo 인기
            }else{
                new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),""+pagingNo,"20",orderBy,mMenuNo,mMiddleTitle.equals("전체") ? "all" : "nothing");
            }
        }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),communityNo);
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
                        communityItemsArrayList = new ArrayList<>();
                        newCommunityAdapter = new exCommunityAdapter(getActivity(), mMenuNo,mParentTitle,this);
                    }

                    for (int i = 0; i < jsonArray.length(); i++) {
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
                            communityItems.setImgListPath(JsonIsNullCheck(object, "IMAGE_FILE"));
                            communityItems.setcMenuTitle(JsonIsNullCheck(object, "C_MENU_TITLE"));
                            communityItems.setcMenuNo(JsonIsNullCheck(object,"C_MENU_NO"));
                            communityItems.setmMenuNo(JsonIsNullCheck(object,"MENU_NO"));
                            communityItems.setContents(JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n"));
                            communityItems.setHashTag(JsonIsNullCheck(object, "HASHTAG"));
                            communityItems.setLikeCnt(JsonIntIsNullCheck(object, "LIKE_CNT"));
                            communityItems.setCommentCnt(JsonIntIsNullCheck(object, "COMMENT_CNT"));
                            communityItems.setLv(JsonIsNullCheck(object, "LV"));
                            communityItems.setUserNo(JsonIsNullCheck(object, "USER_NO"));
                            communityItems.setLikeFlag(JsonIntIsNullCheck(object,"LIKE_FLAG"));
                            communityItems.setScrapFlag(JsonIntIsNullCheck(object,"SCRAP_FLAG"));
                            communityItems.setPriority(JsonIntIsNullCheck(object,"PRIORITY"));
                            communityItems.setExpandableFlag(1);

                            communityItemsArrayList.add(communityItems);
                            mList.add(new CommunityRecyclerViewItem(communityItems,ITEM_TYPE));
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
            }
            progressDialog.dismiss();
        }else{

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

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"aaaa : " + requestCode);
        if (requestCode == 4343){
            if (resultCode == RESULT_OK){
                Bundle result = new Bundle();
                result.putString("bundleKey", data.getStringExtra("text"));
                getParentFragmentManager().setFragmentResult("requestKey",result);
            }
        }
    }
}
