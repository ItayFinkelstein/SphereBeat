package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FieldValue
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.dal.repository.PostRepository
import fullstack.application.spherebeat.databinding.FragmentCreatePostBinding
import fullstack.application.spherebeat.databinding.FragmentLoginBinding
import fullstack.application.spherebeat.databinding.FragmentViewSongBinding
import fullstack.application.spherebeat.model.Post
import fullstack.application.spherebeat.ui.viewModel.PostViewModel

class FragmentCreatePost : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: PostViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        val args = FragmentCreatePostArgs.fromBundle(requireArguments())

        binding.createPostSongTitleView.setText(args.songName)
        binding.createPostSingerView.setText(args.songArtist)
        binding.ratingBar.rating = args.rating
        binding.postDescription.setText(args.description)


        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // The rating is a float value between 0 and 5
            Log.d("Rating", "User selected rating: $rating")
        }

        binding.createPostSubmitButton.setOnClickListener {
            val post = Post(
                args.postId,
                binding.createPostSongTitleView.text.toString(),
                binding.createPostSingerView.text.toString(),
                90,
                8,
                "",
                binding.ratingBar.rating.toInt(),
                text = binding.postDescription.text.toString(),
                likes = emptyList(),
                lastUpdated = System.currentTimeMillis()
            )
            if (args.postId != "") {
                postViewModel.updatePost(
                    post,
                    {
                        Log.v("Update post", "updated post")
                        findNavController().popBackStack()
                    },
                )
            } else {
                postViewModel.addPost(
                    post,
                    {
                        Log.v("Save post", "saved post")
                        findNavController().popBackStack()
                    },
                )
            }
        }


        return binding.root
    }
}
