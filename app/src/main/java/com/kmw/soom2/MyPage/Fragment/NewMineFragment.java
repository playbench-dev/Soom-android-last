package com.kmw.soom2.MyPage.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.MyPage.Adapter.NewPostMineAdapter;
import com.kmw.soom2.R;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_LIKE_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_LIKE_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_LIST_MINE_COMMENT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_LIST_MINE_SCRAP;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_LIST_MINE_WRITE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_SCRAP_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_SCRAP_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DETAIL;
import static com.kmw.soom2.Common.Utils.COMMUNITY_DETAIL_MOVE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_REFRESH;
import static com.kmw.soom2.Common.Utils.COMMUNITY_WRITE_REFRESH;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class NewMineFragment extends Fragment implements AsyncResponse {

    private String TAG = "NewMineFragment";
    RecyclerView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    NewPostMineAdapter adapter;
    TextView txtNoItem;
    ImageView imgNoItem;
    LinearLayout linNoItem;
    private String communityNo = "";
    private int position = 0;
    int paging = 1;
    int communitySearchTotalPage;
    boolean mLockScrollView = false;
    private String mTitle = "";
    private String POST_MINE_WRITE = "5555";
    private String POST_MINE_COMMENT = "5556";
    private String POST_MINE_SCRAP = "5557";

    ProgressDialog progressDialog;

    public NewMineFragment() {

    }

    public static NewMineFragment newInstance(String mTitle){

        NewMineFragment newMineFragment = new NewMineFragment();

        Bundle args = new Bundle();
        args.putString("mTitle",mTitle);
        newMineFragment.setArguments(args);

        return newMineFragment;
    }

    public String getInstance(String mKey){
        return getArguments().getString(mKey);
    }


    @Override
    public void onResume() {
        super.onResume();

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_mine, container, false);

        listView = (RecyclerView)view.findViewById(R.id.list_view_my_page_writing);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh_new_mine);
        txtNoItem = (TextView)view.findViewById(R.id.txt_new_mine_no_list_text);
        imgNoItem = (ImageView) view.findViewById(R.id.img_new_mine_no_list_icon);
        linNoItem = (LinearLayout)view.findViewById(R.id.lin_new_mine_no_list);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));

        NullCheck(getActivity());

        mTitle = getInstance("mTitle");

        Log.i(TAG,"mTitle : " + mTitle);

        if (mTitle.equals("내가 쓴 글")){
            txtNoItem.setText(getResources().getText(R.string.mine_write));
            imgNoItem.setImageResource(R.drawable.ic_mine_write);
            adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_WRITE);
            NetWorkCall(COMMUNITY_LIST_MINE_WRITE);
        }else if (mTitle.equals("댓글 단 글")){
            txtNoItem.setText(getResources().getText(R.string.mine_comment));
            imgNoItem.setImageResource(R.drawable.ic_mine_comment);
            adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_COMMENT);
            NetWorkCall(COMMUNITY_LIST_MINE_COMMENT);
        }else if (mTitle.equals("저장한 글")){
            txtNoItem.setText(getResources().getText(R.string.mine_scrap));
            imgNoItem.setImageResource(R.drawable.ic_mine_scrap);
            adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_SCRAP);
            NetWorkCall(COMMUNITY_LIST_MINE_SCRAP);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                paging = 1;
                if (mTitle.equals("내가 쓴 글")){
                    adapter = new NewPostMineAdapter(getActivity(),NewMineFragment.this,POST_MINE_WRITE);
                    NetWorkCall(COMMUNITY_LIST_MINE_WRITE);
                }else if (mTitle.equals("댓글 단 글")){
                    adapter = new NewPostMineAdapter(getActivity(),NewMineFragment.this,POST_MINE_COMMENT);
                    NetWorkCall(COMMUNITY_LIST_MINE_COMMENT);
                }else if (mTitle.equals("저장한 글")){
                    adapter = new NewPostMineAdapter(getActivity(),NewMineFragment.this,POST_MINE_SCRAP);
                    NetWorkCall(COMMUNITY_LIST_MINE_SCRAP);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int lastVisibleItemPosition = ((LinearLayoutManager)listView.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCnt = listView.getAdapter().getItemCount();

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && paging < communitySearchTotalPage && mLockScrollView) {
                    mLockScrollView = false;
                    ++paging;
                    if (mTitle.equals("내가 쓴 글")){
                        NetWorkCall(COMMUNITY_LIST_MINE_WRITE);
                    }else if (mTitle.equals("댓글 단 글")){
                        NetWorkCall(COMMUNITY_LIST_MINE_COMMENT);
                    }else if (mTitle.equals("저장한 글")){
                        NetWorkCall(COMMUNITY_LIST_MINE_SCRAP);
                    }
                }
            }
        });

        return view;
    }

    public void NetWorkCall(String mCode){
        if (mCode.equals(COMMUNITY_LIST_MINE_WRITE) || mCode.equals(COMMUNITY_LIST_MINE_COMMENT) || mCode.equals(COMMUNITY_LIST_MINE_SCRAP)){
            if (progressDialog == null){
                if(getActivity() != null){
                    progressDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
                    progressDialog.show();
                }
            }
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+userItem.getUserNo(),""+paging,"15");
        }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+userItem.getUserNo(),communityNo);
        }
    }

    public void NetWorkCall(String mCode, String no){
        if (mCode.equals(COMMUNITY_LIKE_INSERT) || mCode.equals(COMMUNITY_LIKE_DELETE) || mCode.equals(COMMUNITY_SCRAP_INSERT) || mCode.equals(COMMUNITY_SCRAP_DELETE)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+userItem.getUserNo(),no);
        }else if (mCode.equals(NEW_COMMUNITY_DELETE)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(no);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(COMMUNITY_LIST_MINE_WRITE) || mCode.equals(COMMUNITY_LIST_MINE_COMMENT) || mCode.equals(COMMUNITY_LIST_MINE_SCRAP)){
                try {

                    JSONObject jsonObject = new JSONObject(mResult);
                    if (!jsonObject.has("list")){
                        if (progressDialog != null)
                        progressDialog.dismiss();
                        swipeRefreshLayout.setVisibility(View.GONE);
                        linNoItem.setVisibility(View.VISIBLE);
                        return;
                    }
                    linNoItem.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    communitySearchTotalPage = JsonIntIsNullCheck(jsonObject,"Search_TotalPage");

                    int size = jsonArray.length();

                    Log.i(TAG, "result : " + mResult);

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

                            communityItems.setcMenuNo(JsonIsNullCheck(object, "C_MENU_TITLE"));
                            communityItems.setImgListPath(JsonIsNullCheck(object, "IMAGE_FILE"));
//                            communityItems.setcMenuNo(JsonIsNullCheck(object,"C_MENU_NO"));
//                            communityItems.setmMenuNo(JsonIsNullCheck(object,"MENU_NO"));
                            communityItems.setContents(JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n"));
                            communityItems.setHashTag(JsonIsNullCheck(object, "HASHTAG"));
                            communityItems.setLikeCnt(JsonIntIsNullCheck(object, "LIKE_CNT"));
                            communityItems.setCommentCnt(JsonIntIsNullCheck(object, "COMMENT_CNT"));
                            communityItems.setLv(JsonIsNullCheck(object, "LV"));
                            communityItems.setUserNo(JsonIsNullCheck(object, "USER_NO"));
                            communityItems.setLikeFlag(JsonIntIsNullCheck(object,"LIKE_FLAG"));
                            communityItems.setScrapFlag(JsonIntIsNullCheck(object,"SCRAP_FLAG"));
                            communityItems.setExpandableFlag(1);

                            adapter.addItem(communityItems);
                        }
                    }

                    mLockScrollView = true;

                    if (paging == 1){
                        listView.setAdapter(adapter);
                    }
                    adapter.notifyDataSetChanged();

                    progressDialog.dismiss();

                } catch (JSONException e) {

                }
            }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
                try {
                    Log.i(TAG,"mine : " + mResult);
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
                    communityItems.setcMenuNo(JsonIsNullCheck(object, "C_MENU_TITLE"));
                    communityItems.setImgListPath(JsonIsNullCheck(object, "IMAGE_FILE"));
                    communityItems.setContents(JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n"));
                    communityItems.setHashTag(JsonIsNullCheck(object, "HASHTAG"));
                    communityItems.setLikeCnt(JsonIntIsNullCheck(object, "LIKE_CNT"));
                    communityItems.setCommentCnt(JsonIntIsNullCheck(object, "COMMENT_CNT"));
                    communityItems.setLv(JsonIsNullCheck(object, "LV"));
                    communityItems.setUserNo(JsonIsNullCheck(object, "USER_NO"));
                    communityItems.setLikeFlag(JsonIntIsNullCheck(object,"LIKE_FLAG"));
                    communityItems.setScrapFlag(JsonIntIsNullCheck(object,"SCRAP_FLAG"));
                    communityItems.setExpandableFlag(1);

                    adapter.itemArrayList.set(position,communityItems);
                    adapter.notifyDataSetChanged();

                }catch (JSONException e){

                }
            }else if (mCode.equals(NEW_COMMUNITY_DELETE)){
                if (adapter.getItemCount() == 0){
                    swipeRefreshLayout.setVisibility(View.GONE);
                    linNoItem.setVisibility(View.VISIBLE);
                }
            }else if (mCode.equals(COMMUNITY_SCRAP_INSERT)) {
                Toast.makeText(getActivity(), "게시글을 저장했습니다.", Toast.LENGTH_SHORT).show();
            }else if (mCode.equals(COMMUNITY_SCRAP_DELETE)){
                Toast.makeText(getActivity(), "게시글 저장을 취소했습니다.", Toast.LENGTH_SHORT).show();
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

        Log.i(TAG,"requestCode : " + requestCode);

        if (requestCode == COMMUNITY_REFRESH){    //  탭 클릭시 refresh
            if (resultCode == RESULT_OK){
                paging = 1;
                if (mTitle.equals("내가 쓴 글")){
                    adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_WRITE);
                    NetWorkCall(COMMUNITY_LIST_MINE_WRITE);
                }else if (mTitle.equals("댓글 단 글")){
                    adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_COMMENT);
                    NetWorkCall(COMMUNITY_LIST_MINE_COMMENT);
                }else if (mTitle.equals("저장한 글")){
                    adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_SCRAP);
                    NetWorkCall(COMMUNITY_LIST_MINE_SCRAP);
                }
            }
        }else if (requestCode == Integer.parseInt(POST_MINE_WRITE)){   // 탭 별 수정 시 refresh
            if (resultCode == RESULT_OK){
                position = data.getIntExtra("position",-1);
                communityNo = data.getStringExtra("communityNo");
                NetWorkCall(NEW_COMMUNITY_DETAIL);
            }
        }else if (requestCode == Integer.parseInt(POST_MINE_COMMENT)){
            if (resultCode == RESULT_OK){
                position = data.getIntExtra("position",-1);
                communityNo = data.getStringExtra("communityNo");
                NetWorkCall(NEW_COMMUNITY_DETAIL);
            }
        }else if (requestCode == Integer.parseInt(POST_MINE_SCRAP)){
            if (resultCode == RESULT_OK){
                position = data.getIntExtra("position",-1);
                communityNo = data.getStringExtra("communityNo");
                NetWorkCall(NEW_COMMUNITY_DETAIL);
            }
        }else if (requestCode == COMMUNITY_WRITE_REFRESH){  //글 작성시 refresh
            if (resultCode == RESULT_OK){
                paging = 1;
                if (mTitle.equals("내가 쓴 글")){
                    adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_WRITE);
                    NetWorkCall(COMMUNITY_LIST_MINE_WRITE);
                }else if (mTitle.equals("댓글 단 글")){
                    adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_COMMENT);
                    NetWorkCall(COMMUNITY_LIST_MINE_COMMENT);
                }else if (mTitle.equals("저장한 글")){
                    adapter = new NewPostMineAdapter(getActivity(),this,POST_MINE_SCRAP);
                    NetWorkCall(COMMUNITY_LIST_MINE_SCRAP);
                }
            }
        }else if (requestCode == COMMUNITY_DETAIL_MOVE){
            if (resultCode == RESULT_OK){
                Log.i(TAG,"커뮤니티 리스트 RESULT_OK");
                //memo 상세화면에서 삭제시 돌아온 후에 게시물 삭제
                if (data != null){
                    if (data.hasExtra("communityRemove")){
                        Log.i(TAG,"커뮤니티 리스트 if");
                        position = data.getIntExtra("position",-1);
                        adapter.itemArrayList.remove(position);
                        adapter.notifyDataSetChanged();
                    }else{
                        Log.i(TAG,"커뮤니티 리스트 else");
                        if (data.hasExtra("position")){
                            position = data.getIntExtra("position",-1);
                            communityNo = data.getStringExtra("communityNo");
                            NetWorkCall(NEW_COMMUNITY_DETAIL);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
