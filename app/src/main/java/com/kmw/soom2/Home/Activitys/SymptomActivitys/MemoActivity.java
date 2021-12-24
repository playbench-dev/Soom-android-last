package com.kmw.soom2.Home.Activitys.SymptomActivitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.kmw.soom2.Common.Crop;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.ShowVideoDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Communitys.Activitys.GalleryActivity;
import com.kmw.soom2.Communitys.Activitys.VideoActivity;
import com.kmw.soom2.Home.HomeItem.VideoMenuItem;
import com.kmw.soom2.MyPage.Activity.AccountActivity;
import com.kmw.soom2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.HOME_FEED_DELETE;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.IMAGE_DELETE_URL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.IMAGE_INSERT_URL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.IMAGE_UPDATE_URL;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEMO_FEED_INSERT;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEMO_FEED_UPDATE;
import static com.kmw.soom2.Common.Utils.CAMERA_PERMISSION;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.OneBtnPopup;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;
import static com.kmw.soom2.Common.Utils.formatHHMM;
import static com.kmw.soom2.Common.Utils.formatHHMM2;
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener, AsyncResponse {

    private String TAG = "MemoActivity";
    TextView txtBack;
    ImageView imgRemove;
    TextView txtDate,txtTime;
    TextView txtPictureLength;
    EditText edtContents;
    LinearLayout linAttachmentParent,linAttachment;
    Button btnFinish;
    TextView txtContentsLength;
    ScrollView mScrollView;

    Intent beforeIntent;

    String realUrl = "";
    String mWritingNo = "";
    String mRegisterDt = "";
    String mImagesNo = "";

    String[] realUrlList = new String[]{};
    ArrayList<String> testList = new ArrayList<>();
    ProgressDialog progressDialog;
    int selectFlag;

    private String mSaveBeforeRegisterDt = "";
    private String mSaveAfterRegisterDt = "";
    private String mSaveBeforeMemo = "";
    private String mSaveAfterMemo = "";
    private String mSaveBeforeUrl = "";
    private String mSaveAfterUrl = "";

    Vector<VideoMenuItem> menus = new Vector<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

        imgRemove.setVisibility(View.GONE);

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("registerDt")){
                try {
                    txtDate.setText(formatYYYYMMDD2.format(formatYYYYMMDD.parse(beforeIntent.getStringExtra("registerDt").substring(0,10))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    txtTime.setText(Utils.formatHHMM.format(Utils.formatHHMMSS.parse(beforeIntent.getStringExtra("registerDt").substring(11,18))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                edtContents.setText(beforeIntent.getStringExtra("memo"));
                mSaveBeforeMemo = beforeIntent.getStringExtra("memo");
                mSaveAfterMemo = beforeIntent.getStringExtra("memo");
                realUrl = beforeIntent.getStringExtra("imgsPath");
                mSaveBeforeUrl = realUrl;
                mSaveAfterUrl = realUrl;
                if (beforeIntent.hasExtra("imagesNo")){
                    mImagesNo = beforeIntent.getStringExtra("imagesNo");
                    Log.i(TAG,"imagesNo : " + mImagesNo);
                }

                if (beforeIntent.getStringExtra("imgsPath").length() > 0){
                    if (realUrl.contains("mp4")){
                        txtPictureLength.setText("1/5");
                        check1();
                    }else{
                        realUrlList = realUrl.split(",");
                        testList = new ArrayList<>(Arrays.asList(realUrlList));
                        txtPictureLength.setText(""+testList.size()+"/5");
                        for (int i = 0; i < testList.size(); i++){
                            selectImg(""+i,testList.get(i),null);
                        }
                    }
                }

                imgRemove.setVisibility(View.VISIBLE);

            }else if (beforeIntent.hasExtra("date")){
                txtDate.setText(beforeIntent.getStringExtra("date"));
                txtTime.setText(beforeIntent.getStringExtra("time"));
            }else{
                txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
                txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
            }
        }else{
            txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
            txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));
        }

        try {
            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
            mSaveBeforeRegisterDt = mRegisterDt;
            mSaveAfterRegisterDt = mRegisterDt;
        } catch (ParseException e) {
            mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
            mSaveBeforeRegisterDt = mRegisterDt;
            mSaveAfterRegisterDt = mRegisterDt;
            e.printStackTrace();
        }

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_memo_record_back);
        imgRemove = (ImageView)findViewById(R.id.img_memo_record_remove);
        txtDate = (TextView)findViewById(R.id.txt_memo_record_date);
        txtTime = (TextView)findViewById(R.id.txt_memo_record_time);
        edtContents = (EditText)findViewById(R.id.edt_memo_record_contents);
        linAttachmentParent = (LinearLayout)findViewById(R.id.lin_memo_record_attachment_parent);
        linAttachment = (LinearLayout)findViewById(R.id.lin_memo_record_attachment);
        btnFinish = (Button)findViewById(R.id.btn_memo_record_finish);
        txtContentsLength = (TextView)findViewById(R.id.txt_memo_record_contents_length);
        txtPictureLength = (TextView)findViewById(R.id.txt_memo_record_picture_length);
        mScrollView = (ScrollView)findViewById(R.id.scroll_view_activity_memo_record);

        txtBack.setOnClickListener(this);
        imgRemove.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);
        linAttachment.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        txtDate.setOnClickListener(this);
        txtTime.setOnClickListener(this);

        edtContents.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txtContentsLength.setText("("+s.length()+"/2000)");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtContents.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (edtContents.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            }
        });

        setHideKeyboard(this,mScrollView);
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

    void popup(){
        final Dialog dialog = new BottomSheetDialog(MemoActivity.this, R.style.SheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_picture_video_layout,null);

        TextView txtPicture = (TextView)contentView.findViewById(R.id.txt_dialog_picture_video_picture);
        TextView txtVideo = (TextView)contentView.findViewById(R.id.txt_dialog_picture_video_video);
        TextView txtCancel = (TextView)contentView.findViewById(R.id.txt_dialog_picture_video_cancel);

        dialog.setContentView(contentView);

        txtPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectFlag = 1;

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
//                            TedPermission.with(MemoActivity.this)
//                                    .setPermissionListener(permissionListener)
//                                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                    .check();
//                        }
//                    }else{
//                        TedPermission.with(MemoActivity.this)
//                                .setPermissionListener(permissionListener)
//                                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                .check();
//                    }
                    TedPermission.with(MemoActivity.this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
            }
        });

        txtVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                selectFlag = 2;

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
//                            TedPermission.with(MemoActivity.this)
//                                    .setPermissionListener(permissionListener)
//                                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                    .check();
//                        }
//                    }else{
//                        TedPermission.with(MemoActivity.this)
//                                .setPermissionListener(permissionListener)
//                                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                .check();
//                    }
                    TedPermission.with(MemoActivity.this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
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

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {

            fileMakedirs();

            if (selectFlag == 1){
                Intent i = new Intent(MemoActivity.this, GalleryActivity.class);
                startActivityForResult(i,1111);
            }else if (selectFlag == 2){
                Intent i = new Intent(MemoActivity.this, VideoActivity.class);
                startActivityForResult(i,2222);
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")){
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
                Toast.makeText(MemoActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                WRITE_PERMISSION = true;
                READ_PERMISSION = true;
                fileMakedirs();
            }

            if (deniedPermissions.contains("android.permission.CAMERA") ){
                CAMERA_PERMISSION = false;
                Toast.makeText(MemoActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                CAMERA_PERMISSION = true;
            }
        }
    };

    void fileMakedirs(){
        String str = Environment.getExternalStorageState();
        if (str.equals(Environment.MEDIA_MOUNTED)) {

            String dirPath = getExternalFilesDir(null).getAbsolutePath();
            File file = new File(dirPath);
            if (!file.exists()) { // 원하는 경로에 폴더가 있는지 확인
                file.mkdirs();
            }
        } else {
            Toast.makeText(MemoActivity.this, "SD Card 인식 실패", Toast.LENGTH_SHORT).show();
        }
    }

    void selectImg(final String idx, final String imagePath, final Bitmap bitmap){
        View listView = new View(this);
        listView = getLayoutInflater().inflate(R.layout.view_select_gallery_list_item,null);

        ImageView img = (ImageView)listView.findViewById(R.id.img_select_gallery_list_item);
        ImageView imgRemove = (ImageView)listView.findViewById(R.id.img_select_gallery_list_item_remove);

//        Glide.with(this).load(file).into(img);

        if (imagePath.length() != 0){
            String replaceText = imagePath;
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(this)
                    .load("https:"+replaceText)
                    .apply(requestOptions)
                    .into(img);
        }else if (bitmap != null){
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(this)
                    .load(bitmap)
                    .apply(requestOptions)
                    .into(img);
        }

        final View finalListView = listView;

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePath.length() != 0){
                    imgDetail(imagePath);
                }else if (bitmap != null){
                    ShowVideoDialog dialog = new ShowVideoDialog(MemoActivity.this, Uri.parse(realUrl));
                    dialog.show();
                }
            }
        });

        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linAttachmentParent.removeView(finalListView);

                if (imagePath.length() != 0){
                    testList.remove(imagePath);
                    txtPictureLength.setText(""+testList.size()+"/5");
                    if (testList.size() == 0){
                        realUrl = "";
                    }else{
                        for (int i = 0; i < testList.size(); i++){
                            if (i == 0){
                                realUrl = testList.get(i);
                            }else{
                                realUrl += "," + testList.get(i);
                            }
                        }
                    }
                }else if (bitmap != null){
                    realUrl = "";
                }
                mSaveAfterUrl = realUrl;
            }
        });
        linAttachmentParent.addView(listView);
    }

    void imgDetail(String imgPath){
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.view_img_detail,null);

        ImageView img = (ImageView)contentView.findViewById(R.id.img_detail);
        TextView txtClose = (TextView)contentView.findViewById(R.id.txt_img_detail);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int height = dm.heightPixels;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.addContentView(contentView,params);

        String replaceText = imgPath;
        if (replaceText.contains("soom2.testserver-1.com:8080")){
            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
        }else if (replaceText.contains("103.55.190.193")){
            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
        }
        Glide.with(this).load("https:"+replaceText).into(img);

        dialog.show();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public Bitmap rotate(Bitmap bitmap, int degrees)
    {
        if(degrees != 0 && bitmap != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);

            try
            {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if(bitmap != converted)
                {
                    bitmap.recycle();
                    bitmap = converted;
                }
            }
            catch(OutOfMemoryError ex)
            {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap;
    }

    private Vector<VideoMenuItem> getVideo() {
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//        String orderBy = MediaStore.Video.Media.DATE_TAKEN;
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);

        menus = new Vector<>();

        assert cursor != null;
        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Video.Thumbnails.MINI_KIND, null);

            // 썸네일 크기 변경할 때.
            //Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, width, height);
            String data = cursor.getString(2);
            menus.add(new VideoMenuItem(title, bitmap, Uri.parse(data),id));
        }

        cursor.close();
        return menus;
    }

    void NetworkCall(String mCode){
        if (mCode.equals(IMAGE_INSERT_URL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mWritingNo , "30", realUrl);
        }else if (mCode.equals(IMAGE_UPDATE_URL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mWritingNo , "30", realUrl);
        }else if (mCode.equals(MEMO_FEED_INSERT)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+ Utils.userItem.getUserNo(),edtContents.getText().toString(),Utils.userItem.getNickname(),""+Utils.userItem.getGender(),""+Utils.userItem.getBirth(),mRegisterDt);
        }else if (mCode.equals(MEMO_FEED_UPDATE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo") , edtContents.getText().toString(), mRegisterDt);
        }else if (mCode.equals(HOME_FEED_DELETE)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(beforeIntent.getStringExtra("medicineHistoryNo"));
        }else if (mCode.equals(IMAGE_DELETE_URL)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(mImagesNo);
        }
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        Log.i(TAG,"mResult : " + mResult);
        if (mResult != null){
            if (mCode.equals(IMAGE_INSERT_URL)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                },500);
            }else if (mCode.equals(IMAGE_UPDATE_URL)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        btnFinish.setClickable(true);
                        finish();
                    }
                },500);
            }else if (mCode.equals(MEMO_FEED_INSERT)){
                if (realUrl.length() == 0){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(RESULT_OK);
                            btnFinish.setClickable(true);
                            finish();
                        }
                    },500);
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(mResult);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            mWritingNo = String.valueOf(object.get("USER_HISTORY_NO"));
                            Log.i(TAG,"mWritingNo : " + mWritingNo);
                            NetworkCall(IMAGE_INSERT_URL);
                        }
                    }catch (JSONException e){

                    }
                }
            }else if (mCode.equals(MEMO_FEED_UPDATE)){
                Log.i(TAG,"realUrl : " + realUrl);
                if (realUrl.length() == 0){
                    NetworkCall(IMAGE_DELETE_URL);
                }else{
                    if (beforeIntent.getStringExtra("imgsPath").length() == 0){
                        mWritingNo = beforeIntent.getStringExtra("medicineHistoryNo");
                        NetworkCall(IMAGE_INSERT_URL);
                    }else{
                        mWritingNo = beforeIntent.getStringExtra("medicineHistoryNo");
                        NetworkCall(IMAGE_UPDATE_URL);
                    }
                }
            }else if (mCode.equals(HOME_FEED_DELETE)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        imgRemove.setClickable(true);
                        finish();
                    }
                },500);
            }else if (mCode.equals(IMAGE_DELETE_URL)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        btnFinish.setClickable(true);
                        finish();
                    }
                },500);
            }
        }
    }

    class VideoSearch extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MemoActivity.this,R.style.MyTheme);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "0";

            int size = getVideo().size();

            for (int i = 0; i < size; i++){
                if (menus.get(i).getUri().equals(Uri.parse(realUrl))){
                    result = ""+i;

                    break;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            selectImg(""+s,"",menus.get(Integer.parseInt(s)).getImg());
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1111){
            if (resultCode == RESULT_OK){

                linAttachmentParent.removeAllViews();

                realUrl = data.getStringExtra("realUrl");
                mSaveAfterUrl = realUrl;
                Log.i(TAG,"real url : " + realUrl);
                realUrlList = realUrl.split(",");
                testList = new ArrayList<>(Arrays.asList(realUrlList));
                txtPictureLength.setText(""+testList.size()+"/5");
                for (int i = 0; i < testList.size(); i++){
                    selectImg(""+i,testList.get(i),null);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnFinish.setEnabled(true);
                    }
                },1000);
            }
        }else if (requestCode == 2222){
            if (resultCode == RESULT_OK){
                linAttachmentParent.removeAllViews();
                realUrl = data.getStringExtra("videoPath");
                mSaveAfterUrl = realUrl;
                txtPictureLength.setText("1/5");
                selectImg("","",(Bitmap)data.getParcelableExtra("videoThumbnail"));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnFinish.setEnabled(true);
                    }
                },1000);
            }

        }
    }
    TwoButtonDialog twoButtonDialog;

    @Override
    public void onBackPressed() {

        if (mSaveBeforeRegisterDt.equals(mSaveAfterRegisterDt) && mSaveBeforeMemo.equals(edtContents.getText().toString()) && mSaveBeforeUrl.equals(realUrl)){
            finish();
        }else{
            twoButtonDialog = new TwoButtonDialog(MemoActivity.this,"메모", "변경사항이 저장되지 않았습니다.\n정말 나가시겠습니까?","취소", "확인");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_memo_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_memo_record_remove : {
                twoButtonDialog = new TwoButtonDialog(this,"메모","메모를 삭제하시겠습니까?","취소","확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        NetworkCall(HOME_FEED_DELETE);
                    }
                });
                break;
            }
            case R.id.txt_memo_record_date : {
                DateTimePicker(1, "날짜를 선택하세요.");
                break;
            }
            case R.id.txt_memo_record_time : {
                DateTimePicker("시간을 선택하세요.");
                break;
            }
            case R.id.lin_memo_record_attachment : {
                popup();
                break;
            }
            case R.id.btn_memo_record_finish : {
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("registerDt")){
                        try {
                            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                        } catch (ParseException e) {
                            mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
                            e.printStackTrace();
                        }
                        if (edtContents.getText().length() != 0 || realUrl.length() != 0){
                            NetworkCall(MEMO_FEED_UPDATE);
                        }else{
                            btnFinish.setEnabled(true);
                            OneBtnPopup(this,"메모","내용을 입력해주세요.","확인");
                        }
                    }else{
                        try {
                            mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                        } catch (ParseException e) {
                            mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
                            e.printStackTrace();
                        }
                        if (edtContents.getText().length() != 0 || realUrl.length() != 0){
                            NetworkCall(MEMO_FEED_INSERT);
                        }else{
                            Log.i(TAG,"edt : " + edtContents.getText().toString() + " real : " + realUrl);
                            btnFinish.setEnabled(true);
                            OneBtnPopup(this,"메모","내용을 입력해주세요.","확인");
                        }
                    }
                }else{
                    try {
                        mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                    } catch (ParseException e) {
                        mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
                        e.printStackTrace();
                    }
                    if (edtContents.getText().length() != 0 || realUrl.length() != 0){
                        NetworkCall(MEMO_FEED_INSERT);
                    }else{
                        Log.i(TAG,"edt : " + edtContents.getText().toString() + " real : " + realUrl);
                        btnFinish.setEnabled(true);
                        OneBtnPopup(this,"메모","내용을 입력해주세요.","확인");
                    }
                }
                break;
            }
        }
    }
    void DateTimePicker(final int flag, String title){
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateDialog = new Dialog(MemoActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateDialog.setContentView(dateTimeView);
        dateDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(true);
        singleDateAndTimePicker.setDisplayMonths(true);
        singleDateAndTimePicker.setDisplayDaysOfMonth(true);
        singleDateAndTimePicker.setDisplayHours(false);
        singleDateAndTimePicker.setDisplayMinutes(false,"");
        singleDateAndTimePicker.setMustBeOnFuture(false);
        Date date = null;

        try {
            date = formatYYYYMMDD2.parse(txtDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                txtDate.setText(formatYYYYMMDD2.format(date));
                try {
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                    mSaveAfterRegisterDt = mRegisterDt;
                } catch (ParseException e) {
                    mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
                    mSaveAfterRegisterDt = mRegisterDt;
                    e.printStackTrace();
                }
                dateDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateDialog.dismiss();
            }
        });
    }
    public void TwoBtnPopup(Context context, String title, String contents, String btnLeftText, String btnRightText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button)layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button)layout.findViewById(R.id.btn_two_btn_popup_right);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateTimeDialog.setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
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
                NetworkCall(HOME_FEED_DELETE);
                dateTimeDialog.dismiss();
            }
        });
    }

    void DateTimePicker(String title) {
        View dateTimeView = getLayoutInflater().inflate(R.layout.pop_up_date_time_picker,null);
        final Dialog dateTimeDialog = new Dialog(MemoActivity.this);
        TextView txtTitle = (TextView)dateTimeView.findViewById(R.id.txt_calendar_picker_title);

        txtTitle.setText(title);

        dateTimeDialog.setContentView(dateTimeView);
        dateTimeDialog.show();

        final SingleDateAndTimePicker singleDateAndTimePicker = (SingleDateAndTimePicker)dateTimeView.findViewById(R.id.single_day_picker);

        singleDateAndTimePicker.setDisplayYears(false);
        singleDateAndTimePicker.setDisplayMonths(false);
        singleDateAndTimePicker.setDisplayDaysOfMonth(false);
        singleDateAndTimePicker.setDisplayHours(true);
        singleDateAndTimePicker.setDisplayMinutes(true,txtDate.getText().toString());

        Date date = null;

        try {
            date = Utils.formatHHMM.parse(txtTime.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        singleDateAndTimePicker.selectDate(calendar);

        singleDateAndTimePicker.clickDateChange(new SingleDateAndTimePicker.OnCustomClick() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                txtTime.setText(Utils.formatHHMM.format(date));
                try {
                    mRegisterDt = formatYYYYMMDD.format(formatYYYYMMDD2.parse(txtDate.getText().toString())) + " " + formatHHMM2.format(formatHHMM.parse(txtTime.getText().toString())) + ":" + formatSS.format(new Date(System.currentTimeMillis()));
                    mSaveAfterRegisterDt = mRegisterDt;
                } catch (ParseException e) {
                    mRegisterDt = Utils.formatYYYYMMDDHHMMSS.format(new Date(System.currentTimeMillis()));
                    mSaveAfterRegisterDt = mRegisterDt;
                    e.printStackTrace();
                }
                dateTimeDialog.dismiss();
            }
        });

        singleDateAndTimePicker.clickCancelDialog(new SingleDateAndTimePicker.OnCancelClick() {
            @Override
            public void onDialogCancel() {
                dateTimeDialog.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1234:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    check();
                } else {
                    fileMakedirs();

                    if (selectFlag == 1){
                        Intent i = new Intent(MemoActivity.this, GalleryActivity.class);
                        startActivityForResult(i,1111);
                    }else if (selectFlag == 2){
                        Intent i = new Intent(MemoActivity.this, VideoActivity.class);
                        startActivityForResult(i,2222);
                    }
                }
                break;
            case 2345:
                new VideoSearch().execute();
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

            if (selectFlag == 1){
                Intent i = new Intent(MemoActivity.this, GalleryActivity.class);
                startActivityForResult(i,1111);
            }else if (selectFlag == 2){
                Intent i = new Intent(MemoActivity.this, VideoActivity.class);
                startActivityForResult(i,2222);
            }
        }
    }

    private void check1() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,}, 2345);
        }else{
            fileMakedirs();
            new VideoSearch().execute();
        }
    }
}
