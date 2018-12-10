package shiptalk.com.shiptalk.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }

    override fun setObservers() {
    }

    override fun initUIComponents() {
    }

    override fun setListeners() {
    }

}