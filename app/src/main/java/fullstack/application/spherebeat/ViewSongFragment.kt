package fullstack.application.spherebeat

import fullstack.application.spherebeat.model.Song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SongFragment : Fragment() {


    // Create a binding object

    // Song data that will be passed to the fragment
    private lateinit var song: Song

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_view_song, container, false)

        // Find views using findViewById
        val songTitleTextView = view.findViewById<TextView>(R.id.song_title_text_view)
        val songArtistTextView = view.findViewById<TextView>(R.id.song_artist_text_view)

        // Set some sample data
        songTitleTextView.text = "Sample Song Title"
        songArtistTextView.text = "Sample Artist"

        return view
    }


/*    companion object {
        *//**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewSongFragment.
         *//*
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewSongFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/
}
