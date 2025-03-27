package fullstack.application.spherebeat.model

import android.graphics.Bitmap
import android.os.Looper
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fullstack.application.spherebeat.model.dao.AppLocalDb
import fullstack.application.spherebeat.firebase.FirebaseModel
import fullstack.application.spherebeat.model.dao.AppLocalDbRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class Model private constructor() {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel : FirebaseModel = FirebaseModel()
    private val localDb: AppLocalDbRepository = AppLocalDb.database

    private var user: LiveData<User>? = null
    private var postList: LiveData<List<Post>>? = null
    private var playlistList: LiveData<List<Playlist>>? = null


    companion object {
        val instance: Model by lazy { Model() }
    }

    enum class LoadingState {
        LOADING,
        NOT_LOADING
    }

    val eventPostsListLoadingState = MutableLiveData(LoadingState.NOT_LOADING)

    // ------------------------------------- USERS ------------------------------------------------
    fun getLoggedUser(userId: String): LiveData<User> {
        if (user == null) {
            user = localDb.userDao().getUserById(userId)
            refreshAllUsers()
        }
        return user!!
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        firebaseModel.updateUser(user) { success ->
            refreshAllUsers()
            callback(success)
        }
    }

    fun signUp(newUser: User, password: String, callback: (Boolean) -> Unit) {
        firebaseModel.signUp(newUser, password) { success ->
            refreshAllUsers()
            callback(success)
        }
    }

    fun logIn(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseModel.logIn(email, password, callback)
    }

    fun logOut() {
        firebaseModel.logOut()
        user = null
    }

    private fun refreshAllUsers() {
        val localLastUpdated = User.lastUpdated
        firebaseModel.getAllUsersSince(localLastUpdated) { users ->
            executor.execute {
                var latestUpdate = localLastUpdated
                // Launching a coroutine on the main thread for DB operations
                kotlinx.coroutines.GlobalScope.launch {
                    for (user in users) {
                        localDb.userDao().insert(user) // Calling the suspend function
                        latestUpdate = maxOf(latestUpdate, user.lastUpdated ?: 0)
                    }
                    User.lastUpdated = latestUpdate
                }
            }
        }
    }


    // ------------------------------------- POSTS ------------------------------------------------
    fun getAllPosts(): LiveData<List<Post>> {
        if (postList == null) {
            postList = localDb.postDao().getAllPosts()
            refreshAllPosts()
        }
        return postList!!
    }

    fun getPostById(postId: String): LiveData<Post> {
        return localDb.postDao().getPostById(postId)
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        firebaseModel.addPost(post) { success ->
            refreshAllPosts()
            callback(success)
        }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        firebaseModel.updatePost(post) { success ->
            refreshAllPosts()
            callback(success)
        }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        firebaseModel.deletePost(post)
        // Using GlobalScope to run delete operation asynchronously
        GlobalScope.launch {
            localDb.postDao().delete(post) // Calling the suspend function
            mainHandler.post { callback(true) }
        }
        refreshAllPosts()
    }

    private fun refreshAllPosts() {
        eventPostsListLoadingState.postValue(LoadingState.LOADING)
        val localLastUpdated = Post.lastUpdated
        firebaseModel.getAllPostsSince(localLastUpdated) { posts ->
            executor.execute {
                var latestUpdate = localLastUpdated
                GlobalScope.launch {
                    for (post in posts) {
                        localDb.postDao().insert(post)
                        latestUpdate = maxOf(latestUpdate, post.lastUpdated ?: 0)
                    }
                    Post.lastUpdated = latestUpdate
                    eventPostsListLoadingState.postValue(LoadingState.NOT_LOADING)
                }
            }
        }
    }

    // ------------------------------------ PLAYLISTS ---------------------------------------------

    fun getAllPlaylists(): LiveData<List<Playlist>> {
        if (playlistList == null) {
            playlistList = localDb.playlistDao().getAllPlaylists()
            refreshAllPlaylists()
        }
        return playlistList!!
    }

    fun getPlaylistById(playlistId: String): LiveData<Playlist> {
        return localDb.playlistDao().getPlaylistById(playlistId)
    }

    fun addPlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        firebaseModel.addPlaylist(playlist) { success ->
            refreshAllPlaylists()
            callback(success)
        }
    }

    fun updatePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        firebaseModel.updatePlaylist(playlist) { success ->
            refreshAllPlaylists()
            callback(success)
        }
    }

    fun deletePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        firebaseModel.deletePlaylist(playlist)
        // Using GlobalScope to run delete operation asynchronously
        GlobalScope.launch {
            localDb.playlistDao().delete(playlist) // Calling the suspend function
            mainHandler.post { callback(true) }
        }
        refreshAllPlaylists()
    }

    private fun refreshAllPlaylists() {
        val localLastUpdated = Playlist.lastUpdated
        firebaseModel.getAllPlaylistsSince(localLastUpdated) { playlists ->
            executor.execute {
                var latestUpdate = localLastUpdated
                GlobalScope.launch {
                    for (playlist in playlists) {
                        localDb.playlistDao().insert(playlist)
                        latestUpdate = maxOf(latestUpdate, playlist.lastUpdated ?: 0)
                    }
                    Playlist.lastUpdated = latestUpdate
                }
            }
        }
    }

    // ------------------------------------- IMAGE ------------------------------------------------
    fun uploadImage(name: String, bitmap: Bitmap, callback: (String?) -> Unit) {
        firebaseModel.uploadImage(name, bitmap, callback)
    }
}
