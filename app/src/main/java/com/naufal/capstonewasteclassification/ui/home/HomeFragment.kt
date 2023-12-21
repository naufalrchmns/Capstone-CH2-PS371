package com.naufal.capstonewasteclassification.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.naufal.capstonewasteclassification.R
import com.naufal.capstonewasteclassification.databinding.FragmentHomeBinding
import com.naufal.capstonewasteclassification.ui.scan.SharedViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var clickCountTextView: TextView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        sharedViewModel.count.observe(viewLifecycleOwner, Observer { count ->
           binding.jumlahsampah.text = count.toString() // Update TextView with count value
        })



        // Set up click listener for the menu button

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun updateClickCount(clickCount: Int) {
        clickCountTextView.text = clickCount.toString()
    }
}