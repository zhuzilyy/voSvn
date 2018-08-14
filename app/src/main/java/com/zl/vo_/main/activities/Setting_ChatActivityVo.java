package com.zl.vo_.main.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseSwitchButton;
import com.zl.vo_.DemoModel;
import com.zl.vo_.R;
import com.zl.vo_.main.main_utils.PhotoUtils;

import java.io.File;
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

    //***************************************************************7.0拍照相册
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;




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
            //设置聊天背景
            selectChatBackGround();
            break;
        case R.id.clearAllmsg_re:
            //清空所有聊天记录
          //  clearMsg_re.setVisibility(View.VISIBLE);
            clearAllMsg();

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
     * 清空所有消息
     */
    private void clearAllMsg() {

        final Dialog dialog = new Dialog(Setting_ChatActivityVo.this);
        View vv = LayoutInflater.from(Setting_ChatActivityVo.this).inflate(R.layout.lay_clearallmsg, null);
        dialog.setContentView(vv);
        ImageView cancel = vv.findViewById(R.id.cancel_iv);
        TextView confirm = vv.findViewById(R.id.tv_confrim);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllmsg();
                dialog.dismiss();
            }
        });
        dialog.show();

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
                        requestPermissions(Setting_ChatActivityVo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                            @Override
                            public void granted() {
                                PhotoUtils.openPic(Setting_ChatActivityVo.this, CODE_GALLERY_REQUEST);
                            }

                            @Override
                            public void denied() {
                                Toast.makeText(Setting_ChatActivityVo.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 1:
                        //指定action   [照相机]
                        requestPermissions(Setting_ChatActivityVo.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                            @Override
                            public void granted() {
                                if (hasSdcard()) {
                                    imageUri = Uri.fromFile(fileUri);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                        //通过FileProvider创建一个content类型的Uri
                                        imageUri = FileProvider.getUriForFile(Setting_ChatActivityVo.this, "com.zl.vo_.fileprovider", fileUri);
                                    PhotoUtils.takePicture(Setting_ChatActivityVo.this, imageUri, CODE_CAMERA_REQUEST);
                                } else {
                                    Toast.makeText(Setting_ChatActivityVo.this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                                    Log.e("asd", "设备没有SD卡");
                                }
                            }

                            @Override
                            public void denied() {
                                Toast.makeText(Setting_ChatActivityVo.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                            }
                        });
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

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    String camera_uri =  getRealPathFromURI(cropImageUri);
                    Log.i("ss",camera_uri);
                    settingsModel.setChatBackGroundPicUrl(camera_uri+"");
                    String imgurl=settingsModel.getChatBackGroundPicUrl();
                    Log.i("ss",imgurl);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.zl.vo_.fileprovider", new File(newUri.getPath()));
                        String picpath =  getRealPathFromURI(cropImageUri);
                        Log.i("ss",picpath);
                        settingsModel.setChatBackGroundPicUrl(picpath+"");
                        Log.i("ss",picpath);
                    } else {
                        Toast.makeText(Setting_ChatActivityVo.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
                    }
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
