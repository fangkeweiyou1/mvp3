package com.wushiyi.mvp.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import java.lang.ref.WeakReference

/**
 * Created by zhangyuncai on 2019/6/24.
 * 这个类集成了一些常用的功能,起到示范性作用
 */
open abstract class SimpleFragment : Fragment(), View.OnClickListener, LoadMvpView {

    lateinit var mContext: Context
    lateinit var mActivity: Activity
    var isViewPrepared = false// 标识fragment视图已经初始化完毕
    var hasFetchData = false// 标识已经触发过懒加载数据
    val composite = CompositeDisposable()//销毁异步
    lateinit var rootView: View
    lateinit var activityWeakReference: WeakReference<Activity>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initEvent()
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun initEvent()
    abstract fun lazyFetchData()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isViewPrepared = true
        lazyFetchDataIfPrepared()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!composite.isDisposed) {
            composite.dispose()
            hasFetchData = false
            isViewPrepared = false
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared()
        }
    }

    private fun lazyFetchDataIfPrepared() {
        // 用户可见fragment && 没有加载过数据 && 视图已经准备完毕
        if (userVisibleHint && !hasFetchData && isViewPrepared) {
            hasFetchData = true
            lazyFetchData()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = context as Activity
        activityWeakReference = WeakReference(mActivity)
    }

    /**
     * 最终走到activity
     */
    override fun showLoadingBar(msg: String?) {
        if (mActivity != null && mActivity is LoadMvpView) {
            (mActivity as LoadMvpView).showLoadingBar(msg)
        }
    }
    /**
     * 最终走到activity
     */
    override fun dismissLoadingBar() {
        if (mActivity != null && mActivity is LoadMvpView) {
            (mActivity as LoadMvpView).dismissLoadingBar()
        }
    }
    /**
     * 最终走到activity
     */
    override fun showLoadingFailureError(throwable: Throwable?) {
        if (mActivity != null && mActivity is LoadMvpView) {
            (mActivity as LoadMvpView).showLoadingFailureError(throwable)
        }
    }

    override fun onClick(v: View) {
    }
}