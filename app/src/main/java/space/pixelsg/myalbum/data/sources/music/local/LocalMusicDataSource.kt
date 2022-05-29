package space.pixelsg.myalbum.data.sources.music.local

import io.reactivex.Observable
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumDetails

interface LocalMusicDataSource {
    fun getAlbumDetails(album: Album) : Observable<AlbumDetails>

    fun saveAlbumDetails(albumDetails: AlbumDetails)
}