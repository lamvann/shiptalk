package shiptalk.com.shiptalk.data.source

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import shiptalk.com.shiptalk.data.User
import shiptalk.com.shiptalk.utils.Constants.SHIPTALK_LOCAL_FILE
import shiptalk.com.shiptalk.utils.Constants.USER_LOGGED_IN
import java.util.*

class UserLocalDataSource(private val applicationContext: Context) : UserDataSource {

    override fun getLoggedInUser(username: String): User? {
        try {
            val sharedPref = applicationContext.getSharedPreferences(
                SHIPTALK_LOCAL_FILE, Context.MODE_PRIVATE
            )
            val gson = Gson()
            val deviceSerialId = sharedPref.getString(android.os.Build.ID, null) ?: UUID.randomUUID()
            val savedUser = gson.fromJson(USER_LOGGED_IN, User::class.java)

            if (savedUser != null && deviceSerialId == savedUser.id){
                return savedUser
            }
            else{
                val user = User(
                    id = deviceSerialId.toString(),
                    username = username
                )
                return logInNewUser(user)
            }
        } catch (e: Exception) {
            Log.e(this.javaClass.canonicalName, e.localizedMessage)
            return null
        }
    }

    private fun logInNewUser(newUser: User) : User? {
        val sharedPref = applicationContext.getSharedPreferences(
            SHIPTALK_LOCAL_FILE, Context.MODE_PRIVATE) ?: return null
        with(sharedPref.edit()) {
            val gson = Gson()
            val json = gson.toJson(newUser)
            putString(USER_LOGGED_IN, json)
            apply()
        }
        return newUser
    }
}