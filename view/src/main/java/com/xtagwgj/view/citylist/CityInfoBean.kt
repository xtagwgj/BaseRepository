package com.xtagwgj.view.citylist

import android.os.Parcel
import android.os.Parcelable

/**
 * 城市信息的实体
 * Created by xtagwgj on 2017/11/19.
 *
 *
 */
data class CityInfoBean(
    var id: Int,//城市Id
    var name: String,//城市名称
    var latitude: String,//纬度
    var longitude: String,//经度
    var firstLetter: String,//首字母
    var sort: Int//排序字段
) : Parcelable {

    constructor() : this(0, "", "", "", "", 0)

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(firstLetter)
        parcel.writeInt(sort)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<CityInfoBean> {
        override fun createFromParcel(parcel: Parcel): CityInfoBean = CityInfoBean(parcel)

        override fun newArray(size: Int): Array<CityInfoBean?> = arrayOfNulls(size)
    }

    fun findCity(list: List<CityInfoBean>, cityName: String): CityInfoBean? {
        try {
            list.indices
                .map { list[it] }
                .filter { cityName == it.name }
                .forEach { return it }
        } catch (e: Exception) {
            return null
        }

        return null
    }

}