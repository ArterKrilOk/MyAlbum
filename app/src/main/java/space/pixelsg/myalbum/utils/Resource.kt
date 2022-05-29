package space.pixelsg.myalbum.utils

data class Resource<out T>(val status: Status, val data: T?, val t: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(t: Throwable, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, t)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status{
    SUCCESS, LOADING, ERROR
}