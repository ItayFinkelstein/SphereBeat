package fullstack.application.spherebeat.base

import android.app.Application
import android.content.Context
import com.cloudinary.android.MediaManager
import fullstack.application.spherebeat.BuildConfig
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.model.Post
import fullstack.application.spherebeat.model.Song
import fullstack.application.spherebeat.model.User

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

        // Reset lastUpdated values for all entities
        resetLocalLastUpdated()
    }

    private fun resetLocalLastUpdated() {
        getSharedPreferences("TAG", Context.MODE_PRIVATE).edit()
            .putLong(Playlist.LOCAL_LAST_UPDATED, 0)
            .putLong(Post.LOCAL_LAST_UPDATED, 0)
            .putLong(User.LOCAL_LAST_UPDATED, 0)
            .putLong(Song.LOCAL_LAST_UPDATED, 0)
            .apply()
    }
}