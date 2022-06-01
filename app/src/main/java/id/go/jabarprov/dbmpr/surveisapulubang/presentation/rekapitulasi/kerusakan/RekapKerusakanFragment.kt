package id.go.jabarprov.dbmpr.surveisapulubang.presentation.rekapitulasi.kerusakan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import id.go.jabarprov.dbmpr.surveisapulubang.R
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentRekapKerusakanBinding

@AndroidEntryPoint
class RekapKerusakanFragment : Fragment() {

    private val kerusakanLubangFragment by lazy { RekapKerusakanLubangFragment() }
    private val kerusakanPotensiFragment by lazy { RekapKerusakanPotensiFragment() }

    private lateinit var binding: FragmentRekapKerusakanBinding

    private var currentFragment: Fragment = kerusakanLubangFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRekapKerusakanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        if (childFragmentManager.fragments.isEmpty()) {
            initChildFragment()
        }
    }

    private fun initUI() {
        binding.apply {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }

            tabLayoutCategory.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> navigateFragment(kerusakanLubangFragment)
                        1 -> navigateFragment(kerusakanPotensiFragment)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

                override fun onTabReselected(tab: TabLayout.Tab?) = Unit
            })
        }
    }

    private fun initChildFragment() {
        childFragmentManager.beginTransaction()
            .add(R.id.frame_layout_fragment_container, kerusakanPotensiFragment)
            .hide(kerusakanPotensiFragment)
            .add(R.id.frame_layout_fragment_container, kerusakanLubangFragment)
            .commit()
    }

    private fun navigateFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .hide(currentFragment).apply {
                currentFragment = fragment
            }
            .show(fragment)
            .commit()
    }

}