package com.example.pgr208exam

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pgr208exam.Fragments.SavedResultsFragment
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FullScreenImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_image)
        val fullImageUrl = intent.getStringExtra("fullImageUrl")
        val byteArrayImage = intent.getByteArrayExtra("bitmapImage")
        val tableAndId = intent.getStringArrayListExtra("tableAndId")
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
                Toast.makeText(applicationContext, "Image downloaded to device", Toast.LENGTH_LONG).show()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(applicationContext, "error occurred", Toast.LENGTH_LONG).show()
            }

        }


        if (byteArrayImage == null) {
            saveBtn.setOnClickListener {
                val bitmap = (fullImageView.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val result = stream.toByteArray();

                val cursor: Cursor = dbHelper.writableDatabase.rawQuery("SELECT MAX(id) FROM originals", null)
                var originalId: Int = -1
                while (cursor.moveToNext()) {
                    originalId = cursor.getInt(0)
                }
                cursor.close()

                dbHelper.writableDatabase.insert("results", null, ContentValues().apply {
                    put("image", result)
                    put("original", originalId)
                    Toast.makeText(applicationContext, "Image saved to application database", Toast.LENGTH_LONG).show()
                })
                dbHelper.close()
            }

            Glide.with(this).load(fullImageUrl).into(fullImageView)
        } else {
            saveBtn.text = "DELETE IMAGE"
            saveBtn.setOnClickListener {
                if (tableAndId != null) {
                    dbHelper.writableDatabase.delete("${tableAndId.get(0)}", "id = ${tableAndId.get(1)}", null)
                    val frg: Fragment? = supportFragmentManager.findFragmentById(R.id.savedResultsFragment)
                    val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
                    if (frg != null) {
                        ft.detach(frg)
                    }
                    if (frg != null) {
                        ft.attach(frg)
                    }
                    ft.commit()
                    finish()
                }
            }

            val bitmapImage = BitmapFactory.decodeByteArray(byteArrayImage, 0, byteArrayImage!!.size)
            Glide.with(this).load(bitmapImage).into(fullImageView)
        }
    }
}
