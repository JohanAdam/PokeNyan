package com.nyan.pokenyan.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nyan.domain.entity.StatsItem
import com.nyan.foodie.event.EventObserver
import com.nyan.pokenyan.Constants
import com.nyan.pokenyan.R
import com.nyan.pokenyan.adapter.SingleAdapter
import com.nyan.pokenyan.adapter.TypeAdapter
import com.nyan.pokenyan.binding.bindImage
import com.nyan.pokenyan.databinding.FragmentPokemonDetailsBinding
import com.nyan.pokenyan.ui.BaseFragment
import com.nyan.pokenyan.ui.dialog.DialogLoading
import com.nyan.pokenyan.viewmodel.detail.DetailsStateEvent
import com.nyan.pokenyan.viewmodel.detail.PokemonDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

class PokemonDetailsFragment : BaseFragment() {

    companion object {
        const val POKEMON_ID: String = "daId"
    }

    private var _binding: FragmentPokemonDetailsBinding? = null
    private val binding get() = _binding!!

    //    private val viewModel: PokemonDetailsViewModel by viewModel {
//        parametersOf(arguments?.getInt(POKEMON_ID))
//    }
    private val viewModel: PokemonDetailsViewModel by viewModels()

    private val typeAdapter by lazy {
        TypeAdapter()
    }

    private val abilitiesAdapter by lazy {
        SingleAdapter()
    }

    private val movesAdapter by lazy {
        SingleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailsBinding.inflate(inflater)
        val view = binding.root

        //Received the data sent from list.
        val id = PokemonDetailsFragmentArgs.fromBundle(requireArguments()).pokemonId
        val name = PokemonDetailsFragmentArgs.fromBundle(requireArguments()).pokemonName
        arguments = bundleOf(POKEMON_ID to id)

        Timber.i("onCreateView: Name is $name")

        viewModel.getPokemonDetails(arguments?.getInt(POKEMON_ID))
        setupView()
        setupObserver()

        return view
    }

    private fun setupView() {
        //Setup types adapter.
        binding.rvTypes.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvTypes.adapter = typeAdapter

        //Setup abilities adapter.
        binding.rvAbilities.adapter = abilitiesAdapter

        //Setup moves adapter.
        binding.rvMoves.adapter = movesAdapter
    }

    private var dialogLoading: DialogFragment? = null
    private fun setupObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, EventObserver {
            Timber.i("setupObserver: loading $it")
            if (it) {
                dialogLoading = DialogLoading.newInstance()
                dialogLoading?.show(
                    requireActivity().supportFragmentManager,
                    DialogLoading::class.java.simpleName
                )
            } else {
                dialogLoading?.dismiss()
            }
        })

        viewModel.errorMsg.observe(viewLifecycleOwner, EventObserver {
            Timber.e("setupObserver: error ${it}")
            showErrorSnackbar(it)
        })

        viewModel.pokemonDetails.observe(viewLifecycleOwner, Observer {
            Timber.e("setupObserver: YAYY")

            bindImage(binding.ivSprite, it.sprites?.frontDefault)

            typeAdapter.submitList(it.types)

            val weightInKg = it.weight?.times(0.1)
            val heightInCm = it.height?.times(10)

            binding.tvWeight.text =
                getString(R.string.kg, String.format("%.1f", weightInKg))//Hectorgrams > Kg
            binding.tvHeight.text = getString(R.string.cm, heightInCm.toString()) //Decimeters > Cm

            //Stats.
            it.stats?.let { statsList ->
                val hp = getStats(Constants.hp, statsList)
                binding.tvHp.text = getString(R.string.hp, hp.toString())
                binding.pbHp.max = Constants.maxStats
                binding.pbHp.progress = hp

                val atk = getStats(Constants.attack, statsList)
                binding.tvAtk.text = getString(R.string.attack, atk.toString())
                binding.pbAtk.max = Constants.maxStats
                binding.pbAtk.progress = atk

                val def = getStats(Constants.defense, statsList)
                binding.tvDef.text = getString(R.string.defense, def.toString())
                binding.pbDef.max = Constants.maxStats
                binding.pbDef.progress = def

                val sAtk = getStats(Constants.special_attack, statsList)
                binding.tvSAtk.text = getString(R.string.s_attack, sAtk.toString())
                binding.pbSAtk.max = Constants.maxStats
                binding.pbSAtk.progress = sAtk

                val sDef = getStats(Constants.special_defense, statsList)
                binding.tvSDef.text = getString(R.string.s_defend, sDef.toString())
                binding.pbSDef.max = Constants.maxStats
                binding.pbSDef.progress = sDef

                val spd = getStats(Constants.speed, statsList)
                binding.tvSpd.text = getString(R.string.speed, spd.toString())
                binding.pbSpd.max = Constants.maxStats
                binding.pbSpd.progress = spd
            }

            abilitiesAdapter.submitList(it.abilities)

            movesAdapter.submitList(it.moves)
        })

    }

    private fun showErrorSnackbar(errorMsg: String) {
        Snackbar.make(binding.root, errorMsg, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_retry) {
                viewModel.getPokemonDetails(arguments?.getInt(POKEMON_ID))
            }
            .show()

    }

    private fun getStats(stat: String, statsList: List<StatsItem?>): Int {
        for (item in statsList) {
            if (item?.stat?.name.equals(stat)) {
                return item?.baseStat!!
            }
        }
        return 0
    }

}