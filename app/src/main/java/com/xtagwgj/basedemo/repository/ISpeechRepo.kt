package com.xtagwgj.basedemo.repository

import android.annotation.SuppressLint
import android.content.Context
import com.xtagwgj.basedemo.db.AppDatabase
import com.xtagwgj.basedemo.db.bean.Joke
import com.xtagwgj.basedemo.db.bean.JokeDetail
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 话术库数据的具体实现类
 * Created by xtagwgj on 2019/1/17
 */
class ISpeechRepo : SpeechRepo {

    /**
     * 获取话术列表
     */
    @SuppressLint("CheckResult")
    override fun getJokeList(context: Context, success: (List<Joke>) -> Unit) {
        val appDatabase = AppDatabase.getInstance(context)

        appDatabase.jokeDao().listJoke()
            .map { jokeList ->
                val directoryList = appDatabase.directoryDao().listAll()
                jokeList.forEach { joke ->
                    joke.directoryList = directoryList.filter { it.jokeTypeId == joke.id }
                }
                jokeList
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                success(it)
            }, {
                it.printStackTrace()
                success(listOf())
            })
    }

    /**
     * 获取话术详情
     * @param context
     * @param direcId   库 id        0为查询全部
     * @param searchKey 输入的字符
     * @param success
     */
    @SuppressLint("CheckResult")
    override fun getDetailList(
        context: Context,
        direcId: Long,
        searchKey: String,
        success: (List<JokeDetail>) -> Unit
    ) {
        val listSingle = if (direcId == 0L) {
            AppDatabase.getInstance(context).jokeDetailDao().listByKey("%$searchKey%")
        } else {
            AppDatabase.getInstance(context).jokeDetailDao().listByKey(direcId, "%$searchKey%")
        }

        listSingle.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                success(it)
            }, {
                it.printStackTrace()
                success(listOf())
            })
    }
}