package com.gxy.utils.base.adaper

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/29
 * @Description:     多条目实现
 */
class MultiQuickAdapter<T : MultiItemEntity> : BaseMultiItemQuickAdapter<T, BaseViewHolder>() {

    override fun convert(holder: BaseViewHolder, item: T) {
        TODO("Not yet implemented")
    }

}