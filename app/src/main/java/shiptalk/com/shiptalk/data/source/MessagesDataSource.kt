package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError

interface MessagesDataSource {

    fun getMessagesFromChannel(channelId: String, callback: GetMessagesCallback)

    interface GetMessagesCallback{
        fun onMessagesLoaded(messages: List<Message>)
        fun onMessagesNotLoaded(error: ResponseError)
    }
}