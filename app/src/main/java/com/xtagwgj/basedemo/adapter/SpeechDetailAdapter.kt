package com.xtagwgj.basedemo.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xtagwgj.basedemo.R
import com.xtagwgj.basedemo.db.bean.JokeDetail

/**
 * 话术详情的适配器
 *
 * Created by xtagwgj on 2019/1/18
 */
class SpeechDetailAdapter :
    BaseQuickAdapter<JokeDetail, BaseViewHolder>(R.layout.item_speech_detail) {

    override fun convert(helper: BaseViewHolder, item: JokeDetail) {
        helper.setText(R.id.tv_detailName, item.name)
            .setText(R.id.tv_detail, Html.fromHtml(item.content))
    }
}