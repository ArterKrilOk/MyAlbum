package space.pixelsg.myalbum.data.repository

import io.reactivex.Observable
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumDetails
import space.pixelsg.myalbum.data.models.AlbumRequest

interface MusicRepository {
    fun getAlbums(albumRequest: AlbumRequest) : Observable<List<Album>>
    fun getAlbumDetails(album: Album) : Observable<AlbumDetails>
}