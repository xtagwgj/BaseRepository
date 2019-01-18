package com.xtagwgj.basedemo.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.xtagwgj.basedemo.db.bean.Joke
import io.reactivex.Single

/**
 *
 * Created by xtagwgj on 2019/1/17
 */
@Dao
interface JokeDao {

    @Query("SELECT * FROM speech_joke")
    fun listJoke(): Single<List<Joke>>

}