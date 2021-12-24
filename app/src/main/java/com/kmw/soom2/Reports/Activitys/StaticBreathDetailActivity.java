package com.kmw.soom2.Reports.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Item.HistoryItems;

import java.util.ArrayList;
import java.util.Collections;

import static android.view.View.GONE;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class StaticBreathDetailActivity extends AppCompatActivity implements View.OnClickListener  {

    String TAG = "StaticBreathDetailActivity";
    TextView txtTitle;
    LinearLayout linHistoryParent, linHistoryExist, linHistoryNonExist, linBackground;
    LinearLayout linBgChange;
    TextView  txtMinValue, txtMaxValue, txtRateValue, txtDetailInfo, txtDetailTitle;
    TextView  txtDanger,txtWarning,txtGood;
    Button btnMoreDetail;
    TextView btnBack;
    Intent beforeIntent;
    LinearLayout linStaticBar;
    ImageView imgDangerRange, imgWarningRange, imgGoodRange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_breath_detail);

        beforeIntent = getIntent();

        findViewByIds();

        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] {0xFFb91e1e,0xFFf7a02b,0xFFf4e859,0xFF09D182});
        gd.setCornerRadius(50f);

        linStaticBar.setBackgroundDrawable(gd);

        NullCheck(this);

        viewSetting();

        txtTitle.setText(beforeIntent.getStringExtra("date"));

    }

    public void viewSetting() {

        if (Utils.breathItem.getHistoryItems().size() > 1) {
            linHistoryNonExist.setVisibility(GONE);

            ArrayList<Integer> scores = new ArrayList<>();
            for (int i = 0; i < Utils.breathItem.getHistoryItems().size(); i++) {
                scores.add(Utils.breathItem.getHistoryItems().get(i).getPefScore());
            }

            float max = Collections.max(scores).floatValue();
            float min = Collections.min(scores).floatValue();

            float pefRate = ((max - min) / ((max + min) / 2)) * 100;

            Log.i(TAG,"rate : " + pefRate);

            if (pefRate < 20) {
                linBackground.setBackgroundResource(R.drawable.static_breath_result_good);
                linBgChange.setBackgroundResource(R.drawable.static_breath_result_good2);
                txtDetailTitle.setText(R.string.result_breath_good_title);
                txtDetailInfo.setText(R.string.result_breath_good);

                txtGood.setTextColor(Color.parseColor("#39CF67"));
                imgGoodRange.setVisibility(View.VISIBLE);
            } else if (pefRate >= 20 && pefRate < 30) {
                linBackground.setBackgroundResource(R.drawable.static_breath_result_warning);
                linBgChange.setBackgroundResource(R.drawable.static_breath_result_warning2);
                txtDetailTitle.setText(R.string.result_breath_warning_title);
                txtDetailInfo.setText(R.string.result_breath_warning);

                txtWarning.setTextColor(Color.parseColor("#F2CA4E"));
                imgWarningRange.setVisibility(View.VISIBLE);
            } else {
                linBackground.setBackgroundResource(R.drawable.static_breath_result_danger);
                linBgChange.setBackgroundResource(R.drawable.static_breath_result_danger2);
                txtDetailTitle.setText(R.string.result_breath_danger_title);
                txtDetailInfo.setText(R.string.result_breath_danger);

                txtDanger.setTextColor(Color.parseColor("#C7514D"));
                imgDangerRange.setVisibility(View.VISIBLE);
            }

            txtMinValue.setText("" + Math.round(min));
            txtMaxValue.setText("" + Math.round(max));
            txtRateValue.setText("" + Math.round(pefRate) + "%");

//            txtGoodRange.setText(Math.round((max*80/100)) + "~" + Math.round(max));
//            txtWarningRange.setText(Math.round(max*50/100) + "~" + (Math.round(max*80/100) - 1));
//            txtDangerRange.setText("~" + (Math.round(max*50/100) - 1));

            for (int i = 0; i < Utils.breathItem.getHistoryItems().size(); i++) {
                historyListMake(Utils.breathItem.getHistoryItems().get(i));
            }
        }else {
            linHistoryExist.setVisibility(GONE);
            for (int i = 0; i < Utils.breathItem.getHistoryItems().size(); i++) {
                historyListMake(Utils.breathItem.getHistoryItems().get(i));
            }
        }
    }
    void historyListMake(HistoryItems item) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_static_breath_history_list_item, null);
        TextView txtScore = listView.findViewById(R.id.static_breath_result_view_score);
        TextView txtInspiration = listView.findViewById(R.id.static_breath_result_view_inspiration);
        TextView txtTime = listView.findViewById(R.id.static_breath_result_view_time);

        if (item.getInspiratorFlag() == 1) {    // 사용
            txtInspiration.setText("사용");
        }else {
            txtInspiration.setText("미사용");
        }
        txtScore.setText("" + item.getPefScore());
//        2020-03-11 10:59:45
        txtTime.setText(item.getRegisterDt().substring(11,16));
        linHistoryParent.addView(listView);
    }

    void findViewByIds() {
        txtTitle = (TextView)findViewById(R.id.txt_statics_breath_detail_date);
        btnBack = (TextView) findViewById(R.id.txt_statics_detail_back);

        linBackground = findViewById(R.id.lin_static_breath_background);
        linHistoryParent = findViewById(R.id.lin_static_breath_result_history_list_parent);
        linHistoryExist = findViewById(R.id.lin_static_breath_data_exist);
        linHistoryNonExist = findViewById(R.id.lin_static_breath_data_non_exist);

        linBgChange     = findViewById(R.id.lin_static_breath_detail_bg_changes);

        linStaticBar = findViewById(R.id.lin_static_result_bar_bg);

        txtDanger = findViewById(R.id.txt_static_result_danger);
        txtWarning = findViewById(R.id.txt_static_result_warning);
        txtGood = findViewById(R.id.txt_static_result_good);

        imgDangerRange = findViewById(R.id.static_breath_result_dander_value_arrange);
        imgGoodRange = findViewById(R.id.static_breath_result_good_value_arrange);
        imgWarningRange = findViewById(R.id.static_breath_result_warning_value_arrange);

        txtMaxValue = findViewById(R.id.txt_static_breath_detail_max_value);
        txtMinValue = findViewById(R.id.txt_static_breath_detail_min_value);
        txtRateValue = findViewById(R.id.txt_static_breath_detail_rate_value);

        txtDetailTitle = findViewById(R.id.txt_static_breath_detail_status);
        txtDetailInfo = findViewById(R.id.txt_static_breath_detail_status_info);

        btnMoreDetail = findViewById(R.id.static_breath_result_detail_info);

        btnBack.setOnClickListener(this);
        btnMoreDetail.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_statics_detail_back : { // 뒤로 가기
                onBackPressed();
                break;
            }
            case R.id.static_breath_result_detail_info : {  // 상세보기 화면으로 이동
                Intent i = new Intent(this, StaticBreathNoResultDetailInfoActivity.class);
                startActivity(i);
                break;
            }
        }
    }
}
