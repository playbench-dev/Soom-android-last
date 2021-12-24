package com.kmw.soom2.Views;

import android.content.Context;
import android.os.Handler;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class LineTextView extends TextView {

    private String TAG = "LineTextView";

    public LineTextView(Context context) {
        super(context);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public CharSequence getText() {
        return super.getText();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        Layout layout = getLayout();
        String text11 = getText().toString();

        int start = 0;
        int end;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"review : " + getLineCount());
            }
        });

        for (int i = 0; i < getLineCount(); i++){
            end = layout.getLineEnd(i);
            start = end;
        }

        super.setText(text, type);
    }

    @Override
    public int getLineCount() {
        return super.getLineCount();
    }

    public void getLineCnt(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"review : " + getLineCount());
            }
        });
    }

    @Override
    public TextUtils.TruncateAt getEllipsize() {
        return super.getEllipsize();
    }

    @Override
    public void setEllipsize(TextUtils.TruncateAt where) {
        super.setEllipsize(where);
    }
}
