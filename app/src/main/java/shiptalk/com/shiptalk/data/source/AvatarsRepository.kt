package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.source.local.AvatarManager
import javax.inject.Singleton

@Singleton
class AvatarsRepository(
    private val avatarManager: AvatarManager
) : AvatarsDataSource {

    override fun getRandomAvatarForConversation(userId: String, channelId: String): String {
        return "" //TODO ->>> avatarManager.getNextAvailableAvatarForUserAndChannel(userId, channelId) Somehow get the drawable with the int avatarId
    }

    companion object {

        private var INSTANCE: AvatarsRepository? = null

        @JvmStatic
        fun getInstance(avatarManager: AvatarManager) =
            INSTANCE ?: synchronized(AvatarsRepository::class.java) {
                INSTANCE
                    ?: AvatarsRepository(
                        avatarManager
                    )
                        .also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}