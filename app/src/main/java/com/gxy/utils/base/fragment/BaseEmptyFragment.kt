package com.gxy.utils.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.core.ui.eventbus.EventBusHandler
import com.core.ui.eventbus.EventBusManager
import com.core.ui.eventbus.EventBusMessage
import com.ethanhua.skeleton.SkeletonScreen
import com.gxy.utils.R
import com.gxy.utils.base.annotation.InjectLayout
import com.gxy.utils.base.annotation.InjectPresenter
import com.gxy.utils.base.presenter.BasePresenter
import com.gxy.utils.base.view.BaseView
import com.gxy.utils.callbacks.IUICallback
import com.gxy.utils.utils.LoggerUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.android.synthetic.main.fragment_base_empty.*
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.EasyPermissions

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/28
 * @Description:     baseFragment
 */
open class BaseEmptyFragment : Fragment(), IUICallback, BaseView,
    EventBusHandler, EasyPermissions.PermissionCallbacks {
    protected lateinit var mContext: Context
    protected lateinit var mInflater: LayoutInflater

    protected var registerEventBus = false

    private val listPresenters = mutableListOf<BasePresenter<BaseView>>()


    protected var layoutView: View? = null
    protected var loadingView: View? = null
    protected var emptyView: View? = null
    protected var errorView: View? = null

    protected var layoutId: Int = -1
    protected var loadingId: Int = -1
    protected var emptyId: Int = -1
    protected var errorId: Int = -1

    protected var showSkeleton: SkeletonScreen? = null
    private var waitDialog: BasePopupView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = activity!!
        mInflater = LayoutInflater.from(mContext)
        onInitData(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base_empty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fl_base_content.removeAllViews()
        //实例化成员变量中的p
        instancePresenters()
        //获取类注解上的布局id
        val injectLayout = javaClass.getAnnotation(InjectLayout::class.java)
        if (null == injectLayout) {
            LoggerUtil.Log("没有设置布局显示")
        } else {
            layoutId = injectLayout.layoutId
            loadingId = injectLayout.lodingLayoutId
            emptyId = injectLayout.emptyLayoutId
            errorId = injectLayout.errorLayoutId
        }
        //实例化不同状态的view
        if (layoutId != -1) {
            layoutView = inflateView(layoutId)
        }
        if (loadingId != -1) {
            loadingView = inflateView(loadingId)
        }
        if (emptyId != -1) {
            emptyView = inflateView(emptyId)
        }
        if (errorId != -1) {
            errorView = inflateView(emptyId)
        }
        if (null != layoutView) {
            fl_base_content.addView(layoutView)
        }
        if (null != loadingView) {
            fl_base_content.addView(loadingView)
        }
        if (null != emptyView) {
            fl_base_content.addView(emptyView)
            emptyView?.setOnClickListener(View.OnClickListener {
                showLoding()
                onRetryLoading()
            })
        }
        if (null != errorView) {
            fl_base_content.addView(errorView)
            errorView?.setOnClickListener(View.OnClickListener {
                showLoding()
                onRetryLoading()
            })
        }
        showLoding()
        onSetViewListener()
        onSetViewData()
    }

    //实例化成员变量中的p
    private fun instancePresenters() {
        try {
            val fields = javaClass.declaredFields
            for (item in fields) {
                //获取到成员变量的注解
                item.getAnnotation(InjectPresenter::class.java) ?: continue
                item.isAccessible = true//开启暴力访问
                val type = item.type as Class<BasePresenter<BaseView>>//获取p层的字节码对象
                val presenterInstance = type.newInstance()//通过字节码对象实例化p层的对象
                //设置成员变量的值为presenterInstance
                item.set(this, presenterInstance)
                //绑定p层的view和生命周期的owner
                presenterInstance.attachView(this)
                presenterInstance.bindLifecycleOwner(this)
                listPresenters.add(presenterInstance)
            }
        } catch (e: Exception) {
            LoggerUtil.Log(e)
        }
    }
    //===Desc:================================================================================

    protected fun inflateView(layoutId: Int): View? =
        mInflater.inflate(layoutId, null, false)

    protected fun getContentView(): FrameLayout =
        fl_base_content

    override fun onInitData(savedInstanceState: Bundle?) {

    }

    override fun onSetViewListener() {

    }

    override fun onSetViewData() {

    }

    override fun onRetryLoading() {

    }

    override fun getBindContext(): Context = mContext


    override fun showLoding() {
        loadingView?.visibility = View.VISIBLE
        layoutView?.visibility = View.GONE
        emptyView?.visibility = View.GONE
        errorView?.visibility = View.GONE
    }

    override fun hideLoding() {
        loadingView?.visibility = View.GONE
    }

    override fun showSuccess() {
        loadingView?.visibility = View.GONE
        layoutView?.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        errorView?.visibility = View.GONE
    }

    override fun hideSuccess() {
        layoutView?.visibility = View.GONE
    }

    override fun showEmpty() {
        loadingView?.visibility = View.GONE
        layoutView?.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    override fun hideEmpty() {
        emptyView?.visibility = View.GONE
    }

    override fun showError() {
        loadingView?.visibility = View.GONE
        layoutView?.visibility = View.GONE
        emptyView?.visibility = View.GONE
        errorView?.visibility = View.VISIBLE
    }

    override fun hideError() {
        errorView?.visibility = View.GONE
    }

    override fun showWait(cancelOutSite: Boolean) {
        if (null == waitDialog) {
            waitDialog = XPopup.Builder(mContext)
                .dismissOnBackPressed(cancelOutSite)
                .dismissOnTouchOutside(cancelOutSite)
                .asLoading()
                .show();
        }
    }

    override fun hideWait(runnable: Runnable?) {
        if (waitDialog?.isShow == true) {
            if (null != runnable) {
                waitDialog?.dismissWith(runnable)
            } else {
                waitDialog?.dismiss()
            }
            waitDialog = null
        }
    }

    override fun showToast(text: String, isLong: Boolean) {
        val toast =
            Toast.makeText(mContext, null, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
        toast.setText(text)
        toast.show()
    }

    override fun showToast(textId: Int, isLong: Boolean) {
        showToast(getString(textId), isLong)
    }

    override fun runMainThread(runnable: Runnable) {
        activity?.runOnUiThread(runnable)
    }

    //===Desc:EventBus相关================================================================================
    fun obtain(what: Int): EventBusMessage {
        val msg = EventBusMessage()
        msg.what = what
        return msg
    }

    fun postSticky(msg: EventBusMessage) {
        EventBus.getDefault().postSticky(msg)
    }

    fun post(msg: EventBusMessage) {
        EventBus.getDefault().post(msg)
    }

    //===Desc:权限相关================================================================================
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //权限通过
        onPermissionsPass(requestCode, perms)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //权限拒绝
        //权限是否永久拒绝
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            onPermissionsPermanentlyFail(requestCode, perms)
        } else {
            onPermissionsFail(requestCode, perms)
        }
    }

    /**子类复写,自己处理权限不通过*/
    protected open fun onPermissionsFail(requestCode: Int, perms: MutableList<String>) {

    }

    /**子类复写,自己处理权限永久不通过*/
    protected open fun onPermissionsPermanentlyFail(requestCode: Int, perms: MutableList<String>) {

    }

    /**子类复写,自己处理权限通过*/
    protected open fun onPermissionsPass(requestCode: Int, perms: MutableList<String>) {

    }

    /**判断是否拥有权限*/
    protected fun hasPermissions(vararg perms: String): Boolean {
        return EasyPermissions.hasPermissions(mContext, *perms)
    }

    /**请求权限
     *
     * @param requestCode 请求码
     * @param rationale 请求权限说明
     * @param perms 需要使用的权限
     */
    protected fun requestPermissions(requestCode: Int, rationale: String, vararg perms: String) {
        EasyPermissions.requestPermissions(this, rationale, requestCode, *perms)
    }

    override fun onDestroy() {
        EventBusManager.unRegister(this)

        //解绑MVP
        listPresenters.map {
            it.detachView()
        }
        listPresenters.clear()
        super.onDestroy()
    }
}