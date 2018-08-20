package com.zl.vo_.main.activities;


import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.squareup.picasso.Picasso;
import com.zl.vo_.DemoApplication;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.db.UserDao;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.UserInfoEntity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.TimeUtil;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;
import com.zl.vo_.widget.RoundImageView;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/21.
 */

public class UserDetailsActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.moreInfo)
    public ImageView moreInfo;
    @BindView(R.id.re_moreInfo)
    public RelativeLayout re_moreInfo;
    @BindView(R.id.re_bigMore)
    public LinearLayout re_bigMore;

    @BindView(R.id.setAlias_re)
    public RelativeLayout setAlias_re;
    @BindView(R.id.sendCard_re)
    public RelativeLayout sendCard_re;
    @BindView(R.id.addBlacklist_re)
    public RelativeLayout addBlacklist_re;
    @BindView(R.id.Complaint_re)
    public RelativeLayout Complaint_re;
    @BindView(R.id.delete_re)
    public RelativeLayout delete_re;
    @BindView(R.id.ClearMsg_re)
    public RelativeLayout ClearMsg_re;

    @BindView(R.id.btn_msg)
    public Button btn_msg;
    @BindView(R.id.btn_add_address)
    public Button btn_addto_address;
    private String friend_hxaccount_;

    //-------------------------
    @BindView(R.id.iv_avatar)
    public CircleImageView avatar;
    @BindView(R.id.tv_name)
    public TextView tv_name;
    @BindView(R.id.tv_nike)
    public TextView tv_nike;
    @BindView(R.id.tv_voNum)
    public TextView tv_voNum;
    @BindView(R.id.tv_region)
    public TextView tv_region;
    @BindView(R.id.phone_re)
    public RelativeLayout phone_re;
    @BindView(R.id.tv_phone)
    public TextView tv_phone;


    @BindView(R.id.reason_re)
    public RelativeLayout reason_re;
    @BindView(R.id.cancel)
    public TextView tv_cancel;
    @BindView(R.id.send)
    public TextView tv_send;
    @BindView(R.id.reson_ed)
    public EditText reson_ed;
    @BindView(R.id.groupName_)
    public TextView selectgroup;
    @BindView(R.id.iv_sex)
    public ImageView iv_sex;
    @BindView(R.id.friend_big_re)
    public RelativeLayout friend_big_re;
    @BindView(R.id.friend_big_iv)
    public ImageView friend_big_iv;

    public    UserInfoEntity.UserInfo.UserFriendInfo friendInfo;
    public DemoModel demoModel;
    public UserDao userDao;
    public DemoDBManager dbManager=DemoDBManager.getInstance();
    //环信id
    private String frindHxID;
    //账号
    private String account;
    //添加方式
    private String applyWay;
    //是否是我的好友
    private String code;
    //是否是从search界面过来的
    private String search_;

    private String PassId;


    @BindView(R.id.re_switGroup)
    public RelativeLayout re_switGroup;
    //好友的userID
    public String userid;
    public UserInfoEntity.UserInfo.UserFriendInfo currentFriendInfo;
    private int REQUEST_CODE_SELEST_MINGPIAN=99;
    @BindView(R.id.loading_view)
    public  RelativeLayout loading_view;
    public  MyFrindEntivity friend;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_activity_userinfo);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        frindHxID=getIntent().getStringExtra("HXid");

        account=getIntent().getStringExtra("account");
        applyWay=getIntent().getStringExtra("way");
        code=getIntent().getStringExtra("code");
        PassId = getIntent().getStringExtra("passId");

        mInit();



    }

    /****
     *
     * @return
     */
    public void getData() {
        RequestParams params=new RequestParams(Url.FriendInfoURL);
        params.addParameter("huanxin_account",myUtils.readUser(UserDetailsActivityVo.this).getHuanxin_account());
        params.addParameter("friend_huanxin_account",frindHxID);
        x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
            @Override
            public void success(Result<UserInfoEntity> data) {
                UserInfoEntity userInfoEntity=  data.data;
                UserInfoEntity.UserInfo userInfo=userInfoEntity.getInfo();
                if(userInfo!=null){
                    friendInfo=  userInfo.getFriend_info();
                    if(friendInfo!=null){
                        userid=friendInfo.getUserid();
                        setCurrentData(friendInfo);
                        UpdataFriend(friendInfo);
                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("ui",ex.getMessage());
            }
        });

    }

    /****
     * 网络请求成功，更新数据库中该对象的信息
     * @param friendInfo
     */
    private void UpdataFriend(UserInfoEntity.UserInfo.UserFriendInfo friendInfo) {
        currentFriendInfo=friendInfo;
        MyFrindEntivity entivity=new MyFrindEntivity();
        entivity.setAccount(friendInfo.getAccount());
        entivity.setAvatar(friendInfo.getAvatar());
        entivity.setNike(friendInfo.getNickname());
        entivity.setPhone(friendInfo.getMobile());
        entivity.setPassid(friendInfo.getPassid());
        entivity.setRemark(friendInfo.getRemark());
        entivity.setAddress(friendInfo.getAddress());
        entivity.setSex(friendInfo.getSex());
        //更新数据
        boolean b=dbManager.updateMyFriendInfo(entivity);
        Log.i("jj","");


    }

    /****
     * 赋值当前界面的数据
     * @param friendInfo
     */
    private void setCurrentData(UserInfoEntity.UserInfo.UserFriendInfo friendInfo) {
        Picasso.with(UserDetailsActivityVo.this).load(friendInfo.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(avatar);
        tv_name.setText(friendInfo.getRemark());
        tv_nike.setText("昵称："+friendInfo.getNickname());
        tv_voNum.setText("VO号："+friendInfo.getAccount());
        tv_region.setText(!TextUtils.isEmpty(friendInfo.getAddress())?friendInfo.getAddress():"中国省市");
        selectgroup.setText(friendInfo.getGroupname());
        iv_sex.setImageResource("男".equals(friendInfo.getSex())?R.mipmap.nan:R.mipmap.nv);
        if(!"1".equals(friendInfo.getFriend_check()) || !TextUtils.isEmpty(PassId)){
            moreInfo.setVisibility(View.GONE);
            re_switGroup.setEnabled(false);

        }else {
            moreInfo.setVisibility(View.VISIBLE);
            re_switGroup.setEnabled(true);
        }
        /***
         * 这个好友如果在手机通讯录中，就显示他的电话号码
         */
       String currengUserPhone= friendInfo.getMobile();
        if(DemoApplication.Phones.contains(currengUserPhone)){
            phone_re.setVisibility(View.VISIBLE);
            tv_phone.setText(friendInfo.getMobile());
        }



    }

    private void mInit() {
        friend= dbManager.getFriendByHxID(frindHxID);


        if(friend!=null&& TextUtils.isEmpty(PassId)){
            //是我的朋友
            Picasso.with(UserDetailsActivityVo.this).load(friend.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(avatar);
            tv_name.setText(friend.getRemark());
            tv_nike.setText("昵称："+friend.getNike());
            tv_voNum.setText("vo号："+friend.getAccount());
            tv_region.setText(friend.getAddress());
            selectgroup.setText(friend.getGroupname());
            iv_sex.setImageResource("男".equals(friend.getSex())?R.mipmap.nan:R.mipmap.nv);

            String isFriend=friend.getFriend_check();

            btn_msg.setVisibility(View.VISIBLE);
            btn_addto_address.setVisibility(View.GONE);

            /***
             * 这个好友如果在手机通讯录中，就显示他的电话号码
             */
            if(DemoApplication.Phones.contains(friend.getPhone())){
                phone_re.setVisibility(View.VISIBLE);
                tv_phone.setText(friend.getPhone());
            }

        }else {
            //不是我的朋友
            btn_msg.setVisibility(View.GONE);
            btn_addto_address.setVisibility(View.VISIBLE);
        }

        //如果这个人是自己
        if(myUtils.readUser(UserDetailsActivityVo.this).getHuanxin_account().equals(frindHxID)){
            LoginData.LoginInfo.LoginAccountInfo user=myUtils.readUser(UserDetailsActivityVo.this);
            Picasso.with(UserDetailsActivityVo.this).load(user.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(avatar);
            tv_name.setText(user.getAccount());
            tv_nike.setText("昵称："+user.getNickname());
            tv_voNum.setText("vo号："+user.getHuanxin_account());
            tv_region.setText(user.getAddress());
            selectgroup.setVisibility(View.GONE);
            moreInfo.setVisibility(View.GONE);
            re_switGroup.setEnabled(false);

            phone_re.setVisibility(View.VISIBLE);
            tv_phone.setText(user.getMobile());
        }else {
            ///网络请求
            getData();
        }


    }
    @OnClick({R.id.moreInfo,R.id.re_moreInfo,R.id.re_bigMore,R.id.setAlias_re,
    R.id.sendCard_re,R.id.Complaint_re,R.id.addBlacklist_re,R.id.delete_re,R.id.btn_add_address,
    R.id.btn_msg,R.id.cancel,R.id.send,R.id.re_switGroup,R.id.ClearMsg_re,R.id.iv_avatar,R.id.friend_big_re})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.friend_big_re:
                friend_big_re.setVisibility(View.GONE);
                break;
            case R.id.iv_avatar:
                if(friend!=null||friendInfo!=null){
                    friend_big_re.setVisibility(View.VISIBLE);
                    if(friend!=null){
                        Picasso.with(UserDetailsActivityVo.this).load(friend.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(friend_big_iv);
                    }else {
                        Picasso.with(UserDetailsActivityVo.this).load(friendInfo.getAvatar()).placeholder(R.drawable.ease_default_avatar).into(friend_big_iv);
                    }

                }else {
                    Toast.makeText(this, "null************", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.moreInfo:
                re_bigMore.setVisibility(View.VISIBLE);
                break;
            case R.id.re_moreInfo:
                re_bigMore.setVisibility(View.GONE);
            //清空聊天记录
                break;
            case R.id.ClearMsg_re:
                re_bigMore.setVisibility(View.GONE);
                final Dialog dialog=new Dialog(UserDetailsActivityVo.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.lay_mdialog);
                dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EMConversation conversation= EMClient.getInstance().chatManager().getConversation(frindHxID);
                        conversation.clearAllMessages();
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.re_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            //设置备注
            case R.id.setAlias_re:
                re_bigMore.setVisibility(View.GONE);
                Intent intent_alias=new Intent(UserDetailsActivityVo.this,setAliasActivityVo.class);
                intent_alias.putExtra("friendUserId",currentFriendInfo.getUserid()) ;
                intent_alias.putExtra("remark",currentFriendInfo.getRemark());
                startActivityForResult(intent_alias,102);

                re_bigMore.setVisibility(View.GONE);
                break;
            //发送名片
            case R.id.sendCard_re:
                re_bigMore.setVisibility(View.GONE);
                Intent intent=new Intent(UserDetailsActivityVo.this,allContactsListActivityVo.class);
                //发送名片

                startActivityForResult(new Intent(UserDetailsActivityVo.this,selectContactsActivityVo.class),REQUEST_CODE_SELEST_MINGPIAN);
                re_bigMore.setVisibility(View.GONE);
                break;
            //加入黑名单
            case R.id.addBlacklist_re:
                re_bigMore.setVisibility(View.GONE);
                final Dialog dialog_blackList=new Dialog(UserDetailsActivityVo.this);
                dialog_blackList.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_blackList.setContentView(R.layout.lay_userdetails_blacklist);
                dialog_blackList.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addBlackList(myUtils.readUser(UserDetailsActivityVo.this).getHuanxin_account(),friendInfo.getHuanxin_account());
                        re_bigMore.setVisibility(View.GONE);
                        dialog_blackList.dismiss();
                    }
                });
                dialog_blackList.findViewById(R.id.re_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_blackList.dismiss();
                    }
                });
                dialog_blackList.show();

                break;
            //反馈
            case R.id.Complaint_re:
                re_bigMore.setVisibility(View.GONE);
                Intent intent2=new Intent(UserDetailsActivityVo.this,UserComplainsActivityVo.class);
                intent2.putExtra("complaint_userid",userid);
                startActivity(intent2);
                re_bigMore.setVisibility(View.GONE);
                break;
            //删除
            case R.id.delete_re:
                re_bigMore.setVisibility(View.GONE);
                final Dialog dialog_del=new Dialog(UserDetailsActivityVo.this);
                dialog_del.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog_del.setContentView(R.layout.lay_userdetails_del);
                dialog_del.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFriend(friendInfo.getUserid());
                        re_bigMore.setVisibility(View.GONE);
                        dialog_del.dismiss();
                    }
                });
                dialog_del.findViewById(R.id.re_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog_del.dismiss();
                    }
                });
                dialog_del.show();
                break;
            case R.id.btn_msg:
                //发送消息
               // Toast.makeText(this, "发送消息", Toast.LENGTH_SHORT).show();
                Intent intent_=new Intent(UserDetailsActivityVo.this, ChatActivity.class);
                intent_.putExtra("userId",frindHxID);
                intent_.putExtra("account",account);
                if(friendInfo!=null){
                    intent_.putExtra("remark",friendInfo.getRemark());
                }
                startActivity(intent_);
                finish();
                break;
            case R.id.btn_add_address:
                //添加到通讯录
                LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(UserDetailsActivityVo.this);
                if(currentUser.getHuanxin_account().equalsIgnoreCase(frindHxID)){
                    Toast.makeText(this, "不能添加自己", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    //本地判断当前添加方式能不能添加
                    boolean b=adjustAddWay(applyWay);
                    if(b){
                        //可以添加，验证对方需不需要验证
                        if("1".equals(friendInfo.getFriend_apply())){
                            //需要验证，弹出理由框
                            final Dialog dialog2 = new Dialog(UserDetailsActivityVo.this);
                            View vv = LayoutInflater.from(UserDetailsActivityVo.this).inflate(R.layout.lay_yanzhen, null);
                            dialog2.setContentView(vv);
                            final TextView tv_yanzhenContent = vv.findViewById(R.id.tv_yanzhenContent);
                            TextView confirm = vv.findViewById(R.id.tv_confrim);
                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(!TextUtils.isEmpty(PassId)){
                                        Toast.makeText(UserDetailsActivityVo.this, "验证消息已发送,等等对方验证", Toast.LENGTH_SHORT).show();
                                        dialog2.dismiss();
                                        return;
                                    }

                                    String content = tv_yanzhenContent.getText().toString().trim();
                                    sendReasonForNewFriend(content);
                                    dialog2.dismiss();
                                }
                            });
                            vv.findViewById(R.id.re_cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog2.dismiss();
                                }
                            });
                            dialog2.show();
                        }else {
                            if(!TextUtils.isEmpty(PassId)){
                                Toast.makeText(this, "验证消息已发送,等等对方验证", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //不需要验证
                            RequestParams params=new RequestParams(Url.NativeAddFriendUrl);
                            params.addParameter("huanxin_account",myUtils.readUser(UserDetailsActivityVo.this).getHuanxin_account());
                            params.addParameter("friend_huanxin_account",friendInfo.getHuanxin_account());
                            x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
                                @Override
                                public void success(Result<UserInfoEntity> data) {
                                    UserInfoEntity userInfoEntity= data.data;
                                    UserInfoEntity.UserInfo userInfo=userInfoEntity.getInfo();
                                    if(userInfo!=null){
                                        UserInfoEntity.UserInfo.UserFriendInfo friend_info=userInfo.getFriend_info();
                                       // Toast.makeText(UserDetailsActivityVo.this, "好友添加走到成功的方法里", Toast.LENGTH_SHORT).show();
                                        //这儿不需要做处理，会在demolHelper的好友监听中，”好友接收“中处理，
                                        //保存到数据库
                                      //  saveContact(friend_info);
                                    }
                                }
                                @Override
                                public void error(Throwable ex, boolean isOnCallback) {
                                    Log.i("err",ex.getMessage());

                                }
                            });

                        }

                    }else {
                        Toast.makeText(this, "查无此人", Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            case R.id.re_switGroup:
                Intent intent1=new Intent(UserDetailsActivityVo.this,GroupListNameActivityVo.class);
                intent1.putExtra("account",account);
                startActivityForResult(intent1,102);
                break;
            default:
                break;
        }
    }

    /***
     * 发送好友申请
     * @param content
     */
    private void sendReasonForNewFriend(final String content) {
        new Thread(new Runnable() {
            public void run() {

                try {
                  //  String reson=reson_ed.getText().toString().trim();
                    EMClient.getInstance().contactManager().addContact(friendInfo.getHuanxin_account(),content);
                    runOnUiThread(new Runnable() {
                        public void run() {

                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                            reason_re.setVisibility(View.GONE);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {

                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                            reason_re.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 200:
                //切换分组完成
                String groupName_Str=data.getStringExtra("groupName");
                selectgroup.setText(groupName_Str);
                DemoHelper.textnum=3;
                break;
            case 201:
                String remark=data.getStringExtra("allas");
                tv_name.setText(remark);
                break;
            case -1:
                //转发名片
                Bundle bundle=data.getExtras();//获取从selectContactActivity界面传回的数据
                MyFrindEntivity friend= (MyFrindEntivity) bundle.getSerializable("ssss");
                //要发给谁
                String username=friend.getAccount();
                String userhxid=friend.getHuanxinID();

                Intent intent=new Intent(UserDetailsActivityVo.this, ChatActivity.class);
                intent.putExtra("userId",userhxid);
                intent.putExtra("account",username);
                intent.putExtra("remark",!TextUtils.isEmpty(friend.getRemark())?friend.getRemark():friend.getNike());


                String from_avatar=friendInfo.getAvatar();
                String from_nick=friendInfo.getNickname();
                String from_account=friendInfo.getAccount();
                String from_hxid=friendInfo.getHuanxin_account();

                //名片上的内容
                intent.putExtra("from_avatar",from_avatar);
                intent.putExtra("from_nick",from_nick);
                intent.putExtra("from_accout",from_account);
                intent.putExtra("from_huanxinID",from_hxid);
                intent.putExtra("card","1");
                DemoApplication.nn=205;
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /****
     * 判断当前的方法可不可以添加
     * @param applyWay
     * @return
     */
    private boolean adjustAddWay(String applyWay) {
        //账号添加
        if(applyWay.equals("apply_account")){
            String isAccount=friendInfo.getApply_account();
            if(isAccount.equals("1")){
                return true;

            }else {
                return false;
            }
        }
        //手机添加
        if(applyWay.equals("apply_mobile")){
            String isMobile=friendInfo.getApply_mobile();
            if(isMobile.equals("1")){
                return true;
            }else {
                return false;
            }
        }
        //群聊添加
        if(applyWay.equals("apply_group")){
            String isMobile=friendInfo.getApply_group();
            if(isMobile.equals("1")){
                return true;
            }else {
                return false;
            }
        }

        //二维码添加
        if(applyWay.equals("apply_erweima")){
            String isMobile=friendInfo.getApply_erweima();
            if(isMobile.equals("1")){
                return true;

            }else {
                return false;
            }
        }



        //名片添加
        if(applyWay.equals("apply_card")){
            String isMobile=friendInfo.getApply_erweima();
            if(isMobile.equals("1")){
                return true;

            }else {
                return false;
            }
        }




        return false;

    }



    /****
     * 将好友加入黑名单
     * @param huanxin_account
     * @param currentUserHxId
     */
    private void addBlackList(String huanxin_account, final String currentUserHxId) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.addToBlackListUrl);
        params.addParameter("huanxin_account",huanxin_account);
        params.addParameter("friend_huanxin_account",currentUserHxId);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    Intent intent=new Intent("needRefresh");
                    intent.putExtra("hxid",currentUserHxId);
                    sendBroadcast(intent);
                    startActivity(new Intent(UserDetailsActivityVo.this, MainActivity.class));
                    finish();
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Log.i("addBlackList",ex.getMessage());
            }
        });
    }

    /****
     * 点击删除按钮，删除好友
     */
    private void deleteFriend(String friendUserId) {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.DeleteFriendURL);
        params.addParameter("userid",myUtils.readUser(UserDetailsActivityVo.this).getUserid());
        params.addParameter("friend_userid",friendUserId);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                Toast.makeText(UserDetailsActivityVo.this, data.info, Toast.LENGTH_SHORT).show();
                if("0".equals(data.code)){
                    DemoHelper.textnum=1;
                    Intent intent=new Intent("needRefresh");

                    sendBroadcast(intent);
                    startActivity(new Intent(UserDetailsActivityVo.this, MainActivity.class));
                    finish();

                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
                Log.i("uu",ex+"==删除好友失败");
            }
        });

    }




}
