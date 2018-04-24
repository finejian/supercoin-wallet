package com.supercoin.scwallet

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_keystore.*

class KeystoreActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keystore)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptCreate()
                return@OnEditorActionListener true
            }
            false
        })
        create_key_store.setOnClickListener { attemptCreate() }
    }

    private fun attemptCreate() {
        password.error = null
        val passwordStr = password.text.toString()
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = "please input 8-20 bit cipher."
            focusView = password
        }

        if (focusView != null) {
            focusView.requestFocus()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 8..20
    }
}
