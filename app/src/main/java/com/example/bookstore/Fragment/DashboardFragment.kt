package com.example.bookstore.Fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookstore.Adapter.DashboardRecyclerAdapter
import com.example.bookstore.Model.Book
import com.example.bookstore.R
import com.example.bookstore.Util.ConnectionManager
import org.json.JSONException


class DashboardFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var ProgressLayout: RelativeLayout
    lateinit var PrograssBar: ProgressBar


    lateinit var recyclerAdapter: DashboardRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    val bookInfoList = arrayListOf<Book>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        PrograssBar = View.findViewById(R.id.progressBar)
        ProgressLayout = View.findViewById(R.id.progressLayout)
        recyclerView = View.findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(activity)
        ProgressLayout.isVisible = true


        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)

            val url = "http://13.235.250.119/v1/book/fetch_books"

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    // handle resposnes from api
                    try {
                        ProgressLayout.isVisible = false
                        val success = it.getBoolean("success")

                        if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image"),

                                    )
                                bookInfoList.add(bookObject)

                                recyclerAdapter =
                                    DashboardRecyclerAdapter(activity as Context, bookInfoList)

                                recyclerView.adapter = recyclerAdapter
                                recyclerView.layoutManager = layoutManager


                            }

                        } else {
                            Toast.makeText(
                                activity as Context,
                                "some error occurred!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some Unexpected error occurred!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }, Response.ErrorListener {
                    println("Error is $it")
                    Toast.makeText(
                        activity as Context,
                        "Volley Error Occurred!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    //handle error from api

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "31bd4939157933"
                        return headers
                    }

                }

            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error in Connection")
            dialog.setMessage("Internet Connection not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val openSetting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(openSetting)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }




        return View
    }


}