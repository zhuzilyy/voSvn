/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zl.vo_.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.zl.vo_.R;
import com.zl.vo_.adapter.AlLFriendsShareAdapter;
import com.zl.vo_.db.DemoDBManager;
import com.zl.vo_.main.activities.*;
import com.zl.vo_.main.main_utils.MinPianConstant;
import com.zl.vo_.main.main_utils.myUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForwardMessageActivity extends VoBaseActivity {
    private MyFrindEntivity selectUser;
    private String forward_msg_id;
    @BindView(R.id.allFriendsForShareLV)
    public ListView LV;
    private AlLFriendsShareAdapter adapter;
    private DemoDBManager dbManager = DemoDBManager.getInstance();
    private List<MyFrindEntivity> allFriends = new ArrayList<>();
    private EMMessage forward_msg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_forwordmsg);
        forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        if (!TextUtils.isEmpty(forward_msg_id)) {
            forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        }
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        mInit();
    }

    private void mInit() {
        allFriends = dbManager.getAllFriends();
        adapter = new AlLFriendsShareAdapter(ForwardMessageActivity.this, allFriends);
        LV.setAdapter(adapter);
        LV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectUser = allFriends.get(position);
                new EaseAlertDialog(ForwardMessageActivity.this, null, getString(R.string.confirm_forward_to, selectUser.getNike()), null, new AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (confirmed) {
                            if (selectUser == null)
                                return;
                            try {
                                ChatActivity.activityInstance.finish();
                            } catch (Exception e) {
                            }

                            Intent intent = new Intent(ForwardMessageActivity.this, ChatActivity.class);
                            // it is single chat
                            intent.putExtra("userId", selectUser.getHuanxinID());
                            intent.putExtra("forward_msg_id", forward_msg_id);
                            intent.putExtra("remark", selectUser.getRemark());
                            startActivity(intent);

                            finish();
                        }
                    }
                }, true).show();
            }
        });

    }


//	protected void onListItemClick(int position) {
//		selectUser = contactAdapter.getItem(position);
//		new EaseAlertDialog(this, null, getString(R.string.confirm_forward_to, selectUser.getNick()), null, new AlertDialogUser() {
//            @Override
//            public void onResult(boolean confirmed, Bundle bundle) {
//                if (confirmed) {
//                    if (selectUser == null)
//                        return;
//                    try {
//                        ChatActivity.activityInstance.finish();
//                    } catch (Exception e) {
//                    }
//                    Intent intent = new Intent(ForwardMessageActivity.this, ChatActivity.class);
//                    // it is single chat
//                    intent.putExtra("userId", selectUser.getUsername());
//                    intent.putExtra("forward_msg_id", forward_msg_id);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        }, true).show();
//	}

}
