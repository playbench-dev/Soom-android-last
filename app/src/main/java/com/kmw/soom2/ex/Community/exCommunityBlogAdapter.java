package com.kmw.soom2.ex.Community;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.HEADER_SPACE;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.HEADER_TYPE;
import static com.kmw.soom2.Communitys.Items.CommunityRecyclerViewItem.ITEM_TYPE;

public class exCommunityBlogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "NewCommunityAdapter";

    public static ArrayList<CommunityRecyclerViewItem> itemArrayList;
    private Context context;
    private Fragment fragment;

    public exCommunityBlogAdapter(Context context ,Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_community_blog_header, parent, false);
                return new exCommunityBlogAdapter.ViewHolderHeader(view);
            }
            case ITEM_TYPE : {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_community_blog_item, parent, false);
                return new exCommunityBlogAdapter.ViewHolderItem(view);
            }
            case HEADER_SPACE : {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_new_community_blog_header_space, parent, false);
                return new exCommunityBlogAdapter.ViewHolderHeaderSpace(view);
            }
        }
        return null;
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        private LinearLayout linHeader;
        private TextView txtTitle;
        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            linHeader = itemView.findViewById(R.id.lin_new_community_blog_header);
            txtTitle = itemView.findViewById(R.id.txt_new_community_blog_header_title);
            itemView.setTag(false);
        }
    }

    private class ViewHolderHeaderSpace extends RecyclerView.ViewHolder {
        public ViewHolderHeaderSpace(@NonNull View itemView) {
            super(itemView);
            itemView.setTag(false);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtTitle;
        private TextView txtExPlanation;
        private TextView txtViews;
        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_new_community_blog_show_image);
            txtTitle = itemView.findViewById(R.id.txt_new_community_blog_title);
            txtExPlanation = itemView.findViewById(R.id.txt_new_community_blog_explanation);
            txtViews = itemView.findViewById(R.id.txt_blog_list_item_views);
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

                    break;
                }
                case HEADER_SPACE : {

                    break;
                }
                case ITEM_TYPE : {
                    CommunityItems communityItems = itemArrayList.get(position).getCommunityItems();
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(8));

                    if (communityItems.getImgListPath().length() > 0) {
                        String[] imgList = communityItems.getImgListPath().split(",");
                        String replaceText = imgList[0];
                        if (replaceText.contains("soom2.testserver-1.com:8080")){
                            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                        }else if (replaceText.contains("103.55.190.193")){
                            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                        }
                        if (communityItems.getProfile().contains("https:")) {
                            Glide.with(context).load(replaceText).apply(requestOptions).into(((exCommunityBlogAdapter.ViewHolderItem) holder).imageView);
                        } else {
                            Glide.with(context).load("https:" + replaceText).apply(requestOptions).into(((exCommunityBlogAdapter.ViewHolderItem) holder).imageView);
                        }
                    }else{
                        Glide.with(context).load(R.drawable.symptom_cause_gradient).into(((exCommunityBlogAdapter.ViewHolderItem) holder).imageView);
                    }


                    if (communityItems.getCommunityTitle().length() != 0){
                        ((exCommunityBlogAdapter.ViewHolderItem) holder).txtTitle.setText(communityItems.getCommunityTitle());
                    }else{
                        ((exCommunityBlogAdapter.ViewHolderItem) holder).txtTitle.setText(communityItems.getName());
                    }

                    if (communityItems.getCommunityExPlanation().length() != 0){
                        ((exCommunityBlogAdapter.ViewHolderItem) holder).txtExPlanation.setText(communityItems.getCommunityExPlanation());
                    }else{
                        ((exCommunityBlogAdapter.ViewHolderItem) holder).txtExPlanation.setText(communityItems.getContents());
                    }
                    ((exCommunityBlogAdapter.ViewHolderItem) holder).txtViews.setText(communityItems.getmViewCnt());

                    ((exCommunityBlogAdapter.ViewHolderItem) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, NewCommunityDetailActivity.class);
                            i.putExtra("communityNo", communityItems.getNo());
                            i.putExtra("userNo","0");
                            i.putExtra("contents",communityItems.getContents().trim());
                            i.putExtra("hashTag",communityItems.getHashTag());
                            i.putExtra("imgsPath",communityItems.getImgListPath());
                            i.putExtra("position", position);
                            i.putExtra("blog",true);
                            i.putExtra("guest",true);
                            context.startActivity(i);
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
        this.itemArrayList = list;
    }

}
