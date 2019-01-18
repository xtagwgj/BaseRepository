package com.xtagwgj.basedemo.repository

import android.content.Context
import com.xtagwgj.basedemo.db.bean.Joke
import com.xtagwgj.basedemo.db.bean.JokeDetail

/**
 * 聊天话术的仓库
 * Created by xtagwgj on 2019/1/17
 */
interface SpeechRepo {

    /**
     * 获取话术列表
     */
    fun getJokeList(context: Context, success: (List<Joke>) -> Unit)

    /**
     * 获取话术详情
     * @param context
     * @param direcId   库 id        0为查询全部
     * @param searchKey 输入的字符
     * @param success
     */
    fun getDetailList(
        context: Context,
        direcId: Long,
        searchKey: String,
        success: (List<JokeDetail>) -> Unit
    )

}