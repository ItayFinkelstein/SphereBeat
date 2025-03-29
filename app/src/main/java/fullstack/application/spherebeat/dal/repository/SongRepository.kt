package fullstack.application.spherebeat.dal.repository

import androidx.lifecycle.LiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.Song
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import java.util.concurrent.Executors

class SongRepository {
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel: FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database

    fun getAllSongs(): LiveData<List<Song>> {
        refreshAllSongs()
        return localDb.songDao().getAllSongs()
    }

    fun getSongById(songId: String): LiveData<Song> {
        refreshAllSongs()
        return localDb.songDao().getSong(songId)
    }

    fun addSong(song: Song, callback: (Boolean) -> Unit) {
        firebaseModel.addSong(song) { success ->
            if (success) {
                refreshAllSongs()
            }
            callback(success)
        }
    }

    fun updateSong(song: Song, callback: (Boolean) -> Unit) {
        firebaseModel.updateSong(song) { success ->
            if (success) {
                refreshAllSongs()
            }
            callback(success)
        }
    }

    fun deleteSong(song: Song, callback: (Boolean) -> Unit) {
        firebaseModel.deleteSong(song) { success ->
            if (success) {
                localDb.songDao().delete(song)
                refreshAllSongs()
            }
            callback(success)
        }
    }

    private fun refreshAllSongs() {
        val lastUpdated = Song.lastUpdated
        firebaseModel.getAllSongsSince(lastUpdated) { songs ->
            executor.execute {
                var currentTime = lastUpdated
                localDb.songDao().clear()
                for (song in songs) {
                    localDb.songDao().insert(song)
                    song.lastUpdated?.let {
                        if (currentTime < it) {
                            currentTime = it
                        }
                    }
                }
                Song.lastUpdated = currentTime
            }
        }
    }
}