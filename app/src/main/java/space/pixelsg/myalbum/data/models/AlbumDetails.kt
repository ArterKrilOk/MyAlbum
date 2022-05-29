package space.pixelsg.myalbum.data.models

data class AlbumDetails(
    val name: String,
    val artist: String,
    val albumId: String,
    val image: Image,
    val tracks: List<Track>,
    val date: String,
)
