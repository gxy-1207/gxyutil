package com.gxy.utils.base.presenter

import com.gxy.utils.base.view.BaseRefreshView

open class BaseRefreshPresenter<V : BaseRefreshView<*>> : BasePresenter<V>() {
    fun refresh() {
        getView().onRefresh()
    }

    fun loadMore() {
        getView().onLoadMore()
    }

    // ===Desc:=================================================================
    /***/
    protected fun resetPageStatus(isLoadMor: Boolean) {
        if (!isLoadMor) {
//            getView().setRecyclerViewEmptyView(R.layout.layout_empty)
        } else {
            getView().resetCurrentPage(isLoadMor, 1)
            getView().loadMoreEnd(false)
        }
    }

    /**停止刷新或者加載更多*/
    protected fun stopRefreshOrLoadMore(isLoadMor: Boolean, hasMore: Boolean) {
        if (isLoadMor) {
            if (hasMore) {
                getView().loadMoreComplete()
            } else {
                getView().loadMoreEnd()
            }
        } else {
            getView().stopRefresh()
        }
    }

}