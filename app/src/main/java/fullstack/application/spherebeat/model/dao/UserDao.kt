package fullstack.application.spherebeat.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fullstack.application.spherebeat.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): LiveData<User>

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)
}
