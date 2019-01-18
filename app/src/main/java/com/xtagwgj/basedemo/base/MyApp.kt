package com.xtagwgj.basedemo.base

import com.blankj.utilcode.util.LogUtils
import com.xtagwgj.base._Application
import com.xtagwgj.basedemo.BuildConfig
import com.xtagwgj.basedemo.db.AppDatabase

/**
 * 应用入口
 *
 * Created by xtagwgj on 2019/1/9
 */
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
        AppDatabase.getInstance(this)
    }
}