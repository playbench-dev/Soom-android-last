package com.kmw.soom2.Communitys.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.MyPage.Activity.AccountActivity;
import com.kmw.soom2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.COMMUNITY_MENU_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HASH_TAG_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.IMAGE_DELETE_URL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NEW_COMMUNITY_UPDATE;
import static com.kmw.soom2.Common.Utils.CAMERA_PERMISSION;
import static com.kmw.soom2.Common.Utils.COMMUNITY_LV_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_MUNU_NO_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_NAME_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_PARENT_LIST;
import static com.kmw.soom2.Common.Utils.COMMUNITY_POPULARUTY;
import static com.kmw.soom2.Common.Utils.COMMUNITY_SELECTABLE;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;

public class CommunityWriteActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse, RadioGroup.OnCheckedChangeListener {

    private String TAG = "CommunityWriteActivity";
    TextView txtBack;
    LinearLayout linTagParent;
    EditText edtTag;
    EditText edtContents;
    LinearLayout linContents;
    LinearLayout linPicture, linPictureParent;
    TextView txtPictureLength;
    Button btnFinish,btnAdd;
    TextView txtContentsLength;
    ScrollView scrollView;
    TextView txtCategory,txtBoard;
    BottomSheetDialog mBottomSheetCategoryDialog;
    BottomSheetDialog mBottomSheetDialog;
    RadioGroup linCategoryParent;
    TextView txtCategoryTitle;
    LinearLayout linNewCategoryParent01,linNewCategoryParent02;
    TextView txtNewCategoryTitle01,txtNewCategoryContents01,txtNewCategoryTitle02,txtNewCategoryContents02;
    RadioButton rdoNewCategory01,rdoNewCategory02;
    ArrayList<RadioButton> radioButtonArrayList = new ArrayList<>();
    ArrayList<String> menuNoList = new ArrayList<>();
    ArrayList<String> cMenuNoList = new ArrayList<>();
    Typeface typefaceBold, typefaceRegular;

    Handler mHandler = new Handler();

    ArrayList<String> tagSelectList = new ArrayList<>();
    ArrayList<String> sumTagList = new ArrayList<>();

    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    public ArrayList<String> imgPathList = new ArrayList<>();

//    ArrayList<String> tagList;

    String realUrl = "";

    String[] realUrlList = new String[]{};
    ArrayList<String> testList = new ArrayList<>();

    Intent beforeIntent;

    String hashTag = "";
    ProgressDialog progressDialog;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TwoButtonDialog twoButtonDialog;
    String mImagesNo = "";
    String mMenuNo = "";
    String mCMenuNo = "";

    int flag = 1; // 1 - 글쓰기, 2 - 글수정, 3 - 공유하기

    private String mSaveBeforeHashTag = "";
    private String mSaveBeforeContents = "";
    private String mSaveBeforeUrl = "";

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_write);

        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        NullCheck(this);

        FindViewById();

        beforeIntent = getIntent();

        typefaceBold = ResourcesCompat.getFont(this, R.font.notosanscjkkr_bold);
        typefaceRegular = ResourcesCompat.getFont(this, R.font.notosanscjkkr_medium_regular);

        Log.i(TAG,"hashTag : " + beforeIntent.getStringExtra("hashTag"));

        if (beforeIntent != null) {
            if (beforeIntent.hasExtra("imagesNo")){
                mImagesNo = beforeIntent.getStringExtra("imagesNo");
            }
            if (beforeIntent.hasExtra("hospitalName")) {
                flag = 3;
                // memo: 2021-01-13 김지훈 수정 시작
                // memo: 병원 공유하기 시 카테고리 안보이는 부분, 공유하는 텍스트 관련 whitespace 제거
                Log.i(TAG, "hos name : " + (beforeIntent.getStringExtra("hospitalName") + "|" + beforeIntent.hasExtra("hospitalName")));
                if (beforeIntent.hasExtra("hospitalName"))
                    if (!beforeIntent.getStringExtra("hospitalName").equals(""))
                        edtContents.setText(beforeIntent.getStringExtra("hospitalName") + "\n");
                if (beforeIntent.hasExtra("hospitalAddress"))
                    if (!beforeIntent.getStringExtra("hospitalAddress").equals(""))
                        edtContents.append(beforeIntent.getStringExtra("hospitalAddress") + "\n");
                if (beforeIntent.hasExtra("hospitalDepartment"))
                    if (!beforeIntent.getStringExtra("hospitalDepartment").equals(""))
                        edtContents.append(beforeIntent.getStringExtra("hospitalDepartment") + "\n");
                edtContents.append(beforeIntent.getStringExtra("hospitalDoctorName") + "\n\n" +
                        "\uD83D\uDC49의사 친절도 ( ) / 5점\n" +
                        "\uD83D\uDC49직원 친절도 ( ) / 5점\n" +
                        "\uD83D\uDC49진료의 효과 ( ) / 5점 \n" +
                        "\uD83D\uDC49병원 한줄 평 : ");

                if (Utils.tagList != null){
                    for (int i = 0; i < Utils.tagList.size(); i++) {
                        UnSelectTagList(Utils.tagList.get(i), 0);
                    }
                    sumTagList.addAll(Utils.tagList);
                }else{
                    NetWorkCall(HASH_TAG_LIST);
                }

                btnFinish.setText("공유하기");
                // memo: 2021-01-13 김지훈 수정 종료

            }else if (beforeIntent.hasExtra("communityNo")) {

                flag = 2;

                edtContents.setText(beforeIntent.getStringExtra("contents"));

                if (beforeIntent.hasExtra("imgsPath")){
                    realUrl = beforeIntent.getStringExtra("imgsPath");

                    if (realUrl.length() > 0) {
                        realUrlList = realUrl.split(",");
                        testList = new ArrayList<>(Arrays.asList(realUrlList));
                        txtPictureLength.setText(""+testList.size()+"/5");
                        for (int i = 0; i < testList.size(); i++) {
                            selectImg("" + i, testList.get(i), null);
                        }
                    }
                }

                if (beforeIntent.hasExtra("hashTag")){
                    if (beforeIntent.getStringExtra("hashTag").length() > 0) {
                        hashTag = beforeIntent.getStringExtra("hashTag").replace("##", "#");

                        String[] hashTagList = hashTag.split("#");

                        for (int i = hashTagList.length - 1; i >= 1; i--) {
                            UnSelectTagList("#" + hashTagList[i], 1);
                        }
                        if (Utils.tagList != null){
                            for (int i = 0; i < Utils.tagList.size(); i++) {
                                if (!hashTag.contains(Utils.tagList.get(i))) {
                                    UnSelectTagList(Utils.tagList.get(i), 0);
                                }
                            }
                            sumTagList.addAll(Utils.tagList);
                        }else{
                            NetWorkCall(HASH_TAG_LIST);
                        }
                    }
                }else{
                    if (Utils.tagList != null){
                            for (int i = 0; i < Utils.tagList.size(); i++) {
                                UnSelectTagList(Utils.tagList.get(i), 0);
                            }
                        sumTagList.addAll(Utils.tagList);
                    }else{
                        NetWorkCall(HASH_TAG_LIST);
                    }
                }
            } else if (beforeIntent.hasExtra("medicine")){
                edtContents.setText(beforeIntent.getStringExtra("contents"));
                if (Utils.tagList != null){
                    for (int i = 0; i < Utils.tagList.size(); i++) {
                        UnSelectTagList(Utils.tagList.get(i), 0);
                    }
                    sumTagList.addAll(Utils.tagList);
                }else{
                    NetWorkCall(HASH_TAG_LIST);
                }
            }else {
                Log.i(TAG,"2222");
                flag = 1;
                if (beforeIntent.hasExtra("hashTag")){
                    if (beforeIntent.getStringExtra("hashTag").length() > 0) {
                        hashTag = beforeIntent.getStringExtra("hashTag").replace("##", "#");

                        String[] hashTagList = hashTag.split("#");

                        for (int i = hashTagList.length - 1; i >= 1; i--) {
                            UnSelectTagList("#" + hashTagList[i], 1);
                        }
                        if (Utils.tagList != null){
                            for (int i = 0; i < Utils.tagList.size(); i++) {
                                if (!hashTag.contains(Utils.tagList.get(i))) {
                                    UnSelectTagList(Utils.tagList.get(i), 0);
                                }
                            }
                            sumTagList.addAll(Utils.tagList);
                        }else{
                            NetWorkCall(HASH_TAG_LIST);
                        }
                    }
                }else{
                    if (Utils.tagList != null){
                        for (int i = 0; i < Utils.tagList.size(); i++) {
                            if (!hashTag.contains(Utils.tagList.get(i))) {
                                UnSelectTagList(Utils.tagList.get(i), 0);
                            }
                        }
                        sumTagList.addAll(Utils.tagList);
                    }else{
                        NetWorkCall(HASH_TAG_LIST);
                    }
                }

                if (beforeIntent.hasExtra("sharedData")) {
                    flag = 3;
                    edtContents.setText(beforeIntent.getStringExtra("sharedData"));
                    btnFinish.setText("공유하기");
                }
            }
        } else {
            flag = 1;
            if (Utils.tagList != null){
                for (int i = 0; i < Utils.tagList.size(); i++) {
                    UnSelectTagList(Utils.tagList.get(i), 0);
                }
                sumTagList.addAll(Utils.tagList);
            }else{
                NetWorkCall(HASH_TAG_LIST);
            }
        }
        for (int i = 0; i < tagSelectList.size(); i++){
            if (i == 0){
                mSaveBeforeHashTag += tagSelectList.get(i);
            }else{
                mSaveBeforeHashTag += ","+tagSelectList.get(i);
            }
        }
        mSaveBeforeContents = edtContents.getText().toString();
        mSaveBeforeUrl = realUrl;

        mBottomSheetCategoryDialog = new BottomSheetDialog(this,R.style.SheetDialog);
        final View dialogCategoryView = getLayoutInflater().inflate(R.layout.dialog_community_category, null);
        mBottomSheetCategoryDialog.setContentView(dialogCategoryView);
        linNewCategoryParent01 = dialogCategoryView.findViewById(R.id.lin_category_select_01);
        linNewCategoryParent02 = dialogCategoryView.findViewById(R.id.lin_category_select_02);
        txtNewCategoryTitle01 = dialogCategoryView.findViewById(R.id.txt_community_category_title_01);
        txtNewCategoryTitle02 = dialogCategoryView.findViewById(R.id.txt_community_category_title_02);
        txtNewCategoryContents01 = dialogCategoryView.findViewById(R.id.txt_community_category_contents_01);
        txtNewCategoryContents02 = dialogCategoryView.findViewById(R.id.txt_community_category_contents_02);
        rdoNewCategory01 = dialogCategoryView.findViewById(R.id.rdo_community_category_01);
        rdoNewCategory02 = dialogCategoryView.findViewById(R.id.rdo_community_category_02);

        linNewCategoryParent01.setOnClickListener(this);
        linNewCategoryParent02.setOnClickListener(this);
        rdoNewCategory01.setOnClickListener(this);
        rdoNewCategory02.setOnClickListener(this);

        mBottomSheetDialog = new BottomSheetDialog(this, R.style.SheetDialog);
        final View dialogView = getLayoutInflater().inflate(R.layout.view_community_write_category_dialog, null);
        linCategoryParent = (RadioGroup)dialogView.findViewById(R.id.rdo_category_parent) ;
        txtCategoryTitle = (TextView)dialogView.findViewById(R.id.txt_community_category_title);
        mBottomSheetDialog.setContentView(dialogView);
        linCategoryParent.setOnCheckedChangeListener(this);

        if (beforeIntent.hasExtra("menuNo")){
            mMenuNo = beforeIntent.getStringExtra("menuNo");
            mCMenuNo = beforeIntent.getStringExtra("cMenuNo");

//            txtCategory.setText(beforeIntent.getStringExtra("menuTitle"));
//            txtBoard.setText(beforeIntent.getStringExtra("cMenuTitle"));
        }

        if (COMMUNITY_MUNU_NO_LIST == null){
            NetWorkCall(COMMUNITY_MENU_LIST);
        }else{
//            mMenuNo = beforeIntent.getStringExtra("menuNo");
//            mCMenuNo = beforeIntent.getStringExtra("cMenuNo");
            if (beforeIntent.hasExtra("menuNo")){
                mMenuNo = beforeIntent.getStringExtra("menuNo");
                mCMenuNo = beforeIntent.getStringExtra("cMenuNo");

                for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                    if (COMMUNITY_MUNU_NO_LIST.get(i).equals(mMenuNo)){
                        txtCategory.setText(COMMUNITY_NAME_LIST.get(i));
                    }else if (COMMUNITY_MUNU_NO_LIST.get(i).equals(mCMenuNo)){
                        txtBoard.setText(COMMUNITY_NAME_LIST.get(i));
                    }
                }
            }

        }

        if (beforeIntent.hasExtra("menuWriteNo")){
            if (!beforeIntent.getStringExtra("menuWriteNo").equals("3")){
                mMenuNo = beforeIntent.getStringExtra("menuWriteNo");
                txtCategory.setText(beforeIntent.getStringExtra("menuWriteTitle"));
            }else{
                for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                    if (COMMUNITY_LV_LIST.get(i).equals("1")){
                        if (COMMUNITY_MUNU_NO_LIST.get(i).equals("1")){
                            mMenuNo = COMMUNITY_MUNU_NO_LIST.get(i);
                            txtCategory.setText(COMMUNITY_NAME_LIST.get(i));
                            break;
                        }
                    }
                }
            }
        }
    }

    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_community_write_back);
        linTagParent = (LinearLayout) findViewById(R.id.lin_community_write_tag_parent);
        edtTag = (EditText) findViewById(R.id.edt_community_writing_tag_insert);
        edtContents = (EditText) findViewById(R.id.edt_community_writing_contents);
        linContents = (LinearLayout) findViewById(R.id.lin_community_writing_contents);
        linPicture = (LinearLayout) findViewById(R.id.lin_community_write_picture);
        linPictureParent = (LinearLayout) findViewById(R.id.lin_community_write_picture_parent);
        btnFinish = (Button) findViewById(R.id.btn_community_write_finish);
        txtContentsLength = (TextView) findViewById(R.id.txt_community_write_contents_length);
        txtPictureLength = (TextView) findViewById(R.id.txt_community_write_picture_length);
        scrollView = (ScrollView) findViewById(R.id.src_community_write);
        btnAdd = (Button) findViewById(R.id.btn_community_write_add);
        txtCategory = (TextView)findViewById(R.id.txt_community_write_category);
        txtBoard = (TextView)findViewById(R.id.txt_community_write_board);

        txtBack.setOnClickListener(this);
        linPicture.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        txtCategory.setOnClickListener(this);
        txtBoard.setOnClickListener(this);


        edtTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0){
                    btnAdd.setBackgroundTintList(null);
                    btnAdd.setTextColor(getColor(R.color.black));
                }else{
                    btnAdd.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
                    btnAdd.setTextColor(getColor(R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTag.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                switch (actionId) {

                    case EditorInfo.IME_ACTION_DONE:
                        if (edtTag.getText().length() != 0) {
                            if (edtTag.getText().toString().contains("#")) {
                                if (sumTagList.contains(edtTag.getText().toString())){
                                    linTagParent.removeViewAt(sumTagList.indexOf(edtTag.getText().toString()));
                                    sumTagList.remove(edtTag.getText().toString());
                                    tagSelectList.remove(edtTag.getText().toString());
                                }
                                if (!tagSelectList.contains(edtTag.getText().toString())){
                                    UnSelectTagList(edtTag.getText().toString(), 1);
                                    sumTagList.add(0,edtTag.getText().toString());
                                }
                            } else {
                                if (sumTagList.contains("#"+edtTag.getText().toString())){
                                    linTagParent.removeViewAt(sumTagList.indexOf("#"+edtTag.getText().toString()));
                                    sumTagList.remove("#"+edtTag.getText().toString());
                                    tagSelectList.remove("#"+edtTag.getText().toString());
                                }
                                if (!tagSelectList.contains("#"+edtTag.getText().toString())){
                                    UnSelectTagList("#" + edtTag.getText().toString(), 1);
                                    sumTagList.add(0,"#"+edtTag.getText().toString());
                                }

                            }
                            edtTag.setText("");
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        edtContents.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (edtContents.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        edtContents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContentsLength.setText("("+s.toString().length()+"/2000)");
                if (s.toString().length() >= 2000){
                    Toast.makeText(CommunityWriteActivity.this, "내용 입력은 2000자까지 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setHideKeyboard(this,scrollView);
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

    void CategoryDialog(int selectFlag){
        //selectFlag 1 - category 2 - board
        linCategoryParent.removeAllViews();
        radioButtonArrayList = new ArrayList<>();
        menuNoList = new ArrayList<>();
        cMenuNoList = new ArrayList<>();

        for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
            if (selectFlag == 1){
                if (COMMUNITY_LV_LIST.get(i).equals("1") && !COMMUNITY_MUNU_NO_LIST.get(i).equals("3")){
                    if (COMMUNITY_MUNU_NO_LIST.get(i).equals("1")){
                        txtNewCategoryTitle01.setTag(COMMUNITY_MUNU_NO_LIST.get(i));
                        txtNewCategoryTitle01.setText(COMMUNITY_NAME_LIST.get(i));
                        if (mMenuNo.equals(COMMUNITY_MUNU_NO_LIST.get(i))){
                            txtNewCategoryTitle01.setTextColor(Color.rgb(9,209,130));
                            txtNewCategoryContents01.setTextColor(Color.rgb(9,209,130));
                            rdoNewCategory01.setChecked(true);
                        }else{
                            txtNewCategoryTitle01.setTextColor(Color.rgb(72,72,72));
                            txtNewCategoryContents01.setTextColor(Color.rgb(72,72,72));
                            rdoNewCategory01.setChecked(false);
                        }
                    }else{
                        txtNewCategoryTitle02.setTag(COMMUNITY_MUNU_NO_LIST.get(i));
                        txtNewCategoryTitle02.setText(COMMUNITY_NAME_LIST.get(i));
                        if (mMenuNo.equals(COMMUNITY_MUNU_NO_LIST.get(i))){
                            txtNewCategoryTitle02.setTextColor(Color.rgb(9,209,130));
                            txtNewCategoryContents02.setTextColor(Color.rgb(9,209,130));
                            rdoNewCategory02.setChecked(true);
                        }else{
                            txtNewCategoryTitle02.setTextColor(Color.rgb(72,72,72));
                            txtNewCategoryContents02.setTextColor(Color.rgb(72,72,72));
                            rdoNewCategory02.setChecked(false);
                        }
                    }
                    menuNoList.add(COMMUNITY_MUNU_NO_LIST.get(i));
                }
            }else if (selectFlag == 2){
                txtCategoryTitle.setText("상세 게시판");
                if (COMMUNITY_LV_LIST.get(i).equals("2") && COMMUNITY_PARENT_LIST.get(i).equals(mMenuNo) && COMMUNITY_SELECTABLE.get(i).equals("1")){
                    RadioButton rdoBtn = new RadioButton(this);
                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(40));
//                    params.topMargin = 20;
                    rdoBtn.setLayoutParams(params);
                    Drawable image = getResources().getDrawable( R.drawable.community_check_box );
                    int h = dpToPx(20);
                    int w = dpToPx(20);
                    image.setBounds( 0, 0, w, h );
                    rdoBtn.setCompoundDrawables(null,null,image,null);
//                    rdoBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.community_check_box,0);
                    rdoBtn.setText(COMMUNITY_NAME_LIST.get(i));
                    rdoBtn.setButtonDrawable(null);

                    rdoBtn.setTypeface(typefaceRegular);

                    if (mCMenuNo.equals(COMMUNITY_MUNU_NO_LIST.get(i))){
                        Log.i(TAG,"mCMenuNo : " + mCMenuNo + " aa : " + COMMUNITY_NAME_LIST.get(i));

                        rdoBtn.setTypeface(typefaceBold);
                        rdoBtn.setTextColor(Color.parseColor("#09d182"));
                        rdoBtn.setChecked(true);
                    }else{
                        rdoBtn.setTypeface(typefaceRegular);
                        rdoBtn.setTextColor(Color.parseColor("#8E8E8E"));
                    }

                    int finalI = i;
                    rdoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCMenuNo = COMMUNITY_MUNU_NO_LIST.get(finalI);
                            txtBoard.setText(COMMUNITY_NAME_LIST.get(finalI));
                            mBottomSheetDialog.dismiss();
                        }
                    });

                    linCategoryParent.addView(rdoBtn);
                    radioButtonArrayList.add(rdoBtn);
                    menuNoList.add(COMMUNITY_MUNU_NO_LIST.get(i));
                }
            }
        }

        if (selectFlag == 1){
            mBottomSheetCategoryDialog.show();
        }else{
            mBottomSheetDialog.show();
        }
    }

    public int dpToPx(float dp){
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
        return px;
    }

    void UnSelectTagList(final String name, final int mode) {
        View listView = new View(getApplicationContext());
        listView = getLayoutInflater().inflate(R.layout.view_un_select_tag_list_item, null);
        CheckBox rdoTag = (CheckBox)listView.findViewById(R.id.linear_un_select_tag_list_item);
        rdoTag.setText(name);

        if (mode == 0) {
            linTagParent.addView(listView);
        } else if (mode == 1) {
            if (!tagSelectList.contains(name)){
                linTagParent.addView(listView, 0);
                tagSelectList.add(name);
                rdoTag.setTextColor(getResources().getColor(R.color.white));
                rdoTag.setChecked(true);
            }
        }

        rdoTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tagSelectList.contains(name)) {
                    tagSelectList.remove(name);
                    rdoTag.setTextColor(Color.parseColor("#bababa"));
                } else {
                    tagSelectList.add(name);
                    rdoTag.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {

            fileMakedirs();

            Intent i = new Intent(CommunityWriteActivity.this, GalleryActivity.class);
            if (imgPathList.size() > 0) {
                i.putExtra("imagePathList", imgPathList);
            }
            startActivityForResult(i, 1111);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")) {
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
                Toast.makeText(CommunityWriteActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                WRITE_PERMISSION = true;
                READ_PERMISSION = true;
                fileMakedirs();
            }

            if (deniedPermissions.contains("android.permission.CAMERA")) {
                CAMERA_PERMISSION = false;
                Toast.makeText(CommunityWriteActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                CAMERA_PERMISSION = true;
            }
        }
    };

    void fileMakedirs() {
        String str = Environment.getExternalStorageState();
        if (str.equals(Environment.MEDIA_MOUNTED)) {

            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(dirPath);
            if (!file.exists()) { // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
            }
        } else {
            Toast.makeText(CommunityWriteActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
        }
    }

    void selectImg(final String idx, final String imagePath, final Bitmap bitmap) {
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_select_gallery_list_item, null);

        ImageView img = (ImageView) listView.findViewById(R.id.img_select_gallery_list_item);
        ImageView imgRemove = (ImageView) listView.findViewById(R.id.img_select_gallery_list_item_remove);

//        Glide.with(this).load(file).into(img);
        String replaceText = imagePath;
        if (replaceText.contains("soom2.testserver-1.com:8080")){
            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
        }else if (replaceText.contains("103.55.190.193")){
            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(this)
                .load("https:" + replaceText)
                .apply(requestOptions)
                .into(img);

        final View finalListView = listView;

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replaceText = imagePath;
                if (replaceText.contains("soom2.testserver-1.com:8080")){
                    replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
                }else if (replaceText.contains("103.55.190.193")){
                    replaceText = replaceText.replace("103.55.190.193","soomcare.info");
                }
                imgDetail("https:" + replaceText);
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linPictureParent.removeView(finalListView);

                testList.remove(imagePath);
                txtPictureLength.setText(""+testList.size()+"/5");
                if (testList.size() == 0){
                    realUrl = "";
                }else{
                    for (int i = 0; i < testList.size(); i++) {
                        if (i == 0) {
                            realUrl = testList.get(i);
                        } else {
                            realUrl += "," + testList.get(i);
                        }
                    }
                }
            }
        });
        linPictureParent.addView(listView);
    }

    void imgDetail(String imagePath) {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.view_img_detail, null);

        ImageView img = (ImageView) contentView.findViewById(R.id.img_detail);
        TextView txtClose = (TextView) contentView.findViewById(R.id.txt_img_detail);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.addContentView(contentView, params);

        Glide.with(this).load(imagePath).into(img);

        dialog.show();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111) {
            if (resultCode == RESULT_OK) {
                linPictureParent.removeAllViews();

                realUrl = data.getStringExtra("realUrl");
                realUrlList = realUrl.split(",");

                testList = new ArrayList<>(Arrays.asList(realUrlList));
                txtPictureLength.setText(""+testList.size()+"/5");
                for (int i = 0; i < testList.size(); i++) {
                    selectImg("" + i, testList.get(i), null);
                }
            }
        }
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    String response = "";
    OneButtonDialog oneButtonDialog;

    void NetWorkCall(String mCode){
        if (mCode.equals(IMAGE_DELETE_URL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mImagesNo);
        }else if (mCode.equals(HASH_TAG_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
        }else if (mCode.equals(COMMUNITY_MENU_LIST)){
            new NetworkUtils.NetworkCall(CommunityWriteActivity.this,this,TAG,mCode).execute();
        }else if (mCode.equals(NEW_COMMUNITY_INSERT)){
            String hasgTag = "";

            for (int i = 0; i < tagSelectList.size(); i++) {
                if (i == 0) {
                    hasgTag = tagSelectList.get(i);
                } else {
                    hasgTag += "," + tagSelectList.get(i);
                }
            }
            new NetworkUtils.NetworkCall(CommunityWriteActivity.this,this,TAG,mCode).execute("" + Utils.userItem.getUserNo(),edtContents.getText().toString(), hasgTag.replace("#", ""),mMenuNo,mCMenuNo);
        }else if (mCode.equals(NEW_COMMUNITY_UPDATE)){
            String hasgTag = "";

            for (int i = 0; i < tagSelectList.size(); i++) {
                if (i == 0) {
                    hasgTag = tagSelectList.get(i);
                } else {
                    hasgTag += "," + tagSelectList.get(i);
                }
            }
            new NetworkUtils.NetworkCall(CommunityWriteActivity.this,this,TAG,mCode).execute(beforeIntent.getStringExtra("communityNo"),edtContents.getText().toString(), hasgTag.replace("#", ""),mMenuNo,mCMenuNo);
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (mResult != null){
            if (mCode.equals(IMAGE_DELETE_URL)){
                editor.putBoolean("communityCheck11",true);
                editor.apply();
                btnFinish.setEnabled(true);
                Intent intent = null;
                intent = new Intent();
                intent.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
                intent.putExtra("position",beforeIntent.getIntExtra("position",-1));
                setResult(RESULT_OK,intent);
                finish();
            }else if (mCode.equals(HASH_TAG_LIST)){
                try {
                    Utils.tagList = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (!JsonIsNullCheck(object, "TITLE").equals("")) {
                            Utils.tagList.add("#" + JsonIsNullCheck(object, "TITLE"));
                        }
                    }

                    for (int i = 0; i < Utils.tagList.size(); i++) {
                        if (!hashTag.contains(Utils.tagList.get(i))) {
                            UnSelectTagList(Utils.tagList.get(i), 0);
                        }
                    }

                    sumTagList.addAll(Utils.tagList);
                } catch (JSONException e) {

                }
            }else if (mCode.equals(COMMUNITY_MENU_LIST)){
                try {
                    COMMUNITY_MUNU_NO_LIST = new ArrayList<>();
                    COMMUNITY_LV_LIST = new ArrayList<>();
                    COMMUNITY_NAME_LIST = new ArrayList<>();
                    COMMUNITY_PARENT_LIST = new ArrayList<>();
                    COMMUNITY_SELECTABLE = new ArrayList<>();
                    COMMUNITY_POPULARUTY = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++){
                        COMMUNITY_MUNU_NO_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"MENU_NO"));
                        COMMUNITY_LV_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"LV"));
                        COMMUNITY_NAME_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"NAME"));
                        COMMUNITY_PARENT_LIST.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"PARENT"));
                        COMMUNITY_SELECTABLE.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"SELECTABLE"));
                        COMMUNITY_POPULARUTY.add(JsonIsNullCheck(jsonArray.getJSONObject(i),"POPULARITY"));
                    }

                    if (beforeIntent.hasExtra("menuNo")){
                        mMenuNo = beforeIntent.getStringExtra("menuNo");
                        mCMenuNo = beforeIntent.getStringExtra("cMenuNo");

                        for (int i = 0; i < COMMUNITY_MUNU_NO_LIST.size(); i++){
                            if (COMMUNITY_MUNU_NO_LIST.get(i).equals(mMenuNo)){
                                txtCategory.setText(COMMUNITY_NAME_LIST.get(i));
                            }else if (COMMUNITY_MUNU_NO_LIST.get(i).equals(mCMenuNo)){
                                txtBoard.setText(COMMUNITY_NAME_LIST.get(i));
                            }
                        }
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(NEW_COMMUNITY_INSERT)){
                Log.i(TAG,"insert : " + mResult);
                if (realUrl.length() == 0) {
                    if (beforeIntent.hasExtra("sharedData")) {
                        oneButtonDialog= new OneButtonDialog(CommunityWriteActivity.this, "공유하기", "커뮤니티 공유 완료", "확인");
                        oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                editor.putBoolean("communityCheck11",true);
                                editor.apply();
                                finish();
                            }
                        });
                    } else {
                        editor.putBoolean("communityCheck11",true);
                        editor.apply();
                        btnFinish.setEnabled(true);
                        Intent intent = null;
                        intent = new Intent();
                        intent.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
                        intent.putExtra("position",beforeIntent.getIntExtra("position",-1));
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(mResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            new InsertImageNetWork().execute(String.valueOf(object.get("COMMUNITY_NO")));
                        }
                    } catch (JSONException e) {
                        btnFinish.setEnabled(true);
                    }
                }
            }else if (mCode.equals(NEW_COMMUNITY_UPDATE)){
                if (realUrl.length() == 0) {
                    NetWorkCall(IMAGE_DELETE_URL);
                } else {
                    if (beforeIntent.getStringExtra("imgsPath").length() == 0) {
                        new InsertImageNetWork().execute(beforeIntent.getStringExtra("communityNo"));
                    } else {
                        new UpdateImageNetWork().execute(beforeIntent.getStringExtra("communityNo"));
                    }
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int radioButtonID = group.getCheckedRadioButtonId();
        View radioButton = group.findViewById(radioButtonID);
        int idx = 0;
        if (radioButton instanceof RadioButton){
            idx = group.indexOfChild(radioButton);
        }
//        for (int i = 0; i < radioButtonArrayList.size(); i++){
//            if (i == idx){
//                radioButtonArrayList.get(i).setTypeface(typefaceBold);
//                radioButtonArrayList.get(i).setTextColor(Color.parseColor("#343434"));
//            }else{
//                radioButtonArrayList.get(i).setTypeface(typefaceRegular);
//                radioButtonArrayList.get(i).setTextColor(Color.parseColor("#8E8E8E"));
//            }
//        }
    }

    public class InsertCommunityNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String hasgTag = "";

            for (int i = 0; i < tagSelectList.size(); i++) {
                if (i == 0) {
                    hasgTag = tagSelectList.get(i);
                } else {
                    hasgTag += "," + tagSelectList.get(i);
                }
            }

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("USER_NO", "" + Utils.userItem.getUserNo())
                    .add("CONTENTS", edtContents.getText().toString())
                    .add("HASHTAG", hasgTag.replace("#", ""))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertCommunity(), body);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (realUrl.length() == 0) {
                if (beforeIntent.hasExtra("sharedData")) {
                    oneButtonDialog= new OneButtonDialog(CommunityWriteActivity.this, "공유하기", "커뮤니티 공유 완료", "확인");
                    oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                        @Override
                        public void OnButtonClick(View v) {
                            editor.putBoolean("communityCheck11",true);
                            editor.apply();
                            finish();
                        }
                    });
                } else {
                    editor.putBoolean("communityCheck11",true);
                    editor.apply();
                    btnFinish.setEnabled(true);
                    Intent intent = null;
                    intent = new Intent();
                    intent.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
                    intent.putExtra("position",beforeIntent.getIntExtra("position",-1));
                    setResult(RESULT_OK,intent);
                    finish();

                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        new InsertImageNetWork().execute(String.valueOf(object.get("COMMUNITY_NO")));
                    }
                } catch (JSONException e) {
                    btnFinish.setEnabled(true);
                }
            }
        }
    }

    public class InsertImageNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("WRITING_NO", strings[0])
                    .add("CATEGORY", "2")
                    .add("IMAGE_FILE", realUrl)
                    .add("IMAGE_ORDER_NO", "1")
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.insertImage(), body);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (beforeIntent.hasExtra("sharedData")) {
                oneButtonDialog= new OneButtonDialog(CommunityWriteActivity.this, "공유하기", "커뮤니티 공유 완료", "확인");
                oneButtonDialog.setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                    @Override
                    public void OnButtonClick(View v) {
                        editor.putBoolean("communityCheck11",true);
                        editor.apply();
                        finish();
                    }
                });
            } else {
                editor.putBoolean("communityCheck11",true);
                editor.apply();
                btnFinish.setEnabled(true);
                Intent intent = null;
                intent = new Intent();
                intent.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
                intent.putExtra("position",beforeIntent.getIntExtra("position",-1));
                setResult(RESULT_OK,intent);
                finish();

            }
        }
    }


    public class UpdateCommunityNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String hasgTag = "";

            for (int i = 0; i < tagSelectList.size(); i++) {
                if (i == 0) {
                    hasgTag = tagSelectList.get(i);
                } else {
                    hasgTag += "," + tagSelectList.get(i);
                }
            }

            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("COMMUNITY_NO", beforeIntent.getStringExtra("communityNo"))
                    .add("CONTENTS", edtContents.getText().toString())
                    .add("HASHTAG", hasgTag.replace("#", ""))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateCommunity(), body);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            if (realUrl.length() == 0) {
                NetWorkCall(IMAGE_DELETE_URL);
            } else {
                if (beforeIntent.getStringExtra("imgsPath").length() == 0) {
                    new InsertImageNetWork().execute(beforeIntent.getStringExtra("communityNo"));
                } else {
                    new UpdateImageNetWork().execute(beforeIntent.getStringExtra("communityNo"));
                }
            }

        }
    }

    public class UpdateImageNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            @SuppressLint("WrongThread") RequestBody body = new FormBody.Builder()
                    .add("WRITING_NO", strings[0])
                    .add("CATEGORY", "2")
                    .add("IMAGE_FILE", realUrl)
                    .add("IMAGE_ORDER_NO", "1")
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateImage(), body);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            editor.putBoolean("communityCheck11",true);
            editor.apply();
            btnFinish.setEnabled(true);
            Intent intent = null;
            intent = new Intent();
            intent.putExtra("communityNo",beforeIntent.getStringExtra("communityNo"));
            intent.putExtra("position",beforeIntent.getIntExtra("position",-1));
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onBackPressed() {
        String title = "";
        if (flag == 1){
            title = "게시글 작성";
        }else if (flag == 2){
            title = "게시글 수정";
        }else if (flag == 3){
            title = "공유하기";
        }
        if (tagSelectList.size() > 0){
            String tag = "";
            for (int i = 0; i < tagSelectList.size(); i++){
                if (i == 0){
                    tag += tagSelectList.get(i);
                }else{
                    tag += ","+tagSelectList.get(i);
                }
            }
            if (mSaveBeforeHashTag.equals(tag) && mSaveBeforeContents.equals(edtContents.getText().toString()) && mSaveBeforeUrl.equals(realUrl)){
                finish();
            }else{
                twoButtonDialog = new TwoButtonDialog(CommunityWriteActivity.this,title, "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        twoButtonDialog.dismiss();
                        finish();
                    }
                });

                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        twoButtonDialog.dismiss();
                    }
                });
            }
        }else{
            if (mSaveBeforeContents.equals(edtContents.getText().toString()) && mSaveBeforeUrl.equals(realUrl)){
                finish();
            }else{
                twoButtonDialog = new TwoButtonDialog(CommunityWriteActivity.this,title, "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        twoButtonDialog.dismiss();
                        finish();
                    }
                });

                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        twoButtonDialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    check();
                } else {
                    fileMakedirs();

                    Intent i = new Intent(CommunityWriteActivity.this, GalleryActivity.class);
                    if (imgPathList.size() > 0) {
                        i.putExtra("imagePathList", imgPathList);
                    }
                    startActivityForResult(i, 1111);
                }
                break;
            default:
                break;
        }
    }

    //오드로이드 용
    private void check() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,}, 1234);
        }else{
            fileMakedirs();

            Intent i = new Intent(CommunityWriteActivity.this, GalleryActivity.class);
            if (imgPathList.size() > 0) {
                i.putExtra("imagePathList", imgPathList);
            }
            startActivityForResult(i, 1111);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_community_write_back: {
                onBackPressed();
                break;
            }
            case R.id.lin_community_write_picture: {
                if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//                        if (!Environment.isExternalStorageManager()){
//                            try {
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                                intent.addCategory("android.intent.category.DEFAULT");
//                                intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
//                                startActivityForResult(intent, 300);
//                            }catch (Exception e){
//                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                                startActivityForResult(intent, 300);
//                            }
//                        }else{
//                            TedPermission.with(this)
//                                    .setPermissionListener(permissionListener)
//                                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                    .check();
//                        }
//                    }else{
//                        TedPermission.with(this)
//                                .setPermissionListener(permissionListener)
//                                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
//                                .check();
//                    }
                    TedPermission.with(this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
                break;
            }
            case R.id.btn_community_write_finish: {
                btnFinish.setEnabled(false);

                if (beforeIntent != null) {
                    if (beforeIntent.hasExtra("communityNo")) {
                        if (mMenuNo.length() == 0){
                            new OneButtonDialog(this,"게시판 선택","게시판을 선택해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    btnFinish.setEnabled(true);
                                }
                            });
                        }else if (mCMenuNo.length() == 0){
                            new OneButtonDialog(this,"상세 게시판 선택","상세 게시판을 선택해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    btnFinish.setEnabled(true);
                                }
                            });
                        }else if (edtContents.getText().toString().trim().length() == 0){
                            new OneButtonDialog(this,"글쓰기","내용을 입력해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    btnFinish.setEnabled(true);
                                }
                            });
                        }else{
                            NetWorkCall(NEW_COMMUNITY_UPDATE);
                        }
                    } else {
                        if (mMenuNo.length() == 0){
                            new OneButtonDialog(this,"게시판 선택","게시판을 선택해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    btnFinish.setEnabled(true);
                                }
                            });
                        }else if (mCMenuNo.length() == 0){
                            new OneButtonDialog(this,"상세 게시판 선택","상세 게시판을 선택해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    btnFinish.setEnabled(true);
                                }
                            });
                        }else if (edtContents.getText().toString().trim().length() == 0){
                            new OneButtonDialog(this,"글쓰기","내용을 입력해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    btnFinish.setEnabled(true);
                                }
                            });
                        }else{
                            NetWorkCall(NEW_COMMUNITY_INSERT);
                        }
                    }
                } else {
                    if (mMenuNo.length() == 0){
                        new OneButtonDialog(this,"게시판 선택","게시판을 선택해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                btnFinish.setEnabled(true);
                            }
                        });
                    }else if (mCMenuNo.length() == 0){
                        new OneButtonDialog(this,"상세 게시판 선택","상세 게시판을 선택해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                btnFinish.setEnabled(true);
                            }
                        });
                    }else if (edtContents.getText().toString().trim().length() == 0){
                        new OneButtonDialog(this,"글쓰기","내용을 입력해주세요.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                btnFinish.setEnabled(true);
                            }
                        });
                    }else{
                        NetWorkCall(NEW_COMMUNITY_INSERT);
                    }
                }
                break;
            }
            case R.id.btn_community_write_add:{
                if (edtTag.getText().length() != 0) {
                    if (edtTag.getText().toString().contains("#")) {
                        if (sumTagList.contains(edtTag.getText().toString())){
                            linTagParent.removeViewAt(sumTagList.indexOf(edtTag.getText().toString()));
                            sumTagList.remove(edtTag.getText().toString());
                            tagSelectList.remove(edtTag.getText().toString());
                        }
                        if (!tagSelectList.contains(edtTag.getText().toString())){
                            UnSelectTagList(edtTag.getText().toString(), 1);
                            sumTagList.add(0,edtTag.getText().toString());
                        }
                    } else {
                        if (sumTagList.contains("#"+edtTag.getText().toString())){
                            linTagParent.removeViewAt(sumTagList.indexOf("#"+edtTag.getText().toString()));
                            sumTagList.remove("#"+edtTag.getText().toString());
                            tagSelectList.remove("#"+edtTag.getText().toString());
                        }
                        if (!tagSelectList.contains("#"+edtTag.getText().toString())){
                            UnSelectTagList("#" + edtTag.getText().toString(), 1);
                            sumTagList.add(0,"#"+edtTag.getText().toString());
                        }
                    }
                    edtTag.setText("");
                }
                break;
            }
            case R.id.txt_community_write_category : {
                CategoryDialog(1);
                break;
            }
            case R.id.txt_community_write_board : {
                if (mMenuNo.length() != 0){
                    CategoryDialog(2);
                }else{
                    new OneButtonDialog(this,"게시판 선택","게시판을 먼저 선택해주세요.","확인");
                }

                break;
            }
            case R.id.lin_category_select_01 : {
                mMenuNo = txtNewCategoryTitle01.getTag().toString();
                txtCategory.setText(txtNewCategoryTitle01.getText().toString());
                mBottomSheetCategoryDialog.dismiss();
                mCMenuNo = "";
                txtBoard.setText("");
                break;
            }
            case R.id.lin_category_select_02 : {
                mMenuNo = txtNewCategoryTitle02.getTag().toString();
                txtCategory.setText(txtNewCategoryTitle02.getText().toString());
                mBottomSheetCategoryDialog.dismiss();
                mCMenuNo = "";
                txtBoard.setText("");
                break;
            }
            case R.id.rdo_community_category_01 : {
                mMenuNo = txtNewCategoryTitle01.getTag().toString();
                txtCategory.setText(txtNewCategoryTitle01.getText().toString());
                mBottomSheetCategoryDialog.dismiss();
                mCMenuNo = "";
                txtBoard.setText("");
                break;
            }
            case R.id.rdo_community_category_02 : {
                mMenuNo = txtNewCategoryTitle02.getTag().toString();
                txtCategory.setText(txtNewCategoryTitle02.getText().toString());
                mBottomSheetCategoryDialog.dismiss();
                mCMenuNo = "";
                txtBoard.setText("");
                break;
            }
        }
    }
}
