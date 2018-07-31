package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zl.vo_.R;
import com.zl.vo_.adapter.GameAdapter;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.Entity.gameData;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/21.
 */

public class Find_GameActivityVo extends VoBaseActivity implements View.OnClickListener {
    public ImageView iv_back;
    @BindView(R.id.findGame_lv)
    public ListView findGame_lv;
    private GameAdapter  adapter;
    private List<gameData.gameInfo.gameCell> bigList=new ArrayList<>();
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_find_game);
        VoBaseActivity.addActivity(this);
        ButterKnife.bind(this);
        getData();
        mInit();
    }

    private void getData() {
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.GameList);
        x.http().post(params, new MyCommonCallback<Result<gameData>>() {
            @Override
            public void success(Result<gameData> data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                  gameData game_data=  data.data;
                    gameData.gameInfo game_Info= game_data.getInfo();
                    List<gameData.gameInfo.gameCell> cells=game_Info.getPackage_list();
                    bigList.clear();
                    bigList.addAll(cells);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }

    private void mInit() {
        adapter=new GameAdapter(Find_GameActivityVo.this,bigList);
        findGame_lv.setAdapter(adapter);



    }
    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
