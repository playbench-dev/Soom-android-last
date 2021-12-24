package com.kmw.soom2.Communitys.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Communitys.Adapters.CommunityImgDetailViewPagerAdapter;
import com.kmw.soom2.Communitys.Items.CustomViewPager;
import com.kmw.soom2.R;
import com.kmw.soom2.Views.CustomExoPlayerView;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.NullCheck;

public class ViewImgDetailActivity extends AppCompatActivity {
    CommunityImgDetailViewPagerAdapter adapter;
    Intent beforeIntent;
    TextView txtClose;
    ArrayList<String> listViewItems;
    final static String TAG = "VIEW_IMG_DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_img_detail);

        NullCheck(this);

        txtClose = (TextView) findViewById(R.id.txt_img_detail_activity);
        beforeIntent = getIntent();
        listViewItems = (ArrayList<String>) beforeIntent.getSerializableExtra("videoArray");
        CustomViewPager viewPagerDetail = (CustomViewPager) findViewById(R.id.view_pager_img_detail);

        adapter = new CommunityImgDetailViewPagerAdapter(ViewImgDetailActivity.this, listViewItems, 1);

        viewPagerDetail.setAdapter(adapter);
        if (beforeIntent.hasExtra("position")){
            Log.i(TAG,"position : " + beforeIntent.getIntExtra("position",0));
            viewPagerDetail.setCurrentItem(beforeIntent.getIntExtra("position",0));
        }


        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new CustomExoPlayerView(ViewImgDetailActivity.this).playing()) {
                    new CustomExoPlayerView(ViewImgDetailActivity.this).releasePlayer();
                }
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (new CustomExoPlayerView(ViewImgDetailActivity.this).playing()) {
            new CustomExoPlayerView(ViewImgDetailActivity.this).pause();
        }
        super.onBackPressed();
    }


    @Override
    public void onPause() {
        super.onPause();
        if (new CustomExoPlayerView(ViewImgDetailActivity.this).playing()) {
            new CustomExoPlayerView(ViewImgDetailActivity.this).pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (new CustomExoPlayerView(ViewImgDetailActivity.this).playing()) {
            new CustomExoPlayerView(ViewImgDetailActivity.this).pause();
        }
    }
}
