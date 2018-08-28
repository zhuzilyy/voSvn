package com.zl.vo_.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zl.vo_.DemoHelper;

/**
 * Created by Administrator on 2017/11/9.
 * 数据库帮助类
 *       1.版本号
 *       2.创建4张表
 *       3.提供一个构造：获取当前用户的名称_vo.db
 *       4.提供一个getInstance方法获取帮助类的实例
 *       5.在oncreate()方法中创建表
 *       6.提供关闭数据库的方法
 *
 *
 *
 */

public class DbOpenHelper extends SQLiteOpenHelper{
    //数据库版本号
    private static final int DATABASE_VERSION = 6;
    //dbHelper实例
    private static DbOpenHelper instance;

    //创建用户表
    private static final String USERNAME_TABLE_CREATE="CREATE TABLE "
            +UserDao.TABLE_NAME+"("
            +UserDao.COLUMN_NAME_NICK+" TEXT,"
            +UserDao.COLUMN_NAME_AVATAR +" TEXT,"
            +UserDao.COLUMN_NAME_MGROUPID +" TEXT,"
            +UserDao.COLUMN_NAME_REMARK +" TEXT,"
            +UserDao.COLUMN_NAME_ID+" TEXT PRIMARY KEY)";
    //存储消息的表
    private static final String INIVTE_MESSAGE_TABLE_CREATE = "CREATE TABLE "
            + InviteMessgeDao.TABLE_NAME + " ("
            + InviteMessgeDao.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + InviteMessgeDao.COLUMN_NAME_FROM + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUP_ID + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUP_Name + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_REASON + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_STATUS + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_ISINVITEFROMME + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_UNREAD_MSG_COUNT + " INTEGER, "
            + InviteMessgeDao.COLUMN_NAME_TIME + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_AVATAR + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_DESC+ " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_NICK + " TEXT, "
            + InviteMessgeDao.COLUMN_NAME_GROUPINVITER + " TEXT); ";

    //机器人表
    private static final String ROBOT_TABLE_CREATE = "CREATE TABLE "
            + UserDao.ROBOT_TABLE_NAME + " ("
            + UserDao.ROBOT_COLUMN_NAME_ID + " TEXT PRIMARY KEY, "
            + UserDao.ROBOT_COLUMN_NAME_NICK + " TEXT, "
            + UserDao.ROBOT_COLUMN_NAME_AVATAR + " TEXT);";

    //
    private static final String CREATE_PREF_TABLE="CREATE TABLE "
            +UserDao.PREF_TABLE_NAME+"("
            +UserDao.COLUMN_NAME_DISABLED_GROUPS+" TEXT,"
            +UserDao.COLUMN_NAME_DISABLED_IDS+" TEXT)";

    //创建我的好友分组表
    private static final String MMGROUP_TABLE_CREATE="CREATE TABLE "
            +MyFriendGroupDao.TABLE_NAME+"("
            +MyFriendGroupDao.COLUMN_NAME_GROUPNAME+" TEXT UNIQUE,"
            +MyFriendGroupDao.COLUMN_NAME_GROUPID+" INTEGER PRIMARY KEY AUTOINCREMENT)";

    private static final String CreateAccountSwitTable="create table switchAccount (id integer primary key autoincrement,account text)";

    //好友表
    private static final String  createMyFriendsTable="create table myfriendtable(id integer primary key autoincrement," +
            "account text,huanxinId text,nike text,avatar text,groupid text,userid text,phone text," +
            "address text,remark text,groupname text,passid text,friend_check text,sex text)";



    //密码表
    private static final String createPasswordTable="create table mypasswordtable(id integer primary key autoincrement ," +
            "is_shake text,is_set text,time text,hidestate text default 0,next_time text)";



    //构造
    private DbOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }
    //获取数据库的名字
    private static String getUserDatabaseName() {
        return  DemoHelper.getInstance().getCurrentUsernName() + "_vo.db";
    }

    //获取数据库帮助类的实例
    public static DbOpenHelper getInstance(Context context){
        if(instance==null){
            instance=new DbOpenHelper(context.getApplicationContext());
        }
        return instance;
    }








    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USERNAME_TABLE_CREATE);
        db.execSQL(INIVTE_MESSAGE_TABLE_CREATE);
        db.execSQL(CREATE_PREF_TABLE);
        db.execSQL(ROBOT_TABLE_CREATE);
        db.execSQL(MMGROUP_TABLE_CREATE);
        db.execSQL(CreateAccountSwitTable);
        //好友表
        db.execSQL(createMyFriendsTable);
        //隐藏好友表
        db.execSQL(createPasswordTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //数据库更新
    }
    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
