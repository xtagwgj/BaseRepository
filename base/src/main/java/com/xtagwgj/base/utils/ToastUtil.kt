package com.xtagwgj.base.utils

import android.content.Context
import androidx.annotation.StringRes
import android.view.Gravity
import android.widget.Toast

/**
 * toast 工具类，可自动替换未取消的消息
 * Created by xtagwgj on 2019/1/9
 */
object ToastUtil {
    private var oldMsg: String? = null

    private var toast: Toast? = null


    private var oneTime: Long = 0
    private var twoTime: Long = 0

    fun showToast(context: Context, content: String?) {
        if (content == null || content.isEmpty()) {
            return
        }

        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
            }
            toast?.show()
            oneTime = System.currentTimeMillis()
        } else {
            twoTime = System.currentTimeMillis()
            if (content == oldMsg) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast?.show()
                }
            } else {
                oldMsg = content
                toast?.setText(content)
                toast?.show()
            }
        }
        oneTime = twoTime
    }

    fun showToast(context: Context, @StringRes resId: Int) {
        showToast(context, context.getString(resId))
    }
}