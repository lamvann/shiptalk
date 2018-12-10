package shiptalk.com.shiptalk.ui.messagethread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseFragment

class MessageThreadFragment : BaseFragment() {

    private lateinit var viewModel: MessageThreadViewModel
    private lateinit var parentActivity: MessageThreadActivity

    companion object {
        fun newInstance() = MessageThreadFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.message_thread_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parentActivity = activity as MessageThreadActivity
        viewModel = parentActivity.obtainViewModel()
    }

    override fun setObservers() {

    }

    override fun initUIComponents() {

    }

    override fun setListeners() {

    }

}
