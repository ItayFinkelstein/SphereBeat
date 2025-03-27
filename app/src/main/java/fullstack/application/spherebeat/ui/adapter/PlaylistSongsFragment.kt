package fullstack.application.spherebeat.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.model.Song

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlaylistSongsFragment : Fragment(), PlaylistSongsAdapter.OnItemClickListener {
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
        /*itemList = listOf(
            Song(R.drawable.elton_john_album, "Yellow Brick Road", "Elton john"),
            Song(R.drawable.taylor_swift_album, "1989", "Taylor Swift"),
            Song(R.drawable.beatles_album, "Hey Jude", "The Beatles")
        )
*/
        itemList = listOf(
            Song("1", "Yellow Brick Road", "Elton John", 1973, 240, "R.drawable.elton_john_album"),
            Song("2", "1989", "Taylor Swift", 2014, 231, "R.drawable.taylor_swift_album"),
            Song("3", "Hey Jude", "The Beatles", 1968, 431, "R.drawable.beatles_album")
        )

        adapter = PlaylistSongsAdapter(itemList, this)
        recyclerView.adapter = adapter
        // Inflate the layout for this fragment
        return rootView
    }

    override fun onItemClick(song: Song) {
        // Handle the song item click here
        Toast.makeText(context, "Clicked on: ${song.name}", Toast.LENGTH_SHORT).show()
    }
}