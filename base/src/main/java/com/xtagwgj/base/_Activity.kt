package com.xtagwgj.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import com.xtagwgj.base.mvvm._ViewModel
import com.xtagwgj.base.utils.ToastUtil

/**
 * activity 的基类
 * Created by xtagwgj on 2019/1/9
 */
abstract class _Activity<T : _ViewModel?> : AppCompatActivity(), LifecycleObserver {

    protected var mViewModel: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())

        initView()
        initEventListener()
        initViewModel()
    }

    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): T?

    abstract fun initView()

    abstract fun initEventListener()

    open fun initViewModel() {
        mViewModel = getViewModel()?.apply {
            lifecycle.addObserver(this!!)
        }
    }

    /**
     * 显示吐司
     */
    protected fun showToast(msg: String?) {
        mViewModel?.toast?.postValue(msg) ?: ToastUtil.showToast(this, msg)
    }

    /**
     * 显示加载文本
     */
    protected fun showLoadingText(msg: String?) {
        mViewModel?.loadingText?.postValue(msg)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel?.let {
            lifecycle.removeObserver(it!!)
        }
    }
}