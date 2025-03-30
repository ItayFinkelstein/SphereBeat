package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.ui.adapter.PlaylistSongsAdapter
import fullstack.application.spherebeat.databinding.FragmentPlaylistSongsBinding
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.model.Song
import fullstack.application.spherebeat.ui.viewModel.PlaylistViewModel
import fullstack.application.spherebeat.ui.viewModel.SongViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistSongsFragment : Fragment(R.layout.playlist_songs_layout) {
    private var _binding: FragmentPlaylistSongsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlaylistSongsAdapter
    private lateinit var itemList: List<Song>
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private val songViewModel: SongViewModel by viewModels()
    private var songs: List<Song> = emptyList()
    private var playlistSongs: List<Song> = emptyList()
    private var playlist: Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistSongsBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val args = PlaylistSongsFragmentArgs.fromBundle(requireArguments())
        val playlistName = args.playlistName

        binding.playlistSongTitle.text = playlistName
        Log.d("PlaylistSongsFragment", "Playlist name: $playlistName")

        if (!args?.image.isNullOrEmpty()) {
            Picasso.get()
                .load(args?.image)
                .placeholder(R.drawable.icons_song)
                .into(binding.topImage)
        } else {
            binding.topImage.setImageResource(R.drawable.icons_song)
        }

        // Sample data
        songViewModel.songList.observe(viewLifecycleOwner, { fetchedSongs ->
            fetchedSongs?.let {
                songs = fetchedSongs;
                playlistSongs = songs.filter { playlist?.songs?.contains(it.id) == true }
                adapter.update(playlistSongs)
            }
        })

        playlistViewModel.playlistList.observe(viewLifecycleOwner, { playlists ->
            playlists?.let {
                playlist = playlists.find {
                    it.id == args.playlistId
                }

                playlistSongs = songs.filter { playlist?.songs?.contains(it.id) == true }
                adapter.update(playlistSongs)
            }
        })

        //adapter = PlaylistSongsAdapter(itemList, this)
        adapter = PlaylistSongsAdapter(playlistSongs, args.playlistId, object : PlaylistSongsAdapter.OnPlaylistSongClickListener {
            override fun onPlaylistSongClick(song: Song) {
                val action = PlaylistSongsFragmentDirections.actionPlaylistSongsFragmentToViewSongFragment(song.id, song.name, song.singer, song.coverUrl)
                findNavController().navigate(action)
            }});
        binding.recyclerView.adapter = adapter
        // Inflate the layout for this fragment
        return binding.root
    }
}