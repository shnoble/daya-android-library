package com.daya.accounts.authenticator

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.daya.accounts.authenticator.sample.R
import com.daya.android.common.DayaLog

class DayaAccountSelectorActivity : AppCompatActivity() {

    private lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daya_account_selector)

        accountManager = AccountManager.get(this)

        val accounts = accountManager.accounts
        DayaLog.d(TAG, "Account size: ${accounts.size}")
        accounts.forEach {
            DayaLog.d(
                TAG, """
                Account type: ${it.type}
                Account name: ${it.name}
            """.trimIndent())
        }

        var accountsText = ""
        accounts.forEach {
            accountsText += "Account: ${it.name}\n"
        }
        findViewById<TextView>(R.id.accounts).text = accountsText

        findViewById<Button>(R.id.select).setOnClickListener {
            if (accounts.isNotEmpty()) {
                val account = accounts[0]
                val data = Bundle().apply {
                    putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
                    putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
                    putString(DayaAccountConstants.KEY_AUTH_TOKEN_TYPE, DayaAccountConstants.AUTH_TOKEN_TYPE_SIMPLE)
                    putString(AccountManager.KEY_AUTHTOKEN, accountManager.peekAuthToken(account, DayaAccountConstants.AUTH_TOKEN_TYPE_SIMPLE))
                }
                val result = Intent()
                result.putExtras(data)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }

    companion object {
        private const val TAG = "DayaAccountSelectorActivity"
    }
}
