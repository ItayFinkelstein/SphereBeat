package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fullstack.application.spherebeat.databinding.FragmentViewSongBinding

class ViewSongFragment : Fragment() {

    private var _binding: FragmentViewSongBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewSongBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = ViewSongFragmentArgs.fromBundle(requireArguments())
        binding.songTitleTextView.text = args.songName;
        binding.songArtistTextView.text = args.artistName;
        binding.songImage.setImageResource(resources.getIdentifier(args.imageUrl, "drawable", context?.packageName))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
