package shiptalk.com.shiptalk.ui.messagethread

import androidx.lifecycle.MutableLiveData
import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError
import shiptalk.com.shiptalk.data.source.MessagesDataSource
import shiptalk.com.shiptalk.data.source.MessagesRepository
import shiptalk.com.shiptalk.data.source.UserRepository
import shiptalk.com.shiptalk.ui.BaseViewModel
import shiptalk.com.shiptalk.utils.Constants
import javax.inject.Singleton

@Singleton
class MessageThreadViewModel(
    private val messagesRepository: MessagesRepository,
    private val userRepository: UserRepository
) : BaseViewModel(), MessagesDataSource.GetMessagesCallback,
    MessagesDataSource.GetSentMessageCallback,
    MessagesDataSource.GetVoteCallback {

    private var _onNotVoted : MutableLiveData<Boolean> = MutableLiveData()
    val onNotVoted: MutableLiveData<Boolean>
        get() = _onNotVoted

    private var _onMessagesLoaded : MutableLiveData<List<Message>> = MutableLiveData()
    val onMessagesLoaded: MutableLiveData<List<Message>>
        get() = _onMessagesLoaded

    private var _onMessagesNotLoaded : MutableLiveData<String> = MutableLiveData()
    val onMessagesNotLoaded: MutableLiveData<String>
        get() = _onMessagesNotLoaded

    private var _onMessagesResponse : MutableLiveData<Boolean> = MutableLiveData()
    val onMessagesResponse: MutableLiveData<Boolean>
        get() = _onMessagesResponse

    private var _onSentMessage : MutableLiveData<Boolean> = MutableLiveData()
    val onSentMessage: MutableLiveData<Boolean>
        get() = _onSentMessage

    init {
        _onSentMessage.value = false
        _onMessagesResponse.value = false
    }

    override fun onVoted() {
        //No need to implement, data should be updated accordingly from pubnub
    }

    override fun onNotVoted(error: ResponseError) {
        onNotVoted.value = false
    }

    override fun onMessageSent() {
        _onSentMessage.value = true
    }

    override fun onMessageNotSent(error: ResponseError) {
        _onSentMessage.value = false
    }

    override fun onMessagesLoaded(messages: List<Message>) {
        _onMessagesLoaded.value = messages
        _onMessagesResponse.value = true
    }

    override fun onMessagesNotLoaded(error: ResponseError) {
        _onMessagesNotLoaded.value = error.errorMessage
        _onMessagesResponse.value = false
    }

    fun getMessagesFromChatRoomChannel(channelId: String) {
        messagesRepository.getMessagesFromChannel(channelId, this)
    }

    fun sendMessage(message: String, channelId: String){
        val messageObject = Message(
            message = message,
            senderId = userRepository.user?.userId
        )
        messagesRepository.sendMessageForChannel(messageObject.toMap(), channelId, this)
    }

    fun getMessageFromCache(messageId: String) : Message? {
        return messagesRepository.cachedMessages[messageId]
    }



    fun upvoteMessage(messageId: String){
        messagesRepository.upvoteMessage(messageId, Constants.CHATROOM_CHANNEL_ID, this)
    }

    fun downvoteMessage(messageId: String){
        messagesRepository.downvoteMessage(messageId, Constants.CHATROOM_CHANNEL_ID, this)
    }

}
