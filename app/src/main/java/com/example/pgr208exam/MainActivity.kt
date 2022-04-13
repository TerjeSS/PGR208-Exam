package com.example.pgr208exam


import android.os.Bundle
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.AndroidNetworking
import com.example.pgr208exam.Fragments.ImageSearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private var dbHelper = FeedReaderDbHelper(this)
    var resultList: ArrayList<String> = ArrayList<String>();




    override fun onCreate(savedInstanceState: Bundle?) {

        //onCreate for the main activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         var getReqResult: ArrayList<String>? = null;

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

        //fast networking
        AndroidNetworking.initialize(applicationContext);

    }

    fun getImage(): ArrayList<Bitmap> {
        var bitmapArray = arrayListOf<Bitmap>()
        val cursor: Cursor = dbHelper.writableDatabase.query("newtable", arrayOf("id", "image"),
            null, null, null, null, null, null)

        while (cursor.moveToNext()) {
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