package fullstack.application.spherebeat.ui

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.dal.repository.ImageRepository
import fullstack.application.spherebeat.dal.repository.UserRepository
import fullstack.application.spherebeat.databinding.FragmentEditProfileBinding
import java.util.UUID

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var didSetProfileImage = false
    private val userRepository: UserRepository = UserRepository()
    private val imageRepository: ImageRepository = ImageRepository()
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val args = EditProfileFragmentArgs.fromBundle(requireArguments())
        binding.username.setText(args.userName)

        if (args.image.isNotEmpty()) {
            Picasso.get()
                .load(args.image)
                .placeholder(R.drawable.profile_icon)
                .into(binding.profileImageView)
        } else {
            binding.profileImageView.setImageResource(R.drawable.profile_icon)
        }

        // Initialize camera launcher
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                if (bitmap != null) {
                    binding.profileImageView.setImageBitmap(bitmap)
                    didSetProfileImage = true
                }
            }

        binding.takePhotoButton.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        userId = args.userId

        binding.submit.setOnClickListener {
            onSubmit()
        }

        return binding.root
    }

    private fun onSubmit() {
        userRepository.getUserById(userId!!).observe(viewLifecycleOwner, { user ->
            if (didSetProfileImage) {
                binding.profileImageView.isDrawingCacheEnabled = true
                binding.profileImageView.buildDrawingCache()
                val bitmap = (binding.profileImageView.drawable as BitmapDrawable).bitmap

                imageRepository.uploadImageBitmap(
                    bitmap = bitmap,
                    name = UUID.randomUUID().toString(),
                    onSuccess = { uri ->
                        Log.d("SignUp", "image uri: $uri")
                        if (!uri.isNullOrBlank()) {
                            val updatedUser = user.copy(avatarUrl = uri, name = binding.username.text.toString())
                            userRepository.updateUser(updatedUser) {
                                displayUserAvatar(updatedUser.avatarUrl)
                            }
                        }
                    },
                    onError = {}
                )
            } else {
                val updatedUser = user.copy(name = binding.username.text.toString())
                userRepository.updateUser(updatedUser) {
                    displayUserAvatar(updatedUser.avatarUrl)
                }
            }
        })
        findNavController().popBackStack()
    }

    private fun displayUserAvatar(avatarUrl: String) {
        if (avatarUrl.isNotEmpty()) {
            Picasso.get()
                .load(avatarUrl)
                .placeholder(R.drawable.profile_icon)
                .into(binding.profileImageView)
        }
    }
}