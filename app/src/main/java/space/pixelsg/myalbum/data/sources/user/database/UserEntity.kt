package space.pixelsg.myalbum.data.sources.user.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity (
    @PrimaryKey val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val dateOfBirth: String,
    val hashedPassword: String,
)