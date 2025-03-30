package fullstack.application.spherebeat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentSearchSongBinding
import fullstack.application.spherebeat.databinding.PostLayoutBinding
import fullstack.application.spherebeat.model.Song

class SearchSongAdapter(private var itemList: List<Song>, private val onSearchSongClickListener: OnSearchSongClickListener) : RecyclerView.Adapter<SearchSongAdapter.SearchSongViewHolder>() {

    interface OnSearchSongClickListener {
        fun onSearchSongClick(song: Song)
    }

class SearchSongViewHolder(val binding: FragmentSearchSongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSongViewHolder {
        val binding = FragmentSearchSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchSongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchSongViewHolder, position: Int) {
        val song = itemList[position]

        holder.binding.searchSongText.text = song.name
        holder.binding.searchSongSinger.text = song.singer

        if (!song.coverUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(song.coverUrl)
                .placeholder(R.drawable.icons_song)
                .into(holder.binding.searchSongImage)
        } else {
            holder.binding.searchSongImage.setImageResource(R.drawable.icons_song)
        }
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