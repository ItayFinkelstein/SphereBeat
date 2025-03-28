package fullstack.application.spherebeat.base

import android.app.Application
import android.content.Context

class ApplicationContext : Application() {
    object Globals {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()

        Globals.context = applicationContext
    }
}