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
package com.zl.vo_.db;

import android.content.ContentValues;
import android.content.Context;


import com.zl.vo_.domain.InviteMessage;

import java.util.List;

/*****
 * 邀请信息的操作类
 */

public class InviteMessgeDao {
	//表名称
	static final String TABLE_NAME = "new_friends_msgs";
	//消息id
	static final String COLUMN_NAME_ID = "id";
	//来自哪儿
	static final String COLUMN_NAME_FROM = "username";
	//群组ID
	static final String COLUMN_NAME_GROUP_ID = "groupid";
	//群组名称
	static final String COLUMN_NAME_GROUP_Name = "groupname";
	//时间
	static final String COLUMN_NAME_TIME = "time";
	//理由
	static final String COLUMN_NAME_REASON = "reason";
	//状态？
	public static final String COLUMN_NAME_STATUS = "status";
	//邀请是否来自我
	static final String COLUMN_NAME_ISINVITEFROMME = "isInviteFromMe";
	//组邀请者
	static final String COLUMN_NAME_GROUPINVITER = "groupinviter";
	//未读消息总数
	static final String COLUMN_NAME_UNREAD_MSG_COUNT = "unreadMsgCount";
	//头像
	static final String COLUMN_NAME_AVATAR = "avatar";
	//昵称
	static final String COLUMN_NAME_NICK = "nick";
	//群描述（群头像）
	static final String COLUMN_NAME_DESC = "group_desc";


	
	//消息构造
	public InviteMessgeDao(Context context){
	}
	
	/**
	 * 保存一条消息
	 * @param message
	 * @return  return cursor of the message
	 */
	public Integer saveMessage(InviteMessage message){
		return DemoDBManager.getInstance().saveMessage(message);
	}
	
	/**
	 * 更新一条消息
	 * @param msgId
	 * @param values
	 */
	public void updateMessage(int msgId,ContentValues values){
	    DemoDBManager.getInstance().updateMessage(msgId, values);
	}
	
	/**
	 * 获取消息列表
	 * @return
	 */
	public List<InviteMessage> getMessagesList(){
		return DemoDBManager.getInstance().getMessagesList();
	}

	/**
	 * 删除来自谁的消息
	 * @param from
     */
	public void deleteMessage(String from){
	    DemoDBManager.getInstance().deleteMessage(from);
	}

	/***
	 * 删除群组消息
	 * @param groupId
     */
	public void deleteGroupMessage(String groupId) {
		DemoDBManager.getInstance().deleteGroupMessage(groupId);
	}

	/***
	 * 删除群组里来自谁的消息
	 * @param groupId
	 * @param from
     */
	public void deleteGroupMessage(String groupId, String from) {
		DemoDBManager.getInstance().deleteGroupMessage(groupId, from);
	}




	/***
	 * 获取未读消息的总数
	 * @return
     */
	public int getUnreadMessagesCount(){
	    return DemoDBManager.getInstance().getUnreadNotifyCount();
	}

	/***
	 * 保存未读消息总数
	 * @param count
     */
	public void saveUnreadMessageCount(int count){
	    DemoDBManager.getInstance().setUnreadNotifyCount(count);
	}
}
