package com.daya.android.file.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mEditText.setText("Android File IO Test");
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
}
