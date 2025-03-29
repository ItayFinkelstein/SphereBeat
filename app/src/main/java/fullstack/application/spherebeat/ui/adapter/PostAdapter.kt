package fullstack.application.spherebeat.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.dal.repository.PostRepository
import fullstack.application.spherebeat.dal.repository.UserRepository
import fullstack.application.spherebeat.databinding.PostLayoutBinding
import fullstack.application.spherebeat.model.Post

class PostAdapter(private var itemList: List<Post>?, private var onPostClickListener: OnPostClickListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val userRepository: UserRepository = UserRepository()
    private val postRepository: PostRepository = PostRepository()

    interface OnPostClickListener {
        fun onPostClick(name: String, singer: String, description: String, rating: Float)
        fun onEditPostClick(name: String, singer: String, description: String, rating: Float)
    }
    class PostViewHolder(val binding: PostLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = itemList?.get(position)
        //holder.binding.post.text = item?.songName
        holder.binding.postSinger.text = item?.singer
        holder.binding.postImage.setImageResource(R.drawable.icons_song)
        holder.binding.rating.text = "${item?.rating.toString()} / 5"
        holder.binding.postText.text = item?.text

        holder.binding.likeButton.setImageResource(
            if (userRepository.getLoggedUser()?.let { item?.likes?.contains(it.uid) } == true) {
                R.drawable.heart_filled
            } else {
                R.drawable.heart_unfilled
            }
        )

        holder.binding.likeButton.setOnClickListener {
            if (item != null) {
                postRepository.likePost(item, userRepository.getLoggedUser()?.uid) {
                    holder.binding.likeButton.setImageResource(
                        if (userRepository.getLoggedUser()?.let { item.likes.contains(it.uid) } == true) {
                            R.drawable.heart_filled
                        } else {
                            R.drawable.heart_unfilled
                        }
                    )
                }
            }
        }

        holder.itemView.setOnClickListener {
            item?.let { it ->
                onPostClickListener.onPostClick(it.songName, it.singer, it.text, it.rating.toFloat()) }
        }

        holder.binding.postEditButton.setOnClickListener {
            item?.let { it ->
                onPostClickListener.onEditPostClick(
                    it.songName,
                    it.singer,
                    it.text,
                    it.rating.toFloat()
                )
            }
        }
    }

    fun update(posts: List<Post>?) {
        this.itemList = posts
        Log.d("PostAdapter", "update: ${posts?.size}")
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}