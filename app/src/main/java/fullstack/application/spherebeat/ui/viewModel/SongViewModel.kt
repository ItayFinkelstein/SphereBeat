package fullstack.application.spherebeat.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fullstack.application.spherebeat.model.Song
import fullstack.application.spherebeat.dal.repository.SongRepository

class SongViewModel : ViewModel() {
    private val songRepository: SongRepository = SongRepository()

    val songList: LiveData<List<Song>> = songRepository.getAllSongs()

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> get() = _loadingState

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun getSongById(songId: String): LiveData<Song> {
        return songRepository.getSongById(songId)
    }

    fun addSong(song: Song, callback: (String, Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        songRepository.addSong(song) { data, success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(data, success)
        }
    }

    fun updateSong(song: Song, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        songRepository.updateSong(song) { success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(success)
        }
    }

    fun deleteSong(song: Song, callback: (Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        songRepository.deleteSong(song) { success ->
            _loadingState.postValue(LoadingState.NOT_LOADING)
            callback(success)
        }
    }

    fun fetchSongsFromApi(songName: String, callback: (List<Song>, Boolean) -> Unit) {
        _loadingState.value = LoadingState.LOADING
        songRepository.getAccessToken(
            onSuccess = { accessToken ->
                Log.d("SongViewModel", "Access token: $accessToken")
                songRepository.getSongsFromApi(songName, accessToken) { data, success ->
                    _loadingState.postValue(LoadingState.NOT_LOADING)
                    callback(data, success)
                }
            },
            onError = {
                _loadingState.postValue(LoadingState.NOT_LOADING)
                callback(emptyList(), false)
            }
        )
    }
}