package com.example.pgr208exam.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
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
        var view = inflater.inflate(R.layout.fragment_saved_results, container, false)

        linearLayout = view.findViewById(R.id.linearLayout)

        var firstArray = arrayOf(R.drawable.one, R.drawable.two)
        var secondArray = arrayOf(R.drawable.three, R.drawable.two, R.drawable.one)
        var bothArrays = arrayOf(firstArray, secondArray)

        for (item in bothArrays) {
            val horizontalScroll: LinearLayout = LinearLayout(context)
            var params: LayoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                300
            )
            horizontalScroll.layoutParams = params
            linearLayout.addView(horizontalScroll)


            for (item in item) {
                val imageView: ImageView = ImageView(context)

                val imageParams: LayoutParams = LayoutParams(
                    100,
                    250
                )
                imageView.layoutParams = imageParams
                imageView.setImageResource(item)
                horizontalScroll.addView(imageView)
            }
        }


        return view
    }


    }

