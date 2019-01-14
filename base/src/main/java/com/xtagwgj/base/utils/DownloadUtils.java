package com.xtagwgj.base.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.xtagwgj.base.utils.permissionpage.PermissionPageUtil;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;


/**
 * apk下载
 *
 * @author xtagwgj
 * on 2017/10/26.
 */
/*
        //Manifest中需要权限：
          <uses-permission android:name="android.permission.INTERNET" />
          <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
          <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />


        //为了适配 Android7.0 需要在 Manifest 中如下配置（其中com.xbdl.flalexaclock是项目的包名，一定要记得替换）
        <provider
            android:authorities="com.xbdl.flalexaclock.fileProvider"
            android:name="android.support.v4.content.FileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"/>
        </provider>

        //在 res 中创建文件夹 xml，并创建一个文件 file_paths 用来指定能够临时访问的文件路径，并在文件中如下配置：
        <?xml version="1.0" encoding="utf-8"?>
        <paths xmlns:android="http://schemas.android.com/apk/res/android">
            <!--http://blog.csdn.net/qq_26981913/article/details/73650304-->
             <external-path
                name="download"
                path="." />

        //使用方法：
        1.创建工具类对象，handler 为空则不返回下载进度；参数(上下文， handler)
        DownloadUtils downloadUtils = DownloadUtils(this, @SuppressLint("HandlerLeak")
         object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == DownloadUtils.DOWNLOAD_APK_UPDATE) {
                    val percent =  msg.arg2 * 1F / msg.arg1
                    val progress = Math.round(percent * 100)
                    Log.e("MainAccessActivity", "apk 下载更新 --> total=${msg.arg1} ,curr=${msg.arg2} ,percent=$progress%")
                }
            }
        })

        2.下载 apk 文件；参数(文件路径，文件保存的名称)
        downloadUtils?.downloadAPK(
                    DownloadUtils.ApkDownloadInfo.Builder()
                            .setUrl(TEST_APK_DOWNLOAD_PATH)
                            .setTitle("new Apk")
                            .setApkName("coolApk.apk")
                            .build()
         )


        3.使用完记得注销接收者
        downloadUtils?.unRegisterReceiver()
 */

public class DownloadUtils {

    private static final String TAG = "DownloadUtils";

    private static final String SUFFIX_NAME = ".apk";

    public static final int DOWNLOAD_APK_UPDATE = 1021;

    /**
     * 保存的下载 id
     */
    private final String DOWNLOAD_ID = "downloadId";

    /**
     * FileDownloadManager.getDownloadStatus如果没找到会返回-1
     */
    private static final int STATUS_UN_FIND = -1;

    /**
     * 下载器
     */
    private DownloadManager downloadManager;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 下载进度的观察者
     */
    private DownloadChangeObserver downloadChangeObserver;

    /**
     * 下载的 apk 信息
     */
    private ApkDownloadInfo apkDownloadInfo;

    private static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");

    public DownloadUtils(@NonNull Context context, @Nullable Handler handler) {
        this.mContext = context;
        if (handler != null) {
            downloadChangeObserver = new DownloadChangeObserver(handler);
        }

        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 下载apk
     */
    public void downloadAPK(@NonNull ApkDownloadInfo apkDownloadInfo) {
        this.apkDownloadInfo = apkDownloadInfo;

        if (!checkDownloadState(mContext)) {
            Toast.makeText(mContext, "请在设置中打开下载功能", Toast.LENGTH_SHORT).show();
            showDownloadSetting(mContext);
            return;
        }

        //获取DownloadManager
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager == null) {
            return;
        }

        //获取上次的下载 id
        long downloadId = readDownloadId();
        apkDownloadInfo.setDownloadId(downloadId);

        //如果上一次有下载的结果
        if (downloadId != -1L) {

            switch (getDownloadStatus(downloadId)) {
                //下载成功
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.e(TAG, "downloadId=" + downloadId + " ,status = STATUS_SUCCESSFUL");

                    Uri uri = getDownloadUri(downloadId);
                    if (uri != null) {
                        //本地的版本大于当前程序的版本直接安装
                        if (compare(mContext, queryDownloadedApk(downloadManager, downloadId).getPath())) {

                            if (downloadChangeObserver != null) {
                                Handler mHandler = downloadChangeObserver.mHandler;
                                if (mHandler != null) {
                                    Message msg = Message.obtain();
                                    msg.what = DOWNLOAD_APK_UPDATE;
                                    try {
                                        msg.arg1 = mContext.getContentResolver().openInputStream(uri).available();
                                        msg.arg2 = msg.arg1;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        msg.arg1 = 1;
                                        msg.arg2 = 1;
                                    }
                                    mHandler.sendMessage(msg);
                                }
                            }

                            installAPK();
                            return;
                        } else {
                            //从FileDownloadManager中移除这个任务
                            downloadManager.remove(downloadId);
                        }
                    }
                    startDownload(apkDownloadInfo.getUrl());
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Log.e(TAG, "download failed " + downloadId);
                    startDownload(apkDownloadInfo.getUrl());
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Log.e(TAG, "downloadId=" + downloadId + " ,status = STATUS_RUNNING");
                    break;
                case DownloadManager.STATUS_PENDING:
                    Log.e(TAG, "downloadId=" + downloadId + " ,status = STATUS_PENDING");
                    break;
                case DownloadManager.STATUS_PAUSED:
                    Log.e(TAG, "downloadId=" + downloadId + " ,status = STATUS_PAUSED");
                    break;
                case STATUS_UN_FIND:
                    Log.e(TAG, "downloadId=" + downloadId + " ,status = STATUS_UN_FIND");
                    startDownload(apkDownloadInfo.getUrl());
                    break;

                default:

                    break;
            }

        } else {
            //没有下载过，开始下载
            startDownload(apkDownloadInfo.getUrl());
        }

//        //注册广播接收者，监听下载状态
//        mContext.registerReceiver(receiver,
//                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 解绑 receiver
     */
    public void unRegisterReceiver() {
        if (mContext != null) {
            if (receiver != null) {
                mContext.unregisterReceiver(receiver);
            }
        }

        unRegisterObserver();
    }

    /**
     * 解绑观察者
     */
    private void unRegisterObserver() {
        if (mContext != null && downloadChangeObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(downloadChangeObserver);
        }
    }

    private void startDownload(String url) {
        Log.e(TAG, "startDownload url=" + url + " ,name = " + apkDownloadInfo.getApkName());

        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(apkDownloadInfo.getTitle());
        request.setDescription(apkDownloadInfo.getDescription());
        request.setVisibleInDownloadsUi(true);
        request.setMimeType("application/vnd.android.package-archive");
        //设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        //设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        //设置下载的路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apkDownloadInfo.getApkName());

        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        long downloadId = downloadManager.enqueue(request);
        Log.e("downloadUtils", "downloadId = " + downloadId);
        apkDownloadInfo.setDownloadId(downloadId);

        //保存本次下载的 id
        saveDownloadId(downloadId);

        //注册监听
        if (downloadChangeObserver != null) {
            mContext.getContentResolver().registerContentObserver(
                    CONTENT_URI,
                    true,
                    downloadChangeObserver);
        }
    }

    /**
     * 广播监听下载的各个状态
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    private int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                }
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(apkDownloadInfo.getDownloadId());
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));

            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installAPK();
                    unRegisterObserver();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;

                default:

                    break;
            }
        }
        cursor.close();
    }

    /**
     * 获取保存文件的地址
     */
    private Uri getDownloadUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

    /**
     * 下载到本地后执行安装
     */
    private void installAPK() {
        Log.e(TAG, "installApk");
        //获取下载文件的Uri
        File apkFile = queryDownloadedApk(downloadManager, apkDownloadInfo.getDownloadId());
        Log.e(TAG, "installApk apkFilePath:" + apkFile.getPath());

        if (apkFile != null && apkFile.exists()) {
            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Uri apkUri;
            //判读版本是否在7.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                //获取下载的文件夹路径 mContext.getCacheDir() 对应于 file_paths 里面的 cache-path； download对应于 file_path 里面的 path
                apkUri = FileProvider.getUriForFile(
                        mContext,
                        mContext.getPackageName() + ".fileProvider",
                        apkFile);
            } else {
                apkUri = Uri.fromFile(apkFile);
            }

            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }
    }

    /**
     * 获取 apk 包的信息
     */
    private PackageInfo getApkPackageInfo(String archiveFilePath) {
        PackageManager pm = mContext.getPackageManager();
        return pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
    }

    /**
     * 保存本次下载 id
     */
    private void saveDownloadId(long id) {
        SPUtils.getInstance().put(DOWNLOAD_ID, id);
    }

    /**
     * 读取上一次保存的下载 id
     */
    private long readDownloadId() {
        return SPUtils.getInstance().getLong(DOWNLOAD_ID, -1L);
    }

    /**
     * 删除最近下载的文件
     *
     * @return 删除文件的数目
     */
    public int deleteDownloadFile() {
        if (downloadManager == null) {
            downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        long downloadId = readDownloadId();

        if (downloadManager != null && downloadId != -1L) {
            return downloadManager.remove(downloadId);
        } else {
            return 0;
        }
    }

    /**
     * 检查 downloadManager 是否可用
     *
     * @param context 上下文
     * @return true 可用 false 不可用
     */
    private boolean checkDownloadState(Context context) {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 显示下载设置页面
     */
    private void showDownloadSetting(Context context) {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(context, intent)) {
            context.startActivity(intent);
        }
    }


    /**
     * 要启动的intent是否可用
     *
     * @return boolean
     */
    private boolean intentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    /**
     * 下载的apk和当前程序版本比较
     *
     * @param context Context 当前运行程序的Context
     * @param path    apk file's location
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private boolean compare(Context context, String path) {

        PackageInfo apkInfo = getApkPackageInfo(path);
        if (apkInfo == null) {
            return false;
        }

        String localPackage = context.getPackageName();

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);

            Log.e(TAG, "apk file packageName=" + apkInfo.packageName +
                    ",versionName=" + apkInfo.versionName);

            Log.e(TAG, "current app packageName=" + packageInfo.packageName +
                    ",versionName=" + packageInfo.versionName);

            if (apkInfo.packageName.equals(localPackage)) {
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通过downLoadId查询下载的apk，解决6.0以后安装的问题
     */
    private File queryDownloadedApk(DownloadManager downloadManager, long downloadId) {
        File targetApkFile = null;
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloadManager.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    Log.e(TAG, "queryDownloadedApk uriString=" + uriString);
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }

    public void goAppDetailsSettingIntent(Context context) {
//        Intent localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
//        activity.startActivity(localIntent);

        new PermissionPageUtil().jump2Page(context);
    }

    /**
     * 下载进度的观察者
     */
    private class DownloadChangeObserver extends ContentObserver {

        private Handler mHandler;

        private DownloadChangeObserver(Handler handler) {
            super(handler);
            mHandler = handler;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            if (downloadManager == null) {
                downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            }

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(apkDownloadInfo.getDownloadId());
            final Cursor cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                int totalSize = cursor.getInt(totalColumn);
                int currentSize = cursor.getInt(currentColumn);

                if (mHandler != null && totalSize > 0) {
                    Message msg = Message.obtain();
                    msg.what = DOWNLOAD_APK_UPDATE;
                    msg.arg1 = totalSize;
                    msg.arg2 = currentSize;
                    mHandler.sendMessage(msg);
                }
            }
        }
    }

    public static class ApkDownloadInfo {
        private long downloadId = -1;
        private String title;
        private String description;
        private String url;
        private String apkName;

        private ApkDownloadInfo(String title, String description, String url, String apkName) {
            this.title = title;
            this.description = description;
            this.url = url;
            this.apkName = apkName;
        }

        private long getDownloadId() {
            return downloadId;
        }

        private String getTitle() {
            return title;
        }

        private String getDescription() {
            return description;
        }

        private String getApkName() {
            return apkName;
        }

        private String getUrl() {
            return url;
        }

        private void setDownloadId(long downloadId) {
            this.downloadId = downloadId;
        }

        public static class Builder {
            private String title = "新版本Apk";
            private String description = "Apk Downloading Description";
            private String apkName;
            private String url;

            public Builder setTitle(String title) {
                this.title = title;
                return this;
            }

            public Builder setDescription(String description) {
                this.description = description;
                return this;
            }

            public Builder setUrl(String url) {
                this.url = url;
                return this;
            }

            public Builder setApkName(String apkName) {
                if (!apkName.endsWith(SUFFIX_NAME)) {
                    this.apkName = apkName + SUFFIX_NAME;
                } else {
                    this.apkName = apkName;
                }
                return this;
            }

            public ApkDownloadInfo build() {

                if (url == null || url.trim().length() == 0) {
                    throw new NullPointerException("apk下载路径不能为空");
                }

                if (apkName == null || apkName.trim().length() == 0) {
                    apkName = getNameByUrl();
                }

                return new ApkDownloadInfo(title, description, url, apkName);
            }

            private String getNameByUrl() {
                String tempUrl = url;

                if (!tempUrl.endsWith(SUFFIX_NAME)) {
                    int lastSuffix = tempUrl.lastIndexOf(SUFFIX_NAME);
                    if (lastSuffix != -1) {
                        tempUrl = tempUrl.substring(0, lastSuffix + 4);
                    } else {
                        return "release" + SUFFIX_NAME;
                    }
                }

                int lastSuffix = tempUrl.lastIndexOf("/");
                return lastSuffix != -1 ? tempUrl.substring(lastSuffix + 1) : "release" + SUFFIX_NAME;
            }

        }

    }

}
