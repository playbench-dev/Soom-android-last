package com.kmw.soom2.ex.Home;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kmw.soom2.Common.DateTimePicker.SingleDateAndTimePicker;
import com.kmw.soom2.Common.PopupDialog.ShowVideoDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.GalleryActivity;
import com.kmw.soom2.Communitys.Activitys.VideoActivity;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.MemoActivity;
import com.kmw.soom2.Home.HomeItem.VideoMenuItem;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

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
import static com.kmw.soom2.Common.Utils.formatHHMMSS;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD;
import static com.kmw.soom2.Common.Utils.formatYYYYMMDD2;

public class exMemoMoveActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "MemoActivity";
    TextView txtBack;
    ImageView imgRemove;
    TextView txtDate,txtTime;
    TextView txtPictureLength;
    EditText edtContents;
    LinearLayout linAttachmentParent,linAttachment;
    Button btnFinish;
    TextView txtContentsLength;

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
        setContentView(R.layout.activity_ex_memo);

        beforeIntent = getIntent();

        FindViewById();

        txtDate.setText(Utils.formatYYYYMMDD2.format(new Date(System.currentTimeMillis())));
        txtTime.setText(Utils.formatHHMM.format(new Date(System.currentTimeMillis())));

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_memo_record_back : {
                onBackPressed();
                break;
            }
            case R.id.img_memo_record_remove : {

                break;
            }
            case R.id.txt_memo_record_date : {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.txt_memo_record_time : {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.lin_memo_record_attachment : {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
            case R.id.btn_memo_record_finish : {
                Intent i = new Intent(this, NewAnotherLoginActivity.class);
                startActivity(i);
                break;
            }
        }
    }

}
