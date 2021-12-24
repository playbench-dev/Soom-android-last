package com.kmw.soom2.ex.Drug;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.DrugControl.Adapter.NewDrugAdapter;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineInsertActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class exDrugListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "DrugListTestAdapter";
    ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");
    Date startDate = null;

    public exDrugListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.new_drug_list_item, parent, false);
        return new exDrugListAdapter.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MedicineTakingItem item = medicineTakingItems.get(position);

        if (position == 0){
            Glide.with(context).load("https://soomcare.info/img/admin/1615865015877.png").into(((exDrugListAdapter.ViewHolderItem)holder).imgIcon);
        }else if (position == 1){
            Glide.with(context).load("https://soomcare.info/img/admin/1615865055198.png").into(((exDrugListAdapter.ViewHolderItem)holder).imgIcon);
        }else if (position == 2){
            Glide.with(context).load("https://soomcare.info/img/admin/1615945698310.png").into(((exDrugListAdapter.ViewHolderItem)holder).imgIcon);
        }

        ((exDrugListAdapter.ViewHolderItem)holder).txtName.setText(medicineTakingItems.get(position).getMedicineKo());

        if (medicineTakingItems.get(position).getFrequency() == 0 || medicineTakingItems.get(position).getFrequency() == -1){
            ((exDrugListAdapter.ViewHolderItem)holder).txtCnt.setText("필요시");
        }else{
            ((exDrugListAdapter.ViewHolderItem)holder).txtCnt.setText("하루 " + medicineTakingItems.get(position).getFrequency() + "번");
        }

    }

    @Override
    public int getItemCount() {
        return medicineTakingItems.size();
    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView txtName,txtCnt;
        ImageView imgIcon;
        LinearLayout linMove;
        LinearLayout linReview;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            linMove = (LinearLayout)itemView.findViewById(R.id.lin_new_drug_list_item_move);
            linReview = (LinearLayout)itemView.findViewById(R.id.lin_new_drug_list_item_review);
            imgIcon = (ImageView)itemView.findViewById(R.id.img_new_drug_list_item_icon);
            txtName = (TextView)itemView.findViewById(R.id.txt_new_drug_list_item_name);
            txtCnt = (TextView)itemView.findViewById(R.id.txt_new_drug_list_item_cnt);
            itemView.setTag(false);
        }
    }

    public void addItem(MedicineTakingItem medicineTakingItem){
        medicineTakingItems.add(medicineTakingItem);
    }
}