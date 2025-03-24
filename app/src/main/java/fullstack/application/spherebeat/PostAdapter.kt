package fullstack.application.spherebeat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.types.Post

class PostAdapter(private val itemList: List<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

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
        val item = itemList[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.textView.text = item.songName
        holder.singerView.text = item.singer
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
