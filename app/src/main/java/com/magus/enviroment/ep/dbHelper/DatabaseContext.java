package com.magus.enviroment.ep.dbHelper;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by pau on 15/5/26.
 */
public class DatabaseContext extends ContextWrapper {

    public DatabaseContext(Context base) {
        super(base);
    }
    /**
     * @param name
     * @return
     */
    @Override
    public File getDatabasePath(String name) {
        boolean isSdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        try {
            // 获得db文件的绝对路径
            String databaseFilename = isSdCardExist ? (DatabaseHelper.PATH + DatabaseHelper.DATABASE_NAME)
                    : this.getFileStreamPath(DatabaseHelper.DATABASE_NAME).getAbsolutePath();
            File dir = new File(DatabaseHelper.PATH);
            // 如果目录不存在，创建这个目录
            if (!dir.exists()) {
                dir.mkdir();
            }
            return new File(databaseFilename);
        } catch (Exception e) {
            return super.getDatabasePath(name);
        }
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return super.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), mode, factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return super.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), mode, factory, errorHandler);
    }
}
