package fullstack.application.spherebeat.dal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.Post
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import java.util.concurrent.Executors

class PostRepository {
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel: FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database

    fun getAllPosts(): LiveData<List<Post>> {
        refreshAllPosts()
        return localDb.postDao().getAllPosts()
    }

    fun getPostById(postId: String): LiveData<Post> {
        refreshAllPosts()
        return localDb.postDao().getPostById(postId)
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        firebaseModel.addPost(post) { success ->
            if (success) {
                refreshAllPosts()
            }
            callback(success)
        }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        firebaseModel.updatePost(post) { success ->
            if (success) {
                refreshAllPosts()
            }
            callback(success)
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        firebaseModel.deletePost(post) { success ->
            if (success) {
                localDb.postDao().delete(post)
                refreshAllPosts()
            }
            callback(success)
        }
    }

    private fun refreshAllPosts() {
        val lastUpdated = Post.lastUpdated
        Log.d("PostRepository", "firebaseModel.getAllPostsSince($lastUpdated)")

        firebaseModel.getAllPostsSince(lastUpdated) { posts ->

            executor.execute {
                var currentTime = lastUpdated
                for (post in posts) {
                    localDb.postDao().insert(post)
                    Log.d("PostRepository post singer", post.singer)

                    post.lastUpdated?.let {
                        if (currentTime < it) {
                            currentTime = it
                        }
                    }
                }
                Post.lastUpdated = currentTime
            }
        }
    }
}