package com.example.pgr208exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class FullScreenImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val fullImageUrl = intent.getStringExtra("fullImageUrl")
        val fullImageView : ImageView = findViewById(R.id.fullImageView)

        Glide.with(this).load(fullImageUrl).into(fullImageView)
    }
}