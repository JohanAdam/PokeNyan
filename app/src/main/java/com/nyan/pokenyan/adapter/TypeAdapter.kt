package com.nyan.pokenyan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nyan.domain.entity.TypesItem
import com.nyan.pokenyan.databinding.ListItemTypesBinding

class TypeAdapter:
    ListAdapter<TypesItem, TypeAdapter.TypeViewHolder>(DIFF_CALLBACK) {

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<TypesItem>() {
        override fun areItemsTheSame(oldItem: TypesItem, newItem: TypesItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TypesItem, newItem: TypesItem): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TypeViewHolder {
        return TypeViewHolder(
            ListItemTypesBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = getItem(position)
        holder.bind(type)
    }

    class TypeViewHolder(private var binding: ListItemTypesBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(type: TypesItem?) {
            binding.type = type
            binding.executePendingBindings()
        }

    }

}