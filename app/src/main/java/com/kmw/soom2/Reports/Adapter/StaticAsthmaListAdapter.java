package com.kmw.soom2.Reports.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Item.HistoryItems;
import com.kmw.soom2.Reports.Item.StaticAsthmaItems;
import com.kmw.soom2.Reports.Activitys.StaticAsthmaResultActivity;

import java.util.ArrayList;

public class StaticAsthmaListAdapter extends BaseAdapter {
    Context context;
    public ArrayList<StaticAsthmaItems> itemsArrayList = new ArrayList<>();
    public StaticAsthmaListAdapter(Context context) {
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
            view = inflater.inflate(R.layout.view_static_asthma_list_item,null);

            viewHolder = new ViewHolder();

            viewHolder.txtDate = (TextView)view.findViewById(R.id.txt_statics_asthma_date);
            viewHolder.txtScore = (TextView)view.findViewById(R.id.txt_statics_asthma_score);
            viewHolder.txtStatus = (TextView)view.findViewById(R.id.txt_statics_asthma_status);

            view.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)view.getTag();
        }

        if (itemsArrayList.get(i).getScore() == 25) {
            viewHolder.txtScore.setText(""+itemsArrayList.get(i).getScore());
            viewHolder.txtScore.setTextColor(Color.parseColor("#6ad07e"));
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.result_asthma_good_status));
        }else if (itemsArrayList.get(i).getScore() < 25 && itemsArrayList.get(i).getScore() >= 20) {
            viewHolder.txtScore.setText(""+itemsArrayList.get(i).getScore());
            viewHolder.txtScore.setTextColor(Color.parseColor("#F2D266"));
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.result_asthma_warning_status));
        }else {
            viewHolder.txtScore.setText(""+itemsArrayList.get(i).getScore());
            viewHolder.txtScore.setTextColor(Color.parseColor("#ED410E"));
            viewHolder.txtStatus.setText(context.getResources().getString(R.string.result_asthma_danger_status));
        }
        viewHolder.txtDate.setText(itemsArrayList.get(i).getDate().replace("-","."));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, StaticAsthmaResultActivity.class);
                    intent.putExtra("date",itemsArrayList.get(i).getDate().replace("-","."));
                    intent.putExtra("score",itemsArrayList.get(i).getScore());
                    intent.putExtra("type", itemsArrayList.get(i).getType());
                    intent.putExtra("selected", itemsArrayList.get(i).getSelectedItems());
                    context.startActivity(intent);

            }
        });

        return view;
    }
    public void addItem(HistoryItems item) {
        StaticAsthmaItems asthmaItem = new StaticAsthmaItems();

        asthmaItem.setDate(item.getRegisterDt().substring(0,10));
        asthmaItem.setScore(item.getActScore());
        if (item.getActState() == 1) {
            asthmaItem.setStatus("양호");
        }else if (item.getActState() == 2) {
            asthmaItem.setStatus("주의");
        }else {
            asthmaItem.setStatus("위험");
        }
        asthmaItem.setType(item.getActType());
        asthmaItem.setSelectedItems(item.getActSelected());

        itemsArrayList.add(asthmaItem);
    }
    private class ViewHolder {
        public TextView txtDate;
        public TextView txtStatus;
        public TextView txtScore;

    }
}
