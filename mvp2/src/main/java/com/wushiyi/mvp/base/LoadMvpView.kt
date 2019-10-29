package com.wushiyi.mvp.base

import android.support.annotation.UiThread

/**
 * Created by zhangyuncai on 2019/6/11.
 */
open interface LoadMvpView  {
    /**
     * 加载进度条
     */
    @UiThread
    fun showLoadingBar(msg: String?)

    /**
     * 销毁进度条
     */
    @UiThread
    fun dismissLoadingBar()

    /**
     * 加载接口异常
     */
    @UiThread
    fun showLoadingFailureError(throwable: Throwable?)
}