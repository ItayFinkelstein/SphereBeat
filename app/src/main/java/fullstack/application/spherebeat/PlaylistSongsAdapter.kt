package fullstack.application.spherebeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.types.Song

class PlaylistSongsAdapter(private val itemList: List<Song>) : RecyclerView.Adapter<PlaylistSongsAdapter.PlaylistSongsViewHolder>() {

    class PlaylistSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val singerView: TextView  = itemView.findViewById(R.id.playlist_song_singer)
        val imageView: ImageView = itemView.findViewById(R.id.playlist_song_image)
        val textView: TextView = itemView.findViewById(R.id.playlist_song_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_songs_layout, parent, false)
        return PlaylistSongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSongsViewHolder, position: Int) {
        val item = itemList[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.textView.text = item.songName
        holder.singerView.text = item.singer
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
