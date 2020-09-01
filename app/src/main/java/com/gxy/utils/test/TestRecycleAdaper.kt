package com.gxy.utils.test

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gxy.utils.R
import com.gxy.utils.base.adaper.BaseAdape

class TestRecycleAdaper : BaseAdape<TestRecycleData>(R.layout.tahuida_recycle_item) {

    override fun convert(holder: BaseViewHolder, item: TestRecycleData) {
        super.convert(holder, item)
        holder.setText(R.id.tv_huida_name, item?.uname)
        holder.setText(R.id.tv_huida_time, item?.date)
        holder.setText(R.id.tv_content, item?.content)
        holder.setText(R.id.tv_leixing, item?.cid)
        holder.setText(R.id.tv_num, item?.sum)
    }
}