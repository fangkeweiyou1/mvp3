package com.wushiyi.mvp.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by zhangyuncai on 2019/6/17.
 * 通用型fragmentadapter
 */
open class BaseFragmentPagerAdapter constructor(fm: FragmentManager, var mFragmentList: MutableList<Fragment>) : FragmentPagerAdapter(fm) {
    var titles: MutableList<String>? = mutableListOf<String>()

    /**
     * @param mFragmentList fragment集合
     * @param titles tab标题集合
     */
    constructor(fm: FragmentManager, mFragmentList: MutableList<Fragment>, titles: MutableList<String>?) : this(fm, mFragmentList) {
        this.titles = titles
    }

    override fun getItem(p0: Int): Fragment {
        return mFragmentList[p0]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (titles != null && titles!!.size > position) {
            return titles!![position]
        }
        return null
    }
}