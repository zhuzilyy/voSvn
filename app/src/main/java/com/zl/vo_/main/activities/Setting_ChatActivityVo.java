package com.zl.vo_.main.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class Setting_ChatActivityVo extends VoBaseActivity implements View.OnClickListener{
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.switch_Usehandset)
    public EaseSwitchButton speakerSwitch;
    public DemoModel settingsModel;
    @BindView(R.id.setChatBackground_re)
    public RelativeLayout setChatBackground_re;
    @BindView(R.id.clearAllmsg_re)
    public RelativeLayout clearAllmsg_re;
    //-------------------------
    @BindView(R.id.clearMsg_re)
    public RelativeLayout clearMsg_re;
    @BindView(R.id._cancel)
    public Button _cancel;
    @BindView(R.id._confirm)
    public Button _confirm;

    public DemoModel demoModel=new DemoModel(Setting_ChatActivityVo.this);




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_seting_chat);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        DialogUIUtils.init(Setting_ChatActivityVo.this);

        settingsModel=new DemoModel(Setting_ChatActivityVo.this);
        // the speaker is switched on or not?
        if (settingsModel.getSettingMsgSpeaker()) {
            speakerSwitch.openSwitch();
        } else {
            speakerSwitch.closeSwitch();
        }
    }
    @OnClick({R.id.iv_back,R.id.switch_Usehandset,R.id.setChatBackground_re,R.id.clearAllmsg_re,
    R.id.clearMsg_re,R.id._cancel,R.id._confirm})
    @Override
    public void onClick(View v) {
    switch(v.getId()){
        case R.id.iv_back:
            finish();
            break;
        case R.id.switch_Usehandset:
            if (speakerSwitch.isSwitchOpen()){
                speakerSwitch.closeSwitch();
                settingsModel.setSettingMsgSpeaker(false);

            } else {
                speakerSwitch.openSwitch();
                settingsModel.setSettingMsgSpeaker(true);
            }

            break;
        case R.id.setChatBackground_re:
            selectChatBackGround();


            break;
        case R.id.clearAllmsg_re:
            //清空所有聊天记录
            clearMsg_re.setVisibility(View.VISIBLE);

            break;
        case R.id.clearMsg_re:
            clearMsg_re.setVisibility(View.GONE);
            break;
        case R.id._cancel:
            clearMsg_re.setVisibility(View.GONE);
            break;
        case R.id._confirm:
            //清空所有聊天记录
            clearMsg_re.setVisibility(View.GONE);
            clearAllmsg();
            break;

        default:
            break;
    }
    }

    /***
     * 清空所有聊天记录
     */
    private void clearAllmsg() {

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

        try {
            for (EMConversation conversation : conversations.values()) {
                conversation.clearAllMessages();
            }
            sendBroadcast(new Intent("updateRemark"));
            Toast.makeText(this, "清除成功", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }


    }


    /****
     * 选择聊天背景图
     */
    private void selectChatBackGround() {

        //头像
        List<String> strings = new ArrayList<>();
        strings.add("相册");
        strings.add("照相机");
        strings.add("取消背景");

        DialogUIUtils.showBottomSheetAndCancel(Setting_ChatActivityVo.this, strings, "取消", new DialogUIItemListener() {
            @Override
            public void onItemClick(CharSequence text, int position) {

                switch(position){
                    case 0:

                        //指定action   Intent.ACTION_PICK[图像]
                        Intent intent_album = new Intent(Intent.ACTION_PICK);
                        //设置类型
                        intent_album.setType("image/*");
                        startActivityForResult(intent_album, 1);

                        break;
                    case 1:
                        //指定action   [照相机]
                        Intent intent_camera = new Intent("android.media.action.IMAGE_CAPTURE");
                        startActivityForResult(intent_camera, 2);
                        break;
                    case 2:
                        //取消背景
                        settingsModel.setChatBackGroundPicUrl("");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onBottomBtnClick() {
            }
        }).show();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:

                    //相册返回
                    //获取访问资源[选中的图片]的uri
                    Uri uri = data.getData();

//                    String picPath= uri.getPath();
//
                   String img_path= getRealPathFromURI(uri);


                    settingsModel.setChatBackGroundPicUrl(img_path);
                    String imgurl=settingsModel.getChatBackGroundPicUrl();



                    break;
                case 2:
                    //照相机返回

                    /***
                     * 从Android 4.2之后，data.getdata(),不能够获取图片资源的路径[uri]
                     * 需要采用下面的方式
                     */
                    Uri camera_uri;
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap2 = (Bitmap) bundle.get("data");

                    if (data.getData() != null) {
                        camera_uri = data.getData();

                    } else {
                        camera_uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap2, null, null));
                    }

                   // String picPath_=camera_uri.getPath()+".jpg";


                    settingsModel.setChatBackGroundPicUrl(camera_uri+"");
                    Log.i("address",camera_uri+"");
                    break;




                default:
                    break;
            }

        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

        if(cursor!=null){
            if(cursor.moveToFirst()){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
        }else {
            res= contentUri.getPath();
        }


        return res;
    }


}
