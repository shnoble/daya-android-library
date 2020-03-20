package com.daya.accounts.authenticator

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import com.daya.accounts.authenticator.DayaAccountConstants.KEY_AUTH_TOKEN_TYPE
import com.daya.accounts.authenticator.sample.R
import com.daya.android.common.DayaLog


class DayaAccountAuthenticatorActivity : AccountAuthenticatorActivity() {

    private lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daya_account_authenticator)

        accountManager = AccountManager.get(this)

        val accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)
        val accounts = accountManager.getAccountsByType(accountType)
        val startIntent = if (accounts.isNotEmpty()) {
            Intent(this, DayaAccountSelectorActivity::class.java)
        } else {
            Intent(this, DayaAccountCreatorActivity::class.java)
        }

        intent.extras?.let { startIntent.putExtras(it) }
        startActivityForResult(startIntent, REQUEST_ACCOUNT)
    }

    private fun setResult(intent: Intent) {
        val accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
        val accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)
        val authTokenType = intent.getStringExtra(KEY_AUTH_TOKEN_TYPE)
        val authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN)
        DayaLog.d(TAG, """
            Result: 
                accountName = $accountName
                accountType = $accountType
                authTokenType = $authTokenType
                authToken = $authToken
        """.trimIndent())

        val account = Account(accountName, accountType)
        accountManager.addAccountExplicitly(account, null, null)
        accountManager.setAuthToken(account, authTokenType, authToken)
        setAccountAuthenticatorResult(intent.extras)
        setResult(RESULT_OK, intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                data?.let { setResult(it) }
            }
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "DayaAccountAuthenticatorActivity"
        private const val REQUEST_ACCOUNT = 100
    }
}
