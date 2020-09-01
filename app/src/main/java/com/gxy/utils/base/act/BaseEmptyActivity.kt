package com.gxy.utils.base.act

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.core.ui.eventbus.EventBusHandler
import com.core.ui.eventbus.EventBusManager
import com.core.ui.eventbus.EventBusMessage
import com.ethanhua.skeleton.Skeleton
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
import kotlinx.android.synthetic.main.activity_linear_toolbar.*
import org.greenrobot.eventbus.EventBus
import pub.devrel.easypermissions.EasyPermissions
import qiu.niorgai.StatusBarCompat

/**
 * @Author:         gxy
 * @CreateDate:     2020/8/28
 * @Description:     baseActivity
 */
open class BaseEmptyActivity : AppCompatActivity(), IUICallback, BaseView,
    EventBusHandler, EasyPermissions.PermissionCallbacks {
    protected lateinit var mContext: Context
    protected lateinit var mInflater: LayoutInflater
    protected var registerEventBus = false

    private val presenters = mutableListOf<BasePresenter<*>>()

    protected var showSkeleton: SkeletonScreen? = null
    private var waitDialog: BasePopupView? = null

    /** Toolbar是悬浮还是线性  */
    protected var toolbarOverlap = false

    /** 显示在界面的Toolbar,为null不显示  */
    protected var mToolbar: Toolbar? = null


    protected var layoutView: View? = null
    protected var loadingView: View? = null
    protected var emptyView: View? = null
    protected var errorView: View? = null

    protected var layoutId: Int = -1
    protected var loadingId: Int = -1
    protected var emptyId: Int = -1
    protected var errorId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        mInflater = LayoutInflater.from(mContext)
        onInitData(savedInstanceState)
        if (registerEventBus) {
            EventBusManager.register(this)
        }
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
            val toolbarLayoutId = injectLayout.toolbarLayoutId
            if (toolbarLayoutId != -1) {
                mToolbar = inflateToolbar(toolbarLayoutId)
            }
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
        //设置主页面布局
        if (toolbarOverlap) {
            // Toolbar悬浮
            setContentView(R.layout.activity_overlap_toolbar)
        } else {
            //Toolbar垂直线性
            setContentView(R.layout.activity_linear_toolbar)
        }
        //设置toolbar显示到界面
        if (null == mToolbar) {
            fl_toolbar_container.visibility = View.GONE
        } else {
            fl_toolbar_container.removeAllViews()
            mToolbar?.title = ""
            //将toolbar关联到Activity
            setSupportActionBar(mToolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            fl_toolbar_container.addView(mToolbar)
            fl_toolbar_container.visibility = View.VISIBLE
        }
        fl_base_content.removeAllViews()
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
                presenters.add(presenterInstance)
            }
        } catch (e: Exception) {
            LoggerUtil.Log(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBusManager.unRegister(this)
        presenters.map {
            it.detachView()
        }
    }

    protected fun inflateView(@LayoutRes layoutId: Int): View? =
        mInflater.inflate(layoutId, null, false)

    protected fun inflateToolbar(@LayoutRes layoutId: Int): Toolbar? {
        val view = mInflater.inflate(layoutId, null, false)
        if (view is Toolbar) {
            return view
        }
        throw IllegalArgumentException("Can not find toolbar at layout id is $layoutId")
    }

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
        if (loadingId != -1) {
            showSkeleton = Skeleton.bind(loadingView)
                .shimmer(true)
                .colorInt(Color.parseColor("#FAFAFA"))
                .angle(30)
                .duration(1000)
                .load(loadingId)
                .show()
        }
    }

    override fun hideLoding() {
        loadingView?.visibility = View.GONE
        layoutView?.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        errorView?.visibility = View.GONE
        showSkeleton?.hide()
        showSkeleton = null
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
            waitDialog = XPopup.Builder(this)
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
        runOnUiThread(runnable)
    }

    protected fun getContentView(): FrameLayout =
        fl_base_content

    protected fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_base_content, fragment)
            .commitNowAllowingStateLoss()
    }

    //===Desc:Toolbar相关方法================================================================================
    protected fun setToolbarText(@IdRes viewId: Int, text: String) {
        if (null == mToolbar) {
            return
        }
        mToolbar?.findViewById<TextView>(viewId)?.text = text
    }

    protected fun setToolbarText(@IdRes viewId: Int, @StringRes textId: Int) {
        if (null == mToolbar) {
            return
        }
        mToolbar?.findViewById<TextView>(viewId)?.setText(textId)
    }

    protected fun setToolbarText(
        @IdRes viewId: Int,
        text: String,
        @ColorInt color: Int,
        size: Float
    ) {
        setToolbarText(viewId, text)
        setToolbarTextColor(viewId, color)
        setToolbarTextSize(viewId, size)
    }

    protected fun setToolbarText(
        @IdRes viewId: Int,
        @StringRes textId: Int,
        @ColorInt color: Int,
        size: Float
    ) {
        setToolbarText(viewId, textId)
        setToolbarTextColor(viewId, color)
        setToolbarTextSize(viewId, size)
    }

    protected fun setToolbarTextColor(@IdRes viewId: Int, @ColorInt color: Int) {
        if (null == mToolbar) {
            return
        }
        mToolbar?.findViewById<TextView>(viewId)?.setTextColor(color)
    }

    protected fun setToolbarTextSize(@IdRes viewId: Int, size: Float) {
        if (null == mToolbar) {
            return
        }
        mToolbar?.findViewById<TextView>(viewId)?.textSize = size
    }

    protected fun setToolbarBackgroundColor(@ColorInt color: Int) {
        mToolbar?.setBackgroundColor(color)
    }

    protected fun setToolbarLeftImage(
        @DrawableRes drawableId: Int,
        click: View.OnClickListener? = null
    ) {
        mToolbar?.setNavigationIcon(drawableId)
        if (null != click) {
            mToolbar?.setNavigationOnClickListener(click)
        }
    }
    //===Desc:================================================================================
    /**
     * 更换StatusBar上面文本颜色,只支持黑色和白色
     */
    protected open fun changeStatusBarTextColor(isBlack: Boolean) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            @Suppress("DEPRECATION")
            val visibility = if (isBlack) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                View.SYSTEM_UI_FLAG_VISIBLE
            }
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = visibility
        }
    }

    protected fun getStatusBarHeight(): Int {
        val resources = resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    //半透明状态栏
    protected fun translucentStatusBar(hideStatusBarBackground: Boolean = false) {
        StatusBarCompat.translucentStatusBar(this, hideStatusBarBackground)
    }

    //设置状态栏颜色
    protected fun setStatusBarColor(@ColorInt statusColor: Int, alpha: Int = -1) {
        if (alpha == -1) {
            StatusBarCompat.setStatusBarColor(this, statusColor)
        } else {
            StatusBarCompat.setStatusBarColor(this, statusColor, alpha)
        }
    }

    //===Desc:EventBus================================================================================
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

    //===Desc:运行时权限================================================================================
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //权限通过
        onPermissionsPass(requestCode, perms)
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
        return EasyPermissions.hasPermissions(this, *perms)
    }

    /**请求权限
     * @param requestCode 请求码
     * @param rationale 请求权限说明
     * @param perms 需要使用的权限
     */
    protected fun requestPermissions(requestCode: Int, rationale: String, vararg perms: String) {
        EasyPermissions.requestPermissions(this, rationale, requestCode, *perms)
    }

}