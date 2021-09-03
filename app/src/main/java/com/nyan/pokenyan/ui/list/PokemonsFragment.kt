package com.nyan.pokenyan.ui.list

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nyan.domain.entity.PokemonEntity
import com.nyan.foodie.event.EventObserver
import com.nyan.pokenyan.R
import com.nyan.pokenyan.adapter.LoaderStateAdapter
import com.nyan.pokenyan.adapter.PokemonsAdapter
import com.nyan.pokenyan.databinding.FragmentPokemonsBinding
import com.nyan.pokenyan.viewmodel.list.PokemonsViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.bind
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

    private val listener = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            pokemonAdapter.submitData(lifecycle, PagingData.empty())
            viewModel.searchPokemon(s.toString())
        }

        override fun afterTextChanged(s: Editable?) {
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

    override fun onResume() {
        super.onResume()
        binding.etSearch.addTextChangedListener(listener)
    }

    override fun onPause() {
        super.onPause()
        binding.etSearch.removeTextChangedListener(listener)
    }

    private fun setupView() {
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
                viewModel.getPokemons()
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
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.action_retry)
                ) {
                    viewModel.getPokemons()
                }
                .show()
//            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

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