package space.pixelsg.myalbum.data.models.exceptions

open class RequestException : Exception()

class EmptyQueryException : RequestException()
class NothingWasFoundException : RequestException()