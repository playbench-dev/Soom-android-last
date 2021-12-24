package com.kmw.soom2.Communitys.Items;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by woong on 2015. 10. 20..
 */
public class GalleryManager {

    public static ArrayList<GalleryItem> photoList = new ArrayList<>();

    private Context mContext;

    public GalleryManager(Context context) {
        mContext = context;
    }


    /**
     * 갤러리 이미지 반환
     *
     * @return
     */
    public List<GalleryItem> getAllPhotoPathList() {
        photoList.clear();

        GalleryItem test = null;

        String[] proj = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED};

        Cursor imageCursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        if (imageCursor != null && imageCursor.moveToFirst()) {
            String title;
            String thumbsID;
            String thumbsImageID;
            String thumbsData;
            String date1;
            String imgSize;

            int thumbsIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int thumbsDataCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int date = imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
            int thumbsImageIDCol = imageCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
            int thumbsSizeCol = imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE);
            int num = 0;
            do {
                thumbsID = imageCursor.getString(thumbsIDCol);
                thumbsData = imageCursor.getString(thumbsDataCol);
                thumbsImageID = imageCursor.getString(thumbsImageIDCol);
                imgSize = imageCursor.getString(thumbsSizeCol);
                date1 = imageCursor.getString(date);

                num++;
                test = new GalleryItem();

//                Log.i("GalleryManager","date : " + date1);
                Log.i("GalleryManager","id : " + thumbsID);

                if (imgSize != null) {
                    if (thumbsImageID != null && !imgSize.equals("0")) {
                        test.setDataTest(thumbsData);
                        test.setIdTest(thumbsID);
                        test.setSizeTest(imgSize);
                        test.setDate(Long.parseLong(date1));

                        photoList.add(test);
                    }
                } else {
                    if (thumbsImageID != null) {
                        test.setDataTest(thumbsData);
                        test.setIdTest(thumbsID);
                        test.setSizeTest("");
                        test.setDate(Long.parseLong(date1));
                        photoList.add(test);
                    }
                }


            } while (imageCursor.moveToNext());

        }
        imageCursor.close();

        Collections.sort(photoList, compare);

        return photoList;
    }

    Comparator<GalleryItem> compare = new Comparator<GalleryItem>() {
        @Override
        public int compare(GalleryItem test, GalleryItem t1) {
            int position = 0;
            try {
                if (test == null) {
                    position = 0;
                } else if (t1 == null) {
                    position = 0;
                } else if (Integer.parseInt(test.getIdTest()) > Integer.parseInt(t1.getIdTest())) {
                    position = -1;
                } else if (Integer.parseInt(test.getIdTest()) == Integer.parseInt(t1.getIdTest())) {
                    position = 0;
                } else if (Integer.parseInt(test.getIdTest()) < Integer.parseInt(t1.getIdTest())) {
                    position = 1;
                }
            } catch (Exception e) {

            }
            return position;
        }
    };
}
