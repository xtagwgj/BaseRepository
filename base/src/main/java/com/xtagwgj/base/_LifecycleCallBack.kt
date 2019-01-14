package com.xtagwgj.base

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.xtagwgj.base.statusbar.Eyes
import org.jetbrains.annotations.NotNull

/**
 * 自定义的Lifecycle
 * Created by xtagwgj on 2019/1/9
 */
abstract class _LifecycleCallBack : Application.ActivityLifecycleCallbacks {

    private val showTitleMap = HashMap<Int, Boolean>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        AppManager.addActivity(activity)

        //状态栏的显示
        initStatusBarStyle(activity)

        // 是否全屏显示
        if (showFullScreen(activity) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.window.apply {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                statusBarColor = Color.TRANSPARENT
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        if (showTitleMap[activity.taskId] != true) {
            initToolbar(activity)
            showTitleMap[activity.taskId] = true
        }
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        showTitleMap.remove(activity.taskId)
        AppManager.removeActivity(activity)
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle?) {
    }

    /**
     *  状态栏 模式
     */
    open fun initStatusBarStyle(@NotNull activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Eyes.setStatusBarColor(activity, activity.resources.getColor(R.color.colorPrimaryDark, activity.theme))
        } else {
            Eyes.setStatusBarColor(activity, activity.resources.getColor(R.color.colorPrimaryDark))
        }
    }

    abstract fun showFullScreen(@NotNull activity: Activity): Boolean

    abstract fun initToolbar(@NotNull activity: Activity)
}