package space.pixelsg.myalbum.ui.albumslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.databinding.AlbumListItemBinding
import space.pixelsg.myalbum.utils.Extensions.diffCallback

class AlbumViewAdapter(
    private val onAlbumClickListener: OnAlbumClickListener
) : RecyclerView.Adapter<AlbumViewAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(private val binding: AlbumListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindAlbum(album: Album, onAlbumClickListener: OnAlbumClickListener) {
            binding.album = album
            binding.invalidateAll()
            binding.root.setOnClickListener { onAlbumClickListener.onClick(it, album) }
        }
    }

    private val albums: MutableList<Album> = mutableListOf()
    private var onLoadMoreListener: OnLoadMoreListener? = null
    var loading: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<AlbumListItemBinding>(inflater, R.layout.album_list_item, parent, false)
        return AlbumViewHolder(binding)
    }
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) = holder.bindAlbum(albums[position], onAlbumClickListener)
    override fun getItemCount(): Int = albums.size

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun clear() {
        val size = albums.size
        albums.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun addAll(newAlbums: List<Album>) {
        val fullAlbumsList = albums + newAlbums
        val result = DiffUtil.calculateDiff(albums.diffCallback(fullAlbumsList))
        albums.clear()
        albums.addAll(fullAlbumsList)
        result.dispatchUpdatesTo(this)
    }

    fun assignRecyclerView(recyclerView: RecyclerView) {
        val glm = recyclerView.layoutManager as GridLayoutManager?
        if (glm != null) recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItem = glm.findLastVisibleItemPosition()
                if (!loading && albums.size <= lastVisibleItem + 8) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener!!.onLoadMore()
                    }
                    loading = true
                }
            }
        })
    }

    fun setOnLoadMoreListener(onLoadMoreListener: () -> Unit) {
        this.onLoadMoreListener = object : OnLoadMoreListener {
            override fun onLoadMore() {
                onLoadMoreListener()
            }
        }
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    interface OnAlbumClickListener {
        fun onClick(v: View, album: Album)
    }
}