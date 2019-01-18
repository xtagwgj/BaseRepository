package com.xtagwgj.basedemo.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.xtagwgj.base.mvvm._ViewModel
import com.xtagwgj.basedemo.db.bean.Joke
import com.xtagwgj.basedemo.db.bean.JokeDetail
import com.xtagwgj.basedemo.repository.ISpeechRepo

/**
 * 聊天话术的 ViewModel
 *
 * Created by xtagwgj on 2019/1/18
 */
class SpeechViewModel : _ViewModel() {

    private val speechRepo by lazy {
        ISpeechRepo()
    }

    private var jokeTypeList: MutableLiveData<List<Joke>>? = null

    /**
     * 获取话术类别列表
     */
    fun getJokeTypeList(context: Context): MutableLiveData<List<Joke>> {
        jokeTypeList = jokeTypeList ?: MutableLiveData()
        speechRepo.getJokeList(context) {
            jokeTypeList?.postValue(it)
        }
        return jokeTypeList!!
    }

    private var jokeDetailList: MutableLiveData<List<JokeDetail>>? = null
    fun getJokeDetailLiveData(): MutableLiveData<List<JokeDetail>> {
        jokeDetailList = jokeDetailList ?: MutableLiveData()
        return jokeDetailList!!
    }

    /**
     * 获取话术详情
     */
    fun getJokeDetails(context: Context, direcId: Long = 0L, key: String) {
        speechRepo.getDetailList(context, direcId, key) {
            jokeDetailList?.postValue(it)
        }
    }


}