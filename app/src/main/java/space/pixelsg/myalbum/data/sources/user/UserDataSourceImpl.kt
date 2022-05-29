package space.pixelsg.myalbum.data.sources.user

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import space.pixelsg.myalbum.data.models.User
import space.pixelsg.myalbum.data.sources.user.database.UserEntity
import space.pixelsg.myalbum.data.sources.user.database.UsersDatabase
import space.pixelsg.myalbum.data.sources.user.storage.UserStorage

class UserDataSourceImpl (
    private val usersDatabase: UsersDatabase,
    private val userStorage: UserStorage
) : UserDataSource {

    override fun getUsers(): Observable<List<User>> {
        return usersDatabase.userDao().getUsers().map { list ->
            return@map list.map { User(
                email = it.email,
                firstName = it.lastName,
                lastName = it.lastName,
                phone = it.phone,
                dateOfBirth = it.dateOfBirth,
                hashedPassword = it.hashedPassword
            ) }
        }
    }

    override fun getUser(email: String): Observable<User> =
        usersDatabase.userDao().getUser(email).toObservable()


    override fun addUser(user: User): Completable = Completable.create {
        if(it.isDisposed)
            return@create

        usersDatabase.userDao().insert(UserEntity(
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
            dateOfBirth = user.dateOfBirth,
            phone = user.phone,
            hashedPassword = user.hashedPassword
        ))

        it.onComplete()
    }

    override fun getAuthedUser(): Observable<User> = userStorage.getUser()

    override fun setAuthedUser(user: User?): Completable {
        if (user == null)
            return userStorage.logOut()
        else
            return userStorage.authUser(user)
    }
}