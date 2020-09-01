package com.gxy.utils.base.view

import android.view.View
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/29
 * @Description:     用于列表的view
 */
interface BaseRefreshView<T> : BaseView {
    fun getAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    fun onRefresh()

    fun onLoadMore()
    fun stopRefresh()

    fun loadMoreComplete() {
        getAdapter().loadMoreModule.loadMoreComplete()
    }

    fun loadMoreEnd(gone: Boolean = false) {
        getAdapter().loadMoreModule.loadMoreEnd(gone)
    }

    fun loadMoreFail() {
        getAdapter().loadMoreModule.loadMoreFail()
    }

    //===Desc:================================================================================

    fun setNewData(list: List<T>) {
        val data = mutableListOf<T>()
        data.addAll(list)
        getAdapter().setNewInstance(data)
    }

    fun addData(data: T, position: Int = -1) {
        if (position < 0) {
            getAdapter().addData(data)
        } else {
            getAdapter().addData(position, data)
        }
    }

    fun addData(list: List<T>, position: Int = -1) {
        if (position < 0) {
            getAdapter().addData(list)
        } else {
            getAdapter().addData(position, list)
        }
    }

    fun remove(data: T) {
        getAdapter().remove(data)

    }

    fun removeAt(position: Int) {
        getAdapter().removeAt(position)
    }

    fun getData(): List<T> = getAdapter().data

    fun indexOf(data: T): Int = getData().indexOf(data)

    //===Desc:================================================================================

    fun setRecyclerViewView(@LayoutRes layoutId: Int) {
        getAdapter().setEmptyView(layoutId)
    }

    fun setRecyclerViewEmptyView(emptyView: View?) {
        if (null != emptyView) {
            getAdapter().setNewInstance(mutableListOf())
            getAdapter().setEmptyView(emptyView)
        }
    }

    fun setRecyclerViewEmptyView(@LayoutRes id: Int)

    fun addHeaderView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL) {
        getAdapter().addHeaderView(view, index, orientation)
    }

    fun addFooterView(view: View, index: Int = -1, orientation: Int = LinearLayout.VERTICAL) {
        getAdapter().addFooterView(view, index, orientation)
    }

    fun removeHeader(header: View) {
        getAdapter().removeHeaderView(header)
    }

    fun removeAllHeader() {
        getAdapter().removeAllHeaderView()
    }

    fun removeFooter(footer: View) {
        getAdapter().removeFooterView(footer)
    }

    fun removeAllFooter() {
        getAdapter().removeAllFooterView()
    }

    //===Desc:================================================================================

    fun enableRefresh(enable: Boolean)

    fun enableLoadMore(enable: Boolean)

    fun getPage(): Int

    fun setPage(currentPage: Int)

    fun resetCurrentPage(isLoadMore: Boolean, defaultPage: Int)

}