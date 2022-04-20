package com.example.pgr208exam

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ItemAdapter(
    private val imageList: List<String>,
    val context: Context
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.image_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.result_image, null)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl: String = imageList[position]
        val imageView: ImageView = holder.imageView
        Glide.with(context).asBitmap()
            .load(imageUrl).into(imageView)
        imageView.setOnClickListener {
            run {
                val intent = Intent(context, FullScreenImage().javaClass)
                intent.putExtra("fullImageUrl", imageUrl)
                intent.putExtra("originalImage", imageUrl)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return imageList.size;
    }
}