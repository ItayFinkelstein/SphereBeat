package fullstack.application.spherebeat.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import fullstack.application.spherebeat.base.ApplicationContext
import fullstack.application.spherebeat.util.Converters

@Entity(tableName = "posts")
@TypeConverters(Converters::class)
data class Post(
    @PrimaryKey
    var id: String,
    val songName: String,
    val singer: String,
    val songReleaseDate: Long,
    val songLength: Int,
    val coverUrl: String,
    val rating: Int,
    val text: String,
    val likes: List<String>,  // List of user IDs
    val lastUpdated: Long? = null
) {
    companion object {
        var lastUpdated: Long
            get() = ApplicationContext.Globals.context?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                ?.getLong(LOCAL_LAST_UPDATED, 0) ?: 0
            set(value) {
                ApplicationContext.Globals.context
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.apply {
                        edit().putLong(LOCAL_LAST_UPDATED, value).apply()
                    }
            }

        const val ID_KEY = "id"
        const val SONG_NAME_KEY = "songName"
        const val SINGER_KEY = "singer"
        const val SONG_RELEASE_DATE_KEY = "songReleaseDate"
        const val SONG_LENGTH_KEY = "songLength"
        const val COVER_URL_KEY = "coverUrl"
        const val RATING_KEY = "rating"
        const val TEXT_KEY = "text"
        const val LIKES_KEY = "likes"
        const val LAST_UPDATED = "lastUpdated"
        const val LOCAL_LAST_UPDATED = "localPostLastUpdated"

        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID_KEY] as? String ?: ""
            val songName = json[SONG_NAME_KEY] as? String ?: ""
            val singer = json[SINGER_KEY] as? String ?: ""
            val songReleaseDate = (json[SONG_RELEASE_DATE_KEY] as? Number)?.toLong() ?: 0L
            val songLength = (json[SONG_LENGTH_KEY] as? Number)?.toInt() ?: 0
            val coverUrl = json[COVER_URL_KEY] as? String ?: ""
            val rating = (json[RATING_KEY] as? Number)?.toInt() ?: 1
            val text = json[TEXT_KEY] as? String ?: ""
            val likes = json[LIKES_KEY] as? List<String> ?: emptyList()
            val timestamp = json[LAST_UPDATED] as? Timestamp
            val lastUpdatedLongTimestamp = timestamp?.toDate()?.time
            return Post(
                id = id,
                songName = songName,
                singer = singer,
                songReleaseDate = songReleaseDate,
                songLength = songLength,
                coverUrl = coverUrl,
                rating = rating,
                text = text,
                likes = likes,
                lastUpdated = lastUpdatedLongTimestamp
            )
        }
    }

    val json: Map<String, Any>
        get() = hashMapOf(
            ID_KEY to id,
            SONG_NAME_KEY to songName,
            SINGER_KEY to singer,
            SONG_RELEASE_DATE_KEY to songReleaseDate,
            SONG_LENGTH_KEY to songLength,
            COVER_URL_KEY to coverUrl,
            RATING_KEY to rating,
            TEXT_KEY to text,
            LIKES_KEY to likes,
            LAST_UPDATED to FieldValue.serverTimestamp()
        )
}
