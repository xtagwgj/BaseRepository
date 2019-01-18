package com.xtagwgj.basedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.xtagwgj.base.mvvm._ViewModel
import com.xtagwgj.basedemo.base.BaseActivity

/**
 * 话术详情页面
 * Created by xtagwgj on 2019/1/9
 */
class SpeechDetailActivity : BaseActivity<_ViewModel>() {

    companion object {
        private const val PARAM_DIRECTORY_ID = "param_directory_id"
        private const val PARAM_TITLE = "param_title"

        fun doAction(activity: AppCompatActivity, title: String = "", directoryId: Long = 0L) {
            activity.startActivity(Intent(activity, SpeechDetailActivity::class.java).apply {
                putExtra(PARAM_TITLE, title)
                putExtra(PARAM_DIRECTORY_ID, directoryId)
            })
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_fragment

    override fun getViewModel(): _ViewModel? {
        return null
    }

    override fun initView() {
        val directoryId = intent.getLongExtra(PARAM_DIRECTORY_ID, 0L)

        //设置页面的标题
        if (directoryId > 0L) title = intent.getStringExtra(PARAM_TITLE)

        //加载显示的 Fragment
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.container,
                SpeechDetailFragment.newInstance(directoryId)
            )
            .commit()
    }

    override fun initEventListener() {

    }
}