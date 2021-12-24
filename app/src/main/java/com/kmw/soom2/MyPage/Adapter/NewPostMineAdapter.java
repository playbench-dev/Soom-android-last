package com.kmw.soom2.MyPage.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;
import com.kmw.soom2.Common.Item.ExpandableTextView;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.Communitys.Adapters.NewCommunityViewPagerAdapter;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CustomViewPager;
import com.kmw.soom2.Communitys.Fragments.NewCommunityChildWebFragment;
import com.kmw.soom2.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.COMMUNITY_DETAIL_MOVE;

public class NewPostMineAdapter extends RecyclerView.Adapter<NewPostMineAdapter.ViewHolder> {

    private String TAG = "NewCommunityAdapter";
    public ArrayList<CommunityItems> itemArrayList = new ArrayList<>();
    private Context context;
    private Display display;
    private WindowManager wm;
    private NewCommunityViewPagerAdapter adapter;
    private Fragment fragment;
    private String mTag;

    public NewPostMineAdapter(Context context, Fragment fragment, String mTag) {
        this.context = context;
        this.fragment = fragment;
        this.mTag = mTag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = null;

        view = inflater.inflate(R.layout.view_new_community_web_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        Log.i(TAG,"size : " + position);

        CommunityItems communityItems = itemArrayList.get(position);

        RequestOptions requestOptions = new RequestOptions();

        if (communityItems.getProfile().length() > 0) {
            String replaceText = communityItems.getProfile();
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            if (communityItems.getProfile().contains("https:")) {
                Glide.with(context).load(replaceText).apply(requestOptions.circleCrop()).into(viewHolder.imgProfile);
            } else {
                Glide.with(context).load("https:" + replaceText).apply(requestOptions.circleCrop()).into(viewHolder.imgProfile);
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
            Glide.with(context).load(resource).apply(requestOptions.circleCrop()).into(viewHolder.imgProfile);
        }
        viewHolder.txtName.setText(communityItems.getName());

        if (communityItems.getLabel().length() != 0){
            viewHolder.txtLabel.setVisibility(View.VISIBLE);
            viewHolder.txtLabel.setText(communityItems.getLabel());
        }else{
            viewHolder.txtLabel.setVisibility(View.GONE);
        }

        if (communityItems.getLabelColor().length() > 0){
            viewHolder.txtLabel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(communityItems.getLabelColor())));
        }

        viewHolder.txtDate.setText(communityItems.getDate().substring(2,16).replace("-","."));
        viewHolder.txtContents.setText(communityItems.getContents().trim());

        int cnt = StringUtils.countMatches(communityItems.getContents().trim(),"\n");

        if (communityItems.getLineCnt() == 0) {
            viewHolder.txtContents.post(() -> {
                communityItems.setLineCnt(viewHolder.txtContents.getLineCount());
                if (communityItems.getName().equals("마구")){
                    Log.i(TAG,"line : " + communityItems.getLineCnt());
                }
                if (communityItems.getLineCnt() > 6 && communityItems.getExpandableFlag() == 1) {
                    viewHolder.txtMore.setVisibility(View.VISIBLE);
                    if (communityItems.getImgListPath().length() > 0){
                        viewHolder.txtContents.setMaxLines(4);
                    }else{
                        viewHolder.txtContents.setMaxLines(6);
                    }
                } else {
                    viewHolder.txtMore.setVisibility(View.GONE);
                    viewHolder.txtContents.setMaxLines(Integer.MAX_VALUE);
                }
                this.notifyDataSetChanged();
            });
        }else {
            if (communityItems.getLineCnt() > 6 && communityItems.getExpandableFlag() == 1){
                viewHolder.txtMore.setVisibility(View.VISIBLE);
                if (communityItems.getName().equals("마구")){
                    Log.i(TAG,"line1 : " + communityItems.getLineCnt());
                }
                if (communityItems.getImgListPath().length() > 0){
                    viewHolder.txtContents.setMaxLines(4);
                }else{
                    viewHolder.txtContents.setMaxLines(6);
                }
            }else{
                viewHolder.txtMore.setVisibility(View.GONE);
                viewHolder.txtContents.setMaxLines(Integer.MAX_VALUE);
            }
        }

        viewHolder.txtMore.setOnClickListener(new View.OnClickListener() {
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

        if (communityItems.getcMenuNo().length() == 0){
            viewHolder.txtCmenuNo.setVisibility(View.GONE);
        }else{
            viewHolder.txtCmenuNo.setVisibility(View.VISIBLE);
            viewHolder.txtCmenuNo.setText(communityItems.getcMenuNo());
        }

        String[] hashTagList = communityItems.getHashTag().split(",");
        String hasgTag = "";

        viewHolder.linTag.removeAllViews();

        if (communityItems.getHashTag().length() == 0) {
            viewHolder.linTag.setVisibility(View.GONE);
//                        ((NewCommunityWebAdapter.ViewHolderItem) holder).txtTag.setVisibility(View.GONE);
        } else {
            viewHolder.linTag.setVisibility(View.VISIBLE);
//                        ((NewCommunityWebAdapter.ViewHolderItem) holder).txtTag.setVisibility(View.VISIBLE);
        }

        Typeface face = ResourcesCompat.getFont(context, R.font.notosanscjkkr_medium);

        if (communityItems.getHashTag().length() == 0){
            viewHolder.linTagVisible.setVisibility(View.GONE);
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

                            textView.setText("#" + hashTagList[i]);
                            textView.setTextColor(context.getColor(R.color.color343434));
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                            textView.setTypeface(face);
                            textView.setIncludeFontPadding(false);
                            viewHolder.linTagVisible.setVisibility(View.VISIBLE);
                            viewHolder.linTag.addView(textView);
                        }
                    }

//                    ((NewCommunityWebAdapter.ViewHolderItem) holder).txtTag.setText(hasgTag);

        viewHolder.txtLike.setText(""+communityItems.getLikeCnt());
        viewHolder.txtComment.setText(""+communityItems.getCommentCnt());

        if (communityItems.getPriority() == 99 || communityItems.getPriority() == 0){
            viewHolder.imgPin.setVisibility(View.GONE);
        }else{
            viewHolder.imgPin.setVisibility(View.VISIBLE);
        }

        String[] imgList = communityItems.getImgListPath().split(",");

        if (communityItems.getImgListPath().length() == 0) {
            viewHolder.frameLayout.setVisibility(View.GONE);
        } else {
            viewHolder.frameLayout.setVisibility(View.VISIBLE);
        }

        if (!imgList[0].endsWith(".mp4")) {
            adapter = new NewCommunityViewPagerAdapter(context, 0);

            for (int i = 0; i < imgList.length; i++) {
                adapter.addItem(imgList[i]);
            }
            if (imgList.length > 1){
                viewHolder.linPagerCnt.setVisibility(View.VISIBLE);
                viewHolder.txtPagerCnt.setText("1+");
            }else{
                viewHolder.linPagerCnt.setVisibility(View.GONE);
            }
        } else {
            adapter = new NewCommunityViewPagerAdapter(context, 1);
            viewHolder.linPagerCnt.setVisibility(View.GONE);
            for (int i = 0; i < imgList.length; i++) {
                adapter.addItem(imgList[i]);
            }
            viewHolder.viewPager.setPagingDisabled();
        }
        viewHolder.viewPager.setAdapter(adapter);

        if (communityItems.getLikeFlag() == 1){
            viewHolder.imgLike.setImageResource(R.drawable.ic_like_on);
            viewHolder.txtLikeIcon.setTextColor(context.getColor(R.color.color09D192));
            viewHolder.txtLike.setTextColor(context.getColor(R.color.color09D192));
        }else{
            viewHolder.imgLike.setImageResource(R.drawable.ic_like_off);
            viewHolder.txtLikeIcon.setTextColor(context.getColor(R.color.color626262));
            viewHolder.txtLike.setTextColor(context.getColor(R.color.color626262));
        }

        if (Utils.VERSION.equals("1.5.7")){
            if (communityItems.getScrapFlag() == 1){
                viewHolder.imgCopy.setImageResource(R.drawable.ic_copy_on);
                viewHolder.txtCopyIcon.setTextColor(context.getColor(R.color.color09D192));
            }else{
                viewHolder.imgCopy.setImageResource(R.drawable.ic_copy_off);
                viewHolder.txtCopyIcon.setTextColor(context.getColor(R.color.color626262));
            }
        }else{

        }

        viewHolder.txtComment.setOnClickListener(new View.OnClickListener() {
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
                i.putExtra("comment1",true);
                if (fragment == null){
                    ((Activity)context).startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                }else{
                    fragment.startActivityForResult(i, COMMUNITY_DETAIL_MOVE);
                }
            }
        });

        viewHolder.linLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (communityItems.getLikeFlag() == 1){
                    new NewCommunityChildWebFragment.DeleteCommunityLikeNetWork().execute(communityItems.getNo(), "1");
                    viewHolder.imgLike.setImageResource(R.drawable.ic_like_off);
                    viewHolder.txtLikeIcon.setTextColor(context.getColor(R.color.color626262));
                    communityItems.setLikeCnt(communityItems.getLikeCnt() - 1);
                    communityItems.setLikeFlag(0);
                    viewHolder.txtLike.setText(""+communityItems.getLikeCnt());
                    viewHolder.txtLike.setTextColor(context.getColor(R.color.color626262));
                }else{
                    new NewCommunityChildWebFragment.InsertCommunityLikeNetWork().execute(communityItems.getNo(), "1");
                    viewHolder.imgLike.setImageResource(R.drawable.ic_like_on);
                    viewHolder.txtLikeIcon.setTextColor(context.getColor(R.color.color09D192));
                    communityItems.setLikeCnt(communityItems.getLikeCnt() + 1);
                    communityItems.setLikeFlag(1);
                    viewHolder.txtLike.setText(""+communityItems.getLikeCnt());
                    viewHolder.txtLike.setTextColor(context.getColor(R.color.color09D192));
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.txtContents.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.linCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.VERSION.equals("1.5.7")){
                    if (communityItems.getScrapFlag() == 1){
                        new NewCommunityChildWebFragment.DeleteCommunityLikeNetWork().execute(communityItems.getNo(), "2");
                        viewHolder.imgCopy.setImageResource(R.drawable.ic_copy_off);
                        viewHolder.txtCopyIcon.setTextColor(context.getColor(R.color.color626262));
                        communityItems.setScrapFlag(0);
                    }else{
                        new NewCommunityChildWebFragment.InsertCommunityLikeNetWork().execute(communityItems.getNo(), "2");
                        viewHolder.imgCopy.setImageResource(R.drawable.ic_copy_on);
                        viewHolder.txtCopyIcon.setTextColor(context.getColor(R.color.color09D192));
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

        viewHolder.linComment.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(@NonNull View itemView) {
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
        }
    }

    public void addItem(CommunityItems communityItems){
        itemArrayList.add(communityItems);
    }

}
