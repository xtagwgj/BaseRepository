package com.xtagwgj.basedemo.db.bean

import androidx.room.*

/**
 * 库详情
 * Created by xtagwgj on 2019/1/17
 */
@Entity(
    tableName = "speech_directory",
    foreignKeys = [ForeignKey(
        entity = Joke::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("joke_type_id")
    )],
    indices = [Index("joke_type_id")]
)
data class Directory(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,//分类名称

    @ColumnInfo(name = "joke_type_id")
    var jokeTypeId: Long
)