package com.xtagwgj.view.roundview;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * 用于需要圆角矩形框背景的 Button 的情况,减少直接使用Button时引入的shape资源文件
 *
 * @author xtagwgj
 * @date 2018/1/24
 */

public class RoundButton extends Button {
    private RoundViewDelegate delegate;

    public RoundButton(Context context) {
        this(context, null);
    }

    public RoundButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        delegate = new RoundViewDelegate(this, context, attrs);
    }

    /**
     * use delegate to set attr
     */
    public RoundViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.isWidthHeightEqual() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate.isRadiusHalfHeight()) {
            delegate.setCornerRadius(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }
    }
}
