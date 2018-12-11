package shiptalk.com.shiptalk.data.source

import shiptalk.com.shiptalk.data.User

interface UserDataSource {

    fun getLoggedInUser(username: String) : User?

    fun doesUserExist() : Boolean
}