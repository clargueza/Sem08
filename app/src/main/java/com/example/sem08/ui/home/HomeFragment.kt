package com.example.sem08.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sem08.R
import com.example.sem08.databinding.FragmentHomeBinding
import com.example.sem08.viewmodel.HomeViewModel

class LugarFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.addLugarFabBt.setOnClickListener() {
            findNavController().navigate(R.id.action_nav_home_to_addLugar)
        }
       /* //Activacion del RecyclerView
        val lugarAdapter=HomeAdapter()  //Un objeto del adaptador que se desarrolló para dibujar los lugares en cada cajita
        val reciclador = binding.reciclador //Se recupera el RecyclerView de la vista...
        reciclador.adapter = lugarAdapter  //Se asocia el adaptador programado al reciclador de la vista
        reciclador.layoutManager = LinearLayoutManager(requireContext()) */

      //  homeViewModel.obtenerLugares.observe(viewLifecycleOwner) { lugares ->            lugarAdapter.setData(lugares)
        //}
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
