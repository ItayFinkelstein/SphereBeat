package fullstack.application.spherebeat.dal.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import fullstack.application.spherebeat.base.ApplicationContext
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import java.io.File

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

    fun uploadImage(
        bitmap: Bitmap,
        name: String,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) {
        val context = ApplicationContext.Globals.context ?: return
        val file: File = bitmap.toFile(context, name)

        MediaManager.get().upload(file.path)
            .option("folder", "images")
            .callback(object  : UploadCallback {
                override fun onStart(requestId: String?) {
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String ?: ""
                    onSuccess(url)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    onError(error?.description ?: "Unknown error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {

                }

            })
            .dispatch()
    }
}