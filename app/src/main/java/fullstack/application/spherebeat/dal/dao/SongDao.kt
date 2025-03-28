package fullstack.application.spherebeat.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fullstack.application.spherebeat.model.Song

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song: Song)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(songs: List<Song>)

    @Query("SELECT * FROM songs WHERE id = :songId")
    fun getSong(songId: String): LiveData<Song>

    @Query("SELECT * FROM songs")
    fun getAllSongs(): LiveData<List<Song>>

    @Delete
    fun delete(song: Song)

    @Query("DELETE FROM songs")
    fun clear()
}
