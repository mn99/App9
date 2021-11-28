package com.example.app9

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.Spannable
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app9.databinding.LabelBinding
import com.example.app9.databinding.MenuItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

//MainHelperModel.kt
fun ViewModel.executeAsyncWithCallback(function: suspend () -> Unit, callback: (success: Boolean) -> Unit) {
    viewModelScope.launch {
        val success = try {
            withContext(Dispatchers.IO) { function() }
            true
        } catch (exception: SQLiteConstraintException) {
            false
        }
        callback(success)
    }
}

//MainHelperActivity.kt
fun ChipGroup.bindLabels(labels: HashSet<String>) {
    if (labels.isEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        removeAllViews()
        for (label in labels) {
            val inflater = LayoutInflater.from(context)
            val displayLabel = LabelBinding.inflate(inflater).root
            displayLabel.text = label
            addView(displayLabel)
        }
    }
}

//BaseNoteModel.kt
fun Context.getLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else resources.configuration.locale
}

//BaseNoteModel.kt
fun List<ListItem>?.getBody() = buildString {
    this@getBody?.forEachIndexed { index, (body) ->
        appendLine("${(index + 1)}) $body")
    }
}

//BaseNoteModel.kt
fun String.applySpans(representations: List<SpanRepresentation>): Editable {
    val editable = Editable.Factory.getInstance().newEditable(this)
    representations.forEach { (bold, link, italic, monospace, strikethrough, start, end) ->
        if (bold) {
            editable.setSpan(StyleSpan(Typeface.BOLD), start, end)
        }
        if (italic) {
            editable.setSpan(StyleSpan(Typeface.ITALIC), start, end)
        }
        if (link) {
            val url = getURL(start, end)
            editable.setSpan(URLSpan(url), start, end)
        }
        if (monospace) {
            editable.setSpan(TypefaceSpan("monospace"), start, end)
        }
        if (strikethrough) {
            editable.setSpan(StrikethroughSpan(), start, end)
        }
    }
    return editable
}

//Extensions.kt
private fun String.getURL(start: Int, end: Int): String {
    return if (end <= length) {
        TakeNote.getURLFrom(substring(start, end))
    } else TakeNote.getURLFrom(substring(start, length))
}

//Extensions.kt
private fun Spannable.setSpan(span: Any, start: Int, end: Int) {
    try {
        if (end <= length) {
            setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else setSpan(span, start, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
}

//NoteFragment.kt
class Operation(val textId: Int, val drawableId: Int, val operation: () -> Unit)

//NoteFragment.kt
fun Fragment.showMenu(vararg operations: Operation) {
    val context = requireContext()

    val linearLayout = LinearLayout(context)
    linearLayout.orientation = LinearLayout.VERTICAL
    linearLayout.layoutParams = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )

    val dialog = BottomSheetDialog(context)
    dialog.setContentView(linearLayout)

    for (operation in operations) {
        val item = MenuItemBinding.inflate(layoutInflater).root
        item.setText(operation.textId)
        item.setOnClickListener {
            dialog.dismiss()
            operation.operation.invoke()
        }
        item.setCompoundDrawablesRelativeWithIntrinsicBounds(operation.drawableId, 0, 0, 0)
        linearLayout.addView(item)
    }

    dialog.show()
}

//TakeNote.kt
fun TextInputEditText.setOnNextAction(onNext: () -> Unit) {
    setRawInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)

    setOnKeyListener { v, keyCode, event ->
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            onNext()
            return@setOnKeyListener true
        } else return@setOnKeyListener false
    }

    setOnEditorActionListener { v, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            onNext()
            return@setOnEditorActionListener true
        } else return@setOnEditorActionListener false
    }
}