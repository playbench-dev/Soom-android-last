package com.kmw.soom2.Login.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kmw.soom2.R;

public class AgreeActivity extends AppCompatActivity implements View.OnClickListener {

    private String                      TAG = "AgreeActivity";
    private TextView                    mBackTextView;
    private TextView                    mTextViewCategory;
    private WebView                     mWebView;

    private Intent                      mBeforeIntent;
    private String[]                    mCategoryList = new String[]{"서비스 이용약관","개인정보처리방침","개인민감정보처리방침"};
    private BottomSheetDialog           mCategoryBottomSheetDialog;
    private View                        mBottomSheetDialogCategory;
    private LinearLayout                mLinearBottomSheetListParent;
    private Button                      mButtonService;
    private Button                      mButtonPersonal;
    private Button                      mButtonSensitivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yk);

        mBeforeIntent                   = getIntent();

        mBackTextView                   = (TextView) findViewById(R.id.txt_agree_detail_back);
        mTextViewCategory               = (TextView) findViewById(R.id.txt_agree_detail_category);
        mWebView                        = (WebView) findViewById(R.id.webView_agree_detail);

        mCategoryBottomSheetDialog      = new BottomSheetDialog(this);
        mBottomSheetDialogCategory      = getLayoutInflater().inflate(R.layout.agree_category_popup, null);
        mLinearBottomSheetListParent    = (LinearLayout)mBottomSheetDialogCategory.findViewById(R.id.lin_agree_category_popup_list_parent);
        mButtonService                  = (Button)mBottomSheetDialogCategory.findViewById(R.id.btn_agree_category_popup_service);
        mButtonPersonal                 = (Button)mBottomSheetDialogCategory.findViewById(R.id.btn_agree_category_popup_personal);
        mButtonSensitivity              = (Button)mBottomSheetDialogCategory.findViewById(R.id.btn_agree_category_popup_sensitivity);

        mCategoryBottomSheetDialog.setContentView(mBottomSheetDialogCategory);

        mButtonService.setText(mCategoryList[0]);
        mButtonPersonal.setText(mCategoryList[1]);
        mButtonSensitivity.setText(mCategoryList[2]);

        if (mBeforeIntent.hasExtra("flag")){
            if (mBeforeIntent.getIntExtra("flag",0) == 1){
                mTextViewCategory.setText(mCategoryList[0]);
                mWebView.loadUrl("file:///android_asset/soom_service.html");
            }else if (mBeforeIntent.getIntExtra("flag",0) == 2){
                mTextViewCategory.setText(mCategoryList[1]);
                mWebView.loadUrl("file:///android_asset/soom_personal.html");
            }else if (mBeforeIntent.getIntExtra("flag",0) == 3){
                mTextViewCategory.setText(mCategoryList[2]);
                mWebView.loadUrl("file:///android_asset/soom_sensitivity.html");
            }
        }else{
            mTextViewCategory.setText("서비스 이용약관");
            mWebView.loadUrl("file:///android_asset/soom_service.html");
        }

        mBackTextView.setOnClickListener(this);
        mTextViewCategory.setOnClickListener(this);
        mButtonService.setOnClickListener(this);
        mButtonPersonal.setOnClickListener(this);
        mButtonSensitivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_agree_detail_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_agree_detail_category : {
                mCategoryBottomSheetDialog.show();
                break;
            }
            case R.id.btn_agree_category_popup_service : {
                mCategoryBottomSheetDialog.dismiss();
                mTextViewCategory.setText(mCategoryList[0]);
                mWebView.loadUrl("file:///android_asset/soom_service.html");
                break;
            }
            case R.id.btn_agree_category_popup_personal : {
                mCategoryBottomSheetDialog.dismiss();
                mTextViewCategory.setText(mCategoryList[1]);
                mWebView.loadUrl("file:///android_asset/soom_personal.html");
                break;
            }
            case R.id.btn_agree_category_popup_sensitivity : {
                mCategoryBottomSheetDialog.dismiss();
                mTextViewCategory.setText(mCategoryList[2]);
                mWebView.loadUrl("file:///android_asset/soom_sensitivity.html");
                break;
            }
        }
    }
}
