package com.wushiyi.mvp.net.converter;

/**
 * 监听数据回调类
 * @param <T>
 */
public interface IResultListener<T> {
    void result(T t);
}
