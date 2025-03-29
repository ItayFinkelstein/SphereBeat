package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentCreatePlaylistBinding
import fullstack.application.spherebeat.databinding.FragmentPlaylistSongsBinding

class CreatePlaylistFragment : Fragment() {
    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
}
