package com.gxy.utils.base.annotation

import java.lang.annotation.Inherited

@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class InjectLayout(
    /**
     *显示布局id
     * */
    val layoutId: Int = -1,
    /**
     * 需要显示的Toolbar的布局id
     */
    val toolbarLayoutId: Int = -1,
    /**
     *加载中的布局
     * */
    val lodingLayoutId: Int = -1,
    /**
     *加载数据为空的布局
     * */
    val emptyLayoutId: Int = -1,
    /**
     *加载失败的布局
     * */
    val errorLayoutId: Int = -1
)