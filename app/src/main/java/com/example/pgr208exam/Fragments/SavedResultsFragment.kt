package com.example.pgr208exam.Fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

        val spinner: Spinner = view.findViewById(R.id.sortSpinner)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sorting_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        val bitmapArray = ((activity as MainActivity).getImage())

        for (item in bitmapArray) {
            val newLayout: LinearLayout = LinearLayout(context)
            val params: LayoutParams = LayoutParams(
                100,
                250

                )
            newLayout.layoutParams = params
            linearLayout.addView(newLayout)

            val imageView: ImageView = ImageView(context)
            val imageParams: LayoutParams = LayoutParams(
                100,
                250
            )
            imageView.layoutParams = imageParams

            imageView.setImageBitmap(item)
            newLayout.addView(imageView)
        }

        return view
    }

}

