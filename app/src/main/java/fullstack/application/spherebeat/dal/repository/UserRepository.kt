package fullstack.application.spherebeat.dal.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.User
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import java.util.concurrent.Executors

class UserRepository {
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel: FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database

    fun getLoggedUser(): FirebaseUser? {
        return firebaseModel.getLoggedUser()
    }

    fun getAllUsers(): LiveData<List<User>> {
        refreshAllUsers()
        return localDb.userDao().getAllUsers()
    }

    fun getUserById(userId: String): LiveData<User> {
        refreshAllUsers()
        return localDb.userDao().getUserById(userId)
    }

    fun signUp(user: User, callback: (Boolean) -> Unit) {
        firebaseModel.signUp(user) { success ->
            if (success) {
                refreshAllUsers()
            }
            callback(success)
        }
    }

    fun login(email: String, password: String, callback: (Boolean, FirebaseUser?) -> Unit) {
        firebaseModel.logIn(email, password) { success, user ->
            if (success) {
                refreshAllUsers()
            }
            callback(success, user)
        }
    }

    fun logOut(callback: (Boolean) -> Unit) {
        firebaseModel.logOut { success ->
            if (success) {
                refreshAllUsers()
            }
            callback(success)
        }
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        firebaseModel.updateUser(user) { success ->
            if (success) {
                refreshAllUsers()
            }
            callback(success)
        }
    }

    private fun refreshAllUsers() {
        val lastUpdated = User.lastUpdated
        firebaseModel.getAllUsersSince(lastUpdated) { users ->
            executor.execute {
                var currentTime = lastUpdated
                //localDb.userDao().clear()
                for (user in users) {
                    localDb.userDao().insert(user)
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