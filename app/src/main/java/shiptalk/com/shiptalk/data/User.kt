package shiptalk.com.shiptalk.data

open class User(
    var userId: String? = null,
    var email: String? = null,
    var username: String? = null,
    var avatarsPerChannel: ArrayList<Pair<String, String>>? = null
) {
}