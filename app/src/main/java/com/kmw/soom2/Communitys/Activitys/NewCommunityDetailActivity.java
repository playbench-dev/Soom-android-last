package com.kmw.soom2.Communitys.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.kmw.soom2.Common.Item.ExpandableTextView;
import com.kmw.soom2.Common.Item.FlowLayout;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.TempUtil;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Adapters.CommunityViewPagerAdapter;
import com.kmw.soom2.Communitys.Adapters.NewCommunityWebAdapter;
import com.kmw.soom2.Communitys.Items.CommentItem;
import com.kmw.soom2.Communitys.Items.CustomViewPager;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.MyPage.Activity.InquiryActivity;
import com.kmw.soom2.MyPage.Fragment.NewMineFragment;
import com.kmw.soom2.R;
import com.kmw.soom2.SplashActivity;
import com.kmw.soom2.Views.MentionTestEdit;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_LIST_ALL_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_CALL_DETAIL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_COMMENT_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_COMMENT_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_COMMENT_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_COMMENT_REPLY_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_COMMENT_REPLY_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_COMMENT_REPLY_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_COMMENT_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_LIKE_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_LIKE_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_SCRAP_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_SCRAP_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMENT_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMENT_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMENT_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMENT_REPLY_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMENT_REPLY_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMENT_REPLY_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMENT_UPDATE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_DETAIL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_VIEWS_UP;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class NewCommunityDetailActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "NewCommunityDetailActivity";
    TextView txtBack;
    LinearLayout linFocus;
    private ImageView imgProfile, imgMore;
    private CustomViewPager viewPager;
    private TextView txtName, txtDate, txtLike, txtComment;
    private FlowLayout linTag;
    private TextView txtContents;
    private ImageView imgLike;
    private LinearLayout linShared;
    private LinearLayout linLike;
    private TextView  txtLikeIcon;
    private TextView txtMore;
    private TabLayout tabLayout;
    private LinearLayout tabStip;
    private FrameLayout frameLayout;
    public static LinearLayout linMentionParent;
    private TextView txtViews;
    private ImageView imgViews;
    private TextView txtLabel;
    private TextView txtCategory;
    private ImageView imgPin;

    LinearLayout linCommentParent,linCommentNo;
    LinearLayout linCommentMentionParent;
    MentionTestEdit edtComment;
    //    EditText edtComment;
    Button btnCommentSend;
    Intent beforeIntent;
    String selectCommentNo = "";
    ScrollView scrollView;
    public static HorizontalScrollView scrMention;
    InputMethodManager imm;
    int status = 1;
    boolean pushFlag;
    RequestOptions requestOptions = new RequestOptions();
    Display display;
    WindowManager wm;
    CommunityViewPagerAdapter adapter;

    int mLikeCnt,mCommentCnt;
    String mImagesNo = "";
    String mUserNo;
    ArrayList<String> commentNoOverlapList = new ArrayList<>();
    ArrayList<String> commentReplyNoOverlapList = new ArrayList<>();
    private NickNameSearchNetWork nickNameSearchNetWork;
    public static boolean mentionEnabled = false;
    public static int mentionStart = 0;
    int mentionEnd = 0;
    String mentionStr = "";

    List<Integer> mentionLengthList = new ArrayList<>();
    List<String> mentionNicknameList = new ArrayList<>();
    List<Integer> mentionLocationList = new ArrayList<>();

    public static ArrayList<Integer> mentionUserNoList = new ArrayList<>();
    public static ArrayList<String> mentionSelectList = new ArrayList<>();

    int viewHeight = -1;

    View rootView;
    boolean isKeyBoardShowing = false;
    int keyboardHeight;
    int screenHeight;
    int spaceHeight;

    OneButtonDialog oneButtonDialog;

    boolean communityDetailRefresh = false;

    private String mContents = "";
    private String mHashtag = "";
    private String mImagsPath = "";

    int scrapFlag = 0;
    public RequestManager requestManager;

    private ProgressDialog progressDialog;
    private String mMenuNo = "", mCMenuNo = "", mMenuTitle = "", mCMenuTitle = "";
    private String groupNum = "", originCommentNo = "", originReCommentNo = "";


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_community_detail);

        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        requestManager = Glide.with(this);

        beforeIntent = getIntent();

//        Log.i(TAG,"communityNo test : " + getIntent().getData().getQueryParameter("no"));

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        FindViewById();

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();

        if (beforeIntent.hasExtra("communityNo")){
            if (beforeIntent.getStringExtra("communityNo").length() == 0){
                onBackPressed();
            }else{
                Log.i(TAG,"viewHeight : " + viewHeight);
                NullCheck(this);

                if (Utils.userItem == null){
                    Intent i = new Intent(NewCommunityDetailActivity.this, SplashActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (beforeIntent.hasExtra("android_channel_id")){
                        i.putExtra("android_channel_id",beforeIntent.getStringExtra("android_channel_id"));
                        i.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
                        i.putExtra("push",true);
                    }
                    finish();
                    startActivity(i);
                }else {
                    if (beforeIntent.hasExtra("comment1")) {

                    }else if (beforeIntent.hasExtra("comment")){
                        edtComment.setFocusableInTouchMode(true);
                        edtComment.requestFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(edtComment, 1);
                    }
                    Log.i(TAG,"time start : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())));
                    NetWorkCall(NEW_COMMUNITY_DETAIL);
                }

                if (beforeIntent.hasExtra("guest")){
                    edtComment.setHint("로그인을 해주세요.");
                    btnCommentSend.setEnabled(false);
                    btnCommentSend.setBackgroundTintList(getColorStateList(R.color.dbdbdb));
                }
            }
        }else{
            onBackPressed();
        }
    }

    void FindViewById(){
        rootView = this.getWindow().getDecorView();
        txtBack = (TextView) findViewById(R.id.txt_community_detail_back);
        linCommentParent = (LinearLayout) findViewById(R.id.lin_community_detail_comment_parent);
        linCommentNo = (LinearLayout) findViewById(R.id.lin_community_detail_comment_no);
        edtComment = (MentionTestEdit) findViewById(R.id.edt_community_detail_comment);
        linCommentMentionParent = (LinearLayout)findViewById(R.id.lin_community_detail_mention_parent);
        btnCommentSend = (Button) findViewById(R.id.btn_community_detail_comment_send);
        scrollView = (ScrollView) findViewById(R.id.src_community_detail);
        imgProfile = (ImageView) findViewById(R.id.img_community_detail_profile);
        imgMore = (ImageView) findViewById(R.id.img_community_detail_more);
        txtName = (TextView) findViewById(R.id.txt_community_detail_name);
        txtDate = (TextView) findViewById(R.id.txt_community_detail_date);
        txtContents = (TextView) findViewById(R.id.txt_community_detail_contents);
        txtLike = (TextView) findViewById(R.id.txt_community_detail_like_cnt);
        txtComment = (TextView) findViewById(R.id.txt_community_detail_comment_cnt);
        viewPager = (CustomViewPager) findViewById(R.id.view_pager_community_detail_picture);
        imgLike = (ImageView) findViewById(R.id.img_community_detail_like_icon);
        linTag = (FlowLayout)findViewById(R.id.lin_community_detail_tag_parent);
        linShared = (LinearLayout) findViewById(R.id.lin_community_detail_shared_icon);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_community_detail);
        txtLikeIcon = (TextView)findViewById(R.id.txt_community_detail_like_icon) ;
        linLike = (LinearLayout)findViewById(R.id.lin_community_detail_like_icon);
        linFocus = (LinearLayout)findViewById(R.id.lin_community_detail_focus);
        linMentionParent = (LinearLayout)findViewById(R.id.lin_mention_parent);
        scrMention = (HorizontalScrollView)findViewById(R.id.scr_mention);
        txtMore = (TextView)findViewById(R.id.txt_community_detail_more);
        txtViews = (TextView)findViewById(R.id.txt_community_detail_views);
        imgViews = (ImageView)findViewById(R.id.img_community_detail_views);
        txtLabel = (TextView)findViewById(R.id.txt_community_detail_label);
        txtCategory = (TextView)findViewById(R.id.txt_new_community_detail_category_text);
        imgPin = (ImageView)findViewById(R.id.img_new_community_detail_pin);

        tabStip = ((LinearLayout) tabLayout.getChildAt(0));
        frameLayout = (FrameLayout) findViewById(R.id.frame_community_detail);

        txtBack.setOnClickListener(this);
        btnCommentSend.setOnClickListener(this);
        scrollView.setOnClickListener(this);
        linCommentParent.setOnClickListener(this);

        txtLike.setPaintFlags(Paint.ANTI_ALIAS_FLAG|Paint.UNDERLINE_TEXT_FLAG);

        setHideKeyboard(this,scrollView);

        if (beforeIntent.hasExtra("blog")){
            if (beforeIntent.getBooleanExtra("blog",false)){
                imgViews.setVisibility(View.VISIBLE);
                txtViews.setVisibility(View.VISIBLE);
            }
        }

        edtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beforeIntent.hasExtra("guest")){
                    Intent intent = new Intent(NewCommunityDetailActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
                    edtComment.setFocusable(false);
                }
            }
        });

        edtComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (beforeIntent.hasExtra("guest")){
                    if (hasFocus){
                        Intent intent = new Intent(NewCommunityDetailActivity.this,NewAnotherLoginActivity.class);
                        startActivity(intent);
                        edtComment.setFocusable(false);
                    }
                }else{
                    if (hasFocus){
                        edtComment.setHint("\"@닉네임\"을 입력하면 소환됩니다.");
                    }else {
                        edtComment.setHint("댓글을 작성해주세요.");
                    }
                }
            }
        });

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getKeyboardHeight(rootView);
//                viewHeight = display.getHeight();
            }
        });

        //memo 태그시 주석 제거
        edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (start < s.length()){
                    switch (s.charAt(start)) {
                        case '@':
                            mentionEnabled = false;
                            Log.i(TAG,"mention off");
                            scrMention.setVisibility(View.GONE);
                            linMentionParent.removeAllViews();
                            if (nickNameSearchNetWork != null){
                                if (!nickNameSearchNetWork.isCancelled()){
                                    nickNameSearchNetWork.cancel(true);
                                }
                            }
                            break;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1000){
                    Toast.makeText(NewCommunityDetailActivity.this, "댓글 작성은 1000자 이하로만 가능합니다.", Toast.LENGTH_SHORT).show();
                }else{
                    if (start < s.length())
                        switch (s.charAt(start)) {
                            case '@':
                                mentionEnabled = true;
                                break;
                        }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beforeIntent.hasExtra("guest")){
                    final Dialog dialog = new BottomSheetDialog(NewCommunityDetailActivity.this, R.style.SheetDialog);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View contentView = inflater.inflate(R.layout.dialog_community_guest_url, null);

                    TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_community_guest_cancel);
                    TextView txtUrl = (TextView) contentView.findViewById(R.id.txt_dialog_community_guest_url);
                    TextView txtInquiry = (TextView)contentView.findViewById(R.id.txt_dialog_community_guest_inquiry);

                    dialog.setContentView(contentView);

                    txtUrl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("url","https://soomcare.co.kr/community?COMMUNITY_NO="+beforeIntent.getStringExtra("communityNo")); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(NewCommunityDetailActivity.this, "url이 복사되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    txtInquiry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Toast.makeText(NewCommunityDetailActivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    txtCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }else{
                    if (mUserNo.equals("" + Utils.userItem.getUserNo())) {
//                    Thirdpopup(context,itemsArrayList.get(position).getNo());

                        final Dialog dialog = new BottomSheetDialog(NewCommunityDetailActivity.this, R.style.SheetDialog);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View contentView = inflater.inflate(R.layout.dialog_community_third_btn, null);

                        TextView txtRemove = (TextView) contentView.findViewById(R.id.txt_dialog_comment_third_remove);
                        TextView txtEdit = (TextView) contentView.findViewById(R.id.txt_dialog_comment_third_edit);
                        TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_comment_third_cancel);
                        TextView txtUrl = (TextView) contentView.findViewById(R.id.txt_dialog_comment_third_url);

                        dialog.setContentView(contentView);

                        txtRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                final Dialog dateTimeDialog = new Dialog(NewCommunityDetailActivity.this);

                                LayoutInflater inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater.inflate(R.layout.two_btn_popup, null);

                                TextView txtTitle = (TextView) layout.findViewById(R.id.txt_two_btn_popup_title);
                                TextView txtContents = (TextView) layout.findViewById(R.id.txt_two_btn_popup_contents);
                                final Button btnLeft = (Button) layout.findViewById(R.id.btn_two_btn_popup_left);
                                Button btnRight = (Button) layout.findViewById(R.id.btn_two_btn_popup_right);

                                txtTitle.setText("삭제하기");
                                txtContents.setText("게시물을 삭제하시겠습니까?");
                                btnLeft.setText("취소");
                                btnRight.setText("삭제");

                                dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                dateTimeDialog.setCancelable(false);
                                DisplayMetrics dm = getResources().getDisplayMetrics();
                                int width = dm.widthPixels;
                                int height = dm.heightPixels;

                                dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                layout.setMinimumWidth((int) (width * 0.88f));
                                layout.setMinimumHeight((int) (height * 0.45f));

                                dateTimeDialog.setContentView(layout);
                                dateTimeDialog.show();

                                btnLeft.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dateTimeDialog.dismiss();
                                    }
                                });

                                btnRight.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dateTimeDialog.dismiss();
                                        //게시물 삭제
                                        NetWorkCall(NEW_COMMUNITY_DELETE);
                                    }
                                });
                            }
                        });

                        txtEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent i = new Intent(NewCommunityDetailActivity.this, CommunityWriteActivity.class);
                                i.putExtra("communityNo", beforeIntent.getStringExtra("communityNo"));
                                i.putExtra("contents", mContents);
                                i.putExtra("hashTag", mHashtag);
                                i.putExtra("imgsPath", mImagsPath);
                                i.putExtra("hashTagList", Utils.tagList);
                                i.putExtra("imagesNo",mImagesNo);
                                i.putExtra("menuNo",mMenuNo);
                                i.putExtra("cMenuNo",mCMenuNo);
                                i.putExtra("menuTitle",mMenuTitle);
                                i.putExtra("cMenuTitle",mCMenuTitle);
                                startActivityForResult(i, 1111);
                            }
                        });

                        txtUrl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("url","https://soomcare.co.kr/community?COMMUNITY_NO="+beforeIntent.getStringExtra("communityNo")); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(NewCommunityDetailActivity.this, "url이 복사되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();

                    } else {
                        final Dialog dialog = new BottomSheetDialog(NewCommunityDetailActivity.this, R.style.SheetDialog);
                        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View contentView = inflater.inflate(R.layout.dialog_community_two_btn, null);

                        TextView txtDeclaration = (TextView) contentView.findViewById(R.id.txt_dialog_comment_two_declaration);
                        TextView txtScrap = (TextView) contentView.findViewById(R.id.txt_dialog_comment_two_scrap);
                        TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_comment_two_cancel);
                        TextView txtUrl = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_url);
                        TextView txtBlock = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_block);
                        TextView txtReport = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_report);

                        dialog.setContentView(contentView);

                        if (scrapFlag == 1){
                            txtScrap.setText("저장 취소하기");
                        }

                        txtDeclaration.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent i = new Intent(NewCommunityDetailActivity.this, InquiryActivity.class);
                                i.putExtra("inquiry", "inquiry");
                                startActivity(i);
                            }
                        });

                        // Todo 차단 신고 기능 구현하기
                        // 차단 기능
                        txtBlock.setOnClickListener(view -> {
                            dialog.dismiss();

                            TwoButtonDialog twoBtnDialog = new TwoButtonDialog(NewCommunityDetailActivity.this,"차단하기","현 게시물의 작성자를 차단하면\n현 작성자의 글을 볼 수 없습니다.\n사용자를 차단하시겠습니까?","취소","차단");
                            twoBtnDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                                @Override
                                public void OkButtonClick(View v) {
                                    ArrayList<String> blackList = TempUtil.getStringArrayPref(NewCommunityDetailActivity.this, "BLACK_LIST");
                                    blackList.add(mUserNo);
                                    TempUtil.setStringArrayPref(NewCommunityDetailActivity.this, "BLACK_LIST", blackList);

                                    Toast.makeText(NewCommunityDetailActivity.this, "사용자가 차단되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        });

                        // 신고 기능
                        txtReport.setOnClickListener(view -> {
                            dialog.dismiss();
                            Intent i = new Intent(NewCommunityDetailActivity.this, InquiryActivity.class);
                            i.putExtra("inquiry", "inquiry");
                            startActivity(i);
                        });

                        txtScrap.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (scrapFlag != 0) {
                                    if (scrapFlag == 1) {
                                        scrapFlag = 0;
                                        NetWorkCall(COMMUNITY_SCRAP_DELETE);
                                    } else {
                                        scrapFlag = 1;
                                        NetWorkCall(COMMUNITY_SCRAP_INSERT);
                                    }
                                } else {
                                    scrapFlag = 1;
                                    NetWorkCall(COMMUNITY_SCRAP_INSERT);
                                }
                                dialog.dismiss();
                            }
                        });

                        txtUrl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("url","https://soomcare.co.kr/community?COMMUNITY_NO="+beforeIntent.getStringExtra("communityNo")); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                                clipboardManager.setPrimaryClip(clipData);
                                Toast.makeText(NewCommunityDetailActivity.this, "url이 복사되었습니다.", Toast.LENGTH_SHORT).show();
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
                }
            }
        });

        txtLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beforeIntent.hasExtra("guest")){
                    Intent intent = new Intent(NewCommunityDetailActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent i = new Intent(NewCommunityDetailActivity.this, LikeActivity.class);
                    i.putExtra("communityNo", beforeIntent.getStringExtra("communityNo"));
                    startActivity(i);
                }
            }
        });

        txtComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtComment.setText("");
                selectCommentNo = "";
                mentionUserNoList = null;
                mentionSelectList = null;

                if (mentionUserNoList == null){
                    mentionUserNoList = new ArrayList<>();
                    mentionSelectList = new ArrayList<>();
                }

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, linFocus.getBottom());
                    }
                });
            }
        });

        linLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beforeIntent.hasExtra("guest")){
                    Intent intent = new Intent(NewCommunityDetailActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
                }else{
                    if (imgLike.getTag() != null) {
                        if (imgLike.getTag().equals(1)) {
                            NetWorkCall(COMMUNITY_LIKE_DELETE);
                            imgLike.setImageResource(R.drawable.ic_like_off);
                            txtLikeIcon.setTextColor(getColor(R.color.color626262));

                            txtLike.setText("응원해요 " + (--mLikeCnt));
                            imgLike.setTag(0);
                        } else {
                            NetWorkCall(COMMUNITY_LIKE_INSERT);
                            imgLike.setImageResource(R.drawable.ic_like_on);
                            txtLikeIcon.setTextColor(getColor(R.color.color09D192));

                            txtLike.setText("응원해요 " + (++mLikeCnt));
                            imgLike.setTag(1);
                        }
                    } else {
                        NetWorkCall(COMMUNITY_LIKE_INSERT);
                        imgLike.setImageResource(R.drawable.ic_like_on);
                        txtLikeIcon.setTextColor(getColor(R.color.color09D192));

                        txtLike.setText("응원해요 " + (++mLikeCnt));
                        imgLike.setTag(1);
                    }
                }
            }
        });

        linShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "https://soomcare.co.kr/community?COMMUNITY_NO="+beforeIntent.getStringExtra("communityNo"));
                startActivity(Intent.createChooser(sharingIntent,"공유하기"));
            }
        });

        //memo 태그 시 주석제거
        edtComment.setMentionTextColor(getColor(R.color.color00ce60));
        edtComment.setPattern("@[\\u4e00-\\u9fa5\\w\\-]+");

        edtComment.setOnMentionInputListener(new MentionTestEdit.OnMentionInputListener() {
            @Override
            public void onMentionCharacterInput(boolean status, String mention) {
                Log.i(TAG,"mention : " + mention + " status : " + status);
                if (nickNameSearchNetWork != null){
                    if (!nickNameSearchNetWork.isCancelled()){
                        nickNameSearchNetWork.cancel(true);
                    }
                }
                Log.i(TAG,"mentionEnabled : " + mentionEnabled);
                if (mentionEnabled){
                    mentionStr = mention;
                    nickNameSearchNetWork = new NickNameSearchNetWork();
                    nickNameSearchNetWork.execute(mention);
                }
            }
        });
    }

    public int dpToPx(float dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
        return px;
    }

    void NetWorkCall(String mCode){
        if (mCode.equals(COMMUNITY_LIKE_INSERT) || mCode.equals(COMMUNITY_LIKE_DELETE) || mCode.equals(COMMUNITY_SCRAP_INSERT) || mCode.equals(COMMUNITY_SCRAP_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),beforeIntent.getStringExtra("communityNo"));
        }else if (mCode.equals(NEW_COMMUNITY_DELETE) || mCode.equals(COMMUNITY_COMMENT_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("communityNo"));
        }else if (mCode.equals(COMMUNITY_CALL_DETAIL)){
            progressDialog = new ProgressDialog(NewCommunityDetailActivity.this,R.style.MyTheme);

            progressDialog.show();
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+userItem.getUserNo(),beforeIntent.getStringExtra("communityNo"));
        }else if (mCode.equals(ALARM_LIST_ALL_READ)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+String.valueOf(userItem.getUserNo()));
        }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
            Log.i(TAG,"no : " + beforeIntent.getStringExtra("communityNo"));
            new NetworkUtils.NetworkCall(NewCommunityDetailActivity.this,this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,""+Utils.userItem.getUserNo(),beforeIntent.getStringExtra("communityNo"));
        }else if (mCode.equals(NEW_COMMENT_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,beforeIntent.getStringExtra("communityNo"));
        }else if (mCode.equals(NEW_COMMENT_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(edtComment.getText().toString(),""+userItem.getUserNo(),beforeIntent.getStringExtra("communityNo"),MentionJsonArray());
        }else if (mCode.equals(NEW_COMMENT_REPLY_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(edtComment.getText().toString(),""+userItem.getUserNo(),beforeIntent.getStringExtra("communityNo"),MentionJsonArray(),selectCommentNo,groupNum,originCommentNo);
        }else if (mCode.equals(NEW_COMMENT_UPDATE)){
            Log.i(TAG,"selectCNo : " + selectCommentNo + " mention : " + (MentionJsonArray().length() == 0 ? "[]" : MentionJsonArray()) + " orginCNo : " + originCommentNo);
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(edtComment.getText().toString(),selectCommentNo,MentionJsonArray().length() == 0 ? "[]" : MentionJsonArray(),originCommentNo);
        }else if (mCode.equals(NEW_COMMENT_REPLY_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(edtComment.getText().toString(),selectCommentNo,MentionJsonArray().length() == 0 ? "[]" : MentionJsonArray(),originCommentNo,originReCommentNo);
        }else if (mCode.equals(NEW_COMMENT_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(selectCommentNo,originCommentNo);
        }else if (mCode.equals(NEW_COMMENT_REPLY_DELETE)){
//            Log.i(TAG,"1 : " + selectCommentNo + " 2 : " + originCommentNo + " 3 : " + originReCommentNo);
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(selectCommentNo,originCommentNo,originReCommentNo);
        }else if (mCode.equals(NEW_COMMUNITY_VIEWS_UP)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,beforeIntent.getStringExtra("communityNo"));
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(COMMUNITY_LIKE_INSERT) || mCode.equals(COMMUNITY_LIKE_DELETE) || mCode.equals(COMMUNITY_SCRAP_INSERT) || mCode.equals(COMMUNITY_SCRAP_DELETE)){
                //응원해요, 스크랩 클릭 시 setResult 만 전달해주면 될 듯??
                if (mCode.equals(COMMUNITY_SCRAP_INSERT)){
                    Toast.makeText(NewCommunityDetailActivity.this, "게시글을 저장했습니다.", Toast.LENGTH_SHORT).show();
                }else if (mCode.equals(COMMUNITY_SCRAP_DELETE)){
                    Toast.makeText(NewCommunityDetailActivity.this, "게시글 저장을 취소했습니다.", Toast.LENGTH_SHORT).show();
                }
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (JsonIsNullCheck(jsonObject,"result").equals("Y")){
                        communityDetailRefresh = true;
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(NEW_COMMUNITY_DELETE)){
                pushFlag = beforeIntent.getBooleanExtra("push",false);
                if(pushFlag){
                    Intent communityIntent = new Intent(NewCommunityDetailActivity.this, MainActivity.class);
                    communityIntent.putExtra("community", true);
                    communityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(communityIntent);
                }else {
                    Intent intent = null;
                    intent = new Intent();
                    intent.putExtra("position",beforeIntent.getIntExtra("position",0));
                    intent.putExtra("communityRemove",true);
                    setResult(RESULT_OK,intent);
                }
                finish();
            }else if (mCode.equals(COMMUNITY_COMMENT_LIST)){
                linCommentParent.removeAllViews();
                commentNoOverlapList = new ArrayList<>();
                commentReplyNoOverlapList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (!jsonObject.has("list")){
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.length() > 0){
                        linCommentNo.setVisibility(View.GONE);
                        linCommentParent.setVisibility(View.VISIBLE);
                    }else{
                        linCommentNo.setVisibility(View.VISIBLE);
                        linCommentParent.setVisibility(View.GONE);
                    }

                    ArrayList<CommentItem> recommentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        if (JsonIntIsNullCheck(object,"CLASS") == 1){
                            if (!commentNoOverlapList.contains(JsonIsNullCheck(object,"COMMENT_NO"))){
                            commentNoOverlapList.add(JsonIsNullCheck(object,"COMMENT_NO"));
                            CommentItem commentItem = new CommentItem();
                            commentItem.setCommentNo(JsonIsNullCheck(object,"COMMENT_NO"));
                            commentItem.setProfilePath(JsonIsNullCheck(object,"PROFILE_IMG"));
                            commentItem.setNickname(JsonIsNullCheck(object,"NICKNAME"));
                            commentItem.setContents(JsonIsNullCheck(object,"COMMENT"));
                            commentItem.setMention(JsonIsNullCheck(object,"MENTION"));
                            commentItem.setGender(JsonIntIsNullCheck(object,"GENDER"));
                                commentItem.setGroupNum(JsonIsNullCheck(object,"GROUP_NUM"));
                                commentItem.setOriginCommentNo(JsonIsNullCheck(object,"ORIGIN_COMMENT_NO"));
                                commentItem.setOriginReCommentNo(JsonIsNullCheck(object,"ORIGIN_RE_COMMENT_NO"));
//                        Log.i(TAG,"list : " + JsonIsNullCheck(object,"COMMENT"));

                            commentItem.setUserNo(JsonIsNullCheck(object,"USER_NO"));
                            if (JsonIsNullCheck(object,"UPDATE_DT").length() == 0){
                                commentItem.setDate(JsonIsNullCheck(object,"CREATE_DT"));
                            }else{
                                commentItem.setDate(JsonIsNullCheck(object,"UPDATE_DT"));
                            }
                            CommentView(commentItem,recommentList);
                        }
                        }else{
                            if (!commentReplyNoOverlapList.contains(JsonIsNullCheck(object,"COMMENT_NO"))){
                                commentReplyNoOverlapList.add(JsonIsNullCheck(object,"COMMENT_NO"));
                                CommentItem commentItem = new CommentItem();
                                commentItem.setCommentReplyNo(JsonIsNullCheck(object,"COMMENT_NO"));
                                commentItem.setCommentNo(JsonIsNullCheck(object,"GROUP_NUM"));
                                commentItem.setProfilePath(JsonIsNullCheck(object,"PROFILE_IMG"));
                                commentItem.setNickname(JsonIsNullCheck(object,"NICKNAME"));
                                commentItem.setContents(JsonIsNullCheck(object,"COMMENT"));
                                commentItem.setUserNo(JsonIsNullCheck(object,"USER_NO"));
                                commentItem.setMention(JsonIsNullCheck(object,"MENTION"));
                                commentItem.setGender(JsonIntIsNullCheck(object,"GENDER"));
                                commentItem.setGroupNum(JsonIsNullCheck(object,"GROUP_NUM"));
                                commentItem.setOriginCommentNo(JsonIsNullCheck(object,"ORIGIN_COMMENT_NO"));
                                commentItem.setOriginReCommentNo(JsonIsNullCheck(object,"ORIGIN_RE_COMMENT_NO"));
                                commentItem.setGroupNum(JsonIsNullCheck(object,"GROUP_NUM"));
                                if (JsonIsNullCheck(object,"UPDATE_DT").length() == 0){
                                    commentItem.setDate(JsonIsNullCheck(object,"CREATE_DT"));
                                }else{
                                    commentItem.setDate(JsonIsNullCheck(object,"UPDATE_DT"));
                                }
                                recommentList.add(commentItem);
                            }
                        }

                    }

                    if (beforeIntent.hasExtra("comment")){
                        scrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.smoothScrollTo(0, linFocus.getBottom());
                            }
                        },200);
                    }else{

                    }
                    btnCommentSend.setEnabled(true);
                }catch (JSONException e){
                    btnCommentSend.setEnabled(true);
                }
                if (progressDialog != null){
                    progressDialog.dismiss();
                }
            }else if (mCode.equals(NEW_COMMENT_INSERT) || mCode.equals(NEW_COMMENT_REPLY_INSERT)){
                Log.i(TAG,"comment insert : " + mResult);
                edtComment.setText("");
                mentionUserNoList = null;
                mentionSelectList = null;
                selectCommentNo = "";

                txtComment.setText("" + (++mCommentCnt));

                if (mentionUserNoList == null){
                    mentionUserNoList = new ArrayList<>();
                    mentionSelectList = new ArrayList<>();
                }

                imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
                txtBack.setFocusableInTouchMode(true);
                txtBack.requestFocus();

                communityDetailRefresh = true;

                NetWorkCall(NEW_COMMENT_LIST);
            }else if (mCode.equals(NEW_COMMENT_DELETE) || mCode.equals(NEW_COMMENT_REPLY_DELETE)){
                selectCommentNo = "";
                communityDetailRefresh = true;

                NetWorkCall(NEW_COMMENT_LIST);
            }else if (mCode.equals(NEW_COMMENT_UPDATE) || mCode.equals(NEW_COMMENT_REPLY_UPDATE)){
                Log.i(TAG,"comment update : " + mResult);
                edtComment.setText("");
                mentionUserNoList = null;
                mentionSelectList = null;
                selectCommentNo = "";

                if (mentionUserNoList == null){
                    mentionUserNoList = new ArrayList<>();
                    mentionSelectList = new ArrayList<>();
                }

                status = 1;
                imm.hideSoftInputFromWindow(edtComment.getWindowToken(), 0);
                txtBack.setFocusableInTouchMode(true);
                txtBack.requestFocus();

                communityDetailRefresh = true;

                NetWorkCall(NEW_COMMENT_LIST);
            }else if (mCode.equals(NEW_COMMUNITY_DETAIL)){
                try {
                    JSONObject jsonObject = new JSONObject(mResult);

                    Log.i(TAG,"detail : " + jsonObject.toString());

                    if (jsonObject.has("list")){
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        int size = jsonArray.length();

                        Log.i(TAG,"size : " + size);

                        for (int i = 0; i < size; i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {

                                mUserNo = JsonIsNullCheck(object, "USER_NO");

                                if (JsonIsNullCheck(object, "PROFILE_IMG").length() > 0) {
                                    String replaceText = JsonIsNullCheck(object, "PROFILE_IMG");
                                    if (replaceText.contains("soom2.testserver-1.com:8080")){
                                        replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                                    }else if (replaceText.contains("103.55.190.193")){
                                        replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                                    }
                                    String finalReplaceText = replaceText;
                                    if (JsonIsNullCheck(object, "PROFILE_IMG").contains("https:")) {
                                        imgProfile.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                requestManager.load(finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                                            }
                                        });
                                    } else {
                                        imgProfile.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                requestManager.load("https:" + finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                                            }
                                        });
                                    }
                                } else {
                                    int resource = 0;
                                    if (JsonIntIsNullCheck(object,"GENDER") == 1){
                                        resource = R.drawable.ic_no_profile_m;
                                    }else if (JsonIntIsNullCheck(object,"GENDER") == 2){
                                        resource = R.drawable.ic_no_profile_w;
                                    }else{
                                        resource = R.drawable.ic_no_profile;
                                    }
                                    int finalResource = resource;
                                    imgProfile.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            requestManager.load(finalResource).apply(requestOptions.circleCrop()).into(imgProfile);
                                        }
                                    });
                                }
                                mMenuNo = JsonIsNullCheck(object,"MENU_NO");
                                if (mMenuNo.equals("3")){
                                    imgViews.setVisibility(View.VISIBLE);
                                    txtViews.setVisibility(View.VISIBLE);
                                }

                                if (JsonIntIsNullCheck(object,"PRIORITY") == 99 || JsonIntIsNullCheck(object,"PRIORITY") == 0){
                                    imgPin.setVisibility(View.GONE);
                                }else{
                                    imgPin.setVisibility(View.VISIBLE);
                                }

//                                if (mMenuNo.equals("1")){
//                                    txtCategory.setTextColor(Color.parseColor("#09D182"));
//                                    txtCategory.setBackgroundResource(R.drawable.community_middle_category_on_01);
//                                }else if (mMenuNo.equals("2")){
//                                    txtCategory.setTextColor(Color.parseColor("#6B8CBF"));
//                                    txtCategory.setBackgroundResource(R.drawable.community_middle_category_on_02);
//                                }else if (mMenuNo.equals("3")){
//                                    txtCategory.setTextColor(Color.parseColor("#CEB218"));
//                                    txtCategory.setBackgroundResource(R.drawable.community_middle_category_on_03);
//                                }


                                mCMenuNo = JsonIsNullCheck(object,"C_MENU_NO");
                                mMenuTitle = JsonIsNullCheck(object,"MENU_TITLE");
                                mCMenuTitle = JsonIsNullCheck(object,"C_MENU_TITLE");
                                txtName.setText(JsonIsNullCheck(object, "NICKNAME"));
                                txtViews.setText(JsonIsNullCheck(object,"APP_VIEWS"));

                                if (JsonIsNullCheck(object, "LABEL_NAME").length() != 0){
                                    txtLabel.setText(JsonIsNullCheck(object, "LABEL_NAME"));
                                }else{
                                    txtLabel.setVisibility(View.GONE);
                                }

                                if (JsonIsNullCheck(object, "LABEL_COLOR").length() != 0){
                                    txtLabel.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(JsonIsNullCheck(object, "LABEL_COLOR"))));
                                }

                                if (JsonIsNullCheck(object, "C_MENU_TITLE").length() == 0 || JsonIsNullCheck(object, "C_MENU_TITLE").equals("")){
                                    txtCategory.setVisibility(View.GONE);
                                }else{
                                    txtCategory.setVisibility(View.VISIBLE);
                                    txtCategory.setText(JsonIsNullCheck(object, "C_MENU_TITLE"));
                                }
                                if (JsonIsNullCheck(object, "UPDATE_DT").length() == 0) {
                                    txtDate.setText(JsonIsNullCheck(object, "CREATE_DT").substring(2,16).replace("-","."));
                                } else {
                                    txtDate.setText(JsonIsNullCheck(object, "UPDATE_DT").substring(2,16).replace("-","."));
                                }

                                mContents = JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n").trim();
                                txtContents.setText(JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n").trim());

                                int cnt = StringUtils.countMatches(JsonIsNullCheck(object, "CONTENTS").replace("<br>", "\n").trim(),"\n");

                                String[] hashTagList = JsonIsNullCheck(object, "HASHTAG").split(",");
                                String hasgTag = "";

                                linTag.removeAllViews();

                                if (JsonIsNullCheck(object, "HASHTAG").length() == 0) {
//                                    txtTag.setVisibility(View.GONE);
                                    linTag.setVisibility(View.GONE);
                                } else {
//                                    txtTag.setVisibility(View.VISIBLE);
                                    linTag.setVisibility(View.VISIBLE);
                                }

                                Typeface typeface = ResourcesCompat.getFont(NewCommunityDetailActivity.this,R.font.notosanscjkkr_medium_regular);

                                for (int j = 0; j < hashTagList.length; j++) {
                                    hasgTag += "#" + hashTagList[j];
                                    mHashtag += hasgTag;
                                    TextView textView = new TextView(NewCommunityDetailActivity.this);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    if (j != hashTagList.length){
                                        params.rightMargin = 10;
                                    }
                                    params.bottomMargin = 5;
                                    textView.setLayoutParams(params);
                                    textView.setText("#" + hashTagList[j]);
                                    textView.setTextColor(Color.parseColor("#a7a7a7"));
                                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);

                                    textView.setTypeface(typeface);

                                    linTag.addView(textView);
                                }

//                                txtTag.setText(hasgTag);
                                txtLike.setText("응원해요 " + JsonIntIsNullCheck(object, "LIKE_CNT"));
                                txtComment.setText("" + JsonIntIsNullCheck(object, "COMMENT_CNT"));
                                if (JsonIntIsNullCheck(object,"LIKE_FLAG") == 1){
                                    imgLike.setImageResource(R.drawable.ic_like_on);
                                    txtLikeIcon.setTextColor(getColor(R.color.color09D192));

                                    imgLike.setTag(1);
                                }else{
                                    imgLike.setTag(0);
                                }
                                if (JsonIntIsNullCheck(object,"SCRAP_FLAG") == 1){
                                    scrapFlag = 1;
                                }else{
                                    scrapFlag = 0;
                                }
                                mLikeCnt = JsonIntIsNullCheck(object, "LIKE_CNT");
                                mCommentCnt = JsonIntIsNullCheck(object, "COMMENT_CNT");
                                mImagesNo = JsonIsNullCheck(object,"IMAGES_NO");

                                mImagsPath = JsonIsNullCheck(object, "IMAGE_FILE");
                                String[] imgList = JsonIsNullCheck(object, "IMAGE_FILE").split(",");

                                if (JsonIsNullCheck(object, "IMAGE_FILE").length() == 0) {
                                    frameLayout.setVisibility(View.GONE);
                                } else {
                                    frameLayout.setVisibility(View.VISIBLE);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(display.getWidth(), display.getWidth()-dpToPx(30));
                                    frameLayout.setLayoutParams(params);
                                }

                                if (imgList.length <= 1) {
                                    tabLayout.setVisibility(View.GONE);
                                } else {
                                    tabLayout.setVisibility(View.VISIBLE);
                                    tabLayout.removeAllTabs();
                                    for (int j = 0; j < imgList.length; j++) {
                                        tabLayout.addTab(tabLayout.newTab());
                                    }
                                }
                                if (!imgList[0].endsWith(".mp4")) {
                                    adapter = new CommunityViewPagerAdapter(NewCommunityDetailActivity.this, 0);
                                    for (int j = 0; j < imgList.length; j++) {
                                        adapter.addItem(imgList[j]);
                                    }
                                } else {
                                    adapter = new CommunityViewPagerAdapter(NewCommunityDetailActivity.this, 1);

                                    for (int j = 0; j < imgList.length; j++) {
                                        adapter.addItem(imgList[j]);
                                    }
                                    viewPager.setPagingDisabled();
                                    tabLayout.setVisibility(View.GONE);
                                }
                                viewPager.setAdapter(adapter);
                                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

                            }
                        }

                        NetWorkCall(NEW_COMMENT_LIST);
                        NetWorkCall(NEW_COMMUNITY_VIEWS_UP);

                        if (beforeIntent.hasExtra("push") || beforeIntent.hasExtra("keyNo")){
                            NetWorkCall(ALARM_LIST_ALL_READ);
                        }
                    }else{
                        oneButtonDialog= new OneButtonDialog(NewCommunityDetailActivity.this, "게시글", "해당 게시글은 삭제되었습니다.", "확인");
                        oneButtonDialog.setCancelable(false);
                        oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                finish();
                            }
                        });
                    }
                } catch (JSONException e) {

                }
            }else if (mCode.equals(NEW_COMMENT_LIST)){
                Log.i(TAG,"comment list : " + mResult);
                linCommentParent.removeAllViews();
                commentNoOverlapList = new ArrayList<>();
                commentReplyNoOverlapList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    if (!jsonObject.has("list")){
                        linCommentNo.setVisibility(View.VISIBLE);
                        linCommentParent.setVisibility(View.GONE);
                        if (progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    if (jsonArray.length() > 0){
                        linCommentNo.setVisibility(View.GONE);
                        linCommentParent.setVisibility(View.VISIBLE);
                    }else{
                        linCommentNo.setVisibility(View.VISIBLE);
                        linCommentParent.setVisibility(View.GONE);
                    }

                    ArrayList<CommentItem> recommentList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);
                        if (JsonIntIsNullCheck(object,"CLASS") == 1){
                            if (!commentNoOverlapList.contains(JsonIsNullCheck(object,"COMMENT_NO"))){
                                commentNoOverlapList.add(JsonIsNullCheck(object,"COMMENT_NO"));
                                CommentItem commentItem = new CommentItem();
                                commentItem.setCommentNo(JsonIsNullCheck(object,"COMMENT_NO"));
                                commentItem.setProfilePath(JsonIsNullCheck(object,"PROFILE_IMG"));
                                commentItem.setNickname(JsonIsNullCheck(object,"NICKNAME"));
                                commentItem.setContents(JsonIsNullCheck(object,"COMMENT"));
                                commentItem.setMention(JsonIsNullCheck(object,"MENTION"));
                                commentItem.setGender(JsonIntIsNullCheck(object,"GENDER"));
                                commentItem.setGroupNum(JsonIsNullCheck(object,"GROUP_NUM"));
                                commentItem.setOriginCommentNo(JsonIsNullCheck(object,"ORIGIN_COMMENT_NO"));
                                commentItem.setOriginReCommentNo(JsonIsNullCheck(object,"ORIGIN_RE_COMMENT_NO"));
//                        Log.i(TAG,"list : " + JsonIsNullCheck(object,"COMMENT"));

                                commentItem.setUserNo(JsonIsNullCheck(object,"USER_NO"));
                                if (JsonIsNullCheck(object,"UPDATE_DT").length() == 0){
                                    commentItem.setDate(JsonIsNullCheck(object,"CREATE_DT"));
                                }else{
                                    commentItem.setDate(JsonIsNullCheck(object,"UPDATE_DT"));
                                }

                                CommentView(commentItem,recommentList);
                            }
                        }else{
                            if (!commentReplyNoOverlapList.contains(JsonIsNullCheck(object,"COMMENT_NO"))){
                                commentReplyNoOverlapList.add(JsonIsNullCheck(object,"COMMENT_NO"));
                                CommentItem commentItem = new CommentItem();
                                commentItem.setCommentNo(JsonIsNullCheck(object,"COMMENT_NO"));
                                commentItem.setProfilePath(JsonIsNullCheck(object,"PROFILE_IMG"));
                                commentItem.setNickname(JsonIsNullCheck(object,"NICKNAME"));
                                commentItem.setContents(JsonIsNullCheck(object,"COMMENT"));
                                commentItem.setMention(JsonIsNullCheck(object,"MENTION"));
                                commentItem.setGender(JsonIntIsNullCheck(object,"GENDER"));
                                commentItem.setGroupNum(JsonIsNullCheck(object,"GROUP_NUM"));
                                commentItem.setOriginCommentNo(JsonIsNullCheck(object,"ORIGIN_COMMENT_NO"));
                                commentItem.setOriginReCommentNo(JsonIsNullCheck(object,"ORIGIN_RE_COMMENT_NO"));
//                        Log.i(TAG,"list : " + JsonIsNullCheck(object,"COMMENT"));

                                commentItem.setUserNo(JsonIsNullCheck(object,"USER_NO"));
                                if (JsonIsNullCheck(object,"UPDATE_DT").length() == 0){
                                    commentItem.setDate(JsonIsNullCheck(object,"CREATE_DT"));
                                }else{
                                    commentItem.setDate(JsonIsNullCheck(object,"UPDATE_DT"));
                                }
                                RecommentView(commentItem);
                            }
                        }
                    }
                    if (beforeIntent.hasExtra("comment")){
                        scrollView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.smoothScrollTo(0, linFocus.getBottom());
                            }
                        },200);
                    }else{

                    }
                    btnCommentSend.setEnabled(true);
                }catch (JSONException e){
                    btnCommentSend.setEnabled(true);
                }
                if (progressDialog != null){
                    progressDialog.dismiss();
                }
            }
        }
    }

    String response;

    private class NickNameSearchNetWork extends AsyncTask<String, String, String> {

        boolean isDoing = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.i(TAG,"cancelled");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            linMentionParent.removeAllViews();
        }

        @Override
        protected String doInBackground(String... strings) {
            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("NICKNAME", strings[0])
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.selectMentionUserInfo(), body);
//                Log.i(TAG,"NickNameSearchNetWork : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (nickNameSearchNetWork.isCancelled()){
                Log.i(TAG,"cancel");
            }else{
                if (s != null){
                    linMentionParent.removeAllViews();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.has("list")){
                            scrMention.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                if (!mentionUserNoList.contains(JsonIntIsNullCheck(object,"USER_NO"))){
                                    mentionList(JsonIntIsNullCheck(object,"USER_NO"),JsonIsNullCheck(object,"PROFILE_IMG"),JsonIsNullCheck(object,"NICKNAME"),JsonIntIsNullCheck(object,"GENDER"));
                                }
                            }

                            if (linMentionParent.getChildCount() == 0){
                                scrMention.setVisibility(View.GONE);
                                linMentionParent.removeAllViews();
                            }
                        }else{
                            scrMention.setVisibility(View.GONE);
                            linMentionParent.removeAllViews();
                        }
                    }catch (JSONException e){

                    }
                }else{
                    linMentionParent.removeAllViews();
                }
            }
        }
    }

    private void mentionList(int userNo, String profilePath, String nickName, int gender){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.metion_list_item,null);

        ImageView imgProfile = (ImageView)listView.findViewById(R.id.img_mention_list_item_profile);
        TextView txtNickName = (TextView)listView.findViewById(R.id.txt_mention_list_item_nickname);

        if (profilePath.length() > 0) {
            String replaceText = profilePath;
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            String finalReplaceText = replaceText;
            if (profilePath.contains("https:")) {
                imgProfile.post(new Runnable() {
                    @Override
                    public void run() {
                        requestManager.load(finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                    }
                });
            } else {
                imgProfile.post(new Runnable() {
                    @Override
                    public void run() {
                        requestManager.load("https:" + finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                    }
                });
            }
        } else {
            int resource = R.drawable.ic_no_profile;
            if (gender == 1){
                resource = R.drawable.ic_no_profile_m;
            }else if (gender == 2){
                resource = R.drawable.ic_no_profile_w;
            }
            int finalResource = resource;
            imgProfile.post(new Runnable() {
                @Override
                public void run() {
                    requestManager.load(finalResource).apply(requestOptions.circleCrop()).into(imgProfile);
                }
            });
        }

        txtNickName.setText(nickName);

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beforeIntent.hasExtra("guest")){
                    Intent intent = new Intent(NewCommunityDetailActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
                }else{
                    if (mentionSelectList == null){
                        mentionSelectList = new ArrayList<>();
                        mentionUserNoList = new ArrayList<>();
                    }

                    mentionSelectList.add("@" + nickName);
                    mentionUserNoList.add(userNo);
                    String aaaa = edtComment.getText().toString().substring(0,mentionStart) + "@" + nickName + " " + edtComment.getText().toString().substring(mentionStart+mentionStr.length()+1,edtComment.getText().length());

                    edtComment.setText(aaaa);
                    edtComment.selectColorMentionString();
    //                edtComment.selectColorMentionString();
    //                edtComment.setText(edtComment.getText().toString() + " ");
                    mentionEnabled = false;
                    linMentionParent.removeAllViews();
                    scrMention.setVisibility(View.GONE);
                }
            }
        });

        linMentionParent.addView(listView);
    }

    private void CommentView(final CommentItem commentItem, ArrayList<CommentItem> reComment){
        View listView = new View(this);

        listView = getLayoutInflater().inflate(R.layout.view_comment_list_item,null);

        LinearLayout linearLayout = (LinearLayout)listView.findViewById(R.id.lin_comment_list_item_space);
        ImageView imgProfile = (ImageView)listView.findViewById(R.id.img_comment_list_item_profile);
        TextView txtNickname = (TextView)listView.findViewById(R.id.txt_comment_list_item_name);
        ImageView imgMore = (ImageView)listView.findViewById(R.id.img_comment_list_item_more);
        TextView txtContents = (TextView)listView.findViewById(R.id.txt_comment_list_item_comment);
        TextView txtDate = (TextView)listView.findViewById(R.id.txt_comment_list_item_date);
        TextView txtReComment11 = (TextView)listView.findViewById(R.id.txt_comment_list_item_recomment);
        LinearLayout linRecommentParent = (LinearLayout)listView.findViewById(R.id.lin_comment_list_item_parent);

        txtNickname.setText(commentItem.getNickname());
        txtDate.setText(commentItem.getDate().substring(2,16).replace("-","."));

        RequestOptions requestOptions = new RequestOptions();

        if (commentItem.getProfilePath().length() > 0){
            String replaceText = commentItem.getProfilePath();
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            String finalReplaceText = replaceText;
            if (commentItem.getProfilePath().contains("https:")){
                imgProfile.post(new Runnable() {
                    @Override
                    public void run() {
                        requestManager.load(finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                    }
                });
            }else{
                imgProfile.post(new Runnable() {
                    @Override
                    public void run() {
                        requestManager.load("https:"+finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                    }
                });
            }
        } else {
            int resource = R.drawable.ic_no_profile;
            if (commentItem.getGender() == 1){
                resource = R.drawable.ic_no_profile_m;
            }else if (commentItem.getGender() == 2){
                resource = R.drawable.ic_no_profile_w;
            }
            int finalResource = resource;
            imgProfile.post(new Runnable() {
                @Override
                public void run() {
                    requestManager.load(finalResource).apply(requestOptions.circleCrop()).into(imgProfile);
                }
            });
        }

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beforeIntent.hasExtra("guest")){
                    final Dialog dialog = new BottomSheetDialog(NewCommunityDetailActivity.this, R.style.SheetDialog);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View contentView = inflater.inflate(R.layout.dialog_community_comment_guest_url, null);

                    TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_community_guest_cancel);
                    TextView txtInquiry = (TextView)contentView.findViewById(R.id.txt_dialog_community_guest_inquiry);

                    dialog.setContentView(contentView);

                    txtInquiry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Toast.makeText(NewCommunityDetailActivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    txtCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }else{
                    if (commentItem.getUserNo().equals(""+Utils.userItem.getUserNo())){
                        selectCommentNo = commentItem.getCommentNo();
                        groupNum = commentItem.getGroupNum();
                        originCommentNo = commentItem.getOriginCommentNo();
                        originReCommentNo = commentItem.getOriginReCommentNo();
                        Thirdpopup(2,commentItem.getCommentNo(),commentItem.getContents(),commentItem.getMention());
                    }else{
                        selectCommentNo = commentItem.getCommentNo();
                        groupNum = commentItem.getGroupNum();
                        originCommentNo = commentItem.getOriginCommentNo();
                        originReCommentNo = commentItem.getOriginReCommentNo();
                        Twopopup(2,commentItem.getCommentNo());
                    }
                }
            }
        });

        //memo 태그시 주석삭제
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(commentItem.getContents());

        if (commentItem.getMention().length() > 0){
            try {
                JSONArray jsonArray = new JSONArray(commentItem.getMention());
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    stringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color00ce60)), JsonIntIsNullCheck(object,"LOCATION"), JsonIntIsNullCheck(object,"LOCATION") + JsonIntIsNullCheck(object,"LENGTH"), 33);
                    stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), JsonIntIsNullCheck(object,"LOCATION"), JsonIntIsNullCheck(object,"LOCATION") + JsonIntIsNullCheck(object,"LENGTH"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                txtContents.append(stringBuilder);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            txtContents.setText(commentItem.getContents());
        }

        View finalListView = listView;
        txtReComment11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (beforeIntent.hasExtra("guest")){
                    Intent intent = new Intent(NewCommunityDetailActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
                }else{
                    edtComment.setFocusableInTouchMode(true);
                    edtComment.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(edtComment,1);
//                imm.showSoftInput(edtComment, 0);

                    selectCommentNo = commentItem.getCommentNo();
                    mentionUserNoList = null;
                    mentionSelectList = null;

                    if (mentionSelectList == null){
                        mentionSelectList = new ArrayList<>();
                        mentionUserNoList = new ArrayList<>();
                    }
                    //memo 태그시 주석제거
                    mentionSelectList.add("@" + commentItem.getNickname());
                    mentionUserNoList.add(Integer.valueOf(commentItem.getUserNo()));

                    edtComment.setText("@" + commentItem.getNickname() + " ");
                    edtComment.selectColorMentionString();
                    viewHeight = display.getHeight();
                    Log.i(TAG,"viewHeight : " + viewHeight);
                    groupNum = commentItem.getGroupNum();
                    originCommentNo = commentItem.getOriginCommentNo();
                    originReCommentNo = commentItem.getOriginReCommentNo();

                    scrollToView(finalListView,linearLayout,scrollView,0);
                }
            }
        });

        linCommentParent.addView(listView);
    }

    void RecommentView(final CommentItem commentItem){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_comment_reply_list_item,null);

        LinearLayout lin = (LinearLayout)listView.findViewById(R.id.lin_comment_reply_list_item);
        LinearLayout linearLayout = (LinearLayout)listView.findViewById(R.id.lin_comment_reply_list_item_space);
        ImageView imgProfile = (ImageView)listView.findViewById(R.id.img_comment_reply_list_item_profile);
        TextView txtNickname = (TextView)listView.findViewById(R.id.txt_comment_reply_list_item_name);
        ImageView imgMore = (ImageView)listView.findViewById(R.id.img_comment_reply_list_item_more);
        TextView txtContents = (TextView)listView.findViewById(R.id.txt_comment_reply_list_item_comment);
        TextView txtDate = (TextView)listView.findViewById(R.id.txt_comment_reply_list_item_date);
        TextView txtReComment11 = (TextView)listView.findViewById(R.id.txt_comment_reply_list_item_recomment);

        txtNickname.setText(commentItem.getNickname());
        txtDate.setText(commentItem.getDate().substring(2,16).replace("-","."));

        RequestOptions requestOptions = new RequestOptions();

        if (commentItem.getProfilePath().length() > 0){
            String replaceText = commentItem.getProfilePath();
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            String finalReplaceText = replaceText;
            if (commentItem.getProfilePath().contains("https:")){
                imgProfile.post(new Runnable() {
                    @Override
                    public void run() {
                        requestManager.load(finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                    }
                });
            }else{
                imgProfile.post(new Runnable() {
                    @Override
                    public void run() {
                        requestManager.load("https:"+finalReplaceText).apply(requestOptions.circleCrop()).into(imgProfile);
                    }
                });
            }
        }else {
            int resource = R.drawable.ic_no_profile;
            if (commentItem.getGender() == 1){
                resource = R.drawable.ic_no_profile_m;
            }else if (commentItem.getGender() == 2){
                resource = R.drawable.ic_no_profile_w;
            }
            int finalResource = resource;
            imgProfile.post(new Runnable() {
                @Override
                public void run() {
                    requestManager.load(finalResource).apply(requestOptions.circleCrop()).into(imgProfile);
                }
            });
        }

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (beforeIntent.hasExtra("guest")){
                        final Dialog dialog = new BottomSheetDialog(NewCommunityDetailActivity.this, R.style.SheetDialog);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View contentView = inflater.inflate(R.layout.dialog_community_comment_guest_url, null);

                        TextView txtCancel = (TextView) contentView.findViewById(R.id.txt_dialog_community_guest_cancel);
                        TextView txtInquiry = (TextView)contentView.findViewById(R.id.txt_dialog_community_guest_inquiry);

                        dialog.setContentView(contentView);

                        txtInquiry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                Toast.makeText(NewCommunityDetailActivity.this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        dialog.show();
                    }else{
                        if (commentItem.getUserNo().equals(""+Utils.userItem.getUserNo())){
                        groupNum = commentItem.getGroupNum();
                        originCommentNo = commentItem.getOriginCommentNo();
                        originReCommentNo = commentItem.getOriginReCommentNo();
                        selectCommentNo = commentItem.getCommentNo();
                        Thirdpopup(3,commentItem.getCommentReplyNo(),commentItem.getContents(),commentItem.getMention());
                    }else{
                        groupNum = commentItem.getGroupNum();
                        originCommentNo = commentItem.getOriginCommentNo();
                        originReCommentNo = commentItem.getOriginReCommentNo();
                        selectCommentNo = commentItem.getCommentNo();
                        Twopopup(3,commentItem.getCommentReplyNo());
                    }
                }
            }
        });

        //memo 태그시 주석삭제
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(commentItem.getContents());

        if (commentItem.getMention().length() > 0){
            try {
                JSONArray jsonArray = new JSONArray(commentItem.getMention());
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    stringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color00ce60)), JsonIntIsNullCheck(object,"LOCATION"), JsonIntIsNullCheck(object,"LOCATION") + JsonIntIsNullCheck(object,"LENGTH"), 33);
                    stringBuilder.setSpan(new StyleSpan(Typeface.BOLD), JsonIntIsNullCheck(object,"LOCATION"), JsonIntIsNullCheck(object,"LOCATION") + JsonIntIsNullCheck(object,"LENGTH"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                txtContents.append(stringBuilder);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            txtContents.setText(commentItem.getContents());
        }

        View finalListView = listView;
        txtReComment11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if (beforeIntent.hasExtra("guest")){
                    Intent intent = new Intent(NewCommunityDetailActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
                }else{
    edtComment.setFocusableInTouchMode(true);
                edtComment.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(edtComment,1);

                selectCommentNo = commentItem.getCommentNo();
                mentionUserNoList = null;
                mentionSelectList = null;

                if (mentionSelectList == null){
                    mentionSelectList = new ArrayList<>();
                    mentionUserNoList = new ArrayList<>();
                }
                //memo 태그시 주석제거
                mentionSelectList.add("@" + commentItem.getNickname());
                mentionUserNoList.add(Integer.valueOf(commentItem.getUserNo()));
                edtComment.setText("@" + commentItem.getNickname() + " ");
                edtComment.selectColorMentionString();

                groupNum = commentItem.getGroupNum();
                originCommentNo = commentItem.getOriginCommentNo();
                originReCommentNo = commentItem.getOriginReCommentNo();

                scrollToView(finalListView,finalListView,scrollView,0);

                }

            }
        });

        linCommentParent.addView(listView);
    }
    TwoButtonDialog twoButtonDialog;

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

    void Thirdpopup(final int flag, final String no, final String contents, String mentions){
        final Dialog dialog = new BottomSheetDialog(NewCommunityDetailActivity.this,R.style.SheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_comment_third_btn,null);

        TextView txtRemove = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_remove);
        TextView txtEdit = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_edit);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_comment_third_cancel);


        dialog.setContentView(contentView);

        txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                twoButtonDialog = new TwoButtonDialog(NewCommunityDetailActivity.this,"삭제하기","댓글을 삭제하시겠습니까?","취소","삭제");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        if (flag == 2){
                            txtComment.setText("" + (--mCommentCnt));
                            NetWorkCall(NEW_COMMENT_DELETE);
                        }else{
                            txtComment.setText("" + (--mCommentCnt));
                            NetWorkCall(NEW_COMMENT_REPLY_DELETE);
                        }
                    }
                });
            }
        });

        ArrayList<String> nicknameList = new ArrayList<>();
        ArrayList<Integer> userNoList = new ArrayList<>();
        ArrayList<Integer> startList = new ArrayList<>();
        ArrayList<Integer> endList = new ArrayList<>();

        if (mentions.length() > 0){
            try {
                JSONArray jsonArray = new JSONArray(mentions);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    nicknameList.add(JsonIsNullCheck(object,"NICKNAME"));
                    userNoList.add(JsonIntIsNullCheck(object,"TARGET_USER_NO"));
                    startList.add(JsonIntIsNullCheck(object,"LOCATION"));
                    endList.add(JsonIntIsNullCheck(object,"LENGTH"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                status = flag;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mentionSelectList == null){
                            mentionSelectList = new ArrayList<>();
                            mentionUserNoList = new ArrayList<>();
                        }
                        mentionSelectList.addAll(nicknameList);
                        mentionUserNoList.addAll(userNoList);

                        mentionEnabled = false;
                        linMentionParent.removeAllViews();
                        scrMention.setVisibility(View.GONE);

                        edtComment.setText(contents);
                        for (int i = 0; i < mentionSelectList.size(); i++){
                            try {
                                edtComment.selectColorMentionString(startList.get(i),endList.get(i));
                            }catch (IndexOutOfBoundsException e){

                            }
                        }

                        edtComment.setSelection(contents.length());
                        edtComment.setFocusableInTouchMode(true);
                        edtComment.requestFocus();
                        imm.showSoftInput(edtComment, 0);
                    }
                },500);
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

    void Twopopup(final int flag, final String no){
        final Dialog dialog = new BottomSheetDialog(NewCommunityDetailActivity.this,R.style.SheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_comment_two_btn,null);

        TextView txtDeclaration = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_declaration);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_comment_two_cancel);

        dialog.setContentView(contentView);

        txtDeclaration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(NewCommunityDetailActivity.this, InquiryActivity.class);
                i.putExtra("inquiry","inquiry");
                startActivity(i);
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

    private String MentionJsonArray(){
        //memo 태그시 주석제거

        Log.i(TAG,"mention array1 : " + edtComment.getMentionList(true));

        mentionNicknameList = edtComment.getMentionList(true);
        mentionLocationList = edtComment.getMentionLocation(true);
        mentionLengthList = edtComment.getMentionLengthList(true);

        Log.i(TAG,"mention array2 : " + edtComment.getText().toString());

        JSONArray jsonArray = new JSONArray();

        Log.i(TAG,"mention size : " + mentionNicknameList.size());

        if (mentionNicknameList.size() == 0){
            return "";
        }

        for (int i = 0; i < mentionNicknameList.size(); i++){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("NICKNAME",mentionNicknameList.get(i));
                jsonObject.accumulate("LENGTH",mentionLengthList.get(i));
                jsonObject.accumulate("LOCATION",mentionLocationList.get(i));
                jsonObject.accumulate("TARGET_USER_NO",mentionUserNoList.get(i));

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        Log.i(TAG,"mention array : " + jsonArray.toString());
        return jsonArray.toString();
    }

    private void getKeyboardHeight(View targetView){
        Rect rect = new Rect();
        targetView.getWindowVisibleDisplayFrame(rect);
        screenHeight = targetView.getRootView().getHeight();
        int tempKeyboardSize = screenHeight - rect.bottom;

        if (tempKeyboardSize > screenHeight * 0.1){
            keyboardHeight = screenHeight - rect.bottom;
            Log.i(TAG,"screenHeight height1 : " + screenHeight);
            Log.i(TAG,"keyboardHeight height2 : " + keyboardHeight);
            if (!isKeyBoardShowing){
                isKeyBoardShowing = true;
                spaceHeight = screenHeight - keyboardHeight;
            }
        }else{
            if (isKeyBoardShowing){
                isKeyBoardShowing = false;
            }
        }
        Log.i(TAG,"showing : " + isKeyBoardShowing);
    }

    public void scrollToView(View view, View view1,final ScrollView scrollView, int count) {
        if (view != null && view != scrollView) {
            count += view.getTop();
            scrollToView((View) view.getParent(),view1, scrollView, count);
        } else if (scrollView != null) {
            final int finalCount = (count - (spaceHeight - (view1.getHeight() * 2)));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.smoothScrollTo(0, finalCount);
                }
            }, 500);
        }
    }

    @Override
    public void onBackPressed() {
        pushFlag = beforeIntent.getBooleanExtra("push",false);
        Log.i(TAG,"result 1111");
        if(pushFlag){
            Intent communityIntent = new Intent(NewCommunityDetailActivity.this,MainActivity.class);
            communityIntent.putExtra("community", true);
            communityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(communityIntent);

//            setResult(RESULT_OK);
        }else if (communityDetailRefresh){
            Intent intent = null;
            intent = new Intent();
            intent.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
            intent.putExtra("position",beforeIntent.getIntExtra("position",-1));
            setResult(RESULT_OK,intent);
        }else{
            Log.i(TAG,"result 5555");
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_community_detail_back : {
                pushFlag = beforeIntent.getBooleanExtra("push",false);
                if(pushFlag){
                    Intent communityIntent = new Intent(NewCommunityDetailActivity.this,MainActivity.class);
                    communityIntent.putExtra("community", true);
                    communityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(communityIntent);
                }else if (communityDetailRefresh){
                    Intent intent = null;
                    intent = new Intent();
                    intent.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
                    intent.putExtra("position",beforeIntent.getIntExtra("position",-1));
                    setResult(RESULT_OK,intent);
                }else{
                    Log.i(TAG,"result 5555");
                    setResult(RESULT_OK);
                }
                finish();
                break;
            }
            case R.id.btn_community_detail_comment_send : {
                if (beforeIntent.hasExtra("guest")){
                    Intent intent = new Intent(NewCommunityDetailActivity.this, NewAnotherLoginActivity.class);
                    startActivity(intent);
                }else{
                    btnCommentSend.setEnabled(false);
                    if (status == 1){
                        if (selectCommentNo.length() == 0){
                            if (edtComment.getText().length() > 0){
                                NetWorkCall(NEW_COMMENT_INSERT);
                            }
                        }else{
                            NetWorkCall(NEW_COMMENT_REPLY_INSERT);
                        }
                    }else if (status == 2){
                        if (edtComment.getText().length() > 0){
                            NetWorkCall(NEW_COMMENT_UPDATE);
                        }
                    }else if (status == 3){
                        if (edtComment.getText().length() > 0){
                            NetWorkCall(NEW_COMMENT_REPLY_UPDATE);
                        }
                    }
                }

                break;
            }
            case R.id.src_community_detail : {

                break;
            }
            case R.id.lin_community_detail_comment_parent : {

                break;
            }
        }
    }

    public void setHideKeyboard(final Context context, View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText || view instanceof ScrollView)) {

                view.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        return false;
                    }

                });
            }

            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {

                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                    View innerView = ((ViewGroup) view).getChildAt(i);

                    setHideKeyboard(context, innerView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111){
            if (resultCode == RESULT_OK){
                communityDetailRefresh = true;
                NetWorkCall(NEW_COMMUNITY_DETAIL);
            }
        }
    }
}