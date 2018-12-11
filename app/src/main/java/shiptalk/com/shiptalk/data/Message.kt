package shiptalk.com.shiptalk.data

import java.util.*

class Message(
    var messageId: String? = UUID.randomUUID().toString(),
    var senderId: String? = null,
    var timeCreated: Long? = System.currentTimeMillis(),
    var message: String? = null,
    var voteCount: Int? = 0,
    var senderAvatar: String? = null,
    var isBlocked: Boolean? = false
) {

    fun toMap(): Map<String, Any> {
        val result = HashMap<String, Any>()
        with(result) {
            if (messageId != null) messageId?.let { put("messageId", it) }
            if (senderId != null) senderId?.let { put("senderId", it) }
            if (timeCreated != null) timeCreated?.let { put("timeCreated", it) }
            if (message != null) message?.let { put("message", it) }
            if (voteCount != null) voteCount?.let { put("voteCount", it) }
            if (senderAvatar != null) senderAvatar?.let { put("senderAvatar", it) }
            if (isBlocked != null) isBlocked?.let { put("isBlocked", it) }
        }
        return result
    }

    fun isValidated() : Boolean{
        return messageId != null && senderId != null && message != null
    }
}