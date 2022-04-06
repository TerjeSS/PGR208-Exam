package com.example.pgr208exam

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.IOException
import java.io.OutputStream


class FullScreenImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val fullImageUrl = intent.getStringExtra("fullImageUrl")
        val fullImageView : ImageView = findViewById(R.id.fullImageView)
        val downloadBtn : Button = findViewById(R.id.btn_download)
        var outputStream : OutputStream;

        downloadBtn.setOnClickListener {
            val bitmap = (fullImageView.getDrawable() as BitmapDrawable).bitmap
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, "Image title here")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            val uri: Uri? =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            var outstream: OutputStream;
            try {
                outstream = contentResolver.openOutputStream(uri!!)!!
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outstream)
                outstream.close()
                Toast.makeText(applicationContext, "Success", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "error occurred", Toast.LENGTH_LONG).show()
            }

        }


        Glide.with(this).load(fullImageUrl).into(fullImageView)
    }
}