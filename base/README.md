## Theme
沉浸模式 styles.xml 文件
```xml
<resources>

    <!-- 继承自父类的BaseTheme -->
    <style name="AppTheme" parent="BaseTheme">
        <!-- 设置自己想要的样式 -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

    <!-- 启动页面的主题 有白色背景，最好使用图片替代-->
    <style name="SplashTheme" parent="_SplashTheme">
        <!-- 可修改启动页面的图片 -->
        <item name="android:windowBackground">@mipmap/bg_login</item>
    </style>

</resources>
```

## 关于AndroidManifest
-  解决 Android 28 抓取不到 http 请求的问题
```xml
android:networkSecurityConfig="@xml/network_security_config"
```
-  文件读取和更新必须的配置
Manifest中配置


    <!--使用动态的 applicationId 是为了修复 package 改变时导致的路径错误问题"-->
    <provider
                android:name="android.support.v4.content.FileProvider"
                android:authorities="${applicationId}.fileProvider"
                android:exported="false"
                android:grantUriPermissions="true">
                <meta-data
                    android:name="android.support.FILE_PROVIDER_PATHS"
                    android:resource="@xml/provider_paths" />
    </provider>

res/xml/provider_paths.xml文件


    <?xml version="1.0" encoding="utf-8"?>
    <paths xmlns:android="http://schemas.android.com/apk/res/android">
        <external-path
            name="files_root"
            path="Android/data/com/bm" />
    
        <!--<external-path/>代表的根目录: Environment.getExternalStorageDirectory()-->
        <external-path
            name="external_storage_root"
            path="." />
    
        <external-path
            name="download"
            path="." />
    
    </paths>

## Application
应用入口，记得在Manifests中注册
```kotlin
class MyApp : _Application() {

    /**
     * 全周期监听 Activity，在这里进行所有 activity 的共有操作
     */
    override fun getLifecycle(): ActivityLifecycleCallbacks {
        return LifecycleCallBack()
    }

    override fun onCreate() {
        super.onCreate()

        //日志开关
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG)

        /**
         * 其他初始化操作
         */
        ...
    }
}
```

LifecycleCallBack.kt
```kotlin
class LifecycleCallBack : _LifecycleCallBack() {

    /**
     * 设置要全屏显示的 activity,比如闪屏页、登录页等等
     */
    override fun showFullScreen(activity: Activity): Boolean =
        when (activity.javaClass) {
            SplashActivity::class.java -> {
                true
            }

            else -> false
        }

    /**
     * 初始化 Toolbar
     */
    override fun initToolbar(activity: Activity) {
        activity.findViewById<RelativeLayout>(R.id.toolbar)?.let {

            //控制是否显示和隐藏 toolbar
            if (activity is AppCompatActivity) {
                activity.supportActionBar?.setDisplayShowTitleEnabled(false)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.actionBar?.setDisplayShowTitleEnabled(false)
            }

            //顶部标题栏的初始化，以及其他的初始化操作

            //初始化标题文字
            activity.findViewById<TextView>(R.id.toolbar_title)?.run {
                text = activity.title ?: ""
            }

            activity.findViewById<View>(R.id.toolbar_back)?.run {
                visibility = if (activity::class.java == getMainActivityClass()) {
                    View.GONE
                } else {
                    setOnClickListener {
                        //返回上一个界面
                        AppManager.finishActivity(activity)
                    }

                    View.VISIBLE
                }
            }
        }
    }

    /**
     * 获取不显示返回键的页面
     */
    private fun getMainActivityClass(): Class<out Activity>? {
        return MainActivity::class.java
    }
}
```

## Activity基类
```java
/**
 * 对本 app 基类进行定制,示例
 */
abstract class BaseActivity<T : _ViewModel?> : _Activity<T>() {

    /**
     * 加载框
     */
    private var loadingDialog: RxDialogLoading? = null

    override fun initViewModel() {
        super.initViewModel()

		//Toast 文本
        mViewModel?.toast?.observe(this, Observer {
			//Toast工具，根据自己的需求替换
            ToastUtil.showToast(this, it)
        })

        //加载框的文本
        mViewModel?.loadingText?.observe(this, Observer { loadingText ->
			//加载框实现，根据自己的需求替换
            if (StringUtils.isEmpty(loadingText)) {
                loadingDialog?.cancel()
            } else {
                if (loadingDialog == null) {
                    loadingDialog = RxDialogLoading(this)
                    loadingDialog?.setCancelable(true)
                    loadingDialog?.loadingView?.setStyle(Style.CIRCLE)
                }
                loadingDialog?.setLoadingText(loadingText)
                loadingDialog?.show()
            }
        })
    }
	
	//其他代码
}
```
## TitleBar 示例
其中`background `属性决定了标题栏沉浸样式的颜色，可改为自定义控件引用
```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/toolbar"
    android:layout_width="match_parent"
    android:layout_height="@dimen/abc_action_bar_default_height_material"
    android:background="@color/colorPrimaryDark"
    android:theme="@style/BaseTheme"
    app:contentInsetStart="0dp"
    app:popupTheme="@style/AppTheme.PopupOverlay"
    app:titleTextColor="@android:color/white">

    <ImageView
        android:id="@+id/toolbar_back"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_marginStart="8dp"
        android:contentDescription="@null"
        android:padding="8dp"
        android:src="@mipmap/icon_arrow" />

    <TextView
        android:id="@+id/toolbar_title"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:layout_gravity="center"
        android:textColor="@android:color/white"
        android:textSize="@dimen/text_size_title"
        tools:text="标题栏" />

    <ImageView
        android:id="@+id/toolbar_more"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_alignParentEnd="true"
        android:layout_gravity="center"
        android:layout_marginEnd="8dp"
        android:contentDescription="@null"
        android:padding="8dp"
        android:src="@mipmap/icon_more"
        android:visibility="gone"
        tools:visibility="visible" />

    <TextView
        android:id="@+id/toolbar_moreText"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_alignParentEnd="true"
        android:layout_gravity="center"
        android:layout_marginEnd="8dp"
        android:gravity="center"
        android:padding="8dp"
        android:textColor="@android:color/white"
        android:visibility="gone"
        tools:text="编辑"
        tools:visibility="visible" />

</RelativeLayout>
```

## APK 下载

Manifest中需要权限
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
在 res 中创建文件夹 xml，并创建一个文件 file_paths 用来指定能够临时访问的文件路径，并在文件中如下配置：
```
<?xml version="1.0" encoding="utf-8"?>
        <paths xmlns:android="http://schemas.android.com/apk/res/android">
            <!--http://blog.csdn.net/qq_26981913/article/details/73650304-->
             <external-path
                name="download"
                path="." />
```

为了适配 Android7.0 需要在 Manifest 中如下配置（其中com.xbdl.flalexaclock是项目的包名，一定要记得替换）

```xml
<provider
            android:authorities="${applicationId}.fileProvider"
            android:name="android.support.v4.content.FileProvider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths"/>
</provider>
```

关键代码
```kotlin
class MainActivity : BaseActivity<_ViewModel>() {
    /**
     * 下载 apk 的工具
     */
    protected var downloadUtils: DownloadUtils? = null

    /**
     * 下载进度的监听
     */
    private val downloadApkHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == DownloadUtils.DOWNLOAD_APK_UPDATE) {
                val percent = msg.arg2 * 1F / msg.arg1
                val progress = Math.round(percent * 100)
                LogUtils.e("MainAccessActivity", "apk 下载更新 --> total=${msg.arg1} ,curr=${msg.arg2} ,percent=$progress%")
                //更新进度
                //updateDialog.setProgress(progress)
            }
        }
    }

    /**
     * 下载新版本的 apk
     * @param downloadPath      apk 下载地址
     * @param apkTitle          系统下载器显示的标题
     * @param apkName           apk 文件的保存名称
     * @param downloadHandler   下载进度的监听，可为 null
     */
    @SuppressLint("CheckResult")
    protected fun downloadApk(downloadPath: String,
                              apkTitle: String = "发现新的版本",
                              apkName: String = "release.apk",
                              apkDescription: String = "",
                              downloadHandler: Handler? = null) {

        downloadUtils = downloadUtils ?: DownloadUtils(this, downloadHandler)
		
		//权限申请，可替换
        RxPermissions(this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe({
            if (it == true) {
			//开始下载 apk 文件
                downloadUtils?.downloadAPK(
                    DownloadUtils.ApkDownloadInfo.Builder()
                        .setUrl(downloadPath)
                        .setTitle(apkTitle)
                        .setApkName(apkName)
                        .setDescription(apkDescription)
                        .build()
                )
            } else {
                downloadUtils?.goAppDetailsSettingIntent(this)
            }
        }, {
            it.printStackTrace()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadUtils?.unRegisterReceiver()
    }
}
```

调用下载的方法
```java
downloadApk(
            versionBean.url,//下载地址
            apkName = "BM_${versionBean.code}_${versionBean.value}.apk",//保存的文件名
            apkDescription = versionBean.remark,//更新信息
            downloadHandler = downloadApkHandler//下载进度的监听
)
```