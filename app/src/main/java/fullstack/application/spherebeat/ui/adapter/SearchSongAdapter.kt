package fullstack.application.spherebeat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentSearchSongBinding
import fullstack.application.spherebeat.databinding.PostLayoutBinding
import fullstack.application.spherebeat.model.Song

class SearchSongAdapter(private var itemList: List<Song>, private val onSearchSongClickListener: OnSearchSongClickListener) : RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder>() {

    interface OnSearchSongClickListener {
        fun onSearchSongClick(song: Song)
    }

//    class SearchSongsViewHolder(itemView: FragmentSearchSongBinding) : RecyclerView.ViewHolder(itemView) {
//        val singerView: TextView = itemView.findViewById(R.id.search_song_singer)
//        val imageView: ImageView = itemView.findViewById(R.id.search_song_image)
//        val textView: TextView = itemView.findViewById(R.id.search_song_text)
//
//        fun bind(song: Song) {
//            textView.text = song.name
//            singerView.text = song.singer
//            // imageView.setImageResource(song.imageResId) // Uncomment and set the image resource if available
//        }
//    }
class SearchSongViewHolder(val binding: FragmentSearchSongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder {
        val binding = FragmentSearchSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSongViewHolder, position: Int) {
        val song = itemList[position]

        holder.binding.searchSongText.text = song.name
        holder.binding.searchSongSinger.text = song.singer
        //holder.binding.searchSongImage.
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