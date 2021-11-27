package com.example.app9

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app9.databinding.LabelBinding
import com.google.android.material.chip.ChipGroup
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