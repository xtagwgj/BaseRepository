package com.xtagwgj.basedemo.db;

import android.annotation.SuppressLint;
import android.content.Context;

import com.blankj.utilcode.util.ResourceUtils;
import com.xtagwgj.basedemo.db.bean.Directory;
import com.xtagwgj.basedemo.db.bean.Joke;
import com.xtagwgj.basedemo.db.bean.JokeDetail;
import com.xtagwgj.basedemo.db.dao.DirectoryDao;
import com.xtagwgj.basedemo.db.dao.JokeDao;
import com.xtagwgj.basedemo.db.dao.JokeDetailDao;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {
        Joke.class,
        Directory.class,
        JokeDetail.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "UserDatabase.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            copyLocalDb(context);
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room
                .databaseBuilder(context, AppDatabase.class, DB_NAME)
                //迁移数据库使用
//                .addMigrations(MIGRATION_1_2)
                //迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }


    public abstract JokeDao jokeDao();

    public abstract DirectoryDao directoryDao();

    public abstract JokeDetailDao jokeDetailDao();

    /**
     * 版本1到2的升级
     */
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @SuppressLint("CheckResult")
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };


    private static void copyLocalDb(Context context) {
        File file = new File(context.getFilesDir().getParentFile(), "databases/" + DB_NAME);
        if (!file.exists()) {
            ResourceUtils.copyFileFromAssets("speech.sqlite3", file.getPath());
        }
    }
}