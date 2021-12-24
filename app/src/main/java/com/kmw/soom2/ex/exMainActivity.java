package com.kmw.soom2.ex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.R;
import com.kmw.soom2.ex.Community.exCommunityFragment;
import com.kmw.soom2.ex.Drug.exDrugFragment;
import com.kmw.soom2.ex.Home.exHomeFragment;
import com.kmw.soom2.ex.MyPage.exMyPageFragment;
import com.kmw.soom2.ex.Static.exStaticFragment;

public class exMainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MainActivity";

    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    Fragment homeFragment,drugFragment,communityFragmnet,staticFragment,myPageFragment;
    LinearLayout linTabHome,linTabDrug,linTabCommuninty,linTabStatics,linTabMyPage;
    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_main);

        beforeIntent = getIntent();

        frameLayout = (FrameLayout)findViewById(R.id.frame_layout);
        linTabHome = (LinearLayout)findViewById(R.id.lin_home_tab_home);
        linTabDrug = (LinearLayout)findViewById(R.id.lin_home_tab_drug);
        linTabCommuninty = (LinearLayout)findViewById(R.id.lin_home_tab_community);
        linTabStatics = (LinearLayout)findViewById(R.id.lin_home_tab_statics);
        linTabMyPage = (LinearLayout)findViewById(R.id.lin_home_tab_my_page);

        linTabHome.setOnClickListener(this);
        linTabDrug.setOnClickListener(this);
        linTabCommuninty.setOnClickListener(this);
        linTabStatics.setOnClickListener(this);
        linTabMyPage.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();

        linTabHome.performClick();

        if (beforeIntent.getData() != null){
            Uri uri = getIntent().getData();
            Uri uri1 = Uri.parse(uri.getQueryParameter("url"));
            Log.i(TAG,"scheme data : " + getIntent().getData());
            Log.i(TAG,"scheme host : " + uri.getHost());
            Log.i(TAG,"scheme path : " + uri.getPath());
            Log.i(TAG,"scheme path : " + uri1.getPath());
            Log.i(TAG,"scheme param : " + uri1.getQueryParameter("no"));
            if (uri.getHost().equals("home")){
                tintSelect(linTabHome,linTabDrug,linTabCommuninty,linTabStatics,linTabMyPage);
                if(homeFragment == null) {
                    homeFragment = new exHomeFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, homeFragment).commit();
                }
                if (homeFragment != null) fragmentManager.beginTransaction().show(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            } else if (uri.getHost().equals("manage")){
                tintSelect(linTabDrug,linTabHome,linTabCommuninty,linTabStatics,linTabMyPage);

                if(drugFragment == null) {
                    drugFragment = new exDrugFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, drugFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().show(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            }else if (uri.getHost().equals("community")){               //memo 커뮤니티 탭 이동
                tintSelect(linTabCommuninty,linTabHome,linTabDrug,linTabStatics,linTabMyPage);
                if(communityFragmnet == null) {
                    if (uri.getPath().equals("/all/")){
                        communityFragmnet = new exCommunityFragment().newInstance("0");
                    }else if (uri.getPath().equals("/contents/")){
                        communityFragmnet = new exCommunityFragment().newInstance("1");
                    }else if (uri.getPath().equals("/notice/")){
                        communityFragmnet = new exCommunityFragment().newInstance("2");
                    }else if (uri.getPath().equals("/write/")){
                        communityFragmnet = new exCommunityFragment().newInstance("write");
                    }else if (uri.getPath().equals("/search/")){
                        communityFragmnet = new exCommunityFragment().newInstance("search");
                    }
                    fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragmnet).commit();
                }
                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().show(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            }else if (uri.getHost().equals("community_detail")){        //memo 게시글 이동
                tintSelect(linTabCommuninty,linTabHome,linTabDrug,linTabStatics,linTabMyPage);

                if(communityFragmnet == null) {
                    communityFragmnet = new exCommunityFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragmnet).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().show(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            }else if (uri.getHost().equals("report")){
                tintSelect(linTabStatics,linTabHome,linTabDrug,linTabCommuninty,linTabMyPage);

                if(staticFragment == null) {
                    staticFragment = new exStaticFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, staticFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().show(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

            }else if (uri.getHost().equals("mypage")){
                tintSelect(linTabMyPage,linTabHome,linTabDrug,linTabCommuninty,linTabStatics);

                if(myPageFragment == null) {
                    myPageFragment = new exMyPageFragment().newInstance(uri1.getPath().replaceAll("/",""));
                    fragmentManager.beginTransaction().add(R.id.frame_layout, myPageFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().show(myPageFragment).commit();
            }
            getIntent().setData(null);
        }else{
            linTabHome.performClick();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"destroy");
    }

//    public void logSentFriendRequestEvent () {
//        logger.logEvent("sentFriendRequest");
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void tintSelect(LinearLayout lin1,LinearLayout lin2,LinearLayout lin3,LinearLayout lin4,LinearLayout lin5){
        lin1.setClickable(false);
        lin2.setClickable(true);
        lin3.setClickable(true);
        lin4.setClickable(true);
        lin5.setClickable(true);
        for (int i = 0; i < lin1.getChildCount(); i++){
            if (lin1.getChildAt(i) instanceof TextView){
                TextView textView1 = (TextView)lin1.getChildAt(i);
                textView1.setTextColor(getResources().getColor(R.color.black));

                TextView textView2 = (TextView)lin2.getChildAt(i);
                textView2.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                TextView textView3 = (TextView)lin3.getChildAt(i);
                textView3.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                TextView textView4 = (TextView)lin4.getChildAt(i);
                textView4.setTextColor(getResources().getColor(R.color.colorb1b1b1));

                TextView textView5 = (TextView)lin5.getChildAt(i);
                textView5.setTextColor(getResources().getColor(R.color.colorb1b1b1));

            }else if (lin1.getChildAt(i) instanceof ImageView){
                ImageView imageView1 = (ImageView) lin1.getChildAt(i);
                imageView1.setColorFilter(getResources().getColor(R.color.black));

                ImageView imageView2 = (ImageView) lin2.getChildAt(i);
                imageView2.setColorFilter(getResources().getColor(R.color.colorb1b1b1));

                ImageView imageView3 = (ImageView) lin3.getChildAt(i);
                imageView3.setColorFilter(getResources().getColor(R.color.colorb1b1b1));

                ImageView imageView4 = (ImageView) lin4.getChildAt(i);
                imageView4.setColorFilter(getResources().getColor(R.color.colorb1b1b1));

                ImageView imageView5 = (ImageView) lin5.getChildAt(i);
                imageView5.setColorFilter(getResources().getColor(R.color.colorb1b1b1));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lin_home_tab_home :

                tintSelect(linTabHome,linTabDrug,linTabCommuninty,linTabStatics,linTabMyPage);

                if(homeFragment == null) {
                    homeFragment = new exHomeFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, homeFragment).commit();
                }

                if (homeFragment != null) fragmentManager.beginTransaction().show(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();
                break;
            case R.id.lin_home_tab_drug :

                tintSelect(linTabDrug,linTabHome,linTabCommuninty,linTabStatics,linTabMyPage);

                if(drugFragment == null) {
                    drugFragment = new exDrugFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, drugFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().show(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

                break;
            case R.id.lin_home_tab_community :

                tintSelect(linTabCommuninty,linTabHome,linTabDrug,linTabStatics,linTabMyPage);

                if(communityFragmnet == null) {
//                    communityFragmnet = new CommunityFragment();
                    communityFragmnet = new exCommunityFragment().newInstance("");;
                    fragmentManager.beginTransaction().add(R.id.frame_layout, communityFragmnet).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().show(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

                break;
            case R.id.lin_home_tab_statics :

                tintSelect(linTabStatics,linTabHome,linTabDrug,linTabCommuninty,linTabMyPage);

                if(staticFragment == null) {
                    staticFragment = new exStaticFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, staticFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().show(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().hide(myPageFragment).commit();

                break;
            case R.id.lin_home_tab_my_page :

                tintSelect(linTabMyPage,linTabHome,linTabDrug,linTabCommuninty,linTabStatics);

                if(myPageFragment == null) {
                    myPageFragment = new exMyPageFragment().newInstance("");
                    fragmentManager.beginTransaction().add(R.id.frame_layout, myPageFragment).commit();
                }

                if(homeFragment != null) fragmentManager.beginTransaction().hide(homeFragment).commit();
                if(drugFragment != null) fragmentManager.beginTransaction().hide(drugFragment).commit();
                if(communityFragmnet != null) fragmentManager.beginTransaction().hide(communityFragmnet).commit();
                if(staticFragment != null) fragmentManager.beginTransaction().hide(staticFragment).commit();
                if(myPageFragment != null) fragmentManager.beginTransaction().show(myPageFragment).commit();

                break;
        }
    }
}