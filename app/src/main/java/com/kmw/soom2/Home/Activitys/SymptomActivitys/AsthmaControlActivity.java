package com.kmw.soom2.Home.Activitys.SymptomActivitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kmw.soom2.Home.Activitys.AdultActivitys.AdultCheckFirstActivity;
import com.kmw.soom2.Home.Activitys.AdultActivitys.NewActLicenseActivity;
import com.kmw.soom2.Home.Activitys.KidsActivitys.KidsCheckFirstActivity;
import com.kmw.soom2.R;

import static com.kmw.soom2.Common.Utils.NullCheck;

public class AsthmaControlActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "AsthmaControlActivity";
    TextView txtBack;
    TextView txtLicense;
    Button btnAdult,btnKids;
    Intent beforeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asthma_control);

        beforeIntent = getIntent();

        FindViewById();

        NullCheck(this);

    }

    void FindViewById(){
        txtBack = (TextView)findViewById(R.id.txt_asthma_first_back);
        btnAdult = (Button)findViewById(R.id.btn_asthma_first_adult);
        btnKids = (Button)findViewById(R.id.btn_asthma_first_kids);
        txtLicense = (TextView)findViewById(R.id.txt_asthma_control_license);

        txtBack.setOnClickListener(this);
        btnAdult.setOnClickListener(this);
        btnKids.setOnClickListener(this);
        txtLicense.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_asthma_first_back : {
                onBackPressed();
                break;
            }
            case R.id.btn_asthma_first_adult : {
                Intent i = new Intent(this,AdultCheckFirstActivity.class);
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("homeFragment")){
                        i.putExtra("homeFragment",true);
                    }
                }
                startActivity(i);
                break;
            }
            case R.id.btn_asthma_first_kids : {
                Intent i = new Intent(this,KidsCheckFirstActivity.class);
                if (beforeIntent != null){
                    if (beforeIntent.hasExtra("homeFragment")){
                        i.putExtra("homeFragment",true);
                    }
                }
                startActivity(i);
                break;
            }
            case R.id.txt_asthma_control_license : {
                Intent i = new Intent(this, NewActLicenseActivity.class);
                startActivity(i);
                break;
            }
        }
    }
}
