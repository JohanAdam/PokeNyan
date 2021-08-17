package com.nyan.pokenyan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nyan.domain.entity.PokemonEntity
import com.nyan.pokenyan.databinding.ListItemPokemonBinding
import timber.log.Timber

class PokemonAdapterTest :
    PagingDataAdapter<PokemonEntity, PokemonAdapterTest.PokemonViewHolder>(REPO_COMPARATOR) {

    companion object REPO_COMPARATOR : DiffUtil.ItemCallback<PokemonEntity>() {
        override fun areItemsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonViewHolder {
        return PokemonViewHolder(ListItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bind(pokemon)
    }

    class PokemonViewHolder(private var binding: ListItemPokemonBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonEntity?) {
            binding.data = pokemon
            binding.executePendingBindings()

            //Set on click listener.
            binding.rootView.setOnClickListener {
                pokemon?.let {
                    Timber.i("bind: click ${pokemon.name}")
                }
            }
        }

    }

}