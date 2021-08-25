package com.nyan.pokenyan.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.nyan.foodie.event.EventObserver
import com.nyan.pokenyan.databinding.FragmentPokemonDetailsBinding
import com.nyan.pokenyan.viewmodel.detail.DetailsStateEvent
import com.nyan.pokenyan.viewmodel.detail.PokemonDetailsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class PokemonDetailsFragment: Fragment() {

    companion object  {
        const val POKEMON_ID: String = "daId"
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

        setupObserver()

        return view
    }

    private fun setupObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, EventObserver {
            Timber.i("setupObserver: loading $it")
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            Timber.e("setupObserver: error ${it}")
        })

        viewModel.pokemonDetails.observe(viewLifecycleOwner, Observer {
            Timber.e("setupObserver: YAYY")
        })

    }

}