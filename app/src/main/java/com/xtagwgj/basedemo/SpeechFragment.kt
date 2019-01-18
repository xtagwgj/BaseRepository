package com.xtagwgj.basedemo

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.xtagwgj.base._Fragment
import com.xtagwgj.basedemo.adapter.SpeechJokeAdapter
import com.xtagwgj.basedemo.viewmodel.SpeechViewModel
import kotlinx.android.synthetic.main.fragment_speech.*

/**
 * 话术的 Fragment
 * Created by xtagwgj on 2019/1/17
 */
class SpeechFragment : _Fragment<SpeechViewModel>() {

    companion object {
        fun newInstance() = SpeechFragment()
    }

    private val mAdapter by lazy {
        SpeechJokeAdapter { directory ->
            activity?.let {
                SpeechDetailActivity.doAction(it as AppCompatActivity, directory.name, directory.id)
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.fragment_speech

    override fun getViewModel(): SpeechViewModel? {
        return ViewModelProviders.of(this).get(SpeechViewModel::class.java)
    }

    override fun initView() {
        super.initView()

        mSpeechRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
        }.adapter = mAdapter

    }

    override fun initEventListener() {
        super.initEventListener()
        tvSearch.setOnClickListener {
            activity?.let {
                SpeechDetailActivity.doAction(it as AppCompatActivity)
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()

        //获取话术类型列表
        mViewModel?.getJokeTypeList(activity!!)?.observe(this, Observer {
            mAdapter.setNewData(it)
        })
    }

}