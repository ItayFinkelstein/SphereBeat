package fullstack.application.spherebeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.model.Song

class PlaylistSongsAdapter(private val itemList: List<Song>) : RecyclerView.Adapter<PlaylistSongsAdapter.PlaylistSongsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(song: Song)
    }



    class PlaylistSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val singerView: TextView  = itemView.findViewById(R.id.playlist_song_singer)
        val imageView: ImageView = itemView.findViewById(R.id.playlist_song_image)
        val textView: TextView = itemView.findViewById(R.id.playlist_song_text)
        fun bind(song: Song) {
            //imageView.setImageResource(song.imageResId) TODO: Uncomment this line
            textView.text = song.name
            singerView.text = song.singer

            // Set the click listener
            itemView.setOnClickListener {
                //listener.onItemClick(song) // Call the listener's method when an item is clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_songs_layout, parent, false)
        return PlaylistSongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSongsViewHolder, position: Int) {
        val song = itemList[position]
        holder.bind(song)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
