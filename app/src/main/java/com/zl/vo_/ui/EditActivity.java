package com.zl.vo_.ui;

import android.Manifest;
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

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.GroupAvatarData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.LifeNote;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.PhotoUtils;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EditActivity extends VoBaseActivity{
	private ImageView groupAvatar;
	private String GroupId;
	private RelativeLayout loadingView;

	//***************************************************************7.0拍照相册
	private static final int CODE_GALLERY_REQUEST = 0xa0;
	private static final int CODE_CAMERA_REQUEST = 0xa1;
	private static final int CODE_RESULT_REQUEST = 0xa2;
	private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
	private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
	private Uri imageUri;
	private Uri cropImageUri;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.em_activity_edit);
		GroupId=getIntent().getStringExtra("groupId");
		loadingView=findViewById(R.id.loading_view);
		String GroupAvatarStr=getIntent().getStringExtra("groupAvatar");
		((TextView)findViewById(R.id.tv_title)).setText("群头像设置");
		groupAvatar=findViewById(R.id.iv_avatar);

		if(!TextUtils.isEmpty(GroupAvatarStr)){
			Glide.with(EditActivity.this).load(GroupAvatarStr).into(groupAvatar);
		}
		//头像点击事件
		groupAvatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				List<String> strings = new ArrayList<>();
				strings.add("相册");
				strings.add("照相机");
				DialogUIUtils.showBottomSheetAndCancel(EditActivity.this, strings, "取消", new DialogUIItemListener() {
					@Override
					public void onItemClick(CharSequence text, int position) {

						switch(position){
							case 0:
								requestPermissions(EditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new VoBaseActivity.RequestPermissionCallBack() {
									@Override
									public void granted() {
										PhotoUtils.openPic(EditActivity.this, CODE_GALLERY_REQUEST);
									}
									@Override
									public void denied() {
										Toast.makeText(EditActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
									}
								});
								break;
							case 1:
								//指定action   [照相机]
								requestPermissions(EditActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new VoBaseActivity.RequestPermissionCallBack() {
									@Override
									public void granted() {
										if (hasSdcard()) {
											imageUri = Uri.fromFile(fileUri);
											if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
												//通过FileProvider创建一个content类型的Uri
												imageUri = FileProvider.getUriForFile(EditActivity.this, "com.zl.vo_.fileprovider", fileUri);
											PhotoUtils.takePicture(EditActivity.this, imageUri, CODE_CAMERA_REQUEST);
										} else {
											Toast.makeText(EditActivity.this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
											Log.e("asd", "设备没有SD卡");
										}
									}
									@Override
									public void denied() {
										Toast.makeText(EditActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
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



	/*			//头像
				List<String> strings = new ArrayList<>();
				strings.add("相册");
				strings.add("照相机");

				DialogUIUtils.showBottomSheetAndCancel(EditActivity.this, strings, "取消", new DialogUIItemListener() {
					//
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
							default:
								break;
						}
					}
					@Override
					public void onBottomBtnClick() {
					}
				}).show();


			*/


			}
		});



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
		int output_X = 1120, output_Y = 1120;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case CODE_CAMERA_REQUEST://拍照完成回调
					cropImageUri = Uri.fromFile(fileCropUri);
					PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
					break;
				case CODE_GALLERY_REQUEST://访问相册完成回调
					if (hasSdcard()) {
						cropImageUri = Uri.fromFile(fileCropUri);
						Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
							newUri = FileProvider.getUriForFile(this, "com.zl.vo_.fileprovider", new File(newUri.getPath()));
						PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
					} else {
						Toast.makeText(EditActivity.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
					}
					break;
				case CODE_RESULT_REQUEST:
					Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
					if (bitmap != null) {
						showImages(bitmap);
						//setLifeNoteBg(bitmap);
					}
					break;
			}
		}
	}

	private void showImages(Bitmap bitmap) {
		groupAvatar.setImageBitmap(bitmap);
	}

	public void save(View view){
		//保存群组头像并刷新
		Bitmap bitmap=getBitmap();
		String base64_= myUtils.Bitmap2StrByBase64(bitmap);
		loadingView.setVisibility(View.VISIBLE);


		RequestParams params=new RequestParams(Url.UploadGroupAvatar);
		params.addParameter("img_data",base64_);
		x.http().post(params, new MyCommonCallback<Result<GroupAvatarData>>() {
			@Override
			public void success(Result<GroupAvatarData> data_) {
				GroupAvatarData avatarData=data_.data;
				GroupAvatarData.GroupAvatarInfo avatarInfo=avatarData.getInfo();
				final String groupUrl=avatarInfo.getImg_url();
				if(!TextUtils.isEmpty(groupUrl)){
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								EMClient.getInstance().groupManager().changeGroupDescription(GroupId,groupUrl);
								loadingView.setVisibility(View.VISIBLE);
								finish();
							} catch (HyphenateException e) {
								Log.i("err",e.getMessage());

								e.printStackTrace();
							}
						}
					}).start();
				}

			}

			@Override
			public void error(Throwable ex, boolean isOnCallback) {
				Log.i("eff",ex.getMessage());
				loadingView.setVisibility(View.VISIBLE);
			}
		});






	}

	public void back(View view) {
		finish();
	}

	private Bitmap getBitmap(){
		Drawable drawable = groupAvatar.getDrawable();
		Bitmap bitmap = myUtils.drawableToBitmap(drawable);
		return bitmap;

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
       /* intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);*/
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
