package com.kmw.soom2.Common.ServerManagement;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.kmw.soom2.Common.Utils;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Dns;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.kmw.soom2.Common.Utils.NetWorkCheckAndPingCheck;
import static com.kmw.soom2.Common.Utils.userItem;
import static org.apache.http.conn.ssl.SSLSocketFactory.SSL;

public class NetworkUtils {

    public static String mRequestResult = "";

    //로그인
    public static String LOGIN_PROCESS = "LOGIN_PROCESS";
    public static String LOGIN_SNS_PROCESS = "LOGIN_SNS_PROCESS";

    //초기 알람셋팅
    public static String ALARM_FIRST_SETTING = "ALARM_SIGN_UP_SETTING";
    //태그 리스트
    public static String HASH_TAG_LIST = "HASH_TAG_LIST";
    //보고서 -> 증상보고서 -> 원인 링크
    public static String SYMPTOM_MANAGE_LINK = "SYMPTOM_MANAGE_LINK";
    //전체 약 리스트
    public static String MEDICINE_CALL_LIST = "MEDICINE_CALL_LIST";
    public static String MEDICINE_CALL_DETAIL = "MEDICINE_CALL_DETAIL";
    //약 타입 리스트
    public static String MEDICINE_TYPE_CALL_LIST = "MEDICINE_TYPE_CALL_LIST";
    //닉네임 중복체크
    public static String EMAIL_OVERLAP = "EMAIL_OVERLAP";
    //이메일 중복체크
    public static String NICK_NAME_OVERLAP = "NICK_NAME_OVERLAP";
    //아이디 중복체크
    public static String ID_OVERLAP = "ID_OVERLAP";
    //회원가입
    public static String USER_SIGN_UP = "USER_SIGN_UP";
    public static String STAFF_SIGN_UP = "STAFF_SIGN_UP";
    //아이디 찾기
    public static String FIND_USER_ID = "FIND_USER_ID";
    //비밀번호 찾기
    public static String FIND_USER_PW = "FIND_USER_PW";
    //천식검사
    public static String ACT_INSERT = "ACT_INSERT";
    public static String ACT_UPDATE = "ACT_UPDATE";
    //약등록
    public static String MEDICINE_INSERT = "MEDICINE_INSERT";
    public static String MEDICINE_INSERT_APPLICATION = "MEDICINE_INSERT_APPLICATION";
    public static String MEDICINE_UPDATE = "MEDICINE_UPDATE";
    public static String MEDICINE_DELETE = "MEDICINE_DELETE";
    public static String MEDICINE_REVIEW_LIST = "MEDICINE_REVIEW_LIST";
    public static String MEDICINE_REVIEW_LIST_ONRESUME = "MEDICINE_REVIEW_LIST_ONRESUME";
    public static String MEDICINE_HISTORY_LIST = "MEDICINE_HISTORY_LIST";
    public static String MEDICINE_HISTORY_LIST_CHECK = "MEDICINE_HISTORY_LIST_CHECK";
    public static String SELECT_HOME_FEED_LIST = "SELECT_HOME_FEED_LIST";
    public static String SELECT_HOME_FEED_LIST_CHECK = "SELECT_HOME_FEED_LIST_CHECK";
    //리뷰
    public static String REVIEW_INSERT = "REVIEW_INSERT";
    public static String REVIEW_UPDATE = "REVIEW_UPDATE";
    public static String REVIEW_DELETE = "REVIEW_DELETE";
    //폐기능
    public static String PEF_INSERT = "PEF_INSERT";
    public static String PEF_UPDATE = "PEF_UPDATE";
    //홈 피드 리스트
    public static String HOME_FEED_CALL_LIST = "HOME_FEED_CALL_LIST";
    public static String HOME_FEED_UPDATE = "HOME_FEED_UPDATE";
    public static String HOME_FEED_DELETE = "HOME_FEED_DELETE";
    public static String HOME_FEED_ALL_DELETE = "HOME_FEED_ALL_DELETE";
    public static String HOME_FEED_MEDICINE_INSERT = "HOME_FEED_MEDICINE_INSERT";
    //이미지 경로
    public static String IMAGE_INSERT_URL = "IMAGE_INSERT_URL";
    public static String IMAGE_UPDATE_URL = "IMAGE_UPDATE_URL";
    //메모 피드
    public static String MEMO_FEED_INSERT = "MEMO_FEED_INSERT";
    public static String MEMO_FEED_UPDATE = "MEMO_FEED_UPDATE";
    //이용약관
    public static String AGREE_SELECT = "AGREE_SELECT";
    public static String AGREE_UPDATE = "AGREE_UPDATE";
    public static String AGREE_UPDATE_MARKETING = "AGREE_UPDATE_MARKETING";
    //커뮤니티
    public static String COMMUNITY_SELECT_ALL_LIST = "COMMUNITY_SELECT_ALL_LIST";
    public static String COMMUNITY_SELECT_BY_MENU_LIST = "COMMUNITY_SELECT_BY_MENU_LIST";
    public static String COMMUNITY_SELECT_BY_MENU_TAG_LIST = "COMMUNITY_SELECT_BY_MENU_TAG_LIST";
    public static String COMMUNITY_SELECT_BY_MENU_CONTENTS_ALL_LIST = "COMMUNITY_SELECT_BY_MENU_CONTENTS_ALL_LIST";
    public static String COMMUNITY_CALL_LIST = "COMMUNITY_CALL_LIST";
    public static String COMMUNITY_CALL_DETAIL = "COMMUNITY_CALL_DETAIL";
    public static String COMMUNITY_TAG_LIST = "COMMUNITY_TAG_LIST";
    public static String COMMUNITY_TAG_SEARCH_LIST = "COMMUNITY_TAG_SEARCH_LIST";
    //커뮤니티 분류
    public static String COMMUNITY_MENU_LIST = "COMMUNITY_MENU_LIST";
    //좋아요 스크랩 리스트
    public static String LIKE_AND_SCRAP_LIST = "LIKE_AND_SCRAP_LIST";
    //알림 상태
    public static String ALARM_UNREAD_CALL = "ALARM_UNREAD_CALL";

    public static String IMAGE_DELETE_URL = "IMAGE_DELETE_URL";

    public static String COMMUNITY_LIST_MINE_WRITE = "COMMUNITY_LIST_MINE_WRITE";
    public static String COMMUNITY_LIST_MINE_COMMENT = "COMMUNITY_LIST_MINE_COMMENT";
    public static String COMMUNITY_LIST_MINE_SCRAP = "COMMUNITY_LIST_MINE_SCRAP";
    public static String COMMUNITY_LIKE_INSERT = "COMMUNITY_LIKE_INSERT";
    public static String COMMUNITY_LIKE_DELETE = "COMMUNITY_LIKE_DELETE";
    public static String COMMUNITY_SCRAP_INSERT = "COMMUNITY_SCRAP_INSERT";
    public static String COMMUNITY_SCRAP_DELETE = "COMMUNITY_SCRAP_DELETE";
    public static String COMMUNITY_DELETE = "COMMUNITY_DELETE";
    public static String COMMUNITY_COMMENT_LIST = "COMMUNITY_COMMENT_LIST";
    public static String COMMUNITY_COMMENT_INSERT = "COMMUNITY_COMMENT_INSERT";
    public static String COMMUNITY_COMMENT_REPLY_INSERT = "COMMUNITY_COMMENT_REPLY_INSERT";
    public static String COMMUNITY_COMMENT_DELETE = "COMMUNITY_COMMENT_DELETE";
    public static String COMMUNITY_COMMENT_REPLY_DELETE = "COMMUNITY_COMMENT_REPLY_DELETE";
    public static String COMMUNITY_COMMENT_UPDATE = "COMMUNITY_COMMENT_UPDATE";
    public static String COMMUNITY_COMMENT_REPLY_UPDATE = "COMMUNITY_COMMENT_REPLY_UPDATE";

    public static String ALARM_READ = "ALARM_READ";
    public static String PUSH_READ = "PUSH_READ";

    public static String FEED_INSERT_MEDICINE = "FEED_INSERT_MEDICINE";
    public static String FEED_UPDATE_MEDICINE = "FEED_UPDATE_MEDICINE";

    public static String ASTHMA_INSERT = "ASTHMA_INSERT";
    public static String ASTHMA_UPDATE = "ASTHMA_UPDATE";
    public static String ASTHMA_CALL_LIST = "ASTHMA_CALL_LIST";

    public static String NOTICE_DETAIL_CALL = "NOTICE_DETAIL_CALL";

    public static String NEW_SYMPTOM_LIST_INSERT = "NEW_SYMPTOM_LIST_INSERT";
    public static String NEW_SYMPTOM_LIST_UPDATE = "NEW_SYMPTOM_LIST_UPDATE";
    public static String NEW_SYMPTOM_LIST_DELETE = "NEW_SYMPTOM_LIST_DELETE";

    public static String ALARM_LIST_ALL_READ = "ALARM_LIST_ALL_READ";
    public static String MEDICINE_LIST_BY_DATE_PAST = "MEDICINE_LIST_BY_DATE_PAST";
    public static String MEDICINE_LIST_BY_DATE_CURRENT = "MEDICINE_LIST_BY_DATE_CURRENT";

    public static String NEW_COMMUNITY_LIST = "NEW_COMMUNITY_LIST";
    public static String NEW_COMMUNITY_DETAIL = "NEW_COMMUNITY_DETAIL";
    public static String NEW_COMMUNITY_TEXT_LIST = "NEW_COMMUNITY_TEXT_LIST";
    public static String NEW_COMMUNITY_INSERT = "NEW_COMMUNITY_INSERT";
    public static String NEW_COMMUNITY_UPDATE = "NEW_COMMUNITY_UPDATE";
    public static String NEW_COMMUNITY_DELETE = "NEW_COMMUNITY_DELETE";
    public static String NEW_COMMENT_LIST = "NEW_COMMENT_LIST";
    public static String NEW_COMMENT_INSERT = "NEW_COMMENT_INSERT";
    public static String NEW_COMMENT_REPLY_INSERT = "NEW_COMMENT_REPLY_INSERT";
    public static String NEW_COMMENT_UPDATE = "NEW_COMMENT_UPDATE";
    public static String NEW_COMMENT_REPLY_UPDATE = "NEW_COMMENT_REPLY_UPDATE";

    public static String NEW_COMMENT_DELETE = "NEW_COMMENT_DELETE";
    public static String NEW_COMMENT_REPLY_DELETE = "NEW_COMMENT_REPLY_DELETE";
    public static String NEW_COMMUNITY_VIEWS_UP = "NEW_COMMUNITY_VIEWS_UP";

    public static String BLOG_BANNER = "BLOG_BANNER";
    public static String RUTIN_BANNER = "RUTIN_BANNER";
    public static String MEDICINE_BANNER = "MEDICINE_BANNER";

    public static class NetworkCall extends AsyncTask<String, String, String> {

        public AsyncResponse delegate = null;
        String code;
        String mUrl;
        Context context;
        private String TAG;

        public NetworkCall(Context context, AsyncResponse delegate, String TAG, String code) {
            this.code = code;
            this.context = context;
            this.TAG = TAG;
            this.delegate = delegate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //memo 네트워크 체크 후 진행, 미연결시 요청종료
            if (!NetWorkCheckAndPingCheck(context)) {
                this.onCancelled();
            }
        }

        @Override
        protected String doInBackground(String... strings) {

            FormBody.Builder body = null;

            if (code.equals(LOGIN_PROCESS)) {
                mUrl = Utils.Server.loginProcess();
                body = new FormBody.Builder()
                        .add("ID", strings[0])
                        .add("PASSWORD", strings[1])
                        .add("DEVICE_CODE", strings[2])
                        .add("OS_TYPE", "1");
            } else if (code.equals(LOGIN_SNS_PROCESS)) {
                Log.i(TAG, "Network 1111 1");
                mUrl = Utils.Server.loginProcess();
                body = new FormBody.Builder()
                        .add("ID", strings[0])
                        .add("DEVICE_CODE", strings[1])
                        .add("OS_TYPE", "1");
            } else if (code.equals(HASH_TAG_LIST)) {
                mUrl = Utils.Server.hashTagList();
                body = new FormBody.Builder();
            } else if (code.equals(SYMPTOM_MANAGE_LINK)) {
                Log.i(TAG, "Network 3333");
                mUrl = Utils.Server.selectForeignKeyList();
                body = new FormBody.Builder();
            } else if (code.equals(MEDICINE_CALL_LIST)) {
                mUrl = Utils.Server.selectMedicineList();
                body = new FormBody.Builder();
            }else if (code.equals(MEDICINE_CALL_DETAIL)) {
                mUrl = Utils.Server.selectMedicineList();
                body = new FormBody.Builder()
                        .add("MEDICINE_NO", strings[0]);
            } else if (code.equals(MEDICINE_TYPE_CALL_LIST)) {
                Log.i(TAG, "Network 5555");
                mUrl = Utils.Server.selectMedicineType();
                body = new FormBody.Builder();
            } else if (code.equals(EMAIL_OVERLAP)) {
                mUrl = Utils.Server.overlapUserEmail();
                body = new FormBody.Builder()
                        .add("EMAIL", strings[0]);
            } else if (code.equals(NICK_NAME_OVERLAP)) {
                mUrl = Utils.Server.overlapUserNickname();
                body = new FormBody.Builder()
                        .add("NICKNAME", strings[0]);
            } else if (code.equals(ID_OVERLAP)) {
                mUrl = Utils.Server.overlapUserId();
                body = new FormBody.Builder()
                        .add("ID", strings[0]);
            } else if (code.equals(USER_SIGN_UP)) {
                mUrl = Utils.Server.signupUser();
                body = new FormBody.Builder()
                        .add("EMAIL", strings[0])
                        .add("ID", strings[1])
                        .add("NICKNAME", strings[2])
                        .add("GENDER", strings[3])
                        .add("LV", strings[4])
                        .add("BIRTH", strings[5])
                        .add("DIAGNOSIS_FLAG", strings[6])
                        .add("LOGIN_TYPE", strings[7])
                        .add("DEVICE_CODE", strings[8])
                        .add("NAME", strings[9])
                        .add("OS_TYPE", "1")
                        .add("OUTBREAK_DT", strings[10]);
                if (strings[7].equals("1")) {
                    if (strings[11].length() != 0) {
                        body.add("PASSWORD", strings[11]);
                    }
                }
            } else if (code.equals(STAFF_SIGN_UP)) {
                mUrl = Utils.Server.signupUser();
                body = new FormBody.Builder()
                        .add("EMAIL", strings[0])
                        .add("ID", strings[1])
                        .add("NICKNAME", strings[2])
                        .add("GENDER", strings[3])
                        .add("BIRTH", strings[4])
                        .add("LOGIN_TYPE", strings[5])
                        .add("DEVICE_CODE", strings[6])
                        .add("NAME", strings[7])
                        .add("OS_TYPE", "1")
                        .add("LV", "60")
                        .add("ALIVE_FLAG", "1");
                if (strings[5].equals("1")) {
                    if (strings[8].length() != 0) {
                        body.add("PASSWORD", strings[8]);
                    }
                }
            } else if (code.equals(ALARM_FIRST_SETTING)) {
                mUrl = Utils.Server.insertAlarmSetting();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("SYMPTOM_FLAG", "1")
                        .add("DOSING_FLAG", "1")
                        .add("NOTICE_FLAG", "1")
                        .add("COMMUNITY_COMMENT_FLAG", "1")
                        .add("COMMUNITY_COMMENT_REPLY_FLAG", "1")
                        .add("COMMUNITY_LIKE_FLAG", "1");
            } else if (code.equals(FIND_USER_ID)) {
                mUrl = Utils.Server.findUserId();
                body = new FormBody.Builder()
                        .add("NICKNAME", strings[0]);
            } else if (code.equals(FIND_USER_PW)) {
                mUrl = Utils.Server.sendUserPasswordToEmail();
                body = new FormBody.Builder()
                        .add("NICKNAME", strings[0])
                        .add("EMAIL", strings[1]);
            } else if (code.equals(ACT_INSERT)) {
                mUrl = Utils.Server.insertActHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("REGISTER_DT", strings[1])
                        .add("ACT_SCORE", strings[2])
                        .add("ACT_TYPE", strings[3])
                        .add("ACT_STATE", strings[4])
                        .add("ACT_SELECTED", strings[5])
                        .add("NICKNAME", strings[6])
                        .add("GENDER", strings[7])
                        .add("BIRTH", strings[8])
                        .add("CATEGORY", "21");
            } else if (code.equals(ACT_UPDATE)) {
                mUrl = Utils.Server.updateActHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_HISTORY_NO", strings[0])
                        .add("REGISTER_DT", strings[1])
                        .add("ACT_SCORE", strings[2])
                        .add("ACT_TYPE", strings[3])
                        .add("ACT_STATE", strings[4])
                        .add("ACT_SELECTED", strings[5])
                        .add("CATEGORY", "21");
            } else if (code.equals(HOME_FEED_DELETE)) {
                mUrl = Utils.Server.deleteHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_HISTORY_NO", strings[0]);
            } else if (code.equals(HOME_FEED_UPDATE)) {
                mUrl = Utils.Server.deleteHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_HISTORY_NO", strings[0]);
            } else if (code.equals(HOME_FEED_ALL_DELETE)) {
                mUrl = Utils.Server.deleteHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_HISTORY_NO", strings[0]);
            } else if (code.equals(MEDICINE_INSERT)) {
                mUrl = Utils.Server.insertMedicineHistory();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("MEDICINE_NO", strings[1])
                        .add("FREQUENCY", strings[2])
                        .add("VOLUME", strings[3])
                        .add("UNIT", strings[4])
                        .add("START_DT", strings[5])
                        .add("END_DT", strings[6]);
            } else if (code.equals(MEDICINE_INSERT_APPLICATION)) {
                mUrl = Utils.Server.insertMedicineApplication();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("MEDICINE_NAME", strings[1])
                        .add("STATUS", "1");
            } else if (code.equals(MEDICINE_UPDATE)) {
                mUrl = Utils.Server.updateMedicineHistory();
                body = new FormBody.Builder()
                        .add("MEDICINE_HISTORY_NO", strings[0])
                        .add("MEDICINE_NO", strings[1])
                        .add("FREQUENCY", strings[2])
                        .add("VOLUME", strings[3])
                        .add("UNIT", strings[4])
                        .add("START_DT", strings[5])
                        .add("END_DT", strings[6]);
            } else if (code.equals(MEDICINE_DELETE)) {
                mUrl = Utils.Server.deleteMedicineHistory();
                body = new FormBody.Builder()
                        .add("MEDICINE_HISTORY_NO", strings[0])
                        .add("USER_NO", strings[1]);
            } else if (code.equals(MEDICINE_REVIEW_LIST)) {
                mUrl = Utils.Server.selectMedicineReviewList();
                body = new FormBody.Builder()
                        .add("MEDICINE_NO", strings[0]);
            }else if (code.equals(MEDICINE_REVIEW_LIST_ONRESUME)){
                mUrl = Utils.Server.selectMedicineReviewList();
                body = new FormBody.Builder()
                        .add("MEDICINE_NO", strings[0]);
            }else if (code.equals(MEDICINE_HISTORY_LIST)) {
                mUrl = Utils.Server.selectMedicineHistoryList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0]);
            } else if (code.equals(MEDICINE_HISTORY_LIST_CHECK)) {
                mUrl = Utils.Server.selectMedicineHistoryList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0]);
            } else if (code.equals(REVIEW_INSERT)) {
                mUrl = Utils.Server.insertMedicineReview();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("MEDICINE_NO", strings[1])
                        .add("CONTENTS", strings[2])
                        .add("DOSE_PERIOD", strings[3])
                        .add("SIDE_EFFECT_FLAG", strings[4]);
            } else if (code.equals(REVIEW_UPDATE)) {
                mUrl = Utils.Server.updateMedicineReview();
                body = new FormBody.Builder()
                        .add("REVIEW_NO", strings[0])
                        .add("MEDICINE_NO", strings[1])
                        .add("CONTENTS", strings[2])
                        .add("DOSE_PERIOD", strings[3])
                        .add("SIDE_EFFECT_FLAG", strings[4]);
            } else if (code.equals(REVIEW_DELETE)) {
                mUrl = Utils.Server.updateMedicineReview();
                body = new FormBody.Builder()
                        .add("REVIEW_NO", strings[0])
                        .add("MEDICINE_NO", strings[1])
                        .add("ALIVE_FLAG", "0");
            } else if (code.equals(PEF_INSERT)) {
                mUrl = Utils.Server.insertPefHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("REGISTER_DT", strings[1])
                        .add("PEF_SCORE", strings[2])
                        .add("INSPIRATOR_FLAG", strings[3])
                        .add("NICKNAME", strings[4])
                        .add("GENDER", strings[5])
                        .add("BIRTH", strings[6])
                        .add("CATEGORY", "22");
            } else if (code.equals(PEF_UPDATE)) {
                mUrl = Utils.Server.updatePefHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_HISTORY_NO", strings[0])
                        .add("REGISTER_DT", strings[1])
                        .add("PEF_SCORE", strings[2])
                        .add("INSPIRATOR_FLAG", strings[3])
                        .add("CATEGORY", "22");
            } else if (code.equals(HOME_FEED_CALL_LIST)) {
                mUrl = Utils.Server.selectHomeFeedList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("CATEGORY", strings[1])
                        .add("YEAR", strings[2])
                        .add("MONTH", strings[3]);
            } else if (code.equals(HOME_FEED_MEDICINE_INSERT)) {
                mUrl = Utils.Server.insertMedicineHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("MEDICINE_NO", strings[1])
                        .add("FREQUENCY", strings[2])
                        .add("VOLUME", strings[3])
                        .add("UNIT", strings[4])
                        .add("EMERGENCY_FLAG", strings[5])
                        .add("START_DT", strings[6])
                        .add("END_DT", strings[7])
                        .add("KO", strings[8])
                        .add("NICKNAME", strings[9])
                        .add("GENDER", strings[10])
                        .add("BIRTH", strings[11])
                        .add("REGISTER_DT", strings[12])
                        .add("CATEGORY", "1");
            } else if (code.equals(IMAGE_INSERT_URL)) {
                mUrl = Utils.Server.insertImage();
                body = new FormBody.Builder()
                        .add("WRITING_NO", strings[0])
                        .add("CATEGORY", strings[1])
                        .add("IMAGE_FILE", strings[2])
                        .add("IMAGE_ORDER_NO", "1");
            } else if (code.equals(IMAGE_UPDATE_URL)) {
                mUrl = Utils.Server.updateImage();
                body = new FormBody.Builder()
                        .add("WRITING_NO", strings[0])
                        .add("CATEGORY", strings[1])
                        .add("IMAGE_FILE", strings[2]);
            } else if (code.equals(MEMO_FEED_INSERT)) {
                mUrl = Utils.Server.insertMemoHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("MEMO", strings[1])
                        .add("NICKNAME", strings[2])
                        .add("GENDER", strings[3])
                        .add("BIRTH", strings[4])
                        .add("REGISTER_DT", strings[5])
                        .add("CATEGORY", "30");
            } else if (code.equals(MEMO_FEED_UPDATE)) {
                mUrl = Utils.Server.updateMemoHomeFeedHistoryList();
                body = new FormBody.Builder()
                        .add("USER_HISTORY_NO", strings[0])
                        .add("MEMO", strings[1])
                        .add("REGISTER_DT", strings[2])
                        .add("CATEGORY", "30");
            } else if (code.equals(AGREE_SELECT)) {
                mUrl = Utils.Server.selectUserPermissionInfo();
                Log.i(TAG, "agree select");
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0]);
            } else if (code.equals(AGREE_UPDATE)) {
                mUrl = Utils.Server.updatePermissionInfo();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("PROFILE_IMG", userItem.getProfileImg())
                        .add("ESSENTIAL_PERMISSION_FLAG", strings[1])
                        .add("MARKETING_PERMISSION_FLAG", strings[2]);
            } else if (code.equals(COMMUNITY_CALL_LIST)) {
                mUrl = Utils.Server.selectCommunityList();
                body = new FormBody.Builder()
                        .add("Search_ShowCNT", "15")
                        .add("Search_Page", strings[0]);
            } else if (code.equals(COMMUNITY_MENU_LIST)) {
                mUrl = Utils.Server.selectComMenuList();
                body = new FormBody.Builder();
            } else if (code.equals(LIKE_AND_SCRAP_LIST)) {
                mUrl = Utils.Server.selectCommunityLikeList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0]);
            } else if (code.equals(ALARM_UNREAD_CALL)) {
                mUrl = Utils.Server.selectUnreadMessage();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0]);
            } else if (code.equals(COMMUNITY_SELECT_ALL_LIST)) {
                mUrl = Utils.Server.selectCommunityByMenuList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("Search_Page", strings[1])
                        .add("Search_ShowCNT", strings[2]);
            } else if (code.equals(COMMUNITY_SELECT_BY_MENU_LIST)) {
                mUrl = Utils.Server.selectCommunityByMenuList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("C_MENU_NO", strings[1])
                        .add("Search_Page", strings[2])
                        .add("Search_ShowCNT", strings[3]);
            } else if (code.equals(COMMUNITY_SELECT_BY_MENU_TAG_LIST)) {
                mUrl = Utils.Server.selectCommunityByMenuList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("HASHTAG", strings[1])
                        .add("Search_Page", strings[2])
                        .add("Search_ShowCNT", strings[3]);
            } else if (code.equals(COMMUNITY_SELECT_BY_MENU_CONTENTS_ALL_LIST)) {
                mUrl = Utils.Server.selectCommunityByMenuList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("MENU_NO", strings[1])
                        .add("Search_Page", strings[2])
                        .add("Search_ShowCNT", strings[3]);
            } else if (code.equals(COMMUNITY_CALL_DETAIL)) {
                mUrl = Utils.Server.selectCommunityDetail();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMUNITY_NO", strings[1]);
            } else if (code.equals(COMMUNITY_TAG_LIST)) {
                mUrl = Utils.Server.selectCommunityTagList();
                body = new FormBody.Builder();
            } else if (code.equals(IMAGE_DELETE_URL)) {
                mUrl = Utils.Server.deleteCommunityImagesList();
                body = new FormBody.Builder().add("IMAGES_NO", strings[0]);
            } else if (code.equals(COMMUNITY_LIST_MINE_WRITE)) {
                mUrl = Utils.Server.selectCommunityListOfMine();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("Search_Page", strings[1])
                        .add("Search_ShowCnt", strings[2]);
            } else if (code.equals(COMMUNITY_LIST_MINE_COMMENT)) {
                mUrl = Utils.Server.selectCommunityCommentListOfMine();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("Search_Page", strings[1])
                        .add("Search_ShowCnt", strings[2]);
            } else if (code.equals(COMMUNITY_LIST_MINE_SCRAP)) {
                mUrl = Utils.Server.selectCommunityScrapListOfMine();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("Search_Page", strings[1])
                        .add("Search_ShowCnt", strings[2])
                        .add("FLAG", "2");
            } else if (code.equals(FEED_INSERT_MEDICINE)) {
                mUrl = Utils.Server.insertFeedHistoryList();
                body = new FormBody.Builder()
                        .add("MEDICINE_LIST", strings[0]);
            } else if (code.equals(COMMUNITY_LIKE_INSERT)) {
                mUrl = Utils.Server.insertCommunityLike();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMUNITY_NO", strings[1])
                        .add("FLAG", "1");
            } else if (code.equals(COMMUNITY_LIKE_DELETE)) {
                mUrl = Utils.Server.deleteCommunityLike();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMUNITY_NO", strings[1])
                        .add("FLAG", "1");
            } else if (code.equals(COMMUNITY_SCRAP_INSERT)) {
                mUrl = Utils.Server.insertCommunityLike();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMUNITY_NO", strings[1])
                        .add("FLAG", "2");
            } else if (code.equals(COMMUNITY_SCRAP_DELETE)) {
                mUrl = Utils.Server.deleteCommunityLike();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMUNITY_NO", strings[1])
                        .add("FLAG", "2");
            } else if (code.equals(COMMUNITY_DELETE)) {
                mUrl = Utils.Server.deleteCommunity();
                body = new FormBody.Builder()
                        .add("COMMUNITY_NO", strings[0]);
            } else if (code.equals(COMMUNITY_COMMENT_LIST)) {
                mUrl = Utils.Server.selectCommunityCommentList();
                body = new FormBody.Builder()
                        .add("COMMUNITY_NO", strings[0]);
            } else if (code.equals(COMMUNITY_COMMENT_INSERT)) {
                mUrl = Utils.Server.insertCommunityComment();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMUNITY_NO", strings[1])
                        .add("COMMENT", strings[2])
                        .add("MENTION", strings[3]);
            } else if (code.equals(COMMUNITY_COMMENT_REPLY_INSERT)) {
                mUrl = Utils.Server.insertCommunityCommentReply();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMENT_NO", strings[1])
                        .add("COMMENT", strings[2])
                        .add("MENTION", strings[3])
                        .add("COMMUNITY_NO",strings[4]);
            } else if (code.equals(COMMUNITY_COMMENT_DELETE)) {
                mUrl = Utils.Server.deleteCommunityComment();
                body = new FormBody.Builder()
                        .add("COMMENT_NO", strings[0]);
            } else if (code.equals(COMMUNITY_COMMENT_REPLY_DELETE)) {
                mUrl = Utils.Server.deleteCommunityCommentReply();
                body = new FormBody.Builder()
                        .add("COMMENT_REPLY_NO", strings[0]);
            } else if (code.equals(COMMUNITY_COMMENT_UPDATE)) {
                mUrl = Utils.Server.updateCommunityComment();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMENT_NO", strings[1])
                        .add("COMMENT", strings[2])
                        .add("MENTION", strings[3]);
            } else if (code.equals(COMMUNITY_COMMENT_REPLY_UPDATE)) {
                mUrl = Utils.Server.updateCommunityCommentReply();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMENT_REPLY_NO", strings[1])
                        .add("COMMENT", strings[2])
                        .add("MENTION", strings[3]);
            } else if (code.equals(ALARM_READ)) {
                mUrl = Utils.Server.updateAlarmReadFlag();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("KEY_NO", strings[1])
                        .add("CHANNEL_ID", strings[2]);
            } else if (code.equals(PUSH_READ)) {
                mUrl = Utils.Server.updateAlarmReadLocalFlag();
                body = new FormBody.Builder()
                        .add("PUSH_NO", strings[0]);
            } else if (code.equals(ASTHMA_INSERT)) {
                mUrl = Utils.Server.insertAsthmaHistory();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("REGISTER_DT", strings[1])
                        .add("LOCATION", strings[2])
                        .add("ASTHMA_SCORE", strings[3])
                        .add("NICKNAME", strings[4])
                        .add("GENDER", strings[5])
                        .add("BIRTH", strings[6])
                        .add("CATEGORY", "24");
            } else if (code.equals(ASTHMA_UPDATE)) {
                mUrl = Utils.Server.updateAsthmaHistory();
                body = new FormBody.Builder()
                        .add("USER_HISTORY_NO", strings[0])
                        .add("REGISTER_DT", strings[1])
                        .add("LOCATION", strings[2])
                        .add("ASTHMA_SCORE", strings[3])
                        .add("NICKNAME", strings[4])
                        .add("GENDER", strings[5])
                        .add("BIRTH", strings[6])
                        .add("CATEGORY", "24");
            } else if (code.equals(NOTICE_DETAIL_CALL)) {
                mUrl = Utils.Server.selectNoticeList();
                body = new FormBody.Builder()
                        .add("NOTICE_NO", strings[0]);
            } else if (code.equals(NEW_SYMPTOM_LIST_INSERT)) {
                mUrl = Utils.Server.insertSymptomHistoryList();
                body = new FormBody.Builder()
                        .add("FEED_LIST", strings[0]);
            } else if (code.equals(NEW_SYMPTOM_LIST_UPDATE)) {
                mUrl = Utils.Server.deleteHistoryList();
                body = new FormBody.Builder()
                        .add("HISTORY_NOS", strings[0]);
            } else if (code.equals(NEW_SYMPTOM_LIST_DELETE)) {
                mUrl = Utils.Server.deleteHistoryList();
                body = new FormBody.Builder()
                        .add("HISTORY_NOS", strings[0]);
            } else if (code.equals(SELECT_HOME_FEED_LIST)) {
                mUrl = Utils.Server.selectHomeFeedList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("YEAR", strings[1])
                        .add("MONTH", strings[2]);
            } else if (code.equals(SELECT_HOME_FEED_LIST_CHECK)) {
                mUrl = Utils.Server.selectHomeFeedList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("YEAR", strings[1])
                        .add("MONTH", strings[2]);
            } else if (code.equals(FEED_UPDATE_MEDICINE)) {
                mUrl = Utils.Server.deleteHistoryList();
                body = new FormBody.Builder()
                        .add("HISTORY_NOS", strings[0]);
            } else if (code.equals(ASTHMA_CALL_LIST)) {
                mUrl = Utils.Server.selectAsthma();
                body = new FormBody.Builder()
                        .add("areaNo", strings[0])
                        .add("date", strings[1]);
            } else if (code.equals(COMMUNITY_TAG_SEARCH_LIST)) {
                mUrl = Utils.Server.selectCommunityByMenuList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("Search_ShowCNT", "15")
                        .add("Search_Page", strings[1])
                        .add("SEARCH_TEXT", strings[2]);
            } else if (code.equals(ALARM_LIST_ALL_READ)) {
                mUrl = Utils.Server.alarmListAllRead();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0]);
            }else if (code.equals(MEDICINE_LIST_BY_DATE_PAST)){
                mUrl = Utils.Server.selectMedicineHistoryListByDate();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                .add("END_DT_PAST",strings[1])
                .add("Search_Page",strings[2])
                .add("Search_ShowCNT",strings[3]);
            }else if (code.equals(MEDICINE_LIST_BY_DATE_CURRENT)){
                mUrl = Utils.Server.selectMedicineHistoryListByDate();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("END_DT_CURRENT",strings[1])
                        .add("Search_Page",strings[2])
                        .add("Search_ShowCNT",strings[3]);
            }else if (code.equals(NEW_COMMUNITY_LIST)){
                mUrl = Utils.Server.selectNewCommunityList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("Search_Page",strings[1])
                        .add("Search_ShowCNT",strings[2])
                        .add("ORDER_BY",strings[3])
                        .add(strings[5].equals("all") ? "MENU_NO" : "C_MENU_NO",strings[4]);
            }else if (code.equals(NEW_COMMUNITY_DETAIL)){
                mUrl = Utils.Server.selectNewCommunityList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("COMMUNITY_NO",strings[1]);
            }else if (code.equals(NEW_COMMUNITY_TEXT_LIST)){
                mUrl = Utils.Server.selectNewCommunityList();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("Search_Page",strings[1])
                        .add("Search_ShowCNT",strings[2])
                        .add("MENU_NO",strings[3])
                        .add("SEARCH_TEXT",strings[4])
                        .add("ORDER_BY","1");
            }else if (code.equals(NEW_COMMUNITY_INSERT)){
                mUrl = Utils.Server.insertNewCommunity();
                body = new FormBody.Builder()
                        .add("USER_NO", strings[0])
                        .add("CONTENTS",strings[1])
                        .add("HASHTAG",strings[2])
                        .add("MENU_NO",strings[3])
                        .add("C_MENU_NO",strings[4]);
            }else if (code.equals(NEW_COMMUNITY_UPDATE)){
                mUrl = Utils.Server.updateNewCommunity();
                body = new FormBody.Builder()
                        .add("COMMUNITY_NO", strings[0])
                        .add("CONTENTS",strings[1])
                        .add("HASHTAG",strings[2])
                        .add("MENU_NO",strings[3])
                        .add("C_MENU_NO",strings[4]);
            }else if (code.equals(NEW_COMMUNITY_DELETE)){
                mUrl = Utils.Server.deleteNewCommunity();
                body = new FormBody.Builder()
                        .add("COMMUNITY_NO", strings[0]);
            }else if (code.equals(NEW_COMMENT_LIST)){
                mUrl = Utils.Server.selectNewCommentList();
                body = new FormBody.Builder()
                        .add("COMMUNITY_NO", strings[0]);
            }else if (code.equals(NEW_COMMENT_INSERT)){
                mUrl = Utils.Server.insertNewComment();
                body = new FormBody.Builder()
                        .add("COMMENT", strings[0])
                        .add("CLASS", "1")
                        .add("USER_NO", strings[1])
                        .add("COMMUNITY_NO", strings[2])
                        .add("MENTION", strings[3]);
            }else if (code.equals(NEW_COMMENT_REPLY_INSERT)){
                mUrl = Utils.Server.insertNewComment();
                body = new FormBody.Builder()
                        .add("COMMENT", strings[0])
                        .add("CLASS", "2")
                        .add("USER_NO", strings[1])
                        .add("COMMUNITY_NO", strings[2])
                        .add("MENTION", strings[3])
                        .add("COMMENT_NO", strings[4])
                        .add("GROUP_NUM", strings[5])
                        .add("ORIGIN_COMMENT_NO", strings[6]);
            }else if (code.equals(NEW_COMMENT_UPDATE)){
                mUrl = Utils.Server.updateNewComment();
                body = new FormBody.Builder()
                        .add("COMMENT", strings[0])
                        .add("COMMENT_NO", strings[1])
                        .add("MENTION", strings[2])
                        .add("ORIGIN_COMMENT_NO", strings[3])
                        .add("CLASS", "1");
            }else if (code.equals(NEW_COMMENT_REPLY_UPDATE)){
                mUrl = Utils.Server.updateNewComment();
                body = new FormBody.Builder()
                        .add("COMMENT", strings[0])
                        .add("COMMENT_NO", strings[1])
                        .add("MENTION", strings[2])
                        .add("ORIGIN_COMMENT_NO", strings[3])
                        .add("ORIGIN_RE_COMMENT_NO", strings[4])
                        .add("CLASS", "2");
            }else if (code.equals(NEW_COMMENT_DELETE)){
                mUrl = Utils.Server.deleteNewComment();
                body = new FormBody.Builder()
                        .add("COMMENT_NO", strings[0])
                        .add("ORIGIN_COMMENT_NO", strings[1])
                        .add("CLASS", "1");
            }
            else if (code.equals(NEW_COMMENT_REPLY_DELETE)){
                mUrl = Utils.Server.deleteNewComment();
                body = new FormBody.Builder()
                        .add("COMMENT_NO", strings[0])
                        .add("ORIGIN_COMMENT_NO", strings[1])
                        .add("ORIGIN_RE_COMMENT_NO", strings[2])
                        .add("CLASS", "2");
            }else if (code.equals(NEW_COMMUNITY_VIEWS_UP)){
                mUrl = Utils.Server.insertCommunityViews();
                body = new FormBody.Builder()
                        .add("COMMUNITY_NO", strings[0]);
            }else if (code.equals(BLOG_BANNER)){
                mUrl = Utils.Server.selectCommunityBlogBannerList();
                body = new FormBody.Builder();
            }else if (code.equals(RUTIN_BANNER)){
                mUrl = Utils.Server.selectRutinBanner();
                body = new FormBody.Builder();
            }else if (code.equals(MEDICINE_BANNER)){
                mUrl = Utils.Server.selectMedicineBannerList();
                body = new FormBody.Builder();
            }

//            ConnectionPool connectionPool = new ConnectionPool();

            OkHttpClient client = new OkHttpClient();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.dns(Dns.SYSTEM);

            try {
                mRequestResult = POST(client,builder,mUrl,body.build(),code);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return mRequestResult;

        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                if (NetWorkCheckAndPingCheck(context)) {
                    delegate.ProcessFinish(code, s);
                } else {
                    this.onCancelled();
                }
            } else {
                Toast.makeText(context, "서버와 통신이 불안전합니다. 잠시후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String POST(OkHttpClient client, OkHttpClient.Builder builder, String url, RequestBody body, String code) throws IOException {

        final SSLContext sslContext;
        javax.net.ssl.SSLSocketFactory sslSocketFactory = null;
        try {
            sslContext = SSLContext.getInstance(SSL);
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        String result = response.body().string().trim();

        response.close();

        return result;

    }

    final static TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) throws
                        CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                               String authType) throws
                        CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }
    };

    public static class ApiDns implements Dns {
        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            if (hostname == null) {
                throw new UnknownHostException("hostname == null");
            } else {
                try {
                    List<InetAddress> mInetAddressesList = new ArrayList<>();
                    InetAddress[] mInetAddresses = InetAddress.getAllByName(hostname);
                    for (InetAddress address : mInetAddresses) {
                        if (address instanceof Inet4Address) {
                            mInetAddressesList.add(0, address);
                        } else {
                            mInetAddressesList.add(address);
                        }
                    }
                    return mInetAddressesList;
                } catch (NullPointerException var4) {
                    UnknownHostException unknownHostException = new UnknownHostException("Broken system behaviour");
                    unknownHostException.initCause(var4);
                    throw unknownHostException;
                }
            }
        }
    }
}
