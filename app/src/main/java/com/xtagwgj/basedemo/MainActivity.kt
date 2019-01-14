package com.xtagwgj.basedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.xtagwgj.base.AppManager
import com.xtagwgj.base.mvvm._ViewModel

/**
 * 主页
 * Created by xtagwgj on 2019/1/9
 */
class MainActivity : BaseActivity<_ViewModel>() {

    companion object {
        fun doAction(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun getViewModel(): _ViewModel? {
        return null
    }

    override fun initView() {

    }

    override fun initEventListener() {
    }

    private var exitTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            //未处理监听事件，请求后续监听
            showToast("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            AppManager.AppExit(this, false)
        }

    }
}