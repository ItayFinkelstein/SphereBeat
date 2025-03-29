package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentPostBinding
import fullstack.application.spherebeat.ui.adapter.PostAdapter
import fullstack.application.spherebeat.model.Post
import fullstack.application.spherebeat.ui.viewModel.PostViewModel

class PostFragment : Fragment() {
    private var binding: FragmentPostBinding? = null
    private lateinit var adapter: PostAdapter
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        val rootView = binding!!.root

        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(emptyList()) // Initialize with an empty list
        binding!!.recyclerView.adapter = adapter

        postViewModel.postList.observe(viewLifecycleOwner, { posts ->
            adapter.update(posts)

            binding?.progressBar?.visibility = View.GONE
        })
        adapter = PostAdapter(postViewModel.postList.value, object : PostAdapter.OnPostClickListener {
            override fun onPostClick(name: String, singer: String, description: String, rating: Float) {
                val action = PostFragmentDirections.actionPostFragmentToShowPostFragment(name, singer, description,
                    rating
                )
                findNavController().navigate(action)
            }
            override fun onEditPostClick(name: String, singer: String, description: String, rating: Float) {
                val action = PostFragmentDirections.actionPostFragmentToCreatePostFragment( rating, name, singer, description,

                )
                findNavController().navigate(action)
            }

        });

        binding.recyclerView.adapter = adapter

        binding.createPostButton.setOnClickListener {
            val action = PostFragmentDirections.actionPostFragmentToCreatePostFragment(6.0F)
            findNavController().navigate(action)
        }

        binding?.swipeToRefresh?.setOnRefreshListener {
            postViewModel.refresh()
        }

        postViewModel.loadingState.observe(viewLifecycleOwner) { state ->
            binding?.swipeToRefresh?.isRefreshing = state == PostViewModel.LoadingState.LOADING
        }

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        getAllPosts()
    }

    private fun getAllPosts() {
        binding?.progressBar?.visibility = View.VISIBLE
        postViewModel.refresh()
    }
}