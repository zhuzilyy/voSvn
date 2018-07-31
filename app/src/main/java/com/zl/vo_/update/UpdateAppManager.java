package com.zl.vo_.update;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.zl.vo_.utils.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/12/25.
 */
public class UpdateAppManager {

    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/" + "AutoUpdate" + "/";
    // 下载应用存放全路径
    private static final String FILE_NAME = Environment.getExternalStorageDirectory() + "/" + "AutoUpdate" + "/" + "app.apk";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    //Log日志打印标签
    private static final String TAG = "Update_log";

    private Context context;
    //获取版本数据的地址
    private String version_path = Url.GetNewVOVewsionUrl;
    //获取新版APK的默认地址
    private String apk_path = "http://47.95.115.55:8080/voadmin/test.apk";
    // 下载应用的进度条
    private ProgressDialog progressDialog;

    //新版本号和描述语言
    private int update_versionCode;
    private String update_describe;
    private String update_downurlapk;

    public UpdateAppManager(Context context) {
        this.context = context;
    }

    /**
     * 获取当前版本号
     */
    private int getCurrentVersion() {
        try {

            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            Log.e(TAG, "当前版本名和版本号" + info.versionName + "--" + info.versionCode);

            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();

            Log.e(TAG, "获取当前版本号出错");
            return 0;
        }
    }

    /**
     * 从服务器获得更新信息
     */
    public void getUpdateMsg() {

        class mAsyncTask extends AsyncTask<String, Integer, String> {
            @Override
            protected String doInBackground(String... params) {
                HttpURLConnection connection = null;
                try {
                    URL url_version = new URL(params[0]);
                    connection = (HttpURLConnection) url_version.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream in = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));

                    Log.e(TAG, "bufferReader读到的数据--" + reader);

                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {             //回到主线程，更新UI

                Log.e(TAG, "异步消息处理反馈--" + s);
                try {
                    JSONObject object = new JSONObject(s);

                    // JSONObject dataJson=object.getJSONObject("data");
                    // JSONObject infoJson=dataJson.getJSONObject("Info");
                    String version = object.getString("version");

                    update_versionCode = Integer.parseInt(version);
                    // update_describe=infoJson.getString("describe");
                    update_downurlapk = object.getString("url");
                    Log.i("downurl", update_downurlapk + "");

//                    update_versionCode = object.getInt("version");
//                    update_describe = object.getString("describe");

                    Log.e(TAG, "新版本号--" + update_versionCode);
                    Log.e(TAG, "新版本描述--\n" + update_describe);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int currentVersion = getCurrentVersion();
                if (update_versionCode > currentVersion) {

                    Log.e(TAG, "提示更新！");
                    showNoticeDialog();
                } else {
                    Log.e(TAG, "已是最新版本！");
                }
            }
        }

        new mAsyncTask().execute(version_path);
    }


    /**
     * 显示提示更新对话框
     */
    private void showNoticeDialog() {
        new AlertDialog.Builder(context)
                .setTitle("检测到新版本！")
                .setMessage(update_describe)
                .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                }).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("正在更新...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        new downloadAsyncTask().execute();
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
                url = new URL(update_downurlapk);
                connection = (HttpURLConnection) url.openConnection();
                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }
                File file = new File(FILE_NAME);
                if (!file.exists()) {
                    file.createNewFile();
                }
                Log.i("ta", file.getAbsolutePath());
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
        File appFile = new File(FILE_NAME);
        if (!appFile.exists()) {
            return;
        }
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = appFile.getName().substring(appFile.getName().lastIndexOf(".") + 1);
        String type = mime.getMimeTypeFromExtension(ext);
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, "com.zl.vo_.fileprovider", appFile);//com.zl.vo_.fileprovider
                intent.setDataAndType(contentUri, type);
            } else if (Build.VERSION.SDK_INT >= 26) {
                //8.0
//                boolean b = context.getPackageManager().canRequestPackageInstalls();
//                if (b) {
//                    //installApk();//安装应用的逻辑(写自己的就可以)
//                } else {
//                    //请求安装未知应用来源的权限
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 150);
//
//                }

            } else {
                intent.setDataAndType(Uri.fromFile(appFile), type);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            Toast.makeText(context, "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
        }
    }





}