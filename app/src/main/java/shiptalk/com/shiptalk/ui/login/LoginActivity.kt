package shiptalk.com.shiptalk.ui.login

import android.os.Bundle
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity

class LoginActivity : BaseActivity() {

    override fun obtainViewModel() = obtainViewModel(LoginViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

}
