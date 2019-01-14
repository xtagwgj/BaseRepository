package com.xtagwgj.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xtagwgj.base.mvvm._ViewModel

/**
 * Fragment 基类
 * Created by xtagwgj on 2019/1/10
 */
abstract class _Fragment<T : _ViewModel?> : Fragment() {
    protected var mViewModel: T? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEventListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
    }

    abstract fun getViewModel(): T?

    abstract fun getLayoutRes(): Int

    open fun initView() {}

    open fun initEventListener() {}

    open fun initViewModel() {
        mViewModel = getViewModel()
    }
}