package fullstack.application.spherebeat.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.dal.repository.PlaylistRepository

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

    fun addPlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.addPlaylist(playlist) { success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(success)
        }
    }

    fun updatePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.updatePlaylist(playlist) { success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(success)
        }
    }

    fun deletePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        playlistRepository.deletePlaylist(playlist) { success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(success)
        }
    }
}