package com.zl.vo_.main.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.squareup.picasso.Picasso;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.PhotoUtils;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/22.
 */

public class FixAvatarActivityVo extends VoBaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_avatar)
    public ImageView avatar;
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.tv_save)
    public TextView tv_save;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

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
        setContentView(R.layout.lay_fixavatar);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(FixAvatarActivityVo.this);

        if(currentUser!=null){

            if(!TextUtils.isEmpty(currentUser.getAvatar())){
                //默认头像
                Picasso.with(FixAvatarActivityVo.this).load(myUtils.readUser(FixAvatarActivityVo.this).getAvatar())
                        .placeholder(R.drawable.fx_default_useravatar).into(avatar);
            }
        }
    }
    @OnClick({R.id.iv_avatar,R.id.iv_back,R.id.tv_save})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_avatar:
                //头像
                /*List<String> strings = new ArrayList<>();
                strings.add("相册");
                strings.add("照相机");

                DialogUIUtils.showBottomSheetAndCancel(FixAvatarActivityVo.this, strings, "取消", new DialogUIItemListener() {
              //
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        switch(position){
                            case 0:
                                //指定action   Intent.ACTION_PICK[图像]
                                Intent intent_album = new Intent(Intent.ACTION_PICK);
                                //设置类型
                                intent_album.setType("image*//*");
                                startActivityForResult(intent_album, 1);
                                break;
                            case 1:
                                //指定action   [照相机]
                                Intent intent_camera = new Intent("android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(intent_camera, 2);
                                break;
                            default:
                                break;
                        }
                    }
                    @Override
                    public void onBottomBtnClick() {
                    }
                }).show();*/


                List<String> strings = new ArrayList<>();
                strings.add("相册");
                strings.add("照相机");

                DialogUIUtils.showBottomSheetAndCancel(FixAvatarActivityVo.this, strings, "取消", new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {

                        switch(position){
                            case 0:

                                requestPermissions(FixAvatarActivityVo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                                    @Override
                                    public void granted() {
                                        PhotoUtils.openPic(FixAvatarActivityVo.this, CODE_GALLERY_REQUEST);
                                    }

                                    @Override
                                    public void denied() {
                                        Toast.makeText(FixAvatarActivityVo.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            case 1:
                                //指定action   [照相机]
                                requestPermissions(FixAvatarActivityVo.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                                    @Override
                                    public void granted() {
                                        if (hasSdcard()) {
                                            imageUri = Uri.fromFile(fileUri);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                                //通过FileProvider创建一个content类型的Uri
                                                imageUri = FileProvider.getUriForFile(FixAvatarActivityVo.this, "com.zl.vo_.fileprovider", fileUri);
                                            PhotoUtils.takePicture(FixAvatarActivityVo.this, imageUri, CODE_CAMERA_REQUEST);
                                        } else {
                                            Toast.makeText(FixAvatarActivityVo.this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                                            Log.e("asd", "设备没有SD卡");
                                        }
                                    }

                                    @Override
                                    public void denied() {
                                        Toast.makeText(FixAvatarActivityVo.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            default:
                                break;
                        }

                    }

                    @Override
                    public void onBottomBtnClick() {
                    }
                }).show();

                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:

                loading_view.setVisibility(View.VISIBLE);

                //保存头像

                Bitmap bitmap=getBitmap();
                String base64_=myUtils.Bitmap2StrByBase64(bitmap);
                Log.i("base",base64_+ "==");

                RequestParams params=new RequestParams(Url.UpdateBaseInfoUrl);
                params.addParameter("userid",myUtils.readUser(FixAvatarActivityVo.this).getUserid());
                params.addParameter("avatar_data",base64_);

                x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
                    @Override
                    public void success(Result<LoginData> data) {
                        loading_view.setVisibility(View.GONE);
                        LoginData loginData=data.data;
                        LoginData.LoginInfo loginInfo=loginData.getInfo();
                        if(loginInfo!=null){
                            LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                            if(user!=null){
                              Log.i("uu","pp=="+user.getAvatar());


                                //清除本地保存的用户信息
                                myUtils.clearSharedUser(FixAvatarActivityVo.this);
                                myUtils.saveUser(user,FixAvatarActivityVo.this);
                                String hh= myUtils.readUser(FixAvatarActivityVo.this).getAvatar();
                                Log.i("pp","头像保存成功"+hh);
                                sendBroadcast(new Intent("FixPersonInfoOK"));
                                finish();






                            }
                        }
                    }

                    @Override
                    public void error(Throwable ex, boolean isOnCallback) {
                        loading_view.setVisibility(View.GONE);
                    }
                });




            default:
                break;
        }
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


    private Bitmap  getBitmap(){
        Drawable drawable = avatar.getDrawable();
        Bitmap bitmap = myUtils.drawableToBitmap(drawable);
        return bitmap;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 0, 0, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.zl.vo_.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 0, 0, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(FixAvatarActivityVo.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);

                    }
                    break;
            }
        }
    }

    private void showImages(Bitmap bitmap) {
        avatar.setImageBitmap(bitmap);
    }

    /**
     * 将图片裁剪到指定大小
     *
     * @param uri
     */
    public void CutPic(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);// 设置Intent中的view是可以裁剪的
        // 设置宽高比
       intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置裁剪图片的宽高
        intent.putExtra("outputX", 360);
        intent.putExtra("outputY", 360);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        // 设置是否返回数据
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为3
        startActivityForResult(intent, 3);

    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

}
