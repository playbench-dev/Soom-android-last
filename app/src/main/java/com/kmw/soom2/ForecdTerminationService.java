package com.kmw.soom2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kmw.soom2.Common.Utils;

public class ForecdTerminationService extends Service {

    private String TAG = "ForecdTerminationService";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
        pref = getSharedPreferences("preferences",MODE_PRIVATE);
        editor = pref.edit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
        Log.e("Error","onTaskRemoved - 강제 종료 " + rootIntent);
        if (pref.getInt("homeCoachMark1",0) != 2){
            editor.putInt("homeCoachMark1",0);
        }
        if (pref.getInt("homeCoachMark2",0) != 2){
            editor.putInt("homeCoachMark2",0);
        }
        if (pref.getInt("medicineCoachMark1",0) != 2){
            editor.putInt("medicineCoachMark1",0);
        }
        if (pref.getInt("medicineCoachMark2",0) != 2){
            editor.putInt("medicineCoachMark2",0);
        }
        if (pref.getInt("communityCoachMark",0) != 2){
            editor.putInt("communityCoachMark",0);
        }
        if (pref.getBoolean("communityCheck11",false)){
            editor.putBoolean("communityCheck11",false);
        }
        editor.putBoolean("backgroundCheck",true);
        editor.apply();
        stopSelf(); //서비스 종료
    }
}

