package space.pixelsg.myalbum.data.sources.music.remote.models

import com.google.gson.annotations.SerializedName

data class RemoteResult(
    @SerializedName("results" ) var results : Results? = Results()
)

data class Results (
    @SerializedName("opensearch:totalResults" ) var totalResults : String?           = null,
    @SerializedName("opensearch:startIndex"   ) var startIndex   : String?           = null,
    @SerializedName("opensearch:itemsPerPage" ) var itemsPerPage : String?           = null,
    @SerializedName("albummatches"            ) var albummatches : Albummatches?     = Albummatches(),
)

data class Albummatches (
    @SerializedName("album" ) var album : ArrayList<RemoteAlbum> = arrayListOf()
)

data class RemoteAlbum (
    @SerializedName("name"       ) var name       : String?          = null,
    @SerializedName("artist"     ) var artist     : String?          = null,
    @SerializedName("url"        ) var url        : String?          = null,
    @SerializedName("image"      ) var image      : ArrayList<RemoteImage> = arrayListOf(),
    @SerializedName("streamable" ) var streamable : String?          = null,
    @SerializedName("mbid"       ) var albumId    : String?          = null
)

data class RemoteImage (
    @SerializedName("#text" ) var url   : String? = null,
    @SerializedName("size"  ) var size  : String? = null
)


data class RemoteTrack (
    @SerializedName("duration"   ) var duration   : Int?        = null,
    @SerializedName("url"        ) var url        : String?     = null,
    @SerializedName("name"       ) var name       : String?     = null,
    @SerializedName("artist"     ) var artist     : Artist?     = Artist()
)

data class Tracks (
    @SerializedName("track" ) var track : ArrayList<RemoteTrack> = arrayListOf()
)

data class Artist (
    @SerializedName("url"  ) var url  : String? = null,
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("mbid" ) var artistId : String? = null
)

data class RemoteAlbumDetails (
    @SerializedName("artist"    ) var artist    : String?          = null,
    @SerializedName("name"      ) var name      : String?          = null,
    @SerializedName("image"     ) var image     : ArrayList<RemoteImage> = arrayListOf(),
    @SerializedName("tracks"    ) var tracks    : Tracks?          = Tracks(),
    @SerializedName("listeners" ) var listeners : String?          = null,
    @SerializedName("playcount" ) var playcount : String?          = null,
    @SerializedName("url"       ) var url       : String?          = null,
    @SerializedName("wiki"      ) var wiki      : Wiki?            = Wiki()
)

data class Wiki (

    @SerializedName("published" ) var published : String? = null,
    @SerializedName("summary"   ) var summary   : String? = null,
    @SerializedName("content"   ) var content   : String? = null

)

data class RemoteDetailsResult (
    @SerializedName("album" ) var album : RemoteAlbumDetails? = RemoteAlbumDetails()
)