package com.kmw.soom2.DrugControl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.Activitys.MedicineInsertActivity.MedicineInsertActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.Views.PagingScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_LIST_BY_DATE_PAST;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.calDateBetweenAandB;

public class DrugCompleteListActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String TAG = "DrugCompleteListActivity";
    TextView txtBack;
    LinearLayout linListParent;
    LinearLayout linNoItem;
    Intent beforeIntent;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yy.MM.dd");
    SimpleDateFormat dateFormatBefore = new SimpleDateFormat("yyyyMMdd");

    int pageNo = 1;
    boolean mLockScrollView = false;
    int search_total_page;
    PagingScrollView pagingScrollView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_complete_list);

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

        linListParent.removeAllViews();

        progressDialog = new ProgressDialog(this,R.style.MyTheme);
//        Drawable drawable = new ProgressBar(getActivity()).getIndeterminateDrawable().mutate();
//        drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent),
//                PorterDuff.Mode.SRC_IN);
//        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        pageNo = 1;
        NetworkCall(MEDICINE_LIST_BY_DATE_PAST);
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_drug_complete_back);
        linListParent = (LinearLayout)findViewById(R.id.lin_drug_complete_list_parent);
        linNoItem = (LinearLayout)findViewById(R.id.lin_drug_complete_no_item);
        pagingScrollView = findViewById(R.id.scr_drug_complete);

        txtBack.setOnClickListener(this);

        pagingScrollView.setScrollViewListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = pagingScrollView.getChildAt(pagingScrollView.getChildCount() - 1);
                int diff = (view.getBottom() - (pagingScrollView.getHeight() + pagingScrollView.getScrollY()));

                if (diff == 0 && mLockScrollView == true && pageNo < search_total_page) { // 스크롤 bottom
                    pageNo++;
                    NetworkCall(MEDICINE_LIST_BY_DATE_PAST);
                }
            }
        });
    }

    void medicineListItem(final int medicineNo, final int historyNo, final String name, final String startDt, final String endDt, final int frequency, final String volume, final String unit,
                          final String instruction, final String information, final String efficacy, final String stabilty, final String precaution, final int medicineTypeNo,String medicineImg){
        View listView = new View(DrugCompleteListActivity.this);
        listView = getLayoutInflater().inflate(R.layout.new_drug_complete_list_item,null);

        TextView txtName = (TextView)listView.findViewById(R.id.txt_new_drug_complete_list_item_name);
        TextView txtStartDate = (TextView)listView.findViewById(R.id.txt_new_drug_complete_list_item_start_date);
        TextView txtEndDate = (TextView)listView.findViewById(R.id.txt_new_drug_complete_list_item_end_date);

        TextView txtAmount = (TextView)listView.findViewById(R.id.txt_new_drug_complete_list_item_amount);
        TextView txtCnt = (TextView)listView.findViewById(R.id.txt_new_drug_complete_list_item_cnt);
        TextView txtPeriod = (TextView)listView.findViewById(R.id.txt_new_drug_complete_list_item_vice_date);
        ImageView imgIcon = (ImageView)listView.findViewById(R.id.img_new_drug_complete_list_item_icon);
        LinearLayout linReview = (LinearLayout)listView.findViewById(R.id.lin_new_drug_complete_list_item_review);

        if (Utils.MEDICINE_TYPE_LIST != null){
            for (int i = 0; i < Utils.MEDICINE_TYPE_LIST.size(); i++){
                if (Utils.MEDICINE_TYPE_LIST.get(i).getMedicineTypeNo() == medicineTypeNo){
                    if (Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg().length() != 0){
                        String replaceText = Utils.MEDICINE_TYPE_LIST.get(i).getTypeImg();
                        if (replaceText.contains("soom2.testserver-1.com:8080")){
                            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                        }else if (replaceText.contains("103.55.190.193")){
                            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                        }
                        Glide.with(DrugCompleteListActivity.this).load("https:"+replaceText).into(imgIcon);
                    }
                }
            }
        }

        txtName.setText(name);
        try {
            txtStartDate.setText(dateFormat.format(dateFormatBefore.parse(startDt)));
            txtEndDate.setText(dateFormat.format(dateFormatBefore.parse(endDt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (frequency == 0 || frequency == -1){
            txtCnt.setText("필요시");
        }else{
            txtCnt.setText("하루 " + frequency + "번");
        }
        txtAmount.setText("" + volume + unit);

        txtPeriod.setText(""+ calDateBetweenAandB(startDt,endDt) + "/" + calDateBetweenAandB(startDt,endDt));

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrugCompleteListActivity.this, MedicineInsertActivity.class);
                i.putExtra("medicineNo",medicineNo);
                i.putExtra("historyNo",historyNo);
                i.putExtra("name",name);
                i.putExtra("startDt",startDt);
                i.putExtra("endDt",endDt);
                i.putExtra("frequency",frequency);
                i.putExtra("volume",volume);
                i.putExtra("unit",unit);
                i.putExtra("efficacy",efficacy);
                i.putExtra("instructions",instruction);
                i.putExtra("information",information);
                i.putExtra("stabilityRating",stabilty);
                i.putExtra("precautions",precaution);
                i.putExtra("medicineTypeNo",medicineTypeNo);
                i.putExtra("medicineImg",medicineImg);
                i.putExtra("complete",true);
                startActivity(i);
            }
        });

        linReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DrugCompleteListActivity.this, MedicineInsertActivity.class);
                i.putExtra("medicineNo",medicineNo);
                i.putExtra("historyNo",historyNo);
                i.putExtra("name",name);
                i.putExtra("startDt",startDt);
                i.putExtra("endDt",endDt);
                i.putExtra("frequency",frequency);
                i.putExtra("volume",volume);
                i.putExtra("unit",unit);
                i.putExtra("efficacy",efficacy);
                i.putExtra("instructions",instruction);
                i.putExtra("information",information);
                i.putExtra("stabilityRating",stabilty);
                i.putExtra("precautions",precaution);
                i.putExtra("medicineTypeNo",medicineTypeNo);
                i.putExtra("medicineImg",medicineImg);
                i.putExtra("complete",true);
                i.putExtra("review",true);
                startActivity(i);
            }
        });

        linListParent.addView(listView);
    }

    public Drawable getThumb(int progress) {
        View thumbView = LayoutInflater.from(DrugCompleteListActivity.this).inflate(R.layout.view_custom_seekbar_thumb, null, false);
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_drug_complete_back : {
                onBackPressed();
                break;
            }
        }
    }

    void NetworkCall(String mCode){
        if (mCode.equals(MEDICINE_LIST_BY_DATE_PAST)){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(System.currentTimeMillis()));
            c.add(Calendar.DATE,1);
            Log.i(TAG,"date : " + new SimpleDateFormat("yyyyMMdd").format(c.getTime()));
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),new SimpleDateFormat("yyyyMMdd").format(c.getTime()),""+pageNo,"15");
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            if (mCode.equals(MEDICINE_LIST_BY_DATE_PAST)){
                if (JsonIsNullCheck(jsonObject,"result").equals("N")){
                    progressDialog.dismiss();
                }
                search_total_page = JsonIntIsNullCheck(jsonObject,"Search_TotalPage");
                if (pageNo == 1){
                    linListParent.removeAllViews();
                }

                JSONArray jsonArray = jsonObject.getJSONArray("list");
                if (jsonArray.length() != 0){
                    linNoItem.setVisibility(View.GONE);
                }
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    JSONObject objectCls = object.getJSONObject("clsMedicineBean");
                    medicineListItem(JsonIntIsNullCheck(object,"MEDICINE_NO"),JsonIntIsNullCheck(object,"MEDICINE_HISTORY_NO"),
                            JsonIsNullCheck(object,"KO"),JsonIsNullCheck(object,"START_DT"),JsonIsNullCheck(object,"END_DT"),
                            JsonIntIsNullCheck(object,"FREQUENCY"),JsonIsNullCheck(object,"VOLUME"),JsonIsNullCheck(object,"UNIT"),
                            JsonIsNullCheck(objectCls,"EFFICACY"),JsonIsNullCheck(objectCls,"INSTRUCTIONS"),JsonIsNullCheck(objectCls,"INFORMATION"),
                            JsonIsNullCheck(objectCls,"STABILITY_RATING"),JsonIsNullCheck(objectCls,"PRECAUTIONS"),JsonIntIsNullCheck(object,"MEDICINE_TYPE_NO"),"");
                }
                mLockScrollView = true;
                progressDialog.dismiss();
            }
        }catch (JSONException e){

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
