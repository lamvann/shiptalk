package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.User

class UserRepository(
    private val userLocalDataSource: UserLocalDataSource
) : UserDataSource {

    private var _user: User? = null
    val user get() = _user

    override fun doesUserExist(): Boolean {
        return userLocalDataSource.doesUserExist()
    }

    override fun getLoggedInUser(username: String): User? {
        if (_user == null) {
            _user = userLocalDataSource.getLoggedInUser(username)
        }
        return _user
    }

    companion object {

        private var INSTANCE: UserRepository? = null

        @JvmStatic
        fun getInstance(userLocalDataSource: UserLocalDataSource) =
            INSTANCE ?: synchronized(UserRepository::class.java) {
                INSTANCE
                    ?: UserRepository(
                        userLocalDataSource
                    )
                        .also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}