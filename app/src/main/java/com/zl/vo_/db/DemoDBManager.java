package com.zl.vo_.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.util.Log;


import com.baidu.platform.comapi.map.E;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.HanziToPinyin;
import com.zl.vo_.Constant;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.domain.InviteMessage;
import com.zl.vo_.domain.RobotUser;
import com.zl.vo_.main.Entity.MineGroupEntityNet;
import com.zl.vo_.main.Entity.MyFriendGroupEntity;
import com.zl.vo_.main.Entity.MyPwdEntity;
import com.zl.vo_.main.Entity.MyPwdList;
import com.zl.vo_.main.Entity.pwdCell;
import com.zl.vo_.main.activities.MyFrindEntivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/****
 * 项目数据库管理者
 */
public  class  DemoDBManager {
    //dbMgr 数据库管理实例
    static private DemoDBManager dbMgr = new DemoDBManager();
    //数据库帮助类
    private DbOpenHelper dbHelper;

    //数据库管理类构造（创建数据库帮助类）
    private DemoDBManager() {
        dbHelper = DbOpenHelper.getInstance(DemoApplication.getInstance().getApplicationContext());
    }

    /***
     * 获取数据库实例（单例）
     *
     * @return
     */
    public static synchronized DemoDBManager getInstance() {
        if (dbMgr == null) {
            dbMgr = new DemoDBManager();
        }
        return dbMgr;
    }

    //----------------------------------------


    /***
     * 查询一个分组
     * @param groupid
     * @return
     */
    public MyFriendGroupEntity setlecMyFriendOneGroup(String groupid) {

        //以读写的方式打开数据库
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()) {
            String sql = "select * from " + MyFriendGroupDao.TABLE_NAME + " where _mGroupId = " +groupid ;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                MyFriendGroupEntity en = new MyFriendGroupEntity();
                en.setGroupId(cursor.getInt(cursor.getColumnIndex("_mGroupId")));
                en.setGroupName(cursor.getString(cursor.getColumnIndex("_mGoupName")));


                return en;

            }
        }
        return null;
    }



    /***
     * 保存一个我的分组
     */
    synchronized public boolean saveMyFriendGroup(MineGroupEntityNet.MineGroupInfo.MineGroupInfoArr infoArr) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db.isOpen()) {
            String sql="insert into _mGroup(_mGroupId,_mGoupName) values('"+infoArr.getGroupid()+"','"+infoArr.getGroupname()+"')";
            try {
                db.execSQL(sql);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
               return false;
            }
        }
        return false;
    }

    /****
     * 删除一个组
     */
    synchronized public boolean deleteMyFriendGroup(String groupName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            String sql="delete from _mGroup where _mGoupName='"+groupName+"'";
            try {
                db.execSQL(sql);
                Log.i("rr","ok");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("rr","err=="+e);
                return false;
            }
        }
        return false;
    }

    /***
     * 更新一个组名称
     */

    synchronized public boolean updateMyFriendGroupName(int groupID, String newGroupName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            String sql="update _mGroup set _mGoupName='"+newGroupName+"' where _mGroupId='"+groupID+"'";
            try {
                db.execSQL(sql);
                Log.i("rr","ok");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("rr","err=="+e);
                return false;
            }
        }
        return false;
    }
    /****
     * 查询所有组
     */
    synchronized public List<MyFriendGroupEntity> selectMyFriendGroupList(){
        List<MyFriendGroupEntity> groupEntities=new ArrayList<>();
        //以读写的方式打开数据库
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql="select * from "+MyFriendGroupDao.TABLE_NAME;
           Cursor cursor= db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                MyFriendGroupEntity en = new MyFriendGroupEntity();
                en.setGroupId(cursor.getInt(cursor.getColumnIndex("_mGroupId")));
                en.setGroupName(cursor.getString(cursor.getColumnIndex("_mGoupName")));
                groupEntities.add(en);



            }
        }
            return groupEntities;
    }


    //-----------------------------------------

    /**
     * 保存联系人列表
     *
     * @param contactList
     */
    synchronized public void saveContactList(List<EaseUser> contactList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, null, null);
            for (EaseUser user : contactList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
                if (user.getNick() != null)
                    values.put(UserDao.COLUMN_NAME_NICK, user.getNick());
                if (user.getAvatar() != null)
                    values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
                db.replace(UserDao.TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取联系人列表
     *
     * @return
     */
    synchronized public Map<String, EaseUser> getContactList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, EaseUser> users = new Hashtable<String, EaseUser>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.TABLE_NAME /* + " desc" */, null);
            while (cursor.moveToNext()) {

                String username = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.COLUMN_NAME_AVATAR));
                EaseUser user = new EaseUser();
                user.setUsername(username);

                user.setNick(nick);
                user.setAvatar(avatar);
                if (username.equals(Constant.NEW_FRIENDS_USERNAME) || username.equals(Constant.GROUP_USERNAME)
                        || username.equals(Constant.CHAT_ROOM) || username.equals(Constant.CHAT_ROBOT)) {
                    user.setInitialLetter("");
                } else {
                    EaseCommonUtils.setUserInitialLetter(user);
                }
                users.put(username, user);
            }
            cursor.close();
        }
        return users;
    }

    public List<EaseUser> getContactsFromDb(){
        List<EaseUser> users_=new ArrayList<>();
        //以读写的方式打开数据库
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql="select * from uers";
            Cursor cursor= db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                EaseUser user=new EaseUser();
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setNick(cursor.getString(cursor.getColumnIndex("nick")));
                user.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                users_.add(user);



            }
        }
        return users_;


    }



    /**
     * 删除一个联系人
     *
     * @param username
     */
    synchronized public void deleteContact(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.TABLE_NAME, UserDao.COLUMN_NAME_ID + " = ?", new String[]{username});
        }
    }

    /**
     * 保存一个联系人
     *
     * @param user
     */
    synchronized public void saveContact(EaseUser user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDao.COLUMN_NAME_ID, user.getUsername());
        if (user.getNick() != null)
            values.put(UserDao.COLUMN_NAME_NICK, user.getNick());
        if (user.getAvatar() != null)
            values.put(UserDao.COLUMN_NAME_AVATAR, user.getAvatar());
        if(user.getRemark() != null){
            values.put(UserDao.COLUMN_NAME_REMARK,user.getRemark());
        }
        if (db.isOpen()) {
            db.replace(UserDao.TABLE_NAME, null, values);
        }

    }

    /**
     * 获取不可用的群组
     *
     * @return
     */
    public List<String> getDisabledGroups() {
        return getList(UserDao.COLUMN_NAME_DISABLED_GROUPS);
    }

    /***
     * 设置一个群组不可用
     *
     * @param groups
     */
    public void setDisabledGroups(List<String> groups) {
        setList(UserDao.COLUMN_NAME_DISABLED_GROUPS, groups);
    }

    /***
     * 获取不可用的用户列表
     *
     * @return
     */
    public List<String> getDisabledIds() {
        return getList(UserDao.COLUMN_NAME_DISABLED_IDS);
    }

    /****
     * 设置用户不可用
     *
     * @param ids
     */
    public void setDisabledIds(List<String> ids) {
        setList(UserDao.COLUMN_NAME_DISABLED_IDS, ids);
    }

    /***
     * @param column
     * @param strList
     */
    synchronized private void setList(String column, List<String> strList) {
        StringBuilder strBuilder = new StringBuilder();

        for (String hxid : strList) {
            strBuilder.append(hxid).append("$");
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(column, strBuilder.toString());

            db.update(UserDao.PREF_TABLE_NAME, values, null, null);
        }
    }

    synchronized private List<String> getList(String column) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + column + " from " + UserDao.PREF_TABLE_NAME, null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        String strVal = cursor.getString(0);
        if (strVal == null || strVal.equals("")) {
            return null;
        }

        cursor.close();

        String[] array = strVal.split("$");

        if (array.length > 0) {
            List<String> list = new ArrayList<String>();
            Collections.addAll(list, array);
            return list;
        }

        return null;
    }

    /**
     * 保存一条消息
     *
     * @param message
     * @return return cursor of the message
     */
    public synchronized Integer saveMessage(InviteMessage message) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int id = -1;
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_FROM, message.getFrom());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_ID, message.getGroupId());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUP_Name, message.getGroupName());
            values.put(InviteMessgeDao.COLUMN_NAME_REASON, message.getReason());
            values.put(InviteMessgeDao.COLUMN_NAME_TIME, message.getTime());
            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, message.getStatus().ordinal());
            values.put(InviteMessgeDao.COLUMN_NAME_GROUPINVITER, message.getGroupInviter());
            values.put(InviteMessgeDao.COLUMN_NAME_AVATAR, message.getAvatar());
            values.put(InviteMessgeDao.COLUMN_NAME_NICK, message.getNick());
            values.put(InviteMessgeDao.COLUMN_NAME_DESC, message.getGroup_desc());


            db.insert(InviteMessgeDao.TABLE_NAME, null, values);

            Cursor cursor = db.rawQuery("select last_insert_rowid() from " + InviteMessgeDao.TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }



            cursor.close();
        }
        return id;
    }

    /**
     * 更新消息
     *
     * @param msgId
     * @param values
     */
    synchronized public void updateMessage(int msgId, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.update(InviteMessgeDao.TABLE_NAME, values, InviteMessgeDao.COLUMN_NAME_ID + " = ?", new String[]{String.valueOf(msgId)});
        }
    }

    /**
     * 获取消息
     *
     * @return
     */
    synchronized public List<InviteMessage> getMessagesList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<InviteMessage> msgs = new ArrayList<InviteMessage>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + InviteMessgeDao.TABLE_NAME + " desc", null);
            while (cursor.moveToNext()) {
                InviteMessage msg = new InviteMessage();
                int id = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_ID));
                String from = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_FROM));
                String groupid = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_ID));
                String groupname = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUP_Name));
                String reason = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_REASON));
                String group_desc=cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_DESC));
                long time = cursor.getLong(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_TIME));
                int status = cursor.getInt(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_STATUS));
                String groupInviter = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_GROUPINVITER));
                //头像
                String avatar = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_AVATAR));
                //昵称
                String nick = cursor.getString(cursor.getColumnIndex(InviteMessgeDao.COLUMN_NAME_NICK));

                msg.setId(id);
                msg.setFrom(from);
                msg.setGroupId(groupid);
                msg.setGroupName(groupname);
                msg.setReason(reason);
                msg.setTime(time);
                msg.setGroup_desc(group_desc);
                msg.setGroupInviter(groupInviter);
                msg.setStatus(InviteMessage.InviteMessageStatus.values()[status]);
                msg.setAvatar(avatar);
                msg.setNick(nick);
                msgs.add(msg);
            }
            cursor.close();
        }
        return msgs;
    }

    /**
     * 删除邀请消息
     *
     * @param from
     */
    synchronized public void deleteMessage(String from) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_FROM + " = ?", new String[]{from});
        }
    }

    /**
     * 删除来自群组的邀请消息
     *
     * @param groupId
     */
    synchronized public void deleteGroupMessage(String groupId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_GROUP_ID + " = ?", new String[]{groupId});
        }
    }

    /**
     * 删除群组里某人的邀请
     *
     * @param groupId
     */
    synchronized public void deleteGroupMessage(String groupId, String from) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(InviteMessgeDao.TABLE_NAME, InviteMessgeDao.COLUMN_NAME_GROUP_ID + " = ? AND " + InviteMessgeDao.COLUMN_NAME_FROM + " = ? ",
                    new String[]{groupId, from});
        }
    }

    /****
     * 获取未读通知的总数
     *
     * @return
     */
    synchronized int getUnreadNotifyCount() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select " + InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT + " from " + InviteMessgeDao.TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    /****
     * 这只未读通知的总数
     *
     * @param count
     */
    synchronized void setUnreadNotifyCount(int count) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT, count);

            db.update(InviteMessgeDao.TABLE_NAME, values, null, null);
        }
    }

    /****
     * 关闭数据库
     */
    synchronized public void closeDB() {
        if (dbHelper != null) {
            dbHelper.closeDB();
        }
        dbMgr = null;
    }


    /**
     * 保存机器人列表
     */
    synchronized public void saveRobotList(List<RobotUser> robotList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(UserDao.ROBOT_TABLE_NAME, null, null);
            for (RobotUser item : robotList) {
                ContentValues values = new ContentValues();
                values.put(UserDao.ROBOT_COLUMN_NAME_ID, item.getUsername());
                if (item.getNick() != null)
                    values.put(UserDao.ROBOT_COLUMN_NAME_NICK, item.getNick());
                if (item.getAvatar() != null)
                    values.put(UserDao.ROBOT_COLUMN_NAME_AVATAR, item.getAvatar());
                db.replace(UserDao.ROBOT_TABLE_NAME, null, values);
            }
        }
    }

    /**
     * 获取机器人map
     */
    synchronized public Map<String, RobotUser> getRobotList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Map<String, RobotUser> users = null;
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + UserDao.ROBOT_TABLE_NAME, null);
            if (cursor.getCount() > 0) {
                users = new Hashtable<String, RobotUser>();
            }
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_ID));
                String nick = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_NICK));
                String avatar = cursor.getString(cursor.getColumnIndex(UserDao.ROBOT_COLUMN_NAME_AVATAR));
                RobotUser user = new RobotUser(username);
                user.setNick(nick);
                user.setAvatar(avatar);
                String headerName = null;
                if (!TextUtils.isEmpty(user.getNick())) {
                    headerName = user.getNick();
                } else {
                    headerName = user.getUsername();
                }
                if (Character.isDigit(headerName.charAt(0))) {
                    user.setInitialLetter("#");
                } else {
                    user.setInitialLetter(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target
                            .substring(0, 1).toUpperCase());
                    char header = user.getInitialLetter().toLowerCase().charAt(0);
                    if (header < 'a' || header > 'z') {
                        user.setInitialLetter("#");
                    }
                }

                try {
                    users.put(username, user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
        return users;
    }
    //***************************************************************
    //********账号增删改查**********************************************

    /****
     * 保存一个账号
     * @param account
     * @return
     */

    synchronized  public boolean saveAccountSwitch(String account){

        SQLiteDatabase db=dbHelper.getWritableDatabase();


        if(db.isOpen()){
            String saveSql="insert into switchAccount(id,account) values(null,'"+account+"')";
            try{
                db.execSQL(saveSql);
                Log.i("switch","账号保存成功");
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("switch","账号保存失败");
                return false;
            }
        }
     return false;
    }

    /****
     * 查询所有登录的账号
     */
    synchronized public List<String> selectAccount(){
        List<String> accountList=new ArrayList<>();
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql="select * from switchAccount";
            Cursor cursor=db.rawQuery(sql,null);
            while (cursor.moveToNext()){
                accountList.add(cursor.getString(cursor.getColumnIndex("account")));
            }
        }
        return accountList;

    }

    /****
     * 删除账号
     */
    synchronized public boolean deleteSwitchAccount(String account){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql="delete from switchAccount where account='"+account+"'";
            try{
                db.execSQL(sql);
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                return false;

            }
        }
        return false;
    }

    /****
     * 查询一个账号
     */

    synchronized public String getSwitchAccount(String account){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            try{

                String sql="select * from switchAccount where account='"+account+"'";
                Cursor cursor=db.rawQuery(sql,null);
                while (cursor.moveToNext()){
                   String account_str= cursor.getString(cursor.getColumnIndex("account"));
                    return account_str;
                }
            }catch (SQLException e){

                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    //***********************************************************
    //环信自带的表不用了，用自己的好友表

    /****
     * 保存一个账号
     * @param
     * @return
     */

    synchronized  public boolean saveMyFriend(MyFrindEntivity friend){

        SQLiteDatabase db=dbHelper.getWritableDatabase();


        if(db.isOpen()){
            String saveSql="insert into myfriendtable(account,huanxinId,nike,avatar,groupid,userid,remark)" +
                    " values('"+friend.getAccount()+"','"+friend.getHuanxinID()+"','"+friend.getNike()+"'," +
                    "'"+friend.getAvatar()+"','"+friend.getGrooupID()+"','"+friend.getUserID()+"','"+friend.getRemark()+"')";
            try{
                db.execSQL(saveSql);
                Log.i("friend","好友保存成功");
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("friend","好友保存失败");
                return false;
            }
        }
        return false;
    }

    /****
     * 删除好友
     */
    synchronized public boolean deleteMyFrind(MyFrindEntivity friend){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql="delete from myfriendtable where account='"+friend.getHuanxinID()+"'";
            try{
                db.execSQL(sql);
                Log.i("friend","好友删除成功");
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("friend","好友删除失败");
                return false;

            }
        }
        return false;
    }

    /****
     * 查询一个好友通过环信Id
     */

    synchronized public MyFrindEntivity getFriendByHxID(String hxid){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            try{
                String sql="select * from myfriendtable where huanxinId='"+hxid+"'";
                Cursor cursor=db.rawQuery(sql,null);
                while (cursor.moveToNext()){
                    MyFrindEntivity friend=new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));
                    friend.setNike(cursor.getString(cursor.getColumnIndex("nike")));
                    friend.setSex(cursor.getString(cursor.getColumnIndex("sex")));


                    Log.i("friend","查找好友成功");
                    return friend;
                }
            }catch (SQLException e){
                Log.i("friend","查找好友失败");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }










    /****
     * 查询一个好友通过账号
     */

    synchronized public MyFrindEntivity getFriend(String account){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            try{
                String sql="select * from myfriendtable where account='"+account+"'";
                Cursor cursor=db.rawQuery(sql,null);
                while (cursor.moveToNext()){
                    MyFrindEntivity friend=new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));

                    Log.i("friend","查找好友成功");
                    return friend;
                }
            }catch (SQLException e){
                Log.i("friend","查找好友失败");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }




    /****
     * 查询一个好友通过userId
     */

    synchronized public MyFrindEntivity getFriendByUserID(String UserId){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            try{
                String sql="select * from myfriendtable where userid='"+UserId+"'";
                Cursor cursor=db.rawQuery(sql,null);
                while (cursor.moveToNext()){
                    MyFrindEntivity friend=new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));
                    Log.i("friend","查找好友成功");
                    return friend;
                }
            }catch (SQLException e){
                Log.i("friend","查找好友失败");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }



    /****
     * 查询好友列表
     */
    synchronized public List<MyFrindEntivity> getAllFriends() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<MyFrindEntivity> myFrindEntivities = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select * from myfriendtable";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    MyFrindEntivity friend = new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));
                    friend.setNike(cursor.getString(cursor.getColumnIndex("nike")));

                    myFrindEntivities.add(friend);
                    Log.i("friend", "查找所有好友成功");

                }
                return myFrindEntivities;
            } catch (SQLException e) {
                Log.i("friend", "查找所有好友失败");
                e.printStackTrace();
                return myFrindEntivities;
            }

        }
        return myFrindEntivities;
    }

    /****
     * 保存好友列表
     */

    public void saveMyFriendList(List<MyFrindEntivity> frindEntivities){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            String sql="delete from myfriendtable";
            try {
                db.execSQL(sql);
                int num=0;
                for (MyFrindEntivity en:frindEntivities) {
                  Log.i("yu",frindEntivities.size()+"=====hh===");
                   try {
                       String sql2="insert into myfriendtable (account,huanxinId,nike,avatar,groupid,userid,phone,address,remark,groupname,passid,friend_check)" +
                               "values('"+en.getAccount()+"','"+en.getHuanxinID()+"','"+en.getNike()+"','"+en.getAvatar()+"'," +
                               "'"+en.getGrooupID()+"','"+en.getUserID()+"','"+en.getPhone()+"','"+en.getAddress()+"','"+en.getRemark()+"'," +
                               "'"+en.getGroupname()+"','"+en.getPassid()+"','"+en.getFriend_check()+"')";
                        db.execSQL(sql2);
                        Log.i("uu",sql2);
                        num++;
                   }catch (Exception e){
                       e.printStackTrace();
                       Log.i("UU",e.getMessage()+"保存好友出错");
                   }
                }
                Log.i("yu","====num=="+num);
                List<MyFrindEntivity> entivities5= getAllFriends();
                Log.i("yu","====num=="+num);

            }catch (Exception e){
                e.printStackTrace();
                Log.i("UU",e.getMessage()+"删除表出错");
            }
        }
    }

    /****
     * 根据账号更新数据
     */

    synchronized public boolean updateMyFriendInfo(MyFrindEntivity en) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {

            String sql="update myfriendtable set nike='"+en.getNike()+"',avatar='"+en.getAvatar()+"'," +
                    "phone='"+en.getPhone()+"',address='"+en.getAddress()+"',remark='"+en.getRemark()+"'," +
                    "passid='"+en.getPassid()+"' where account='"+en.getAccount()+"'";

            try {
                db.execSQL(sql);
                Log.i("rr","ok");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i("rr","err=="+e);
                return false;
            }
        }
        return false;
    }


    /****
     * 将从网络上请求下来的数据保存到数据库，
     * @param bigList
     */

    public void saveGroups_(List<MyFriendGroupEntity> bigList) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            //先删除表
            String sql="delete from _mGroup";
            try {
                db.execSQL(sql);

                for (MyFriendGroupEntity en:bigList) {
                    Log.i("yu",bigList.size()+"=====hh===");
                    try {
                        String sql2="insert into _mGroup ( _mGroupId,_mGoupName) values('"+en.getGroupId()+"','"+en.getGroupName()+"')";
                        db.execSQL(sql2);
                        Log.i("uu",sql2);
                        Log.i("UU","保存好友分组成功");
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.i("UU",e.getMessage()+"保存好友分组出错");
                    }
                }


            }catch (Exception e){
                e.printStackTrace();
                Log.i("UU",e.getMessage()+"删除我的好友分组表出错");
            }
        }

    }

    //*************************************************************************
    //*******************隐藏好友表**********************************************


   /* public List<MyPwdEntity.MyPwdInfo.MyPwdCell> getMyPass(){
        SQLiteDatabase db=dbHelper.getWritableDatabase();

        if(db.isOpen()){
            String sql="select * from mypasswordtable";
            try{

                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()){


                }
            }catch (Exception e){

            }
        }
    }*/



    /***
     * 保存密码
     */

    public boolean saveMyPwd(MyPwdEntity.MyPwdInfo.MyPwdCell cell){
        SQLiteDatabase db=dbHelper.getWritableDatabase();


        if(db.isOpen()){


                String saveSql="insert into mypasswordtable(id,is_shake,is_set,time)values(" +
                        "'"+cell.getPassid()+"','"+cell.getIsmove()+"','"+cell.getIsset()+"','"+cell.getStarttime()+"')";
            try{
                db.execSQL(saveSql);
                Log.i("friend","密码保存成功");
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("friend","密码保存失败");
                return false;
            }
        }
        return false;

    }
    /***
     * 更新密码表
     */
    public boolean UpdateMyPwd(MyPwdEntity.MyPwdInfo.MyPwdCell cell,String state){
        SQLiteDatabase db=dbHelper.getWritableDatabase();


        if(db.isOpen()){


            String sql="update mypasswordtable set is_shake='"+cell.getIsmove()+"'," +
                    "is_set='"+cell.getIsset()+"',time='"+cell.getStarttime()+"',hidestate='"+state+"' where id= "+cell.getPassid();



            try{
                db.execSQL(sql);
                Log.i("friend","密码更新成功");
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("friend","密码更新失败");
                return false;
            }
        }
        return false;

    }

    /****
     * 更新临时状态
     */

    public boolean updateMyPwd2(String state, String passid){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql;
            if(TextUtils.isEmpty(passid)){
                sql="update mypasswordtable set hidestate='"+state+"'";
            }else {
                sql="update mypasswordtable set hidestate='"+state+"' where id ="+passid;
            }
            try{
                db.execSQL(sql);
                Log.i("friend","密码临时状态更新成功");
                List<MyFrindEntivity> hashideStateMyFriendList= getHashideState();
                Log.i("ii",""+hashideStateMyFriendList.size());

                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("friend","密码临时状态更新失败");
                return false;
            }
        }else {
            return false;
        }


    }

    /****
     * 更新下次隐藏的时间
     */

    public boolean updateMyPwdnexttime(String nexttime, String passid){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql;
                sql="update mypasswordtable set next_time='"+ nexttime +"' where id ="+passid;


            try{
                db.execSQL(sql);
                Log.i("friend","下次隐藏时间临时状态更新成功");
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("friend","下次隐藏时间临时状态更新失败");
                return false;
            }
        }else {
            return false;
        }


    }





    /****
     * 摇一摇更新临时状态
     */

    public boolean updateMyPwd2Shake(String state){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql;

                sql="update mypasswordtable set hidestate='"+state+"' where is_shake = 1";

            try{
                db.execSQL(sql);
                Log.i("friend","密码临时状态=摇一摇=更新成功");
                return true;
            }catch (SQLException e){
                e.printStackTrace();
                Log.i("friend","密码临时状态=摇一摇=更新失败");
                return false;
            }
        }else {
            return false;
        }


    }



    public pwdCell selectMyPwdState(String passid){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        pwdCell cell=new pwdCell();
        if(db.isOpen()){
            try{
                String sql="select * from mypasswordtable where id = '"+passid+"'";
                Cursor cursor=db.rawQuery(sql,null);
                while (cursor.moveToNext()){
                        cell.setHidestate(cursor.getString(cursor.getColumnIndex("hidestate")));
                        cell.setIsmove(cursor.getString(cursor.getColumnIndex("is_shake")));
                        cell.setIsset(cursor.getString(cursor.getColumnIndex("is_set")));
                        cell.setPassid(cursor.getString(cursor.getColumnIndex("id")));
                        cell.setStarttime(cursor.getString(cursor.getColumnIndex("time")));

                    return cell;
                }

            }catch (SQLException e){

                e.printStackTrace();
                return cell;
            }
        }

            return cell;


    }

    /****
     * 查询加密人的此时的临时状态（是隐藏还是显示）
     */
    public String pwdFriendHideState(String from){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql="select b.hidestate from myfriendtable a left join mypasswordtable b on a.passid = b.id where a.id=from";
            try {
                Cursor cursor = db.rawQuery(sql, null);
                if(cursor.moveToNext()){
                   String hideState= cursor.getString(cursor.getColumnIndex("hidestate"));
                    return hideState;
                }
            }catch (SQLException e){
                Log.i("e",e.getMessage());
            }
        }
        return null;
    }


    /****
     * 查询passid 有临时状态的人
     * @return
     */
    public List<MyFrindEntivity> getHashideState() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<MyFrindEntivity> myFrindEntivities = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select a.* from myfriendtable a left join mypasswordtable b on a.passid = b.id where (a.passid!='' and b.hidestate = '1')";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    MyFrindEntivity friend = new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));

                    myFrindEntivities.add(friend);
                    Log.i("friend", "查找所有好友成功");

                }
                return myFrindEntivities;
            } catch (SQLException e) {
                Log.i("friend", "查找所有好友失败");
                e.printStackTrace();
                return myFrindEntivities;
            }

        }
        return myFrindEntivities;
    }






    /****
     * 查询所有隐藏的人
     * @return
     */
    public List<MyFrindEntivity> gethidefriends() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<MyFrindEntivity> myFrindEntivities = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select a.* from myfriendtable a left join mypasswordtable b on a.passid=b.id where (a.passid!='' and b.hidestate = 0)";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    MyFrindEntivity friend = new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));

                    myFrindEntivities.add(friend);
                    Log.i("friend", "查找所有好友成功");

                }
                return myFrindEntivities;
            } catch (SQLException e) {
                Log.i("friend", "查找所有好友失败");
                e.printStackTrace();
                return myFrindEntivities;
            }

        }
        return myFrindEntivities;
    }






    /****
     * 查询所有隐藏（加密的人）
     */
    public List<MyFrindEntivity> gethidefriends_pwdstate() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<MyFrindEntivity> myFrindEntivities = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select a.* from myfriendtable a left join mypasswordtable b on a.passid=b.id where (a.passid!='')";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    MyFrindEntivity friend = new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));

                    myFrindEntivities.add(friend);
                    Log.i("friend", "查找所有好友成功");

                }
                return myFrindEntivities;
            } catch (SQLException e) {
                Log.i("friend", "查找所有好友失败");
                e.printStackTrace();
                return myFrindEntivities;
            }

        }
        return myFrindEntivities;
    }



    /****
     * 查询所有可以显示的人
     * @return
     */
    public List<MyFrindEntivity> getAllShowfriends() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<MyFrindEntivity> myFrindEntivities = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select a.* from myfriendtable a left join mypasswordtable b on a.passid=b.id where ((a.passid!='' and b.hidestate=1) or a.passid = '')";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    MyFrindEntivity friend = new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));

                    myFrindEntivities.add(friend);
                    Log.i("friend", "查找所有好友成功");

                }
                return myFrindEntivities;
            } catch (SQLException e) {
                Log.i("friend", "查找所有好友失败");
                e.printStackTrace();
                return myFrindEntivities;
            }

        }
        return myFrindEntivities;
    }

    /***
     * 插入或更新密码表
     * @param pwdLists
     */
    public boolean updateOrInsertPwdTable(List<MyPwdList.MyPwdListInfo.pwdList> pwdLists) {

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            List<String> pwdCellIds=getAllMyPwdIds();
            String sql="";
            for (int i = 0; i <pwdLists.size() ; i++) {
                MyPwdList.MyPwdListInfo.pwdList ll= pwdLists.get(i);
                if(pwdCellIds.contains(pwdLists.get(i).getPassid())){
                    //包含：更新
                    sql="update mypasswordtable set is_shake='"+ll.getIsmove()+"'," +
                            "is_set='"+ll.getIsset()+"',time='"+ll.getStarttime()+" ' where id ="+ll.getPassid();
                }else {
                    //没有：插入
                    sql="insert into mypasswordtable(id,is_shake,is_set,time) values('"+ll.getPassid()+"','"+ll.getIsmove()+"'," +
                            "'"+ll.getIsset()+"','"+ll.getStarttime()+"')";
                }
                try {
                    db.execSQL(sql);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("kk",e.getMessage());
                    return  false;
                }

            }
            return true;

            }

         return false;

        }





    /****
     * 获取密码表
     */
    public List<String> getAllMyPwdIds() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<String> pwdids = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select * from mypasswordtable ";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {

                    pwdids.add(cursor.getString(cursor.getColumnIndex("id")));
                    Log.i("friend", "查找所有密码id成功");

                }
                return pwdids;
            } catch (SQLException e) {
                Log.i("friend", "查找所有密码id失败");
                e.printStackTrace();
                return pwdids;
            }


        }
        return pwdids;
    }

    /****
     * 查询有“下次隐藏时间”的密码对象
     * @return
     */
    public List<pwdCell> gethasNextHideTime() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<pwdCell> pwdCells = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select * from mypasswordtable where next_time !='' and hidestate = 1";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    pwdCell cell=new pwdCell();
                    cell.setPassid(cursor.getString(cursor.getColumnIndex("id")));
                    cell.setNexttime(cursor.getString(cursor.getColumnIndex("next_time")));
                    cell.setHidestate(cursor.getString(cursor.getColumnIndex("hidestate")));
                    cell.setIsset(cursor.getString(cursor.getColumnIndex("is_set")));
                    cell.setIsmove(cursor.getString(cursor.getColumnIndex("is_shake")));
                    pwdCells.add(cell);
                    Log.i("cell", "查询有“下次隐藏时间”的密码对象成功");


                }
                return pwdCells;
            } catch (SQLException e) {
                Log.i("cell", "查询有“下次隐藏时间”的密码对象失败");
                e.printStackTrace();
                return pwdCells;
            }


        }
        return pwdCells;

    }

    /****
     * 根据用户名，查询好友
     * @param username
     * @return
     */
    public MyFrindEntivity selectMyFrind(String username) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        MyFrindEntivity friend=new MyFrindEntivity();

        List<MyFrindEntivity> entivities=getAllFriends();
        if (db.isOpen()) {
            try {
                String sql = "select * from myfriendtable where huanxinId = '"+username+"' ";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {


                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));
                    friend.setNike(cursor.getString(cursor.getColumnIndex("nike")));



                    return friend;
                }

             }catch (Exception e){
                Log.i("yui",e.getMessage());
                return null;

            }
        }
        return null;
    }

    /***
     * 跟新好友头像
     * @param
     * @param hxIdFrom
     */
    public void updateFriendAvatar(String hxIdFrom, String avatar, String nick) {

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if(db.isOpen()){
            try {
                String sql="update myfriendtable set avatar='"+avatar+"',nike='"+nick+"' where huanxinId= '"+hxIdFrom+"'";

            }catch (Exception e){
                e.printStackTrace();
            }


        }



    }

    /****
     * 根据passid 查询好友
     * @return
     * @param passids
     */
    public List<MyFrindEntivity> getAllFriendsByPassid(String passids) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<MyFrindEntivity> myFrindEntivities = new ArrayList<>();
        if (db.isOpen()) {
            try {
                String sql = "select a.* from myfriendtable a left join mypasswordtable b on a.passid=b.id where a.passid='"+passids+"'";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    MyFrindEntivity friend = new MyFrindEntivity();
                    friend.setAccount(cursor.getString(cursor.getColumnIndex("account")));
                    friend.setHuanxinID(cursor.getString(cursor.getColumnIndex("huanxinId")));
                    friend.setAvatar(cursor.getString(cursor.getColumnIndex("avatar")));
                    friend.setGrooupID(cursor.getString(cursor.getColumnIndex("groupid")));
                    friend.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
                    friend.setGroupname(cursor.getString(cursor.getColumnIndex("groupname")));
                    friend.setPassid(cursor.getString(cursor.getColumnIndex("passid")));
                    friend.setFriend_check(cursor.getString(cursor.getColumnIndex("friend_check")));
                    friend.setUserID(cursor.getString(cursor.getColumnIndex("userid")));

                    myFrindEntivities.add(friend);
                    Log.i("friend", "查找所有好友成功");

                }
                return myFrindEntivities;
            } catch (SQLException e) {
                Log.i("friend", "查找所有好友失败");
                e.printStackTrace();
                return myFrindEntivities;
            }

        }
        return myFrindEntivities;
    }
    //当一键清空好友后，好友加密表中的数据将被清空
    public  boolean clearAllEncryptionFriends(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if(db.isOpen()){
            String sql="delete from mypasswordtable";
            try {
                db.execSQL(sql);
                return true;
            }catch (Exception e){
                e.printStackTrace();
                Log.i("UU",e.getMessage()+"删除加密好友表失败");
            }
        }
        return false;
    }


}
