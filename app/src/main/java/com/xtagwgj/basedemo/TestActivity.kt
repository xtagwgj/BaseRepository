package com.xtagwgj.basedemo

import com.blankj.utilcode.util.DeviceUtils
import com.xtagwgj.base.mvvm._ViewModel
import com.xtagwgj.basedemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_test.*

/**
 *
 * Created by xtagwgj on 2019/1/25
 */
class TestActivity : BaseActivity<_ViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_test

    override fun getViewModel(): _ViewModel? = null

    override fun initView() {
        btReboot.setOnClickListener {
            DeviceUtils.reboot()
        }
    }

    override fun initEventListener() {

    }
}