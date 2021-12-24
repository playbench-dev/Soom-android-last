package com.kmw.soom2.Home.Activitys.MedicineInsertActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmw.soom2.Common.PopupDialog.OneButtonDialog;
import com.kmw.soom2.Common.ServerManagement.AsyncResponse;
import com.kmw.soom2.Common.ServerManagement.NetworkUtils;
import com.kmw.soom2.Common.Utils;
import com.kmw.soom2.Home.HomeItem.MedicineTypeItem;
import com.kmw.soom2.Login.Activitys.NewAnotherLoginActivity;
import com.kmw.soom2.MainActivity;
import com.kmw.soom2.Home.HomeAdapter.MedicineSelectAdapter;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Home.HomeItem.RecyclerViewMainStickyItemDecoration;
import com.kmw.soom2.R;
import com.kmw.soom2.ex.exNewAnotherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_LIST_ALL_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.ALARM_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_CALL_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_HISTORY_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_INSERT_APPLICATION;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.MEDICINE_TYPE_CALL_LIST;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.PUSH_READ;
import static com.kmw.soom2.Common.ServerManagement.NetworkUtils.SYMPTOM_MANAGE_LINK;
import static com.kmw.soom2.Common.Utils.JsonIntIsNullCheck;
import static com.kmw.soom2.Common.Utils.JsonIsNullCheck;
import static com.kmw.soom2.Common.Utils.MEDICINE_LIST;
import static com.kmw.soom2.Common.Utils.MEDICINE_TYPE_LIST;
import static com.kmw.soom2.Common.Utils.MedicineTakingItems;
import static com.kmw.soom2.Common.Utils.NullCheck;
import static com.kmw.soom2.Common.Utils.userItem;

public class MedicineSelectActivity extends AppCompatActivity implements View.OnClickListener , AsyncResponse {

    private String TAG = "MedicineSelectActivity";
    TextView txtBack;
//    TextView txtSearch;
    EditText txtSearch;

    RecyclerView recyclerView;
    MedicineSelectAdapter adapter;
    ArrayList<RecyclerViewItemList> mList = new ArrayList<>();
    ProgressDialog progressDialog;
    int cnt = 0;

    Intent beforeIntent;

    // memo: 2021-01-14 김지훈 수정 시작
    // 약 리스트 (약 검색용)
    ArrayList<MedicineTakingItem> itemArrayList = new ArrayList<>();
    // 검색 한 약 리스트 (어뎁터용)
    ArrayList<RecyclerViewItemList> searchList = new ArrayList<>();
    // 검색한 약이 없는 경우 화면
    LinearLayout linNoList;
    // 검색한 약이 없는 경우 요청 버튼
    Button btnRequest;
    // 가입 후 약 등록을 하러 바로 들어온 경우
    boolean newbieCheck = false;
    // memo: 2021-01-14 김지훈 수정 종료

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_select);

        beforeIntent = getIntent();

        if(!beforeIntent.hasExtra("guest")){
            NullCheck(this);
        }

        FindViewById();

        if (beforeIntent != null){
            if (beforeIntent.hasExtra("newbie")){
                newbieCheck = true;
            }
            if (beforeIntent.hasExtra("keyNo") || beforeIntent.hasExtra("pushNo")){
                NetworkCall(ALARM_LIST_ALL_READ);
            }
        }


    }

    void FindViewById() {
        txtBack = (TextView) findViewById(R.id.txt_medicine_select_back);
//        txtSearch = (TextView) findViewById(R.id.txt_medicine_select_search);
        // memo: 2021-01-14 김지훈 수정 시작
        txtSearch = (EditText) findViewById(R.id.txt_medicine_select_search);
        linNoList = (LinearLayout)findViewById(R.id.lin_medicine_search_no_list);
        btnRequest = (Button)findViewById(R.id.btn_medicine_search_request);
        // memo: 2021-01-14 김지훈 수정 종료
        recyclerView = (RecyclerView) findViewById(R.id.recycler_medicine_select);

        txtBack.setOnClickListener(this);
        // memo: 2021-01-14 김지훈 수정 시작
//        txtSearch.setOnClickListener(this);

        btnRequest.setOnClickListener(this);

        if (beforeIntent.hasExtra("guest")){
            txtSearch.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        Intent intent = new Intent(MedicineSelectActivity.this, NewAnotherLoginActivity.class);
                        startActivity(intent);
                    }
                    return false;
                }
            });

        }

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s);

                if(text.equals("")) {
                    if (beforeIntent.hasExtra("guest")){
                        adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, mList,MedicineSelectActivity.this,true);
                    }else{
                        adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, mList,MedicineSelectActivity.this);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(MedicineSelectActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());

                    recyclerView.setVisibility(View.VISIBLE);
                    linNoList.setVisibility(View.GONE);
                }else {
                    searchList = new ArrayList<>();
                    for (int i = 0; i < itemArrayList.size(); i++) {
                        if (itemArrayList.get(i).getMedicineKo().contains(text.replace(" ",""))) {
                            Log.i(TAG,"Medicine Ko : " + itemArrayList.get(i).getMedicineKo());
                            searchList.add(new RecyclerViewItemList(itemArrayList.get(i), RecyclerViewItemList.ITEM_TYPE));
                        }
                    }
                    if (searchList.size() > 0) {
                        if (beforeIntent.hasExtra("guest")){
                            adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, searchList,MedicineSelectActivity.this,true);
                        }else{
                            adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, searchList,MedicineSelectActivity.this);
                        }
                        recyclerView.setVisibility(View.VISIBLE);
                        linNoList.setVisibility(View.GONE);
                    } else {
                        if (beforeIntent.hasExtra("guest")){
                            adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, searchList,MedicineSelectActivity.this,true);
                        }else{
                            adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, searchList,MedicineSelectActivity.this);
                        }
                        recyclerView.setVisibility(View.GONE);
                        linNoList.setVisibility(View.VISIBLE);
                    }
                    Log.i(TAG,"medicine ko : 1111");
                    recyclerView.setLayoutManager(new LinearLayoutManager(MedicineSelectActivity.this, LinearLayoutManager.VERTICAL, false));
                    if (recyclerView.getItemDecorationCount() > 0) {
                        recyclerView.removeItemDecorationAt(0);
                    }
                }
                Log.i(TAG,"medicine ko : 2222");
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 약 리스트 변수화
        if (MEDICINE_LIST != null){
            for (int i = 0; i < MEDICINE_LIST.size(); i++){
                itemArrayList.add(MEDICINE_LIST.get(i));
            }

            mList.add(new RecyclerViewItemList("최근 추가된 약", RecyclerViewItemList.HEADER_TYPE));

            if (MEDICINE_LIST != null){
                for (int i = MEDICINE_LIST.size()-1; i > (MEDICINE_LIST.size() - 7); i--) {
                    mList.add(new RecyclerViewItemList(MEDICINE_LIST.get(i), RecyclerViewItemList.ITEM_TYPE));
                }
            }

            mList.add(new RecyclerViewItemList("자주 찾는 천식약", RecyclerViewItemList.HEADER_TYPE));
            if (MEDICINE_LIST != null){
                for (int i = 0; i < MEDICINE_LIST.size(); i++) {
                    if (MEDICINE_LIST.get(i).getBookMark() == 1) {
                        mList.add(new RecyclerViewItemList(MEDICINE_LIST.get(i), RecyclerViewItemList.ITEM_TYPE));
                    }
                }
            }

            if (beforeIntent.hasExtra("guest")){
                adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, mList,MedicineSelectActivity.this,true);
            }else{
                adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, mList,MedicineSelectActivity.this);
            }

            recyclerView.setLayoutManager(new LinearLayoutManager(MedicineSelectActivity.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
            recyclerView.setAdapter(adapter);
        }else{
            NetworkCall(MEDICINE_CALL_LIST);
        }
    }

    void NetworkCall(String mCode){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this,R.style.MyTheme);
            progressDialog.show();
        }
        if (mCode.equals(MEDICINE_HISTORY_LIST)){
            if (beforeIntent.hasExtra("guest")){
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute("0");
            }else{
                new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo());
            }
        }else if (mCode.equals(MEDICINE_INSERT_APPLICATION)) {
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+Utils.userItem.getUserNo(),txtSearch.getText().toString());
        }else if (mCode.equals(MEDICINE_CALL_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
        }else if (mCode.equals(ALARM_LIST_ALL_READ)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute(""+String.valueOf(userItem.getUserNo()));
        }else if (mCode.equals(MEDICINE_TYPE_CALL_LIST)){
            new NetworkUtils.NetworkCall(this,this,TAG,mCode).execute();
        }
    }

    @Override
    public void ProcessFinish(String mCode, String mResult) {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (mResult != null){
            if (mCode.equals(MEDICINE_INSERT_APPLICATION)) {
                try{
                    JSONObject jsonObject = new JSONObject(mResult);

                    if (jsonObject.has("result")){
                        if (jsonObject.get("result").equals("Y")){
                            new OneButtonDialog(MedicineSelectActivity.this,"약 등록 요청완료!","약을 정확히 등록하기 위해\n최대 이틀이 소요될 수 있습니다.\n등록 완료 후 알림을 보내드립니다.","확인").setOnButtonClickListener(new OneButtonDialog.OnButtonClickListener() {
                                @Override
                                public void OnButtonClick(View v) {
                                    txtSearch.setText("");
                                }
                            });
                        }
                    }
                }catch (JSONException e){

                }
            }else if (mCode.equals(MEDICINE_CALL_LIST)){
                MEDICINE_LIST = new ArrayList<>();
                Log.i(TAG,"medicine type list : " + mResult);
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        //notice_list 테이블의 PUSH_FLAG 1인 경우 push_alarm_list 테이블에 저장 (PUSH_MESSAGE 값을 push_alarm_list의 CONTENTS로 저장)

                        MedicineTakingItem medicineTakingItem = new MedicineTakingItem();

                        medicineTakingItem.setMedicineNo(JsonIntIsNullCheck(object, "MEDICINE_NO"));
                        medicineTakingItem.setMedicineKo(JsonIsNullCheck(object, "KO"));
                        medicineTakingItem.setMedicineEn(JsonIsNullCheck(object, "EN"));
                        medicineTakingItem.setManufacturer(JsonIsNullCheck(object, "MANUFACTURER"));
                        medicineTakingItem.setIngredient(JsonIsNullCheck(object, "INGREDIENT"));
                        medicineTakingItem.setForm(JsonIsNullCheck(object, "FORM"));
                        medicineTakingItem.setMedicineTypeNo(JsonIntIsNullCheck(object, "MEDICINE_TYPE_NO"));
                        medicineTakingItem.setStorageMethod(JsonIsNullCheck(object, "STORAGE_METHOD"));
                        medicineTakingItem.setUnit(JsonIsNullCheck(object, "UNIT"));
                        medicineTakingItem.setEfficacy(JsonIsNullCheck(object, "EFFICACY"));
                        medicineTakingItem.setInformation(JsonIsNullCheck(object, "INFORMATION"));
                        medicineTakingItem.setInstructions(JsonIsNullCheck(object, "INSTRUCTIONS"));
                        medicineTakingItem.setStabiltyRationg(JsonIsNullCheck(object, "STABILITY_RATING"));
                        medicineTakingItem.setPrecaution(JsonIsNullCheck(object, "PRECAUTIONS"));
                        medicineTakingItem.setBookMark(JsonIntIsNullCheck(object, "BOOKMARK"));
                        medicineTakingItem.setMedicineImg(JsonIsNullCheck(object, "MEDICINE_IMG"));
                        medicineTakingItem.setCreateDt(JsonIsNullCheck(object, "CREATE_DT"));
                        medicineTakingItem.setUpdateDt(JsonIsNullCheck(object, "UPDATE_DT"));

                        MEDICINE_LIST.add(medicineTakingItem);
                    }
                    NetworkCall(MEDICINE_TYPE_CALL_LIST);
                } catch (JSONException e) {

                }
            }else if (mCode.equals(MEDICINE_TYPE_CALL_LIST)){
                MEDICINE_TYPE_LIST = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(mResult);
                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        MedicineTypeItem medicineTypeItem = new MedicineTypeItem();

                        medicineTypeItem.setMedicineTypeNo(JsonIntIsNullCheck(object, "MEDICINE_TYPE_NO"));
                        medicineTypeItem.setTypeImg(JsonIsNullCheck(object, "TYPE_IMG"));

                        MEDICINE_TYPE_LIST.add(medicineTypeItem);
                    }

                    for (int i = 0; i < MEDICINE_LIST.size(); i++){
                        itemArrayList.add(MEDICINE_LIST.get(i));
                    }

                    mList.add(new RecyclerViewItemList("최근 추가된 약", RecyclerViewItemList.HEADER_TYPE));

                    if (MEDICINE_LIST != null){
                        for (int i = MEDICINE_LIST.size()-1; i > (MEDICINE_LIST.size() - 7); i--) {
                            mList.add(new RecyclerViewItemList(MEDICINE_LIST.get(i), RecyclerViewItemList.ITEM_TYPE));
                        }
                    }

                    mList.add(new RecyclerViewItemList("자주 찾는 천식약", RecyclerViewItemList.HEADER_TYPE));
                    if (MEDICINE_LIST != null){
                        for (int i = 0; i < MEDICINE_LIST.size(); i++) {
                            if (MEDICINE_LIST.get(i).getBookMark() == 1) {
                                mList.add(new RecyclerViewItemList(MEDICINE_LIST.get(i), RecyclerViewItemList.ITEM_TYPE));
                            }
                        }
                    }

                    if (beforeIntent.hasExtra("guest")){
                        adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, mList,MedicineSelectActivity.this,true);
                    }else{
                        adapter = new MedicineSelectAdapter(MedicineSelectActivity.this, mList,MedicineSelectActivity.this);
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(MedicineSelectActivity.this, LinearLayoutManager.VERTICAL, false));
                    recyclerView.addItemDecoration(new RecyclerViewMainStickyItemDecoration());
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        // memo: 2021-01-14 김지훈 수정 시작
        if(beforeIntent.getBooleanExtra("medicineInsert",false)){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("medicineInsert", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            super.onBackPressed();
            return;
        }
        if (newbieCheck) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("newbieBack", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
            return;
        }
        if (beforeIntent.hasExtra("pushNo")){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("newbieBack", true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
            return;
        }

//        Intent i = new Intent(MedicineSelectActivity.this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.putExtra("medicineInsert", true);
//        setResult(RESULT_OK);
//        startActivity(i);

        super.onBackPressed();
        // memo: 2021-01-14 김지훈 수정 종료

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_medicine_select_back: {
                onBackPressed();
                break;
            }
            case R.id.txt_medicine_select_search: {
                Intent i = new Intent(this, MedicineSearchActivity.class);
                startActivity(i);
                break;
            }
            // memo: 2021-01-14 김지훈 수정 시작
                // 약 등록 요청 버튼 클릭 이벤트 추가
            case R.id.btn_medicine_search_request: {
                NetworkCall(MEDICINE_INSERT_APPLICATION);
                break;
            }
            // memo: 2021-01-14 김지훈 수정 종료
        }
    }
}
