package com.kmw.soom2.ex.Static;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Activitys.TotalStaticActivity;
import com.kmw.soom2.ex.exNewAnotherActivity;

import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class exStaticTotalActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "";
    private TextView txtBack;
    private WebView webView;
    private ImageView imgShared;
    private Button btnMove;
    private String mUrl = "https://soomcare.info/report_example";
    //    private String mUrl = "http://192.168.0.17:8080/mobile_report_list?USER_NO=";
//    private String mUrl = "http://192.168.0.33:8080/community?USER_NO=1557&COMMUNITY_NO=210";
    private String mSharedUrl = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_static_total);

        FindViewById();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_total_static_back);
        webView = (WebView)findViewById(R.id.webView_total_statics);
        imgShared = (ImageView)findViewById(R.id.img_total_statics_shared);
        btnMove = (Button)findViewById(R.id.btn_ex_total_static_move);

        imgShared.setVisibility(View.GONE);

        txtBack.setOnClickListener(this);
        imgShared.setOnClickListener(this);
        btnMove.setOnClickListener(this);

        progressDialog = new ProgressDialog(this,R.style.MyTheme);

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

        webView.loadUrl(mUrl);

        imgShared.setVisibility(View.GONE);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG,"url : " + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.i(TAG,"url : " + url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            progressDialog.dismiss();
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
            case  R.id.btn_ex_total_static_move : {
                Intent intent = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(intent);
                break;
            }
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