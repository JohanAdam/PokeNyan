package com.nyan.pokenyan.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.nyan.pokenyan.databinding.FragmentPokemonDetailsBinding
import com.nyan.pokenyan.viewmodel.detail.PokemonDetailsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PokemonDetailsFragment: Fragment() {

    companion object  {
        val POKEMON_ID: String = "daId"
    }

    private var _binding: FragmentPokemonDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonDetailsViewModel by viewModel {
        parametersOf(arguments?.getInt(POKEMON_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokemonDetailsBinding.inflate(inflater)
        val view = binding.root

        //Received the data sent from list.
        val id = PokemonDetailsFragmentArgs.fromBundle(requireArguments()).pokemonId
        arguments = bundleOf(POKEMON_ID to id)

        return view
    }

}