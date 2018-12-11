package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.source.remote.MessageService
import javax.inject.Singleton

@Singleton
class MessagesRepository(private val messageService: MessageService) : MessagesDataSource {
    override fun sendMessageForChannel(
        message: Map<String, Any>,
        channelId: String,
        callback: MessagesDataSource.GetSentMessageCallback
    ) {
        messageService.sendMessageForChannel(message, channelId, callback)
    }

    override fun getMessagesFromChannel(channelId: String, callback: MessagesDataSource.GetMessagesCallback) {
        messageService.getMessagesFromChannel(channelId, callback)
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