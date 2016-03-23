package com.android.incongress.cd.conference.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 本地国家码数据库
 * Created by Jacky on 2016/2/28.
 */
public class CountryDb {
    private static SQLiteDatabase database;
    public static final String DATABASE_FILENAME = "country_code.db";
    public static final String PACKAGE_NAME = "com.android.incongress.cd.conference";
    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME;

    public static SQLiteDatabase openDatabase(Context context) {
        try {
            String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists()) {
                dir.mkdir();
            }
            if (!(new File(databaseFilename)).exists()) {
                InputStream is = context.getResources().openRawResource(R.raw.country_code);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[8192];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openOrCreateDatabase(
                    databaseFilename, null);
            return database;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
