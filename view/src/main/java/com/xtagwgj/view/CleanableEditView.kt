package com.xtagwgj.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText

/**
 * 可清除内容的editText
 * Created by xtagwgj on 2017/11/19.
 */
class CleanableEditView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.editTextStyle
) : EditText(context, attrs, defStyle), TextWatcher, View.OnFocusChangeListener {

    init {
        initWidget()
    }

    /**
     * 左右两侧图片资源
     */
    private var left: Drawable? = null
    private var right: Drawable? = null

    /**
     * 是否获取焦点，默认没有焦点
     */
    private var hasFocus = false

    /**
     * 手指抬起时的X坐标
     */
    private var xUp = 0


    /**
     * 初始化各组件
     *
     * @param attrs 属性集
     */
    private fun initWidget() {
        try {
            left = compoundDrawables[0]
            right = compoundDrawables[2]
            Log.e("CleanableEditView", "left = $left, right=$right")
            initDatas()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 初始化数据
     */
    private fun initDatas() {
        try {
            // 第一次显示，隐藏删除图标
            setCompoundDrawablesWithIntrinsicBounds(left, null, null, null)
            addListeners()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 添加事件监听
     */
    private fun addListeners() {
        try {
            onFocusChangeListener = this
            addTextChangedListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun beforeTextChanged(
        s: CharSequence, start: Int, count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, after: Int) {
        if (hasFocus) {
            if (TextUtils.isEmpty(s)) {
                // 如果为空，则不显示删除图标
                setCompoundDrawablesWithIntrinsicBounds(left, null, null, null)
            } else {
                // 如果非空，则要显示删除图标
                if (null == right) {
                    right = compoundDrawables[2]
                }
                setCompoundDrawablesWithIntrinsicBounds(left, null, right, null)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        try {
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // 获取点击时手指抬起的X坐标
                    xUp = event.x.toInt()
                    // 当点击的坐标到当前输入框右侧的距离小于等于getCompoundPaddingRight()的距离时，则认为是点击了删除图标
                    // getCompoundPaddingRight()的说明：Returns the right padding of the view, plus space for the right Drawable if any.
                    if (width - xUp <= compoundPaddingRight) {
                        if (!TextUtils.isEmpty(text.toString())) {
                            setText("")
                        }
                    }
                }
                else -> {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return super.onTouchEvent(event)
    }

    override fun afterTextChanged(s: Editable) {}

    override fun onFocusChange(v: View, hasFocus: Boolean) = try {
        this.hasFocus = hasFocus
        val msg = text.toString()

        if (hasFocus) {
            if (msg.equals("", ignoreCase = true)) {
                setCompoundDrawablesWithIntrinsicBounds(left, null, null, null)
            } else {
                setCompoundDrawablesWithIntrinsicBounds(left, null, right, null)
            }
        }
        if (hasFocus) {
        } else {
            setCompoundDrawablesWithIntrinsicBounds(left, null, null, null)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    //获取输入内容
    fun text_String(): String = text.toString()
}