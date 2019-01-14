package com.xtagwgj.base.mvvm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 存储和处理跟 UI 界面相关的数据
 * Created by xtagwgj on 2019/1/9
 */
open class _ViewModel : ViewModel(), LifecycleObserver {

    /**
     * 要显示的吐司信息
     */
    val toast: MutableLiveData<String> = MutableLiveData()

    /**
     * 显示 loading 的对话框
     */
    val loadingText = MutableLiveData<String>()


}