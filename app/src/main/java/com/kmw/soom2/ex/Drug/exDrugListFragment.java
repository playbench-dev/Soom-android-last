package com.kmw.soom2.ex.Drug;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
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

import com.kmw.soom2.Common.BusProvider;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Adapters.CommunityBannerAdapter;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.HomeAdapter.ViewPagerAdapter;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_BANNER;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_LIST_BY_DATE_CURRENT;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_BANNER_LIST;

public class exDrugListFragment extends Fragment implements AsyncResponse {

    private String TAG = "exDrugListFragment";
    LinearLayout linCurrentList;
    LinearLayout linCurrentPlus;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView listTest;
    exDrugListAdapter newDrugAdapter;

    private ViewPager viewPager;
    private TextView txtPagerCnt;
    private TextView txtPagerTotalCnt;
    private LinearLayout linPagerCntParent;
    private CommunityBannerAdapter viewPagerAdapter;

    private Handler handler = new Handler();

    public exDrugListFragment() {

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
        View view = inflater.inflate(R.layout.ex_fragment_drug_list, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_drug_list);
        linCurrentPlus = (LinearLayout) view.findViewById(R.id.lin_drug_current_plus);
        linCurrentList = (LinearLayout)view.findViewById(R.id.lin_drug_current_parent);
        listTest = (RecyclerView)view.findViewById(R.id.list_drug_test);
        viewPager                   = view.findViewById(R.id.view_pager_community_banner);
        txtPagerCnt                 = view.findViewById(R.id.txt_community_banner_pager_cnt);
        txtPagerTotalCnt            = view.findViewById(R.id.txt_community_banner_pager_total_cnt);
        linPagerCntParent           = view.findViewById(R.id.lin_pager_community_banner_cnt_parent);

        linCurrentPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MedicineSelectActivity.class);
                i.putExtra("guest",true);
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

        listTest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        newDrugAdapter = new exDrugListAdapter(getActivity());

        MedicineTakingItem medicineTakingItem = new MedicineTakingItem();
        medicineTakingItem.setMedicineKo("기침약");
        medicineTakingItem.setFrequency(1);
        newDrugAdapter.addItem(medicineTakingItem);
        MedicineTakingItem medicineTakingItem1 = new MedicineTakingItem();
        medicineTakingItem1.setMedicineKo("스테로이드제");
        medicineTakingItem1.setFrequency(0);
        newDrugAdapter.addItem(medicineTakingItem1);
        MedicineTakingItem medicineTakingItem2 = new MedicineTakingItem();
        medicineTakingItem2.setMedicineKo("진해거담제");
        medicineTakingItem2.setFrequency(1);
        newDrugAdapter.addItem(medicineTakingItem2);

        listTest.setAdapter(newDrugAdapter);

        NetworkCall(MEDICINE_BANNER);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    void NetworkCall(String mCode){
        if (mCode.equals(MEDICINE_BANNER)){
            new NetworkUtils.NetworkCall(getActivity(),this,TAG,mCode).execute();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            try {
                JSONObject jsonObject = new JSONObject(mResult);
                if (mCode.equals(MEDICINE_BANNER)){
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
                            viewPagerAdapter = new CommunityBannerAdapter(getActivity(), MEDICINE_BANNER_LIST,true);
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
                }
            }catch (JSONException e){

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
}

