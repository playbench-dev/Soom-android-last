package com.kmw.soom2.Home.HomeAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Home.Activitys.AdultActivitys.AdultCheckResultActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AstmaPercentActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.DustRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.BreathRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewMedicineEditRecordActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.NewSymptomRecordActivity;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Item.HistoryItems;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.HEADER_TYPE;
import static com.kmw.soom2.Home.HomeItem.RecyclerViewItemList.ITEM_TYPE;

public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String TAG = "RecyclerViewAdapter";
    private ArrayList<RecyclerViewItemList> arrayList;

    Context context;
    Typeface typeface,typeface1;
    private String[] causes = new String[]{"감기", "미세먼지", "찬바람", "운동", "집먼지진드기", "애완동물", "음식알러지", "꽃가루", "스트레스", "담배연기", "요리연기", "기타", "강한냄새", "건조함", "곰팡이", "높은습도"};
    float dpWidth;
    Calendar calendar = Calendar.getInstance();

    public CalendarRecyclerViewAdapter(Context context, ArrayList<RecyclerViewItemList> list,float dpWidth){
        this.context = context;
        this.arrayList = list;
        this.dpWidth = dpWidth;
        typeface = ResourcesCompat.getFont(context, R.font.notosanscjkkr_medium);
        typeface1 = ResourcesCompat.getFont(context, R.font.notosanscjkkr_bold);
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
        View view;
        switch (viewType) {
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_header, parent, false);
                return new CalendarRecyclerViewAdapter.ViewHolderHeader(view);
            case ITEM_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_view_item_list, parent, false);
                return new CalendarRecyclerViewAdapter.ViewHolderItem(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RecyclerViewItemList item = null;
        if (position < arrayList.size()){
            item = arrayList.get(position);

            switch (item.getViewType()) {
                case HEADER_TYPE:

//                if (holder instanceof ViewHolderHeader){

                    try {
                        calendar.setTime(Utils.formatYYYYMMDD.parse(item.getTitle()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ((CalendarRecyclerViewAdapter.ViewHolderHeader) holder).title.setText(item.getTitle().substring(0,10)+"("+dayChoice(calendar)+")");
//                    ((ViewHolderHeader) holder).title.setText(item.getTitle().substring(0,10));

                    ((ViewHolderHeader) holder).title.invalidate();

                    ((ViewHolderHeader) holder).imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Intent i = new Intent(context, MedicineRecordEditActivity.class);
//                            context.startActivity(i);

                            Intent i = new Intent(context, CommunityWriteActivity.class);
                            i.putExtra("sharedData", createSharedText(arrayList.get(position).getHistoryItemList()));
//                            i.putExtra("hashTag","#기록공유");
                            i.putExtra("hashTagList", Utils.tagList);
//                            i.putExtra("menuNo","1");
//                            i.putExtra("cMenuNo","18");
//                            i.putExtra("menuTitle","오늘의 숨");
//                            i.putExtra("cMenuTitle","기록공유");
                            context.startActivity(i);
                        }
                    });
//                }
                    break;
                case ITEM_TYPE:

                    if (holder instanceof CalendarRecyclerViewAdapter.ViewHolderItem){

                        try {
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(item.getEtcItem().getRegisterDt().substring(11,18))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String[] symptomTest = item.getMedicineKo().get(0).split(",");
                        if (symptomTest.length > 1){
                            String causesText = "";
                            for (int x = 0; x < symptomTest.length; x++){
                                if (symptomTest[x].equals("11")){
                                    if (x == 0){
                                        causesText += "기침";
                                    }else{
                                        causesText += ", 기침";
                                    }
                                }else if (symptomTest[x].equals("12")){
                                    if (x == 0){
                                        causesText += "숨쉬기 불편";
                                    }else{
                                        causesText += ", 숨쉬기 불편";
                                    }
                                }else if (symptomTest[x].equals("13")){
                                    if (x == 0){
                                        causesText += "천명음";
                                    }else{
                                        causesText += ", 천명음";
                                    }
                                }else if (symptomTest[x].equals("14")){
                                    if (x == 0){
                                        causesText += "가슴답답";
                                    }else{
                                        causesText += ", 가슴답답";
                                    }
                                }else if (symptomTest[x].equals("15")){
                                    if (x == 0){
                                        causesText += "기타증상";
                                    }else{
                                        causesText += ", 기타증상";
                                    }
                                }else if (symptomTest[x].equals("16")){
                                    if (x == 0){
                                        causesText += "가래";
                                    }else{
                                        causesText += ", 가래";
                                    }
                                }
                            }
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText(causesText);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));


                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);
                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;
                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params1);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                        }else if (item.getMedicineKo().get(0).equals("11")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("기침");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));


                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;
                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params1);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                        }else if (item.getMedicineKo().get(0).equals("12")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("숨쉬기 불편");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);

                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;

                                textView1.setLayoutParams(params1);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("13")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("천명음");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;
                                textView1.setLayoutParams(params1);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("14")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("가슴답답");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;
                                textView1.setLayoutParams(params1);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("15")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("기타증상");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;
                                textView1.setLayoutParams(params1);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("16")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("가래");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getCause().length() != 0){
                                String[] causeList = item.getEtcItem().getCause().split(",");
                                String cause = "";
                                for (int i = 0; i < causeList.length; i++){
                                    cause += " " + causes[Integer.parseInt(causeList[i])];
                                }
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);

                                textView1.setLayoutParams(params);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(cause);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("원인 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;
                                textView1.setLayoutParams(params1);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }

                        }else if (item.getMedicineKo().get(0).equals("40")){
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("증상없음");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorefca4a));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorefca4a));

                            if (item.getEtcItem().getMemo().length() != 0){
                                LinearLayout linearLayout = new LinearLayout(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout.setLayoutParams(params);

                                TextView textView = new TextView(context);

                                textView.setLayoutParams(params);

                                TextView textView1 = new TextView(context);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 6;
                                textView1.setLayoutParams(params1);
                                textView1.setTypeface(typeface);
                                textView1.setIncludeFontPadding(false);
                                textView1.setTextColor(context.getResources().getColor(R.color.black));
                                textView1.setText(item.getEtcItem().getMemo());

                                textView1.setSingleLine(false);
                                textView1.setEllipsize(TextUtils.TruncateAt.END);
                                textView1.setMaxLines(2);

                                textView.setTypeface(typeface1);
                                textView.setIncludeFontPadding(false);
                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                textView.setText("메모 : ");

                                textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                linearLayout.addView(textView);
                                linearLayout.addView(textView1);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);
                            }
                        }else if (item.getMedicineKo().get(0).equals("21")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("천식조절검사");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.color3382b7));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.color3382b7));

                            if (item.getEtcItem().getActScore() != 0){
                                TextView textView = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textView.setLayoutParams(params);
                                textView.setTextColor(context.getResources().getColor(R.color.black));


                                textView.setTypeface(typeface);
                                textView.setIncludeFontPadding(false);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                String strFront = "";
                                String strChange = "";
                                String strBack = "";
                                if (item.getEtcItem().getActScore() == 25){
                                    strFront = "<b>점수 : </b>" + item.getEtcItem().getActScore() + " / 조절(";
                                    strChange = "<font color=\"#74BF62\">양호</font>";
                                    strBack = ")<br/><b>결과 : </b>천식 증상이 잘 조절되고 있습니다.";
                                    textView.setText(Html.fromHtml(strFront + strChange + strBack));
                                }else if (item.getEtcItem().getActScore() >= 20){
                                    strFront = "<b>점수 : </b>" + item.getEtcItem().getActScore() + " / 부분조절(";
                                    strChange = "<font color=\"#EFD33C\">주의</font>";
                                    strBack = ")<br/><b>결과 : </b>증상이 부분적으로만 조절되고 있습니다.";
                                    textView.setText(Html.fromHtml(strFront + strChange + strBack));
                                }else{
                                    strFront = "<b>점수 : </b>" + item.getEtcItem().getActScore() + " / 조절안됨(";
                                    strChange = "<font color=\"#BF3232\">위험</font>";
                                    strBack = ")<br/><b>결과 : </b>천식 증상이 조절되지 않고 있습니다.";
                                    textView.setText(Html.fromHtml(strFront + strChange + strBack));
                                }

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                            }

                        }else if (item.getMedicineKo().get(0).equals("22")){

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("폐기능");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.color8489e2));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.color8489e2));

                            if (item.getEtcItem().getPefScore() != 0){
                                TextView textView = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textView.setLayoutParams(params);

                                textView.setTypeface(typeface);
                                textView.setIncludeFontPadding(false);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextColor(context.getResources().getColor(R.color.black));

                                SpannableStringBuilder ssb = new SpannableStringBuilder("측정값 : "+item.getEtcItem().getPefScore());

                                ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 5
                                        , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                                textView.setText(ssb);

                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                            }

                            if (item.getEtcItem().getInspiratorFlag() != 0){
                                TextView textView = new TextView(context);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                textView.setLayoutParams(params);

                                textView.setTypeface(typeface);
                                textView.setIncludeFontPadding(false);
                                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

                                textView.setTextColor(context.getResources().getColor(R.color.black));
                                SpannableStringBuilder ssb = null;

                                if (item.getEtcItem().getInspiratorFlag() == 1){
                                    ssb = new SpannableStringBuilder("측정시 복약여부 : 흡입기 사용");
                                }else {
                                    ssb = new SpannableStringBuilder("측정시 복약여부 : 흡입기 미사용");
                                }
                                ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 10
                                        , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                textView.setText(ssb);
                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                            }

                        }else if (item.getMedicineKo().get(0).equals("23")){
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("미세먼지");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.color3382b7));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.color3382b7));


                            TextView textView = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            textView.setLayoutParams(params);

                            textView.setTypeface(typeface);
                            textView.setIncludeFontPadding(false);

                            textView.setTextColor(context.getResources().getColor(R.color.black));
                            String dustStatus = "";
                            String ultraDustStatus = "";

                            String strFront = "<b>위치 : </b>"+item.getEtcItem().getLocation() + "<br/>" + "<b>미세먼지 : </b>" + item.getEtcItem().getDust() + " / ";
                            String strChange = "<br/>" + "<b>초미세먼지 : </b>" + item.getEtcItem().getUltraDust() + " / ";
                            String strBack = "";

                            if (item.getEtcItem().getDustStatus() == 1){
                                dustStatus = "<font color=\"#74BF62\">좋음</font>";
                            }else if (item.getEtcItem().getDustStatus() == 2){
                                dustStatus = "<font color=\"#EFD33C\">보통</font>";
                            }else if (item.getEtcItem().getDustStatus() == 3){
                                dustStatus = "<font color=\"#E89433\">나쁨</font>";
                            }else if (item.getEtcItem().getDustStatus() == 4){
                                dustStatus = "<font color=\"#BF3232\">매우나쁨</font>";
                            }

                            if (item.getEtcItem().getUltraDustStatus() == 1){
                                ultraDustStatus = "<font color=\"#74BF62\">좋음</font>";
                            }else if (item.getEtcItem().getUltraDustStatus() == 2){
                                ultraDustStatus = "<font color=\"#EFD33C\">보통</font>";
                            }else if (item.getEtcItem().getUltraDustStatus() == 3){
                                ultraDustStatus = "<font color=\"#E89433\">나쁨</font>";
                            }else if (item.getEtcItem().getUltraDustStatus() == 4){
                                ultraDustStatus = "<font color=\"#BF3232\">매우나쁨</font>";
                            }

                            textView.setText(Html.fromHtml(strFront + dustStatus + strChange + ultraDustStatus));

                            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);

                        }else if (item.getMedicineKo().get(0).equals("24")){
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("천식폐질환 가능지수");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.color3382b7));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.color3382b7));

                            TextView textView = new TextView(context);
                            TextView textView1 = new TextView(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            textView.setLayoutParams(params);
                            textView1.setLayoutParams(params);

                            textView.setTypeface(typeface);
                            textView.setIncludeFontPadding(false);
                            textView.setTextColor(context.getResources().getColor(R.color.black));
                            textView1.setTypeface(typeface);
                            textView1.setIncludeFontPadding(false);
                            textView1.setTextColor(context.getResources().getColor(R.color.black));

                            String dustStatus = "";

                            if (item.getEtcItem().getAsthmaScore() == 1){
                                dustStatus = "<b>지수 : </b><font color=\"#EFD33C\">보통</font>";
                            }else if (item.getEtcItem().getAsthmaScore() == 2){
                                dustStatus = "<b>지수 : </b><font color=\"#E89433\">높음</font>";
                            }else if (item.getEtcItem().getAsthmaScore() == 3){
                                dustStatus = "<b>지수 : </b><font color=\"#BF3232\">매우높음</font>";
                            }else{
                                dustStatus = "<b>지수 : </b><font color=\"#74BF62\">낮음</font>";
                            }

                            SpannableStringBuilder ssb = new SpannableStringBuilder("위치 : "+item.getEtcItem().getLocation());

                            ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, 4
                                    , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                            textView.setText(ssb);
                            textView1.setText(Html.fromHtml(dustStatus));

                            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView1);

                        }else if (item.getMedicineKo().get(0).equals("30")){
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();
                            if (item.getEtcItem().getImageFile().length() != 0){
                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.VISIBLE);
                            }else{
                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                            }
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("메모");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.ff6767));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.ff6767));

                            LinearLayout linearLayout = new LinearLayout(context);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            linearLayout.setLayoutParams(params);

                            TextView textView = new TextView(context);
                            textView.setLayoutParams(params);

                            textView.setTypeface(typeface);
                            textView.setIncludeFontPadding(false);
                            textView.setSingleLine(false);
                            textView.setEllipsize(TextUtils.TruncateAt.END);
                            textView.setMaxLines(2);

                            textView.setTextColor(context.getResources().getColor(R.color.black));
                            textView.setText(""+item.getEtcItem().getMemo());

                            TextView textView1 = new TextView(context);
                            textView1.setLayoutParams(params);

                            textView1.setTypeface(typeface1);
                            textView1.setIncludeFontPadding(false);
                            textView1.setTextColor(context.getResources().getColor(R.color.black));
                            textView1.setText("내용 : ");

                            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                            textView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                            linearLayout.addView(textView1);
                            linearLayout.addView(textView);
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout);

                        }else{
                            if (item.getMedicineKo().size() != 0){
                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).imgMemo.setVisibility(View.GONE);
                                ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.removeAllViews();

                                LinearLayout linearLayout1 = new LinearLayout(context);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout1.setLayoutParams(params1);

                                LinearLayout linearLayout2 = new LinearLayout(context);
                                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout2.setLayoutParams(params2);

                                LinearLayout linearLayout3 = new LinearLayout(context);
                                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                linearLayout3.setLayoutParams(params3);

//                                String medicineKo = "";
//                                String word = "[응급]";
//                                for (int i = 0; i < item.getMedicineKo().size(); i++){
//                                    if (i == 0){
//                                        medicineKo += item.getMedicineKo().get(i);
//                                    }else{
//                                        medicineKo += ", "+item.getMedicineKo().get(i);
//                                    }
//                                }

                                String basic = "";
                                String emergency = "";

                                for (int i = 0; i < item.getMedicineKo().size(); i++){
                                    if (item.getMedicineKo().get(i).contains("[응급]")){
                                        if (emergency.length() <= 0){
                                            emergency += item.getMedicineKo().get(i).replace("[응급]","");
                                        }else{
                                            emergency += ", "+item.getMedicineKo().get(i).replace("[응급]","");
                                        }
                                    }else{
                                        if (basic.length() <= 0){
                                            basic += item.getMedicineKo().get(i);
                                        }else{
                                            basic += ", "+item.getMedicineKo().get(i);
                                        }
                                    }
                                }

//                                SpannableString spannableString = new SpannableString(medicineKo);
//                                for (int x = 0; x < medicineKo.length(); x++){
//                                    if (medicineKo.charAt(x) == '[')
//                                        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#BF3232")),x,x+4, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                                }

//                                for (int i = 0; i < item.getMedicineKo().size(); i++){
//                                    TextView textView = new TextView(context);
//                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                    textView.setLayoutParams(params);
//
//                                    textView.setTypeface(typeface1);
//                                    textView.setIncludeFontPadding(false);
//
//                                    if(320<=dpWidth&&dpWidth<360){
//
//                                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
//                                    }else if(dpWidth>360){
//
//                                        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
//                                    }
//                                    textView.setTextColor(context.getResources().getColor(R.color.black));
//                                    textView.setText(item.getMedicineKo().get(i));
//                                    ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(textView);
//                                }

                                if (basic.length() > 0){
                                    TextView textView = new TextView(context);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textView.setLayoutParams(params);

                                    textView.setTypeface(typeface1);
                                    textView.setIncludeFontPadding(false);

                                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                    textView.setTextColor(context.getResources().getColor(R.color.black));
                                    textView.setText("일반 복용 : ");

                                    TextView textViewBasic = new TextView(context);
                                    LinearLayout.LayoutParams paramsBasic = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textViewBasic.setLayoutParams(paramsBasic);

                                    textViewBasic.setTypeface(typeface);
                                    textViewBasic.setIncludeFontPadding(false);

                                    textViewBasic.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                    textViewBasic.setTextColor(context.getResources().getColor(R.color.black));
                                    textViewBasic.setText(basic);

                                    linearLayout1.addView(textView);
                                    linearLayout1.addView(textViewBasic);

                                    ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout1);
                                }

                                if (emergency.length() > 0){
                                    TextView textViewETitle = new TextView(context);
                                    LinearLayout.LayoutParams paramsETitle = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textViewETitle.setLayoutParams(paramsETitle);

                                    textViewETitle.setTypeface(typeface1);
                                    textViewETitle.setIncludeFontPadding(false);

                                    textViewETitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                    textViewETitle.setTextColor(context.getResources().getColor(R.color.black));
                                    textViewETitle.setText("응급 복용 : ");

                                    TextView textViewEmergency = new TextView(context);
                                    LinearLayout.LayoutParams paramsEmergency = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textViewEmergency.setLayoutParams(paramsEmergency);

                                    textViewEmergency.setTypeface(typeface);
                                    textViewEmergency.setIncludeFontPadding(false);

                                    textViewEmergency.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                    textViewEmergency.setTextColor(context.getResources().getColor(R.color.black));
                                    textViewEmergency.setText(emergency);

                                    linearLayout2.addView(textViewETitle);
                                    linearLayout2.addView(textViewEmergency);

                                    ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout2);
                                }

                                if (item.getEtcItem().getMemo().length() != 0){
                                    TextView textViewMTitle = new TextView(context);
                                    LinearLayout.LayoutParams paramsMTitle = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textViewMTitle.setLayoutParams(paramsMTitle);

                                    textViewMTitle.setTypeface(typeface1);
                                    textViewMTitle.setIncludeFontPadding(false);

                                    textViewMTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                    textViewMTitle.setTextColor(context.getResources().getColor(R.color.black));
                                    textViewMTitle.setText("메모 : ");

                                    TextView textViewMemo = new TextView(context);
                                    LinearLayout.LayoutParams paramsMemo = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    textViewMemo.setLayoutParams(paramsMemo);

                                    textViewMemo.setTypeface(typeface);
                                    textViewMemo.setIncludeFontPadding(false);

                                    textViewMemo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
                                    textViewMemo.setTextColor(context.getResources().getColor(R.color.black));
                                    textViewMemo.setText(item.getEtcItem().getMemo());

                                    linearLayout3.addView(textViewMTitle);
                                    linearLayout3.addView(textViewMemo);

                                    ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linListParent.addView(linearLayout3);
                                }
                            }

                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setText("약");
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).title.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linColor.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                        }

                        final RecyclerViewItemList finalItem = item;

                        ((CalendarRecyclerViewAdapter.ViewHolderItem) holder).linParent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (symptomTest.length > 1){
                                    Intent i = new Intent(context, NewSymptomRecordActivity.class);
                                    i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                    i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                    i.putExtra("category", finalItem.getMedicineKo().get(0));
                                    i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                    i.putExtra("cause", finalItem.getEtcItem().getCause());
                                    i.putExtra("memo", finalItem.getEtcItem().getMemo());
                                    ((Activity) context).startActivityForResult(i,1111);
                                }else{
                                    if (finalItem.getMedicineKo().get(0).equals("11") || finalItem.getMedicineKo().get(0).equals("12") ||
                                            finalItem.getMedicineKo().get(0).equals("13") || finalItem.getMedicineKo().get(0).equals("14") ||
                                            finalItem.getMedicineKo().get(0).equals("15") || finalItem.getMedicineKo().get(0).equals("16") ||
                                            finalItem.getMedicineKo().get(0).equals("40")){
                                        Intent i = new Intent(context, NewSymptomRecordActivity.class);
                                        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                        i.putExtra("category", finalItem.getMedicineKo().get(0));
                                        i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                        i.putExtra("cause", finalItem.getEtcItem().getCause());
                                        i.putExtra("memo", finalItem.getEtcItem().getMemo());
                                        ((Activity) context).startActivityForResult(i,1111);

                                    }else if (finalItem.getMedicineKo().get(0).equals("21")){
                                        Intent i = new Intent(context, AdultCheckResultActivity.class);
                                        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                        i.putExtra("category", finalItem.getMedicineKo().get(0));
                                        i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                        i.putExtra("resultType", finalItem.getEtcItem().getActType());
                                        i.putExtra("kidsScore", finalItem.getEtcItem().getActSelected());
                                        i.putExtra("score",finalItem.getEtcItem().getActScore());
                                        ((Activity) context).startActivityForResult(i,1111);
                                    }else if (finalItem.getMedicineKo().get(0).equals("22")){
                                        Intent i = new Intent(context, BreathRecordActivity.class);
                                        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                        i.putExtra("category", finalItem.getMedicineKo().get(0));
                                        i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                        i.putExtra("pefScore", finalItem.getEtcItem().getPefScore());
                                        i.putExtra("inspiratorFlag", finalItem.getEtcItem().getInspiratorFlag());
                                        ((Activity) context).startActivityForResult(i,1111);
                                    }else if (finalItem.getMedicineKo().get(0).equals("23")){
                                        Intent i = new Intent(context, DustRecordActivity.class);
                                        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                        i.putExtra("category", finalItem.getMedicineKo().get(0));
                                        i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                        i.putExtra("location",finalItem.getEtcItem().getLocation());
                                        i.putExtra("dust",finalItem.getEtcItem().getDust());
                                        i.putExtra("ultraDust",finalItem.getEtcItem().getUltraDust());
                                        i.putExtra("dustStatus",finalItem.getEtcItem().getDustStatus());
                                        i.putExtra("ultraDustStatus",finalItem.getEtcItem().getUltraDustStatus());
                                        i.putExtra("lat",finalItem.getEtcItem().getLat());
                                        i.putExtra("lng",finalItem.getEtcItem().getLng());
                                        ((Activity) context).startActivityForResult(i,1111);
                                    }else if (finalItem.getMedicineKo().get(0).equals("24")){
                                        Intent i = new Intent(context, AstmaPercentActivity.class);
                                        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                        i.putExtra("category", finalItem.getMedicineKo().get(0));
                                        i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                        i.putExtra("location",finalItem.getEtcItem().getLocation());
                                        i.putExtra("asthmaScore",finalItem.getEtcItem().getAsthmaScore());
                                        ((Activity) context).startActivityForResult(i,1111);
                                    }else if (finalItem.getMedicineKo().get(0).equals("30")){
                                        Intent i = new Intent(context, MemoActivity.class);
                                        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                        i.putExtra("category", finalItem.getMedicineKo().get(0));
                                        i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                        i.putExtra("memo", finalItem.getEtcItem().getMemo());
                                        i.putExtra("imgsPath",finalItem.getEtcItem().getImageFile());
                                        i.putExtra("imagesNo",finalItem.getEtcItem().getImagesNo());
                                        ((Activity) context).startActivityForResult(i,1111);
                                    }else{
                                        Intent i = new Intent(context, NewMedicineEditRecordActivity.class);
                                        i.putExtra("medicineHistoryNo", finalItem.getEtcItem().getUserHistoryNo());
                                        i.putExtra("medicineNo", finalItem.getEtcItem().getMedicineNo());
                                        i.putExtra("volume", finalItem.getEtcItem().getVolume());
                                        i.putExtra("registerDt", finalItem.getEtcItem().getRegisterDt());
                                        i.putExtra("unit", finalItem.getEtcItem().getUnit());
                                        i.putExtra("emergency", finalItem.getEtcItem().getEmergencyFlag());
                                        i.putExtra("ko",finalItem.getMedicineKo().get(0));
                                        ((Activity) context).startActivityForResult(i,1111);
                                    }
                                }
                            }
                        });
                    }
                    break;
            }
        }
    }

    @NonNull
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.list_item_section_text);
            imageView = itemView.findViewById(R.id.direction_button);
            itemView.setTag(true);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        public TextView title,txtTime;
        public LinearLayout linParent,linListParent;
        public LinearLayout linColor;
        public ImageView imgMemo;

        public ViewHolderItem(@NonNull View itemView) {
            super(itemView);
            imgMemo = (ImageView)itemView.findViewById(R.id.img_memo_path);
            title = (TextView)itemView.findViewById(R.id.title_text_view);
            txtTime = (TextView)itemView.findViewById(R.id.time_text_view);
            linListParent = (LinearLayout)itemView.findViewById(R.id.lin_recycler_View_list_item_parent);
            linParent = (LinearLayout)itemView.findViewById(R.id.lin_recycler_view_parent);
            linColor = (LinearLayout)itemView.findViewById(R.id.recycler_view_color);

            itemView.setTag(false);
        }
    }

    public String createSharedText(TreeMap<String, ArrayList<HistoryItems>> items) {

        String sharedText = "";

        TreeMap<String, ArrayList<HistoryItems>> tree = new TreeMap<>(items);

        for (Map.Entry<String, ArrayList<HistoryItems>> entry1 : tree.descendingMap().entrySet()) {
            ArrayList<HistoryItems> value1 = entry1.getValue();
            for (int aa = 0; aa < value1.size(); aa++){
                String[] categorys = value1.get(aa).getCategorySplit().split(",");
                Log.i(TAG,"categorys : " + value1.get(aa).getCategorySplit());
                if (categorys.length > 1){
                    for (int x = 0; x < categorys.length; x++){
                        if (categorys[x].equals("11")){
                            if (x == 0){
                                sharedText += "[기침";
                            }else if (x == categorys.length-1){
                                sharedText += ", 기침]\n";
                            }else{
                                sharedText += ", 기침";
                            }
                        }else if (categorys[x].equals("12")){
                            if (x == 0){
                                sharedText += "[숨쉬기 불편";
                            }else if (x == categorys.length-1){
                                sharedText += ", 숨쉬기 불편]\n";
                            }else{
                                sharedText += ", 숨쉬기 불편";
                            }
                        }else if (categorys[x].equals("13")){
                            if (x == 0){
                                sharedText += "[천명음";
                            }else if (x == categorys.length-1){
                                sharedText += ", 천명음]\n";
                            }else{
                                sharedText += ", 천명음";
                            }
                        }else if (categorys[x].equals("14")){
                            if (x == 0){
                                sharedText += "[가슴답답";
                            }else if (x == categorys.length-1){
                                sharedText += ", 가슴답답]\n";
                            }else{
                                sharedText += ", 가슴답답";
                            }
                        }else if (categorys[x].equals("15")){
                            if (x == 0){
                                sharedText += "[기타증상";
                            }else if (x == categorys.length-1){
                                sharedText += ", 기타증상]\n";
                            }else{
                                sharedText += ", 기타증상";
                            }
                        }else if (categorys[x].equals("16")){
                            if (x == 0){
                                sharedText += "[가래";
                            }else if (x == categorys.length-1){
                                sharedText += ", 가래]\n";
                            }else{
                                sharedText += ", 가래";
                            }
                        }
                    }

                    if (value1.get(aa).getCause().length() != 0) {
                        String[] causeList = value1.get(aa).getCause().split(",");
                        String cause = "";
                        for (int j = 0; j < causeList.length; j++) {
                            if (j == 0){
                                cause += causes[Integer.parseInt(causeList[j])];
                            }else{
                                cause += ", " + causes[Integer.parseInt(causeList[j])];
                            }
                        }

                        if (items.size() == 1){
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += "" + cause + "\n" + value1.get(aa).getMemo();
                            }else{
                                sharedText += "" + cause;
                            }
                        }else{
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += "" + cause + "\n" + value1.get(aa).getMemo() + "\n\n";
                            }else{
                                sharedText += "" + cause + "\n\n";
                            }
                        }
                    }

                }else{
                    if (value1.get(aa).getCategory() == 11 || value1.get(aa).getCategory() == 12 || value1.get(aa).getCategory() == 13 ||
                            value1.get(aa).getCategory() == 14 || value1.get(aa).getCategory() == 15 || value1.get(aa).getCategory() == 16 ||
                            value1.get(aa).getCategory() == 40) {

                        String category = String.valueOf(value1.get(aa).getCategory());

                        if (category.equals("11")) {
                            sharedText += "[기침]\n";
                        } else if (category.equals("12")) {
                            sharedText += "[숨쉬기 불편]\n";
                        } else if (category.equals("13")) {
                            sharedText += "[천명음]\n";
                        } else if (category.equals("14")) {
                            sharedText += "[가슴답답]\n";
                        } else if (category.equals("15")){
                            sharedText += "[기타증상]\n";
                        } else if (category.equals("16")){
                            sharedText += "[가래]\n";
                        }else if (category.equals("40")){
                            if (value1.get(aa).getMemo().length() == 0){
                                sharedText += "[증상없음]\n\n";
                            }else{
                                sharedText += "[증상없음]\n";
                                sharedText += value1.get(aa).getMemo() + "\n\n";
                            }
                        }

                        if (value1.get(aa).getCause().length() != 0) {
                            String[] causeList = value1.get(aa).getCause().split(",");
                            String cause = "";
                            for (int j = 0; j < causeList.length; j++) {
                                if (j == 0){
                                    cause += causes[Integer.parseInt(causeList[j])];
                                }else{
                                    cause += ", " + causes[Integer.parseInt(causeList[j])];
                                }
                            }

                            if (items.size() == 1){
                                if (value1.get(aa).getMemo().length() != 0){
                                    sharedText += "" + cause + "\n" + value1.get(aa).getMemo();
                                }else{
                                    sharedText += "" + cause;
                                }
                            }else{
                                if (value1.get(aa).getMemo().length() != 0){
                                    sharedText += "" + cause + "\n" + value1.get(aa).getMemo() + "\n\n";
                                }else{
                                    sharedText += "" + cause + "\n\n";
                                }
                            }
                        }
                    }else if (value1.get(aa).getCategory() == 21) {
                        sharedText += "[천식조절 검사]\n";
                        if (value1.get(aa).getActScore() != 0) {
                            if (value1.get(aa).getActState() == 1) {
                                sharedText += "양호 - " + value1.get(aa).getActScore() + " / 조절(양호)\n천식 증상이 잘 조절되고 있습니다.";
                                if (items.size() == 1){

                                }else{
                                    sharedText += "\n\n";
                                }
                            } else if (value1.get(aa).getActState() == 2) {
                                sharedText += "주의 - " + value1.get(aa).getActScore() + " / 부분조절(주의)\n증상이 부분적으로만 조절되고 있습니다.";
                                if (items.size() == 1){

                                }else{
                                    sharedText += "\n\n";
                                }
                            } else {
                                sharedText += "위험 - " + value1.get(aa).getActScore() + " / 조절안됨(위험)\n천식 증상이 조절되지 않고 있습니다.";
                                if (items.size() == 1){

                                }else{
                                    sharedText += "\n\n";
                                }
                            }
                        }
                    } else if (value1.get(aa).getCategory() == 22) {
                        sharedText += "[폐기능]\n";

                        sharedText += "" + value1.get(aa).getPefScore() + "\n";
                        if (value1.get(aa).getInspiratorFlag() == 1) {
                            sharedText += "흡입기 사용";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        } else {
                            sharedText += "흡입기 미사용";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        }
                    } else if (value1.get(aa).getCategory() == 23) {

                        sharedText += "[미세먼지]\n";

                        if (value1.get(aa).getDust() != 0 && value1.get(aa).getDustState() != 0 && value1.get(aa).getUltraDust() != 0 && value1.get(aa).getUltraDustState() != 0) {
                            String dustState = "";
                            if (value1.get(aa).getDustState() == 1) {
                                dustState = "좋음";
                            } else if (value1.get(aa).getDustState() == 2) {
                                dustState = "보통";
                            } else if (value1.get(aa).getDustState() == 3) {
                                dustState = "나쁨";
                            } else if (value1.get(aa).getDustState() == 4) {
                                dustState = "매우나쁨";
                            }
                            String ultraDustState = "";
                            if (value1.get(aa).getUltraDustState() == 1) {
                                ultraDustState = "좋음";
                            } else if (value1.get(aa).getUltraDustState() == 2) {
                                ultraDustState = "보통";
                            } else if (value1.get(aa).getUltraDustState() == 3) {
                                ultraDustState = "나쁨";
                            } else if (value1.get(aa).getUltraDustState() == 4) {
                                ultraDustState = "매우나쁨";
                            }
                            sharedText += "위치 - " + value1.get(aa).getLocation() + "\n미세먼지 - " + value1.get(aa).getDust() + " / " + dustState + "\n초미세먼지 - "
                                    + value1.get(aa).getUltraDust() + " / " + ultraDustState + "";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        } else {
                            sharedText += "위치 - " + value1.get(aa).getLocation() + "";
                            if (items.size() == 1){

                            }else{
                                sharedText += "\n\n";
                            }
                        }
                    }else if (value1.get(aa).getCategory() == 24){
                        sharedText += "[천식폐질환 가능지수]\n";

                        sharedText += "위치 - " + value1.get(aa).getLocation() + "\n";

                        String dustState = "";
                        if (value1.get(aa).getAsthmaScore() == 1) {
                            dustState = "보통";
                        } else if (value1.get(aa).getAsthmaScore() == 2) {
                            dustState = "높음";
                        } else if (value1.get(aa).getAsthmaScore() == 3) {
                            dustState = "매우높음";
                        } else {
                            dustState = "낮음";
                        }

                        sharedText += "지수 : " + dustState;
                        if (items.size() == 1){

                        }else{
                            sharedText += "\n\n";
                        }
                    }
                    else if (value1.get(aa).getCategory() == 30) {
                        sharedText += "[메모]\n";
                        if (items.size() == 1){
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += "" + value1.get(aa).getMemo();
                            }else{
                                sharedText += "";
                            }
                        }else{
                            if (value1.get(aa).getMemo().length() != 0){
                                sharedText += value1.get(aa).getMemo() + "\n\n";
                            }else{
                                sharedText += "\n\n";
                            }
                        }
                    }else {
//                        sharedText += "[복약]\n";
////                    sharedText += value1.get(aa).getMedicineNo() + "\n\n";
//                        Log.i(TAG,"getKo : " + value1.get(aa).getKo());
//                        Log.i(TAG,"getKo : " + value1.stream().map(s->s.getKo()).collect(Collectors.joining("\n\n")));
//                        if (value1.get(aa).getEmergencyFlag() == 2){
//                            Log.i(TAG,"getKo 1111");
//                            sharedText += value1.get(aa).getKo();
////                        sharedText += value1.stream().map(s->(s.getKo()+"[응급]")).collect(Collectors.joining("\n\n"));
//                        }else{
//                            Log.i(TAG,"getKo 2222");
//                            sharedText += value1.get(aa).getKo();
////                        sharedText += value1.stream().map(s->s.getKo()).collect(Collectors.joining("\n\n"));
//                        }
//                        if (items.size() == 1){
//
//                        }else{
//                            sharedText += "\n\n";
//                        }
                    }
                }
            }
            if (value1.get(0).getCategory() == 1){
                sharedText += "[복약]\n";
//                    sharedText += value1.get(aa).getMedicineNo() + "\n\n";
                sharedText += value1.stream().map(s->s.getKo()).collect(Collectors.joining("\n"));
//                        if (value1.get(aa).getEmergencyFlag() == 2){
////                            sharedText += value1.get(aa).getKo();
//                        sharedText += value1.stream().map(s->(s.getKo()+"[응급]")).collect(Collectors.joining("\n\n"));
//                        }else{
////                            sharedText += value1.get(aa).getKo();
//                        }
                if (value1.get(0).getMemo().toString().length() > 0){
                    sharedText += "\n"+value1.get(0).getMemo();
                }
                sharedText += "\n\n";
            }
        }
        return sharedText;
    }

    private String dayChoice(Calendar calendar){

        String day = "";

        Log.i(TAG,"calendar : " + calendar.get(Calendar.DATE));

        if (calendar.get(Calendar.DAY_OF_WEEK) == 1){
            day = "일";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 2){
            day = "월";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 3){
            day = "화";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 4){
            day = "수";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 5){
            day = "목";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 6){
            day = "금";
        }else if (calendar.get(Calendar.DAY_OF_WEEK) == 7){
            day = "토";
        }

        return day;
    }
}
