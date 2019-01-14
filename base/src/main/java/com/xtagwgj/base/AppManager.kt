package com.xtagwgj.base

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import java.util.*

/**
 * 管理activity
 * Created by xtagwgj on 2017/11/18.
 */
object AppManager {
    private val TAG = "AppManager"

    private val activityStack = Stack<Activity>()

    fun addActivity(activity: Activity) {
        activityStack.add(activity)
        Log.d(TAG, "$activity 已经入栈")
    }

    fun currActivity() = activityStack.lastElement()


    fun finishActivity(activity: Activity = currActivity()) {
        Log.d(TAG, "$activity 已经出栈")
        activityStack.remove(activity)
        activity.finish()
    }

    fun finishActivity(cls: Class<Activity>) {
        activityStack.forEach {
            if (it.javaClass == cls) {
                finishActivity(it)
            }
        }
    }

    fun removeActivity(activity: Activity) {
        finishActivity(activity)
    }

    fun finishAllActivity() {
        activityStack.asReversed().forEach {
            finishActivity(it)
        }

        activityStack.clear()
    }

    fun isOpenActivity(cls: Class<Activity>): Boolean {

        activityStack.forEach {
            if (it.javaClass == cls) {
                return true
            }
        }

        return false
    }

    fun finishAllButThis(cls: Class<Activity>) {
        if (activityStack.size > 1) {
            activityStack.asReversed().forEach {
                if (it::class.java !== cls) {
                    finishActivity(it)
                }
            }
        }
    }

    fun getActivity(cls: Class<Activity>): Activity? {
        activityStack.forEach {
            if (it.javaClass == cls) {
                return it
            }
        }

        return null
    }

    fun return2Activity(cls: Class<Activity>) {
        val thisList = activityStack.asReversed()
        LogUtils.e("dd", thisList)


        while (thisList.first().javaClass != cls) {
            finishActivity(thisList.first())
        }

    }

    fun AppExit(context: Context, isBackground: Boolean = false) {
        try {
            finishAllActivity()
            val aMgr = context.getSystemService("activity") as ActivityManager
            aMgr.killBackgroundProcesses(context.packageName)
        } catch (e: Exception) {
        } finally {
            if (!isBackground) {
                System.exit(0)
            }
        }
    }

    fun activityStackSize() = activityStack.size
}