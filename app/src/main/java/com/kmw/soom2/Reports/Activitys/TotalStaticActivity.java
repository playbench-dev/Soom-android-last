package com.kmw.soom2.Reports.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class TotalStaticActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "TotalStaticActivity";
    private TextView txtBack;
    private WebView webView;
    private ImageView imgShared;
    private String mUrl = "https://soomcare.info/mobile_report_list?USER_NO=";
//    private String mUrl = "http://192.168.0.17:8080/mobile_report_list?USER_NO=";
//    private String mUrl = "http://192.168.0.33:8080/community?USER_NO=1557&COMMUNITY_NO=210";
    private String mSharedUrl = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_static);

        NullCheck(this);
        
        FindViewById();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_total_static_back);
        webView = (WebView)findViewById(R.id.webView_total_statics);
        imgShared = (ImageView)findViewById(R.id.img_total_statics_shared);

        imgShared.setVisibility(View.GONE);

        txtBack.setOnClickListener(this);
        imgShared.setOnClickListener(this);

        progressDialog = new ProgressDialog(this,R.style.MyTheme);
        progressDialog.show();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            webSettings.setTextZoom(100);
        webView.setWebViewClient(new WebViewClientClass());

        mUrl += userItem.getUserNo();
        webView.loadUrl(mUrl);

    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG,"shouldOverrideUrlLoading url : " + url);
            progressDialog.show();
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG,"onPageStarted url : " + url);
            progressDialog.dismiss();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

//            if (url.contains("mobile_report_list")){
//                Toast.makeText(TotalStaticActivity.this, "aaaa", Toast.LENGTH_SHORT).show();
//            }
            if (url.contains("mobile_report_list")){
                Log.i(TAG,"onPageFinished url1 : " + url);
                imgShared.setVisibility(View.GONE);
            }else{
                Log.i(TAG,"onPageFinished url2 : " + url);
                mSharedUrl = url;
                imgShared.setVisibility(View.VISIBLE);
            }
            super.onPageFinished(view, url);

        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_total_static_back :
                onBackPressed();
                break;
            case R.id.img_total_statics_shared :
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, mSharedUrl);
                startActivity(Intent.createChooser(sharingIntent,"공유하기"));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
