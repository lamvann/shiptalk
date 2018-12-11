package shiptalk.com.shiptalk.ui.chatroom

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.chat_room_fragment.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseFragment

class ChatRoomFragment : BaseFragment(), OnMessageItemClickListener {
    private lateinit var viewModel: ChatRoomViewModel

    private lateinit var parentActivity: ChatRoomActivity

    private lateinit var chatRoomListAdapter: ChatRoomListAapter
    companion object {
        fun newInstance() = ChatRoomFragment()

    }

    override fun onUpvoteClickListener(messageId: String) {
        viewModel.upvoteMessage(messageId)
    }

    override fun onDownvoteClickListener(messageId: String) {
        viewModel.downvoteMessage(messageId)
    }

    override fun onMessageItemClickListener(messageId: String) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        parentActivity = activity as ChatRoomActivity
        viewModel = parentActivity.obtainViewModel()
        //Instantiate List Adapter
        chatRoomListAdapter = ChatRoomListAapter(
            parentActivity.obtainViewModel().onMessagesLoaded.value,
            parentActivity,
            this
        )
        setObservers()
        return inflater.inflate(R.layout.chat_room_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        floatingActionButton.setOnClickListener {
            activity?.let {
                val dialog = NewPostDialog(it).show()

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIComponents()
    }

    override fun setObservers() {
        viewModel.onMessagesResponse.observe(this, Observer {
            if(it == true){
                chatRoomListAdapter.updateMessages(viewModel.onMessagesLoaded.value)
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
            adapter = chatRoomListAdapter
        }
//        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.primaryColor),
//            resources.getColor(R.color.primaryDarkColor),
//            resources.getColor(R.color.accentColor))
    }

    override fun setListeners() {

    }

}
