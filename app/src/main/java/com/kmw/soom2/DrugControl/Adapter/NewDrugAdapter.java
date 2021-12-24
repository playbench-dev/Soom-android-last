package com.kmw.soom2.DrugControl.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineInsertActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;

public class NewDrugAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "DrugListTestAdapter";
    ArrayList<MedicineTakingItem> medicineTakingItems = new ArrayList<>();
    Context context;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");
    Date startDate = null;

    public NewDrugAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.new_drug_list_item, parent, false);
        return new NewDrugAdapter.ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MedicineTakingItem item = medicineTakingItems.get(position);

        //memo 20210407 수정완료
        if (Utils.MEDICINE_TYPE_LIST != null){
            for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++){
                if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == medicineTakingItems.get(position).getMedicineTypeNo()){
                    if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0){
                        String replaceText = Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg();
                        if (replaceText.contains("soom2.testserver-1.com:8080")){
                            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                        }else if (replaceText.contains("103.55.190.193")){
                            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                        }
                        Glide.with(context).load("https:"+replaceText).into(((NewDrugAdapter.ViewHolderItem)holder).imgIcon);
                    }
                }
            }
        }

        ((NewDrugAdapter.ViewHolderItem)holder).txtName.setText(medicineTakingItems.get(position).getMedicineKo());

        if (medicineTakingItems.get(position).getFrequency() == 0 || medicineTakingItems.get(position).getFrequency() == -1){
            ((NewDrugAdapter.ViewHolderItem)holder).txtCnt.setText("필요시");
        }else{
            ((NewDrugAdapter.ViewHolderItem)holder).txtCnt.setText("하루 " + medicineTakingItems.get(position).getFrequency() + "번");
        }

        ((ViewHolderItem)holder).linMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MedicineInsertActivity.class);
                i.putExtra("medicineNo",medicineTakingItems.get(position).getMedicineNo());
                i.putExtra("historyNo",medicineTakingItems.get(position).getMedicineHistoryNo());
                i.putExtra("name",medicineTakingItems.get(position).getMedicineKo());
                i.putExtra("startDt",medicineTakingItems.get(position).getStartDt());
                i.putExtra("endDt",medicineTakingItems.get(position).getEndDt());
                i.putExtra("frequency",medicineTakingItems.get(position).getFrequency());
                i.putExtra("volume",medicineTakingItems.get(position).getVolume());
                i.putExtra("unit",medicineTakingItems.get(position).getUnit());
                i.putExtra("efficacy",medicineTakingItems.get(position).getEfficacy());
                i.putExtra("instructions",medicineTakingItems.get(position).getInstructions());
                i.putExtra("information",medicineTakingItems.get(position).getInformation());
                i.putExtra("stabilityRating",medicineTakingItems.get(position).getStabiltyRationg());
                i.putExtra("precautions",medicineTakingItems.get(position).getPrecaution());
                i.putExtra("medicineTypeNo",medicineTakingItems.get(position).getMedicineTypeNo());
                i.putExtra("medicineImg",medicineTakingItems.get(position).getMedicineImg());
                i.putExtra("medicinePosition",position);
                ((Activity)context).startActivityForResult(i,2222);
            }
        });

        ((ViewHolderItem)holder).linReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MedicineInsertActivity.class);
                i.putExtra("medicineNo",medicineTakingItems.get(position).getMedicineNo());
                i.putExtra("historyNo",medicineTakingItems.get(position).getMedicineHistoryNo());
                i.putExtra("name",medicineTakingItems.get(position).getMedicineKo());
                i.putExtra("startDt",medicineTakingItems.get(position).getStartDt());
                i.putExtra("endDt",medicineTakingItems.get(position).getEndDt());
                i.putExtra("frequency",medicineTakingItems.get(position).getFrequency());
                i.putExtra("volume",medicineTakingItems.get(position).getVolume());
                i.putExtra("unit",medicineTakingItems.get(position).getUnit());
                i.putExtra("efficacy",medicineTakingItems.get(position).getEfficacy());
                i.putExtra("instructions",medicineTakingItems.get(position).getInstructions());
                i.putExtra("information",medicineTakingItems.get(position).getInformation());
                i.putExtra("stabilityRating",medicineTakingItems.get(position).getStabiltyRationg());
                i.putExtra("precautions",medicineTakingItems.get(position).getPrecaution());
                i.putExtra("medicineTypeNo",medicineTakingItems.get(position).getMedicineTypeNo());
                i.putExtra("medicineImg",medicineTakingItems.get(position).getMedicineImg());
                i.putExtra("medicinePosition",position);
                i.putExtra("review",true);
                ((Activity)context).startActivityForResult(i,2222);
            }
        });
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
