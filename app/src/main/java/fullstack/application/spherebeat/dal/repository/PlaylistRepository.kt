package fullstack.application.spherebeat.dal.repository

import android.util.Log
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

    fun likePlaylist(playlist: Playlist, userId: String?, callback: (Boolean) -> Unit) {
        val updatedLikes = playlist.likes.toMutableList()
        if (userId != null) {
            if (updatedLikes.contains(userId)) {
                updatedLikes.remove(userId) // Unlike the playlist
            } else {
                updatedLikes.add(userId) // Unlike the playlist
            }
            firebaseModel.updatePlaylistLikes(playlist.id, updatedLikes) { success ->
                if (success) {
                    refreshAllPlaylists()
                }
                callback(success)
            }
        } else {
            callback(false)
        }
    }

    fun addSongToPlaylist(playlist: Playlist, songId: String?, callback: (Boolean) -> Unit) {
        val updatedSongs = playlist.songs.toMutableList()

        if (songId != null) {
            if (!updatedSongs.contains(songId)) {
                updatedSongs.add(songId)
            }
            firebaseModel.updatePlaylistSongs(playlist.id, updatedSongs) { success ->
                if (success) {
                    refreshAllPlaylists()
                }
                callback(success)
            }
        } else {
            callback(false)
        }
    }

    fun removeSongFromPlaylist(playlist: Playlist, songId: String?, callback: (Boolean) -> Unit) {
        val updatedSongs = playlist.songs.toMutableList()

        if (songId != null) {
            if (updatedSongs.contains(songId)) {
                updatedSongs.remove(songId)
            }
            firebaseModel.updatePlaylistSongs(playlist.id, updatedSongs) { success ->
                if (success) {
                    refreshAllPlaylists()
                }
                callback(success)
            }
        } else {
            callback(false)
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

    fun deletePlaylistById(id: String, callback: (Boolean) -> Unit) {
        firebaseModel.deletePlaylist(id) { success ->
            if (success) {
                //localDb.playlistDao().deleteById(id) // TODO: Check later
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