package com.wushiyi.mvp.base

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by zhangyuncai on 2019/6/11.
 */
open class NetTransformer<T> constructor(netParams: NetParams, val loadMvpView: LoadMvpView?) : ObservableTransformer<T, T> {

    var isShowLoading = true//是否显示菊花窗口,默认显示
    var isDissLoading = true//是否关闭菊花窗口,默认显示
    var isToastError = true//当state为不成功时,是否需要弹窗提示,默认弹
    var loadingMsg = ""//菊花中的加载内容

    init {
        netParams?.let {
            this.isShowLoading = it.isShowLoading
            this.isDissLoading = it.isDissLoading
            this.isToastError = it.isToastError
            this.loadingMsg = it.loadingMsg
        }
    }


    override fun apply(p0: Observable<T>): ObservableSource<T> {
        return p0.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    if (isShowLoading) {
                        loadMvpView?.showLoadingBar(loadingMsg)
                    }
                }
                .doFinally {
                    if (isDissLoading) {
                        loadMvpView?.dismissLoadingBar()
                    }
                }
    }
}