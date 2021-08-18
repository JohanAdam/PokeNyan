package com.nyan.pokenyan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nyan.domain.entity.PokemonEntity
import com.nyan.pokenyan.databinding.ListItemPokemonBinding

class PokemonsAdapter(private val onItemClick: (PokemonEntity) -> Unit):
    PagingDataAdapter<PokemonEntity, PokemonsAdapter.PokemonViewHolder>(DIFF_CALLBACK) {

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<PokemonEntity>() {
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
        return PokemonViewHolder(
            ListItemPokemonBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = getItem(position)
        holder.bind(pokemon, onItemClick)
    }

    class PokemonViewHolder(private var binding: ListItemPokemonBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonEntity?, onItemClick: (PokemonEntity) -> Unit) {
            binding.data = pokemon
            binding.executePendingBindings()

            //Set on click listener.
            binding.rootView.setOnClickListener {
                pokemon?.let {
                    onItemClick(pokemon)
                }
            }
        }

    }

}