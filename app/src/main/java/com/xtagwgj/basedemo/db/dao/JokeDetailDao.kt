package com.xtagwgj.basedemo.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.xtagwgj.basedemo.db.bean.JokeDetail
import io.reactivex.Single

/**
 *
 * Created by xtagwgj on 2019/1/17
 */
@Dao
interface JokeDetailDao {

//    @Query("SELECT * FROM speech_jokedetail where direc_id = :directoryId")
//    fun listByDirectoryId(directoryId: Long): Single<List<JokeDetail>>

    @Query("SELECT * FROM speech_jokedetail where content like :searchKey")
    fun listByKey(searchKey: String): Single<List<JokeDetail>>

    @Query("SELECT * FROM speech_jokedetail where direc_id = :directoryId and content like :searchKey")
    fun listByKey(directoryId: Long, searchKey: String): Single<List<JokeDetail>>
}