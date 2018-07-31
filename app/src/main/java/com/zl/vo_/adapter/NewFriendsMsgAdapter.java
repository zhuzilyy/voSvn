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
package com.zl.vo_.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.zl.vo_.R;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.domain.InviteMessage;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.UserInfoEntity;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;


import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

	private Context context;
	private InviteMessgeDao messgeDao;

	public NewFriendsMsgAdapter(Context context, int textViewResourceId, List<InviteMessage> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		messgeDao = new InviteMessgeDao(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.em_row_invite_msg, null);
			holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.agreeBtn = (Button) convertView.findViewById(R.id.agree);
			holder.refuseBtn = (Button) convertView.findViewById(R.id.refuse);
			// holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final InviteMessage msg = getItem(position);
		if (msg != null) {
            holder.agreeBtn.setVisibility(View.GONE);
            holder.refuseBtn.setVisibility(View.GONE);

			holder.message.setText(msg.getReason());
			holder.name.setText(msg.getGroupInviter()!=null?msg.getGroupName():msg.getNick());
            if(msg.getGroupInviter()==null){
                Glide.with(context).load(msg.getAvatar()).into(holder.avator);

            }else {
                Glide.with(context).load(msg.getGroup_desc()).into(holder.avator);
               // holder.avator.setImageResource(R.drawable.ease_group_icon);
            }

			// holder.time.setText(DateUtils.getTimestampString(new
			// Date(msg.getTime())));
			if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEAGREED) {
                //已经同意了
                holder.agreeBtn.setVisibility(View.GONE);
				holder.refuseBtn.setVisibility(View.GONE);
				holder.message.setText(context.getResources().getString(R.string.Has_agreed_to_your_friend_request));

			} else if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEINVITEED || msg.getStatus() == InviteMessage.InviteMessageStatus.BEAPPLYED ||
			        msg.getStatus() == InviteMessage.InviteMessageStatus.GROUPINVITATION) {
			    holder.agreeBtn.setVisibility(View.VISIBLE);
				holder.refuseBtn.setVisibility(View.VISIBLE);
				if(msg.getStatus() == InviteMessage.InviteMessageStatus.BEINVITEED){
					if (TextUtils.isEmpty(msg.getReason())) {
						// use default text
						//holder.message.setText(context.getResources().getString(R.string.Request_to_add_you_as_a_friend));
                       // Log.i("cuuu",msg.getReason());
                       // messgeDao.deleteMessage(msg.getFrom());
					}else {
                        String res=msg.getReason();
                        holder.message.setText(res);
                    }
				}else if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEAPPLYED) { //application to join group
					if (TextUtils.isEmpty(msg.getReason())) {
                        Log.i("cuuu",msg.getReason());
						holder.message.setText(context.getResources().getString(R.string.Apply_to_the_group_of) + msg.getGroupName());
					}
                    messgeDao.deleteMessage(msg.getGroupId());
				} else if (msg.getStatus() == InviteMessage.InviteMessageStatus.GROUPINVITATION) {
				    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.name.setText(msg.getGroupInviter());
                        holder.message.setText(context.getResources().getString(R.string.invite_join_group) + msg.getGroupName());
                    }
                   // messgeDao.deleteMessage(msg.getGroupId());
				}
				
				//点击同意
                holder.agreeBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // accept invitation
                        acceptInvitation(holder.agreeBtn, holder.refuseBtn, msg);
                    }
                });
                //点击拒绝
				holder.refuseBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// decline invitation
					    refuseInvitation(holder.agreeBtn, holder.refuseBtn, msg);
					}
				});
			} else {
                //状态完结的[已经同意，已经拒绝的]
				String str = "";
				InviteMessage.InviteMessageStatus status = msg.getStatus();
                switch (status) {
                    case AGREED:
                        //已经同意
                        str = context.getResources().getString(R.string.Has_agreed_to);
                        break;
                    case REFUSED:
                        //已经拒绝
                        str = context.getResources().getString(R.string.Has_refused_to);
                        break;
                    case GROUPINVITATION_ACCEPTED:
                        //群组邀请同意的
                        str = context.getResources().getString(R.string.accept_join_group);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case GROUPINVITATION_DECLINED:
                        //群组邀请拒绝的
                        str = context.getResources().getString(R.string.refuse_join_group);
                        str = String.format(str, msg.getGroupInviter());
                        break;

                    //**************多设备消息状态开始**********************************************
                    case MULTI_DEVICE_CONTACT_ADD:
                        //已经发送给谁[多设备]
                        str = context.getResources().getString(R.string.multi_device_contact_add);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_BAN:
                        str = context.getResources().getString(R.string.multi_device_contact_ban);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_ALLOW:
                        str = context.getResources().getString(R.string.multi_device_contact_allow);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_ACCEPT:
                        str = context.getResources().getString(R.string.multi_device_contact_accept);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_CONTACT_DECLINE:
                        str = context.getResources().getString(R.string.multi_device_contact_decline);
                        str = String.format(str, msg.getFrom());
                        break;
                    case MULTI_DEVICE_GROUP_CREATE:
                        str = context.getResources().getString(R.string.multi_device_group_create);
                        break;
                    case MULTI_DEVICE_GROUP_DESTROY:
                        str = context.getResources().getString(R.string.multi_device_group_destroy);
                        break;
                    case MULTI_DEVICE_GROUP_JOIN:
                        str = context.getResources().getString(R.string.multi_device_group_join);
                        break;
                    case MULTI_DEVICE_GROUP_LEAVE:
                        str = context.getResources().getString(R.string.multi_device_group_leave);
                        break;
                    case MULTI_DEVICE_GROUP_APPLY:
                        str = context.getResources().getString(R.string.multi_device_group_apply);
                        break;
                    case MULTI_DEVICE_GROUP_APPLY_ACCEPT:
                        str = context.getResources().getString(R.string.multi_device_group_apply_accept);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_APPLY_DECLINE:
                        str = context.getResources().getString(R.string.multi_device_group_apply_decline);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_INVITE:
                        str = context.getResources().getString(R.string.multi_device_group_invite);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_INVITE_ACCEPT:
                        str = context.getResources().getString(R.string.multi_device_group_invite_accept);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_INVITE_DECLINE:
                        str = context.getResources().getString(R.string.multi_device_group_invite_decline);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_KICK:
                        str = context.getResources().getString(R.string.multi_device_group_kick);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_BAN:
                        str = context.getResources().getString(R.string.multi_device_group_ban);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_ALLOW:
                        str = context.getResources().getString(R.string.multi_device_group_allow);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_BLOCK:
                        str = context.getResources().getString(R.string.multi_device_group_block);
                        break;
                    case MULTI_DEVICE_GROUP_UNBLOCK:
                        str = context.getResources().getString(R.string.multi_device_group_unblock);
                        break;
                    case MULTI_DEVICE_GROUP_ASSIGN_OWNER:
                        str = context.getResources().getString(R.string.multi_device_group_assign_owner);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_ADD_ADMIN:
                        str = context.getResources().getString(R.string.multi_device_group_add_admin);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_REMOVE_ADMIN:
                        str = context.getResources().getString(R.string.multi_device_group_remove_admin);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_ADD_MUTE:
                        str = context.getResources().getString(R.string.multi_device_group_add_mute);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    case MULTI_DEVICE_GROUP_REMOVE_MUTE:
                        str = context.getResources().getString(R.string.multi_device_group_remove_mute);
                        str = String.format(str, msg.getGroupInviter());
                        break;
                    //**************多设备消息状态结束**********************************************
                    default:
                        break;
                }
                holder.message.setText(str);
            }
        }

		return convertView;
	}

    /**
     * accept invitation
     *
     * @param buttonAgree
     * @param buttonRefuse
     * @param msg
     */
    private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        //正在同意
        String str1 = context.getResources().getString(R.string.Are_agree_with);
        //已经同意
        final String str2 = context.getResources().getString(R.string.Has_agreed_to);
        //同意失败
        final String str3 = context.getResources().getString(R.string.Agree_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        try {
            if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEINVITEED) {//来自朋友的申请
                //同意好友的申请，走自己的服务器
                agreedFromFriend(msg);

            } else if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEAPPLYED) { //接受申请来自群组
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
                        }catch (Exception e){
                        }
                    }
                }).start();

            } else if (msg.getStatus() == InviteMessage.InviteMessageStatus.GROUPINVITATION) {//来自群组邀请
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
                        }catch (Exception e){

                        }
                    }
                }).start();

            }
            msg.setStatus(InviteMessage.InviteMessageStatus.AGREED);
            // update database
            ContentValues values = new ContentValues();
            values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
            messgeDao.updateMessage(msg.getId(), values);
            ((Activity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    pd.dismiss();
                    buttonAgree.setText(str2);

                    buttonAgree.setEnabled(false);

                    buttonRefuse.setVisibility(View.INVISIBLE);
                }
            });

        }catch (final Exception e) {
            ((Activity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    pd.dismiss();
                    Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

    }

    /****
     * 同意好友的申请，走自己的服务器
     * @param msg
     */
    private void agreedFromFriend(InviteMessage msg) {
        RequestParams params=new RequestParams(Url.NativeAddFriendUrl);
        params.addParameter("huanxin_account", myUtils.readUser(context).getHuanxin_account());
        params.addParameter("friend_huanxin_account", msg.getFrom());
        x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
            @Override
            public void success(Result<UserInfoEntity> data) {
                if("0".equals(data.code)){
                    UserInfoEntity userInfoEntity= data.data;
                    UserInfoEntity.UserInfo userInfo=userInfoEntity.getInfo();
                    if(userInfo!=null){
                        UserInfoEntity.UserInfo.UserFriendInfo friend_info=userInfo.getFriend_info();
                        //发送广播
                        context.sendBroadcast(new Intent("addFrindOK"));

                    }
                }else {
                    Toast.makeText(context, data.info, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                Log.i("from","[][][][]"+ex.getMessage());
                Toast.makeText(context, "同意失败", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
	 * accept invitation
	 * 
	 * @param buttonAgree
	 * @param buttonRefuse
	 * @param msg
	 */
	/*private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
        if(msg.getGroupInviter()==null){
            //个人同意
            //点击同意，从自己的服务器上获取好友信息，保存到数据库中并刷新列表
            RequestParams params=new RequestParams(Url.NativeAddFriendUrl);
            params.addParameter("huanxin_account", myUtils.readUser(context).getHuanxin_account());
            params.addParameter("friend_huanxin_account", msg.getFrom());
            Log.i("from", msg.getFrom()+"[][][][]");
            x.http().post(params, new MyCommonCallback<Result<UserInfoEntity>>() {
                @Override
                public void success(Result<UserInfoEntity> data) {
                    if("0".equals(data.code)){
                        UserInfoEntity userInfoEntity= data.data;
                        UserInfoEntity.UserInfo userInfo=userInfoEntity.getInfo();
                        if(userInfo!=null){
                            UserInfoEntity.UserInfo.UserFriendInfo friend_info=userInfo.getFriend_info();
                            //发送广播
                            context.sendBroadcast(new Intent("addFrindOK"));
                            buttonAgree.setText("已同意");
                            buttonAgree.setBackgroundDrawable(null);
                            buttonAgree.setEnabled(false);
                            buttonRefuse.setVisibility(View.INVISIBLE);
                        }

                    }else {
                        Toast.makeText(context, data.info, Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void error(Throwable ex, boolean isOnCallback) {
                    Log.i("from","[][][][]"+ex.getMessage());
                    Toast.makeText(context, "同意失败", Toast.LENGTH_LONG).show();
                }
            });
        }else {
            //群组同意
            final ProgressDialog pd = new ProgressDialog(context);
		String str1 = context.getResources().getString(R.string.Are_agree_with);
		final String str2 = context.getResources().getString(R.string.Has_agreed_to);
		final String str3 = context.getResources().getString(R.string.Agree_with_failure);
		pd.setMessage(str1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		new Thread(new Runnable() {
			public void run() {
				// call api
				try {
					if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEINVITEED) {//accept be friends
						EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
					} else if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEAPPLYED) { //accept application to join group
						EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
					} else if (msg.getStatus() == InviteMessage.InviteMessageStatus.GROUPINVITATION) {
					    EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
					}
                    msg.setStatus(InviteMessage.InviteMessageStatus.AGREED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							buttonAgree.setText(str2);
							buttonAgree.setBackgroundDrawable(null);
							buttonAgree.setEnabled(false);

							buttonRefuse.setVisibility(View.INVISIBLE);
						}
					});
				} catch (final Exception e) {
					((Activity) context).runOnUiThread(new Runnable() {

						@Override
						public void run() {
							pd.dismiss();
							Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});

				}
			}
		}).start();
        }
	}*/
	
	/**
     * decline invitation
     * 
     * @param buttonAgree
     * @param buttonRefuse
	 * @param msg
     */
    private void refuseInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_refuse_with);
        final String str2 = context.getResources().getString(R.string.Has_refused_to);
        final String str3 = context.getResources().getString(R.string.Refuse_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // call api
                try {
                    if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEINVITEED) {//decline the invitation
                        EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
                    } else if (msg.getStatus() == InviteMessage.InviteMessageStatus.BEAPPLYED) { //decline application to join group
                        EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
                    } else if (msg.getStatus() == InviteMessage.InviteMessageStatus.GROUPINVITATION) {
                        EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getGroupInviter(), "");
                    }
                    msg.setStatus(InviteMessage.InviteMessageStatus.REFUSED);
                    // update database
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonRefuse.setText(str2);
                           // buttonRefuse.setBackgroundDrawable(null);
                            buttonRefuse.setEnabled(false);

                            buttonAgree.setVisibility(View.INVISIBLE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }).start();
    }

	private static class ViewHolder {
		ImageView avator;
		TextView name;
		TextView message;
        Button agreeBtn;
		Button refuseBtn;
	}

}
