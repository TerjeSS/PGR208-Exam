package com.example.pgr208exam.Fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.example.pgr208exam.MainActivity
import com.example.pgr208exam.R

class SavedResultsFragment : Fragment() {

    lateinit var linearLayout: LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_results, container, false)

        linearLayout = view.findViewById(R.id.linearLayout)

        val newLayout: LinearLayout = LinearLayout(context)
        val params: LayoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            300
        )
        newLayout.layoutParams = params
        linearLayout.addView(newLayout)

        val imageView: ImageView = ImageView(context)
        val imageParams: LayoutParams = LayoutParams(
            100,
            250
        )
        imageView.layoutParams = imageParams



        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener {

            ((activity as MainActivity).databaseQuery())

        }




        return view
    }


    }

