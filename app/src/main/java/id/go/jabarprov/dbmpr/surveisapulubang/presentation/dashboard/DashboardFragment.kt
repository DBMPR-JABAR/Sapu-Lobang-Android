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
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.core.Resource
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentDashboardBinding
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.Role
import id.go.jabarprov.dbmpr.surveisapulubang.domain.entities.User
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.models.MenuDashboardItem
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.AuthViewModel
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.viewmodels.user.store.AuthAction
import id.go.jabarprov.dbmpr.surveisapulubang.presentation.widgets.LoadingDialog
import id.go.jabarprov.dbmpr.surveisapulubang.utils.extensions.showToast
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private val authViewModel: AuthViewModel by activityViewModels()

    lateinit var binding: FragmentDashboardBinding

    private val loadingDialog by lazy { LoadingDialog.create() }

    private val listMenuDashboardItem by lazy {
        mutableListOf<MenuDashboardItem>()
    }

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

        binding.apply {
            recyclerViewMenuDashboard.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = MenuDashboardAdapter(listMenuDashboardItem)
                setHasFixedSize(true)
            }
        }
    }

    private fun observeAuthState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.uiState.collect {
                    processUserState(it.userState)
                }
            }
        }
    }

    private fun processUserState(state: Resource<User>) {
        when (state) {
            is Resource.Failed -> {
                loadingDialog.dismiss()
            }
            is Resource.Initial -> {
                loadingDialog.dismiss()
                val goToLoginFragment =
                    DashboardFragmentDirections.actionDashboardFragmentToLoginFragment()
                findNavController().navigate(goToLoginFragment)
            }
            is Resource.Loading -> {
                loadingDialog.show(childFragmentManager)
            }
            is Resource.Success -> {
                processUserRole(state.data.internalRole)
            }
        }
    }

    private fun processUserRole(role: Role) {
        when (role) {
            Role.ADMINISTRATOR -> {
                listMenuDashboardItem.clear()
                listMenuDashboardItem.addAll(
                    listOf(
                        MenuDashboardItem(
                            image = R.drawable.img_entry_lubang,
                            description = "Entry Jumlah Lubang",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryLubangFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_entry_rencana,
                            description = "Entry Rencana Penanganan",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryRencanaFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_entry_penanganan,
                            description = "Entry Penanganan Lubang",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryPenangananFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_rekap,
                            description = "Rekap Hasil Survei",
                            buttonText = "Lihat Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToRekapitulasiFragment()
                                findNavController().navigate(action)
                            },
                        )
                    )
                )
            }
            Role.KSUP -> {
                listMenuDashboardItem.clear()
                listMenuDashboardItem.addAll(
                    listOf(
                        MenuDashboardItem(
                            image = R.drawable.img_entry_rencana,
                            description = "Entry Rencana Penanganan",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryRencanaFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_rekap,
                            description = "Rekap Hasil Survei",
                            buttonText = "Lihat Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToRekapitulasiFragment()
                                findNavController().navigate(action)
                            },
                        )
                    )
                )
            }
            Role.MANDOR -> {
                listMenuDashboardItem.clear()
                listMenuDashboardItem.addAll(
                    listOf(
                        MenuDashboardItem(
                            image = R.drawable.img_entry_lubang,
                            description = "Entry Jumlah Lubang",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryLubangFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_entry_penanganan,
                            description = "Entry Penanganan Lubang",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryPenangananFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_rekap,
                            description = "Rekap Hasil Survei",
                            buttonText = "Lihat Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToRekapitulasiFragment()
                                findNavController().navigate(action)
                            },
                        )
                    )
                )
            }
            Role.PENGAMAT -> {
                listMenuDashboardItem.clear()
                listMenuDashboardItem.addAll(
                    listOf(
                        MenuDashboardItem(
                            image = R.drawable.img_entry_lubang,
                            description = "Entry Jumlah Lubang",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryLubangFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_entry_penanganan,
                            description = "Entry Penanganan Lubang",
                            buttonText = "Entry Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToEntryPenangananFragment()
                                findNavController().navigate(action)
                            },
                        ),
                        MenuDashboardItem(
                            image = R.drawable.img_rekap,
                            description = "Rekap Hasil Survei",
                            buttonText = "Lihat Data",
                            onClickAction = {
                                val action =
                                    DashboardFragmentDirections.actionDashboardFragmentToRekapitulasiFragment()
                                findNavController().navigate(action)
                            },
                        )
                    )
                )
            }
            Role.UNSUPPORTED -> {
                showToast("Role Tidak Dapat Digunakan")
                authViewModel.processAction(AuthAction.LogoutUserAction)
            }
        }
    }
}