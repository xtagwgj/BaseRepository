package com.xtagwgj.view.citylist.sortlistview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SectionIndexer
import android.widget.TextView
import com.xtagwgj.view.R

class SortAdapter(private val mContext: Context, list: List<SortModel>) : BaseAdapter(), SectionIndexer {
    private var list: List<SortModel>? = null

    init {
        this.list = list
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    fun updateListView(list: List<SortModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getCount(): Int = this.list!!.size

    override fun getItem(position: Int): Any = list!![position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, arg2: ViewGroup): View {
        var viewCreate = view
        var viewHolder: ViewHolder?
        if (viewCreate == null) {
            viewHolder = ViewHolder()
            viewCreate = LayoutInflater.from(mContext).inflate(R.layout.sortlistview_item, null)
            viewHolder.tvTitle = viewCreate!!.findViewById<View>(R.id.title) as TextView
            viewHolder.tvLetter = viewCreate.findViewById<View>(R.id.catalog) as TextView
            viewCreate.tag = viewHolder
        } else {
            viewHolder = viewCreate.tag as ViewHolder
        }
        if (null != list && !list!!.isEmpty()) {
            val (_, sortLetters) = list!![position]
            //根据position获取分类的首字母的Char ascii值
            val section = getSectionForPosition(position)

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                viewHolder.tvLetter!!.visibility = View.VISIBLE
                viewHolder.tvLetter!!.text = sortLetters
            } else {
                viewHolder.tvLetter!!.visibility = View.GONE
            }

            viewHolder.tvTitle!!.text = this.list!![position].name
        }

        return viewCreate

    }


    internal class ViewHolder {
        var tvLetter: TextView? = null
        var tvTitle: TextView? = null
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    override fun getSectionForPosition(position: Int): Int {
        return list!![position].sortLetters!![0].toInt()
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    override fun getPositionForSection(section: Int): Int {
        for (i in 0 until count) {
            val sortStr = list!![i].sortLetters
            val firstChar = sortStr!!.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }

        return -1
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private fun getAlpha(str: String): String {
        val sortStr = str.trim { it <= ' ' }.substring(0, 1).toUpperCase()
        // 正则表达式，判断首字母是否是英文字母
        return if (sortStr.matches("[A-Z]".toRegex())) {
            sortStr
        } else {
            "#"
        }
    }

    override fun getSections(): Array<Any>? {
        return null
    }
}