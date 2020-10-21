package com.example.bookstore.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookstore.Fragment.AboutFragment
import com.example.bookstore.Fragment.DashboardFragment
import com.example.bookstore.Fragment.FavouritesFragment
import com.example.bookstore.Fragment.ProfileFragment
import com.example.bookstore.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var toolbar:Toolbar
    lateinit var drawerlayout:DrawerLayout

    lateinit var navigationview : NavigationView

    var previousmenuitem:MenuItem?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar=findViewById(R.id.toolBar)
        drawerlayout=findViewById(R.id.drawerLayout)
        navigationview=findViewById(R.id.navigationview)
        setUpToolbar()

        openDashboard()

        val setActionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,drawerlayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerlayout.addDrawerListener(setActionBarDrawerToggle)
        setActionBarDrawerToggle.syncState()

        navigationview.setNavigationItemSelectedListener {

            if(previousmenuitem!=null)
            {
                previousmenuitem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousmenuitem=it
            when(it.itemId){
                R.id.dashboard -> {
                    supportActionBar?.title="Dashboard"

                   openDashboard()

                    drawerlayout.closeDrawers()
                    Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show()
                }
                R.id.favourites -> {
                    supportActionBar?.title="Favourites"

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavouritesFragment())
                        .commit()
                    drawerlayout.closeDrawers()

                    Toast.makeText(this, "Favourites", Toast.LENGTH_SHORT).show()
                }
                R.id.profile -> {
                    supportActionBar?.title="Profile"

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .commit()
                    drawerlayout.closeDrawers()

                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
                R.id.about -> {
                    supportActionBar?.title="About"

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment())
                        .commit()
                    drawerlayout.closeDrawers()

                    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                }


            }
            return@setNavigationItemSelectedListener true
        }



    }
    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Store"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId

        if(id==android.R.id.home)
        {
            drawerlayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
    fun openDashboard(){
        val fragment= DashboardFragment()
        val transcation=supportFragmentManager.beginTransaction()
        transcation.replace(R.id.frameLayout,fragment)
        transcation.commit()

        supportActionBar?.title ="Dashboard"
        navigationview.setCheckedItem(R.id.dashboard)

    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag)
        {
            !is DashboardFragment ->openDashboard()

            else->super.onBackPressed()
        }
    }
}