package com.xtagwgj.basedemo.db.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * 话术库分类
 * Created by xtagwgj on 2019/1/17
 */
@Entity(tableName = "speech_joke")
data class Joke(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String//库名称
) {
    @Ignore
    var directoryList = listOf<Directory>()
}