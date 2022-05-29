package space.pixelsg.myalbum.ui.signup

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.databinding.SecondSignupFragmentBinding
import space.pixelsg.myalbum.utils.Extensions

class SecondSignUpFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = SecondSignUpFragment()
    }

    private lateinit var binding: SecondSignupFragmentBinding
    private lateinit var viewModel: SecondSignUpViewModel
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[SecondSignUpViewModel::class.java]
        viewModel.arguments = arguments

        binding = DataBindingUtil.inflate(inflater, R.layout.second_signup_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.textInputEditPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher("ru"))
        binding.btnSignUp.setOnClickListener {
            compositeDisposable.add(
                viewModel.createNewUser().subscribe(this::onUserCreated, this::onError)
            )
        }



        viewModel.passwordMinimumLength.observe(viewLifecycleOwner) {
            binding.textViewPassSize.setTextColor(
                if (it) resources.getColor(R.color.success) else resources.getColor(R.color.error) )
        }
        viewModel.passwordContainDigit.observe(viewLifecycleOwner) {
            binding.textViewPassNumber.setTextColor(
                if (it) resources.getColor(R.color.success) else resources.getColor(R.color.error) )
        }
        viewModel.passwordContainCapitalLetter.observe(viewLifecycleOwner) {
            binding.textViewPassLetterUpper.setTextColor(
                if (it) resources.getColor(R.color.success) else resources.getColor(R.color.error) )
        }
        viewModel.passwordContainSmallLetter.observe(viewLifecycleOwner) {
            binding.textViewPassLetterLower.setTextColor(
                if (it) resources.getColor(R.color.success) else resources.getColor(R.color.error) )
        }
        viewModel.passwordInputSuccess.observe(viewLifecycleOwner) {
            binding.textInputLayoutPassword.error = if (!it) " " else ""
        }
        viewModel.phoneInputSuccess.observe(viewLifecycleOwner) {
            binding.textInputLayoutPhone.error = if (!it) " " else ""
        }
        viewModel.emailInputSuccess.observe(viewLifecycleOwner) {
            binding.textInputLayoutEmail.error = if (!it) " " else ""
        }
        viewModel.allInputsSuccess.observe(viewLifecycleOwner) {
            binding.btnSignUp.isEnabled = it
        }

        return binding.root
    }

    private fun onUserCreated() {
        Extensions.makeSnackOnTop(binding.root, R.string.new_user_created, Snackbar.LENGTH_LONG).show()
        binding.root.findNavController().navigate(R.id.action_secondSignUpFragment_to_authFragment2)
    }

    private fun onError(t: Throwable) {
        if(t is SQLiteConstraintException)
            Extensions.makeSnackOnTop(binding.root, R.string.email_taken, Snackbar.LENGTH_LONG).show()
        else
            Extensions.makeSnackOnTop(binding.root, R.string.can_not_create_user, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}