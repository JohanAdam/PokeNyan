package com.nyan.pokenyan.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nyan.domain.entity.PokemonEntity
import com.nyan.foodie.event.EventObserver
import com.nyan.pokenyan.adapter.PokemonAdapter
import com.nyan.pokenyan.adapter.PokemonAdapterTest
import com.nyan.pokenyan.databinding.FragmentPokemonsBinding
import com.nyan.pokenyan.viewmodel.list.PokemonsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
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

    private val pokemonAdapterTest by lazy {
        PokemonAdapterTest()
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getPokemons()
        }

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

//        viewModel.listPokemonTest.observe(viewLifecycleOwner, {
//            Timber.i("setupObserver: Received!")
//            binding.rv.adapter = pokemonAdapterTest
//            lifecycleScope.launch {
//                Timber.i("setupObserver: WEEE")
//                pokemonAdapterTest.submitData(it)
//            }
//        })

        fetchShit()
    }

    private fun fetchShit() {
        Timber.i("fetchShit: ")
        binding.rv.adapter = pokemonAdapterTest
        lifecycleScope.launch {
            viewModel.testPokemonPaging().distinctUntilChanged().collectLatest {
                Timber.i("fetchShit: collectLatest!")
                pokemonAdapterTest.submitData(it)
            }
        }
    }

    private fun displayProgressBar(isLoading: Boolean) {
        Timber.i("displayProgressBar: $isLoading")
        binding.progressBar.visibility = if(isLoading) View.VISIBLE else View.GONE

        if (isLoading) {
            pokemonAdapter.submitList(null)
//            pokemonAdapterTest.submitData(null)
        } else {
            if (binding.swipeRefreshLayout.isRefreshing) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
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