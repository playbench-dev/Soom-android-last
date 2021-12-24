package com.kmw.soom2.Home.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebBackForwardList;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static com.kmw.soom2.Common.Utils.NullCheck;

public class UrlWebViewActivity extends AppCompatActivity {
    private String TAG = "UrlWebViewActivity";
    WebView webView;
    TextView txtTitle;
    Intent beforeIntent;
    TextView txtBack;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_web_view);

        beforeIntent = getIntent();

        FindViewById();

        if (beforeIntent.hasExtra("title")){
            txtTitle.setText(beforeIntent.getStringExtra("title"));
        }

        NullCheck(this);

    }

    void FindViewById() {
        webView = findViewById(R.id.webview_url_web_view);
        txtBack = (TextView) findViewById(R.id.txt_url_web_view_back);
        txtTitle = (TextView)findViewById(R.id.txt_web_view_title);
        url = beforeIntent.getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new MyWebViewClient());

        if (beforeIntent.hasExtra("url")) {
            Log.i(TAG,"url : " + url);
            webView.loadUrl(url);
        }

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
                if (beforeIntent.hasExtra("guest")){
//                    if (beforeIntent.hasExtra("around")){
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                        if (String.valueOf(request.getUrl()).contains("community")){
//                            if (String.valueOf(request.getUrl()).contains("all")){
//                                intent.setData(Uri.parse("exmain://community/all/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("contents")){
//                                intent.setData(Uri.parse("exmain://community/contents/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("notice")){
//                                intent.setData(Uri.parse("exmain://community/notice/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("write")){
//                                intent.setData(Uri.parse("exmain://community/write/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("search")){
//                                intent.setData(Uri.parse("exmain://community/search/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("community_detail")){
//                                intent.setData(Uri.parse("exmain://community_detail?url="+String.valueOf(request.getUrl())));
//                            }else{
//                                intent.setData(Uri.parse("exmain://community/tab/?url="+String.valueOf(request.getUrl())));
//                            }
//                        }else if (String.valueOf(request.getUrl()).contains("home")){
//                            if (String.valueOf(request.getUrl()).contains("medicine")){
//                                intent.setData(Uri.parse("exmain://home/medicine/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("symptom")){
//                                intent.setData(Uri.parse("exmain://home/symptom/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("pef")){
//                                intent.setData(Uri.parse("exmain://home/pef/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("memo")){
//                                intent.setData(Uri.parse("exmain://home/memo/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("act")){
//                                intent.setData(Uri.parse("exmain://home/act/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("dust")){
//                                intent.setData(Uri.parse("exmain://home/dust/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("asthma")){
//                                intent.setData(Uri.parse("exmain://home/asthma/?url="+String.valueOf(request.getUrl())));
//                            }else{
//                                intent.setData(Uri.parse("exmain://home/tab/?url="+String.valueOf(request.getUrl())));
//                            }
//                        }else if (String.valueOf(request.getUrl()).contains("manage")){
//                            if (String.valueOf(request.getUrl()).contains("medicine")){
//                                intent.setData(Uri.parse("exmain://manage/medicine/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("alarm")){
//                                intent.setData(Uri.parse("exmain://manage/alarm/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("search")){
//                                intent.setData(Uri.parse("exmain://manage/search/?url="+String.valueOf(request.getUrl())));
//                            }else{
//                                intent.setData(Uri.parse("exmain://manage/tab/?url="+String.valueOf(request.getUrl())));
//                            }
//                        }else if (String.valueOf(request.getUrl()).contains("report")){
//                            if (String.valueOf(request.getUrl()).contains("medicine")){
//                                intent.setData(Uri.parse("exmain://report/medicine/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("symptom")){
//                                intent.setData(Uri.parse("exmain://report/symptom/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("pef")){
//                                intent.setData(Uri.parse("exmain://report/pef/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("act")){
//                                intent.setData(Uri.parse("exmain://report/act/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("total")){
//                                intent.setData(Uri.parse("exmain://report/total/?url="+String.valueOf(request.getUrl())));
//                            }else{
//                                intent.setData(Uri.parse("exmain://report/tab/?url="+String.valueOf(request.getUrl())));
//                            }
//                        }else if (String.valueOf(request.getUrl()).contains("mypage")){
//
//                            if (String.valueOf(request.getUrl()).contains("hospital")){
//                                intent.setData(Uri.parse("exmain://mypage/hospital/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("inquery")){
//                                intent.setData(Uri.parse("exmain://mypage/inquery/?url="+String.valueOf(request.getUrl())));
//                            }else if (String.valueOf(request.getUrl()).contains("notice")){
//                                intent.setData(Uri.parse("exmain://mypage/notice/?url="+String.valueOf(request.getUrl())));
//                            }else{
//                                intent.setData(Uri.parse("exmain://mypage/tab/?url="+String.valueOf(request.getUrl())));
//                            }
//                        }
//                        intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }else{
//                        Intent intent = new Intent(UrlWebViewActivity.this, exNewAnotherActivity.class);
//                        startActivity(intent);
//                        onBackPressed();
//                    }
                    Intent intent = new Intent(UrlWebViewActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
//                    onBackPressed();
                }else{
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
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
                }
            }else{
                view.loadUrl(String.valueOf(request.getUrl()));
            }
            return true;
        }

//        @Override
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed();
//        }
    }

    @Override
    public void onBackPressed() {
//        WebBackForwardList list = webView.copyBackForwardList();
//        if (list.getCurrentIndex() <= 1 || !webView.canGoBack()) {
//
//        } else {
//            webView.goBack();
//        }
        super.onBackPressed();
    }
}
