package com.xtagwgj.basedemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.flyco.tablayout.listener.CustomTabEntity

/**
 * 主页显示的切换的适配器
 *
 * Created by xtagwgj on 2019/1/17
 */
class MainPagerAdapter(
    private val fragments: List<Fragment>,
    private val tabEntitys: ArrayList<CustomTabEntity>,
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabEntitys[position].tabTitle
    }
}