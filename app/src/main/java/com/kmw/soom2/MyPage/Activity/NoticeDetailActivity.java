package com.kmw.soom2.MyPage.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_LIST_ALL_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NOTICE_DETAIL_CALL;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class NoticeDetailActivity extends AppCompatActivity implements AsyncResponse {
    final String TAG = "NoticeDetailActivity";
    private WebView webView;
    TextView txtBack;
    OneButtonDialog oneButtonDialog;
    Intent beforeIntent;
    Pattern pattern = Pattern.compile("(soomcare:\\/\\/www\\.|SoomCare:\\/\\/www\\.|soomcare:\\/\\/|SoomCare:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

        NetworkCall(NOTICE_DETAIL_CALL);

    }

    Linkify.TransformFilter mTransform = new Linkify.TransformFilter() {
        @Override
        public String transformUrl(Matcher match, String url) {
            return url;
        }
    };

    void FindViewById() {
        webView = findViewById(R.id.webview_notice_detail);
        txtBack = (TextView)findViewById(R.id.txt_notice_detail_back);

        webView.setBackgroundColor(0); //배경색
        webView.setHorizontalScrollBarEnabled(false); //가로 스크롤
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setTextZoom(100);

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.i(TAG,"url : " + String.valueOf(request.getUrl()));
//            if (String.valueOf(request.getUrl()).contains("SoomCare")){
//                Linkify.addLinks(txtBack, pattern, "communitytest://?url=" + String.valueOf(request.getUrl()) , null, mTransform);
//            }else if (String.valueOf(request.getUrl()).contains("soomcare")){
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                intent.setData(Uri.parse("communitytest://?url="+String.valueOf(request.getUrl())));
//                startActivity(intent);
//            }else{
//                view.loadUrl(String.valueOf(request.getUrl()));
//            }
            //memo google forum일 경우 처리
            if (String.valueOf(request.getUrl()).startsWith("intent://")){
                try {
                    Intent intent = Intent.parseUri(String.valueOf(request.getUrl()), Intent.URI_INTENT_SCHEME);
                    Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                    if (intent != null) {
                        String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                        Log.i(TAG,"fallbackUrl  : " + fallbackUrl);
                        if (fallbackUrl != null) {
                            view.loadUrl(fallbackUrl);
                        } else {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (String.valueOf(request.getUrl()).startsWith("soomcare://")){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                Log.i(TAG,"String.valueOf(request.getUrl()) : " + String.valueOf(request.getUrl()));
                if (String.valueOf(request.getUrl()).contains("community")){
                    if (String.valueOf(request.getUrl()).contains("all")){
                        intent.setData(Uri.parse("main://community/all/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("contents")){
                        intent.setData(Uri.parse("main://community/contents/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("notice")){
                        intent.setData(Uri.parse("main://community/notice/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("write")){
                        intent.setData(Uri.parse("main://community/write/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("search")){
                        intent.setData(Uri.parse("main://community/search/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("community_detail")){
                        intent.setData(Uri.parse("main://community_detail?url="+String.valueOf(request.getUrl())));
                    }else{
                        intent.setData(Uri.parse("main://community/tab/?url="+String.valueOf(request.getUrl())));
                    }
                }else if (String.valueOf(request.getUrl()).contains("home")){
                    if (String.valueOf(request.getUrl()).contains("medicine")){
                        intent.setData(Uri.parse("main://home/medicine/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("symptom")){
                        intent.setData(Uri.parse("main://home/symptom/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("pef")){
                        intent.setData(Uri.parse("main://home/pef/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("memo")){
                        intent.setData(Uri.parse("main://home/memo/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("act")){
                        intent.setData(Uri.parse("main://home/act/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("dust")){
                        intent.setData(Uri.parse("main://home/dust/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("asthma")){
                        intent.setData(Uri.parse("main://home/asthma/?url="+String.valueOf(request.getUrl())));
                    }else{
                        intent.setData(Uri.parse("main://home/tab/?url="+String.valueOf(request.getUrl())));
                    }
                }else if (String.valueOf(request.getUrl()).contains("manage")){
                    if (String.valueOf(request.getUrl()).contains("medicine")){
                        intent.setData(Uri.parse("main://manage/medicine/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("alarm")){
                        intent.setData(Uri.parse("main://manage/alarm/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("search")){
                        intent.setData(Uri.parse("main://manage/search/?url="+String.valueOf(request.getUrl())));
                    }else{
                        intent.setData(Uri.parse("main://manage/tab/?url="+String.valueOf(request.getUrl())));
                    }
                }else if (String.valueOf(request.getUrl()).contains("report")){
                    if (String.valueOf(request.getUrl()).contains("medicine")){
                        intent.setData(Uri.parse("main://report/medicine/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("symptom")){
                        intent.setData(Uri.parse("main://report/symptom/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("pef")){
                        intent.setData(Uri.parse("main://report/pef/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("act")){
                        intent.setData(Uri.parse("main://report/act/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("total")){
                        intent.setData(Uri.parse("main://report/total/?url="+String.valueOf(request.getUrl())));
                    }else{
                        intent.setData(Uri.parse("main://report/tab/?url="+String.valueOf(request.getUrl())));
                    }
                }else if (String.valueOf(request.getUrl()).contains("mypage")){

                    if (String.valueOf(request.getUrl()).contains("hospital")){
                        intent.setData(Uri.parse("main://mypage/hospital/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("inquery")){
                        intent.setData(Uri.parse("main://mypage/inquery/?url="+String.valueOf(request.getUrl())));
                    }else if (String.valueOf(request.getUrl()).contains("notice")){
                        intent.setData(Uri.parse("main://mypage/notice/?url="+String.valueOf(request.getUrl())));
                    }else{
                        intent.setData(Uri.parse("main://mypage/tab/?url="+String.valueOf(request.getUrl())));
                    }
                }else if (String.valueOf(request.getUrl()).contains("login")){
                    intent.setData(Uri.parse("login://login"));
                }
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else if (String.valueOf(request.getUrl()).startsWith("https://play.google.com")){
                Intent i = new Intent(Intent.ACTION_VIEW, request.getUrl());
                startActivity(i);
            }else{
                view.loadUrl(String.valueOf(request.getUrl()));
            }
            return true;
        }

        //사용하면 안됨 - 구글 정책 위반
//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();
//            super.onReceivedSslError(view,handler,error);
//        }
    }


    @Override
    public void onBackPressed() {
        if (beforeIntent.hasExtra("push")){
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    void NetworkCall(String mCode){
        if (mCode.equals(NOTICE_DETAIL_CALL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("noticeNo"));
        }else if (mCode.equals(ALARM_LIST_ALL_READ)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+String.valueOf(userItem.getUserNo()));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(NOTICE_DETAIL_CALL)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.getJSONObject(0).getInt("ALIVE_FLAG") == 1){
//                        webView.loadUrl("https://choomo.app/999902");
                        webView.loadUrl(Utils.Server.noticeDetail() + beforeIntent.getStringExtra("noticeNo"));
                    }else{
                        oneButtonDialog= new OneButtonDialog(NoticeDetailActivity.this, "공지사항", "삭제된 공지사항입니다.", "확인");
                        oneButtonDialog.setCancelable(false);
                        oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                onBackPressed();
                            }
                        });
                    }

                    if (beforeIntent.hasExtra("keyNo") || beforeIntent.hasExtra("push")){
                        NetworkCall(ALARM_LIST_ALL_READ);
                    }
                }catch (JSONException e){

                }
            }
        }
    }
}
