package fullstack.application.spherebeat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fullstack.application.spherebeat.MainActivity
import com.google.firebase.auth.FirebaseUser
import fullstack.application.spherebeat.dal.repository.UserRepository
import fullstack.application.spherebeat.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userRepository: UserRepository = UserRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun signInUser(email: String, password: String) {
        userRepository.login(email, password) { success, user ->
            if (success) {
                updateUI(user)
            } else {
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Log.v("Login success", "\"Welcome, ${user.email}\"")
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        } else {
            // No user signed in, stay on the sign-in screen
            Log.v("Login error", "Please sign in")

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            signInUser(binding.EmailTextInput.text.toString(), binding.PasswordTextInput.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}