package fullstack.application.spherebeat.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import fullstack.application.spherebeat.model.User
import fullstack.application.spherebeat.dal.repository.UserRepository

class UserViewModel : ViewModel() {
    private val userRepository: UserRepository = UserRepository()
    val userList: LiveData<List<User>> = userRepository.getAllUsers()

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = _loadingState

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun getLoggedUser(): FirebaseUser? {
        return userRepository.getLoggedUser()
    }

    fun getUserById(userId: String): LiveData<User> {
        return userRepository.getUserById(userId)
    }

    fun signUp(user: User, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        userRepository.signUp(user) { success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(success)
        }
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        userRepository.updateUser(user) { success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(success)
        }
    }
}