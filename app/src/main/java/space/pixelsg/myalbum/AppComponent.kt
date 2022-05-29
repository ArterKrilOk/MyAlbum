package space.pixelsg.myalbum

import dagger.Component
import space.pixelsg.myalbum.data.DataModule
import space.pixelsg.myalbum.data.repository.MusicRepository
import space.pixelsg.myalbum.data.repository.UserRepository
import space.pixelsg.myalbum.vm.AppViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, DataModule::class])
interface AppComponent {
    fun getMusicRepository() : MusicRepository
    fun getUserRepository() : UserRepository

    fun injectInto(appViewModel: AppViewModel)
}