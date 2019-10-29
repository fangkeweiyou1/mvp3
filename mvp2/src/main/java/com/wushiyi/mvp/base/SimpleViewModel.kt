package com.wushiyi.mvp.base

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.wushiyi.mvp.sContext
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer

/**
 * Created by zhangyuncai on 2019/6/17.
 * 这个类集成了一些常用的功能,起到示范性作用
 */
open class SimpleViewModel : ViewModel(), LoadMvpView {
    val mContext: Context= sContext
    override fun showLoadingBar(msg: String?) {
        loadMvpView.showLoadingBar(msg)
    }

    override fun dismissLoadingBar() {
        loadMvpView.dismissLoadingBar()
    }

    override fun showLoadingFailureError(throwable: Throwable?) {
        loadMvpView.showLoadingFailureError(throwable)
    }

    lateinit var loadMvpView: LoadMvpView
    val composite = CompositeDisposable()

    open fun newThrowableConsumer(throwableConsumer: Consumer<Throwable>?): Consumer<Throwable> {
        val value = object : Consumer<Throwable> {
            override fun accept(p0: Throwable) {
                if (throwableConsumer != null) {
                    throwableConsumer.accept(p0)
                }
                loadMvpView.showLoadingFailureError(p0)
            }

        }
        return value
    }


    /**
     * @param observable 接口请求
     * @param resultConsumer 结果返回
     */
    open fun <B> baseSubscribe(observable: Observable<B>, resultConsumer: Consumer<B>) {
        baseSubscribe(observable, null, resultConsumer, null)
    }

    /**
     * @param observable 接口请求
     * @param resultConsumer 结果返回
     * @param netParams 加载框参数
     */
    open fun <B> baseSubscribe(observable: Observable<B>, netParams: NetParams?, resultConsumer: Consumer<B>) {
        baseSubscribe(observable, netParams, resultConsumer, null)
    }

    /**
     * @param observable 接口请求
     * @param resultConsumer 结果返回
     * @param netParams 加载框参数
     * @param throwableConsumer 错误返回
     */
    open fun <B> baseSubscribe(observable: Observable<B>, netParams: NetParams?, resultConsumer: Consumer<B>, throwableConsumer: Consumer<Throwable>?) {
        var transformer = NetTransformer<B>(netParams ?: NetParams(), loadMvpView)
        val subscribe = observable.compose(transformer)
                .subscribe(resultConsumer, newThrowableConsumer(throwableConsumer))
        composite.add(subscribe)
    }

    override fun onCleared() {
        if (!composite.isDisposed) {
            composite.dispose()
        }
        super.onCleared()
    }

}