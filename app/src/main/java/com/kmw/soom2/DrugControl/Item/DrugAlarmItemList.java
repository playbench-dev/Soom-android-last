package com.kmw.soom2.DrugControl.Item;

public class DrugAlarmItemList {

    public DrugAlarmItemList() {

    }

    int idx;
    String selectDay;
    long selectTime;
    int pushCheck;
    String drugName;
    long compareTime;

    public long getCompareTime() {
        return compareTime;
    }

    public void setCompareTime(long compareTime) {
        this.compareTime = compareTime;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getSelectDay() {
        return selectDay;
    }

    public void setSelectDay(String selectDay) {
        this.selectDay = selectDay;
    }

    public long getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(long selectTime) {
        this.selectTime = selectTime;
    }

    public int getPushCheck() {
        return pushCheck;
    }

    public void setPushCheck(int pushCheck) {
        this.pushCheck = pushCheck;
    }
}
