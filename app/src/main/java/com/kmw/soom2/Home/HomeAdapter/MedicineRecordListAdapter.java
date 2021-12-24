package com.kmw.soom2.Home.HomeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.CalendarActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MedicinRecordActivity;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.R;

import java.util.ArrayList;
import static com.kmw.soom2.Home.Activitys.SymptomActivitys.MedicinRecordActivity.medicineTakingItems1;

public class MedicineRecordListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<MedicineTakingItem> itemArrayList = new ArrayList<>();
    String title;


    public MedicineRecordListAdapter(LayoutInflater inflater, Context context) {
        this.context = context;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null){

            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.view_medicine_record_list_item,null);

            viewHolder.imgIcon = (ImageView)view.findViewById(R.id.img_medicine_record_list_item_icon);
            viewHolder.txtName = (TextView)view.findViewById(R.id.txt_medicine_record_list_item_name);
            viewHolder.linStatus = (LinearLayout)view.findViewById(R.id.lin_medicine_record_list_item_status);
            viewHolder.txtStatus = (ImageView)view.findViewById(R.id.txt_medicine_record_list_item_status);
            viewHolder.linClick = (LinearLayout)view.findViewById(R.id.lin_medicine_record_list_item_click);

            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }

        if (Utils.MEDICINE_TYPE_LIST != null){
            for (int j = 0; j < Utils.MEDICINE_TYPE_LIST.size(); j++){
                if (Utils.MEDICINE_TYPE_LIST.get(j).getMedicineTypeNo() == itemArrayList.get(i).getMedicineTypeNo()){
                    if (Utils.MEDICINE_TYPE_LIST.get(j).getTypeImg().length() != 0){
                        String replaceText = Utils.MEDICINE_TYPE_LIST.get(j).getTypeImg();
                        if (replaceText.contains("soom2.testserver-1.com:8080")){
                            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                        }else if (replaceText.contains("103.55.190.193")){
                            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                        }
                        Glide.with(context).load("https:"+replaceText).into(viewHolder.imgIcon);
                    }
                }
            }
        }

        viewHolder.txtName.setText(itemArrayList.get(i).getMedicineKo());
        viewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.dbdbdb));
        viewHolder.imgIcon.setBackgroundTintList(context.getColorStateList(R.color.white));

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.linClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemArrayList.get(i).getEmergencyFlag().equals("1")){
                    finalViewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.ff6767));
                    finalViewHolder.imgIcon.setBackgroundTintList(context.getColorStateList(R.color.ff676760));
                    finalViewHolder.txtName.setTextColor(context.getColor(R.color.ff6767));
                    medicineTakingItems1.get(i).setEmergencyFlag("2");
                }else if (itemArrayList.get(i).getEmergencyFlag().equals("2")){
                    finalViewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.dbdbdb));
                    finalViewHolder.imgIcon.setBackgroundTintList(context.getColorStateList(R.color.white));
                    finalViewHolder.txtName.setTextColor(context.getColor(R.color.black));
                    medicineTakingItems1.get(i).setEmergencyFlag("0");
                }else{
                    finalViewHolder.linStatus.setBackgroundTintList(context.getColorStateList(R.color.colorPrimary));
                    finalViewHolder.imgIcon.setBackgroundTintList(context.getColorStateList(R.color.color33d16b30));
                    finalViewHolder.txtName.setTextColor(context.getColor(R.color.color09D192));
                    medicineTakingItems1.get(i).setEmergencyFlag("1");
                }
            }
        });

        return view;
    }

    public void addItem(MedicineTakingItem medicineTakingItem){
        itemArrayList.add(medicineTakingItem);
    }

    public class ViewHolder{
        ImageView imgIcon;
        TextView txtName;
        LinearLayout linStatus;
        ImageView txtStatus;
        LinearLayout linClick;
    }
}
