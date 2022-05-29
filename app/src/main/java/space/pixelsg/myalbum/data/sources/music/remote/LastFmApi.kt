package space.pixelsg.myalbum.data.sources.music.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import space.pixelsg.myalbum.data.sources.music.remote.models.RemoteDetailsResult
import space.pixelsg.myalbum.data.sources.music.remote.models.RemoteResult

interface LastFmApi {
    companion object {
        const val API_KEY = "caf99e9b90354e9f12499a88a1c29bf3"
        const val FORMAT = "json"
    }

    @GET("/2.0/")
    fun getAlbums(
        @Query("album")  query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("api_key") key: String = API_KEY,
        @Query("format") format: String = FORMAT,
        @Query("method") method: String = "album.search",
    ) : Observable<RemoteResult>

    @GET("/2.0/")
    fun getAlbumDetails(
        @Query("mbid")  album_id: String,
        @Query("api_key") key: String = API_KEY,
        @Query("format") format: String = FORMAT,
        @Query("method") method: String = "album.getinfo",
    ) : Observable<RemoteDetailsResult>
}