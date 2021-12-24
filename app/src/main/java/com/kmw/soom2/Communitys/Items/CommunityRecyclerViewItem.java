package com.kmw.soom2.Communitys.Items;

import com.kmw.soom2.Home.HomeItem.MedicineTakingItem;

public class CommunityRecyclerViewItem {

    int viewType;
    CommunityItems communityItems;
    public final static int HEADER_TYPE = 0;
    public final static int ITEM_TYPE = 1;
    public final static int HEADER_SPACE = 2;

    public CommunityRecyclerViewItem(CommunityItems communityItems, int viewType){
        this.communityItems = communityItems;
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public CommunityItems getCommunityItems() {
        return communityItems;
    }

    public void setCommunityItems(CommunityItems communityItems) {
        this.communityItems = communityItems;
    }

}
