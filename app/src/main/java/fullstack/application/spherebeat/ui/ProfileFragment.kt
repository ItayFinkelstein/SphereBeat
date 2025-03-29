package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentProfileBinding
import fullstack.application.spherebeat.ui.viewModel.UserViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val userViewModel: UserViewModel by viewModels()


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.Email.text = "Email: " + currentUser?.email

        userViewModel.userList.observe(viewLifecycleOwner, { users ->
            users?.let {
                val selectedUser = users.find { it.email == currentUser?.email }
                binding.username.text = "Username: " + selectedUser?.name

                if (!selectedUser?.avatarUrl.isNullOrEmpty()) {
                    Picasso.get()
                        .load(selectedUser?.avatarUrl)
                        .placeholder(R.drawable.profile_icon)
                        .into(binding.userAvatar)
                }

            }
        })

        return binding.root
    }


}