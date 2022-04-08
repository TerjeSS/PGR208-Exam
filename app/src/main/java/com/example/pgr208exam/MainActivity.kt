package com.example.pgr208exam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.AndroidNetworking
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jacksonandroidnetworking.JacksonParserFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        //onCreate for the main activity
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //FragmentManager and NavController for switching between fragments
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationView)
        val navController = navHostFragment.navController

        bottomNavigationView.setupWithNavController(navController)

        //All the fragments
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.selectImageFragment, R.id.imageSearchFragment, R.id.savedResultsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        //fast networking
        AndroidNetworking.initialize(applicationContext);
        AndroidNetworking.setParserFactory(JacksonParserFactory())


    }
}