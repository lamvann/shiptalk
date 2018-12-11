package shiptalk.com.shiptalk.ui.messagethread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseFragment
import shiptalk.com.shiptalk.utils.Constants.MESSAGE_ID

class MessageThreadFragment : BaseFragment() {

    private lateinit var viewModel: MessageThreadViewModel
    private lateinit var parentActivity: MessageThreadActivity
    private var messageId : String? = null

    companion object {
        fun newInstance(messageId: String): MessageThreadFragment{
            val messageThreadFragment = MessageThreadFragment()
            val args = Bundle()
            args.putString(MESSAGE_ID, messageId)
            messageThreadFragment.arguments = args
            return messageThreadFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parentActivity = activity as MessageThreadActivity
        viewModel = parentActivity.obtainViewModel()
        messageId = arguments?.getString(MESSAGE_ID, null)
        viewModel.getMessagesFromChatRoomChannel("$messageId _channel")
        if(messageId == null){
            parentActivity.goToChatRoomActivity()
            parentActivity.finish()
        }
        return inflater.inflate(R.layout.message_thread_fragment, container, false)
    }

    override fun setObservers() {

    }

    override fun initUIComponents() {

    }

    override fun setListeners() {

    }

}
