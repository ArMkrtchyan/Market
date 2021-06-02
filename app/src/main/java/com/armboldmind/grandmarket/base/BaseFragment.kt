package com.armboldmind.grandmarket.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.armboldmind.grandmarket.data.models.entity.Keys
import com.armboldmind.grandmarket.shared.customview.StateLayout
import com.armboldmind.grandmarket.shared.globalextensions.connectivityManager
import com.armboldmind.grandmarket.shared.globalextensions.keysLiveData
import com.armboldmind.grandmarket.ui.MainActivity

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IBaseView {

    protected lateinit var mKeys: Keys
    protected lateinit var mActivity: BaseActivity<*>
    protected open val toolbar: Toolbar? = null
    protected open val emptyModel: StateLayout.EmptyModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            mActivity = context
        }
    }

    inline fun <reified T : BaseViewModel> createViewModel(modelClass: Class<T>, view: IBaseView): T {
        return ViewModelProvider(this).get(modelClass)
                .apply {
                    setStateHandler(view::handleViewState)
                }
    }


    private lateinit var _binding: VB
    protected val mBinding: VB
        get() = _binding

    protected abstract val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
    protected abstract fun initView(binding: VB, keysFromDb: Keys)
    protected abstract fun setLoading(isLoading: Boolean)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (!::_binding.isInitialized) {
            _binding = inflate(inflater, container, false)
            keysLiveData().observe(viewLifecycleOwner) { keysFromDb ->
                mKeys = keysFromDb
                initView(_binding, keysFromDb)
            }
        }
        toolbar?.let {
            mActivity.setSupportActionBar(it)
            mActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        return _binding.root
    }

    override fun showError(keys: Keys?, message: Throwable, onRetryClick: (() -> Unit)?) {
        setLoading(false)
        mActivity.showError(mKeys, message, onRetryClick)
    }


    override fun showSnackBar(message: String) {
        mActivity.showSnackBar(message)
    }

    override fun showSnackBar(@StringRes resId: Int) {
        showSnackBar(resources.getString(resId))
    }

    override fun showToast(message: String) {
        mActivity.showToast(message)
    }

    override fun showToast(@StringRes resId: Int) {
        showToast(resources.getString(resId))
    }

    override fun hasPermission(permission: String): Boolean {
        return mActivity.hasPermission(permission)
    }

    override fun hideSoftInput() {
        mActivity.hideSoftInput()
    }

    override fun setLightStatusBar() {
        mActivity.setLightStatusBar()
    }

    override fun clearLightStatusBar() {
        mActivity.clearLightStatusBar()
    }

    override fun hideBottomNavigation() {
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun showBottomNavigation() {
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        connectivityManager().registerConnectionObserver(viewLifecycleOwner)
        hideSoftInput()
    }

    override fun onStop() {
        super.onStop()
        connectivityManager().unregisterConnectionObserver(viewLifecycleOwner)
    }

    protected fun initAnimation(newDuration: Long = 500) {
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(android.R.transition.move)
                .apply {
                    this?. duration = newDuration
                    this?.addListener(object : Transition.TransitionListener {
                        override fun onTransitionStart(transition: Transition) {
                            MainActivity.isAnimated = true
                        }

                        override fun onTransitionEnd(transition: Transition) {
                            MainActivity.isAnimated = false
                        }

                        override fun onTransitionCancel(transition: Transition) {
                            MainActivity.isAnimated = false
                        }

                        override fun onTransitionPause(transition: Transition) {

                        }

                        override fun onTransitionResume(transition: Transition) {

                        }

                    })
                }
    }

    fun showKeyBoard(editText: EditText) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun showLongToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG)
                .show()
    }
}
