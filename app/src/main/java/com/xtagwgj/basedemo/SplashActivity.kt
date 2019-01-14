package com.xtagwgj.basedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 启动页面
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        MainActivity.doAction(this)
    }
}
