package com.daya.android.file.sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.annotation.WorkerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1000;

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mEditText.setText("Android Internal File IO Test");

        String externalStorageInfo = "==================================================\n"
                + "External Storage Information\n";

        String externalStorageState = Environment.getExternalStorageState();
        externalStorageInfo += "- External Storage State: " + externalStorageState + "\n";

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            String externalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
            externalStorageInfo += "- External Storage Directory: " + externalStorageDirectory + "\n";

            String rootDirectory = Environment.getRootDirectory().getAbsolutePath();
            externalStorageInfo += "- Root Directory: " + rootDirectory + "\n";

            String dataDirectory = Environment.getDataDirectory().getAbsolutePath();
            externalStorageInfo += "- Data Directory: " + dataDirectory + "\n";

            String DownloadCacheDirectory = Environment.getDownloadCacheDirectory().getAbsolutePath();
            externalStorageInfo += "- Download Cache Directory: " + DownloadCacheDirectory + "\n";
        }

        Log.d(TAG, externalStorageInfo);

        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, permission + " permission granted.");

            //chooseAccountIntent();
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                saveFile();
                break;

            case R.id.load_button:
                loadFile();
                break;

            case R.id.delete_button:
                deleteFile();
                break;

            case R.id.save_external_button:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                saveExternalFile();
                break;

            case R.id.load_external_button:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                loadExternalFile();
                break;
        }
    }

    private void saveFile() {
        Log.d(TAG, "Save File");
        try {
            FileHandler.writeFile(this, "test.txt", mEditText.getText().toString().getBytes());
        } catch (FileWriteException e) {
            e.printStackTrace();
        }
    }

    private void loadFile() {
        Log.d(TAG, "Load File");
        try {
            byte[] data = FileHandler.readFile(this, "test.txt");
            mEditText.setText(new String(data));
        } catch (FileReadException e) {
            e.printStackTrace();
            mEditText.setText("Not Found File");
        }
    }

    private void deleteFile() {
        Log.d(TAG, "Delete File");
        if (FileHandler.deleteFile(this, "test.txt")) {
            mEditText.setText("Deleted");
        } else {
            mEditText.setText("Delete filed");
        }
    }

    @RequiresPermission(
            allOf = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }
    )
    private void saveExternalFile() {
        String externalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        File directory = new File(externalStorageDirectory + "/dir");
        if (!directory.mkdir()) {
            Log.d(TAG, "Make directory failed: " + directory.getAbsolutePath());
        }

        File file = new File(externalStorageDirectory + "/dir/file.txt");

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            String str = "This file exists in SDCard";
            outputStream.write(str.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @RequiresPermission(
            allOf = {
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }
    )
    private void loadExternalFile() {
        String externalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(externalStorageDirectory + "/dir/file.txt");
            byte[] data = new byte[inputStream.available()];
            while (inputStream.read(data) != -1) {}

            Log.d(TAG, "Load Data: " + new String(data));
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static class FileHandler {
        private FileHandler() {}

        @WorkerThread
        private static void writeFile(@NonNull Context context,
                                      @NonNull String name,
                                      @NonNull byte[] buffer) throws FileWriteException {

            FileOutputStream outputStream = null;
            try {
                outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
                outputStream.write(buffer);

            } catch (IOException e) {
                throw new FileWriteException(e.getMessage(), e);

            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @WorkerThread
        private static byte[] readFile(@NonNull Context context,
                                       @NonNull String name) throws FileReadException {
            FileInputStream inputStream = null;
            byte[] data = null;
            try {
                inputStream = context.openFileInput(name);
                data = new byte[inputStream.available()];
                while (inputStream.read(data) != -1) {}

            } catch (IOException e) {
                throw new FileReadException(e.getMessage(), e);

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return data;
        }

        @WorkerThread
        private static boolean deleteFile(@NonNull Context context,
                                          @NonNull String name) {
            return context.deleteFile(name);
        }
    }

    private static class FileWriteException extends Exception {
        static final long serialVersionUID = 1;

        private FileWriteException(String message, Throwable cause) {
            super(message, cause);
        }

        private FileWriteException(Throwable cause) {
            super(cause);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    private static class FileReadException extends Exception {
        static final long serialVersionUID = 1;

        private FileReadException(String message, Throwable cause) {
            super(message, cause);
        }

        private FileReadException(Throwable cause) {
            super(cause);
        }

        @Override
        public String toString() {
            return getMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Log.d(TAG, "Request Permissions Result:");
                for (int i = 0; i < permissions.length; i++) {
                    String result = (grantResults[i] == PackageManager.PERMISSION_GRANTED) ? "PERMISSION_GRANTED" : "PERMISSION_DENIED";
                    Log.d(TAG, "- " + permissions[i] + ": " + result);
                }
                break;
        }
    }
}
