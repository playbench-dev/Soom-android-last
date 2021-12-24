package com.kmw.soom2.Login.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.StartActivity;

public class NewPermissionInfoActivity extends AppCompatActivity {

    Button btnDone;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_permission_info);

        pref = getSharedPreferences(Utils.preferencesName, MODE_PRIVATE);
        editor = pref.edit();

        btnDone = findViewById(R.id.btn_new_permission_info_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("workThrough",true);
                editor.apply();
                StartActivity(NewPermissionInfoActivity.this,LoginSignUpSelectActivity.class);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}