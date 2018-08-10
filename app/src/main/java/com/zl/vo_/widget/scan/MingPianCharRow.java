package com.zl.vo_.widget.scan;

import android.content.Context;
import android.content.Entity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.util.NetUtils;
import com.zl.vo_.DemoHelper;
import com.zl.vo_.R;
import com.zl.vo_.main.main_utils.MinPianConstant;
import com.zl.vo_.widget.RoundImageView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/12/19.
 */
public class MingPianCharRow extends EaseChatRow {
    private CircleImageView head;
    private TextView nick;
    private TextView account;
    private String hxid;
    private TextView percentage;


    public MingPianCharRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        if(DemoHelper.getInstance().isMingPianTexteMessage(message)){
            inflater.inflate(message.direct()== EMMessage.Direct.RECEIVE?
                    R.layout.ease_row_received_mingpian_call:R.layout.ease_row_send_mingpian_call,this);
        }
    }

    @Override
    protected void onFindViewById() {
        head=findViewById(R.id.card_head);
        nick=findViewById(R.id.tv_nick);
        account=findViewById(R.id.tv_account);




    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
        EMMessageBody textMessageBody=message.getBody();
        if(message.direct()==EMMessage.Direct.RECEIVE){

            if(message.getBooleanAttribute("mingpianType",true)) {
                message.getStringAttribute(MinPianConstant.myExtType, null);
                hxid = message.getStringAttribute(MinPianConstant.huanxinID, null);
                String headurl=message.getStringAttribute(MinPianConstant.userAvater,null);
                String nickStr=message.getStringAttribute(MinPianConstant.name,null);
                String accountStr=message.getStringAttribute(MinPianConstant.cardUserPhone,null);
                Glide.with(MingPianCharRow.this).load(headurl).into(head);

                nick.setText(nickStr);
                account.setText(accountStr);
            }

        }else {
            if(message.getBooleanAttribute("mingpianType",true)) {

                message.getStringAttribute(MinPianConstant.myExtType, null);
                hxid = message.getStringAttribute(MinPianConstant.huanxinID, null);
                String headurl=message.getStringAttribute(MinPianConstant.userAvater,null);
                String nickStr=message.getStringAttribute(MinPianConstant.name,null);
                String accountStr=message.getStringAttribute(MinPianConstant.cardUserPhone,null);
                Glide.with(MingPianCharRow.this).load(headurl).into(head);

                nick.setText(nickStr);
                account.setText(accountStr);
            }
        }


    }

    @Override
    protected void onBubbleClick() {

    }
}
