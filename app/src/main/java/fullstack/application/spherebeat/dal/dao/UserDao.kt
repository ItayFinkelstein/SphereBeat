package fullstack.application.spherebeat.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fullstack.application.spherebeat.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): LiveData<User>

    @Query("SELECT * FROM users")
    fun getAllUsers(): LiveData<List<User>>

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("DELETE FROM users")
    fun clear()
}
