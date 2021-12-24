package com.kmw.soom2.Communitys.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.kmw.soom2.Views.CustomExoPlayerView;
import com.kmw.soom2.Communitys.Items.GalleryItem;
import com.kmw.soom2.R;
import com.kmw.soom2.Views.ImagePinch;

import java.util.ArrayList;
import java.util.List;


public class CommunityImgDetailViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> listViewItems = new ArrayList<>();
    List<GalleryItem> galleryItems;
    int flag = 0;
    int videoFlag = -1;
    public static CustomExoPlayerView pv;
    String videoUrl;

    public CommunityImgDetailViewPagerAdapter(Context context, ArrayList<String> listViewItems, int videoFlag) {
        this.context = context;
        this.listViewItems.addAll(listViewItems);
        this.videoFlag = videoFlag;
        if(videoFlag == 1) {
            this.videoUrl = listViewItems.get(0);
        }
    }

    public CommunityImgDetailViewPagerAdapter(Context context, List<GalleryItem> galleryItems, int flag) {
        this.context = context;
        this.galleryItems = galleryItems;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (flag == 1) {
            return galleryItems.size();
        } else {
            if(videoFlag == 1){
                return 1;
            }else {
                return listViewItems.size();
            }
        }
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
        view = inflater.inflate(R.layout.view_community_image_detail, null);
        ImagePinch imgParent = (ImagePinch) view.findViewById(R.id.img_view_pager_community_detail_parent);
        ImageView img = (ImageView) view.findViewById(R.id.img_view_pager_community_detail);
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (flag == 1) {
            Glide.with(context).asBitmap().load(getImageInfo(galleryItems.get(position).getIdTest())).into(img);
        } else if (listViewItems.get(position).contains("https:")) {
            if (videoFlag == 1) {
                img.setVisibility(View.GONE);
                pv = view.findViewById(R.id.video_view_pager_community_detail);
                pv.setVisibility(View.VISIBLE);
                pv.initializePlayer(videoUrl);
                Log.i("CommunityImgDetailView","videoUrl : " + videoUrl);
            } else {
                String replaceText = listViewItems.get(position);
                if (replaceText.contains("soom2.testserver-1.com:8080")){
                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                }else if (replaceText.contains("103.55.190.193")){
                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                }
                Glide.with(context).asBitmap().load(replaceText).into(img);
            }
        } else {
            if (videoFlag == 1) {
                img.setVisibility(View.GONE);

                pv = view.findViewById(R.id.video_view_pager_community_detail);
                pv.setVisibility(View.VISIBLE);
                pv.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);

                pv.initializePlayer(videoUrl);
                Log.i("CommunityImgDetailView","videoUrl : " + videoUrl);
            } else {
                String replaceText = listViewItems.get(position);
                if (replaceText.contains("soom2.testserver-1.com:8080")){
                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                }else if (replaceText.contains("103.55.190.193")){
                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                }
                Glide.with(context).asBitmap().load("https:" + replaceText).into(img);
            }
        }

        container.addView(view);
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

    public void videoStop() {
        pv.releasePlayer();
    }

    private String getImageInfo(String thumbID) {
        String imageDataPath = null;
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor imageCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, "_ID='" + thumbID + "'", null, null);

        if (imageCursor != null && imageCursor.moveToFirst()) {
            if (imageCursor.getCount() > 0) {
                int imgData = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);

                imageDataPath = imageCursor.getString(imgData);
            }
        }
        imageCursor.close();
        return imageDataPath;
    }

}
