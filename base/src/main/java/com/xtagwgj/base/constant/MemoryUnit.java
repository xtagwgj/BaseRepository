package com.xtagwgj.base.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 存储相关常量
 * Created by xtagwgj on 2019/1/9
 */
@IntDef({MemoryUnit.BYTE, MemoryUnit.KB, MemoryUnit.MB, MemoryUnit.GB})
@Retention(RetentionPolicy.SOURCE)
public @interface MemoryUnit {

    /**
     * Byte与Byte的倍数
     */
    int BYTE = 1;

    /**
     * KB与Byte的倍数
     */
    int KB = 1024;

    /**
     * MB与Byte的倍数
     */
    int MB = 1048576;

    /**
     * GB与Byte的倍数
     */
    int GB = 1073741824;
}
