package com.zl.vo_.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zl.vo_.R;
import com.zl.vo_.main.Entity.gameData;
import com.zl.vo_.update.UpdateAppManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */

public class GameAdapter extends BaseAdapter {
    private Context mContext;
    private List<gameData.gameInfo.gameCell> biglist;
    //********************
    // 下载应用的进度条
    private ProgressDialog progressDialog;
    private String TAG="gameAdapter";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() +"/" + "VoGame" +"/";
    // 下载应用存放全路径
    //private static final String FILE_NAME = Environment.getExternalStorageDirectory() +"/" + "VoGame" +"/" + "test.apk";
    private String currentDownloadPath;
    private String currentAppName;

    //***************

    public GameAdapter(Context mContext, List<gameData.gameInfo.gameCell> bigList) {
        this.mContext = mContext;
        this.biglist=bigList;
    }

    @Override
    public int getCount() {
        return biglist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final gameData.gameInfo.gameCell cell=biglist.get(i);
        viewHolder holder=null;
        if(view==null){
            view= LayoutInflater.from(mContext).inflate(R.layout.lay_game_item,null);
            holder=new viewHolder();
            holder.img=view.findViewById(R.id.game_img);
            holder.name=view.findViewById(R.id.game_name);
            holder.tv_doload=view.findViewById(R.id.game_download);
            holder.sign=view.findViewById(R.id.game_sign);
            holder.discription=view.findViewById(R.id.game_discription);
            view.setTag(holder);
        }else {
            holder= (viewHolder) view.getTag();
        }
        holder.sign.setText(cell.getSign()+".");
        holder.name.setText(cell.getGamename());
        holder.discription.setText(cell.getPackdescription());
        Glide.with(mContext).load(cell.getPicurl()).into(holder.img);
        boolean b=testPackageName(cell.getPackname());
        if(b){
            holder.tv_doload.setText("启动");
        }else {
            holder.tv_doload.setText("下载");
        }

        final String state=holder.tv_doload.getText().toString().trim();
        holder.tv_doload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if("启动".equals(state)){
                    GoStart(cell.getPackname());
                }else if("下载".equals(state)){
                    currentDownloadPath=cell.getDownurl();
                    currentAppName=cell.getApkname();
                   GoDownload(cell);
                }
            }
        });




        return view;
    }


    /****
     * 游戏已经下载，启动
     * @param packname
     */
    private void GoStart(String packname) {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(packname);
        if (intent != null) {
            mContext.startActivity(intent);
        }
    }

    /****
     * 测试是否安装改游戏
     * @param packname
     * @return
     */
    private boolean testPackageName(String packname) {
        if(!TextUtils.isEmpty(packname)){
            // 通过包名获取要跳转的app，创建intent对象
            Intent intent =mContext.getPackageManager().getLaunchIntentForPackage(packname);
            // 这里如果intent为空，就说名没有安装要跳转的应用嘛
            if (intent != null) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }


    /**
     * 游戏未下载，下载
     * @param
     * @param cell
     */
    private void GoDownload(gameData.gameInfo.gameCell cell) {
        showDownloadDialog(cell);
    }

    //****游戏下载开始********************************************



    /**
     * 显示下载进度对话框
     * @param cell
     */
    public void showDownloadDialog(gameData.gameInfo.gameCell cell) {

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new  downloadAsyncTask().execute();
    }

    /**
     * 下载新版本应用
     */
    private class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "执行至--onPreExecute");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            Log.e(TAG, "执行至--doInBackground");

            URL url;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            try {
                url = new URL(currentDownloadPath);
                connection = (HttpURLConnection) url.openConnection();
                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }
                File file=new File(FILE_PATH+currentAppName);
                if(!file.exists()){
                    file.createNewFile();
                }
                Log.i("ta",file.getAbsolutePath());
                out = new FileOutputStream(file);//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;
                Log.e(TAG, "执行至--readLength = 0");
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;
                    int curProgress = (int) (((float) readLength / fileLength) * 100);
                    Log.e(TAG, "当前下载进度：" + curProgress);
                    publishProgress(curProgress);
                    if (readLength >= fileLength) {
                        Log.e(TAG, "执行至--readLength >= fileLength");
                        break;
                    }
                }
                out.flush();
                return INSTALL_TOKEN;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            Log.e(TAG, "异步更新进度接收到的值：" + values[0]);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {

            progressDialog.dismiss();//关闭进度条
            //安装应用
            installApp();
        }
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        File appFile = new File(FILE_PATH+currentAppName);
        if (!appFile.exists()) {
            return;
        }
        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }



//结束********************************************




    class viewHolder{
        ImageView img;
        TextView sign;
        TextView name;
        TextView tv_doload;
        TextView discription;
    }
}
