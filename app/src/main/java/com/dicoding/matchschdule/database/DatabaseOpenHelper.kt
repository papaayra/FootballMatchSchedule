package com.dicoding.matchschdule.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.dicoding.matchschdule.favorite.Favorite
import org.jetbrains.anko.db.*
import java.awt.font.TextAttribute

class DatabaseOpenHelper(ctx: Context): ManagedSQLiteOpenHelper(ctx, "FavoriteFootball.db", null ) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            Favorite.TABLE_FAVORITE, true,
            Favorite.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            Favorite.EVENT_ID to TEXT + UNIQUE,
            Favorite.EVENT_NAME to TEXT,
            Favorite.EVENT_DATE to TEXT,
            Favorite.HOME_TEAM_ID to TEXT + UNIQUE,
            Favorite.HOME_TEAM_NAME to TEXT,
            Favorite.HOME_TEAM_SCORE to TEXT,
            Favorite.AWAY_TEAM_ID to TEXT + UNIQUE,
            Favorite.AWAY_TEAM_NAME to TEXT,
            Favorite.AWAY_TEAM_SCORE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(Favorite.TABLE_FAVORITE, true)
    }

    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper{
            if (instance==null){
                instance = DatabaseOpenHelper(ctx.applicationContext)

            }
            return  instance as DatabaseOpenHelper
        }

    }
}
val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)