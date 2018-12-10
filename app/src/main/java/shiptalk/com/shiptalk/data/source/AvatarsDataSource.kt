package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.ResponseError

interface AvatarsDataSource {

    fun getRandomAvatarForConversation(callback: GetRandomAvatarForConversationCallback) : String

    interface GetRandomAvatarForConversationCallback{
        fun onRandomAvatarForConversationLoaded(pictureURL: String)
        fun onRandomAvatarForConversationNotLoaded(error: ResponseError)
    }
}