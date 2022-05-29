package space.pixelsg.myalbum.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import space.pixelsg.myalbum.data.models.User

interface UserRepository {
    fun getUser(email: String) : Observable<User>
    fun addUser(user: User) : Completable

    fun getAuthedUser() : Observable<User>
    fun setAuthedUser(user: User?) : Completable
}