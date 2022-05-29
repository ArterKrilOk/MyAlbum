package space.pixelsg.myalbum.ui.albumslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.exceptions.EmptyQueryException
import space.pixelsg.myalbum.data.models.exceptions.NothingWasFoundException
import space.pixelsg.myalbum.databinding.AlbumsListFragmentBinding
import space.pixelsg.myalbum.utils.ConnectionLiveData
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.utils.Status


class AlbumsListFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = AlbumsListFragment()
    }

    private lateinit var binding: AlbumsListFragmentBinding
    private lateinit var viewModel: AlbumsListViewModel
    private lateinit var adapter: AlbumViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[AlbumsListViewModel::class.java]

        binding = DataBindingUtil.inflate(inflater, R.layout.albums_list_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        //Create AlbumViewAdapter with item click listener
        adapter = AlbumViewAdapter(object : AlbumViewAdapter.OnAlbumClickListener {
            override fun onClick(v: View, album: Album) {
                onAlbumClick(v, album)
            }
        })
        //Set recycler view
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter
        adapter.assignRecyclerView(binding.recyclerView)

        //Set search bar text listener
        binding.searchBar.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchQueryText.onNext(query ?: String())
                adapter.clear()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if(query.isNullOrEmpty() || query.isBlank()) {
                    viewModel.searchQueryText.onNext(query ?: String())
                    adapter.clear()
                }
                return true
            }

        })

        binding.searchBar.btnProfile.setOnClickListener {
            it.findNavController().navigate(R.id.action_albumsListFragment_to_userBottomFragment)
        }

        //Add load more listener ( load on scroll )
        adapter.setOnLoadMoreListener(viewModel::getAlbums)

        //Add observers to live data
        viewModel.connectionLiveData.observe(viewLifecycleOwner, this::onConnectionUpdate)
        viewModel.albumsLiveData.observe(viewLifecycleOwner, this::onResourceUpdate)

        return binding.root
    }

    fun openAuth() {
        binding.root.findNavController().navigate(R.id.action_userBottomFragment_to_authFragment)
    }

    private fun onResourceUpdate(res: Resource<List<Album>>) {
        when (res.status) {
            Status.LOADING -> {
                adapter.loading = true
                if (adapter.itemCount == 0) {
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.textViewMessage.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                    binding.textViewMessage.visibility = View.GONE
                }
            }
            Status.SUCCESS -> {
                adapter.loading = false
                binding.progressCircular.visibility = View.GONE
                binding.textViewMessage.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.addAll(res.data ?: listOf())
            }
            Status.ERROR -> onError(res.t)
        }
    }

    private fun onError(t: Throwable?) {
        adapter.loading = false
        binding.progressCircular.visibility = View.GONE

        if (t is EmptyQueryException) { //Album request is invalid
            binding.recyclerView.visibility = View.GONE
            binding.textViewMessage.visibility = View.VISIBLE
            binding.textViewMessage.setText(R.string.enter_search_query)
        } else if (adapter.itemCount == 0 && t is NothingWasFoundException) { //Cant find anything
            binding.recyclerView.visibility = View.GONE
            binding.textViewMessage.visibility = View.VISIBLE
            binding.textViewMessage.setText(R.string.can_not_find)
        }
    }

    private fun onConnectionUpdate(connection: ConnectionLiveData.ConnectionType) {
        when (connection) {
            ConnectionLiveData.ConnectionType.NOT_CONNECTED -> {
                binding.textViewNoInternet.visibility = View.VISIBLE
                binding.contentLayout.visibility = View.GONE
            }
            ConnectionLiveData.ConnectionType.WIFI, ConnectionLiveData.ConnectionType.MOBILE -> {
                binding.textViewNoInternet.visibility = View.GONE
                binding.contentLayout.visibility = View.VISIBLE
            }
        }
    }

    private fun onAlbumClick(v: View, album: Album) {
        val bundle = Bundle()
        bundle.putSerializable("album", album)
        binding.root.findNavController().navigate(
            R.id.action_albumsListFragment_to_albumDetailsActivity,
            bundle
        )
    }
}