package com.kmw.soom2.ex.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Home.Activitys.AdultActivitys.AdultCheckResultActivity;
import com.kmw.soom2.Home.Activitys.GuideActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AstmaPercentActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.DustRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewMedicineEditRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewSymptomRecordActivity;
import com.kmw.soom2.Home.HomeAdapter.RecyclerViewAdapter;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Item.HistoryItems;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.BOTTOM_INFO;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.EX_ITEM_01;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.EX_ITEM_02;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.EX_ITEM_03;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.EX_ITEM_04;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.EX_ITEM_05;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.HEADER_TYPE;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.ITEM_TYPE;

public class exItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "exItemAdapter";
    private ArrayList<RecyclerViewItemList> itemListArrayList = new ArrayList<>();
    Context context;

    public void addItem(ArrayList<RecyclerViewItemList> itemListArrayList){
        this.itemListArrayList = itemListArrayList;
    }

    public exItemAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case EX_ITEM_01:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_home_item_01, parent, false);
                return new exItemAdapter.ViewHolderexItem(view);
            case EX_ITEM_02:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_home_item_02, parent, false);
                return new exItemAdapter.ViewHolderexItem(view);
            case EX_ITEM_03:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_home_item_03, parent, false);
                return new exItemAdapter.ViewHolderexItem(view);
            case EX_ITEM_04:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_home_item_04, parent, false);
                return new exItemAdapter.ViewHolderexItem(view);
            case EX_ITEM_05:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ex_home_item_05, parent, false);
                return new exItemAdapter.ViewHolderexItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

    }

    @NonNull
    @Override
    public int getItemCount() {
        return 5;
    }

    private class ViewHolderexItem extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public ViewHolderexItem(@NonNull View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.lin_ex_item_corner);
            itemView.setTag(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewItemList item = itemListArrayList.get(position);
        if (item != null) {
            return item.getViewType();
        }
        return 0;
    }
}
