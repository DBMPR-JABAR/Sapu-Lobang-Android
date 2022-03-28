package id.go.jabarprov.dbmpr.surveisapulubang.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentLoginBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store.AuthAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.runSafety
import kotlinx.coroutines.launch

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val authViewModel: AuthViewModel by activityViewModels()

    private val loadingDialog by lazy { LoadingDialog.create() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        observeState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.setOnClickListener {
            if (binding.editTextUsername.text != null && binding.editTextPassword.text != null) {
                authViewModel.processAction(
                    AuthAction.LoginUserAction(
                        binding.editTextUsername.text.toString(),
                        binding.editTextPassword.text.toString()
                    )
                )
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isFailed) {
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        runSafety {
                            loadingDialog.dismiss()
                        }
                    }

                    if (it.isSuccess && it.user != null) {
                        runSafety {
                            loadingDialog.dismiss()
                        }
                        val goToDashboardAction =
                            LoginFragmentDirections.actionLoginFragmentToDashboardFragment()
                        findNavController().navigate(goToDashboardAction)
                    }
                }
            }
        }
    }

}