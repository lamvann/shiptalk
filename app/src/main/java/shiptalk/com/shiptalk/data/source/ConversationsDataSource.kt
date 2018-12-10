package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Conversation
import shiptalk.com.shiptalk.data.ResponseError

interface ConversationsDataSource {

    fun getActiveConversations(callback: GetActiveConversationsCallback) : ArrayList<Conversation>

    interface GetActiveConversationsCallback{
        fun onConversationsLoaded(conversations: ArrayList<Conversation>)
        fun onConversationsNotLoaded(error: ResponseError)
    }
}