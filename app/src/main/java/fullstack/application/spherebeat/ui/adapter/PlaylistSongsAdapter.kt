package fullstack.application.spherebeat.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.PlaylistLayoutBinding
import fullstack.application.spherebeat.databinding.PlaylistSongsLayoutBinding
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.model.Song
import fullstack.application.spherebeat.ui.adapter.PlaylistAdapter.PlaylistViewHolder
import fullstack.application.spherebeat.ui.viewModel.PlaylistViewModel

class PlaylistSongsAdapter(private var itemList: List<Song>, private val playlistId: String, private val onPlaylistSongClickListener: OnPlaylistSongClickListener) : RecyclerView.Adapter<PlaylistSongsAdapter.PlaylistSongsViewHolder>() {

    interface OnPlaylistSongClickListener {
        fun onPlaylistSongClick(song: Song)
    }

    private val playlistViewModel: PlaylistViewModel = PlaylistViewModel()

    private var playlists: List<Playlist> = emptyList() // To store playlists
    private var currentPlaylist: Playlist? = null

    init {
        // Observe the playlist list from ViewModel
        playlistViewModel.playlistList.observeForever { playlists ->
            this.playlists = playlists ?: emptyList()
            currentPlaylist = playlists?.find { it.id == playlistId }
            notifyDataSetChanged()
        }

    }

    fun update(newSongs: List<Song>) {
        itemList = newSongs
        notifyDataSetChanged()

    // Notify the adapter that the dataset has changed
    }

    class PlaylistSongsViewHolder(val binding: PlaylistSongsLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSongsViewHolder {
        val binding = PlaylistSongsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistSongsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistSongsViewHolder, position: Int) {
        val song = itemList[position]
        holder.binding.playlistSongText.text = song.name
        holder.binding.playlistSongSinger.text = song.singer

        holder.itemView.setOnClickListener {
            onPlaylistSongClickListener.onPlaylistSongClick(song)
        }

        holder.binding.deleteButton.setOnClickListener {
            currentPlaylist?.let {
                playlistViewModel.removeSongFromPlaylist(it, song.id, {
                    if (it) {
                        Log.v("Playlist", "removed song with id " + song.id + " to playlist " + currentPlaylist?.name)
                    } else {
                        Log.w("Playlist", "failed to remove song with id " + song.id + " to playlist " + currentPlaylist?.name)
                    }
                })
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
