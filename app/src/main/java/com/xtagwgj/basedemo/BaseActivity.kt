package com.xtagwgj.basedemo

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.StringUtils
import com.xtagwgj.base._Activity
import com.xtagwgj.base.mvvm._ViewModel
import com.xtagwgj.base.utils.ToastUtil
import com.xtagwgj.view.loadingdialog.RxDialogLoading
import com.xtagwgj.view.loadingdialog.progressing.Style

/**
 * 对本 app 进行定制
 * Created by xtagwgj on 2019/1/9
 */
abstract class BaseActivity<T : _ViewModel?> : _Activity<T>() {

    /**
     * 加载框
     */
    private var loadingDialog: RxDialogLoading? = null

    override fun initViewModel() {
        super.initViewModel()

        //
        mViewModel?.toast?.observe(this, Observer {
            ToastUtil.showToast(this, it)
        })

        //加载框的文本
        mViewModel?.loadingText?.observe(this, Observer { loadingText ->
            if (StringUtils.isEmpty(loadingText)) {
                loadingDialog?.cancel()
            } else {
                if (loadingDialog == null) {
                    loadingDialog = RxDialogLoading(this)
                    loadingDialog?.setCancelable(true)
                    loadingDialog?.loadingView?.setStyle(Style.CIRCLE)
                }
                loadingDialog?.setLoadingText(loadingText)
                loadingDialog?.show()
            }
        })
    }
}