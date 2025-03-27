package fullstack.application.spherebeat.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import java.util.concurrent.Executors

class PlaylistRepository() {
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel: FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database
    private var playlistList: LiveData<List<Playlist>>? = null
    private val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun getAllPlaylists(): LiveData<List<Playlist>> {
        loadingState.value = LoadingState.LOADING
        if (playlistList == null) {
            playlistList = localDb.playlistDao().getAllPlaylists()
            refreshAllPlaylists()
        }
        loadingState.value = LoadingState.NOT_LOADING
        return playlistList!!
    }

    fun getPlaylistById(playlistId: String): LiveData<Playlist> {
        loadingState.value = LoadingState.LOADING
        val playlist = localDb.playlistDao().getPlaylistById(playlistId)
        loadingState.value = LoadingState.NOT_LOADING
        return playlist
    }

    fun addPlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.addPlaylist(playlist) { success ->
            refreshAllPlaylists()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    fun updatePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.updatePlaylist(playlist) { success ->
            refreshAllPlaylists()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    fun deletePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.deletePlaylist(playlist) { success ->
            localDb.playlistDao().delete(playlist)
            refreshAllPlaylists()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    private fun refreshAllPlaylists() {
        val lastUpdated = Playlist.lastUpdated
        firebaseModel.getAllPlaylistsSince(lastUpdated) { playlists ->
            executor.execute {
                var currentTime = lastUpdated
                for (playlist in playlists) {
                    localDb.playlistDao().insertAll(playlist)
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