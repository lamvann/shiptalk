package shiptalk.com.shiptalk.data.source

import android.content.Context
import com.google.gson.Gson
import shiptalk.com.shiptalk.data.User
import shiptalk.com.shiptalk.utils.Constants.SHIPTALK_LOCAL_FILE
import shiptalk.com.shiptalk.utils.Constants.USER_LOGGED_IN
import java.util.*

class UserLocalDataSource(private val applicationContext: Context) : UserDataSource {

    override fun doesUserExist(): Boolean {
        val sharedPref = applicationContext.getSharedPreferences(
            SHIPTALK_LOCAL_FILE, Context.MODE_PRIVATE
        )
        val gson = Gson()
        val deviceSerialId = android.os.Build.ID ?: UUID.randomUUID()
        val savedUser = try {
            sharedPref.getString(USER_LOGGED_IN, null).let { gson.fromJson(it, User::class.java) }
        } catch (e: Exception) {
            null
        }

        return savedUser != null && deviceSerialId == savedUser.userId
    }

    override fun getLoggedInUser(username: String): User? {
        val sharedPref = applicationContext.getSharedPreferences(
            SHIPTALK_LOCAL_FILE, Context.MODE_PRIVATE
        )
        val gson = Gson()
        val deviceSerialId = android.os.Build.ID ?: UUID.randomUUID()
        val savedUser = try {
            sharedPref.getString(USER_LOGGED_IN, null).let { gson.fromJson(it, User::class.java) }
        } catch (e: Exception) {
            null
        }
        return if (savedUser != null && deviceSerialId == savedUser.userId) {
            savedUser
        } else {
            val user = User(
                userId = deviceSerialId.toString(),
                username = username
            )
            logInNewUser(user)
        }
}

private fun logInNewUser(newUser: User): User? {
    val sharedPref = applicationContext.getSharedPreferences(
        SHIPTALK_LOCAL_FILE, Context.MODE_PRIVATE
    ) ?: return null
    with(sharedPref.edit()) {
        val gson = Gson()
        val json = gson.toJson(newUser)
        putString(USER_LOGGED_IN, json)
        apply()
    }
    return newUser
}
}