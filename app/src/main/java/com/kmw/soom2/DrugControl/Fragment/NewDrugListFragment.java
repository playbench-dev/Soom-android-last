package com.kmw.soom2.DrugControl.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.kmw.soom2.Common.ActivityResultEvent;
import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Adapters.CommunityBannerAdapter;
import com.kmw.soom2.DrugControl.Adapter.NewDrugAdapter;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_BANNER;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_LIST_BY_DATE_CURRENT;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_BANNER_LIST;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.preferencesName;

public class NewDrugListFragment extends Fragment implements AsyncResponse{

    private String TAG = "DrugListFragment";
    LinearLayout linComplete,linCurrentList;
    LinearLayout linCurrentPlus;
    LinearLayout linNoItem;
    Toolbar toolbarListTitle;

    SwipeRefreshLayout swipeRefreshLayout;
    TextView txtDrugComplete;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");
    RecyclerView listTest;

//    DrugListRecyclerViewAdapter drugListTestAdapter;
    NewDrugAdapter newDrugAdapter;

    int pageNo = 1;
    boolean mLockScrollView = false;
    int search_total_page;

    private ProgressDialog progressDialog;
    SharedPreferences pref;

    private ViewPager viewPager;
    private TextView txtPagerCnt;
    private TextView txtPagerTotalCnt;
    private LinearLayout linPagerCntParent;
    private CommunityBannerAdapter viewPagerAdapter;

    private Handler handler = new Handler();

    public ArrayList<MedicineTakingItem> MedicineTakingItems;

    public NewDrugListFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_list_new, container, false);

        pref = getActivity().getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_drug_list);
        linCurrentPlus = (LinearLayout) view.findViewById(R.id.lin_drug_current_plus);
        linCurrentList = (LinearLayout)view.findViewById(R.id.lin_drug_current_parent);
        linNoItem = (LinearLayout)view.findViewById(R.id.lin_drug_list_no_item);
        toolbarListTitle = (Toolbar)view.findViewById(R.id.toolbar_drug_list);
        listTest = (RecyclerView)view.findViewById(R.id.list_drug_test);
        viewPager                   = view.findViewById(R.id.view_pager_community_banner);
        txtPagerCnt                 = view.findViewById(R.id.txt_community_banner_pager_cnt);
        txtPagerTotalCnt            = view.findViewById(R.id.txt_community_banner_pager_total_cnt);
        linPagerCntParent           = view.findViewById(R.id.lin_pager_community_banner_cnt_parent);

        NullCheck(getActivity());

        linCurrentPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MedicineSelectActivity.class);
                getActivity().startActivityForResult(i,1234);
            }
        });

        DisplayMetrics dm1 = getActivity().getResources().getDisplayMetrics();
        int width1 = dm1.widthPixels;

        viewPager.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (width1 * 0.402));
                viewPager.setLayoutParams(params);
                viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        progressDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
//        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
//        drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent),
//                PorterDuff.Mode.SRC_IN);
//        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setCancelable(false);
        progressDialog.show();

        NetworkCall(MEDICINE_BANNER);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                progressDialog.show();
                NetworkCall(MEDICINE_LIST_BY_DATE_CURRENT);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        listTest.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int lastVisibleItemPosition = ((LinearLayoutManager)listTest.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCnt = listTest.getAdapter().getItemCount();

                if (lastVisibleItemPosition == (itemTotalCnt - 1) && pageNo < search_total_page && mLockScrollView) {
                    mLockScrollView = false;
                    ++pageNo;
                    NetworkCall(MEDICINE_LIST_BY_DATE_CURRENT);
                }
            }
        });

        return view;
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onActivityResultEvent(@NonNull ActivityResultEvent event) {
        Log.i(TAG,"event code : " + event.getRequestCode());
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234){
            Log.i(TAG,"code 들어옴");
            if (resultCode == RESULT_OK){
                Log.i(TAG,"okkkkkkkkk");
            }
        }else if (requestCode == 2222){
            if (pref.getInt("medicineRefresh",0) == 1){
                pageNo = 1;
                progressDialog.show();
                NetworkCall(MEDICINE_LIST_BY_DATE_CURRENT);
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(MEDICINE_LIST_BY_DATE_CURRENT)){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(System.currentTimeMillis()));
            c.add(Calendar.DATE,1);
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute(""+ Utils.userItem.getUserNo(),new SimpleDateFormat("yyyyMMdd").format(c.getTime()),""+pageNo,"15");
        }else if (mCode.equals(MEDICINE_BANNER)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (mCode.equals(MEDICINE_LIST_BY_DATE_CURRENT)){

                search_total_page = JsonIntIsNullCheck(jsonObject,"Search_TotalPage");
                if (pageNo == 1){
                    newDrugAdapter = new NewDrugAdapter(getActivity());
                }
                if (jsonObject.getString("result").equals("N")){
                    progressDialog.dismiss();
                    linNoItem.setVisibility(View.VISIBLE);
                    toolbarListTitle.setVisibility(View.GONE);
                    linCurrentList.setVisibility(View.GONE);
                }
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                if (jsonArray.length() != 0){
                    linNoItem.setVisibility(View.GONE);
                    toolbarListTitle.setVisibility(View.VISIBLE);
                    linCurrentList.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject objectCls = object.getJSONObject("clsMedicineBean");
                    MedicineTakingItem medicineTakingItem = new MedicineTakingItem();
                    medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object,"MEDICINE_NO"));
                    medicineTakingItem.setMedicineHistoryNo(JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"));
                    medicineTakingItem.setMedicineKo(JsonIsNullCheck(object,"KO"));
                    medicineTakingItem.setStartDt(JsonIsNullCheck(object,"START_DT"));
                    medicineTakingItem.setEndDt(JsonIsNullCheck(object,"END_DT"));
                    medicineTakingItem.setFrequency(JsonIntIsNullCheck(object,"FREQUENCY"));
                    medicineTakingItem.setVolume(JsonIsNullCheck(object,"VOLUME"));
                    medicineTakingItem.setUnit(JsonIsNullCheck(object,"UNIT"));
                    medicineTakingItem.setEfficacy(JsonIsNullCheck(objectCls,"EFFICACY"));
                    medicineTakingItem.setInstructions(JsonIsNullCheck(objectCls,"INSTRUCTIONS"));
                    medicineTakingItem.setInformation(JsonIsNullCheck(objectCls,"INFORMATION"));
                    medicineTakingItem.setStabiltyRationg(JsonIsNullCheck(objectCls,"STABILITY_RATING"));
                    medicineTakingItem.setPrecaution(JsonIsNullCheck(objectCls,"PRECAUTIONS"));
                    medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object,"MEDICINE_TYPE_NO"));
                    medicineTakingItem.setMedicineImg("");

                    newDrugAdapter.addItem(medicineTakingItem);
                }
                if (pageNo == 1){
                    listTest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    listTest.setAdapter(newDrugAdapter);
                }
                newDrugAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                mLockScrollView = true;
            }else if (mCode.equals(MEDICINE_BANNER)){
                Log.i(TAG,"banner : " + mResult);
                if (jsonObject.has("list")){
                    MEDICINE_BANNER_LIST = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            BannerItem bannerItem = new BannerItem();
                            bannerItem.setBannerNo(JsonIsNullCheck(object, "BANNER_NO"));
                            bannerItem.setBannerType(JsonIsNullCheck(object, "BANNER_TYPE"));
                            bannerItem.setBannerLink(JsonIsNullCheck(object, "BANNER_LINK"));
                            bannerItem.setImageFile(JsonIsNullCheck(object, "IMAGE_FILE"));
                            bannerItem.setPriority(JsonIntIsNullCheck(object, "PRIORITY"));
                            bannerItem.setBannerTitle(JsonIsNullCheck(object,"BANNER_TITLE"));

                            MEDICINE_BANNER_LIST.add(bannerItem);
                        }
                    }
                    if (MEDICINE_BANNER_LIST.size() > 0){
                        Collections.sort(MEDICINE_BANNER_LIST, UpComparator);
                        viewPagerAdapter = new CommunityBannerAdapter(getActivity(), MEDICINE_BANNER_LIST);
                    }

                    if (MEDICINE_BANNER_LIST.size() > 0){
                        viewPager.setVisibility(View.VISIBLE);
                        linPagerCntParent.setVisibility(View.VISIBLE);
                        viewPager.setAdapter(viewPagerAdapter);
                        txtPagerCnt.setText("1");
                        txtPagerTotalCnt.setText("/"+MEDICINE_BANNER_LIST.size());
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                int pos = position % MEDICINE_BANNER_LIST.size();
                                txtPagerCnt.setText(""+(pos+1));
                                txtPagerTotalCnt.setText("/"+MEDICINE_BANNER_LIST.size());
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                                handler.postDelayed(this, 8000);
                            }
                        }, 8000);
                    }else{
                        viewPager.setVisibility(View.GONE);
                        linPagerCntParent.setVisibility(View.GONE);
                    }
                }
                NetworkCall(MEDICINE_LIST_BY_DATE_CURRENT);
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
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
}
