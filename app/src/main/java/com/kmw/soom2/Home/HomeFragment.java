package com.kmw.soom2.Home;

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
import com.kmw.soom2.Home.Activitys.GuideActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AstmaPercentActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.DustRecordActivity;
import com.kmw.soom2.Home.Activitys.FilterActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewSymptomRecordActivity;
import com.kmw.soom2.Home.Activitys.UrlWebViewActivity;
import com.kmw.soom2.Home.HomeAdapter.RecyclerViewAdapter;
import com.kmw.soom2.Home.HomeAdapter.ViewPagerAdapter;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.EtcItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.Home.HomeItem.ZoomAnimation;
import com.kmw.soom2.Login.Activitys.AgreeActivity;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Item.HistoryItems;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import static com.kmw.soom2.Common.Utils.COMMUNITY_BLOG_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.FEED_SHARED_POSITION;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class HomeFragment extends Fragment implements View.OnTouchListener, View.OnClickListener, AsyncResponse {

    private String TAG = "HomeFragment";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    LinearLayout linFeedShared;
    RecyclerViewAdapter adapter = null;
    CoordinatorLayout coordinatorLayout;
    ViewPager viewPager;
    TextView txtPagerCnt;
    TextView txtPagerTotalCnt;
    ViewPagerAdapter viewPagerAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;

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
    LinearLayout linNoList;
    LinearLayout linGuideParent;
    LinearLayout linMedicine, linSymptom, linBreath, linMemo, linMeasure;
    SwipeRefreshLayout mRefreshView;

    //memo 약관동의 팝업
    View                bottomSheetDialog_Agree_View;
    BottomSheetDialog   mAgreeBottomSheetDialog;
    LinearLayout        mLinAgreeAll;
    CheckBox            mChbAgreeAll;
    CheckBox            mChbAgreeService;
    CheckBox            mChbAgreePersonal;
    CheckBox            mChbAgreeSensitivity;
    CheckBox            mChbAgreeMarketing;
    Button              mBtnAgreeDone;
    ImageView           mImageDetailService;
    ImageView           mImageDetailPersonal;
    ImageView           mImageDetailSensitivity;

    boolean mLockScrollView = false;
    boolean actIsPossible = true;

    int paging = 1;
    int searchTotalPage = 0;
    float width;

    ProgressDialog progressDialog;
    public static ArrayList<String> filterTextList = new ArrayList<>();
    public ArrayList<BannerItem> bannerItemArrayList = new ArrayList<>();

    public static boolean categoryAct = false;

    public int REQUEST_CODE = 0;

    public ArrayList<String>                     registerDtList = new ArrayList<>();
    public ArrayList<ArrayList<String>>          koList = new ArrayList<>();
    public ArrayList<ArrayList<Integer>>         emergencyList = new ArrayList<>();
    public ArrayList<String>                     hisItemsRegisterDt = new ArrayList<>();
    public ArrayList<EtcItem>                    etcItemArrayList = new ArrayList<>();
    public ArrayList<RecyclerViewItemList>       mList = new ArrayList<>();
    public ArrayList<HistoryItems>               hisItems = new ArrayList<>();
    public ArrayList<String>                     AllRegisterDtList;
    public ArrayList<ArrayList<String>>          AllKoList;
    public ArrayList<EtcItem>                    AllEtcItemArrayList;
    public ArrayList<RecyclerViewItemList>       AllList;
    public int                                   FEED_LIST_LAST_POSITION;
    public int                                   FEED_ALL_LIST_LAST_POSITION;
    public int                                   mSize = 0;

    private Typeface typeface,typeface1;
    private String mTag                         = "";
    int headerIndex = 0;

    public static TreeMap<String, TreeMap<String, ArrayList<HistoryItems>>> finalMap;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String mTag){

        HomeFragment homeFragment = new HomeFragment();

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

//        if (getVisibleFragment() instanceof HomeFragment){

            new SelectBannerListNetWork(2).execute();

            Log.i(TAG,"onResume");
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

            if (userItem == null) {
                if (getNetworkState() != null && getNetworkState().isConnected()) {
                    new LoginProcessNetWork().execute();
                }
            } else {
//                try {
//                    txtPeriod.setText("숨 관리, " + (calDateBetweenAandB(format1.format(format2.parse(userItem.getCreateDt())), format1.format(new Date(System.currentTimeMillis()))) + 1) + "일차");
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
            }

            if (REQUEST_CODE == 2222 || REQUEST_CODE == 1111){
                REQUEST_CODE = 0;
            }else{
                REQUEST_CODE = 0;
            }
//        }
//        new SelectActHistoryListNetWork().execute();
    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getActivity().getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        progressDialog = new ProgressDialog(getActivity(),R.style.MyTheme);

        filterTextList = new ArrayList<>();

        filterTextList.add("1");
        filterTextList.add("11");
        filterTextList.add("12");
        filterTextList.add("13");
        filterTextList.add("14");
        filterTextList.add("15");
        filterTextList.add("16");
        filterTextList.add("21");
        filterTextList.add("22");
        filterTextList.add("23");
        //천식질환 지수 안보이게
        filterTextList.add("24");
        filterTextList.add("30");
        filterTextList.add("40");

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
        if (progressDialog != null){
            progressDialog.dismiss();
        }
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

        View v = inflater.inflate(R.layout.fragment_new_home, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout_fragment_home);
        tabStip = ((LinearLayout) tabLayout.getChildAt(0));
        tabLayout.setVisibility(View.GONE);

        recyclerView = v.findViewById(R.id.recycler_view_fragment_home);
        relativeLayout = v.findViewById(R.id.rela_view_fragment_home);
        linFeedShared = v.findViewById(R.id.lin_home_feed_shared);
        coordinatorLayout = v.findViewById(R.id.home_coordinator);

        mRefreshView = v.findViewById(R.id.refresh_home_fragment);

        linMedicine = (LinearLayout) v.findViewById(R.id.lin_home_medicine);
        linSymptom = (LinearLayout) v.findViewById(R.id.lin_home_symptom);
        linBreath = (LinearLayout) v.findViewById(R.id.lin_home_breath);
        linMemo = (LinearLayout) v.findViewById(R.id.lin_home_memo);
        linMeasure = (LinearLayout) v.findViewById(R.id.lin_home_measure);
        linNoList = (LinearLayout) v.findViewById(R.id.lin_home_fragment_symptom_no_list);
        linGuideParent = (LinearLayout)v.findViewById(R.id.lin_home_feed_bottom_info_parent);
        txtPagerCnt = (TextView)v.findViewById(R.id.txt_home_banner_pager_cnt);
        txtPagerTotalCnt = (TextView)v.findViewById(R.id.txt_home_banner_pager_total_cnt);

        alarmImageView = v.findViewById(R.id.alarm_fragment_home);
        calendarImageView = (ImageView) v.findViewById(R.id.calendar_fragment_home);
        filterImageView = (ImageView) v.findViewById(R.id.filter_fragment_home);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager_fragment_home);
        txtPeriod = (TextView) v.findViewById(R.id.day_count_fragment_home);
        collapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.collapsing_tool_bar);
        toolbar = (Toolbar)v.findViewById(R.id.home_tool_bar);

        //memo 약관동의 팝업
//        AgreePopUp();

        typeface = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_medium);
        typeface1 = ResourcesCompat.getFont(getActivity(), R.font.notosanscjkkr_bold);

        NullCheck(getActivity());

        ViewCompat.requestApplyInsets(collapsingToolbarLayout);

        DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
        int width1 = dm1.widthPixels;

        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width1 * 0.408));
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

        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                paging = 1;
                searchTotalPage = 0;
                mLockScrollView = false;
                categoryAct = false;

                mSize = 0;
                mList = new ArrayList<>();
                hisItems = new ArrayList<>();
                hisItemsRegisterDt = new ArrayList<>();
                registerDtList = new ArrayList<>();
                koList = new ArrayList<>();
                etcItemArrayList = new ArrayList<>();
                AllRegisterDtList = new ArrayList<>();
                AllKoList = new ArrayList<>();
                AllEtcItemArrayList = new ArrayList<>();

                new SelectHomeFeedHistoryListNetWork().execute("1");
                mRefreshView.setRefreshing(false);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView1, int dx, int dy) {
                super.onScrolled(recyclerView1, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView1.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCnt = recyclerView1.getAdapter().getItemCount();

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && (paging < searchTotalPage) && mLockScrollView) {
                    mLockScrollView = false;
                    paging++;
                    new SelectHomeFeedHistoryListNetWork().execute("" + paging);
                }
            }
        });

        measureBottomSheetDialog = new BottomSheetDialog(getActivity(), R.style.SheetDialog);
        final View bottomSheetDialogMeasuer = getLayoutInflater().inflate(R.layout.fragment_measure_dialog, null);
        measureBottomSheetDialog.setContentView(bottomSheetDialogMeasuer);
        Button btnPercent = (Button)measureBottomSheetDialog.findViewById(R.id.asthma_percent_button_fragment_measure_dialog);
        Button btnBreath = (Button) measureBottomSheetDialog.findViewById(R.id.asthma_control_button_fragment_measure_dialog);
        Button btnDust = (Button) measureBottomSheetDialog.findViewById(R.id.dust_button_fragment_measure_dialog);

        alarmImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PushAlarmListActivity.class);
//                getActivity().startActivityForResult(intent, 1111);
                startActivity(intent);
            }
        });

        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REQUEST_CODE = 1111;
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                getActivity().startActivityForResult(intent, 1111);
            }
        });

        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REQUEST_CODE = 2222;
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.putStringArrayListExtra("filter", filterTextList);
                startActivityForResult(intent, 2222);
            }
        });

        linMedicine.setOnClickListener(this);
        linSymptom.setOnClickListener(this);
        linBreath.setOnClickListener(this);
        linMemo.setOnClickListener(this);
        linMeasure.setOnClickListener(this);
        linGuideParent.setOnClickListener(this);
        linFeedShared.setOnClickListener(this);

        btnBreath.setOnClickListener(this);
        btnDust.setOnClickListener(this);
        btnPercent.setOnClickListener(this);

        new SelectBannerListNetWork(1).execute();

        filterTextList = new ArrayList<>();

        filterTextList.add("1");
        filterTextList.add("11");
        filterTextList.add("12");
        filterTextList.add("13");
        filterTextList.add("14");
        filterTextList.add("15");
        filterTextList.add("16");
        filterTextList.add("21");
        filterTextList.add("22");
        filterTextList.add("23");
        filterTextList.add("24");
        filterTextList.add("30");
        filterTextList.add("40");

        adapter = new RecyclerViewAdapter(getContext(), this, width,typeface,typeface1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
        adapter.addItem(mList);
        recyclerView.setAdapter(adapter);

        new SelectHomeFeedHistoryListNetWork().execute("1");

        NetworkCall(AGREE_SELECT);

        mTag = getInstance("mTag");

        if (mTag == null || mTag.length() == 0){

        }else{
            if (mTag.equals("medicine")){
                linMedicine.performClick();
            }else if (mTag.equals("symptom")){
                linSymptom.performClick();
            }else if (mTag.equals("pef")){
                linBreath.performClick();
            }else if (mTag.equals("memo")){
                linMemo.performClick();
            }else if (mTag.equals("act")){
//                if (!categoryAct) {
//                    REQUEST_CODE = 1111;
//                    Intent i = new Intent(getActivity(), AsthmaControlActivity.class);
//                    i.putExtra("homeFragment", true);
//                    startActivityForResult(i, 1111);
//                } else {
//                    new OneButtonDialog(getActivity(), "1일 검사 횟수 초과", "이미 검사를 완료하셨습니다.\n천식조절검사(ACT)는\n하루에 한번 할 수 있습니다.", "확인");
//                }
                new ActCheckNetwork().execute();
            }else if (mTag.equals("dust")){
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), DustRecordActivity.class);
                startActivityForResult(i, 1111);
            }else if (mTag.equals("asthma")){
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), AstmaPercentActivity.class);
                startActivityForResult(i, 1111);
            }
        }

        return v;
    }

    void AgreePopUp(){
        bottomSheetDialog_Agree_View    = getLayoutInflater().inflate(R.layout.agree_dialog,null);
        mAgreeBottomSheetDialog         = new BottomSheetDialog(getActivity());

        mLinAgreeAll                    = (LinearLayout)bottomSheetDialog_Agree_View.findViewById(R.id.lin_agree_check_all);
        mChbAgreeAll                    = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_all);
        mChbAgreeService                = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_service);
        mChbAgreePersonal               = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_personal);
        mChbAgreeSensitivity            = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_sensitivity);
        mChbAgreeMarketing              = (CheckBox)bottomSheetDialog_Agree_View.findViewById(R.id.chb_agree_marketing);
        mBtnAgreeDone                   = (Button)bottomSheetDialog_Agree_View.findViewById(R.id.btn_agree_done);
        mImageDetailService             = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_service);
        mImageDetailPersonal            = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_personal);
        mImageDetailSensitivity         = (ImageView)bottomSheetDialog_Agree_View.findViewById(R.id.img_agree_move_sensitivity);

        mAgreeBottomSheetDialog.setContentView(bottomSheetDialog_Agree_View);
        mAgreeBottomSheetDialog.setCancelable(false);

        mChbAgreeAll.setOnClickListener(this);
        mChbAgreeService.setOnClickListener(this);
        mChbAgreePersonal.setOnClickListener(this);
        mChbAgreeSensitivity.setOnClickListener(this);
        mChbAgreeMarketing.setOnClickListener(this);
        mBtnAgreeDone.setOnClickListener(this);
        mImageDetailService.setOnClickListener(this);
        mImageDetailPersonal.setOnClickListener(this);
        mImageDetailSensitivity.setOnClickListener(this);

        mAgreeBottomSheetDialog.show();
    }

    public void MarketingOneBtnPopup(Context context) {

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.marketing_one_btn_popup, null);

        TextView txtAgreeDate = (TextView) layout.findViewById(R.id.txt_marketing_one_btn_popup_contents);
        Button btnOk = (Button) layout.findViewById(R.id.btn_marketing_one_btn_popup_ok);

        txtAgreeDate.setText("동의일자 " + new SimpleDateFormat("yyyy.MM.dd").format(new Date(System.currentTimeMillis())));

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateTimeDialog.setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //memo 약관동의 url
                dateTimeDialog.dismiss();
                NetworkCall(AGREE_UPDATE);
            }
        });
    }

    public void setTabLayout() {
        for (int i = 0; i < bannerItemArrayList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
            tabStip.getChildAt(i).setOnTouchListener(this);
        }
    }

    String response;

    public class ActCheckNetwork extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            RequestBody body = new FormBody.Builder().add("USER_NO", "" + userItem.getUserNo()).add("Search_ShowCNT", "1").add("CATEGORY","21").add("Search_Page", "1").build();
            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedListByCategory(), body);
                Log.i(TAG,"response : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            categoryAct = false;
            if (s != null){
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    if (!jsonObject.has("list")) {
                        categoryAct = false;
                        Log.i(TAG,"respose : true");
                    }else{
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                                if (JsonIsNullCheck(object, "REGISTER_DT").substring(0, 10).equals(Utils.formatYYYYMMDD.format(new Date(System.currentTimeMillis())))) {
                                    if (categoryAct) {

                                    } else {
                                        categoryAct = true;
                                    }
                                }
                            }
                        }
                    }
                }catch (JSONException e){
                    categoryAct = false;
                }
            }else{
                categoryAct = false;
            }

            if (!categoryAct) {
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), AsthmaControlActivity.class);
                i.putExtra("homeFragment", true);
                startActivityForResult(i, 1111);
            } else {
                new OneButtonDialog(getActivity(), "1일 검사 횟수 초과", "이미 검사를 완료하셨습니다.\n천식조절검사(ACT)는\n하루에 한번 할 수 있습니다.", "확인");
            }
        }
    }

    public class SelectHomeFeedHistoryListNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody body = new FormBody.Builder().add("USER_NO", "" + userItem.getUserNo()).add("Search_ShowCNT", "50").add("CATEGORY",filterTextList.toString().replace("[","").replace("]","").replace(" ","")).add("Search_Page", strings[0]).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectHomeFeedListByCategory(), body);
                Log.i(TAG,"response : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String s) {

            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (!jsonObject.has("list")) {
                        progressDialog.dismiss();
                        relativeLayout.setVisibility(View.GONE);
                        linNoList.setVisibility(View.VISIBLE);
                    }else{
                        linFeedShared.setVisibility(View.VISIBLE);
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    searchTotalPage = jsonObject.getInt("Search_TotalPage");

                    if (paging == 1){
                        FEED_LIST_LAST_POSITION = 0;
                        FEED_ALL_LIST_LAST_POSITION = 0;
                        headerIndex = 0;
                    }

                    ArrayList<KoListItem> koStringArr = new ArrayList<>();

                    int size = jsonArray.length();

                    for (int i = 0; i < size; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        Log.i(TAG,"HomeFeed : " + object.toString());

                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            HistoryItems hisItem = new HistoryItems();
                            if (JsonIntIsNullCheck(object, "CATEGORY") > 10 && JsonIntIsNullCheck(object, "CATEGORY") < 20){
                                if (hisItemsRegisterDt.contains(JsonIsNullCheck(object, "REGISTER_DT")) && (hisItems.get(hisItemsRegisterDt.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).getCategory() >= 10 && hisItems.get(hisItemsRegisterDt.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).getCategory() <= 20)){
                                    String category = hisItems.get(hisItemsRegisterDt.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).getCategorySplit() + "," + JsonIsNullCheck(object, "CATEGORY");
                                    hisItems.get(hisItemsRegisterDt.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).setCategorySplit(category);
                                }else{
                                    hisItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                    hisItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                    hisItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                    hisItem.setCategorySplit(JsonIsNullCheck(object, "CATEGORY"));
                                    hisItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                                    hisItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                                    hisItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                                    hisItem.setAge(JsonIntIsNullCheck(object, "AGE"));
                                    hisItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                                    hisItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                                    hisItemsRegisterDt.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                    hisItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                                    hisItem.setAliveFlag(JsonIntIsNullCheck(object, "ALIVE_FLAG"));

                                    /// 복약 관련
                                    hisItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                    hisItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                    hisItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                    hisItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                    hisItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                    hisItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                                    hisItem.setEndDt(JsonIsNullCheck(object, "END_DT"));

                                    if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") != 1) {
                                        hisItem.setKo(JsonIsNullCheck(object, "KO") + "[응급]");
                                    } else {
                                        hisItem.setKo(JsonIsNullCheck(object, "KO"));
                                    }

                                    /// 증상
                                    hisItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                                    /// 메모
                                    hisItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                                    /// 미세먼지
                                    hisItem.setLatitude(JsonIsNullCheck(object, "LATITUDE"));
                                    hisItem.setLongitute(JsonIsNullCheck(object, "LONGITUDE"));
                                    hisItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                                    hisItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                                    hisItem.setDustState(JsonIntIsNullCheck(object, "DUST_STATE"));
                                    hisItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                                    hisItem.setUltraDustState(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                                    /// PEF
                                    hisItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                                    hisItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                                    /// ACT
                                    hisItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                                    hisItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                                    hisItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                                    hisItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                                    hisItem.setAsthmaScore(JsonIntIsNullCheck(object,"ASTHMA_SCORE"));
                                    hisItems.add(hisItem);
                                }
                            }else{
                                hisItem.setUserHistoryNo(JsonIntIsNullCheck(object, "USER_HISTORY_NO"));
                                hisItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                                hisItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                hisItem.setCategorySplit(JsonIsNullCheck(object, "CATEGORY"));
                                hisItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                                hisItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                                hisItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                                hisItem.setAge(JsonIntIsNullCheck(object, "AGE"));
                                hisItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                                hisItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                                hisItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                                hisItemsRegisterDt.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                hisItem.setAliveFlag(JsonIntIsNullCheck(object, "ALIVE_FLAG"));

                                /// 복약 관련
                                hisItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                                hisItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                hisItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                hisItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                hisItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                hisItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                                hisItem.setEndDt(JsonIsNullCheck(object, "END_DT"));

                                if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") != 1) {
                                    hisItem.setKo(JsonIsNullCheck(object, "KO") + "[응급]");
                                } else {
                                    hisItem.setKo(JsonIsNullCheck(object, "KO"));
                                }

                                /// 증상
                                hisItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                                /// 메모
                                hisItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                                /// 미세먼지
                                hisItem.setLatitude(JsonIsNullCheck(object, "LATITUDE"));
                                hisItem.setLongitute(JsonIsNullCheck(object, "LONGITUDE"));
                                hisItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                                hisItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                                hisItem.setDustState(JsonIntIsNullCheck(object, "DUST_STATE"));
                                hisItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                                hisItem.setUltraDustState(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                                /// PEF
                                hisItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                                hisItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                                /// ACT
                                hisItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                                hisItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                                hisItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                                hisItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                                hisItem.setAsthmaScore(JsonIntIsNullCheck(object,"ASTHMA_SCORE"));
                                hisItems.add(hisItem);
                            }
                        }
                    }

                    Log.i(TAG,"size : " + hisItems.size());

                    HashMap<String, ArrayList<HistoryItems>> map = setupHistoryData(hisItems);
                    TreeMap<String, ArrayList<HistoryItems>> treeMap = new TreeMap<>(map);

                    finalMap = new TreeMap<>();

                    for (Map.Entry<String, ArrayList<HistoryItems>> entry : treeMap.descendingMap().entrySet()) {
                        String key = entry.getKey();
                        ArrayList<HistoryItems> value = entry.getValue();
                        TreeMap<String, ArrayList<HistoryItems>> subMapItems = new TreeMap<>(setupHistoryDataWithRegiDt(value));
                        finalMap.put(key, subMapItems);
                    }

                    Log.i(TAG,"size entry : " + finalMap.size());

                    for (int i = 0; i < size; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {

                            ArrayList<String> koChildList = new ArrayList<>();
                            ArrayList<String> AllKoChildList = new ArrayList<>();

                            EtcItem etcItem = new EtcItem();

                            if (JsonIntIsNullCheck(object, "CATEGORY") == 21 && JsonIsNullCheck(object, "REGISTER_DT").substring(0, 10).equals(Utils.formatYYYYMMDD.format(new Date(System.currentTimeMillis())))) {
                                if (categoryAct){

                                }else{
                                    categoryAct = true;
                                }
                            }

                            if (filterTextList.contains(JsonIsNullCheck(object, "CATEGORY"))) {
                                if (JsonIntIsNullCheck(object, "CATEGORY") == 1) {
                                    if (registerDtList.contains(JsonIsNullCheck(object, "REGISTER_DT")) && etcItemArrayList.get(registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).getCategory() == 1 ) {
                                        if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") == 1) {
                                            koList.get(registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).add(JsonIsNullCheck(object, "KO"));
                                        } else {
                                            koList.get(registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).add(JsonIsNullCheck(object, "KO") + "[응급]");
                                        }
                                    } else {
                                        registerDtList.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                        if (JsonIntIsNullCheck(object, "EMERGENCY_FLAG") == 1) {
                                            koChildList.add(JsonIsNullCheck(object, "KO"));
                                        } else {
                                            koChildList.add(JsonIsNullCheck(object, "KO") + "[응급]");
                                        }
                                        koList.add(koChildList);

                                        etcItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                                        etcItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                                        etcItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                                        etcItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                                        etcItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                                        etcItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                                        etcItem.setAsthmaScore(JsonIntIsNullCheck(object,"ASTHMA_SCORE"));
                                        etcItem.setDustStatus(JsonIntIsNullCheck(object, "DUST_STATE"));
                                        etcItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                                        etcItem.setUltraDustStatus(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                                        etcItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                        etcItem.setEndDt(JsonIsNullCheck(object, "END_DT"));
                                        etcItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                        etcItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                        etcItem.setImagesNo(JsonIsNullCheck(object,"IMAGES_NO"));
                                        etcItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                                        etcItem.setLat(JsonIsNullCheck(object, "LATITUDE"));
                                        etcItem.setLng(JsonIsNullCheck(object, "LONGITUDE"));
                                        etcItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                                        etcItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                                        etcItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                                        etcItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                                        etcItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                        etcItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                        etcItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                                        etcItem.setMedicineNo(JsonIsNullCheck(object, "MEDICINE_NO"));
                                        etcItem.setUserHistoryNo(JsonIsNullCheck(object, "USER_HISTORY_NO"));
                                        etcItem.setKo(JsonIsNullCheck(object, "KO"));
                                        etcItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));
                                        etcItemArrayList.add(etcItem);
                                    }
                                } else {
                                    if (JsonIntIsNullCheck(object, "CATEGORY") >= 10 && JsonIntIsNullCheck(object, "CATEGORY") <=20){
                                        if (registerDtList.contains(JsonIsNullCheck(object, "REGISTER_DT")) && (etcItemArrayList.get(registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).getCategory() >= 10 && etcItemArrayList.get(registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"))).getCategory() <= 20)){
                                            int idx = registerDtList.indexOf(JsonIsNullCheck(object, "REGISTER_DT"));
                                            String ko = koList.get(idx).get(0) + "," + JsonIntIsNullCheck(object, "CATEGORY");
                                            koChildList.add(ko);
                                            koList.set(idx,koChildList);
                                            etcItemArrayList.get(idx).setUserHistoryNo(etcItemArrayList.get(idx).getUserHistoryNo()+","+JsonIsNullCheck(object, "USER_HISTORY_NO"));
                                        }else{
                                            registerDtList.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                            koChildList.add("" + JsonIntIsNullCheck(object, "CATEGORY"));
                                            koList.add(koChildList);

                                            etcItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                                            etcItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                                            etcItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                                            etcItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                                            etcItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                                            etcItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                                            etcItem.setAsthmaScore(JsonIntIsNullCheck(object,"ASTHMA_SCORE"));
                                            etcItem.setDustStatus(JsonIntIsNullCheck(object, "DUST_STATE"));
                                            etcItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                                            etcItem.setUltraDustStatus(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                                            etcItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                            etcItem.setEndDt(JsonIsNullCheck(object, "END_DT"));
                                            etcItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                            etcItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                            etcItem.setImagesNo(JsonIsNullCheck(object,"IMAGES_NO"));
                                            etcItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                                            etcItem.setLat(JsonIsNullCheck(object, "LATITUDE"));
                                            etcItem.setLng(JsonIsNullCheck(object, "LONGITUDE"));
                                            etcItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                                            etcItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                                            etcItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                                            etcItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                                            etcItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                            etcItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                            etcItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                                            etcItem.setMedicineNo(JsonIsNullCheck(object, "MEDICINE_NO"));
                                            etcItem.setUserHistoryNo(JsonIsNullCheck(object, "USER_HISTORY_NO"));
                                            etcItem.setKo(JsonIsNullCheck(object, "KO"));
                                            etcItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));

                                            etcItemArrayList.add(etcItem);
                                        }
                                    }else{
                                        registerDtList.add(JsonIsNullCheck(object, "REGISTER_DT"));
                                        koChildList.add("" + JsonIntIsNullCheck(object, "CATEGORY"));
                                        koList.add(koChildList);

                                        etcItem.setActScore(JsonIntIsNullCheck(object, "ACT_SCORE"));
                                        etcItem.setActSelected(JsonIsNullCheck(object, "ACT_SELECTED"));
                                        etcItem.setActState(JsonIntIsNullCheck(object, "ACT_STATE"));
                                        etcItem.setActType(JsonIntIsNullCheck(object, "ACT_TYPE"));
                                        etcItem.setCause(JsonIsNullCheck(object, "CAUSE"));
                                        etcItem.setDust(JsonIntIsNullCheck(object, "DUST"));
                                        etcItem.setAsthmaScore(JsonIntIsNullCheck(object,"ASTHMA_SCORE"));
                                        etcItem.setDustStatus(JsonIntIsNullCheck(object, "DUST_STATE"));
                                        etcItem.setUltraDust(JsonIntIsNullCheck(object, "ULTRA_DUST"));
                                        etcItem.setUltraDustStatus(JsonIntIsNullCheck(object, "ULTRA_DUST_STATE"));
                                        etcItem.setEmergencyFlag(JsonIntIsNullCheck(object, "EMERGENCY_FLAG"));
                                        etcItem.setEndDt(JsonIsNullCheck(object, "END_DT"));
                                        etcItem.setFrequency(JsonIntIsNullCheck(object, "FREQUENCY"));
                                        etcItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                                        etcItem.setImagesNo(JsonIsNullCheck(object,"IMAGES_NO"));
                                        etcItem.setInspiratorFlag(JsonIntIsNullCheck(object, "INSPIRATOR_FLAG"));
                                        etcItem.setLat(JsonIsNullCheck(object, "LATITUDE"));
                                        etcItem.setLng(JsonIsNullCheck(object, "LONGITUDE"));
                                        etcItem.setLocation(JsonIsNullCheck(object, "LOCATION"));
                                        etcItem.setMemo(JsonIsNullCheck(object, "MEMO"));
                                        etcItem.setPefScore(JsonIntIsNullCheck(object, "PEF_SCORE"));
                                        etcItem.setStartDt(JsonIsNullCheck(object, "START_DT"));
                                        etcItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                                        etcItem.setVolume(JsonIsNullCheck(object, "VOLUME"));
                                        etcItem.setRegisterDt(JsonIsNullCheck(object, "REGISTER_DT"));
                                        etcItem.setMedicineNo(JsonIsNullCheck(object, "MEDICINE_NO"));
                                        etcItem.setUserHistoryNo(JsonIsNullCheck(object, "USER_HISTORY_NO"));
                                        etcItem.setKo(JsonIsNullCheck(object, "KO"));
                                        etcItem.setCategory(JsonIntIsNullCheck(object, "CATEGORY"));

                                        etcItemArrayList.add(etcItem);
                                    }
                                }
                            }
                        }
                    }



                    for (int i = mSize; i < koList.size(); i++) {
                        Log.i(TAG,"register11 : " + registerDtList.get(i).substring(0, 10));
                        if (i != 0) {
                            if (registerDtList.get(i - 1).substring(0, 10).equals(registerDtList.get(i).substring(0, 10))) {
//                                Log.i(TAG,"register22 : " + registerDtList.get(i).substring(0, 10));
//                                mList.get(headerIndex).setHistoryItemList(finalMap.get);
                                mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                            } else {
                                headerIndex = i;
                                mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10))));
                                mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                            }
                        } else {
                            if (mList.size() > 0) {
                                if (mList.get(mList.size() - 1).getEtcItem() != null) {
                                    if (mList.get(mList.size() - 1).getEtcItem().getRegisterDt().contains(registerDtList.get(i).substring(0, 10))) {
//                                        Log.i(TAG,"register33 : " + registerDtList.get(i).substring(0, 10));
                                        mList.get(headerIndex).setHistoryItemList(finalMap.get(headerIndex));
                                        mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                                    }
                                }
                            } else {
                                headerIndex = i;
                                mList.add(new RecyclerViewItemList(registerDtList.get(i), RecyclerViewItemList.HEADER_TYPE, finalMap.get(registerDtList.get(i).substring(0, 10))));
                                mList.add(new RecyclerViewItemList(koList.get(i), etcItemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                            }
                        }
                    }

                    if (paging == searchTotalPage){
                        mList.add(new RecyclerViewItemList(RecyclerViewItemList.BOTTOM_INFO));
                    }

                    if (paging == 1) {
                        mSize = registerDtList.size();
                        adapter = new RecyclerViewAdapter(getActivity(), HomeFragment.this, width,typeface,typeface1);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
                        adapter.addItem(mList);
                        recyclerView.setAdapter(adapter);
                    }else{
                        mSize = registerDtList.size();
                        adapter.addItem(mList);
                    }

                    adapter.notifyDataSetChanged();

                    if (adapter.getItemCount() == 1) {
                        relativeLayout.setVisibility(View.GONE);
                        linNoList.setVisibility(View.VISIBLE);
                    } else {
                        relativeLayout.setVisibility(View.VISIBLE);
                        linNoList.setVisibility(View.GONE);
                    }

                    mLockScrollView = true;
                    progressDialog.dismiss();

                } catch (JSONException e) {

                }
            }else{
                mLockScrollView = true;
                progressDialog.dismiss();
            }
        }
    }

    public HashMap<String, ArrayList<HistoryItems>> setupHistoryDataWithRegiDt(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt())) {
                map.get(datas.get(i).getRegisterDt()).add(datas.get(i));
            } else {
                map.put(datas.get(i).getRegisterDt(), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt()).add(datas.get(i));
            }
        }

        return map;
    }

    public HashMap<String, ArrayList<HistoryItems>> setupHistoryData(ArrayList<HistoryItems> datas) {
        HashMap<String, ArrayList<HistoryItems>> map = new HashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            if (map.containsKey(datas.get(i).getRegisterDt().substring(0, 10))) {
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            } else {
                map.put(datas.get(i).getRegisterDt().substring(0, 10), new ArrayList<HistoryItems>());
                map.get(datas.get(i).getRegisterDt().substring(0, 10)).add(datas.get(i));
            }
        }

        return map;
    }

    public class SelectBannerListNetWork extends AsyncTask<String, String, String> {

        int status;
        String mUrl;

        public SelectBannerListNetWork(int status) {
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            FormBody.Builder body;

            body = (new FormBody.Builder());

            if (status == 2){
                body.add("USER_NO",String.valueOf(userItem.getUserNo()));
                mUrl = Utils.Server.selectUnreadMessage();
            }else if (status == 1){
                mUrl = Utils.Server.selectBannerList();
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, mUrl, body.build());
                Log.i(TAG,"aaaaa : " + response);
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

                    if (status == 1){

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


                        viewPagerAdapter = new ViewPagerAdapter(getContext(), bannerItemArrayList);

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
                    }else if (status == 2){
                        if (JsonIsNullCheck(jsonObject,"FLAG").equals("2")) {
                            alarmImageView.setImageResource(R.drawable.ic_alarm_on);
                        }else {
                            alarmImageView.setImageResource(R.drawable.ic_alarm_off);
                        }
                    }
                } catch (JSONException e) {

                }
            }
        }
    }

    public class LoginProcessNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stEmail = pref.getString("USER_ID", "");
            String stPassword = pref.getString("USER_PASSWORD", "");
            String stToken = pref.getString("DEVICE_CODE", "");

            FormBody body;

            if (pref.getInt("LOGIN_TYPE", 0) == 1 || pref.getInt("LOGIN_TYPE", 0) == 0) {
                body = (new FormBody.Builder()).add("ID", stEmail).add("PASSWORD", stPassword).add("DEVICE_CODE", stToken).add("OS_TYPE", "1").build();
            } else {
                body = (new FormBody.Builder()).add("ID", stEmail).add("DEVICE_CODE", stToken).add("OS_TYPE", "1").build();
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body);

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
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.length() > 0) {

                        JSONObject object = jsonArray.getJSONObject(0);
                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            UserItem userItem = new UserItem();

                            userItem.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                            userItem.setLv(JsonIntIsNullCheck(object, "LV"));
                            userItem.setId(JsonIsNullCheck(object, "ID"));
                            userItem.setEmail(JsonIsNullCheck(object, "EMAIL"));
                            userItem.setPassword(JsonIsNullCheck(object, "PASSWORD"));
                            userItem.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                            userItem.setName(JsonIsNullCheck(object, "NAME"));
                            userItem.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                            userItem.setGender(JsonIntIsNullCheck(object, "GENDER"));
                            userItem.setDiagnosisFlag(JsonIntIsNullCheck(object, "DIAGNOSIS_FLAG"));  // 확진 여부
                            userItem.setOutbreakDt(JsonIsNullCheck(object, "OUTBREAK_DT")); // 발병일
                            userItem.setProfileImg(JsonIsNullCheck(object, "PROFILE_IMG"));  // 프로필 사진
                            userItem.setDeviceCode(JsonIsNullCheck(object, "DEVICE_CODE"));  // fcm토큰
                            userItem.setLoginType(JsonIntIsNullCheck(object, "LOGIN_TYPE"));  //  1: 일반 이메일, 2 : 네이버, 3 : 카카오, 4 : 애플
                            userItem.setOsType(JsonIntIsNullCheck(object, "OS_TYPE")); // 1 : android, 2 : ios
                            userItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                            userItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                            userItem.setPhone(JsonIsNullCheck(object, "PHONE"));
                            userItem.setEssentialPermissionFlag(JsonIntIsNullCheck(object,"ESSENTIAL_PERMISSION_FLAG"));
                            userItem.setMarketingPermissionFlag(JsonIntIsNullCheck(object,"MARKETING_PERMISSION_FLAG"));

                            Utils.userItem = userItem;

                            editor.putString("DEVICE_CODE", JsonIsNullCheck(object, "DEVICE_CODE"));
                            editor.putString("USER_ID", JsonIsNullCheck(object, "ID"));
                            editor.putString("USER_PASSWORD", JsonIsNullCheck(object, "PASSWORD"));
                            editor.putInt("LOGIN_TYPE", JsonIntIsNullCheck(object, "LOGIN_TYPE"));
                            editor.putInt("OS_TYPE", JsonIntIsNullCheck(object, "OS_TYPE"));

                            editor.putInt("IS_LOGIN_FLAG", 1);  // 1 : 로그인 됨
                            editor.apply();

//                            try {
//                                txtPeriod.setText("숨 관리, " + (calDateBetweenAandB(format1.format(format2.parse(Utils.userItem.getCreateDt())), format1.format(new Date(System.currentTimeMillis()))) + 1) + "일차");
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }

//                        new SelectHomeFeedHistoryListNetWork().execute("1");
                        } else {

                        }
                    } else {

                    }
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
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), MedicinRecordActivity.class);
                startActivityForResult(i, 1111);
                break;
            }
            case R.id.lin_home_symptom: {
                REQUEST_CODE = 1111;
//                Intent i = new Intent(getActivity(), SymptomRecord.class);
                Intent i = new Intent(getActivity(), NewSymptomRecordActivity.class);
                startActivityForResult(i, 1111);
                break;
            }
            case R.id.lin_home_breath: {
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), BreathRecordActivity.class);
                startActivityForResult(i, 1111);
                break;
            }
            case R.id.lin_home_memo: {
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), MemoActivity.class);
                startActivityForResult(i, 1111);
                break;
            }
            case R.id.lin_home_measure: {
                measureBottomSheetDialog.show();
                break;
            }
            case R.id.asthma_control_button_fragment_measure_dialog: {
//                if (!categoryAct) {
//                    REQUEST_CODE = 1111;
//                    Intent i = new Intent(getActivity(), AsthmaControlActivity.class);
//                    i.putExtra("homeFragment", true);
//                    startActivityForResult(i, 1111);
//                } else {
//                    new OneButtonDialog(getActivity(), "1일 검사 횟수 초과", "이미 검사를 완료하셨습니다.\n천식조절검사(ACT)는\n하루에 한번 할 수 있습니다.", "확인");
//                }
                new ActCheckNetwork().execute();
                break;
            }
            case R.id.asthma_percent_button_fragment_measure_dialog : {
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), AstmaPercentActivity.class);
                startActivityForResult(i, 1111);
                break;
            }
            case R.id.dust_button_fragment_measure_dialog: {
                REQUEST_CODE = 1111;
                Intent i = new Intent(getActivity(), DustRecordActivity.class);
                startActivityForResult(i, 1111);
                break;
            }
            case R.id.lin_home_feed_bottom_info_parent : {
//                Intent intent = new Intent(getActivity(), GuideActivity.class);
//                startActivityForResult(intent, 9999);

                for (int i = 0; i < Utils.linkKeys.size(); i++) {
                    if (Utils.linkKeys.get(i).getTitle().equals("guide_user")) {
                        Log.i(TAG,"link : " + Utils.linkKeys.get(i).getLinkUrl());
                        if (Utils.linkKeys.get(i).getLinkUrl() == null || Utils.linkKeys.get(i).getLinkUrl().length() == 0) {
                            Intent intent = new Intent(getActivity(), GuideActivity.class);
                            startActivityForResult(intent, 9999);
                            break;
                        }else{
                            Intent intent = new Intent(getActivity(), UrlWebViewActivity.class);
                            intent.putExtra("url",Utils.linkKeys.get(i).getLinkUrl());
                            startActivity(intent);
                            break;
                        }
                    }
                }
                break;
            }
            case R.id.chb_agree_all : {
                if (!mChbAgreeAll.isChecked()){
                    mChbAgreeService.setChecked(false);
                    mChbAgreePersonal.setChecked(false);
                    mChbAgreeSensitivity.setChecked(false);
                    mChbAgreeMarketing.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    mChbAgreeService.setChecked(true);
                    mChbAgreePersonal.setChecked(true);
                    mChbAgreeSensitivity.setChecked(true);
                    mChbAgreeMarketing.setChecked(true);
                    mBtnAgreeDone.setEnabled(true);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                }
                break;
            }
            case R.id.chb_agree_service: {
                if (!mChbAgreeService.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreePersonal.isChecked() && mChbAgreeSensitivity.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_personal : {
                if (!mChbAgreePersonal.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreeSensitivity.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_sensitivity : {
                if (!mChbAgreeSensitivity.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setEnabled(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreePersonal.isChecked()){
                        if (mChbAgreeMarketing.isChecked()){
                            mChbAgreeAll.setChecked(true);
                            mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                        }
                        mBtnAgreeDone.setEnabled(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                    }
                }
                break;
            }
            case R.id.chb_agree_marketing : {
                if (!mChbAgreeMarketing.isChecked()){
                    mChbAgreeAll.setChecked(false);
                    mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.dbdbdb));
                    mLinAgreeAll.setBackgroundResource(R.drawable.edit_text_border);
                }else{
                    if (mChbAgreeService.isChecked() && mChbAgreeSensitivity.isChecked() && mChbAgreePersonal.isChecked()){
                        mChbAgreeAll.setChecked(true);
                        mBtnAgreeDone.setBackgroundTintList(getResources().getColorStateList(R.color.color09D182));
                        mLinAgreeAll.setBackgroundResource(R.drawable.agree_check_all_bg);
                    }
                }
                break;
            }
            case R.id.img_agree_move_service : {
                Intent i = new Intent(getActivity(),AgreeActivity.class);
                i.putExtra("flag",1);
                startActivity(i);
                break;
            }
            case R.id.img_agree_move_personal : {
                Intent i = new Intent(getActivity(),AgreeActivity.class);
                i.putExtra("flag",2);
                startActivity(i);
                break;
            }
            case R.id.img_agree_move_sensitivity : {
                Intent i = new Intent(getActivity(),AgreeActivity.class);
                i.putExtra("flag",3);
                startActivity(i);
                break;
            }
            case R.id.btn_agree_done : {
                if (mChbAgreeMarketing.isChecked()){
                    mAgreeBottomSheetDialog.dismiss();
                    userItem.setEssentialPermissionFlag(1);
                    userItem.setMarketingPermissionFlag(1);
                    MarketingOneBtnPopup(getActivity());
                }else{
                    //memo 약관동의 url
                    mAgreeBottomSheetDialog.dismiss();
                    userItem.setEssentialPermissionFlag(1);
                    NetworkCall(AGREE_UPDATE);
                }
                break;
            }
            case R.id.lin_home_feed_shared : {
                Log.i(TAG,"position!!!!!");
                Intent i = new Intent(getActivity(), CommunityWriteActivity.class);
                i.putExtra("sharedData", createSharedText(mList.get(FEED_SHARED_POSITION).getHistoryItemList()));
                i.putExtra("hashTagList", Utils.tagList);
//                i.putExtra("menuNo","1");
//                i.putExtra("cMenuNo","18");
//                i.putExtra("menuTitle","오늘의 숨");
//                i.putExtra("cMenuTitle","기록공유");
                startActivity(i);
                break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (measureBottomSheetDialog.isShowing()) {
            measureBottomSheetDialog.dismiss();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            new SelectBannerListNetWork(2).execute();
            if (filterTextList != null){
                if (filterTextList.size() != 13){
                    try {
                        filterTextList = new ArrayList<>();

                        filterTextList.add("1");
                        filterTextList.add("11");
                        filterTextList.add("12");
                        filterTextList.add("13");
                        filterTextList.add("14");
                        filterTextList.add("15");
                        filterTextList.add("16");
                        filterTextList.add("21");
                        filterTextList.add("22");
                        filterTextList.add("23");
                        filterTextList.add("24");
                        filterTextList.add("30");
                        filterTextList.add("40");
                        filterImageView.setImageResource(R.drawable.ic_filter_off);

                        paging = 1;
                        searchTotalPage = 0;
                        mLockScrollView = false;
                        categoryAct = false;

                        mSize = 0;
                        mList = new ArrayList<>();
                        hisItems = new ArrayList<>();
                        hisItemsRegisterDt = new ArrayList<>();
                        registerDtList = new ArrayList<>();
                        koList = new ArrayList<>();
                        etcItemArrayList = new ArrayList<>();
                        AllRegisterDtList = new ArrayList<>();
                        AllKoList = new ArrayList<>();
                        AllEtcItemArrayList = new ArrayList<>();

                        new SelectHomeFeedHistoryListNetWork().execute("1");
                    }catch (Exception e){

                    }
                }
            }
        }
        if (requestCode == 1111) {
            if (resultCode == RESULT_OK) {

                Log.i(TAG,"dddd");

                paging = 1;
                searchTotalPage = 0;
                mLockScrollView = false;
                categoryAct = false;

                filterTextList = new ArrayList<>();

                filterTextList.add("1");
                filterTextList.add("11");
                filterTextList.add("12");
                filterTextList.add("13");
                filterTextList.add("14");
                filterTextList.add("15");
                filterTextList.add("16");
                filterTextList.add("21");
                filterTextList.add("22");
                filterTextList.add("23");
                filterTextList.add("24");
                filterTextList.add("30");
                filterTextList.add("40");

                filterImageView.setImageResource(R.drawable.ic_filter_off);

                mSize = 0;
                mList = new ArrayList<>();
                hisItems = new ArrayList<>();
                hisItemsRegisterDt = new ArrayList<>();
                registerDtList = new ArrayList<>();
                koList = new ArrayList<>();
                etcItemArrayList = new ArrayList<>();
                AllRegisterDtList = new ArrayList<>();
                AllKoList = new ArrayList<>();
                AllEtcItemArrayList = new ArrayList<>();

                new SelectHomeFeedHistoryListNetWork().execute("1");
            }
        } else if (requestCode == 2222) {
            if (resultCode == RESULT_OK) {
                paging = 1;
                searchTotalPage = 0;
                mLockScrollView = false;
                categoryAct = false;

                if (filterTextList.size() != 13){
                    filterImageView.setImageResource(R.drawable.ic_filter_on);
                }else{
                    filterImageView.setImageResource(R.drawable.ic_filter_off);
                }

                mSize = 0;
                mList = new ArrayList<>();
                hisItems = new ArrayList<>();
                hisItemsRegisterDt = new ArrayList<>();
                registerDtList = new ArrayList<>();
                koList = new ArrayList<>();
                etcItemArrayList = new ArrayList<>();
                AllRegisterDtList = new ArrayList<>();
                AllKoList = new ArrayList<>();
                AllEtcItemArrayList = new ArrayList<>();

                new SelectHomeFeedHistoryListNetWork().execute("1");
            }
        }else if (requestCode == 9999){
            if (resultCode == RESULT_OK){
                new ActCheckNetwork().execute();
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(AGREE_SELECT)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+ userItem.getUserNo());
        }else if (mCode.equals(AGREE_UPDATE)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+ userItem.getUserNo(),mChbAgreeSensitivity.isChecked() == true ? "1" : "-1",mChbAgreeMarketing.isChecked() == true ? "1" : "-1");
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(AGREE_SELECT)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    JSONObject object = jsonArray.getJSONObject(0);

                    userItem.setEssentialPermissionFlag(JsonIntIsNullCheck(object,"ESSENTIAL_PERMISSION_FLAG"));
                    userItem.setMarketingPermissionFlag(JsonIntIsNullCheck(object,"MARKETING_PERMISSION_FLAG"));

                    if (JsonIntIsNullCheck(object,"ESSENTIAL_PERMISSION_FLAG") < 1){
                        AgreePopUp();
                    }else{
//                        if (pref.getInt("homeCoachMark1",0) == 0 || pref.getInt("homeCoachMark2",0) == 0){
//                            Intent i = new Intent(getActivity(), HomeCoachMarkActivity.class);
//                            getActivity().overridePendingTransition(0,0);
//                            startActivity(i);
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (mCode.equals(AGREE_UPDATE)){
//                if (pref.getInt("homeCoachMark1",0) == 0 || pref.getInt("homeCoachMark2",0) == 0){
//                    Intent i = new Intent(getActivity(), HomeCoachMarkActivity.class);
//                    getActivity().overridePendingTransition(0,0);
//                    startActivity(i);
//                }
            }
        }
    }

    private String dayChoice(Calendar calendar){

        String day = "";

        if (calendar.get(Calendar.DAY_OF_WEEK) == 1){
            day = "일";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 2){
            day = "월";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 3){
            day = "화";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 4){
            day = "수";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 5){
            day = "목";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 6){
            day = "금";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 7){
            day = "토";
        }
        return day;
    }

//    private String[] causes = new String[]{"감기", "미세먼지", "찬바람", "운동", "집먼지진드기", "애완동물", "음식알러지", "꽃가루", "스트레스", "담배연기", "요리연기", "강한냄새", "건조함", "곰팡이", "높은습도", "모르겠어요"};
    private String[] causes = new String[]{"감기", "미세먼지", "찬바람", "운동", "집먼지진드기", "애완동물", "음식알러지", "꽃가루", "스트레스", "담배연기", "요리연기", "기타", "강한냄새", "건조함", "곰팡이", "높은습도"};

    public String createSharedText(TreeMap<String, ArrayList<HistoryItems>> items) {

        String sharedText = "";

        TreeMap<String, ArrayList<HistoryItems>> tree = new TreeMap<>(items);

        for (Map.Entry<String, ArrayList<HistoryItems>> entry1 : tree.descendingMap().entrySet()) {
            ArrayList<HistoryItems> value1 = entry1.getValue();
            for (int aa = 0; aa < value1.size(); aa++){
                String[] categorys = value1.get(aa).getCategorySplit().split(",");
                if (categorys.length > 1){
                    for (int x = 0; x < categorys.length; x++){
                        if (categorys[x].equals("11")){
                            if (x == 0){
                                sharedText += "[기침";
                            }else if (x == categorys.length-1){
                                sharedText += ", 기침]\n";
                            }else{
                                sharedText += ", 기침";
                            }
                        }else if (categorys[x].equals("12")){
                            if (x == 0){
                                sharedText += "[숨쉬기 불편";
                            }else if (x == categorys.length-1){
                                sharedText += ", 숨쉬기 불편]\n";
                            }else{
                                sharedText += ", 숨쉬기 불편";
                            }
                        }else if (categorys[x].equals("13")){
                            if (x == 0){
                                sharedText += "[천명음";
                            }else if (x == categorys.length-1){
                                sharedText += ", 천명음]\n";
                            }else{
                                sharedText += ", 천명음";
                            }
                        }else if (categorys[x].equals("14")){
                            if (x == 0){
                                sharedText += "[가슴답답";
                            }else if (x == categorys.length-1){
                                sharedText += ", 가슴답답]\n";
                            }else{
                                sharedText += ", 가슴답답";
                            }
                        }else if (categorys[x].equals("15")){
                            if (x == 0){
                                sharedText += "[기타증상";
                            }else if (x == categorys.length-1){
                                sharedText += ", 기타증상]\n";
                            }else{
                                sharedText += ", 기타증상";
                            }
                        }else if (categorys[x].equals("16")){
                            if (x == 0){
                                sharedText += "[가래";
                            }else if (x == categorys.length-1){
                                sharedText += ", 가래]\n";
                            }else{
                                sharedText += ", 가래";
                            }
                        }
                    }

                    if (value1.get(aa).getCause().length() != 0) {

                        String[] causeList = value1.get(aa).getCause().split(",");
//                        int[] causeList = new int[causeList1.length];
//                        for (int x = 0; x < causeList1.length; x++) {
//                            causeList[x] = Integer.parseInt(causeList1[x]);
//                        }
//                        Arrays.sort(causeList);
                        String cause = "";
                        for (int j = 0; j < causeList.length; j++) {
                            Log.i(TAG,"cause : " + causeList[j]);
                            if (j == 0){
                                cause += "원인 : " + causes[Integer.parseInt(causeList[j])];
                            }else{
                                cause += ", " + causes[Integer.parseInt(causeList[j])];
                            }
                        }

                        if (items.size() == 1){
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += "" + cause + "\n메모 내용 : " + value1.get(aa).getMemo();
                            }else{
                                sharedText += "" + cause;
                            }
                        }else{
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += "" + cause + "\n메모 내용 : " + value1.get(aa).getMemo() + "\n\n";
                            }else{
                                sharedText += "" + cause + "\n\n";
                            }
                        }
                    }

                }else{
                    if (value1.get(aa).getCategory() == 11 || value1.get(aa).getCategory() == 12 || value1.get(aa).getCategory() == 13 ||
                            value1.get(aa).getCategory() == 14 || value1.get(aa).getCategory() == 15 || value1.get(aa).getCategory() == 16 ||
                            value1.get(aa).getCategory() == 40) {

                        String category = String.valueOf(value1.get(aa).getCategory());

                        if (category.equals("11")) {
                            sharedText += "[기침]\n";
                        } else if (category.equals("12")) {
                            sharedText += "[숨쉬기 불편]\n";
                        } else if (category.equals("13")) {
                            sharedText += "[천명음]\n";
                        } else if (category.equals("14")) {
                            sharedText += "[가슴답답]\n";
                        } else if (category.equals("15")){
                            sharedText += "[기타증상]\n";
                        } else if (category.equals("16")){
                            sharedText += "[가래]\n";
                        }else if (category.equals("40")){
                            if (value1.get(aa).getMemo().length() == 0){
                                sharedText += "[증상없음]\n\n";
                            }else{
                                sharedText += "[증상없음]\n";
                                sharedText += "메모 내용 : " + value1.get(aa).getMemo() + "\n\n";
                            }
                        }

                        if (value1.get(aa).getCause().length() != 0) {
                            String[] causeList = value1.get(aa).getCause().split(",");
//                            int[] causeList = new int[causeList1.length];
//                            for (int x = 0; x < causeList1.length; x++) {
//                                causeList[x] = Integer.parseInt(causeList1[x]);
//                            }
//                            Arrays.sort(causeList);
                            String cause = "";
                            for (int j = 0; j < causeList.length; j++) {
                                if (j == 0){
                                    cause += "원인 : " + causes[Integer.parseInt(causeList[j])];
                                }else{
                                    cause += ", " + causes[Integer.parseInt(causeList[j])];
                                }
                            }

                            if (items.size() == 1){
                                if (value1.get(aa).getMemo().length() != 0){
                                    sharedText += "" + cause + "\n메모 내용 : " + value1.get(aa).getMemo();
                                }else{
                                    sharedText += "" + cause;
                                }
                            }else{
                                if (value1.get(aa).getMemo().length() != 0){
                                    sharedText += "" + cause + "\n메모 내용 : " + value1.get(aa).getMemo() + "\n\n";
                                }else{
                                    sharedText += "" + cause + "\n\n";
                                }
                            }
                        }
                    }else if (value1.get(aa).getCategory() == 21) {
                        sharedText += "[천식조절 검사]\n";
                        if (value1.get(aa).getActScore() != 0) {
                            if (value1.get(aa).getActState() == 1) {
                                sharedText += "점수 : " + value1.get(aa).getActScore() + " / 조절(양호)\n결과 : 천식 증상이 잘 조절되고 있습니다.";
                                if (items.size() == 1){

                                }else{
                                    sharedText += "\n\n";
                                }
                            } else if (value1.get(aa).getActState() == 2) {
                                sharedText += "점수 : " + value1.get(aa).getActScore() + " / 부분조절(주의)\n결과 : 증상이 부분적으로만 조절되고 있습니다.";
                                if (items.size() == 1){

                                }else{
                                    sharedText += "\n\n";
                                }
                            } else {
                                sharedText += "점수 : " + value1.get(aa).getActScore() + " / 조절안됨(위험)\n결과 : 천식 증상이 조절되지 않고 있습니다.";
                                if (items.size() == 1){

                                }else{
                                    sharedText += "\n\n";
                                }
                            }
                        }
                    } else if (value1.get(aa).getCategory() == 22) {
                        sharedText += "[폐기능]\n";

                        sharedText += "측정값 : " + value1.get(aa).getPefScore() + "\n";
                        if (value1.get(aa).getInspiratorFlag() == 1) {
                            sharedText += "흡입기 복약여부 : 흡입기 사용";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        } else {
                            sharedText += "흡입기 복약여부 : 흡입기 미사용";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        }
                    } else if (value1.get(aa).getCategory() == 23) {

                        sharedText += "[미세먼지]\n";

                        if (value1.get(aa).getDust() != 0 && value1.get(aa).getDustState() != 0 && value1.get(aa).getUltraDust() != 0 && value1.get(aa).getUltraDustState() != 0) {
                            String dustState = "";
                            if (value1.get(aa).getDustState() == 1) {
                                dustState = "좋음";
                            } else if (value1.get(aa).getDustState() == 2) {
                                dustState = "보통";
                            } else if (value1.get(aa).getDustState() == 3) {
                                dustState = "나쁨";
                            } else if (value1.get(aa).getDustState() == 4) {
                                dustState = "매우나쁨";
                            }
                            String ultraDustState = "";
                            if (value1.get(aa).getUltraDustState() == 1) {
                                ultraDustState = "좋음";
                            } else if (value1.get(aa).getUltraDustState() == 2) {
                                ultraDustState = "보통";
                            } else if (value1.get(aa).getUltraDustState() == 3) {
                                ultraDustState = "나쁨";
                            } else if (value1.get(aa).getUltraDustState() == 4) {
                                ultraDustState = "매우나쁨";
                            }
                            sharedText += "위치 : " + value1.get(aa).getLocation() + "\n미세먼지 : " + value1.get(aa).getDust() + " / " + dustState + "\n초미세먼지 : "
                                    + value1.get(aa).getUltraDust() + " / " + ultraDustState + "";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        } else {
                            sharedText += "위치 : " + value1.get(aa).getLocation() + "";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        }
                    }else if (value1.get(aa).getCategory() == 24){
                        sharedText += "[천식폐질환 가능지수]\n";

                        sharedText += "위치 : " + value1.get(aa).getLocation() + "\n";

                        String dustState = "";
                        if (value1.get(aa).getAsthmaScore() == 1) {
                            dustState = "보통";
                        } else if (value1.get(aa).getAsthmaScore() == 2) {
                            dustState = "높음";
                        } else if (value1.get(aa).getAsthmaScore() == 3) {
                            dustState = "매우높음";
                        } else {
                            dustState = "낮음";
                        }

                        sharedText += "지수 : " + dustState;
                        if (items.size() == 1){

                        }else{
                            sharedText += "\n\n";
                        }
                    }
                    else if (value1.get(aa).getCategory() == 30) {
                        sharedText += "[메모]\n";
                        if (items.size() == 1){
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += "내용 : " + value1.get(aa).getMemo();
                            }else{
                                sharedText += "";
                            }
                        }else{
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += "내용 : " + value1.get(aa).getMemo() + "\n\n";
                            }else{
                                sharedText += "\n\n";
                            }
                        }
                    }else {

                    }
                }
            }
            if (value1.get(0).getCategory() == 1){
                sharedText += "[복약]\n";
                String[] test = value1.stream().map(s->s.getKo()).collect(Collectors.joining("\n")).split("\n");
                String basicText = "일반 복용 : ";
                String emergencyText = "응급 복용 : ";
                for (int p = 0; p < test.length; p++){
                    if (test[p].contains("[응급]")){
                        emergencyText += test[p].replace("[응급]","") + " ";
                    }else{
                        basicText += test[p] + " ";
                    }
                }

                if (basicText.length() > 8){
                    sharedText += basicText;
                }

                if (emergencyText.length() > 8){
                    if (basicText.length() > 8){
                        sharedText += "\n";
                    }
                    sharedText += emergencyText;
                }

                if (value1.get(0).getMemo().toString().length() > 0){
                    sharedText += "\n메모 내용 : " + value1.get(0).getMemo();
                }

                sharedText += "\n\n";
            }
        }
        return sharedText;
    }

    private Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }
}
