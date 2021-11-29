package com.example.app9

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.app9.databinding.RecyclerBaseNoteBinding
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat

class BaseNoteAdapter(

    private val settingsHelper: SettingsHelper,
    private val formatter: SimpleDateFormat,
    private val itemListener: ItemListener
) : ListAdapter<BaseNote, BaseNoteViewHolder>(DiffCallback()) {

    private val prettyTime = PrettyTime()

    override fun onBindViewHolder(holder: BaseNoteViewHolder, position: Int) {
        val baseNote = getItem(position)
        holder.bind(baseNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseNoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerBaseNoteBinding.inflate(inflater, parent, false)
        return BaseNoteViewHolder(binding, settingsHelper, itemListener, prettyTime, formatter, inflater)
    }


    private class DiffCallback : DiffUtil.ItemCallback<BaseNote>() {

        override fun areItemsTheSame(oldItem: BaseNote, newItem: BaseNote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BaseNote, newItem: BaseNote): Boolean {
            return oldItem == newItem
        }
    }
}