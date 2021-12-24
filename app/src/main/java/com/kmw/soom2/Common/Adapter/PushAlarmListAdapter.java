package com.kmw.soom2.Common.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmw.soom2.Common.Activity.PushAlarmListActivity;
import com.kmw.soom2.Common.Item.PushItems;
import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineSelectActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewSymptomRecordActivity;
import com.kmw.soom2.MyPage.Activity.NoticeDetailActivity;
import com.kmw.soom2.R;

import java.util.ArrayList;

public class PushAlarmListAdapter extends BaseAdapter {
    private final String TAG = "PushAlarmListAdapter";
    ArrayList<PushItems> itemsArrayList = new ArrayList<>();
    Context context;

    public PushAlarmListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_push_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.txtTitle = view.findViewById(R.id.txt_push_list_title);
            viewHolder.txtDate = view.findViewById(R.id.txt_push_list_date);
            viewHolder.txtContents = view.findViewById(R.id.txt_push_list_contents);

            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.txtTitle.setText(itemsArrayList.get(i).getTitle());
        if (itemsArrayList.get(i).getChannelId().equals("home")){
            viewHolder.txtContents.setMaxLines(Integer.MAX_VALUE);
            viewHolder.txtContents.setText(itemsArrayList.get(i).getContents());
        }else{
            viewHolder.txtContents.setMaxLines(2);
            viewHolder.txtContents.setText(itemsArrayList.get(i).getContents());
        }

        viewHolder.txtDate.setText(itemsArrayList.get(i).getCreateDt());

        //미확인
        if (itemsArrayList.get(i).getReadFlag() == 2){
            view.setBackgroundColor(context.getColor(R.color.effdf7));
        }else{
            view.setBackgroundColor(context.getColor(R.color.white));
        }


        View finalView = view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "channel : " + itemsArrayList.get(i).getChannelId() + ", no : " + itemsArrayList.get(i).getKeyNo());

                itemsArrayList.get(i).setReadFlag(1);
                if (itemsArrayList.get(i).getChannelId().equals("community") && !itemsArrayList.get(i).getKeyNo().equals("0")){
                    for (int x = 0; x < itemsArrayList.size(); x++){
                        if (itemsArrayList.get(x).getChannelId().equals("community")){
                            if (itemsArrayList.get(i).getKeyNo().equals(itemsArrayList.get(x).getKeyNo())){
                                itemsArrayList.get(x).setReadFlag(1);
                            }
                        }
                    }
//                    new PushAlarmListActivity.ReadAlarmUpdateNetwork(1).execute(itemsArrayList.get(i).getKeyNo(),itemsArrayList.get(i).getChannelId());
                    Intent intent = new Intent(context, NewCommunityDetailActivity.class);
                    intent.putExtra("push",false);
                    intent.putExtra("keyNo",""+itemsArrayList.get(i).getKeyNo());
                    intent.putExtra("communityNo",itemsArrayList.get(i).getKeyNo());
                    context.startActivity(intent);
                } else if (itemsArrayList.get(i).getChannelId().equals("notice") && !itemsArrayList.get(i).getKeyNo().equals("0")){
//                    new PushAlarmListActivity.ReadAlarmUpdateNetwork(1).execute(itemsArrayList.get(i).getKeyNo(),itemsArrayList.get(i).getChannelId());
                    Intent intent = new Intent(context, NoticeDetailActivity.class);
                    intent.putExtra("contents",itemsArrayList.get(i).getContents());
                    intent.putExtra("noticeNo",itemsArrayList.get(i).getKeyNo());
                    intent.putExtra("keyNo",""+itemsArrayList.get(i).getKeyNo());
                    context.startActivity(intent);
                }else if (itemsArrayList.get(i).getChannelId().equals("dosing")){
                    Intent intent = new Intent(context, MedicineSelectActivity.class);
                    Log.i(TAG,"push : " + ""+itemsArrayList.get(i).getPushNo() + " key : " + ""+itemsArrayList.get(i).getKeyNo());
                    if (itemsArrayList.get(i).getKeyNo().equals("0") || itemsArrayList.get(i).getKeyNo().equals("")){
                        intent.putExtra("pushNo",""+itemsArrayList.get(i).getPushNo());
                    }else{
                        intent.putExtra("keyNo",""+itemsArrayList.get(i).getKeyNo());
                    }
                    context.startActivity(intent);
                }else if (itemsArrayList.get(i).getChannelId().equals("symptom")){
                    Intent intent = new Intent(context, NewSymptomRecordActivity.class);
                    intent.putExtra("pushNo",""+itemsArrayList.get(i).getPushNo());
                    context.startActivity(intent);
                }else if (itemsArrayList.get(i).getKeyNo().equals("0") || itemsArrayList.get(i).getKeyNo().equals("")){
                    finalView.setBackgroundColor(context.getColor(R.color.white));
                    new PushAlarmListActivity.ReadAlarmUpdateNetwork(2).execute(""+itemsArrayList.get(i).getPushNo());
                }
            }
        });
        return view;
    }

    public void addItem(PushItems item) {
        itemsArrayList.add(item);
    }

    private class ViewHolder {
        public TextView txtTitle;
        public TextView txtDate;
        public TextView txtContents;
    }
}
