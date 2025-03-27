package fullstack.application.spherebeat.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fullstack.application.spherebeat.dal.remote.FirebaseModel
import fullstack.application.spherebeat.model.Post
import fullstack.application.spherebeat.dal.local.AppLocalDb
import fullstack.application.spherebeat.dal.local.AppLocalDbRepository
import fullstack.application.spherebeat.model.User
import java.util.concurrent.Executors

class PostRepository() {
    private var executor = Executors.newSingleThreadExecutor()
    private val firebaseModel: FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database
    private var postList: LiveData<List<Post>>? = null
    val loadingState = MutableLiveData<LoadingState>()

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    fun getAllPosts(): LiveData<List<Post>> {
        loadingState.value = LoadingState.LOADING
        if (postList == null) {
            postList = localDb.postDao().getAllPosts()
            refreshAllPosts()
        }
        loadingState.value = LoadingState.NOT_LOADING
        return postList!!
    }

    fun getPostById(postId: String): LiveData<Post> {
        loadingState.value = LoadingState.LOADING
        val post = localDb.postDao().getPostById(postId)
        loadingState.value = LoadingState.NOT_LOADING
        return post
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.addPost(post) { success ->
            refreshAllPosts()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.updatePost(post) { success ->
            refreshAllPosts()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        loadingState.value = LoadingState.LOADING
        firebaseModel.deletePost(post) { success ->
            localDb.postDao().delete(post)
            refreshAllPosts()
            loadingState.value = LoadingState.NOT_LOADING
            callback(success)
        }
    }

    private fun refreshAllPosts() {
        val lastUpdated = Post.lastUpdated
        firebaseModel.getAllPostsSince(lastUpdated) { posts ->
            executor.execute {
                var currentTime = lastUpdated
                for (post in posts) {
                    localDb.postDao().insertAll(post)
                    post.lastUpdated?.let {
                        if (currentTime < it) {
                            currentTime = it
                        }
                    }
                }
                User.lastUpdated = currentTime
            }
        }
    }
}