package com.nyan.pokenyan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nyan.domain.entity.TypesItem
import com.nyan.pokenyan.databinding.ListItemSingleBinding
import com.nyan.pokenyan.databinding.ListItemTypesBinding

class SingleAdapter:
    ListAdapter<String, SingleAdapter.TypeViewHolder>(DIFF_CALLBACK) {

    companion object DIFF_CALLBACK : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TypeViewHolder {
        return TypeViewHolder(
            ListItemSingleBinding.inflate(
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

    class TypeViewHolder(private var binding: ListItemSingleBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String?) {
            binding.data = data
            binding.executePendingBindings()
        }

    }

}