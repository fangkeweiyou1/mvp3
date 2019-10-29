package com.wushiyi.mvp

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.wushiyi.mvp.di.AppComponent
import timber.log.Timber

/**
 * Created by zhangyuncai on 2019/6/29.
 */
lateinit var sApplication: Application
lateinit var sContext: Context
lateinit var appComponent: AppComponent

/**
 * 打印日志,方便敲代码
 */
fun dddBug(content: Any) {
    Timber.d("----------->>>>>>>>-----------$content")
}

fun Fragment.myStartActivity(clazz: Class<*>) {
    val intent = Intent(context, clazz)
    startActivity(intent)
}

fun AppCompatActivity.myStartActivity(clazz: Class<*>) {
    val intent = Intent(this, clazz)
    startActivity(intent)
}

/**
 * 新建fragment对象
 */
fun <T : Fragment> sNewStanceFragment(context: Context, clazz: Class<T>, bundle: Bundle?): T {
    return Fragment.instantiate(context, clazz.getName(), bundle) as T
}

/**
 * 新建fragment对象
 */
fun <T : Fragment> sNewStanceFragment(context: Context, clazz: Class<T>): T {
    return Fragment.instantiate(context, clazz.getName()) as T
}