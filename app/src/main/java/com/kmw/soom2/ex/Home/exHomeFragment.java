package com.kmw.soom2.ex.Home;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Communitys.Items.KoListItem;
import com.kmw.soom2.Home.Activitys.CalendarActivity;
import com.kmw.soom2.Home.Activitys.FilterActivity;
import com.kmw.soom2.Home.Activitys.GuideActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AstmaPercentActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.DustRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewSymptomRecordActivity;
import com.kmw.soom2.Home.Activitys.UrlWebViewActivity;
import com.kmw.soom2.Home.HomeAdapter.RecyclerViewAdapter;
import com.kmw.soom2.Home.HomeAdapter.ViewPagerAdapter;
import com.kmw.soom2.Home.HomeFragment;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.EtcItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.Home.HomeItem.ZoomAnimation;
import com.kmw.soom2.Login.Activitys.AgreeActivity;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Item.HistoryItems;
import com.kmw.soom2.ex.exNewAnotherActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.AGREE_SELECT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.AGREE_UPDATE;
import static com.kmw.soom2.Common.Utils.FEED_SHARED_POSITION;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;
import static com.kmw.soom2.Common.Utils.userItem;

public class exHomeFragment extends Fragment implements View.OnTouchListener, View.OnClickListener{

    private String TAG = "HomeFragment";

    CoordinatorLayout coordinatorLayout;
    ViewPager viewPager;
    TextView txtPagerCnt;
    TextView txtPagerTotalCnt;
    ViewPagerAdapter viewPagerAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    LinearLayout linGuest;

    Handler handler;
    SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    ImageView calendarImageView, filterImageView, alarmImageView;
    BottomSheetDialog measureBottomSheetDialog;
    ImageView measureImageView;
    String date;
    TabLayout tabLayout;
    LinearLayout tabStip;
    TextView txtPeriod;
    LinearLayout linMedicine, linSymptom, linBreath, linMemo, linMeasure;

    public ArrayList<BannerItem> bannerItemArrayList = new ArrayList<>();

    public static boolean categoryAct = false;

    public int REQUEST_CODE = 0;

    public ArrayList<EtcItem>                    etcItemArrayList = new ArrayList<>();
    public ArrayList<RecyclerViewItemList>       mList = new ArrayList<>();

    private Typeface typeface,typeface1;
    private String mTag                         = "";

    private RecyclerView                        recyclerView;
    private exItemAdapter                       adapter;

    public exHomeFragment() {

    }

    public static exHomeFragment newInstance(String mTag){

        exHomeFragment homeFragment = new exHomeFragment();

        Bundle args = new Bundle();
        args.putString("mTag",mTag);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    public String getInstance(String mKey){
        if (getArguments().getString(mKey) == null){
            return null;
        }
        return getArguments().getString(mKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (handler != null){
            handler.removeMessages(0);
            handler = null;
        }

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                handler.postDelayed(this, 8000);
            }
        }, 8000);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG,"onDestroyView");
        super.onDestroyView();
        BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        Log.i(TAG,"onStop");
        super.onStop();
        if (handler != null){
            handler.removeMessages(0);
            handler = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Configuration configuration = getResources().getConfiguration();
        configuration.fontScale = (float) 1; //0.85 small size, 1 normal size, 1,15 big etc
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        configuration.densityDpi = (int) getResources().getDisplayMetrics().xdpi;
        getResources().updateConfiguration(configuration, metrics);

        View v = inflater.inflate(R.layout.ex_home_fragmnet, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout_fragment_home);
        tabStip = ((LinearLayout) tabLayout.getChildAt(0));
        tabLayout.setVisibility(View.GONE);

        coordinatorLayout = v.findViewById(R.id.home_coordinator);

        linGuest = v.findViewById(R.id.lin_ex_home_guest);
        recyclerView = v.findViewById(R.id.recycler_view_fragment_home);

        linMedicine = (LinearLayout) v.findViewById(R.id.lin_home_medicine);
        linSymptom = (LinearLayout) v.findViewById(R.id.lin_home_symptom);
        linBreath = (LinearLayout) v.findViewById(R.id.lin_home_breath);
        linMemo = (LinearLayout) v.findViewById(R.id.lin_home_memo);
        linMeasure = (LinearLayout) v.findViewById(R.id.lin_home_measure);

        alarmImageView = v.findViewById(R.id.alarm_fragment_home);
        calendarImageView = (ImageView) v.findViewById(R.id.calendar_fragment_home);
        filterImageView = (ImageView) v.findViewById(R.id.filter_fragment_home);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager_fragment_home);
        txtPeriod = (TextView) v.findViewById(R.id.day_count_fragment_home);
        collapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.collapsing_tool_bar);
        toolbar = (Toolbar)v.findViewById(R.id.home_tool_bar);
        txtPagerCnt = (TextView)v.findViewById(R.id.txt_home_banner_pager_cnt);
        txtPagerTotalCnt = (TextView)v.findViewById(R.id.txt_home_banner_pager_total_cnt);

        typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_medium);
        typeface1 = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);

        NullCheck(getActivity());

        ViewCompat.requestApplyInsets(collapsingToolbarLayout);

        DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
        int width1 = dm1.widthPixels;

        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width1 * 0.41));
                viewPager.setLayoutParams(params);
                viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pos = position % bannerItemArrayList.size();
                txtPagerCnt.setText(""+(pos+1));
                txtPagerTotalCnt.setText("/"+bannerItemArrayList.size());
//                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        measureImageView = (ImageView) v.findViewById(R.id.measure_fragment_home);

        measureBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
        final View bottomSheetDialogMeasuer = getLayoutInflater().inflate(R.layout.fragment_measure_dialog, null);
        measureBottomSheetDialog.setContentView(bottomSheetDialogMeasuer);
        Button btnPercent = (Button)measureBottomSheetDialog.findViewById(R.id.asthma_percent_button_fragment_measure_dialog);
        Button btnBreath = (Button) measureBottomSheetDialog.findViewById(R.id.asthma_control_button_fragment_measure_dialog);
        Button btnDust = (Button) measureBottomSheetDialog.findViewById(R.id.dust_button_fragment_measure_dialog);

        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REQUEST_CODE = 1111;
                Intent intent = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REQUEST_CODE = 2222;
                Intent intent = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(intent);
            }
        });

        linGuest.setOnClickListener(this);
        linMedicine.setOnClickListener(this);
        linSymptom.setOnClickListener(this);
        linBreath.setOnClickListener(this);
        linMemo.setOnClickListener(this);
        linMeasure.setOnClickListener(this);

        btnBreath.setOnClickListener(this);
        btnDust.setOnClickListener(this);
        btnPercent.setOnClickListener(this);

        adapter = new exItemAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
        mList.add(new RecyclerViewItemList(RecyclerViewItemList.EX_ITEM_01));
        mList.add(new RecyclerViewItemList(RecyclerViewItemList.EX_ITEM_02));
        mList.add(new RecyclerViewItemList(RecyclerViewItemList.EX_ITEM_03));
        mList.add(new RecyclerViewItemList(RecyclerViewItemList.EX_ITEM_04));
        mList.add(new RecyclerViewItemList(RecyclerViewItemList.EX_ITEM_05));
        adapter.addItem(mList);
        recyclerView.setAdapter(adapter);

        new SelectBannerListNetWork().execute();

        return v;
    }

    public void setTabLayout() {
        for (int i = 0; i < bannerItemArrayList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            tabStip.getChildAt(i).setOnTouchListener(this);
        }
    }

    String response;

    public class SelectBannerListNetWork extends AsyncTask<String, String, String> {

        String mUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            FormBody.Builder body;

            body = (new FormBody.Builder());

            mUrl = Utils.Server.selectBannerList();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, mUrl, body.build());
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    bannerItemArrayList = new ArrayList();

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = jsonArray.length() - 1; i >= 0; i--) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        BannerItem bannerItem = new BannerItem();
                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            bannerItem.setBannerNo(JsonIsNullCheck(object, "BANNER_NO"));
                            bannerItem.setBannerType(JsonIsNullCheck(object, "BANNER_TYPE"));
                            bannerItem.setBannerLink(JsonIsNullCheck(object, "BANNER_LINK"));
                            bannerItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                            bannerItem.setPriority(JsonIntIsNullCheck(object, "PRIORITY"));
                            bannerItem.setBannerTitle(JsonIsNullCheck(object,"BANNER_TITLE"));
                            bannerItemArrayList.add(bannerItem);
                        }
                    }

                    Collections.sort(bannerItemArrayList, UpComparator);

                    viewPagerAdapter = new ViewPagerAdapter(getContext(), bannerItemArrayList,true);

                    setTabLayout();
//                    int limit = (viewPagerAdapter.getCount() > 1 ? viewPagerAdapter.getCount() - 1 : 1);
//                    viewPager.setOffscreenPageLimit(limit);
                    viewPager.setAdapter(viewPagerAdapter);
                    viewPagerAdapter.notifyDataSetChanged();

                    final int FIRST_PAGE = 60006 * bannerItemArrayList.size() / 2;
                    viewPager.setCurrentItem(FIRST_PAGE);
                    viewPager.setPageTransformer(true, new ZoomAnimation());
                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            TabLayout.Tab tab = tabLayout.getTabAt(Integer.MAX_VALUE % (Integer.MAX_VALUE - position) % bannerItemArrayList.size());
                            tab.select();
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                } catch (JSONException e) {

                }
            }
        }
    }

    private Comparator<BannerItem> UpComparator = new Comparator<BannerItem>() {
        @Override
        public int compare(BannerItem bannerItem, BannerItem t1) {
            int reset;
            if (bannerItem.getPriority() > t1.getPriority()) {
                reset = -1;
            } else if (bannerItem.getPriority() == t1.getPriority()) {
                reset = 0;
            } else
                reset = 1;
            return reset;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_home_medicine: {
                Intent i = new Intent(getActivity(), exMedicineMoveActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_home_symptom: {
                Intent i = new Intent(getActivity(), exSymptomMoveActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_home_breath: {
                Intent i = new Intent(getActivity(), exBreathMoveActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_home_memo: {
                Intent i = new Intent(getActivity(), exMemoMoveActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_home_measure: {
                measureBottomSheetDialog.show();
                break;
            }
            case R.id.asthma_control_button_fragment_measure_dialog: {
                Intent i = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.asthma_percent_button_fragment_measure_dialog : {
                Intent i = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.dust_button_fragment_measure_dialog: {
                Intent i = new Intent(getActivity(), NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_ex_home_guest : {
                for (int i = 0; i < Utils.linkKeys.size(); i++) {
                    if (Utils.linkKeys.get(i).getTitle().equals("lookaround")) {
                        if (Utils.linkKeys.get(i).getLinkUrl() != null) {
                            Intent intent = new Intent(getActivity(), UrlWebViewActivity.class);
                            intent.putExtra("url",Utils.linkKeys.get(i).getLinkUrl());
                            intent.putExtra("guest",true);
                            intent.putExtra("around",true);
                            startActivity(intent);
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

}
