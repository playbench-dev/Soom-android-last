package com.kmw.soom2.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.signature.ObjectKey;
import com.kmw.soom2.Common.Item.ForeignKeys;
import com.kmw.soom2.Communitys.Items.CommunityItems;
import com.kmw.soom2.Communitys.Items.CommunityLikeScrapItem;
import com.kmw.soom2.GlobalApplication;
import com.kmw.soom2.Home.HomeAdapter.RecyclerViewAdapter;
import com.kmw.soom2.Home.HomeItem.BannerItem;
import com.kmw.soom2.Home.HomeItem.EtcItem;
import com.kmw.soom2.Home.HomeItem.MedicineTypeItem;
import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;
import com.kmw.soom2.Home.HomeItem.RecyclerViewItemList;
import com.kmw.soom2.Login.Item.UserItem;
import com.kmw.soom2.MyPage.Item.HospitalItem;
import com.kmw.soom2.R;
import com.kmw.soom2.Reports.Item.HistoryItems;
import com.kmw.soom2.Reports.Item.StaticBreathItems;
import com.kmw.soom2.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Utils {

    public static boolean BACKGROUND_CHECK;
    public static SimpleDateFormat formatDD = new SimpleDateFormat("dd일");
    public static SimpleDateFormat formatYYYYMM = new SimpleDateFormat("yyyy년 MM월");
    public static SimpleDateFormat formatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat formatYYYYMMDD2 = new SimpleDateFormat("yyyy년 MM월 dd일");
    public static SimpleDateFormat formatHHMM = new SimpleDateFormat("a hh:mm");
    public static SimpleDateFormat formatHHMMSS = new SimpleDateFormat("HH:mm:ss");
    public static SimpleDateFormat formatHHMM2 = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat formatYYYYMMDDHHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat formatYYYYMMDDAHHMM = new SimpleDateFormat("yyyy-MM-dd a HH:mm");
    public static SimpleDateFormat formatSS = new SimpleDateFormat("ss");
    public static boolean CAMERA_PERMISSION;
    public static boolean WRITE_PERMISSION;
    public static boolean READ_PERMISSION;
    public static boolean AUDIO_PERMISSION;
    public static boolean LOCATION_PERMISSION;
    public static Uri PICK_URI;
    public static String FILE_PATH;
    public static ArrayList<String>                     tagList;
    public static ArrayList<CommunityItems>             itemsArrayList;
    public static ArrayList<CommunityItems>             itemsNoticeArrayList;
    public static ArrayList<CommunityItems>             itemsWriteArrayList;
    public static ArrayList<CommunityItems>             itemsScrabArrayList;
    public static ArrayList<CommunityItems>             itemsCommentArrayList;
    public static ArrayList<CommunityLikeScrapItem>     likeItemArrayList;
    public static ArrayList<CommunityLikeScrapItem>     scrapItemArrayList;
    public static int                                   COMMUNITY_LEFT_PAGEING;
    public static int                                   COMMUNITY_LEFT_SEARCH_TOTAL_PAGE;
    public static int                                   COMMUNITY_RIGHT_PAGEING;
    public static int                                   COMMUNITY_RIGHT_SEARCH_TOTAL_PAGE;
    public static int                                   COMMUNITY_SELECT_TAB_POSITION;
    public static ArrayList<MedicineTakingItem>         MEDICINE_LIST;
    public static ArrayList<MedicineTypeItem>           MEDICINE_TYPE_LIST;

    public static ArrayList<String>                     registerDtList;
    public static ArrayList<ArrayList<String>>          koList;
    public static ArrayList<EtcItem>                    etcItemArrayList;
    public static ArrayList<RecyclerViewItemList>       mList;
    public static ArrayList<HistoryItems>               hisItems;
    public static ArrayList<String>                     AllRegisterDtList;
    public static ArrayList<ArrayList<String>>          AllKoList;
    public static ArrayList<EtcItem>                    AllEtcItemArrayList;
    public static ArrayList<RecyclerViewItemList>       AllList;
    public static int                                   FEED_LIST_LAST_POSITION;
    public static int                                   FEED_ALL_LIST_LAST_POSITION;
    public static int                                   FEED_PAGING;
    public static StaticBreathItems breathItem;
    public static UserItem userItem;
    public static ArrayList<ForeignKeys> linkKeys;
    public static String preferencesName = "preferences";
    public static ArrayList<BannerItem> bannerItemArrayList;
    public static String TOKEN = "";
    public static HospitalItem hospitalItem;

    public static float LAT;
    public static float LNG;
    public static String LOCATION;
    public static String SIDO_NAME;
    public static String GUNGU_NAME;
    public static String STATION_NAME;
    public static int DUST;
    public static int ULTRA_DUST;

    public static int FEED_SHARED_POSITION;

    public static int COMMUNITY_REFRESH = 9999;
    public static int COMMUNITY_WRITE_REFRESH = 9998;
    public static int COMMUNITY_DETAIL_MOVE = 9997;
    public static int NEW_COMMUNITY_SEARCH = 5555;
    public static String COMMUNITY_SELECT_TAG;
    public static String UPDATE_VERSION;

    //memo 약관리
    public static ArrayList<MedicineTakingItem> MedicineTakingItems;
    public static int MEDICINE_SELECT_TAB_POSITION;

    //memo 커뮤니티 분류
    public static ArrayList<String> COMMUNITY_MUNU_NO_LIST;
    public static ArrayList<String> COMMUNITY_LV_LIST;
    public static ArrayList<String> COMMUNITY_NAME_LIST;
    public static ArrayList<String> COMMUNITY_PARENT_LIST;
    public static ArrayList<String> COMMUNITY_HASHTAG_NO;
    public static ArrayList<String> COMMUNITY_HASHTAG_TITLE;
    public static ArrayList<String> COMMUNITY_SELECTABLE;
    public static ArrayList<String> COMMUNITY_POPULARUTY;
    public static ArrayList<BannerItem> COMMUNITY_BLOG_BANNER_LIST = new ArrayList<>();
    public static ArrayList<BannerItem> COMMUNITY_RUTIN_BANNER_LIST = new ArrayList<>();
    public static ArrayList<BannerItem> MEDICINE_BANNER_LIST = new ArrayList<>();
    public static String VERSION = "1.5.8";

    public static void StartActivity(Context context, Class nextActivity) {
        Intent intent = new Intent(context, nextActivity);
        context.startActivity(intent);
    }

    public static class Server {

        public static String server = "https://soomcare.info"; // 원래 서버
        public static String server1 = "http://192.168.0.17:8080"; //명성이 서버

        public static String selectMedicineBannerList(){
            String str = server + "/admin/selectMedicineBannerList.do";
            return str;
        }

        public static String selectNewCommunityList(){
            String str = server + "/admin/selectNewCommunityList.do";
            return str;
        }

        public static String insertNewCommunity(){
            String str = server + "/admin/insertCommunity.do";
            return str;
        }

        public static String updateNewCommunity(){
            String str = server + "/admin/updateCommunity.do";
            return str;
        }

        public static String deleteNewCommunity(){
            String str = server + "/admin/deleteCommunity.do";
            return str;
        }

        public static String selectNewCommentList(){
            String str = server + "/admin/selectNewCommentList.do";
            return str;
        }

        public static String insertNewComment(){
            String str = server + "/admin/insertNewComment.do";
            return str;
        }

        public static String updateNewComment(){
            String str = server + "/admin/updateNewComment.do";
            return str;
        }

        public static String deleteNewComment(){
            String str = server + "/admin/deleteNewComment.do";
            return str;
        }

        public static String insertCommunityViews(){
            String str = server + "/admin/updateViewCount.do";
            return str;
        }

        public static String selectBannerList() {
            String str = server + "/admin/selectBannerList.do";
            return str;
        }

        public static String hashTagList() {
            String str = server + "/admin/selectHashTagList.do";
            return str;
        }

        public static String selectCommunityList() {
            String str = server + "/admin/selectCommunityList.do";
            return str;
        }

        public static String selectNoticeList() {
            String str = server + "/admin/selectNoticeList.do";
            return str;
        }

        public static String deleteCommunityImagesList(){
            String str = server + "/admin/deleteCommunityImagesList.do";
            return str;
        }

        public static String selectMedicineHistoryList() {
            String str = server + "/admin/selectMedicineHistoryList.do";
            return str;
        }

        public static String selectHomeFeedList() {
            String str = server + "/admin/selectHomeFeedHistoryList.do";
            return str;
        }

        public static String selectHomeFeedListByCategory() {
            String str = server + "/admin/selectHomeFeedHistoryListByCategory.do";
            return str;
        }

        public static String updateSymptomHomeFeedHistory() {
            String str = server + "/admin/updateSymptomHomeFeedHistoryList.do";
            return str;
        }

        public static String insertSymptomHomeFeedHistory() {
            String str = server + "/admin/insertSymptomHomeFeedHistoryList.do";
            return str;
        }

        public static String deleteHomeFeedHistoryList() {
            String str = server + "/admin/deleteHomeFeedHistoryList.do";
            return str;
        }

        public static String insertPefHomeFeedHistoryList() {
            String str = server + "/admin/insertPefHomeFeedHistoryList.do";
            return str;
        }

        public static String updatePefHomeFeedHistoryList() {
            String str = server + "/admin/updatePefHomeFeedHistoryList.do";
            return str;
        }

        public static String loginProcess() {
            String str = server + "/loginProcess.do";
            return str;
        }

        public static String overlapUserId() {
            String str = server + "/common/overlapUserIdCheck.do";
            return str;
        }

        public static String overlapUserEmail() {
            String str = server + "/common/overlapUserEmailCheck.do";
            return str;
        }

        public static String overlapUserNickname() {
            String str = server + "/common/overlapUserNicknameCheck.do";
            return str;
        }

        public static String signupUser() {
            String str = server + "/common/signupUser.do";
            return str;
        }

        public static String insertAlarmSetting() {
            String str = server + "/admin/insertAlarmSetting.do";
            return str;
        }

        public static String imageUpload() {
            String str = server + "/admin/upload_img.do?StoreDir=soom2&UploadDir=app&ShowURL=//soomcare.info/img";
            return str;
        }

        public static String insertActHomeFeedHistoryList() {
            String str = server + "/admin/insertActHomeFeedHistoryList.do";
            return str;
        }

        public static String updateActHomeFeedHistoryList() {
            String str = server + "/admin/updateActHomeFeedHistoryList.do";
            return str;
        }

        public static String noticeDetail() {
            String str = server + "/mobile_notice?NOTICE_NO=";
            return str;
        }

        public static String insertMemoHomeFeedHistoryList() {
            String str = server + "/admin/insertMemoHomeFeedHistoryList.do";
            return str;
        }

        public static String updateMemoHomeFeedHistoryList() {
            String str = server + "/admin/updateMemoHomeFeedHistoryList.do";
            return str;
        }

        public static String insertImage() {
            String str = server + "/admin/insertImages.do";
            return str;
        }

        public static String updateImage() {
            String str = server + "/admin/updateImages.do";
            return str;
        }

        public static String selectMentionUserInfo() {
            String str = server + "/admin/selectMentionUserInfo.do";
            return str;
        }

        public static String insertDustHomeFeedHistoryList() {
            String str = server + "/admin/insertFeedHistory.do";
            return str;
        }

        public static String updateDustHomeFeedHistoryList() {
            String str = server + "/admin/updateDustHomeFeedHistoryList.do";
            return str;
        }

        public static String selectHospitalInfo() {
            String str = server + "/admin/selectHospitalList.do";
            return str;
        }

        public static String updateUserInfo() {
            String str = server + "/common/updateUserInfo.do";
            return str;
        }

        public static String logoutProcess() {
            String str = server + "/common/logoutProcess.do";
            return str;
        }

        public static String deleteUserInfo() {
            String str = server + "/common/deleteUserInfo.do";
            return str;
        }

        public static String selectUserInfo() {
            String str = server + "/common/selectUserInfo.do";
            return str;
        }

        public static String selectAlarmInfo() {
            String str = server + "/admin/selectAlarmSetting.do";
            return str;
        }

        public static String updateAlarmInfo() {
            String str = server + "/admin/updateAlarmSetting.do";
            return str;
        }

        public static String selectComMenuList() {
            String str = server + "/admin/selectComMenuList.do";
            return str;
        }

        //USER_NO : 필수
        //ESSENTIAL_PERMISSION_FLAG : 1 이상의 숫자 입력 (1인경우 동의)
        //MARKETING_PERMISSION_FLAG : 1 이상의 숫자 입력 (1인경우 동의)
        public static String updatePermissionInfo() {
            String str = server + "/common/updatePermissionInfo.do";
            return str;
        }

        //USER_NO : 필수
        public static String selectUserPermissionInfo() {
            String str = server + "/common/selectUserPermissionInfo.do";
            return str;
        }

        public static String insertInquery() {
            String str = server + "/admin/insertInquery.do";
            return str;
        }

        public static String insertHospital() {
            String str = server + "/admin/insertHospital.do";
            return str;
        }

        public static String updateHospital() {
            String str = server + "/admin/updateHospital.do";
            return str;
        }

        public static String selectPushAlarmList() {
            String str = server + "/admin/selectPushAlarmList.do";
            return str;
        }

        public static String selectUnreadMessage(){
            String str = server + "/api/selectUnreadMessage.do";
            return str;
        }

        public static String updateAlarmReadFlag(){
            String str = server + "/api/updateAlarmReadFlag.do";
            return str;
        }

        public static String insertAsthmaHistory(){
            String str = server + "/admin/insertAsthmaHistory.do";
            return str;
        }

        public static String updateAsthmaHistory(){
            String str = server + "/admin/updateAsthmaHistory.do";
            return str;
        }

        public static String updateAlarmReadLocalFlag(){
            String str = server + "/api/updateAlarmReadFlagByNo.do";
            return str;
        }

        public static String selectForeignKeyList() {
            String str = server + "/admin/selectForeignLinkList.do";
            return str;
        }

        public static String selectMedicineList() {
            String str = server + "/admin/selectMedicineList.do";
            return str;
        }

        public static String insertCommunity() {
            String str = server + "/admin/insertCommunity.do";
            return str;
        }

        public static String updateCommunity() {
            String str = server + "/admin/updateCommunity.do";
            return str;
        }

        public static String deleteCommunity() {
            String str = server + "/admin/deleteCommunity.do";
            return str;
        }

        public static String selectCommunityLikeList() {
            String str = server + "/admin/selectCommunityLikeList.do";
            return str;
        }

        public static String selectCommunityScrapList() {
            String str = server + "/admin/selectCommunityScrapList.do";
            return str;
        }

        public static String selectCommunityCommentList() {
            String str = server + "/admin/selectCommunityCommentList.do";
            return str;
        }

        public static String selectMyCommunityCommentList() {
            String str = server + "/admin/selectMyCommunityCommentList.do";
            return str;
        }

        public static String selectMyCommunityCommentListCnt() {
            String str = server + "/admin/selectMyCommunityCommentListCnt.do";
            return str;
        }

        public static String insertCommunityComment() {
            String str = server + "/admin/insertCommunityComment.do";
            return str;
        }

        public static String insertCommunityCommentReply() {
            String str = server + "/admin/insertCommunityCommentReply.do";
            return str;
        }

        public static String deleteCommunityComment() {
            String str = server + "/admin/deleteCommunityComment.do";
            return str;
        }

        public static String deleteCommunityCommentReply() {
            String str = server + "/admin/deleteCommunityCommentReply.do";
            return str;
        }

        public static String updateCommunityComment() {
            String str = server + "/admin/updateCommunityComment.do";
            return str;
        }

        public static String updateCommunityCommentReply() {
            String str = server + "/admin/updateCommunityCommentReply.do";
            return str;
        }

        public static String insertCommunityLike() {
            String str = server + "/admin/insertCommunityLike.do";
            return str;
        }

        public static String deleteCommunityLike() {
            String str = server + "/admin/deleteCommunityLike.do";
            return str;
        }

        public static String selectMedicineReviewList() {
            String str = server + "/admin/selectMedicineReviewList.do";
            return str;
        }

        public static String insertMedicineReview() {
            String str = server + "/admin/insertMedicineReview.do";
            return str;
        }

        public static String updateMedicineReview() {
            String str = server + "/admin/updateMedicineReview.do";
            return str;
        }

        public static String deleteMedicineReview() {
            String str = server + "/admin/deleteMedicineReview.do";
            return str;
        }

        public static String insertMedicineHomeFeedHistoryList() {
            String str = server + "/admin/insertMedicineHomeFeedHistoryList.do";
            return str;
        }

        public static String insertMedicineHistory() {
            String str = server + "/admin/insertMedicineHistory.do";
            return str;
        }

        public static String updateMedicineHistory() {
            String str = server + "/admin/updateMedicineHistory.do";
            return str;
        }

        public static String deleteMedicineHistory() {
            String str = server + "/admin/deleteMedicineHistory.do";
            return str;
        }

        public static String selectMedicineType() {
            String str = server + "/admin/selectMedicineType.do";
            return str;
        }

        public static String insertMedicineApplication() {
            String str = server + "/admin/insertMedicineApplication.do";
            return str;
        }

        public static String insertSymptomHistoryList() {
            String str = server + "/admin/insertSymptomHistoryList.do";
            return str;
        }

        public static String deleteHistoryList() {
            String str = server + "/admin/deleteHistoryList.do";
            return str;
        }

        //        public static String findUserId(){
//            String str = server + "/common/findUserId.do";
//            return str;
//        }
        public static String sendUserPasswordToEmail() {
            String str = server + "/admin/findPW.do";
            return str;
        }

        public static String alarmListAllRead() {
            String str = server + "/api/updateAlarmReadFlagByUserNo.do";
            return str;
        }

//        public static String selectCommunityByMenuList() {
//            String str = server + "/admin/selectCommunityByMenuList.do";
//            return str;
//        }

        public static String selectCommunityByMenuList() {
            String str = server + "/admin/selectCommunityPinList.do";
            return str;
        }

        public static String selectCommunityDetail(){
            String str = server + "/admin/selectCommunityByMenuList.do";
            return str;
        }

        //        public static String sendUserPasswordToEmail(){
//            String str = server + "/common/sendUserPasswordToEmail.do";
//            return str;
//        }
        public static String findUserId() {  // 닉네임으로 서치
            String str = server + "/admin/findID.do";
            return str;
        }

        public static String selectCommunityTagList(){
            String str = server + "/admin/selectCommunityTagList.do";
            return str;
        }

        public static String selectCommunityListOfMine(){
            String str = server + "/admin/selectCommunityListOfMine.do";
            return str;
        }

        public static String insertFeedHistoryList(){
            String str = server + "/admin/insertFeedHistoryList.do";
            return str;
        }

        public static String selectCommunityCommentListOfMine(){
            String str = server + "/admin/selectCommunityCommentListOfMine.do";
            return str;
        }

        public static String selectCommunityBlogBannerList(){
            String str = server + "/admin/selectCommunityBlogBannerList.do";
            return str;
        }

        public static String selectRutinBanner(){
            String str = server + "/admin/selectCommnuityBannerList.do";
            return str;
        }

        public static String selectCommunityScrapListOfMine(){
            String str = server + "/admin/selectCommunityScrapListOfMine.do";
            return str;
        }

        public static String selectAsthma(){
            String str = server + "/admin/selectAsthma.do";
            return str;
        }

        public static String selectMedicineHistoryListByDate(){
            String str = server + "/admin/selectMedicineHistoryListByDate.do";
            return str;
        }
    }

    public static String POST(OkHttpClient client, String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static void Expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void Collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void OneBtnPopup(Activity context, String title, String contents, String btnText) {

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.one_btn_popup, null);

        TextView txtTitle = (TextView) layout.findViewById(R.id.txt_one_btn_popup_title);
        TextView txtContents = (TextView) layout.findViewById(R.id.txt_one_btn_popup_contents);
        Button btnOk = (Button) layout.findViewById(R.id.btn_one_btn_popup_ok);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnOk.setText(btnText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dateTimeDialog.setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

    }

    public static Button TwoBtnPopup(Activity context, String title, String contents, String btnLeftText, String btnRightText) {

        final Dialog dateTimeDialog = new Dialog(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.two_btn_popup, null);

        TextView txtTitle = (TextView) layout.findViewById(R.id.txt_two_btn_popup_title);
        TextView txtContents = (TextView) layout.findViewById(R.id.txt_two_btn_popup_contents);
        final Button btnLeft = (Button) layout.findViewById(R.id.btn_two_btn_popup_left);
        Button btnRight = (Button) layout.findViewById(R.id.btn_two_btn_popup_right);

        txtTitle.setText(title);
        txtContents.setText(contents);
        btnLeft.setText(btnLeftText);
        btnRight.setText(btnRightText);

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dateTimeDialog.setCancelable(false);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dateTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        layout.setMinimumWidth((int) (width * 0.88f));
        layout.setMinimumHeight((int) (height * 0.45f));

        dateTimeDialog.setContentView(layout);
        dateTimeDialog.show();

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimeDialog.dismiss();
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return btnRight;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static long calDateBetweenAandB(String startDt, String endDt) {
        String date1 = endDt;
        String date2 = startDt;

        try { // String Type을 Date Type으로 캐스팅하면서 생기는 예외로 인해 여기서 예외처리 해주지 않으면 컴파일러에서 에러가 발생해서 컴파일을 할 수 없다.
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            // date1, date2 두 날짜를 parse()를 통해 Date형으로 변환.
            Date FirstDate = format.parse(date1);
            Date SecondDate = format.parse(date2);

            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            long calDateDays = calDate / (24 * 60 * 60 * 1000);

            calDateDays = Math.abs(calDateDays);

            return calDateDays;
        } catch (ParseException e) {
            // 예외 처리
        }

        return 0;
    }

    public static void setReadMore(final TextView view, final String text, final int maxLine) {
        final Context context = view.getContext();
        final String expanedText = "더보기";

        if (view.getTag() != null && view.getTag().equals(text)) { //Tag로 전값 의 text를 비교하여똑같으면 실행하지 않음.
            return;
        }
        view.setTag(text); //Tag에 text 저장
        view.setText(text); // setText를 미리 하셔야  getLineCount()를 호출가능
        view.post(new Runnable() { //getLineCount()는 UI 백그라운드에서만 가져올수 있음
            @Override
            public void run() {
                if (view.getLineCount() >= maxLine) { //Line Count가 설정한 MaxLine의 값보다 크다면 처리시작

                    int lineEndIndex = view.getLayout().getLineVisibleEnd(maxLine - 1); //Max Line 까지의 text length

                    String[] split = text.split("\n"); //text를 자름
                    int splitLength = 0;

                    String lessText = "";
                    for (String item : split) {
                        splitLength += item.length() + 1;
                        if (splitLength >= lineEndIndex) { //마지막 줄일때!
                            if (item.length() >= expanedText.length()) {
                                lessText += item.substring(0, item.length() - (expanedText.length())) + "\n\n" + expanedText;
                            } else {
                                lessText += item + "\n\n" + expanedText;
                            }
                            break; //종료
                        }
                        lessText += item + "\n";
                    }
                    SpannableString spannableString = new SpannableString(lessText);
                    spannableString.setSpan(new ClickableSpan() {//클릭이벤트
                        @Override
                        public void onClick(View v) {
                            view.setText(text);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) { //컬러 처리
                            ds.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                        }
                    }, spannableString.length() - expanedText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.setText(spannableString);
                    view.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        });
    }

    public static String JsonIsNullCheck(JSONObject object, String value) {
        try {
            if (object.isNull(value)) {
                return "";
            } else {
                return object.getString(value);
            }
        } catch (JSONException e) {

        }
        return null;
    }

    public static int JsonIntIsNullCheck(JSONObject object, String value) {
        try {
            if (object.isNull(value)) {
                return 0;
            } else {
                return object.getInt(value);
            }
        } catch (JSONException e) {

        }
        return 0;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean NetWorkCheckAndPingCheck(Context context){
        boolean check = false;
        if (getNetworkState(context) != null && getNetworkState(context).isConnected()){
            check = true;
        }else{
            Toast.makeText(context, "네트워크 연결이 끊어졌습니다.", Toast.LENGTH_SHORT).show();
        }
        return check;
    }

    public static NetworkInfo getNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo;
    }

    public static void NullCheck(Context context){
        Log.i("USER_INFO_SHARED NULL","1");
        if (userItem == null){
            Log.i("USER_INFO_SHARED NULL","2");
            userItem = new UserItem();
            SharedPreferences pref = context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
            userItem.setUserNo(pref.getInt("userNo",0));
            userItem.setLv(pref.getInt("userLv",0));
            userItem.setId(pref.getString("userId",""));
            userItem.setEmail(pref.getString("userEmail",""));
            userItem.setPassword(pref.getString("userPassword",""));
            userItem.setNickname(pref.getString("userNickname",""));
            userItem.setName(pref.getString("userName",""));
            userItem.setBirth(pref.getInt("userBirth",0));
            userItem.setGender(pref.getInt("userGender",0));
            userItem.setDiagnosisFlag(pref.getInt("userDiagnosisFlag",0));
            userItem.setOutbreakDt(pref.getString("userOutBreadDt",""));
            userItem.setProfileImg(pref.getString("userProfileImg",""));
            userItem.setDeviceCode(pref.getString("userDeviceCode",""));
            userItem.setLoginType(pref.getInt("userLoginType",0));
            userItem.setOsType(pref.getInt("userOsType",0));
            userItem.setCreateDt(pref.getString("userCreateDt",""));
            userItem.setUpdateDt(pref.getString("userUpdateDt",""));
            userItem.setPhone(pref.getString("userPhone",""));
            userItem.setLabelName(pref.getString("userLabelName",""));
            userItem.setLabelColor(pref.getString("userLabelColor",""));
            userItem.setEssentialPermissionFlag(pref.getInt("userEssentialPermissionFlag",0));
            userItem.setMarketingPermissionFlag(pref.getInt("userMarketingPermissionFlag",0));
        }else if (userItem.getUserNo() == 0){
            Log.i("USER_INFO_SHARED NULL","3");
            userItem = new UserItem();
            SharedPreferences pref = context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
            userItem.setUserNo(pref.getInt("userNo",0));
            userItem.setLv(pref.getInt("userLv",0));
            userItem.setId(pref.getString("userId",""));
            userItem.setEmail(pref.getString("userEmail",""));
            userItem.setPassword(pref.getString("userPassword",""));
            userItem.setNickname(pref.getString("userNickname",""));
            userItem.setName(pref.getString("userName",""));
            userItem.setBirth(pref.getInt("userBirth",0));
            userItem.setGender(pref.getInt("userGender",0));
            userItem.setDiagnosisFlag(pref.getInt("userDiagnosisFlag",0));
            userItem.setOutbreakDt(pref.getString("userOutBreadDt",""));
            userItem.setProfileImg(pref.getString("userProfileImg",""));
            userItem.setDeviceCode(pref.getString("userDeviceCode",""));
            userItem.setLoginType(pref.getInt("userLoginType",0));
            userItem.setOsType(pref.getInt("userOsType",0));
            userItem.setCreateDt(pref.getString("userCreateDt",""));
            userItem.setUpdateDt(pref.getString("userUpdateDt",""));
            userItem.setPhone(pref.getString("userPhone",""));
            userItem.setLabelName(pref.getString("userLabelName",""));
            userItem.setLabelColor(pref.getString("userLabelColor",""));
            userItem.setEssentialPermissionFlag(pref.getInt("userEssentialPermissionFlag",0));
            userItem.setMarketingPermissionFlag(pref.getInt("userMarketingPermissionFlag",0));
        }
    }

    public static void CustomToast(Context context, LayoutInflater inflater, String text){
        View toastDesign = inflater.inflate(R.layout.toast_item, null); //toast_design.xml 파일의 toast_design_root 속성을 로드
        TextView txtText = (TextView)toastDesign.findViewById(R.id.txt_toast_text);

        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        txtText.setWidth(display.getWidth());
        txtText.setText(text);

        Toast toast = new Toast(context);

        toast.setGravity(Gravity.BOTTOM, 0, 16); // CENTER를 기준으로 0, 0 위치에 메시지 출력
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(toastDesign);
        toast.show();
    }

    public static void USER_INFO_SHARED(Context context, UserItem userItem){
        Log.i("USER_INFO_SHARED","저장");
        SharedPreferences pref = context.getSharedPreferences(preferencesName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("userNo",IntNullCheck(userItem.getUserNo()));
        editor.putInt("userLv",IntNullCheck(userItem.getLv()));
        editor.putString("userId",StringNullCheck(userItem.getId()));
        editor.putString("userEmail",StringNullCheck(userItem.getEmail()));
        editor.putString("userPassword",StringNullCheck(userItem.getPassword()));
        editor.putString("userNickname",StringNullCheck(userItem.getNickname()));
        editor.putString("userName",StringNullCheck(userItem.getName()));
        editor.putInt("userBirth",IntNullCheck(userItem.getBirth()));
        editor.putInt("userGender",IntNullCheck(userItem.getGender()));
        editor.putInt("userDiagnosisFlag",IntNullCheck(userItem.getDiagnosisFlag()));
        editor.putString("userOutBreadDt",StringNullCheck(userItem.getOutbreakDt()));
        editor.putString("userProfileImg",StringNullCheck(userItem.getProfileImg()));
        editor.putString("userDeviceCode",StringNullCheck(userItem.getDeviceCode()));
        editor.putInt("userLoginType",IntNullCheck(userItem.getLoginType()));
        editor.putInt("userOsType",IntNullCheck(userItem.getOsType()));
        editor.putString("userCreateDt",StringNullCheck(userItem.getCreateDt()));
        editor.putString("userUpdateDt",StringNullCheck(userItem.getUpdateDt()));
        editor.putString("userPhone",StringNullCheck(userItem.getPhone()));
        editor.putString("userLabelName",StringNullCheck(userItem.getLabelName()));
        editor.putString("userLabelColor",StringNullCheck(userItem.getLabelColor()));
        editor.putInt("userEssentialPermissionFlag",IntNullCheck(userItem.getEssentialPermissionFlag()));
        editor.putInt("userMarketingPermissionFlag",IntNullCheck(userItem.getMarketingPermissionFlag()));
        editor.apply();

        NullCheck(context);
    }

    public static String StringNullCheck(String object){
        if (object == null || object.length() == 0){
            return "";
        }
        return object;
    }

    public static int IntNullCheck(int object){
        if (object == 0){
            return 0;
        }
        return object;
    }

}
