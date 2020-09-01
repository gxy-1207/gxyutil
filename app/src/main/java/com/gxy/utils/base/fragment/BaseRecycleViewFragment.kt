package com.gxy.utils.base.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ethanhua.skeleton.Skeleton
import com.github.lee.core.utils.generateBeautifulColor
import com.gxy.utils.R
import com.gxy.utils.base.view.BaseRefreshView
import kotlinx.android.synthetic.main.fragment_base_refresh.*

abstract class BaseRecycleViewFragment<T> : BaseEmptyFragment(), BaseRefreshView<T> {
    private lateinit var mAdapter: BaseQuickAdapter<T, BaseViewHolder>
    protected var autoRefresh: Boolean = true
    protected var currentPage = 0
    //===Desc:================================================================================

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        mAdapter = onCreateAdapter()
    }

    override fun onSetViewListener() {
        super.onSetViewListener()
        getAdapter().loadMoreModule.setOnLoadMoreListener {
            loadMore()
        }
    }

    override fun onSetViewData() {
        super.onSetViewData()
        layoutView = inflateView(R.layout.fragment_base_refresh)
        getContentView().addView(layoutView)

        //需要执行完父类
        srl_refresh_refresh.setOnRefreshListener {
            refresh()
        }

        //设置LayoutManager
        val lm = onCreateLayoutManager()
        if (null == lm) {
            rv_refresh_content.layoutManager = LinearLayoutManager(mContext)
        } else {
            rv_refresh_content.layoutManager = lm
        }
        getAdapter().loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        //设置Adapter
        rv_refresh_content.adapter = getAdapter()

        hideTopOverlap()
        hideBottomOverlap()

        //设置刷新颜色
        srl_refresh_refresh.setColorSchemeColors(
            generateBeautifulColor(),
            generateBeautifulColor(),
            generateBeautifulColor(),
            generateBeautifulColor()
        )
        //判断是否需要进如界面自动刷新
        super.showSuccess()
        if (autoRefresh) {
            showLoding()
            refresh()
        }
    }
//===Desc:================================================================================

    protected abstract fun onCreateAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    protected open fun onCreateLayoutManager(): RecyclerView.LayoutManager? = null


    /**
     * 添加顶部悬浮容器
     */
    protected open fun addTopSuspensionView(view: View?, param: FrameLayout.LayoutParams? = null) {
        fl_refresh_top.removeAllViews()
        if (null == param) {
            fl_refresh_top.addView(view)
        } else {
            fl_refresh_top.addView(view, param)
        }
        fl_refresh_top.visibility = View.VISIBLE
    }

    /**
     *添加底部悬浮容器
     * */
    protected open fun addBottomSuspensionView(
        view: View?,
        param: FrameLayout.LayoutParams? = null
    ) {
        fl_refresh_bottom.removeAllViews()
        if (null == param) {
            fl_refresh_bottom.addView(view)
        } else {
            fl_refresh_bottom.addView(view, param)
        }
        fl_refresh_bottom.visibility = View.VISIBLE
    }

    /**
     * 隐藏底部容器
     * */
    private fun hideBottomOverlap() {
        fl_refresh_bottom.visibility = View.VISIBLE
    }

    /**
     * 隐藏顶部容器
     * */
    private fun hideTopOverlap() {
        fl_refresh_top.visibility = View.GONE
    }
    //===Desc:================================================================================

    private fun refresh() {
        onRefresh()
    }

    private fun loadMore() {
        onLoadMore()
    }

    override fun showLoding() {
        super.showSuccess()
        if (loadingId != -1 && null != rv_refresh_content) {
            showSkeleton = Skeleton.bind(rv_refresh_content)
                .adapter(getAdapter())
                .shimmer(true)
                .colorInt(Color.parseColor("#FAFAFA"))
                .angle(30)
                .duration(1000)
                .load(loadingId)
                .show()
        }
    }

    override fun hideLoding() {
        super.hideLoding()
        showSkeleton?.hide()
        showSkeleton = null
    }

    override fun showSuccess() {
        super.showSuccess()
        showSkeleton?.hide()
        showSkeleton = null
    }

    // ===Desc:=================================================================
    override fun getAdapter(): BaseQuickAdapter<T, BaseViewHolder> = mAdapter

    override fun onRefresh() {
        srl_refresh_refresh.isRefreshing = true
        onRefreshOrLoadMore(false)
    }

    override fun onLoadMore() {
        onRefreshOrLoadMore(true)
    }

    override fun stopRefresh() {
        srl_refresh_refresh?.isRefreshing = false
    }

    override fun setRecyclerViewEmptyView(id: Int) {
        setRecyclerViewEmptyView(inflateView(id))
    }

    override fun enableRefresh(enable: Boolean) {
        srl_refresh_refresh.isEnabled = enable
    }

    override fun enableLoadMore(enable: Boolean) {
        mAdapter.loadMoreModule.isEnableLoadMore = enable
    }

    override fun getPage(): Int = currentPage

    override fun setPage(currentPage: Int) {
        this.currentPage = currentPage
    }

    override fun resetCurrentPage(isLoadMore: Boolean, defaultPage: Int) {
        if (isLoadMore) {
            currentPage--
            if (currentPage < defaultPage) {
                currentPage = defaultPage
            }
        } else {
            currentPage = defaultPage
        }
    }
    //===Desc:================================================================================

    protected fun addItemDecoration(vararg decoration: RecyclerView.ItemDecoration) {
        for (item in decoration) {
            rv_refresh_content.addItemDecoration(item)
        }
    }

    protected fun getRecyclerView(): RecyclerView =
        rv_refresh_content

    protected open fun onRefreshOrLoadMore(isLoadMore: Boolean = true) {

    }
}