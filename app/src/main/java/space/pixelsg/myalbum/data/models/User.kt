package space.pixelsg.myalbum.data.models

import java.security.MessageDigest

data class User(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val phone: String,
    val email: String,
    val hashedPassword: String,
) {
    companion object {
        @JvmStatic
        fun createNewUser(
            firstName: String,
            lastName: String,
            dateOfBirth: String,
            phone: String,
            email: String,
            password: String,
        ) : User {
            val bytes = MessageDigest.getInstance("MD5").digest(password.toByteArray())
            val hashedPassword = bytes.joinToString("") { "%02x".format(it) }
            return User(
                firstName, lastName, dateOfBirth, phone, email, hashedPassword
            )
        }

        @JvmStatic
        fun isPasswordSame(password: String, user: User): Boolean {
            val bytes = MessageDigest.getInstance("MD5").digest(password.toByteArray())
            val hashedPassword = bytes.joinToString("") { "%02x".format(it) }
            return hashedPassword == user.hashedPassword
        }
    }
}
