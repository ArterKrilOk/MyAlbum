package space.pixelsg.myalbum.ui.albumdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumDetails
import space.pixelsg.myalbum.data.models.Track
import space.pixelsg.myalbum.databinding.ActivityAlbumDetailsBinding
import space.pixelsg.myalbum.utils.ConnectionLiveData
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.utils.Status

class AlbumDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlbumDetailsBinding
    private lateinit var viewModel: AlbumDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[AlbumDetailsViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album_details)

        viewModel.albumDetailsLiveData.observe(this, this::onResource)
        viewModel.connectionLiveData.observe(this, this::onConnectionUpdate)


        val album = intent.extras?.getSerializable("album") as Album
        viewModel.getAlbumDetails(album)
    }

    private fun onResource(res: Resource<AlbumDetails>) {
        when (res.status) {
            Status.LOADING -> {
                binding.nestedScrollView.visibility = View.GONE
                binding.textViewMessage.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
            }
            Status.SUCCESS -> {
                binding.nestedScrollView.visibility = View.VISIBLE
                binding.textViewMessage.visibility = View.GONE
                binding.progressCircular.visibility = View.GONE
                res.data?.let { setAlbumDetails(it) }
            }
            Status.ERROR -> {
                binding.nestedScrollView.visibility = View.GONE
                binding.textViewMessage.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
                binding.textViewMessage.setText(R.string.something_went_wrong)
            }
        }
    }

    private fun setAlbumDetails(albumDetails: AlbumDetails) {
        binding.albumHeaderLayout.textViewName.text = albumDetails.name
        binding.albumHeaderLayout.textViewArtist.text = albumDetails.artist
        binding.albumHeaderLayout.textViewDate.text = albumDetails.date
        binding.albumHeaderLayout.textViewSongs.text = albumDetails.tracks.size.toString()

        setTracksAdapter(albumDetails.tracks)
        Glide
            .with(binding.albumHeaderLayout.imageView)
            .load(albumDetails.image.full)
            .centerCrop()
            .into(binding.albumHeaderLayout.imageView)
    }

    private fun setTracksAdapter(tracks: List<Track>) {
        val adapter = TrackViewAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        adapter.setTracks(tracks)
    }

    private fun onConnectionUpdate(connection: ConnectionLiveData.ConnectionType) {
        when (connection) {
            ConnectionLiveData.ConnectionType.NOT_CONNECTED -> {
                binding.textViewMessage.visibility = View.VISIBLE
                binding.nestedScrollView.visibility = View.GONE
                binding.textViewMessage.setText(R.string.no_internet_connection)
            }
            ConnectionLiveData.ConnectionType.WIFI, ConnectionLiveData.ConnectionType.MOBILE -> {
                binding.textViewMessage.visibility = View.GONE
                binding.nestedScrollView.visibility = View.VISIBLE
            }
        }
    }
}