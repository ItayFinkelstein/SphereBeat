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
import fullstack.application.spherebeat.databinding.FragmentCreatePostBinding
import fullstack.application.spherebeat.databinding.FragmentLoginBinding
import fullstack.application.spherebeat.databinding.FragmentViewSongBinding

class FragmentCreatePost : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        val args = FragmentCreatePostArgs.fromBundle(requireArguments())

        binding.createPostSongTitleView.setText(args.songName)
        binding.createPostSingerView.setText(args.songArtist)
        binding.ratingBar.rating = args.rating


        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // The rating is a float value between 0 and 5
            Log.d("Rating", "User selected rating: $rating")
        }


        return binding.root
    }
}
