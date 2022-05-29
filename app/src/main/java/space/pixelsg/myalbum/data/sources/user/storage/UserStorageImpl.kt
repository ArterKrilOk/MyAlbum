package space.pixelsg.myalbum.data.sources.user.storage

import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import space.pixelsg.myalbum.data.models.User
import space.pixelsg.myalbum.data.models.exceptions.NotAuthedException
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class UserStorageImpl (
    cacheFolder: File
) : UserStorage {

    companion object {
        const val CACHE_FILE_NAME = "user_cache"
    }

    private val cacheFile: File = File(cacheFolder, CACHE_FILE_NAME)
    private val gson: Gson = Gson()

    override fun authUser(user: User): Completable = Completable.create {
        if (it.isDisposed)
            return@create

        val writer = FileWriter(cacheFile, false)
        writer.write(gson.toJson(user))
        writer.close()

        it.onComplete()
    }

    override fun logOut(): Completable = Completable.create {
        if (it.isDisposed)
            return@create

        if (cacheFile.exists())
            cacheFile.delete()

        it.onComplete()
    }

    override fun getUser(): Observable<User> = Observable.create {
        if (it.isDisposed)
            return@create

        if (!cacheFile.exists())
            throw NotAuthedException()

        val user = gson.fromJson(FileReader(cacheFile), User::class.java)
        it.onNext(user)

        it.onComplete()
    }
}