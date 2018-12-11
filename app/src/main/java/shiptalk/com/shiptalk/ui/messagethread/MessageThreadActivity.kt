package shiptalk.com.shiptalk.ui.messagethread

import android.os.Bundle
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity
import shiptalk.com.shiptalk.utils.Constants.MESSAGE_ID

class MessageThreadActivity : BaseActivity() {

    private lateinit var viewModel: MessageThreadViewModel

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



        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MessageThreadFragment.newInstance(messageId))
                .commitNow()
        }
    }

}
