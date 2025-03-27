package fullstack.application.spherebeat.dal.repository

import android.graphics.Bitmap
import fullstack.application.spherebeat.dal.remote.FirebaseModel

class ImageRepository {
    private val firebaseModel : FirebaseModel = FirebaseModel()

    fun uploadImage(name: String, bitmap: Bitmap, callback: (String?) -> Unit) {
        firebaseModel.uploadImage(name, bitmap, callback)
    }
}