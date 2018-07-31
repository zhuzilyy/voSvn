package com.zl.vo_.widget.scan;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.R;
import com.zl.vo_.main.main_utils.MinPianConstant;
import com.zl.vo_.main.main_utils.ShareConstant;

/**
 * Created by Administrator on 2017/12/19.
 */

public class GameShareCharRow extends EaseChatRow {
    private ImageView head;
    private TextView nick;
    private TextView title;
    private String hxid;


    public GameShareCharRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        if(DemoHelper.getInstance().isShareTexteMessage(message)){
            inflater.inflate(message.direct()== EMMessage.Direct.RECEIVE?
                    R.layout.ease_row_received_share_call:R.layout.ease_row_send_share_call,this);
        }


    }

    @Override
    protected void onFindViewById() {
        title=findViewById(R.id.share_title);
        head=findViewById(R.id.share_head);
        nick=findViewById(R.id.share_info);




    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
        EMMessageBody textMessageBody=message.getBody();
        if(message.direct()==EMMessage.Direct.RECEIVE){

            if(message.getBooleanAttribute("ShareType",true)) {
                message.getStringAttribute(ShareConstant.myExtType, null);
                String gameAvatar=message.getStringAttribute(ShareConstant.gameAvatar,null);
                String gameContent=message.getStringAttribute(ShareConstant.gameContent,null);
                String gameName=message.getStringAttribute(ShareConstant.gameName,null);
                title.setText(gameName);
                Glide.with(GameShareCharRow.this).load(gameAvatar).into(head);

                nick.setText(gameContent);


                /****
                 *
                 *
                 message.setAttribute(ShareConstant.myExtType,"ShareType");//设置拓展字段
                 message.setAttribute(ShareConstant.packageName,package_name);  //包名
                 message.setAttribute(ShareConstant.gameAvatar,gameAvatar);//头像
                 message.setAttribute(ShareConstant.gameContent,game_content);//邀请信息
                 if(!TextUtils.isEmpty(param_a)){
                 message.setAttribute(ShareConstant.params_a,param_a);//参数01
                 }
                 if(!TextUtils.isEmpty(param_b)){
                 message.setAttribute(ShareConstant.params_b,param_b);//参数02
                 }

                 if(!TextUtils.isEmpty(param_c)){
                 message.setAttribute(ShareConstant.params_c,param_c);//参数03
                 }

                 if(!TextUtils.isEmpty(param_d)){
                 message.setAttribute(ShareConstant.params_d,param_d);//参数04
                 }

                 if(!TextUtils.isEmpty(param_e)){
                 message.setAttribute(ShareConstant.params_e,param_e);//参数05
                 }
                 message.setAttribute("avatar", myUtils.readUser(ChatActivity.this).getAvatar());//我的头像
                 message.setAttribute("nick",myUtils.readUser(ChatActivity.this).getNickname());//我的昵称

                 *
                 */

            }

        }else {
            if(message.getBooleanAttribute("ShareType",true)) {
                message.getStringAttribute(ShareConstant.myExtType, null);
                String gameAvatar=message.getStringAttribute(ShareConstant.gameAvatar,null);
                String gameContent=message.getStringAttribute(ShareConstant.gameContent,null);
                String gameName=message.getStringAttribute(ShareConstant.gameName,null);
                title.setText(gameName);
                Glide.with(GameShareCharRow.this).load(gameAvatar).into(head);

                nick.setText(gameContent);


            }
        }


    }

    @Override
    protected void onBubbleClick() {

    }
}
