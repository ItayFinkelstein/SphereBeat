package fullstack.application.spherebeat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import fullstack.application.spherebeat.util.Converters

@Entity(tableName = "playlists")
data class Playlist(
    @PrimaryKey
    var id: String,
    val name: String,
    val coverUrl: String,
    @TypeConverters(Converters::class)
    val songs: List<Song>, // List of songs
    val lastUpdated: Long? = null
) {
    companion object {
        const val ID_KEY = "id"
        const val NAME_KEY = "name"
        const val COVER_URL_KEY = "coverUrl"
        const val SONGS_KEY = "songs"
        const val LAST_UPDATED = "lastUpdated"

        fun fromJSON(json: Map<String, Any>): Playlist {
            val id = json[ID_KEY] as? String ?: ""
            val name = json[NAME_KEY] as? String ?: ""
            val coverUrl = json[COVER_URL_KEY] as? String ?: ""
            val songList = (json[SONGS_KEY] as? List<Map<String, Any>> ?: emptyList())
                .map { Song.fromJSON(it) }
            val timestamp = json[LAST_UPDATED] as? Timestamp
            val lastUpdatedLongTimestamp = timestamp?.toDate()?.time
            return Playlist(id, name, coverUrl, songList, lastUpdatedLongTimestamp)
        }
    }

    val json: Map<String, Any>
        get() = hashMapOf(
            ID_KEY to id,
            NAME_KEY to name,
            COVER_URL_KEY to coverUrl,
            SONGS_KEY to songs.map { it.json },
            LAST_UPDATED to FieldValue.serverTimestamp()
        )
}
