package com.example.pgr208exam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.NonDisposableHandle.parent
import java.util.*

class ItemAdapter(dummyData: List<String>, imageClickListener: View.OnClickListener) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    val imageClickListener = imageClickListener;

    class ItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    val view : View = LayoutInflater.from(parent.context).inflate(R.layout.inside_recycler, null)
    return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.view.findViewById<ImageView>(R.id.iv_inside_rc).setOnClickListener(imageClickListener)
    }

    override fun getItemCount(): Int {
        return 25;
    }
}