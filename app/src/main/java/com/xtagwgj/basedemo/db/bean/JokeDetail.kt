package com.xtagwgj.basedemo.db.bean

import androidx.room.*

/**
 * 话术详情
 * Created by xtagwgj on 2019/1/17
 */
@Entity(
    tableName = "speech_jokedetail",
    foreignKeys = [ForeignKey(
        entity = Directory::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("direc_id")
    )],
    indices = [Index("direc_id")]
)
data class JokeDetail(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var sort: Int,
    var name: String,//分类名称
    var update_time: String,
    @ColumnInfo(name = "direc_id")
    var direc: Long,
    var content: String
){}