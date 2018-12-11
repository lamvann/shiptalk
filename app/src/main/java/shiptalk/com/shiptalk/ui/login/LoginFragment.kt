package shiptalk.com.shiptalk.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.abc_alert_dialog_button_bar_material.*
import kotlinx.android.synthetic.main.login_fragment.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseFragment

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
            val username = choose_username.text.trim().toString()
            if (!username.isEmpty()) {
                viewModel.getLoggedInUser(username)
                parentActivity.goToChatRoomActivity()
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