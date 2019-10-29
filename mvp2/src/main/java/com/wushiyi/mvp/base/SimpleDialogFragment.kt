package com.wushiyi.mvp.base

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.FloatRange
import android.support.annotation.LayoutRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.wushiyi.util.DisplayUtil

/**
 * Created by zhangyuncai on 2019/7/3.
 */
open abstract class SimpleDialogFragment : DialogFragment(), LoadMvpView, View.OnClickListener {
    lateinit var mActivity: Activity
    lateinit var mContext: Context
    lateinit var rootView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = context as Activity
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        // 设置宽度为屏宽, 靠近屏幕底部。
        val window = getDialog().window ?: return
        //设置窗体背景色透明
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //设置宽高
        val layoutParams = window.attributes
        layoutParams.width = getWidthStyle()
        layoutParams.height = getHeightStyle()
        //透明度
        //        layoutParams.dimAmount = getDimAmount();
        //位置
        layoutParams.gravity = getGravity()
        window.attributes = layoutParams
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //去除Dialog默认头部
        val dialog = dialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(isCancelableOutside())
        if (dialog.window != null && getDialogAnimationRes() > 0) {
            dialog.window!!.setWindowAnimations(getDialogAnimationRes())
        }
        initView()
        initEvent()
        initData()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    abstract fun initEvent()


    /**
     * 外部点击是否可以取消dialog
     */
    open fun isCancelableOutside(): Boolean {
        return true
    }

    //获取弹窗显示动画,子类实现
    open fun getDialogAnimationRes(): Int {
        return 0
    }

    //默认透明度为0.2
    open fun getDimAmount(): Float {
        return 0.2f
    }

    /**
     * 获取 dialog 显示在界面的位置样式
     *
     * @return 位置样式
     */
    open fun getGravity(): Int {
        return Gravity.BOTTOM
    }

//    /**
//     * 获取 dialog 显示在界面的动画样式
//     *
//     * @return 位置样式
//     */
//    protected fun getAnimations(): Int {
//        return R.style.DataSheetAnimation
//    }

    /**
     * 子类复写 去实现 dialog 宽度的选择
     *
     * @return dialog宽度
     */
    open fun getWidthStyle(): Int {
        return WindowManager.LayoutParams.MATCH_PARENT
    }

    /**
     * 子类复写 去实现 dialog 高度的选择
     *
     * @return dialog高度
     */
    open fun getHeightStyle(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    open fun getScreenHeight(): Int {
        val dialogHeight = getContextRect(activity!!)
        return if (dialogHeight == 0) ViewGroup.LayoutParams.MATCH_PARENT else dialogHeight
    }

    /**
     * 解决DialogFragment全屏时状态栏变黑
     * 获取内容区域
     * @param activity
     * @return
     */
    private fun getContextRect(activity: Activity): Int {
        //应用区域
        val outRect1 = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(outRect1)
        return outRect1.height()
    }

    //获取屏幕宽度的百分比
    open fun getScreenWidth(@FloatRange(from = 0.1, to = 1.0) percent: Float): Float {
        return DisplayUtil.getScreenWidthAndHight()[0] * percent
    }

    override fun show(manager: FragmentManager, tag: String) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit()
            super.show(manager, tag)
        } catch (e: Exception) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace()
        }

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