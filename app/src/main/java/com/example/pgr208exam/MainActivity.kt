package com.example.pgr208exam


import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.AndroidNetworking
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private var dbHelper = FeedReaderDbHelper(this)


    override fun onCreate(savedInstanceState: Bundle?) {

        //deleteUnused()
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

        //fast networking
        AndroidNetworking.initialize(applicationContext);

    }

    fun saveUpload() {

    }

    fun deleteUnused() {
        dbHelper.writableDatabase.delete("originals", "id NOT IN(SELECT original FROM results)",null)
        dbHelper.writableDatabase.delete("results", "original NOT IN (SELECT id FROM originals)", null)
    }

    fun getImage(table: String): Cursor {
        //var bitmapArray = arrayListOf<Bitmap>()
        val cursor: Cursor
        if (table == "resultsNumber") {
            cursor = dbHelper.writableDatabase.rawQuery(
                "select original, count(*)" +
                        " from results group by original order by 2 desc", null
            )
        } else {
            cursor = dbHelper.writableDatabase.rawQuery("SELECT * FROM $table", null)
        }

        return cursor
    }

    fun bitArray(x: Bitmap): ByteArray {
        //Convert image to bitArray
        val stream = ByteArrayOutputStream()
        x.compress(Bitmap.CompressFormat.PNG, 100, stream)

        return stream.toByteArray();

    }
}