package space.pixelsg.myalbum.ui.albumdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.data.models.Track
import space.pixelsg.myalbum.databinding.TrackListItemBinding

class TrackViewAdapter : RecyclerView.Adapter<TrackViewAdapter.TrackViewHolder>() {
    inner class TrackViewHolder(
        private val binding: TrackListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindTrack(track: Track) {
            binding.track = track
            binding.invalidateAll()
        }
    }

    private val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = DataBindingUtil.inflate<TrackListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.track_list_item,
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) = holder.bindTrack(tracks[position])
    override fun getItemCount(): Int = tracks.size

    fun clear() {
        val size = tracks.size
        tracks.clear()
        notifyItemRangeRemoved(0, size)
    }

    fun setTracks(newTracks: List<Track>) {
        clear()
        tracks.addAll(newTracks)
        notifyItemRangeInserted(0, newTracks.size)
    }
}