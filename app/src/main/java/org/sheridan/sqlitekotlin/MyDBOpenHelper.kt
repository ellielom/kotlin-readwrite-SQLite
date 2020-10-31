package org.sheridan.sqlitekotlin

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class MyDBOpenHelper(
    context: Context?,
    //name: String?,
    factory: SQLiteDatabase.CursorFactory?
    //version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private val version = 1
        private val name = "MyName.db"
        val TABLE_NAME = "products"
        val COLUMN_ID = "_id"
        val COLUMN_NAME1 = "prodName"
        val COLUMN_NAME2 = "quantity"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_PRODUCTS_TABLE = "CREATE TABLE ${TABLE_NAME} (${COLUMN_ID} INTEGER PRIMARY KEY, ${COLUMN_NAME1} TEXT, ${COLUMN_NAME2} TEXT)"

        db?.execSQL(CREATE_PRODUCTS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${TABLE_NAME}")
        onCreate(db)
    }

    fun addProducts(prod : Product) {
        val values = ContentValues()
        values.put(COLUMN_NAME1, prod.prodName)
        values.put(COLUMN_NAME2, prod.quantity)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllProducts() : Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ${TABLE_NAME}", null)
    }

    fun searchProducts(prodName : String) : Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM ${TABLE_NAME} WHERE prodName = '${prodName}'", null)
    }

    fun deleteProducts(prodName : String) : Boolean {
        var result = false

        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${TABLE_NAME} WHERE prodName = '${prodName}'", null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }



}