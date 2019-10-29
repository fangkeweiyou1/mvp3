package com.wushiyi.mvp.di

import android.app.Application
import android.content.Context
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by zhangyuncai on 2019/6/28.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: Application)

    fun retrofit(): Retrofit
    fun application(): Application
    fun context(): Context
    fun okhttpClient(): OkHttpClient
}