package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError
import javax.security.auth.callback.Callback

interface MessagesDataSource {

    fun getMessagesFromChannel(channelId: String, callback: Callback) : ArrayList<Message>

    interface GetMessagesCallback{
        fun onMessagesLoaded(messages: ArrayList<Message>)
        fun onMessagesNotLoaded(error: ResponseError)
    }
}