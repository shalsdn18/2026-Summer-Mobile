package kr.hnu.ice.tossapplication.data

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase

data class StockModel(val code: String, val name: String, val purchasePrice: Double, val currentPrice: Double, val quantity: Int)

class StockRepository(private val dbHelper: AppDbHelper) {
    fun getStocks(): List<StockModel> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(StockContract.StockEntry.TABLE_NAME, null, null, null, null, null, null)
        val list = mutableListOf<StockModel>()
        with(cursor) {
            while (moveToNext()) {
                list.add(StockModel(
                    getString(getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_STOCK_CODE)),
                    getString(getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_STOCK_NAME)),
                    getDouble(getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_PURCHASE_PRICE)),
                    getDouble(getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_CURRENT_PRICE)),
                    getInt(getColumnIndexOrThrow(StockContract.StockEntry.COLUMN_QUANTITY))
                ))
            }
            close()
        }
        return list
    }

    fun insertStock(stock: StockModel) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues().apply {
            put(StockContract.StockEntry.COLUMN_STOCK_CODE, stock.code)
            put(StockContract.StockEntry.COLUMN_STOCK_NAME, stock.name)
            put(StockContract.StockEntry.COLUMN_PURCHASE_PRICE, stock.purchasePrice)
            put(StockContract.StockEntry.COLUMN_CURRENT_PRICE, stock.currentPrice)
            put(StockContract.StockEntry.COLUMN_QUANTITY, stock.quantity)
        }
        db.insertWithOnConflict(StockContract.StockEntry.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE)
    }
}