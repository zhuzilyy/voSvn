package com.zl.vo_.main.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LifeNoteInfoData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.adapter.LifeNoteMorePicAdapte;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class LifeNoteDetailsActivityVo extends VoBaseActivity implements View.OnClickListener{
    private String id_str;

    @BindView(R.id.lifenote_details_gridview)
    public com.zl.vo_.main.views.NewGridView lifenote_details_gridview;
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.lifenote_details_time)
    public TextView lifenote_details_time;
    @BindView(R.id.lifenote_details_content)
    public TextView lifenote_details_content;

    public TextView time;
    private View header;

    //private LifeNoteDetailsAdapter adapter;
    private TextView content;
    private LifeNoteMorePicAdapte adapte;

    @BindView(R.id.lifenote_big_re)
    public RelativeLayout lifenote_big_re;
    @BindView(R.id.lifenote_big_iv)
    public ImageView lifenote_big_iv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_lifenote_details);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        id_str=getIntent().getStringExtra("id");
        getData(id_str);
        mInit();


    }
    //网络请求
    private void getData(String id) {
        RequestParams params=new RequestParams(Url.GetLifeNotInfoUrl);
        params.addParameter("id",id);
        x.http().post(params, new MyCommonCallback<Result<LifeNoteInfoData>>() {
            @Override
            public void success(Result<LifeNoteInfoData> data) {
                if("0".equals(data.code)){
                    LifeNoteInfoData infoData= data.data;
                    LifeNoteInfoData.LifeNoteInfo noteInfo=infoData.getInfo();
                    LifeNoteInfoData.LifeNoteInfo.LifeNoteDetails details=noteInfo.getPerson_record_info();
                    if(details!=null){
                        setUi(details);
                    }
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {

            }
        });
    }

    /****
     * 给界面ui设置数据
     * @param details
     */
    private void setUi(final LifeNoteInfoData.LifeNoteInfo.LifeNoteDetails details) {
        lifenote_details_time.setText(details.getAddtime());
        lifenote_details_content.setText(details.getContent());
        adapte=new LifeNoteMorePicAdapte(LifeNoteDetailsActivityVo.this,details.getPicarr());
        lifenote_details_gridview.setAdapter(adapte);
        lifenote_details_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lifenote_big_re.setVisibility(View.VISIBLE);
                Glide.with(LifeNoteDetailsActivityVo.this).load(details.getPicarr().get(i)).into(lifenote_big_iv);

            }
        });

    }

    private void mInit() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

       /* header= LayoutInflater.from(LifeNoteDetailsActivityVo.this).inflate(R.layout.lay_lifenotedetails_header,null);
        time= header.findViewById(R.id.lifenote_details_time);
        content= header.findViewById(R.id.lifenote_details_content);*/

    }

    @OnClick({R.id.lifenote_big_re,R.id.lifenote_big_iv})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.lifenote_big_re:
                lifenote_big_re.setVisibility(View.GONE);

                break;
            case R.id.lifenote_big_iv:
                lifenote_big_re.setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }
}
