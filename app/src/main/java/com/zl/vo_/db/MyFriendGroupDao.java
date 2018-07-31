package com.zl.vo_.db;

import com.zl.vo_.main.Entity.MineGroupEntityNet;
import com.zl.vo_.main.Entity.MyFriendGroupEntity;

import java.net.PortUnreachableException;
import java.util.List;

/**
 * Created by Administrator on 2017/11/16.
 */

public class MyFriendGroupDao {

    public static final String TABLE_NAME = "_mGroup";
    public static final String COLUMN_NAME_GROUPID = "_mGroupId";
    public static final String COLUMN_NAME_GROUPNAME = "_mGoupName";

    public MyFriendGroupDao() {
    }
    /***
     * 保存一个分组
     */
    public void saveMyGroup(MineGroupEntityNet.MineGroupInfo.MineGroupInfoArr infoArr){
        DemoDBManager dbManager= DemoDBManager.getInstance();
        dbManager.saveMyFriendGroup(infoArr);
    }
    /***
     * 删除一个分组
     */
    public void deleteMyGroup(String groupName){
        DemoDBManager.getInstance().deleteMyFriendGroup(groupName);

    }
    /***
     * 更改一个组名称
     */

    public void updateMyGroup(int groupId,String groupName){
        DemoDBManager.getInstance().updateMyFriendGroupName(groupId,groupName);
    }
    /***
     * 查询所有的组信息
     */
    public List<MyFriendGroupEntity> selectGroupEntities(){
       return DemoDBManager.getInstance().selectMyFriendGroupList();


    }
    /***
     * 查询一个组
     */
    public MyFriendGroupEntity setlecMyFriendOneGroup(String groupid){
        return DemoDBManager.getInstance().setlecMyFriendOneGroup(groupid);

    }

    /***
     * 将从网络上请求下来的数据保存到数据库，
     * @param bigList
     */
    public void saveGroups(List<MyFriendGroupEntity> bigList) {
        DemoDBManager.getInstance().saveGroups_(bigList);
    }
}
