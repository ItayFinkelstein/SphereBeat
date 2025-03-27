package fullstack.application.spherebeat.dal.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fullstack.application.spherebeat.base.ApplicationContext
import fullstack.application.spherebeat.dao.PostDao
import fullstack.application.spherebeat.dao.UserDao
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

    val database: AppLocalDbRepository by lazy {

        val context = ApplicationContext.Globals.context ?: throw IllegalStateException("Application context is missing")

        Room.databaseBuilder(
            context = context,
            klass = AppLocalDbRepository::class.java,
            name = "app_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}