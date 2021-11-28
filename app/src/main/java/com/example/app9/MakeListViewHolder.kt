package com.example.app9

import android.text.InputType
import android.view.MotionEvent
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.app9.databinding.RecyclerListItemBinding

class MakeListViewHolder(val binding: RecyclerListItemBinding, listItemListener: ListItemListener) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.ListItem.setOnNextAction {
            listItemListener.onMoveToNext(adapterPosition)
        }

        binding.CheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            binding.ListItem.paint.isStrikeThruText = isChecked
            binding.ListItem.isEnabled = !isChecked

            listItemListener.onItemCheckedChange(adapterPosition, isChecked)
        }

        binding.ListItem.addTextChangedListener(onTextChanged = { text, start, count, after ->
            listItemListener.onItemTextChange(adapterPosition, text.toString())
        })

        binding.DragHandle.setOnTouchListener { v, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                listItemListener.onStartDrag(this)
            }
            false
        }
    }

    fun bind(item: ListItem) {
        binding.ListItem.setText(item.body)
        binding.CheckBox.isChecked = item.checked
        binding.ListItem.setRawInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
    }

    fun requestFocus() = binding.ListItem.requestFocus()
}