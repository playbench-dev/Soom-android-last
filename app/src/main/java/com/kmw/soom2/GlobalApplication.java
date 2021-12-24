package com.kmw.soom2;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.androidnetworking.AndroidNetworking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.kmw.soom2.Common.UpdateHelper;
import com.kmw.soom2.Common.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.brightinventions.slf4android.FileLogHandlerConfiguration;
import pl.brightinventions.slf4android.LoggerConfiguration;
import com.kakao.sdk.common.KakaoSdk;
public class GlobalApplication extends Application {
    private String TAG = "GlobalApplication";
    private static GlobalApplication instance;
    private static final Logger LOG = LoggerFactory.getLogger(GlobalApplication.class.getSimpleName());

    private Timer mActivityTransitionTimer;
    private TimerTask mActivityTransitionTimerTask;
    public boolean wasInBackground;
    private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public static GlobalApplication getGlobalApplicationContext() {

        if (instance == null) {

            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");

        }
        return instance;
    }

//    class AppLifecycleTracker implements ActivityLifecycleCallbacks {
//
//        private int numStarted = 0;
//
//        @Override
//        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
//
//        }
//
//        @Override
//        public void onActivityStarted(@NonNull Activity activity) {
//            if (numStarted == 0) {
//                // app went to foreground
//            }
//            editor.putBoolean("backgroundCheck",false);
//            editor.apply();
//            numStarted++;
//            Log.i(TAG,"start : " + numStarted);
//        }
//
//        @Override
//        public void onActivityResumed(@NonNull Activity activity) {
//
//        }
//
//        @Override
//        public void onActivityPaused(@NonNull Activity activity) {
//
//        }
//
//        @Override
//        public void onActivityStopped(@NonNull Activity activity) {
//            numStarted--;
//            if (numStarted == 0) {
//                // app went to background
//                editor.putBoolean("backgroundCheck",true);
//                editor.apply();
//            }
//            Log.i(TAG,"stop : " + numStarted);
//        }
//
//        @Override
//        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
//
//        }
//
//        @Override
//        public void onActivityDestroyed(@NonNull Activity activity) {
//
//        }
//    }


    @Override
    public void onCreate() {

        super.onCreate();

        pref = getSharedPreferences(Utils.preferencesName,MODE_PRIVATE);
        editor = pref.edit();

//        registerActivityLifecycleCallbacks(new AppLifecycleTracker());

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String,Object> defaultValue = new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE,false);
        defaultValue.put(UpdateHelper.KEY_UPDATE_NECESSARY,false);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION,""+BuildConfig.VERSION_NAME);

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(5)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);

        remoteConfig.fetchAndActivate().addOnCompleteListener( new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()){
                    remoteConfig.activate();
                }
            }
        });

        FileLogHandlerConfiguration fileHandler = LoggerConfiguration.fileLogHandler(this);
        fileHandler.setRotateFilesCountLimit(9);
        LoggerConfiguration.configuration()
                .addHandlerToRootLogger(fileHandler);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){//오레오 버전 이상은 채널로 만들어 줘야 한다.
//            deleteNotificationChannel("drugAlarm");
//            deleteNotificationChannel("notice");
//            deleteNotificationChannel("community");

            makeNotificationChannel("drugAlarm","drugAlarm","",NotificationManager.IMPORTANCE_HIGH, defaultSoundUri);
            makeNotificationChannel("notice","notice","",NotificationManager.IMPORTANCE_HIGH, defaultSoundUri);
            makeNotificationChannel("community","community","",NotificationManager.IMPORTANCE_HIGH, defaultSoundUri);
            makeNotificationChannel("home","home","",NotificationManager.IMPORTANCE_HIGH, defaultSoundUri);
        }
        instance = this;
        // Kakao Sdk 초기화

        KakaoSdk.init(this,getString(R.string.kakao_app_key));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void deleteNotificationChannel(String id)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.deleteNotificationChannel(id);
    }
    @Override

    public void onTerminate() {

        super.onTerminate();

        instance = null;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, String description,int importance, Uri soundUri)
    {
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//        channel.setShowBadge(true);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build();

        long[] pattern = {0,500,0,500};

        channel.setVibrationPattern(pattern);
        channel.enableVibration(true);
        channel.setDescription(description);
        channel.setSound(soundUri,audioAttributes);

        notificationManager.createNotificationChannel(channel);
    }
}