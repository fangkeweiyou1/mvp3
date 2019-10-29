package com.wushiyi.mvp.base

/**
 * Created by zhangyuncai on 2019/10/25.
 */
/*
    var isShowLoading = true//是否显示菊花窗口,默认显示
    var isDissmissLoading = true//是否关闭菊花窗口,默认显示
    var isToastError = true//当state为不成功时,是否需要弹窗提示,默认弹
    var loadingMsg = ""//菊花中的加载内容
 */
class NetParams constructor(var isShowLoading: Boolean = true,
                            var isDissLoading: Boolean = true,
                            var isToastError: Boolean = true,
                            var loadingMsg: String = "") {
    /**
     *  二参构造和所有参数构造一样的,一般不使用
     */
    constructor(isShowLoading: Boolean?, isDissLoading: Boolean?) : this(isShowLoading
            ?: true, isDissLoading ?: true, true, "")

    /**
     * 一参构造
     * @param isShowLoading 是否 显示/关闭 加载框
     */
    constructor(isShowLoading: Boolean?) : this(isShowLoading
            ?: true, isShowLoading
            ?: true,
            true, "")

    /**
     * 无参构造和所有参数构造一样的,一般不使用
     */
    constructor() : this(true, true, true, "")
}
