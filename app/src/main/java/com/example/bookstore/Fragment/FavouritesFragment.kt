package com.example.bookstore.Fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.bookstore.Adapter.FavoriteRecyclerAdapter
import com.example.bookstore.R
import com.example.bookstore.database.BookDatabase
import com.example.bookstore.database.BookEntity


class FavouritesFragment : Fragment() {

    lateinit var recyclerFav: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavoriteRecyclerAdapter
    var dbBookList = listOf<BookEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)

        recyclerFav = view.findViewById(R.id.recyclerFavourite)
        progressLayout = view.findViewById(R.id.favProgressLayout)
        progressBar = view.findViewById(R.id.FavprogressBar)

        dbBookList = RetrieveFavourites(activity as Context).execute().get()


        layoutManager = GridLayoutManager(activity as Context, 2)

        if (activity != null) {
            progressLayout.visibility = View.GONE
            recyclerAdapter = FavoriteRecyclerAdapter(activity as Context, dbBookList)
            recyclerFav.layoutManager = layoutManager
            recyclerFav.adapter = recyclerAdapter
        }





        return view
    }


    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "books-db").build()

            return db.bookDao().getAllBooks()
        }

    }
}