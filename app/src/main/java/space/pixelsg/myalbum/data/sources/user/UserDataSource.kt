package space.pixelsg.myalbum.data.sources.user

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import space.pixelsg.myalbum.data.models.User

interface UserDataSource {
    fun getUsers() : Observable<List<User>>
    fun getUser(email: String) : Observable<User>
    fun addUser(user: User) : Completable

    fun getAuthedUser() : Observable<User>
    fun setAuthedUser(user: User?) : Completable
}