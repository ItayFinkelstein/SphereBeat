package fullstack.application.spherebeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.RatingBar

class FragmentCreatePost : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment's layout
        val view = inflater.inflate(R.layout.fragment_create_post, container, false)

        // Handle the rating logic here
        val ratingBar = view.findViewById<RatingBar>(R.id.rating_bar)

        // Set a listener if you want to get the rating when it changes
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // Handle rating change
            // The rating is a float value between 0 and 5
            Log.d("Rating", "User selected rating: $rating")
        }

        // Set up the post description input (if needed)
        val postDescription = view.findViewById<EditText>(R.id.post_description)

        return view
    }
}
