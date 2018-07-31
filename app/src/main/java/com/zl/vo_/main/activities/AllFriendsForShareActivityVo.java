package com.zl.vo_.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zl.vo_.DemoApplication;
import com.zl.vo_.R;
import com.zl.vo_.adapter.AlLFriendsShareAdapter;
import com.zl.vo_.db.DemoDBManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/12/27.
 */

public class AllFriendsForShareActivityVo extends VoBaseActivity {
    @BindView(R.id.allFriendsForShareLV)
    public ListView allFriendsForShareLV;
    private AlLFriendsShareAdapter adapter;
    private DemoDBManager dbManager=DemoDBManager.getInstance();
    private List<MyFrindEntivity> allFriends=new ArrayList<>();
    private String param_gameid;
    private String param_roomid;
    private String param_kindid;

    private String packagename;
    private String game_avatar;
    private String game_content;
    private String game_name;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.lay_allfriend_share);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        Intent intent=getIntent();
        if(intent!=null){
            param_gameid=intent.getStringExtra("param_gameid");
            param_roomid=intent.getStringExtra("param_roomid");
            param_kindid=intent.getStringExtra("param_kindid");
            packagename=intent.getStringExtra("package_name");
            game_avatar=intent.getStringExtra("game_avatar");
            game_content=intent.getStringExtra("game_content");
            game_name=intent.getStringExtra("game_name");
        }
        mInit();
    }

    private void mInit() {
        allFriends=dbManager.getAllFriends();
        adapter=new AlLFriendsShareAdapter(AllFriendsForShareActivityVo.this,allFriends);
        allFriendsForShareLV.setAdapter(adapter);
        allFriendsForShareLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //带上参数给指定的好友发送分享
                Intent intent=new Intent(AllFriendsForShareActivityVo.this, ChatActivity.class);
                intent.putExtra("userId",allFriends.get(i).getHuanxinID());
                intent.putExtra("account",allFriends.get(i).getAccount());
                intent.putExtra("from_nick",allFriends.get(i).getNike());


                //名片上的内容
                intent.putExtra("from_avatar",game_avatar);
                intent.putExtra("package_name",packagename);
                intent.putExtra("game_content",game_content);
                intent.putExtra("game_name",game_name);
                if(!TextUtils.isEmpty(param_gameid)){
                    intent.putExtra("param_gameid",param_gameid);
                }
                if(!TextUtils.isEmpty(param_roomid)){
                    intent.putExtra("param_roomid",param_roomid);
                }
                if(!TextUtils.isEmpty(param_kindid)){
                    intent.putExtra("param_kindid",param_kindid);
                }

                intent.putExtra("share","2");
                startActivity(intent);
                //********************************************
                DemoApplication.PARAM_GAMEID=null;
                DemoApplication.PARAM_ROOMID=null;
                DemoApplication.PARAM_KINDID=null;

                DemoApplication.GAME_CONTENT=null;
                DemoApplication.PACKAGE_NAME=null;
                DemoApplication.GAME_AVATAR=null;
                DemoApplication.GAME_NAME=null;

                AllFriendsForShareActivityVo.this.finish();



            }
        });




    }
}
