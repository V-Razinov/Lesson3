package com.example.lesson3.other.extensions

import android.widget.EditText

inline fun EditText.doOnEditorAction(actionId: Int, crossinline action: (EditText) -> Unit) {
    setOnEditorActionListener { _, id, _ ->
        if (id == actionId)
            action(this)
        false
    }
}