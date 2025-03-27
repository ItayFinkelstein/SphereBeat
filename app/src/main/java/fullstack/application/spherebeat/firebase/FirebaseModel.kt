package fullstack.application.spherebeat.firebase

import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import fullstack.application.spherebeat.base.Constants
import fullstack.application.spherebeat.model.*
import java.io.ByteArrayOutputStream

class FirebaseModel {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    // --------------------------------------- USER AUTH ----------------------------------------------
    fun getLoggedUserId(): String? {
        return auth.currentUser?.uid
    }

    fun signUp(newUser: User, password: String, listener: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(newUser.email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    newUser.id = auth.currentUser?.uid ?: ""
                    saveUserToDB(newUser) { listener(true) }
                } else {
                    listener(false)
                }
            }
    }

    fun logIn(email: String, password: String, listener: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task -> listener(task.isSuccessful) }
    }

    fun logOut() {
        auth.signOut()
    }

    // --------------------------------------- USERS ----------------------------------------------
    private fun saveUserToDB(user: User, listener: (Boolean) -> Unit) {
        db.collection(Constants.Collections.USERS_COLLECTION).document(user.id).set(user)
            .addOnSuccessListener { listener(true) }
            .addOnFailureListener { listener(false) }
    }

    fun updateUser(user: User, listener: (Boolean) -> Unit) {
        db.collection(Constants.Collections.USERS_COLLECTION).document(user.id).set(user)
            .addOnSuccessListener { listener(true) }
            .addOnFailureListener { listener(false) }
    }

    fun getAllUsersSince(lastUpdated: Long, listener: (List<User>) -> Unit) {
        db.collection(Constants.Collections.USERS_COLLECTION)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.toObjects(User::class.java)
                listener(users)
            }
    }

    // --------------------------------------- POSTS ----------------------------------------------
    fun getAllPostsSince(lastUpdated: Long, listener: (List<Post>) -> Unit) {
        db.collection(Constants.Collections.POSTS_COLLECTION)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnSuccessListener { snapshot ->
                val posts = snapshot.toObjects(Post::class.java)
                listener(posts)
            }
    }

    fun addPost(post: Post, listener: (Boolean) -> Unit) {
        val postRef = db.collection(Constants.Collections.POSTS_COLLECTION).document()
        post.id = postRef.id
        postRef.set(post)
            .addOnSuccessListener { listener(true) }
            .addOnFailureListener { listener(false) }
    }

    fun updatePost(post: Post, listener: (Boolean) -> Unit) {
        db.collection(Constants.Collections.POSTS_COLLECTION).document(post.id).set(post)
            .addOnSuccessListener { listener(true) }
            .addOnFailureListener { listener(false) }
    }

    fun deletePost(post: Post) {
        db.collection(Constants.Collections.POSTS_COLLECTION).document(post.id).delete()
    }

    // --------------------------------------- PLAYLISTS ----------------------------------------------
    fun getAllPlaylistsSince(lastUpdated: Long, listener: (List<Playlist>) -> Unit) {
        db.collection(Constants.Collections.PLAYLISTS_COLLECTION)
            .whereGreaterThan("lastUpdated", lastUpdated)
            .get()
            .addOnSuccessListener { snapshot ->
                val playlists = snapshot.toObjects(Playlist::class.java)
                listener(playlists)
            }
    }

    fun addPlaylist(playlist: Playlist, listener: (Boolean) -> Unit) {
        val playlistRef = db.collection(Constants.Collections.PLAYLISTS_COLLECTION).document()
        playlist.id = playlistRef.id
        playlistRef.set(playlist)
            .addOnSuccessListener { listener(true) }
            .addOnFailureListener { listener(false) }
    }

    fun updatePlaylist(playlist: Playlist, listener: (Boolean) -> Unit) {
        db.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlist.id).set(playlist)
            .addOnSuccessListener { listener(true) }
            .addOnFailureListener { listener(false) }
    }

    fun deletePlaylist(playlist: Playlist) {
        db.collection(Constants.Collections.PLAYLISTS_COLLECTION).document(playlist.id).delete()
    }

    fun uploadImage(name: String, bitmap: Bitmap, listener: (String?) -> Unit) {
        val storageRef = storage.reference.child("images/$name.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        storageRef.putBytes(data)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    listener(uri.toString())
                }
            }
            .addOnFailureListener { listener(null) }
    }
}
