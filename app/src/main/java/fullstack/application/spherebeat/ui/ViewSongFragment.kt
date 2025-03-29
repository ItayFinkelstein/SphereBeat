package fullstack.application.spherebeat.ui

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import fullstack.application.spherebeat.databinding.FragmentViewSongBinding
import fullstack.application.spherebeat.ui.viewModel.PlaylistViewModel

class ViewSongFragment : Fragment() {

    private var _binding: FragmentViewSongBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve arguments
        val args = ViewSongFragmentArgs.fromBundle(requireArguments())
        binding.songTitleTextView.text = args.songName
        binding.songArtistTextView.text = args.artistName
        binding.songImage.setImageResource(resources.getIdentifier(args.imageUrl, "drawable", context?.packageName))

        playlistViewModel.playlistList.observe(viewLifecycleOwner, Observer { playlists ->
            playlists?.let {
                val playlistSpinnerAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.simple_spinner_dropdown_item,
                    it.map { playlist -> playlist.name } // Assuming Playlist has a 'name' property
                )
                binding.playlistSpinner.adapter = playlistSpinnerAdapter
            }
        })

        binding.createPostButton.setOnClickListener {
            val action = ViewSongFragmentDirections.actionViewSongFragmentToCreatePostFragment(
                rating = 0.0F,
                songName = args.songName,
                songArtist = args.artistName,
            );
            findNavController().navigate(action)
        }
        binding.addSongButton.setOnClickListener {
            val selectedPlaylistName = binding.playlistSpinner.selectedItem as? String

            if (selectedPlaylistName != null) {
                // Find the selected playlist object by name
                val selectedPlaylist = playlistViewModel.playlistList.value?.find {
                    it.name == selectedPlaylistName
                }

                if (selectedPlaylist != null) {
                    playlistViewModel.addSongToPlaylist(selectedPlaylist, args.songId, {
                        if (it) {
                            Log.v("Playlist", "added song with id " + args.songId + " to playlist " + selectedPlaylist.name)
                        } else {
                            Log.w("Playlist", "failed to add song with id " + args.songId + " to playlist " + selectedPlaylist.name)
                        }
                    })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
