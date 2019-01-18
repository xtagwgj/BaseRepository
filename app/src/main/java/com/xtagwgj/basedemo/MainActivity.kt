package com.xtagwgj.basedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.xtagwgj.base.AppManager
import com.xtagwgj.base.mvvm._ViewModel
import com.xtagwgj.basedemo.base.BaseActivity
import com.xtagwgj.basedemo.bean.TabEntity

/**
 * 主页
 * Created by xtagwgj on 2019/1/9
 */
class MainActivity : BaseActivity<_ViewModel>() {

    /**
     *  fragment 的集合
     */
    private val fragments by lazy {
        listOf<Fragment>(
            SpeechFragment.newInstance()
        )
    }

    /**
     * 底部 tab 按钮的实体类集合
     */
    private val tabEntities by lazy {
        arrayListOf<CustomTabEntity>(
            TabEntity(
                getString(R.string.text_tab_chat),
                R.drawable.icon_chat_on,
                R.drawable.icon_chat_off
            )
//            TabEntity(getString(R.string.text_tab_msg), R.mipmap.icon_message_on, R.mipmap.icon_message),
//            TabEntity(getString(R.string.text_tab_mine), R.mipmap.icon_mine_on, R.mipmap.icon_mine_off)
        )
    }

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

//        displayBottomTab()
    }

    override fun initEventListener() {
    }

//    private fun displayBottomTab(showIndex: Int = 0) {
//        with(tabLayout) {
//            setTabData(tabEntities)
//            setOnTabSelectListener(object : OnTabSelectListener {
//                override fun onTabSelect(position: Int) {
//                    view_pager.currentItem = position
//                }
//
//                override fun onTabReselect(position: Int) {
//
//                }
//            })
//        }
//
//        view_pager.adapter = MainPagerAdapter(fragments, tabEntities, supportFragmentManager)
//        view_pager.currentItem = showIndex
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            fragments.forEach {
                it.onActivityResult(requestCode, resultCode, data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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