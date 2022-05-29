package space.pixelsg.myalbum.data.sources.music.remote

import io.reactivex.Observable
import io.reactivex.Single
import space.pixelsg.myalbum.data.models.*
import space.pixelsg.myalbum.data.models.exceptions.NothingWasFoundException

class RemoteMusicDataSourceImpl(private val lastFmApi: LastFmApi) : RemoteMusicDataSource {


    override fun getAlbums(albumRequest: AlbumRequest): Observable<List<Album>> {
        return lastFmApi
            .getAlbums(albumRequest.query, albumRequest.page, albumRequest.limit)
            .map {
                val albums = mutableListOf<Album>()

                if(it.results != null)
                    for (rawAlbum in it.results!!.albummatches!!.album) {
                        if (rawAlbum.albumId.isNullOrEmpty()) continue
                        val name = rawAlbum.name ?: continue
                        val albumId = rawAlbum.albumId ?: continue
                        val artist = rawAlbum.artist ?: continue
                        val imageThumb = rawAlbum.image.first().url ?: continue
                        val imageFull = rawAlbum.image.last().url ?: continue

                        albums.add(Album(name, artist, albumId, Image(imageThumb, imageFull)))
                    }

                return@map albums
            }
            .concatMap {
                if (it.isEmpty())
                    return@concatMap Observable.error(NothingWasFoundException())
                return@concatMap Single.just(it).toObservable()
            }
    }

    override fun getAlbumDetails(album: Album): Observable<AlbumDetails> {
        return lastFmApi
            .getAlbumDetails(album.albumId)
            .map {
                if(it.album == null)
                    throw NullPointerException()

                val image = Image(
                    thumb = it.album!!.image.first().url ?: String(),
                    full = it.album!!.image.last().url ?: String(),
                )

                val tracks = mutableListOf<Track>()

                for(rawTrack in it.album!!.tracks!!.track) {

                    val track = Track(
                        name = rawTrack.name ?: String(),
                        duration = rawTrack.duration ?: 0,
                        artist = rawTrack.artist!!.name ?: String(),
                        image = image
                    )
                    tracks.add(track)
                }

                return@map AlbumDetails(
                    name = it.album!!.name ?: String(),
                    artist = it.album!!.artist ?: String(),
                    albumId = album.albumId,
                    image = image,
                    date = it.album!!.wiki!!.published ?: String(),
                    tracks = tracks
                )
            }
    }
}