package space.pixelsg.myalbum.data.models.exceptions


open class AuthException : Exception()

class NotAuthedException : AuthException()
class UserAlreadyExistException : AuthException()
class EmailOrPasswordIncorrectException : AuthException()