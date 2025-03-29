package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import fullstack.application.spherebeat.databinding.FragmentCreatePlaylistBinding
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.ui.viewModel.PlaylistViewModel

class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        binding.submitButton.setOnClickListener {
            playlistViewModel.addPlaylist(
                Playlist(
                    null.toString(),
                    binding.createPlaylistName.text.toString(),
                    binding.createPlaylistImage.toString(),
                    emptyList(),
                    emptyList()
                ),
                callback = {
                    Log.v("Save playlist", "saved playlist")
                    findNavController().popBackStack()
                }
            )
        }
        return binding.root
    }
}
