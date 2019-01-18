package com.xtagwgj.basedemo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xtagwgj.base._Fragment
import com.xtagwgj.basedemo.adapter.SpeechDetailAdapter
import com.xtagwgj.basedemo.viewmodel.SpeechViewModel
import com.xtagwgj.view.recyclerview.SimpleDividerDecoration
import kotlinx.android.synthetic.main.fragment_speech_detail.*

/**
 * 话术详情的 Fragment
 * Created by xtagwgj on 2019/1/18
 */
class SpeechDetailFragment : _Fragment<SpeechViewModel>() {

    companion object {
        private const val PARAM_DIRECTORY_ID = "param_directory_id"

        fun newInstance(directoryId: Long = 0L): Fragment {
            return SpeechDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong(PARAM_DIRECTORY_ID, directoryId)
                }
            }
        }
    }

    private val mAdapter by lazy {
        SpeechDetailAdapter()
    }

    private var directoryId = 0L

    override fun getViewModel(): SpeechViewModel? {
        return ViewModelProviders.of(this).get(SpeechViewModel::class.java)
    }

    override fun getLayoutRes(): Int = R.layout.fragment_speech_detail

    override fun initView() {
        super.initView()

        //获取传过来的库详情ID
        directoryId = arguments?.getLong(PARAM_DIRECTORY_ID) ?: 0L

        mSpeechDetailRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(SimpleDividerDecoration(activity!!))
        }.adapter = mAdapter
    }

    override fun initEventListener() {
        super.initEventListener()

        //监听输入的关键字
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                fetchByKey(s?.toString() ?: "")
            }
        })
    }

    override fun initViewModel() {
        super.initViewModel()

        mViewModel?.getJokeDetailLiveData()?.observe(this, Observer {
            mAdapter.setNewData(it)
        })

        fetchByKey()
    }

    /**
     * 通过输入关键字获取话术
     */
    private fun fetchByKey(key: String = etSearch.text.toString().trim()) {
        mViewModel?.getJokeDetails(activity!!, directoryId, key)
    }
}