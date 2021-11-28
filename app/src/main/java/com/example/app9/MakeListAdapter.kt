package com.example.app9

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app9.databinding.RecyclerListItemBinding
import java.util.ArrayList

class MakeListAdapter(
    private val items: ArrayList<ListItem>,
    private val listItemListener: ListItemListener
) : RecyclerView.Adapter<MakeListViewHolder>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MakeListViewHolder, position: Int) {
        val listItem = items[position]
        holder.bind(listItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakeListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerListItemBinding.inflate(inflater, parent, false)
        return MakeListViewHolder(binding, listItemListener)
    }
}