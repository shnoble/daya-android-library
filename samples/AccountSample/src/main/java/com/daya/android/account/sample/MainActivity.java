package com.daya.android.account.sample;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int CHOOSE_ACCOUNT_REQUEST_CODE = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String permission = Manifest.permission.GET_ACCOUNTS;
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, permission + " permission granted.");

            chooseAccountIntent();
        } else {
            Log.w(TAG, permission + " permission denied.");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Log.d(TAG, "shouldShowRequestPermissionRationale: true");
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
            } else {
                Log.d(TAG, "shouldShowRequestPermissionRationale: false");
                ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
            }
        }


    }

    private void chooseAccountIntent() {
        Intent intent = AccountManager.newChooseAccountIntent(
                null, null, new String[]{"com.google"}, null, null, null, null);
        startActivityForResult(intent, CHOOSE_ACCOUNT_REQUEST_CODE);
    }

    private void readAccount() {
        AccountManager accountManager = AccountManager.get(this);
        Account[] accounts = accountManager.getAccounts();

        for (Account account : accounts) {
            String accountName = account.name;
            String accountType = account.type;

            Log.d(TAG, "Account Name: " + accountName);
            Log.d(TAG, "Account Type: " + accountType);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Log.d(TAG, "Request Permissions Result:");

                boolean isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                String result = (isGranted) ? "PERMISSION_GRANTED" : "PERMISSION_DENIED";
                Log.d(TAG, "- " + permissions[0] + ": " + result);

                if (isGranted) {
                    chooseAccountIntent();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (data != null) {
                Bundle extras = data.getExtras();
                final String accountName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);
                final String accountType = extras.getString(AccountManager.KEY_ACCOUNT_TYPE);
                Log.d(TAG, "Account Name: " + accountName);
                Log.d(TAG, "Account Type: " + accountType);

                readAccount();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Get Auth Token for Google
                        AccountManager accountManager = AccountManager.get(MainActivity.this);
                        AccountManagerFuture<Bundle> accountManagerFuture = accountManager.getAuthToken(new Account(accountName, accountType), "ah", null, MainActivity.this, null, null);
                        Bundle authTokenBundle = null;
                        try {
                            authTokenBundle = accountManagerFuture.getResult();
                        } catch (OperationCanceledException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (AuthenticatorException e) {
                            e.printStackTrace();
                        }
                        if (authTokenBundle != null) {
                            String authToken = authTokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
                            Log.d(TAG, "Account Auth Token: " + authToken);
                        } else {
                            Log.d(TAG, "Account Auth Token is null");
                        }
                    }
                }).start();



            }
        }
    }
}
