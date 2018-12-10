package shiptalk.com.shiptalk.ui.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseFragment

class ChatRoomFragment : BaseFragment() {

    private lateinit var viewModel: ChatRoomViewModel
    private lateinit var parentActivity: ChatRoomActivity

    companion object {
        fun newInstance() = ChatRoomFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.chat_room_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        parentActivity = activity as ChatRoomActivity
        viewModel = parentActivity.obtainViewModel()
        // TODO: Use the ViewModel
    }

    override fun setObservers() {

    }

    override fun initUIComponents() {

    }

    override fun setListeners() {

    }

}
