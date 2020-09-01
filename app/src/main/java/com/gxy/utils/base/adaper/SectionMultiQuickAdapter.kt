package com.gxy.utils.base.adaper

import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.entity.SectionEntity
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/29
 * @Description:     分组多条目布局
 */
class SectionMultiQuickAdapter<T : SectionEntity>(val sectionHeadResId: Int, val layoutId: Int) :
    BaseSectionQuickAdapter<T, BaseViewHolder>(sectionHeadResId, layoutId),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: T) {
        TODO("Not yet implemented")
    }

    override fun convertHeader(helper: BaseViewHolder, item: T) {
        TODO("Not yet implemented")
    }

}