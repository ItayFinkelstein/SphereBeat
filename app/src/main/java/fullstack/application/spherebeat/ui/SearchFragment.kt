package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentSearchBinding
import fullstack.application.spherebeat.ui.viewModel.SongViewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val songViewModel: SongViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val searchBar: EditText = binding.searchSearchBar

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.v("Search", "Search text changed: " + s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.button.setOnClickListener {
            val songName = searchBar.text.toString()
            Log.d("Search", "Searching for song: $songName")
            if (songName.isNotEmpty()) {
                songViewModel.fetchSongsFromApi(songName) {
                    Log.v("Search", "Fetched songs from API")
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}