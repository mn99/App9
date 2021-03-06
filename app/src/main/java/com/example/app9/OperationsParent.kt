package com.example.app9

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.InputType
import android.view.LayoutInflater
import androidx.core.util.forEach
import com.example.app9.databinding.AddLabelBinding
import com.example.app9.databinding.DialogInputBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

interface OperationsParent {

    fun accessContext(): Context

    fun insertLabel(label: Label, onComplete: (success: Boolean) -> Unit)


    fun shareNote(title: String?, body: CharSequence?) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, title)
        intent.putExtra(TakeNote.EXTRA_SPANNABLE, body)
        intent.putExtra(Intent.EXTRA_TEXT, body.toString())
        val chooser = Intent.createChooser(intent, accessContext().getString(R.string.share_note))
        accessContext().startActivity(chooser)
    }

    fun shareNote(title: String?, items: List<ListItem>?) = shareNote(title, items.getBody())


    fun labelNote(labels: List<String>, previousLabels: HashSet<String>, onLabelsUpdated: (labels: HashSet<String>) -> Unit) {
        val checkedLabels = getCheckedLabels(labels, previousLabels)

        val inflater = LayoutInflater.from(accessContext())
        val addLabel = AddLabelBinding.inflate(inflater).root

        val alertDialogBuilder = MaterialAlertDialogBuilder(accessContext())
        alertDialogBuilder.setTitle(R.string.labels)
        alertDialogBuilder.setNegativeButton(R.string.cancel, null)

        if (labels.isNotEmpty()) {
            alertDialogBuilder.setMultiChoiceItems(labels.toTypedArray(), checkedLabels, null)
            alertDialogBuilder.setPositiveButton(R.string.save, null)
        } else alertDialogBuilder.setView(addLabel)

        val dialog = alertDialogBuilder.create()
        dialog.show()

        addLabel.setOnClickListener {
            dialog.dismiss()
            displayAddLabelDialog(previousLabels, onLabelsUpdated)
        }

        dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener {
            val selectedLabels = HashSet<String>()
            dialog.listView.checkedItemPositions.forEach { key, value ->
                if (value) {
                    val label = labels[key]
                    selectedLabels.add(label)
                }
            }
            dialog.dismiss()
            onLabelsUpdated(selectedLabels)
        }
    }

    private fun displayAddLabelDialog(previousLabels: HashSet<String>, onLabelsUpdated: (labels: HashSet<String>) -> Unit) {
        val inflater = LayoutInflater.from(accessContext())
        val binding = DialogInputBinding.inflate(inflater)

        binding.edit.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        binding.edit.filters = arrayOf()

        val dialogBuilder = MaterialAlertDialogBuilder(accessContext())
        dialogBuilder.setTitle(R.string.add_label)
        dialogBuilder.setView(binding.root)
        dialogBuilder.setNegativeButton(R.string.cancel, null)
        dialogBuilder.setPositiveButton(R.string.save, null)

        val dialog = dialogBuilder.show()
        binding.edit.requestFocus()

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val value = binding.edit.text.toString().trim()
            if (value.isNotEmpty()) {
                val label = Label(value)
                insertLabel(label) { success ->
                    if (success) {
                        dialog.dismiss()
                        labelNote(listOf(value), previousLabels, onLabelsUpdated)
                    } else binding.root.error = accessContext().getString(R.string.label_exists)
                }
            } else dialog.dismiss()
        }
    }

    companion object {

        private fun getCheckedLabels(labels: List<String>, previousLabels: HashSet<String>): BooleanArray {
            return labels.map { label -> previousLabels.contains(label) }.toBooleanArray()
        }
    }
}