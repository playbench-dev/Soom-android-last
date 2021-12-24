package com.kmw.soom2.Communitys.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Communitys.Activitys.ViewImgDetailActivity;
import com.kmw.soom2.Communitys.Items.CustomViewPager;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class NewCommunityViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> listViewItems = new ArrayList<>();
    CommunityImgDetailViewPagerAdapter adapter;
    Display display;
    WindowManager wm;
    int flag = -1;
    public static BottomSheetDialog dialog;
//    YourGlideModule yourGlideModule;

    public NewCommunityViewPagerAdapter(Context context, int flag) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        this.flag = flag;
//        yourGlideModule = new YourGlideModule(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listViewItems.size();
    }

    @Override
    public void startUpdate(View view) {

    }

    @Override
    public Object instantiateItem(View view, int i) {
        return null;
    }

    @Override
    public void destroyItem(View view, int i, Object o) {

    }

    @Override
    public void finishUpdate(View view) {

    }

    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        View view = null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.new_view_community_view_pager_list_item, null);
        ImageView img = (ImageView) view.findViewById(R.id.img_community_view_pager_list_item_main);

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        if (listViewItems.get(position).length() > 0) {
//            GlideApp.with();
            if (flag == 1) {
                String replaceText = listViewItems.get(1);
                if (replaceText.contains("soom2.testserver-1.com:8080")){
                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                }else if (replaceText.contains("103.55.190.193")){
                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                }
                if (listViewItems.get(position).contains("https:")) {
                    Glide.with(context).load(replaceText).into(img);
                } else {
                    Glide.with(context).load("https:" + replaceText).into(img);
                }
            } else {
                String replaceText = listViewItems.get(position);
                if (replaceText.contains("soom2.testserver-1.com:8080")){
                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                }else if (replaceText.contains("103.55.190.193")){
                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                }
                if (listViewItems.get(position).contains("https:")) {
                    Glide.with(context).load(replaceText).into(img);
                } else {
                    Glide.with(context).load("https:" + replaceText).into(img);
                }
            }
        }

        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new BottomSheetDialog(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentView = inflater.inflate(R.layout.view_img_detail_viewpager, null);
                TextView txtClose = (TextView) contentView.findViewById(R.id.txt_img_detail);
//                LinearLayout videoBottom = (LinearLayout) contentView.findViewById(R.id.video_bottom);
//                ImageView videoRotation = (ImageView) contentView.findViewById(R.id.video_rotation);
                CustomViewPager viewPagerDetail = (CustomViewPager) contentView.findViewById(R.id.view_pager_img_detail);
                if (flag != 1) {
                    adapter = new CommunityImgDetailViewPagerAdapter(context, listViewItems, flag);

                    DisplayMetrics dm = context.getResources().getDisplayMetrics();
                    int height = dm.heightPixels;

                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
                    dialog.addContentView(contentView, params);

                    viewPagerDetail.setAdapter(adapter);
                    viewPagerDetail.setCurrentItem(position);

                    dialog.show();
                } else {
                    Intent i = new Intent(context, ViewImgDetailActivity.class);
                    i.putExtra("position",position);
                    i.putExtra("videoArray",listViewItems);
                    context.startActivity(i);
                }

                txtClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (flag == 1) {
                            adapter.videoStop();
                        }
                    }
                });
            }
        });

        return view;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {

    }

    public void addItem(String imagePath) {
        listViewItems.add(imagePath);
    }

}
