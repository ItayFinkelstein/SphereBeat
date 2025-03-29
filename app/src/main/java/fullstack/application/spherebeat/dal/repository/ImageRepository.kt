package fullstack.application.spherebeat.dal.repository

import android.graphics.Bitmap
import android.util.Log
import fullstack.application.spherebeat.util.toFile

import androidx.lifecycle.MutableLiveData
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.cloudinary.android.policy.GlobalUploadPolicy
import fullstack.application.spherebeat.BuildConfig
import fullstack.application.spherebeat.base.ApplicationContext
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import java.io.File

class ImageRepository {
    private val firebaseModel : FirebaseModel = FirebaseModel()
    val loadingState = MutableLiveData<LoadingState>()

    init {
        val config = mapOf(
            "cloud_name" to BuildConfig.CLOUDINARY_NAME,
            "api_key" to BuildConfig.CLOUD_API_KEY,
            "api_secret" to BuildConfig.CLOUD_API_SECRET
        )

        ApplicationContext.Globals.context?.let {
            MediaManager.init(it, config)
            MediaManager.get().globalUploadPolicy = GlobalUploadPolicy.defaultPolicy()
        }
    }

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun uploadImage(
        bitmap: Bitmap,
        name: String,
        onSuccess: (String?) -> Unit,
        onError: (String?) -> Unit
    ) {
        Log.d("ImageRepository", "uploadImage called with name: $name")
        val context = ApplicationContext.Globals.context ?: return
        val file: File = bitmap.toFile(context, name)
        Log.d("ImageRepository", "File created: ${file.path}")

        MediaManager.get().upload(file.path)
            .option("folder", "images")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Log.d("ImageRepository", "Upload started: $requestId")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    Log.d("ImageRepository", "Upload progress: $bytes/$totalBytes")
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String ?: ""
                    Log.d("ImageRepository", "Upload success: $url")
                    onSuccess(url)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Log.e("ImageRepository", "Upload error: ${error?.description}")
                    onError(error?.description ?: "Unknown error")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                    Log.d("ImageRepository", "Upload rescheduled: ${error?.description}")
                }
            })
            .dispatch()
    }

}