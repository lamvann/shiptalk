package shiptalk.com.shiptalk.ui.login

import android.content.Intent
import android.os.Bundle
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity
import shiptalk.com.shiptalk.ui.chatroom.ChatRoomActivity

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel

    override fun obtainViewModel() = obtainViewModel(LoginViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTopBar()
        setContentView(R.layout.login_activity)
        viewModel = obtainViewModel()
//        if(viewModel.doesUserExist()) goToChatRoomActivity()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

    fun goToChatRoomActivity(){
        val intent = Intent(this, ChatRoomActivity::class.java)
        startActivity(intent)
    }
}
