package com.xtagwgj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 不能滚动的 GridView
 * Created by xtagwgj on 2019/1/17
 */
public class NoScrollGridView extends GridView {
    public NoScrollGridView(Context context) {
        this(context, null);
    }

    public NoScrollGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
