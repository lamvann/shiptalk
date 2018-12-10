package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Conversation
import javax.inject.Singleton

@Singleton
class ConversationsRepository : ConversationsDataSource{

    override fun getActiveConversations(callback: ConversationsDataSource.GetActiveConversationsCallback): ArrayList<Conversation> {
        return ArrayList()
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