package com.example.basicbankingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.basicbankingapp.databinding.FragmentHomeBinding
import com.example.basicbankingapp.HomeFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        bottomBarVisibility(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.buttonViewAll.setOnClickListener {
            nextScreen()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun nextScreen(){
        val action = HomeFragmentDirections.actionNavigationHomeToNavigationCustomer()
        findNavController().navigate(action)
        bottomBarVisibility(true)
    }

    fun bottomBarVisibility(visibility: Boolean) {
        val bottomBar: BottomNavigationView? = activity?.findViewById(R.id.nav_view)
        if (bottomBar != null) {
            if (visibility) {
                bottomBar.visibility = View.VISIBLE
            } else {
                bottomBar.visibility = View.GONE

            }
        }
    }




}

