package com.kmw.soom2.Home.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Home.Activitys.SymptomActivitys.AsthmaControlActivity;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.NullCheck;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView               mImageViewBack;
    Button                  mButtonMove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        FindViewById();

        NullCheck(this);

    }

    void FindViewById(){
        mImageViewBack      = (ImageView)findViewById(R.id.img_guide_close);
        mButtonMove         = (Button)findViewById(R.id.btn_guide_move);

        mImageViewBack.setOnClickListener(this);
        mButtonMove.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_guide_close : {
                onBackPressed();
                break;
            }
            case R.id.btn_guide_move : {
                setResult(RESULT_OK);
                onBackPressed();
                break;
            }
        }
    }
}
