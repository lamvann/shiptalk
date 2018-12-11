package shiptalk.com.shiptalk.ui.messagethread

import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_item_message.*
import kotlinx.android.synthetic.main.message_thread_activity.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.ui.BaseActivity
import shiptalk.com.shiptalk.ui.chatroom.NewPostDialog
import shiptalk.com.shiptalk.ui.chatroom.OnMessageItemClickListener
import shiptalk.com.shiptalk.utils.Constants.MESSAGE_ID

class MessageThreadActivity : BaseActivity() {

    private lateinit var viewModel: MessageThreadViewModel
    private lateinit var messageParent: Message
    var channelId : String? = null

    override fun obtainViewModel() = obtainViewModel(MessageThreadViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTopBar()
        setContentView(R.layout.message_thread_activity)
        viewModel = obtainViewModel()
        val messageId = intent.getStringExtra(MESSAGE_ID)
        if(messageId == null){
            goToChatRoomActivity()
            finish()
        }

        val message = viewModel.getMessageFromCache(messageId)

        if(message != null){
            messageParent = message
        }
        else{
            goToChatRoomActivity()
            finish()
        }

        initComponents()
        setListeners()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MessageThreadFragment.newInstance(messageId))
                .commitNow()
        }
    }

    fun postMessage(message: String, channelId: String) {
        viewModel.sendMessage(message, channelId)
    }

    fun setListeners() {
        floatingActionButton.setOnClickListener {
            NewPostDialog(this).show()
        }
    }

    fun initComponents(){
        vote_count.text = messageParent.voteCount.toString()
        okay_icon.visibility = View.INVISIBLE
        dislike_icon.visibility = View.INVISIBLE
        conversation_text.text = messageParent.message

    }
}
