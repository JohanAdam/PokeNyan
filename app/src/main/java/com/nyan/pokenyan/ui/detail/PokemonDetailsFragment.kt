package com.nyan.pokenyan.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.nyan.foodie.event.EventObserver
import com.nyan.pokenyan.adapter.TypeAdapter
import com.nyan.pokenyan.binding.bindImage
import com.nyan.pokenyan.databinding.FragmentPokemonDetailsBinding
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

    private val typeAdapter by lazy {
        TypeAdapter()
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

        setupView()
        setupObserver()

        return view
    }

    private fun setupView() {
        //Setup adapter.
        binding.rvTypes.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTypes.adapter = typeAdapter
    }

    private fun setupObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, EventObserver {
            Timber.i("setupObserver: loading $it")
            binding.layoutLoading.root.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            Timber.e("setupObserver: error ${it}")
        })

        viewModel.pokemonDetails.observe(viewLifecycleOwner, Observer {
            Timber.e("setupObserver: YAYY")
            binding.tvNames.text = it.name

            bindImage(binding.ivSprite, it.sprites?.frontDefault)

            typeAdapter.submitList(it.types)
        })

    }

}