package space.pixelsg.myalbum.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.reactivex.disposables.CompositeDisposable
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.databinding.FirstSignupFragmentBinding
import space.pixelsg.myalbum.utils.Extensions

class FirstSignUpFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = FirstSignUpFragment()
    }

    private lateinit var binding: FirstSignupFragmentBinding
    private lateinit var viewModel: FirstSignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[FirstSignUpViewModel::class.java]

        binding = DataBindingUtil.inflate(inflater, R.layout.first_signup_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnNext.setOnClickListener { viewModel.openSecondSignUpFragment(it) }

        //Add date input formatter
        binding.textInputDate.addTextChangedListener(Extensions.dateTextWatcher())

        viewModel.firstNameSuccessInput.observe(viewLifecycleOwner) {
            binding.textInputLayoutFirstName.error = if (!it) " " else ""
        }
        viewModel.lastNameSuccessInput.observe(viewLifecycleOwner) {
            binding.textInputLayoutLastName.error = if (!it) " " else ""
        }
        viewModel.dateSuccessInput.observe(viewLifecycleOwner) {
            binding.textInputLayoutDate.error = if (!it) " " else ""
        }
        viewModel.nextButtonEnabled.observe(viewLifecycleOwner) {
            binding.btnNext.isEnabled = it
        }

        return binding.root
    }
}