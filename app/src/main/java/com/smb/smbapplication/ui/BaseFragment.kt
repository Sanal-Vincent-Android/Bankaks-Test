package com.smb.smbapplication.ui

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.smb.smbapplication.binding.FragmentDataBindingComponent
import com.smb.smbapplication.common.autoCleared
import com.smb.smbapplication.di.Injectable
import javax.inject.Inject




/**
 * A generic Fragment   .
 * @param <T> The type of the ViewDataBinding.
*/

abstract class BaseFragment< T : ViewDataBinding> : Fragment() , Injectable {

    private var mActivity: BaseActivity? = null

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    @Inject
    protected lateinit var mViewModelFactory: ViewModelProvider.Factory

    var mBinding by autoCleared<T>()

    @LayoutRes
    abstract fun getLayoutId(): Int

    private fun dismissKeyboard(activity: FragmentActivity?, windowToken: IBinder) {

        if (activity != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(windowToken, 0)
        }
    }
    fun dismissKeyboard(windowToken: IBinder) {
        dismissKeyboard(activity, windowToken)
    }
    fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar.make(mBinding.root, message, duration).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(),
                container, false, dataBindingComponent)

        return mBinding?.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            this.mActivity = context
        }
    }


    fun <V : ViewModel> getViewModel(clazz: Class<V>): V {
       return  ViewModelProviders.of(this, mViewModelFactory).get(clazz)

    }

}
