package fullstack.application.spherebeat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import fullstack.application.spherebeat.R
import fullstack.application.spherebeat.databinding.FragmentListsBinding
import fullstack.application.spherebeat.ui.adapter.PlaylistAdapter
import fullstack.application.spherebeat.ui.viewModel.PlaylistViewModel

class PlaylistFragment : Fragment() {
    private var binding: FragmentListsBinding? = null
    private lateinit var adapter: PlaylistAdapter
    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListsBinding.inflate(inflater, container, false)
        val rootView = binding!!.root

        binding!!.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PlaylistAdapter(playlistViewModel.playlistList.value, object : PlaylistAdapter.OnPlaylistClickListener {
            override fun onPlaylistClick(name: String, imageUrl: String) {
                val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistSongFragment(name, imageUrl)
                findNavController().navigate(action)
            }
        })
        binding!!.recyclerView.adapter = adapter

        playlistViewModel.playlistList.observe(viewLifecycleOwner, { playlists ->
            adapter.update(playlists)
            binding?.progressBar?.visibility = View.GONE
        })

        binding!!.createPlaylistButton.setOnClickListener {
            val action = PlaylistFragmentDirections.actionPlaylistFragmentToCreatePlaylistFragment()
            findNavController().navigate(action)
        }

        binding?.swipeToRefresh?.setOnRefreshListener {
            playlistViewModel.refresh()
        }

        playlistViewModel.loadingState.observe(viewLifecycleOwner) { state ->
            binding?.swipeToRefresh?.isRefreshing = state == PlaylistViewModel.LoadingState.LOADING
        }

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        getAllPlaylists()
    }

    private fun getAllPlaylists() {
        binding?.progressBar?.visibility = View.VISIBLE
        playlistViewModel.refresh()
    }
}