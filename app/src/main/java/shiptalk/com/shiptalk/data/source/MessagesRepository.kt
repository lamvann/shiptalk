package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError
import shiptalk.com.shiptalk.data.source.remote.MessageService
import shiptalk.com.shiptalk.utils.Constants.MAX_NUMBER_DOWNVOTES
import java.util.*
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class MessagesRepository(private val messageService: MessageService) : MessagesDataSource {
    var cachedMessages = HashMap<String, Message>()

    fun upvoteMessage(messageId: String, channelId: String, callback: MessagesDataSource.GetVoteCallback) {
        val message = cachedMessages[messageId]
        if (message == null) {
            callback.onNotVoted(ResponseError(1, "Not able to get message from list"))
        } else {
            if (message.voteCount == null) {
                message.voteCount = 1
            } else {
                message.voteCount = message.voteCount?.plus(1)
            }
            message.timeCreated = System.currentTimeMillis()
            messageService.sendMessageForChannel(
                message.toMap(),
                channelId,
                object : MessagesDataSource.GetSentMessageCallback {
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
        if (message == null) {
            callback.onNotVoted(ResponseError(1, "Not able to get message from list"))
        } else {
            if (message.voteCount == null) {
                message.voteCount = -1
            } else {
                message.voteCount = message.voteCount?.minus(1)
            }
            if(message.voteCount != null && message.voteCount!! < MAX_NUMBER_DOWNVOTES){
                message.isBlocked = true
            }
            message.timeCreated = System.currentTimeMillis()
            messageService.sendMessageForChannel(
                message.toMap(),
                channelId,
                object : MessagesDataSource.GetSentMessageCallback {
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

    override fun getMessagesFromChannel(
        channelId: String,
        callback: MessagesDataSource.GetMessagesCallback,
        newMessageCallback: MessagesDataSource.GetNewMessageCallback
    ) {
        messageService.getMessagesFromChannel(channelId, object : MessagesDataSource.GetMessagesCallback {
            override fun onMessagesLoaded(messages: ArrayList<Message>) {
                cachedMessages.clear()
                messages.forEach { message ->
                    val messageId = message.messageId
                    if (messageId != null) cachedMessages[messageId] = message
                }
                callback.onMessagesLoaded(messages)
            }

            override fun onMessagesNotLoaded(error: ResponseError) {
                callback.onMessagesNotLoaded(error)
            }

        }, object : MessagesDataSource.GetNewMessageCallback {
            override fun onMessageLoaded(message: Message) {
                cachedMessages[message.messageId.toString()] = message
                cachedMessages = filterUniqueMessages(cachedMessages)
                callback.onMessagesLoaded(orderChronologically(cachedMessages))
            }

            override fun onMessageNotLoaded(error: ResponseError) {
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

    fun filterUniqueMessages(unfilteredMessages: HashMap<String, Message>) : HashMap<String, Message> {
        if (unfilteredMessages.size < 2) {
            return unfilteredMessages
        }

        val uniqueMessages = HashMap<String, Message>()

        for (message in unfilteredMessages) {
            //Check for concurrency
            if (uniqueMessages.containsKey(message.key)) {
                val messageUnique = uniqueMessages[message.key]
                if (messageUnique?.timeCreated != null
                    && message.value.timeCreated != null
                    && messageUnique.timeCreated!! < message.value.timeCreated!!
                ) {
                    uniqueMessages[message.key] = message.value
                }
            } else {
                uniqueMessages[message.key] = message.value
            }
        }

        val uniqueMessagesNoBlockers = HashMap<String, Message>()

        for (message in uniqueMessages.values) {
            if (!message.isBlocked!!) {
                uniqueMessagesNoBlockers[message.messageId!!] = message
            }
        }
        return uniqueMessagesNoBlockers
    }


    private fun orderChronologically(messages: HashMap<String, Message>): ArrayList<Message> {
        val messagesList = ArrayList(messages.values)
        if (messagesList.isEmpty()) {
            return messagesList
        }
        Collections.sort(messagesList, ComparatorClass())
        return messagesList
    }

    internal inner class ComparatorClass : Comparator<Message> {

        override fun compare(m1: Message, m2: Message): Int {
            return m2.timeCreated!!.compareTo(m1.timeCreated!!)
        }
    }

}