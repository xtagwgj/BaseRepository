package com.xtagwgj.basedemo

import android.app.Activity
import android.os.Build
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.xtagwgj.base.AppManager
import com.xtagwgj.base._LifecycleCallBack

/**
 * 处理各个页面的共有操作
 * Created by xtagwgj on 2019/1/9
 */
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