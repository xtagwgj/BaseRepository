package com.xtagwgj.basedemo.bean

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * Tab的使体内
 * Created by xtagwgj on 2017/7/4.
 */
open class TabEntity(val title: String, val selectedIcon: Int, val unSelectedIcon: Int) : CustomTabEntity {
    override fun getTabUnselectedIcon(): Int = unSelectedIcon

    override fun getTabSelectedIcon(): Int = selectedIcon

    override fun getTabTitle(): String = title
}