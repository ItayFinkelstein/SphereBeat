package fullstack.application.spherebeat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.model.Song

class SearchSongsAdapter(private var itemList: List<Song>, private val onSearchSongClickListener: OnSearchSongClickListener) : RecyclerView.Adapter<SearchSongsAdapter.SearchSongsViewHolder>() {

    interface OnSearchSongClickListener {
        fun onSearchSongClick(song: Song)
    }

    class SearchSongsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val singerView: TextView = itemView.findViewById(R.id.search_song_singer)
        val imageView: ImageView = itemView.findViewById(R.id.search_song_image)
        val textView: TextView = itemView.findViewById(R.id.search_song_text)

        fun bind(song: Song) {
            textView.text = song.name
            singerView.text = song.singer
            // imageView.setImageResource(song.imageResId) // Uncomment and set the image resource if available
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SearchSongsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchSongsViewHolder, position: Int) {
        val song = itemList[position]
        holder.bind(song)
        holder.itemView.setOnClickListener {
            onSearchSongClickListener.onSearchSongClick(song)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun update(newItemList: List<Song>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}