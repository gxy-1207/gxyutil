package com.gxy.utils.base.adaper

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/29
 * @Description:    单个条目
 */
open class BaseAdape<T>(val layoutId: Int) : BaseQuickAdapter<T, BaseViewHolder>(layoutId),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: T) {

    }
}