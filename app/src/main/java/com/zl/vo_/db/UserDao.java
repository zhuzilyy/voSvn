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

import android.content.Context;

import com.hyphenate.easeui.domain.EaseUser;
import com.zl.vo_.domain.RobotUser;

import java.util.List;
import java.util.Map;

public class UserDao {
	/****
	 * 用户数据库操作类
	 * 用户表的 字段  和 方法
	 *
	 *
	 */
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	public static final String COLUMN_NAME_MGROUPID = "groupid";
	
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

	public static final String ROBOT_TABLE_NAME = "robots";
	public static final String ROBOT_COLUMN_NAME_ID = "username";
	public static final String ROBOT_COLUMN_NAME_NICK = "nick";
	public static final String ROBOT_COLUMN_NAME_AVATAR = "avatar";
	
	//构造
	public UserDao(Context context) {
	}


	public void saveContactList(List<EaseUser> contactList) {
	    DemoDBManager.getInstance().saveContactList(contactList);
	}

	/**
	 * 获取联系人[map]
	 * 
	 * @return
	 */
	public Map<String, EaseUser> getContactList() {
		DemoDBManager dbManager=DemoDBManager.getInstance();
	    return dbManager.getContactList();
	}
	
	/**
	 * 删除一个联系人[void]
	 * @param username
	 */
	public void deleteContact(String username){
	    DemoDBManager.getInstance().deleteContact(username);
	}
	
	/**
	 * 保存一个联系人
	 * @param user
	 */
	public void saveContact(EaseUser user){
	    DemoDBManager.getInstance().saveContact(user);
	}

	/***
	 * 设置一个群组不可用
	 * @param groups
     */
	public void setDisabledGroups(List<String> groups){
	    DemoDBManager.getInstance().setDisabledGroups(groups);
    }

	/***
	 * 获取不可用的群组[list集合]
	 * @return
     */
    public List<String> getDisabledGroups(){
        return DemoDBManager.getInstance().getDisabledGroups();
    }

	/***
	 * 设置使其不可用
	 * @param ids
     */
    public void setDisabledIds(List<String> ids){
        DemoDBManager.getInstance().setDisabledIds(ids);
    }

	/***
	 * 获取不可用的人员名单[list集合]
	 * @return
     */
    public List<String> getDisabledIds(){
        return DemoDBManager.getInstance().getDisabledIds();
    }

	/***
	 * 获取机器人集合[map]
	 * @return
     */
    public Map<String, RobotUser> getRobotUser(){
    	return DemoDBManager.getInstance().getRobotList();
    }

	/***
	 * 保存机器人集合
	 * @param robotList
     */
    public void saveRobotUser(List<RobotUser> robotList){
    	DemoDBManager.getInstance().saveRobotList(robotList);
    }
}
