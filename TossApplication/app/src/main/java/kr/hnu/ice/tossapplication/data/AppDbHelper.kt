package kr.hnu.ice.tossapplication.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class AppDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE ${StockContract.StockEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${StockContract.StockEntry.COLUMN_STOCK_CODE} TEXT UNIQUE NOT NULL, " +
                "${StockContract.StockEntry.COLUMN_STOCK_NAME} TEXT NOT NULL, " +
                "${StockContract.StockEntry.COLUMN_PURCHASE_PRICE} REAL NOT NULL, " +
                "${StockContract.StockEntry.COLUMN_CURRENT_PRICE} REAL NOT NULL, " +
                "${StockContract.StockEntry.COLUMN_QUANTITY} INTEGER NOT NULL)"
        db.execSQL(sql)
    }
    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${StockContract.StockEntry.TABLE_NAME}")
        onCreate(db)
    }
    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "TossStock.db"
    }
}