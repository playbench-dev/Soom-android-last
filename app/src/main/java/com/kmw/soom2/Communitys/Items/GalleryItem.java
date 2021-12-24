package com.kmw.soom2.Communitys.Items;

public class GalleryItem {

    public GalleryItem() {

    }
    private String imgPath;
    private boolean selected;

    public GalleryItem(String imgPath, boolean selected) {
        this.imgPath = imgPath;
        this.selected = selected;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    String dataTest;
    String idTest;
    String sizeTest;
    long date;
    long index;

    public long getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDataTest() {
        return dataTest;
    }

    public void setDataTest(String dataTest) {
        this.dataTest = dataTest;
    }

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest;
    }

    public String getSizeTest() {
        return sizeTest;
    }

    public void setSizeTest(String sizeTest) {
        this.sizeTest = sizeTest;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }



}
