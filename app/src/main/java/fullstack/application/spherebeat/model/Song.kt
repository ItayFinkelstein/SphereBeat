package fullstack.application.spherebeat.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import fullstack.application.spherebeat.base.ApplicationContext

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey
    var id: String,
    val name: String,
    val singer: String,
    val releaseDate: Long,
    val length: Int,
    val coverUrl: String,
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
        const val NAME_KEY = "name"
        const val SINGER_KEY = "singer"
        const val RELEASE_DATE_KEY = "releaseDate"
        const val LENGTH_KEY = "length"
        const val COVER_URL_KEY = "coverUrl"
        const val LAST_UPDATED = "lastUpdated"
        const val LOCAL_LAST_UPDATED = "localSongLastUpdated"

        fun fromJSON(json: Map<String, Any>): Song {
            val id = json[ID_KEY] as? String ?: ""
            val name = json[NAME_KEY] as? String ?: ""
            val singer = json[SINGER_KEY] as? String ?: ""
            val releaseDate = (json[RELEASE_DATE_KEY] as? Timestamp)?.toDate()?.time ?: 0
            val length = (json[LENGTH_KEY] as? Number)?.toInt() ?: 0
            val coverUrl = json[COVER_URL_KEY] as? String ?: ""
            val timestamp = json[LAST_UPDATED] as? Timestamp
            val lastUpdatedLongTimestamp = timestamp?.toDate()?.time
            return Song(id, name, singer, releaseDate, length, coverUrl, lastUpdatedLongTimestamp)
        }
    }

    val json: Map<String, Any>
        get() = hashMapOf(
            ID_KEY to id,
            NAME_KEY to name,
            SINGER_KEY to singer,
            RELEASE_DATE_KEY to releaseDate,
            LENGTH_KEY to length,
            COVER_URL_KEY to coverUrl,
            LAST_UPDATED to FieldValue.serverTimestamp()
        )
}
