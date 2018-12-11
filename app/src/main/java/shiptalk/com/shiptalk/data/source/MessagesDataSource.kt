package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError

interface MessagesDataSource {

    fun getMessagesFromChannel(channelId: String, callback: GetMessagesCallback, newMessageCallback: GetNewMessageCallback)

    fun sendMessageForChannel(message: Map<String, Any>, channelId: String, callback: GetSentMessageCallback)

    interface GetVoteCallback {
        fun onVoted()
        fun onNotVoted(error: ResponseError)
    }

    interface GetMessagesCallback{
        fun onMessagesLoaded(messages: ArrayList<Message>)
        fun onMessagesNotLoaded(error: ResponseError)
    }

    interface GetNewMessageCallback{
        fun onMessageLoaded(message: Message)
        fun onMessageNotLoaded(error: ResponseError)
    }

    interface GetSentMessageCallback{
        fun onMessageSent()
        fun onMessageNotSent(error: ResponseError)
    }
}