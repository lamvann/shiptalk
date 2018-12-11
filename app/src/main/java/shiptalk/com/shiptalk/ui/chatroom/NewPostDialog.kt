package shiptalk.com.shiptalk.ui.chatroom

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.post_something.*
import shiptalk.com.shiptalk.R
import shiptalk.com.shiptalk.ui.BaseActivity
import shiptalk.com.shiptalk.ui.messagethread.MessageThreadActivity

class NewPostDialog(val activity: BaseActivity) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.post_something)

        if (activity is ChatRoomActivity) {
            post_button.setOnClickListener {
                activity.postMessage(text_field.text.toString())
                dismiss()
            }
        } else if (activity is MessageThreadActivity) {
            post_button.setOnClickListener{
                activity.postMessage(text_field.text.toString(), activity.channelId.toString())
                dismiss()
            }
        }
    }
}