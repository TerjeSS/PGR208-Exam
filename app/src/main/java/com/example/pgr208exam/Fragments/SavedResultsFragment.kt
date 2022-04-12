package com.example.pgr208exam.Fragments

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import androidx.core.graphics.scaleMatrix
import androidx.core.view.get
import com.example.pgr208exam.MainActivity
import com.example.pgr208exam.R

class SavedResultsFragment : Fragment() {

    data class OriginalImage(val id: Int, val image: Bitmap, val date: String)
    data class ResultsImage(val id: Int, val image: Bitmap, val date: String, val original: Int)

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
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sorting_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val sortingArray = resources.getStringArray(R.array.sorting_array)
        val orderCursor = ((activity as MainActivity).getImage("results", "orderThem"))
        val orderArray: MutableList<Int> = arrayListOf()

        while (orderCursor.moveToNext()) {
            val original = orderCursor.getInt(orderCursor.getColumnIndexOrThrow("original"))
            orderArray.add(original)
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val originalsArray: MutableList<OriginalImage> = arrayListOf()
                val resultsArray: MutableList<ResultsImage> = arrayListOf()

                linearLayout.removeAllViews();

                val originalsCursor =
                    ((activity as MainActivity).getImage("originals", sortingArray[pos]))
                val resultsCursor =
                    ((activity as MainActivity).getImage("results", sortingArray[pos]))

                while (originalsCursor.moveToNext()) {
                    val imageId = originalsCursor.getInt(0)
                    val image: ByteArray =
                        originalsCursor.getBlob(originalsCursor.getColumnIndexOrThrow("image"))
                    val date = originalsCursor.getString(2)
                    val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    if (sortingArray[pos] == "Collection size") {
                        if (originalsArray.isEmpty()) {
                            for (item in orderArray) {
                                originalsArray += OriginalImage(-1, image2, "-1")
                            }
                        }

                        if (orderArray.contains(imageId)) {
                            originalsArray[orderArray.indexOf(imageId)] =
                                OriginalImage(imageId, image2, date)
                        } else {
                            originalsArray += (OriginalImage(imageId, image2, date))
                        }
                    } else {
                        originalsArray += (OriginalImage(imageId, image2, date))
                    }

                }

                while (resultsCursor.moveToNext()) {
                    val imageId = resultsCursor.getInt(0)
                    val image: ByteArray =
                        resultsCursor.getBlob(resultsCursor.getColumnIndexOrThrow("image"))
                    val date = resultsCursor.getString(2)
                    val original = resultsCursor.getInt(3)
                    val image2: Bitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                    resultsArray += ResultsImage(imageId, image2, date, original)
                }


                for (item in originalsArray) {
                    val newLayout: LinearLayout = LinearLayout(context)
                    val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        500,
                    )
                    newLayout.orientation = LinearLayout.HORIZONTAL
                    newLayout.layoutParams = params
                    linearLayout.addView(newLayout)

                    val imageView: ImageView = ImageView(context)
                    val imageParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        200,
                        500
                    )
                    imageView.layoutParams = imageParams

                    imageView.setOnClickListener {

                    }

                    imageView.setImageBitmap(item.image)
                    newLayout.addView(imageView)

                    val horizontalScrollLayout: HorizontalScrollView = HorizontalScrollView(context)
                    val horizontalParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    horizontalScrollLayout.layoutParams = horizontalParams
                    newLayout.addView(horizontalScrollLayout)

                    val newLinearLayout = LinearLayout(context)
                    newLinearLayout.layoutParams = params
                    horizontalScrollLayout.addView(newLinearLayout)

                    for (newItem in resultsArray) {
                        if (newItem.original == item.id) {
                            val newImageView: ImageView = ImageView(context)
                            val newImageParams: LinearLayout.LayoutParams =
                                LinearLayout.LayoutParams(
                                    200,
                                    500
                                )
                            newImageView.layoutParams = newImageParams

                            newImageView.setImageBitmap(newItem.image)
                            newLinearLayout.addView(newImageView)
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

        return view
    }

}

