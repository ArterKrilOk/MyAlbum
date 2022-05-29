package space.pixelsg.myalbum.data.models

import java.io.Serializable

data class Album (
    val name: String,
    val artist: String,
    val albumId: String,
    val image: Image
) : Serializable
