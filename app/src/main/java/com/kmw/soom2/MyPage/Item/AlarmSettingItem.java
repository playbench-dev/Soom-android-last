package com.kmw.soom2.MyPage.Item;

public class AlarmSettingItem {
    int asthmaFlag;
    int symptomFlag;
    int dosingFlag;
    int noticeFlag;
    int communityLikeFlag;
    int communityCommentFlag;
    int communityCommentReplyFlag;

    int allFlag;

    public int checkSettingFlag() {
        if ((communityLikeFlag == 1) || (communityCommentFlag == 1) || (communityCommentReplyFlag == 1)) {
            allFlag = 1;
        }else {
            allFlag = -1;
        }

        return allFlag;
    }

    public void setAllCheck(boolean flag) {
        if (flag) {
            asthmaFlag = 1;
            dosingFlag = 1;
            noticeFlag = 1;
            communityLikeFlag = 1;
            communityCommentFlag = 1;
            communityCommentReplyFlag = 1;


        }else {
            asthmaFlag = -1;
            dosingFlag = -1;
            noticeFlag = -1;
            communityLikeFlag = -1;
            communityCommentFlag = -1;
            communityCommentReplyFlag = -1;
        }
    }

    public int getSymptomFlag() {
        return symptomFlag;
    }

    public void setSymptomFlag(int symptomFlag) {
        this.symptomFlag = symptomFlag;
    }

    public int getAsthmaFlag() {
        return asthmaFlag;
    }

    public void setAsthmaFlag(int asthmaFlag) {
        this.asthmaFlag = asthmaFlag;
    }

    public int getDosingFlag() {
        return dosingFlag;
    }

    public void setDosingFlag(int dosingFlag) {
        this.dosingFlag = dosingFlag;
    }

    public int getNoticeFlag() {
        return noticeFlag;
    }

    public void setNoticeFlag(int noticeFlag) {
        this.noticeFlag = noticeFlag;
    }

    public int getCommunityLikeFlag() {
        return communityLikeFlag;
    }

    public void setCommunityLikeFlag(int communityLikeFlag) {
        this.communityLikeFlag = communityLikeFlag;
    }

    public int getCommunityCommentFlag() {
        return communityCommentFlag;
    }

    public void setCommunityCommentFlag(int communityCommentFlag) {
        this.communityCommentFlag = communityCommentFlag;
    }

    public int getCommunityCommentReplyFlag() {
        return communityCommentReplyFlag;
    }

    public void setCommunityCommentReplyFlag(int communityCommentReplyFlag) {
        this.communityCommentReplyFlag = communityCommentReplyFlag;
    }

    @Override
    public String toString() {
        return "AlarmSettingItem{" +
                "symptomFlag=" + asthmaFlag +
                ", dosingFlag=" + dosingFlag +
                ", noticeFlag=" + noticeFlag +
                ", communityLikeFlag=" + communityLikeFlag +
                ", communityCommentFlag=" + communityCommentFlag +
                ", communityCommentReplyFlag=" + communityCommentReplyFlag +
                '}';
    }
}
