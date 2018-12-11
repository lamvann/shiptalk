package shiptalk.com.shiptalk.ui.login

import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.login_fragment.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun obtainViewModel() = obtainViewModel(LoginViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTopBar()
        setContentView(R.layout.login_activity)
        viewModel = obtainViewModel()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }
}
