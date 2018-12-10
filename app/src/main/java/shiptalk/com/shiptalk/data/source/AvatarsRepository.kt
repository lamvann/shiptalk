package shiptalk.com.shiptalk.data.source

import javax.inject.Singleton

@Singleton
class AvatarsRepository() : AvatarsDataSource {

    override fun getRandomAvatarForConversation(callback: AvatarsDataSource.GetRandomAvatarForConversationCallback): String {
        return ""
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