package shiptalk.com.shiptalk.ui.login

import shiptalk.com.shiptalk.data.User
import shiptalk.com.shiptalk.data.source.UserDataSource
import shiptalk.com.shiptalk.data.source.UserRepository
import shiptalk.com.shiptalk.ui.BaseViewModel
import javax.inject.Singleton

@Singleton
class LoginViewModel(
    private val userRepository: UserRepository
) : BaseViewModel(), UserDataSource {

    override fun getLoggedInUser(username: String): User? {
        return userRepository.getLoggedInUser(username)
    }

}
