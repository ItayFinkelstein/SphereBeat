package fullstack.application.spherebeat.base

import android.app.Application
import android.content.Context
import com.cloudinary.android.MediaManager
import fullstack.application.spherebeat.BuildConfig
import fullstack.application.spherebeat.dal.local.AppLocalDb

class ApplicationContext : Application() {
    object Globals {
        var context: Context? = null
        var isMediaManagerInitialized = false
    }


    override fun onCreate() {
        super.onCreate()
        AppLocalDb.initialize(this)

        if (!Globals.isMediaManagerInitialized) {
            val config = mapOf(
                "cloud_name" to BuildConfig.CLOUDINARY_NAME,
                "api_key" to BuildConfig.CLOUD_API_KEY,
                "api_secret" to BuildConfig.CLOUD_API_SECRET
            )
            MediaManager.init(this, config)
            Globals.isMediaManagerInitialized = true
        }

        Globals.context = applicationContext
    }
}