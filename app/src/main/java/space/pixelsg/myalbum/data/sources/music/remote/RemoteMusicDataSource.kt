package space.pixelsg.myalbum.data.sources.music.remote

import io.reactivex.Observable
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumDetails
import space.pixelsg.myalbum.data.models.AlbumRequest

interface RemoteMusicDataSource {
    fun getAlbums(albumRequest: AlbumRequest) : Observable<List<Album>>

    fun getAlbumDetails(album: Album) : Observable<AlbumDetails>
}