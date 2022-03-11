package id.go.jabarprov.dbmpr.surveisapulubang.presentation.splash_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import id.go.jabarprov.dbmpr.surveisapulubang.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.delay

class SplashScreenFragment : Fragment() {

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
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            delay(1000)
            val action = SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }
}