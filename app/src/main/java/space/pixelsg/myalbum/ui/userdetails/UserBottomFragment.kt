package space.pixelsg.myalbum.ui.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import space.pixelsg.myalbum.R
import space.pixelsg.myalbum.data.models.User
import space.pixelsg.myalbum.databinding.UserDetailsBottomFragmentBinding
import space.pixelsg.myalbum.ui.albumslist.AlbumsListFragment
import space.pixelsg.myalbum.utils.Resource
import space.pixelsg.myalbum.utils.Status

class UserBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: UserDetailsBottomFragmentBinding
    private lateinit var viewModel: UserDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]

        binding = DataBindingUtil.inflate(inflater, R.layout.user_details_bottom_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnLogOut.setOnClickListener {
            viewModel.logOut()
            val fragment = parentFragmentManager.findFragmentById(R.id.nav_host_fragment)
            if(fragment is AlbumsListFragment)
                fragment.openAuth()
        }
        viewModel.userLiveData.observe(viewLifecycleOwner, this::onResource)

        return binding.root
    }

    private fun onResource(res: Resource<User>) {
        when (res.status) {
            Status.LOADING -> {
                binding.progressCircular.visibility = View.VISIBLE
                binding.contentLayout.visibility = View.GONE
                binding.btnLogOut.visibility = View.GONE
            }
            Status.SUCCESS -> {
                binding.progressCircular.visibility = View.GONE
                binding.contentLayout.visibility = View.VISIBLE
                binding.btnLogOut.visibility = View.VISIBLE

                binding.textViewFirstName.text = getString(R.string.user_first_name, res.data!!.firstName)
                binding.textViewLastName.text = getString(R.string.user_last_name, res.data.lastName)
                binding.textViewDate.text = getString(R.string.user_date_of_birth, res.data.dateOfBirth)

                binding.textViewEmail.text = getString(R.string.user_email, res.data.email)
            }
            Status.ERROR -> {
                dismiss()
            }
        }
    }
}