package fullstack.application.spherebeat.dal.remote

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.storage.FirebaseStorage
import fullstack.application.spherebeat.base.Constants
import fullstack.application.spherebeat.model.*
import fullstack.application.spherebeat.util.toFirebaseTimestamp
import java.io.ByteArrayOutputStream

class FirebaseModel {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    init {
        database = FirebaseFirestore.getInstance()
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings { })
        }
        database.firestoreSettings = settings
    }

    companion object {
        @Volatile private var instance: FirebaseModel? = null

        fun getInstance(): FirebaseModel =
            instance ?: synchronized(this) {
                instance ?: FirebaseModel().also { instance = it }
            }
    }

    // --------------------------------------- USER AUTH ----------------------------------------------
    fun getLoggedUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signUp(newUser: User, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(newUser.email, newUser.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    newUser.id = auth.currentUser?.uid ?: ""
                    saveUserToDB(newUser) { callback(true) }
                } else {
                    callback(false)
                }
            }
    }

    fun logIn(email: String, password: String, callback: (Boolean, FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val newUser = User (
                            id = it.uid,
                            email = it.email ?: "",
                            name = it.displayName ?: "",
                            password = password,
                            avatarUrl = ""
                        )
                        saveUserToDB(newUser) { success ->
                            callback(success, user)
                        }
                    } ?: callback(false, null)
                } else {
                    callback(false, null)
                }
            }
    }

    fun logOut( callback: (Boolean) -> Unit) {
        auth.signOut()
        callback(true)
    }

    // --------------------------------------- USERS ----------------------------------------------
    private fun saveUserToDB(user: User, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.USERS_COLLECTION).document(user.id).set(user.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.USERS_COLLECTION).document(user.id).set(user.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun getAllUsersSince(lastUpdated: Long, callback: (List<User>) -> Unit) {
        database.collection(Constants.Collections.USERS_COLLECTION)
            .whereGreaterThanOrEqualTo(User.LAST_UPDATED, lastUpdated.toFirebaseTimestamp)
            .get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val users: MutableList<User> = mutableListOf()
                        for (json in it.result) {
                            users.add(User.fromJSON(json.data))
                        }
                        callback(users)
                    }

                    false -> callback(listOf())
                }
            }
    }

    // --------------------------------------- POSTS ----------------------------------------------
    fun getAllPostsSince(lastUpdated: Long, callback: (List<Post>) -> Unit) {
        database.collection(Constants.Collections.POSTS_COLLECTION)
            .whereGreaterThanOrEqualTo(Post.LAST_UPDATED, lastUpdated.toFirebaseTimestamp)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                        val posts: MutableList<Post> = mutableListOf()

                        for (json in it.result) {
                            posts.add(Post.fromJSON(json.data))
                        }
                        callback(posts)
                } else {
                    callback(listOf())
                }
            }
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        val postRef = database.collection(Constants.Collections.POSTS_COLLECTION).document()
        post.id = postRef.id
        postRef.set(post.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.POSTS_COLLECTION).document(post.id).set(post.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updatePostLikes(postId: String, likes: List<String>, callback: (Boolean) -> Unit) {
        val postRef = database.collection(Constants.Collections.POSTS_COLLECTION).document(postId)
        postRef.update("likes", likes)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    fun deletePost(id: String, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.POSTS_COLLECTION).document(id).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // --------------------------------------- PLAYLISTS ----------------------------------------------
    fun getAllPlaylistsSince(lastUpdated: Long, callback: (List<Playlist>) -> Unit) {
        database.collection(Constants.Collections.PLAYLISTS_COLLECTION)
            .whereGreaterThanOrEqualTo(Playlist.LAST_UPDATED, lastUpdated.toFirebaseTimestamp)
            .get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val playlists: MutableList<Playlist> = mutableListOf()
                        for (json in it.result) {
                            playlists.add(Playlist.fromJSON(json.data))
                        }
                        callback(playlists)
                    }

                    false -> callback(listOf())
                }
            }
    }

    fun addPlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        val playlistRef = database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document()
        playlist.id = playlistRef.id
        playlistRef.set(playlist.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updatePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlist.id)
            .set(playlist.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updatePlaylistLikes(playlistId: String, likes: List<String>, callback: (Boolean) -> Unit) {
        val playlistRef = database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlistId)
        playlistRef.update("likes", likes)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    fun updatePlaylistSongs(playlistId: String, songs: List<String>, callback: (Boolean) -> Unit) {
        val playlistRef = database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlistId)
        playlistRef.update("songs", songs)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
    }

    fun deletePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlist.id)
            .delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun deletePlaylist(id: String, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(id).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun uploadImage(name: String, bitmap: Bitmap, callback: (String?) -> Unit) {
        val storageRef = storage.reference.child("images/$name.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        storageRef.putBytes(data)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    callback(uri.toString())
                }
            }
            .addOnFailureListener { callback(null) }
    }

    // --------------------------------------- SONGS ----------------------------------------------
    fun getAllSongsSince(lastUpdated: Long, callback: (List<Song>) -> Unit) {
        database.collection(Constants.Collections.SONGS_COLLECTION)
            .whereGreaterThanOrEqualTo(Song.LAST_UPDATED, lastUpdated.toFirebaseTimestamp)
            .get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val songs: MutableList<Song> = mutableListOf()
                        for (json in it.result) {
                            songs.add(Song.fromJSON(json.data))
                        }
                        callback(songs)
                    }

                    false -> callback(listOf())
                }
            }
    }

    fun addSong(song: Song, callback: (Boolean) -> Unit) {
        val songRef = database.collection(Constants.Collections.SONGS_COLLECTION).document()
        song.id = songRef.id
        songRef.set(song.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updateSong(song: Song, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.SONGS_COLLECTION).document(song.id).set(song.toJson())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun deleteSong(song: Song, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.SONGS_COLLECTION).document(song.id).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }
}
