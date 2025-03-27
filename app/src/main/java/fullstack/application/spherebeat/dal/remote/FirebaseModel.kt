package fullstack.application.spherebeat.dal.remote

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.storage.FirebaseStorage
import fullstack.application.spherebeat.base.Constants
import fullstack.application.spherebeat.model.*
import java.io.ByteArrayOutputStream

class FirebaseModel {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    init {
        val settings = firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }
        database.firestoreSettings = settings
    }

    // --------------------------------------- USER AUTH ----------------------------------------------
    fun getLoggedUserId(): String? {
        return auth.currentUser?.uid
    }

    fun signUp(newUser: User, password: String, callback: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(newUser.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    newUser.id = auth.currentUser?.uid ?: ""
                    saveUserToDB(newUser) { callback(true) }
                } else {
                    callback(false)
                }
            }
    }

    fun logIn(email: String, password: String, callback: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> callback(task.isSuccessful) }
    }

    fun logOut() {
        auth.signOut()
    }

    // --------------------------------------- USERS ----------------------------------------------
    private fun saveUserToDB(user: User, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.USERS_COLLECTION).document(user.id).set(user)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updateUser(user: User, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.USERS_COLLECTION).document(user.id).set(user)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun getAllUsersSince(lastUpdated: Long, callback: (List<User>) -> Unit) {
        database.collection(Constants.Collections.USERS_COLLECTION)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.toObjects(User::class.java)
                callback(users)
            }
    }

    // --------------------------------------- POSTS ----------------------------------------------
    fun getAllPostsSince(lastUpdated: Long, callback: (List<Post>) -> Unit) {
        database.collection(Constants.Collections.POSTS_COLLECTION)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnSuccessListener { snapshot ->
                val posts = snapshot.toObjects(Post::class.java)
                callback(posts)
            }
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        val postRef = database.collection(Constants.Collections.POSTS_COLLECTION).document()
        post.id = postRef.id
        postRef.set(post)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.POSTS_COLLECTION).document(post.id).set(post)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.POSTS_COLLECTION).document(post.id).delete()
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    // --------------------------------------- PLAYLISTS ----------------------------------------------
    fun getAllPlaylistsSince(lastUpdated: Long, callback: (List<Playlist>) -> Unit) {
        database.collection(Constants.Collections.PLAYLISTS_COLLECTION)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnSuccessListener { snapshot ->
                val playlists = snapshot.toObjects(Playlist::class.java)
                callback(playlists)
            }
    }

    fun addPlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        val playlistRef = database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document()
        playlist.id = playlistRef.id
        playlistRef.set(playlist)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun updatePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlist.id).set(playlist)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun deletePlaylist(playlist: Playlist, callback: (Boolean) -> Unit) {
        database.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlist.id).delete()
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
}
