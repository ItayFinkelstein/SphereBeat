package fullstack.application.spherebeat.dal.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel

class ImageRepository {
    private val firebaseModel : FirebaseModel = FirebaseModel()
    val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun uploadImage(name: String, bitmap: Bitmap, callback: (String?) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.uploadImage(name, bitmap) { uri ->
            loadingState.value = LoadingState.NOT_LOADING
            callback(uri)
        }
    }
}