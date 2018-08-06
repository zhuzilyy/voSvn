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

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMGroup;
import com.zl.vo_.R;

import java.util.List;

public class GroupAdapter extends ArrayAdapter<EMGroup> {

	private LayoutInflater inflater;
	private String newGroup;
	private String addPublicGroup;
	private Context context;

	public GroupAdapter(Context context, int res, List<EMGroup> groups) {
		super(context, res, groups);
		this.inflater = LayoutInflater.from(context);
		newGroup = context.getResources().getString(R.string.The_new_group_chat);
		addPublicGroup = context.getResources().getString(R.string.add_public_group_chat);
		this.context=context;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return 0;
		} else if (position == 1) {
			return 1;
		}  else {
			return 2;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (position == 0) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_row_add_group, parent, false);
			}
			((ImageView) convertView.findViewById(R.id.avatar)).setImageResource(R.mipmap.build_group);
			((TextView) convertView.findViewById(R.id.name)).setText(newGroup);
		} else {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.em_row_group, parent, false);
			}
			EMGroup emGroup=getItem(position-1);
			((TextView) convertView.findViewById(R.id.name)).setText(getItem(position-1).getGroupName());
			if(!TextUtils.isEmpty(emGroup.getDescription())){
				Glide.with(context).load(emGroup.getDescription()).into((ImageView)convertView.findViewById(R.id.avatar));
			}else {
				((ImageView)convertView.findViewById(R.id.avatar)).setImageResource(R.drawable.ease_group_icon);
			}

		}

		return convertView;
	}

	@Override
	public int getCount() {
		return super.getCount() + 1;
	}

}
