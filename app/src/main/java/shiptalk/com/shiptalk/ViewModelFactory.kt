package shiptalk.com.shiptalk

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import shiptalk.com.shiptalk.data.source.UserLocalDataSource
import shiptalk.com.shiptalk.data.source.UserRepository
import shiptalk.com.shiptalk.ui.chatroom.ChatRoomViewModel
import shiptalk.com.shiptalk.ui.login.LoginViewModel
import shiptalk.com.shiptalk.ui.messagethread.MessageThreadViewModel

class ViewModelFactory private constructor(
    private val application: Application,
    private val userRepository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(userRepository)
                isAssignableFrom(ChatRoomViewModel::class.java) ->
                    ChatRoomViewModel()
                isAssignableFrom(MessageThreadViewModel::class.java) ->
                    MessageThreadViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        private fun provideUserRepository(context: Context): UserRepository {
            return UserRepository.getInstance(UserLocalDataSource(context))
        }

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    application,
                    provideUserRepository(application.applicationContext)
                )
                    .also { INSTANCE = it }
            }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}