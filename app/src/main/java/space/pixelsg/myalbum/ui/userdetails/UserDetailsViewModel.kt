package space.pixelsg.myalbum.ui.userdetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import space.pixelsg.myalbum.data.models.User
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.vm.AppViewModel

class UserDetailsViewModel(application: Application) : AppViewModel(application) {
    private val compositeDisposable = CompositeDisposable()

    val userLiveData: MutableLiveData<Resource<User>> = MutableLiveData()

    init {
        userRepository
            .getAuthedUser()
            .subscribe(object : Observer<User> {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                userLiveData.postValue(Resource.loading(null))
            }

            override fun onNext(t: User) {
                userLiveData.postValue(Resource.success(t))
            }

            override fun onError(e: Throwable) {
                userLiveData.postValue(Resource.error(e, null))
            }

            override fun onComplete() {}
        })
    }

    fun logOut() {
        userRepository.setAuthedUser(null).subscribe()
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}