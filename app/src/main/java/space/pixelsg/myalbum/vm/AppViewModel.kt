package space.pixelsg.myalbum.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import space.pixelsg.myalbum.App
import space.pixelsg.myalbum.data.repository.MusicRepository
import space.pixelsg.myalbum.data.repository.UserRepository
import javax.inject.Inject

open class AppViewModel(application: Application) : AndroidViewModel(application) {
    @Inject lateinit var userRepository : UserRepository
    @Inject lateinit var  musicRepository: MusicRepository

    init {
        (application as App).appComponent.injectInto(this)
    }
}