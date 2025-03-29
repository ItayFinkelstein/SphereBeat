package fullstack.application.spherebeat.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import fullstack.application.spherebeat.base.ApplicationContext
import fullstack.application.spherebeat.model.Post.Companion

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey
    var id: String,
    val name: String,
    val coverUrl: String,
    val songs: List<String>,
    val likes: List<String>,  // List of user IDs
    val lastUpdated: Long? = null
) {
    companion object {
        var lastUpdated: Long
            get() = ApplicationContext.Globals.context?.getSharedPreferences(
                "TAG",
                Context.MODE_PRIVATE
            )
                ?.getLong(LOCAL_LAST_UPDATED, 0) ?: 0
            set(value) {
                ApplicationContext.Globals.context
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.apply {
                        edit().putLong(LOCAL_LAST_UPDATED, value).apply()
                    }
            }

        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val COVER_URL_KEY = "coverUrl"
        const val SONGS_KEY = "songs"
        const val LIKES_KEY = "likes"
        const val LAST_UPDATED = "lastUpdated"
        const val LOCAL_LAST_UPDATED = "localPlaylistLastUpdated"

        fun fromJSON(json: Map<String, Any>): Playlist {
            val id = json[ID_KEY] as? String ?: ""
            val name = json[NAME_KEY] as? String ?: ""
            val coverUrl = json[COVER_URL_KEY] as? String ?: ""
            val songList = json[SONGS_KEY] as? List<String> ?: emptyList()
            val likes = json[Post.LIKES_KEY] as? List<String> ?: emptyList()
            val timestamp = json[LAST_UPDATED] as? Timestamp
            val lastUpdatedLongTimestamp = timestamp?.toDate()?.time
            return Playlist(id, name, coverUrl, songList, likes, lastUpdatedLongTimestamp)
        }
    }

    fun toJson(): Map<String, Any> {
        return hashMapOf(
            ID_KEY to id,
            NAME_KEY to name,
            COVER_URL_KEY to coverUrl,
            SONGS_KEY to songs,
            LIKES_KEY to likes,
            LAST_UPDATED to FieldValue.serverTimestamp()
        )
    }
}
