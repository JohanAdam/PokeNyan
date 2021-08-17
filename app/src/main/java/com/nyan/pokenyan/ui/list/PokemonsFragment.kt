package com.nyan.pokenyan.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyan.domain.entity.PokemonEntity
import com.nyan.foodie.event.EventObserver
import com.nyan.pokenyan.adapter.PokemonAdapter
import com.nyan.pokenyan.databinding.FragmentPokemonsBinding
import com.nyan.pokenyan.viewmodel.list.PokemonsViewModel
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PokemonsFragment: Fragment() {

    private var _binding: FragmentPokemonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonsViewModel by viewModel()

    private val pokemonAdapter by lazy {
        PokemonAdapter { pokemon ->
            Timber.i("Select pokemon %s", pokemon.name)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPokemonsBinding.inflate(inflater)
        val view = binding.root

        setupView()
        setupObserver()

        return view
    }

    private fun setupView() {
        binding.rv.layoutManager = LinearLayoutManager(context)
    }

    private fun setupObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, EventObserver {
            displayProgressBar(it)
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.listPokemon.observe(viewLifecycleOwner, {
            displayData(it)
        })

    }

    private fun displayProgressBar(isLoading: Boolean) {
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE

        if (isLoading) {
            pokemonAdapter.submitList(null)
        }
    }

    private fun displayData(data: List<PokemonEntity>?) {
        binding.rv.adapter = pokemonAdapter
        pokemonAdapter.submitList(data)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}