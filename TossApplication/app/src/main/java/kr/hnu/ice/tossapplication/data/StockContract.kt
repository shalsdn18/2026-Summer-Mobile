package kr.hnu.ice.tossapplication.data

import android.provider.BaseColumns

object StockContract {
    object StockEntry : BaseColumns {
        const val TABLE_NAME = "holding_stocks"
        const val COLUMN_STOCK_CODE = "stock_code"
        const val COLUMN_STOCK_NAME = "stock_name"
        const val COLUMN_PURCHASE_PRICE = "purchase_price"
        const val COLUMN_CURRENT_PRICE = "current_price"
        const val COLUMN_QUANTITY = "quantity"
    }
}