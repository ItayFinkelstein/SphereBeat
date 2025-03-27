package fullstack.application.spherebeat.ui

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import fullstack.application.spherebeat.MainActivity
import fullstack.application.spherebeat.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var didSetProfileImage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize camera launcher
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                binding.profileImageView.setImageBitmap(bitmap)
                didSetProfileImage = true
            }
        }

        binding.takePhotoButton.setOnClickListener {
            cameraLauncher?.launch(null)
        }

        binding.signUpButton.setOnClickListener {
            onSignUp()
        }
    }

    private fun onSignUp() {
        if (didSetProfileImage) {
            binding.profileImageView.isDrawingCacheEnabled = true
            binding.profileImageView.buildDrawingCache()
            val bitmap = (binding.profileImageView.drawable as BitmapDrawable).bitmap

            // TODO: Handle image upload

        } else {
            // TODO: implement
            //createUser()
        }

        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
