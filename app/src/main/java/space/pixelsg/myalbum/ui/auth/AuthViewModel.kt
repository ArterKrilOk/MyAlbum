package space.pixelsg.myalbum.ui.auth

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import space.pixelsg.myalbum.data.models.User
import space.pixelsg.myalbum.data.models.exceptions.EmailOrPasswordIncorrectException
import space.pixelsg.myalbum.utils.Extensions.isEmailCorrect
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.vm.AppViewModel

class AuthViewModel(application: Application) : AppViewModel(application) {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val emailField : ObservableField<String> = ObservableField()
    val passwordField : ObservableField<String> = ObservableField()

    val authedUserLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()

    init {
        //Check is user authed
        userRepository
            .getAuthedUser()
            .subscribe(object : Observer<User> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }
                override fun onNext(t: User) {}
                override fun onError(e: Throwable) {}

                override fun onComplete() {
                    authedUserLiveData.postValue(Resource.success(Unit))
                }
            })
    }

    fun auth() {
        authedUserLiveData.postValue(Resource.loading(null))

        if(!checkFields()) {
            authedUserLiveData.postValue(Resource.error(EmailOrPasswordIncorrectException(), null))
            return
        }

        userRepository
            .getUser(emailField.get()!!)
            .concatMapCompletable {
                if(User.isPasswordSame(passwordField.get()!!, it))
                    return@concatMapCompletable userRepository.setAuthedUser(it)
                return@concatMapCompletable Completable.error(EmailOrPasswordIncorrectException())
            }
            .toObservable<User>()
            .subscribe(object : Observer<User>{
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: User) {}

                override fun onError(e: Throwable) {
                    authedUserLiveData.postValue(Resource.error(e, null))
                }

                override fun onComplete() {
                    authedUserLiveData.postValue(Resource.success(Unit))
                }

            })
    }

    private fun checkFields() : Boolean {
        if (emailField.get().isNullOrEmpty() || !emailField.get()!!.isEmailCorrect())
            return false
        if(passwordField.get().isNullOrEmpty() || passwordField.get()!!.isBlank())
            return false
        return true
    }
    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}