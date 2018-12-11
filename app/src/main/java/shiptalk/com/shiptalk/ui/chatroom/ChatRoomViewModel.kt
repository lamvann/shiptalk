package shiptalk.com.shiptalk.ui.chatroom

import androidx.lifecycle.MutableLiveData
import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError
import shiptalk.com.shiptalk.data.source.MessagesDataSource
import shiptalk.com.shiptalk.data.source.MessagesRepository
import shiptalk.com.shiptalk.data.source.UserRepository
import shiptalk.com.shiptalk.ui.BaseViewModel
import shiptalk.com.shiptalk.utils.Constants.CHATROOM_CHANNEL_ID
import javax.inject.Singleton

@Singleton
class ChatRoomViewModel(
    private val messagesRepository: MessagesRepository,
    private val userRepository: UserRepository
) : BaseViewModel(), MessagesDataSource.GetMessagesCallback, MessagesDataSource.GetSentMessageCallback,
    MessagesDataSource.GetVoteCallback {

    private var _onMessagesLoaded : MutableLiveData<List<Message>> = MutableLiveData()
    val onMessagesLoaded: MutableLiveData<List<Message>>
        get() = _onMessagesLoaded

    private var _onNotVoted : MutableLiveData<Boolean> = MutableLiveData()
    val onNotVoted: MutableLiveData<Boolean>
        get() = _onNotVoted

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
        getMessagesFromChatRoomChannel()
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

    override fun onVoted() {
        //No need to implement, data should be updated accordingly from pubnub
    }

    override fun onNotVoted(error: ResponseError) {
        onNotVoted.value = false
    }

    fun getMessagesFromChatRoomChannel(){
        messagesRepository.getMessagesFromChannel(CHATROOM_CHANNEL_ID, this)
    }

    fun sendMessage(message: String){
        val messageObject = Message(
            message = message,
            senderId = userRepository.user?.userId
        )
        messagesRepository.sendMessageForChannel(messageObject.toMap(), CHATROOM_CHANNEL_ID, this)
    }

    fun upvoteMessage(messageId: String){
        messagesRepository.upvoteMessage(messageId, CHATROOM_CHANNEL_ID, this)
    }

    fun downvoteMessage(messageId: String){
        messagesRepository.downvoteMessage(messageId, CHATROOM_CHANNEL_ID, this)
    }

}
