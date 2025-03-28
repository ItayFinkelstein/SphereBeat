package fullstack.application.spherebeat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.model.Post

class PostAdapter(private var itemList: List<Post>?, private var onPostClickListener: OnPostClickListener) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    interface OnPostClickListener {
        fun onPostClick(name: String, singer: String, description: String, rating: Float)
    }
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.post_image)
        val textView: TextView = itemView.findViewById(R.id.post_text)
        val singerView: TextView = itemView.findViewById(R.id.post_singer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_layout, parent, false)
        return PostViewHolder(view)
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = itemList?.get(position)
        // holder.imageView.setImageResource(item.imageResId) TODO: Uncomment this line
        holder.textView.text = item?.songName
        holder.singerView.text = item?.singer
        holder.itemView.setOnClickListener {
            item?.let { it ->
                onPostClickListener.onPostClick(it.songName, it.singer, it.text, it.rating.toFloat()) }
        }
    }

    fun update(posts: List<Post>?) {
        this.itemList = posts
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }
}
