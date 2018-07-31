package com.zl.vo_.main.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIItemListener;
import com.squareup.picasso.Picasso;
import com.ybao.pullrefreshview.layout.BaseFooterView;
import com.ybao.pullrefreshview.layout.BaseHeaderView;
import com.ybao.pullrefreshview.layout.PullRefreshLayout;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.LifeNoteListData;
import com.zl.vo_.main.Entity.LoginData;
import com.zl.vo_.main.Entity.Result;
import com.zl.vo_.main.adapter.LifeNoteAdapter;
import com.zl.vo_.main.https.MyCommonCallback;
import com.zl.vo_.main.main_utils.PhotoUtils;
import com.zl.vo_.main.main_utils.myUtils;
import com.zl.vo_.utils.Url;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/6.
 */

public class LifeNote extends VoBaseActivity implements View.OnClickListener,BaseHeaderView.OnRefreshListener,BaseFooterView.OnLoadListener{
    @BindView(R.id.iv_back)
    public ImageView back;
    @BindView(R.id.lv)
    public ListView lv;
    @BindView(R.id.header)
    public BaseHeaderView headerView;
    @BindView(R.id.footer)
    public BaseFooterView footerView;
    @BindView(R.id.root)
    public PullRefreshLayout mmroot;
    private boolean isLoad;
    private boolean isRefrsh;
    public LifeNoteAdapter adapter;
//    @BindView(R.id.writeLifeNote_tv)
//    public TextView writeLifeNote_tv;
    //-------------------------

    public ImageView lifeNote_bg_icon;
    public ImageView lifeNote_header_bg;
    public CircleImageView lifeNote_header_avator;
    public TextView lifeNote_header_name;

    public List<LifeNoteListData.LifeNoteListInfo.LifeNoteListList> bigList=new ArrayList<>();
    private  int page=1;
    @BindView(R.id.loading_view)
    public RelativeLayout loading_view;

    @BindView(R.id.re_del)
    public RelativeLayout re_del;
    @BindView(R.id.btn_confirm)
    public Button btn_confirm;
    @BindView(R.id.btn_cancel)
    public Button btn_cancel;

    public MyReceiver myReceiver;

    public int currentPositon;
    private int nn=0;
    private RelativeLayout writeLifeNote_card;
//***************************************************************7.0拍照相册
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_life_note);
        ButterKnife.bind(this);
        VoBaseActivity.addActivity(this);
        //------------------
        myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter("publishLifeNoteOk");
        registerReceiver(myReceiver,filter);
        //-----------------
        getData(page);
        mInit();
    }
    /****
     * 网络请求人生笔记数据
     */
    private void getData(int page) {
        if(!isLoad&&!isRefrsh){
            loading_view.setVisibility(View.VISIBLE);
        }
        RequestParams params=new RequestParams(Url.GetLifeNoteUrl);
        params.addParameter("userid",myUtils.readUser(LifeNote.this).getUserid());
        params.addParameter("page",page);
        x.http().post(params, new MyCommonCallback<Result<LifeNoteListData>>() {
            @Override
            public void success(Result<LifeNoteListData> data) {
                loading_view.setVisibility(View.GONE);
                if("0".equals(data.code)){
                    LifeNoteListData listData=data.data;
                    LifeNoteListData.LifeNoteListInfo listInfo=listData.getInfo();
                    List<LifeNoteListData.LifeNoteListInfo.LifeNoteListList> noteListLists= listInfo.getPerson_record_list();
                    if(isRefrsh){
                        isRefrsh=false;
                        bigList.clear();
                        bigList.addAll(noteListLists);
                        adapter.notifyDataSetChanged();
                    }else if(isLoad){
                        isLoad=false;
                        bigList.addAll(noteListLists);
                        adapter.notifyDataSetChanged();
                    }else {
                        bigList.addAll(noteListLists);
                        adapter.notifyDataSetChanged();
                    }
                }
                if(bigList.size()<=0){
                    Toast.makeText(LifeNote.this, "暂无信息", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
    private void mInit() {
        headerView.setOnRefreshListener(this);
        footerView.setOnLoadListener(this);
        //*******************************************
        adapter=new LifeNoteAdapter(LifeNote.this,bigList);
        View lifeNoteHeader= LayoutInflater.from(LifeNote.this).inflate(R.layout.life_note_header,null);
        lv.addHeaderView(lifeNoteHeader);
        lifeNote_header_bg=lifeNoteHeader.findViewById(R.id.lifeNote_header_bg);
        lifeNote_bg_icon=lifeNoteHeader.findViewById(R.id.lifeNote_bg_icon);
        lifeNote_header_name=lifeNoteHeader.findViewById(R.id.lifeNote_header_name);
        lifeNote_header_avator=lifeNoteHeader.findViewById(R.id.lifeNote_header_avator);
        writeLifeNote_card=lifeNoteHeader.findViewById(R.id.writeLifeNote_circle);
        lifeNote_bg_icon.setOnClickListener(this);
        writeLifeNote_card.setOnClickListener(this);
        lv.setAdapter(adapter);
        /**
         * 删除弹框
         */
        adapter.setDeleteLifeNoteLister(new LifeNoteAdapter.deleteLifeNoteLister() {
            @Override
            public void deleteLifeNote(View v, final int positon) {
                currentPositon=Integer.parseInt(bigList.get(positon).getId());
                //删除某条人生笔记
                final Dialog dialog = new Dialog(LifeNote.this);
                dialog.setContentView(LayoutInflater.from(LifeNote.this).inflate(R.layout.lay_dia_delete_item,null));
                Button btn_confirm = dialog.findViewById(R.id.confirm_btn);
                RelativeLayout cancelBtn = dialog.findViewById(R.id.cancelBtn);
                cancelBtn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteLifeNote_item(positon+"");
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        LoginData.LoginInfo.LoginAccountInfo currentUser= myUtils.readUser(LifeNote.this);
        if(currentUser!=null){
            if(!TextUtils.isEmpty(currentUser.getAvatar())){
                //默认头像
                Picasso.with(LifeNote.this).load(myUtils.readUser(LifeNote.this).getAvatar())
                        .placeholder(R.drawable.fx_default_useravatar).into(lifeNote_header_avator);
                lifeNote_header_name.setText(currentUser.getAccount());
            }
            if(!TextUtils.isEmpty(currentUser.getPerson_record_backpic())){
                Picasso.with(LifeNote.this).load(myUtils.readUser(LifeNote.this).getPerson_record_backpic())
                        .placeholder(R.mipmap.qiujing).into(lifeNote_header_bg);
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    /****
     * 删除人生笔
     */
    private void deleteLifeNote_item(String id) {
        //TODO 人生笔记删除失败
        loading_view.setVisibility(View.VISIBLE);
        RequestParams params=new RequestParams(Url.DelLifeNoteUrl);
        params.addParameter("id",id);
        x.http().post(params, new MyCommonCallback<Result>() {
            @Override
            public void success(Result data) {
                loading_view.setVisibility(View.GONE);
                String info = data.info;
                if("0".equals(data.code)){
                    isRefrsh=true;
                    page=1;
                    nn=1;
                  getData(page);
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }
    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.writeLifeNote_circle:
                startActivity(new Intent(LifeNote.this,WriterLifeNote.class));
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.lifeNote_bg_icon:
                //头像
                List<String> strings = new ArrayList<>();
                strings.add("相册");
                strings.add("照相机");

                DialogUIUtils.showBottomSheetAndCancel(LifeNote.this, strings, "取消", new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {

                        switch(position){
                            case 0:
                                requestPermissions(LifeNote.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                                    @Override
                                    public void granted() {
                                        PhotoUtils.openPic(LifeNote.this, CODE_GALLERY_REQUEST);
                                    }

                                    @Override
                                    public void denied() {
                                        Toast.makeText(LifeNote.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            case 1:
                                //指定action   [照相机]
                                requestPermissions(LifeNote.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new RequestPermissionCallBack() {
                                    @Override
                                    public void granted() {
                                        if (hasSdcard()) {
                                            imageUri = Uri.fromFile(fileUri);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                                //通过FileProvider创建一个content类型的Uri
                                                imageUri = FileProvider.getUriForFile(LifeNote.this, "com.zl.vo_.fileprovider", fileUri);
                                            PhotoUtils.takePicture(LifeNote.this, imageUri, CODE_CAMERA_REQUEST);
                                        } else {
                                            Toast.makeText(LifeNote.this, "设备没有SD卡！", Toast.LENGTH_SHORT).show();
                                            Log.e("asd", "设备没有SD卡");
                                        }
                                    }

                                    @Override
                                    public void denied() {
                                        Toast.makeText(LifeNote.this, "部分权限获取失败，正常功能受到影响", Toast.LENGTH_LONG).show();
                                    }
                                });
                                break;
                            default:
                                break;
                        }

                    }

                    @Override
                    public void onBottomBtnClick() {
                    }
                }).show();




                break;

            default:
            break;


        }
    }


    private void showImages(Bitmap bitmap) {
        lifeNote_header_bg.setImageBitmap(bitmap);
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int output_X = 480, output_Y = 480;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 0, 0, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.zl.vo_.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 0, 0, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(LifeNote.this, "设备没有SD卡!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showImages(bitmap);
                        setLifeNoteBg(bitmap);
                    }
                    break;
            }
        }
    }
    /****
     * 设置人生笔记背景
     * @param bitmap_album
     */
    private void setLifeNoteBg(Bitmap bitmap_album) {

        Log.i("ui",bitmap_album.toString());
        //Bitmap bitmap=getBitmap();
        String base64_=myUtils.Bitmap2StrByBase64(bitmap_album);
        Log.i("base",base64_+ "==");

        RequestParams params=new RequestParams(Url.UpdateBaseInfoUrl);
        params.addParameter("userid",myUtils.readUser(LifeNote.this).getUserid());
        params.addParameter("person_record_backpic_data",base64_);

        x.http().post(params, new MyCommonCallback<Result<LoginData>>() {
            @Override
            public void success(Result<LoginData> data) {

                loading_view.setVisibility(View.GONE);
                LoginData loginData=data.data;
                LoginData.LoginInfo loginInfo=loginData.getInfo();
                if(loginInfo!=null){
                    LoginData.LoginInfo.LoginAccountInfo user=loginInfo.getAccount_info();
                    if(user!=null){
                        Log.i("uu","pp=="+user.getPerson_record_backpic());
                        //清除本地保存的用户信息
                        myUtils.clearSharedUser(LifeNote.this);
                        myUtils.saveUser(user,LifeNote.this);
                        String hh= myUtils.readUser(LifeNote.this).getAvatar();
                        Log.i("pp","设置背景成功"+hh);
                        sendBroadcast(new Intent("FixPersonInfoOK"));
                    }
                }
            }
            @Override
            public void error(Throwable ex, boolean isOnCallback) {
                loading_view.setVisibility(View.GONE);
            }
        });
    }



    @Override
    public void onLoad(BaseFooterView baseFooterView) {
        baseFooterView.postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoad=true;
                page++;
                getData(page);
                footerView.stopLoad();
            }
        },1000);

    }

    @Override
    public void onRefresh(BaseHeaderView baseHeaderView) {
        baseHeaderView.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefrsh=true;
                page=1;
                getData(page);
                headerView.stopRefresh();
            }
        },1000);
    }

    //************************
    public class  MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
           String action= intent.getAction();
            if("publishLifeNoteOk".equals(action)){
                page=1;
                isRefrsh=true;
                getData(page);

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }


    }
}
