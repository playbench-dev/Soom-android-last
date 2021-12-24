package com.kmw.soom2.Common.PopupDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.kmw.soom2.R;

public class TwoButtonDialog extends Dialog implements View.OnClickListener {
    String title, contents, btnLeftText, btnRightText;

    private TwoButtonDialog.OkbuttonClickListener okButtonClickListener = null;
    private TwoButtonDialog.CancelButtonClickListener cancelButtonClickListener = null;

    /////////////////////////////////////////////////////////////////////////////////////////////
    public interface OkbuttonClickListener {
        void OkButtonClick(View v);

    }

    public void setOkButtonClickListener(TwoButtonDialog.OkbuttonClickListener okbuttonClickListener) {
        this.okButtonClickListener = okbuttonClickListener;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////
    public interface CancelButtonClickListener {
        void CancelButtonClick(View v);

    }

    public void setCancelButtonClickListener(TwoButtonDialog.CancelButtonClickListener cancelButtonClickListener) {
        this.cancelButtonClickListener = cancelButtonClickListener;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    public TwoButtonDialog(Context context, String title, String contents, String btnLeftText, String btnRightText) {
        super(context);
        this.title = title;
        this.contents = contents;
        this.btnLeftText = btnLeftText;
        this.btnRightText = btnRightText;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View layout = getLayoutInflater().inflate(R.layout.two_btn_popup,null);

        setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        setContentView(layout);

        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView txtTitle = (TextView) findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView) findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button) findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button) findViewById(R.id.btn_two_btn_popup_right);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_two_btn_popup_left: {
                if(cancelButtonClickListener !=null){
                    cancelButtonClickListener.CancelButtonClick(v);
                    dismiss();
                }else{
                    dismiss();
                }
                break;
            }
            case R.id.btn_two_btn_popup_right: {
                if(okButtonClickListener !=null){
                    okButtonClickListener.OkButtonClick(v);
                    dismiss();
                }else {
                    dismiss();
                }
                break;
            }
        }
    }
}
