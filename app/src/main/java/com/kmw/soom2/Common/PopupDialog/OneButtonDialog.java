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

public class OneButtonDialog extends Dialog implements View.OnClickListener{
    String title,contents,btnText;
    boolean staffCheck = false;

    private OnButtonClickListener onButtonClickListener = null;

    public interface OnButtonClickListener {
        void OnButtonClick(View v);

    }
    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
    public OneButtonDialog(Context context, String title, String contents, String btnText) {
        super(context);
        this.title = title;
        this.contents = contents;
        this.btnText = btnText;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View layout = getLayoutInflater().inflate(R.layout.one_btn_popup,null);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        setContentView(layout);
        setCancelable(false);

        show();
    }

    public OneButtonDialog(Context context, String title, String contents, String btnText, boolean staffCheck) {
        super(context);
        this.title = title;
        this.contents = contents;
        this.btnText = btnText;
        this.staffCheck = staffCheck;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View layout = getLayoutInflater().inflate(R.layout.staff_dialog_popup,null);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        layout.setMinimumWidth((int) (width * 0.88f));
//        layout.setMinimumHeight((int) (height * 0.65f));

        setContentView(layout);
        setCancelable(false);

        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!staffCheck){
            TextView txtTitle = (TextView) findViewById(R.id.txt_one_btn_popup_title);
            TextView txtContents = (TextView) findViewById(R.id.txt_one_btn_popup_contents);
            Button btnOk = (Button) findViewById(R.id.btn_one_btn_popup_ok);

            txtTitle.setText(title);
            txtContents.setText(contents);
            btnOk.setText(btnText);
            btnOk.setOnClickListener(this);
        }else{
            Button btnOk = (Button)findViewById(R.id.btn_one_btn_popup_ok);
            btnOk.setText(btnText);
            btnOk.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_one_btn_popup_ok:
                if(onButtonClickListener!=null){
                    onButtonClickListener.OnButtonClick(v);
                    dismiss();
                }else {
                    dismiss();
                }
                break;
        }
    }
}
