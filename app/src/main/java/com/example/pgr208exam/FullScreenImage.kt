package com.example.pgr208exam

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class FullScreenImage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)

        val fullImageUrl = intent.getStringExtra("fullImageUrl")
        val byteArrayImage = intent.getByteArrayExtra("bitmapImage")
        val fullImageView: ImageView = findViewById(R.id.fullImageView)
        val downloadBtn: Button = findViewById(R.id.btn_download)
        val saveBtn: Button = findViewById(R.id.btn_save)
        var outputStream: OutputStream;

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

        fun getDateTime(): String {
            val calendar: Calendar = Calendar.getInstance()
            val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            return dateFormat.format(calendar.time)
        }

        saveBtn.setOnClickListener {
            val bitmap = (fullImageView.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val result = stream.toByteArray();

            dbHelper.writableDatabase.insert("originals", null, ContentValues().apply {
                put("image", result)
                put("date", getDateTime())
            })

            dbHelper.writableDatabase.insert("results", null, ContentValues().apply {
                put("image", result)
                put("date", getDateTime())
                put("original", 12)
            })

        }




        if (byteArrayImage == null) {
            Glide.with(this).load(fullImageUrl).into(fullImageView)
        } else {
            val bitmapImage = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage!!.size)
            Glide.with(this).load(bitmapImage).into(fullImageView)
        }
    }
}
