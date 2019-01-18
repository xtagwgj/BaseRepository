package com.xtagwgj.basedemo.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.xtagwgj.basedemo.db.bean.Directory

/**
 *
 * Created by xtagwgj on 2019/1/17
 */
@Dao
interface DirectoryDao {

//    @Query(
//        "SELECT speech_joke.name as jokeName,speech_directory.id as " +
//                "direcId,speech_directory.name AS direcName FROM speech_directory,speech_joke WHERE " +
//                "speech_directory.joke_type_id=speech_joke.id"
//    )
//    fun listWithJoke(): Single<List<JokeDirector>>

    @Query("SELECT * FROM speech_directory WHERE joke_type_id = :jokeId")
    fun listById(jokeId: Long): List<Directory>

    @Query("SELECT * FROM speech_directory")
    fun listAll(): List<Directory>

}