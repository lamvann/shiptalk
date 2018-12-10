package shiptalk.com.shiptalk.ui

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import shiptalk.com.shiptalk.BaseViewInterface
import shiptalk.com.shiptalk.ViewModelFactory

abstract class BaseActivity : AppCompatActivity(), BaseViewInterface {

    /**
     * Obtains ViewModel for Activity from a ViewModelProvider
     */
    fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application as Application)).get(viewModelClass)

    /**
     * Safely commit fragment manager transaction
     */
    private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
        beginTransaction().apply {
            action()
        }.commit()
    }

    /**
     * This method must be used for all BaseActivity to be able to get the correct custom ViewModel
     * override fun obtainViewModel() = obtainViewModel(CustomViewModel::class.java)
     */
    abstract fun obtainViewModel(): BaseViewModel
}