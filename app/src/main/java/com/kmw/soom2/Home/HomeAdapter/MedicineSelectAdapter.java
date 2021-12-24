package com.kmw.soom2.Home.HomeAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineInsertActivity;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.MedicineTakingItems;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.HEADER_TYPE;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.ITEM_TYPE;



public class MedicineSelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "RecyclerViewAdapter";
    private ArrayList<RecyclerViewItemList> arrayList;
    Context context;
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");
    Activity activity;
    boolean guest = false;


    // memo: 2021-01-14 김지훈 수정 시작
    // 신규 가입자 중 약 추가하기로 들어온 경우
    Boolean newbieCheck;
    // memo: 2021-01-14 김지훈 수정 종료


    public MedicineSelectAdapter(Context context, ArrayList<RecyclerViewItemList> list, Activity activity) {
        this.context = context;
        this.arrayList = list;
        this.activity = activity;
    }
    public MedicineSelectAdapter(Context context, ArrayList<RecyclerViewItemList> list, Activity activity,boolean guest) {
        this.context = context;
        this.arrayList = list;
        this.activity = activity;
        this.guest = guest;
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerViewItemList item = arrayList.get(position);
        if (item != null) {
            return item.getViewType();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder");
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_medicine_select_parent, parent, false);
                return new ViewHolderHeader(view);
            case ITEM_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_medicine_select_child, parent, false);
                return new ViewHolderItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final RecyclerViewItemList item = arrayList.get(position);

        switch (item.getViewType()) {
            case RecyclerViewItemList.HEADER_TYPE:
                ((ViewHolderHeader) holder).title.setText(item.getTitle());
                break;
            case RecyclerViewItemList.ITEM_TYPE:
                ((ViewHolderItem) holder).txtName.setText(item.getMedicineTakingItem().getMedicineKo());
//                Glide.with(context).load(item.getMedicineTakingItem().getMedicineImg()).into(((ViewHolderItem)holder).imgIcon);
//                ((ViewHolderItem)holder).imgIcon.setImageResource(item.getMedicineTakingItem().getMedicineImg());
                if (Utils.MEDICINE_TYPE_LIST != null){
                    for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++) {
                        if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == item.getMedicineTakingItem().getMedicineTypeNo()) {
                            if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0) {
                                String replaceText = Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg();
                                if (replaceText.contains("soom2.testserver-1.com:8080")){
                                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                                }else if (replaceText.contains("103.55.190.193")){
                                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                                }
                                Glide.with(context).load("https:" + replaceText).into(((ViewHolderItem) holder).imgIcon);
                            } else {
                                Glide.with(context).load("https://soomcare.info/img/admin/1606365720312.png").into(((ViewHolderItem) holder).imgIcon);

                            }
                        }
                    }
                }

                ((ViewHolderItem) holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean check = false;
                        // memo: 2021-01-14 김지훈 수정 시작
                        // 회원가입 후 약 추가하기로 들어온 사용자의 경우 MedicineTakingItems가 null이라 size 가져올 때 에러 생김
                        if (guest){
                            Intent i = new Intent(context, NewAnotherLoginActivity.class);
                            context.startActivity(i);
                        }else{
//                            if (MedicineTakingItems != null) {
//                                for (int x = 0; x < MedicineTakingItems.size(); x++) {
//                                    if (MedicineTakingItems.get(x).getMedicineNo() == item.getMedicineTakingItem().getMedicineNo() && MedicineTakingItems.get(x).getInsertCheck() == 1) {
//                                        check = true;
//                                        break;
//                                    } else {
//                                        check = false;
//                                    }
//                                }
//                            }
                            // memo: 2021-01-14 김지훈 수정 종료
//                            if (check) {
//                                new OneButtonDialog(activity, "약 등록", "이미 추가된 약입니다.", "확인");
//                            } else {
                                Intent i = new Intent(context, MedicineInsertActivity.class);
                                i.putExtra("medicineNo", item.getMedicineTakingItem().getMedicineNo());
                                i.putExtra("medicineKo", item.getMedicineTakingItem().getMedicineKo());
                                i.putExtra("medicineImg", item.getMedicineTakingItem().getMedicineImg());
                                i.putExtra("manufacturer", item.getMedicineTakingItem().getManufacturer());

                                i.putExtra("instructions", item.getMedicineTakingItem().getInstructions());
                                i.putExtra("ingredient", item.getMedicineTakingItem().getIngredient());
                                i.putExtra("storageMethod", item.getMedicineTakingItem().getStorageMethod());
                                i.putExtra("efficacy", item.getMedicineTakingItem().getEfficacy());
                                i.putExtra("information", item.getMedicineTakingItem().getInformation());
                                i.putExtra("stabilityRating", item.getMedicineTakingItem().getStabiltyRationg());
                                i.putExtra("precautions", item.getMedicineTakingItem().getPrecaution());
                                context.startActivity(i);
//                            }
                        }
                    }
                });
                break;
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView title;

        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_medicine_select_header_title);
            itemView.setTag(true);
        }
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgIcon;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_medicine_select_child_name);
            imgIcon = itemView.findViewById(R.id.img_medicine_select_child_icon);
            itemView.setTag(false);
        }
    }
}

