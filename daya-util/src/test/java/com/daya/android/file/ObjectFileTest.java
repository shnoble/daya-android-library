package com.daya.android.file;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by shhong on 2017. 10. 27..
 */
public class ObjectFileTest {
    private static final String FILE_NAME = "test.dat";
    private static final int USER_DATA_COUNT = 3;

    @Before
    public void setUp() throws Exception {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            System.out.println("Delete file: " + file.delete());
        }
    }

    @Test
    public void testWriteAndCloseOnce() throws Exception {
        // Write file
        ObjectOutputFile outputFile = new ObjectOutputFile(FILE_NAME, true);
        UserData[] writeUserDatas = new UserData[USER_DATA_COUNT];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < USER_DATA_COUNT; i++) {
            builder.append("UserData");
            writeUserDatas[i] = new UserData(builder.toString() + i, i);
            outputFile.writeObject(writeUserDatas[i]);
        }
        outputFile.close();

        // Read file
        ObjectInputFile inputFile = new ObjectInputFile(FILE_NAME);
        UserData[] readUserDatas = new UserData[USER_DATA_COUNT];
        for (int i = 0; i < USER_DATA_COUNT; i++) {
            readUserDatas[i] = (UserData) inputFile.readObject();
        }
        inputFile.close();

        // Verify data
        for (int i = 0; i < USER_DATA_COUNT; i++) {
            assertEquals(writeUserDatas[i].getName(), readUserDatas[i].getName());
            assertEquals(writeUserDatas[i].getAge(), readUserDatas[i].getAge());
        }
    }

    @Test
    public void testWriteAndCloseMultiple() throws Exception {
        int MULTIPLE_COUNT = 5;
        // Write file
        UserData[] writeUserDatas = new UserData[USER_DATA_COUNT];
        for (int j = 0; j < MULTIPLE_COUNT; j++) {
            ObjectOutputFile outputFile = new ObjectOutputFile(FILE_NAME, true);
            for (int i = 0; i < USER_DATA_COUNT; i++) {
                writeUserDatas[i] = new UserData("UserData" + i, i);
                outputFile.writeObject(writeUserDatas[i]);
            }
            outputFile.close();
        }

        // Read file
        ObjectInputFile inputFile = new ObjectInputFile(FILE_NAME);
        UserData[] readUserDatas = new UserData[USER_DATA_COUNT];
        for (int j = 0; j < MULTIPLE_COUNT; j++) {
            for (int i = 0; i < USER_DATA_COUNT; i++) {
                readUserDatas[i] = (UserData) inputFile.readObject();
            }
            // Verify data
            for (int i = 0; i < USER_DATA_COUNT; i++) {
                assertEquals(writeUserDatas[i].getName(), readUserDatas[i].getName());
                assertEquals(writeUserDatas[i].getAge(), readUserDatas[i].getAge());
            }
        }
        inputFile.close();
    }
}