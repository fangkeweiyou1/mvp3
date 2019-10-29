package com.wushiyi.mvp.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

/**
 * Created by zhangyuncai on 2019/10/28.
 * 这个类集成了一些常用的功能,起到示范性作用
 */
public abstract class SimpleMvvmActivity<VIEWMODEL extends SimpleViewModel, VDB extends ViewDataBinding> extends SimpleMvpActivity<VIEWMODEL> {
    protected VDB binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化databinding
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        //databinding添加生命周期
        binding.setLifecycleOwner(this);
    }
}
