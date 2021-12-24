package com.kmw.soom2.Communitys.Items;

import java.util.ArrayList;

public class CommunityItems implements Comparable{

    public CommunityItems() {

    }

    public ArrayList<String> getHash() {
        return hash;
    }

    public void setHash(ArrayList<String> hash) {
        this.hash = hash;
    }

    ArrayList<String> hash = new ArrayList<>();
    String no;
    String lv;
    String profile;
    String name;
    String date;
    String imgListPath;
    String contents;
    String hashTag;
    int gender;
    int likeCnt;
    int commentCnt;
    String userNo;
    int expandableFlag;
    int likeFlag;
    int scrapFlag;
    String imagesNo;
    int priority;
    String cMenuNo;
    int lineCnt;
    String communityTitle;
    String communityExPlanation;
    String label;
    String labelColor;
    String mMenuNo;
    String mViewCnt;
    String cMenuTitle;

    public String getcMenuTitle() {
        return cMenuTitle;
    }

    public void setcMenuTitle(String cMenuTitle) {
        this.cMenuTitle = cMenuTitle;
    }

    public String getmViewCnt() {
        return mViewCnt;
    }

    public void setmViewCnt(String mViewCnt) {
        this.mViewCnt = mViewCnt;
    }

    public String getmMenuNo() {
        return mMenuNo;
    }

    public void setmMenuNo(String mMenuNo) {
        this.mMenuNo = mMenuNo;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getCommunityTitle() {
        return communityTitle;
    }

    public void setCommunityTitle(String communityTitle) {
        this.communityTitle = communityTitle;
    }

    public String getCommunityExPlanation() {
        return communityExPlanation;
    }

    public void setCommunityExPlanation(String communityExPlanation) {
        this.communityExPlanation = communityExPlanation;
    }

    public int getLineCnt() {
        return lineCnt;
    }

    public void setLineCnt(int lineCnt) {
        this.lineCnt = lineCnt;
    }

    public String getcMenuNo() {
        return cMenuNo;
    }

    public void setcMenuNo(String cMenuNo) {
        this.cMenuNo = cMenuNo;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getImagesNo() {
        return imagesNo;
    }

    public void setImagesNo(String imagesNo) {
        this.imagesNo = imagesNo;
    }

    public int getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(int likeFlag) {
        this.likeFlag = likeFlag;
    }

    public int getScrapFlag() {
        return scrapFlag;
    }

    public void setScrapFlag(int scrapFlag) {
        this.scrapFlag = scrapFlag;
    }

    public int getExpandableFlag() {
        return expandableFlag;
    }

    public void setExpandableFlag(int expandableFlag) {
        this.expandableFlag = expandableFlag;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public String getLv() {
        return lv;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgListPath() {
        return imgListPath;
    }

    public void setImgListPath(String imgListPath) {
        this.imgListPath = imgListPath;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getCommentCnt() {
        return commentCnt;
    }

    public void setCommentCnt(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
