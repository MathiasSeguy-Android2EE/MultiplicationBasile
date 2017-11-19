package com.android2ee.basile.multiplication.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.android2ee.basile.multiplication.cross.model.Score;

/**
 * Created by Created by Mathias Seguy alias Android2ee on 16/11/2017.
 */
@Database(entities = {Score.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {
    public abstract ScoreDao getScoreDao();
}
