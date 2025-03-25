package fullstack.application.spherebeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.types.Playlist
import fullstack.application.spherebeat.types.Song

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistSongsFragment : Fragment() {
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

        val rootView = inflater.inflate(R.layout.fragment_playlist_songs, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Sample data
        itemList = listOf(
            Song(R.drawable.elton_john_album, "Yellow Brick Road", "Elton john"),
            Song(R.drawable.taylor_swift_album, "1989", "Taylor Swift"),
            Song(R.drawable.beatles_album, "Hey Jude","The Beatles")
        )

        adapter = PlaylistSongsAdapter(itemList)
        recyclerView.adapter = adapter
        // Inflate the layout for this fragment
        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}