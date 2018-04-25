package com.supercoin.scwallet

import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_keystore.*
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.crypto.Wallet
import java.lang.ref.WeakReference

class KeystoreActivity : BaseActivity() {

    override fun displayBackButton(): Boolean {
        return true
    }

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

        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = "please input 8-20 bit cipher."
            focusView = password
        }

        if (focusView != null) {
            focusView.requestFocus()
            return
        }

        CreateWalletFileTask(this, passwordStr).execute()
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length in 8..20
    }


    private class CreateWalletFileTask(controller: KeystoreActivity, val password: String) : AsyncTask<Void, Void, String>() {
        private val controller = WeakReference<KeystoreActivity>(controller)

        override fun doInBackground(vararg p0: Void?): String {
            return try {
                val keyPair = Keys.createEcKeyPair()
                val ecKeyPair = ECKeyPair.create(keyPair.privateKey)
                val privateKey = ecKeyPair.privateKey
                val publicKey = ecKeyPair.publicKey
                val address = Keys.getAddress(ecKeyPair)

                val accountInfo = "account info \n" +
                        "privateKey: ${privateKey.toString().substring(0, 20)} ... \n" +
                        "publicKey: ${publicKey.toString().substring(0, 20)} ... \n" +
                        "address: 0x$address \n" +
                        "\n"

                val keystore = Wallet.createLight(password, ecKeyPair)
                val keystoreInfo = "keystore info \n" +
                        "id: ${keystore.id} \n" +
                        "version: ${keystore.version} \n" +
                        "crypto.cipher: ${keystore.crypto.cipher} \n" +
                        "crypto.ciphertext: ${keystore.crypto.ciphertext.substring(0, 20)} ... \n" +
                        "crypto.kdf: ${keystore.crypto.kdf} \n" +
                        "crypto.mac: ${keystore.crypto.mac.substring(0, 20)} ... \n" +
                        "\n"

                val decryptECKeyPair = Wallet.decrypt(password, keystore)
                val decryptAccountInfo = "account info by decrypt keystore \n" +
                        "privateKey: ${decryptECKeyPair.privateKey.toString().substring(0, 20)} ... \n" +
                        "publicKey: ${decryptECKeyPair.publicKey.toString().substring(0, 20)} ... \n" +
                        "address: 0x${Keys.getAddress(decryptECKeyPair)} \n" +
                        "\n"


                return accountInfo + keystoreInfo + decryptAccountInfo
            } catch (e: Exception) {
                e.printStackTrace()
                e.cause.toString()
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            controller.get()!!.result.text = result
        }
    }
}
