package shiptalk.com.shiptalk.data.source

class UserRepository : UserDataSource{
    override fun loginUser() {

    }

    companion object {

        private var INSTANCE: UserRepository? = null

        @JvmStatic
        fun getInstance() =
            INSTANCE ?: synchronized(UserRepository::class.java) {
                INSTANCE
                    ?: UserRepository(

                    )
                        .also { INSTANCE = it }
            }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}