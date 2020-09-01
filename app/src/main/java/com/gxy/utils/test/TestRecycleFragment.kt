package com.gxy.utils.test

import android.os.Bundle
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gxy.utils.R
import com.gxy.utils.base.annotation.InjectLayout
import com.gxy.utils.base.annotation.InjectPresenter
import com.gxy.utils.base.fragment.BaseRecycleViewFragment

@InjectLayout(lodingLayoutId = R.layout.tahuida_recycle_loding_item)
class TestRecycleFragment : BaseRecycleViewFragment<TestRecycleData>() {
    @InjectPresenter
    private lateinit var testRecyclePresenter: TestRecyclePresenter

    companion object {
        fun newInstance(): TestRecycleFragment {
            val args = Bundle()
            val fragment = TestRecycleFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateAdapter(): BaseQuickAdapter<TestRecycleData, BaseViewHolder> {
        return TestRecycleAdaper()
    }

    override fun onRefreshOrLoadMore(isLoadMore: Boolean) {
        super.onRefreshOrLoadMore(isLoadMore)
        currentPage = 1
        testRecyclePresenter.getTestRecycle(4351.toString(), currentPage)
    }
}