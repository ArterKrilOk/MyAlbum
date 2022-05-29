package space.pixelsg.myalbum.data.sources.music.local

import io.reactivex.Observable
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumDetails

class LocalMusicDataSourceImpl() : LocalMusicDataSource {
    private val details : MutableMap<String, AlbumDetails>

    init {
        details = HashMap()
    }

    override fun getAlbumDetails(album: Album): Observable<AlbumDetails> {
        return Observable.create{
            if(it.isDisposed)
                return@create

            if(details.containsKey(album.albumId))
                it.onNext(details[album.albumId]!!)

            it.onComplete()
        }
    }

    override fun saveAlbumDetails(albumDetails: AlbumDetails) {
        details[albumDetails.albumId] = albumDetails
    }
}