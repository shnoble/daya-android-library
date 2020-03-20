package com.daya.accounts.authenticator

import android.accounts.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.daya.android.common.DayaLog

class DayaAccountAuthenticator(
    private val context: Context?
) : AbstractAccountAuthenticator(context) {

    /**
     * Returns a Bundle that contains the Intent of the activity that can be used to edit the properties.
     * In order to indicate success the activity should call response.setResult() with a non-null Bundle.
     *
     * @param response Used to set the result for the request.
     * If the Constants.INTENT_KEY is set in the bundle then this response field is to be used
     * for sending future results if and when the Intent is started.
     * @param accountType The AccountType whose properties are to be edited.
     * @return a Bundle containing the result or the Intent to start to continue the request.
     * If this is null then the request is considered to still be active and the result should sent later using response.
     */
    override fun editProperties(
        response: AccountAuthenticatorResponse?,
        accountType: String?
    ): Bundle {
        DayaLog.d(TAG, """
            editProperties:
                response: $response,
                accountType: $accountType
        """.trimIndent())
        return Bundle()
    }

    /**
     * Adds an account of the specified accountType.
     *
     * @param response To send the result back to the AccountManager, will never be null.
     * @param accountType The type of account to add, will never be null.
     * @param authTokenType The type of auth token to retrieve after adding the account, may be null.
     * @param requiredFeatures A String array of authenticator-specific features that the added account must support, may be null.
     * @param options A Bundle of authenticator-specific options.
     * It always contains AccountManager#KEY_CALLER_PID and AccountManager#KEY_CALLER_UID fields which will let authenticator know the identity of the caller.
     * @return A Bundle result or null if the result is to be returned via the response. The result will contain either:
     *      AccountManager#KEY_INTENT, or
     *      AccountManager#KEY_ACCOUNT_NAME and AccountManager#KEY_ACCOUNT_TYPE of the account that was added, or
     *      AccountManager#KEY_ERROR_CODE and AccountManager#KEY_ERROR_MESSAGE to indicate an error
     * @throws NetworkErrorException If the authenticator could not honor the request due to a network error.
     */
    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        DayaLog.d(TAG, """
            addAccount:
                response: $response,
                accountType: $accountType,
                requiredFeatures: $requiredFeatures,
                options: $options
        """.trimIndent())
        DayaLog.d(TAG, "Options key sets: ${options?.keySet()}")
        val selectAccount = options?.getBoolean("SelectAccount", false);
        DayaLog.d(TAG, "Select account: $selectAccount")
        val intent = Intent(context, DayaAccountAuthenticatorActivity::class.java).apply {
            putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
            putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        }
        return Bundle().apply {
            putParcelable(AccountManager.KEY_INTENT, intent)
        }
    }

    /**
     * Checks that the user knows the credentials of an account.
     *
     * @param response To send the result back to the AccountManager, will never be null.
     * @param account The account whose credentials are to be checked, will never be null.
     * @param options A Bundle of authenticator-specific options, may be null.
     * @return A Bundle result or null if the result is to be returned via the response. The result will contain either:
     *      AccountManager#KEY_INTENT, or
     *      AccountManager#KEY_BOOLEAN_RESULT, true if the check succeeded, false otherwise
     *      AccountManager#KEY_ERROR_CODE and AccountManager#KEY_ERROR_MESSAGE to indicate an error.
     * @throws NetworkErrorException If the authenticator could not honor the request due to a network error.
     */
    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle {
        DayaLog.d(TAG, """
            confirmCredentials:
                response: $response,
                account: $account,
                options: $options
        """.trimIndent())
        return Bundle()
    }

    /**
     * Gets an authtoken for an account.
     * If not null, the resultant Bundle will contain different sets of keys depending on whether a token was successfully issued and,
     * if not, whether one could be issued via some Activity.
     *
     * @param response To send the result back to the AccountManager, will never be null.
     * @param account The account whose credentials are to be retrieved, will never be null.
     * @param authTokenType The type of auth token to retrieve, will never be null.
     * @param options A Bundle of authenticator-specific options.
     * It always contains AccountManager#KEY_CALLER_PID and AccountManager#KEY_CALLER_UID fields which will let authenticator know the identity of the caller.
     * @return A Bundle result or null if the result is to be returned via the response.
     * @throws NetworkErrorException If the authenticator could not honor the request due to a network error
     */
    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        DayaLog.d(TAG, """
            getAuthToken:
                response: $response,
                account: $account,
                authTokenType: $authTokenType,
                options: $options
        """.trimIndent())

        val accountManager = AccountManager.get(context)
        val authToken = accountManager.peekAuthToken(account, authTokenType)
        return Bundle().apply {
            account?.let {
                putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
                putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
                putString(AccountManager.KEY_AUTHTOKEN, authToken)
            }
        }
    }

    /**
     * Ask the authenticator for a localized label for the given authTokenType.
     *
     * @param authTokenType The authTokenType whose label is to be returned, will never be null.
     * @return The localized label of the auth token type, may be null if the type isn't known.
     */
    override fun getAuthTokenLabel(authTokenType: String?): String {
        DayaLog.d(TAG, """
            getAuthTokenLabel:
                authTokenType: $authTokenType
        """.trimIndent())
        return "authTokenLabel"
    }

    /**
     * Update the locally stored credentials for an account.
     *
     * @param response To send the result back to the AccountManager, will never be null.
     * @param account The account whose credentials are to be updated, will never be null.
     * @param authTokenType The type of auth token to retrieve after updating the credentials, may be null.
     * @param options A Bundle of authenticator-specific options, may be null.
     * @return A Bundle result or null if the result is to be returned via the response. The result will contain either:
     *      AccountManager#KEY_INTENT, or
     *      AccountManager#KEY_ACCOUNT_NAME and AccountManager#KEY_ACCOUNT_TYPE of the account whose credentials were updated, or
     *      AccountManager#KEY_ERROR_CODE and AccountManager#KEY_ERROR_MESSAGE to indicate an error.
     * @throws NetworkErrorException If the authenticator could not honor the request due to a network error.
     */
    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        DayaLog.d(TAG, """
            updateCredentials:
                response: $response,
                account: $account,
                authTokenType: $authTokenType,
                options: $options
        """.trimIndent())
        return Bundle()
    }

    /**
     * Checks if the account supports all the specified authenticator specific features.
     *
     * @param response To send the result back to the AccountManager, will never be null.
     * @param account The account to check, will never be null.
     * @param features An array of features to check, will never be null.
     * @return 	A Bundle result or null if the result is to be returned via the response. The result will contain either:
     *      AccountManager#KEY_INTENT, or
     *      AccountManager#KEY_BOOLEAN_RESULT, true if the account has all the features, false otherwise
     *      AccountManager#KEY_ERROR_CODE and AccountManager#KEY_ERROR_MESSAGE to indicate an error.
     * @throws NetworkErrorException If the authenticator could not honor the request due to a network error.
     */
    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ): Bundle {
        DayaLog.d(TAG, """
            updateCredentials:
                response: $response,
                account: $account,
                features: $features
        """.trimIndent())
        return Bundle()
    }

    companion object {
        private const val TAG = "DayaAccountAuthenticator"
    }
}