package com.wushiyi.mvp.base

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle

/**
 * Created by zhangyuncai on 2019/6/17.
 * 这个类集成了一些常用的功能,起到示范性作用
 */
open abstract class SimpleMvpActivity<VIEWMODEL : SimpleViewModel> : SimpleAppCompatActivity() {
    lateinit var mViewModel: VIEWMODEL

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = ViewModelProviders.of(this).get(getViewModelClazz())
        mViewModel.loadMvpView = this
        super.onCreate(savedInstanceState)
    }

    abstract fun getViewModelClazz():Class<VIEWMODEL>

}