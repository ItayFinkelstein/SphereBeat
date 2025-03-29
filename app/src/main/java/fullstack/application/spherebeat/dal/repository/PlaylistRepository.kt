package fullstack.application.spherebeat.dal.repository

import androidx.lifecycle.LiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import java.util.concurrent.Executors

class PlaylistRepository {
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel: FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database

    fun getAllPlaylists(): LiveData<List<Playlist>> {
        refreshAllPlaylists()
        return localDb.playlistDao().getAllPlaylists()
    }

    fun getPlaylistById(playlistId: String): LiveData<Playlist> {
        refreshAllPlaylists()
        return localDb.playlistDao().getPlaylistById(playlistId)
    }

    fun addPlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        firebaseModel.addPlaylist(playlist) { success ->
            if (success) {
                refreshAllPlaylists()
            }
            callback(success)
        }
    }

    fun updatePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        firebaseModel.updatePlaylist(playlist) { success ->
            if (success) {
                refreshAllPlaylists()
            }
            callback(success)
        }
    }

    fun deletePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        firebaseModel.deletePlaylist(playlist) { success ->
            if (success) {
                localDb.playlistDao().delete(playlist)
                refreshAllPlaylists()
            }
            callback(success)
        }
    }

    private fun refreshAllPlaylists() {
        val lastUpdated = Playlist.lastUpdated
        firebaseModel.getAllPlaylistsSince(lastUpdated) { playlists ->
            executor.execute {
                var currentTime = lastUpdated
                localDb.playlistDao().clear()
                for (playlist in playlists) {
                    localDb.playlistDao().insert(playlist)
                    playlist.lastUpdated?.let {
                        if (currentTime < it) {
                            currentTime = it
                        }
                    }
                }
                Playlist.lastUpdated = currentTime
            }
        }
    }
}