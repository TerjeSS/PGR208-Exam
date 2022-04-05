package com.example.pgr208exam

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    //private var dbHelper = FeedReaderDbHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        //onCreate for the main activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FragmentManager and NavController for switching between fragments
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationView)
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        //All the fragments
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.selectImageFragment,
                R.id.imageSearchFragment,
                R.id.savedResultsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        /*val imagesArray = intArrayOf(R.drawable.one, R.drawable.two, R.drawable.three)

        for (item in imagesArray) {
            dbHelper.writableDatabase.insert("images", null, ContentValues().apply {
                put("image", bitArray(item))
            })
        }*/

    }

    /*fun bitArray(x: Int): ByteArray {
        //Dummy data in SQLite
        val bitmap = (x as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()

        return bitmapData;
    }*/
}