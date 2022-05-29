package id.go.jabarprov.dbmpr.surveisapulubang.presentation.splash_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentSplashScreenBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store.AuthAction
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private val authViewModel by activityViewModels<AuthViewModel>()

    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkAccessToken()
        observeAuthViewModel()
    }

    private fun checkAccessToken() {
        authViewModel.processAction(AuthAction.CheckAccessToken)
    }

    private fun observeAuthViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                authViewModel.uiState.collect {
                    delay(1000)
                    processCheckTokenState(it.checkTokenState)
                }
            }
        }
    }

    private fun processCheckTokenState(state: Resource<User>) {
        when (state) {
            is Resource.Failed -> {
                showToast(state.errorMessage)
                val action =
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
                findNavController().navigate(action)
            }
            is Resource.Success -> {
                val action =
                    SplashScreenFragmentDirections.actionSplashScreenFragmentToDashboardFragment()
                findNavController().navigate(action)
            }
            else -> Unit
        }
    }
}