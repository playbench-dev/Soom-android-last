package com.kmw.soom2.Views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.kmw.soom2.Common.Utils;

import static com.kmw.soom2.Communitys.Adapters.CommunityImgDetailViewPagerAdapter.pv;

public class CustomExoPlayerView extends PlayerView {
    Handler updateHandler = new Handler();
    long currentPosition;
    public static SimpleExoPlayer player;
    DataSource.Factory mediaDataSourceFactory;
    DefaultTrackSelector trackSelector;
    TrackGroupArray lastSeenTrackGroupArray;
    AdaptiveTrackSelection.Factory videoTrackSelectionFactory;
    public CustomExoPlayerView(Context context) {
        super(context);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomExoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void initializePlayer(String url) {
        if (trackSelector != null) {
        } else {
        }

        trackSelector = new DefaultTrackSelector();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "mediaPlayerSample"));
        ExtractorMediaSource mediaSource;

        String replaceText = url;
        if (replaceText.contains("soom2.testserver-1.com:8080")){
            replaceText = replaceText.replace("soom2.testserver-1.com:8080","soomcare.info");
        }else if (replaceText.contains("103.55.190.193")){
            replaceText = replaceText.replace("103.55.190.193","soomcare.info");
        }
        if (url.contains("https:")) {
            mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse(replaceText));
        } else {
            mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                    .createMediaSource(Uri.parse("https:" + replaceText));
        }

        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);


        player.prepare(mediaSource, false, false);
        player.setPlayWhenReady(true);

        setShutterBackgroundColor(Color.TRANSPARENT);
        setPlayer(player);
        requestFocus();
        updateHandler.postDelayed(updateVideoTime, 100);

        lastSeenTrackGroupArray = null;
    }

    //현재 동영상의 시간
    public int getCurrentPosition() {
        return (int) player.getCurrentPosition();
    }
    public boolean playing(){
        if(player!=null){
            return true;
        }else{
            return false;
        }
    }
    //동영상이 실행되고있는지 확인
    public boolean isPlaying() {
        return player.getPlayWhenReady();
    }

    //동영상 정지
    public void pause() {
        player.setPlayWhenReady(false);
    }

    //동영상 재생
    public void start() {
        player.setPlayWhenReady(true);
    }

    //동영상 시간 설정
    public void seekTo(int position) {
        player.seekTo(position);
    }


    @Override
    public void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            pv.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            pv.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);

        }
    }
    @Override
    public void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }

    //동영상 해제
    public void releasePlayer() {
        player.release();
        trackSelector = null;
    }
    private Runnable updateVideoTime = new Runnable(){

        public void run(){

            currentPosition = player.getCurrentPosition();

            updateHandler.postDelayed(this, 100);
            if(player.getDuration()>0) {
                if (currentPosition >= player.getDuration()) {
//                    releasePlayer();
                    seekTo(0);
                }
            }else {
                if (currentPosition <= player.getDuration()) {
//                    releasePlayer();
                    seekTo(0);
                }
            }
        }
    };
}