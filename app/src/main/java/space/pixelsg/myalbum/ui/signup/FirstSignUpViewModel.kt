package space.pixelsg.myalbum.ui.signup

import android.app.Application
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.vm.AppViewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class FirstSignUpViewModel(application: Application) : AppViewModel(application) {
    val firstNameSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val lastNameSubject: BehaviorSubject<String> = BehaviorSubject.create()
    val dateSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val firstNameSuccessInput: MutableLiveData<Boolean> = MutableLiveData(false)
    val lastNameSuccessInput: MutableLiveData<Boolean> = MutableLiveData(false)
    val dateSuccessInput: MutableLiveData<Boolean> = MutableLiveData(false)
    val nextButtonEnabled: MutableLiveData<Boolean> = MutableLiveData(false)


    init {
        compositeDisposable.addAll(
            firstNameSubject.subscribe {
                firstNameSuccessInput.postValue(it.length > 4)
                nextButtonEnabled.postValue(firstNameSuccessInput.value == true &&
                        lastNameSuccessInput.value == true &&
                        dateSuccessInput.value == true)
            },
            lastNameSubject.subscribe {
                lastNameSuccessInput.postValue(it.length > 4)
                nextButtonEnabled.postValue(firstNameSuccessInput.value == true &&
                        lastNameSuccessInput.value == true &&
                        dateSuccessInput.value == true)
            },
            dateSubject.subscribe {
                dateSuccessInput.postValue(false)
                if(it.length == 10) {
                    val format = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    val lDate: LocalDate = LocalDate.parse(it, format)

                dateSuccessInput.postValue(calculateAge(lDate, LocalDate.now()) >= 18)
                    nextButtonEnabled.postValue(
                        firstNameSuccessInput.value == true &&
                                lastNameSuccessInput.value == true &&
                                dateSuccessInput.value == true
                    )
                }
            }
        )
    }

    private fun calculateAge(birthDate: LocalDate?, currentDate: LocalDate?): Int {
        return if (birthDate != null && currentDate != null) {
            Period.between(birthDate, currentDate).years
        } else {
            0
        }
    }

    fun openSecondSignUpFragment(view: View) {
        val bundle = bundleOf("firstName" to firstNameSubject.value, "lastName" to lastNameSubject.value, "date" to dateSubject.value)
        view.findNavController().navigate(R.id.action_firstSignUpFragment_to_secondSignUpFragment, bundle)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}