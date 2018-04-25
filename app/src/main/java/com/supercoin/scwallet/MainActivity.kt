package com.supercoin.scwallet

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_key_store.setOnClickListener { startActivity(Intent(this, KeystoreActivity::class.java)) }
        btn_web3j.setOnClickListener { startActivity(Intent(this, QueryEthActivity::class.java)) }
    }
}