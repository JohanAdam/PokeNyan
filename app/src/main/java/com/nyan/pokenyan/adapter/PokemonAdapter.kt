package com.nyan.pokenyan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nyan.domain.entity.PokemonEntity
import com.nyan.pokenyan.databinding.ListItemPokemonBinding

class PokemonAdapter(private val onClickListener: (PokemonEntity) -> Unit): ListAdapter<PokemonEntity, PokemonAdapter.PokemonViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<PokemonEntity>() {
        override fun areItemsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem.name == newItem.name
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
        holder.bind(pokemon, onClickListener)
    }

    class PokemonViewHolder(private var binding: ListItemPokemonBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonEntity, onClickListener: (PokemonEntity) -> Unit) {
            binding.data = pokemon
            binding.executePendingBindings()

            //Set on click listener.
            binding.rootView.setOnClickListener {
                onClickListener(pokemon)
            }
        }

    }

}