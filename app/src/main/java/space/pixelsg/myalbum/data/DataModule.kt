package space.pixelsg.myalbum.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import space.pixelsg.myalbum.data.repository.MusicRepository
import space.pixelsg.myalbum.data.repository.MusicRepositoryImpl
import space.pixelsg.myalbum.data.repository.UserRepository
import space.pixelsg.myalbum.data.repository.UserRepositoryImpl
import space.pixelsg.myalbum.data.sources.music.local.LocalMusicDataSource
import space.pixelsg.myalbum.data.sources.music.local.LocalMusicDataSourceImpl
import space.pixelsg.myalbum.data.sources.music.remote.LastFmApi
import space.pixelsg.myalbum.data.sources.music.remote.RemoteMusicDataSource
import space.pixelsg.myalbum.data.sources.music.remote.RemoteMusicDataSourceImpl
import space.pixelsg.myalbum.data.sources.user.UserDataSource
import space.pixelsg.myalbum.data.sources.user.UserDataSourceImpl
import space.pixelsg.myalbum.data.sources.user.database.UsersDatabase
import space.pixelsg.myalbum.data.sources.user.storage.UserStorage
import space.pixelsg.myalbum.data.sources.user.storage.UserStorageImpl
import java.io.File
import javax.inject.Singleton

@Module
class DataModule {
    companion object {
        const val RETROFIT_BASE_RUL = "https://ws.audioscrobbler.com"
    }

    @Singleton
    @Provides
    fun provideLastFmApi() : LastFmApi {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(RETROFIT_BASE_RUL)
            .build()

        return retrofit.create(LastFmApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRemoteMusicDataSource(
        lastFmApi: LastFmApi
    ) : RemoteMusicDataSource = RemoteMusicDataSourceImpl(lastFmApi)

    @Singleton
    @Provides
    fun provideLocalMusicDataSource() : LocalMusicDataSource = LocalMusicDataSourceImpl()

    @Singleton
    @Provides
    fun provideMusicRepository(
        remoteMusicDataSource: RemoteMusicDataSource,
        localMusicDataSource: LocalMusicDataSource
    ) : MusicRepository = MusicRepositoryImpl(remoteMusicDataSource, localMusicDataSource)


    @Singleton
    @Provides
    fun provideCacheFolder(context: Context) : File = context.cacheDir

    @Singleton
    @Provides
    fun provideUserStorage(cacheFolder: File) : UserStorage = UserStorageImpl(cacheFolder)

    @Singleton
    @Provides
    fun provideUsersDatabase(context: Context) : UsersDatabase {
        return Room.databaseBuilder(
            context,
            UsersDatabase::class.java,
            "users_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideUserDataSource(
        usersDatabase: UsersDatabase,
        userStorage: UserStorage
    ) : UserDataSource = UserDataSourceImpl(usersDatabase, userStorage)

    @Singleton
    @Provides
    fun provideUserRepository(
        userDataSource: UserDataSource
    ) : UserRepository = UserRepositoryImpl(userDataSource)
}