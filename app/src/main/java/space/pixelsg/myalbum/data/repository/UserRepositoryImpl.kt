package space.pixelsg.myalbum.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import space.pixelsg.myalbum.data.models.User
import space.pixelsg.myalbum.data.sources.user.UserDataSource

class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {

    override fun getUser(email: String): Observable<User> = userDataSource.getUser(email)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())


    override fun addUser(user: User): Completable = userDataSource.addUser(user)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun getAuthedUser(): Observable<User> = userDataSource.getAuthedUser()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun setAuthedUser(user: User?): Completable = userDataSource.setAuthedUser(user)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}