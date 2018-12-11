package shiptalk.com.shiptalk.ui.messagethread

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_item_message.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity

import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.utils.Constants.MESSAGE_ID

class MessageThreadActivity : BaseActivity() {

    private lateinit var viewModel: MessageThreadViewModel
    private lateinit var messageParent: Message

    override fun obtainViewModel() = obtainViewModel(MessageThreadViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

//        initComponents()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MessageThreadFragment.newInstance(messageId))
                .commitNow()
        }
    }

    fun initComponents(){
        findViewById<TextView>(R.id.vote_count).text = messageParent.voteCount.toString()
        findViewById<ImageView>(R.id.okay_icon).setOnClickListener { viewModel.upvoteMessage(messageParent.messageId.toString()) }
        findViewById<ImageView>(R.id.dislike_icon).setOnClickListener { viewModel.downvoteMessage(messageParent.messageId.toString()) }
        findViewById<TextView>(R.id.conversation_text).text = messageParent.message
    }
}
