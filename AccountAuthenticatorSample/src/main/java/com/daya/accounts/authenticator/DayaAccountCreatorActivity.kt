package com.daya.accounts.authenticator

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.daya.accounts.authenticator.sample.R

class DayaAccountCreatorActivity : AppCompatActivity() {

    private lateinit var accountType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daya_account_creator)

        accountType = intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE)!!

        findViewById<Button>(R.id.create_button).setOnClickListener {
            val id = findViewById<TextView>(R.id.id).text.toString()
            val password = findViewById<TextView>(R.id.password).text.toString()
            if (id.isNotEmpty() && password.isNotEmpty()) {
                val authToken = id + password
                val data = Bundle().apply {
                    putString(AccountManager.KEY_ACCOUNT_NAME, id)
                    putString(AccountManager.KEY_ACCOUNT_TYPE, accountType)
                    putString(DayaAccountConstants.KEY_AUTH_TOKEN_TYPE, DayaAccountConstants.AUTH_TOKEN_TYPE_SIMPLE)
                    putString(AccountManager.KEY_AUTHTOKEN, authToken)
                }
                val result = Intent()
                result.putExtras(data)
                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }
}
