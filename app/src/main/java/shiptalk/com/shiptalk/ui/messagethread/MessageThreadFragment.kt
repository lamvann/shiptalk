package shiptalk.com.shiptalk.ui.messagethread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.chat_room_fragment.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseFragment
import shiptalk.com.shiptalk.ui.chatroom.OnMessageItemClickListener
import shiptalk.com.shiptalk.utils.Constants.MESSAGE_ID

class MessageThreadFragment : BaseFragment() {

    private lateinit var viewModel: MessageThreadViewModel
    private lateinit var parentActivity: MessageThreadActivity
    private lateinit var messageThreadListAdapter: MessageThreadListAdapter
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parentActivity.initComponents()
        parentActivity.setListeners()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIComponents()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        parentActivity = activity as MessageThreadActivity
        viewModel = parentActivity.obtainViewModel()

        //Instantiate List Adapter
        messageThreadListAdapter = MessageThreadListAdapter(
            parentActivity.obtainViewModel().onMessagesLoaded.value,
            parentActivity
        )
        setObservers()
        messageId = arguments?.getString(MESSAGE_ID, null)
        parentActivity.channelId = "$messageId _channel"
        viewModel.getMessagesFromChatRoomChannel(parentActivity.channelId.toString())
        if(messageId == null){
            parentActivity.goToChatRoomActivity()
            parentActivity.finish()
        }

        return inflater.inflate(R.layout.message_thread_fragment, container, false)
    }

    override fun setObservers() {
        viewModel.onMessagesResponse.observe(this, Observer {
            if(it == true){
                messageThreadListAdapter.updateMessages(viewModel.onMessagesLoaded.value)
            }
            else if (it == false){
                //display error  viewModel.onMessagesNotLoaded.value
            }
        })
    }

    override fun initUIComponents() {
        rvChatRoom.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = messageThreadListAdapter
        }
//        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.primaryColor),
//            resources.getColor(R.color.primaryDarkColor),
//            resources.getColor(R.color.accentColor))
    }

    override fun setListeners() {

    }

}
