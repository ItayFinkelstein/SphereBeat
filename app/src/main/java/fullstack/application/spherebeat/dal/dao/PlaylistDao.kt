package fullstack.application.spherebeat.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.model.User

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg playlist: Playlist)

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: String): LiveData<Playlist>

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): LiveData<List<Playlist>>

    @Delete
    fun delete(playlist: Playlist)

    @Query("DELETE FROM playlists")
    fun clear()
}
