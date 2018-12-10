package shiptalk.com.shiptalk.ui.login

import shiptalk.com.shiptalk.data.source.UserRepository
import shiptalk.com.shiptalk.ui.BaseViewModel
import javax.inject.Singleton

@Singleton
class LoginViewModel(
    userRepository: UserRepository
) : BaseViewModel() {

}
