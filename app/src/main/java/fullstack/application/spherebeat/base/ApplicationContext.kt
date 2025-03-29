package fullstack.application.spherebeat.base

import android.app.Application
import android.content.Context
import fullstack.application.spherebeat.dal.local.AppLocalDb

class ApplicationContext : Application() {
    object Globals {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        AppLocalDb.initialize(this)

        Globals.context = applicationContext
    }
}