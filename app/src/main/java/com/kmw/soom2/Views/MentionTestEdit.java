package com.kmw.soom2.Views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import com.kmw.soom2.Communitys.Activitys.NewCommunityDetailActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MentionTestEdit extends AppCompatEditText {

    private String TAG = "MentionTestEdit";
    public static final String DEFAULT_MENTION_PATTERN = "@[\\u4e00-\\u9fa5\\w\\-]+";
    private Pattern mPattern;
    private Runnable mAction;
    private int mMentionTextColor;
    private boolean mIsSelected;
    private MentionTestEdit.Range mLastSelectedRange;
    private List<MentionTestEdit.Range> mRangeArrayList;
    private MentionTestEdit.OnMentionInputListener mOnMentionInputListener;
    private boolean enableCheck = false;
    private String strCreate = "";

    public MentionTestEdit(Context context) {
        super(context);
        this.init();
    }

    public MentionTestEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public MentionTestEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MentionTestEdit.HackInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }

    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        if (this.mAction == null) {
            this.mAction = new Runnable() {
                public void run() {
                    MentionTestEdit.this.setSelection(MentionTestEdit.this.getText().length());
                }
            };
        }

        this.post(this.mAction);
    }

    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
//        this.colorMentionString();
//        this.selectColorMentionString();
    }

    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
//        if (this.mLastSelectedRange == null || !this.mLastSelectedRange.isEqual(selStart, selEnd)) {
//            MentionTestEdit.Range closestRange = this.getRangeOfClosestMentionString(selStart, selEnd);
//            if (closestRange != null && closestRange.to == selEnd) {
//                this.mIsSelected = false;
//            }
//
//            Log.i(TAG,"selStart : " + selStart + " selEnd : " + selEnd);
//
//            MentionTestEdit.Range nearbyRange = this.getRangeOfNearbyMentionString(selStart, selEnd);
//            if (nearbyRange != null) {
//                if (selStart == selEnd) {
//                    this.setSelection(nearbyRange.getAnchorPosition(selStart));
//                } else {
//                    if (selEnd < nearbyRange.to) {
//                        this.setSelection(selStart, nearbyRange.to);
//                    }
//
//                    if (selStart > nearbyRange.from) {
//                        this.setSelection(nearbyRange.from, selEnd);
//                    }
//                }
//            }
//        }
    }

    public void setPattern(String pattern) {
        this.mPattern = Pattern.compile(pattern);
    }

    public void setMentionTextColor(int color) {
        this.mMentionTextColor = color;
    }

    public List<String> getMentionList(boolean excludeMentionCharacter) {
        List<String> mentionList = new ArrayList();
        if (TextUtils.isEmpty(this.getText().toString())) {
            return mentionList;
        } else {
            Matcher matcher = this.mPattern.matcher(this.getText().toString());

            while(matcher.find()) {
                String mentionText = matcher.group();
                mentionList.add(mentionText);
            }

            return mentionList;
        }
    }

    public List<Integer> getMentionLengthList(boolean excludeMentionCharacter) {
        List<Integer> mentionList = new ArrayList();
        if (TextUtils.isEmpty(this.getText().toString())) {
            return mentionList;
        } else {
            Matcher matcher = this.mPattern.matcher(this.getText().toString());

            while(matcher.find()) {
                String mentionText = matcher.group();
                Log.i("TAG","matcher : " + mentionText);
                mentionList.add(mentionText.length());
            }
            return mentionList;
        }
    }

    public List<Integer> getMentionLocation(boolean excludeMentionCharacter) {
        List<Integer> mentionList = new ArrayList();
        if (TextUtils.isEmpty(this.getText().toString())) {
            return mentionList;
        } else {
            Matcher matcher = this.mPattern.matcher(this.getText().toString());
            while(matcher.find()) {
                String mentionText = matcher.group();
                mentionList.add(getText().toString().indexOf(mentionText));
            }

            return mentionList;
        }
    }

    public void setOnMentionInputListener(MentionTestEdit.OnMentionInputListener onMentionInputListener) {
        this.mOnMentionInputListener = onMentionInputListener;
    }

    private void init() {
        this.mRangeArrayList = new ArrayList(5);
        this.mPattern = Pattern.compile("@[\\u4e00-\\u9fa5\\w\\-]+");
        this.mMentionTextColor = -65536;
        this.addTextChangedListener(new MentionTestEdit.MentionTextWatcher());
    }

    public void clearMention(){
        if (NewCommunityDetailActivity.mentionSelectList != null){
            for (int i = 0; i < NewCommunityDetailActivity.mentionSelectList.size(); i++){
                if (!getText().toString().contains(NewCommunityDetailActivity.mentionSelectList.get(i))){
                    NewCommunityDetailActivity.mentionSelectList.remove(i);
                    NewCommunityDetailActivity.mentionUserNoList.remove(i);
                }
            }
        }
    }

    public void selectColorMentionString(){

        strCreate = "";

        if (NewCommunityDetailActivity.mentionSelectList != null){
            for (int i = 0; i < NewCommunityDetailActivity.mentionSelectList.size(); i++){
                if (!getText().toString().contains(NewCommunityDetailActivity.mentionSelectList.get(i))){
                    NewCommunityDetailActivity.mentionSelectList.remove(i);
                    NewCommunityDetailActivity.mentionUserNoList.remove(i);
                }
            }
        }

        this.mIsSelected = false;
        if (this.mRangeArrayList != null) {
            this.mRangeArrayList.clear();
        }

        Editable spannableText = this.getText();
        if (spannableText != null && !TextUtils.isEmpty(spannableText.toString())) {
            ForegroundColorSpan[] oldSpans = (ForegroundColorSpan[])spannableText.getSpans(0, spannableText.length(), ForegroundColorSpan.class);
            ForegroundColorSpan[] var3 = oldSpans;
            int var4 = oldSpans.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                ForegroundColorSpan oldSpan = var3[var5];
                spannableText.removeSpan(oldSpan);
            }

            int lastMentionIndex = -1;
            String text = spannableText.toString();

            Log.i(TAG,"spannableText : " + text);

            Matcher matcher = this.mPattern.matcher(text);

            while(matcher.find()) {
                String mentionText = matcher.group();

                Log.i(TAG,"mentionText : " + mentionText);

                int start;

                for (int i = 0; i < NewCommunityDetailActivity.mentionSelectList.size(); i++){

                    Log.i(TAG,"NewCommunityDetailActivity.mentionSelectList : " + NewCommunityDetailActivity.mentionSelectList.get(i));

                    if (mentionText.equals(NewCommunityDetailActivity.mentionSelectList.get(i))){

                        if (lastMentionIndex != -1) {
                            start = text.indexOf(mentionText, lastMentionIndex);
                        } else {
                            start = text.indexOf(mentionText);
                        }

                        int end = start + mentionText.length();

                        spannableText.setSpan(new ForegroundColorSpan(this.mMentionTextColor), start, end, 33);
                        spannableText.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        spannableText.setSpan(new UnderlineSpan(), start, end, 33);

                        lastMentionIndex = end;
                        this.mRangeArrayList.add(new MentionTestEdit.Range(start, end));
                    }
                }
            }
        }

        enableCheck = false;
    }

    public void selectColorMentionString(int start, int end){

        strCreate = "";

        this.mIsSelected = false;
        if (this.mRangeArrayList != null) {
            this.mRangeArrayList.clear();
        }

        Editable spannableText = this.getText();
        if (spannableText != null && !TextUtils.isEmpty(spannableText.toString())) {
            ForegroundColorSpan[] oldSpans = (ForegroundColorSpan[])spannableText.getSpans(0, spannableText.length(), ForegroundColorSpan.class);
            ForegroundColorSpan[] var3 = oldSpans;
            int var4 = oldSpans.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                ForegroundColorSpan oldSpan = var3[var5];
                spannableText.removeSpan(oldSpan);
            }
            spannableText.setSpan(new ForegroundColorSpan(this.mMentionTextColor), start, end, 33);
            spannableText.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            this.mRangeArrayList.add(new MentionTestEdit.Range(start, end));
        }
    }

    private void colorMentionString() {
        this.mIsSelected = false;
        if (this.mRangeArrayList != null) {
            this.mRangeArrayList.clear();
        }

        Editable spannableText = this.getText();
        if (spannableText != null && !TextUtils.isEmpty(spannableText.toString())) {
            ForegroundColorSpan[] oldSpans = (ForegroundColorSpan[])spannableText.getSpans(0, spannableText.length(), ForegroundColorSpan.class);
            ForegroundColorSpan[] var3 = oldSpans;
            int var4 = oldSpans.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                ForegroundColorSpan oldSpan = var3[var5];
                spannableText.removeSpan(oldSpan);
            }

            int lastMentionIndex = -1;
            String text = spannableText.toString();
            Matcher matcher = this.mPattern.matcher(text);

            while(matcher.find()) {
                String mentionText = matcher.group();
                int start;
                if (lastMentionIndex != -1) {
                    start = text.indexOf(mentionText, lastMentionIndex);
                } else {
                    start = text.indexOf(mentionText);
                }
                int end = start + mentionText.length();
                spannableText.setSpan(new ForegroundColorSpan(this.mMentionTextColor), start, end, 33);
                lastMentionIndex = end;
                this.mRangeArrayList.add(new MentionTestEdit.Range(start, end));
            }
        }
    }

    private MentionTestEdit.Range getRangeOfClosestMentionString(int selStart, int selEnd) {
        if (this.mRangeArrayList == null) {
            return null;
        } else {
            Iterator var3 = this.mRangeArrayList.iterator();

            MentionTestEdit.Range range;
            do {
                if (!var3.hasNext()) {
                    return null;
                }

                range = (MentionTestEdit.Range)var3.next();
            } while(!range.contains(selStart, selEnd));

            return range;
        }
    }

    private MentionTestEdit.Range getRangeOfNearbyMentionString(int selStart, int selEnd) {
        if (this.mRangeArrayList == null) {
            return null;
        } else {
            Iterator var3 = this.mRangeArrayList.iterator();

            MentionTestEdit.Range range;
            do {
                if (!var3.hasNext()) {
                    return null;
                }

                range = (MentionTestEdit.Range)var3.next();
            } while(!range.isWrappedBy(selStart, selEnd));

            return range;
        }
    }

    public interface OnMentionInputListener {
        void onMentionCharacterInput(boolean status, String mention);
    }

    private class Range {
        int from;
        int to;

        public Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public boolean isWrappedBy(int start, int end) {
            return start > this.from && start < this.to || end > this.from && end < this.to;
        }

        public boolean contains(int start, int end) {
            return this.from <= start && this.to >= end;
        }

        public boolean isEqual(int start, int end) {
            return this.from == start && this.to == end || this.from == end && this.to == start;
        }

        public int getAnchorPosition(int value) {
            return value - this.from - (this.to - value) >= 0 ? this.to : this.from;
        }
    }

    private class HackInputConnection extends InputConnectionWrapper {
        private EditText editText;

        public HackInputConnection(InputConnection target, boolean mutable, MentionTestEdit editText) {
            super(target, mutable);
            this.editText = editText;
        }

        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == 0 && event.getKeyCode() == 67) {
                int selectionStart = this.editText.getSelectionStart();
                int selectionEnd = this.editText.getSelectionEnd();
                Log.i(TAG,"start : " + selectionStart + " end : " + selectionEnd);
                if (selectionStart < selectionEnd){
                    String text = editText.getText().toString().substring(selectionStart,selectionEnd);
                    if (NewCommunityDetailActivity.mentionSelectList.contains(text)){
                        NewCommunityDetailActivity.mentionUserNoList.remove(NewCommunityDetailActivity.mentionSelectList.indexOf(text));
                        NewCommunityDetailActivity.mentionSelectList.remove(text);
                        Log.i(TAG,"size : " + NewCommunityDetailActivity.mentionSelectList.size());
                        strCreate = "";
                    }
                }
                Log.i(TAG,"size : " + NewCommunityDetailActivity.mentionSelectList.size());
                MentionTestEdit.Range closestRange = MentionTestEdit.this.getRangeOfClosestMentionString(selectionStart, selectionEnd);
                if (closestRange == null) {
                    MentionTestEdit.this.mIsSelected = false;
                    Log.i(TAG,"1111");
                    return super.sendKeyEvent(event);
                } else if (!MentionTestEdit.this.mIsSelected && selectionStart != closestRange.from) {
                    MentionTestEdit.this.mIsSelected = true;
                    MentionTestEdit.this.mLastSelectedRange = closestRange;
                    this.setSelection(closestRange.to, closestRange.from);
                    Log.i(TAG,"2222");
                    return true;
                } else {
                    MentionTestEdit.this.mIsSelected = false;
                    Log.i(TAG,"3333");
                    strCreate = "";
                    if (NewCommunityDetailActivity.mentionSelectList != null){
                        Log.i(TAG,"11");
                        for (int i = 0; i < NewCommunityDetailActivity.mentionSelectList.size(); i++){
                            Log.i(TAG,"22");
                            if (!getText().toString().contains(NewCommunityDetailActivity.mentionSelectList.get(i))){
                                Log.i(TAG,"33");
                                NewCommunityDetailActivity.mentionSelectList.remove(i);
                                NewCommunityDetailActivity.mentionUserNoList.remove(i);
                            }
                        }
                    }
                    return super.sendKeyEvent(event);
                }
            } else {
                this.setSelection(this.editText.getSelectionEnd(),this.editText.getSelectionStart());
                return super.sendKeyEvent(event);
            }
        }

        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            Log.i(TAG,"before : " + beforeLength + " after : " + afterLength);
            if (beforeLength == 1 && afterLength == 0) {
                return this.sendKeyEvent(new KeyEvent(0, 67)) && this.sendKeyEvent(new KeyEvent(1, 67));
            } else {
                return super.deleteSurroundingText(beforeLength, afterLength);
            }
        }
    }

    String test = "";

    private class MentionTextWatcher implements TextWatcher {

        private MentionTextWatcher() {

        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (i < charSequence.length())
                switch (charSequence.charAt(i)) {
                    case '@':
                        NewCommunityDetailActivity.mentionEnabled = false;
                        NewCommunityDetailActivity.mentionStart = 0;
                        enableCheck = false;
                        strCreate = "";
                        break;
                }
        }

        public void onTextChanged(CharSequence charSequence, int index, int i1, int count) {
            Log.i(TAG,"index : " + index + " index1 : " + i1 + " cnt : " + count);

            if (charSequence.toString().length() > 0){
                if (count == 1 && !TextUtils.isEmpty(charSequence)) {
                    char mentionChar = charSequence.toString().charAt(index);
                    if ('@' == mentionChar && MentionTestEdit.this.mOnMentionInputListener != null) {
                        NewCommunityDetailActivity.mentionStart = index;
                        enableCheck = true;
                        strCreate = "";
                        test = "";
                    }else if ('\u0020' == mentionChar && MentionTestEdit.this.mOnMentionInputListener != null){
                        enableCheck = false;
                        strCreate = "";
                    }
                }

                Matcher matcher = mPattern.matcher(charSequence.toString());

                while(matcher.find()) {
                    String mentionText = matcher.group();
                    if (!NewCommunityDetailActivity.mentionSelectList.contains(mentionText)){
                        strCreate = mentionText;
                    }
                }

                Log.i(TAG,"strCreate : " + strCreate);

                if (strCreate.length() > 0){
                    if (!test.equals(strCreate)){
                        MentionTestEdit.this.mOnMentionInputListener.onMentionCharacterInput(enableCheck,strCreate.replace("@",""));
                        test = strCreate;
                    }
                }else{
                    MentionTestEdit.this.mOnMentionInputListener.onMentionCharacterInput(false,strCreate.replace("@",""));
                }
            }else{
                enableCheck = false;
                strCreate = "";
            }
        }

        public void afterTextChanged(Editable editable) {

        }
    }
}
