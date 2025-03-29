package fullstack.application.spherebeat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentPlaylistSongsBinding
import fullstack.application.spherebeat.databinding.FragmentSearchSongBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchSongFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchSongFragment : Fragment() {
    private var _binding: FragmentSearchSongBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchSongBinding.inflate(inflater, container, false)

//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        val args = PlaylistSongsFragmentArgs.fromBundle(requireArguments())
//        val playlistName = args.playlistName
        return inflater.inflate(R.layout.fragment_search_song, container, false)
    }
}