package shiptalk.com.shiptalk.ui.messagethread

import android.os.Bundle
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity

class MessageThreadActivity : BaseActivity() {

    private lateinit var viewModel: MessageThreadViewModel

    override fun obtainViewModel() = obtainViewModel(MessageThreadViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_thread_activity)
        viewModel = obtainViewModel()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MessageThreadFragment.newInstance())
                .commitNow()
        }
    }

}
