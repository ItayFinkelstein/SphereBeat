package fullstack.application.spherebeat.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.User
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import java.util.concurrent.Executors

class UserRepository() {
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel : FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database
    private var user: LiveData<User>? = null
    val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun getLoggedUser(userId: String): LiveData<User> {
        loadingState.value = LoadingState.LOADING
        if (user == null) {
            user = localDb.userDao().getUserById(userId)
            refreshAllUsers()
        }
        loadingState.value = LoadingState.NOT_LOADING
        return user!!
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.updateUser(user) { success ->
            refreshAllUsers()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    fun signUp(newUser: User, password: String, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.signUp(newUser, password) { success ->
            refreshAllUsers()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    fun logIn(email: String, password: String, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.logIn(email, password) { success ->
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    fun logOut() {
        loadingState.value = LoadingState.LOADING
        firebaseModel.logOut()
        user = null
        loadingState.value = LoadingState.NOT_LOADING
    }

    private fun refreshAllUsers() {
        val lastUpdated = User.lastUpdated
        firebaseModel.getAllUsersSince(lastUpdated) { users ->
            executor.execute {
                var currentTime = lastUpdated
                for (user in users) {
                    localDb.userDao().insertAll(user)
                    user.lastUpdated?.let {
                        if (currentTime < it) {
                            currentTime = it
                        }
                    }
                }
                User.lastUpdated = currentTime
            }
        }
    }
}