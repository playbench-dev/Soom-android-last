package com.kmw.soom2.ex.Community;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem;
import com.kmw.soom2.Communitys.Items.SpacesItemDecoration;
import com.kmw.soom2.R;
import com.kmw.soom2.SplashActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DETAIL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_DETAIL_MOVE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_REFRESH;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECT_TAG;
import static com.kmw.soom2.Common.Utils.COMMUNITY_WRITE_REFRESH;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.preferencesName;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.HEADER_SPACE;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.HEADER_TYPE;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.ITEM_TYPE;

public class exChildBlogFragment extends Fragment implements AsyncResponse {

    private String TAG = "exChildBlogFragment";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String mMenuNo = "";
    private String mParentTitle;
    private String mMiddleTitle;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private exCommunityBlogAdapter newCommunityAdapter;
    private ArrayList<CommunityItems> communityItemsArrayList;
    public ArrayList<CommunityRecyclerViewItem>       mList = new ArrayList<>();

    private int communitySearchTotalPage;
    public int pagingNo = 1;
    private String communityNo = "";
    private int position = -1;
    private boolean mLockScrollView = false;
    public static Activity thisActivity = null;
    private ProgressDialog progressDialog;

    public String mPopularity = "";
    public String MIDDLE_TITLE = "";

    public String mHeaderTitle = "최신 순";
    public String mHeaderOrderBy = "1";
    public String MENU_NO = "";
    public String PARENT_MENU_NO = "";
    public Activity activity;
    public AsyncResponse asyncResponse;

    private LinearLayout linHeader;
    private TextView txtTitle;

    public exChildBlogFragment() {

    }

    public static exChildBlogFragment newInstance(String mMenuNo, String mParentTitle, String mMiddleTitle){

        exChildBlogFragment newFragment = new exChildBlogFragment();

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
                    NetworkCallBlog();
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
        View v = inflater.inflate(R.layout.fragment_child_blog, container, false);

        NullCheck(getActivity());

        pref = getActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        editor = pref.edit();

        FindViewById(v);

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

        Log.i(TAG,"mTag : " + mMenuNo + " middle : " + mMiddleTitle + " parent : " + mParentTitle);

        if (COMMUNITY_POPULARUTY != null){
            mPopularity = COMMUNITY_POPULARUTY.get(COMMUNITY_MUNU_NO_LIST.indexOf(mMenuNo));
            if (mPopularity.equals("1")){
                mHeaderTitle = "인기 순";
            }
        }

        MIDDLE_TITLE = mMiddleTitle;
        COMMUNITY_SELECT_TAG = mMenuNo;
        MENU_NO = mMenuNo;
        PARENT_MENU_NO = mParentTitle;
        activity = getActivity();
        asyncResponse = this;

        if (MIDDLE_TITLE.equals("전체")){
            mHeaderOrderBy = "2";
        }

        NetworkCallBlog();

        return v;
    }

    private void FindViewById(View view){
        swipeRefreshLayout          = view.findViewById(R.id.community_child_new_refresh_layout);
        recyclerView                = view.findViewById(R.id.community_child_new_recyclerview);
        linHeader                   = view.findViewById(R.id.lin_new_community_blog_header);
        txtTitle                    = view.findViewById(R.id.txt_new_community_blog_header_title);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCnt = recyclerView.getAdapter().getItemCount();

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && pagingNo < communitySearchTotalPage && mLockScrollView) {
                    mLockScrollView = false;
                    ++pagingNo;
                    progressDialog.show();
                    NetworkCallBlog();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pagingNo = 1;
                progressDialog.show();
                NetworkCallBlog();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        linHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHeaderTitle.equals("조회수 순")){
                    mHeaderTitle = "최신 순";
                    if (MIDDLE_TITLE.equals("전체")){
                        mHeaderOrderBy = "2";
                    }else{
                        mHeaderOrderBy = "1";
                    }
                    txtTitle.setText(mHeaderTitle);
                    pagingNo = 1;
                    NetworkCallBlog();
                }else if (mHeaderTitle.equals("최신 순")){
                    mHeaderTitle = "조회수 순";
                    mHeaderOrderBy = "4";
                    txtTitle.setText(mHeaderTitle);
                    pagingNo = 1;
                    NetworkCallBlog();
                }
            }
        });
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
                    NetworkCallBlog();
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
//                pagingNo = 1;
//                progressDialog.show();
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
        }
    }

    private void NetworkCall(String mCode){
        if (mCode.equals(NEW_COMMUNITY_LIST)){
            String orderBy = "";
            if (mMiddleTitle.equals("전체")){ //상단고정
                orderBy = "2";
            }else if (mMiddleTitle.equals("인기")){ //인기순
                orderBy = "3";
            }else {                         //날짜순
                orderBy = "1";
            }
            if (mPopularity.equals("1")){
                new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),""+pagingNo,"20","3",mParentTitle,"all");
            }else{
                new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),""+pagingNo,"20","1",mMenuNo,mMiddleTitle.equals("전체") ? "all" : "nothing");
            }

        }else if(mCode.equals(NEW_COMMUNITY_DETAIL)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),communityNo);
        }
    }

    public void NetworkCallBlog(){
        if (mPopularity.equals("1")){
            new NetworkUtils.NetworkCall(activity,asyncResponse,"Blog",NEW_COMMUNITY_LIST).execute(""+Utils.userItem.getUserNo(),""+pagingNo,"20","3",PARENT_MENU_NO,"all");
        }else{
            new NetworkUtils.NetworkCall(activity,asyncResponse,"Blog",NEW_COMMUNITY_LIST).execute(""+Utils.userItem.getUserNo(),""+pagingNo,"20",mHeaderOrderBy,MENU_NO,MIDDLE_TITLE.equals("전체") ? "all" : "nothing");
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            Log.i(TAG,"mResult : " + mResult);
            if (mCode.equals(NEW_COMMUNITY_LIST)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    communitySearchTotalPage = jsonObject.getInt("Search_TotalPage");

                    if (pagingNo == 1){
                        mList = new ArrayList<>();
                        communityItemsArrayList = new ArrayList<>();
                        newCommunityAdapter = new exCommunityBlogAdapter(getActivity(),this);
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
                            communityItems.setCommunityTitle(JsonIsNullCheck(object,"TITLE"));
                            communityItems.setCommunityExPlanation(JsonIsNullCheck(object,"EXPLANATION"));
                            communityItems.setcMenuNo(JsonIsNullCheck(object,"C_MENU_NO"));
                            communityItems.setmViewCnt(JsonIsNullCheck(object,"APP_VIEWS"));

                            if (JsonIsNullCheck(object, "UPDATE_DT").length() == 0) {
                                communityItems.setDate(JsonIsNullCheck(object, "CREATE_DT"));
                            } else {
                                communityItems.setDate(JsonIsNullCheck(object, "UPDATE_DT"));
                            }
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
    public void onPause() {
        super.onPause();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}

