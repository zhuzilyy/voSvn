package com.zl.vo_.main.Entity;

import com.zl.vo_.main.activities.MyFrindEntivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9.
 */

public class BigMineGroupList {

    public BigMineGroupList() {

        friendList=new ArrayList<>();

    }

    public List<MyFrindEntivity> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<MyFrindEntivity> friendList) {
        this.friendList = friendList;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    private String groupId;
    private List<MyFrindEntivity> friendList;
    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
