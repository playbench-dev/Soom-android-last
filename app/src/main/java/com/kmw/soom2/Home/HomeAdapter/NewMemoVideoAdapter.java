package com.kmw.soom2.Home.HomeAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.PopupDialog.ShowVideoDialog;
import com.kmw.soom2.Communitys.Activitys.VideoActivity;
import com.kmw.soom2.Communitys.Items.GalleryItem;
import com.kmw.soom2.Home.HomeItem.PictureItem;
import com.kmw.soom2.Home.HomeItem.VideoMenuItem;
import com.kmw.soom2.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class NewMemoVideoAdapter extends RecyclerView.Adapter<NewMemoVideoAdapter.ViewHolder> {
    Context context;
    private Vector<VideoMenuItem> menus = new Vector<>();
    private ArrayList<VideoMenuItem> menus1 = new ArrayList<>();
    int height;

    public NewMemoVideoAdapter(Context context, int height) {
        this.context = context;
        this.height = height;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgThumbnail;
        public LinearLayout linNum;
        public TextView txtNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imgThumbnail = (ImageView)itemView.findViewById(R.id.img_video_thumbnail);
            this.linNum = (LinearLayout)itemView.findViewById(R.id.lin_video_num);
            this.txtNum = (TextView)itemView.findViewById(R.id.txt_video_num);
        }
    }

    @NonNull
    @Override
    public NewMemoVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_menu, parent, false);

        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);

        NewMemoVideoAdapter.ViewHolder vh = new NewMemoVideoAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewMemoVideoAdapter.ViewHolder holder, int position) {
        VideoMenuItem menu  = menus1.get(position);

        String title = "";
        Bitmap img = null;
        Uri uri = null;

        if (menu != null){
            title = menu.getTitle();
            img = menu.getImg();
            uri = menu.getUri();
        }

        Display display= ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        if (img != null){
            Glide.with(context).load(menus1.get(position).getImg()).apply(new RequestOptions().override(display.getWidth()/3,display.getWidth()/3)).into(((NewMemoVideoAdapter.ViewHolder) holder).imgThumbnail);
        }

        //다이얼로그로 동영상의 Uri를 보내며 다이얼로그를 띄우는코드.

        ((NewMemoVideoAdapter.ViewHolder) holder).imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowVideoDialog dialog = new ShowVideoDialog(context, menus1.get(position).getUri());
                dialog.show();
            }
        });

        if (VideoActivity.videoPathList.size() > 0){
            if (VideoActivity.videoPathList.contains(menus1.get(position).getUri())){
                ((NewMemoVideoAdapter.ViewHolder) holder).txtNum.setBackgroundResource(R.drawable.radius_50dp);
                ((NewMemoVideoAdapter.ViewHolder) holder).txtNum.setBackgroundTintList(context.getColorStateList(R.color.colorPrimary));
            }else{
                ((NewMemoVideoAdapter.ViewHolder) holder).txtNum.setBackgroundResource(0);
            }
        }

        ((NewMemoVideoAdapter.ViewHolder) holder).linNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VideoActivity.videoPathList.contains(menus1.get(position).getUri())){
                    ((NewMemoVideoAdapter.ViewHolder) holder).txtNum.setBackgroundResource(0);
                    VideoActivity.videoPathList.remove(menus1.get(position).getUri());
                    VideoActivity.bitmapThumbnail = null;
                    notifyDataSetChanged();
                }else{
                    VideoActivity.videoPathList = new ArrayList<>();
                    ((NewMemoVideoAdapter.ViewHolder) holder).txtNum.setBackgroundResource(R.drawable.radius_50dp);
                    ((NewMemoVideoAdapter.ViewHolder) holder).txtNum.setBackgroundTintList(context.getColorStateList(R.color.colorPrimary));
                    VideoActivity.videoPathList.add(menus1.get(position).getUri());
                    VideoActivity.bitmapThumbnail = menus1.get(position).getImg();
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus1.size();
    }

    public void setUp(VideoMenuItem videoMenuItem) {
        this.menus1.add(videoMenuItem);
        Collections.sort(this.menus1, compare);
        notifyDataSetChanged();
    }

    Comparator<VideoMenuItem> compare = new Comparator<VideoMenuItem>() {
        @Override
        public int compare(VideoMenuItem test, VideoMenuItem t1) {
            int position = 0;
            try {
                if (test == null) {
                    position = 0;
                } else if (t1 == null) {
                    position = 0;
                } else if (test.getId() > t1.getId()) {
                    position = -1;
                } else if (test.getId() == t1.getId()) {
                    position = 0;
                } else if (test.getId() < t1.getId()) {
                    position = 1;
                }
            } catch (Exception e) {

            }
            return position;
        }
    };
}

