package com.wushiyi.mvp

import android.app.Application
import com.wushiyi.mvp.di.AppModule
import com.wushiyi.mvp.di.DaggerAppComponent
import com.wushiyi.util.LogUtil
import com.wushiyi.util.UtilInit
import timber.log.Timber

/**
 * Created by zhangyuncai on 2019/6/29.
 */
object MvpInit {
    //APP域名根地址(可以作参考)
    var baseUrl = ""
    var isDebug=true

    fun init(application: Application) {
        MvpInit.init(application,true)
    }

    fun init(application: Application, isDebug: Boolean) {
        sApplication = application
        sContext = application.applicationContext
        MvpInit.isDebug=isDebug
        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        } else {
        }
        UtilInit.initUtil(application)
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(application, baseUrl))
                .build()
        LogUtil.debug=isDebug
    }

    /*

    //集成MVP框架,建议自己再按照以下类名及代码重写一遍


    private class BaseViewModel : SimpleViewModel() {

    }

    private abstract class BaseSimpleFragment : SimpleFragment() {

    }

    private abstract class BaseAppCompatActivity : SimpleAppCompatActivity() {

        /**
         * 这里可以写国际化相关代码
         * exp: super.attachBaseContext(LocalManageUtil.attachBaseContext(newBase))
         */
        override fun attachBaseContext(newBase: Context?) {
            super.attachBaseContext(newBase)
        }

    }

    private abstract class BaseMvpActivity<VIEWMODEL : SimpleViewModel> : BaseAppCompatActivity() {
        lateinit var mViewModel: VIEWMODEL

        init {

        }

        override fun onCreate(savedInstanceState: Bundle?) {
            mViewModel = ViewModelProviders.of(this).get(getViewModelClazz())
            mViewModel.loadMvpView = this
            super.onCreate(savedInstanceState)
        }

        abstract fun getViewModelClazz(): Class<VIEWMODEL>
    }

    private abstract class BaseMvpFragment<VIEWMODEL : BaseViewModel> : SimpleMvpFragment<VIEWMODEL>() {

    }

     */


    /*

    相关提示
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'
apply plugin: 'kotlin-kapt' // 使用 kapt 注解处理工具

minSdkVersion 21

 compileOptions {
        encoding 'UTF-8'
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }



 api 'com.github.fangkeweiyou1:libutils:1.0.2'

    //生命周期组件
    api "android.arch.lifecycle:extensions:$versions.lifecycle_version"
    kapt "android.arch.lifecycle:compiler:$versions.lifecycle_version"
    // Dagger
    api "com.google.dagger:dagger:$versions.daggerVersion"
    kapt "com.google.dagger:dagger-compiler:$versions.daggerVersion"    // kapt 代替 annotationProcessor
    // Logging
    api "com.jakewharton.timber:timber:$versions.timberVersion"
    // RetroFit
    api "com.squareup.retrofit2:retrofit:$versions.retrofitVersion"
    api "com.squareup.retrofit2:converter-gson:$versions.retrofitVersion"
    api "com.squareup.okhttp3:logging-interceptor:$versions.loggingInterceptorVersion"

    // Rx
    api "io.reactivex.rxjava2:rxandroid:$versions.rxAndroidVersion"
    // Already has RxJava, but still better to explicitly get latest version.
    api "io.reactivex.rxjava2:rxjava:$versions.rxJavaVersion"
    api "com.squareup.retrofit2:adapter-rxjava2:$versions.retrofitVersion"
    //gldie
    implementation 'com.github.bumptech.glide:glide:4.9.0'




     */
}