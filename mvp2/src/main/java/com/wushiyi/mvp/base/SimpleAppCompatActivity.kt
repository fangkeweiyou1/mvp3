package com.wushiyi.mvp.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by zhangyuncai on 2019/6/10.
 */
open abstract class SimpleAppCompatActivity : AppCompatActivity(), LoadMvpView, View.OnClickListener {
    lateinit var mActivity: AppCompatActivity
    val composite = CompositeDisposable()//销毁异步

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        setContentView(getLayoutId())
        supportActionBar?.hide()
        findId()
        initView()
        initEvent()
        initData()
    }

    /**
     * 获取布局
     */
    abstract fun getLayoutId(): Int

    /**
     * 如果是java类继承,使用这个找控件ID
     */
    open fun findId() {}

    /**
     * 初始化界面信息设置等
     */
    abstract fun initView()

    /**
     * 归纳所有事件
     */
    abstract fun initEvent()

    /**
     * 归纳所有请求接口
     */
    abstract fun initData()

    /**
     * 设置全透明状态栏
     */
    protected fun setStatusTrans() {
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    /**
     * 界面销毁
     */
    override fun onDestroy() {
        if (!composite.isDisposed) {
            composite.dispose()
        }
        super.onDestroy()
    }

    /**
     * 加载进度条
     */
    override fun showLoadingBar(msg: String?) {
    }

    /**
     * 销毁进度条
     */
    override fun dismissLoadingBar() {
    }

    /**
     * 加载接口异常
     */
    override fun showLoadingFailureError(throwable: Throwable?) {
        throwable?.printStackTrace()
    }

    /**
     * 结束当前页面
     */
    open fun backClick(view: View?) {
        finish()
    }

    /**
     * 点击事件回调
     */
    override fun onClick(v: View) {

    }


}