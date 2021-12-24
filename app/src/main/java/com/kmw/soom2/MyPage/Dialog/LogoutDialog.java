package com.kmw.soom2.MyPage.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.kmw.soom2.R;

public class LogoutDialog extends Dialog implements View.OnClickListener{
    Button cancelButton,logoutButton;
    Context context;
    private LogoutButtonListener logoutButtonListener;

    public LogoutDialog(Context context, LogoutButtonListener logoutButtonListener) {
        super(context);
        this.context = context;
        this.logoutButtonListener = logoutButtonListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.logout_dialog);
    }



    public interface LogoutButtonListener {

        void logoutButton(boolean data);
    }
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        View layout = getLayoutInflater().inflate(R.layout.two_btn_popup,null);

        setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        setContentView(layout);

        setCancelable(false);

        cancelButton = (Button) findViewById(R.id.cancel_button_logout_dialog);
        logoutButton = (Button) findViewById(R.id.logout_button_logout_dialog);
        cancelButton.setOnClickListener(this);
        logoutButtonListener.logoutButton(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel_button_logout_dialog:
                dismiss();

            case R.id.logout_button_logout_dialog:
                logoutButtonListener.logoutButton(true);
                dismiss();
        }
    }
}

