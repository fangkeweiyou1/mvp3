package com.wushiyi.jitmvp

import android.app.Application
import com.wushiyi.mvp.MvpInit

/**
 * Created by zhangyuncai on 2019/6/29.
 */
class JitApplication : Application() {

    companion object{
        val ANDROID="ANDROID"
    }
    override fun onCreate() {
        super.onCreate()
        MvpInit.baseUrl="https://www.baidu.com"
        MvpInit.init(this)
    }
}