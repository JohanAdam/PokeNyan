package com.nyan.pokenyan.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.nyan.domain.entity.PokemonEntity
import com.nyan.foodie.event.EventObserver
import com.nyan.pokenyan.adapter.LoaderStateAdapter
import com.nyan.pokenyan.adapter.PokemonsAdapter
import com.nyan.pokenyan.databinding.FragmentPokemonsBinding
import com.nyan.pokenyan.viewmodel.list.PokemonsViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PokemonsFragment : Fragment() {

    private var _binding: FragmentPokemonsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PokemonsViewModel by viewModel()

    private val pokemonAdapter by lazy {
        PokemonsAdapter { selectedPokemon ->
            viewModel.openPokemonDetails(selectedPokemon)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonsBinding.inflate(inflater)
        val view = binding.root

        setupView()
        setupObserver()

        return view
    }

    private fun setupView() {
//        binding.rv.layoutManager = LinearLayoutManager(context)

        //adapter setup.
        val loaderStateAdapter = LoaderStateAdapter {
            pokemonAdapter.retry()
        }
        binding.rv.adapter = pokemonAdapter.withLoadStateFooter(loaderStateAdapter)

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == pokemonAdapter.itemCount && loaderStateAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
        binding.rv.layoutManager = gridLayoutManager

        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                pokemonAdapter.refresh()
            }
        }

        //Listen for pagination load.
        pokemonAdapter.addLoadStateListener { loadState ->
            //Show progressbar if the list is empty && when load state is loading.
            if (loadState.refresh is LoadState.Loading && pokemonAdapter.snapshot().isEmpty()) {
                displayProgressBar(true)
            } else {
                displayProgressBar(false)
            }
        }
    }

    private fun setupObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, EventObserver {
            displayProgressBar(it)
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

//        viewModel.listPokemon.observe(viewLifecycleOwner, {
//            displayData(it)
//        })

        viewModel.listPokemon.observe(viewLifecycleOwner, {
            displayData(it)
        })

        viewModel.navigateToDetails.observe(viewLifecycleOwner, EventObserver {
            this.findNavController().navigate(PokemonsFragmentDirections.actionShowDetail(it.id, it.name!!))
        })
    }

    private fun displayProgressBar(isLoading: Boolean) {
        Timber.i("displayProgressBar: $isLoading")
        lifecycleScope.launch {
            if (isAdded) {
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (binding.swipeRefreshLayout.isRefreshing) {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    private fun displayData(data: PagingData<PokemonEntity>) {
        lifecycleScope.launch {
            pokemonAdapter.submitData(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}