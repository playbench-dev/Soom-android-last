package com.kmw.soom2.Communitys.Activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Communitys.Adapters.GalleryAdapter;
import com.kmw.soom2.Communitys.Items.GalleryItem;
import com.kmw.soom2.Communitys.Items.GalleryManager;
import com.kmw.soom2.Communitys.Items.GridDividerDecoration;
import com.kmw.soom2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static com.kmw.soom2.Common.ImageResizeUtils.exifOrientationToDegrees;
import static com.kmw.soom2.Common.Utils.FILE_PATH;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.PICK_URI;
import static com.kmw.soom2.Communitys.Items.GalleryManager.photoList;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "GalleryActivity";
    TextView txtBack, txtDone;
    RecyclerView gridView;
    GalleryAdapter galleryAdapter;
    public static ArrayList<String> galleryImagePathList = new ArrayList<>();
    public static ArrayList<File> bitmapArrayList = new ArrayList<>();
    ArrayList<GalleryItem> itemArrayLists = new ArrayList<>();
    private GalleryManager mGalleryManager;
    GalleryItem test = new GalleryItem();
    File imgFile;
    Intent beforeIntent;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        beforeIntent = getIntent();
        galleryImagePathList.clear();
        galleryImagePathList = new ArrayList<>();

        NullCheck(this);

        FindViewById();

        checkPermission();

        progressDialog = new ProgressDialog(this,R.style.MyTheme);
        progressDialog.setCancelable(false);

    }

    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_gallery_back);
        txtDone = (TextView) findViewById(R.id.txt_gallery_done);
        txtBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }

    public void checkPermission() {
        int permission = ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            String[] PERMISSIONS_STORAGE = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            };
            int REQUEST_EXTERNAL_STORAGE = 1;

            ActivityCompat.requestPermissions(
                    GalleryActivity.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            initLayout();
            init();
            test.setDataTest("");
            test.setIdTest("");
            test.setSizeTest("");
            test.setDate(0);
            photoList.add(0, test);

        }
    }

    private void initLayout() {
        gridView = (RecyclerView) findViewById(R.id.grid_gallery);
    }

    private void init() {

        //갤러리 리사이클러뷰 초기화
        initRecyclerGallery();
    }

    private void initRecyclerGallery() {

        galleryAdapter = new GalleryAdapter(GalleryActivity.this, initGalleryPathList(), R.layout.view_gallery_list_item);
        gridView.setAdapter(galleryAdapter);

        gridView.setLayoutManager(new GridLayoutManager(this, 3));
        gridView.setItemAnimator(new DefaultItemAnimator());
        gridView.addItemDecoration(new GridDividerDecoration(getResources(), R.drawable.divider_recycler_gallery));
    }

    private List<GalleryItem> initGalleryPathList() {
        mGalleryManager = new GalleryManager(getApplicationContext());
        return mGalleryManager.getAllPhotoPathList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermission();
                } else {
                    Toast.makeText(GalleryActivity.this, "권한요청을 거부했습니다.", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2222) {
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), PICK_URI);
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(FILE_PATH);
                        int exifOrientation = exif.getAttributeInt(
                                ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(exifOrientation);
                        bitmap = rotate(bitmap, exifDegree);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "nameofimage", "description");

                init();
                GalleryItem test = new GalleryItem();
                test.setDataTest("");
                test.setIdTest("");
                test.setSizeTest("");
                test.setDate(0);
                photoList.add(0, test);

                galleryAdapter.notifyDataSetChanged();
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
            }
        }
        return bitmap;
    }

    String response = "";

    public class UploadImgNetWork extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            MultipartBody.Builder body;

            body = (new MultipartBody.Builder());

            body.setType(MultipartBody.FORM);

            for (int i = bitmapArrayList.size() - 1; i >= 0; i--) {
                body.addFormDataPart("profileImage" + (bitmapArrayList.size() - i), "profileImage" + (bitmapArrayList.size() - i), RequestBody.create(MediaType.parse("image/*"), bitmapArrayList.get(i)));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                response = Utils.POST(client, Utils.Server.imageUpload(), body.build());
                Log.i(TAG,"response : " + response);
                return response;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);
                if (object.get("result").equals("Y")) {
                    Intent intent = new Intent();
                    intent.putExtra("imagePathList", galleryImagePathList);
                    intent.putExtra("realUrl", String.valueOf(object.get("RealURL")));
                    setResult(RESULT_OK, intent);
                    txtDone.setEnabled(true);
                    imgFile.deleteOnExit();
                    progressDialog.dismiss();
                    onBackPressed();
                } else {
                    Toast.makeText(GalleryActivity.this, "파일 업로드 실패", Toast.LENGTH_SHORT).show();
                    imgFile.deleteOnExit();
                    progressDialog.dismiss();
                    onBackPressed();
                }
            } catch (JSONException e) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_gallery_back: {
                onBackPressed();
                break;
            }
            case R.id.txt_gallery_done: {
                txtDone.setEnabled(false);
                bitmapArrayList = new ArrayList<>();
                if (galleryImagePathList.size()>0){
                    progressDialog.show();
                }

                for (int i = galleryImagePathList.size()-1; i >= 0; i--) {
                    imgFile = new File(galleryImagePathList.get(i));
                    bitmapArrayList.add(imgFile);
                    if (i == 0) {
                        new UploadImgNetWork().execute();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}