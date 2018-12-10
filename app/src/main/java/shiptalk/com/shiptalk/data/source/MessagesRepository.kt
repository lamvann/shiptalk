package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Message
import javax.inject.Singleton
import javax.security.auth.callback.Callback

@Singleton
class MessagesRepository() : MessagesDataSource {

    override fun getMessagesFromChannel(channelId: String, callback: Callback): ArrayList<Message> {
        val arrayList = ArrayList<Message>()
        arrayList.add(Message())
        return arrayList
    }

    companion object {

        private var INSTANCE: AvatarsRepository? = null

        @JvmStatic
        fun getInstance() =
            INSTANCE ?: synchronized(AvatarsRepository::class.java) {
                INSTANCE
                    ?: AvatarsRepository(
                    )
                        .also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}