package space.pixelsg.myalbum.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.room.EmptyResultSetException
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.auth_fragment.*
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.data.models.exceptions.EmailOrPasswordIncorrectException
import space.pixelsg.myalbum.databinding.AuthFragmentBinding
import space.pixelsg.myalbum.utils.Extensions
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.utils.Status

class AuthFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() = AuthFragment()
    }

    private lateinit var binding : AuthFragmentBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        binding = DataBindingUtil.inflate(inflater, R.layout.auth_fragment, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnAuth.setOnClickListener { viewModel.auth() }

        viewModel.authedUserLiveData.observe(viewLifecycleOwner, this::onResource)

        return binding.root
    }

    private fun onResource(res: Resource<Unit>) {
        when (res.status) {
            Status.LOADING -> {
                binding.textInputLayoutEmail.isEnabled = false
                binding.textInputLayoutPassword.isEnabled = false
                binding.btnAuth.isEnabled = false
            }
            Status.SUCCESS -> {
                binding.textInputLayoutEmail.isEnabled = true
                binding.textInputLayoutPassword.isEnabled = true
                binding.btnAuth.isEnabled = true
                onAuthSuccess()
            }
            Status.ERROR -> {
                binding.textInputLayoutEmail.isEnabled = true
                binding.textInputLayoutPassword.isEnabled = true
                binding.btnAuth.isEnabled = true
                onAuthError(res.t!!)
            }
        }
    }

    private fun onAuthSuccess() {
        binding.root.findNavController().navigate(R.id.action_authFragment_to_albumsListFragment)
    }

    private fun onAuthError(t: Throwable) {
        if(t is EmailOrPasswordIncorrectException || t is EmptyResultSetException)
            Extensions.makeSnackOnTop(binding.root, R.string.email_password_incorrect, Snackbar.LENGTH_LONG).show()
        else
            Extensions.makeSnackOnTop(binding.root, R.string.something_went_wrong, Snackbar.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateAccount.setOnClickListener {
            it.findNavController().navigate(R.id.action_authFragment_to_firstSignUpFragment)
        }
    }
}