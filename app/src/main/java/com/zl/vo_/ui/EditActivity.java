package com.zl.vo_.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.GroupAvatarData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


public class EditActivity extends BaseActivity{
	private ImageView groupAvatar;
	private String GroupId;
	private RelativeLayout loadingView;

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
				//头像
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


			}
		});



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



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 1:

					//相册返回
					//获取访问资源[选中的图片]的uri
					Uri uri = data.getData();
					//将uri解析成为bitmap
					Bitmap bitmap_album = BitmapFactory.decodeFile(uri.getPath());
					//进行裁剪
					CutPic(uri);

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


					//进行裁剪
					CutPic(camera_uri);
					groupAvatar.setImageBitmap(bitmap2);
					//将bitmap转换为drawable
//                    Drawable drawable=new BitmapDrawable(bitmap2);
//                    head.setBackground(drawable);

					break;


				case 3:
					//裁剪返回

					if (data != null) {

						Bitmap bitmap = data.getParcelableExtra("data");
						Bitmap temps = zoomImage(bitmap, 500,500);
						Log.i("base","Height=" + temps.getHeight() + "      Width=" + temps.getWidth());
						groupAvatar.setImageBitmap(temps);
                      /*  mmvv.setImageBitmap(temps);*/
						//将bitmap转换为drawable
//                        Drawable drawable2=new BitmapDrawable(temps);
//                        head.setBackground(drawable2);
					}else {



					}

					break;
				default:
					break;
			}

		}

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
