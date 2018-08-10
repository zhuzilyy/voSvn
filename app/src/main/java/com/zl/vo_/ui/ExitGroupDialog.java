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

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.vo_.R;
import com.zl.vo_.main.activities.VoBaseActivity;


public class ExitGroupDialog extends VoBaseActivity {
    private ImageView imageView;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_logout_actionsheet);

        TextView text = (TextView) findViewById(R.id.tv_text);
        Button exitBtn = (Button) findViewById(R.id.btn_exit);
        
        text.setText(R.string.exit_group_hint);

        String toast = getIntent().getStringExtra("deleteTo0ast");
        if(toast != null) {
            text.setText(toast);
            exitBtn.setText("解散");
        }
        imageView=findViewById(R.id.iv_back);
        tv_title=findViewById(R.id.tv_title);
        tv_title.setText("退出");
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void logout(View view){
    	setResult(RESULT_OK);
        finish();
    }
    public void cancel(View view) {
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
