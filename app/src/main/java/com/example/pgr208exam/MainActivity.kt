package com.example.pgr208exam

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private var dbHelper = FeedReaderDbHelper(this)

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

    }

    fun dummyData(x: Int) {
        dbHelper.writableDatabase.insert("newtable", null, ContentValues().apply {
            put("image", bitArray(x))
        })
    }

    fun getImage(): ArrayList<Bitmap> {
        var bitmapArray = arrayListOf<Bitmap>()
        val cursor: Cursor = dbHelper.writableDatabase.query("newtable", arrayOf("id", "image"),
            null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val retrievedId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val retrievedImage = cursor.getBlob(cursor.getColumnIndexOrThrow("image"))
            val image2: Bitmap = BitmapFactory.decodeByteArray(retrievedImage, 0, retrievedImage.size)
            bitmapArray.add(image2)
        }
        return bitmapArray
    }

    fun bitArray(x: Int): ByteArray {
        //Convert image to bitArray
        val stream = ByteArrayOutputStream()
        val bitmap = BitmapFactory.decodeResource(resources, x)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val bitmapData = stream.toByteArray()

        return bitmapData;
    }
}