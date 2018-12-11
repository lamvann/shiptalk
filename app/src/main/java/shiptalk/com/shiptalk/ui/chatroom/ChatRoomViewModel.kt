package shiptalk.com.shiptalk.ui.chatroom

import androidx.lifecycle.MutableLiveData
import shiptalk.com.shiptalk.data.Message
import shiptalk.com.shiptalk.data.ResponseError
import shiptalk.com.shiptalk.data.source.MessagesDataSource
import shiptalk.com.shiptalk.data.source.MessagesRepository
import shiptalk.com.shiptalk.ui.BaseViewModel
import shiptalk.com.shiptalk.utils.Constants.CHATROOM_CHANNEL_ID
import javax.inject.Singleton

@Singleton
class ChatRoomViewModel(
    private val messagesRepository: MessagesRepository
) : BaseViewModel(), MessagesDataSource.GetMessagesCallback {

    private var _onMessagesLoaded : MutableLiveData<List<Message>> = MutableLiveData()
    val onMessagesLoaded: MutableLiveData<List<Message>>
        get() = _onMessagesLoaded

    private var _onMessagesNotLoaded : MutableLiveData<String> = MutableLiveData()
    val onMessagesNotLoaded: MutableLiveData<String>
        get() = _onMessagesNotLoaded

    private var _onMessagesResponse : MutableLiveData<Boolean> = MutableLiveData()
    val onMessagesResponse: MutableLiveData<Boolean>
        get() = _onMessagesResponse

    init {
        _onMessagesResponse.value = false
    }

    override fun onMessagesLoaded(messages: List<Message>) {
        _onMessagesLoaded.value = messages
    }

    override fun onMessagesNotLoaded(error: ResponseError) {
        _onMessagesNotLoaded.value = error.errorMessage
    }

    fun getMessagesFromChatRoomChannel(){
        messagesRepository.getMessagesFromChannel(CHATROOM_CHANNEL_ID, this)
    }

}
