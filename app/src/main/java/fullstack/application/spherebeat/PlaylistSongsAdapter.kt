package fullstack.application.spherebeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.types.Song

class PlaylistSongsAdapter(private val itemList: List<Song>, private val listener: OnItemClickListener) : RecyclerView.Adapter<PlaylistSongsAdapter.PlaylistSongsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(song: Song)
    }

    class PlaylistSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val singerView: TextView  = itemView.findViewById(R.id.playlist_song_singer)
        val imageView: ImageView = itemView.findViewById(R.id.playlist_song_image)
        val textView: TextView = itemView.findViewById(R.id.playlist_song_text)

        fun bind(song: Song, listener: OnItemClickListener) {
            imageView.setImageResource(song.imageResId)
            textView.text = song.songName
            singerView.text = song.singer

            // Set the click listener
            itemView.setOnClickListener {
                listener.onItemClick(song) // Call the listener's method when an item is clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_songs_layout, parent, false)
        return PlaylistSongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSongsViewHolder, position: Int) {
        val song = itemList[position]
        holder.bind(song, listener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
