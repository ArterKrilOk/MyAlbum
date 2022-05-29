package space.pixelsg.myalbum.data.sources.user.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserEntity::class], version = 2)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
}