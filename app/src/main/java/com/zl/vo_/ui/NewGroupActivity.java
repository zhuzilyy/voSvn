/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zl.vo_.ui;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager.EMGroupStyle;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.exceptions.HyphenateException;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.GroupAvatarData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.activities.FixAvatarActivityVo;
import com.zl.vo_.main.activities.VoBaseActivity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.PhotoUtils;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.main.views.MEditText;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NewGroupActivity extends VoBaseActivity {
	private MEditText groupNameEditText;
	private ProgressDialog progressDialog;
	private EditText introductionEditText;
	private CheckBox publibCheckBox;
	private CheckBox memberCheckbox;
	private TextView secondTextView;

	private ImageView edit_group_avatar;
	//***************************************************************7.0拍照相册
	private static final int CODE_GALLERY_REQUEST = 0xa0;
	private static final int CODE_CAMERA_REQUEST = 0xa1;
	private static final int CODE_RESULT_REQUEST = 0xa2;
	private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
	private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
	private Uri imageUri;
	private Uri cropImageUri;



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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_group);
		VoBaseActivity.addActivity(this);
		groupNameEditText = findViewById(R.id.edit_group_name);
		introductionEditText = (EditText) findViewById(R.id.edit_group_introduction);
		publibCheckBox = (CheckBox) findViewById(R.id.cb_public);
		memberCheckbox = (CheckBox) findViewById(R.id.cb_member_inviter);
		secondTextView = (TextView) findViewById(R.id.second_desc);

		edit_group_avatar=findViewById(R.id.edit_group_avatar);

		publibCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

		    @Override
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if(isChecked){
		            secondTextView.setText(R.string.join_need_owner_approval);
		        }else{
                    secondTextView.setText(R.string.Open_group_members_invited);
		        }
		    }
		});

		edit_group_avatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//头像
				List<String> strings = new ArrayList<>();
				strings.add("相册");
				strings.add("照相机");

				DialogUIUtils.showBottomSheetAndCancel(NewGroupActivity.this, strings, "取消", new DialogUIItemListener() {
					@Override
					public void onItemClick(CharSequence text, int position) {

						switch(position){
							case 0:
								requestPermissions(NewGroupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
									@Override
									public void granted() {
										PhotoUtils.openPic(NewGroupActivity.this, CODE_GALLERY_REQUEST);
									}

									@Override
									public void denied() {
										Toast.makeText(NewGroupActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
									}
								});
								break;
							case 1:
								//指定action   [照相机]
								requestPermissions(NewGroupActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
									@Override
									public void granted() {
										if (hasSdcard()) {
											imageUri = Uri.fromFile(fileUri);
											if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
												//通过FileProvider创建一个content类型的Uri
												imageUri = FileProvider.getUriForFile(NewGroupActivity.this, "com.zl.vo_.fileprovider", fileUri);
											PhotoUtils.takePicture(NewGroupActivity.this, imageUri, CODE_CAMERA_REQUEST);
										} else {
											Toast.makeText(NewGroupActivity.this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
											Log.e("asd", "设备没有SD卡");
										}
									}

									@Override
									public void denied() {
										Toast.makeText(NewGroupActivity.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
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
			}
		});
	}
	
	/**
	 * @param v
	 */
	public void save(View v) {
		String name = groupNameEditText.getText().toString();
		if (TextUtils.isEmpty(name)) {
		    new EaseAlertDialog(this, R.string.Group_name_cannot_be_empty).show();
		} else {
			// select from contact list
			startActivityForResult(new Intent(this, GroupPickContactsActivity.class).putExtra("groupName", name), 200);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String st1 = getResources().getString(R.string.Is_to_create_a_group_chat);
		int output_X = 480, output_Y = 480;
		final String st2 = getResources().getString(R.string.Failed_to_create_groups);
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
						Toast.makeText(NewGroupActivity.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
					}
					break;
				case CODE_RESULT_REQUEST:
					Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
					if (bitmap != null) {
						showImages(bitmap);

					}
					break;
				case 200:
					Bitmap bitmap2=getBitmap();
					String base64_= myUtils.Bitmap2StrByBase64(bitmap2);
					//new group
					progressDialog = new ProgressDialog(this);
					progressDialog.setMessage(st1);
					progressDialog.setCanceledOnTouchOutside(false);
					progressDialog.show();
					//上传群头像
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
										final String groupName = groupNameEditText.getText().toString().trim();
										///String desc = introductionEditText.getText().toString();
										String[] members = data.getStringArrayExtra("newmembers");
										try {
											EMGroupOptions option = new EMGroupOptions();
											option.maxUsers = 200;
											option.inviteNeedConfirm = true;
											String desc=groupUrl;
											String reason = NewGroupActivity.this.getString(R.string.invite_join_group);
											reason  = EMClient.getInstance().getCurrentUser() + reason + groupName;

											if(publibCheckBox.isChecked()){
												option.style = memberCheckbox.isChecked() ? EMGroupStyle.EMGroupStylePublicJoinNeedApproval : EMGroupStyle.EMGroupStylePublicOpenJoin;
											}else{
												option.style = memberCheckbox.isChecked()? EMGroupStyle.EMGroupStylePrivateMemberCanInvite: EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
											}
											EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);
											runOnUiThread(new Runnable() {
												public void run() {
													progressDialog.dismiss();
													setResult(RESULT_OK);
													finish();
												}
											});
										} catch (final HyphenateException e) {
											runOnUiThread(new Runnable() {
												public void run() {
													progressDialog.dismiss();
													Toast.makeText(NewGroupActivity.this, st2 + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
												}
											});
										}

									}
								}).start();
							}else {
								progressDialog.dismiss();
								Toast.makeText(NewGroupActivity.this, st2 , Toast.LENGTH_LONG).show();
							}
						}
						@Override
						public void error(Throwable ex, boolean isOnCallback) {

						}
					});
			}
			}
	}


	private void showImages(Bitmap bitmap) {
		edit_group_avatar.setImageBitmap(bitmap);
	}


	/**
	 * 检查设备是否存在SDCard的工具方法
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}

	private Bitmap  getBitmap(){
		Drawable drawable = edit_group_avatar.getDrawable();
		if(drawable==null){
			edit_group_avatar.setImageResource(R.mipmap.newcreategroup);
			drawable=edit_group_avatar.getDrawable();
		}
		Bitmap bitmap = myUtils.drawableToBitmap(drawable);
		return bitmap;

	}

	public void back(View view) {
		finish();
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

}
