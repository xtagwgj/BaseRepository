package com.xtagwgj.basedemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xtagwgj.basedemo.R
import com.xtagwgj.basedemo.db.bean.Directory
import com.xtagwgj.basedemo.db.bean.Joke

/**
 * 话术分类的适配器
 *
 * Created by xtagwgj on 2019/1/18
 */
class SpeechJokeAdapter(val typeChoose: (Directory) -> Unit) :
    BaseQuickAdapter<Joke, BaseViewHolder>(R.layout.item_speech) {

    override fun convert(helper: BaseViewHolder, item: Joke) {
        helper.setText(R.id.tv_typeName, item.name)
        val gridView = helper.getView<GridView>(R.id.speechTypeGrid)
        gridView.adapter = GridAdapter(item.directoryList, typeChoose)
    }

    class GridAdapter(val datas: List<Directory>, val typeChoose: (Directory) -> Unit) :
        BaseAdapter() {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val convertViews: View
            val itemViewTag: ItemViewTag

            if (convertView == null) {
                convertViews =
                        LayoutInflater.from(parent.context).inflate(R.layout.item_speech_name, null)
                itemViewTag = ItemViewTag(convertViews.findViewById(R.id.typeText))
                convertViews.tag = itemViewTag
            } else {
                convertViews = convertView
                itemViewTag = convertView.tag as ItemViewTag
            }

            itemViewTag.nameText.text = getItem(position).name
            itemViewTag.nameText.setOnClickListener {
                typeChoose(getItem(position))
            }

            return convertViews
        }

        override fun getItem(position: Int): Directory {
            return datas[position]
        }

        override fun getItemId(position: Int): Long {
            return getItem(position).id
        }

        override fun getCount(): Int {
            return datas.size
        }

        data class ItemViewTag(
            var nameText: TextView
        )

    }

}