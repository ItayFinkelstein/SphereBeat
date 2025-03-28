package fullstack.application.spherebeat.dal.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fullstack.application.spherebeat.dal.dao.PlaylistDao
import fullstack.application.spherebeat.dal.dao.PostDao
import fullstack.application.spherebeat.dal.dao.SongDao
import fullstack.application.spherebeat.dal.dao.UserDao
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.model.Post
import fullstack.application.spherebeat.model.Song
import fullstack.application.spherebeat.model.User
import fullstack.application.spherebeat.util.Converters

@Database(entities = [User::class, Post::class, Song::class, Playlist::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppLocalDbRepository: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao
}

object AppLocalDb {
    private var INSTANCE: AppLocalDbRepository? = null

    fun initialize(context: Context) {
        if (INSTANCE == null) {
            synchronized(AppLocalDb::class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppLocalDbRepository::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }
    }

    val database: AppLocalDbRepository
        get() = INSTANCE ?: throw IllegalStateException("AppLocalDb is not initialized")
}