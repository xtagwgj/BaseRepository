package com.xtagwgj.base.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 时间单位 相关常量
 */
@IntDef({TimeUnit.MSEC, TimeUnit.SEC, TimeUnit.MIN, TimeUnit.HOUR, TimeUnit.DAY})
@Retention(RetentionPolicy.SOURCE)
public @interface TimeUnit {

    /**
     * 秒与毫秒的倍数
     */
    int MSEC = 1;

    /**
     * 秒与毫秒的倍数
     */
    int SEC = 1000;

    /**
     * 分与毫秒的倍数
     */
    int MIN = 60000;

    /**
     * 时与毫秒的倍数
     */
    int HOUR = 3600000;

    /**
     * 天与毫秒的倍数
     */
    int DAY = 86400000;

    /**
     * 周与毫秒的倍数
     */
    int WEEK = 604800000;
}
