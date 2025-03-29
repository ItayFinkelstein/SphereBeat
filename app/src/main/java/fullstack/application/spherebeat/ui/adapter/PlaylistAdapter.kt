package fullstack.application.spherebeat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.PlaylistLayoutBinding
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.ui.viewModel.PlaylistViewModel
import fullstack.application.spherebeat.ui.viewModel.UserViewModel

class PlaylistAdapter(private var itemList: List<Playlist>?, private var onPlaylistClickListener: OnPlaylistClickListener) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {
    private val userViewModel: UserViewModel = UserViewModel()
    private val playlistViewModel: PlaylistViewModel = PlaylistViewModel()

    interface OnPlaylistClickListener {
        fun onPlaylistClick(id: String, name: String, imageUrl: String)
    }

    class PlaylistViewHolder(val binding: PlaylistLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.binding.playlistName.text = item?.name
        holder.binding.playlistImage.setImageResource(R.drawable.taylor_swift_album)

        holder.itemView.setOnClickListener {
            item?.let {
                onPlaylistClickListener.onPlaylistClick(it.id, it.name, it.coverUrl)
            }
        }

        holder.binding.likeButton.setImageResource(
            if (userViewModel.getLoggedUser()?.let { item?.likes?.contains(it.uid) } == true) {
                R.drawable.heart_filled
            } else {
                R.drawable.heart_unfilled
            }
        )

        holder.binding.likeButton.setOnClickListener {
            if (item != null) {
                playlistViewModel.likePlaylist(item, userViewModel.getLoggedUser()?.uid) {
                    holder.binding.likeButton.setImageResource(
                        if (userViewModel.getLoggedUser()?.let { item.likes.contains(it.uid) } == true) {
                            R.drawable.heart_filled
                        } else {
                            R.drawable.heart_unfilled
                        }
                    )
                }
            }
        }
    }

    fun update(playlists: List<Playlist>?) {
        this.itemList = playlists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}