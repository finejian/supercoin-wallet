package com.supercoin.scwallet

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_query_eth.*
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import java.lang.ref.WeakReference

class QueryEthActivity : BaseActivity() {

    override fun displayBackButton(): Boolean {
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_eth)

        ConnectWeb3jTask(this).execute()
    }

    private class ConnectWeb3jTask(controller: QueryEthActivity) : AsyncTask<Void, Void, String>() {
        private val controller = WeakReference<QueryEthActivity>(controller)

        override fun doInBackground(vararg p0: Void?): String {
            // https://github.com/web3j/web3j

            val web3 = Web3jFactory.build(HttpService("https://mainnet.infura.io/LQRs8Y2bwWmULQZHJ9bg"))

            val poloniexWalletAddr = "0x32be343b94f860124dc4fee278fdcbd38c102d88"
            return "target address eth balance:\n ${web3.ethGetBalance(poloniexWalletAddr, DefaultBlockParameterName.LATEST).sendAsync().get().balance}  \n\n" +
                    "block number:\n ${web3.ethBlockNumber().sendAsync().get().blockNumber}  \n\n" +
                    "client version:\n ${web3.web3ClientVersion().sendAsync().get().web3ClientVersion} "
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            controller.get()?.progressbar?.visibility = View.GONE
            controller.get()?.result?.text = result
        }
    }
}
