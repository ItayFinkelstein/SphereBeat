package fullstack.application.spherebeat.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fullstack.application.spherebeat.model.Song

object Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromSongList(value: String): List<Song> {
        val type = object : TypeToken<List<Song>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun toSongList(list: List<Song>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>): String {
        return gson.toJson(list)
    }
}
