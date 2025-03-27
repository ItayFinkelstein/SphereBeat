package fullstack.application.spherebeat.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import fullstack.application.spherebeat.base.ApplicationContext

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    var id: String,
    val email: String,
    val name: String,
    val avatarUrl: String,
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
        const val EMAIL_KEY = "email"
        const val NAME_KEY = "name"
        const val AVATAR_URL_KEY = "avatarUrl"
        const val LAST_UPDATED = "lastUpdated"
        const val LOCAL_LAST_UPDATED = "localUserLastUpdated"

        fun fromJSON(json: Map<String, Any>): User {
            val id = json[ID_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""
            val name = json[NAME_KEY] as? String ?: ""
            val avatarUrl = json[AVATAR_URL_KEY] as? String ?: ""
            val timestamp = json[LAST_UPDATED] as? Timestamp
            val lastUpdatedLongTimestamp = timestamp?.toDate()?.time
            return User(
                id = id,
                email = email,
                name = name,
                avatarUrl = avatarUrl,
                lastUpdated = lastUpdatedLongTimestamp
            )
        }
    }

    val json: Map<String, Any>
        get() = hashMapOf(
            ID_KEY to id,
            EMAIL_KEY to email,
            NAME_KEY to name,
            AVATAR_URL_KEY to avatarUrl,
            LAST_UPDATED to FieldValue.serverTimestamp()
        )

}
