package com.kmw.soom2.MyPage.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kakao.sdk.user.UserApiClient;
import com.kmw.soom2.BuildConfig;
import com.kmw.soom2.Common.BackPressEditText;
import com.kmw.soom2.Common.Crop;
import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.PopupDialog.TwoButtonDialog;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Activitys.CommunityWriteActivity;
import com.kmw.soom2.Communitys.Activitys.GalleryActivity;
import com.kmw.soom2.ImageCropTestActivity;
import com.kmw.soom2.Login.Activitys.LoginSignUpSelectActivity;
import com.kmw.soom2.Login.Item.ItemClass;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.MyPage.Dialog.LogoutDialog;
import com.kmw.soom2.MyPage.Dialog.SecessionDialog;
import com.kmw.soom2.R;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nhn.android.naverlogin.OAuthLogin;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.Crop.REQUEST_CROP;
import static com.kmw.soom2.Common.Crop.REQUEST_PICK;
import static com.kmw.soom2.Common.Crop.REQUEST_PICTURE_PICK;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.EMAIL_OVERLAP;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.NICK_NAME_OVERLAP;
import static com.kmw.soom2.Common.Utils.CAMERA_PERMISSION;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.READ_PERMISSION;
import static com.kmw.soom2.Common.Utils.WRITE_PERMISSION;
import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    final String TAG = "AccountActivity";
    EditText passEditText,phoneNumberEditText, nameEditView;
    TextView nickNameCountTextView,accountTextView,secessionTextView, txtSave,txtNicknameCheck, txtEmailCheck;
    BackPressEditText nickNameEditText;
    Button logoutButton;
    LogoutDialog logoutDialog;
    SecessionDialog secessionDialog;
    TextView txtPwCheckStauts;

    ImageView profileImageView, cameraImageView;
    ImageView btnBack;
    ImageView imgSingType;
    ScrollView scrollView;
    ImageView mImagePwShow;

    ItemClass itemClass = new ItemClass();

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout passcontainer;

    String realImageUrl;

    UserItem userItem;
    String response;

    Bitmap bitmap = null;
    Uri selectedImageUri;
    TwoButtonDialog twoButtonDialog;
    String realUrl;
    private static Context mContext;
    public static OAuthLogin mOAuthLoginInstance;

    boolean enableNickname = true;
    boolean enableEmail = true;
    boolean enablePw = true;

    private int pictureSelect = 1;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private Uri mImageCaptureUri;
    boolean mShowPwFlag = false;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mContext = this;

        preferences = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = preferences.edit();

        txtSave = findViewById(R.id.save_text_view_activity_account);
        btnBack = findViewById(R.id.back_img_view_activity_account);

        profileImageView = findViewById(R.id.account_imageview_profile);
        cameraImageView = findViewById(R.id.account_imageview_profile_camera);

        scrollView = (ScrollView)findViewById(R.id.scr_account_parent);
        phoneNumberEditText = (EditText) findViewById(R.id.phone_num_edit_text_activity_account);
        nickNameEditText = (BackPressEditText) findViewById(R.id.nickname_edit_activity_account);
        passEditText = (EditText) findViewById(R.id.pass_edit_text_activity_account);
//        passEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        nickNameCountTextView = (TextView) findViewById(R.id.edit_text_count_text_view_activity_account);
        secessionTextView = (TextView) findViewById(R.id.here_text_view_activity_account);
        nameEditView = (EditText) findViewById(R.id.name_edit_view_activity_account);
        imgSingType = findViewById(R.id.img_account_sign_type);
        logoutButton = (Button) findViewById(R.id.log_out_button_activity_account);
        passcontainer = (LinearLayout) findViewById(R.id.pass_container_activity_account);
        txtNicknameCheck = (TextView)findViewById(R.id.txt_account_nickname_check);
        txtEmailCheck = (TextView)findViewById(R.id.txt_account_email_check);
        mImagePwShow = findViewById(R.id.img_pw_show);
        txtPwCheckStauts = findViewById(R.id.txt_pw_check_account);


        logoutButton.setOnClickListener(this);
        secessionTextView.setOnClickListener(this);
        txtSave.setOnClickListener(this);
        profileImageView.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        scrollView.setOnClickListener(this);

        mImagePwShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowPwFlag){
                    passEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mImagePwShow.setImageResource(R.drawable.ic_pw_off);
                    mShowPwFlag = false;
                    passEditText.setSelection(passEditText.getText().length());
                }else{
                    passEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                    mImagePwShow.setImageResource(R.drawable.ic_pw_on);
                    mShowPwFlag = true;
                    passEditText.setSelection(passEditText.getText().length());
                }
            }
        });

        passEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                mPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View v, int keyCode, KeyEvent event) {
//                        if (mPasswordEditText.length() > 0) {
//                            if (keyCode == KeyEvent.KEYCODE_DEL) {
//                                mPasswordEditText.setText("");
//                            }
//                        }
//                        return false;
//                    }
//                });
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enablePw = false;
                if (passEditText.getText().toString().length() > 0 && itemClass.checkPass(passEditText.getText().toString())) {
                    txtPwCheckStauts.setText("사용가능한 비밀번호입니다.");
                    txtPwCheckStauts.setTextColor(Color.parseColor("#08d181"));
                    txtPwCheckStauts.setVisibility(View.VISIBLE);
                    enablePw = true;
                } else if (passEditText.getText().toString().length() > 0 && !itemClass.checkPass(passEditText.getText().toString())) {
                    txtPwCheckStauts.setText("비밀번호 형식이 올바르지 않습니다.");
                    txtPwCheckStauts.setTextColor(Color.parseColor("#DB7676"));
                    txtPwCheckStauts.setVisibility(View.VISIBLE);
                    enablePw = false;
                } else if (passEditText.getText().toString().length() == 0) {
                    txtPwCheckStauts.setVisibility(View.INVISIBLE);
                    enablePw = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NullCheck(this);

        userItem = Utils.userItem;
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));

        if (userItem.getProfileImg().length() != 0) {
            String replaceText = userItem.getProfileImg();
            if (replaceText.contains("soom2.testserver-1.com:8080")){
                replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
            }else if (replaceText.contains("103.55.190.193")){
                replaceText = replaceText.replace("103.55.190.193","soomcare.info");
            }
            if (userItem.getProfileImg().contains("https:")) {
                Glide.with(this).asBitmap().load(replaceText).apply(requestOptions).into(profileImageView);
            }else {
                Glide.with(this).asBitmap().load("https:"+ replaceText).apply(requestOptions).into(profileImageView);
            }
//            cameraImageView.setVisibility(View.INVISIBLE);
        }else{
            int resource = R.drawable.ic_no_profile;
            if (userItem.getGender() == 1){
                resource = R.drawable.ic_no_profile_m;
            }else if (userItem.getGender() == 2){
                resource = R.drawable.ic_no_profile_w;
            }
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(40));
            Glide.with(this)
                    .load(resource)
                    .apply(requestOptions)
                    .into(profileImageView);
        }

        if (userItem.getLoginType() != 1) {
            passcontainer.setVisibility(View.GONE);
            if (userItem.getLoginType() == 2) {
                imgSingType.setImageResource(R.drawable.ic_naver_type);
            }else {
                imgSingType.setImageResource(R.drawable.ic_kakao_type);
            }
            nameEditView.setEnabled(true);
        } else {
            passEditText.setText(userItem.getPassword());
            imgSingType.setImageResource(R.drawable.ic_email_type);
            nameEditView.setEnabled(false);
        }

//        String email = preferences.getString("email", "");
//        String nickName = preferences.getString("nickname","");

        nameEditView.setText(userItem.getEmail());
        nickNameEditText.setText(userItem.getNickname());
        nickNameCountTextView.setText("" + nickNameEditText.length());

        phoneNumberEditText.setText((userItem.getPhone() != null) ? userItem.getPhone() : "");

        nameEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!itemClass.checkEmail(nameEditView.getText().toString())){
                    txtEmailCheck.setVisibility(View.VISIBLE);
                    txtEmailCheck.setTextColor(Color.parseColor("#DB7676"));
                    txtEmailCheck.setText("이메일 형식이 올바르지 않습니다.");
                    enableEmail = false;
                }else{
                    txtEmailCheck.setVisibility(View.INVISIBLE);
                }

                if (!userItem.getEmail().equals(s.toString())){
                    enableEmail = false;
                }else{
                    txtEmailCheck.setVisibility(View.INVISIBLE);
                    enableEmail = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        logoutDialog = new LogoutDialog(this, new LogoutDialog.LogoutButtonListener() {
            @Override
            public void logoutButton(boolean data) {
                new LogoutProcessNetwork().execute();
            }
        });

        secessionDialog = new SecessionDialog(this, new SecessionDialog.SecessionButtonListener() {
            @Override
            public void secessionButton(boolean data) {
                new DeleteUserInfoNetwork().execute();
            }
        });

        nickNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableNickname = false;
                if (s.length() < 2){
                    txtNicknameCheck.setVisibility(View.VISIBLE);
                    txtNicknameCheck.setTextColor(Color.parseColor("#DB7676"));
                    txtNicknameCheck.setText("닉네임은 두글자 이상이여야 합니다.");
                    enableNickname = false;
                }else{
                    txtNicknameCheck.setVisibility(View.INVISIBLE);
                }

                if (!userItem.getNickname().equals(s.toString())){
                    enableNickname = false;
                }else{
                    txtNicknameCheck.setVisibility(View.INVISIBLE);
                    enableNickname = true;
                }

                nickNameCountTextView.setText("" + nickNameEditText.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nickNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (userItem.getNickname().equals(nickNameEditText.getText().toString())){
                        txtNicknameCheck.setVisibility(View.INVISIBLE);
                        enableNickname = true;
                    }else{
                        if (nickNameEditText.getText().length() < 2){
                            txtNicknameCheck.setVisibility(View.VISIBLE);
                            txtNicknameCheck.setTextColor(Color.parseColor("#DB7676"));
                            txtNicknameCheck.setText("닉네임은 두글자 이상이여야 합니다.");
                            enableNickname = false;
                        }else{
                            if (userItem.getNickname().equals(nickNameEditText.getText().toString())){
                                txtNicknameCheck.setVisibility(View.INVISIBLE);
                                enableNickname = true;
                            }else{
                                new OverlapUserNickNameNetwork(0).execute(nickNameEditText.getText().toString());
                            }

                        }
                    }
                }
            }
        });

        nameEditView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (userItem.getEmail().equals(nameEditView.getText().toString())){
                        txtEmailCheck.setVisibility(View.INVISIBLE);
                        enableEmail = true;
                    }else{
                        new OverlapEmailNetwork(0).execute(nameEditView.getText().toString());
                    }
                }
            }
        });

        phoneNumberEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        nickNameEditText.setOnBackPressListener(onBackPressListener);

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            Intent intent = result.getData();
                            int CallType = intent.getIntExtra("CallType", 0);
                            if(CallType == 0){
                                //실행될 코드
                            }else if(CallType == 1){
                                //실행될 코드
                            }else if(CallType == 2){
                                //실행될 코드
                            }
                        }
                    }
                });
    }

    private void doTakePhotoAction()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //사진을 찍기 위하여 설정합니다.
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException e) {
            Toast.makeText(AccountActivity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();              finish();
        }
        if (photoFile != null) {
            mImageCaptureUri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri); //사진을 찍어 해당 Content uri를 photoUri에 적용시키기 위함
            startActivityForResult(intent, PICK_FROM_CAMERA);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(getExternalFilesDir(null).getAbsolutePath()); //test라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private File createCacheImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "IP" + timeStamp + "_";
        File storageDir = new File(getExternalCacheDir().getAbsolutePath()); //test라는 경로에 이미지를 저장하기 위함
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void doTakeAlbumAction()
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
//        intent.putExtra("aspectX", 4);
//        intent.putExtra("aspectY", 4);
//        intent.putExtra("crop",true);
//        intent.putExtra("scale", true);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private void didBackPressOnEditText()
    {
        if(nickNameEditText.getText().toString().length() >= 2){
            if (!userItem.getNickname().equals(nickNameEditText.getText().toString())){
                new OverlapUserNickNameNetwork(0).execute(nickNameEditText.getText().toString());
            }
        }

        nickNameEditText.clearFocus();
    }

    private BackPressEditText.OnBackPressListener onBackPressListener = new BackPressEditText.OnBackPressListener()
    {
        @Override
        public void onBackPress()
        {
            didBackPressOnEditText();
        }
    };

    public NetworkInfo getNetworkState() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences = getSharedPreferences("preferences",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        switch (v.getId()){
            case R.id.log_out_button_activity_account:
//                logoutDialog.show();
                logoutButton.setClickable(false);

//                logoutDialog.show();
                    new LogoutProcessNetwork().execute();
                break;
            case R.id.here_text_view_activity_account:
                secessionTextView.setClickable(false);
                twoButtonDialog = new TwoButtonDialog(AccountActivity.this,"회원탈퇴", "정말 탈퇴하실 건가요? \n(탈퇴하시면 현재 이메일 주소로 재가입은 불가능해요!)","취소", "확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        if(getNetworkState() != null && getNetworkState().isConnected()) {
                            if(editor != null) {
                                editor.clear();
                                editor.apply();
                            }
                            new DeleteUserInfoNetwork().execute();
                        }
                        Intent i = new Intent(AccountActivity.this, LoginSignUpSelectActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        logoutButton.setClickable(true);
                        secessionTextView.setClickable(true);
                        finish();
                    }
                });

                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        logoutButton.setClickable(true);
                        secessionTextView.setClickable(true);
                    }
                });

                break;
            case R.id.save_text_view_activity_account:
//                txtSave.setClickable(false);
                Log.i(TAG,"aaaaaa");
                if (enableNickname && enableEmail && enablePw){
                    Log.i(TAG,"date0 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                    new UpdateUserInfoNetwork().execute();
                }else if (enableEmail){
                    if (!nickNameEditText.getText().toString().equals(userItem.getNickname())){
                        new OverlapUserNickNameNetwork(1).execute(nickNameEditText.getText().toString());
                    }
                }else if (enableNickname){
                    if (!nameEditView.getText().toString().equals(userItem.getEmail())){
                        new OverlapEmailNetwork(1).execute(nameEditView.getText().toString());
                    }
                }else{
                    //popup
                }
                break;
            case R.id.account_imageview_profile:
                BottomSheet();
                break;
            case R.id.back_img_view_activity_account:
                onBackPressed();
                break;

            case R.id.scr_account_parent : {
                scrollView.setFocusableInTouchMode(true);
                scrollView.requestFocus();
                break;
            }
        }
    }

    PermissionListener permissionListener = new PermissionListener() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionGranted() {
            if (pictureSelect == 1){
//                sendTakePhotoIntent();
                doTakePhotoAction();
            }else{
//                Crop.pickImage(AccountActivity.this);
                doTakeAlbumAction();
            }
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            if (deniedPermissions.contains("android.permission.READ_EXTERNAL_STORAGE") || deniedPermissions.contains("android.permission.WRITE_EXTERNAL_STORAGE")){
                WRITE_PERMISSION = false;
                READ_PERMISSION = false;
                Toast.makeText(AccountActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                WRITE_PERMISSION = true;
                READ_PERMISSION = true;
            }

            if (deniedPermissions.contains("android.permission.CAMERA") ){
                CAMERA_PERMISSION = false;
                Toast.makeText(AccountActivity.this, "권한을 거부하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                CAMERA_PERMISSION = true;
            }
        }
    };

    public void OneBtnPopup(Context context, String title, String contents, String btnText){

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.one_btn_popup,null);

        TextView txtTitle = (TextView)layout.findViewById(R.id.txt_one_btn_popup_title);
        TextView txtContents = (TextView)layout.findViewById(R.id.txt_one_btn_popup_contents);
        Button btnOk = (Button)layout.findViewById(R.id.btn_one_btn_popup_ok);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnOk.setText(btnText);
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

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
                txtSave.setClickable(true);
                finish();
            }
        });

    }
    public class UpdateUserInfoNetwork extends AsyncTask<String, String, String> {
        int type = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder());
            Log.i(TAG,"date1 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
            if (profileImageView.getDrawable() == null){
                type = 0;
                body.add("PROFILE_IMG", "");
            }else if (realUrl == null){
                type = 1;
                body.add("PROFILE_IMG", userItem.getProfileImg());
            }else{
                type = 2;
                body.add("PROFILE_IMG", realUrl);
            }

            if (Utils.userItem.getLoginType() == 1) {
                body.add("PASSWORD",passEditText.getText().toString());
            }else if (Utils.userItem.getLoginType() != 1){
                body.add("EMAIL",nameEditView.getText().toString());
            }
            if (phoneNumberEditText.getText().toString() != "") {
                body.add("PHONE", phoneNumberEditText.getText().toString());
            }
            body.add("NICKNAME", nickNameEditText.getText().toString());
            body.add("USER_NO", String.valueOf(userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.updateUserInfo(), body.build());
                Log.i(TAG,"date2 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                return response;
            } catch (IOException e) {
                txtSave.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 가입 실패
                    txtSave.setClickable(true);
                }else {
                    new GetReloadUserDataNetwork().execute();
                }
            }catch (JSONException e){
                txtSave.setClickable(true);
            }
        }
    }
    public class GetReloadUserDataNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;
            Log.i(TAG,"date3 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
            body = (new FormBody.Builder())
                    .add("ID", userItem.getId());

            if (userItem.getLoginType() == 1) {
                body.add("PASSWORD", passEditText.getText().toString());
            }
            body.add("DEVICE_CODE", Utils.TOKEN);
            body.add("OS_TYPE", "1");

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.loginProcess(), body.build());
                Log.i(TAG,"date4 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
                return response;
            } catch (IOException e) {
                txtSave.setClickable(true);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("list");

                if (jsonArray.length() > 0) {

                    JSONObject object = jsonArray.getJSONObject(0);
                    if (JsonIntIsNullCheck(object, "ALIVE_FLAG") == 1) {
                        UserItem userItem1 = new UserItem();

                        userItem1.setUserNo(JsonIntIsNullCheck(object, "USER_NO"));
                        userItem1.setLv(JsonIntIsNullCheck(object, "LV"));
                        userItem1.setId(JsonIsNullCheck(object, "ID"));
                        userItem1.setEmail(JsonIsNullCheck(object, "EMAIL"));
                        userItem1.setPassword(JsonIsNullCheck(object, "PASSWORD"));
                        userItem1.setNickname(JsonIsNullCheck(object, "NICKNAME"));
                        userItem1.setName(JsonIsNullCheck(object, "NAME"));
                        userItem1.setBirth(JsonIntIsNullCheck(object, "BIRTH"));
                        userItem1.setGender(JsonIntIsNullCheck(object, "GENDER"));
                        userItem1.setDiagnosisFlag(JsonIntIsNullCheck(object, "DIAGNOSIS_FLAG"));  // 확진 여부
                        userItem1.setOutbreakDt(JsonIsNullCheck(object, "OUTBREAK_DT")); // 발병일
                        userItem1.setProfileImg(JsonIsNullCheck(object, "PROFILE_IMG"));  // 프로필 사진
                        userItem1.setDeviceCode(JsonIsNullCheck(object, "DEVICE_CODE"));  // fcm토큰
                        userItem1.setLoginType(JsonIntIsNullCheck(object, "LOGIN_TYPE"));  //  1: 일반 이메일, 2 : 네이버, 3 : 카카오, 4 : 애플
                        userItem1.setOsType(JsonIntIsNullCheck(object, "OS_TYPE")); // 1 : android, 2 : ios
                        userItem1.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        userItem1.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));
                        userItem1.setPhone(JsonIsNullCheck(object, "PHONE"));

                        Utils.userItem = userItem1;

                        userItem = userItem1;

                        editor.putString("DEVICE_CODE",userItem1.getDeviceCode());
                        editor.putString("USER_ID", userItem1.getId());
                        editor.putString("USER_PASSWORD", userItem1.getPassword());
                        editor.putInt("LOGIN_TYPE", userItem1.getLoginType());
                        editor.putInt("OS_TYPE", userItem1.getOsType());

                        editor.apply();
                        new OneButtonDialog(AccountActivity.this,"회원정보 수정","저장되었습니다.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(View v) {
                                onBackPressed();
                            }
                        });

                    }else {
                    }
                }else {

                }
                txtSave.setClickable(true);
            }catch (JSONException e){
                txtSave.setClickable(true);
    //                StartActivity(SplashActivity.this, HomeActivity.class);
            }
        }
    }
    public class imageUploadNetwork extends AsyncTask<String, String, String> {

        String path;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
//            FormBody.Builder body;
//            body = (new FormBody.Builder())
//                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()));

            File file = new File(strings[0]);

//            path = strings[0];

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("profileImage", "profileImage", RequestBody.create(MediaType.parse("image/*"), file))
                    .build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.imageUpload(), body);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null){
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    realUrl = JsonIsNullCheck(jsonObject, "RealURL");
                }catch (JSONException e){
//                    Toast.makeText(AccountActivity.this, "path : " + path, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class LogoutProcessNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.logoutProcess(), body.build());

                return response;
            } catch (IOException e) {
                e.printStackTrace();
                logoutButton.setClickable(true);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                twoButtonDialog = new TwoButtonDialog(AccountActivity.this,"로그아웃", "로그아웃 하시겠습니까?","취소", "확인");
                twoButtonDialog.setOkButtonClickListener(new TwoButtonDialog.OkbuttonClickListener() {
                    @Override
                    public void OkButtonClick(View v) {
                        editor = preferences.edit();
                        editor.putInt("IS_LOGIN_FLAG",0);
                        editor.apply();

                        if(userItem.getLoginType() == 2) {
                            mOAuthLoginInstance = OAuthLogin.getInstance();
                            mOAuthLoginInstance.logout(AccountActivity.this);
                        }else if(userItem.getLoginType() == 3) {
                            onClickLogout();
                        }

                        Utils.itemsArrayList = null;
                        Utils.itemsNoticeArrayList = null;
                        Utils.itemsWriteArrayList = null;
                        Utils.itemsScrabArrayList = null;
                        Utils.itemsCommentArrayList = null;
                        Utils.likeItemArrayList = null;
                        Utils.scrapItemArrayList = null;
                        Utils.COMMUNITY_LEFT_PAGEING = 0;
                        Utils.COMMUNITY_LEFT_SEARCH_TOTAL_PAGE = 0;
                        Utils.COMMUNITY_RIGHT_PAGEING = 0;
                        Utils.COMMUNITY_RIGHT_SEARCH_TOTAL_PAGE = 0;
                        Utils.COMMUNITY_SELECT_TAB_POSITION = 0;

                        Utils.registerDtList = null;
                        Utils.koList = null;
                        Utils.etcItemArrayList = null;
                        Utils.mList = null;
                        Utils.hisItems = null;
                        Utils.AllRegisterDtList = null;
                        Utils.AllKoList = null;
                        Utils.AllEtcItemArrayList = null;
                        Utils.AllList = null;
                        Utils.FEED_LIST_LAST_POSITION = 0;
                        Utils.FEED_ALL_LIST_LAST_POSITION = 0;

                        Utils.userItem = null;

                        Utils.MedicineTakingItems = null;
                        Utils.MEDICINE_SELECT_TAB_POSITION = 0;


                        Intent i = new Intent(AccountActivity.this, LoginSignUpSelectActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        logoutButton.setClickable(true);
                        secessionTextView.setClickable(true);
                        finish();
                    }
                });
                twoButtonDialog.setCancelButtonClickListener(new TwoButtonDialog.CancelButtonClickListener() {
                    @Override
                    public void CancelButtonClick(View v) {
                        logoutButton.setClickable(true);
                        secessionTextView.setClickable(true);
                    }
                });
//                Intent i = new Intent(AccountActivity.this, HomeActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//                onBackPressed();
            }catch (JSONException e){
                logoutButton.setClickable(true);
            }
        }
    }
    public class DeleteUserInfoNetwork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            FormBody.Builder body;

            body = (new FormBody.Builder())
                    .add("USER_NO", String.valueOf(Utils.userItem.getUserNo()));

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.deleteUserInfo(), body.build());
                return response;
            } catch (IOException e) {
                secessionTextView.setClickable(false);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                editor = preferences.edit();

                if(userItem.getLoginType() == 2) {
                    mOAuthLoginInstance = OAuthLogin.getInstance();
                    mOAuthLoginInstance.logout(AccountActivity.this);
                }else if(userItem.getLoginType() == 3) {
                    onClickLogout();
                }

                Utils.itemsArrayList = null;
                Utils.itemsNoticeArrayList = null;
                Utils.itemsWriteArrayList = null;
                Utils.itemsScrabArrayList = null;
                Utils.itemsCommentArrayList = null;
                Utils.likeItemArrayList = null;
                Utils.scrapItemArrayList = null;
                Utils.COMMUNITY_LEFT_PAGEING = 0;
                Utils.COMMUNITY_LEFT_SEARCH_TOTAL_PAGE = 0;
                Utils.COMMUNITY_RIGHT_PAGEING = 0;
                Utils.COMMUNITY_RIGHT_SEARCH_TOTAL_PAGE = 0;
                Utils.COMMUNITY_SELECT_TAB_POSITION = 0;

                Utils.registerDtList = null;
                Utils.koList = null;
                Utils.etcItemArrayList = null;
                Utils.mList = null;
                Utils.hisItems = null;
                Utils.AllRegisterDtList = null;
                Utils.AllKoList = null;
                Utils.AllEtcItemArrayList = null;
                Utils.AllList = null;
                Utils.FEED_LIST_LAST_POSITION = 0;
                Utils.FEED_ALL_LIST_LAST_POSITION = 0;;

                Utils.userItem = null;

                Utils.MedicineTakingItems = null;
                Utils.MEDICINE_SELECT_TAB_POSITION = 0;

                editor.putInt("IS_LOGIN_FLAG",0);
                editor.putInt("LOGIN_FLAG",1);
                editor.apply();

            }catch (JSONException e){
                secessionTextView.setClickable(true);
            }
        }
    }

    private void goToAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_PICK);
    }

    private Uri photoUri;
    private String imageFilePath;

    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", photoFile);
                Log.i(TAG,"uri : " + photoUri);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_PICTURE_PICK);
            }
        }
    }

//    private File createImageFile() throws IOException {
//        String timeStamp = "SOOM";
//        String imageFileName = "TEST_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,      /* prefix */
//                ".jpg",         /* suffix */
//                storageDir          /* directory */
//        );
//
//        imageFilePath = image.getAbsolutePath();
//        Log.i(TAG,"image : " + imageFilePath);
//        return image;
//    }

    private String rotateImage(int degree, String imagePath){

        if(degree<=0){
            return imagePath;
        }
        try{
            Bitmap b= BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if(b.getWidth()>b.getHeight()){
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            }else if (imageType.equalsIgnoreCase("jpeg")|| imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        return imagePath;
    }

    public String getPathFromUri(Uri uri){

        Cursor cursor = getContentResolver().query(uri, null, null, null, null );

        cursor.moveToNext();

        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );

        cursor.close();

        return path;
    }

    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));

        cursor.close();
        return path;
    }

    private void cropImage(Uri uri){
        CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setAspectRatio(300,300)
                //사각형 모양으로 자른다
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_PICK && resultCode == RESULT_OK) {
//            Log.i(TAG, "getData1 : " + result);
////            imgIntent = result;
////            rotateImage(90,getPathFromUri(result.getData()));
//            editor.putBoolean("picture",false);
//            editor.apply();
//            beginCrop(result.getData());
////            beginCrop(Uri.fromFile(new File(rotateImage(90,getPathFromUri(result.getData())))));
//        } else if (requestCode == REQUEST_PICTURE_PICK && resultCode == RESULT_OK) {
//            editor.putBoolean("picture",true);
//            editor.apply();
//            beginCrop(Uri.fromFile(new File(rotateImage(90, imageFilePath))));
////            beginCrop(photoUri);
//        } else if (requestCode == REQUEST_CROP && resultCode == RESULT_OK) {
//            Log.i(TAG, "getData2 : " + result);
////            imageFilePath = String.valueOf(Crop.getOutput(result));
//            handleCrop(resultCode, result);
//        } else if (requestCode == 1111 && resultCode == RESULT_OK) {
//            /// 기존 데이터???
//            Log.i(TAG, "기존테이터");
//        }
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case CROP_FROM_CAMERA: {
                Log.i(TAG,"data : " + data);
                Log.i(TAG,"data2 : " + new File(mImageCaptureUri.getPath()));
                Log.i(TAG,"data3 : " + imageFilePath);
                new imageUploadNetwork().execute(imageFilePath);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
                Glide.with(this).asBitmap().load(mImageCaptureUri).apply(requestOptions).into(profileImageView);
                break;
            }

            case 300 : {
                if (pictureSelect == 1){
                    doTakePhotoAction();
                }else{
                    doTakeAlbumAction();
                }
                break;
            }

            case CROP_IMAGE_ACTIVITY_REQUEST_CODE : {
//                File croppedFileName = null;
//                try {
//                    croppedFileName = createCacheImageFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
                CropImage.ActivityResult activityResult = CropImage.getActivityResult(data);
//                Log.i(TAG,"uri : " + activityResult.getUri().getPath());
//                Log.i(TAG,"uri : " + activityResult.getOriginalUri());
//                File tempFile = new File(getExternalCacheDir().getAbsolutePath(), croppedFileName.getName());
//
//                imageFilePath = String.valueOf(activityResult.getUri()).replace("file:/","content:");
//                mImageCaptureUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID+".fileprovider", tempFile);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
                Glide.with(this).load(activityResult.getOriginalUri()).apply(requestOptions).into(profileImageView);

                new imageUploadNetwork().execute(activityResult.getUri().getPath());

                break;
            }

            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.i(TAG,"mUri : " + mImageCaptureUri);
                cropImage(mImageCaptureUri);
                break;
            }

            case PICK_FROM_CAMERA: {
                Log.i(TAG,"bbbb : " + mImageCaptureUri);

                this.grantUriPermission("com.android.camera", mImageCaptureUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Intent intent = new Intent("com.android.camera.action.CROP");

                intent.setDataAndType(mImageCaptureUri, "image/*");

                List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                Log.i(TAG,"size : " + list.size());
                for(ResolveInfo resolveInfo : list){
                    String packageName = resolveInfo.activityInfo.packageName;
                    String name = resolveInfo.activityInfo.applicationInfo.loadLabel(getPackageManager()).toString();

                    Log.i(TAG, "name: " + name + " -" + resolveInfo.activityInfo.applicationInfo.className);

                    getApplicationContext().grantUriPermission(packageName,mImageCaptureUri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

//                int size = list.size();

//                if (size == 0) {
//                    Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 4);
                    intent.putExtra("aspectY", 4);
                    intent.putExtra("scale", true);
                    File croppedFileName = null;
                    try {
                        croppedFileName = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    File folder = new File(getExternalFilesDir(null).getPath() + "/test/");
                    File tempFile = new File(getExternalFilesDir(null).getAbsolutePath(), croppedFileName.getName());

                    Log.i(TAG,"tempFile : " + tempFile.getAbsolutePath());
                    imageFilePath = tempFile.getAbsolutePath();
                    mImageCaptureUri = FileProvider.getUriForFile(this, getPackageName()+".fileprovider", tempFile);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    Log.i(TAG,"temp 11");
                    intent.putExtra("return-data", false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //Bitmap 형태로 받기 위해 해당 작업 진행
                    Log.i(TAG,"temp 22");
                    Intent i = new Intent(intent);
                    ResolveInfo res = list.get(0);
                Log.i(TAG,"temp 33");
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    i.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    grantUriPermission(res.activityInfo.packageName, mImageCaptureUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.i(TAG,"temp 44");
//                getApplicationContext().grantUriPermission(packageName,mImageCaptureUri,Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    startActivityForResult(i, CROP_FROM_CAMERA);

//                }
                break;
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "cropped"+new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date(System.currentTimeMillis()))+".jpg"));
        imageFilePath = destination.getPath();
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            if (pictureSelect == 2){
                new imageUploadNetwork().execute(imageFilePath);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
                Glide.with(this).asBitmap().load(imageFilePath).apply(requestOptions).into(profileImageView);
            }else if (pictureSelect == 1){
                new imageUploadNetwork().execute(imageFilePath);
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
                Glide.with(this).asBitmap().load(photoUri).apply(requestOptions).into(profileImageView);
            }
            cameraImageView.setVisibility(View.VISIBLE);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickLogout() {
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                return null;
            }
        });
    }

    private static class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
            }

            return null;
        }

        protected void onPostExecute(Void v) {
        }
    }

    private void isNicknameCanUsed() {
        txtNicknameCheck.setText("사용가능한 닉네임입니다.");
        txtNicknameCheck.setTextColor(Color.parseColor("#09D182"));
        txtNicknameCheck.setVisibility(View.VISIBLE);
    }
    private void isNicknameCanNotUsed() {
        txtNicknameCheck.setText("이미 사용중인 닉네임입니다.");
        txtNicknameCheck.setTextColor(Color.parseColor("#DB7676"));
        txtNicknameCheck.setVisibility(View.VISIBLE);
    }

    public class OverlapUserNickNameNetwork extends AsyncTask<String, String, String> {

        int status = 0;

        public OverlapUserNickNameNetwork(int status) {
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... strings) {
            String stEmail = strings[0];

            RequestBody body = new FormBody.Builder().add("NICKNAME", stEmail).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.overlapUserNickname(), body);

                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 사용 중
                    isNicknameCanNotUsed();
                    enableNickname = false;
                    if (status == 1){
                        new OneButtonDialog(AccountActivity.this,"회원정보 수정","계정정보를 확인해주세요.","확인");
                    }
                }else {
                    isNicknameCanUsed();
                    enableNickname = true;
                    if (status == 1 && enableEmail){
                        new UpdateUserInfoNetwork().execute();
                    }
                }
            }catch (JSONException e){
            }
        }
    }

    public class OverlapEmailNetwork extends AsyncTask<String, String, String> {


        int status = 0;

        public OverlapEmailNetwork(int status) {
            this.status = status;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stEmail = strings[0];

            RequestBody body = new FormBody.Builder().add("EMAIL", strings[0]).build();

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.overlapUserEmail(), body);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (JsonIsNullCheck(jsonObject, "result").equals("N")) {
                    // 사용 중
                    isEmailCanNotUsed();
                    enableEmail = false;
                    if (status == 1){
                        new OneButtonDialog(AccountActivity.this,"회원정보 수정","계정정보를 확인해주세요.","확인");
                    }
                } else {
                    isEmailCanUsed();
                    enableEmail = true;
                    if (status == 1 && enableNickname){
                        new UpdateUserInfoNetwork().execute();
                    }
                }
            } catch (JSONException e) {
            }
        }
    }

    private void isEmailCanNotUsed() {
        txtEmailCheck.setText("이미 사용중인 이메일입니다.");
        txtEmailCheck.setTextColor(Color.parseColor("#DB7676"));
        txtEmailCheck.setVisibility(View.VISIBLE);
    }

    private void isEmailCanUsed() {
        txtEmailCheck.setText("사용가능한 이메일입니다.");
        txtEmailCheck.setTextColor(Color.parseColor("#09D182"));
        txtEmailCheck.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (!userItem.getEmail().equals(nameEditView.getText().toString()) ||
                !userItem.getPassword().equals(passEditText.getText().toString()) ||
                !userItem.getNickname().equals(nickNameEditText.getText().toString()) ||
                !userItem.getPhone().equals(phoneNumberEditText.getText().toString())){

            twoButtonDialog = new TwoButtonDialog(AccountActivity.this,"계정수정", "변경사항이 저장되지 않았습니다. 정말 나가시겠습니까?","취소", "확인");
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

        }else{
            super.onBackPressed();
        }
    }

    void BottomSheet() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this,R.style.SheetDialog);
        View contentView = getLayoutInflater().inflate(R.layout.bottom_sheet_three_button, null);
        Button mButtonCamera = (Button)contentView.findViewById(R.id.btn_picture_dialog_camera);
        Button mButtonAlbum = (Button)contentView.findViewById(R.id.btn_picture_dialog_album);
        Button mButtonRemove = (Button)contentView.findViewById(R.id.btn_picture_dialog_remove);
        dialog.setContentView(contentView);

        dialog.show();

        if (profileImageView.getDrawable() == null){
            mButtonRemove.setVisibility(View.GONE);
        }else{
            mButtonRemove.setVisibility(View.VISIBLE);
        }

        Log.i(TAG,"path : " + profileImageView.getDrawable());

        mButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureSelect = 1;
                dialog.dismiss();
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
//                            TedPermission.with(AccountActivity.this)
//                                    .setPermissionListener(permissionListener)
//                                    .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                    .check();
//                        }
//                    }else{
//                        TedPermission.with(AccountActivity.this)
//                                .setPermissionListener(permissionListener)
//                                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                .check();
//                    }
                    TedPermission.with(AccountActivity.this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
            }
        });

        mButtonAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureSelect = 2;
                dialog.dismiss();
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
//                            Log.i(TAG,"aaaaaaa");
//                            doTakeAlbumAction();
//                        }
//                    }else{
//                        TedPermission.with(AccountActivity.this)
//                                .setPermissionListener(permissionListener)
//                                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
//                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                                .check();
//                    }
                    TedPermission.with(AccountActivity.this)
                            .setPermissionListener(permissionListener)
                            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
                            .check();
                }else{
                    check();
                }
            }
        });

        mButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realUrl = "";
                int resource = R.drawable.ic_no_profile;
                if (userItem.getGender() == 1){
                    resource = R.drawable.ic_no_profile_m;
                }else if (userItem.getGender() == 2){
                    resource = R.drawable.ic_no_profile_w;
                }
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(400));
                Glide.with(AccountActivity.this)
                        .load(resource)
                        .apply(requestOptions)
                        .into(profileImageView);
                dialog.dismiss();
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
                    if (pictureSelect == 1){
//                        sendTakePhotoIntent();
                        doTakePhotoAction();
                    }else{
//                        Crop.pickImage(AccountActivity.this);
                        doTakeAlbumAction();
                    }
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
            if (pictureSelect == 1){
//                sendTakePhotoIntent();
                doTakePhotoAction();
            }else{
//                Crop.pickImage(AccountActivity.this);
                doTakeAlbumAction();
            }
        }
    }
}
