package com.zl.vo_.main.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zl.vo_.R;
import com.zl.vo_.adapter.GridImageAdapter;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.util.FullyGridLayoutManager;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/28.
 */

public class WriterLifeNote extends VoBaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    @BindView(R.id.iv_back)
    public ImageView iv_back;
    @BindView(R.id.writeLifeNote_tv_submit)
    public TextView writeLifeNote_tv_submit;
    @BindView(R.id.writeLifeNote_tv_content)
    public EditText writeLifeNote_tv_content;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_writer_lifenote);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        mInit();
    }

    private void mInit() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(WriterLifeNote.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(WriterLifeNote.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(6);
        recyclerView.setAdapter(adapter);




    }
    @OnClick({R.id.iv_back,R.id.writeLifeNote_tv_submit})
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.writeLifeNote_tv_submit:

                String content=writeLifeNote_tv_content.getText().toString().trim();
                String picdata=getpicData_base64();
                if(TextUtils.isEmpty(content)&&TextUtils.isEmpty(picdata)){
                    Toast.makeText(this, "您要发表什么呢", Toast.LENGTH_SHORT).show();
                    return;
                }

                //发表人生笔记
                submitLifeNote(content,picdata);

                break;
            default:
                break;
        }

    }

    /***
     * 获取上传的图片
     * @return
     */
    private String getpicData_base64() {
        List<Bitmap> bitmaps=new ArrayList<>();
        String basePic="";

        for (int i = 0; i <selectList.size() ; i++) {
            Bitmap bitmap=BitmapFactory.decodeFile(selectList.get(i).getPath());
            bitmaps.add(bitmap);
        }
       if(bitmaps.size()>0){
           for (int i = 0; i <bitmaps.size() ; i++) {
              String baseStr=myUtils.Bitmap2StrByBase64(bitmaps.get(i));
               basePic+=baseStr+",";

           }
           return basePic;
       }
        return basePic;
    }

    /****
     * 写人生笔记
     */
    private void submitLifeNote(String Content,String picData) {

        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.WriteLifeNoteUrl);
        params.addParameter("userid",myUtils.readUser(WriterLifeNote.this).getUserid());
        if(!TextUtils.isEmpty(Content)){
            params.addParameter("content",Content);
        }

        if(!TextUtils.isEmpty(picData)){
            params.addParameter("picdata",picData);
        }


        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                Toast.makeText(WriterLifeNote.this, data.info, Toast.LENGTH_SHORT).show();
                if("0".equals(data.code)){
                    sendBroadcast(new Intent("publishLifeNoteOk"));
                    finish();
                }

            }

            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {

            if (true) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(WriterLifeNote.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(6)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        //.previewVideo(true)// 是否可预览视频
                        // .enablePreviewAudio(true) // 是否可播放音频
                        // .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        //.enableCrop(cb_crop.isChecked())// 是否裁剪
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        //.withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        // .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                        // .isGif(cb_isGif.isChecked())// 是否显示gif图片
                        // .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                        // .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                        // .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        //.showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        //  .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        .cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        }

    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getCompressPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
