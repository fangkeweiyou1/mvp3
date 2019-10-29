package com.wushiyi.mvp.base

import android.arch.lifecycle.ViewModelProviders
import android.content.Context

/**
 * Created by zhangyuncai on 2019/6/17.
 * 这个类集成了一些常用的功能,起到示范性作用
 * 依赖此框架的可以使用此类也可以复制代码过去
 */
open abstract class SimpleMvpFragment<VIEWMODEL : SimpleViewModel> : SimpleFragment() {
    lateinit var mViewModel: VIEWMODEL

    override fun onAttach(context: Context) {
        mViewModel = ViewModelProviders.of(this).get(getViewModelClazz())
        mViewModel.loadMvpView = this@SimpleMvpFragment
        super.onAttach(context)
    }
    abstract fun getViewModelClazz():Class<VIEWMODEL>
}