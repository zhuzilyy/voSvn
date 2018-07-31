package com.zl.vo_.utils;

/**
 * Created by Administrator on 2017/11/27.
 */

public class Url {

   // public static String mURL="http://47.95.115.55:8080/voadmin/home/api/";
    public static String mURL="http://api.ykhswl.net/voadmin/home/api/";
    //注册
    public static String RegisterURL=mURL+"register";
    //登录
    public static String LoginURL=mURL+"login";
    //过程广告页
    public static String AdURL=mURL+"index_image";
    //获取通讯录好友推荐列表（未成为用户好友都的、未注册APP的）
    public static String Friend_hintURL=mURL+"friend_hint";
    //查找好友log
    public static String FindFriendURL=mURL+"find_friend";
    //好友申请前验证接口
    public static String ApplyFriendURL=mURL+"apply_friend";
    //用户资料接口
    public static String FriendInfoURL=mURL+"friend_info";
    //添加好友
    public static String NativeAddFriendUrl=mURL+"add_friend";
    //删除好友接口
    public static String DeleteContactUrl=mURL+"del_friend";
    //通过环信id获取好友信息
    public static String FriendInfoUrl=mURL+"friend_info";
    //获取好友列表接口
    public static String FriendListUrl=mURL+"friend_list";
    //删除接口
    public static String DeleteFriendURL=mURL+"del_friend";
    //删除接口
    public static String CreateGroupURL=mURL+"create_group";
    //切换分组
    public static String SwitchGroupUrl=mURL+"append_to_group";
    //修改分组名称
    public static String UpdataGroupNameUrl=mURL+"update_groupname";
    //获取分组信息
    public static String GetGroupInfoUrl=mURL+"get_grouplist";
    //获取分组信息
    public static String DeleteGroupUrl=mURL+"del_group";
    //修改个人信息
    public static String UpdateBaseInfoUrl=mURL+"update_baseinfo";
    //发表人生笔记
    public static String WriteLifeNoteUrl=mURL+"add_person_record";
    //获取人生笔记列表
    public static String GetLifeNoteUrl=mURL+"person_record_list";
    //删除人生笔记
    public static String DelLifeNoteUrl=mURL+"del_person_record";
    //获取单个人生笔记信息
    public static String GetLifeNotInfoUrl=mURL+"person_record_info";
    //获取单个人生笔记信息
    public static String AppendToGroupUrl=mURL+"append_to_group_more";
    //修改备注信息
    public static String UpdateRemarkUrl=mURL+"update_remark";
    //------------------------------------
    //修改备注信息
    public static String addPrivaacyPwdUrl=mURL+"add_privacypass";
    //个人隐私密码验证
    public static String CheckPwdUrl=mURL+"check_privacypass";
    //	修改隐私密码好友和修改隐私密码配置
    public static String AppendToPrivacy_More_Url=mURL+"append_to_privacypass_more";
    //初始化更新密码表
    public static String updateMyPWDUrl=mURL+"privacypass_list";
    //加入黑名单
    public static String addToBlackListUrl=mURL+"add_blacklist";
    //获取黑名单列表接口
    public static String BlackListUrl=mURL+"black_list";
    //移出黑名单
    public static String DeleteBlackListUrl=mURL+"del_blacklist";
    //获取会员产品
    public static String GetVIPProductUrl=mURL+"vipproduct_list";
    //修改VO账号
    public static String SetVOAccountUrl=mURL+"set_account";
    //获取指定用户接口
    public static String AccountInfoURL=mURL+"account_info";


    //修改个人配置信息
    public static String UpdateInfoUrl=mURL+"update_personinfo";

    //修改个人配置信息
    public static String OneKeyAllClearUrl=mURL+"del_privacypass_all";

    //修改个人配置信息
    public static String GetGroupListUrl=mURL+"get_groupsUser";
    //修改个人配置信息
    public static String GetUserInfoNewMsgUrl=mURL+"msg_huanxin_account_info";
    //对个人的投诉
    public static String ADD_Complaint=mURL+"add_complaint";
    //游戏
    public static String GameList=mURL+"package_list";
    //对平台的投诉
    public static String AddComplaintForPlat=mURL+"add_feedback";
    //重置密码
    public static String ForgetPwd=mURL+"find_password";
    //VIPwebview
    public static String VIPWebView=mURL+"WEBVIEW?id=4";
    //上传图片
    public static String UploadGroupAvatar=mURL+"upload_img";
    //设置人生笔记的密码
    public static String SetLifeNotePwdUrl=mURL+"update_personpass";
    //获取短信验证
    public static String getCodeUrl=mURL+"sendsmscode";
    //获取短信验证
    public static String PrivacyInfosUrl=mURL+"private_info";
    //阿里支付
    public static String ZFBUrl=mURL+"alipay";
    //微信支付
    public static String WxPayUrl=mURL+"wxpay";
    //vo版本更新获取信息
    public static String GetNewVOVewsionUrl="http://api.ykhswl.net/vo_admin_system/api.php";//http://v.cpasp.com/vo_admin_system/api.php
    //获取新版APK的默认地址
    public static String GetNewVOVewsionAPKUrl=mURL+"apk_info";
    //功能介绍
    public static String FunctionIntroduceUrl=mURL+"page_list";

    //************************************************
    //设置人生笔记密码
    public static String SetLifeNotePwdUrl2=mURL+"insert_personpass";
    //修改人生笔记密码
    public static String FixLifeNotePwdUrl2=mURL+"update_personpass";
    //清空人生笔密码码
    public static String CancelLifeNotePwdUrl2=mURL+"del_personpass";

    //验证密保人生笔密码码-part01
    public static String FindLifeNotePwd_ValidataAnswerUrl2=mURL+"find_personpass";

    //验证密保人生笔密码码-part02
    public static String FindLifeNotePwd_CreateNewPwdUrl2=mURL+"reset_personpass";



    //获取群主、会员信息
    public static String GroupInvitationUrl=mURL+"get_group_user_info";

    /**
     * 薛金柱
     */
    //微信登录
    public static String WX_LOGIN=mURL+"weixin_login";
    //微信注册
    public static String WX_REGISTER=mURL+"weixin_mobile_login";







}
