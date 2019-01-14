package com.xtagwgj.view.citylist.sortlistview

import java.util.*

class PinyinComparator : Comparator<SortModel> {

    override fun compare(o1: SortModel, o2: SortModel): Int {
        return if (o1.sortLetters == "@" || o2.sortLetters == "#") {
            -1
        } else if (o1.sortLetters == "#" || o2.sortLetters == "@") {
            1
        } else {
            o1.sortLetters!!.compareTo(o2.sortLetters!!)
        }
    }

}
