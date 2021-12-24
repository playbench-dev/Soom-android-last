package com.kmw.soom2.Common.PopupDialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class CustomProgressDialog {
    private ProgressDialog mDialog;
    private Context mContext;
    private boolean         mDialogFlag;
    private boolean         mDialogCancelFlag;
    private String          mDialogString;
    private Handler mDialogTimerHandler;

    public CustomProgressDialog(Context tContext){
        mContext = tContext;
    }

    public void ThreadProgress(){
        if(mDialog == null){
            mDialog = new ProgressDialog(mContext);
            mDialog.setIndeterminate(true);
            mDialog.setCanceledOnTouchOutside(false);
            if(mDialogCancelFlag == false){
                mDialog.setCancelable(false);
            }else{
                mDialog.setCancelable(true);
            }
            mDialog.setMessage(mDialogString);
            mDialog.show();
        }else{
            mDialog.setMessage(mDialogString);
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 사용하고자 하는 코드
                while(mDialogFlag){
                    if(mDialog == null){
                        Log.e("dialog fail", "fail");
                    }
                }

                mDialog.dismiss();
                mDialog = null;
                mDialogCancelFlag = false;
            }
        }, 0);
    }

    public void Show(String tString){
        if(mDialog == null){
            mDialogFlag         = true;
            mDialogCancelFlag   = false;
            mDialogString       = tString;
            ThreadProgress();
        }else{
            mDialog.setMessage(tString);
        }
    }

    public void ShowCancelEnable(String tString){
        if(mDialog == null){
            mDialogFlag         = true;
            mDialogCancelFlag   = true;
            mDialogString       = tString;
            ThreadProgress();
        }else{
            mDialogCancelFlag = true;
            mDialog.setMessage(tString);
        }
    }

    public void Dismiss(){
        mDialogFlag         = false;
        mDialogCancelFlag   = false;
        mDialogString       = "";
    }

    public boolean getDialogShow(){
        if(mDialog != null){
            return true;
        }
        return false;
    }
}
