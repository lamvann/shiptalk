package shiptalk.com.shiptalk.ui.chatroom

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import kotlinx.android.synthetic.main.post_something.*
import shiptalk.com.shiptalk.R

class NewPostDialog(val activity: ChatRoomActivity) : Dialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.post_something)

        post_button.setOnClickListener {
            Log.e("tag", "hey")
            activity.postMessage(text_field.text.toString())
            dismiss()
        }
    }
}