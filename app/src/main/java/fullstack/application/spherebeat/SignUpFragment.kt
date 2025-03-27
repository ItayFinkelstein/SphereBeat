package fullstack.application.spherebeat

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import fullstack.application.spherebeat.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseUser

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private var cameraLauncher: ActivityResultLauncher<Void?>? = null
    private var didSetProfileImage = false
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
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

    private fun signUpUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    //Toast.makeText(this, "Sign Up failed.", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in, you can go to a different screen or display user info
            Log.v("Sign up success", "Welcome, ${user.email}")
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        } else {
            // No user signed in, stay on the sign-in screen
            Log.v("Sign up failure", "Please sign in")
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

        signUpUser(binding.EmailTextInput.text.toString(), binding.PasswordTextInput.text.toString())



        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
