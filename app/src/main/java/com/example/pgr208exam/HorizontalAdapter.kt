package com.example.pgr208exam

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pgr208exam.Fragments.SavedResultsFragment
import java.io.ByteArrayOutputStream


class HorizontalAdapter(val list: ArrayList<SavedResultsFragment.ResultsImage>, val context: Context) :
    RecyclerView.Adapter<HorizontalAdapter.MyView>() {
    class MyView(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        init {
            imageView = view
                .findViewById<ImageView>(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.scroll_recycler,
                parent,
                false
            )
        return MyView(itemView)
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        val listData = list[position].image


        //Loading Image into view
        Glide.with(context).load(listData).into(holder.imageView)
        holder.imageView.setOnClickListener {
            val bitmap: Bitmap = list[position].image
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val byteArray = stream.toByteArray()
            val intent = Intent(context, FullScreenImage().javaClass)
            intent.putExtra("bitmapImage", byteArray)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}