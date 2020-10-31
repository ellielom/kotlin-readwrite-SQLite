package org.sheridan.sqlitekotlin

import android.database.Cursor
import android.database.CursorIndexOutOfBoundsException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState != null) {
            this.txtName.setText(savedInstanceState.getString("prodName"))
            this.txtQuantity.setText(savedInstanceState.getString("prodQuantity"))

            this.txtProductName.setText(savedInstanceState.getString("prodListing"))
            this.txtProductQuantity.setText(savedInstanceState.getString("quantityListing"))
        }


        btnAdd.setOnClickListener {
            val dbHandler = MyDBOpenHelper(this, null)
            if (this.txtName.text.toString() == "") {
                Toast.makeText(this, "Must provide a product name", Toast.LENGTH_LONG).show()
            } else if (this.txtQuantity.text.toString() == "") {
                Toast.makeText(this, "Must provide product quantity", Toast.LENGTH_LONG).show()
            } else {

                val product = Product(txtName.text.toString(), txtQuantity.text.toString())
                dbHandler.addProducts(product)
                Toast.makeText(
                    this,
                    "${txtQuantity.text.toString()} ${txtName.text.toString()}(s) added to the database",
                    Toast.LENGTH_LONG
                ).show()

                txtName.setText("")
                txtQuantity.setText("")
            }
        }

        btnShow.setOnClickListener {
            txtProductName.setText("")
            txtProductQuantity.setText("")

            try {
                this.writeCursor("all")
            } catch (ex: Exception) {
                Toast.makeText(this, "No products in the database", Toast.LENGTH_LONG).show()
            }
        }


        btnSearch.setOnClickListener {
            txtProductName.setText("")
            txtProductQuantity.setText("")

            if (this.txtName.text.toString() == "") {
                Toast.makeText(this, "Must provide a product name", Toast.LENGTH_LONG).show()
            } else {
                try {
                    this.writeCursor("some")
                } catch (ex: CursorIndexOutOfBoundsException) {
                    Toast.makeText(this, "Product not found", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnDelete.setOnClickListener {
            txtProductName.setText("")
            txtProductQuantity.setText("")


            if (this.txtName.text.toString() == "") {
                Toast.makeText(this, "Must provide a product name", Toast.LENGTH_LONG).show()
            } else {
                try {
                    val dbHandler = MyDBOpenHelper(this, null)
                    dbHandler.deleteProducts(this.txtName.text.toString())
                    Toast.makeText(this, "Product deleted", Toast.LENGTH_LONG).show()

                } catch (ex: CursorIndexOutOfBoundsException) {
                    Toast.makeText(this, "Product not found", Toast.LENGTH_LONG).show()
                }
            }

        }

    }






    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("prodName", this.txtName.text.toString())
        savedInstanceState.putString("prodQuantity", this.txtQuantity.text.toString())

        savedInstanceState.putString("prodListing", this.txtProductName.text.toString())
        savedInstanceState.putString("quantityListing", this.txtProductQuantity.text.toString())
    }






    fun writeCursor(type: String) {
        val dbHandler = MyDBOpenHelper(this, null)

        var cursor = dbHandler.getAllProducts()
        if (type.equals("some")) {
            cursor = dbHandler.searchProducts(this.txtName.text.toString())
        }

        cursor!!.moveToFirst()

        txtProductName.append(
            (cursor.getString(cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME1)))
        )
        txtProductName.append("\n")

        txtProductQuantity.append(
            (cursor.getString(cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME2)))
        )
        txtProductQuantity.append("\n")



        while (cursor.moveToNext()) {
            txtProductName.append(
                (cursor.getString(cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME1)))
            )
            txtProductName.append("\n")

            txtProductQuantity.append(
                (cursor.getString(cursor.getColumnIndex(MyDBOpenHelper.COLUMN_NAME2)))
            )
            txtProductQuantity.append("\n")
        }

        cursor.close()
    }


}
