package com.zl.vo_.main.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.zl.vo_.Constant;
import com.zl.vo_.R;
import com.zl.vo_.main.Fragmengs.ChatFragment;
import com.zl.vo_.main.main_fragments.MainActivity;
import com.zl.vo_.main.main_utils.MinPianConstant;
import com.zl.vo_.main.main_utils.ShareConstant;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.runtimepermissions.PermissionsManager;
import com.zl.vo_.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * chat activity，EaseChatFragment was used {@link #}
 *
 */
public class ChatActivity extends VoBaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    public String toChatUsername;
    public String account;
    public int chattype;
    public String isCard;
    public String isShare;

    private List<LocalMedia> selectList = new ArrayList<>();


    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);

        activityInstance = this;
        VoBaseActivity.addActivity(this);
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        account=getIntent().getExtras().getString("account");
        chattype=getIntent().getExtras().getInt(Constant.EXTRA_CHAT_TYPE);

//        /***
//         * 从UserDetailsActivityVo的发送名片
//         */
        isCard=getIntent().getExtras().getString("card");
        if("1".equals(isCard)){
            String from_avatar=getIntent().getStringExtra("from_avatar");
            String from_nick=getIntent().getStringExtra("from_nick");
            String from_accout=getIntent().getStringExtra("from_accout");
            String from_huanxinId=getIntent().getStringExtra("from_huanxinID");

            EMMessage message=EMMessage.createTxtSendMessage("[名片]",toChatUsername);
            message.setChatType(EMMessage.ChatType.Chat);
            message.setAttribute(MinPianConstant.myExtType,"mingpianType");//设置拓展字段
            message.setAttribute(MinPianConstant.huanxinID,from_huanxinId);  //环信id
            message.setAttribute(MinPianConstant.userAvater,from_avatar);//头像
            message.setAttribute(MinPianConstant.name,from_nick);//昵称
            message.setAttribute(MinPianConstant.cardUserPhone,from_accout);//账号
            message.setAttribute("account",from_accout);

            message.setAttribute("avatar", myUtils.readUser(ChatActivity.this).getAvatar());//我的头像
            message.setAttribute("nick",myUtils.readUser(ChatActivity.this).getNickname());//我的昵称
            try {
                EMClient.getInstance().chatManager().sendMessage(message);
            } catch (Exception e) {
                Log.i("ui", e.getMessage());
            }

        }


        /***
         * 发送分享
         */
        isShare=getIntent().getStringExtra("share");
        if("2".equals(isShare)){

            String gameAvatar=getIntent().getStringExtra("from_avatar");
            String package_name=getIntent().getStringExtra("package_name");
            String game_content=getIntent().getStringExtra("game_content");
            String game_name=getIntent().getStringExtra("game_name");
            String param_gameid=getIntent().getStringExtra("param_gameid");
            String param_roomid=getIntent().getStringExtra("param_roomid");
            String param_kindid=getIntent().getStringExtra("param_kindid");
//            String param_d=getIntent().getStringExtra("param_d");
//            String param_e=getIntent().getStringExtra("param_e");

            String from_nick=getIntent().getStringExtra("from_nick");
            EMMessage message=EMMessage.createTxtSendMessage("[分享]",toChatUsername);

            message.setAttribute(ShareConstant.myExtType,"ShareType");//设置拓展字段
            message.setAttribute(ShareConstant.packageName,package_name);  //包名
            message.setAttribute(ShareConstant.gameAvatar,gameAvatar);//头像
            message.setAttribute(ShareConstant.huanxinID,toChatUsername);
            message.setAttribute(ShareConstant.gameContent,game_content);//邀请信息
            message.setAttribute(ShareConstant.gameName,game_name);  //游戏名称
            if(!TextUtils.isEmpty(param_gameid)){
                message.setAttribute(ShareConstant.params_gameid,param_gameid);//参数01
            }
            if(!TextUtils.isEmpty(param_roomid)){
                message.setAttribute(ShareConstant.params_roomid,param_roomid);//参数02
            }

            if(!TextUtils.isEmpty(param_kindid)){
                message.setAttribute(ShareConstant.params_kindid,param_kindid);//参数03
            }

//            if(!TextUtils.isEmpty(param_d)){
//                message.setAttribute(ShareConstant.params_d,param_d);//参数04
//            }
//
//            if(!TextUtils.isEmpty(param_e)){
//                message.setAttribute(ShareConstant.params_e,param_e);//参数05
//            }
            message.setAttribute("avatar", myUtils.readUser(ChatActivity.this).getAvatar());//我的头像
            message.setAttribute("nick",myUtils.readUser(ChatActivity.this).getNickname());//我的昵称

            try{
                EMClient.getInstance().chatManager().sendMessage(message);
            }catch (Exception e){
                Log.i("ui",e.getMessage());
            }

        }

        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        //chatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


}
