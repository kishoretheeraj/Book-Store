package com.example.bookstore.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookstore.R
import com.example.bookstore.Util.ConnectionManager
import com.example.bookstore.database.BookDatabase
import com.example.bookstore.database.BookEntity
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtDescription: TextView
    lateinit var imgBookImage: ImageView
    lateinit var BtnadddtoFav: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var bookId: String? = "100"
    lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookNameDes)
        txtBookAuthor = findViewById(R.id.txtBookAuthorDes)
        txtBookPrice = findViewById(R.id.txtBookPriceDes)
        txtBookRating = findViewById(R.id.txtBookRatingDes)
        txtDescription = findViewById(R.id.txtDes)
        imgBookImage = findViewById(R.id.imgBookDes)
        BtnadddtoFav = findViewById(R.id.Btnfav)



        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"

        progressBar = findViewById(R.id.progressBarDes)
        progressBar.visibility = View.VISIBLE

        progressLayout = findViewById(R.id.progressLayoutDes)
        progressLayout.visibility = View.VISIBLE

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")

        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {


            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val success = it.getBoolean("success")


                        if (success) {
                            val bookJsonObjectRequest = it.getJSONObject("book_data")

                            progressLayout.visibility = View.GONE

                            val bookImageUrl = bookJsonObjectRequest.getString("image")

                            Picasso.get().load(bookJsonObjectRequest.getString("image"))
                                .error(R.drawable.book).into(imgBookImage)
                            txtBookName.text = bookJsonObjectRequest.getString("name")
                            txtBookAuthor.text = bookJsonObjectRequest.getString("author")
                            txtBookPrice.text = bookJsonObjectRequest.getString("price")
                            txtBookRating.text = bookJsonObjectRequest.getString("rating")
                            txtDescription.text = bookJsonObjectRequest.getString("description")

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtDescription.text.toString(),
                                bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()

                            if (isFav) {
                                BtnadddtoFav.text = "Remove From favourites"
                                val favColor = ContextCompat.getColor(
                                    applicationContext,
                                    R.color.colorFavourite
                                )
                                BtnadddtoFav.setBackgroundColor(favColor)
                            } else {
                                BtnadddtoFav.text = "Add to favourites"
                                val nofavColor =
                                    ContextCompat.getColor(applicationContext, R.color.colorPrimary)
                                BtnadddtoFav.setBackgroundColor(nofavColor)
                            }




                            BtnadddtoFav.setOnClickListener {
                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                        .get()
                                ) {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()

                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book Added to Favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        BtnadddtoFav.text = "Remove From Favourites"
                                        val favColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorFavourite
                                        )
                                        BtnadddtoFav.setBackgroundColor(favColor)

                                    } else {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some Error Occurred",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()

                                    if (result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book Added to Favourites",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        BtnadddtoFav.text = "Add to Favourites"
                                        val nofavColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorPrimary
                                        )
                                        BtnadddtoFav.setBackgroundColor(nofavColor)
                                    }

                                }
                            }


                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "some error occurred !!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Unexpected error occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "31bd4939157933"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error in Connection")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val openSetting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(openSetting)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()

        }


    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {
                1 -> {
                    //check db if the book is favourite or not
                    val book: BookEntity? = db.bookDao().getBookbyId(bookEntity.book_id.toString())
                    db.close()
                    return book != null

                }
                2 -> {
                    //save the book into db as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }
                3 -> {
                    // remove the  favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

            return false
        }
    }
}