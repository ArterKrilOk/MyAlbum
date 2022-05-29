package space.pixelsg.myalbum.ui.signup

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import space.pixelsg.myalbum.data.models.User
import space.pixelsg.myalbum.utils.Extensions.isEmailCorrect
import space.pixelsg.myalbum.utils.Extensions.isPhoneCorrect
import space.pixelsg.myalbum.vm.AppViewModel

class SecondSignUpViewModel(application: Application) : AppViewModel(application) {
    var arguments: Bundle? = null

    val emailSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val phoneSubject: BehaviorSubject<String> = BehaviorSubject.createDefault("+7")
    val passwordSubject: BehaviorSubject<String> = BehaviorSubject.create()

    val emailInputSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val phoneInputSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordInputSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    val passwordMinimumLength: MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordContainDigit: MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordContainSmallLetter: MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordContainCapitalLetter: MutableLiveData<Boolean> = MutableLiveData(false)

    val allInputsSuccess: MutableLiveData<Boolean> = MutableLiveData(false)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.addAll(
            emailSubject.subscribe {
                emailInputSuccess.postValue(it.isEmailCorrect())
                allInputsSuccess.postValue(passwordInputSuccess.value == true &&
                        phoneInputSuccess.value == true &&
                        emailInputSuccess.value == true)
            },
            phoneSubject.subscribe {
                phoneInputSuccess.postValue(it.isPhoneCorrect())
                allInputsSuccess.postValue(passwordInputSuccess.value == true &&
                        phoneInputSuccess.value == true &&
                        emailInputSuccess.value == true)
            },
            passwordSubject.subscribe {
                passwordMinimumLength.postValue(isMinimum6Symbols(it))
                passwordContainDigit.postValue(isContainNumber(it))
                passwordContainSmallLetter.postValue(isContainSmallLetter(it))
                passwordContainCapitalLetter.postValue(isContainCapitalLetter(it))

                val passSuccess = isMinimum6Symbols(it) &&
                        isContainCapitalLetter(it) &&
                        isContainNumber(it) &&
                        isContainSmallLetter(it)
                passwordInputSuccess.postValue(passSuccess)

                allInputsSuccess.postValue(passSuccess &&
                        phoneInputSuccess.value == true &&
                        emailInputSuccess.value == true)
            },

        )
    }

    fun createNewUser() : Completable {
        if(phoneInputSuccess.value != true ||
            emailInputSuccess.value != true ||
            passwordInputSuccess.value != true ||
                arguments == null)
            return Completable.error(IllegalArgumentException())

        val email = emailSubject.value ?: String()
        val phone = phoneSubject.value ?: String()
        val password = passwordSubject.value ?: String()
        val firstName = arguments!!.getString("firstName") ?: String()
        val lastName = arguments!!.getString("lastName") ?: String()
        val date = arguments!!.getString("date") ?: String()

        return userRepository.addUser(User.createNewUser(
                firstName,
                lastName,
                date,
                phone,
                email,
                password
            ))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun isMinimum6Symbols(string: String) = string.length > 6
    private fun isContainSmallLetter(string: String) : Boolean {
        for(char in string)
            if (char.isLetter() && char.isLowerCase())
                return true
        return false
    }
    private fun isContainCapitalLetter(string: String) : Boolean {
        for(char in string)
            if (char.isLetter() && char.isUpperCase())
                return true
        return false
    }
    private fun isContainNumber(string: String) : Boolean {
        for(char in string)
            if(char.isDigit())
                return true
        return false
    }
}