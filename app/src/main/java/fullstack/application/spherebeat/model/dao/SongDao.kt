package fullstack.application.spherebeat.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import fullstack.application.spherebeat.model.Song

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(songs: List<Song>)

    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSong(songId: String): LiveData<Song>

    @Query("SELECT * FROM songs")
    fun getAllSongs(): LiveData<List<Song>>

    @Delete
    suspend fun delete(song: Song)

    @Query("DELETE FROM songs")
    suspend fun clear()
}
