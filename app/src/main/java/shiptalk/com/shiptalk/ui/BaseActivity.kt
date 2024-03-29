package shiptalk.com.shiptalk.ui

import android.app.Application
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import shiptalk.com.shiptalk.ViewModelFactory

abstract class BaseActivity : AppCompatActivity() {

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

    abstract fun obtainViewModel(): BaseViewModel

    fun hideTopBar() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}