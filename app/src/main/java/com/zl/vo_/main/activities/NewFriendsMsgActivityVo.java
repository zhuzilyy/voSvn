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
package com.zl.vo_.main.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.dou361.dialogui.DialogUIUtils;
import com.zl.vo_.R;
import com.zl.vo_.adapter.NewFriendsMsgAdapter;
import com.zl.vo_.db.InviteMessgeDao;
import com.zl.vo_.domain.InviteMessage;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Application and notification
 *
 */
public class NewFriendsMsgActivityVo extends VoBaseActivity {
	private ListView listView;
	List<InviteMessage> msgs;
	InviteMessgeDao dao;
	@BindView(R.id.loading_view)
	public RelativeLayout loading_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_friends_msg);
		VoBaseActivity.addActivity(this);
		ButterKnife.bind(this);

		DialogUIUtils.init(NewFriendsMsgActivityVo.this);
		final ListView listView = findViewById(R.id.list);
		dao= new InviteMessgeDao(this);
		msgs = dao.getMessagesList();
		Collections.reverse(msgs);

		final NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
				final Dialog dialog=new Dialog(NewFriendsMsgActivityVo.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.lay_newmsg_del);
				dialog.findViewById(R.id.msg_cancel).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
				dialog.findViewById(R.id.msg_confrim).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dao.deleteMessage(msgs.get(i).getFrom());
						msgs = dao.getMessagesList();
						NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(NewFriendsMsgActivityVo.this, 1, msgs);
						listView.setAdapter(adapter);
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});

	}


	public void back(View view) {

		finish();
	}
}
