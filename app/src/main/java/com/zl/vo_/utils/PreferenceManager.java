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
package com.zl.vo_.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
	/**
	 * name of preference
	 */
	public static final String PREFERENCE_NAME = "saveInfo";
	private static SharedPreferences mSharedPreferences;
	private static PreferenceManager mPreferencemManager;
	private static SharedPreferences.Editor editor;
	//设置通知的key(存储)
	private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
	//设置加我为好友是否需要验证
	private String SHARED_KEY_SETTING_VERIFICATION="shared_key_setting_verification";
	//是否像我推荐通讯录好友
	private String SHARED_KEY_SETTING_RECOMMEDEDFRIEND="shared_key_setting_recommedfriend";


	//设置声音的key（存储）
	private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
	//设置是否显示消息详情
	private String SHARED_KEY_SETTING_MESDETAILS = "shared_key_setting_mes_details";

	//设置震动（存储）
	private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
	//设置听筒
	private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";

	private static String SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE = "shared_key_setting_chatroom_owner_leave";
	//退出群删除消息
    private static String SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP = "shared_key_setting_delete_messages_when_exit_group";
    //自动接收群组的邀请
	private static String SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION = "shared_key_setting_auto_accept_group_invitation";
    //
	private static String SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE = "shared_key_setting_adaptive_video_encode";
	//设置离线回调
	private static String SHARED_KEY_SETTING_OFFLINE_PUSH_CALL = "shared_key_setting_offline_push_call";
	//群同步
	private static String SHARED_KEY_SETTING_GROUPS_SYNCED = "SHARED_KEY_SETTING_GROUPS_SYNCED";
	//联系人同步
	private static String SHARED_KEY_SETTING_CONTACT_SYNCED = "SHARED_KEY_SETTING_CONTACT_SYNCED";
	//黑名单同步
	private static String SHARED_KEY_SETTING_BALCKLIST_SYNCED = "SHARED_KEY_SETTING_BALCKLIST_SYNCED";
	//当前用户的UserName
	private static String SHARED_KEY_CURRENTUSER_USERNAME = "SHARED_KEY_CURRENTUSER_USERNAME";
	//当前用户的昵称
	private static String SHARED_KEY_CURRENTUSER_NICK = "SHARED_KEY_CURRENTUSER_NICK";
	//当前用户的头像
	private static String SHARED_KEY_CURRENTUSER_AVATAR = "SHARED_KEY_CURRENTUSER_AVATAR";

	//设置聊天背景图
	private static String SHARED_KEY_CHATBACKGROUNDPIC = "SHARED_KEY_CHATBACKGROUNDPIC";



	//------------------------------------------------
	//设定消息免打扰的开始时间
	private static String MESSAGE_DONOTDISTURB_STARTTIME="key_msg_disturb_starttime";
	//设定消息免打扰的结束时间
	private static String MESSAGE_DONOTDISTURB_ENDTIME="key_msg_disturb_endtime";
	//设定是否开启消息免打扰
	private static String MSG_DISTURB="key_msg_isdisturb";


	//----------------------------------------------




	private static String SHARED_KEY_REST_SERVER = "SHARED_KEY_REST_SERVER";
	private static String SHARED_KEY_IM_SERVER = "SHARED_KEY_IM_SERVER";
	private static String SHARED_KEY_ENABLE_CUSTOM_SERVER = "SHARED_KEY_ENABLE_CUSTOM_SERVER";
	private static String SHARED_KEY_ENABLE_CUSTOM_APPKEY = "SHARED_KEY_ENABLE_CUSTOM_APPKEY";
	private static String SHARED_KEY_CUSTOM_APPKEY = "SHARED_KEY_CUSTOM_APPKEY";
	private static String SHARED_KEY_MSG_ROAMING = "SHARED_KEY_MSG_ROAMING";

	private static String SHARED_KEY_CALL_MIN_VIDEO_KBPS = "SHARED_KEY_CALL_MIN_VIDEO_KBPS";
	private static String SHARED_KEY_CALL_MAX_VIDEO_KBPS = "SHARED_KEY_CALL_Max_VIDEO_KBPS";
	private static String SHARED_KEY_CALL_MAX_FRAME_RATE = "SHARED_KEY_CALL_MAX_FRAME_RATE";
	private static String SHARED_KEY_CALL_AUDIO_SAMPLE_RATE = "SHARED_KEY_CALL_AUDIO_SAMPLE_RATE";
	private static String SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION = "SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION";
	private static String SHARED_KEY_CALL_FRONT_CAMERA_RESOLUTION = "SHARED_KEY_FRONT_CAMERA_RESOLUTIOIN";
	private static String SHARED_KEY_CALL_FIX_SAMPLE_RATE = "SHARED_KEY_CALL_FIX_SAMPLE_RATE";

	private static String VOAvatar="voavatar";
	private static String VONick="vonick";


	@SuppressLint("CommitPrefEdits")
	private PreferenceManager(Context cxt) {
		//key="saveInfo" 私有的
		mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}





	/***
	 * 初始化preferenceManager（new一个实例）
	 * @param cxt
     */
	public static synchronized void init(Context cxt){
	    if(mPreferencemManager == null){
	        mPreferencemManager = new PreferenceManager(cxt);
	    }
	}

	/**
	 * 获取preferenceManager对象（如果返回null,提示请先初始化）
	 *
	 * @param
	 * @return
	 */
	public synchronized static PreferenceManager getInstance() {
		if (mPreferencemManager == null) {
			throw new RuntimeException("please init first!");
		}

		return mPreferencemManager;
	}
	//----------------------------------------------------------
	public void setVOAvatar(String voAvatar){
		editor.putString(MESSAGE_DONOTDISTURB_STARTTIME, voAvatar);
		editor.apply();
	}

	public String getVOAvatar() {
		return mSharedPreferences.getString(VOAvatar, null);
	}


	public void setVONick(String voNick){
		editor.putString(VONick, voNick);
		editor.apply();
	}


	public String getVONick() {
		return mSharedPreferences.getString(VONick, null);
	}









	//保存消息免打扰的开始时间
	public void setSettingDistrubStartTime(String startTime) {
		editor.putString(MESSAGE_DONOTDISTURB_STARTTIME, startTime);
		editor.apply();
	}
	//保存消息免打扰的结束时间
	public void setSettingDistrubEndTime(String endTime) {
		editor.putString(MESSAGE_DONOTDISTURB_ENDTIME, endTime);
		editor.apply();
	}
	//获取消息免打扰的开始时间
	public String getSettingDistrubStartTime() {
		return mSharedPreferences.getString(MESSAGE_DONOTDISTURB_STARTTIME, "0:00");
	}
	//获取消息免打扰的开始时间
	public String getSettingDistrubEndTime() {
		return mSharedPreferences.getString(MESSAGE_DONOTDISTURB_ENDTIME, "0:00");
	}
	//**********************
	//保存是否消息提示
	public void setIsDisturb(boolean paramBoolean) {
		editor.putBoolean(MSG_DISTURB, paramBoolean);
		editor.apply();
	}
	//获取消息是否语音者
	public boolean getIsDisturb() {
		return mSharedPreferences.getBoolean(MSG_DISTURB,false);
	}

	//*************************

   //----------------------------------------------------------

	//保存是否消息提示
	public void setSettingMsgNotification(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
		editor.apply();
	}
	//获取是否消息提示
	public boolean getSettingMsgNotification() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, true);
	}

	//获取加我为好友时是否需要验证
	public boolean getSettingVerification() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VERIFICATION, true);
	}
	//保存是否加我为好友时是否需要验证
	public void setSettingVerification(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_VERIFICATION, paramBoolean);
		editor.apply();
	}

	//获取是否推荐通讯录好友
	public boolean getSettingRecommedFriend (){
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_RECOMMEDEDFRIEND, true);
	}
	//保存是否推荐通讯录好友
	public void setSettingRecommedFriend(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_RECOMMEDEDFRIEND, paramBoolean);
		editor.apply();
	}




	//保存是否开启消息提示音
	public void setSettingMsgSound(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
		editor.apply();
	}
	//获取是否开启消息提示音
	public boolean getSettingMsgSound() {

		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true);
	}

	//获取是否设置了显示消息详情
	public boolean getSettingMsgDetails() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_MESDETAILS, false);
	}
	//保存是否设置了显示消息详情
	public void setSettingMsgDetails(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_MESDETAILS, paramBoolean);
		editor.apply();
	}
	//保存是否消息震动
	public void setSettingMsgVibrate(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
		editor.apply();
	}
	//获取消息是否震动
	public boolean getSettingMsgVibrate() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
	}
	//保存消息语音化
	public void setSettingMsgSpeaker(boolean paramBoolean) {
		editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
		editor.apply();
	}
	//获取消息是否语音者
	public boolean getSettingMsgSpeaker() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER,false);
	}
	//保存聊天室主人是否离开
	public void setSettingAllowChatroomOwnerLeave(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, value);
        editor.apply();
    }
	//获取聊天室主人是否离开
	public boolean getSettingAllowChatroomOwnerLeave() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CHATROOM_OWNER_LEAVE, true);
    }
	//设置退出群组时删除聊天记录
    public void setDeleteMessagesAsExitGroup(boolean value){
        editor.putBoolean(SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP, value);
        editor.apply();
    }
	//获取当退出群组时是否删除聊天记录
    public boolean isDeleteMessagesAsExitGroup() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP, true);
    }
	//设置自动同意群组加群邀请
    public void setAutoAcceptGroupInvitation(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION, value);
        editor.commit();
    }
	//获取是否自动同意群组的邀请
    public boolean isAutoAcceptGroupInvitation() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION, true);
    }
	//设置视频自适应码率编码
    public void setAdaptiveVideoEncode(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, value);
        editor.apply();
    }
	//是否设置视频自适应码率编码
    public boolean isAdaptiveVideoEncode() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, false);
    }
	//设置离线推送
	public void setPushCall(boolean value) {
		editor.putBoolean(SHARED_KEY_SETTING_OFFLINE_PUSH_CALL, value);
		editor.apply();
	}
	//获取是否离线推送
	public boolean isPushCall() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_OFFLINE_PUSH_CALL, false);
	}
    //设置群组同步
	public void setGroupsSynced(boolean synced){
	    editor.putBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, synced);
        editor.apply();
	}
	//获取群组是否同步
	public boolean isGroupsSynced(){
	    return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, false);
	}
	//设置联系人是否同步
	public void setContactSynced(boolean synced){
        editor.putBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, synced);
        editor.apply();
    }
	//获取是否设置联系人同步
    public boolean isContactSynced(){
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, false);
    }
	//设置黑名单同步
    public void setBlacklistSynced(boolean synced){
        editor.putBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, synced);
        editor.apply();
    }
	//获取黑名单是否同步
    public boolean isBacklistSynced(){
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, false);
    }
	//设置当前用户的昵称
	public void setCurrentUserNick(String nick) {
		editor.putString(SHARED_KEY_CURRENTUSER_NICK, nick);
		editor.apply();
	}
	//设置当前用户的头像
	public void setCurrentUserAvatar(String avatar) {
		editor.putString(SHARED_KEY_CURRENTUSER_AVATAR, avatar);
		editor.apply();
	}
	//获取当前用户的昵称
	public String getCurrentUserNick() {
		return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_NICK, null);
	}
	//获取当前用户的头像
	public String getCurrentUserAvatar() {
		return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_AVATAR, null);
	}
	//设置当前用户的userName
	public void setCurrentUserName(String username){
		editor.putString(SHARED_KEY_CURRENTUSER_USERNAME, username);
		editor.apply();
	}
	//获取当前用户的userName
	public String getCurrentUsername(){
		return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_USERNAME, null);
	}
	//
	public void setRestServer(String restServer){
		editor.putString(SHARED_KEY_REST_SERVER, restServer).commit();
		editor.commit();
	}

	public String getRestServer(){
		return mSharedPreferences.getString(SHARED_KEY_REST_SERVER, null);
	}

	public void setIMServer(String imServer){
		editor.putString(SHARED_KEY_IM_SERVER, imServer);
		editor.commit();
	}

	public String getIMServer(){
		return mSharedPreferences.getString(SHARED_KEY_IM_SERVER, null);
	}

	public void enableCustomServer(boolean enable){
		editor.putBoolean(SHARED_KEY_ENABLE_CUSTOM_SERVER, enable);
		editor.apply();
	}

	public boolean isCustomServerEnable(){
		return mSharedPreferences.getBoolean(SHARED_KEY_ENABLE_CUSTOM_SERVER, false);
	}

	public void enableCustomAppkey(boolean enable) {
		editor.putBoolean(SHARED_KEY_ENABLE_CUSTOM_APPKEY, enable);
		editor.apply();
	}

	public boolean isCustomAppkeyEnabled() {
		return mSharedPreferences.getBoolean(SHARED_KEY_ENABLE_CUSTOM_APPKEY, false);
	}

	public String getCustomAppkey() {
		return mSharedPreferences.getString(SHARED_KEY_CUSTOM_APPKEY, "");
	}

	public void setCustomAppkey(String appkey) {
		editor.putString(SHARED_KEY_CUSTOM_APPKEY, appkey);
		editor.apply();
	}
	//移除当前用户的信息
	public void removeCurrentUserInfo() {
		editor.remove(SHARED_KEY_CURRENTUSER_NICK);
		editor.remove(SHARED_KEY_CURRENTUSER_AVATAR);
		editor.apply();
	}
	//获取消息是否漫游
	public boolean isMsgRoaming() {
		return mSharedPreferences.getBoolean(SHARED_KEY_MSG_ROAMING, false);
	}
	//设置消息是否漫游
	public void setMsgRoaming(boolean isRoaming) {
		editor.putBoolean(SHARED_KEY_MSG_ROAMING, isRoaming);
		editor.apply();
	}

	/**
	 * ----------------------------------------- Call Option -----------------------------------------
	 */

	/**
	 * Min Video kbps
	 * if no value was set, return -1
	 * @return
	 */
	public int getCallMinVideoKbps() {
		return mSharedPreferences.getInt(SHARED_KEY_CALL_MIN_VIDEO_KBPS, -1);
	}

	public void setCallMinVideoKbps(int minBitRate) {
		editor.putInt(SHARED_KEY_CALL_MIN_VIDEO_KBPS, minBitRate);
		editor.apply();
	}

	/**
	 * Max Video kbps
	 * if no value was set, return -1
	 * @return
	 */
	public int getCallMaxVideoKbps() {
		return mSharedPreferences.getInt(SHARED_KEY_CALL_MAX_VIDEO_KBPS, -1);
	}

	public void setCallMaxVideoKbps(int maxBitRate) {
		editor.putInt(SHARED_KEY_CALL_MAX_VIDEO_KBPS, maxBitRate);
		editor.apply();
	}

	/**
	 * Max frame rate
	 * if no value was set, return -1
	 * @return
	 */
	public int getCallMaxFrameRate() {
		return mSharedPreferences.getInt(SHARED_KEY_CALL_MAX_FRAME_RATE, -1);
	}

	public void setCallMaxFrameRate(int maxFrameRate) {
		editor.putInt(SHARED_KEY_CALL_MAX_FRAME_RATE, maxFrameRate);
		editor.apply();
	}

	/**
	 * audio sample rate
	 * if no value was set, return -1
	 * @return
	 */
	public int getCallAudioSampleRate() {
		return mSharedPreferences.getInt(SHARED_KEY_CALL_AUDIO_SAMPLE_RATE, -1);
	}

	public void setCallAudioSampleRate(int audioSampleRate) {
		editor.putInt(SHARED_KEY_CALL_AUDIO_SAMPLE_RATE, audioSampleRate);
		editor.apply();
	}

	/**
	 * back camera resolution
	 * format: 320x240
	 * if no value was set, return ""
	 */
	public String getCallBackCameraResolution() {
		return mSharedPreferences.getString(SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION, "");
	}

	public void setCallBackCameraResolution(String resolution) {
		editor.putString(SHARED_KEY_CALL_BACK_CAMERA_RESOLUTION, resolution);
		editor.apply();
	}

	/**
	 * front camera resolution
	 * format: 320x240
	 * if no value was set, return ""
	 */
	public String getCallFrontCameraResolution() {
		return mSharedPreferences.getString(SHARED_KEY_CALL_FRONT_CAMERA_RESOLUTION, "");
	}

	public void setCallFrontCameraResolution(String resolution) {
		editor.putString(SHARED_KEY_CALL_FRONT_CAMERA_RESOLUTION, resolution);
		editor.apply();
	}

	/**
	 * fixed video sample rate
	 *  if no value was set, return false
	 * @return
     */
	public boolean isCallFixedVideoResolution() {
		return mSharedPreferences.getBoolean(SHARED_KEY_CALL_FIX_SAMPLE_RATE, false);
	}

	public void setCallFixedVideoResolution(boolean enable) {
		editor.putBoolean(SHARED_KEY_CALL_FIX_SAMPLE_RATE, enable);
		editor.apply();
	}

	/****
	 * 保存聊天背景
	 * @param picPath_
     */
	public void setChatBackGroundPicUrl(String picPath_) {
		editor.putString(SHARED_KEY_CHATBACKGROUNDPIC, picPath_);
		editor.apply();
	}

	/****
	 * 获取聊天背景
	 * @param
	 */
	public String GetChatBackGroundPicUrl() {
		return mSharedPreferences.getString(SHARED_KEY_CHATBACKGROUNDPIC, "");
	}

}
