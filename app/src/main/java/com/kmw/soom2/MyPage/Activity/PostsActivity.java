package com.kmw.soom2.MyPage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.MyPage.Fragment.NewMineFragment;
import com.kmw.soom2.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.NullCheck;

public class PostsActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener , View.OnClickListener{

    private String TAG = "PostsActivity";
    TabLayout tabLayout;
    TextView txtBack;
    Intent beforeIntent;


    FragmentManager fragmentManager;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    ArrayList<String> fragmentTagArrayList = new ArrayList<>();
    Fragment mFragmentWrite,mFragmentComment,mFragmentScrap;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        beforeIntent = getIntent();

        txtBack = (TextView)findViewById(R.id.back_text_view_activity_posts);
        tabLayout = (TabLayout) findViewById(R.id.posts_tab_layout_container);
        //memo viewpager
//        viewPager = (ViewPager)findViewById(R.id.view_pager_activity_posts);
        tabLayout.addTab(tabLayout.newTab().setText("내가 쓴 글"));
        tabLayout.addTab(tabLayout.newTab().setText("댓글 단 글"));
        tabLayout.addTab(tabLayout.newTab().setText("저장한 글"));

        NullCheck(this);

        fragmentManager = getSupportFragmentManager();

        //memo viewpager
//        adapter = new CommunityFragmentAdapter(fragmentManager,3);
//        adapter.addFragment(new NewMineFragment().newInstance("내가 쓴 글"),"내가 쓴 글");
//        adapter.addFragment(new NewMineFragment().newInstance("댓글 단 글"),"댓글 단 글");
//        adapter.addFragment(new NewMineFragment().newInstance("저장한 글"),"저장한 글");
//
//        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(2);
//        tabLayout.setupWithViewPager(viewPager);

        if (beforeIntent.hasExtra("paging")){
            tabLayout.setScrollPosition(beforeIntent.getIntExtra("paging",0),0f,true);
            tabLayout.getTabAt(beforeIntent.getIntExtra("paging",0)).select();

            if (beforeIntent.getIntExtra("paging",0) == 0){
                if (mFragmentWrite == null){
                    mFragmentWrite = new NewMineFragment().newInstance("내가 쓴 글");
                    fragmentManager.beginTransaction().add(R.id.view_pager_activity_posts,mFragmentWrite).commit();
                }
                if (mFragmentWrite != null) fragmentManager.beginTransaction().show(mFragmentWrite).commit();
                if (mFragmentComment != null) fragmentManager.beginTransaction().hide(mFragmentComment).commit();
                if (mFragmentScrap != null) fragmentManager.beginTransaction().hide(mFragmentScrap).commit();
            }else if (beforeIntent.getIntExtra("paging",0) == 1){
                if (mFragmentComment == null){
                    mFragmentComment = new NewMineFragment().newInstance("댓글 단 글");
                    fragmentManager.beginTransaction().add(R.id.view_pager_activity_posts,mFragmentComment).commit();
                }
                if (mFragmentWrite != null) fragmentManager.beginTransaction().hide(mFragmentWrite).commit();
                if (mFragmentComment != null) fragmentManager.beginTransaction().show(mFragmentComment).commit();
                if (mFragmentScrap != null) fragmentManager.beginTransaction().hide(mFragmentScrap).commit();
            }else if (beforeIntent.getIntExtra("paging",0) == 2){
                if (mFragmentScrap == null){
                    mFragmentScrap = new NewMineFragment().newInstance("저장한 글");
                    fragmentManager.beginTransaction().add(R.id.view_pager_activity_posts,mFragmentScrap).commit();
                }
                if (mFragmentWrite != null) fragmentManager.beginTransaction().hide(mFragmentWrite).commit();
                if (mFragmentComment != null) fragmentManager.beginTransaction().hide(mFragmentComment).commit();
                if (mFragmentScrap != null) fragmentManager.beginTransaction().show(mFragmentScrap).commit();
            }
        }else{
            if (mFragmentWrite == null){
                mFragmentWrite = new NewMineFragment().newInstance("내가 쓴 글");
                fragmentManager.beginTransaction().add(R.id.view_pager_activity_posts,mFragmentWrite).commit();
            }
            if (mFragmentWrite != null) fragmentManager.beginTransaction().show(mFragmentWrite).commit();
            if (mFragmentComment != null) fragmentManager.beginTransaction().hide(mFragmentComment).commit();
            if (mFragmentScrap != null) fragmentManager.beginTransaction().hide(mFragmentScrap).commit();
        }

        txtBack.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i(TAG,"position : " + tab.getPosition());
        if (tab.getPosition() == 0){
            if (mFragmentWrite == null){
                mFragmentWrite = new NewMineFragment().newInstance("내가 쓴 글");
                fragmentManager.beginTransaction().add(R.id.view_pager_activity_posts,mFragmentWrite).commit();
            }
            if (mFragmentWrite != null) fragmentManager.beginTransaction().show(mFragmentWrite).commit();
            if (mFragmentComment != null) fragmentManager.beginTransaction().hide(mFragmentComment).commit();
            if (mFragmentScrap != null) fragmentManager.beginTransaction().hide(mFragmentScrap).commit();
        }else if (tab.getPosition() == 1){
            if (mFragmentComment == null){
                mFragmentComment = new NewMineFragment().newInstance("댓글 단 글");
                fragmentManager.beginTransaction().add(R.id.view_pager_activity_posts,mFragmentComment).commit();
            }
            if (mFragmentWrite != null) fragmentManager.beginTransaction().hide(mFragmentWrite).commit();
            if (mFragmentComment != null) fragmentManager.beginTransaction().show(mFragmentComment).commit();
            if (mFragmentScrap != null) fragmentManager.beginTransaction().hide(mFragmentScrap).commit();
        }else if (tab.getPosition() == 2){
            if (mFragmentScrap == null){
                mFragmentScrap = new NewMineFragment().newInstance("저장한 글");
                fragmentManager.beginTransaction().add(R.id.view_pager_activity_posts,mFragmentScrap).commit();
            }
            if (mFragmentWrite != null) fragmentManager.beginTransaction().hide(mFragmentWrite).commit();
            if (mFragmentComment != null) fragmentManager.beginTransaction().hide(mFragmentComment).commit();
            if (mFragmentScrap != null) fragmentManager.beginTransaction().show(mFragmentScrap).commit();
        }

        //memo viewpager
//        ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
//        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());
//        View tabViewChild = tabView.getChildAt(1);
//        Typeface typeface = ResourcesCompat.getFont(PostsActivity.this, R.font.notosanscjkkr_bold);
//        ((TextView) tabViewChild).setTypeface(typeface);
//        ((TextView) tabViewChild).setTextColor(getColor(R.color.colorPrimary));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_text_view_activity_posts : {
                onBackPressed();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BusProvider.getInstance().post(new ActivityResultEvent(requestCode, resultCode, data));
    }

}
