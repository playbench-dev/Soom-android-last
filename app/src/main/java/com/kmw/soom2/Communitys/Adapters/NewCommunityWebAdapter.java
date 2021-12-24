package com.kmw.soom2.Communitys.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.kmw.soom2.Common.Item.ExpandableTextView;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem;
import com.kmw.soom2.Communitys.Items.CustomViewPager;
import com.kmw.soom2.Communitys.Fragments.NewCommunityChildWebFragment;
import com.kmw.soom2.Communitys.Fragments.NewCommunityMiddleWebFragment;
import com.kmw.soom2.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.Utils.COMMUNITY_BLOG_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_DETAIL_MOVE;
import static com.kmw.soom2.Common.Utils.COMMUNITY_RUTIN_BANNER_LIST;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.HEADER_TYPE;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.ITEM_TYPE;

public class NewCommunityWebAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "NewCommunityAdapter";

    public ArrayList<CommunityRecyclerViewItem> itemArrayList;
    private Context context;
    private NewCommunityViewPagerAdapter adapter;
    private Fragment fragment;
    private String mTag;
    private String mParent;

    public NewCommunityWebAdapter(Context context, Fragment fragment, String mTag, String mParent) {
        this.context = context;
        this.fragment = fragment;
        this.mTag = mTag;
        this.mParent = mParent;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_community_web_list_item, parent, false);
                return new ViewHolderItem(view);
            }
        }
        return null;
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private ViewPager viewPager;
        private TextView txtPagerCnt;
        private TextView txtPagerTotalCnt;
        private LinearLayout linPagerCntParent;
        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            viewPager = (ViewPager) itemView.findViewById(R.id.view_pager_community_banner);
            txtPagerCnt = (TextView)itemView.findViewById(R.id.txt_community_banner_pager_cnt);
            txtPagerTotalCnt = (TextView)itemView.findViewById(R.id.txt_community_banner_pager_total_cnt);
            linPagerCntParent = (LinearLayout)itemView.findViewById(R.id.lin_pager_community_banner_cnt_parent);
            itemView.setTag(false);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        private ImageView imgProfile, imgMore;
        private CustomViewPager viewPager;
        private TextView txtName, txtDate, txtLike, txtComment, txtTag;
        private FlexboxLayout linTag;
        private TextView txtContents;
        private TextView txtMore;
        private ExpandableTextView txtLenthTrim;
        private ImageView imgLike, imgComment, imgCopy;
        private LinearLayout linLike, linComment, linCopy;
        private TextView  txtLikeIcon,txtCopyIcon;
        private LinearLayout tabStip;
        private FrameLayout frameLayout;
        private TextView txtPagerCnt;
        private ImageView imgPin;
        private LinearLayout linPagerCnt;
        private TextView txtCmenuNo;
        private TextView txtLabel;
        private LinearLayout linTagVisible;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.img_community_list_item_profile);
            imgMore = (ImageView) itemView.findViewById(R.id.img_community_list_item_more);
            txtName = (TextView) itemView.findViewById(R.id.txt_community_list_item_name);
            txtDate = (TextView) itemView.findViewById(R.id.txt_community_list_item_date);
            txtContents = (TextView) itemView.findViewById(R.id.txt_community_list_item_contents);
            txtMore = (TextView) itemView.findViewById(R.id.txt_community_list_item_more);
            txtLenthTrim = (ExpandableTextView) itemView.findViewById(R.id.txt_length_trim);
            txtLike = (TextView) itemView.findViewById(R.id.txt_community_list_item_like_cnt);
            txtComment = (TextView) itemView.findViewById(R.id.txt_community_list_item_comment_cnt);
            viewPager = (CustomViewPager) itemView.findViewById(R.id.view_pager_community_list_item_picture);
            imgLike = (ImageView) itemView.findViewById(R.id.img_community_list_item_like_icon);
            txtTag = (TextView) itemView.findViewById(R.id.txt_community_list_item_tag);
            imgComment = (ImageView) itemView.findViewById(R.id.img_community_list_item_comment_icon);
            linTag = (FlexboxLayout)itemView.findViewById(R.id.lin_community_list_item_tag_parent);
            txtPagerCnt = (TextView)itemView.findViewById(R.id.txt_community_pager_cnt);
            imgPin = (ImageView)itemView.findViewById(R.id.img_community_list_item_pin);
            linPagerCnt = (LinearLayout)itemView.findViewById(R.id.lin_community_pager_cnt);
            txtCmenuNo = (TextView)itemView.findViewById(R.id.txt_new_community_list_item_category_text);
            txtLabel = (TextView)itemView.findViewById(R.id.txt_community_list_item_label);
            linTagVisible = (LinearLayout)itemView.findViewById(R.id.lin_new_community_list_item_tag_visible);

            txtLikeIcon = (TextView)itemView.findViewById(R.id.txt_community_list_item_like_icon) ;

            linLike = (LinearLayout)itemView.findViewById(R.id.lin_community_list_item_like_icon);
            linComment = (LinearLayout)itemView.findViewById(R.id.lin_community_list_item_comment_icon);

            imgCopy = (ImageView) itemView.findViewById(R.id.img_community_list_item_shared_icon);
            txtCopyIcon = (TextView)itemView.findViewById(R.id.txt_community_list_item_shared_icon) ;
            linCopy = (LinearLayout)itemView.findViewById(R.id.lin_community_list_item_shared_icon);

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
                    if (mParent.equals("1")){
                        if (COMMUNITY_BLOG_BANNER_LIST.size() > 0){
                            ((ViewHolderHeader)holder).viewPager.setVisibility(View.VISIBLE);
                            ((ViewHolderHeader)holder).linPagerCntParent.setVisibility(View.VISIBLE);
                            ((ViewHolderHeader)holder).viewPager.setAdapter(NewCommunityMiddleWebFragment.viewPagerBlogAdapter);
                            ((ViewHolderHeader)holder).txtPagerCnt.setText("1");
                            ((ViewHolderHeader)holder).txtPagerTotalCnt.setText("/"+COMMUNITY_BLOG_BANNER_LIST.size());
                            ((ViewHolderHeader)holder).viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    int pos = position % COMMUNITY_BLOG_BANNER_LIST.size();
                                    ((ViewHolderHeader)holder).txtPagerCnt.setText(""+(pos+1));
                                    ((ViewHolderHeader)holder).txtPagerTotalCnt.setText("/"+COMMUNITY_BLOG_BANNER_LIST.size());
//                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                        }else{
                            ((ViewHolderHeader)holder).viewPager.setVisibility(View.GONE);
                            ((ViewHolderHeader)holder).linPagerCntParent.setVisibility(View.GONE);
                        }

                    }else if (mParent.equals("2")){
                        if (COMMUNITY_RUTIN_BANNER_LIST.size() > 0){
                            ((ViewHolderHeader)holder).viewPager.setVisibility(View.VISIBLE);
                            ((ViewHolderHeader)holder).linPagerCntParent.setVisibility(View.VISIBLE);
                            ((ViewHolderHeader)holder).viewPager.setAdapter(NewCommunityMiddleWebFragment.viewPagerRutinAdapter);
                            ((ViewHolderHeader)holder).txtPagerCnt.setText("1");
                            ((ViewHolderHeader)holder).txtPagerTotalCnt.setText("/"+COMMUNITY_RUTIN_BANNER_LIST.size());
                            ((ViewHolderHeader)holder).viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    int pos = position % COMMUNITY_RUTIN_BANNER_LIST.size();
                                    ((ViewHolderHeader)holder).txtPagerCnt.setText(""+(pos+1));
                                    ((ViewHolderHeader)holder).txtPagerTotalCnt.setText("/"+COMMUNITY_RUTIN_BANNER_LIST.size());
//                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                        }else{
                            ((ViewHolderHeader)holder).viewPager.setVisibility(View.GONE);
                            ((ViewHolderHeader)holder).linPagerCntParent.setVisibility(View.GONE);
                        }
                    }
                    break;
                }
                case ITEM_TYPE : {
                    CommunityItems communityItems = itemArrayList.get(position).getCommunityItems();

                    RequestOptions requestOptions = new RequestOptions();

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
                    if (communityItems.getLabel().length() != 0){
                        ((ViewHolderItem) holder).txtLabel.setVisibility(View.VISIBLE);
                        ((ViewHolderItem) holder).txtLabel.setText(communityItems.getLabel());
                    }else{
                        ((ViewHolderItem) holder).txtLabel.setVisibility(View.GONE);
                    }
                    if (communityItems.getLabelColor().length() != 0){
                        ((ViewHolderItem) holder).txtLabel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(communityItems.getLabelColor())));
                    }

                    ((ViewHolderItem) holder).txtDate.setText(communityItems.getDate().substring(2,16).replace("-","."));
                    ((ViewHolderItem) holder).txtContents.setText(communityItems.getContents().trim());

                    int cnt = StringUtils.countMatches(communityItems.getContents().trim(),"\n");

                    if (communityItems.getLineCnt() == 0) {
                        ((ViewHolderItem) holder).txtContents.post(() -> {
                            communityItems.setLineCnt(((ViewHolderItem) holder).txtContents.getLineCount());
                            if (communityItems.getName().equals("마구")){
                                Log.i(TAG,"line : " + communityItems.getLineCnt());
                            }
                            if (communityItems.getLineCnt() > 6 && communityItems.getExpandableFlag() == 1) {
                                ((ViewHolderItem) holder).txtMore.setVisibility(View.VISIBLE);
                                if (communityItems.getImgListPath().length() > 0){
                                    ((ViewHolderItem) holder).txtContents.setMaxLines(4);
                                }else{
                                    ((ViewHolderItem) holder).txtContents.setMaxLines(6);
                                }
                            } else {
                                ((ViewHolderItem) holder).txtMore.setVisibility(View.GONE);
                                ((ViewHolderItem) holder).txtContents.setMaxLines(Integer.MAX_VALUE);
                            }
                            this.notifyDataSetChanged();
                        });
                    }else {
                        if (communityItems.getLineCnt() > 6 && communityItems.getExpandableFlag() == 1){
                            ((ViewHolderItem) holder).txtMore.setVisibility(View.VISIBLE);
                            if (communityItems.getName().equals("마구")){
                                Log.i(TAG,"line1 : " + communityItems.getLineCnt());
                            }
                            if (communityItems.getImgListPath().length() > 0){
                                ((ViewHolderItem) holder).txtContents.setMaxLines(4);
                            }else{
                                ((ViewHolderItem) holder).txtContents.setMaxLines(6);
                            }
                        }else{
                            ((ViewHolderItem) holder).txtMore.setVisibility(View.GONE);
                            ((ViewHolderItem) holder).txtContents.setMaxLines(Integer.MAX_VALUE);
                        }
                    }

                    ((ViewHolderItem) holder).txtMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, NewCommunityDetailActivity.class);
                            i.putExtra("communityNo", communityItems.getNo());
                            i.putExtra("userNo",communityItems.getUserNo());
                            i.putExtra("contents",communityItems.getContents().trim());
                            i.putExtra("hashTag",communityItems.getHashTag());
                            i.putExtra("imgsPath",communityItems.getImgListPath());
                            i.putExtra("position", position);
                            if (fragment == null){
                                ((Activity)context).startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }else{
                                fragment.startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }
                        }
                    });

                    if (communityItems.getcMenuNo().length() == 0 || communityItems.getcMenuNo().equals("0") || !(mTag.equals("1") || mTag.equals("2") || mTag.equals("3"))){
                        ((ViewHolderItem)holder).txtCmenuNo.setVisibility(View.GONE);
                    }else{
                        ((ViewHolderItem)holder).txtCmenuNo.setVisibility(View.VISIBLE);
                        ((ViewHolderItem)holder).txtCmenuNo.setText(communityItems.getcMenuTitle());
                        if (mTag.equals("1")){
                            ((ViewHolderItem)holder).txtCmenuNo.setTextColor(Color.parseColor("#09D182"));
                            ((ViewHolderItem)holder).txtCmenuNo.setBackgroundResource(R.drawable.community_middle_category_on_01);
                        }else if (mTag.equals("2")){
                            ((ViewHolderItem)holder).txtCmenuNo.setTextColor(Color.parseColor("#6B8CBF"));
                            ((ViewHolderItem)holder).txtCmenuNo.setBackgroundResource(R.drawable.community_middle_category_on_02);
                        }else if (mTag.equals("3")){
                            ((ViewHolderItem)holder).txtCmenuNo.setTextColor(Color.parseColor("#CEB218"));
                            ((ViewHolderItem)holder).txtCmenuNo.setBackgroundResource(R.drawable.community_middle_category_on_03);
                        }
                    }

//                    if (communityItems.getPriority() == 99 || communityItems.getPriority() == 0){
//                        ((ViewHolderItem)holder).imgPin.setVisibility(View.GONE);
//                    }else{
//                        ((ViewHolderItem)holder).imgPin.setVisibility(View.VISIBLE);
//                    }

                    String[] hashTagList = communityItems.getHashTag().split(",");
                    String hasgTag = "";

                    ((ViewHolderItem) holder).linTag.removeAllViews();

                    if (communityItems.getHashTag().length() == 0) {
                        ((ViewHolderItem) holder).linTag.setVisibility(View.GONE);
                    } else {
                        ((ViewHolderItem) holder).linTag.setVisibility(View.VISIBLE);
                    }

                    Typeface face = ResourcesCompat.getFont(context, R.font.notosanscjkkr_medium);

                    if (communityItems.getHashTag().length() == 0){
                        ((ViewHolderItem) holder).linTagVisible.setVisibility(View.GONE);
                    }else{
                        int hashTagSize = 0;
                        if (hashTagList.length > 2){
                            hashTagSize = 2;
                        }else{
                            hashTagSize = hashTagList.length;
                        }
                        for (int i = 0; i < hashTagSize; i++) {
                            hasgTag += "#" + hashTagList[i];
                            TextView textView = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            if (i == 0){
                                params.rightMargin = 11;
                            }
                            textView.setLayoutParams(params);

                            textView.setText("#" + hashTagList[i] + " ");
                            textView.setTextColor(context.getColor(R.color.color343434));
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                            textView.setTypeface(face);
                            textView.setIncludeFontPadding(false);
                            ((ViewHolderItem) holder).linTagVisible.setVisibility(View.VISIBLE);
                            ((ViewHolderItem) holder).linTag.addView(textView);
                        }
                    }

                    ((ViewHolderItem) holder).txtLike.setText(""+communityItems.getLikeCnt());
                    ((ViewHolderItem) holder).txtComment.setText(""+communityItems.getCommentCnt());

                    String[] imgList = communityItems.getImgListPath().split(",");

                    if (communityItems.getImgListPath().length() == 0) {
                        ((ViewHolderItem) holder).frameLayout.setVisibility(View.GONE);
                    } else {
                        ((ViewHolderItem) holder).frameLayout.setVisibility(View.VISIBLE);
                    }

                    if (!imgList[0].endsWith(".mp4")) {
                        adapter = new NewCommunityViewPagerAdapter(context, 0);

                        for (int i = 0; i < imgList.length; i++) {
                            adapter.addItem(imgList[i]);
                        }
                        if (imgList.length > 1){
                            ((ViewHolderItem) holder).linPagerCnt.setVisibility(View.VISIBLE);
                            ((ViewHolderItem) holder).txtPagerCnt.setText("1+");
                        }else{
                            ((ViewHolderItem) holder).linPagerCnt.setVisibility(View.GONE);
                        }
                    } else {
                        adapter = new NewCommunityViewPagerAdapter(context, 1);
                        ((ViewHolderItem) holder).linPagerCnt.setVisibility(View.GONE);
                        for (int i = 0; i < imgList.length; i++) {
                            adapter.addItem(imgList[i]);
                        }
                        ((ViewHolderItem) holder).viewPager.setPagingDisabled();
                    }
                    ((ViewHolderItem) holder).viewPager.setAdapter(adapter);

                    if (communityItems.getLikeFlag() == 1){
                        ((ViewHolderItem) holder).imgLike.setImageResource(R.drawable.ic_like_on);
                        ((ViewHolderItem) holder).txtLikeIcon.setTextColor(context.getColor(R.color.color09D192));
                        ((ViewHolderItem) holder).txtLike.setTextColor(context.getColor(R.color.color09D192));
                    }else{
                        ((ViewHolderItem) holder).imgLike.setImageResource(R.drawable.ic_like_off);
                        ((ViewHolderItem) holder).txtLikeIcon.setTextColor(context.getColor(R.color.color626262));
                        ((ViewHolderItem) holder).txtLike.setTextColor(context.getColor(R.color.color626262));
                    }

                    if (Utils.VERSION.equals("1.5.7")){
                        if (communityItems.getScrapFlag() == 1){
                            ((ViewHolderItem) holder).imgCopy.setImageResource(R.drawable.ic_copy_on);
                            ((ViewHolderItem) holder).txtCopyIcon.setTextColor(context.getColor(R.color.color09D192));
                        }else{
                            ((ViewHolderItem) holder).imgCopy.setImageResource(R.drawable.ic_copy_off);
                            ((ViewHolderItem) holder).txtCopyIcon.setTextColor(context.getColor(R.color.color626262));
                        }
                    }else{

                    }

                    ((ViewHolderItem) holder).txtCmenuNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("text",((ViewHolderItem) holder).txtCmenuNo.getText().toString());
                            fragment.onActivityResult(4343,RESULT_OK,intent);
                        }
                    });

                    ((ViewHolderItem) holder).txtComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i(TAG,"aaaaaaaa");
                            Intent i = new Intent(context, NewCommunityDetailActivity.class);
                            i.putExtra("communityNo", communityItems.getNo());
                            i.putExtra("userNo",communityItems.getUserNo());
                            i.putExtra("contents",communityItems.getContents().trim());
                            i.putExtra("hashTag",communityItems.getHashTag());
                            i.putExtra("imgsPath",communityItems.getImgListPath());
                            i.putExtra("position", position);
                            i.putExtra("comment",true);
                            i.putExtra("comment1",true);
                            if (fragment == null){
                                ((Activity)context).startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }else{
                                fragment.startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }
                        }
                    });

                    ((ViewHolderItem) holder).linLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (communityItems.getLikeFlag() == 1){
                                new NewCommunityChildWebFragment.DeleteCommunityLikeNetWork().execute(communityItems.getNo(), "1");
                                ((ViewHolderItem) holder).imgLike.setImageResource(R.drawable.ic_like_off);
                                ((ViewHolderItem) holder).txtLikeIcon.setTextColor(context.getColor(R.color.color626262));
                                communityItems.setLikeCnt(communityItems.getLikeCnt() - 1);
                                communityItems.setLikeFlag(0);
                                ((ViewHolderItem) holder).txtLike.setTextColor(context.getColor(R.color.color626262));
                                ((ViewHolderItem) holder).txtLike.setText(""+communityItems.getLikeCnt());
                            }else{
                                new NewCommunityChildWebFragment.InsertCommunityLikeNetWork().execute(communityItems.getNo(), "1");
                                ((ViewHolderItem) holder).imgLike.setImageResource(R.drawable.ic_like_on);
                                ((ViewHolderItem) holder).txtLikeIcon.setTextColor(context.getColor(R.color.color09D192));
                                communityItems.setLikeCnt(communityItems.getLikeCnt() + 1);
                                communityItems.setLikeFlag(1);
                                ((ViewHolderItem) holder).txtLike.setTextColor(context.getColor(R.color.color09D192));
                                ((ViewHolderItem) holder).txtLike.setText(""+communityItems.getLikeCnt());
                            }
                        }
                    });

                    ((ViewHolderItem) holder).linCopy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Utils.VERSION.equals("1.5.7")){
                                if (communityItems.getScrapFlag() == 1){
                                    new NewCommunityChildWebFragment.DeleteCommunityLikeNetWork().execute(communityItems.getNo(), "2");
                                    ((ViewHolderItem) holder).imgCopy.setImageResource(R.drawable.ic_copy_off);
                                    ((ViewHolderItem) holder).txtCopyIcon.setTextColor(context.getColor(R.color.color626262));
                                    communityItems.setScrapFlag(0);
                                }else{
                                    new NewCommunityChildWebFragment.InsertCommunityLikeNetWork().execute(communityItems.getNo(), "2");
                                    ((ViewHolderItem) holder).imgCopy.setImageResource(R.drawable.ic_copy_on);
                                    ((ViewHolderItem) holder).txtCopyIcon.setTextColor(context.getColor(R.color.color09D192));
                                    communityItems.setScrapFlag(1);
                                }
                            }else{
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/html");
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://soomcare.co.kr/community?COMMUNITY_NO="+communityItems.getNo());
                                context.startActivity(Intent.createChooser(sharingIntent,"공유하기"));
                            }
                        }
                    });

                    ((ViewHolderItem) holder).linComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, NewCommunityDetailActivity.class);
                            i.putExtra("communityNo", communityItems.getNo());
                            i.putExtra("userNo",communityItems.getUserNo());
                            i.putExtra("contents",communityItems.getContents().trim());
                            i.putExtra("hashTag",communityItems.getHashTag());
                            i.putExtra("imgsPath",communityItems.getImgListPath());
                            i.putExtra("position", position);
                            i.putExtra("comment",true);
                            if (fragment == null){
                                ((Activity)context).startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }else{
                                fragment.startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }
                        }
                    });

                    ((ViewHolderItem) holder).txtContents.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, NewCommunityDetailActivity.class);
                            i.putExtra("communityNo", communityItems.getNo());
                            i.putExtra("userNo",communityItems.getUserNo());
                            i.putExtra("contents",communityItems.getContents().trim());
                            i.putExtra("hashTag",communityItems.getHashTag());
                            i.putExtra("imgsPath",communityItems.getImgListPath());
                            i.putExtra("position", position);
                            if (fragment == null){
                                ((Activity)context).startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }else{
                                fragment.startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }
                        }
                    });

                    ((ViewHolderItem) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, NewCommunityDetailActivity.class);
                            i.putExtra("communityNo", communityItems.getNo());
                            i.putExtra("userNo",communityItems.getUserNo());
                            i.putExtra("contents",communityItems.getContents().trim());
                            i.putExtra("hashTag",communityItems.getHashTag());
                            i.putExtra("imgsPath",communityItems.getImgListPath());
                            i.putExtra("position", position);
                            if (fragment == null){
                                ((Activity)context).startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }else{
                                fragment.startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                            }
                        }
                    });
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

