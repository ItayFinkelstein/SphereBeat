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
        val postsLiveData = localDb.postDao().getAllPosts()
        postsLiveData.observeForever { posts ->
            Log.d("PostRepository", "Fetched posts: ${posts.size}")
        }
        return postsLiveData
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

    fun likePost(post: Post, userId: String?, callback: (Boolean) -> Unit) {
        val updatedLikes = post.likes.toMutableList()
        if (userId != null) {
            if (updatedLikes.contains(userId)) {
                updatedLikes.remove(userId) // Unlike the post
            } else {
                updatedLikes.add(userId) // Like the post
            }
            firebaseModel.updatePostLikes(post.id, updatedLikes) { success ->
                if (success) {
                    refreshAllPosts()
                }
                callback(success)
            }
        } else {
            callback(false)
        }
    }


    fun deletePostById(id: String, callback: (Boolean) -> Unit) {
        firebaseModel.deletePost(id) { success ->
            if (success) {
                //localDb.postDao().deleteById(id) // TODO: Check later
                refreshAllPosts()
            }
            callback(success)
        }
    }
    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        firebaseModel.deletePost(post.id) { success ->
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
                localDb.postDao().clear()
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