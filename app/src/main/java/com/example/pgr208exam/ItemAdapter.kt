package com.example.pgr208exam

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pgr208exam.Fragments.ImageSearchFragment

class ItemAdapter(
    val dummyData: List<String>,
    val context: Context
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById<ImageView>(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.inside_recycler, null)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl: String = dummyData[position]
        var imageView = holder.imageView
        Glide.with(context).asBitmap()
            .load(imageUrl).into(imageView)
        imageView.setOnClickListener {
            run {
                val intent = Intent(context, FullScreenImage().javaClass)
                intent.putExtra("fullImageUrl", imageUrl)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return dummyData.size;
    }
}