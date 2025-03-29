package fullstack.application.spherebeat.dal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.Song
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import fullstack.application.spherebeat.dal.networking.SongsClient
import fullstack.application.spherebeat.dal.networking.SpotifyAuth
import java.text.SimpleDateFormat
import java.util.Locale
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
                // localDb.songDao().clear() TODO: fix to work with the getSongsFromApi
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

    fun getSongsFromApi(songName: String, accessToken: String, callback: (List<Song>) -> Unit) {
        executor.execute {
            try {
                val request = SongsClient.songsApiClient.searchTracks(songName, "track",5, "Bearer $accessToken")
                val response = request.execute()

                if (response.isSuccessful) {
                    val songs = response.body()?.tracks?.items?.map { track ->
                        val id = track.id ?: ""
                        val name = track.name ?: ""
                        val singer = track.artists?.firstOrNull()?.name ?: "Unknown"
                        val cover = track.album?.images?.getOrNull(2)?.url ?: ""
                        val releaseDate = parseReleaseDate(track.album?.release_date)
                        val length = track.duration_ms ?: 0

                        Log.d("SpotifyTrack", "ID: $id, Name: $name, Singer: $singer, Cover: $cover Length: $length, Release Date: $releaseDate")
                        Song(id, name, singer, releaseDate, length, cover)
                    } ?: emptyList()

                    callback(songs)
                } else {
                    Log.e("SpotifyTrack", "Failed to fetch songs! response code: ${response.code()}, message: ${response.message()}, error body: ${response.errorBody()?.string()}")
                    callback(emptyList())
                }
            } catch (e: Exception) {
                Log.e("TAG", "Failed to fetch songs! with exception ${e}")
                callback(emptyList())
            }
        }
    }

    private fun parseReleaseDate(dateString: String?): Long {
        if (dateString == null) return 0L
        val formats = listOf("yyyy-MM-dd", "yyyy-MM", "yyyy")
        for (format in formats) {
            try {
                val dateFormat = SimpleDateFormat(format, Locale.getDefault())
                return dateFormat.parse(dateString)?.time ?: 0L
            } catch (e: java.text.ParseException) {
                // Continue to the next format
            }
        }
        return 0L
    }

    fun getAccessToken(onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        SpotifyAuth.fetchAccessToken(
            onSuccess = { token -> onSuccess(token) },
            onError = { error -> onError(error) }
        )
    }
}