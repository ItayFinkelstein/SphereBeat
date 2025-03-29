package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.ui.adapter.PlaylistSongsAdapter
import fullstack.application.spherebeat.databinding.FragmentPlaylistSongsBinding
import fullstack.application.spherebeat.model.Song

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistSongsFragment : Fragment(R.layout.playlist_songs_layout) {
    private var _binding: FragmentPlaylistSongsBinding? = null
    private val binding get() = _binding!!
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlaylistSongsAdapter
    private lateinit var itemList: List<Song>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        binding.topImage.setImageResource(R.drawable.taylor_swift_album)
        // Sample data
        itemList = listOf(
            Song(
                "1",
                "Yellow Brick Road",
                "Elton John",
                4000,
                2,
                resources.getResourceEntryName(R.drawable.taylor_swift_album)
            ),
            Song("2", "Bad Blood", "Taylor Swift", 5000, 2, resources.getResourceEntryName(R.drawable.taylor_swift_album)),
//            Song(R.drawable.beatles_album, "Hey Jude", "The Beatles")
        )

        //adapter = PlaylistSongsAdapter(itemList, this)
        adapter = PlaylistSongsAdapter(itemList, object : PlaylistSongsAdapter.OnPlaylistSongClickListener {
            override fun onPlaylistSongClick(song: Song) {
                val action = PlaylistSongsFragmentDirections.actionPlaylistSongsFragmentToViewSongFragment(song.id, song.name, song.singer, song.coverUrl)
                findNavController().navigate(action)
            }});
        binding.recyclerView.adapter = adapter
        // Inflate the layout for this fragment
        return binding.root
    }
}