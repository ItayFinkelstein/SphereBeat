package fullstack.application.spherebeat.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import fullstack.application.spherebeat.model.Playlist

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: Playlist)

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: String): LiveData<Playlist>

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): LiveData<List<Playlist>>

    @Delete
    suspend fun delete(playlist: Playlist)

    @Query("DELETE FROM playlists")
    suspend fun clear()
}
