package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.Conversation
import javax.inject.Singleton

@Singleton
class ConversationsRepository : ConversationsDataSource{//TO GET CHANNELS TODO if not used, delete it

    override fun getActiveConversations(callback: ConversationsDataSource.GetActiveConversationsCallback): ArrayList<Conversation> {
        return ArrayList()
    }

    companion object {

        private var INSTANCE: ConversationsRepository? = null

        @JvmStatic
        fun getInstance() =
            INSTANCE ?: synchronized(ConversationsRepository::class.java) {
                INSTANCE
                    ?: ConversationsRepository(
                    )
                        .also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}