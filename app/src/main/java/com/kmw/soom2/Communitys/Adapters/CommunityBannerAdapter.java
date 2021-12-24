package com.kmw.soom2.Communitys.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Home.Activitys.UrlWebViewActivity;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CommunityBannerAdapter extends PagerAdapter {
    ArrayList<BannerItem> bannerItemArrayList = new ArrayList<>();
    Context context;
    private int pos = 0;
    private String callNumber = "";
    boolean guest = false;


    public CommunityBannerAdapter(Context context, ArrayList<BannerItem> bannerItemArrayList) {
        this.bannerItemArrayList = bannerItemArrayList;
        this.context = context;
    }

    public CommunityBannerAdapter(Context context, ArrayList<BannerItem> bannerItemArrayList, boolean guest) {
        this.bannerItemArrayList = bannerItemArrayList;
        this.context = context;
        this.guest = guest;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_pager_banner_list_item, null);
        pos = position % bannerItemArrayList.size();
        ImageView imageView = (ImageView) view.findViewById(R.id.view_pager_image);
        (container).addView(view);

//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions = requestOptions.transforms(new FitCenter(), new RoundedCorners(6));

        String replaceText = bannerItemArrayList.get(pos).getImageFile();
        if (replaceText.contains("soom2.testserver-1.com:8080")){
            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
        }else if (replaceText.contains("103.55.190.193")){
            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
        }
        Glide.with(context).load("https:" + replaceText)
                .into(imageView);

        if (pos >= bannerItemArrayList.size() - 1) {
            pos = 0;
        } else {
            ++pos;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerType().equals("1")){
                    if(bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerLink() != null) {
//                        Intent i = new Intent(context, UrlWebViewActivity.class);
//                        if (guest){
//                            Intent i = new Intent(context, exNewAnotherActivity.class);
//                            context.startActivity(i);
//                        }else{
//
//                        }
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerLink()));
                        i.putExtra("url",bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerLink());
                        context.startActivity(i);
                    }else {

                    }
                }else if (bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerType().equals("3")){
                    Intent i = new Intent(context, UrlWebViewActivity.class);
                    i.putExtra("url",bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerLink());
                    i.putExtra("title",bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerTitle());
                    if (guest){
                        i.putExtra("guest",true);
                        i.putExtra("around",true);
                    }
                    context.startActivity(i);
                }else if (bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerType().equals("2")){
                    //전화하기
//                    callNumber = bannerItemArrayList.get(position % bannerItemArrayList.size()).getBannerLink();
//                    TedPermission.with(context)
//                            .setPermissionListener(permissionListener)
//                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                            .setPermissions(Manifest.permission.CALL_PHONE)
//                            .check();
                }
            }
        });

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub

        //ViewPager에서 보이지 않는 View는 제거
        //세번째 파라미터가 View 객체 이지만 데이터 타입이 Object여서 형변환 실시
        container.removeView((View) object);
    }
}
