package com.example.pgr208exam

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Log.INFO
import android.util.Log.VERBOSE
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.logging.Level.INFO


class FullScreenImage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val fullImageUrl = intent.getStringExtra("fullImageUrl")
        val fullImageView : ImageView = findViewById(R.id.fullImageView)
        val downloadBtn : Button = findViewById(R.id.btn_download)
        val saveBtn : Button = findViewById(R.id.btn_save)
        var outputStream : OutputStream;

        var dbHelper = FeedReaderDbHelper(this)


        //Attaches listener to download image to internal storage.
        downloadBtn.setOnClickListener {
            val bitmap = (fullImageView.drawable as BitmapDrawable).bitmap
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

        saveBtn.setOnClickListener {
            val bitmap = (fullImageView.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val result = stream.toByteArray();

            dbHelper.writableDatabase.insert("newtable", null, ContentValues().apply {
                put("image", result)
            })

        }





        Glide.with(this).load(fullImageUrl).into(fullImageView)
    }
}
