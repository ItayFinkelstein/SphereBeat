package fullstack.application.spherebeat.dal.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fullstack.application.spherebeat.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg post: Post)

    @Query("SELECT * FROM posts WHERE id = :postId")
    fun getPostById(postId: String): LiveData<Post>

    @Query("SELECT * FROM posts")
    fun getAllPosts(): LiveData<List<Post>>

    @Delete
    fun delete(post: Post)
}
