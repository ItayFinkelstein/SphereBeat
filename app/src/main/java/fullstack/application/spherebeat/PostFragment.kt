package fullstack.application.spherebeat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.adapter.PostAdapter
import fullstack.application.spherebeat.model.Post

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlaylistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var itemList: List<Post>

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

        val rootView = inflater.inflate(R.layout.fragment_lists, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        /*// Sample data
        itemList = listOf(
            Post(
                R.drawable.elton_john_album, "Yellow Brick Road",
                singer = "Elton John",
                text = ""
            ),
            Post(
                R.drawable.taylor_swift_album, "Timeless",
                singer = "Taylor Swift",
                text = ""
            ),
            Post(
                R.drawable.beatles_album, "Yellow Submarine",
                singer = "The Beatles",
                text = ""
            )
        )*/
        // TODO: fix the above code
        itemList = listOf(
            Post(
                id = "1",
                songName = "Yellow Brick Road",
                singer = "Elton John",
                songReleaseDate = 1973,
                songLength = 240,
                coverUrl = "R.drawable.elton_john_album",
                rating = 5,
                text = "",
                likes = listOf()
            ),
            Post(
                id = "2",
                songName = "Timeless",
                singer = "Taylor Swift",
                songReleaseDate = 2014,
                songLength = 231,
                coverUrl = "R.drawable.taylor_swift_album",
                rating = 4,
                text = "",
                likes = listOf()
            ),
            Post(
                id = "3",
                songName = "Yellow Submarine",
                singer = "The Beatles",
                songReleaseDate = 1968,
                songLength = 431,
                coverUrl = "R.drawable.beatles_album",
                rating = 5,
                text = "",
                likes = listOf()
            )
        )

        adapter = PostAdapter(itemList)
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
            PlaylistFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}