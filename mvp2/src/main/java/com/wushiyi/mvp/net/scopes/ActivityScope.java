package com.wushiyi.mvp.net.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by zhangyuncai on 2018/9/11.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {

}

