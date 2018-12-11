package shiptalk.com.shiptalk.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.login_fragment.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseFragment
import shiptalk.com.shiptalk.ui.chatroom.ChatRoomActivity
import shiptalk.com.shiptalk.utils.Constants.EXTRA_USERNAME

class LoginFragment : BaseFragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var parentActivity: LoginActivity

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parentActivity = activity as LoginActivity
        viewModel = parentActivity.obtainViewModel()

        talk_button.setOnClickListener {
            val username = choose_username.text
            if (!username.isEmpty()) {
                viewModel.getLoggedInUser(username.toString())
                val intent = Intent(activity, ChatRoomActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, username)
                startActivity(intent)
            }
        }
    }

    override fun setObservers() {
    }

    override fun initUIComponents() {
    }

    override fun setListeners() {
    }

}