package space.pixelsg.myalbum.data.sources.user.storage

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import space.pixelsg.myalbum.data.models.User

interface UserStorage {
    fun authUser(user: User) : Completable
    fun logOut() : Completable
    fun getUser() : Observable<User>
}