package com.kmw.soom2.Home.Activitys.MedicineInsertActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.MyPage.Activity.MyHospitalInfoActivity;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_REVIEW_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.REVIEW_DELETE;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class MedicineReviewListActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String                      TAG = "";
    private ImageView                   mImageBack;
    private ScrollView                  mScrList;
    private LinearLayout                mLinearListParent;
    private LinearLayout                mLinearInsert;
    private Intent                      beforeIntent;
    private RequestOptions              requestOptions = new RequestOptions();
    private SimpleDateFormat            dateFormat3 = new SimpleDateFormat("yyyy");
    private int mReviewNo;
    private int mMedicineNo;
    private View mReviewRemoveView;
    private boolean resultCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_review_list);

        beforeIntent                    = getIntent();

        FindViewById();

        NetworkCall(MEDICINE_REVIEW_LIST);

    }

    void FindViewById(){
        mImageBack                      = findViewById(R.id.img_review_list_back);
        mScrList                        = findViewById(R.id.scr_review_list);
        mLinearListParent               = findViewById(R.id.lin_review_list_parent);
        mLinearInsert                   = findViewById(R.id.lin_medicine_insert_third_review_insert);

        mImageBack.setOnClickListener(this);
        mLinearInsert.setOnClickListener(this);

    }

    void ReviewList(boolean more, final int userNo, final int reviewNo, String name, String date, String lv, final String contents, final int sideEffectFlag, final String dosePeriod, String birth, int gender, String profilePath, String labelName, String labelColor) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.new_review_list_item, null);

        ImageView imgProfile = (ImageView)listView.findViewById(R.id.img_review_list_item_profile);
        ImageView imgShared = (ImageView)listView.findViewById(R.id.img_review_list_item_shared);
        ImageView imgMine = (ImageView)listView.findViewById(R.id.img_review_list_item_mine);
        TextView txtName = (TextView) listView.findViewById(R.id.txt_review_list_item_name);
        TextView txtDate = (TextView) listView.findViewById(R.id.txt_review_list_item_date);
        TextView txtInfo = (TextView) listView.findViewById(R.id.txt_review_list_item_birth_gender);
        TextView txtContents = (TextView) listView.findViewById(R.id.txt_review_list_item_contents);
        TextView txtPeriod = (TextView) listView.findViewById(R.id.txt_review_list_item_period);
        TextView txtEffect = (TextView) listView.findViewById(R.id.txt_review_list_item_effect);
        TextView txtMore = (TextView) listView.findViewById(R.id.txt_review_list_more);
        TextView txtEdit = (TextView)listView.findViewById(R.id.txt_review_list_edit);
        TextView txtLabel = (TextView)listView.findViewById(R.id.txt_review_list_item_label);
        LinearLayout linBg = (LinearLayout)listView.findViewById(R.id.lin_review_list_item_bg);
        ImageView imgMore = (ImageView)listView.findViewById(R.id.img_review_list_item_more);

        if (profilePath.length() > 0) {
            String replaceText = profilePath;
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            if (profilePath.contains("https:")) {
                Glide.with(MedicineReviewListActivity.this).load(replaceText).apply(requestOptions.circleCrop()).into(imgProfile);
            } else {
                Glide.with(MedicineReviewListActivity.this).load("https:" + replaceText).apply(requestOptions.circleCrop()).into(imgProfile);
            }
        } else {
            int resource = 0;
            if (gender == 1){
                resource = R.drawable.ic_no_profile_m;
            }else if (gender == 2){
                resource = R.drawable.ic_no_profile_w;
            }else{
                resource = R.drawable.ic_no_profile;
            }
            Glide.with(MedicineReviewListActivity.this).load(resource).apply(requestOptions.circleCrop()).into(imgProfile);
        }
        if (labelName.length() != 0){
            txtLabel.setText(labelName);
        }else{
            txtLabel.setVisibility(View.GONE);
        }

        if (labelColor.length() != 0){
            txtLabel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(labelColor)));
        }

        if (userItem.getUserNo() == userNo){
            imgMine.setVisibility(View.VISIBLE);
            imgShared.setVisibility(View.VISIBLE);
            txtEdit.setVisibility(View.VISIBLE);
            linBg.setBackgroundTintList(getColorStateList(R.color.F2F7F5));
            imgMore.setVisibility(View.GONE);
        }else{
            imgMine.setVisibility(View.GONE);
            imgShared.setVisibility(View.GONE);
            txtEdit.setVisibility(View.GONE);
            imgMore.setVisibility(View.VISIBLE);
        }

        txtDate.setText(date.substring(2, 10).replace("-","."));
        txtName.setText(name);

        txtPeriod.setText(dosePeriod);

        String effect = "";
        if (sideEffectFlag == 1) {
            effect = "있었어요";
            txtEffect.setText("있었어요");
        } else if (sideEffectFlag == 2) {
            effect = "없었어요";
            txtEffect.setText("없었어요");
        } else if (sideEffectFlag == 3) {
            effect = "모르겠어요";
            txtEffect.setText("모르겠어요");
        }

        txtContents.setText(contents);

        if (more){
            txtMore.setVisibility(View.GONE);
            txtContents.setMaxLines(Integer.MAX_VALUE);
            View finalListView1 = listView;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScrList.smoothScrollTo(0, finalListView1.getTop());
                }
            }, 500);
        }else{
            if (txtContents.getLineCount() == 0) {
                txtContents.post(() -> {
                    int lineCount = txtContents.getLineCount();

                    if (lineCount > 3 ){
                        txtMore.setVisibility(View.VISIBLE);
                    } else {
                        txtMore.setVisibility(View.GONE);
                        txtContents.setMaxLines(Integer.MAX_VALUE);
                    }
                });
            }else {
                if (txtContents.getLineCount() > 3){
                    txtMore.setVisibility(View.VISIBLE);
                }else{
                    txtMore.setVisibility(View.GONE);
                    txtContents.setMaxLines(Integer.MAX_VALUE);
                }
            }
        }

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new BottomSheetDialog(MedicineReviewListActivity.this, R.style.SheetDialog);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View contentView = inflater.inflate(R.layout.dialog_community_comment_guest_url, null);

                TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_community_guest_cancel);
                TextView txtInquiry = (TextView)contentView.findViewById(R.id.txt_dialog_community_guest_inquiry);

                dialog.setContentView(contentView);

                txtInquiry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Toast.makeText(MedicineReviewListActivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtContents.setMaxLines(Integer.MAX_VALUE);
                v.setVisibility(View.GONE);
            }
        });

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MedicineReviewListActivity.this, MedicineReviewWriteActivity.class);
                i.putExtra("reviewNo", reviewNo);
                i.putExtra("medicineNo", beforeIntent.getIntExtra("medicineNo", 0));
                i.putExtra("contents", contents);
                i.putExtra("sideEffectFlag", sideEffectFlag);
                i.putExtra("dosePeriod", dosePeriod);
                startActivityForResult(i,1111);
            }
        });

        String finalEffect = effect;
        imgShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MedicineReviewListActivity.this, CommunityWriteActivity.class);
                i.putExtra("medicine",true);
                i.putExtra("menuNo","2");
                i.putExtra("cMenuNo","22");
                i.putExtra("menuTitle","후기있숨");
                i.putExtra("cMenuTitle","약 후기");
                i.putExtra("contents","약 : " + beforeIntent.getStringExtra("medicineName") + "\n복용기간 : " + dosePeriod + "\n" + "부작용여부 : " + finalEffect + "\n\n" + contents);
                startActivityForResult(i,2222);
            }
        });

        mLinearListParent.addView(listView);
    }

    public static void setReadMore(final TextView view, final String text, final int maxLine) {
        final Context context = view.getContext();
        final String expanedText = "···더보기";

        if (view.getTag() != null && view.getTag().equals(text)) { //Tag로 전값 의 text를 비교하여똑같으면 실행하지 않음.
            return;
        }
        view.setTag(text); //Tag에 text 저장
        view.setText(text); // setText를 미리 하셔야  getLineCount()를 호출가능

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getLineCount() == maxLine){

                }else if (view.getLineCount() > maxLine) { //Line Count가 설정한 MaxLine의 값보다 크다면 처리시작

                    int lineEndIndex = view.getLayout().getLineVisibleEnd(maxLine - 1); //Max Line 까지의 text length

                    String[] split = text.split("\n"); //text를 자름
                    int splitLength = 0;

                    String lessText = "";
                    for (String item : split) {
                        splitLength += item.length() + 1;
                        if (splitLength >= lineEndIndex) { //마지막 줄일때!
                            if (item.length() >= expanedText.length()) {
                                lessText += item.substring(0, item.length() - (expanedText.length())) + expanedText;
                            } else {
                                lessText += item + expanedText;
                            }
                            break; //종료
                        }
                        lessText += item + "\n";
                    }
                    SpannableString spannableString = new SpannableString(lessText);
                    spannableString.setSpan(new ClickableSpan() {//클릭이벤트
                        @Override
                        public void onClick(View v) {
                            view.setText(text);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) { //컬러 처리
                            ds.setColor(ContextCompat.getColor(context, R.color.black));
                            ds.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        }
                    }, spannableString.length() - expanedText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.setText(spannableString);
                    view.setMovementMethod(LinkMovementMethod.getInstance());
                }
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public static void Twopopup(final Context context) {
        final Dialog dialog = new BottomSheetDialog(context, R.style.SheetDialog);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_comment_two_btn, null);

        TextView txtDeclaration = (TextView) contentView.findViewById(R.id.txt_dialog_comment_two_declaration);
        TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_comment_two_cancel);

        dialog.setContentView(contentView);

        txtDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(context, InquiryActivity.class);
                i.putExtra("inquiry", "inquiry");
                context.startActivity(i);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    void NetworkCall(String mCode){
        if (mCode.equals(MEDICINE_REVIEW_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute("" + beforeIntent.getIntExtra("medicineNo", 0));
        }else if (mCode.equals(REVIEW_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+mReviewNo,""+beforeIntent.getIntExtra("medicineNo", 0));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mCode.equals(MEDICINE_REVIEW_LIST)){

            mLinearListParent.removeAllViews();

            try {
                JSONObject jsonObject = new JSONObject(mResult);

                if (jsonObject.has("list")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.i(TAG,"review : " + object.toString());
                        JSONObject userObject = object.getJSONObject("clsUserBean");
                        if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                            if (beforeIntent.hasExtra("reviewNo")){
                                if (beforeIntent.getIntExtra("reviewNo",0) == JsonIntIsNullCheck(object, "REVIEW_NO")){
                                    ReviewList(true,JsonIntIsNullCheck(object, "USER_NO"), JsonIntIsNullCheck(object, "REVIEW_NO"),
                                            JsonIsNullCheck(object, "NICKNAME"), JsonIsNullCheck(object, "UPDATE_DT").length() != 0 ? JsonIsNullCheck(object, "UPDATE_DT") : JsonIsNullCheck(object, "CREATE_DT"),
                                            JsonIsNullCheck(object, "LV"), JsonIsNullCheck(object, "CONTENTS"),
                                            JsonIntIsNullCheck(object, "SIDE_EFFECT_FLAG"), JsonIsNullCheck(object, "DOSE_PERIOD"),
                                            JsonIsNullCheck(object, "BIRTH"),JsonIntIsNullCheck(object,"GENDER"),JsonIsNullCheck(object,"PROFILE_IMG"),JsonIsNullCheck(object,"LABEL_NAME"),JsonIsNullCheck(object,"LABEL_COLOR"));
                                }else{
                                    ReviewList(false,JsonIntIsNullCheck(object, "USER_NO"), JsonIntIsNullCheck(object, "REVIEW_NO"),
                                            JsonIsNullCheck(object, "NICKNAME"), JsonIsNullCheck(object, "UPDATE_DT").length() != 0 ? JsonIsNullCheck(object, "UPDATE_DT") : JsonIsNullCheck(object, "CREATE_DT"),
                                            JsonIsNullCheck(object, "LV"), JsonIsNullCheck(object, "CONTENTS"),
                                            JsonIntIsNullCheck(object, "SIDE_EFFECT_FLAG"), JsonIsNullCheck(object, "DOSE_PERIOD"),
                                            JsonIsNullCheck(object, "BIRTH"),JsonIntIsNullCheck(object,"GENDER"),JsonIsNullCheck(object,"PROFILE_IMG"),JsonIsNullCheck(object,"LABEL_NAME"),JsonIsNullCheck(object,"LABEL_COLOR"));
                                }
                            }else{
                                ReviewList(false,JsonIntIsNullCheck(object, "USER_NO"), JsonIntIsNullCheck(object, "REVIEW_NO"),
                                        JsonIsNullCheck(object, "NICKNAME"), JsonIsNullCheck(object, "UPDATE_DT").length() != 0 ? JsonIsNullCheck(object, "UPDATE_DT") : JsonIsNullCheck(object, "CREATE_DT"),
                                        JsonIsNullCheck(object, "LV"), JsonIsNullCheck(object, "CONTENTS"),
                                        JsonIntIsNullCheck(object, "SIDE_EFFECT_FLAG"), JsonIsNullCheck(object, "DOSE_PERIOD"),
                                        JsonIsNullCheck(object, "BIRTH"),JsonIntIsNullCheck(object,"GENDER"),JsonIsNullCheck(object,"PROFILE_IMG"),JsonIsNullCheck(object,"LABEL_NAME"),JsonIsNullCheck(object,"LABEL_COLOR"));
                            }

                        }
                    }
                }
            } catch (JSONException e) {

            }
        }else if (mCode.equals(REVIEW_DELETE)){
            mLinearListParent.removeView(mReviewRemoveView);
            if (mLinearListParent.getChildCount() == 0){
                new OneButtonDialog(MedicineReviewListActivity.this,"약 후기 전체보기","존재하는 약 후기가 없습니다.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                    @Override
                    public void OnButtonClick(View v) {
                        resultCheck = true;
                        onBackPressed();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111){
            if (resultCode == RESULT_OK){
                resultCheck = true;
                NetworkCall(MEDICINE_REVIEW_LIST);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (resultCheck){
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_review_list_back : {
                onBackPressed();
                break;
            }
            case R.id.lin_medicine_insert_third_review_insert : {
                Intent intent = new Intent(this,MedicineReviewWriteActivity.class);
                intent.putExtra("medicineNo", beforeIntent.getIntExtra("medicineNo", 0));
                startActivity(intent);
                break;
            }
        }
    }
}