package com.kmw.soom2.Communitys.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.kmw.soom2.Common.SpacesItemDecoration;
import com.kmw.soom2.Home.HomeAdapter.MemoVideoAdapter;
import com.kmw.soom2.Home.HomeAdapter.NewMemoVideoAdapter;
import com.kmw.soom2.Home.HomeItem.VideoMenuItem;
import com.kmw.soom2.R;

import java.util.ArrayList;
import java.util.Vector;

import static com.kmw.soom2.Common.Utils.NullCheck;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "VideoActivity";
    TextView txtBack,txtDone;
    RecyclerView gridView;
    NewMemoVideoAdapter adapter;
    public static ArrayList<Uri> videoPathList = new ArrayList<>();
    public static Bitmap bitmapThumbnail = null;

    Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        NullCheck(this);

        FindViewById();

        int numberOfColumns = 3;
        gridView.setLayoutManager(new GridLayoutManager(VideoActivity.this, numberOfColumns));
        gridView.addItemDecoration(new SpacesItemDecoration(5));

        display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        adapter = new NewMemoVideoAdapter(VideoActivity.this,display.getWidth()/3);

        for (int i = 0; i < 20; i++){
            adapter.setUp(null);
        }

        gridView.setAdapter(adapter);

//        new getVideo().execute();

        Handler handler = new Handler()

        {
            public void handleMessage(Message msg)
            {
                display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                adapter = new NewMemoVideoAdapter(VideoActivity.this,display.getWidth()/3);
                gridView.setAdapter(adapter);
                getVideo();
            }
        };

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
            }
        }.start();
    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_video_back);
        txtDone = (TextView)findViewById(R.id.txt_video_done);
        gridView = (RecyclerView)findViewById(R.id.grid_video);

        txtBack.setOnClickListener(this);
        txtDone.setOnClickListener(this);
    }

    public class getVideo extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            display= ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            adapter = new NewMemoVideoAdapter(VideoActivity.this,display.getWidth()/3);
            gridView.setAdapter(adapter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getVideo();
                }
            });
        }
    }

    private Vector<VideoMenuItem> getVideo() {
        String[] proj = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA
        };
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        Vector<VideoMenuItem> menus = new Vector<>();
        assert cursor != null;

        while (cursor.moveToNext()) {
            String title = cursor.getString(1);
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
            Log.i(TAG,"video id : " + id);
            Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), id, MediaStore.Video.Thumbnails.MINI_KIND, null);
            // 썸네일 크기 변경할 때.
            //Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, width, height);
            String data = cursor.getString(2);
//            menus.add(new VideoMenuItem(title, bitmap, Uri.parse(data)));
            adapter.setUp(new VideoMenuItem(title, bitmap, Uri.parse(data), id));
        }

        cursor.close();
        return menus;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_video_back : {
                onBackPressed();
                break;
            }
            case R.id.txt_video_done : {
                if (videoPathList.size() > 0){
                    if (bitmapThumbnail != null){
                        Bitmap resizedBmp = Bitmap.createScaledBitmap(bitmapThumbnail, (int) 150, (int) 150, true);
                        Intent i = new Intent();
                        i.putExtra("videoThumbnail",resizedBmp);
                        i.putExtra("videoPath",String.valueOf(videoPathList.get(0)));
                        setResult(RESULT_OK,i);
                        onBackPressed();
                    }else{
                        onBackPressed();
                    }
                }else{
                    onBackPressed();
                }
                break;
            }
        }
    }
}
