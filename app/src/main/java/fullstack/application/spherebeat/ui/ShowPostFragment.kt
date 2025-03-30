package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentShowPostBinding
import fullstack.application.spherebeat.databinding.FragmentViewSongBinding

class ShowPostFragment : Fragment() {
    private var _binding: FragmentShowPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowPostBinding.inflate(inflater, container, false)
        val args = ShowPostFragmentArgs.fromBundle(requireArguments())

        binding.showPostSongTitleView.text = args.songName
        binding.showPostSingerView.text = args.songArtist
        binding.showPostDescription.text = args.description
        binding.showPostRatingBar.rating = args.rating

        if (args.imageUrl.isNotEmpty()) {
            Picasso.get()
                .load(args.imageUrl)
                .placeholder(R.drawable.icons_song)
                .into(binding.showPostSongImage)
        } else {
            binding.showPostSongImage.setImageResource(R.drawable.icons_song)
        }

        return binding.root
    }
}
