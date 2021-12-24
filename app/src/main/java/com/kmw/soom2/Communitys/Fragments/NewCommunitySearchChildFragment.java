package com.kmw.soom2.Communitys.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.NewCommunitySearchActivity;
import com.kmw.soom2.Communitys.Adapters.NewCommunitySearchAdapter;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem;
import com.kmw.soom2.R;
import com.kmw.soom2.SplashActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DETAIL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_TEXT_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_DETAIL_MOVE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_REFRESH;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECT_TAG;
import static com.kmw.soom2.Common.Utils.COMMUNITY_WRITE_REFRESH;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NEW_COMMUNITY_SEARCH;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.preferencesName;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.ITEM_TYPE;

public class NewCommunitySearchChildFragment extends Fragment implements AsyncResponse {

    private String TAG = "NewCommunityChildWebFragment";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private String mMenuNo = "";
    private String mParentTitle;
    private String mMiddleTitle;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private NewCommunitySearchAdapter newCommunityAdapter;
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
    private LinearLayout linPagerCnt;
    private TextView txtNoList;

    public NewCommunitySearchChildFragment() {

    }

    public static NewCommunitySearchChildFragment newInstance(String mMenuNo, String mParentTitle, String mMiddleTitle){

        NewCommunitySearchChildFragment newFragment = new NewCommunitySearchChildFragment();

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
        View v = inflater.inflate(R.layout.fragment_child_new, container, false);

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

        COMMUNITY_SELECT_TAG = mMenuNo;

        //mMiddleTitle == 전체 ? MENU_NO : C_MENU_NO
        NetworkCall(NEW_COMMUNITY_TEXT_LIST);

        return v;
    }

    private void FindViewById(View view){
        swipeRefreshLayout          = view.findViewById(R.id.community_child_new_refresh_layout);
        recyclerView                = view.findViewById(R.id.community_child_new_recyclerview);
        viewPager                   = view.findViewById(R.id.view_pager_community_banner);
        linPagerCnt                 = view.findViewById(R.id.lin_pager_community_banner_cnt_parent);
        txtNoList                   = view.findViewById(R.id.txt_new_community_search_no_list);

        viewPager.setVisibility(View.GONE);
        linPagerCnt.setVisibility(View.GONE);

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
                    NetworkCall(NEW_COMMUNITY_TEXT_LIST);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pagingNo = 1;
                progressDialog.show();
                NetworkCall(NEW_COMMUNITY_TEXT_LIST);
                swipeRefreshLayout.setRefreshing(false);
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
                    NetworkCall(NEW_COMMUNITY_TEXT_LIST);
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
                        mList.remove(position);
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
        }else if (requestCode == NEW_COMMUNITY_SEARCH){
            if (resultCode == RESULT_OK){
                pagingNo = 1;
                progressDialog.show();
                NetworkCall(NEW_COMMUNITY_TEXT_LIST);
            }
        }
    }

    private void NetworkCall(String mCode){
       if (mCode.equals(NEW_COMMUNITY_TEXT_LIST)){
           new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),""+pagingNo,"20",mMenuNo, NewCommunitySearchActivity.edtSearch.getText().toString());
        }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
           new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),communityNo);
       }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            Log.i(TAG,"mResult : " + mResult);
            if (mCode.equals(NEW_COMMUNITY_TEXT_LIST)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (!jsonObject.has("list")){
                        swipeRefreshLayout.setVisibility(View.GONE);
                        txtNoList.setVisibility(View.VISIBLE);
                    }else{
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        txtNoList.setVisibility(View.GONE);
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    communitySearchTotalPage = jsonObject.getInt("Search_TotalPage");

                    if (pagingNo == 1){
                        mList = new ArrayList<>();
                        communityItemsArrayList = new ArrayList<>();
                        newCommunityAdapter = new NewCommunitySearchAdapter(getActivity(),this, mMenuNo);
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
            }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
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
    public void onPause() {
        super.onPause();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}

