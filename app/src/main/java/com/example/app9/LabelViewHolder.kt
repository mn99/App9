package com.example.app9

import androidx.recyclerview.widget.RecyclerView
import com.example.app9.databinding.RecyclerLabelBinding

class LabelViewHolder(private val binding: RecyclerLabelBinding, itemListener: ItemListener) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            itemListener.onClick(adapterPosition)
        }

        binding.root.setOnLongClickListener {
            itemListener.onLongClick(adapterPosition)
            return@setOnLongClickListener true
        }
    }

    fun bind(label: Label) {
        binding.root.text = label.value
    }
}