package shiptalk.com.shiptalk.data

import java.util.*

class Channel(
    var id: String? = null,
    var displayName: String? = null,
    var messages: ArrayList<Message>? = null
) {
}