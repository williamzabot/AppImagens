package com.williamzabot.appimagens.extensions

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.hideKeyboard() {
    (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    window.setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}