package com.kmw.soom2.Communitys.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.Communitys.Activitys.NewCommunitySearchActivity;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem;
import com.kmw.soom2.Communitys.Items.CustomViewPager;
import com.kmw.soom2.Home.HomeAdapter.ViewPagerAdapter;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.Community.exCommunityAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.COMMUNITY_BLOG_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_DETAIL_MOVE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.HEADER_TYPE;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.ITEM_TYPE;

public class NewCommunitySearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "NewCommunitySearchAdapter";

    public ArrayList<CommunityRecyclerViewItem> itemArrayList;
    private Context context;
    private NewCommunitySearchViewPagerAdapter adapter;
    private Fragment fragment;
    private String mTag;

    public NewCommunitySearchAdapter(Context context, Fragment fragment, String mTag) {
        this.context = context;
        this.fragment = fragment;
        this.mTag = mTag;
    }

    @Override
    public int getItemViewType(int position) {
        CommunityRecyclerViewItem item = itemArrayList.get(position);
        if (item != null) {
            return item.getViewType();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case HEADER_TYPE : {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_community_baner_item, parent, false);
                return new ViewHolderHeader(view);
            }
            case ITEM_TYPE : {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_community_search_list_item, parent, false);
                return new ViewHolderItem(view);
            }
        }
        return null;
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.view_pager_community_banner);
            itemView.setTag(false);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        private ImageView imgProfile;
        private CustomViewPager viewPager;
        private TextView txtName, txtDate;
        private TextView txtContents;
        private TextView txtMore;
        private LinearLayout tabStip;
        private FrameLayout frameLayout;
        private TextView txtCmenuNo;
        private TextView txtLabel;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.img_community_list_item_profile);
            txtName = (TextView) itemView.findViewById(R.id.txt_community_list_item_name);
            txtDate = (TextView) itemView.findViewById(R.id.txt_community_list_item_date);
            txtContents = (TextView) itemView.findViewById(R.id.txt_community_list_item_contents);
            txtMore = (TextView) itemView.findViewById(R.id.txt_community_list_item_more);
            viewPager = (CustomViewPager) itemView.findViewById(R.id.view_pager_community_list_item_picture);
            txtCmenuNo = (TextView)itemView.findViewById(R.id.txt_new_community_list_item_category_text);
            txtLabel = (TextView)itemView.findViewById(R.id.txt_community_list_item_label);

            frameLayout = (FrameLayout) itemView.findViewById(R.id.frame_community_list_item);
            itemView.setTag(false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CommunityRecyclerViewItem item = null;
        if (position < itemArrayList.size()){
            item = itemArrayList.get(position);

            switch (item.getViewType()){
                case HEADER_TYPE : {
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, COMMUNITY_BLOG_BANNER_LIST);
                    ((ViewHolderHeader)holder).viewPager.setAdapter(viewPagerAdapter);
                    break;
                }
                case ITEM_TYPE : {
                    CommunityItems communityItems = itemArrayList.get(position).getCommunityItems();

                    RequestOptions requestOptions = new RequestOptions();
//                    requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
                    if (communityItems.getProfile().length() > 0) {
                        String replaceText = communityItems.getProfile();
                        if (replaceText.contains("soom2.testserver-1.com:8080")){
                            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                        }else if (replaceText.contains("103.55.190.193")){
                            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                        }
                        if (communityItems.getProfile().contains("https:")) {
                            Glide.with(context).load(replaceText).apply(requestOptions.circleCrop()).into(((ViewHolderItem) holder).imgProfile);
                        } else {
                            Glide.with(context).load("https:" + replaceText).apply(requestOptions.circleCrop()).into(((ViewHolderItem) holder).imgProfile);
                        }
                    } else {
                        int resource = 0;
                        if (communityItems.getGender() == 1){
                            resource = R.drawable.ic_no_profile_m;
                        }else if (communityItems.getGender() == 2){
                            resource = R.drawable.ic_no_profile_w;
                        }else{
                            resource = R.drawable.ic_no_profile;
                        }
                        Glide.with(context).load(resource).apply(requestOptions.circleCrop()).into(((ViewHolderItem) holder).imgProfile);
                    }
                    ((ViewHolderItem) holder).txtName.setText(communityItems.getName());
                    if (communityItems.getLabel().length() != 0){
                        ((ViewHolderItem) holder).txtLabel.setVisibility(View.VISIBLE);
                        ((ViewHolderItem) holder).txtLabel.setText(communityItems.getLabel());
                    }else{
                        ((ViewHolderItem) holder).txtLabel.setVisibility(View.GONE);
                    }

                    if (communityItems.getLabelColor().length() > 0){
                        ((ViewHolderItem) holder).txtLabel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(communityItems.getLabelColor())));
                    }
                    ((ViewHolderItem) holder).txtDate.setText(communityItems.getDate().substring(2,16).replace("-","."));
                    ((ViewHolderItem) holder).txtContents.setText(communityItems.getContents().trim());

                    int cnt = StringUtils.countMatches(communityItems.getContents().trim(),"\n");

                    if (communityItems.getLineCnt() == 0) {
                        ((ViewHolderItem) holder).txtContents.post(() -> {
                            communityItems.setLineCnt(((ViewHolderItem) holder).txtContents.getLineCount());

                            if (communityItems.getLineCnt() > 3 && communityItems.getExpandableFlag() == 1) {
                                ((ViewHolderItem) holder).txtMore.setVisibility(View.VISIBLE);
                                ((ViewHolderItem) holder).txtContents.setMaxLines(3);
                            } else {
                                ((ViewHolderItem) holder).txtMore.setVisibility(View.GONE);
                                ((ViewHolderItem) holder).txtContents.setMaxLines(Integer.MAX_VALUE);
                            }
                            this.notifyDataSetChanged();
                        });
                    }else {
                        if (communityItems.getLineCnt() > 3 && communityItems.getExpandableFlag() == 1){
                            ((ViewHolderItem) holder).txtMore.setVisibility(View.VISIBLE);
                            ((ViewHolderItem) holder).txtContents.setMaxLines(3);
                        }else{
                            ((ViewHolderItem) holder).txtMore.setVisibility(View.GONE);
                            ((ViewHolderItem) holder).txtContents.setMaxLines(Integer.MAX_VALUE);
                        }
                    }

                    ((ViewHolderItem) holder).txtMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,NewCommunityDetailActivity.class);
                            if (NewCommunitySearchActivity.mGuest){
                                intent.putExtra("guest",true);
                            }
                            intent.putExtra("communityNo", communityItems.getNo());
                            intent.putExtra("userNo",communityItems.getUserNo());
                            intent.putExtra("contents",communityItems.getContents().trim());
                            intent.putExtra("hashTag",communityItems.getHashTag());
                            intent.putExtra("imgsPath",communityItems.getImgListPath());
                            intent.putExtra("position", position);
                            if (fragment != null){
                                fragment.startActivityForResult(intent, COMMUNITY_DETAIL_MOVE);
                            }else{
                                ((Activity)context).startActivityForResult(intent,COMMUNITY_DETAIL_MOVE);
                            }
                        }
                    });

                    ((ViewHolderItem) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context,NewCommunityDetailActivity.class);
                            if (NewCommunitySearchActivity.mGuest){
                                intent.putExtra("guest",true);
                            }
                            intent.putExtra("communityNo", communityItems.getNo());
                            intent.putExtra("userNo",communityItems.getUserNo());
                            intent.putExtra("contents",communityItems.getContents().trim());
                            intent.putExtra("hashTag",communityItems.getHashTag());
                            intent.putExtra("imgsPath",communityItems.getImgListPath());
                            intent.putExtra("position", position);
                            if (fragment != null){
                                fragment.startActivityForResult(intent, COMMUNITY_DETAIL_MOVE);
                            }else{
                                ((Activity)context).startActivityForResult(intent,COMMUNITY_DETAIL_MOVE);
                            }
                        }
                    });

                    if (communityItems.getcMenuNo().length() == 0 || communityItems.getcMenuNo().equals("0")){
                        ((ViewHolderItem)holder).txtCmenuNo.setVisibility(View.GONE);
                    }else{
                        ((ViewHolderItem)holder).txtCmenuNo.setVisibility(View.VISIBLE);
                        ((ViewHolderItem)holder).txtCmenuNo.setText(communityItems.getcMenuTitle());
//                        if (mTag.equals("1")){
//                            ((NewCommunitySearchAdapter.ViewHolderItem)holder).txtCmenuNo.setTextColor(Color.parseColor("#09D182"));
//                            ((NewCommunitySearchAdapter.ViewHolderItem)holder).txtCmenuNo.setBackgroundResource(R.drawable.community_middle_category_on_01);
//                        }else if (mTag.equals("2")){
//                            ((NewCommunitySearchAdapter.ViewHolderItem)holder).txtCmenuNo.setTextColor(Color.parseColor("#6B8CBF"));
//                            ((NewCommunitySearchAdapter.ViewHolderItem)holder).txtCmenuNo.setBackgroundResource(R.drawable.community_middle_category_on_02);
//                        }else if (mTag.equals("3")){
//                            ((NewCommunitySearchAdapter.ViewHolderItem)holder).txtCmenuNo.setTextColor(Color.parseColor("#CEB218"));
//                            ((NewCommunitySearchAdapter.ViewHolderItem)holder).txtCmenuNo.setBackgroundResource(R.drawable.community_middle_category_on_03);
//                        }
                    }

                    String[] imgList = communityItems.getImgListPath().split(",");
                    if (communityItems.getImgListPath().length() == 0){
                        ((ViewHolderItem) holder).frameLayout.setVisibility(View.GONE);
                    }else{
                        ((ViewHolderItem) holder).frameLayout.setVisibility(View.VISIBLE);
                    }

                    if (!imgList[0].endsWith(".mp4")) {
                        adapter = new NewCommunitySearchViewPagerAdapter(context, 0);

                        for (int i = 0; i < imgList.length; i++) {
                            adapter.addItem(imgList[i]);
                        }
//                        if (imgList.length > 1){
//                            ((NewCommunitySearchAdapter.ViewHolderItem) holder).linPagerCnt.setVisibility(View.VISIBLE);
//                            ((NewCommunitySearchAdapter.ViewHolderItem) holder).txtPagerCnt.setText("1+");
//                        }else{
//                            ((NewCommunitySearchAdapter.ViewHolderItem) holder).linPagerCnt.setVisibility(View.GONE);
//                        }
                    } else {
                        adapter = new NewCommunitySearchViewPagerAdapter(context, 1);
//                        ((NewCommunitySearchAdapter.ViewHolderItem) holder).linPagerCnt.setVisibility(View.GONE);
                        for (int i = 0; i < imgList.length; i++) {
                            adapter.addItem(imgList[i]);
                        }
                        ((ViewHolderItem) holder).viewPager.setPagingDisabled();
                    }
                    ((ViewHolderItem) holder).viewPager.setAdapter(adapter);

                    break;
                }
            }
        }


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public void addItem(ArrayList<CommunityRecyclerViewItem> list){
        itemArrayList = new ArrayList<>();
        itemArrayList.addAll(list);
    }

}

