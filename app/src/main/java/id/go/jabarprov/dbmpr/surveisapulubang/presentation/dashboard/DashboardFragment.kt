package id.go.jabarprov.dbmpr.surveisapulubang.presentation.dashboard

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
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentDashboardBinding
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store.AuthAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    lateinit var binding: FragmentDashboardBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        observeAuthState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonLogout.setOnClickListener {
            authViewModel.processAction(AuthAction.LogoutUserAction)
        }

        binding.buttonEntryLubang.setOnClickListener {
            val goToEntryLubang =
                DashboardFragmentDirections.actionDashboardFragmentToEntryLubangFragment()
            findNavController().navigate(goToEntryLubang)
        }

        binding.buttonEntryPenanganan.setOnClickListener {
            val goToEntryPenanganan =
                DashboardFragmentDirections.actionDashboardFragmentToEntryPenangananFragment()
            findNavController().navigate(goToEntryPenanganan)
        }

        binding.buttonEntryRencana.setOnClickListener {
            val goToEntryRencana =
                DashboardFragmentDirections.actionDashboardFragmentToEntryRencanaFragment()
            findNavController().navigate(goToEntryRencana)
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    if (it.isLoading) {
                        loadingDialog.show(childFragmentManager, "Loading Dialog")
                    }

                    if (it.isFailed) {
                        loadingDialog.dismiss()
                    }

                    if (it.isSuccess && it.user == null) {
                        loadingDialog.dismiss()
                        val goToLoginFragment =
                            DashboardFragmentDirections.actionDashboardFragmentToLoginFragment()
                        findNavController().navigate(goToLoginFragment)
                    }
                }
            }
        }
    }
}