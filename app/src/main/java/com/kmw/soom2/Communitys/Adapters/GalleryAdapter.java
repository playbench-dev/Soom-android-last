package com.kmw.soom2.Communitys.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Communitys.Activitys.GalleryActivity;
import com.kmw.soom2.Communitys.Items.GalleryItem;
import com.kmw.soom2.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.kmw.soom2.Common.Utils.FILE_PATH;
import static com.kmw.soom2.Common.Utils.PICK_URI;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.PhotoViewHolder> {

    private String TAG = "GalleryAdapter";
    CommunityImgDetailViewPagerAdapter adapter;

    Context mContext;
    List<GalleryItem> itemArrayLists = new ArrayList<>();
    int layout;

    public GalleryAdapter(Context mContext, List<GalleryItem> itemArrayLists, int layout) {
        this.mContext = mContext;
        this.itemArrayLists = itemArrayLists;
        this.layout = layout;
    }


    @Override
    public long getItemId(int position) {

        return super.getItemId(position);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.PhotoViewHolder holder, int position) {
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (position == 0) {
            holder.imageView.setBackgroundColor(mContext.getResources().getColor(R.color.f5f5f5));
            Glide.with(mContext)
                    .load(R.drawable.ic_community_camera)
                    .centerCrop().override(display.getWidth() / 3, display.getWidth() / 3)
                    .into(holder.imageView);
            holder.linCheck.setVisibility(View.GONE);
        } else {
            holder.linCheck.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(itemArrayLists.get(position).getDataTest())
                    .centerCrop()
                    .override(display.getWidth() / 3, display.getWidth() / 3)
                    .into(holder.imageView);
        }
        if (GalleryActivity.galleryImagePathList.size() > 0){
            if (GalleryActivity.galleryImagePathList.contains(getImageInfo(itemArrayLists.get(position).getIdTest()))){
                holder.txtCheck.setBackgroundResource(R.drawable.radius_50dp);
                holder.txtCheck.setBackgroundTintList(mContext.getColorStateList(R.color.colorPrimary));
                holder.txtCheck.setText(""+(GalleryActivity.galleryImagePathList.indexOf(getImageInfo(itemArrayLists.get(position).getIdTest()))+1));
            }else {
                holder.txtCheck.setBackgroundResource(0);
                holder.txtCheck.setText("");
            }
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0){

                    File dir = null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Soom");
                    }else{
                        dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Soom");
                    }

                    if(!dir.exists()) {
                        dir.mkdir();
                    }

                    File filePath = null;
                    try {
                        filePath = File.createTempFile("IMG", ".jpg", dir);
                        if(!filePath.exists()) {
                            filePath.createNewFile();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    FILE_PATH = String.valueOf(filePath);
                    Uri providerURI = FileProvider.getUriForFile( mContext ,mContext.getPackageName()+".fileprovider" , filePath);
                    PICK_URI = providerURI;
                    intent.putExtra(MediaStore.EXTRA_OUTPUT , providerURI);

                    filePath.deleteOnExit();
                    ((GalleryActivity)mContext).startActivityForResult(intent,2222);
                }else {
//                    adapter = new CommunityImgDetailViewPagerAdapter(mContext, itemArrayLists, 1);
//                    final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
//                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    View contentView = inflater.inflate(R.layout.view_img_detail_viewpager, null);
//
//                    ViewPager viewPagerDetail = (ViewPager) contentView.findViewById(R.id.view_pager_img_detail);
//                    TextView txtClose = (TextView) contentView.findViewById(R.id.txt_img_detail);
//
//                    DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//                    int height = dm.heightPixels;
//
//                    WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
//                    dialog.addContentView(contentView, params);
//
//                    viewPagerDetail.setAdapter(adapter);
//                    viewPagerDetail.setCurrentItem(position);
//
//                    viewPagerDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//                        float tempPositionOffset = 0;
//                        @Override
//                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                            if ((position) == 0) {
//                                if (tempPositionOffset < positionOffset) {
//                                    viewPagerDetail.setCurrentItem(1);
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onPageSelected(int position) {
//                            viewPagerDetail.setCurrentItem(position);
//                        }
//
//                        @Override
//                        public void onPageScrollStateChanged(int state) {
//
//                        }
//                    });
//                    dialog.show();
//
//                    txtClose.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                        }
//                    });
                    if (GalleryActivity.galleryImagePathList.contains(getImageInfo(itemArrayLists.get(position).getIdTest()))){
                        holder.txtCheck.setBackgroundResource(0);
                        GalleryActivity.galleryImagePathList.remove(getImageInfo(itemArrayLists.get(position).getIdTest()));
                        holder.txtCheck.setText("");
                        notifyDataSetChanged();
                    }else{
                        if (GalleryActivity.galleryImagePathList.size() < 5){
                            holder.txtCheck.setBackgroundResource(R.drawable.radius_50dp);
                            holder.txtCheck.setBackgroundTintList(mContext.getColorStateList(R.color.colorPrimary));
                            GalleryActivity.galleryImagePathList.add(getImageInfo(itemArrayLists.get(position).getIdTest()));
                            holder.txtCheck.setText(""+ GalleryActivity.galleryImagePathList.size());
                        }else{
                            Toast.makeText(mContext, "사진은 첨부는 5장까지 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        holder.txtCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GalleryActivity.galleryImagePathList.contains(getImageInfo(itemArrayLists.get(position).getIdTest()))){
                    holder.txtCheck.setBackgroundResource(0);
                    GalleryActivity.galleryImagePathList.remove(getImageInfo(itemArrayLists.get(position).getIdTest()));
                    holder.txtCheck.setText("");
                    notifyDataSetChanged();
                }else{
                    if (GalleryActivity.galleryImagePathList.size() < 5){
                        holder.txtCheck.setBackgroundResource(R.drawable.radius_50dp);
                        holder.txtCheck.setBackgroundTintList(mContext.getColorStateList(R.color.colorPrimary));
                        GalleryActivity.galleryImagePathList.add(getImageInfo(itemArrayLists.get(position).getIdTest()));
                        holder.txtCheck.setText(""+ GalleryActivity.galleryImagePathList.size());
                    }else{
                        Toast.makeText(mContext, "사진은 첨부는 5장까지 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return itemArrayLists.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView txtCheck;
        public LinearLayout linCheck;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_gallery_list_item);
            txtCheck = (TextView) itemView.findViewById(R.id.txt_gallery_list_item_num);
            linCheck = (LinearLayout) itemView.findViewById(R.id.lin_gallery_list_item_num);
        }
    }

    private String getImageInfo(String thumbID) {
        String imageDataPath = null;
        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor imageCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, "_ID='" + thumbID + "'", null, null);

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