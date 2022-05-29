package space.pixelsg.myalbum.data.models

data class AlbumRequest(
    val query: String,
    var page: Int,
    val limit: Int = 30
) {
    fun isNotValid() : Boolean {
        return query.isEmpty() || query.isBlank() || page <= 0 || limit <= 0
    }
}
