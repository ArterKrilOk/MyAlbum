package space.pixelsg.myalbum.data.repository

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import space.pixelsg.myalbum.data.models.Album
import space.pixelsg.myalbum.data.models.AlbumDetails
import space.pixelsg.myalbum.data.models.AlbumRequest
import space.pixelsg.myalbum.data.sources.music.local.LocalMusicDataSource
import space.pixelsg.myalbum.data.sources.music.remote.RemoteMusicDataSource

class MusicRepositoryImpl(
    private val remoteMusicDataSource: RemoteMusicDataSource,
    private val localMusicDataSource: LocalMusicDataSource,
) : MusicRepository {


    override fun getAlbums(albumRequest: AlbumRequest): Observable<List<Album>> =
        remoteMusicDataSource.getAlbums(albumRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    override fun getAlbumDetails(album: Album): Observable<AlbumDetails> = Observable.concat(
            localMusicDataSource.getAlbumDetails(album),                //Cache
            remoteMusicDataSource.getAlbumDetails(album))               //Normal Source
            .firstElement()                                             //Get first element
            .toObservable()
            .doOnNext { localMusicDataSource.saveAlbumDetails(it) }     //Save element to cache for the next time
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}