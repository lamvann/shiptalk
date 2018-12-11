package shiptalk.com.shiptalk.ui.chatroom

interface OnMessageItemClickListener {
    fun onMessageItemClickListener(messageId: String)

    fun onUpvoteClickListener(messageId: String)

    fun onDownvoteClickListener(messageId: String)
}