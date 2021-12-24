package com.kmw.soom2.Home.HomeAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineInsertActivity;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSearchActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.util.ArrayList;

import static com.kmw.soom2.Common.Utils.MedicineTakingItems;

public class MedicineSearchAdapter extends BaseAdapter {

    private String TAG = "MedicineSearchAdapter";
    Context context;
    ArrayList<MedicineTakingItem> itemArrayList = new ArrayList<>();
    ArrayList<MedicineTakingItem> itemArrayList1 = new ArrayList<>();
    boolean newbieCheck;
    Activity activity;
    public MedicineSearchAdapter(Context context,boolean newbieCheck,Activity activity) {
        this.context = context;
        this.newbieCheck = newbieCheck;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_medicine_select_child,null);

            viewHolder = new ViewHolder();

            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txt_medicine_select_child_name);
            viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.img_medicine_select_child_icon);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.txtName.setText(itemArrayList.get(position).getMedicineKo());

        for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++){
            if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == itemArrayList.get(position).getMedicineTypeNo()){
                if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0){
                    String replaceText = Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg();
                    if (replaceText.contains("soom2.testserver-1.com:8080")){
                        replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                    }else if (replaceText.contains("103.55.190.193")){
                        replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                    }
                    Glide.with(context).load("https:"+replaceText).into(viewHolder.imgIcon);
                }else {
                    Glide.with(context).load("https://soomcare.info/img/admin/1606365720312.png").into(viewHolder.imgIcon);
                }
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!newbieCheck){

                    boolean check = false;

                    for (int x = 0; x < MedicineTakingItems.size(); x++){
                        if (MedicineTakingItems.get(x).getMedicineNo() == itemArrayList.get(position).getMedicineNo()&&MedicineTakingItems.get(x).getAliveFlag() == 1){
                            check = true;
                            break;
                        }else{
                            check = false;
                        }
                    }

                    if (check){
                        new OneButtonDialog(activity,"약 등록","이미 추가된 약입니다.","확인");
                    }else{
                        Intent i = new Intent(context, MedicineInsertActivity.class);
                        i.putExtra("medicineNo",itemArrayList.get(position).getMedicineNo());
                        i.putExtra("medicineKo",itemArrayList.get(position).getMedicineKo());
                        i.putExtra("medicineImg",itemArrayList.get(position).getMedicineImg());
                        i.putExtra("manufacturer",itemArrayList.get(position).getManufacturer());

                        i.putExtra("instructions",itemArrayList.get(position).getInstructions());
                        i.putExtra("ingredient",itemArrayList.get(position).getIngredient());
                        i.putExtra("storageMethod",itemArrayList.get(position).getStorageMethod());
                        i.putExtra("efficacy",itemArrayList.get(position).getEfficacy());
                        i.putExtra("information",itemArrayList.get(position).getInformation());
                        i.putExtra("stabilityRating",itemArrayList.get(position).getStabiltyRationg());
                        i.putExtra("precautions",itemArrayList.get(position).getPrecaution());
                        i.putExtra("newbieCheck",newbieCheck);
                        context.startActivity(i);
                    }
                }else{
                    Intent i = new Intent(context, MedicineInsertActivity.class);
                    i.putExtra("medicineNo",itemArrayList.get(position).getMedicineNo());
                    i.putExtra("medicineKo",itemArrayList.get(position).getMedicineKo());
                    i.putExtra("medicineImg",itemArrayList.get(position).getMedicineImg());
                    i.putExtra("manufacturer",itemArrayList.get(position).getManufacturer());

                    i.putExtra("instructions",itemArrayList.get(position).getInstructions());
                    i.putExtra("ingredient",itemArrayList.get(position).getIngredient());
                    i.putExtra("storageMethod",itemArrayList.get(position).getStorageMethod());
                    i.putExtra("efficacy",itemArrayList.get(position).getEfficacy());
                    i.putExtra("information",itemArrayList.get(position).getInformation());
                    i.putExtra("stabilityRating",itemArrayList.get(position).getStabiltyRationg());
                    i.putExtra("precautions",itemArrayList.get(position).getPrecaution());
                    i.putExtra("newbieCheck",newbieCheck);
                    context.startActivity(i);
                }
            }
        });

        return convertView;
    }

    public void searchName(String text){

        itemArrayList.clear();

        if (text.length() == 0){
            itemArrayList.addAll(itemArrayList1);
        }else{
            for (int i = 0; i < itemArrayList1.size(); i++){
                if (itemArrayList1.get(i).getMedicineKo().contains(text)){
                    itemArrayList.add(itemArrayList1.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void addItem(MedicineTakingItem medicineTakingItem){

        itemArrayList.add(medicineTakingItem);
        itemArrayList1.add(medicineTakingItem);
    }

    private class ViewHolder{
        public TextView txtName;
        public ImageView imgIcon;
    }
}
