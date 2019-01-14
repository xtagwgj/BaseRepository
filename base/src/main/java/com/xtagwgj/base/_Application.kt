package com.xtagwgj.base

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 *
 * Created by xtagwgj on 2019/1/9
 */
abstract class _Application : Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化工具类
        Utils.init(this)

        //注册生命周期的回调，处理各 activity 的共有事件
        registerActivityLifecycleCallbacks(getLifecycle())
    }

    abstract fun getLifecycle(): ActivityLifecycleCallbacks

}