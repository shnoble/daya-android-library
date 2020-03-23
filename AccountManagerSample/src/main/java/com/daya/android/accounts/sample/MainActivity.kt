package com.daya.android.accounts.sample

import android.accounts.Account
import android.accounts.AccountManager
import android.accounts.AccountManager.KEY_ACCOUNT_NAME
import android.accounts.AccountManager.KEY_ACCOUNT_TYPE
import android.accounts.AuthenticatorException
import android.accounts.OperationCanceledException
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.daya.android.common.DayaLog
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var accountManager: AccountManager

    private val accountType
        get() = getString(R.string.account_type)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accountManager = AccountManager.get(this)

        findViewById<Button>(R.id.login_button).setOnClickListener {
            addAccount()
        }

        findViewById<Button>(R.id.choose_account_button).setOnClickListener {
            chooseAccount()
        }

        findViewById<Button>(R.id.show_accounts_button).setOnClickListener {
            showAccounts()
        }
    }

    private fun addAccount() {
        val addAccountOptions = Bundle().apply {
            putBoolean("SelectAccount", true)
        }
        accountManager.addAccount(accountType, null, null, addAccountOptions, this, {
            DayaLog.d(TAG, "Add account result.")
            try {
                val bundle = it.result
                DayaLog.d(
                    TAG, """Add account result: 
                        Account name: ${bundle.getString(AccountManager.KEY_ACCOUNT_NAME)}
                        Account type: ${bundle.getString(AccountManager.KEY_ACCOUNT_TYPE)}
                    """.trimMargin()
                )
                val account = Account(
                    bundle.getString(AccountManager.KEY_ACCOUNT_NAME),
                    bundle.getString(AccountManager.KEY_ACCOUNT_TYPE)
                )
                getAuthToken(account)

            } catch (e: AuthenticatorException) {
                e.printStackTrace()
            } catch (e: OperationCanceledException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }, Handler())
    }

    private fun getAuthToken(account: Account) {
        accountManager.getAuthToken(account, "simpleToken", null, this, {
            val result = it.result
            DayaLog.d(
                TAG, """Get auth token result: 
                        Account name: ${result.getString(AccountManager.KEY_ACCOUNT_NAME)}
                        Account type: ${result.getString(AccountManager.KEY_ACCOUNT_TYPE)}
                        Auth token: ${result.getString(AccountManager.KEY_AUTHTOKEN)}
                    """.trimMargin()
            )
        }, Handler())
    }

    private fun chooseAccount() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            DayaLog.d(TAG, "Account type: $accountType")
            val intent = AccountManager.newChooseAccountIntent(
                null,
                null,
                arrayOf(accountType),
                null,
                null,
                null,
                null
            )
            startActivityForResult(intent, REQUEST_CHOOSE_ACCOUNT)
        }
    }

    private fun showAccounts() {
        val accounts = accountManager.getAccountsByType(accountType)
        DayaLog.d(TAG, "Account size: ${accounts.size}")
        accounts.forEach {
            DayaLog.d(
                TAG, """
                Account type: ${it.type}
                Account name: ${it.name}
            """.trimIndent()
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION) {
            val accountManager = AccountManager.get(this)
            val accounts = accountManager.getAccountsByType(accountType)
            accounts.forEach {
                Log.d(localClassName + "aaaaa1", it.toString())
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHOOSE_ACCOUNT) {
            if (resultCode == Activity.RESULT_OK) {
                DayaLog.d(TAG, "Success to choose account.")
                DayaLog.d(
                    TAG, """
                        Success to choose account: 
                            Account type: ${data?.getStringExtra(KEY_ACCOUNT_TYPE)}
                            Account name: ${data?.getStringExtra(KEY_ACCOUNT_NAME)}
                    """.trimIndent()
                )

            } else {
                DayaLog.d(TAG, "Failed to choose account.")
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CHOOSE_ACCOUNT = 100
        private const val REQUEST_PERMISSION = 101
    }
}


