package fullstack.application.spherebeat.base

import android.app.Application
import fullstack.application.spherebeat.dal.local.AppLocalDb

class SphereBeatApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLocalDb.initialize(this)
    }
}