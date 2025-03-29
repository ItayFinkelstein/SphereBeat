package fullstack.application.spherebeat.ui

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.dal.repository.ImageRepository
import fullstack.application.spherebeat.databinding.FragmentCreatePlaylistBinding
import fullstack.application.spherebeat.model.Playlist
import fullstack.application.spherebeat.model.User
import fullstack.application.spherebeat.ui.viewModel.PlaylistViewModel
import java.util.UUID

class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val playlistViewModel: PlaylistViewModel by viewModels()
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var didSetPlaylistImage = false
    private val imageRepository: ImageRepository = ImageRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        // Initialize camera launcher
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                if (bitmap != null) {
                    binding.plalistImageView.setImageBitmap(bitmap)
                    didSetPlaylistImage = true
                }
            }

        binding.takePhotoButton.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        binding.submitButton.setOnClickListener {
            var newPlaylist : Playlist = createPlaylistFromFields()

            if (didSetPlaylistImage) {
                binding.plalistImageView.isDrawingCacheEnabled = true
                binding.plalistImageView.buildDrawingCache()
                val bitmap = (binding.plalistImageView.drawable as BitmapDrawable).bitmap

                imageRepository.uploadImageBitmap(
                    bitmap = bitmap,
                    name = UUID.randomUUID().toString(),
                    onSuccess = { uri ->
                        if (!uri.isNullOrBlank()) {
                            newPlaylist = createPlaylistFromFields().copy(coverUrl = uri)
                            displayImage(newPlaylist.coverUrl)
                            playlistViewModel.addPlaylist(
                                newPlaylist,
                                callback = {
                                    Log.v("Save playlist", "saved playlist")
                                    findNavController().popBackStack()
                                }
                            )
                        }
                    },
                    onError = {
                        Log.e("CreatePlaylistFragment", "Failed to upload image")
                    }
                )
            } else {
                playlistViewModel.addPlaylist(
                    newPlaylist,
                    callback = {
                        Log.v("Save playlist", "saved playlist")
                        findNavController().popBackStack()
                    }
                )
            }
        }

        return binding.root
    }

    private fun createPlaylistFromFields(): Playlist {
        return Playlist(
            UUID.randomUUID().toString(),
            binding.createPlaylistName.text.toString(),
            "",
            emptyList(),
            emptyList()
        )
    }

    private fun displayImage(avatarUrl: String) {
        if (!avatarUrl.isNullOrEmpty()) {
            Picasso.get()
                .load(avatarUrl)
                .placeholder(R.drawable.elton_john_album)
                .into(binding.plalistImageView)
        }
    }
}
