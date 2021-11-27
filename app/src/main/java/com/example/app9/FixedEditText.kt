package com.example.app9

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class FixedEditText(context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    var isActionModeOn = false

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (!isActionModeOn) {
            super.onWindowFocusChanged(hasWindowFocus)
        }
    }
}