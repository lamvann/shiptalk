package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError
import shiptalk.com.shiptalk.data.source.remote.MessageService
import javax.inject.Singleton

@Singleton
class MessagesRepository(private val messageService: MessageService) : MessagesDataSource {
    var cachedMessages = HashMap<String, Message>()

    fun upvoteMessage(messageId: String, channelId: String, callback: MessagesDataSource.GetVoteCallback) {
        val message = cachedMessages[messageId]
        if(message == null){
            callback.onNotVoted(ResponseError(1, "Not able to get message from list"))
        }
        else{
            if (message.voteCount == null){
                message.voteCount = 1
            }
            else{
                message.voteCount = message.voteCount?.plus(1)
            }
            messageService.sendMessageForChannel(message.toMap(), channelId, object: MessagesDataSource.GetSentMessageCallback {
                override fun onMessageSent() {
                    callback.onVoted()
                }

                override fun onMessageNotSent(error: ResponseError) {
                    callback.onNotVoted(error)
                }

            })
        }
    }

    fun downvoteMessage(messageId: String, channelId: String, callback: MessagesDataSource.GetVoteCallback) {
        val message = cachedMessages[messageId]
        if(message == null){
            callback.onNotVoted(ResponseError(1, "Not able to get message from list"))
        }
        else{
            if (message.voteCount == null){
                message.voteCount = -1
            }
            else{
                message.voteCount = message.voteCount?.minus(1)
            }
            messageService.sendMessageForChannel(message.toMap(), channelId, object: MessagesDataSource.GetSentMessageCallback {
                override fun onMessageSent() {
                    callback.onVoted()
                }

                override fun onMessageNotSent(error: ResponseError) {
                    callback.onNotVoted(error)
                }

            })
        }
    }

    override fun sendMessageForChannel(
        message: Map<String, Any>,
        channelId: String,
        callback: MessagesDataSource.GetSentMessageCallback
    ) {
        messageService.sendMessageForChannel(message, channelId, callback)
    }

    override fun getMessagesFromChannel(channelId: String, callback: MessagesDataSource.GetMessagesCallback) {
        messageService.getMessagesFromChannel(channelId, object: MessagesDataSource.GetMessagesCallback{
            override fun onMessagesLoaded(messages: List<Message>) {
                cachedMessages.clear()
                messages.forEach { message ->
                    val messageId = message.messageId
                    if(messageId != null) cachedMessages[messageId] = message
                }
                callback.onMessagesLoaded(messages)
            }

            override fun onMessagesNotLoaded(error: ResponseError) {
                callback.onMessagesNotLoaded(error)
            }

        })
    }

    companion object {

        private var INSTANCE: MessagesRepository? = null

        @JvmStatic
        fun getInstance(messageService: MessageService) =
            INSTANCE ?: synchronized(MessagesRepository::class.java) {
                INSTANCE
                    ?: MessagesRepository(
                        messageService
                    )
                        .also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}