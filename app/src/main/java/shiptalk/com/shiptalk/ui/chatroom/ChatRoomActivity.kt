package shiptalk.com.shiptalk.ui.chatroom

import android.os.Bundle
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity

class ChatRoomActivity : BaseActivity() {

    private lateinit var viewModel: ChatRoomViewModel

    override fun obtainViewModel() = obtainViewModel(ChatRoomViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTopBar()
        setContentView(R.layout.chat_room_activity)
        viewModel = obtainViewModel()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChatRoomFragment.newInstance())
                .commitNow()
        }
    }
}
