package space.pixelsg.myalbum.data.sources.user.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Observable
import io.reactivex.Single
import space.pixelsg.myalbum.data.models.User

@Dao
interface UserDao {
    @Insert
    fun insert(userEntity: UserEntity)

    @Delete
    fun delete(userEntity: UserEntity)

    @Query("SELECT * FROM users")
    fun getUsers() : Observable<List<UserEntity>>

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUser(email: String) : Single<User>
}