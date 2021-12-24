package com.kmw.soom2.Login.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Login.Adapter.WorkThroughViewPagerAdapter;
import com.kmw.soom2.Login.Fragment.WorkThroughA;
import com.kmw.soom2.Login.Fragment.WorkThroughB;
import com.kmw.soom2.Login.Fragment.WorkThroughC;
import com.kmw.soom2.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.StartActivity;

public class NewWorkThroughActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private String TAG = "NewWorkThroughActivity";
    ViewPager viewPager;
    ArrayList<Fragment> arrayList;
    WorkThroughViewPagerAdapter adapter;
//    TabLayout tabLayout;
    ImageView imgIndicator01,imgIndicator02,imgIndicator03;
    Button btnStart;
    TextView txtSkip;
    int selectPosition = 0;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_work_through);

        viewPager                       = (ViewPager) findViewById(R.id.view_pager_activity_work_through);
//        tabLayout                       = (TabLayout) findViewById(R.id.tab_layout_activity_work_through);
        imgIndicator01                  = findViewById(R.id.img_work_through_indicator_01);
        imgIndicator02                  = findViewById(R.id.img_work_through_indicator_02);
        imgIndicator03                  = findViewById(R.id.img_work_through_indicator_03);
        btnStart                        = findViewById(R.id.btn_work_through_start);
        txtSkip                         = findViewById(R.id.txt_new_work_through_skip);

        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        arrayList = new ArrayList<>();

        setupViewPager();

        if (viewPager != null){
            adapter = new WorkThroughViewPagerAdapter(getSupportFragmentManager(), arrayList);
            if (adapter != null){
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(this);
            }
        }

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosition == 0){
                    selectPosition++;
                    viewPager.setCurrentItem(selectPosition);
                    btnStart.setText("다음");
                    txtSkip.setVisibility(View.VISIBLE);
                }else if (selectPosition == 1){
                    selectPosition++;
                    viewPager.setCurrentItem(selectPosition);
                    btnStart.setText("시작하기");
                    txtSkip.setVisibility(View.INVISIBLE);
                }else {
                    editor.putBoolean("workThrough",true);
                    editor.apply();
                    StartActivity(NewWorkThroughActivity.this,LoginSignUpSelectActivity.class);
                    finish();
                }
            }
        });

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("workThrough",true);
                editor.apply();
                StartActivity(NewWorkThroughActivity.this,LoginSignUpSelectActivity.class);
                finish();
            }
        });
    }

    private void setupViewPager() {
        arrayList.add(new WorkThroughA());
        arrayList.add(new WorkThroughB());
        arrayList.add(new WorkThroughC());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectPosition = position;
        if (position == 0){
            imgIndicator01.setImageResource(R.drawable.selected_dot_work_through);
            imgIndicator02.setImageResource(R.drawable.default_dot_work_through);
            imgIndicator03.setImageResource(R.drawable.default_dot_work_through);
            btnStart.setText("다음");
            txtSkip.setVisibility(View.VISIBLE);
        }else if (position == 1){
            imgIndicator02.setImageResource(R.drawable.selected_dot_work_through);
            imgIndicator01.setImageResource(R.drawable.default_dot_work_through);
            imgIndicator03.setImageResource(R.drawable.default_dot_work_through);
            btnStart.setText("다음");
            txtSkip.setVisibility(View.VISIBLE);
        }else{
            imgIndicator03.setImageResource(R.drawable.selected_dot_work_through);
            imgIndicator02.setImageResource(R.drawable.default_dot_work_through);
            imgIndicator01.setImageResource(R.drawable.default_dot_work_through);
            btnStart.setText("시작하기");
            txtSkip.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onBackPressed() {

    }
}