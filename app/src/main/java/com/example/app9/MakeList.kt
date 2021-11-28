package com.example.app9

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app9.databinding.ActivityMakeListBinding
import java.util.*

class MakeList : MainHelperActivity() {

    private lateinit var adapter: MakeListAdapter
    private lateinit var binding: ActivityMakeListBinding

    override val model: MakeListModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMakeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.EnterTitle.setOnNextAction {
            moveToNext(-1)
        }

        setupListeners()
        setupRecyclerView()
        setupToolbar(binding.Toolbar)

        if (model.isNewNote) {
            if (model.items.isEmpty()) {
                addListItem()
            }
        }

        binding.AddItem.setOnClickListener {
            addListItem()
        }

        setStateFromModel()
    }


    override fun getLabelGroup() = binding.LabelGroup

    override fun getPinnedIndicator() = binding.Pinned

    override fun getPinnedParent() = binding.LinearLayout

    override fun shareNote() = shareNote(model.title, model.items)


    private fun addListItem() {
        val position = model.items.size
        val listItem = ListItem(String(), false)
        model.items.add(listItem)
        adapter.notifyItemInserted(position)
        binding.RecyclerView.post {
            val viewHolder = binding.RecyclerView.findViewHolderForAdapterPosition(position) as MakeListViewHolder?
            viewHolder?.requestFocus()
        }
    }

    private fun setupListeners() {
        binding.EnterTitle.addTextChangedListener(onTextChanged = { text, start, count, after ->
            model.title = text.toString().trim()
        })
    }


    private fun setupRecyclerView() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val drag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipe = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(drag, swipe)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                model.items.removeAt(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                Collections.swap(model.items, viewHolder.adapterPosition, target.adapterPosition)
                adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }
        })

        adapter = MakeListAdapter(model.items, object : ListItemListener {

            override fun onMoveToNext(position: Int) {
                moveToNext(position)
            }

            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                itemTouchHelper.startDrag(viewHolder)
            }

            override fun onItemTextChange(position: Int, newText: String) {
                model.items[position].body = newText
            }

            override fun onItemCheckedChange(position: Int, checked: Boolean) {
                model.items[position].checked = checked
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.RecyclerView)

        binding.RecyclerView.adapter = adapter
        binding.RecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setStateFromModel() {
        val formatter = BaseNoteModel.getDateFormatter(getLocale())

        binding.EnterTitle.setText(model.title)
        binding.DateCreated.text = formatter.format(model.timestamp)

        binding.LabelGroup.bindLabels(model.labels)
    }

    private fun moveToNext(currentPosition: Int) {
        val viewHolder = binding.RecyclerView.findViewHolderForAdapterPosition(currentPosition + 1) as MakeListViewHolder?
        if (viewHolder != null) {
            if (viewHolder.binding.CheckBox.isChecked) {
                moveToNext(currentPosition + 1)
            } else viewHolder.requestFocus()
        } else addListItem()
    }
}