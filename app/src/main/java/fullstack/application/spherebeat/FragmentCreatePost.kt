package fullstack.application.spherebeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.RatingBar
import android.widget.TextView

class FragmentCreatePost : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_post, container, false)

        val starsRatingBar = view.findViewById<RatingBar>(R.id.rating_bar)
        val songView: TextView = view.findViewById(R.id.create_post_song_title_view)
        val singerView: TextView = view.findViewById(R.id.create_post_singer_view)

        songView.text = "Candle in the Wind"
        singerView.text = "Elton John"


        starsRatingBar.setOnRatingBarChangeListener { _, rating, _ ->
            // The rating is a float value between 0 and 5
            Log.d("Rating", "User selected rating: $rating")
        }


        return view
    }
}
