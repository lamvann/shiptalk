package shiptalk.com.shiptalk.data.source

interface AvatarsDataSource {

    fun getRandomAvatarForConversation(userId: String, channelId: String) : String
}