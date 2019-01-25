package com.xtagwgj.view.citylist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.xtagwgj.view.CleanableEditView
import com.xtagwgj.view.R
import com.xtagwgj.view.citylist.sortlistview.*
import java.util.*

/**
 * 城市选择
 * 方便用户自定义标题栏
 */
class CityListSelectFragment : Fragment() {

    lateinit var mCityTextSearch: CleanableEditView
    lateinit var mCurrentCityTag: TextView
    lateinit var mCurrentCity: TextView
    lateinit var mLocalCityTag: TextView
    lateinit var mLocalCity: TextView
    lateinit var sortListView: ListView
    lateinit var mDialog: TextView
    lateinit var mSidrbar: SideBar

    lateinit var adapter: SortAdapter

    /**
     * 汉字转换成拼音的类
     */
    private var sourceDateList: MutableList<SortModel>? = null

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private var pinyinComparator: PinyinComparator? = null
    private var cityListInfo: List<CityInfoBean> = ArrayList()

    private var cityInfoBean: CityInfoBean? = CityInfoBean()

    private var onCitySelectedListener: OnCitySelectedListener? = null

    companion object {
        private val PARAM_CURR = "param_city"

        @JvmStatic
        fun newInstance(currCity: String? = null) = CityListSelectFragment().apply {
            arguments = Bundle().apply {
                putString(PARAM_CURR, currCity ?: "")
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCitySelectedListener) {
            onCitySelectedListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnCitySelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onCitySelectedListener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_city_list_select, container, false)
        initView(view)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        setCityData(CityUtils.getCityList())
    }

    private fun initView(view: View) {
        mCityTextSearch = view.findViewById<View>(R.id.cityInputText) as CleanableEditView
        mCurrentCityTag = view.findViewById<View>(R.id.currentCityTag) as TextView
        mCurrentCity = view.findViewById<View>(R.id.currentCity) as TextView
        mLocalCityTag = view.findViewById<View>(R.id.localCityTag) as TextView
        mLocalCity = view.findViewById<View>(R.id.localCity) as TextView
        sortListView = view.findViewById<View>(R.id.country_lvcountry) as ListView
        mDialog = view.findViewById<View>(R.id.dialog) as TextView
        mSidrbar = view.findViewById(R.id.sidrbar)
        mCurrentCity.text = arguments?.get(PARAM_CURR)?.toString() ?: ""

        view.findViewById<View>(R.id.llCurrentCity).visibility =
                if (mCurrentCity.text.isNullOrEmpty())
                    View.GONE
                else
                    View.VISIBLE
    }


    private fun setCityData(cityList: List<CityInfoBean>) {
        cityListInfo = cityList
        val count = cityList.size
        val list = (0 until count).map { cityList[it].name }

        sourceDateList!!.addAll(filledData(list.toTypedArray()))
        // 根据a-z进行排序源数据
        Collections.sort(sourceDateList!!, pinyinComparator)
        adapter.notifyDataSetChanged()
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private fun filledData(date: Array<String>): List<SortModel> {
        val mSortList = ArrayList<SortModel>()

        for (i in date.indices) {
            val sortModel = SortModel()
            sortModel.name = date[i]
            //汉字转换成拼音
            val pinyin = CharacterParser.getSelling(date[i])
            val sortString = pinyin.substring(0, 1).toUpperCase()

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]".toRegex())) {
                sortModel.sortLetters = sortString.toUpperCase()
            } else {
                sortModel.sortLetters = "#"
            }

            mSortList.add(sortModel)
        }
        return mSortList
    }


    private fun initList() {
        sourceDateList = ArrayList()
        adapter = SortAdapter(context!!, sourceDateList!!)
        sortListView.adapter = adapter

        //实例化汉字转拼音类
        pinyinComparator = PinyinComparator()
        mSidrbar.setTextView(mDialog)
        //设置右侧触摸监听
        mSidrbar.setOnTouchingLetterChangedListener(object : SideBar.OnTouchingLetterChangedListener {

            override fun onTouchingLetterChanged(s: String) {
                //该字母首次出现的位置
                val position = adapter.getPositionForSection(s[0].toInt())
                if (position != -1) {
                    sortListView.setSelection(position)
                }
            }
        })

        sortListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val cityName = (adapter.getItem(position) as SortModel).name
            cityInfoBean = cityInfoBean?.findCity(cityListInfo, cityName!!)
            cityInfoBean?.let {
                onCitySelectedListener?.onCitySelected(it)
            }
        }

        //根据输入框输入值的改变来过滤搜索
        mCityTextSearch.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private fun filterData(filterStr: String) {
        var filterDateList: MutableList<SortModel>? = ArrayList()

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDateList
        } else {
            filterDateList!!.clear()
            for (sortModel in sourceDateList!!) {
                val name = sortModel.name
                if (name!!.contains(filterStr) || CharacterParser.getSelling(name).startsWith(filterStr)) {
                    filterDateList.add(sortModel)
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList!!, pinyinComparator)
        adapter.updateListView(filterDateList)
    }

    interface OnCitySelectedListener {
        fun onCitySelected(infoBean: CityInfoBean)
    }
}