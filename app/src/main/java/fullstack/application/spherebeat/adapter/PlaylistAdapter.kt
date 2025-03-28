package fullstack.application.spherebeat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.adapter.PlaylistSongsAdapter.OnItemClickListener
import fullstack.application.spherebeat.model.Playlist

class PlaylistAdapter(private val itemList: List<Playlist>, private val onPlaylistClickListener: OnPlaylistClickListener) : RecyclerView.Adapter<PlaylistAdapter.ItemViewHolder>() {

    interface OnPlaylistClickListener {
        fun onPlaylistClick(name: String)
    }
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.item_image)
        val textView: TextView = itemView.findViewById(R.id.item_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        //holder.imageView.setImageResource(item.imageResId)
        // TODO: Uncomment the above
        holder.textView.text = item.name
        holder.imageView.setOnClickListener {
            onPlaylistClickListener.onPlaylistClick(item.name)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
