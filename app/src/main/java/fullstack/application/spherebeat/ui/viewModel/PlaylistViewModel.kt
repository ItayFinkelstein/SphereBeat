package fullstack.application.spherebeat.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.dal.repository.PlaylistRepository
import fullstack.application.spherebeat.model.Post

class PlaylistViewModel : ViewModel() {
    private val playlistRepository: PlaylistRepository = PlaylistRepository()
    val playlistList: LiveData<List<Playlist>> = playlistRepository.getAllPlaylists()

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = _loadingState

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun getPlaylistById(playlistId: String): LiveData<Playlist> {
        return playlistRepository.getPlaylistById(playlistId)
    }

    fun refresh() {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.getAllPlaylists()
        _loadingState.value = LoadingState.NOT_LOADING
    }

    fun addPlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.addPlaylist(playlist) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }

    fun likePlaylist(playlist: Playlist, userId: String?, callback: (Boolean) -> Unit) {
        playlistRepository.likePlaylist(playlist, userId) { success ->
            callback(success)
        }
    }

    fun updatePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.updatePlaylist(playlist) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }

    fun deletePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.deletePlaylist(playlist) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }

    fun deletePlaylistById(playlistId: String, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.deletePlaylistById(playlistId) { success ->
            if (success) {
                _loadingState.postValue(LoadingState.NOT_LOADING)
            }
            callback(success)
        }
    }
}