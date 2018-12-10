package shiptalk.com.shiptalk

import android.annotation.SuppressLint
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory private constructor(application: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                /*isAssignableFrom(::class.java) ->
                    LoginViewModel(
                        application,
                        authRepository,
                        fcmRepository,
                        ordersRepository,
                        orderInProgressRepository,
                        userProfileRepository
                    )*/
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    application/*,
                    provideAuthRepository(application.applicationContext),
                    provideUserProfileRepository(application.applicationContext),
                    provideOrderInProgressRepository(application.applicationContext),
                    ordersRepository = provideOrdersRepository(application, application.applicationContext),
                    coverageRepository = provideCoverageRepository(application.applicationContext),
                    fcmRepository = provideFCMRepository(application.applicationContext),
                    promoRepository = providePromoRepository(application.applicationContext)*/

                )
                    .also { INSTANCE = it }
            }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}